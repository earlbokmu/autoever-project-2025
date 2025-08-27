package com.project.autoever.service.impl;

import com.project.autoever.dto.UserSmsRequestDto;
import com.project.autoever.entity.User;
import com.project.autoever.redis.RedisRateLimiter;
import com.project.autoever.repository.UserRepository;
import com.project.autoever.service.UserSmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserSmsServiceImpl implements UserSmsService {

    private final UserRepository userRepository;
    private final RedisRateLimiter redisRateLimiter;
    private final WebClient kakaoClient;  // Bean 주입
    private final WebClient smsClient;    // Bean 주입

    private static final int KAKAO_LIMIT = 100;
    private static final int SMS_LIMIT = 500;
    private static final int WINDOW_SECONDS = 60; //1분 당(60초)


    public void sendMessagesByAgeGroup(int ageGroup, String message) {
        List<User> allUsers = userRepository.findAll();

        List<User> targetUsers = allUsers.stream()
                .filter(user -> {
                    int age = user.getAge();
                    return age / 10 == ageGroup / 10; // 예: 25세 → 2, 30세 → 3
                })
                .toList();

        for (User user : targetUsers) {
            //첫줄은 동일하게 시작
            String str = user.getName() + "님, 안녕하세요. 현대 오토에버입니다.\\n";
            message = str + message;
            //phone 포맷은 xxx-xxxx-xxxx
            sendMessage(user.getPhoneNumber(), message);
        }
    }

    public void sendMessage(String phone, String message) {
        try {
            // 카카오톡 Rate Limit 체크
            if (!redisRateLimiter.tryAcquire("rate:kakao", WINDOW_SECONDS, KAKAO_LIMIT)) {
                throw new RuntimeException("카카오톡 분당 호출 제한 초과");
            }

            kakaoClient.post()
                    .uri("/kakaotalk-messages")
                    .bodyValue(new UserSmsRequestDto(phone, message))
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            System.out.println("카카오톡 발송 성공: " + phone);

        } catch (Exception ex) {
            System.out.println("카카오톡 발송 실패 → SMS 시도");

            try {
                // SMS Rate Limit 체크
                if (!redisRateLimiter.tryAcquire("rate:sms", WINDOW_SECONDS, SMS_LIMIT)) {
                    throw new RuntimeException("SMS 분당 호출 제한 초과");
                }

                smsClient.post()
                        .uri(uriBuilder -> uriBuilder
                                .path("/sms")
                                .queryParam("phone", phone)
                                .build())
                        .bodyValue("message=" + message)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();

                System.out.println("SMS 발송 성공: " + phone);

            } catch (Exception smsEx) {
                System.out.println("SMS 발송 실패: " + smsEx.getMessage());
            }
        }
    }


}
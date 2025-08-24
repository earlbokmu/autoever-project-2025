package com.project.autoever.service.impl;

import com.project.autoever.dto.UserSmsRequestDto;
import com.project.autoever.redis.RedisRateLimiter;
import com.project.autoever.service.UserSmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Slf4j
@Service
public class UserSmsServiceImpl implements UserSmsService {

    private final WebClient kakaoClient;
    private final WebClient smsClient;
    private final RedisRateLimiter redisRateLimiter;

    private static final int KAKAO_LIMIT = 100;
    private static final int SMS_LIMIT = 500;
    private static final int WINDOW_SECONDS = 60;

    public UserSmsServiceImpl(WebClient.Builder builder, RedisRateLimiter redisRateLimiter) {
        this.redisRateLimiter = redisRateLimiter;

        this.kakaoClient = builder
                .baseUrl("http://localhost:8081")
                .defaultHeaders(headers -> {
                    headers.setBasicAuth("autoever", "1234");
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .build();

        this.smsClient = builder
                .baseUrl("http://localhost:8082")
                .defaultHeaders(headers -> {
                    headers.setBasicAuth("autoever", "5678");
                    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                })
                .build();
    }

    public void sendMessage(String phone, String message) {
        try {
            // 카카오톡 Rate Limit 체크
            if (!redisRateLimiter.tryAcquire("rate:kakao", KAKAO_LIMIT, WINDOW_SECONDS)) {
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
                if (!redisRateLimiter.tryAcquire("rate:sms", SMS_LIMIT, WINDOW_SECONDS)) {
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
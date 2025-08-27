package com.project.autoever.service.impl;

import com.project.autoever.constants.CommonMessage;
import com.project.autoever.dto.UserInfoResponseDto;
import com.project.autoever.dto.UserRequestDto;
import com.project.autoever.entity.User;
import com.project.autoever.exception.CommonException;
import com.project.autoever.repository.UserRepository;
import com.project.autoever.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

/**
 * 사용자 관련 비즈니스 로직을 구현한 서비스 클래스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void registerUser(UserRequestDto userRequestDto) {
        log.info("회원가입 시도: {}", userRequestDto.getAccount());

        // 사용자 계정 중복 체크
        if (userRepository.existsByAccount(userRequestDto.getAccount())) {
            log.warn(CommonMessage.DUPLICATE_ACCOUNT);
            throw new CommonException(CommonMessage.DUPLICATE_ACCOUNT);
        }

        // 주민등록번호 중복 체크
        if (userRepository.existsByResidentNumber(userRequestDto.getResidentNumber())) {
            log.warn(CommonMessage.DUPLICATE_RESIDENT_NUMBER);
            throw new CommonException(CommonMessage.DUPLICATE_RESIDENT_NUMBER);
        }

        // DTO를 엔티티로 변환 및 비밀번호 암호화
        User user = User.builder()
                .account(userRequestDto.getAccount())
                .password(passwordEncoder.encode(userRequestDto.getPassword()))
                .name(userRequestDto.getName())
                .residentNumber(userRequestDto.getResidentNumber())
                .age(calculateAgeFromResidentNumber(userRequestDto.getResidentNumber()))
                .phoneNumber(userRequestDto.getPhoneNumber())
                .address(userRequestDto.getAddress())
                .build();

        // 사용자 저장
        User savedUser = userRepository.save(user);
        log.info("회원가입 성공: {}", savedUser.getAccount());

    }

    @Override
    public UserInfoResponseDto getCurrentUserInfo(String account) {
        User user = userRepository.findByAccount(account)
                .orElseThrow(() -> new CommonException(CommonMessage.USER_NOT_FOUND));

        return UserInfoResponseDto.from(user);
    }


    /*
     * 주민번호로 연령대 구하기
     */
    public static int calculateAgeFromResidentNumber(String rrn) {
        String birthDate = rrn.substring(0, 6);
        char genderCode = rrn.charAt(7);

        int year = Integer.parseInt(birthDate.substring(0, 2));
        int month = Integer.parseInt(birthDate.substring(2, 4));
        int day = Integer.parseInt(birthDate.substring(4, 6));

        // 세기 판별
        if (genderCode == '1' || genderCode == '2') {
            year += 1900;
        } else if (genderCode == '3' || genderCode == '4') {
            year += 2000;
        }

        LocalDate birthday = LocalDate.of(year, month, day);
        return Period.between(birthday, LocalDate.now()).getYears();
    }
}

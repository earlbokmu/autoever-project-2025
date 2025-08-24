package com.project.autoever.service.impl;

import com.project.autoever.constants.ExceptionMessage;
import com.project.autoever.dto.UserRequestDto;
import com.project.autoever.entity.User;
import com.project.autoever.exception.CommonException;
import com.project.autoever.repository.UserRepository;
import com.project.autoever.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자 관련 비즈니스 로직을 구현한 서비스 클래스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * 사용자 등록
     */
    @Override
    @Transactional
    public void registerUser(UserRequestDto userRequestDto) {
        log.info("회원가입 시도: {}", userRequestDto.getAccount());

        // 사용자 계정 중복 체크
        if (userRepository.existsByAccount(userRequestDto.getAccount())) {
            log.warn("이미 존재하는 계정입니다: {}", userRequestDto.getAccount());
            throw new CommonException(ExceptionMessage.DUPLICATE_ACCOUNT);
        }

        // 주민등록번호 중복 체크
        if (userRepository.existsByResidentNumber(userRequestDto.getResidentNumber())) {
            log.warn("이미 등록된 주민등록번호입니다: {}", userRequestDto.getResidentNumber());
            throw new CommonException(ExceptionMessage.DUPLICATE_RESIDENT_NUMBER);
        }

        // DTO를 엔티티로 변환 및 비밀번호 암호화
        User user = User.builder()
                .account(userRequestDto.getAccount())
                .password(userRequestDto.getPassword())
                .name(userRequestDto.getName())
                .residentNumber(userRequestDto.getResidentNumber())
                .phoneNumber(userRequestDto.getPhoneNumber())
                .address(userRequestDto.getAddress())
                .build();

        // 사용자 저장
        User savedUser = userRepository.save(user);
        log.info("회원가입 성공: {}", savedUser.getAccount());

    }
}

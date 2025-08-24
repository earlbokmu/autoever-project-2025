package com.project.autoever.service;

import com.project.autoever.dto.UserInfoResponseDto;
import com.project.autoever.dto.UserLoginRequestDto;
import com.project.autoever.dto.UserRequestDto;

/**
 * 사용자 관련 비즈니스 로직을 처리하는 서비스 인터페이스
 */
public interface UserService {

    /**
     * 새로운 사용자를 등록합니다.
     *
     * @param userRequestDto 등록할 사용자 정보
     * @throws IllegalArgumentException 이미 존재하는 사용자명이나 주민등록번호인 경우
     */
    void registerUser(UserRequestDto userRequestDto);

    /**
     * 사용자 로그인 처리
     * @param loginRequest 로그인 요청 DTO
     */
    void login(UserLoginRequestDto loginRequest);

    /**
     * 현재 로그인한 사용자 정보 반환
     * @param account 사용자 계정
     * @return 사용자 상세정보 DTO
     */
    UserInfoResponseDto getCurrentUserInfo(String account);
}

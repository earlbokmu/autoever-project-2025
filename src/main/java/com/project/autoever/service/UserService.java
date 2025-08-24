package com.project.autoever.service;

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

}

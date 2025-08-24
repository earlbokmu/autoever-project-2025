package com.project.autoever.service;

import com.project.autoever.dto.UserUpdateRequestDto;
import com.project.autoever.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserAdminService {

    /**
     * 모든 사용자 목록을 조회합니다.
     *
     * @param pageable 페이징 정보
     * @return 사용자 목록
     */
    Page<User> getAllUsers(Pageable pageable);

    /**
     * 계정으로 사용자를 조회합니다.
     *
     * @param account 사용자 계정
     * @return 사용자 정보
     */
    User getUserByAccount(String account);

    /**
     * 사용자 정보를 수정합니다.
     *
     * @param account    사용자 계정
     * @param updateDto 수정할 사용자 정보
     */
    void updateUser(String account, UserUpdateRequestDto updateDto);

    /**
     * 사용자를 삭제합니다.
     *
     * @param account 사용자 계정
     */
    void deleteUser(String account);

    /**
     * 모든 사용자를 삭제합니다.
     */
    void deleteAllUsers();
}

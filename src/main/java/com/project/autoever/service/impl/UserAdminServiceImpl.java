package com.project.autoever.service.impl;

import com.project.autoever.constants.CommonMessage;
import com.project.autoever.dto.UserUpdateRequestDto;
import com.project.autoever.entity.User;
import com.project.autoever.exception.CommonException;
import com.project.autoever.repository.UserRepository;
import com.project.autoever.service.UserAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserAdminServiceImpl implements UserAdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional(readOnly = true)
    public Page<User> getAllUsers(Pageable pageable) {
        log.info("모든 사용자 조회 요청");
        return userRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public User getUserByAccount(String account) {
        log.info("사용자 조회 요청: {}", account);
        return userRepository.findByAccount(account)
                .orElseThrow(() -> new CommonException(CommonMessage.USER_NOT_FOUND));
    }

    @Override
    @Transactional
    public void updateUser(String account, UserUpdateRequestDto updateDto) {
        log.info("사용자 정보 수정 요청: {}", account);
        User user = userRepository.findByAccount(account)
                .orElseThrow(() -> new CommonException(CommonMessage.USER_NOT_FOUND));

        // 비밀번호와 주소만 업데이트
        user.setPassword(passwordEncoder.encode(updateDto.getPassword()));
        user.setAddress(updateDto.getAddress());

        userRepository.save(user);
        log.info("사용자 정보 수정 완료: {}", account);
    }


    @Override
    @Transactional
    public void deleteUser(String account) {
        log.info("사용자 삭제 요청: {}", account);
        if (!userRepository.existsByAccount(account)) {
            throw new CommonException(CommonMessage.USER_NOT_FOUND);
        }
        userRepository.deleteByAccount(account);
        log.info("사용자 삭제 완료: {}", account);
    }

    @Override
    @Transactional
    public void deleteAllUsers() {
        log.info("모든 사용자 삭제 요청");
        userRepository.deleteAllInBatch();
        log.info("모든 사용자 삭제 완료");
    }


}

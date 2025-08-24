package com.project.autoever.controller;

import com.project.autoever.dto.UserUpdateRequestDto;
import com.project.autoever.entity.User;
import com.project.autoever.service.UserAdminService;
import com.project.autoever.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserAdminService userAdminService;

    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(
            @PageableDefault(size = 100) Pageable pageable) {
        return ResponseEntity.ok(userAdminService.getAllUsers(pageable));
    }

    @GetMapping("/{account}")
    public ResponseEntity<User> getUserByAccount(@PathVariable String account) {
        return ResponseEntity.ok(userAdminService.getUserByAccount(account));
    }

    @PutMapping("/{account}")
    public ResponseEntity<Void> updateUser(
            @PathVariable String account,
            @Valid @RequestBody UserUpdateRequestDto updateDto) {
        userAdminService.updateUser(account, updateDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{account}")
    public ResponseEntity<Void> deleteUser(@PathVariable String account) {
        userAdminService.deleteUser(account);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllUsers() {
        userAdminService.deleteAllUsers();
        return ResponseEntity.noContent().build();
    }
}

package com.project.autoever.controller;

import com.project.autoever.dto.UserInfoResponseDto;
import com.project.autoever.dto.UserLoginRequestDto;
import com.project.autoever.dto.UserRequestDto;
import com.project.autoever.security.CustomUserDetails;
import com.project.autoever.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 API", description = "사용자 관련 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
        @RequestBody @Valid UserRequestDto userRequestDto, Errors errors) {
        try {
            // Validation 예외처리
            /*List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            if(!fieldErrors.isEmpty()) {
                for (FieldError fieldError : bindingResult.getFieldErrors()) {
                    return ResponseEntity.badRequest().body(fieldError.getDefaultMessage());
                }
            }*/

            userService.registerUser(userRequestDto);
            return ResponseEntity.ok("회원가입이 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "로그인", description = "사용자 로그인을 처리합니다.")
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Valid @RequestBody UserLoginRequestDto loginRequest,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("계정과 비밀번호를 모두 입력해주세요.");
        }

        try {
            // 인증 시도
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getAccount(),
                    loginRequest.getPassword()
                )
            );

            // 인증 성공 시 세션에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return ResponseEntity.ok("{\"message\": \"로그인 성공\"}");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("{\"message\": \"로그인 실패: 계정 또는 비밀번호가 올바르지 않습니다.\"}");
        }
    }

    @Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자의 정보를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponseDto> getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
            authentication.getPrincipal().equals("anonymousUser")) {
            throw new UsernameNotFoundException("로그인이 필요합니다.");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(userService.getCurrentUserInfo(userDetails.getUsername()));
    }
}

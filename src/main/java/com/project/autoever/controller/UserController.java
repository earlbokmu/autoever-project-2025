package com.project.autoever.controller;

import com.project.autoever.dto.UserRequestDto;
import com.project.autoever.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}

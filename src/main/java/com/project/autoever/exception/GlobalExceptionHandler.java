package com.project.autoever.exception;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
@Hidden
public class GlobalExceptionHandler {

    /**
     * DTO 검증 실패(@Valid) 시 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException e) {
        // 첫 번째 필드 오류 메시지만 반환
        String errorMessage = e.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity
                .badRequest()
                .body("입력값 오류: " + errorMessage);
    }

    /**
     * 잘못된 비즈니스 로직 등
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
                .badRequest()
                .body("잘못된 요청: " + e.getMessage());
    }

    /**
     * 그 외 서버 오류
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 오류: " + e.getMessage());
    }
}
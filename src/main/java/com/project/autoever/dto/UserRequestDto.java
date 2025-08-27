package com.project.autoever.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Schema(description = "사용자 회원가입 요청 DTO")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    @Schema(description = "사용자 계정", example = "user", required = true)
    @NotBlank(message = "사용자 계정은 필수 입력값입니다.")
    private String account;

    @Schema(description = "비밀번호", example = "password", required = true)
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;

    @Schema(description = "사용자 이름", example = "홍길동", required = true)
    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;

    @Schema(description = "주민등록번호 (하이픈 제외 13자리)", example = "990101-1234567", required = true)
    @NotBlank(message = "주민등록번호는 필수 입력값입니다.")
    @Size(min = 13, max = 14, message = "주민등록번호는 13자리여야 합니다.")
    private String residentNumber;

    @Schema(description = "휴대폰 번호 (하이픈 제외 11자리)", example = "010-1234-5678", required = true)
    @NotBlank(message = "핸드폰 번호는 필수 입력값입니다.")
    @Size(min = 11, max = 13, message = "핸드폰 번호는 11자리여야 합니다.")
    private String phoneNumber;

    @Schema(description = "주소", example = "서울시 강남구 테헤란로 123", required = true)
    @NotBlank(message = "주소는 필수 입력값입니다.")
    private String address;

}

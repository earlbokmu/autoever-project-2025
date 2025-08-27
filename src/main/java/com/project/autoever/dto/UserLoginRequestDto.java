// 로그인 요청 DTO
package com.project.autoever.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequestDto {

    @NotBlank(message = "계정을 입력해주세요.")
    private String account;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}
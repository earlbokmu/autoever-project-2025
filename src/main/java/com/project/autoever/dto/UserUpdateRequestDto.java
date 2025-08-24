package com.project.autoever.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestDto {
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;
    
    @NotBlank(message = "주소는 필수 입력값입니다.")
    private String address;
}

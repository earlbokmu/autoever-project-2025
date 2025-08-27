package com.project.autoever.dto;

import com.project.autoever.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class UserInfoResponseDto {
    private String account;
    private String name;
    private String residentNumber;
    private String phoneNumber;
    private String address;

    public static UserInfoResponseDto from(User user) {
        return UserInfoResponseDto.builder()
                .account(user.getAccount())
                .name(user.getName())
                .residentNumber(user.getResidentNumber())
                .phoneNumber(user.getPhoneNumber())
                .address(extractTopLevelAddress(user.getAddress()))
                .build();
    }


    private static String extractTopLevelAddress(String address) {
        if (address == null || address.isEmpty()) {
            return "";
        }
        // Extract the first administrative district (e.g., 서울특별시, 경기도, ...)
        String[] parts = address.split(" ");
        if (parts.length > 0) {
            return parts[0];
        }
        return address;
    }
}

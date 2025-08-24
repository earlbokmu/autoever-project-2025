package com.project.autoever.dto;

import com.project.autoever.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoResponseDto {
    private String account;
    private String name;
    private String residentNumber;
    private String phoneNumber;
    private String address;

    public UserInfoResponseDto(User user) {
        this.account = user.getAccount();
        this.name = maskName(user.getName());
        this.residentNumber = maskResidentNumber(user.getResidentNumber());
        this.phoneNumber = maskPhoneNumber(user.getPhoneNumber());
        this.address = extractTopLevelAddress(user.getAddress());
    }

    private String maskName(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }
        if (name.length() == 1) {
            return name;
        }
        if (name.length() == 2) {
            return name.charAt(0) + "*";
        }
        return name.charAt(0) + "*".repeat(name.length() - 2) + name.charAt(name.length() - 1);
    }

    private String maskResidentNumber(String residentNumber) {
        if (residentNumber == null || residentNumber.length() < 8) {
            return "";
        }
        return residentNumber.substring(0, 8) + "******";
    }

    private String maskPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() < 7) {
            return "";
        }
        // 01012345678 -> 010-****-5678
        String prefix = phoneNumber.substring(0, 3);
        String suffix = phoneNumber.substring(phoneNumber.length() - 4);
        return prefix + "-****-" + suffix;
    }

    private String extractTopLevelAddress(String address) {
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

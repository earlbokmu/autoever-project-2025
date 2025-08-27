package com.project.autoever.service;

public interface UserSmsService {
    public void sendMessage(String phone, String message);

    void sendMessagesByAgeGroup(int ageGroup, String message);
}

package com.project.autoever.controller;

import com.project.autoever.constants.CommonMessage;
import com.project.autoever.service.UserSmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sms")
@RequiredArgsConstructor
public class UserSmsController {
    private final UserSmsService userSmsService;

    @PostMapping
    public ResponseEntity<String> sendMessagesByAgeGroup(@RequestParam int ageGroup, @RequestParam String message) {
        try {
            userSmsService.sendMessagesByAgeGroup(ageGroup, message);
            return ResponseEntity.ok(CommonMessage.SEND_SMS_SUCCESS);
        } catch (Exception e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

}

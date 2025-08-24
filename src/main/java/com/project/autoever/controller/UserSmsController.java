package com.project.autoever.controller;

import com.project.autoever.service.UserSmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sms")
@RequiredArgsConstructor
public class UserSmsController {
    private final UserSmsService userSmsService;

    @PostMapping
    public String sendMessage(@RequestParam String phone, @RequestParam String message) {
        userSmsService.sendMessage(phone, message);
        return "메시지 전송 요청 완료";
    }

}

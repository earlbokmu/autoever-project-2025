package com.project.autoever;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.autoever.constants.CommonMessage;
import com.project.autoever.controller.UserController;
import com.project.autoever.dto.UserRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false) //security 필터 끔
class UserRegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void registerUser_success() throws Exception {
        // given
        UserRequestDto requestDto = UserRequestDto.builder()
                .account("testUser")
                .password("password123")
                .name("홍길동")
                .residentNumber("9901011234567")
                .phoneNumber("01012345678")
                .address("서울특별시 강남구 테헤란로")
                .build();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // when & then
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().string("회원가입이 완료되었습니다."));
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 중복 계정(계정명)")
    void registerUser_fail_duplicateAccount() throws Exception {
        // given
        UserRequestDto requestDto = UserRequestDto.builder()
                .account("testUser")
                .password("password123")
                .name("홍길동")
                .residentNumber("9901011221267")
                .phoneNumber("01012345678")
                .address("서울특별시 강남구 테헤란로")
                .build();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // when & then
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(CommonMessage.DUPLICATE_ACCOUNT));
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 중복 계정(주민번호)")
    void registerUser_fail_duplicateResidentNumber() throws Exception {
        // given
        UserRequestDto requestDto = UserRequestDto.builder()
                .account("test1")
                .password("password123")
                .name("홍길동")
                .residentNumber("9901011234567")
                .phoneNumber("01012345678")
                .address("서울특별시 강남구 테헤란로")
                .build();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // when & then
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(CommonMessage.DUPLICATE_RESIDENT_NUMBER));
    }
}

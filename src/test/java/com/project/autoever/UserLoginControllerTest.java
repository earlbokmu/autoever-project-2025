package com.project.autoever;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.autoever.constants.CommonMessage;
import com.project.autoever.dto.UserLoginRequestDto;
import com.project.autoever.dto.UserRequestDto;
import com.project.autoever.entity.User;
import com.project.autoever.security.CustomUserDetails;
import com.project.autoever.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserLoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserService userService;


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
                .andExpect(content().string(CommonMessage.RESIGSTER_SUCCESS));
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void login_fail_wrongPassword() throws Exception {
        // given
        UserLoginRequestDto loginRequest = new UserLoginRequestDto();
        loginRequest.setAccount("testUser");
        loginRequest.setPassword("wrongPassword");

        doThrow(new BadCredentialsException(CommonMessage.INVALID_CREDENTIALS))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(CommonMessage.INVALID_CREDENTIALS));
    }


    @Test
    @DisplayName("로그인 성공 테스트")
    void login_success() throws Exception {
        // given
        UserLoginRequestDto loginRequest = new UserLoginRequestDto();
        loginRequest.setAccount("testUser");
        loginRequest.setPassword("password123");

        User user = User.builder()
                .account("testUser")
                .password("password123")
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

        doReturn(authentication)
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        // when & then
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(CommonMessage.LOGIN_SUCCESS));
    }

}

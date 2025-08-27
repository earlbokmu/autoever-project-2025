package com.project.autoever;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.autoever.controller.UserAdminController;
import com.project.autoever.dto.UserUpdateRequestDto;
import com.project.autoever.entity.User;
import com.project.autoever.service.UserAdminService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserAdminController.class)
@AutoConfigureMockMvc(addFilters = false) // Security 필터 제거
class UserAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserAdminService userAdminService;

    private final String ADMIN_ACCOUNT = "admin";
    private final String ADMIN_PASSWORD = "1212";

    @Test
    @DisplayName("모든 사용자 조회")
    void getAllUsers_success() throws Exception {
        List<User> users = List.of(
                User.builder().account("user1").password("pass1").name("홍길동1").build(),
                User.builder().account("user2").password("pass2").name("홍길동2").build(),
                User.builder().account("user3").password("pass3").name("홍길동3").build(),
                User.builder().account("user4").password("pass4").name("홍길동4").build(),
                User.builder().account("user5").password("pass5").name("홍길동5").build(),
                User.builder().account("user6").password("pass6").name("홍길동6").build(),
                User.builder().account("user7").password("pass7").name("홍길동7").build(),
                User.builder().account("user8").password("pass8").name("홍길동8").build(),
                User.builder().account("user9").password("pass9").name("홍길동9").build(),
                User.builder().account("user10").password("pass10").name("홍길동10").build(),
                User.builder().account("user11").password("pass11").name("홍길동11").build(),
                User.builder().account("user12").password("pass12").name("홍길동12").build()
        );
        when(userAdminService.getAllUsers(any(Pageable.class)))
                .thenReturn(new PageImpl<>(users));

        mockMvc.perform(get("/api/admin/users")
                        .with(httpBasic(ADMIN_ACCOUNT, ADMIN_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].account").value("user1"))
                .andExpect(jsonPath("$.content[1].account").value("user2"))
                .andExpect(jsonPath("$.content[2].account").value("user3"))
                .andExpect(jsonPath("$.content[3].account").value("user4"))
                .andExpect(jsonPath("$.content[4].account").value("user5"))
                .andExpect(jsonPath("$.content[5].account").value("user6"))
                .andExpect(jsonPath("$.content[6].account").value("user7"))
                .andExpect(jsonPath("$.content[7].account").value("user8"))
                .andExpect(jsonPath("$.content[8].account").value("user9"))
                .andExpect(jsonPath("$.content[9].account").value("user10"))
                .andExpect(jsonPath("$.content[10].account").value("user11"))
                .andExpect(jsonPath("$.content[11].account").value("user12"));

    }

    @Test
    @DisplayName("계정으로 사용자 조회")
    void getUserByAccount_success() throws Exception {
        User user = User.builder().account("user1").password("pass1").name("홍길동1").build();
        when(userAdminService.getUserByAccount("user1")).thenReturn(user);

        mockMvc.perform(get("/api/admin/users/user1")
                        .with(httpBasic(ADMIN_ACCOUNT, ADMIN_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.account").value("user1"))
                .andExpect(jsonPath("$.name").value("홍길동1"));
    }

    @Test
    @DisplayName("사용자 정보 업데이트")
    void updateUser_success() throws Exception {
        UserUpdateRequestDto updateDto = new UserUpdateRequestDto();
        updateDto.setPassword("password");
        updateDto.setAddress("경기도 안양시");

        doNothing().when(userAdminService).updateUser(eq("user1"), any(UserUpdateRequestDto.class));

        mockMvc.perform(put("/api/admin/users/user1")
                        .with(httpBasic(ADMIN_ACCOUNT, ADMIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());

        verify(userAdminService, times(1)).updateUser(eq("user1"), any(UserUpdateRequestDto.class));
    }

    @Test
    @DisplayName("단일 사용자 삭제")
    void deleteUser_success() throws Exception {
        doNothing().when(userAdminService).deleteUser("user1");

        mockMvc.perform(delete("/api/admin/users/user1")
                        .with(httpBasic(ADMIN_ACCOUNT, ADMIN_PASSWORD)))
                .andExpect(status().isNoContent());

        verify(userAdminService, times(1)).deleteUser("user1");
    }

    @Test
    @DisplayName("모든 사용자 삭제")
    void deleteAllUsers_success() throws Exception {
        doNothing().when(userAdminService).deleteAllUsers();

        mockMvc.perform(delete("/api/admin/users")
                .with(httpBasic(ADMIN_ACCOUNT, ADMIN_PASSWORD)))
                .andExpect(status().isNoContent());

        verify(userAdminService, times(1)).deleteAllUsers();
    }
}
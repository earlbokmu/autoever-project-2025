package com.project.autoever.config;

import com.project.autoever.security.AdminAuthenticationProvider;
import com.project.autoever.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Slf4j
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AdminAuthenticationProvider adminAuthProvider;
    private final CustomUserDetailsService userDetailsService;

    // 비밀번호 인코더
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager: Dao + Admin Provider 등록
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(userDetailsService);
        daoProvider.setPasswordEncoder(passwordEncoder());

        return new org.springframework.security.authentication.ProviderManager(daoProvider, adminAuthProvider);
    }

    // Security Filter Chain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Swagger UI / H2 Console / 회원가입/로그인 API 예외
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/h2-console/**",
                                "/api/users/**",
                                "/api/sms/**"
                        ).permitAll()
                        // 권한 설정
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults()) // REST 테스트용
                .sessionManagement(session -> session
                        .maximumSessions(1) // 동시에 1 세션만 허용
                )
                // H2 콘솔 화면을 위해 frameOptions 비활성화
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));


        return http.build();
    }
}
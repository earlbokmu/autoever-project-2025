package com.project.autoever.config;

import org.springframework.context.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient kakaoClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://localhost:8081")
                .defaultHeaders(headers -> {
                    headers.setBasicAuth("autoever", "1234");
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .build();
    }

    @Bean
    public WebClient smsClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://localhost:8082")
                .defaultHeaders(headers -> {
                    headers.setBasicAuth("autoever", "5678");
                    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                })
                .build();
    }
}
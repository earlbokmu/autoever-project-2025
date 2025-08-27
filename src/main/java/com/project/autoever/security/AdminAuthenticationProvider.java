package com.project.autoever.security;

import com.project.autoever.constants.CommonMessage;
import io.lettuce.core.dynamic.annotation.CommandNaming;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminAuthenticationProvider implements AuthenticationProvider {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "1212";

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
            UserDetails adminUser = org.springframework.security.core.userdetails.User
                    .withUsername(username)
                    .password(password)
                    .roles("ADMIN")
                    .build();

            return new UsernamePasswordAuthenticationToken(
                    adminUser,
                    password,
                    adminUser.getAuthorities()
            );
        }
        throw new BadCredentialsException(CommonMessage.INVALID_CREDENTIALS);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
package com.example.BookingApp.security;

import com.example.BookingApp.dto.error.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorResponse error = ErrorResponse.builder()
                .code("UNAUTHORIZED")
                .level(ErrorResponse.LevelEnum.ERROR)
                .message("Не авторизован: предоставьте валидный токен")
                .build();

        byte[] body = new ObjectMapper().writeValueAsBytes(error);
        response.getOutputStream().write(body);
    }
}
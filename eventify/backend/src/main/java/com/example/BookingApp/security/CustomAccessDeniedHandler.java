package com.example.BookingApp.security;

import com.example.BookingApp.dto.error.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        ErrorResponse error = ErrorResponse.builder()
                .code("FORBIDDEN")
                .level(ErrorResponse.LevelEnum.ERROR)
                .message("Доступ запрещен: недостаточно прав")
                .build();

        byte[] body = new ObjectMapper().writeValueAsBytes(error);
        response.getOutputStream().write(body);
    }
}
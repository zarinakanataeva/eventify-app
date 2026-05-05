package com.example.BookingApp.controller;

import com.example.BookingApp.dto.NotificationDto;
import com.example.BookingApp.security.JwtRequestFilter;
import com.example.BookingApp.security.JwtUtils;
import com.example.BookingApp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockitoBean
    private JwtRequestFilter jwtRequestFilter;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() throws ServletException, IOException {
        doAnswer(invocation -> {
            HttpServletRequest request = invocation.getArgument(0);
            HttpServletResponse response = invocation.getArgument(1);
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(request, response);
            return null;
        }).when(jwtRequestFilter).doFilter(any(), any(), any());
    }

    @Test
    @WithMockUser(username = "user@test.com")
    void getNotificationSettings_ShouldReturnDto() throws Exception {
        NotificationDto dto = new NotificationDto();
        dto.setNotifyNewEvents(true);

        when(userService.getNotificationsSettings("user@test.com")).thenReturn(dto);

        mockMvc.perform(get("/user/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notifyNewEvents").value(true));
    }

    @Test
    @WithMockUser(username = "testUser")
    void updateNotificationSettings_ShouldReturnUpdatedDto() throws Exception {
        NotificationDto requestDto = new NotificationDto(true, false, 5);

        when(userService.updateNotificationsSettings(eq("testUser"), any(NotificationDto.class)))
                .thenReturn(requestDto);

        mockMvc.perform(put("/user/notifications")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notifyUpcoming").value(false))
                .andExpect(jsonPath("$.notifyBeforeHours").value(5));
    }

    @Test
    @WithMockUser(username = "user@test.com")
    void linkTelegram_ShouldReturnCode() throws Exception {
        String expected = "12345";
        when(userService.initiateTelegramLink("user@test.com")).thenReturn(expected);

        mockMvc.perform(post("/user/telegram/link")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

    @Test
    @WithMockUser(username = "user@test.com")
    void deleteNotifications_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/user/notifications")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
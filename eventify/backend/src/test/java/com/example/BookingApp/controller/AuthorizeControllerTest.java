package com.example.BookingApp.controller;

import com.example.BookingApp.dto.request.LoginRequest;
import com.example.BookingApp.dto.request.RegisterRequest;
import com.example.BookingApp.dto.response.AuthorizeResponse;
import com.example.BookingApp.security.JwtRequestFilter;
import com.example.BookingApp.security.JwtUtils;
import com.example.BookingApp.service.AuthorizeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuthorizeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthorizeControllerTest {

    @MockitoBean
    private JwtRequestFilter jwtRequestFilter;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Autowired
    private MockMvc mockMvc;


    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AuthorizeService authorizeService;

    @Test
    void register_ShouldReturnCreatedAndAuthorizeResponse() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("email@test.com");
        request.setPassword("password");

        AuthorizeResponse response = new AuthorizeResponse();
        response.setToken("test-jwt-token");
        response.setRole(AuthorizeResponse.Role.USER);

        when(authorizeService.register(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("test-jwt-token"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void login_ShouldReturnOkAndToken() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("email@test.com");
        request.setPassword("password");

        AuthorizeResponse response = new AuthorizeResponse();
        response.setToken("login-success-token");
        response.setRole(AuthorizeResponse.Role.USER);

        when(authorizeService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("login-success-token"))
                .andExpect(jsonPath("$.role").value("USER"));
    }
}
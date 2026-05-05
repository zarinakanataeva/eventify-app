package com.example.BookingApp.controller;

import com.example.BookingApp.dto.request.EventCreateRequest;
import com.example.BookingApp.dto.request.EventUpdateRequest;
import com.example.BookingApp.dto.response.BookingResponse;
import com.example.BookingApp.dto.response.EventResponse;
import com.example.BookingApp.security.JwtRequestFilter;
import com.example.BookingApp.security.JwtUtils;
import com.example.BookingApp.service.AdminService;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @MockitoBean
    private JwtRequestFilter jwtRequestFilter;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminService adminService;


    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

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
    @WithMockUser(authorities = "ADMIN")
    void getAllBookings_ShouldReturnPage() throws Exception {
        BookingResponse booking = new BookingResponse();
        booking.setId(1L);
        PageImpl<BookingResponse> page = new PageImpl<>(Collections.singletonList(booking));

        when(adminService.getAllBookings(any(), any(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/admin/bookings")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void confirmBooking_ShouldReturnNoContent() throws Exception {
        Long bookingId = 1L;
        doNothing().when(adminService).confirmBooking(bookingId);

        mockMvc.perform(put("/admin/bookings/{id}/confirm", bookingId)
                        .with(csrf()))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(authorities = "ADMIN")
    void confirmBooking_ShouldReturnOk() throws Exception {
        mockMvc.perform(put("/admin/bookings/1/confirm")
                        .with(csrf()))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateEvent_ShouldReturnUpdatedEvent() throws Exception {
        Long eventId = 1L;
        EventUpdateRequest request = new EventUpdateRequest();
        EventResponse response = new EventResponse();
        response.setId(eventId);

        when(adminService.updateEvent(eq(eventId), any(EventUpdateRequest.class))).thenReturn(response);

        mockMvc.perform(put("/admin/events/{id}", eventId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(eventId));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void createEvent_ShouldReturnCreated() throws Exception {
        EventCreateRequest request = new EventCreateRequest();
        request.setTitle("Admin Event");
        request.setDescription("Some description");
        request.setDateTime(LocalDateTime.now().plusDays(1));
        request.setTotalTickets(100);

        EventResponse response = new EventResponse();
        response.setId(1L);

        when(adminService.createEvent(any())).thenReturn(response);

        mockMvc.perform(post("/admin/events")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }



    @Test
    @WithMockUser(authorities = "USER")
    void deleteEvent_AsUser_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(delete("/admin/events/1"))
                .andExpect(status().isForbidden());
    }
}
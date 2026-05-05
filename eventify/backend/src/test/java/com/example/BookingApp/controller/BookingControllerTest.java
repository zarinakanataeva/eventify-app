package com.example.BookingApp.controller;

import com.example.BookingApp.dto.request.CreateBookingRequest;
import com.example.BookingApp.dto.request.UpdateBookingRequest;
import com.example.BookingApp.dto.response.BookingResponse;
import com.example.BookingApp.security.JwtRequestFilter;
import com.example.BookingApp.security.JwtUtils;
import com.example.BookingApp.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc(addFilters = false)
public class BookingControllerTest {

    @MockitoBean
    private JwtRequestFilter jwtRequestFilter;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookingService bookingService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @WithMockUser(username = "user@test.com")
    void createBooking_ShouldReturnCreated() throws Exception {
        CreateBookingRequest request = new CreateBookingRequest();
        request.setEventId(1L);
        request.setTicketCount(2);

        when(bookingService.createBooking(eq("user@test.com"), any())).thenReturn(new BookingResponse());

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "testuser")
    void updateBooking_ShouldReturnUpdatedResponse() throws Exception {
        Long bookingId = 1L;
        UpdateBookingRequest request = new UpdateBookingRequest();
        request.setTicketsCount(5);

        BookingResponse response = new BookingResponse();
        response.setId(bookingId);

        when(bookingService.updateBooking(eq("testuser"), eq(bookingId), any(UpdateBookingRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/bookings/{id}", bookingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingId));
    }

    @Test
    @WithMockUser(username = "testuser")
    void getBookingById_ShouldReturnBooking() throws Exception {
        Long bookingId = 1L;
        BookingResponse response = new BookingResponse();
        response.setId(bookingId);

        when(bookingService.getBookingById("testuser", bookingId)).thenReturn(response);

        mockMvc.perform(get("/bookings/{id}", bookingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingId));
    }

    @Test
    @WithMockUser(username = "user@test.com")
    void getMyBookings_ShouldReturnList() throws Exception {
        when(bookingService.getMyBookings("user@test.com")).thenReturn(List.of(new BookingResponse()));

        mockMvc.perform(get("/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser(username = "user@test.com")
    void cancelBooking_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/bookings/1"))
                .andExpect(status().isNoContent());
    }
}
package com.example.BookingApp.controller;

import com.example.BookingApp.dto.response.EventResponse;
import com.example.BookingApp.security.JwtRequestFilter;
import com.example.BookingApp.security.JwtUtils;
import com.example.BookingApp.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EventControllerTest {

    @MockitoBean
    private JwtRequestFilter jwtRequestFilter;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventService eventService;

    @Test
    void getAllEvents_ShouldReturnPage() throws Exception {
        EventResponse event = new EventResponse();
        event.setTitle("Концерт");

        when(eventService.getAllEvents(any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(event)));

        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Концерт"));
    }

    @Test
    void getEventById_ShouldReturnEvent() throws Exception {
        EventResponse event = new EventResponse();
        event.setId(1L);

        when(eventService.getEventById(1L)).thenReturn(event);

        mockMvc.perform(get("/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}
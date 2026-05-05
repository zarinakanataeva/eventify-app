package com.example.BookingApp.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventUpdateRequest {

    private String title;

    @Size(max = 255, message = "Название не может быть длиннее 255 символов")
    private String description;

    @FutureOrPresent(message = "Дата мероприятия не может быть в прошлом")
    private LocalDateTime dateTime;

    @Min(value = 1, message = "Количество мест должно быть не менее 1.")
    private Integer totalTickets;

    private String coverUrl;

}

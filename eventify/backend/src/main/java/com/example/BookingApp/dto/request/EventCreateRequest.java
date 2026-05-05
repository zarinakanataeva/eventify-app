package com.example.BookingApp.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventCreateRequest {

    @NotBlank(message = "Название мероприятия не может быть пустым.")
    private String title;

    @NotBlank(message = "Описание мероприятия не может быть пустым.")
    @Size(max = 255, message = "Название не может быть длиннее 255 символов")
    private String description;

    @NotNull(message = "Дата мероприятия не может быть пустым.")
    @FutureOrPresent(message = "Дата мероприятия не может быть в прошлом")
    private LocalDateTime dateTime;

    @NotNull(message = "Количество билетов на мероприятие не может быть пустым.")
    @Min(value = 1, message = "Количество мест должно быть не менее 1.")
    private Integer totalTickets;

    private String coverUrl;

}

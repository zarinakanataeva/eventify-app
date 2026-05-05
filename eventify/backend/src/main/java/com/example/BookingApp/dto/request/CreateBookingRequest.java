package com.example.BookingApp.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingRequest {

    @NotNull(message = "Номер мероприятия обязателен")
    private Long eventId;

    @NotNull(message = "Количество билетов обязательно")
    @Min(value = 1, message = "Минимальное количество билетов - 1")
    private int ticketCount;

}

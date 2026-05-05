package com.example.BookingApp.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdateBookingRequest {

    @Min(value = 1, message = "Минимальное количество билетов - 1")
    private int ticketsCount;

}

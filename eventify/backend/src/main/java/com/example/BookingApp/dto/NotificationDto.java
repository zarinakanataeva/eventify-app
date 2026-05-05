package com.example.BookingApp.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {

    private Boolean notifyNewEvents;
    private Boolean notifyUpcoming;

    @Min(value = 1, message = "Минимальное количество часов - 1")
    @Max(value = 24, message = "Максимально количество часов - 24")
    private Integer notifyBeforeHours;

}

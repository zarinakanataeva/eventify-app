package com.example.BookingApp.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {

    @NotNull
    private Long id;
    @NotNull
    private EventResponse event;
    private String customerEmail;
    private Integer ticketCount;
    private LocalDateTime createdAt;
    private LocalDateTime expiryTime;
    private Boolean confirmed;
    private String timezone;

}

package com.example.BookingApp.dto.error;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {

    private String code;
    private LevelEnum level;
    private String message;
    private List<ErrorDetail> details;

    public enum LevelEnum {
        INFO, WARNING, ERROR
    }
}
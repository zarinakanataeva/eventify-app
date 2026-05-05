package com.example.BookingApp.exception;

import com.example.BookingApp.dto.error.ErrorDetail;
import com.example.BookingApp.dto.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorDetail> details = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorDetail(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        return ErrorResponse.builder()
                .code("VALIDATION_FAILED")
                .level(ErrorResponse.LevelEnum.ERROR)
                .message("Некоторые поля заполнены неверно")
                .details(details)
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(NotFoundException ex) {
        return ErrorResponse.builder()
                .code("RESOURCE_NOT_FOUND")
                .level(ErrorResponse.LevelEnum.ERROR)
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflict(ConflictException ex) {
        return ErrorResponse.builder()
                .code("CONFLICT")
                .level(ErrorResponse.LevelEnum.ERROR)
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(BadRequestException ex) {
        return ErrorResponse.builder()
                .code("BAD_REQUEST")
                .level(ErrorResponse.LevelEnum.ERROR)
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAllExceptions(Exception ex) {
        log.error("Unexpected error: ", ex);
        return ErrorResponse.builder()
                .code("INTERNAL_SERVER_ERROR")
                .level(ErrorResponse.LevelEnum.ERROR)
                .message("Произошла внутренняя ошибка сервера")
                .build();
    }
}
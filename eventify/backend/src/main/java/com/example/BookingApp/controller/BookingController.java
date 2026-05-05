package com.example.BookingApp.controller;

import com.example.BookingApp.dto.error.ErrorResponse;
import com.example.BookingApp.dto.request.CreateBookingRequest;
import com.example.BookingApp.dto.request.UpdateBookingRequest;
import com.example.BookingApp.dto.response.BookingResponse;
import com.example.BookingApp.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @Operation(
            summary = "Создать бронирование",
            description = "Позволяет авторизованному пользователю забронировать билеты на мероприятие."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Бронирование успешно создано",
                    content = @Content(schema = @Schema(implementation = BookingResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка валидации или недостаточно билетов",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован (отсутствует или неверный токен)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещен",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Мероприятие с указанным ID не найдено",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse createBooking(@Valid @RequestBody CreateBookingRequest dto,
                                         @AuthenticationPrincipal UserDetails userDetails) throws BadRequestException {
        return bookingService.createBooking(userDetails.getUsername(), dto);
    }


    @Operation(
            summary = "Обновить бронирование",
            description = "Позволяет обновить количество билетов по бронированию"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Бронирование успешно обновлено",
                    content = @Content(schema = @Schema(implementation = BookingResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка валидации или недостаточно билетов",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещен",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Бронирование с указанным ID не найдено",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping({"/{id}"})
    public BookingResponse updateBooking(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBookingRequest dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws BadRequestException {
        return bookingService.updateBooking(userDetails.getUsername(), id, dto);
    }


    @Operation(
            summary = "Получить бронирование по ID",
            description = "Позволяет получить бронирование по ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Бронирование успешно получено",
                    content = @Content(schema = @Schema(implementation = BookingResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещен",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Бронирование с указанным ID не найдено",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/{id}")
    public BookingResponse getBookingById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return bookingService.getBookingById(userDetails.getUsername(), id);
    }


    @Operation(
            summary = "Получить свои бронирования",
            description = "Позволяет авторизованному пользователю получить список своих бронирований"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Бронирования успешно получены",
                    content = @Content(schema = @Schema(implementation = BookingResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping
    public List<BookingResponse> getMyBookings(
            @AuthenticationPrincipal UserDetails userDetails) {
        return bookingService.getMyBookings(userDetails.getUsername());
    }


    @Operation(
            summary = "Отменить бронирование",
            description = "Позволяет авторизованному пользователю отменить бронирование")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Бронирование успешно отменено",
                    content = @Content(schema = @Schema(implementation = BookingResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещен",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Мероприятие с указанным ID не найдено",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelBooking(@PathVariable Long id,
                              @AuthenticationPrincipal UserDetails userDetails) {
        bookingService.cancelBooking(userDetails.getUsername(), id);
    }
}

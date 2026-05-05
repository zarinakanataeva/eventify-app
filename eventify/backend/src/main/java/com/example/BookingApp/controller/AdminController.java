package com.example.BookingApp.controller;

import com.example.BookingApp.dto.error.ErrorResponse;
import com.example.BookingApp.dto.request.EventCreateRequest;
import com.example.BookingApp.dto.request.EventUpdateRequest;
import com.example.BookingApp.dto.response.BookingResponse;
import com.example.BookingApp.dto.response.EventResponse;
import com.example.BookingApp.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final AdminService adminService;


    @Operation(
            summary = "Получить все бронирования",
            description = "Позволяет администратору получить список всех существующих бронирований"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Страница с бронированиями",
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
            )
    })
    @GetMapping("/bookings")
    public Page<BookingResponse> getAllBookings(
            @RequestParam(required = false) Long eventId,
            @RequestParam(required = false) Boolean unconfirmed,
            Pageable pageable) {
        return adminService.getAllBookings(eventId, unconfirmed, pageable);
    }


    @Operation(
            summary = "Подтвердить бронирование",
            description = "Позволяет администратору подтвердить бронирование"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Бронирование подтверждено"
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
                    description = "Бронирование не найдено",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PutMapping("/bookings/{id}/confirm")
    public void confirmBooking(@PathVariable Long id) {
        adminService.confirmBooking(id);
    }


    @Operation(
            summary = "Создать мероприятие",
            description = "Позволяет администратору создать новое мероприятие."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Мероприятие успешно создано",
                    content = @Content(schema = @Schema(implementation = EventResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный запрос",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
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
                    responseCode = "409",
                    description = "Конфликт",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponse createEvent(@Valid @RequestBody EventCreateRequest dto) {
        return adminService.createEvent(dto);
    }


    @Operation(
            summary = "Обновить мероприятие",
            description = "Позволяет администратору обновить существующее мероприятие"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Мероприятие успешно обновлено",
                    content = @Content(schema = @Schema(implementation = EventResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный запрос",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
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
                    description = "Бронирование не найдено",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Конфликт",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PutMapping("/events/{id}")
    public EventResponse updateEvent(@PathVariable Long id,
                                     @Valid @RequestBody EventUpdateRequest dto) {
        return adminService.updateEvent(id, dto);
    }


    @Operation(
            summary = "Удалить мероприятие",
            description = "Позволяет администратору удалить существующее мероприятие"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Мероприятие успешно удалено"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный запрос",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
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
                    description = "Бронирование не найдено",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Конфликт",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @DeleteMapping("/events/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable Long id) {
        adminService.deleteEvent(id);
    }


    @Operation(
            summary = "Удалить бронирование",
            description = "Позволяет удалить существующее бронирование"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Бронирование успешно удалено"
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
                    description = "Бронирование не найдено",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @DeleteMapping("/bookings/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBooking(@PathVariable Long id) {
        adminService.deleteBooking(id);
    }
}

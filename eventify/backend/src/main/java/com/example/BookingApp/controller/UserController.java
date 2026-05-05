package com.example.BookingApp.controller;

import com.example.BookingApp.dto.NotificationDto;
import com.example.BookingApp.dto.error.ErrorResponse;
import com.example.BookingApp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Получить настройки уведомлений",
            description = "Позволяет пользователю получить свои настройки уведомлений."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Настройки уведомлений получены",
                    content = @Content(schema = @Schema(implementation = NotificationDto.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/notifications")
    public NotificationDto getNotificationSettings(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.getNotificationsSettings(userDetails.getUsername());
    }

    @Operation(
            summary = "Обновить настройки уведомлений",
            description = "Позволяет пользователю обновить настройки своих уведомлений."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Настройки успешно обновлены",
                    content = @Content(schema = @Schema(implementation = NotificationDto.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Настройки уведомлений не найдены",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PutMapping("/notifications")
    public NotificationDto updateNotificationSettings(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody NotificationDto dto) {
        return userService.updateNotificationsSettings(userDetails.getUsername(), dto);
    }


    @Operation(
            summary = "Отменить уведомления",
            description = "Позволяет пользователю удалить настройки уведомлений."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Настройки удалены"),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @DeleteMapping("/notifications")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNotifications(@AuthenticationPrincipal UserDetails userDetails) {
        userService.deleteNotifications(userDetails.getUsername());
    }

    @Operation(
            summary = "Инициировать привязку Telegram",
            description = "Позволяет привязать Telegram аккаунт (вернёт код для /start в боте)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Код для /start команды в боте",
                    content = @Content(schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/telegram/link")
    public String linkTelegram(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.initiateTelegramLink(userDetails.getUsername());
    }
}

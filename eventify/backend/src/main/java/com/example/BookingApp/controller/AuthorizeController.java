package com.example.BookingApp.controller;

import com.example.BookingApp.dto.error.ErrorResponse;
import com.example.BookingApp.dto.request.LoginRequest;
import com.example.BookingApp.dto.request.RegisterRequest;
import com.example.BookingApp.dto.response.AuthorizeResponse;
import com.example.BookingApp.service.AuthorizeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Регистрация и вход")
public class AuthorizeController {

    private final AuthorizeService authorizeService;


    @Operation(
            summary = "Регистрация пользователя",
            description = "Позволяет зарегистрировать нового пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Пользователь зарегистрирован",
                    content = @Content(schema = @Schema(implementation = AuthorizeResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Ошибка валидации логина или пароля",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorizeResponse register(@Valid @RequestBody RegisterRequest dto) throws BadRequestException {
        return authorizeService.register(dto);
    }


    @Operation(
            summary = "Вход в систему",
            description = "Позволяет зарегистрированному пользователю войти и получить токен доступа"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный вход",
                    content = @Content(schema = @Schema(implementation = AuthorizeResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Неверное имя пользователя или пароль",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/login")
    public AuthorizeResponse login(@Valid @RequestBody LoginRequest dto) throws BadRequestException {
        return authorizeService.login(dto);
    }
}
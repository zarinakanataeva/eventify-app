package com.example.BookingApp.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizeResponse {

    public enum Role {
        USER,
        ADMIN
    }

    @NotBlank(message = "Токен авторизации не должен быть пустым")
    private String token;

    @NotNull(message = "Роль пользователя обязательна")
    private Role role;

}

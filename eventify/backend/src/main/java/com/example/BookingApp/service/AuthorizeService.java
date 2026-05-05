package com.example.BookingApp.service;

import com.example.BookingApp.dto.request.LoginRequest;
import com.example.BookingApp.dto.request.RegisterRequest;
import com.example.BookingApp.entity.Notification;
import com.example.BookingApp.entity.Role;
import com.example.BookingApp.exception.NotFoundException;
import com.example.BookingApp.mapper.UserMapper;
import com.example.BookingApp.repository.NotificationRepository;
import com.example.BookingApp.repository.UserRepository;
import com.example.BookingApp.dto.response.AuthorizeResponse;
import com.example.BookingApp.entity.User;
import com.example.BookingApp.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorizeService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;


    @Transactional
    public AuthorizeResponse register(RegisterRequest dto) throws BadRequestException {

        log.info("Регистрация пользователя с email: {}", dto.getEmail());

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            log.warn("Пользователь с email {} уже существует", dto.getEmail());
            throw new BadRequestException("Email занят");
        }

        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.USER);
        User savedUser = userRepository.save(user);

        Notification defaultSettings = new Notification();
        defaultSettings.setUser(savedUser);
        defaultSettings.setNotifyUpcoming(false);
        defaultSettings.setNotifyNewEvents(false);
        defaultSettings.setHoursBeforeHours(1);
        notificationRepository.save(defaultSettings);
        log.info("Пользователь {} зарегистрирован с дефолтными настройками", savedUser.getEmail());

        String token = jwtUtils.generateToken(savedUser);
        AuthorizeResponse response = userMapper.toAuthResponse(savedUser);
        response.setToken(token);

        return response;
    }

    @Transactional
    public AuthorizeResponse login(LoginRequest dto) throws BadRequestException {
        log.info("Осуществляется вход пользователя {}", dto.getEmail());

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() ->
                {
                    log.warn("Пользователь {} не найден", dto.getEmail());
                    return new NotFoundException("Пользователь не найден");
                });

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            log.warn("Неверный пароль");
            throw new BadRequestException("Неверный пароль");
        }
        String token = jwtUtils.generateToken(user);
        log.info("Пользователь {} успешно авторизован", user.getEmail());

        AuthorizeResponse response = userMapper.toAuthResponse(user);
        response.setToken(token);
        return response;

    }
}


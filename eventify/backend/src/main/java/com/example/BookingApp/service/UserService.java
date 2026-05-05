package com.example.BookingApp.service;

import com.example.BookingApp.dto.NotificationDto;
import com.example.BookingApp.entity.Notification;
import com.example.BookingApp.entity.User;
import com.example.BookingApp.exception.NotFoundException;
import com.example.BookingApp.mapper.NotificationMapper;
import com.example.BookingApp.repository.NotificationRepository;
import com.example.BookingApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Transactional(readOnly = true)
    public NotificationDto getNotificationsSettings(String email) {
        User user = getUserByEmail(email);
        log.info("Получение настроек уведомлений для пользователя: {}", user.getId());

        Notification notification = notificationRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Настройки не найдены."));

        return notificationMapper.toDto(notification);
    }

    @Transactional
    public NotificationDto updateNotificationsSettings(String email, NotificationDto notificationDto) {
        User user = getUserByEmail(email);
        log.info("Обновление настроек уведомлений для пользователя: {}", user.getId());

        Notification notification = notificationRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Notification settings = new Notification();
                    settings.setUser(user);
                    settings.setNotifyNewEvents(false);
                    settings.setNotifyUpcoming(false);
                    settings.setHoursBeforeHours(1);
                    return settings;
                });

        notificationMapper.updateEntityFromDto(notificationDto, notification);

        Notification updated = notificationRepository.save(notification);

        log.info("Настройки успешно сохранены для пользователя: {}", user.getId());
        return notificationMapper.toDto(updated);
    }

    @Transactional
    public void deleteNotifications(String email) {
        User user = getUserByEmail(email);
        log.info("Сброс настроек уведомлений для пользователя: {}", user.getId());

        Notification notification = notificationRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Настройки не найдены."));

        notification.setNotifyUpcoming(false);
        notification.setNotifyNewEvents(false);
        notification.setHoursBeforeHours(1);
        notificationRepository.save(notification);
        log.info("Сброшены настройки уведомлений для пользователя: {}", user.getId());
    }

    @Transactional
    public String initiateTelegramLink(String email) {
        User user = getUserByEmail(email);
        log.info("Инициация привязки Telegram для пользователя: {}", user.getId());

        String code = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        user.setTelegramLinkingCode(code);
        userRepository.save(user);
        log.info("Код привязки {} сохранен для пользователя: {}", code, user.getId());
        return code;
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        log.debug("Поиск пользователя по email: {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }
}



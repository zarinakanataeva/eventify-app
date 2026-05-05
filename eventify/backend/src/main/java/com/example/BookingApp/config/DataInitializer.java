package com.example.BookingApp.config;

import com.example.BookingApp.entity.Role;
import com.example.BookingApp.entity.User;
import com.example.BookingApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@mail.com").isEmpty()) {
            User admin = new User();
            admin.setEmail("admin@mail.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            System.out.println("Админ создан: admin@mail.com, пароль: admin123");
        }

        if (userRepository.findByEmail("user@mail.com").isEmpty()) {
            User user = new User();
            user.setEmail("user@mail.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRole(Role.USER);
            userRepository.save(user);
            System.out.println("Юзер создан: user@mail.com, пароль: user123");
        }
    }
}
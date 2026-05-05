package com.example.BookingApp.service;

import com.example.BookingApp.dto.request.CreateBookingRequest;
import com.example.BookingApp.dto.request.UpdateBookingRequest;
import com.example.BookingApp.dto.response.BookingResponse;
import com.example.BookingApp.entity.Booking;
import com.example.BookingApp.entity.Event;
import com.example.BookingApp.entity.User;
import com.example.BookingApp.exception.NotFoundException;
import com.example.BookingApp.mapper.BookingMapper;
import com.example.BookingApp.repository.BookingRepository;
import com.example.BookingApp.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;
    private final BookingMapper bookingMapper;
    private final UserService userService;


    @Transactional
    public BookingResponse createBooking(String email, CreateBookingRequest dto) throws BadRequestException {
        log.info("Пользователь {} создает бронирование с ID: {}", email, dto.getEventId());

        User user = userService.getUserByEmail(email);

        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new NotFoundException("Мероприятие не найдено"));

        if (event.getAvailableTickets() < dto.getTicketCount()) {
            throw new BadRequestException("Билеты на мероприятие закончились");
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setEvent(event);
        booking.setBookingsAmount(dto.getTicketCount());
        booking.setConfirmed(false);

        Booking saved = bookingRepository.save(booking);
        log.info("Бронирование c ID {} успешно создано для пользователя {}", saved.getId(), email);
        return bookingMapper.toDto(saved);
    }

    @Transactional
    public void cancelBooking(String email, Long bookingId) {
        log.info("Пользователь {} отменяет бронирование {}", email, bookingId);

        User user = userService.getUserByEmail(email);


        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        if (!booking.getUser().getId().equals(user.getId())) {
            throw new NotFoundException("Бронирование не найдено");
        }

        bookingRepository.delete(booking);
        log.info("Бронирование {} отменено", bookingId);
    }

    @Transactional
    public BookingResponse updateBooking(String email, Long bookingId, UpdateBookingRequest dto) throws BadRequestException {
        log.info("Пользователь {} обновляет бронирование {}", email, bookingId);

        User user = userService.getUserByEmail(email);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        if (!booking.getUser().getId().equals(user.getId())) {
            throw new NotFoundException("Бронирование не найдено");
        }

        Event event = booking.getEvent();
        int diff = dto.getTicketsCount() - booking.getBookingsAmount();

        if (event.getAvailableTickets() < diff) {
            throw new BadRequestException("Недостаточно билетов");
        }

        booking.setBookingsAmount(dto.getTicketsCount());
        log.info("Бронирование {} успешно обновлено пользователем {}. Новое количество билетов: {}", bookingId, email, dto.getTicketsCount());
        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Transactional(readOnly = true)
    public BookingResponse getBookingById(String email, Long bookingId) {
        log.info("Пользователь {} ищет бронирование {}", email, bookingId);

        User user = userService.getUserByEmail(email);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        if (!booking.getUser().getId().equals(user.getId())) {
            throw new NotFoundException("Бронирование не найдено");
        }

        return bookingMapper.toDto(booking);
    }


    @Transactional(readOnly = true)
    public List<BookingResponse> getMyBookings(String email) {
        User user = userService.getUserByEmail(email);
        log.info("Пользователь {} ищет свои бронирования", email);
        return bookingMapper.toDtoList(bookingRepository.findByUserId(user.getId()));
    }
}
package com.example.BookingApp.service;

import com.example.BookingApp.dto.request.EventCreateRequest;
import com.example.BookingApp.dto.request.EventUpdateRequest;
import com.example.BookingApp.dto.response.BookingResponse;
import com.example.BookingApp.dto.response.EventResponse;
import com.example.BookingApp.entity.Booking;
import com.example.BookingApp.entity.Event;
import com.example.BookingApp.exception.NotFoundException;
import com.example.BookingApp.mapper.BookingMapper;
import com.example.BookingApp.mapper.EventMapper;
import com.example.BookingApp.repository.BookingRepository;
import com.example.BookingApp.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final EventMapper eventMapper;
    private final EventRepository eventRepository;


    @Transactional
    public EventResponse createEvent(EventCreateRequest dto) {
        log.info("Создание нового мероприятия: {}", dto.getTitle());

        Event event = eventMapper.toEntity(dto);
        event.setTotalTickets(event.getTotalTickets());
        Event savedEvent = eventRepository.save(event);

        log.info("Мероприятие создано, ID: {}", savedEvent.getId());
        return eventMapper.toDto(savedEvent);
    }

    @Transactional
    public EventResponse updateEvent(Long id, EventUpdateRequest dto) {
        log.info("Обновление мероприятия с ID: {}", id);

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Мероприятие с ID {} не найдено", id);
                    return new NotFoundException("Не найдено мероприятие с ID: " + id);
                });

        eventMapper.updateEntityFromDto(dto, event);

        if (dto.getTotalTickets() != null) {
            event.setTotalTickets(dto.getTotalTickets());
        }

        Event updatedEvent = eventRepository.save(event);
        log.info("Мероприятие с ID {} успешно обновлено.", id);
        return eventMapper.toDto(updatedEvent);
    }

    @Transactional
    public void deleteEvent(Long id) {
        log.info("Удаление мероприятия с ID: {}", id);

        if (!eventRepository.existsById(id)) {
            log.error("Мероприятие с ID {} не найдено", id);
            throw new NotFoundException("Мероприятие с ID {} не найдено" + id);
        }
        eventRepository.deleteById(id);
        log.info("Мероприятие с ID {} удалено успешно", id);
    }


    @Transactional
    public void confirmBooking(Long bookingId) {
        log.info("Администратор подтверждает бронирование {}", bookingId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        booking.setConfirmed(true);
        bookingRepository.save(booking);
    }

    @Transactional(readOnly = true)
    public Page<BookingResponse> getAllBookings(Long eventId, Boolean unconfirmedOnly, Pageable pageable) {
        Page<Booking> result;
        if (eventId != null && Boolean.TRUE.equals(unconfirmedOnly)) {
            result = bookingRepository.findByEventIdAndConfirmedFalse(eventId, pageable);
        } else if (eventId != null) {
            result = bookingRepository.findByEventId(eventId, pageable);
        } else if (Boolean.TRUE.equals(unconfirmedOnly)) {
            result = bookingRepository.findByConfirmedFalse(pageable);
        } else {
            result = bookingRepository.findAll(pageable);
        }
        return result.map(bookingMapper::toDto);
    }

    @Transactional
    public void deleteBooking(Long bookingId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw new NotFoundException("Booking not found");
        }
        bookingRepository.deleteById(bookingId);
    }
}
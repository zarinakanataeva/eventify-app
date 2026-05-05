package com.example.BookingApp.service;

import com.example.BookingApp.dto.response.EventResponse;
import com.example.BookingApp.entity.Event;
import com.example.BookingApp.exception.NotFoundException;
import com.example.BookingApp.mapper.EventMapper;
import com.example.BookingApp.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {

    private final EventMapper eventMapper;
    private final EventRepository eventRepository;


    @Transactional(readOnly = true)
    public Page<EventResponse> getAllEvents(LocalDateTime from, LocalDateTime to, Pageable pageable) {
        log.info("Получение списка мероприятий с {} до {}, pageable: {}", from, to, pageable);

        Page<Event> eventPage;
        if (from != null && to != null) {
            eventPage = eventRepository.findByDateTimeBetween(from, to, pageable);
        } else {
            eventPage = eventRepository.findAll(pageable);
        }
        log.info("Найдено {} мероприятий для страницы {}", eventPage.getContent().size(), pageable.getPageNumber());
        return eventPage.map(eventMapper::toDto);
    }

    @Transactional(readOnly = true)
    public EventResponse getEventById(Long id) {
        log.info("Получение мероприятия с ID: {}", id);

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Event with ID {} not found", id);
                    return new NotFoundException("Event not found with ID: " + id);
                });

        log.info("Найдено мероприятие: {} ", event.getTitle());
        return eventMapper.toDto(event);
    }
}

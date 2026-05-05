package com.example.BookingApp.repository;

import com.example.BookingApp.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;


@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @EntityGraph(attributePaths = {"bookings"})
    Page<Event> findByDateTimeBetween(LocalDateTime from, LocalDateTime to, Pageable pageable);

}


package com.example.BookingApp.repository;

import com.example.BookingApp.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @EntityGraph(attributePaths = {"user", "event"})
    List<Booking> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"user", "event"})
    Page<Booking> findByEventId(Long eventId, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "event"})
    Page<Booking> findByConfirmedFalse(Pageable pageable);

    @EntityGraph(attributePaths = {"user", "event"})
    Page<Booking> findByEventIdAndConfirmedFalse(Long eventId, Pageable pageable);

}


package com.example.BookingApp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notifications")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "notify_new_events", nullable = false)
    private Boolean notifyNewEvents;

    @Column(name = "notify_upcoming", nullable = false)
    private Boolean notifyUpcoming;

    @Column(name = "hours_before_notify")
    private Integer hoursBeforeHours;

}

package com.example.getticket.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "performances")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats = 100;

    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats = 30;

    @Column(name = "open_time")
    private LocalDateTime openTime;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 비즈니스 메서드
    public void decreaseAvailableSeats() {
        if (this.availableSeats > 0) {
            this.availableSeats--;
        }
    }

    public void increaseAvailableSeats() {
        if (this.availableSeats < this.totalSeats) {
            this.availableSeats++;
        }
    }

    // 생성자
    public Performance(String sessionId, LocalDateTime openTime) {
        this.sessionId = sessionId;
        this.openTime = openTime;
        this.totalSeats = 100;
        this.availableSeats = 30;
    }
}
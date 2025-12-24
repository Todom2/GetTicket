package com.example.getticket.entity;

import com.example.getticket.type.SeatStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "seats")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id", nullable = false)
    private Performance performance;

    @Column(name = "seat_name", nullable = false, length = 10)
    private String seatName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus status;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "selected_at")
    private LocalDateTime selectedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "sold_at")
    private LocalDateTime soldAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 비즈니스 메서드
    public void select(String sessionId) {
        if (this.status == SeatStatus.RESERVED) {
            throw new IllegalStateException("이미 예약된 좌석입니다");
        }

        this.status = SeatStatus.SELECTED;
        this.sessionId = sessionId;
        this.selectedAt = LocalDateTime.now();
        this.expiresAt = LocalDateTime.now().plusMinutes(5);
    }

    public void release() {
        this.status = SeatStatus.AVAILABLE;
        this.sessionId = null;
        this.selectedAt = null;
        this.expiresAt = null;
    }

    public void sell() {
        if (this.status != SeatStatus.SELECTED) {
            throw new IllegalStateException("선점되지 않은 좌석입니다");
        }

        this.status = SeatStatus.RESERVED;
        this.soldAt = LocalDateTime.now();
    }

    public void markAsReserved() {
        this.status = SeatStatus.RESERVED;
        this.soldAt = LocalDateTime.now();
    }

    // 생성자
    public Seat(Performance performance, String seatName) {
        this.performance = performance;
        this.seatName = seatName;
        this.status = SeatStatus.AVAILABLE;
    }

    // 미리 판매된 좌석 생성용
    public Seat(Performance performance, String seatName, SeatStatus status) {
        this.performance = performance;
        this.seatName = seatName;
        this.status = status;
        if (status == SeatStatus.RESERVED) {
            this.soldAt = LocalDateTime.now();
        }
    }
}
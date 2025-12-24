package com.example.getticket.repository;

import com.example.getticket.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat,Long> {
    Optional<Seat> findByPerformanceId(Long performanceId);

    @Query("SELECT s FROM Seat s WHERE s.status = 'SELECTED' AND s.expiresAt < :now")
    List<Seat> findExpiredSeats(@Param("now") LocalDateTime now);
}

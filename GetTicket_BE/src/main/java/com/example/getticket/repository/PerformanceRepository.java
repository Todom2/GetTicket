package com.example.getticket.repository;

import com.example.getticket.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance,Long> {
    Optional<Performance> findBySessionId(String sessionId);
}

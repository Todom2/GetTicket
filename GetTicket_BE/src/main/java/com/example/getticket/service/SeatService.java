package com.example.getticket.service;

import com.example.getticket.dto.request.SeatSelectRequestDto;
import com.example.getticket.dto.response.SeatResponseDto;
import com.example.getticket.dto.response.SeatSelectResponseDto;
import com.example.getticket.entity.Performance;
import com.example.getticket.entity.Seat;
import com.example.getticket.repository.PerformanceRepository;
import com.example.getticket.repository.SeatRepository;
import com.example.getticket.type.SeatStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeatService {
    private final SessionService sessionService;
    private final QueueService queueService;
    private final PerformanceRepository performanceRepository;
    private final SeatRepository seatRepository;
    private final RedissonClient redissonClient;

    public SeatResponseDto getSeats(String sessionId, String entryToken){
        // 1. 세션 검증
        sessionService.validateSession(sessionId);

        // 2. 입장 토큰 검증
        queueService.validateEntryToken(sessionId, entryToken);

        // 3. 공연 조회
        Performance performance = performanceRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new RuntimeException("공연을 찾을 수 없습니다"));

        // 4. 좌석 조회
        List<Seat> seats = seatRepository.findByPerformanceId(performance.getId())
                .stream().toList();

        // 5. DTO 변환
        List<SeatResponseDto.SeatInfo> seatInfos = seats.stream()
                .map(SeatResponseDto.SeatInfo::from)
                .toList();

        // 6. AVAILABLE 좌석 수 ( 임시 )
        Long availableSeats = 30L;

        log.info("좌석 조회: sessionId={}, totalSeats={}, availableSeats={}",
                sessionId, seats.size(), availableSeats);

        return SeatResponseDto.of(seatInfos, (long) seats.size(), availableSeats);
    }

    @Transactional
    public SeatSelectResponseDto selectSeat(SeatSelectRequestDto request) {
        String sessionId = request.getSessionId();
        String entryToken = request.getEntryToken();
        Long seatId = request.getSeatId();

        // 1. 세션 검증
        sessionService.validateSession(sessionId);

        // 2. 입장 토큰 검증
        queueService.validateEntryToken(sessionId, entryToken);

        // 3. Distributed Lock 획득
        String lockKey = "lock:seat:" + seatId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            // Lock 획득 시도 (최대 5초 대기, 10초 유지)
            boolean locked = lock.tryLock(5, 10, TimeUnit.SECONDS);

            if (!locked) {
                log.warn("Lock 획득 실패: seatId={}", seatId);
                return SeatSelectResponseDto.failure();
            }

            // 4. 좌석 조회
            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new RuntimeException("좌석을 찾을 수 없습니다"));

            // 5. 좌석 상태 확인
            if (seat.getStatus() != SeatStatus.AVAILABLE) {
                log.warn("이미 선점된 좌석: seatId={}, status={}", seatId, seat.getStatus());
                return SeatSelectResponseDto.failure();
            }

            // 6. 좌석 선점
            seat.select(sessionId);
            seatRepository.save(seat);

            log.info("좌석 선점 성공: seatId={}, sessionId={}", seatId, sessionId);

            return SeatSelectResponseDto.success(seat);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Lock 대기 중 인터럽트 발생: seatId={}", seatId, e);
            return SeatSelectResponseDto.failure();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}

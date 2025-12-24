package com.example.getticket.service;

import com.example.getticket.dto.request.ReservationRequestDto;
import com.example.getticket.dto.response.ReservationResponseDto;
import com.example.getticket.entity.Performance;
import com.example.getticket.entity.Seat;
import com.example.getticket.repository.PerformanceRepository;
import com.example.getticket.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {
    private final SessionService sessionService;
    private final PerformanceRepository performanceRepository;
    private final SeatRepository seatRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final TaskScheduler taskScheduler;

    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto reservationRequestDto){
        String sessionId = reservationRequestDto.getSessionId();
        Long userCount = reservationRequestDto.getUserCount();

        // 1. 세션의 유효성 검증
        sessionService.validateSession(sessionId);
        log.info("세션 유효성 검증 완료: sessionId={}", sessionId);

        // 2. 예매 시작 시간 계산
        LocalDateTime startTime = LocalDateTime.now().plusSeconds(30);
        log.info("예매 시작 시간: {}",startTime);

        // 3. 공연 생성
        Performance performance = new Performance(sessionId,startTime);
        performanceRepository.save(performance);
        log.info("공연 생성: performanceId={}",performance.getId());

        // 4. 좌석 생성
        List<Seat> seats = createSeats(performance);
        seatRepository.saveAll(seats);
        log.info("좌석 100개 생성(30 AVAIL, 70 RESERVED");

        // 5. 30초 후 스케줄러 예약
        scheduleQueueGeneration(sessionId, userCount, startTime);

        // 6. 시작시간을 레디스에 캐시 용도로 저장(빠른 리턴)
        saveReservationStartTime(sessionId, startTime);


        return ReservationResponseDto.of(startTime);
    }

    private List<Seat> createSeats(Performance performance) {
        List<Seat> seats = new ArrayList<>();
        char[] rows = {'A', 'B', 'C', 'D', 'E'};

        for (char row : rows) {
            for (int num = 1; num <= 20; num++) {
                String seatName = row + String.valueOf(num);
                Seat seat = new Seat(performance, seatName);
                seats.add(seat);
            }
        }
        // 랜덤 섞기
        Collections.shuffle(seats);

        // 앞에서 70개를 RESERVED로
        for (int i = 0; i < 70; i++) {
            seats.get(i).markAsReserved();
        }

        return seats;
    }

    private void scheduleQueueGeneration(String sessionId, Long userCount, LocalDateTime startTime) {
        // LocalDateTime → Instant 변환
        Instant scheduledTime = startTime.atZone(ZoneId.systemDefault()).toInstant();

        // 스케줄러 예약
        taskScheduler.schedule(
                () -> generateQueueUsers(sessionId, userCount),
                scheduledTime
        );

        log.info("큐 생성 스케줄러 예약: sessionId={}, userCount={}, 실행시간={}",
                sessionId, userCount, startTime);
    }

    private void generateQueueUsers(String sessionId, Long userCount) {
        log.info("큐 생성 시작: sessionId={}, userCount={}", sessionId, userCount);

        String queueKey = "queue:" + sessionId;
        long baseTime = System.currentTimeMillis();

        // userCount 만큼 가짜 사용자 생성
        for (int i = 0; i < userCount; i++) {
            String userId = "user_" + i;
            // 랜덤 타임스탬프 (±500ms)
            double score = baseTime + (Math.random() * 1000 - 500);

            // Sorted Set에 추가
            redisTemplate.opsForZSet().add(queueKey, userId, score);
        }

        log.info("큐 생성 완료: {}명 추가", userCount);
    }

    private void saveReservationStartTime(String sessionId, LocalDateTime startTime) {
        String key = "reservation:startTime:" + sessionId;

        redisTemplate.opsForValue().set(
                key,
                startTime.toString(),
                2,
                TimeUnit.HOURS
        );

        log.info("Redis 예매 시작 시간 저장: key={}, startTime={}", key, startTime);
    }
}

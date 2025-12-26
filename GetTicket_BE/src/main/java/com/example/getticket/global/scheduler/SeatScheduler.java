package com.example.getticket.global.scheduler;

import com.example.getticket.entity.Seat;
import com.example.getticket.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeatScheduler {

    private final SeatRepository seatRepository;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 10초마다 만료된 좌석 해제
     */
    @Scheduled(fixedRate = 10000)
    @Transactional
    public void releaseExpiredSeats() {
        log.info("releaseExpiredSeats() 실행");
        LocalDateTime now = LocalDateTime.now();

        // 1. SELECTED 상태이면서 만료 시간이 지난 좌석 조회
        List<Seat> expiredSeats = seatRepository.findExpiredSeats(now);

        if (expiredSeats.isEmpty()) {
            return;
        }

        for (Seat seat : expiredSeats) {
            // 2. 좌석 해제
            seat.release();
            log.info("만료된 좌석 해제: seatId={}, sessionId={}",
                    seat.getId(), seat.getSessionId());

            // 3. 입장 토큰 무효화
            invalidateEntryToken(seat.getSessionId());
        }

        seatRepository.saveAll(expiredSeats);

        log.info("만료된 좌석 {}개 해제 완료", expiredSeats.size());
    }

    /**
     * 입장 토큰 무효화
     */
    private void invalidateEntryToken(String sessionId) {
        // sessionId로 토큰 조회
        String sessionTokenKey = "entry:session:" + sessionId;
        String token = redisTemplate.opsForValue().get(sessionTokenKey);

        if (token != null) {
            // 토큰 삭제
            String tokenKey = "entry:token:" + token;
            redisTemplate.delete(tokenKey);
            redisTemplate.delete(sessionTokenKey);

            log.debug("입장 토큰 무효화: sessionId={}, token={}", sessionId, token);
        }
    }
}
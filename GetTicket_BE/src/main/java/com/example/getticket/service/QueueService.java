package com.example.getticket.service;

import com.example.getticket.dto.request.QueueRequestDto;
import com.example.getticket.dto.response.QueueResponseDto;
import com.example.getticket.global.exception.NotYetStartException;
import com.example.getticket.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueueService {
    private final SessionService sessionService;
    private final RedisTemplate<String, String> redisTemplate;

    public QueueResponseDto enterQueue(QueueRequestDto queueRequestDto){
        String sessionId = queueRequestDto.getSessionId();

        // 1. 세션 검증
        sessionService.validateSession(sessionId);

        // 2. 예매 시작 시간 확인
        validateReservationStartTime(sessionId);

        // 3. 큐에 추가
        String queueKey = "queue:" + sessionId;
        double score = System.currentTimeMillis();

        redisTemplate.opsForZSet().add(queueKey, sessionId, score);
        log.info("사용자 큐 진입: sessionId={}", sessionId);

        // 4. 순서 조회
        Long position = redisTemplate.opsForZSet().rank(queueKey, sessionId);
        log.info("현재 순서: position={}", position);

        return QueueResponseDto.of(position);
    }

    // Polling 을 위한 메서드
    public QueueResponseDto getQueue(String sessionId){
        sessionService.validateSession(sessionId);

        // 1. 토큰 먼저 확인
        String sessionTokenKey = "entry:session:" + sessionId;
        String existingToken = redisTemplate.opsForValue().get(sessionTokenKey);

        if (existingToken != null) {
            log.info("입장 토큰 반환: sessionId={}, token={}", sessionId, existingToken);
            return QueueResponseDto.of(0L, existingToken);
        }

        // 2. 큐에서 순서 확인
        String queueKey = "queue:" + sessionId;
        Long position = redisTemplate.opsForZSet().rank(queueKey, sessionId);

        if (position == null) {
            throw new IllegalStateException("대기열에 없습니다");
        }

        log.debug("순서 조회: sessionId={}, position={}", sessionId, position);

        return QueueResponseDto.of(position, null);
    }

    private void validateReservationStartTime(String sessionId) {
        String key = "reservation:startTime:" + sessionId;
        String startTimeStr = redisTemplate.opsForValue().get(key);

        if (startTimeStr == null) {
            throw new IllegalStateException("예매 설정을 찾을 수 없습니다");
        }

        LocalDateTime startTime = LocalDateTime.parse(startTimeStr);
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(startTime)) {
            throw new NotYetStartException("아직 예매 시작 전입니다");
        }

        log.info("예매 시간 검증 통과: startTime={}, now={}", startTime, now);
    }

    private String generateEntryToken(String sessionId) {
        // 1. UUID 기반 토큰 생성
        String token = UUID.randomUUID().toString();

        // 2. Redis에 저장 (TTL 10분)
        String tokenKey = "entry:token:" + token;
        redisTemplate.opsForValue().set(
                tokenKey,
                sessionId,
                10,
                TimeUnit.MINUTES
        );

        // 3. sessionId로도 역참조 가능하도록 저장
        String sessionTokenKey = "entry:session:" + sessionId;
        redisTemplate.opsForValue().set(
                sessionTokenKey,
                token,
                10,
                TimeUnit.MINUTES
        );

        return token;
    }

    public void validateEntryToken(String sessionId, String token) {
        if (token == null || token.isBlank()) {
            throw new UnauthorizedException("입장 토큰이 필요합니다");
        }

        String tokenKey = "entry:token:" + token;
        String storedSessionId = redisTemplate.opsForValue().get(tokenKey);

        if (storedSessionId == null) {
            throw new UnauthorizedException("유효하지 않거나 만료된 토큰입니다");
        }

        if (!storedSessionId.equals(sessionId)) {
            throw new UnauthorizedException("토큰과 세션이 일치하지 않습니다");
        }

        log.debug("입장 토큰 검증 완료: sessionId={}, token={}", sessionId, token);
    }
}

package com.example.getticket.service;

import com.example.getticket.dto.response.SessionResponseDto;
import com.example.getticket.global.exception.SessionExpiredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionService {
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public SessionResponseDto createSession(){
        // 1. SessionId 생성
        String sessionId = UUID.randomUUID().toString();
        log.info("세션 생성 : sessionId = {}",sessionId);

        // 2. Redis 에 세션 저장 ( TTL 1시간 )
        String redisKey = "session:" +  sessionId;

        redisTemplate.opsForValue().set(
                redisKey,
                sessionId,
                1,
                TimeUnit.HOURS
        );
        log.info("Redis 세션 저장 : key = {}",redisKey);

        return SessionResponseDto.of(sessionId);
    }

    public void validateSession(String sessionId) {
        String redisKey = "session:" + sessionId;
        String value = redisTemplate.opsForValue().get(redisKey);

        if (value == null) {
            throw new SessionExpiredException("유효하지 않거나 만료된 세션입니다");
        }

        log.debug("세션 유효성 검증 완료: sessionId={}", sessionId);
    }
}

package com.example.getticket.global.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class QueueScheduler {
    private final RedisTemplate<String, String> redisTemplate;

    @Scheduled(fixedRate = 50)  // 50ms마다 = 1초에 20명
    public void processQueue() {
        Set<String> queueKeys = redisTemplate.keys("queue:*");

        if (queueKeys.isEmpty()) {
            return;
        }

        for (String queueKey : queueKeys) {
            processQueueEntry(queueKey);
        }
    }

    private void processQueueEntry(String queueKey) {
        // 1. 맨 앞 사람 조회
        Set<String> topUser = redisTemplate.opsForZSet().range(queueKey, 0, 0);

        if (topUser == null || topUser.isEmpty()) {
            return;
        }

        String sessionId = topUser.iterator().next();

        // 2. 이미 토큰 발급했는지 확인
        String sessionTokenKey = "entry:session:" + sessionId;
        if (redisTemplate.hasKey(sessionTokenKey)) {
            // 이미 처리됨, 큐에서만 제거
            redisTemplate.opsForZSet().remove(queueKey, sessionId);
            return;
        }

        // 3. 토큰 생성
        String token = UUID.randomUUID().toString();

        // 4. Redis에 저장
        String tokenKey = "entry:token:" + token;
        redisTemplate.opsForValue().set(tokenKey, sessionId, 10, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(sessionTokenKey, token, 10, TimeUnit.MINUTES);

        // 5. 큐에서 제거
        redisTemplate.opsForZSet().remove(queueKey, sessionId);

        log.info("입장 처리: sessionId={}, token={}", sessionId, token);
    }
}
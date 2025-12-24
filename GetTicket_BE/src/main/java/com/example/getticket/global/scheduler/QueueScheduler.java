package com.example.getticket.global.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class QueueScheduler {
    private final RedisTemplate<String, String> redisTemplate;

    @Scheduled(fixedRate = 1000)
    public void processQueue(){
        Set<String> queueKeys = redisTemplate.keys("queue:*");

        if(queueKeys == null || queueKeys.isEmpty()){
            return;
        }

        for(String queueKey : queueKeys){
            processQueueEntry(queueKey);
        }
    }

    private void processQueueEntry(String queueKey) {
        // 큐 크기 확인
        Long queueSize = redisTemplate.opsForZSet().size(queueKey);

        if (queueSize == null || queueSize == 0) {
            return;
        }

        // 1초에 10명씩 입장
        int entryCount = Math.min(20, queueSize.intValue());

        // 상위 N명 제거
        redisTemplate.opsForZSet().removeRange(queueKey, 0, entryCount - 1);

        log.debug("큐 입장 처리: queue={}, 입장={}명, 남은={}명",
                queueKey, entryCount, queueSize - entryCount);
    }
}

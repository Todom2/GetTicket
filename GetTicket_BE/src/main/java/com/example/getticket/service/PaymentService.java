package com.example.getticket.service;

import com.example.getticket.dto.request.PaymentRequestDto;
import com.example.getticket.dto.response.PaymentResponseDto;
import com.example.getticket.entity.Seat;
import com.example.getticket.repository.SeatRepository;
import com.example.getticket.type.SeatStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final SessionService sessionService;
    private final QueueService queueService;
    private final SeatRepository seatRepository;

    @Transactional
    public PaymentResponseDto payments(PaymentRequestDto paymentRequestDto){
        String sessionId = paymentRequestDto.getSessionId();
        String entryToken = paymentRequestDto.getEntryToken();
        Long seatId = paymentRequestDto.getSeatId();

        // 1. 세션 검증
        sessionService.validateSession(sessionId);

        // 2. 입장 토큰 검증
        queueService.validateEntryToken(sessionId, entryToken);

        // 3. 좌석 조회
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("좌석을 찾을 수 없습니다"));
// 4. 좌석 상태 확인
        if (seat.getStatus() != SeatStatus.SELECTED) {
            throw new IllegalStateException("선점되지 않은 좌석입니다");
        }

        // 5. sessionId 일치 확인
        if (!seat.getSessionId().equals(sessionId)) {
            throw new IllegalStateException("본인이 선점한 좌석이 아닙니다");
        }

        // 6. 결제 완료 처리 (SELECTED → RESERVED)
        seat.sell();
        seatRepository.save(seat);

        log.info("결제 완료: seatId={}, sessionId={}", seatId, sessionId);

        return PaymentResponseDto.success(seat);
    }
}

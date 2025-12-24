package com.example.getticket.dto.response;

import com.example.getticket.entity.Seat;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatSelectResponseDto {
    private Boolean success;
    private SeatDetail seat;
    private Integer timeout;

    // 성공 응답
    public static SeatSelectResponseDto success(Seat seat) {
        return SeatSelectResponseDto.builder()
                .success(true)
                .seat(SeatDetail.from(seat))
                .timeout(300)  // 5분 = 300초
                .build();
    }

    // 실패 응답
    public static SeatSelectResponseDto failure() {
        return SeatSelectResponseDto.builder()
                .success(false)
                .build();
    }

    @Getter
    @Builder
    public static class SeatDetail {
        private Long seatId;
        private String seatNumber;
        private String status;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private LocalDateTime selectedAt;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private LocalDateTime expiresAt;

        public static SeatDetail from(Seat seat) {
            return SeatDetail.builder()
                    .seatId(seat.getId())
                    .seatNumber(seat.getSeatName())
                    .status(seat.getStatus().name())
                    .selectedAt(seat.getSelectedAt())
                    .expiresAt(seat.getExpiresAt())
                    .build();
        }
    }
}

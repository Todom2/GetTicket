package com.example.getticket.dto.response;

import com.example.getticket.entity.Seat;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {

    private Boolean success;
    private SeatDetail seat;

    public static PaymentResponseDto success(Seat seat) {
        return PaymentResponseDto.builder()
                .success(true)
                .seat(SeatDetail.from(seat))
                .build();
    }

    @Getter
    @Builder
    public static class SeatDetail {
        private Long seatId;
        private String seatNumber;
        private String status;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private LocalDateTime soldAt;

        public static SeatDetail from(Seat seat) {
            return SeatDetail.builder()
                    .seatId(seat.getId())
                    .seatNumber(seat.getSeatName())
                    .status(seat.getStatus().name())
                    .soldAt(seat.getSoldAt())
                    .build();
        }
    }
}

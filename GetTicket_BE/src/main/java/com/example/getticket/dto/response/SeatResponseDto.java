package com.example.getticket.dto.response;

import com.example.getticket.entity.Seat;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatResponseDto {
    private List<SeatInfo> seats;
    private Long totalSeats;
    private Long availableSeats;

    public static SeatResponseDto of(List<SeatInfo> seats, Long totalSeats, Long availableSeats) {
        return SeatResponseDto.builder()
                .seats(seats)
                .totalSeats(totalSeats)
                .availableSeats(availableSeats)
                .build();
    }

    @Getter
    @Builder
    public static class SeatInfo {
        private Long id;
        private String seatNumber;
        private String status;

        public static SeatInfo from(Seat seat) {
            return SeatInfo.builder()
                    .id(seat.getId())
                    .seatNumber(seat.getSeatName())
                    .status(seat.getStatus().name())
                    .build();
        }
    }

}

package com.example.getticket.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    private Long remainingSeconds;

    public static ReservationResponseDto of(LocalDateTime startTime, Long remainingSeconds) {
        return ReservationResponseDto.builder()
                .startTime(startTime)
                .remainingSeconds(remainingSeconds)
                .build();
    }
}

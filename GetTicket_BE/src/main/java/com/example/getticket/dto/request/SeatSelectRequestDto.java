package com.example.getticket.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatSelectRequestDto {
    @NotBlank(message = "세션 ID는 필수입니다")
    private String sessionId;

    @NotBlank(message = "입장 토큰은 필수입니다")
    private String entryToken;

    @NotNull(message = "좌석 ID는 필수입니다")
    private Long seatId;
}

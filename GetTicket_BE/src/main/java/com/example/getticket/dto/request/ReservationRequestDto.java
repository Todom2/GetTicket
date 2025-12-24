package com.example.getticket.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDto {
    @NotBlank(message = "세션 ID는 필수입니다")
    private String sessionId;

    @Min(value = 10, message = "최소 10명 이상이어야 합니다")
    @Max(value = 1000, message = "최대 1000명까지 가능합니다")
    private Long userCount;
}

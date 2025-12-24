package com.example.getticket.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueueRequestDto {
    @NotBlank(message = "세션 ID는 필수입니다")
    private String sessionId;
}

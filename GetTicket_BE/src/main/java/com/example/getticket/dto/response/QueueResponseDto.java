package com.example.getticket.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueueResponseDto {
    private Long position;
    private String entryToken;

    public static QueueResponseDto of(Long position) {
        return QueueResponseDto.builder()
                .position(position)
                .build();
    }

    public static QueueResponseDto of(Long position, String entryToken) {
        return QueueResponseDto.builder()
                .position(position)
                .entryToken(entryToken)
                .build();
    }
}

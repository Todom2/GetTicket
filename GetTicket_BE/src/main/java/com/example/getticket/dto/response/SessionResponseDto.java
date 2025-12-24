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
public class SessionResponseDto {
    private String sessionId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    public static SessionResponseDto of(String sessionId) {
        return SessionResponseDto.builder()
                .sessionId(sessionId)
                .createdAt(LocalDateTime.now())
                .build();
    }
}

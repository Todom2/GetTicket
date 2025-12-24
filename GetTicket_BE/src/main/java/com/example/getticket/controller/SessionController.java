package com.example.getticket.controller;

import com.example.getticket.dto.response.SessionResponseDto;
import com.example.getticket.global.response.ApiResponse;
import com.example.getticket.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SessionController {
    private final SessionService sessionService;

    @PostMapping("/session")
    public ResponseEntity<ApiResponse<?>> createSession(){
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(sessionService.createSession()));
    }
}

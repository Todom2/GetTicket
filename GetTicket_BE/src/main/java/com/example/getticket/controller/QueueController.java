package com.example.getticket.controller;

import com.example.getticket.dto.request.QueueRequestDto;
import com.example.getticket.dto.response.QueueResponseDto;
import com.example.getticket.global.response.ApiResponse;
import com.example.getticket.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class QueueController {
    private final QueueService queueService;

    @PostMapping("/queue")
    public ResponseEntity<ApiResponse<?>> enterQueue(@RequestBody QueueRequestDto queueRequestDto){
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(queueService.enterQueue(queueRequestDto)));
    }

    @GetMapping("/queue")
    public ResponseEntity<ApiResponse<?>> getQueue(@RequestParam String sessionId){
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(queueService.getQueue(sessionId)));
    }
}

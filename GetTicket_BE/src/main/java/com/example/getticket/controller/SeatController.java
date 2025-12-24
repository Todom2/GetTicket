package com.example.getticket.controller;

import com.example.getticket.dto.request.SeatSelectRequestDto;
import com.example.getticket.global.response.ApiResponse;
import com.example.getticket.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;

    @GetMapping("/seats")
    public ResponseEntity<ApiResponse<?>> getSeats(
            @RequestParam String sessionId,
            @RequestParam String entryToken
    ){
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(seatService.getSeats(sessionId, entryToken)));
    }

    @PostMapping("/seat")
    public ResponseEntity<ApiResponse<?>> selectSeat(@RequestBody SeatSelectRequestDto seatSelectRequestDto){
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(seatService.selectSeat(seatSelectRequestDto)));
    }
}

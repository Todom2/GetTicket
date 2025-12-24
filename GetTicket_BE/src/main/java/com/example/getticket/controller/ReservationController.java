package com.example.getticket.controller;

import com.example.getticket.dto.request.ReservationRequestDto;
import com.example.getticket.dto.response.ReservationResponseDto;
import com.example.getticket.global.response.ApiResponse;
import com.example.getticket.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("/reservation")
    public ResponseEntity<ApiResponse<?>> createReservation(@RequestBody @Valid ReservationRequestDto reservationRequestDto){
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(reservationService.createReservation(reservationRequestDto)));
    }
}

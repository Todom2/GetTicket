package com.example.getticket.controller;

import com.example.getticket.dto.request.PaymentRequestDto;
import com.example.getticket.global.response.ApiResponse;
import com.example.getticket.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/payment")
    public ResponseEntity<ApiResponse<?>> payments(@RequestBody PaymentRequestDto paymentRequestDto){
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(paymentService.payments(paymentRequestDto)));
    }
}

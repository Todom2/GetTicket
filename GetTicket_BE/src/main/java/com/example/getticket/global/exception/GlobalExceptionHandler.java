package com.example.getticket.global.exception;

import com.example.getticket.global.response.ApiResponse;
import com.example.getticket.global.type.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(SessionExpiredException.class)
    public ResponseEntity<ApiResponse<?>> handleSessionExpiredException(SessionExpiredException e) {
        log.warn("세션 만료: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(StatusCode.SESSION_EXPIRED, e.getMessage()));
    }

    @ExceptionHandler(AlreadyReservedException.class)
    public ResponseEntity<ApiResponse<?>> handleAlreadyReserved(AlreadyReservedException e) {
        log.warn("좌석 이미 선점됨: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(StatusCode.ALREADY_RESERVED, e.getMessage()));
    }

    // Validation 에러 (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        List<ApiResponse.FieldError> fieldErrors = bindingResult.getFieldErrors().stream()
                .map(error -> ApiResponse.FieldError.builder()
                        .field(error.getField())
                        .message(error.getDefaultMessage())
                        .reason(error.getRejectedValue() != null ? error.getRejectedValue().toString() : "null")
                        .build())
                .collect(Collectors.toList());

        log.warn("Validation 에러: {}", fieldErrors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(StatusCode.INVALID_INPUT, fieldErrors));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<?>> handleUnauthorized(UnauthorizedException e) {
        log.warn("인증 실패: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(StatusCode.UNAUTHORIZED, e.getMessage()));
    }

    // IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("잘못된 인자: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(StatusCode.INVALID_INPUT, e.getMessage()));
    }

    // 그 외 모든 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneral(Exception e) {
        log.error("서버 에러", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(StatusCode.INTERNAL_ERROR));
    }
}

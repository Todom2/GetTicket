package com.example.getticket.global.exception;

public class AlreadyReservedException extends RuntimeException {
    public AlreadyReservedException() {
        super("이미 선점된 좌석입니다");
    }

    public AlreadyReservedException(String message) {
        super(message);
    }
}

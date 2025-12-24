package com.example.getticket.global.exception;

public class SessionExpiredException extends RuntimeException {
    public SessionExpiredException() {
        super("세션이 만료되었습니다.");
    }

    public SessionExpiredException(String message) {
        super(message);
    }
}

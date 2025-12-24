package com.example.getticket.global.exception;

public class NotYetStartException extends RuntimeException {
        public NotYetStartException() {
            super("아직 입장 순서가 아닙니다");
        }

        public NotYetStartException(String message) {
            super(message);
        }
}

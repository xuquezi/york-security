package com.example.york.exception;

public class SelfThrowException extends RuntimeException {
    public SelfThrowException(String message) {
        super(message);
    }
}

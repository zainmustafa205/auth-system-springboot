package com.example.authsystem.exception;

public class InvalidRefreshTokenException extends RuntimeException {
        
    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}

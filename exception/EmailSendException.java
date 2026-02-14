package com.example.authsystem.exception;

public class EmailSendException extends RuntimeException {

    public EmailSendException(String message) {
        super(message);
    }
}

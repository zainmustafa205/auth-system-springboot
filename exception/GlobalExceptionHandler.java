package com.example.authsystem.exception;

import com.example.authsystem.dto.response.ApiResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponseDTO> handleUserNotFound(UserNotFoundException ex) {
        return new ResponseEntity<>(
                new ApiResponseDTO(false, ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponseDTO> handleDuplicate(DuplicateResourceException ex) {
        return new ResponseEntity<>(
                new ApiResponseDTO(false, ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiResponseDTO> handleInvalidToken(InvalidTokenException ex) {
        return new ResponseEntity<>(
                new ApiResponseDTO(false, ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ApiResponseDTO> handleInvalidRefreshToken(InvalidRefreshTokenException ex) {
        return new ResponseEntity<>(
                new ApiResponseDTO(false, ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }
    
    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<ApiResponseDTO> handleUnauthorized(UnauthorizedActionException ex) {
        return new ResponseEntity<>(
                new ApiResponseDTO(false, ex.getMessage()),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(EmailSendException.class)
    public ResponseEntity<ApiResponseDTO> handleEmailSend(EmailSendException ex) {
        return new ResponseEntity<>(
                new ApiResponseDTO(false, ex.getMessage()),
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }

    @ExceptionHandler(SmsSendException.class)
    public ResponseEntity<ApiResponseDTO> handleSmsSend(SmsSendException ex) {
        return new ResponseEntity<>(
                new ApiResponseDTO(false, ex.getMessage()),
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }

    // fallback (safety net)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO> handleGeneric(Exception ex) {
        return new ResponseEntity<>(
                new ApiResponseDTO(false, "Something went wrong"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}

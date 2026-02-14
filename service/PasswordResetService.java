package com.example.authsystem.service;

import com.example.authsystem.dto.response.ApiResponseDTO;

public interface PasswordResetService {

    ApiResponseDTO createPasswordResetToken(String email);

    ApiResponseDTO validatePasswordResetToken(String token);

    ApiResponseDTO resetPassword(String token, String newPassword);
}

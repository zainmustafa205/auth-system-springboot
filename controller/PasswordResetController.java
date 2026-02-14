package com.example.authsystem.controller;

import com.example.authsystem.dto.request.ForgotPasswordRequestDTO;
import com.example.authsystem.dto.request.ResetPasswordDTO;
import com.example.authsystem.dto.response.ApiResponseDTO;
import com.example.authsystem.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/password")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    // ================= FORGOT PASSWORD =================
    @PostMapping("/forgot")
    public ResponseEntity<ApiResponseDTO> forgotPassword(
            @RequestBody ForgotPasswordRequestDTO request
    ) {

        ApiResponseDTO response =
                passwordResetService.createPasswordResetToken(request.getValue());

        return ResponseEntity.ok(response);
    }

    // ================= VALIDATE TOKEN =================
    @GetMapping("/validate")
    public ResponseEntity<ApiResponseDTO> validateToken(
            @RequestParam("token") String token
    ) {

        ApiResponseDTO response =
                passwordResetService.validatePasswordResetToken(token);

        return ResponseEntity.ok(response);
    }

    // ================= RESET PASSWORD =================
    @PostMapping("/reset")
    public ResponseEntity<ApiResponseDTO> resetPassword(
            @RequestBody ResetPasswordDTO dto
    ) {

        ApiResponseDTO response =
                passwordResetService.resetPassword(dto.getToken(), dto.getNewPassword());

        return ResponseEntity.ok(response);
    }
}

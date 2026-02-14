package com.example.authsystem.controller;

import com.example.authsystem.dto.request.UpdateEmailDTO;
import com.example.authsystem.dto.request.UpdatePhoneDTO;
import com.example.authsystem.dto.request.UpdateUsernameDTO;
import com.example.authsystem.dto.response.ApiResponseDTO;
import com.example.authsystem.dto.response.AuthResponseDTO;
import com.example.authsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ================= UPDATE EMAIL =================
    @PutMapping("/user/{userId}/email")
    public ResponseEntity<AuthResponseDTO> updateEmail(
            @PathVariable Long userId,
            @RequestBody UpdateEmailDTO dto,
            Authentication authentication
    ) {

        Long jwtUserId = (Long) authentication.getPrincipal();

        AuthResponseDTO response =
                userService.updateEmail(userId, jwtUserId, dto);

        return ResponseEntity.ok(response);
    }

    // ================= UPDATE USERNAME =================
    @PutMapping("/user/{userId}/username")
    public ResponseEntity<AuthResponseDTO> updateUsername(
            @PathVariable Long userId,
            @RequestBody UpdateUsernameDTO dto,
            Authentication authentication
    ) {

        Long jwtUserId = (Long) authentication.getPrincipal();

        AuthResponseDTO response =
                userService.updateUsername(userId, jwtUserId, dto );

        return ResponseEntity.ok(response);
    }

    // ================= UPDATE PHONE =================
    @PutMapping("/user/{userId}/phone")
    public ResponseEntity<AuthResponseDTO> updatePhone(
            @PathVariable Long userId,
            @RequestBody UpdatePhoneDTO dto,
            Authentication authentication
    ) {

        Long jwtUserId = (Long) authentication.getPrincipal();

        AuthResponseDTO response =
                userService.updatePhone(userId, jwtUserId, dto );

        return ResponseEntity.ok(response);
    }

    // ================= ADMIN → MAKE ADMIN =================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/make-admin/{targetUserId}")
    public ResponseEntity<ApiResponseDTO> makeAdmin(
            @PathVariable Long targetUserId,
            Authentication authentication
    ) {

        Long adminId = (Long) authentication.getPrincipal();

        ApiResponseDTO response =
                userService.makeAdmin(adminId, targetUserId);

        return ResponseEntity.ok(response);
    }
}

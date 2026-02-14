package com.example.authsystem.service;

import com.example.authsystem.dto.request.UpdateEmailDTO;
import com.example.authsystem.dto.request.UpdatePhoneDTO;
import com.example.authsystem.dto.request.UpdateUsernameDTO;
import com.example.authsystem.dto.response.ApiResponseDTO;
import com.example.authsystem.dto.response.AuthResponseDTO;
import com.example.authsystem.entity.RefreshToken;
import com.example.authsystem.entity.Role;
import com.example.authsystem.entity.User;
import com.example.authsystem.exception.DuplicateResourceException;
import com.example.authsystem.exception.UnauthorizedActionException;
import com.example.authsystem.exception.UserNotFoundException;
import com.example.authsystem.repository.RefreshTokenRepository;
import com.example.authsystem.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    // ========= UPDATE EMAIL =========
    public AuthResponseDTO updateEmail(Long userId, Long jwtUserId, UpdateEmailDTO dto ) {

        if (!userId.equals(jwtUserId)) {
            throw new UnauthorizedActionException("You can only update your own email");
        }

        if (userRepository.findByEmail(dto.getNewEmail()).isPresent()) {
            throw new DuplicateResourceException("Email already in use");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setEmail(dto.getNewEmail());
        userRepository.save(user);

        // Generate new access token after update
        String newAccessToken = jwtService.generateAccessToken(user);
                    RefreshToken existingToken = refreshTokenRepository
            .findByUser(user)
            .orElseThrow(() ->
                    new RuntimeException("Refresh token not found"));

        return new AuthResponseDTO(
                newAccessToken,
                existingToken.getToken(),
                "Bearer",
                user.getId(),
                user.getRole().name()
        );
    }

    // ========= UPDATE USERNAME =========
    public AuthResponseDTO updateUsername(Long userId, Long jwtUserId, UpdateUsernameDTO dto ) {

        if (!userId.equals(jwtUserId)) {
            throw new UnauthorizedActionException("You can only update your own username");
        }

        if (userRepository.findByUsername(dto.getNewUsername()).isPresent()) {
            throw new DuplicateResourceException("Username already in use");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setUsername(dto.getNewUsername());
        userRepository.save(user);

        // Generate new access token after update
        String newAccessToken = jwtService.generateAccessToken(user);
            RefreshToken existingToken = refreshTokenRepository
            .findByUser(user)
            .orElseThrow(() ->
                    new RuntimeException("Refresh token not found"));

        return new AuthResponseDTO(
                newAccessToken,
                existingToken.getToken(),
                "Bearer",
                user.getId(),
                user.getRole().name()
        );
    }

    // ========= UPDATE PHONE =========
    public AuthResponseDTO updatePhone(Long userId, Long jwtUserId, UpdatePhoneDTO dto ) {

        if (!userId.equals(jwtUserId)) {
            throw new UnauthorizedActionException("You can only update your own phone number");
        }

        if (userRepository.findByPhoneNumber(dto.getNewPhoneNumber()).isPresent()) {
            throw new DuplicateResourceException("Phone number already in use");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setPhoneNumber(dto.getNewPhoneNumber());
        userRepository.save(user);

        // Generate new access token after update
        String newAccessToken = jwtService.generateAccessToken(user);

        RefreshToken existingToken = refreshTokenRepository
            .findByUser(user)
            .orElseThrow(() ->
                    new RuntimeException("Refresh token not found"));
        return new AuthResponseDTO(
                newAccessToken,
                existingToken.getToken(),
                "Bearer",
                user.getId(),
                user.getRole().name()
        );
    }

    // ========= ADMIN → USER TO ADMIN =========
@Transactional
public ApiResponseDTO makeAdmin(Long adminId, Long targetUserId) {

    // Verify admin
    User admin = userRepository.findById(adminId)
            .orElseThrow(() -> new UserNotFoundException("Admin not found"));

    if (admin.getRole() != Role.ROLE_ADMIN) {
        throw new UnauthorizedActionException("Only admin can perform this action");
    }

    // Fetch target user
    User targetUser = userRepository.findById(targetUserId)
            .orElseThrow(() -> new UserNotFoundException("Target user not found"));

    // Promote user
    targetUser.setRole(Role.ROLE_ADMIN);
    userRepository.save(targetUser);

    // Invalidate all active sessions of user
    refreshTokenRepository.deleteByUser(targetUser);

    // Audit log (optional but recommended)
    // log.info(
    //     "Admin [{}] promoted User [{}] to ADMIN",
    //     admin.getUsername(),
    //     targetUser.getUsername()
    // );

    // Return simple response (ADMIN ko)
    return new ApiResponseDTO(
            true,
            "User promoted to ADMIN successfully. User must re-login to apply changes."
    );
}
}

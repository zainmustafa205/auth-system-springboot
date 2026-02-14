package com.example.authsystem.service.impl;
 
import com.example.authsystem.dto.response.ApiResponseDTO;
import com.example.authsystem.entity.PasswordResetToken;
import com.example.authsystem.entity.User;
import com.example.authsystem.exception.InvalidTokenException;
import com.example.authsystem.exception.UserNotFoundException;
import com.example.authsystem.repository.PasswordResetTokenRepository;
import com.example.authsystem.repository.UserRepository;
import com.example.authsystem.service.EmailService;
import com.example.authsystem.service.PasswordResetService;
import com.example.authsystem.service.SmsService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final SmsService smsService;

    // ================= CREATE TOKEN =================
@Override
   @Transactional
public ApiResponseDTO createPasswordResetToken(String identifier) {

    // 1️⃣ Find user by email, username, or phone number
    User user = userRepository
            .findByEmailOrUsernameOrPhoneNumber(identifier, identifier, identifier)
            .orElseThrow(() ->
                    new UserNotFoundException("User not found"));

    //Invalidate old tokens
    tokenRepository.deleteByUser(user);

    ApiResponseDTO response;

    if (identifier.equalsIgnoreCase(user.getEmail()) 
            || identifier.equalsIgnoreCase(user.getUsername())) {

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));

        tokenRepository.save(resetToken);

        //String resetLink = "https://yourapp.com/reset-password?token=" + token;

        response = emailService.sendPasswordResetEmail(
                user.getEmail(),
                user.getUsername(),
                token
        );

    } else if (identifier.equalsIgnoreCase(user.getPhoneNumber())) {


        // Generate OTP (6 digit)
        String otp = String.valueOf(
                (int) (Math.random() * 900000) + 100000
        );

        PasswordResetToken otpToken = new PasswordResetToken();
        otpToken.setToken(passwordEncoder.encode(otp)); // hashed
        otpToken.setUser(user);
        otpToken.setExpiryDate(LocalDateTime.now().plusMinutes(2)); // ⏱ 2 minutes

        tokenRepository.save(otpToken);

        response = smsService.sendPasswordResetOtp(user.getPhoneNumber(), otp);

    } else {
        // fallback
        response = new ApiResponseDTO(false, "Unable to process password reset");
    }

    return response;
}



    // ================= VALIDATE TOKEN =================
@Override
public ApiResponseDTO validatePasswordResetToken(String token) {

    PasswordResetToken resetToken = tokenRepository.findByToken(token)
            .orElseThrow(() -> new InvalidTokenException("Invalid reset token"));

    if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
        tokenRepository.delete(resetToken);
        return new ApiResponseDTO(false, "Reset token expired");
    }

    return new ApiResponseDTO(true, "Reset token is valid");
}


    // ================= RESET PASSWORD =================
@Override
public ApiResponseDTO resetPassword(String token, String newPassword) {

    PasswordResetToken resetToken = tokenRepository.findByToken(token)
            .orElseThrow(() -> new InvalidTokenException("Invalid reset token"));

    if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
        tokenRepository.delete(resetToken);
        return new ApiResponseDTO(false, "Reset token expired");
    }

    User user = resetToken.getUser();
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);

    tokenRepository.delete(resetToken);

    return new ApiResponseDTO(true, "Password has been reset successfully");
}

}

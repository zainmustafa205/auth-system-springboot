package com.example.authsystem.service;

import com.example.authsystem.dto.response.ApiResponseDTO;
import com.example.authsystem.exception.EmailSendException;

import lombok.RequiredArgsConstructor;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    
    private final JavaMailSender mailSender;
 
    public ApiResponseDTO sendPasswordResetEmail(String toEmail, String username, String resetToken) {

        try {
            // ====== Build Email ======
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Password Reset Request");
            message.setText(
                    "Hello " + username + ",\n\n"
                    + "You requested to reset your password.\n\n"
                    + "Your password reset token is:\n\n"
                    + resetToken + "\n\n"
                    + "This token will expire in 15 minutes.\n\n"
                    + "If you did not request this, please ignore this email."
            );

            // ====== Send Email ======
            mailSender.send(message);

            // ====== Log (Optional) ======
            System.out.println("Password reset email sent to: " + toEmail);

                return new ApiResponseDTO(
                     true,
                     "Password reset email sent if user exists"
                );

        } catch (Exception ex) {
            // ====== Log internally ======
            System.err.println("Failed to send password reset email to: " + toEmail);
            System.err.println("Error: " + ex.getMessage());

            // ====== Throw custom exception ======
            throw new EmailSendException("Unable to send password reset email at the moment");
        }
       
    }
}

package com.example.authsystem.service;

import com.example.authsystem.entity.RefreshToken;
import com.example.authsystem.entity.User;
import com.example.authsystem.exception.InvalidRefreshTokenException;
import com.example.authsystem.repository.RefreshTokenRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh.expiry-days}")
    private long refreshTokenExpiryDays;
    
    // CREATE / ROTATE REFRESH TOKEN
    @Transactional
    public RefreshToken createRefreshToken(User user) {

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(refreshTokenExpiryDays));

        return refreshTokenRepository.save(refreshToken);
    }

    // VERIFY
    public RefreshToken verifyRefreshToken(String token) {
 System.out.println("error mila nhi 22222");
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() ->
                        new InvalidRefreshTokenException("Invalid refresh token"));
 System.out.println("error mila nhi 33333");
        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new InvalidRefreshTokenException("Refresh token expired");
        }
 System.out.println("error mila nhi 4444");
        return refreshToken;
    }

    // LOGOUT
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}

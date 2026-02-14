package com.example.authsystem.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.authsystem.dto.request.LoginRequestDTO;
import com.example.authsystem.dto.request.RegisterRequestDTO;
import com.example.authsystem.dto.response.AuthResponseDTO;
import com.example.authsystem.entity.RefreshToken;
import com.example.authsystem.entity.Role;
import com.example.authsystem.entity.User;
import com.example.authsystem.exception.DuplicateResourceException;
import com.example.authsystem.exception.InvalidTokenException;
import com.example.authsystem.exception.UserNotFoundException;
import com.example.authsystem.repository.RefreshTokenRepository;
import com.example.authsystem.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    // ================= REGISTER =================
    public AuthResponseDTO register(RegisterRequestDTO dto) {

        if (userRepository.findByUsername(dto.getUsername()).isPresent())
            throw new DuplicateResourceException("Username already exists");

        if (userRepository.findByEmail(dto.getEmail()).isPresent())
            throw new DuplicateResourceException("Email already exists");

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.ROLE_USER);

        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponseDTO(
                accessToken,
                refreshToken.getToken(),
                "Bearer",
                user.getId(),
                user.getRole().name()
        );
    }

    // ================= LOGIN =================
    @Transactional
    public AuthResponseDTO login(LoginRequestDTO dto) {

        User user = userRepository.findByUsername(dto.getUsernameOrEmail())
        .or(() -> userRepository.findByEmail(dto.getUsernameOrEmail()))
        .orElseThrow(() -> new UserNotFoundException("Invalid credentials"));


        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword()))
            throw new UserNotFoundException("Invalid credentials");

        String accessToken = jwtService.generateAccessToken(user);

        refreshTokenRepository.deleteByUser(user);
        
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponseDTO(
                accessToken,
                refreshToken.getToken(),
                "Bearer",
                user.getId(),
                user.getRole().name()
        );
    }

    // ================= REFRESH =================
    public AuthResponseDTO refreshToken(String refreshTokenValue) {
 System.out.println("error mila nhi 1111");
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenValue);
       
        User user = refreshToken.getUser();
        System.out.println("User: " + refreshToken.getUser());

        String newAccessToken = jwtService.generateAccessToken(user);

        return new AuthResponseDTO(
                newAccessToken,
                refreshTokenValue,
                "Bearer",
                user.getId(),
                user.getRole().name()
        );
    }

    // ================= LOGOUT =================
    public void logout(User user) {
        refreshTokenService.deleteByUser(user);
    }
}

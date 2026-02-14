package com.example.authsystem.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.authsystem.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access.expiration}")
    private long accessTokenExpiration;
 

    // ================= SIGNING KEY =================
    private Key getSigningKey() {
        System.out.println(jwtSecret.length());

        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // ================= ACCESS TOKEN =================
    public String generateAccessToken(User user) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenExpiration);

        return Jwts.builder()
                .setSubject(user.getId().toString())        // USER ID
                .claim("role", user.getRole().name())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

}

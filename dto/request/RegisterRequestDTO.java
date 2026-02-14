package com.example.authsystem.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {
    private String username;
    private String email;
    private String phoneNumber;
    private String password;
}

package com.example.authsystem.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    private String usernameOrEmail; // User can login using username or email
    private String password;
}

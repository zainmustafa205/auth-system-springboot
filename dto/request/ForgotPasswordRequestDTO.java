package com.example.authsystem.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordRequestDTO {
    private String value; // Email OR Phone OR Username
}

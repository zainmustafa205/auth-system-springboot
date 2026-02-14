package com.example.authsystem.service;

import com.example.authsystem.dto.response.ApiResponseDTO;
import com.example.authsystem.exception.SmsSendException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${twilio.from-number}")
    private String fromNumber;

 
    public ApiResponseDTO sendPasswordResetOtp(String phoneNumber, String otp) {

        try {
            Message.creator(
                    new PhoneNumber(phoneNumber),      // TO
                    new PhoneNumber(fromNumber),       // FROM
                    "Your password reset OTP is: " + otp +
                    ". It will expire in 2 minutes."
            ).create();

            return new ApiResponseDTO(
                    true,
                    "OTP has been sent to the phone number if user exists"
            );

        } catch (Exception ex) {
 
            System.err.println("SMS sending failed: " + ex.getMessage());
            throw new SmsSendException("Unable to send OTP at the moment");
        }
    }
}

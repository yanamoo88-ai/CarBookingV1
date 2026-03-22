package com.carbooking.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegistrationDto {
    private String fullName;
    private String email;
    private String password;
    private String phone;
}
package com.carbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Integer id;
    private String fullName; // eq  full_name
    private String email;
    private String phone;
    private String passwordHash; // eq  -> password_hash
    private String role;
    private String createdAt;
}

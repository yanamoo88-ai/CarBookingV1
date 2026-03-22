package com.carbooking.dto;import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Integer id;
    private String fullName; // соответствует full_name
    private String email;
    private String phone;
    private String passwordHash; // соответствует password_hash
    private String role;
    private String createdAt;
}

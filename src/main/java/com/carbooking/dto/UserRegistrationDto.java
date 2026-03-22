package com.carbooking.dto;

//import io.qameta.allure.internal.shadowed.jackson.annotation.JsonProperty;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class UserRegistrationDto {
//    @JsonProperty("full_name")
    private String full_name;
    private String email;
    private String password;
    private String phone;
}
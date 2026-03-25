package com.carbooking.dto;

//import io.qameta.allure.internal.shadowed.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;


@Data
@Builder
@AllArgsConstructor
@ToString(of = {"email"}) // Lombok сам создаст короткий toString, используя только email
public class UserRegistrationDto {
//    @JsonProperty("full_name")
    private String full_name;
    private String email;
    private String password;
    private String phone;
}

package com.carbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarTypeDto {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal pricePerDay;
    private String imageUrl;
}



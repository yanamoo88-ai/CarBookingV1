package com.carbooking.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarTypeDto {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal pricePerDay; // соответствует price_per_day
    private String imageUrl;        // соответствует image_url
}



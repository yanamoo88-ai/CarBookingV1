package com.carbooking.api.dto;
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarAvailabilityResponseDto {
    private Integer carId;
    private String model;
    private String typeName;
    private BigDecimal pricePerDay;
}

// (Для проверки доступности — US-01)
//Этот DTO для отображения списка доступных машин пользователю.

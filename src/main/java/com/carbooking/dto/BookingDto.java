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
public class BookingDto {
    private Integer id;
    private Integer userId; // eq -> user_id
    private Integer carId; // eq -> car_id
    private String startDate; // format DATe
    private String endDate;
    private BigDecimal totalPrice; // eq total_price
    private String status; // enum -> booking status
}


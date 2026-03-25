package com.carbooking.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingRequestDto {
    private Integer user_id;
    private Integer car_id;
    private String start_date;
    private String end_date;
    private Double totalPrice;
    private String status;
}

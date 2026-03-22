package com.carbooking.dto;


import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Integer id;
    private Integer userId; // соответствует user_id
    private Integer carId; // соответствует car_id
    private String startDate; // формат даты из БД
    private String endDate;
    private BigDecimal totalPrice; // соответствует total_price
    private String status; // enum статус бронирования
}


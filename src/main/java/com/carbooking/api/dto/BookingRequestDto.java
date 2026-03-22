package com.carbooking.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingRequestDto {
    private Integer userId; // есть в БД
    private Integer carId; // есть в БД
    private String startDate; // есть в БД
    private String endDate; // есть в БД
    private Double totalPrice; // есть в БД
    private String status; // есть в БД (pending/confirmed/etc)
}

// ❌ НЕТ contactPhone и contactEmail - они в users!
//(Для создания бронирования — US-02) Client
// Мы не передаем total_price или status,
// потому что сервер должен рассчитать цену сам, а статус по умолчанию будет pending.
// Этот объект мы будем отправлять в теле (body) POST-запроса
// на /api/v1/bookings.
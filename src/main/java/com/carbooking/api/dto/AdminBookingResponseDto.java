package com.carbooking.api.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminBookingResponseDto {
    private Integer id;
    private String clientName; // Из таблицы users
    private String clientPhone;
    private String carModel; // Из таблицы cars
    private String status; // pending, confirmed и т.д.
}
//  (Для получения списка — US-03)
// US-03: Список заявок для админа (Admin)
//Задача: Админ должен видеть ID,
// контакты клиента и статус заявки.
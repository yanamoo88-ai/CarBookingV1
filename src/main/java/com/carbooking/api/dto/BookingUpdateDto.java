package com.carbooking.api.dto;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingUpdateDto {
    private String status; // Новый статус
    private String startDate; // Возможность изменить сроки
    private String endDate;
}

// Для PATCH запросов — US-04
//Редактирование заявки (Admin)
//Задача: Изменить статус (например, подтвердить)
// или сроки аренды.
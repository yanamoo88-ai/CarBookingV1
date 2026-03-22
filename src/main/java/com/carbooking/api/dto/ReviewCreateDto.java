package com.carbooking.api.dto;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateDto {
    private Integer bookingId;
    private Integer userId;
    private Integer rating; // 1-5
    private String comment; // Будем проверять на длину в тестах
}



//DTO для работы с отзывами (US-06)
//Здесь важно учесть валидацию текста (мин. 10 символов) и рейтинга (1–5 звезд).
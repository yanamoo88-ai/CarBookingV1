package com.carbooking.api.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {
    private Integer bookingId;
    private Integer userId;
    private Integer rating; // 1-5
    private String comment; // мин. 10 символов по критериям
}

//(Для отправки отзыва — US-06)
//учитываем валидацию: рейтинг 1–5 и текст отзыва.
//US-06: Отзыв о салоне (Client)
//Задача: Оставить отзыв (рейтинг 1-5 и текст минимум 10 символов).

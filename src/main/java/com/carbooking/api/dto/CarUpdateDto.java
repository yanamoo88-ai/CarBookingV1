package com.carbooking.api.dto;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarUpdateDto {
    private String status; // Например, перевод в 'archived'
}

// для управления машинами (US-05)
//Для реализации Soft Delete (удаление без полного удаления из БД).

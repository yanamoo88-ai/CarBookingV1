package com.carbooking.dto;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

    public class CarDto {
    private Integer id;
        private String brand;
        private String model;
        private String numberPlate;
        private Integer categoryId; // foreign key к car_categories
        private String imageUrl; // ссылка на фото
        private String status; // ссылка на фото

    }
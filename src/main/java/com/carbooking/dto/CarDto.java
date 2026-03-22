package com.carbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

    public class CarDto {
    private Integer id;
        private String brand;
        private String model;
        private String numberPlate;
    private Integer categoryId; // foreign key  car_categories
        private String imageUrl;
        private String status;
    }
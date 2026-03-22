package com.carbooking.dto;


import java.time.LocalDateTime;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Integer id;
    //private Integer bookingId; // соответствует booking_id
    private Integer rating;
    private String content;
    private String createdAt;  // соответствует created_at
    // +pole
    private Integer userId;
}
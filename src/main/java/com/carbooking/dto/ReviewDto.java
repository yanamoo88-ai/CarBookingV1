package com.carbooking.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Integer id;
    //private Integer bookingId; // eq booking_id
    private Integer rating;
    private String content;
    private String createdAt;  //eq -> created_at
    // +pole
    private Integer userId;
}
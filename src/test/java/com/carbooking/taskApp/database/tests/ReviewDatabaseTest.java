package com.carbooking.taskApp.database.tests;

import com.carbooking.database.repository.ReviewRepository;
import com.carbooking.dto.ReviewDto;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ReviewDatabaseTest {

    ReviewRepository reviewRepository = new ReviewRepository();


    @Test
    public void testGetReviewByIdFromDb() {
        Integer targetId = 1;
        ReviewDto review = reviewRepository.findById(targetId);

        Assert.assertNotNull(review, "Review not found!");
        Assert.assertNotNull(review.getUserId(), "The user_id field must not be empty"); // Проверка нового поля

        System.out.println("Review found! Left by user with ID: " + review.getUserId());
    }
}

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

        Assert.assertNotNull(review, "Отзыв не найден!");
        Assert.assertNotNull(review.getUserId(), "Поле user_id не должно быть пустым"); // Проверка нового поля

        System.out.println("Отзыв найден! Оставлен пользователем с ID: " + review.getUserId());
    }
}

//Ваш SQL-скрипт содержит триггер check_review_eligibility,
// который запрещает оставлять отзыв, если поездка не завершена (status != 'completed')
// или если user_id в отзыве не совпадает с user_id в бронировании.
//Это значит, что при ручном наполнении базы для тестов,
// данные должны быть логически согласованы, иначе база выдаст ошибку 45000.
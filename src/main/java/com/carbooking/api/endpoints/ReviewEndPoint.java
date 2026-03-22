package com.carbooking.api.endpoints;

import com.carbooking.api.dto.ReviewRequestDto;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class ReviewEndPoint {

    // US-06: Валидация и сохранение отзыва
    public Response postReview(ReviewRequestDto reviewDto) {
        return given()
                .body(reviewDto)
                .when()
                .post("/v1/reviews");
    }
}

// Отвечает за US-06 — отправку отзыва после завершения поездки.

//Что важно помнить:
// * Все методы возвращают Response.
// Это позволит нам в тестах проверять коды ответов (200, 201, 400 и т.д.)
// и содержимое JSON.
// * Пути в методах (/v1/...) должны совпадать с тем,
// что написано в ваших Acceptance Criteria.

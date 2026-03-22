package com.carbooking.taskApp.api.tests;


import com.carbooking.dto.AuthRequestDto;
import com.carbooking.dto.ReviewDto;
import com.carbooking.taskApp.api.config.BaseApiTest;
import com.carbooking.taskApp.utils.LogUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;


import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;


public class ReviewApiNoLogin extends BaseApiTest {

    private String authToken;
    private int userId;

    private void login() {
        LogUtil.step("Логин в систему");

        // Создаем запрос на логин
        AuthRequestDto loginRequest = AuthRequestDto.builder()
                .email("test@example.com")  // замени на реальный email
                .password("password123")     // замени на реальный пароль
                .build();

        // Отправляем запрос на логин
        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body(loginRequest)
                .when()
                .post("/api/auth/login")  // эндпоинт логина
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());

        // Проверяем что залогинились успешно
        assertEquals(response.getStatusCode(), 200, "Ошибка логина");

        // Сохраняем токен и userId
        authToken = response.getBody().path("token");
        userId = response.getBody().path("userId");

        // Если поля называются иначе, попробуй:
        // authToken = response.getBody().path("accessToken");
        // userId = response.getBody().path("id");

        LogUtil.info("Успешный логин! userId: " + userId);
        assertNotNull(authToken, "Токен не должен быть null");
    }

   // no test

    @Test
    public void testGetReviews_ShouldReturn200() {
        LogUtil.step("Получение всех отзывов");

        Response response = RestAssured
                .when()
                .get("/api/reviews")
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 200);
        LogUtil.info("✅ Отзывы получены");
    }
}
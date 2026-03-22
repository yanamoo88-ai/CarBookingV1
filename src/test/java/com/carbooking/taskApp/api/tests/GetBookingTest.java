package com.carbooking.taskApp.api.tests;

import com.carbooking.api.dto.BookingRequestDto;
import com.carbooking.dto.ReviewDto;
import com.carbooking.taskApp.api.config.BaseApiTest;
import com.carbooking.taskApp.utils.LogUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.testng.Assert.*;

public class GetBookingTest extends BaseApiTest {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    // Вспомогательный метод для создания бронирования
    private int createBooking() {
        BookingRequestDto booking = BookingRequestDto.builder()
                .userId(1)
                .carId(1)
                .startDate(LocalDate.now().plusDays(1).atStartOfDay().format(formatter))
                .endDate(LocalDate.now().plusDays(3).atStartOfDay().format(formatter))
                .build();

        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body(booking)
                .when()
                .post("/api/bookings")
                .then()
                .extract()
                .response();

        return response.getBody().path("id");
    }

    @Test
    public void testCreateReview_ValidData_ShouldReturn201() {
        LogUtil.step("Создание отзыва");

        // Создаем бронирование и получаем его ID
        int bookingId = createBooking();
        LogUtil.info("Создано бронирование с ID: " + bookingId);

        // Создаем отзыв на это бронирование
        ReviewDto review = ReviewDto.builder()
                .userId(1)
                //.bookingId(bookingId) // используем bookingId, не carId!
                .rating(5)
                .content("Отлично!")
                .build();

        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body(review)
                .when()
                .post("/api/reviews")
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        LogUtil.jsonBody(response.getBody().asString());

        assertEquals(response.getStatusCode(), 201, "Должен быть 201 Created");
        LogUtil.info("✅ Отзыв создан успешно");
    }

    // Тест без обязательных полей
    @Test
    public void testCreateReview_WithoutBookingId_ShouldReturn400() {
        LogUtil.step("Создание отзыва без bookingId");

        ReviewDto review = ReviewDto.builder()
                .userId(1)
                .rating(5)
                .content("Отлично!")
                .build();

        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body(review)
                .when()
                .post("/api/reviews")
                .then()
                .extract()
                .response();

        assertEquals(response.getStatusCode(), 400);
        LogUtil.info("✅ API требует bookingId");
   }
    @Test
    public void testGetBookingById_ExistingBooking_ShouldReturn200() {
        LogUtil.step("ШАГ 1: Создаем бронирование для теста");

        // Сначала создаем бронирование
        BookingRequestDto newBooking = BookingRequestDto.builder()
                .userId(1)
                .carId(1)
                .startDate(LocalDate.now().plusDays(5).atStartOfDay().format(formatter))
                .endDate(LocalDate.now().plusDays(7).atStartOfDay().format(formatter))
                .build();

        // Создаем и получаем ID
        int bookingId = RestAssured
                .given()
                .body(newBooking)
                .when()
                .post("/bookings")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        LogUtil.info("Создано бронирование с ID: " + bookingId);

        LogUtil.step("ШАГ 2: Получаем бронирование по ID");

        // ВАЖНО: правильно конкатенируем URL
        Response response = RestAssured
                .when()
                .get("/bookings/" + bookingId) //
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        LogUtil.jsonBody(response.getBody().asString());

        // Правильный assert
        assertEquals(response.getStatusCode(), 200, "Должен быть 200 OK");
        assertNotNull(response.getBody().path("id"), "ID не найден в ответе");
        LogUtil.info("✅ Бронирование успешно получено");
    }

    @Test
    public void testGetBookingById_NonExistingId_ShouldReturn404() {
        LogUtil.step("Тест: запрос несуществующего бронирования");

        int nonExistingId = 999999;

        Response response = RestAssured
                .when()
                .get("/bookings/" + nonExistingId)
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());

        assertEquals(response.getStatusCode(), 404,
                "Должен быть 404 Not Found");
        LogUtil.info("✅ API вернул 404 - правильно");
    }
}
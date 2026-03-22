package com.carbooking.taskApp.api.tests;

import com.carbooking.taskApp.api.config.BaseApiTest;
import com.carbooking.api.dto.BookingRequestDto; // Импортируем твой DTO
import com.carbooking.taskApp.utils.LogUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
//import com.carbooking.;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static org.testng.Assert.*;

public class BookingsCreateTest extends BaseApiTest {

    // Формат для даты с временем "YYYY-MM-DD HH:mm:ss"
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    public void testCreateBooking_WithMyDto_ShouldReturn201() {
        LogUtil.step("Создание бронирования с твоим DTO");

        // Создаем DTO через билдер (работает благодаря Lombok)
        BookingRequestDto booking = BookingRequestDto.builder()
                .userId(1)
                .carId(1)
                .startDate(LocalDate.now().plusDays(1).atStartOfDay().format(formatter))
                .endDate(LocalDate.now().plusDays(3).atStartOfDay().format(formatter))
                .build();

        LogUtil.info("Данные для бронирования:");
        LogUtil.param("userId", booking.getUserId());
        LogUtil.param("carId", booking.getCarId());
        LogUtil.param("startDate", booking.getStartDate());
        LogUtil.param("endDate", booking.getEndDate());

        LogUtil.step("Отправляем POST запрос на /api/v1/bookings");

        Response response = RestAssured
                .given()
                .body(booking) // Lombok + Jackson превратят в JSON
                .when()
                .post("/bookings") // или "/api/v1/bookings" - зависит от basePath
                .then()
                .extract()
                .response();

        int statusCode = response.getStatusCode();
        LogUtil.apiResponse(statusCode);

        // При успешном создании обычно 201 Created
        assertTrue(statusCode == 201 || statusCode == 200,
                "Ожидался 201 или 200, получили: " + statusCode);

        String responseBody = response.getBody().asString();
        LogUtil.jsonBody(responseBody);

        LogUtil.info("✅ Бронирование создано успешно!");
    }

    @Test
    public void testCreateBooking_WithoutUserId_ShouldReturn400() {
        LogUtil.step("Тест: создание бронирования без userId");

        // Создаем DTO без userId (он будет null)
        BookingRequestDto booking = BookingRequestDto.builder()
                .carId(1)
                .startDate(LocalDate.now().plusDays(1).atStartOfDay().format(formatter))
                .endDate(LocalDate.now().plusDays(3).atStartOfDay().format(formatter))
                .build();

        LogUtil.warn("Отправка без userId");

        Response response = RestAssured
                .given()
                .body(booking)
                .when()
                .post("/bookings")
                .then()
                .extract()
                .response();

        int statusCode = response.getStatusCode();
        LogUtil.apiResponse(statusCode);

        assertEquals(statusCode, 400, "Должен быть 400 Bad Request");
        LogUtil.info("✅ API вернул 400 - правильно");
    }

    @Test
    public void testCreateBooking_CarAlreadyBooked_ShouldReturnError() {
        LogUtil.step("Тест: бронирование уже занятой машины");

        // Сначала создаем первое бронирование
        BookingRequestDto firstBooking = BookingRequestDto.builder()
                .userId(1)
                .carId(1)
                .startDate(LocalDate.now().plusDays(1).atStartOfDay().format(formatter))
                .endDate(LocalDate.now().plusDays(3).atStartOfDay().format(formatter))
                .build();

        LogUtil.info("Создаем первое бронирование");
        RestAssured
                .given()
                .body(firstBooking)
                .when()
                .post("/bookings")
                .then()
                .statusCode(201);

        LogUtil.step("Пытаемся забронировать ту же машину на те же даты");

        // Пытаемся забронировать снова
        Response response = RestAssured
                .given()
                .body(firstBooking) // Те же данные
                .when()
                .post("/bookings")
                .then()
                .extract()
                .response();

        int statusCode = response.getStatusCode();
        LogUtil.apiResponse(statusCode);

        // Ожидаем конфликт (409 Conflict) или ошибку валидации (400)
        assertTrue(statusCode == 409 || statusCode == 400,
                "Ожидался код 409 или 400, получили: " + statusCode);

        LogUtil.info("✅ API не дал забронировать уже занятую машину");
    }

    @Test
    public void testCreateBooking_WithoutAuth_ShouldReturn401() {
        LogUtil.step("Тест: бронирование без авторизации");

        BookingRequestDto booking = BookingRequestDto.builder()
                .userId(1)
                .carId(1)
                .startDate(LocalDate.now().plusDays(1).atStartOfDay().format(formatter))
                .endDate(LocalDate.now().plusDays(3).atStartOfDay().format(formatter))
                .build();

        // Отправляем без токена авторизации
        Response response = RestAssured
                .given()
                .body(booking)
                .auth().none() // явно убираем авторизацию
                .when()
                .post("/bookings")
                .then()
                .extract()
                .response();

        int statusCode = response.getStatusCode();
        LogUtil.apiResponse(statusCode);

        assertEquals(statusCode, 401, "Должен быть 401 Unauthorized");
        LogUtil.info("✅ API требует авторизацию - правильно");
    }

    @Test
    public void testCreateBooking_InvalidDateRange_EndDateBeforeStart_ShouldReturn400() {
        LogUtil.step("Тест: дата окончания раньше даты начала");

        BookingRequestDto booking = BookingRequestDto.builder()
                .userId(1)
                .carId(1)
                .startDate(LocalDate.now().plusDays(3).atStartOfDay().format(formatter))
                .endDate(LocalDate.now().plusDays(1).atStartOfDay().format(formatter)) // Раньше!
                .build();

        Response response = RestAssured
                .given()
                .body(booking)
                .when()
                .post("/bookings")
                .then()
                .extract()
                .response();

        assertEquals(response.getStatusCode(), 400,
                "Неверный диапазон дат должен вернуть 400");
    }

    @Test
    public void testCreateBooking_PastDates_ShouldReturn400() {
        LogUtil.step("Тест: бронирование на прошедшие даты");

        BookingRequestDto booking = BookingRequestDto.builder()
                .userId(1)
                .carId(1)
                .startDate(LocalDate.now().minusDays(2).atStartOfDay().format(formatter)) // В прошлом
                .endDate(LocalDate.now().plusDays(1).atStartOfDay().format(formatter))
                .build();

        Response response = RestAssured
                .given()
                .body(booking)
                .when()
                .post("/bookings")
                .then()
                .extract()
                .response();

        assertEquals(response.getStatusCode(), 400,
                "Бронирование в прошлом невозможно");
    }
}
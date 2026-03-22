package com.carbooking.taskApp.api.tests;

import com.carbooking.api.dto.BookingRequestDto;
import com.carbooking.api.dto.ReviewRequestDto;
import com.carbooking.taskApp.api.config.BaseApiTest;
import com.carbooking.taskApp.utils.LogUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class BookingApiTest extends BaseApiTest {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // ============= US-01: CREATE BOOKING =============

    @Test
    public void testCreateBooking_ValidData_ShouldReturn201() {
        LogUtil.step("US-01: Создание бронирования с валидными данными");

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

        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body(booking)
                .when()
                .post("/api/bookings")
                .then()
                .extract()
                .response();

        int statusCode = response.getStatusCode();
        LogUtil.apiResponse(statusCode);
        LogUtil.jsonBody(response.getBody().asString());

        assertTrue(statusCode == 201 || statusCode == 200,
                "Ожидался 201 или 200, получили: " + statusCode);

        assertNotNull(response.getBody().path("id"), "Должен вернуться ID бронирования");
        LogUtil.info("✅ Бронирование создано успешно");
    }

    @Test
    public void testCreateBooking_WithoutUserId_ShouldReturn400() {
        LogUtil.step("Негативный: создание бронирования без userId");

        BookingRequestDto booking = BookingRequestDto.builder()
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

        LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 400, "Должен быть 400 Bad Request");
        LogUtil.info("✅ API вернул 400 - правильно");
    }

    @Test
    public void testCreateBooking_InvalidDateRange_ShouldReturn400() {
        LogUtil.step("Негативный: дата окончания раньше даты начала");

        BookingRequestDto booking = BookingRequestDto.builder()
                .userId(1)
                .carId(1)
                .startDate(LocalDate.now().plusDays(3).atStartOfDay().format(formatter))
                .endDate(LocalDate.now().plusDays(1).atStartOfDay().format(formatter)) // раньше!
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

        LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 400, "Неверный диапазон дат должен вернуть 400");
        LogUtil.info("✅ API проверяет корректность дат");
    }

    @Test
    public void testCreateBooking_PastDates_ShouldReturn400() {
        LogUtil.step("Негативный: бронирование на прошедшие даты");

        BookingRequestDto booking = BookingRequestDto.builder()
                .userId(1)
                .carId(1)
                .startDate(LocalDate.now().minusDays(2).atStartOfDay().format(formatter)) // в прошлом
                .endDate(LocalDate.now().plusDays(1).atStartOfDay().format(formatter))
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

        LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 400, "Бронирование в прошлом невозможно");
        LogUtil.info("✅ API не дает бронировать на прошедшие даты");
    }

    // ============= US-02: GET BOOKING =============

    @Test
    public void testGetBookingById_ExistingBooking_ShouldReturn200() {
        LogUtil.step("US-02: Получение существующего бронирования");

        // Сначала создаем бронирование
        BookingRequestDto newBooking = BookingRequestDto.builder()
                .userId(1)
                .carId(1)
                .startDate(LocalDate.now().plusDays(10).atStartOfDay().format(formatter))
                .endDate(LocalDate.now().plusDays(12).atStartOfDay().format(formatter))
                .build();

        int bookingId = RestAssured
                .given()
                .contentType("application/json")
                .body(newBooking)
                .when()
                .post("/api/bookings")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        LogUtil.info("Создано бронирование с ID: " + bookingId);

        // Получаем бронирование по ID
        Response response = RestAssured
                .when()
                .get("/api/bookings/" + bookingId)
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        LogUtil.jsonBody(response.getBody().asString());

        assertEquals(response.getStatusCode(), 200, "Должен быть 200 OK");
        assertNotNull(response.getBody().path("id"), "В ответе должен быть id");
        LogUtil.info("✅ Бронирование успешно получено");
    }

    @Test
    public void testGetBookingById_NonExistingId_ShouldReturn404() {
        LogUtil.step("Негативный: запрос несуществующего бронирования");

        int nonExistingId = 999999;

        Response response = RestAssured
                .when()
                .get("/api/bookings/" + nonExistingId)
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 404, "Для несуществующего ID должен быть 404");
        LogUtil.info("✅ API вернул 404 - правильно");
    }

    @Test
    public void testGetBookingById_InvalidIdFormat_ShouldReturn400() {
        LogUtil.step("Негативный: запрос с некорректным форматом ID");

        Response response = RestAssured
                .when()
                .get("/api/bookings/abc123") // не число
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertTrue(response.getStatusCode() == 400 || response.getStatusCode() == 404,
                "Для некорректного ID должен быть 400 или 404");
        LogUtil.info("✅ API обработал неверный формат ID");
    }

    // ============= US-03: UPDATE BOOKING =============

    @Test
    public void testUpdateBooking_ChangeDates_ShouldReturn200() {
        LogUtil.step("US-03: Обновление дат бронирования");

        // Создаем бронирование
        BookingRequestDto newBooking = BookingRequestDto.builder()
                .userId(1)
                .carId(1)
                .startDate(LocalDate.now().plusDays(15).atStartOfDay().format(formatter))
                .endDate(LocalDate.now().plusDays(17).atStartOfDay().format(formatter))
                .build();

        int bookingId = RestAssured
                .given()
                .contentType("application/json")
                .body(newBooking)
                .when()
                .post("/api/bookings")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        LogUtil.info("Создано бронирование ID: " + bookingId);

        // Обновляем даты
        BookingRequestDto updatedBooking = BookingRequestDto.builder()
                .userId(1)
                .carId(1)
                .startDate(LocalDate.now().plusDays(20).atStartOfDay().format(formatter))
                .endDate(LocalDate.now().plusDays(22).atStartOfDay().format(formatter))
                .build();

        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body(updatedBooking)
                .when()
                .put("/api/bookings/" + bookingId)
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 200, "Обновление должно быть успешным");
        LogUtil.info("✅ Бронирование обновлено");
    }

    @Test
    public void testUpdateBooking_NonExistingId_ShouldReturn404() {
        LogUtil.step("Негативный: обновление несуществующего бронирования");

        BookingRequestDto updatedBooking = BookingRequestDto.builder()
                .userId(1)
                .carId(1)
                .startDate(LocalDate.now().plusDays(15).atStartOfDay().format(formatter))
                .endDate(LocalDate.now().plusDays(17).atStartOfDay().format(formatter))
                .build();

        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body(updatedBooking)
                .when()
                .put("/api/bookings/999999")
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 404, "Должен быть 404");
        LogUtil.info("✅ 404 - правильно");
    }

    // ============= US-04: DELETE BOOKING =============

    @Test
    public void testDeleteBooking_ExistingBooking_ShouldReturn200() {
        LogUtil.step("US-04: Отмена бронирования");

        // Создаем бронирование
        BookingRequestDto newBooking = BookingRequestDto.builder()
                .userId(1)
                .carId(1)
                .startDate(LocalDate.now().plusDays(25).atStartOfDay().format(formatter))
                .endDate(LocalDate.now().plusDays(27).atStartOfDay().format(formatter))
                .build();

        int bookingId = RestAssured
                .given()
                .contentType("application/json")
                .body(newBooking)
                .when()
                .post("/api/bookings")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        LogUtil.info("Создано бронирование ID: " + bookingId);

        // Удаляем бронирование
        Response response = RestAssured
                .when()
                .delete("/api/bookings/" + bookingId)
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 204,
                "Ожидается 200 или 204, получено: " + response.getStatusCode());

        // Проверяем, что бронирование действительно удалено
        Response checkResponse = RestAssured
                .when()
                .get("/api/bookings/" + bookingId)
                .then()
                .extract()
                .response();

        assertEquals(checkResponse.getStatusCode(), 404, "После удаления должен быть 404");
        LogUtil.info("✅ Бронирование успешно отменено");
    }

    @Test
    public void testDeleteBooking_NonExistingId_ShouldReturn404() {
        LogUtil.step("Удаление несуществующего бронирования");

        Response response = RestAssured
                .when()
                .delete("/api/bookings/999999")
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 404, "Должен быть 404");
        LogUtil.info("✅ 404 - правильно");
    }

    // ============= US-05: LIST BOOKINGS =============

    @Test
    public void testGetAllBookings_ShouldReturn200() {
        LogUtil.step("US-05: Получение списка всех бронирований");

        Response response = RestAssured
                .when()
                .get("/api/bookings")
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 200, "Должен быть 200 OK");

        List<Map<String, Object>> bookings = response.getBody().jsonPath().getList("$");
        LogUtil.info("Найдено бронирований: " + (bookings != null ? bookings.size() : 0));
        LogUtil.info("✅ Список бронирований получен");
    }

    @Test
    public void testGetBookings_ByUserId_ShouldReturnFilteredList() {
        LogUtil.step("Получение бронирований по пользователю");

        int userId = 1;

        Response response = RestAssured
                .when()
                .get("/api/bookings?userId=" + userId)
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 200);

        List<Map<String, Object>> bookings = response.getBody().jsonPath().getList("$");
        LogUtil.info("Найдено бронирований для userId=" + userId + ": " +
                (bookings != null ? bookings.size() : 0));

        if (bookings != null && !bookings.isEmpty()) {
            for (Map<String, Object> booking : bookings) {
                assertEquals(booking.get("userId"), userId,
                        "Все бронирования должны быть для userId " + userId);
            }
        }
        LogUtil.info("✅ Фильтр по userId работает");
    }
}
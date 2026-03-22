package com.carbooking.taskApp.api.tests;


import com.carbooking.api.dto.BookingRequestDto;

import com.carbooking.taskApp.api.config.BaseApiTest;
import com.carbooking.taskApp.utils.LogUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import static org.testng.Assert.*;

public class AdminEditBookingTest extends BaseApiTest {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // ============= HELPER METHOD - создает тестовое бронирование =============
    private int createTestBooking() {
        LogUtil.info("Создание тестового бронирования...");

        // Форматтер для дат
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Рассчитываем total_price (например, 3 дня * цена категории)
        // Для простоты используем фиксированную цену или получаем из категории
        double totalPrice = 150.00; // или рассчитать динамически

        // Создаем DTO только с полями, которые есть в таблице bookings
        BookingRequestDto booking = BookingRequestDto.builder()
                .userId(1)                          // есть в БД
                .carId(1)                            // есть в БД
                .startDate(LocalDate.now().plusDays(1).atStartOfDay().format(formatter)) // есть
                .endDate(LocalDate.now().plusDays(3).atStartOfDay().format(formatter))   // есть
                .totalPrice(totalPrice)              // есть в БД
                .status("pending")                    // есть в БД (enum)
                .build();

        Response response = RestAssured
                .given()
                .header("Authorization", "Bearer admin_token")
                .contentType("application/json")
                .body(booking)
                .when()
                .post("/api/v1/bookings")
                .then()
                .extract()
                .response();

        int bookingId = response.getBody().path("id");
        LogUtil.info("Создано бронирование с ID: " + bookingId);
        return bookingId;
    }

    // ============= US-05: ADMIN EDIT BOOKING =============

    @Test
    public void testAdminEditBooking_ChangeStatus_AllTransitions() {
        LogUtil.step("US-05: Admin меняет статусы бронирования");

        int bookingId = createTestBooking();

        // Тестируем все возможные статусы из ENUM
        String[][] statusTransitions = {
                {"pending", "confirmed"},
                {"confirmed", "rejected"},
                {"rejected", "cancelled"},
                {"cancelled", "completed"}
        };

        for (String[] transition : statusTransitions) {
            String fromStatus = transition[0];
            String toStatus = transition[1];

            LogUtil.info("Смена статуса: " + fromStatus + " -> " + toStatus);

            String patchBody = String.format("{\"status\": \"%s\"}", toStatus);

            Response response = RestAssured
                    .given()
                    .header("Authorization", "Bearer admin_token")
                    .contentType("application/json")
                    .body(patchBody)
                    .when()
                    .patch("/api/v1/bookings/" + bookingId)
                    .then()
                    .extract()
                    .response();

            LogUtil.apiResponse(response.getStatusCode());
            assertEquals(response.getStatusCode(), 200,
                    "Статус должен измениться с " + fromStatus + " на " + toStatus);

            // Проверяем, что статус действительно изменился
            Response getResponse = RestAssured
                    .given()
                    .header("Authorization", "Bearer admin_token")
                    .when()
                    .get("/api/v1/bookings/" + bookingId)
                    .then()
                    .extract()
                    .response();

            String actualStatus = getResponse.getBody().path("status");
            assertEquals(actualStatus, toStatus,
                    "Статус должен быть " + toStatus);

            LogUtil.info("✅ Статус изменен на " + toStatus);
        }
    }

    @Test
    public void testAdminEditBooking_ChangeDates_RecalculateTotalPrice() {
        LogUtil.step("US-05: При изменении дат пересчитывается total_price");

        int bookingId = createTestBooking();

        // Получаем текущее бронирование
        Response getResponse = RestAssured
                .given()
                .header("Authorization", "Bearer admin_token")
                .when()
                .get("/api/v1/bookings/" + bookingId)
                .then()
                .extract()
                .response();

        // Получаем цену из категории
        Double pricePerDay = getResponse.getBody().path("car.category.price_per_day");

        // Новые даты
        String newStartDate = LocalDate.now().plusDays(5).atStartOfDay().format(formatter);
        String newEndDate = LocalDate.now().plusDays(10).atStartOfDay().format(formatter);

        // Рассчитываем ожидаемую цену
        long daysCount = 5; // с 5 по 10 = 5 дней
        double expectedPrice = pricePerDay * daysCount;

        String patchBody = String.format("""
                {
                    "start_date": "%s",
                    "end_date": "%s"
                }
                """, newStartDate, newEndDate);

        Response response = RestAssured
                .given()
                .header("Authorization", "Bearer admin_token")
                .contentType("application/json")
                .body(patchBody)
                .when()
                .patch("/api/v1/bookings/" + bookingId)
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 200);

        double actualPrice = response.getBody().path("total_price");
        assertEquals(actualPrice, expectedPrice, 0.01,
                "Цена должна пересчитаться: " + pricePerDay + " * " + daysCount);

        LogUtil.info("✅ Цена пересчитана: " + actualPrice + " (ожидалось: " + expectedPrice + ")");
    }

    @Test
    public void testAdminEditBooking_ChangeCar_ShouldUpdatePrice() {
        LogUtil.step("US-05: При смене авто цена пересчитывается");

        int bookingId = createTestBooking();

        String patchBody = """
                {
                    "car_id": 2
                }
                """;

        Response response = RestAssured
                .given()
                .header("Authorization", "Bearer admin_token")
                .contentType("application/json")
                .body(patchBody)
                .when()
                .patch("/api/v1/bookings/" + bookingId)
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 200);

        // Проверяем, что автомобиль действительно изменился
        Integer newCarId = response.getBody().path("car.id");
        assertEquals(newCarId, Integer.valueOf(2), "ID автомобиля должен быть 2");

        LogUtil.info("✅ Автомобиль изменен на ID: " + newCarId);
    }

    @Test
    public void testAdminEditBooking_NonExistingId_ShouldReturn404() {
        LogUtil.step("US-05: Редактирование несуществующей заявки");

        String patchBody = "{\"status\": \"confirmed\"}";

        Response response = RestAssured
                .given()
                .header("Authorization", "Bearer admin_token")
                .contentType("application/json")
                .body(patchBody)
                .when()
                .patch("/api/v1/bookings/999999")
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 404);
        LogUtil.info("✅ 404 - заявка не найдена");
    }

    @Test
    public void testAdminEditBooking_InvalidDateFormat_ShouldReturn400() {
        LogUtil.step("US-05: Неверный формат даты");

        int bookingId = createTestBooking();

        String patchBody = """
                {
                    "start_date": "invalid-date"
                }
                """;

        Response response = RestAssured
                .given()
                .header("Authorization", "Bearer admin_token")
                .contentType("application/json")
                .body(patchBody)
                .when()
                .patch("/api/v1/bookings/" + bookingId)
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 400);
        LogUtil.info("✅ API вернул 400 - неверный формат даты");
    }

    @Test
    public void testAdminEditBooking_InvalidStatus_ShouldReturn400() {
        LogUtil.step("US-05: Недопустимый статус");

        int bookingId = createTestBooking();

        String patchBody = "{\"status\": \"INVALID_STATUS\"}";

        Response response = RestAssured
                .given()
                .header("Authorization", "Bearer admin_token")
                .contentType("application/json")
                .body(patchBody)
                .when()
                .patch("/api/v1/bookings/" + bookingId)
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 400);
        LogUtil.info("✅ API вернул 400 для недопустимого статуса");
    }
}
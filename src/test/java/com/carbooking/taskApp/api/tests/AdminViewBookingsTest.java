package com.carbooking.taskApp.api.tests;

import com.carbooking.taskApp.api.config.BaseApiTest;
import com.carbooking.taskApp.utils.LogUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.List;
import java.util.Map;
import static org.testng.Assert.*;

public class AdminViewBookingsTest extends BaseApiTest {

    @Test
    public void testAdminViewAllBookings_ShouldReturnFullDetails() {
        LogUtil.step("US-04: Admin просматривает все заявки с деталями");

        Response response = RestAssured
                .given()
                .header("Authorization", "Bearer admin_token")
                .when()
                .get("/api/v1/bookings")
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 200);

        List<Map<String, Object>> bookings = response.getBody().jsonPath().getList("$");
        LogUtil.info("Всего заявок: " + (bookings != null ? bookings.size() : 0));

        if (bookings != null && !bookings.isEmpty()) {
            // Проверяем структуру ответа (должны быть данные из связанных таблиц)
            Map<String, Object> firstBooking = bookings.get(0);

            // Данные из bookings
            assertNotNull(firstBooking.get("id"));
            assertNotNull(firstBooking.get("status"));
            assertNotNull(firstBooking.get("total_price"));
            assertNotNull(firstBooking.get("start_date"));
            assertNotNull(firstBooking.get("end_date"));

            // Данные из users (join)
            Map<String, Object> user = (Map<String, Object>) firstBooking.get("user");
            assertNotNull(user.get("full_name"));
            assertNotNull(user.get("email"));
            assertNotNull(user.get("phone"));

            // Данные из cars + category (join)
            Map<String, Object> car = (Map<String, Object>) firstBooking.get("car");
            assertNotNull(car.get("brand"));
            assertNotNull(car.get("model"));
            assertNotNull(car.get("number_plate"));

            Map<String, Object> category = (Map<String, Object>) car.get("category");
            assertNotNull(category.get("name"));
            assertNotNull(category.get("price_per_day"));

            LogUtil.info("✅ Админ видит полную информацию:");
            LogUtil.param("Клиент", user.get("full_name") + " (" + user.get("email") + ")");
            LogUtil.param("Авто", car.get("brand") + " " + car.get("model") + " [" + car.get("number_plate") + "]");
            LogUtil.param("Категория", category.get("name"));
            LogUtil.param("Статус", firstBooking.get("status"));
            LogUtil.param("Сумма", firstBooking.get("total_price") + " ₽");
        }
    }

    @Test
    public void testAdminViewBookings_FilterByStatus_ShouldReturnFilteredList() {
        LogUtil.step("Фильтрация заявок по статусу");

        String[] statuses = {"pending", "confirmed", "rejected", "cancelled", "completed"};

        for (String status : statuses) {
            Response response = RestAssured
                    .given()
                    .header("Authorization", "Bearer admin_token")
                    .queryParam("status", status)
                    .when()
                    .get("/api/v1/bookings")
                    .then()
                    .extract()
                    .response();

            LogUtil.apiResponse(response.getStatusCode());
            assertEquals(response.getStatusCode(), 200);

            List<Map<String, Object>> bookings = response.getBody().jsonPath().getList("$");

            if (bookings != null && !bookings.isEmpty()) {
                for (Map<String, Object> booking : bookings) {
                    assertEquals(booking.get("status"), status,
                            "Все заявки должны иметь статус " + status);
                }
                LogUtil.info("✅ Найдено " + bookings.size() + " заявок со статусом " + status);
            } else {
                LogUtil.info("ℹ️ Нет заявок со статусом " + status);
            }
        }
    }
}
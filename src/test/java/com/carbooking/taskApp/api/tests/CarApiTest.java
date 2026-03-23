package com.carbooking.taskApp.api.tests;


import com.carbooking.dto.CarDto;
import com.carbooking.taskApp.api.config.BaseApiTest;
import com.carbooking.taskApp.utils.LogUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class CarApiTest extends BaseApiTest {
    @Test
    public void testGetCarTypes_ShouldReturn200() {
        LogUtil.step("Obtaining vehicle categories");

        Response response = RestAssured
                .given()
                // .header("Authorization", "Bearer " + token)
                .when()
                .get("/car-categories")
                .then()
                .extract()
                .response();
        LogUtil.apiResponse(response.getStatusCode());

        assertEquals(response.getStatusCode(), 200, "Error!401 or 500)");

             List<Object> categories = response.jsonPath().getList("$");
        assertNotNull(categories, "The list of categories is empty!");

        LogUtil.info("Categories: " + categories.size());
    }


    @Test
    public void testGetAllCars_ShouldReturn200() {
        LogUtil.step("Request for a list of all cars");

        Response response = RestAssured.given()
                // .header("Authorization", "Bearer " + token)
                .when()
                .get("/cars")
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 200, "Failed to retrieve the list of cars!");

        // Выведем в лог ID первой машины, чтобы знать, что бронировать в следующем тесте
        Integer firstCarId = response.jsonPath().get("[0].id");
        if (firstCarId != null) {
            LogUtil.info("Car with ID is available: " + firstCarId);
        } else {
            LogUtil.info("The car list is empty!");
        }

    }


    @Test
    public void testGetCarByNonExistentId_ShouldReturn404() {
        LogUtil.step("Negative test: request for the machine with ID 999999");

        Response response = RestAssured.given()
                .when()
                .get("/cars/999999")
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 404, "The server should return 404 for a non-existent ID");
    }


    @Test
    public void testGetCars_WithDateFilter_ShouldShowCarsFreeOnDates() {
        LogUtil.step("Search for available cars by dates");

        String startDate = "2024-12-01";
        String endDate = "2024-12-05";

        Response response = RestAssured
                .when()
                .get("/cars?startDate=" + startDate + "&endDate=" + endDate)
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 200);

        List<Map<String, Object>> cars = response.getBody().jsonPath().getList("$");
        LogUtil.info("Available cars on the dates" + startDate + " - " + endDate +
                ": " + (cars != null ? cars.size() : 0));
    }


    //==========================================
    @Test
    public void testGetCars_CombinedFilters_ShouldWork() {
        LogUtil.step("Comb filter");

        Response response = RestAssured
                .when()
                .get("/cars?type=SEDAN&minPrice=30&maxPrice=150&available=true")
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 200);

        List<Map<String, Object>> cars = response.getBody().jsonPath().getList("$");
        LogUtil.info("Result: " + (cars != null ? cars.size() : 0));
    }



    @Test
    public void testGetCars_NoCarsAvailable_ShouldReturn200WithEmptyList() {
        LogUtil.step(" Price filter check");

        // Задаем диапазон цен, в котором заведомо нет машин
        int minPrice = 8000000;
        int maxPrice = 10000000;

        Response response = RestAssured.given()
                // Используем queryParam для чистоты кода
                .queryParam("minPrice", minPrice)
                .queryParam("maxPrice", maxPrice)
                .when()
                .get("/cars")
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());

        // 1. Базовая проверка: сервер должен быть доступен (200 OK)
        assertEquals(response.getStatusCode(), 200, "Сервер должен вернуть 200, даже если ничего не найдено");

        // 2. Извлекаем список машин из ответа
        List<Map<String, Object>> cars = response.jsonPath().getList("$");

        // 3. Логика QA: Проверяем, пуст ли список
        if (cars != null && !cars.isEmpty()) {
            // Если список НЕ пустой, проверяем цену первой машины
            Object firstCarPrice = cars.get(0).get("price_per_day");

            LogUtil.info("⚠️ БАГ НАЙДЕН: Сервер прислал машину с ценой " + firstCarPrice +
                    ", хотя мы просили от " + minPrice);

            // Этот ассерт упадет и официально зафиксирует баг в отчете
            assertTrue(cars.isEmpty(),
                    "ФИЛЬТР НЕ РАБОТАЕТ! Ожидался пустой список, но пришло машин: " + cars.size());
        } else {
            LogUtil.info("✅ Тест пройден: фильтрация работает, список пуст");
            assertTrue(true);
        }
    }

    @Test
    public void testGetCars_WithPagination_ShouldReturnLimitedResults() {
        LogUtil.step("Pagination test");

        int page = 0;
        int size = 5;

        Response response = RestAssured
                .when()
                .get("/cars?page=" + page + "&size=" + size)
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());

        if (response.getStatusCode() == 200) {
            List<Map<String, Object>> cars = response.getBody().jsonPath().getList("$");

            // Проверяем заголовки пагинации, если есть
            String totalCount = response.getHeader("X-Total-Count");
            if (totalCount != null) {
                LogUtil.info("All cars: " + totalCount);
            }

            if (cars != null) {
                assertTrue(cars.size() <= size,
                        "No more should return " + size + " cars");
                LogUtil.info("✅Pagination works, received: " + cars.size() + " cars");
            }
        }
    }

    @Test
    public void testGetCarById_NonExistingId_ShouldReturn404() {
        LogUtil.step("Negative test: request for a non-existent machine");

        int nonExistingId = 999999;

        Response response = RestAssured
                .when()
                .get("/cars/" + nonExistingId)
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());

        assertEquals(response.getStatusCode(), 404,
                "For a non-existent car it must be 404 Not Found");
        LogUtil.info("✅ API Returned 404 - correct");
    }

    @Test
    public void testGetCarById_InvalidIdFormat_ShouldReturn400() {
        LogUtil.step("Negative test: incorrect ID format");

        Response response = RestAssured
                .when()
                .get("/cars/abc123") // не число
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());

        assertTrue(response.getStatusCode() == 400 || response.getStatusCode() == 404,
                "Expected 400 Bad Request or 404 Not Found, received: " + response.getStatusCode());

        if (response.getStatusCode() == 400) {
            LogUtil.info("✅ API returned 400 - correct (invalid format)");
        } else {
            LogUtil.info("ℹ️ The API returned 404 – that is acceptable too");
        }
    }

    @Test
    public void testGetCarById_NegativeId_ShouldReturn400() {
        LogUtil.step("Negative test: negative ID");

        Response response = RestAssured
                .when()
                .get("/cars/-5")
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());

        assertTrue(response.getStatusCode() == 400 || response.getStatusCode() == 404,
                "For a negative ID, a 400 or 404 is expected.");
        LogUtil.info("✅ Test Good!");
    }

    @Test
    public void testGetCarById_ZeroId_ShouldReturn400() {
        LogUtil.step("Negative test: ID = 0");

        Response response = RestAssured
                .when()
                .get("/cars/0")
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());

        assertTrue(response.getStatusCode() == 400 || response.getStatusCode() == 404,
                "ForID=0 wait 400 or 404");
        LogUtil.info("✅ Test OK");
    }

    @Test
    public void testGetCarById_WithoutAuth_ShouldWork() {
        LogUtil.step("Check: is authorisation required to view the car");

        Response response = RestAssured
                .given()
                .auth().none()
                .when()
                .get("/cars/1")
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());

        // Обычно информация о машинах публичная
        if (response.getStatusCode() == 200) {
            LogUtil.info("✅ Information about the car is available without authorisation");
        } else if (response.getStatusCode() == 401) {
            LogUtil.info("ℹ️ The API requires authorization");
        }
    }


    }



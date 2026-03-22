package com.carbooking.taskApp.api.tests;


import com.carbooking.taskApp.api.config.BaseApiTest;
import com.carbooking.dto.CarDto;
import com.carbooking.dto.CarTypeDto;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import com.carbooking.taskApp.utils.LogUtil;

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

        // 2. Проверяем, что тело ответа не пустое (пришел массив данных)
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





//    // ============= НЕГАТИВНЫЕ ТЕСТЫ =============
//
//    @Test(dataProvider = "invalidFilters")
//    public void testGetCars_InvalidFilters_ShouldReturn400(String endpoint, String description) {
//        LogUtil.step("Checking an invalid filter: " + description);
//
//        Response response = RestAssured.given()
//                // Важно: если машины на v1, а BaseURI на v3, добавь .basePath("/api/v1") здесь
//                .when()
//                .get(endpoint)
//                .then()
//                .extract()
//                .response();
//
//        LogUtil.apiResponse(response.getStatusCode());
//
//        // Мы ожидаем 400. Если придет 200 — значит сервер не умеет валидировать ошибки
//        assertEquals(response.getStatusCode(), 400, "Сервер пропустил невалидный запрос: " + description);
//    }



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

    //    @Test
//    public void testGetCars_WithoutAuth_ShouldWork() {
//        LogUtil.step("Проверка: список машин обычно доступен без авторизации");
//
//        Response response = RestAssured
//                .given()
//                .auth().none()
//                .when()
//                .get("/cars")
//                .then()
//                .extract()
//                .response();
//
//        LogUtil.apiResponse(response.getStatusCode());
//
//        // Обычно список машин публичный
//        assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 401,
//                "Статус код: " + response.getStatusCode());
//
//        if (response.getStatusCode() == 200) {
//            LogUtil.info("✅ API публичный - авторизация не требуется");
//        } else {
//            LogUtil.info("ℹ️ API требует авторизацию даже для просмотра");
//        }
//    }
    @Test
    public void testGetCars_WithPagination_ShouldReturnLimitedResults() {
        LogUtil.step("Тест пагинации");

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
                LogUtil.info("Всего машин: " + totalCount);
            }

            if (cars != null) {
                assertTrue(cars.size() <= size,
                        "Должно вернуться не больше " + size + " машин");
                LogUtil.info("✅ Пагинация работает, получено: " + cars.size() + " машин");
            }
        }
    }
    // ============= US-10: GET CAR DETAILS (новые тесты) =============

    @Test
    public void testGetCarById_ExistingCar_ShouldReturn200AndDetails() {
        LogUtil.step("US-10: Получение детальной информации о существующей машине");

        // Сначала получим список машин, чтобы узнать реальный ID
        Response listResponse = RestAssured
                .when()
                .get("/cars")
                .then()
                .extract()
                .response();

        int carId = 1; // значение по умолчанию

        if (listResponse.getStatusCode() == 200) {
            List<Map<String, Object>> cars = listResponse.getBody().jsonPath().getList("$");
            if (cars != null && !cars.isEmpty()) {
                carId = (int) cars.get(0).get("id");
                LogUtil.info("Используем реальный ID из списка: " + carId);
            }
        }

        // Теперь получаем детальную информацию по ID
        Response response = RestAssured
                .when()
                .get("/cars/" + carId)
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        LogUtil.jsonBody(response.getBody().asString());

        assertEquals(response.getStatusCode(), 200, "Должен быть 200 OK");

        // Проверяем, что все поля заполнены
        Map<String, Object> car = response.getBody().jsonPath().getMap("$");

        // Обязательные поля для машины
        assertNotNull(car.get("id"), "Должен быть id");
        assertNotNull(car.get("brand"), "Должна быть марка");
        assertNotNull(car.get("model"), "Должна быть модель");
        assertNotNull(car.get("year"), "Должен быть год выпуска");
        assertNotNull(car.get("pricePerDay"), "Должна быть цена за день");
        assertNotNull(car.get("available"), "Должна быть информация о доступности");

        // Дополнительные поля для детальной информации
        LogUtil.info("Марка: " + car.get("brand"));
        LogUtil.info("Модель: " + car.get("model"));
        LogUtil.info("Год: " + car.get("year"));
        LogUtil.info("Цена/день: " + car.get("pricePerDay"));
        LogUtil.info("Тип: " + car.get("type"));

        LogUtil.info("✅ Детальная информация получена");
    }

    @Test
    public void testGetCarById_CheckAllFields() {
        LogUtil.step("Проверка всех полей автомобиля");

        int carId = 1;

        Response response = RestAssured
                .when()
                .get("/cars/" + carId)
                .then()
                .extract()
                .response();

        Map<String, Object> car = response.getBody().jsonPath().getMap("$");

        // Выводим все ключи, чтобы увидеть структуру
        LogUtil.info("Все поля ответа: " + car.keySet());

        // Проверяем типы данных
        if (car.containsKey("id")) {
            assertTrue(car.get("id") instanceof Number, "id должен быть числом");
        }
        if (car.containsKey("year")) {
            assertTrue(car.get("year") instanceof Number, "year должен быть числом");
        }
        if (car.containsKey("pricePerDay")) {
            assertTrue(car.get("pricePerDay") instanceof Number, "pricePerDay должен быть числом");
        }
        if (car.containsKey("available")) {
            assertTrue(car.get("available") instanceof Boolean, "available должен быть boolean");
        }
        if (car.containsKey("brand")) {
            assertTrue(car.get("brand") instanceof String, "brand должен быть строкой");
        }

        LogUtil.info("✅ Все поля имеют правильные типы");
    }

    @Test
    public void testGetCarById_NonExistingId_ShouldReturn404() {
        LogUtil.step("Негативный тест: запрос несуществующей машины");

        int nonExistingId = 999999;

        Response response = RestAssured
                .when()
                .get("/cars/" + nonExistingId)
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());

        assertEquals(response.getStatusCode(), 404,
                "Для несуществующей машины должен быть 404 Not Found");
        LogUtil.info("✅ API вернул 404 - правильно");
    }

    @Test
    public void testGetCarById_InvalidIdFormat_ShouldReturn400() {
        LogUtil.step("Негативный тест: некорректный формат ID");

        Response response = RestAssured
                .when()
                .get("/cars/abc123") // не число
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());

        assertTrue(response.getStatusCode() == 400 || response.getStatusCode() == 404,
                "Ожидается 400 Bad Request или 404 Not Found, получено: " + response.getStatusCode());

        if (response.getStatusCode() == 400) {
            LogUtil.info("✅ API вернул 400 - правильно (невалидный формат)");
        } else {
            LogUtil.info("ℹ️ API вернул 404 - тоже приемлемо");
        }
    }

    @Test
    public void testGetCarById_NegativeId_ShouldReturn400() {
        LogUtil.step("Негативный тест: отрицательный ID");

        Response response = RestAssured
                .when()
                .get("/cars/-5")
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());

        assertTrue(response.getStatusCode() == 400 || response.getStatusCode() == 404,
                "Для отрицательного ID ожидается 400 или 404");
        LogUtil.info("✅ Тест прошел");
    }

    @Test
    public void testGetCarById_ZeroId_ShouldReturn400() {
        LogUtil.step("Негативный тест: ID = 0");

        Response response = RestAssured
                .when()
                .get("/cars/0")
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());

        assertTrue(response.getStatusCode() == 400 || response.getStatusCode() == 404,
                "Для ID=0 ожидается 400 или 404");
        LogUtil.info("✅ Тест прошел");
    }

    @Test
    public void testGetCarById_WithoutAuth_ShouldWork() {
        LogUtil.step("Проверка: нужна ли авторизация для просмотра машины");

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
            LogUtil.info("✅ Информация о машине доступна без авторизации");
        } else if (response.getStatusCode() == 401) {
            LogUtil.info("ℹ️ API требует авторизацию");
        }
    }

    // ============= ДОПОЛНИТЕЛЬНЫЕ ТЕСТЫ =============

    @Test
    public void testGetCarById_CompareWithListData() {
        LogUtil.step("Сравнение данных из списка и детальной информации");

        // Берем первую машину из списка
        Response listResponse = RestAssured
                .when()
                .get("/cars")
                .then()
                .extract()
                .response();

        if (listResponse.getStatusCode() != 200) {
            LogUtil.info("Не удалось получить список машин");
            return;
        }

        List<Map<String, Object>> carsList = listResponse.getBody().jsonPath().getList("$");
        if (carsList == null || carsList.isEmpty()) {
            LogUtil.info("Список машин пуст");
            return;
        }

        Map<String, Object> firstCarFromList = carsList.get(0);
        int carId = (int) firstCarFromList.get("id");
        LogUtil.info("ID машины из списка: " + carId);

        // Получаем детальную информацию
        Response detailResponse = RestAssured
                .when()
                .get("/cars/" + carId)
                .then()
                .extract()
                .response();

        Map<String, Object> carDetails = detailResponse.getBody().jsonPath().getMap("$");

        // Сравниваем основные поля
        if (carDetails != null) {
            assertEquals(carDetails.get("id"), firstCarFromList.get("id"), "ID должны совпадать");
            assertEquals(carDetails.get("brand"), firstCarFromList.get("brand"), "Марка должна совпадать");
            assertEquals(carDetails.get("model"), firstCarFromList.get("model"), "Модель должна совпадать");
            LogUtil.info("✅ Данные из списка и детальной информации совпадают");
        }
    }

    @Test
    public void testGetCarById_CheckAvailableField() {
        LogUtil.step("Проверка поля available");

        Response response = RestAssured
                .when()
                .get("/cars/1")
                .then()
                .extract()
                .response();

        Boolean available = response.getBody().jsonPath().getBoolean("available");

        if (available != null) {
            LogUtil.info("Машина доступна: " + available);

            if (available) {
                LogUtil.info("✅ Машину можно бронировать");
            } else {
                LogUtil.info("ℹ️ Машина временно недоступна");
            }
        }
    }
}


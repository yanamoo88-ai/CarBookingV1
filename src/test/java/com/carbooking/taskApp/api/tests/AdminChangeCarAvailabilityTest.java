package com.carbooking.taskApp.api.tests;

import com.carbooking.dto.CarDto;
import com.carbooking.taskApp.api.config.BaseApiTest;
import com.carbooking.taskApp.utils.LogUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class AdminChangeCarAvailabilityTest extends BaseApiTest {

    @Test
    public void testAdminChangeCarAvailability_UpdateTimestamp() {
        LogUtil.step("US-06: Проверка обновления updated_at");

        // Сначала создаем авто
        int carId = createTestCar();

        // Получаем время до обновления
        Response beforeResponse = RestAssured
                .given()
                .header("Authorization", "Bearer admin_token")
                .when()
                .get("/api/v1/cars/" + carId)
                .then()
                .extract()
                .response();

        String beforeUpdate = beforeResponse.getBody().path("updated_at");
        LogUtil.info("updated_at до: " + beforeUpdate);

        // Ждем 1 секунду, чтобы время точно изменилось
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Меняем статус
        String patchBody = "{\"available\": false}";

        Response response = RestAssured
                .given()
                .header("Authorization", "Bearer admin_token")
                .contentType("application/json")
                .body(patchBody)
                .when()
                .patch("/api/v1/cars/" + carId)
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 200);

        // Проверяем updated_at после изменения
        String afterUpdate = response.getBody().path("updated_at");
        LogUtil.info("updated_at после: " + afterUpdate);

        assertNotEquals(afterUpdate, beforeUpdate, "updated_at должен обновиться");
        LogUtil.info("✅ updated_at обновлен: " + afterUpdate);
    }

    @Test
    public void testAdminAddCar_DuplicateNumberPlate_ShouldReturn409() {
        LogUtil.step("US-06: Нельзя добавить авто с существующим номером");

        // Уникальный номер для первого авто
        String uniqueNumber = "TEST-" + System.currentTimeMillis();

        // Создаем первое авто
        CarDto car1 = CarDto.builder()
                .brand("Toyota")
                .model("Camry") // ИСПРАВЛЕНО: model, не mode
                .numberPlate(uniqueNumber) // ИСПРАВЛЕНО: numberPlate, не num
                .categoryId(1) // ИСПРАВЛЕНО: categoryId, не cate
                .imageUrl("https://example.com/camry.jpg")
                .build();

        Response response1 = RestAssured
                .given()
                .header("Authorization", "Bearer admin_token")
                .contentType("application/json")
                .body(car1)
                .when()
                .post("/api/v1/cars")
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response1.getStatusCode());

        // Если первое авто создалось успешно (201)
        if (response1.getStatusCode() == 201) {
            LogUtil.info("✅ Первое авто создано с номером: " + uniqueNumber);

            // Пытаемся создать второе авто с ТАКИМ ЖЕ номером
            CarDto car2 = CarDto.builder()
                    .brand("Toyota")
                    .model("Corolla")
                    .numberPlate(uniqueNumber) // Тот же номер!
                    .categoryId(1)
                    .imageUrl("https://example.com/corolla.jpg")
                    .build();

            Response response2 = RestAssured
                    .given()
                    .header("Authorization", "Bearer admin_token")
                    .contentType("application/json")
                    .body(car2)
                    .when()
                    .post("/api/v1/cars")
                    .then()
                    .extract()
                    .response();

            LogUtil.apiResponse(response2.getStatusCode());

            // Должен быть 409 Conflict из-за уникальности number_plate
            assertEquals(response2.getStatusCode(), 409,
                    "Должен быть 409 Conflict - дубликат номера");
            LogUtil.info("✅ API вернул 409 - номер уже существует");
        } else {
            LogUtil.warn("⚠️ Первое авто не создалось. Статус: " + response1.getStatusCode());
        }
    }

    // Helper method для создания тестового авто
    private int createTestCar() {
        CarDto newCar = CarDto.builder()
                .brand("TestBrand")
                .model("TestModel")
                .numberPlate("TST-" + System.currentTimeMillis())
                .categoryId(1)
                .imageUrl("https://example.com/test.jpg")
                .build();

        Response response = RestAssured
                .given()
                .header("Authorization", "Bearer admin_token")
                .contentType("application/json")
                .body(newCar)
                .when()
                .post("/api/v1/cars")
                .then()
                .extract()
                .response();

        return response.getBody().path("id");
    }
}
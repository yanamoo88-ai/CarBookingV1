package com.carbooking.taskApp.api.tests;

import com.carbooking.taskApp.api.config.BaseApiTest;
import com.carbooking.taskApp.utils.LogUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import com.carbooking.taskApp.utils.LogUtil;
import static org.testng.Assert.*;

public class CarCategoriesTest extends BaseApiTest {

    @Test
    public void testGetCarCategories_ShouldReturn200() {
        LogUtil.step("Отправляем GET запрос на /car-categories");

        Response response = RestAssured
                .when()
                .get("/car-categories")
                .then()
                .statusCode(200)
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        String body = response.getBody().asString();
        LogUtil.info("Длина ответа: " + body.length());

        assertNotNull(body, "Ответ не должен быть пустым");
        LogUtil.info("✅ Проверка пройдена");
    }

    @Test
    public void testGetCarCategories_ShouldContainExpectedCategories() {
        LogUtil.step("Отправляем GET запрос на /car-categories");

        Response response = RestAssured
                .when()
                .get("/car-categories")
                .then()
                .statusCode(200)
                .extract()
                .response();

        String body = response.getBody().asString();
        LogUtil.jsonBody(body);

        String[] categories = {"Economy", "Comfort", "Luxury"};

        for (String category : categories) {
            LogUtil.step("Проверяем категорию: " + category);
            boolean found = body.contains(category) ||
                    body.contains("\"name\":\"" + category + "\"");
            assertTrue(found, "Должна быть категория " + category);
            LogUtil.info("✅ Найдена категория " + category);
        }
    }
}

//import com.carbooking.taskApp.api.config.BaseApiTest;
//import io.restassured.RestAssured;
//import io.restassured.response.Response;
//import org.testng.annotations.Test;
//import static org.testng.Assert.*;
//
//public class CarCategoriesTest extends BaseApiTest {
//
//    @Test
//    public void testGetCarTypes() {
//        System.out.println("\n🔵 Тест: получение типов машин");
//
//        // Отправляем GET запрос
//        Response response = RestAssured
//                .when()
//                .get("/cartypes")  // добавится к baseURI = http://localhost:8080/api/cartypes
//                .then()
//                .statusCode(200)
//                .extract()
//                .response();
//
//        // Проверяем что ответ не пустой
//        String responseBody = response.getBody().asString();
//        assertNotNull(responseBody, "Ответ не должен быть null");
//        assertFalse(responseBody.isEmpty(), "Ответ не должен быть пустым");
//
//        // Проверяем что это JSON массив (начинается с [)
//        assertTrue(responseBody.startsWith("["), "Должен вернуться JSON массив");
//
//        System.out.println("✅ Тест прошел! Получен ответ длиной: " + responseBody.length());
//        System.out.println("📊 Первые 100 символов: " + responseBody.substring(0, Math.min(100, responseBody.length())));
//    }
//
//    // Добавим еще один простой тест для проверки
//    @Test
//    public void testCarTypesStatusCode() {
//        System.out.println("\n🔵 Тест: проверка статус кода");
//
//        RestAssured
//                .when()
//                .get("/cartypes")
//                .then()
//                .statusCode(200);
//
//        System.out.println("✅ Статус код 200 OK");
//    }
//}

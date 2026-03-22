package com.carbooking.taskApp.api.tests;

import com.carbooking.dto.UserRegistrationDto;
import com.carbooking.taskApp.api.config.BaseApiTest;
import com.carbooking.taskApp.utils.LogUtil;


import com.carbooking.utils.MyDataProvider;
import com.carbooking.dto.AuthRequestDto
        ;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.*;
public class UserRegistrationTest extends BaseApiTest {

    @Test(dataProvider = "positiveRegistrationData", dataProviderClass = MyDataProvider.class)
    public void testPositiveRegistration(@NotNull UserRegistrationDto user) {
        LogUtil.step("Positive registration test");

        LogUtil.param("full_name", user.getFull_name());
        LogUtil.param("email", user.getEmail());
        // Пароль НЕ логируем для безопасности

        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body(user) // RestAssured сам возьмет password из объекта и отправит в JSON
                .when()
                .post("/auth/register") // Верный путь из Swagger
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());

        // Проверки
        assertEquals(response.getStatusCode(), 201, "Ожидался статус 201");

        Integer userId = response.jsonPath().get("id");
        assertNotNull(userId, "Должен вернуться ID пользователя");

        // Сверяем данные из ответа с тем, что отправляли
        assertEquals(response.jsonPath().get("full_name"), user.getFull_name());
        assertEquals(response.jsonPath().get("email"), user.getEmail());

        LogUtil.info("✅ Пользователь создан с ID: " + userId);
    }

    @Test(dataProvider = "negativeRegistrationData", dataProviderClass = MyDataProvider.class)
    public void testNegativeRegistration(UserRegistrationDto user, String expectedError) {
        LogUtil.step("Негативный тест регистрации");
        LogUtil.param("FullName", user.getFull_name() == null || user.getFull_name().isEmpty() ? "[EMPTY]" : user.getFull_name());
        LogUtil.param("Email", user.getEmail() == null || user.getEmail().isEmpty() ? "[EMPTY]" : user.getEmail());
        LogUtil.param("Phone", user.getPhone() == null || user.getPhone().isEmpty() ? "[EMPTY]" : user.getPhone());
        LogUtil.param("Expected Error", expectedError);

        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body(user)
                .when()
                .post("/auth/register")
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        //assertEquals(response.getStatusCode(),400);
        // }
        assertTrue(response.getStatusCode() == 400 || response.getStatusCode() == 409,
                "Expected 400 or 409, received:" + response.getStatusCode());

        // Проверяем сообщение об ошибке
        String errorMessage = "";
        if (response.getBody().path("message") != null) {
            errorMessage = response.getBody().path("message");
        } else if (response.getBody().path("error") != null) {
            errorMessage = response.getBody().path("error");
        } else if (response.getBody().path("errors") != null) {
            errorMessage = response.getBody().path("errors").toString();
        }

        LogUtil.info("Error message: " + errorMessage);

        // Проверяем, что ошибка соответствует ожидаемой
        if (expectedError != null && !expectedError.isEmpty()) {
            assertTrue(errorMessage.toLowerCase().contains(expectedError.toLowerCase()) ||
                            errorMessage.toLowerCase().contains(expectedError.replace("cannot be empty", "required").toLowerCase()),
                    "An error containing the following was expected:" + expectedError + ", но получено: " + errorMessage);
        }

        LogUtil.info("✅ API returned the correct error");
    }

    @Test(dataProvider = "positiveAuth", dataProviderClass = MyDataProvider.class)
    public void testLoginPositive(AuthRequestDto authRequest) {
        LogUtil.step("Positive authorization test: " + authRequest.getEmail());

        // 1. Отправляем запрос
        Response response = RestAssured.given()
                .contentType("application/json")
                .body(authRequest)
                .when()
                .post("/auth/login") // Убедись, что путь совпадает с твоим API
                .then()
                .extract()
                .response();

        // 2. Логируем ответ для отчета
        LogUtil.apiResponse(response.getStatusCode());

        // 3. АССЕРТ: Проверяем, что сервер ответил 200 OK
        assertEquals(response.getStatusCode(), 200, "Error! The server did not authorise the user.");

        // 4. Извлекаем токен
        String extractedToken = response.jsonPath().getString("token");

        // 5. АССЕРТ: Проверяем, что токен не пустой
        assertNotNull(extractedToken, "The server returned 200, but the 'token' field is missing in the JSON!");


        token = extractedToken;
        LogUtil.info("The token has been successfully obtained and saved.");
    }
    @Test(dataProvider = "negativeAuth", dataProviderClass = MyDataProvider.class)
    public void testLoginNegative(AuthRequestDto authRequest) {
        LogUtil.step("Negative authorisation test: " + authRequest.getEmail());

        Response response = RestAssured.given()
                .contentType("application/json")
                .body(authRequest)
                .post("/auth/login");

        LogUtil.apiResponse(response.getStatusCode());

        // Здесь МЫ ЖДЕМ 401 (Unauthorized)
        assertEquals(response.getStatusCode(), 401, "БАГ! Сервер пустил пользователя с неверными данными.");
    }

}

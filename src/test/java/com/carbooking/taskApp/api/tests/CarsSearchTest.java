package com.carbooking.taskApp.api.tests;
import com.carbooking.taskApp.api.config.BaseApiTest;

import com.carbooking.taskApp.utils.LogUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static org.testng.Assert.*;

public class CarsSearchTest extends BaseApiTest {

    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Test
    public void testSearchCars_WithValidDates_ShouldReturn200() {
        LogUtil.step("Подготовка валидных дат");

        String startDate = LocalDate.now().plusDays(1).format(formatter);
        String endDate = LocalDate.now().plusDays(7).format(formatter);

        LogUtil.param("start_date", startDate);
        LogUtil.param("end_date", endDate);

        LogUtil.step("Отправляем GET запрос с параметрами");

        Response response = RestAssured
                .given()
                .queryParam("start_date", startDate)
                .queryParam("end_date", endDate)
                .when()
                .get("/cars")
                .then()
                .statusCode(200)
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        String body = response.getBody().asString();
        LogUtil.info("Получено машин: " + (body.length() > 2 ? "есть данные" : "пусто"));

        assertNotNull(body);
        LogUtil.info("✅ Тест успешно завершен");
    }

    @Test
    public void testSearchCars_EndDateBeforeStartDate_ShouldReturnError() {
        LogUtil.step("Подготовка некорректных дат");

        String startDate = LocalDate.now().plusDays(7).format(formatter);
        String endDate = LocalDate.now().plusDays(1).format(formatter);

        LogUtil.param("start_date", startDate);
        LogUtil.param("end_date", endDate);
        LogUtil.warn("Ожидаем ошибку от API");

        Response response = RestAssured
                .given()
                .queryParam("start_date", startDate)
                .queryParam("end_date", endDate)
                .when()
                .get("/cars")
                .then()
                .extract()
                .response();

        int statusCode = response.getStatusCode();
        LogUtil.apiResponse(statusCode);

        boolean isError = statusCode == 400 || statusCode == 422;
        assertTrue(isError, "Ожидался код 400 или 422, получили: " + statusCode);

        LogUtil.info("✅ API вернул ошибку " + statusCode + " - правильно");
    }
}
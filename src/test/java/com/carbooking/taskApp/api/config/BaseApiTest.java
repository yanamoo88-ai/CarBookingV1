package com.carbooking.taskApp.api.config;

import com.carbooking.dto.AuthRequestDto;
import com.carbooking.taskApp.utils.LogUtil;
import com.carbooking.utils.TestNGListener;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

import static org.testng.Assert.assertEquals;


@Listeners(TestNGListener.class)
public class BaseApiTest {

    protected static final Logger log = LoggerFactory.getLogger(BaseApiTest.class);
public static String token;
    @BeforeClass
    public void setup() {
                      RestAssured.baseURI = "https://dev.pshacks.org/api/v3";

        // Настройка JSON (Content-Type и Accept)
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();

        RestAssured.filters(
                new RequestLoggingFilter(),
                new ResponseLoggingFilter()
        );

        log.info("✅ BaseApiTest initialised");
        log.info(" BaseURI: {}", RestAssured.baseURI);
    }
    /**
     * Метод для авторизации.
     * Возвращает токен доступа (String), который можно использовать в других тестах.
     */
    protected String login() {
        LogUtil.step("Logging in");


        AuthRequestDto loginRequest = AuthRequestDto.builder()
                .email("test807@test.me")
                .password("password123")
                .build();


        Response response = RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/auth/login")
                .then()
                .extract()
                .response();

                LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 200, "Error during login attempt!");

        // Извлекаем токен из JSON-ответа
        String token = response.getBody().path("token");

        LogUtil.info("The token has been successfully obtained.");

        return token;
    }
}



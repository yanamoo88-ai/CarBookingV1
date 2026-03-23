package com.carbooking.taskApp.api.config;


import com.carbooking.utils.TestNGListener;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;


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
}

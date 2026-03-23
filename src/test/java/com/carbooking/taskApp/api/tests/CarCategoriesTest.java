package com.carbooking.taskApp.api.tests;

import com.carbooking.taskApp.api.config.BaseApiTest;
import com.carbooking.taskApp.utils.LogUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class CarCategoriesTest extends BaseApiTest {

    @Test
    public void testGetCarCategories_ShouldReturn200() {
        LogUtil.step("Sending a GET request to /car-categories");

        Response response = RestAssured
                .when()
                .get("/car-categories")
                .then()
                .statusCode(200)
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        String body = response.getBody().asString();
        LogUtil.info("Response length: " + body.length());

        assertNotNull(body, "The answer must not be empty");
        LogUtil.info("✅ Test good!");
    }

    @Test
    public void testGetCarCategories_ShouldContainExpectedCategories() {
        LogUtil.step("Sending a GET request to  /car-categories");

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
            LogUtil.step("Checking the category: " + category);
            boolean found = body.contains(category) ||
                    body.contains("\"name\":\"" + category + "\"");
            assertTrue(found, "There must be a category " + category);
            LogUtil.info("✅ Category found " + category);
        }
    }
}


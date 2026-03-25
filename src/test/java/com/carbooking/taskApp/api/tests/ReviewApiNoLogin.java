package com.carbooking.taskApp.api.tests;


import com.carbooking.taskApp.api.config.BaseApiTest;
import com.carbooking.taskApp.utils.LogUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ReviewApiNoLogin extends BaseApiTest {

    @Test
    public void testGetReviews_ShouldReturn200() {
               LogUtil.step("Request for a list of reviews WITHOUT authorization");

        Response response = RestAssured
                .given()
                .when()
                .get("/reviews")
                .then()
                .extract()
                .response();


        LogUtil.apiResponse(response.getStatusCode());

        assertEquals(response.getStatusCode(), 200, "Error: The server did not return  200 status!");

                LogUtil.info("✅ Reviews successfully received without login");
    }
}
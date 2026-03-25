package com.carbooking.taskApp.api.tests;

import com.carbooking.dto.BookingRequestDto;
import com.carbooking.taskApp.api.config.BaseApiTest;
import com.carbooking.taskApp.utils.LogUtil;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class BookingApiTest extends BaseApiTest {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // ============= CREATE BOOKING =============

    @Test
    public void testCreateBooking_ValidData_ShouldReturn201() {
                String token = login();
        LogUtil.step(" Creating a booking with valid data");

        BookingRequestDto booking = BookingRequestDto.builder()
                .user_id(1)
                .car_id(1)
                .start_date(LocalDate.now().plusDays(1).atStartOfDay().format(formatter))
                .end_date(LocalDate.now().plusDays(3).atStartOfDay().format(formatter))
                .build();

        LogUtil.info("Booking details:");
        LogUtil.param("userId", booking.getUser_id());
        LogUtil.param("carId", booking.getCar_id());
        LogUtil.param("startDate", booking.getStart_date());
        LogUtil.param("endDate", booking.getEnd_date());

        Response response = RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(booking)
                .when()
                .post("/bookings")
                .then()
                .extract()
                .response();

        int statusCode = response.getStatusCode();
        LogUtil.apiResponse(statusCode);
        LogUtil.jsonBody(response.getBody().asString());

        assertTrue(statusCode == 201 || statusCode == 200,
                "Expected 201 or 200, received: " + statusCode);

        assertNotNull(response.getBody().path("id"), "I need to return the booking ID");
        LogUtil.info("✅ The booking was created successfully");
    }

    @Test
    public void testCreateBooking_WithoutUserId_ShouldReturn400() {
        String token = login();
        LogUtil.step("Negative: creating a booking without userId");

        BookingRequestDto booking = BookingRequestDto.builder()
                .car_id(1)
                .start_date(LocalDate.now().plusDays(1).atStartOfDay().format(formatter))
                .end_date(LocalDate.now().plusDays(3).atStartOfDay().format(formatter))
                .build();

        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body(booking)
                .when()
                .post("/bookings")
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 400, "There must be a 400 Bad Request");
        LogUtil.info("✅ API returned 400 - correct");
    }

    @Test
    public void testCreateBooking_InvalidDateRange_ShouldReturn400() {
        LogUtil.step("Negative: end date is earlier than start date");
        String token = login();

        BookingRequestDto booking = BookingRequestDto.builder()
                .user_id(1)
                .car_id(1)
                .start_date(LocalDate.now().plusDays(3).atStartOfDay().format(formatter))
                .end_date(LocalDate.now().plusDays(1).atStartOfDay().format(formatter)) // раньше!
                .build();

        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body(booking)
                .when()
                .post("bookings")
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 400, "An incorrect date range should return 400");
        LogUtil.info("✅ The API checks the correctness of the dates");
    }

    @Test
    public void testCreateBooking_PastDates_ShouldReturn400() {
        String token = login();
        LogUtil.step("Negative: booking for past dates");

        BookingRequestDto booking = BookingRequestDto.builder()
                .user_id(1)
                .car_id(1)
                .start_date(LocalDate.now().minusDays(2).atStartOfDay().format(formatter)) // в прошлом
                .end_date(LocalDate.now().plusDays(1).atStartOfDay().format(formatter))
                .build();

        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body(booking)
                .when()
                .post("/bookings")
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 400, "Booking in the past is impossible");
        LogUtil.info("✅ The API does not allow booking for past dates");
    }


    @Test
    public void testGetBookingById_ExistingBooking_ShouldReturn200() {
        String token = login();
        LogUtil.step("Retrieving an existing booking");

                BookingRequestDto newBooking = BookingRequestDto.builder()
                .user_id(1)
                .car_id(1)
                .start_date(LocalDate.now().plusDays(10).atStartOfDay().format(formatter))
                .end_date(LocalDate.now().plusDays(12).atStartOfDay().format(formatter))
                .build();

        int bookingId = RestAssured
                .given()
                .contentType("application/json")
                .body(newBooking)
                .when()
                .post("/bookings")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        LogUtil.info("A booking has been created with ID: " + bookingId);

        // Получаем бронирование по ID
        Response response = RestAssured
                .when()
                .get("/bookings/" + bookingId)
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        LogUtil.jsonBody(response.getBody().asString());

        assertEquals(response.getStatusCode(), 200, "It must be 200 OK");
        assertNotNull(response.getBody().path("id"), "The answer should beid");
        LogUtil.info("✅ Booking successfully received");
    }

    @Test
    public void testGetBookingById_NonExistingId_ShouldReturn404() {
        String token = login();
        LogUtil.step("Negative: request for a non-existent booking");

        int nonExistingId = 999999;

        Response response = RestAssured
                .when()
                .get("/bookings/" + nonExistingId)
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 404, "A 404 should be returned for a non-existent ID");
        LogUtil.info("✅ The API returned 404 - correct");
    }

    @Test
    public void testGetBookingById_InvalidIdFormat_ShouldReturn400() {
        String token = login();
        LogUtil.step("Negative: request with an incorrect ID format");

        Response response = RestAssured
                .when()
                .get("/bookings/abc123") // не число
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertTrue(response.getStatusCode() == 400 || response.getStatusCode() == 404,
                "For an incorrect ID, it should be 400 or 404");
        LogUtil.info("✅ The API processed an invalid ID format");
    }

    @Test
    public void testUpdateBooking_ChangeDates_ShouldReturn200() {
        String token = login();
        LogUtil.step(" Update booking dates");

        BookingRequestDto newBooking = BookingRequestDto.builder()
                .user_id(1)
                .car_id(1)
                .start_date(LocalDate.now().plusDays(15).atStartOfDay().format(formatter))
                .end_date(LocalDate.now().plusDays(17).atStartOfDay().format(formatter))
                .build();

        int bookingId = RestAssured
                .given()
                .contentType("application/json")
                .body(newBooking)
                .when()
                .post("/bookings")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        LogUtil.info("Booking ID created:" + bookingId);

        // Обновляем даты
        BookingRequestDto updatedBooking = BookingRequestDto.builder()
                .user_id(1)
                .car_id(1)
                .start_date(LocalDate.now().plusDays(20).atStartOfDay().format(formatter))
                .end_date(LocalDate.now().plusDays(22).atStartOfDay().format(formatter))
                .build();

        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body(updatedBooking)
                .when()
                .put("/bookings/" + bookingId)
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 200, "The update should be successful");
        LogUtil.info("✅ Booking updated");
    }

    @Test
    public void testUpdateBooking_NonExistingId_ShouldReturn404() {
        String token = login();
        LogUtil.step("Negative: updating a non-existent booking");

        BookingRequestDto updatedBooking = BookingRequestDto.builder()
                .user_id(1)
                .car_id(1)
                .start_date(LocalDate.now().plusDays(15).atStartOfDay().format(formatter))
                .end_date(LocalDate.now().plusDays(17).atStartOfDay().format(formatter))
                .build();

        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body(updatedBooking)
                .when()
                .put("/bookings/999999")
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 404, "Must be 404");
        LogUtil.info("✅ 404 - !");
    }


    @Test
    public void testDeleteBooking_ExistingBooking_ShouldReturn200() {
        String token = login();
        LogUtil.step("Cancellation of booking");

        BookingRequestDto newBooking = BookingRequestDto.builder()
                .user_id(1)
                .car_id(1)
                .start_date(LocalDate.now().plusDays(25).atStartOfDay().format(formatter))
                .end_date(LocalDate.now().plusDays(27).atStartOfDay().format(formatter))
                .build();

        int bookingId = RestAssured
                .given()
                .contentType("application/json")
                .body(newBooking)
                .when()
                .post("/bookings")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        LogUtil.info("Booking ID created:" + bookingId);

        // Удаляем бронирование
        Response response = RestAssured
                .when()
                .delete("/bookings/" + bookingId)
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 204,
                "Expected 200 or 204, received:" + response.getStatusCode());

        // Проверяем, что бронирование действительно удалено
        Response checkResponse = RestAssured
                .when()
                .get("/bookings/" + bookingId)
                .then()
                .extract()
                .response();

        assertEquals(checkResponse.getStatusCode(), 404, " 404");
        LogUtil.info("✅ The booking has been successfully cancelled");
    }

    @Test
    public void testDeleteBooking_NonExistingId_ShouldReturn404() {
        String token = login();
        LogUtil.step("Deleting a non-existent booking");

        Response response = RestAssured
                .when()
                .delete("/bookings/999999")
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 404, "Must be 404");
        LogUtil.info("✅ 404 - Gut!");
    }


    @Test
    public void testGetAllBookings_ShouldReturn200() {
        String token = login();
        LogUtil.step("Obtaining a list of all bookings");

        Response response = RestAssured
                .when()
                .get("/bookings")
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 200, "200 OK");

        List<Map<String, Object>> bookings = response.getBody().jsonPath().getList("$");
        LogUtil.info("Found:: " + (bookings != null ? bookings.size() : 0));
        LogUtil.info("✅ List of bookings received");
    }

    @Test
    public void testGetBookings_ByUserId_ShouldReturnFilteredList() {
        String token = login();
        LogUtil.step("Obtaining bookings by user");

        int userId = 1;

        Response response = RestAssured
                .when()
                .get("/bookings?userId=" + userId)
                .then()
                .extract()
                .response();

        LogUtil.apiResponse(response.getStatusCode());
        assertEquals(response.getStatusCode(), 200);

        List<Map<String, Object>> bookings = response.getBody().jsonPath().getList("$");
        LogUtil.info("Found for user Id=" + userId + ": " +
                (bookings != null ? bookings.size() : 0));

        if (bookings != null && !bookings.isEmpty()) {
            for (Map<String, Object> booking : bookings) {
                assertEquals(booking.get("userId"), userId,
                        "All bookings must be for user Id " + userId);
            }
        }
        LogUtil.info("✅ The filter by user Id works");
    }
}
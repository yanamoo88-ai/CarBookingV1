package com.carbooking.api.endpoints;

import com.carbooking.api.dto.BookingRequestDto;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class BookingEndPoint {

    // US-01: Проверка доступности машин
    // Передаем параметры через queryParam, как указано в критериях
    public Response checkAvailability(String startDate, String endDate) {
        return given()
                .queryParam("start", startDate)
                .queryParam("end", endDate)
                .when()
                .get("/v1/check-availability");
    }
    // US-02: Создание бронирования
    // Принимает DTO и отправляет его в теле POST-запроса
    public Response createBooking(BookingRequestDto bookingDto) {
        return given()
                .body(bookingDto)
                .when()
                .post("/v1/bookings");
    }

}
package com.carbooking.api.endpoints;

import com.carbooking.api.dto.BookingUpdateDto;
import com.carbooking.api.dto.CarUpdateDto;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class AdminEndPoint {

    // US-03: Просмотр списка всех заявок
    public Response getAllBookings() {
        return given()
                .when()
                .get("/v1/admin/bookings");
    }

    // US-04: Изменение статуса или дат бронирования
    public Response updateBooking(Integer id, BookingUpdateDto updateDto) {
        return given()
                .body(updateDto)
                .when()
                .patch("/v1/admin/bookings/" + id);
    }

    // US-05: Удаление (архивация) машины по ID
    public Response updateCarStatus(Integer id, CarUpdateDto statusDto) {
        return given()
                .body(statusDto)
                .when()
                .put("/v1/admin/cars/" + id);
    }
}


//Этот эндпоинт реализует функции администратора:
// просмотр списка заявок (US-03), их редактирование (US-04)
// и архивацию машин (US-05).

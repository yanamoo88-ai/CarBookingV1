package com.carbooking.taskApp.api.tests;

import com.carbooking.api.dto.BookingUpdateDto;
import com.carbooking.database.repository.BookingRepository;
import com.carbooking.database.repository.CarRepository;
import com.carbooking.dto.CarDto;
import com.carbooking.taskApp.api.config.BaseApiTest;
import com.carbooking.api.dto.CarUpdateDto;
import com.carbooking.api.endpoints.AdminEndPoint;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AdminApiTest extends BaseApiTest {


    AdminEndPoint adminEndPoint = new AdminEndPoint();
    CarRepository carRepository = new CarRepository();

    BookingRepository bookingRepository = new BookingRepository();

    @Test(description = "US-05: Удаление машины (Soft Delete)")
    public void testArchiveCarAndVerifyInDb() {
        // 1. ID машины, которую мы создали в SQL-скрипте
        Integer targetCarId = 1;

        // 2. Подготавливаем данные для смены статуса
        CarUpdateDto archiveRequest = CarUpdateDto.builder()
                .status("archived") // Переводим в архив
                .build();

        // 3. Отправляем запрос через Admin API
        Response response = adminEndPoint.updateCarStatus(targetCarId, archiveRequest);

        // 4. Проверяем, что API подтвердило успех (200 OK или 204 No Content)
        Assert.assertEquals(response.getStatusCode(), 200, "API должно подтвердить обновление статуса");

        // 5. ПРОВЕРКА В БАЗЕ ДАННЫХ
        // Вызываем репозиторий, чтобы убедиться, что статус реально изменился
        CarDto carFromDb = carRepository.findById(targetCarId);

        Assert.assertNotNull(carFromDb, "Машина не должна исчезнуть из БД");
        Assert.assertEquals(carFromDb.getStatus(), "archived", "Статус в БД должен смениться на 'archived'");

        System.out.println("Тест пройден: Машина ID " + targetCarId + " успешно архивирована.");
    }

//     US-03. Эта история критически важна для администратора:
//     он должен видеть список всех заявок, чтобы понимать загрузку автопарка
//     и иметь под рукой контакты клиентов для связи.
//Согласно вашим критериям приемки, в ответе обязательно должны быть ID заявки,
// имя клиента, телефон и статус.
    @Test(description = "US-03: Админ может просмотреть список всех заявок с контактами клиентов")
    public void testAdminCanSeeAllBookings() {
        // 1. Вызываем метод получения всех бронирований
        Response response = adminEndPoint.getAllBookings();

        // 2. Проверяем, что запрос прошел успешно
        Assert.assertEquals(response.getStatusCode(), 200, "Статус ответа должен быть 200 OK");

        // 3. Проверяем, что вернулся список, и он не пустой (так как в БД есть запись с ID 1)
        Assert.assertTrue(response.jsonPath().getList("$").size() > 0, "Список бронирований не должен быть пустым");

        // 4. Проверяем наличие обязательных полей из US-03
        // Проверим первую запись в списке
        Integer id = response.jsonPath().get("[0].id");
        String clientPhone = response.jsonPath().get("[0].clientPhone");
        String status = response.jsonPath().get("[0].status");

        Assert.assertNotNull(id, "ID заявки должен присутствовать");
        Assert.assertNotNull(clientPhone, "Телефон клиента обязателен по критериям US-03");
        Assert.assertEquals(status, "confirmed", "Статус должен совпадать с данными из БД");

        System.out.println("Тест пройден: Админ видит заявку #" + id + " клиента с телефоном " + clientPhone);
    }
//    * Информативность: Он гарантирует, что админ не просто видит "какие-то записи",
//    а получает именно контактные данные (телефон), которые необходимы по бизнес-требованиям.
// * Формат данных: Мы проверяем корректность работы
// маппинга в вашем API (как данные из таблицы bookings и users соединяются в один JSON-объект).

    @Test(description = "US-04: Админ может подтвердить бронирование или изменить его статус")
    public void testAdminCanUpdateBookingStatus() {
        // 1. ID бронирования, которое мы создали через SQL или API
        Integer bookingId = 1;

        // 2. Подготавливаем данные для обновления (меняем статус на 'confirmed')
        BookingUpdateDto updateData = BookingUpdateDto.builder()
                .status("confirmed")
                .build();

        // 3. Отправляем PATCH запрос через эндпоинт
        Response response = adminEndPoint.updateBooking(bookingId, updateData);

        // 4. Проверяем, что сервер принял изменения
        Assert.assertEquals(response.getStatusCode(), 200, "Статус ответа должен быть 200 OK");

        // 5. Проверяем в БД, что статус реально обновился
        var updatedBooking = bookingRepository.findById(bookingId);
        Assert.assertEquals(updatedBooking.getStatus(), "confirmed", "Статус в базе данных должен обновиться на 'confirmed'");

        System.out.println("Тест пройден: Бронирование #" + bookingId + " успешно подтверждено администратором.");
    }
}
//US-04 (Редактирование бронирования администратором).
//Этот тест проверяет возможность админа подтверждать заявку или изменять её параметры.
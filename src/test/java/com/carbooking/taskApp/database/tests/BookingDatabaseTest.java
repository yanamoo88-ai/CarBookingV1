package com.carbooking.taskApp.database.tests;


import com.carbooking.database.repository.BookingRepository;
import com.carbooking.dto.BookingDto;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BookingDatabaseTest {

    BookingRepository bookingRepository = new BookingRepository();

    @Test
    public void testGetBookingByIdFromDb() {
        // Укажите ID существующего бронирования из вашей базы данных
        Integer targetId = 1;

        BookingDto booking = bookingRepository.findById(targetId);

        // Проверки согласно документации проекта
        Assert.assertNotNull(booking, "Бронирование с ID " + targetId + " не найдено!");
        Assert.assertNotNull(booking.getUserId(), "User ID в бронировании не должен быть пустым");

        System.out.println("Тест пройден! Бронирование найдено. Статус: " + booking.getStatus());
    }
}
package com.carbooking.gui.tests.positive;

import com.carbooking.gui.core.TestBase;
import com.carbooking.gui.pages.BookingPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BookingPositiveTests extends TestBase {

    @Test
    public void testCarBookingSuccess() throws InterruptedException {
        app.driver.get("http://localhost:5173/login");
        loginPage.login("tester1774119303709@gmail.com", "GanzQA2026!");

        app.driver.get("http://localhost:5173/");
        BookingPage bPage = new BookingPage(app.driver);

        // Вводим даты
        bPage.fillDatesAndSearch("28032026", "30032026");
        Thread.sleep(3000); // Ждем обновления списка машин

        bPage.clickFirstAvailableCar();
        Thread.sleep(2000);

        // Если упадет - сделаем скриншот, чтобы увидеть, что на экране
        if (!bPage.isBookingSuccessful()) {
            app.takeScreenshot();
        }

        Assert.assertTrue(bPage.isBookingSuccessful(),
                "ОШИБКА: Бронирование не подтверждено. Скриншот сделан.");
    }
}
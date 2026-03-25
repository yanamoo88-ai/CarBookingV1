package com.carbooking.gui.tests.all;

import com.carbooking.gui.core.TestBase;
import com.carbooking.gui.pages.BookingPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FullWorkflowTest extends TestBase {

    @Test(description = "Полный цикл: Логин -> Поиск -> Бронирование")
    public void testFullCarBookingWorkflow() throws InterruptedException {
        // 1. Авторизация
        app.driver.get("http://localhost:5173/login");
        loginPage.login("tester1774119303709@gmail.com", "GanzQA2026!");

        // 2. Переход к бронированию
        app.driver.get("http://localhost:5173/");
        BookingPage bPage = new BookingPage(app.driver);

        // 3. Поиск машин
        bPage.fillDatesAndSearch("28032026", "30032026");
        Thread.sleep(2000); // Даем React подгрузить список

        // 4. Клик по первой машине
        bPage.clickFirstAvailableCar();
        Thread.sleep(2000);

        // 5. Проверка успеха
        Assert.assertTrue(bPage.isBookingSuccessful(), "Ошибка: Бронирование не завершено!");
    }
}

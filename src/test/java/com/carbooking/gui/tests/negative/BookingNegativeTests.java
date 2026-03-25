package com.carbooking.gui.tests.negative;

import com.carbooking.gui.core.TestBase;
import com.carbooking.gui.pages.BookingPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BookingNegativeTests extends TestBase {

    @Test(priority = 1)
    public void testBookingInPastDates() throws InterruptedException {
        // 1. Логин (нужен для доступа к бронированию)
        app.driver.get("http://localhost:5173/login");
        loginPage.login("tester1774119303709@gmail.com", "GanzQA2026!");

        // 2. Переход на главную
        app.driver.get("http://localhost:5173/");
        BookingPage bPage = new BookingPage(app.driver);

        // 3. Попытка поиска машин на прошедшие даты (например, 2020 год)
        bPage.fillDatesAndSearch("01012020", "05012020");
        Thread.sleep(2000);

        // 4. Проверка: либо кнопок "Book now" нет, либо клик не приводит к успеху
        try {
            bPage.clickFirstAvailableCar();
            Thread.sleep(2000);
            Assert.assertFalse(bPage.isBookingSuccessful(),
                    "БАГ: Система позволила забронировать автомобиль на даты в прошлом!");
        } catch (Exception e) {
            // Если кнопки не найдены — это корректное поведение системы
            System.out.println("Кнопки бронирования не появились для дат в прошлом. Тест пройден.");
        }
    }

    @Test(priority = 2)
    public void testBookingWithoutLogin() {
        // Разлогиниваемся, если сессия активна
        if (homePage.isLogoutButtonPresent()) {
            homePage.clickLogout();
        }

        // Пытаемся зайти на страницу со списком машин напрямую
        app.driver.get("http://localhost:5173/cars");

        // Проверяем, что неавторизованный пользователь либо редиректнут, либо не видит кнопок бронирования
        boolean isLoginPage = app.driver.getCurrentUrl().contains("login");
        boolean canSeeBookButtons = app.driver.getPageSource().contains("Book now");

        Assert.assertTrue(isLoginPage || !canSeeBookButtons,
                "ОШИБКА: Доступ к функционалу бронирования открыт без авторизации!");
    }
}

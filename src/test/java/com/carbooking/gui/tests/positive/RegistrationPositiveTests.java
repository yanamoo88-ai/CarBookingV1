package com.carbooking.gui.tests.positive;

import com.carbooking.gui.core.TestBase;
import com.carbooking.gui.pages.RegistrationPage;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class RegistrationPositiveTests extends TestBase {

    @Test
    public void testUserRegistration() throws InterruptedException {
        app.driver.get("http://localhost:5173/register");
        RegistrationPage regPage = new RegistrationPage(app.driver);

        String email = "tester" + System.currentTimeMillis() + "@gmail.com";


        regPage.register("Ivan Tester", email, "+49123456789", "GanzQA2026!");

        // Ждем 5 секунд, чтобы страница успела переключиться
        WebDriverWait wait = new WebDriverWait(app.driver, Duration.ofSeconds(5));

        try {
            // Пытаемся дождаться появления кнопки Logout
            wait.until(d -> homePage.isLogoutButtonPresent());
        } catch (Exception e) {
            System.out.println("Кнопка Logout не появилась вовремя. Текущий URL: " + app.driver.getCurrentUrl());
        }

        // Финальная проверка
        Assert.assertTrue(homePage.isLogoutButtonPresent(),
                "ОШИБКА: Регистрация прошла, но автоматический вход не выполнен. URL: " + app.driver.getCurrentUrl());
    }
}
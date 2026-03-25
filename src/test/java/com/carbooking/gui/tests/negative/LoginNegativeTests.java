package com.carbooking.gui.tests.negative;

import com.carbooking.gui.core.TestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginNegativeTests extends TestBase {

    @Test(priority = 1)
    public void loginWithInvalidPassword() throws InterruptedException {
        app.driver.get("http://localhost:5173/login");
        // Используем твой реальный email, но заведомо ложный пароль
        loginPage.login("tester1774119303709@gmail.com", "WrongPassword123!");

        Thread.sleep(2000);

        // Проверяем, что кнопка Logout НЕ появилась
        Assert.assertFalse(homePage.isLogoutButtonPresent(),
                "ОШИБКА: Система пустила пользователя с неверным паролем!");
    }

    @Test(priority = 2)
    public void loginWithEmptyEmail() {
        app.driver.get("http://localhost:5173/login");
        loginPage.login("", "GanzQA2026!");

        Assert.assertFalse(homePage.isLogoutButtonPresent(),
                "ОШИБКА: Система позволила вход с пустым полем Email!");
    }

    @Test(priority = 3)
    public void loginWithInvalidEmailFormat() {
        app.driver.get("http://localhost:5173/login");
        loginPage.login("tester.at.gmail.com", "GanzQA2026!");

        Assert.assertFalse(homePage.isLogoutButtonPresent(),
                "ОШИБКА: Система не заблокировала вход с некорректным форматом почты!");
    }
}
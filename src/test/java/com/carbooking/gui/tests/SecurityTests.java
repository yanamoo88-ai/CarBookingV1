package com.carbooking.gui.tests;

import com.carbooking.gui.core.TestBase;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SecurityTests extends TestBase {

    @Test(description = "Выявление угрозы SQL Injection (Риск слива БД)")
    public void findSqlInjectionThreat() {
        app.driver.get("http://localhost:5173/login");
        // Попытка взлома через поле логина
        loginPage.login("' OR 1=1 --", "password");

        // ПРОВЕРКА: Если мы НЕ на странице логина, значит система пустила хакера
        String currentUrl = app.driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("login"),
                "КРИТИЧЕСКАЯ УГРОЗА: SQL-инъекция позволила войти без пароля! Данные пользователей в опасности.");
    }

    @Test(description = "Выявление угрозы IDOR (Риск слива персональных данных)")
    public void findIdorThreat() {
        // 1. Авторизуемся как обычный пользователь
        app.driver.get("http://localhost:5173/login");
        loginPage.login("tester1774119303709@gmail.com", "GanzQA2026!");

        // 2. Пытаемся получить доступ к чужому бронированию по прямому ID
        app.driver.get("http://localhost:5173/bookings/1");

        // 3. ПРОВЕРКА: Должна быть ошибка 403 или текст 'Access Denied'
        String bodyText = app.driver.findElement(By.tagName("body")).getText();
        boolean isProtected = bodyText.contains("403") || bodyText.contains("Access Denied") || bodyText.contains("Forbidden");

        Assert.assertTrue(isProtected,
                "УГРОЗА УТЕЧКИ: Персональные данные других пользователей доступны через манипуляцию ID в URL!");
    }
}
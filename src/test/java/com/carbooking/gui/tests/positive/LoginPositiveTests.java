package com.carbooking.gui.tests.positive;

import com.carbooking.gui.core.TestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginPositiveTests extends TestBase {

    @Test
    public void loginPositiveTest() throws InterruptedException {
        homePage.clickOnLoginLink();
        loginPage.login("tester1774119303709@gmail.com", "GanzQA2026!");
        Thread.sleep(3000);
        Assert.assertTrue(homePage.isLogoutButtonPresent(), "ОШИБКА: Позитивный вход не удался!");
    }
}
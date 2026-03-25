package com.carbooking.gui.tests.negative;

import com.carbooking.gui.core.TestBase;
import com.carbooking.gui.pages.RegistrationPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RegistrationNegativeTests extends TestBase {

    @Test
    public void testRegistrationWithExistingEmail() throws InterruptedException {
        app.driver.get("http://localhost:5173/register");
        RegistrationPage regPage = new RegistrationPage(app.driver);

        String existingEmail = "tester1774119303709@gmail.com";

        regPage.register("Duplicate User", existingEmail, "1234567890", "GanzQA2026!");

        Thread.sleep(2000);

        Assert.assertFalse(homePage.isLogoutButtonPresent(),
                "БАГ: Система позволила регистрацию на дубликат Email!");
    }
}
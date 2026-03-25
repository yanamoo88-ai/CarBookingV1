package com.carbooking.gui.core;

import com.carbooking.gui.fw.ApplicationManager;
import com.carbooking.gui.pages.BookingPage;
import com.carbooking.gui.pages.HomePage;
import com.carbooking.gui.pages.LoginPage;
import com.carbooking.gui.pages.RegistrationPage;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class TestBase {
    protected final ApplicationManager app = new ApplicationManager("chrome");
    protected HomePage homePage;
    protected LoginPage loginPage;
    protected RegistrationPage registrationPage;
    protected BookingPage bookingPage;

    @BeforeMethod
    public void setUp() {
        app.init();
        homePage = new HomePage(app.driver);
        loginPage = new LoginPage(app.driver);
        registrationPage = new RegistrationPage(app.driver);
        bookingPage = new BookingPage(app.driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (!result.isSuccess()) {
            app.takeScreenshot();
        }
        app.stop();
    }
}
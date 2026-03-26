package com.carbooking.gui.core;

import com.carbooking.gui.fw.ApplicationManager;
import com.carbooking.gui.pages.HomePage;
import com.carbooking.gui.pages.LoginPage;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class TestBase {

    protected final ApplicationManager app = new ApplicationManager();
    protected LoginPage loginPage;
    protected HomePage homePage;

    @BeforeMethod
    @Parameters("browser")
    public void setUp(@Optional("chrome") String browser) {
        app.init(browser);
        loginPage = new LoginPage(app.driver);
        homePage = new HomePage(app.driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        app.stop();
    }
}

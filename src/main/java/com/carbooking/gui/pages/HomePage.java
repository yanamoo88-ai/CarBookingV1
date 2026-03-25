package com.carbooking.gui.pages;

import com.carbooking.gui.core.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//h1")
    private WebElement mainHeader;

    @FindBy(xpath = "//*[contains(translate(text(), 'login', 'LOGIN'), 'LOG IN')]")
    private WebElement loginLink;

    @FindBy(xpath = "//*[contains(translate(text(), 'logout', 'LOGOUT'), 'LOG OUT')]")
    private WebElement logoutButton;

    public void clickOnLoginLink() {
        loginLink.click();
    }

    public boolean isHomePageOpened() {
        // Проверяем URL или наличие главного заголовка
        return driver.getCurrentUrl().contains("localhost") || mainHeader.isDisplayed();
    }

    public boolean isLogoutButtonPresent() {
        try {
            return logoutButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickLogout() {
    }
}
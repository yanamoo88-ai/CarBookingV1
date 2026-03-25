package com.carbooking.gui.pages;

import com.carbooking.gui.core.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegistrationPage extends BasePage {
    public RegistrationPage(WebDriver driver) {
        super(driver);
    }

    // Ищем просто все input по порядку
    @FindBy(xpath = "(//input)[1]")
    private WebElement fullNameInput;

    @FindBy(xpath = "(//input)[2]")
    private WebElement emailInput;

    @FindBy(xpath = "(//input)[3]")
    private WebElement phoneInput;

    @FindBy(xpath = "(//input)[4]")
    private WebElement passwordInput;

    @FindBy(xpath = "//button[@type='submit'] | //button[contains(text(),'Sign')]")
    private WebElement signUpButton;

    public void register(String name, String email, String phone, String pass) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Ждем, пока хотя бы один input станет доступен
        wait.until(ExpectedConditions.elementToBeClickable(fullNameInput));

        fullNameInput.sendKeys(name);
        emailInput.sendKeys(email);
        phoneInput.sendKeys(phone);
        passwordInput.sendKeys(pass);
        signUpButton.click();
    }
}
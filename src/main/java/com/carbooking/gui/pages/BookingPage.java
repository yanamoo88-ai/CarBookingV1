package com.carbooking.gui.pages;

import com.carbooking.gui.core.BasePage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class BookingPage extends BasePage {
    public BookingPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//*[contains(text(),'Start Date')]/..//input")
    private WebElement startDateInput;

    @FindBy(xpath = "//*[contains(text(),'End Date')]/..//input")
    private WebElement endDateInput;

    @FindBy(xpath = "//button[contains(.,'Search')]")
    private WebElement searchButton;

    // Ищем любой элемент (кнопку или ссылку), где написано "Book now"
    @FindBy(xpath = "//*[contains(text(),'Book now')] | //button[contains(text(),'Book')]")
    private List<WebElement> bookNowButtons;

    public void fillDatesAndSearch(String start, String end) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(startDateInput));
        startDateInput.clear();
        startDateInput.sendKeys(start);
        endDateInput.clear();
        endDateInput.sendKeys(end);
        searchButton.click();
    }

    public void clickFirstAvailableCar() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        // Ждем, пока кнопки "Book now" появятся в списке
        wait.until(d -> !bookNowButtons.isEmpty());

        WebElement firstBtn = bookNowButtons.get(0);

        // Скроллим прямо к кнопке
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", firstBtn);

        try { Thread.sleep(1000); } catch (InterruptedException e) {}

        // Кликаем через JS (пробивает и ссылки, и кнопки)
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstBtn);
    }

    public boolean isBookingSuccessful() {
        // Успех — это если URL поменялся или на странице появилось "Success", "Confirmed" или "My Bookings"
        String source = driver.getPageSource().toLowerCase();
        return driver.getCurrentUrl().contains("booking") ||
                source.contains("success") ||
                source.contains("confirmed") ||
                source.contains("my bookings");
    }
}
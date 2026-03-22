package com.carbooking.gui.core;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.carbooking.gui.core.ApplicationManager.softly;

public class BasePage {

    public WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void click(WebElement element) {
        element.click();
    }

    public void type(WebElement element, String text) {
        if (text != null) {
            click(element);
            element.clear();
            element.sendKeys(text);
        }
    }

    public boolean shouldHaveText(WebElement element, String text, int time) {
        return wait(time)
                .until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    private WebDriverWait wait(int time) {
        return new WebDriverWait(driver, Duration.ofSeconds(time));
    }

    public void pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isElementDisplayed(WebElement element) {
        try {
            element.isDisplayed();
            return true;
        } catch (NoSuchElementException ex) {
            ex.getMessage();
            return false;
        }
    }

    public void switchToNextWindow(int index) {
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(index));
    }

    public String getValueDomAttribute(String name, WebElement element) {
        return element.getDomAttribute(name);
    }
    public void verifyLinks(String url) {

        try {
            URL linkUrl = new URL(url);

            HttpURLConnection connection = (HttpURLConnection) linkUrl.openConnection();
            connection.setConnectTimeout(5000);
            connection.connect();
            int responseCode = connection.getResponseCode();

            if (responseCode >= 400) {
                softly.fail(url + " - " + connection.getResponseMessage() + "is a broken link");
            } else {
                softly.assertTrue(responseCode < 400, "Expected status code less than 400, but was:" + responseCode);
            }
        } catch (Exception e) {
            System.out.println(url + " - " + e.getMessage() + " ERROR occurred");
        }
        softly.assertAll();
    }

    public boolean isAlertPresent(int time) {
        Alert alert = new WebDriverWait(driver, Duration.ofSeconds(time))
                .until(ExpectedConditions.alertIsPresent());
        if (alert == null) {
            return false;
        }
        return true;
    }

    public boolean waitForElementToAppeared(WebElement element) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOf(element));
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    public boolean waitForElementToDisappear(WebElement element) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.invisibilityOf(element));
            return true;
        } catch (Throwable t) {
            return false;
        }
    }
}
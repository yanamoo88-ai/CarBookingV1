package com.carbooking.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class MyListener implements WebDriverListener {

    Logger logger = LoggerFactory.getLogger(MyListener.class);

    @Override
    public void beforeGet(WebDriver driver, String url) {
        logger.info("Open URL - {}",url);
    }

    @Override
    public void afterGetTitle(WebDriver driver, String result) {
        logger.info("Page title - {}",result);
    }

    @Override
    public void onError(Object target, Method method, Object[] args, InvocationTargetException e) {
        logger.error("Something went wrong!!!");
        logger.error("================================");
        logger.error("Method {}", method);
        logger.error("================================");
        logger.error("Target {}",target);
        logger.error("================================");
        logger.error("Exeption {}",e.getMessage());
    }

    @Override
    public void afterFindElement(WebDriver driver, By locator, WebElement result) {
        logger.info("We found element {}", result.toString());
    }

    @Override
    public void afterFindElements(WebDriver driver, By locator, List<WebElement> result) {
        logger.info("We found elements {}",result.size());
    }

    @Override
    public void beforeClick(WebElement element) {
        logger.info("We click on element {}",element);
    }
}

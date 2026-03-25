package com.carbooking.gui.tests;

import com.carbooking.gui.core.TestBase;
import org.testng.annotations.Test;

public class FirstTest extends TestBase {

    @Test
    public void testOpenBrowser() {
        // Мы просто выводим текст, чтобы убедиться, что код дошел до этой точки
        System.out.println("Сайт открыт, тест прошел успешно!");
    }
}

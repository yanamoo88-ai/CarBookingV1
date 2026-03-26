package com.carbooking.gui.tests.all;

import com.carbooking.gui.tests.SecurityTests;
import com.carbooking.gui.tests.negative.BookingNegativeTests;
import com.carbooking.gui.tests.negative.LoginNegativeTests;
import com.carbooking.gui.tests.positive.BookingPositiveTests;
import com.carbooking.gui.tests.positive.LoginPositiveTests;
import com.carbooking.gui.tests.positive.RegistrationPositiveTests;
import org.testng.TestNG;
import org.testng.annotations.Test;

public class AllTestsRunner {

    @Test
    public void runEverything() {
        TestNG testng = new TestNG();
        // Добавляем сюда все классы тестов
        testng.setTestClasses(new Class[] {
                LoginPositiveTests.class,
                BookingPositiveTests.class,
                RegistrationPositiveTests.class,
                LoginNegativeTests.class,
                BookingNegativeTests.class,
                SecurityTests.class,
                FullWorkflowTest.class
        });
        testng.run();
    }
}

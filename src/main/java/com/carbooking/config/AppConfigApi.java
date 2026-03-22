package com.carbooking.config;

import com.carbooking.utils.PropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.asserts.SoftAssert;

public class AppConfigApi {

    public static Logger logger = LoggerFactory.getLogger(AppConfigApi.class);

    public static SoftAssert softly = new SoftAssert();

    public static String getProperty(String name) {
        return PropertiesLoader.loadProperty(name);
    }

}

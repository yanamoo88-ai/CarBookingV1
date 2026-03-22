package com.carbooking.utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import static com.carbooking.config.AppConfigApi.logger;

public class RetryAnalyser implements IRetryAnalyzer {

    private int retryCount = 0;
    private static final int maxRetryCount = 3;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            logger.info("Retry test " + result.getName() + " with status " + getResultStatusName(result.getStatus()) +
                    " for the " + (this.retryCount + 1) + " times");
            retryCount++;
            return true;
        }
        return false;
    }

    public String getResultStatusName(int status) {
        String resultName = null;
        if (status == 1) {
            resultName = "SUCCESS";
        } if (status == 2) {
            resultName = "FAILED";
        } if (status == 3) {
            resultName = "FAILED";
        }
        return resultName;
    }
}


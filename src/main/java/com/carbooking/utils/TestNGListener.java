package com.carbooking.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestNGListener implements ITestListener {

    private static final Logger log = LoggerFactory.getLogger(TestNGListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        log.info("\n" + "=".repeat(70));
        log.info("****  TEST STARTED: {}", result.getMethod().getMethodName());
        log.info("=".repeat(70));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("✅ TEST PASSED: {}", result.getMethod().getMethodName());
        log.info("-".repeat(70));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("❌ TEST FAILED: {}", result.getMethod().getMethodName());
        log.error("Reason" +
                ": {}", result.getThrowable().getMessage());
        log.error("-".repeat(70));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("⚠️ TEST MISSED: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onStart(ITestContext context) {
        log.info("\n" + "=".repeat(70));
        log.info("**** TEST LAUNCH: {}", context.getName());
        log.info("=".repeat(70));
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("\n" + "=".repeat(70));
        log.info("!!! ALL TESTS COMPLETED");
        log.info("✅ Successfully: {}", context.getPassedTests().size());
        log.info("❌ ERROR: {}", context.getFailedTests().size());
        log.info("⚠️ Missed: {}", context.getSkippedTests().size());
        log.info("=".repeat(70));
    }
}

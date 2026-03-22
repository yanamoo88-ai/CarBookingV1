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
        log.info("🚀 ТЕСТ НАЧАТ: {}", result.getMethod().getMethodName());
        log.info("=".repeat(70));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("✅ ТЕСТ ПРОЙДЕН: {}", result.getMethod().getMethodName());
        log.info("-".repeat(70));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("❌ ТЕСТ ПРОВАЛЕН: {}", result.getMethod().getMethodName());
        log.error("Причина: {}", result.getThrowable().getMessage());
        log.error("-".repeat(70));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("⚠️ ТЕСТ ПРОПУЩЕН: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onStart(ITestContext context) {
        log.info("\n" + "=".repeat(70));
        log.info("🏁 ЗАПУСК ТЕСТОВ: {}", context.getName());
        log.info("=".repeat(70));
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("\n" + "=".repeat(70));
        log.info("🏁 ВСЕ ТЕСТЫ ЗАВЕРШЕНЫ");
        log.info("✅ Успешно: {}", context.getPassedTests().size());
        log.info("❌ Провалено: {}", context.getFailedTests().size());
        log.info("⚠️ Пропущено: {}", context.getSkippedTests().size());
        log.info("=".repeat(70));
    }
}

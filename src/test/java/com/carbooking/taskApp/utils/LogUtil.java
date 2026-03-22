package com.carbooking.taskApp.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {

    private static final Logger log = LoggerFactory.getLogger(LogUtil.class);

    public static void info(String message) {
        log.info("✅ {}", message);
    }

    public static void debug(String message) {
        log.debug("🔍 {}", message);
    }

    public static void warn(String message) {
        log.warn("⚠️ {}", message);
    }

    public static void error(String message) {
        log.error("❌ {}", message);
    }

    public static void step(String message) {
        log.info("\n📌 ШАГ: {}", message);
    }

    public static void apiRequest(String method, String url) {
        log.info("📤 API запрос: {} {}", method, url);
    }

    public static void apiResponse(int statusCode) {
        log.info("📥 API ответ: статус {}", statusCode);
    }

    public static void param(String name, Object value) {
        log.debug(" 📎 Параметр: {} = {}", name, value);
    }

    public static void jsonBody(String json) {
        log.debug(" 📄 Тело: {}", json);
    }
}

package com.ulta.automation.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Thread-safe configuration manager for framework properties.
 * Supports command-line property overriding (e.g., -Denv=live -Dbrowser=edge -Dheadless=false).
 */
public class ConfigReader {
    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static Properties properties;

    static {
        loadProperties();
    }

    private static synchronized void loadProperties() {
        properties = new Properties();
        try (InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is != null) {
                properties.load(is);
                logger.info("Loaded config.properties from classpath.");
            } else {
                try (FileInputStream fis = new FileInputStream("src/main/resources/config.properties")) {
                    properties.load(fis);
                    logger.info("Loaded config.properties from file system.");
                }
            }
        } catch (IOException e) {
            logger.error("Failed to load config.properties", e);
            throw new RuntimeException("Configuration initialization failed", e);
        }
    }

    public static String getProperty(String key) {
        // System property override takes precedence
        String systemVal = System.getProperty(key);
        if (systemVal != null && !systemVal.trim().isEmpty()) {
            return systemVal.trim();
        }
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        String val = getProperty(key);
        return val != null ? val : defaultValue;
    }

    public static String getEnv() {
        return getProperty("env", "mock").toLowerCase();
    }

    public static String getBaseUrl() {
        String env = getEnv();
        if ("live".equalsIgnoreCase(env)) {
            return getProperty("base.url.live", "https://www.ulta.com/");
        } else {
            return getProperty("base.url.mock", "http://localhost:8089/");
        }
    }

    public static String getBrowser() {
        return getProperty("browser", "chrome").toLowerCase();
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless", "true"));
    }

    public static int getImplicitWait() {
        return Integer.parseInt(getProperty("implicit.wait", "10"));
    }

    public static int getExplicitWait() {
        return Integer.parseInt(getProperty("explicit.wait", "15"));
    }

    public static int getPageLoadTimeout() {
        return Integer.parseInt(getProperty("page.load.timeout", "30"));
    }

    public static boolean isSlowMotionEnabled() {
        return Boolean.parseBoolean(getProperty("slow.motion.enabled", "true"));
    }

    public static int getActionDelayMs() {
        return Integer.parseInt(getProperty("action.delay.ms", "1000"));
    }

    public static int getTypingDelayMs() {
        return Integer.parseInt(getProperty("typing.delay.ms", "50"));
    }

    public static boolean isVisualHighlightEnabled() {
        return Boolean.parseBoolean(getProperty("visual.highlight.enabled", "true"));
    }

    public static boolean isScreenshotOnFailure() {
        return Boolean.parseBoolean(getProperty("screenshot.on.failure", "true"));
    }

    public static double getAc5Threshold() {
        return Double.parseDouble(getProperty("ac5.pdp.relevance.threshold", "30.0"));
    }

    public static double getAc6Threshold() {
        return Double.parseDouble(getProperty("ac6.cart.relevance.threshold", "25.0"));
    }

    public static double getAc7Threshold() {
        return Double.parseDouble(getProperty("ac7.e2e.relevance.threshold", "20.0"));
    }

    public static List<String> getKeywords(String category) {
        String raw = getProperty("keywords." + category.toLowerCase().replaceAll("\\s+", ""));
        if (raw == null || raw.trim().isEmpty()) {
            raw = getProperty("keywords.beauty.e2e");
        }
        if (raw != null) {
            return Arrays.stream(raw.split(","))
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}

package com.ulta.automation.utils;

import com.ulta.automation.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Explicit wait utilities for reliable DOM synchronization.
 */
public class WaitUtils {
    private static final Logger logger = LogManager.getLogger(WaitUtils.class);

    public static WebDriverWait getWait(WebDriver driver, int timeoutInSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }

    public static WebDriverWait getDefaultWait(WebDriver driver) {
        return getWait(driver, ConfigReader.getExplicitWait());
    }

    public static WebElement waitForVisibility(WebDriver driver, By locator) {
        try {
            return getDefaultWait(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            logger.warn("Element not visible within timeout: " + locator);
            throw e;
        }
    }

    public static WebElement waitForClickability(WebDriver driver, By locator) {
        try {
            return getDefaultWait(driver).until(ExpectedConditions.elementToBeClickable(locator));
        } catch (Exception e) {
            logger.warn("Element not clickable within timeout: " + locator);
            throw e;
        }
    }

    public static List<WebElement> waitForPresenceOfAll(WebDriver driver, By locator) {
        try {
            return getDefaultWait(driver).until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        } catch (Exception e) {
            logger.warn("Elements not present within timeout: " + locator);
            throw e;
        }
    }

    public static boolean waitForUrlContains(WebDriver driver, String fraction) {
        try {
            return getDefaultWait(driver).until(ExpectedConditions.urlContains(fraction));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Explicitly waits until the browser has completed loading the document DOM.
     */
    public static void waitForPageLoaded(WebDriver driver) {
        try {
            getDefaultWait(driver).until(webDriver ->
                    "complete".equalsIgnoreCase(String.valueOf(
                            ((org.openqa.selenium.JavascriptExecutor) webDriver).executeScript("return document.readyState")
                    ))
            );
        } catch (Exception e) {
            logger.warn("Page load state wait completed or timed out: {}", e.getMessage());
        }
    }

    /**
     * Paces UI interactions when slow-motion mode is enabled so observers can follow test actions.
     */
    public static void applyActionDelay() {
        if (ConfigReader.isSlowMotionEnabled()) {
            pause(ConfigReader.getActionDelayMs());
        }
    }

    public static void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

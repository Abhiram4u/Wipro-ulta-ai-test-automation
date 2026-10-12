package com.ulta.automation.utils;

import com.ulta.automation.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Resilient element interaction wrapper with automated retry, scroll, and JavaScript fallbacks.
 */
public class ElementUtils {
    private static final Logger logger = LogManager.getLogger(ElementUtils.class);

    public static void click(WebDriver driver, By locator) {
        try {
            WebElement element = WaitUtils.waitForClickability(driver, locator);
            JavaScriptUtils.scrollIntoView(driver, element);
            if (ConfigReader.isVisualHighlightEnabled()) {
                JavaScriptUtils.highlightElement(driver, element);
            }
            WaitUtils.applyActionDelay();
            element.click();
            WaitUtils.waitForPageLoaded(driver);
            if (ConfigReader.isVisualHighlightEnabled()) {
                JavaScriptUtils.unhighlightElement(driver, element);
            }
        } catch (Exception e) {
            logger.warn("Standard click failed for locator: {}. Retrying via JavaScript click...", locator);
            try {
                WebElement fallbackElement = driver.findElement(locator);
                JavaScriptUtils.scrollIntoView(driver, fallbackElement);
                if (ConfigReader.isVisualHighlightEnabled()) {
                    JavaScriptUtils.highlightElement(driver, fallbackElement);
                }
                WaitUtils.applyActionDelay();
                JavaScriptUtils.clickByJS(driver, fallbackElement);
                WaitUtils.waitForPageLoaded(driver);
            } catch (Exception ex) {
                logger.error("Click operation completely failed for locator: " + locator, ex);
                throw ex;
            }
        }
    }

    public static void sendKeys(WebDriver driver, By locator, String text) {
        WebElement element = WaitUtils.waitForVisibility(driver, locator);
        JavaScriptUtils.scrollIntoView(driver, element);
        if (ConfigReader.isVisualHighlightEnabled()) {
            JavaScriptUtils.highlightElement(driver, element);
        }
        element.clear();
        
        if (ConfigReader.isSlowMotionEnabled() && ConfigReader.getTypingDelayMs() > 0) {
            for (char ch : text.toCharArray()) {
                element.sendKeys(String.valueOf(ch));
                WaitUtils.pause(ConfigReader.getTypingDelayMs());
            }
        } else {
            element.sendKeys(text);
        }
        
        WaitUtils.applyActionDelay();
        if (ConfigReader.isVisualHighlightEnabled()) {
            JavaScriptUtils.unhighlightElement(driver, element);
        }
    }

    public static String getText(WebDriver driver, By locator) {
        WebElement element = WaitUtils.waitForVisibility(driver, locator);
        return element.getText().trim();
    }

    public static boolean isDisplayed(WebDriver driver, By locator) {
        try {
            WebElement element = WaitUtils.waitForVisibility(driver, locator);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isEnabled(WebDriver driver, By locator) {
        try {
            WebElement element = driver.findElement(locator);
            return element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public static void selectOption(WebDriver driver, By locator, String visibleText) {
        WebElement element = WaitUtils.waitForVisibility(driver, locator);
        Select select = new Select(element);
        select.selectByVisibleText(visibleText);
    }

    public static List<String> getTextsFromList(WebDriver driver, By locator) {
        List<String> texts = new ArrayList<>();
        try {
            List<WebElement> elements = driver.findElements(locator);
            for (WebElement el : elements) {
                String text = el.getText().trim();
                if (!text.isEmpty()) {
                    texts.add(text);
                }
            }
        } catch (Exception e) {
            logger.warn("Could not retrieve texts for locator: " + locator);
        }
        return texts;
    }
}

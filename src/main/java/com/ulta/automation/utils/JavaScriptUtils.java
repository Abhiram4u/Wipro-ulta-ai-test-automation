package com.ulta.automation.utils;

import com.ulta.automation.config.ConfigReader;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * JavaScript utility operations for DOM manipulation and robust clicking.
 */
public class JavaScriptUtils {

    public static Object executeScript(WebDriver driver, String script, Object... args) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js.executeScript(script, args);
    }

    public static void scrollIntoView(WebDriver driver, WebElement element) {
        try {
            executeScript(driver, "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        } catch (Exception ignored) {
            executeScript(driver, "arguments[0].scrollIntoView(true);", element);
        }
    }

    public static void highlightElement(WebDriver driver, WebElement element) {
        try {
            executeScript(driver,
                    "arguments[0].setAttribute('data-original-style', arguments[0].getAttribute('style') || '');" +
                    "arguments[0].style.outline = '3px solid #ff5000';" +
                    "arguments[0].style.boxShadow = '0 0 16px rgba(255, 80, 0, 0.9)';" +
                    "arguments[0].style.borderRadius = '4px';" +
                    "arguments[0].style.transition = 'all 0.3s ease';",
                    element);
        } catch (Exception ignored) {}
    }

    public static void highlightAndDwell(WebDriver driver, WebElement element, long dwellMillis) {
        try {
            scrollIntoView(driver, element);
            highlightElement(driver, element);
            if (!ConfigReader.isHeadless() && dwellMillis > 0) {
                WaitUtils.pause(dwellMillis);
            }
        } catch (Exception ignored) {}
    }

    public static void unhighlightElement(WebDriver driver, WebElement element) {
        try {
            executeScript(driver,
                    "var orig = arguments[0].getAttribute('data-original-style');" +
                    "if (orig !== null) { arguments[0].setAttribute('style', orig); }" +
                    "else { arguments[0].style.outline = ''; arguments[0].style.boxShadow = ''; }",
                    element);
        } catch (Exception ignored) {}
    }

    public static void clickByJS(WebDriver driver, WebElement element) {
        executeScript(driver, "arguments[0].click();", element);
    }

    public static void scrollToBottom(WebDriver driver) {
        executeScript(driver, "window.scrollTo({top: document.body.scrollHeight, behavior: 'smooth'});");
    }

    public static void scrollToTop(WebDriver driver) {
        executeScript(driver, "window.scrollTo({top: 0, behavior: 'smooth'});");
    }

    /**
     * Displays a floating on-screen HUD banner in the browser window showing the active testcase and step.
     * Guarantees a readable dwell time in visible browser mode so observers can follow the test execution.
     */
    public static void displayBanner(WebDriver driver, String acTitle, String stepDetails) {
        displayBanner(driver, acTitle, stepDetails, ConfigReader.getActionDelayMs());
    }

    public static void displayBanner(WebDriver driver, String acTitle, String stepDetails, long dwellMillis) {
        try {
            String script =
                "var banner = document.getElementById('ulta-automation-banner');" +
                "if (!banner) {" +
                "  banner = document.createElement('div');" +
                "  banner.id = 'ulta-automation-banner';" +
                "  banner.style.position = 'fixed';" +
                "  banner.style.top = '14px';" +
                "  banner.style.left = '50%';" +
                "  banner.style.transform = 'translateX(-50%)';" +
                "  banner.style.zIndex = '9999999';" +
                "  banner.style.backgroundColor = '#111827';" +
                "  banner.style.color = '#ffffff';" +
                "  banner.style.padding = '12px 28px';" +
                "  banner.style.borderRadius = '10px';" +
                "  banner.style.boxShadow = '0 8px 32px rgba(0,0,0,0.6), 0 0 16px rgba(255, 80, 0, 0.4)';" +
                "  banner.style.border = '2px solid #ff5000';" +
                "  banner.style.fontFamily = 'Segoe UI, -apple-system, Roboto, sans-serif';" +
                "  banner.style.textAlign = 'center';" +
                "  banner.style.pointerEvents = 'none';" +
                "  banner.style.minWidth = '400px';" +
                "  banner.style.maxWidth = '85vw';" +
                "  banner.style.transition = 'all 0.3s cubic-bezier(0.16, 1, 0.3, 1)';" +
                "  document.body.appendChild(banner);" +
                "} else {" +
                "  banner.style.backgroundColor = '#111827';" +
                "  banner.style.borderColor = '#ff5000';" +
                "  banner.style.boxShadow = '0 8px 32px rgba(0,0,0,0.6), 0 0 16px rgba(255, 80, 0, 0.4)';" +
                "}" +
                "banner.innerHTML = '<div style=\"display:flex; align-items:center; justify-content:center; gap:8px; margin-bottom:5px;\">' +" +
                "  '<span style=\"background:#ff5000; color:#fff; font-size:11px; font-weight:800; padding:3px 9px; border-radius:4px; text-transform:uppercase; letter-spacing:0.5px;\">ACTIVE STEP</span>' +" +
                "  '<span style=\"color:#ff944d; font-size:15px; font-weight:700;\">' + arguments[0] + '</span>' +" +
                "'</div>' +" +
                "'<div style=\"color:#f8fafc; font-size:13.5px; font-weight:500; letter-spacing:0.2px;\">' + arguments[1] + '</div>';";
            executeScript(driver, script, acTitle, stepDetails);

            if (!ConfigReader.isHeadless() && dwellMillis > 0) {
                WaitUtils.pause(dwellMillis);
            }
        } catch (Exception ignored) {}
    }

    /**
     * Displays a distinct completion / success banner when a scenario passes or finishes.
     */
    public static void displaySuccessBanner(WebDriver driver, String acTitle, String stepDetails) {
        try {
            String script =
                "var banner = document.getElementById('ulta-automation-banner');" +
                "if (!banner) {" +
                "  banner = document.createElement('div');" +
                "  banner.id = 'ulta-automation-banner';" +
                "  banner.style.position = 'fixed';" +
                "  banner.style.top = '14px';" +
                "  banner.style.left = '50%';" +
                "  banner.style.transform = 'translateX(-50%)';" +
                "  banner.style.zIndex = '9999999';" +
                "  banner.style.borderRadius = '10px';" +
                "  banner.style.fontFamily = 'Segoe UI, -apple-system, Roboto, sans-serif';" +
                "  banner.style.textAlign = 'center';" +
                "  banner.style.pointerEvents = 'none';" +
                "  banner.style.minWidth = '420px';" +
                "  banner.style.maxWidth = '85vw';" +
                "  banner.style.transition = 'all 0.3s cubic-bezier(0.16, 1, 0.3, 1)';" +
                "  document.body.appendChild(banner);" +
                "}" +
                "banner.style.backgroundColor = '#064e3b';" +
                "banner.style.borderColor = '#10b981';" +
                "banner.style.border = '2px solid #10b981';" +
                "banner.style.padding = '14px 32px';" +
                "banner.style.boxShadow = '0 8px 32px rgba(0,0,0,0.6), 0 0 20px rgba(16, 185, 129, 0.6)';" +
                "banner.innerHTML = '<div style=\"display:flex; align-items:center; justify-content:center; gap:8px; margin-bottom:5px;\">' +" +
                "  '<span style=\"background:#10b981; color:#fff; font-size:11px; font-weight:800; padding:3px 9px; border-radius:4px; text-transform:uppercase;\">PASS &#10004;</span>' +" +
                "  '<span style=\"color:#6ee7b7; font-size:15px; font-weight:700;\">' + arguments[0] + '</span>' +" +
                "'</div>' +" +
                "'<div style=\"color:#ffffff; font-size:14px; font-weight:600;\">' + arguments[1] + '</div>';";
            executeScript(driver, script, acTitle, stepDetails);

            if (!ConfigReader.isHeadless()) {
                WaitUtils.pause(2000);
            }
        } catch (Exception ignored) {}
    }
}

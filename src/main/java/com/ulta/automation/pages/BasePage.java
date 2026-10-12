package com.ulta.automation.pages;

import com.ulta.automation.config.ConfigReader;
import com.ulta.automation.utils.JavaScriptUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;

/**
 * Base Page Object containing common browser operations, popup handling, and screenshot capture.
 */
public abstract class BasePage {
    protected final Logger logger = LogManager.getLogger(getClass());
    protected final WebDriver driver;

    // Common popup / overlay locators on live Ulta (cookie banners, email modals)
    protected final By cookieAcceptButton = By.cssSelector("#onetrust-accept-btn-handler, [aria-label='Accept All Cookies'], button[id*='cookie']");
    protected final By emailModalCloseButton = By.cssSelector("[aria-label='Close'], button.modal-close, .css-close-button");

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    public void dismissPopupsIfPresent() {
        if (!"live".equalsIgnoreCase(ConfigReader.getEnv())) {
            return;
        }

        // Fast check with minimal temporary implicit wait
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        try {
            if (driver.findElements(cookieAcceptButton).size() > 0) {
                WebElement cookieBtn = driver.findElement(cookieAcceptButton);
                if (cookieBtn.isDisplayed()) {
                    JavaScriptUtils.clickByJS(driver, cookieBtn);
                    logger.info("Dismissed cookie consent banner.");
                }
            }
        } catch (Exception ignored) {}

        try {
            if (driver.findElements(emailModalCloseButton).size() > 0) {
                WebElement closeBtn = driver.findElement(emailModalCloseButton);
                if (closeBtn.isDisplayed()) {
                    JavaScriptUtils.clickByJS(driver, closeBtn);
                    logger.info("Dismissed promotional email modal.");
                }
            }
        } catch (Exception ignored) {}

        // Restore configured implicit wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));
    }

    public byte[] takeScreenshotBytes() {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            logger.warn("Could not capture screenshot bytes: {}", e.getMessage());
            return new byte[0];
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public void waitForPageLoaded() {
        com.ulta.automation.utils.WaitUtils.waitForPageLoaded(driver);
    }
}

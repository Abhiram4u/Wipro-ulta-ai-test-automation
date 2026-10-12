package com.ulta.automation.pages;

import com.ulta.automation.config.ConfigReader;
import com.ulta.automation.utils.ElementUtils;
import com.ulta.automation.utils.JavaScriptUtils;
import com.ulta.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Page Object representing Ulta Homepage (AC1, AC2).
 */
public class HomePage extends BasePage {

    // Resilient Locators with support for both Live Ulta.com and Mock Testbed
    private final By searchInput = By.cssSelector("input[type='search'], input[name='q'], [data-testid='search-input'], #search-input");
    private final By searchButton = By.cssSelector("button[type='submit'], [data-testid='search-button'], #search-button, .search-btn");
    private final By accountIcon = By.cssSelector("[data-testid='account-icon'], #account-icon, a[href*='account'], a[href*='signin'], .AccountIcon");
    private final By cartIcon = By.cssSelector("[data-testid='cart-icon'], #cart-icon, a[href*='cart'], .CartIcon");
    private final By cartBadge = By.cssSelector("[data-testid='cart-badge'], #cart-count-badge, .cart-badge, .cart-count");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void navigateTo(String url) {
        logger.info("Navigating to Ulta landing page: {}", url);
        driver.get(url);
        WaitUtils.waitForPageLoaded(driver);
        dismissPopupsIfPresent();
        WaitUtils.applyActionDelay();
    }

    public boolean isSearchBarVisibleAndEnabled() {
        dismissPopupsIfPresent();
        try {
            WebElement input = driver.findElement(searchInput);
            if (ConfigReader.isVisualHighlightEnabled()) {
                JavaScriptUtils.highlightAndDwell(driver, input, 1200);
            }
        } catch (Exception ignored) {}
        boolean visible = ElementUtils.isDisplayed(driver, searchInput);
        boolean enabled = ElementUtils.isEnabled(driver, searchInput);
        logger.info("Search bar status - Visible: [{}], Enabled: [{}]", visible, enabled);
        return visible && enabled;
    }

    public boolean isAccountIconAccessible() {
        dismissPopupsIfPresent();
        try {
            WebElement icon = driver.findElement(accountIcon);
            if (ConfigReader.isVisualHighlightEnabled()) {
                JavaScriptUtils.highlightAndDwell(driver, icon, 1200);
            }
        } catch (Exception ignored) {}
        boolean displayed = ElementUtils.isDisplayed(driver, accountIcon);
        logger.info("Account/Sign-in icon accessible: [{}]", displayed);
        return displayed;
    }

    public SearchResultsPage searchProduct(String query) {
        logger.info("Searching for product query: [{}]", query);
        dismissPopupsIfPresent();
        ElementUtils.sendKeys(driver, searchInput, query);
        try {
            if (driver.findElements(searchButton).size() > 0 && driver.findElement(searchButton).isDisplayed()) {
                ElementUtils.click(driver, searchButton);
            } else {
                WebElement input = driver.findElement(searchInput);
                input.sendKeys(Keys.ENTER);
            }
        } catch (Exception e) {
            WebElement input = driver.findElement(searchInput);
            input.sendKeys(Keys.ENTER);
        }
        WaitUtils.waitForPageLoaded(driver);
        WaitUtils.applyActionDelay();
        return new SearchResultsPage(driver);
    }

    public int getCartBadgeCount() {
        try {
            if (driver.findElements(cartBadge).size() > 0) {
                String text = ElementUtils.getText(driver, cartBadge);
                return Integer.parseInt(text.replaceAll("[^0-9]", ""));
            }
        } catch (Exception e) {
            logger.warn("Could not parse cart badge count, returning 0: {}", e.getMessage());
        }
        return 0;
    }

    public CartPage navigateToCart() {
        logger.info("Navigating to Cart/Bag page.");
        dismissPopupsIfPresent();
        ElementUtils.click(driver, cartIcon);
        WaitUtils.waitForPageLoaded(driver);
        WaitUtils.applyActionDelay();
        return new CartPage(driver);
    }
}

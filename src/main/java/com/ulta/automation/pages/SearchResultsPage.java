package com.ulta.automation.pages;

import com.ulta.automation.config.ConfigReader;
import com.ulta.automation.utils.ElementUtils;
import com.ulta.automation.utils.JavaScriptUtils;
import com.ulta.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object representing Ulta Search Results Page (AC2, AC3).
 */
public class SearchResultsPage extends BasePage {

    private final By productCards = By.cssSelector(".product-card, [data-testid='product-card'], li.ProductCard, .ProductCard");
    private final By firstProductLink = By.cssSelector("[data-testid='product-link'], .product-card a.product-title, .product-title, .ProductCard a");
    private final By resultCountLabel = By.cssSelector("[data-testid='result-count-label'], .result-count, .ProductListingResults__count");
    private final By noResultsMsg = By.cssSelector("[data-testid='no-results-message'], .no-results");

    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }

    public int getProductCardsCount() {
        dismissPopupsIfPresent();
        try {
            WaitUtils.waitForPresenceOfAll(driver, productCards);
            List<WebElement> list = driver.findElements(productCards);
            logger.info("Found [{}] product cards in search results.", list.size());
            return list.size();
        } catch (Exception e) {
            logger.warn("No product cards found within timeout: {}", e.getMessage());
            return 0;
        }
    }

    public boolean hasAtLeastOneProduct() {
        return getProductCardsCount() >= 1;
    }

    public String getFirstProductTitle() {
        dismissPopupsIfPresent();
        try {
            WebElement first = driver.findElement(firstProductLink);
            if (ConfigReader.isVisualHighlightEnabled()) {
                JavaScriptUtils.highlightAndDwell(driver, first, 1200);
            }
        } catch (Exception ignored) {}
        return ElementUtils.getText(driver, firstProductLink);
    }

    public ProductDetailPage clickFirstProduct() {
        logger.info("Clicking the first product in search results.");
        dismissPopupsIfPresent();
        ElementUtils.click(driver, firstProductLink);
        WaitUtils.waitForPageLoaded(driver);
        WaitUtils.applyActionDelay();
        return new ProductDetailPage(driver);
    }

    public boolean isNoResultsDisplayed() {
        return ElementUtils.isDisplayed(driver, noResultsMsg);
    }
}

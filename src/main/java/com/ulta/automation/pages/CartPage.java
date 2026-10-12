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
 * Page Object representing Ulta Shopping Cart / Bag Page (AC4, AC6, AC7).
 */
public class CartPage extends BasePage {

    private final By cartItems = By.cssSelector(".cart-item, [data-testid='cart-item']");
    private final By cartItemTitles = By.cssSelector("[data-testid='cart-item-title'], .cart-item-title");
    private final By cartTotalCount = By.cssSelector("[data-testid='cart-total-count'], #cart-total-count");
    private final By cartRecommendationsSection = By.cssSelector("[data-testid='cart-recommendations-section'], .cart-recommendations-section");
    private final By cartRecommendationTitles = By.cssSelector("[data-testid='cart-recommendation-title'], .cart-rec-title");
    private final By checkoutButton = By.cssSelector("[data-testid='btn-checkout'], .btn-checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int getCartItemsCount() {
        dismissPopupsIfPresent();
        try {
            WaitUtils.waitForPresenceOfAll(driver, cartItems);
            List<WebElement> items = driver.findElements(cartItems);
            if (!items.isEmpty()) {
                JavaScriptUtils.scrollIntoView(driver, items.get(0));
                if (ConfigReader.isVisualHighlightEnabled()) {
                    JavaScriptUtils.highlightElement(driver, items.get(0));
                }
                WaitUtils.applyActionDelay();
            }
            logger.info("Found [{}] items in the cart.", items.size());
            return items.size();
        } catch (Exception e) {
            logger.warn("No cart items found: {}", e.getMessage());
            return 0;
        }
    }

    public List<String> getCartItemTitles() {
        dismissPopupsIfPresent();
        List<String> titles = ElementUtils.getTextsFromList(driver, cartItemTitles);
        logger.info("Cart items list: {}", titles);
        return titles;
    }

    public boolean isCartDisplayingAtLeast(int minCount) {
        return getCartItemsCount() >= minCount;
    }

    public boolean containsItemMatching(String keyword) {
        List<String> titles = getCartItemTitles();
        String lowerKeyword = keyword.toLowerCase().trim();
        for (String title : titles) {
            if (title.toLowerCase().contains(lowerKeyword)) {
                return true;
            }
        }
        return false;
    }

    public boolean isCartRecommendationsSectionLoaded() {
        dismissPopupsIfPresent();
        try {
            WebElement section = WaitUtils.waitForVisibility(driver, cartRecommendationsSection);
            JavaScriptUtils.scrollIntoView(driver, section);
            if (ConfigReader.isVisualHighlightEnabled()) {
                JavaScriptUtils.highlightElement(driver, section);
            }
            WaitUtils.applyActionDelay();
            return section.isDisplayed();
        } catch (Exception e) {
            logger.warn("Cart recommendations section did not load: {}", e.getMessage());
            return false;
        }
    }

    public List<String> getCartRecommendationTitles() {
        isCartRecommendationsSectionLoaded();
        List<String> titles = ElementUtils.getTextsFromList(driver, cartRecommendationTitles);
        logger.info("Extracted [{}] recommendations from Cart page: {}", titles.size(), titles);
        return titles;
    }
}

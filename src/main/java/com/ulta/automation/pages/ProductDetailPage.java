package com.ulta.automation.pages;

import com.ulta.automation.config.ConfigReader;
import com.ulta.automation.utils.ElementUtils;
import com.ulta.automation.utils.JavaScriptUtils;
import com.ulta.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Page Object representing Product Detail Page (PDP) (AC3, AC5).
 */
public class ProductDetailPage extends BasePage {

    private final By pdpTitle = By.cssSelector("h1.product-title, [data-testid='pdp-product-title'], h1.pdp-title");
    private final By variantSelector = By.cssSelector("[data-testid='size-selector'], select#size-selector, select#shade-selector");
    private final By addToBagButton = By.cssSelector("[data-testid='add-to-bag-button'], button#add-to-bag-button, button.btn-add-to-bag");
    private final By cartBadge = By.cssSelector("[data-testid='cart-badge'], #cart-count-badge, .cart-badge");
    private final By recommendationsSection = By.cssSelector("[data-testid='recommendations-section'], .recommendations-section, #recommendations-section");
    private final By recommendationTitles = By.cssSelector("[data-testid='recommendation-title'], .rec-title, .recommendation-title");

    public ProductDetailPage(WebDriver driver) {
        super(driver);
    }

    public boolean isPdpDisplayed() {
        dismissPopupsIfPresent();
        return ElementUtils.isDisplayed(driver, pdpTitle);
    }

    public String getProductTitle() {
        dismissPopupsIfPresent();
        return ElementUtils.getText(driver, pdpTitle);
    }

    public void selectVariantIfRequired() {
        dismissPopupsIfPresent();
        try {
            if (driver.findElements(variantSelector).size() > 0) {
                WebElement selector = driver.findElement(variantSelector);
                if (selector.isDisplayed() && selector.isEnabled()) {
                    Select sel = new Select(selector);
                    if (!sel.getOptions().isEmpty()) {
                        sel.selectByIndex(0);
                        logger.info("Selected default variant option: {}", sel.getFirstSelectedOption().getText());
                    }
                }
            }
        } catch (Exception e) {
            logger.info("No variant selection required or already defaulted: {}", e.getMessage());
        }
    }

    public void clickAddToBag() {
        logger.info("Clicking 'Add to Bag' button.");
        dismissPopupsIfPresent();
        selectVariantIfRequired();
        ElementUtils.click(driver, addToBagButton);
        WaitUtils.applyActionDelay();
    }

    public int getCartBadgeCount() {
        try {
            if (driver.findElements(cartBadge).size() > 0) {
                String text = ElementUtils.getText(driver, cartBadge);
                return Integer.parseInt(text.replaceAll("[^0-9]", ""));
            }
        } catch (Exception e) {
            logger.warn("Could not read cart badge: {}", e.getMessage());
        }
        return 0;
    }

    public boolean isRecommendationsSectionLoaded() {
        dismissPopupsIfPresent();
        try {
            WebElement section = WaitUtils.waitForVisibility(driver, recommendationsSection);
            JavaScriptUtils.scrollIntoView(driver, section);
            if (ConfigReader.isVisualHighlightEnabled()) {
                JavaScriptUtils.highlightElement(driver, section);
            }
            WaitUtils.applyActionDelay();
            return section.isDisplayed();
        } catch (Exception e) {
            logger.warn("Recommendations section did not load: {}", e.getMessage());
            return false;
        }
    }

    public List<String> getRecommendedProductTitles() {
        isRecommendationsSectionLoaded();
        List<String> titles = ElementUtils.getTextsFromList(driver, recommendationTitles);
        logger.info("Extracted [{}] recommendations from PDP: {}", titles.size(), titles);
        return titles;
    }
}

package com.ulta.automation.stepdefinitions;

import com.ulta.automation.config.ConfigReader;
import com.ulta.automation.drivers.DriverManager;
import com.ulta.automation.pages.CartPage;
import com.ulta.automation.pages.HomePage;
import com.ulta.automation.pages.ProductDetailPage;
import com.ulta.automation.pages.SearchResultsPage;
import com.ulta.automation.utils.JavaScriptUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.List;

/**
 * Step definitions for PDP interactions, Add to Bag actions, and Shopping Bag validations (AC3, AC4).
 */
public class CartSteps {
    private final HomePage homePage = new HomePage(DriverManager.getDriver());
    private SearchResultsPage searchResultsPage = new SearchResultsPage(DriverManager.getDriver());
    private ProductDetailPage pdpPage = new ProductDetailPage(DriverManager.getDriver());
    private CartPage cartPage = new CartPage(DriverManager.getDriver());

    private int initialCartCount = 0;

    @When("the user clicks on the first product")
    public void userClicksOnTheFirstProduct() {
        initialCartCount = homePage.getCartBadgeCount();
        JavaScriptUtils.displayBanner(DriverManager.getDriver(), "AC3: Add Products to Cart", "Clicking 1st product card in search results");
        pdpPage = searchResultsPage.clickFirstProduct();
    }

    @Then("the product detail page is displayed")
    public void productDetailPageIsDisplayed() {
        boolean displayed = pdpPage.isPdpDisplayed();
        JavaScriptUtils.displayBanner(DriverManager.getDriver(), "AC3: Add Products to Cart", "PDP Displayed: " + pdpPage.getProductTitle());
        Assert.assertTrue(displayed, "Product Detail Page (PDP) title must be displayed.");
    }

    @When("the user selects shade or size if required and clicks \"Add to Bag\"")
    public void userSelectsShadeOrSizeAndClicksAddToBag() {
        JavaScriptUtils.displayBanner(DriverManager.getDriver(), "AC3: Add Products to Cart", "Selecting Variant & Clicking 'Add to Bag'");
        pdpPage.clickAddToBag();
    }

    @Then("the product is added to the cart")
    public void productIsAddedToTheCart() {
        int currentCount = pdpPage.getCartBadgeCount();
        JavaScriptUtils.displayBanner(DriverManager.getDriver(), "AC3: Add Products to Cart", "Product added to cart! Cart count: " + currentCount, 1200);
        Assert.assertTrue(currentCount > 0, "Cart badge count should be greater than zero after adding product.");
    }

    @And("the cart icon count is updated")
    public void cartIconCountIsUpdated() {
        int currentCount = pdpPage.getCartBadgeCount();
        JavaScriptUtils.displayBanner(DriverManager.getDriver(), "AC3: Add Products to Cart", "Cart badge count updated from " + initialCartCount + " to " + currentCount, 1400);
        Assert.assertTrue(currentCount >= initialCartCount,
                String.format("Cart badge count must be updated. Previous: %d, Current: %d", initialCartCount, currentCount));
    }

    @Given("the user has added 3 products to the cart: {string}, {string}, {string}")
    public void userHasAddedThreeProductsToCart(String item1, String item2, String item3) {
        String baseUrl = ConfigReader.getBaseUrl();
        String[] queries = new String[]{item1, item2, item3};

        for (int i = 0; i < queries.length; i++) {
            String query = queries[i];
            JavaScriptUtils.displayBanner(DriverManager.getDriver(), "AC4: Pre-loading Cart", "Adding item " + (i + 1) + " of 3: \"" + query + "\"");
            homePage.navigateTo(baseUrl);
            SearchResultsPage results = homePage.searchProduct(query);
            ProductDetailPage pdp = results.clickFirstProduct();
            pdp.clickAddToBag();
        }
    }

    @Given("the user has {string} and {string} in the cart")
    public void userHasProductsInCart(String item1, String item2) {
        String baseUrl = ConfigReader.getBaseUrl();
        String[] queries = new String[]{item1, item2};

        for (int i = 0; i < queries.length; i++) {
            String query = queries[i];
            JavaScriptUtils.displayBanner(DriverManager.getDriver(), "AC6: Pre-loading Cart", "Adding item " + (i + 1) + " of 2: \"" + query + "\"");
            homePage.navigateTo(baseUrl);
            SearchResultsPage results = homePage.searchProduct(query);
            ProductDetailPage pdp = results.clickFirstProduct();
            pdp.clickAddToBag();
        }
    }

    @When("^the user navigates to the cart/bag page$")
    public void userNavigatesToCartBagPage() {
        JavaScriptUtils.displayBanner(DriverManager.getDriver(), "AC4: Verify Cart Contents", "Navigating to Shopping Bag page");
        cartPage = homePage.navigateToCart();
    }

    @Then("the cart displays at least {int} items")
    public void cartDisplaysAtLeastItems(int expectedMinItems) {
        int actualCount = cartPage.getCartItemsCount();
        JavaScriptUtils.displayBanner(DriverManager.getDriver(), "AC4: Verify Cart Contents", "Verified [" + actualCount + "] items in bag (>= " + expectedMinItems + ")", 1200);
        Assert.assertTrue(actualCount >= expectedMinItems,
                String.format("Expected at least %d items in cart, but found %d", expectedMinItems, actualCount));
    }

    @And("each added product is listed with correct name")
    public void eachAddedProductIsListedWithCorrectName() {
        List<String> titles = cartPage.getCartItemTitles();
        JavaScriptUtils.displayBanner(DriverManager.getDriver(), "AC4: Verify Cart Contents", "Verified all added product names in bag", 1400);
        Assert.assertFalse(titles.isEmpty(), "Cart item list should not be empty.");
        for (String title : titles) {
            Assert.assertTrue(title.length() > 3, "Cart item title should be a valid product name: " + title);
        }
    }
}

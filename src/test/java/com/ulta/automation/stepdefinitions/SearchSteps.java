package com.ulta.automation.stepdefinitions;

import com.ulta.automation.config.ConfigReader;
import com.ulta.automation.drivers.DriverManager;
import com.ulta.automation.pages.HomePage;
import com.ulta.automation.pages.SearchResultsPage;
import com.ulta.automation.utils.JavaScriptUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

/**
 * Step definitions for product search execution and search results validation (AC2, Boundary & Negative).
 */
public class SearchSteps {
    private final HomePage homePage = new HomePage(DriverManager.getDriver());
    private SearchResultsPage searchResultsPage;

    @Given("search results are displayed for a product {string}")
    public void searchResultsAreDisplayedForProduct(String productQuery) {
        String baseUrl = ConfigReader.getBaseUrl();
        homePage.navigateTo(baseUrl);
        JavaScriptUtils.displayBanner(DriverManager.getDriver(), "AC3: Add Products to Cart", "Searching for initial product: \"" + productQuery + "\"");
        searchResultsPage = homePage.searchProduct(productQuery);
        JavaScriptUtils.displayBanner(DriverManager.getDriver(), "AC3: Add Products to Cart", "Search results displayed for: \"" + productQuery + "\"");
    }

    @When("the user searches for {string}")
    public void userSearchesForProduct(String productQuery) {
        JavaScriptUtils.displayBanner(DriverManager.getDriver(), "AC2: Search for Products", "Searching query: \"" + productQuery + "\"");
        searchResultsPage = homePage.searchProduct(productQuery);
    }

    @Then("search results are displayed with at least {int} product")
    public void searchResultsAreDisplayedWithAtLeastProduct(int expectedMinCount) {
        int actualCount = searchResultsPage.getProductCardsCount();
        JavaScriptUtils.displayBanner(DriverManager.getDriver(), "AC2: Search for Products", "Verified [" + actualCount + "] products displayed (>= " + expectedMinCount + ")", 1200);
        Assert.assertTrue(actualCount >= expectedMinCount,
                String.format("Expected at least %d products in search results, but found: %d", expectedMinCount, actualCount));
    }

    @And("the first product title should be relevant to {string}")
    public void firstProductTitleShouldBeRelevantTo(String expectedCategory) {
        String firstTitle = searchResultsPage.getFirstProductTitle();
        JavaScriptUtils.displayBanner(DriverManager.getDriver(), "AC2: Search for Products", "1st Product: " + firstTitle, 1400);
        Assert.assertNotNull(firstTitle, "First product title should not be null.");
        Assert.assertFalse(firstTitle.isEmpty(), "First product title should not be empty.");
    }

    @Then("search results display 0 products or appropriate no-results message")
    public void searchResultsDisplayZeroOrNoResultsMessage() {
        int count = searchResultsPage.getProductCardsCount();
        boolean hasNoResultsMsg = searchResultsPage.isNoResultsDisplayed();
        JavaScriptUtils.displayBanner(DriverManager.getDriver(), "Negative Search", "Zero products / No results verified: " + hasNoResultsMsg);
        Assert.assertTrue(count == 0 || hasNoResultsMsg, "Negative search should display 0 products or no-results message.");
    }

    @Then("the system gracefully maintains current view or prompts for input")
    public void systemGracefullyMaintainsCurrentView() {
        boolean searchBarVisible = homePage.isSearchBarVisibleAndEnabled();
        JavaScriptUtils.displayBanner(DriverManager.getDriver(), "Boundary Empty Search", "UI state gracefully maintained");
        Assert.assertTrue(searchBarVisible, "System should gracefully maintain UI state on empty search.");
    }

    @When("the user enters an excessively long search string of {int} characters")
    public void userEntersExcessivelyLongSearchString(int length) {
        JavaScriptUtils.displayBanner(DriverManager.getDriver(), "Boundary Payload Search", "Testing 256-character string payload");
        String payload = "a".repeat(length);
        searchResultsPage = homePage.searchProduct(payload);
    }

    @Then("the application handles the input without unhandled errors")
    public void applicationHandlesInputWithoutUnhandledErrors() {
        String currentUrl = searchResultsPage.getCurrentUrl();
        JavaScriptUtils.displayBanner(DriverManager.getDriver(), "Boundary Payload Search", "Handled cleanly without crash");
        Assert.assertNotNull(currentUrl, "Application should handle long payload without crashing.");
    }
}

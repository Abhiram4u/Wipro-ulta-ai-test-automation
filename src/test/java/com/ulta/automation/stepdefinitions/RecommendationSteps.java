package com.ulta.automation.stepdefinitions;

import com.ulta.automation.config.ConfigReader;
import com.ulta.automation.drivers.DriverManager;
import com.ulta.automation.pages.CartPage;
import com.ulta.automation.pages.HomePage;
import com.ulta.automation.pages.ProductDetailPage;
import com.ulta.automation.pages.SearchResultsPage;
import com.ulta.automation.utils.JavaScriptUtils;
import com.ulta.automation.utils.KeywordRelevanceEvaluator;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Step definitions for AI-driven Product Recommendation Relevance verification (AC5, AC6, AC7).
 */
public class RecommendationSteps {
    private static final Logger logger = LogManager.getLogger(RecommendationSteps.class);

    private final HomePage homePage = new HomePage(DriverManager.getDriver());
    private ProductDetailPage pdpPage = new ProductDetailPage(DriverManager.getDriver());
    private CartPage cartPage = new CartPage(DriverManager.getDriver());

    // Holds collected recommendations across sessions for E2E consistency checking (AC7)
    private static final List<String> aggregatedRecommendations = new ArrayList<>();

    @Given("the user is on a product detail page for {string}")
    public void userIsOnProductDetailPageFor(String productQuery) {
        String baseUrl = ConfigReader.getBaseUrl();
        homePage.navigateTo(baseUrl);
        JavaScriptUtils.displayBanner(DriverManager.getDriver(), "AC5: PDP Recommendations", "Navigating to PDP for: \"" + productQuery + "\"");
        SearchResultsPage results = homePage.searchProduct(productQuery);
        pdpPage = results.clickFirstProduct();
        JavaScriptUtils.displayBanner(DriverManager.getDriver(), "AC5: PDP Recommendations", "On PDP: " + pdpPage.getProductTitle());
    }

    @When("the recommendations section loads")
    public void recommendationsSectionLoads() {
        JavaScriptUtils.displayBanner(DriverManager.getDriver(), "AC5: PDP Recommendations", "Loading 'You may also like' recommendations section");
        boolean loaded = pdpPage.isRecommendationsSectionLoaded();
        Assert.assertTrue(loaded, "Recommendations section ('You may also like' / 'Similar products') must load.");
    }

    @Then("at least {int} percent of recommended products contain keywords relevant to the viewed product")
    public void atLeastPercentOfRecommendedProductsContainKeywords(int minPercentThreshold) {
        List<String> titles = pdpPage.getRecommendedProductTitles();
        Assert.assertFalse(titles.isEmpty(), "Recommended product titles list should not be empty.");

        // Cache for AC7
        aggregatedRecommendations.addAll(titles);

        // Retrieve keywords for moisturizer / viewed product
        List<String> keywords = ConfigReader.getKeywords("moisturizer");
        double threshold = (double) minPercentThreshold;

        KeywordRelevanceEvaluator.RelevanceReport report =
                KeywordRelevanceEvaluator.evaluate(titles, keywords, threshold, "AC5: PDP Recommendations");

        JavaScriptUtils.displayBanner(DriverManager.getDriver(), "AC5: PDP Recommendations",
                String.format("Relevance: %.1f%% (Gate: >= %d%%) -> %s", report.getRelevancePercentage(), minPercentThreshold, report.isPassed() ? "PASS ✅" : "FAIL ❌"), 2000);

        Assert.assertTrue(report.isPassed(),
                String.format("AC5 Failed: Expected at least %.1f%% relevant products, but observed %.2f%%. Matched: %s",
                        threshold, report.getRelevancePercentage(), report.getMatchedTitles()));
    }

    @When("the user views the cart page recommendations")
    public void userViewsCartPageRecommendations() {
        JavaScriptUtils.displayBanner(DriverManager.getDriver(), "AC6: Cart Recommendations", "Navigating to Cart recommendations section");
        cartPage = homePage.navigateToCart();
        boolean loaded = cartPage.isCartRecommendationsSectionLoaded();
        Assert.assertTrue(loaded, "Cart page recommendations section must load.");
    }

    @Then("at least {int} percent of recommended products are relevant to the cart items")
    public void atLeastPercentOfRecommendedProductsAreRelevantToCartItems(int minPercentThreshold) {
        List<String> titles = cartPage.getCartRecommendationTitles();
        Assert.assertFalse(titles.isEmpty(), "Cart recommendation titles list should not be empty.");

        // Cache for AC7
        aggregatedRecommendations.addAll(titles);

        // Keywords combined from moisturizer and mascara
        List<String> combinedKeywords = new ArrayList<>();
        combinedKeywords.addAll(ConfigReader.getKeywords("moisturizer"));
        combinedKeywords.addAll(ConfigReader.getKeywords("mascara"));

        double threshold = (double) minPercentThreshold;

        KeywordRelevanceEvaluator.RelevanceReport report =
                KeywordRelevanceEvaluator.evaluate(titles, combinedKeywords, threshold, "AC6: Cart Recommendations");

        JavaScriptUtils.displayBanner(DriverManager.getDriver(), "AC6: Cart Recommendations",
                String.format("Relevance: %.1f%% (Gate: >= %d%%) -> %s", report.getRelevancePercentage(), minPercentThreshold, report.isPassed() ? "PASS ✅" : "FAIL ❌"), 2000);

        Assert.assertTrue(report.isPassed(),
                String.format("AC6 Failed: Expected at least %.1f%% relevant products, but observed %.2f%%. Matched: %s",
                        threshold, report.getRelevancePercentage(), report.getMatchedTitles()));
    }

    @Given("the user has searched for multiple beauty products and added them to cart")
    public void userHasSearchedMultipleBeautyProductsAndAddedToCart() {
        String baseUrl = ConfigReader.getBaseUrl();
        String[] queries = new String[]{"moisturizer", "mascara"};

        for (int i = 0; i < queries.length; i++) {
            String q = queries[i];
            JavaScriptUtils.displayBanner(DriverManager.getDriver(), "AC7: Pre-loading Multi-Product", "Adding: \"" + q + "\" to build recommendation history");
            homePage.navigateTo(baseUrl);
            SearchResultsPage results = homePage.searchProduct(q);
            pdpPage = results.clickFirstProduct();
            // Collect PDP recommendations
            if (pdpPage.isRecommendationsSectionLoaded()) {
                aggregatedRecommendations.addAll(pdpPage.getRecommendedProductTitles());
            }
            pdpPage.clickAddToBag();
        }
    }

    @When("all recommendations from product pages and cart page are collected")
    public void allRecommendationsCollected() {
        JavaScriptUtils.displayBanner(DriverManager.getDriver(), "AC7: E2E Consistency", "Aggregating recommendations across PDPs and Cart");
        cartPage = homePage.navigateToCart();
        if (cartPage.isCartRecommendationsSectionLoaded()) {
            aggregatedRecommendations.addAll(cartPage.getCartRecommendationTitles());
        }
        logger.info("Total aggregated recommendations across touchpoints: {}", aggregatedRecommendations.size());
        Assert.assertFalse(aggregatedRecommendations.isEmpty(), "Aggregated recommendation pool should not be empty.");
    }

    @Then("at least {int} percent of all recommendations fall within the beauty skincare makeup category")
    public void atLeastPercentOfAllRecommendationsFallWithinBeautyCategory(int minPercentThreshold) {
        List<String> globalKeywords = ConfigReader.getKeywords("beauty.e2e");
        double threshold = (double) minPercentThreshold;

        KeywordRelevanceEvaluator.RelevanceReport report =
                KeywordRelevanceEvaluator.evaluate(aggregatedRecommendations, globalKeywords, threshold, "AC7: E2E Category Consistency");

        JavaScriptUtils.displayBanner(DriverManager.getDriver(), "AC7: E2E Consistency",
                String.format("Consistency: %.1f%% (Gate: >= %d%%) -> %s", report.getRelevancePercentage(), minPercentThreshold, report.isPassed() ? "PASS ✅" : "FAIL ❌"), 2000);

        Assert.assertTrue(report.isPassed(),
                String.format("AC7 Failed: Expected at least %.1f%% beauty category consistency, but observed %.2f%%. Matched: %s",
                        threshold, report.getRelevancePercentage(), report.getMatchedTitles()));
    }
}

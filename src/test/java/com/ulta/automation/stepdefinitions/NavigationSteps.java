package com.ulta.automation.stepdefinitions;

import com.ulta.automation.config.ConfigReader;
import com.ulta.automation.drivers.DriverManager;
import com.ulta.automation.pages.HomePage;
import com.ulta.automation.utils.JavaScriptUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

/**
 * Step definitions for landing, navigation, and initial UI accessibility (AC1).
 */
public class NavigationSteps {
    private final HomePage homePage = new HomePage(DriverManager.getDriver());

    @Given("the user launches the Ulta website")
    public void userLaunchesUltaWebsite() {
        String baseUrl = ConfigReader.getBaseUrl();
        homePage.navigateTo(baseUrl);
        JavaScriptUtils.displayBanner(DriverManager.getDriver(), "AC1: Navigate & Landing", "Launched Ulta Beauty Homepage: " + baseUrl);
    }

    @Given("the user is on the homepage")
    public void userIsOnTheHomepage() {
        String baseUrl = ConfigReader.getBaseUrl();
        homePage.navigateTo(baseUrl);
        JavaScriptUtils.displayBanner(DriverManager.getDriver(), "AC2: Search for Products", "User is on Ulta Homepage");
    }

    @When("the homepage loads")
    public void homepageLoads() {
        String currentUrl = homePage.getCurrentUrl();
        JavaScriptUtils.displayBanner(DriverManager.getDriver(), "AC1: Navigate & Landing", "Homepage loaded successfully: " + currentUrl);
        Assert.assertNotNull(currentUrl, "Page URL should not be null.");
        Assert.assertTrue(currentUrl.contains("ulta") || currentUrl.contains("localhost"),
                "Current URL should contain ulta or localhost: " + currentUrl);
    }

    @Then("the search bar is visible and enabled")
    public void searchBarIsVisibleAndEnabled() {
        JavaScriptUtils.displayBanner(DriverManager.getDriver(), "AC1: Navigate & Landing", "Verifying Search Bar is Visible & Enabled", 1200);
        boolean status = homePage.isSearchBarVisibleAndEnabled();
        Assert.assertTrue(status, "Search bar must be visible and enabled on homepage.");
    }

    @Then("^the account/sign-in icon is accessible$")
    public void accountSignInIconIsAccessible() {
        JavaScriptUtils.displayBanner(DriverManager.getDriver(), "AC1: Navigate & Landing", "Verifying Account/Sign-In Navigation Icon", 1200);
        boolean status = homePage.isAccountIconAccessible();
        Assert.assertTrue(status, "Account/Sign-in icon must be accessible on homepage.");
    }
}

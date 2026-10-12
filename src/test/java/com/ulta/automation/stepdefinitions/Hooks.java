package com.ulta.automation.stepdefinitions;

import com.ulta.automation.config.ConfigReader;
import com.ulta.automation.drivers.DriverManager;
import com.ulta.automation.mock.LocalUltaMockServer;
import com.ulta.automation.utils.AIResultAnalyzer;
import com.ulta.automation.utils.JavaScriptUtils;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;

/**
 * Cucumber Lifecycle Hooks for Driver initialization, Mock server management,
 * Allure screenshot attachments, on-screen HUD banners, and automated AI summary generation.
 */
public class Hooks {
    private static final Logger logger = LogManager.getLogger(Hooks.class);

    private static int totalScenariosCount = 0;
    private static int passedScenariosCount = 0;
    private static int failedScenariosCount = 0;

    @BeforeAll
    public static void beforeAllSuite() {
        String env = ConfigReader.getEnv();
        logger.info("Initializing Test Suite under environment: [{}]", env);
        if ("mock".equalsIgnoreCase(env)) {
            LocalUltaMockServer.startServer();
        }
    }

    @Before
    public void setUp(Scenario scenario) {
        logger.info("================================================================================");
        logger.info("STARTING SCENARIO: [{}]", scenario.getName());
        logger.info("================================================================================");
        DriverManager.initDriver();
        try {
            JavaScriptUtils.displayBanner(DriverManager.getDriver(), scenario.getName(), "Initializing step execution...");
        } catch (Exception ignored) {}
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        if (scenario.isFailed() && ConfigReader.isScreenshotOnFailure()) {
            attachScreenshot(scenario, "Step Failure Screenshot");
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        totalScenariosCount++;
        if (scenario.isFailed()) {
            failedScenariosCount++;
            logger.error("SCENARIO FAILED: [{}]", scenario.getName());
            attachScreenshot(scenario, "Final Failure Screenshot");
        } else {
            passedScenariosCount++;
            logger.info("SCENARIO PASSED: [{}]", scenario.getName());
            if (!ConfigReader.isHeadless()) {
                try {
                    JavaScriptUtils.displaySuccessBanner(
                            DriverManager.getDriver(),
                            scenario.getName(),
                            "Scenario Verified & Passed Successfully! Preparing next test..."
                    );
                } catch (Exception ignored) {}
            }
        }
        DriverManager.quitDriver();
    }

    @AfterAll
    public static void afterAllSuite() {
        logger.info("Test execution completed. Triggering AI Result Analyzer...");
        try {
            AIResultAnalyzer.ExecutionStats stats = AIResultAnalyzer.analyzeCucumberReport("target/cucumber-reports/cucumber.json");
            if (stats.totalScenarios == 0 && totalScenariosCount > 0) {
                stats.totalScenarios = totalScenariosCount;
                stats.passedScenarios = passedScenariosCount;
                stats.failedScenarios = failedScenariosCount;
            }
            AIResultAnalyzer.generateAIExecutiveReport(stats, "test-artifacts/AI_TEST_REPORT_SUMMARY.md");
        } catch (Exception e) {
            logger.warn("Could not complete automatic AI result analysis post-suite: {}", e.getMessage());
        }

        if ("mock".equalsIgnoreCase(ConfigReader.getEnv())) {
            LocalUltaMockServer.stopServer();
        }
    }

    private void attachScreenshot(Scenario scenario, String attachmentName) {
        try {
            WebDriver driver = DriverManager.getDriver();
            if (driver != null) {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", attachmentName);
                Allure.addAttachment(attachmentName, "image/png", new ByteArrayInputStream(screenshot), "png");
                logger.info("Attached failure screenshot to Allure report.");
            }
        } catch (Exception e) {
            logger.warn("Failed to capture and attach screenshot: {}", e.getMessage());
        }
    }
}

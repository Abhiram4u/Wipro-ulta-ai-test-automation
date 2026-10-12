package com.ulta.automation.listeners;

import com.ulta.automation.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * TestNG Listener for Allure environment properties injection and lifecycle logging.
 */
public class AllureTestListener implements ITestListener {
    private static final Logger logger = LogManager.getLogger(AllureTestListener.class);

    @Override
    public void onStart(ITestContext context) {
        logger.info("Suite execution started: {}", context.getName());
        writeAllureEnvironmentProperties();
    }

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Executing Test: {}", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test PASSED: {}", result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test FAILED: {}", result.getName(), result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test SKIPPED: {}", result.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Suite execution finished: {}", context.getName());
    }

    private void writeAllureEnvironmentProperties() {
        Properties props = new Properties();
        props.setProperty("Application", "Ulta Beauty");
        props.setProperty("Environment", ConfigReader.getEnv());
        props.setProperty("Base_URL", ConfigReader.getBaseUrl());
        props.setProperty("Browser", ConfigReader.getBrowser());
        props.setProperty("Headless", String.valueOf(ConfigReader.isHeadless()));
        props.setProperty("Java_Version", System.getProperty("java.version"));
        props.setProperty("OS", System.getProperty("os.name"));
        props.setProperty("Framework", "Selenium 4 + Cucumber BDD + TestNG + Allure");
        props.setProperty("AI_Engine", "GitHub Copilot Prompt-Engineered Automation");

        File resultsDir = new File("target/allure-results");
        if (!resultsDir.exists()) {
            resultsDir.mkdirs();
        }

        try (FileOutputStream fos = new FileOutputStream(new File(resultsDir, "environment.properties"))) {
            props.store(fos, "Allure Environment Details");
        } catch (IOException e) {
            logger.warn("Could not write Allure environment.properties: {}", e.getMessage());
        }
    }
}

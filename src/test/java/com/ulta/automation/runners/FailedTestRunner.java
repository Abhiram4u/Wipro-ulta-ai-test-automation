package com.ulta.automation.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * Runner specifically targeted to re-execute failed test scenarios from rerun file.
 */
@CucumberOptions(
        features = "@target/failedrerun.txt",
        glue = {"com.ulta.automation.stepdefinitions"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/rerun-pretty.html",
                "json:target/cucumber-reports/rerun.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true
)
public class FailedTestRunner extends AbstractTestNGCucumberTests {
}

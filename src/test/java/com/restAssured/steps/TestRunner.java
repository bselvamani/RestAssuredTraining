package com.restAssured.steps;

import io.cucumber.testng.*;
import org.testng.annotations.DataProvider;

@CucumberOptions(features = "src/test/resources/features",
        glue = {"com/restAssured/steps"},
        tags = {"not @Ignore"},
        plugin = {"pretty", "html:target/cucumber-html-report", "json:target/cucumber.json"}
)
public class TestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
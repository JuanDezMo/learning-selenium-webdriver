package com.empresa.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.empresa.config.ConfigReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

/**
 * Steps del escenario mínimo que valida el esqueleto del proyecto.
 * Se eliminan cuando existan los escenarios reales de saucedemo.
 */
public class SkeletonSteps {

    private ConfigReader config;

    @Given("the project configuration is loaded")
    public void theProjectConfigurationIsLoaded() {
        config = ConfigReader.load();
    }

    @Then("the base url is {string}")
    public void theBaseUrlIs(String expectedUrl) {
        assertEquals(expectedUrl, config.getRequired("base.url"));
    }
}

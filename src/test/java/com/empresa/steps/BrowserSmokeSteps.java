package com.empresa.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.empresa.driver.DriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

/**
 * Steps de los escenarios que validan el ciclo de vida del navegador.
 * Se eliminan cuando existan los escenarios reales de saucedemo.
 */
public class BrowserSmokeSteps {

    @Given("a browser is opened on a blank page")
    public void aBrowserIsOpenedOnABlankPage() {
        DriverManager.getDriver().get("about:blank");
    }

    @Then("the browser is available")
    public void theBrowserIsAvailable() {
        assertEquals("about:blank", DriverManager.getDriver().getCurrentUrl());
    }
}

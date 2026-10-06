package com.empresa.steps.saucedemo;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.empresa.pages.saucedemo.actions.HeaderActions;
import com.empresa.pages.saucedemo.actions.LoginActions;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/** Steps del menú lateral compartido (disponible en todas las pantallas después del login). */
public class MenuSteps {

    private final HeaderActions headerActions;
    private final LoginActions loginActions;

    public MenuSteps(HeaderActions headerActions, LoginActions loginActions) {
        this.headerActions = headerActions;
        this.loginActions = loginActions;
    }

    @When("the user logs out from the menu")
    public void theUserLogsOutFromTheMenu() {
        headerActions.logout();
    }

    @Then("the login page is displayed")
    public void theLoginPageIsDisplayed() {
        assertTrue(loginActions.isLoginPageDisplayed(), "No se muestra la pantalla de login");
    }
}

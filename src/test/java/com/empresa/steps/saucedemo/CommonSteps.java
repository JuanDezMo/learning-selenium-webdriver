package com.empresa.steps.saucedemo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.empresa.config.TestUsers;
import com.empresa.pages.saucedemo.actions.InventoryActions;
import com.empresa.pages.saucedemo.actions.LoginActions;
import io.cucumber.java.en.Given;

/**
 * Steps que se reutilizan en muchos escenarios de saucedemo.
 *
 * El más importante es la precondición "the user is logged in as": cualquier escenario que
 * necesite un usuario autenticado empieza con ese step, que usa la misma acción de login
 * que los escenarios de login. Así no se repiten pasos de login en cada escenario.
 */
public class CommonSteps {

    private final LoginActions loginActions;
    private final InventoryActions inventoryActions;
    private final TestUsers users;

    public CommonSteps(LoginActions loginActions, InventoryActions inventoryActions, TestUsers users) {
        this.loginActions = loginActions;
        this.inventoryActions = inventoryActions;
        this.users = users;
    }

    @Given("the user is logged in as {string}")
    public void theUserIsLoggedInAs(String userAlias) {
        loginActions.openAndLogin(users.username(userAlias), users.password(userAlias));
        // Si la precondición falla, que el error lo diga aquí y no en un step posterior
        assertEquals("Products", inventoryActions.getPageTitle(),
                "No se pudo iniciar sesión como '" + userAlias + "'");
    }
}

package co.com.learningseleniumwebdriver.steps.saucedemo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import co.com.learningseleniumwebdriver.core.Screenshots;
import co.com.learningseleniumwebdriver.config.TestUsers;
import co.com.learningseleniumwebdriver.pages.saucedemo.actions.InventoryActions;
import co.com.learningseleniumwebdriver.pages.saucedemo.actions.LoginActions;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Steps de los escenarios de login.
 *
 * Los steps son delgados: llaman a las acciones y hacen los asserts.
 * Cucumber crea las acciones y las entrega por el constructor (inyección de dependencias),
 * y todos los steps del mismo escenario comparten las mismas instancias.
 */
public class LoginSteps {

    private final LoginActions loginActions;
    private final InventoryActions inventoryActions;
    private final TestUsers users;

    public LoginSteps(LoginActions loginActions, InventoryActions inventoryActions, TestUsers users) {
        this.loginActions = loginActions;
        this.inventoryActions = inventoryActions;
        this.users = users;
    }

    @Given("the user is on the login page")
    public void theUserIsOnTheLoginPage() {
        loginActions.openLoginPage();
        Screenshots.attach("Login page");
    }

    @When("the user logs in as {string}")
    public void theUserLogsInAs(String userAlias) {
        loginActions.login(users.username(userAlias), users.password(userAlias));
        Screenshots.attach("Login page");
    }

    @When("the user logs in leaving the {string} empty")
    public void theUserLogsInLeavingTheFieldEmpty(String field) {
        // Se usa el usuario estándar y se vacía solo el campo indicado
        String username = field.equals("username") ? "" : users.username("standard");
        String password = field.equals("password") ? "" : users.password("standard");
        loginActions.login(username, password);
    }

    @Then("the products page is displayed")
    public void theProductsPageIsDisplayed() {
        assertEquals("Products", inventoryActions.getPageTitle());
        Screenshots.attach("Products page");
    }

    @Then("the login error message is {string}")
    public void theLoginErrorMessageIs(String expectedMessage) {
        assertEquals(expectedMessage, loginActions.getErrorMessage());
    }
}

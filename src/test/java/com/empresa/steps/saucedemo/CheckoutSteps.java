package com.empresa.steps.saucedemo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.empresa.config.ConfigReader;
import com.empresa.pages.saucedemo.actions.CartActions;
import com.empresa.pages.saucedemo.actions.CheckoutActions;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/** Steps de los escenarios del checkout. Los datos de la persona compradora vienen de la configuración. */
public class CheckoutSteps {

    private final CartActions cartActions;
    private final CheckoutActions checkoutActions;
    private final ConfigReader config = ConfigReader.load();

    public CheckoutSteps(CartActions cartActions, CheckoutActions checkoutActions) {
        this.cartActions = cartActions;
        this.checkoutActions = checkoutActions;
    }

    @When("the user starts the checkout")
    public void theUserStartsTheCheckout() {
        cartActions.startCheckout();
    }

    @When("the user continues the checkout leaving the {string} empty")
    public void theUserContinuesTheCheckoutLeavingTheFieldEmpty(String field) {
        // Se llenan todos los campos con los datos de prueba y se vacía solo el indicado
        String firstName = field.equals("first name") ? "" : config.getRequired("checkout.first.name");
        String lastName = field.equals("last name") ? "" : config.getRequired("checkout.last.name");
        String postalCode = field.equals("postal code") ? "" : config.getRequired("checkout.postal.code");
        checkoutActions.fillInformationAndContinue(firstName, lastName, postalCode);
    }

    @When("the user continues the checkout with valid information")
    public void theUserContinuesTheCheckoutWithValidInformation() {
        checkoutActions.fillInformationAndContinue(
                config.getRequired("checkout.first.name"),
                config.getRequired("checkout.last.name"),
                config.getRequired("checkout.postal.code"));
    }

    @When("the user finishes the purchase")
    public void theUserFinishesThePurchase() {
        checkoutActions.finishPurchase();
    }

    @Then("the order confirmation message is {string}")
    public void theOrderConfirmationMessageIs(String expectedMessage) {
        assertEquals(expectedMessage, checkoutActions.getConfirmationMessage());
    }

    @Then("the checkout error message is {string}")
    public void theCheckoutErrorMessageIs(String expectedMessage) {
        assertEquals(expectedMessage, checkoutActions.getErrorMessage());
    }
}

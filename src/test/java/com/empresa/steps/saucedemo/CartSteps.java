package com.empresa.steps.saucedemo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.empresa.pages.saucedemo.actions.CartActions;
import com.empresa.pages.saucedemo.actions.HeaderActions;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/** Steps de los escenarios del carrito. */
public class CartSteps {

    private final CartActions cartActions;
    private final HeaderActions headerActions;

    public CartSteps(CartActions cartActions, HeaderActions headerActions) {
        this.cartActions = cartActions;
        this.headerActions = headerActions;
    }

    @When("the user opens the cart")
    public void theUserOpensTheCart() {
        headerActions.openCart();
        assertEquals("Your Cart", cartActions.getPageTitle());
    }

    @Then("the cart is empty")
    public void theCartIsEmpty() {
        // El mensaje se arma solo si falla (con una función), para no esperar elementos que no existen
        assertTrue(cartActions.isEmpty(), () -> "El carrito debería estar vacío y contiene: " + cartActions.getProductNames());
    }

    @Then("the cart contains {string}")
    public void theCartContains(String productName) {
        assertTrue(cartActions.getProductNames().contains(productName),
                "El carrito no contiene '" + productName + "'. Contiene: " + cartActions.getProductNames());
    }
}

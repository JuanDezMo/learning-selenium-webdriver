package co.com.learningseleniumwebdriver.steps.saucedemo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.com.learningseleniumwebdriver.pages.saucedemo.actions.HeaderActions;
import co.com.learningseleniumwebdriver.pages.saucedemo.actions.InventoryActions;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Comparator;
import java.util.List;

/** Steps de los escenarios de la pantalla de inventario. */
public class InventorySteps {

    private final InventoryActions inventoryActions;
    private final HeaderActions headerActions;

    public InventorySteps(InventoryActions inventoryActions, HeaderActions headerActions) {
        this.inventoryActions = inventoryActions;
        this.headerActions = headerActions;
    }

    @When("the user sorts the products by {string}")
    public void theUserSortsTheProductsBy(String option) {
        inventoryActions.sortBy(option);
    }

    @Then("the products are listed from the lowest to the highest price")
    public void theProductsAreListedFromTheLowestToTheHighestPrice() {
        List<Double> prices = inventoryActions.getProductPrices();
        List<Double> expected = prices.stream().sorted().toList();
        assertEquals(expected, prices);
    }

    @Then("the products are listed by name from Z to A")
    public void theProductsAreListedByNameFromZToA() {
        List<String> names = inventoryActions.getProductNames();
        List<String> expected = names.stream().sorted(Comparator.reverseOrder()).toList();
        assertEquals(expected, names);
    }

    @When("the user adds {string} to the cart")
    public void theUserAddsToTheCart(String productName) {
        inventoryActions.addToCart(productName);
    }

    @When("the user removes {string} from the cart")
    public void theUserRemovesFromTheCart(String productName) {
        inventoryActions.removeFromCart(productName);
    }

    @Then("the cart badge shows {int}")
    public void theCartBadgeShows(int expectedCount) {
        assertEquals(String.valueOf(expectedCount), headerActions.getCartBadgeText());
    }

    @Then("the cart badge is not displayed")
    public void theCartBadgeIsNotDisplayed() {
        assertTrue(headerActions.waitUntilCartBadgeIsGone(), "El contador del carrito sigue visible");
    }
}

package co.com.learningseleniumwebdriver.pages.saucedemo.locators;

import org.openqa.selenium.By;

/**
 * Localizadores de la pantalla de inventario (lista de productos) de saucedemo.
 * Solo dice dónde está cada elemento; las acciones viven en InventoryActions.
 */
public final class InventoryLocators {

    public static final By PAGE_TITLE = By.cssSelector("[data-test='title']");
    public static final By SORT_DROPDOWN = By.cssSelector("[data-test='product-sort-container']");
    public static final By PRODUCT_NAMES = By.cssSelector("[data-test='inventory-item-name']");
    public static final By PRODUCT_PRICES = By.cssSelector("[data-test='inventory-item-price']");

    private InventoryLocators() {
    }

    /**
     * Localizador dinámico: el botón "Add to cart" de un producto, según su nombre.
     * Ejemplo: "Sauce Labs Backpack" -> add-to-cart-sauce-labs-backpack
     */
    public static By addToCartButton(String productName) {
        return By.cssSelector("[data-test='add-to-cart-" + toSlug(productName) + "']");
    }

    /** Localizador dinámico: el botón "Remove" de un producto, según su nombre. */
    public static By removeButton(String productName) {
        return By.cssSelector("[data-test='remove-" + toSlug(productName) + "']");
    }

    private static String toSlug(String productName) {
        return productName.trim().toLowerCase().replace(' ', '-');
    }
}

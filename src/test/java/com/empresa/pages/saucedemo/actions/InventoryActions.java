package com.empresa.pages.saucedemo.actions;

import static com.empresa.pages.saucedemo.locators.InventoryLocators.PAGE_TITLE;
import static com.empresa.pages.saucedemo.locators.InventoryLocators.PRODUCT_NAMES;
import static com.empresa.pages.saucedemo.locators.InventoryLocators.PRODUCT_PRICES;
import static com.empresa.pages.saucedemo.locators.InventoryLocators.SORT_DROPDOWN;
import static com.empresa.pages.saucedemo.locators.InventoryLocators.addToCartButton;
import static com.empresa.pages.saucedemo.locators.InventoryLocators.removeButton;

import com.empresa.core.BaseActions;
import java.util.List;

/** Acciones de la pantalla de inventario (lista de productos). */
public class InventoryActions extends BaseActions {

    /** Devuelve el título de la pantalla (por ejemplo "Products"); espera a que aparezca. */
    public String getPageTitle() {
        return getText(PAGE_TITLE);
    }

    /** Ordena los productos con la opción indicada, por ejemplo "Price (low to high)". */
    public void sortBy(String optionText) {
        selectByVisibleText(SORT_DROPDOWN, optionText);
    }

    /** Devuelve los precios de los productos en el orden en que se muestran, como números. */
    public List<Double> getProductPrices() {
        return getTexts(PRODUCT_PRICES).stream()
                .map(text -> Double.parseDouble(text.replace("$", "")))
                .toList();
    }

    /** Devuelve los nombres de los productos en el orden en que se muestran. */
    public List<String> getProductNames() {
        return getTexts(PRODUCT_NAMES);
    }

    public void addToCart(String productName) {
        click(addToCartButton(productName));
    }

    public void removeFromCart(String productName) {
        click(removeButton(productName));
    }
}

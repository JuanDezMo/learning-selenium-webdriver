package com.empresa.pages.saucedemo.locators;

import org.openqa.selenium.By;

/**
 * Localizadores de la pantalla de inventario (lista de productos) de saucedemo.
 * Solo dice dónde está cada elemento; las acciones viven en InventoryActions.
 */
public final class InventoryLocators {

    public static final By PAGE_TITLE = By.cssSelector("[data-test='title']");

    private InventoryLocators() {
    }
}

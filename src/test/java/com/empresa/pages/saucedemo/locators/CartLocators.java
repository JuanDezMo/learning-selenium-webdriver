package com.empresa.pages.saucedemo.locators;

import org.openqa.selenium.By;

/** Localizadores de la pantalla del carrito de saucedemo. */
public final class CartLocators {

    public static final By PAGE_TITLE = By.cssSelector("[data-test='title']");
    public static final By ITEM_NAMES = By.cssSelector("[data-test='inventory-item-name']");
    public static final By CHECKOUT_BUTTON = By.cssSelector("[data-test='checkout']");

    private CartLocators() {
    }
}

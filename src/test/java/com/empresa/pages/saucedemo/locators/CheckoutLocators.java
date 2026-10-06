package com.empresa.pages.saucedemo.locators;

import org.openqa.selenium.By;

/** Localizadores del formulario de datos del checkout de saucedemo (paso 1). */
public final class CheckoutLocators {

    public static final By FIRST_NAME_INPUT = By.cssSelector("[data-test='firstName']");
    public static final By LAST_NAME_INPUT = By.cssSelector("[data-test='lastName']");
    public static final By POSTAL_CODE_INPUT = By.cssSelector("[data-test='postalCode']");
    public static final By CONTINUE_BUTTON = By.cssSelector("[data-test='continue']");
    public static final By ERROR_MESSAGE = By.cssSelector("[data-test='error']");

    private CheckoutLocators() {
    }
}

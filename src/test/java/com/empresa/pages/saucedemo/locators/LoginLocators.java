package com.empresa.pages.saucedemo.locators;

import org.openqa.selenium.By;

/**
 * Localizadores de la pantalla de login de saucedemo.
 *
 * Esta clase SOLO dice dónde está cada elemento. No tiene lógica ni usa el navegador.
 * Si cambia el HTML de la pantalla, el único archivo que se modifica es este.
 */
public final class LoginLocators {

    public static final By USERNAME_INPUT = By.id("user-name");
    public static final By PASSWORD_INPUT = By.id("password");
    public static final By LOGIN_BUTTON = By.id("login-button");
    public static final By ERROR_MESSAGE = By.cssSelector("[data-test='error']");

    private LoginLocators() {
    }
}

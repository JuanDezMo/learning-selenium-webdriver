package co.com.learningseleniumwebdriver.pages.saucedemo.locators;

import org.openqa.selenium.By;

/**
 * Localizadores de la cabecera de saucedemo (carrito y menú lateral).
 * La cabecera aparece en todas las pantallas después del login, por eso es un componente compartido.
 */
public final class HeaderLocators {

    public static final By CART_LINK = By.cssSelector("[data-test='shopping-cart-link']");
    public static final By CART_BADGE = By.cssSelector("[data-test='shopping-cart-badge']");
    public static final By MENU_BUTTON = By.id("react-burger-menu-btn");
    public static final By LOGOUT_LINK = By.cssSelector("[data-test='logout-sidebar-link']");

    private HeaderLocators() {
    }
}

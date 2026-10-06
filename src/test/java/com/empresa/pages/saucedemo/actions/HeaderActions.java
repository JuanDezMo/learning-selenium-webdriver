package com.empresa.pages.saucedemo.actions;

import static com.empresa.pages.saucedemo.locators.HeaderLocators.CART_BADGE;
import static com.empresa.pages.saucedemo.locators.HeaderLocators.CART_LINK;
import static com.empresa.pages.saucedemo.locators.HeaderLocators.LOGOUT_LINK;
import static com.empresa.pages.saucedemo.locators.HeaderLocators.MENU_BUTTON;

import com.empresa.core.BaseActions;
import io.qameta.allure.Step;

/**
 * Acciones de la cabecera compartida: contador del carrito, abrir el carrito y menú lateral.
 * Se usa desde cualquier pantalla posterior al login, sin repetir estas acciones en cada una.
 */
public class HeaderActions extends BaseActions {

    /** Espera a que el contador del carrito sea visible y devuelve el número que muestra, como texto. */
    public String getCartBadgeText() {
        return getText(CART_BADGE);
    }

    /** Espera a que el contador del carrito desaparezca; devuelve false si sigue visible. */
    public boolean waitUntilCartBadgeIsGone() {
        return waitUntilGone(CART_BADGE);
    }

    @Step("Open the cart")
    public void openCart() {
        click(CART_LINK);
    }

    /** Abre el menú lateral y elige cerrar sesión. */
    @Step("Log out from the menu")
    public void logout() {
        click(MENU_BUTTON);
        click(LOGOUT_LINK);
    }
}

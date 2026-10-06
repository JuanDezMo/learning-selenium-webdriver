package com.empresa.pages.saucedemo.actions;

import static com.empresa.pages.saucedemo.locators.CartLocators.CHECKOUT_BUTTON;
import static com.empresa.pages.saucedemo.locators.CartLocators.ITEM_NAMES;
import static com.empresa.pages.saucedemo.locators.CartLocators.PAGE_TITLE;

import com.empresa.core.BaseActions;
import java.util.List;

/** Acciones de la pantalla del carrito. Abrir el carrito es una acción de la cabecera (HeaderActions). */
public class CartActions extends BaseActions {

    /** Devuelve el título de la pantalla (por ejemplo "Your Cart"); espera a que aparezca. */
    public String getPageTitle() {
        return getText(PAGE_TITLE);
    }

    /** Devuelve los nombres de los productos que hay en el carrito. */
    public List<String> getProductNames() {
        return getTexts(ITEM_NAMES);
    }

    /** Indica si el carrito no tiene productos (responde de inmediato, sin esperar). */
    public boolean isEmpty() {
        return !isVisible(ITEM_NAMES);
    }

    /** Presiona Checkout para pasar al formulario de datos. */
    public void startCheckout() {
        click(CHECKOUT_BUTTON);
    }
}

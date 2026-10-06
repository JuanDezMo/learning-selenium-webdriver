package com.empresa.pages.saucedemo.actions;

import static com.empresa.pages.saucedemo.locators.InventoryLocators.PAGE_TITLE;

import com.empresa.core.BaseActions;

/**
 * Acciones de la pantalla de inventario (lista de productos).
 * En esta iteración solo se necesita saber si la pantalla está abierta;
 * las demás acciones se agregan en la siguiente iteración.
 */
public class InventoryActions extends BaseActions {

    /** Devuelve el título de la pantalla (por ejemplo "Products"); espera a que aparezca. */
    public String getPageTitle() {
        return getText(PAGE_TITLE);
    }
}

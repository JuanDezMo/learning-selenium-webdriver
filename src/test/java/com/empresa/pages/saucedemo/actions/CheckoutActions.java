package com.empresa.pages.saucedemo.actions;

import static com.empresa.pages.saucedemo.locators.CheckoutLocators.CONFIRMATION_HEADER;
import static com.empresa.pages.saucedemo.locators.CheckoutLocators.CONTINUE_BUTTON;
import static com.empresa.pages.saucedemo.locators.CheckoutLocators.ERROR_MESSAGE;
import static com.empresa.pages.saucedemo.locators.CheckoutLocators.FINISH_BUTTON;
import static com.empresa.pages.saucedemo.locators.CheckoutLocators.FIRST_NAME_INPUT;
import static com.empresa.pages.saucedemo.locators.CheckoutLocators.LAST_NAME_INPUT;
import static com.empresa.pages.saucedemo.locators.CheckoutLocators.POSTAL_CODE_INPUT;

import com.empresa.core.BaseActions;

/** Acciones del checkout: formulario de datos, resumen del pedido y confirmación. */
public class CheckoutActions extends BaseActions {

    /** Llena el formulario y presiona Continue. Cualquier campo puede ir vacío (cadena vacía). */
    public void fillInformationAndContinue(String firstName, String lastName, String postalCode) {
        type(FIRST_NAME_INPUT, firstName);
        type(LAST_NAME_INPUT, lastName);
        type(POSTAL_CODE_INPUT, postalCode);
        click(CONTINUE_BUTTON);
    }

    /** Devuelve el mensaje de error que muestra el formulario. */
    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }

    /** En el resumen del pedido, presiona Finish para confirmar la compra. */
    public void finishPurchase() {
        click(FINISH_BUTTON);
    }

    /** Devuelve el mensaje de confirmación de la compra (por ejemplo "Thank you for your order!"). */
    public String getConfirmationMessage() {
        return getText(CONFIRMATION_HEADER);
    }
}

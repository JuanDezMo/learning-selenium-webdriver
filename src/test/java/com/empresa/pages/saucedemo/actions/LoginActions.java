package com.empresa.pages.saucedemo.actions;

import static com.empresa.pages.saucedemo.locators.LoginLocators.ERROR_MESSAGE;
import static com.empresa.pages.saucedemo.locators.LoginLocators.LOGIN_BUTTON;
import static com.empresa.pages.saucedemo.locators.LoginLocators.PASSWORD_INPUT;
import static com.empresa.pages.saucedemo.locators.LoginLocators.USERNAME_INPUT;

import com.empresa.config.ConfigReader;
import com.empresa.core.BaseActions;
import io.qameta.allure.Step;

/**
 * Acciones de negocio de la pantalla de login.
 *
 * Aquí se dice QUÉ se puede hacer en la pantalla (abrirla, iniciar sesión, leer el error).
 * DÓNDE está cada elemento lo dice LoginLocators. No lleva asserts: esos van en los steps.
 */
public class LoginActions extends BaseActions {

    private final ConfigReader config = ConfigReader.load();

    /** Abre la pantalla de login (la URL base de la aplicación). */
    @Step("Open the login page")
    public void openLoginPage() {
        open(config.getRequired("base.url"));
    }

    /** Escribe las credenciales y presiona Login. Los campos pueden ir vacíos (cadena vacía). */
    @Step("Log in as {username}")
    public void login(String username, String password) {
        type(USERNAME_INPUT, username);
        type(PASSWORD_INPUT, password);
        click(LOGIN_BUTTON);
    }

    /**
     * Abre el login e inicia sesión en un solo paso.
     * Es la precondición reutilizable de cualquier escenario que necesite un usuario autenticado.
     */
    @Step("Open the login page and log in as {username}")
    public void openAndLogin(String username, String password) {
        openLoginPage();
        login(username, password);
    }

    /** Espera a que se muestre el formulario de login; falla con un mensaje claro si no aparece. */
    public boolean isLoginPageDisplayed() {
        return waitUntilVisible(LOGIN_BUTTON).isDisplayed();
    }

    /** Devuelve el mensaje de error que muestra la pantalla de login. */
    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }
}

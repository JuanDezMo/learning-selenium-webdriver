package com.empresa.steps.common;

import com.empresa.driver.DriverManager;
import io.cucumber.java.After;

/**
 * Acciones que Cucumber ejecuta automáticamente antes o después de cada escenario.
 *
 * El navegador se abre la primera vez que un step lo pide (DriverManager.getDriver())
 * y se cierra aquí siempre al terminar el escenario, haya pasado o fallado.
 * Así cada escenario empieza con un navegador nuevo y no quedan procesos abiertos.
 */
public class Hooks {

    @After(order = 0)
    public void closeBrowser() {
        DriverManager.quitDriver();
    }
}

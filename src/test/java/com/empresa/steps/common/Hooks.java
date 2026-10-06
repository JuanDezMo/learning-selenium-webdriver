package com.empresa.steps.common;

import com.empresa.driver.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

/**
 * Acciones que Cucumber ejecuta automáticamente antes o después de cada escenario.
 *
 * El navegador se abre la primera vez que un step lo pide (DriverManager.getDriver())
 * y se cierra aquí siempre al terminar el escenario, haya pasado o fallado.
 * Así cada escenario empieza con un navegador nuevo y no quedan procesos abiertos.
 *
 * Si el escenario falla, antes de cerrar el navegador se adjunta una captura de pantalla al
 * reporte de Allure. Es el único lugar donde se toma la captura.
 */
public class Hooks {

    /** Se ejecuta antes que closeBrowser (en @After, el número mayor corre primero). */
    @After(order = 10)
    public void attachScreenshotOnFailure(Scenario scenario) {
        if (!scenario.isFailed() || !DriverManager.hasDriver()) {
            return;
        }
        try {
            byte[] png = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Screenshot on failure", "image/png", new ByteArrayInputStream(png), "png");
        } catch (RuntimeException e) {
            // Una captura fallida no debe ocultar el error original del escenario
            System.err.println("No se pudo tomar la captura de pantalla: " + e.getMessage());
        }
    }

    @After(order = 0)
    public void closeBrowser() {
        DriverManager.quitDriver();
    }
}

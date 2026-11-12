package co.com.learningseleniumwebdriver.steps.common;

import co.com.learningseleniumwebdriver.core.Screenshots;
import co.com.learningseleniumwebdriver.driver.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Scenario;

/**
 * Acciones que Cucumber ejecuta automáticamente antes o después de cada escenario.
 *
 * El navegador se abre la primera vez que un step lo pide (DriverManager.getDriver())
 * y se cierra aquí siempre al terminar el escenario, haya pasado o fallado.
 * Así cada escenario empieza con un navegador nuevo y no quedan procesos abiertos.
 *
 * Si el escenario falla, antes de cerrar el navegador se adjunta una captura de pantalla al
 * reporte de Allure (ver Screenshots). Los steps pueden adjuntar capturas de evidencia adicionales.
 */
public class Hooks {

    /** En los escenarios con la etiqueta @screenshots adjunta una captura después de cada step. */
    @AfterStep
    public void attachScreenshotAfterStep(Scenario scenario) {
        if (scenario.getSourceTagNames().contains("@screenshots")) {
            Screenshots.attach("Step screenshot");
        }
    }

    /** Se ejecuta antes que closeBrowser (en @After, el número mayor corre primero). */
    @After(order = 10)
    public void attachScreenshotOnFailure(Scenario scenario) {
        if (!scenario.isFailed() || !DriverManager.hasDriver()) {
            return;
        }
        Screenshots.attach("Screenshot on failure");
    }

    @After(order = 0)
    public void closeBrowser() {
        DriverManager.quitDriver();
    }
}

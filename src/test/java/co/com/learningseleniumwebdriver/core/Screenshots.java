package co.com.learningseleniumwebdriver.core;

import co.com.learningseleniumwebdriver.driver.DriverManager;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

/** Adjunta capturas de pantalla al reporte de Allure. */
public final class Screenshots {

    private Screenshots() {
    }

    /** Toma una captura del navegador actual y la adjunta al step en curso. */
    public static void attach(String name) {
        if (!DriverManager.hasDriver()) {
            return;
        }
        try {
            byte[] png = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(name, "image/png", new ByteArrayInputStream(png), "png");
        } catch (RuntimeException e) {
            // Una captura fallida no debe ocultar el error original del escenario
            System.err.println("No se pudo tomar la captura de pantalla: " + e.getMessage());
        }
    }
}

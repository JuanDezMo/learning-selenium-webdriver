package com.empresa.driver;

import com.empresa.config.ConfigReader;
import org.openqa.selenium.WebDriver;

/**
 * Entrega a cada hilo su propio navegador y lo cierra al terminar.
 *
 * Las pruebas, steps y acciones NUNCA crean el navegador con "new ChromeDriver()":
 * siempre lo piden aquí con DriverManager.getDriver().
 * El navegador se guarda en un ThreadLocal, por eso dos hilos nunca comparten instancia
 * (esto permite ejecutar escenarios en paralelo más adelante).
 */
public class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
    }

    /** Devuelve el navegador del hilo actual; lo crea con la configuración del sistema si aún no existe. */
    public static WebDriver getDriver() {
        return getDriver(ConfigReader.load());
    }

    /** Igual que getDriver(), pero con una configuración indicada (útil en pruebas). */
    public static WebDriver getDriver(ConfigReader config) {
        if (DRIVER.get() == null) {
            DRIVER.set(DriverFactory.create(config));
        }
        return DRIVER.get();
    }

    /** Indica si el hilo actual ya tiene un navegador abierto. */
    public static boolean hasDriver() {
        return DRIVER.get() != null;
    }

    /** Cierra el navegador del hilo actual (si existe) y libera el ThreadLocal. */
    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                DRIVER.remove();
            }
        }
    }
}

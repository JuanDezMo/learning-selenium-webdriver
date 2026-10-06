package com.empresa.driver;

import com.empresa.config.ConfigReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Crea un navegador nuevo según la configuración (navegador, modo local o remoto, headless).
 * No guarda nada: de guardar el navegador por hilo se encarga DriverManager.
 */
public class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver create(ConfigReader config) {
        // Se valida todo antes de abrir ningún navegador
        BrowserType browser = BrowserType.fromString(config.get("browser", "chrome"));
        boolean headless = config.getBoolean("headless", false);
        String mode = config.get("mode", "local").trim().toLowerCase();

        return switch (mode) {
            case "local" -> createLocal(browser, headless);
            case "remote" -> createRemote(browser, headless, config.get("remote.url", "http://localhost:4444"));
            default -> throw new IllegalArgumentException(
                    "Modo de ejecución no soportado: '" + mode + "'. Valores permitidos: local, remote");
        };
    }

    private static WebDriver createLocal(BrowserType browser, boolean headless) {
        // Selenium Manager descarga el driver necesario; no hay que instalar binarios
        WebDriver driver = switch (browser) {
            case CHROME -> new ChromeDriver(chromeOptions(headless));
            case FIREFOX -> new FirefoxDriver(firefoxOptions(headless));
        };
        if (!headless) {
            driver.manage().window().maximize();
        }
        return driver;
    }

    private static WebDriver createRemote(BrowserType browser, boolean headless, String remoteUrl) {
        MutableCapabilities options = switch (browser) {
            case CHROME -> chromeOptions(headless);
            case FIREFOX -> firefoxOptions(headless);
        };
        try {
            return new RemoteWebDriver(URI.create(remoteUrl).toURL(), options);
        } catch (MalformedURLException | IllegalArgumentException e) {
            throw new IllegalStateException("La URL del servidor remoto no es válida: " + remoteUrl, e);
        } catch (RuntimeException e) {
            throw new IllegalStateException(
                    "No se pudo crear la sesión en el servidor remoto de navegadores: " + remoteUrl, e);
        }
    }

    private static ChromeOptions chromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        // Chrome reconoce como filtradas las credenciales públicas de demostración (por ejemplo las de
        // saucedemo) y muestra un aviso "Cambia tu contraseña" que bloquea toda la entrada a la página.
        // Se desactiva el gestor de contraseñas para que ningún aviso interfiera con las pruebas.
        Map<String, Object> preferences = new HashMap<>();
        preferences.put("credentials_enable_service", false);
        preferences.put("profile.password_manager_enabled", false);
        preferences.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", preferences);
        if (headless) {
            options.addArguments("--headless=new", "--window-size=1920,1080");
        }
        return options;
    }

    private static FirefoxOptions firefoxOptions(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("-headless", "--width=1920", "--height=1080");
        }
        return options;
    }
}

package com.empresa.driver;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.empresa.config.ConfigReader;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/** Pruebas de DriverManager con un navegador real (siempre headless para no abrir ventanas). */
class DriverManagerTest {

    private final ConfigReader headlessConfig = new ConfigReader(Map.of("headless", "true"), Map.of());

    @AfterEach
    void cleanUp() {
        DriverManager.quitDriver();
    }

    @Test
    void opensAndClosesTheBrowser() {
        WebDriver driver = DriverManager.getDriver(headlessConfig);
        driver.get("about:blank");
        assertTrue(DriverManager.hasDriver());

        DriverManager.quitDriver();

        assertFalse(DriverManager.hasDriver());
    }

    @Test
    void sameThreadGetsTheSameInstance() {
        WebDriver first = DriverManager.getDriver(headlessConfig);
        WebDriver second = DriverManager.getDriver(headlessConfig);

        assertSame(first, second);
    }

    @Test
    void differentThreadsGetDifferentInstances() throws InterruptedException {
        WebDriver mainThreadDriver = DriverManager.getDriver(headlessConfig);
        AtomicReference<WebDriver> otherThreadDriver = new AtomicReference<>();

        Thread other = new Thread(() -> {
            otherThreadDriver.set(DriverManager.getDriver(headlessConfig));
            DriverManager.quitDriver();
        });
        other.start();
        other.join();

        assertNotSame(mainThreadDriver, otherThreadDriver.get());
    }
}

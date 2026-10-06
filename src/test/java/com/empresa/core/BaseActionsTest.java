package com.empresa.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.empresa.config.ConfigReader;
import com.empresa.driver.DriverManager;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

/** Pruebas de BaseActions contra una página local (no depende de internet). */
class BaseActionsTest {

    private BaseActions actions;

    @BeforeEach
    void openSamplePage() {
        ConfigReader config = new ConfigReader(Map.of("headless", "true"), Map.of());
        actions = new BaseActions(DriverManager.getDriver(config), 5);
        actions.open(getClass().getResource("/fixtures/sample.html").toString());
    }

    @AfterEach
    void cleanUp() {
        DriverManager.quitDriver();
    }

    @Test
    void typesAndClicksAndReadsText() {
        actions.type(By.id("name"), "Ana");
        actions.click(By.id("btn"));

        assertEquals("Clicked Ana", actions.getText(By.id("msg")));
    }

    @Test
    void waitsForAnElementThatAppearsLater() {
        // El elemento aparece a los 800 ms; la espera explícita lo encuentra sin pausas fijas
        assertEquals("Late", actions.getText(By.id("late")));
    }

    @Test
    void isVisibleDoesNotWait() {
        assertTrue(actions.isVisible(By.id("btn")));
        assertFalse(actions.isVisible(By.id("hidden")));
        assertFalse(actions.isVisible(By.id("no-existe")));
    }

    @Test
    void failsWithLocatorAndTimeWhenTheElementNeverAppears() {
        BaseActions shortWait = new BaseActions(DriverManager.getDriver(), 1);

        TimeoutException error = assertThrows(TimeoutException.class,
                () -> shortWait.getText(By.id("never")));

        assertTrue(error.getMessage().contains("By.id: never"), error.getMessage());
        assertTrue(error.getMessage().contains("1 segundos"), error.getMessage());
    }
}

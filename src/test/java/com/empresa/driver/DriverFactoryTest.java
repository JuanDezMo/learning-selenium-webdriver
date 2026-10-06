package com.empresa.driver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.empresa.config.ConfigReader;
import java.util.Map;
import org.junit.jupiter.api.Test;

/** Pruebas de DriverFactory y BrowserType que no abren ningún navegador. */
class DriverFactoryTest {

    @Test
    void browserNameIsCaseInsensitive() {
        assertEquals(BrowserType.CHROME, BrowserType.fromString("Chrome"));
        assertEquals(BrowserType.FIREFOX, BrowserType.fromString(" FIREFOX "));
    }

    @Test
    void unsupportedBrowserFailsListingAllowedValues() {
        ConfigReader config = new ConfigReader(Map.of("browser", "safari"), Map.of());

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> DriverFactory.create(config));

        assertTrue(error.getMessage().contains("safari"));
        assertTrue(error.getMessage().contains("chrome"));
        assertTrue(error.getMessage().contains("firefox"));
    }

    @Test
    void unsupportedModeFailsListingAllowedValues() {
        ConfigReader config = new ConfigReader(Map.of("mode", "nube"), Map.of());

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> DriverFactory.create(config));

        assertTrue(error.getMessage().contains("local"));
        assertTrue(error.getMessage().contains("remote"));
    }

    @Test
    void unreachableRemoteServerFailsMentioningTheUrl() {
        ConfigReader config = new ConfigReader(
                Map.of("mode", "remote", "remote.url", "http://localhost:1"), Map.of());

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> DriverFactory.create(config));

        assertTrue(error.getMessage().contains("http://localhost:1"));
    }

    @Test
    void invalidRemoteUrlFailsMentioningTheUrl() {
        ConfigReader config = new ConfigReader(
                Map.of("mode", "remote", "remote.url", "esto no es una url"), Map.of());

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> DriverFactory.create(config));

        assertTrue(error.getMessage().contains("esto no es una url"));
    }
}

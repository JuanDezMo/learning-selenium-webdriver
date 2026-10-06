package com.empresa.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import org.junit.jupiter.api.Test;

class ConfigReaderTest {

    @Test
    void readsValueFromPropertiesFile() {
        ConfigReader config = new ConfigReader(Map.of(), Map.of());

        assertEquals("https://www.saucedemo.com", config.getRequired("base.url"));
    }

    @Test
    void systemPropertyOverridesFile() {
        ConfigReader config = new ConfigReader(Map.of("base.url", "https://desde-sistema"), Map.of());

        assertEquals("https://desde-sistema", config.getRequired("base.url"));
    }

    @Test
    void environmentVariableOverridesFile() {
        ConfigReader config = new ConfigReader(Map.of(), Map.of("BASE_URL", "https://desde-entorno"));

        assertEquals("https://desde-entorno", config.getRequired("base.url"));
    }

    @Test
    void systemPropertyOverridesEnvironmentVariable() {
        ConfigReader config = new ConfigReader(
                Map.of("base.url", "https://desde-sistema"),
                Map.of("BASE_URL", "https://desde-entorno"));

        assertEquals("https://desde-sistema", config.getRequired("base.url"));
    }

    @Test
    void missingRequiredPropertyNamesTheProperty() {
        ConfigReader config = new ConfigReader(Map.of(), Map.of());

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> config.getRequired("propiedad.inexistente"));

        assertTrue(error.getMessage().contains("propiedad.inexistente"));
    }

    @Test
    void usesDefaultsFromDefaultFile() {
        ConfigReader config = new ConfigReader(Map.of(), Map.of());

        assertEquals("chrome", config.get("browser", "otro"));
        assertEquals(10, config.getInt("timeout.seconds", 99));
    }

    @Test
    void invalidIntegerFailsWithClearMessage() {
        ConfigReader config = new ConfigReader(Map.of("timeout.seconds", "abc"), Map.of());

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> config.getInt("timeout.seconds", 10));

        assertTrue(error.getMessage().contains("timeout.seconds"));
    }
}

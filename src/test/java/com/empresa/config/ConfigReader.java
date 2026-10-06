package com.empresa.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * Lee la configuración de las pruebas.
 *
 * Orden de prioridad (el primero que tenga el valor gana):
 *   1. Propiedades de sistema, por ejemplo: ./gradlew test -Dbrowser=firefox
 *   2. Variables de entorno con el prefijo TEST_, el nombre en mayúsculas y "_" en lugar de ".",
 *      por ejemplo: TEST_BASE_URL para la propiedad base.url o TEST_BROWSER para browser.
 *      El prefijo evita chocar con variables del sistema que ya existen (por ejemplo BROWSER).
 *   3. Archivos de propiedades en src/test/resources/config, cargados en este orden
 *      (el último pisa al anterior):
 *        default.properties -> {app}.properties -> {app}-{env}.properties
 *
 * La aplicación se elige con -Dapp (por defecto "saucedemo") y el ambiente con -Denv
 * (por defecto "local").
 */
public class ConfigReader {

    public static final String DEFAULT_APP = "saucedemo";
    public static final String DEFAULT_ENV = "local";
    private static final String ENVIRONMENT_PREFIX = "TEST_";

    private final Properties fileProperties = new Properties();
    private final Map<String, String> systemProperties;
    private final Map<String, String> environmentVariables;

    /** Crea un lector con los valores reales del sistema. */
    public static ConfigReader load() {
        Map<String, String> systemProps = new java.util.HashMap<>();
        System.getProperties().forEach((key, value) -> systemProps.put(key.toString(), value.toString()));
        return new ConfigReader(systemProps, System.getenv());
    }

    /** Constructor con los valores inyectados; permite probar la prioridad sin tocar el sistema. */
    public ConfigReader(Map<String, String> systemProperties, Map<String, String> environmentVariables) {
        this.systemProperties = systemProperties;
        this.environmentVariables = environmentVariables;

        String app = systemProperties.getOrDefault("app", DEFAULT_APP);
        String env = systemProperties.getOrDefault("env", DEFAULT_ENV);

        loadFile("config/default.properties");
        loadFile("config/" + app + ".properties");
        loadFile("config/" + app + "-" + env + ".properties");
    }

    /** Devuelve el valor de la propiedad o falla con un mensaje que nombra la propiedad faltante. */
    public String getRequired(String key) {
        String value = find(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Falta la propiedad obligatoria '" + key
                    + "'. Defínela en src/test/resources/config o con -D" + key + "=valor");
        }
        return value;
    }

    /** Devuelve el valor de la propiedad o el valor por defecto si no existe. */
    public String get(String key, String defaultValue) {
        String value = find(key);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }

    public int getInt(String key, int defaultValue) {
        String value = find(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalStateException("La propiedad '" + key + "' debe ser un número entero y es '" + value + "'");
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = find(key);
        return (value == null || value.isBlank()) ? defaultValue : Boolean.parseBoolean(value.trim());
    }

    private String find(String key) {
        String fromSystem = systemProperties.get(key);
        if (fromSystem != null) {
            return fromSystem;
        }
        String fromEnvironment = environmentVariables.get(toEnvironmentName(key));
        if (fromEnvironment != null) {
            return fromEnvironment;
        }
        return fileProperties.getProperty(key);
    }

    private static String toEnvironmentName(String key) {
        return ENVIRONMENT_PREFIX + key.toUpperCase().replace('.', '_');
    }

    private void loadFile(String resourcePath) {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (input != null) {
                fileProperties.load(input);
            }
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer el archivo de configuración " + resourcePath, e);
        }
    }
}

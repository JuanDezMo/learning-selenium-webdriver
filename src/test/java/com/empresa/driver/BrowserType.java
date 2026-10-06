package com.empresa.driver;

import java.util.Arrays;
import java.util.stream.Collectors;

/** Navegadores soportados por la base. Para agregar uno nuevo, se agrega aquí y en DriverFactory. */
public enum BrowserType {
    CHROME,
    FIREFOX;

    /** Convierte el texto de la configuración en un navegador; falla indicando los valores permitidos. */
    public static BrowserType fromString(String value) {
        if (value != null) {
            for (BrowserType type : values()) {
                if (type.name().equalsIgnoreCase(value.trim())) {
                    return type;
                }
            }
        }
        String allowed = Arrays.stream(values())
                .map(type -> type.name().toLowerCase())
                .collect(Collectors.joining(", "));
        throw new IllegalArgumentException(
                "Navegador no soportado: '" + value + "'. Valores permitidos: " + allowed);
    }
}

package com.empresa.runner;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

/**
 * Punto de entrada de los escenarios de saucedemo.
 *
 * Cada aplicación tiene su propio runner con el nombre Run<Aplicacion>Test. Así los steps y los
 * escenarios de una aplicación nunca se mezclan con los de otra.
 *   - Escenarios: carpeta features/saucedemo
 *   - Steps: paquete com.empresa.steps.saucedemo y los comunes (hooks) de com.empresa.steps.common
 *
 * Para otra aplicación se copia este archivo y se cambia el nombre, la carpeta de features y el
 * paquete de steps (ver docs/guia-patron-pom.md).
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/saucedemo")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.empresa.steps.common,com.empresa.steps.saucedemo")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
public class RunSaucedemoTest {
}

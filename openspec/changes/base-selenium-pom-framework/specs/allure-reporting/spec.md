# Spec Delta

## Purpose

Define el reporte de resultados con Allure, con pasos legibles y evidencia visual en los fallos, generado desde un único punto de enganche en el ciclo de vida de los escenarios.

## ADDED Requirements

### Requirement: Generación de resultados Allure
Cada ejecución SHALL producir resultados compatibles con Allure para todos los escenarios ejecutados.

#### Scenario: Ejecución completa
- **WHEN** termina una ejecución de pruebas
- **THEN** los resultados quedan disponibles en un directorio conocido

### Requirement: Reporte HTML
El proyecto SHALL permitir generar y consultar el reporte HTML de Allure mediante un comando de Gradle.

#### Scenario: Generar el reporte
- **WHEN** se ejecuta el comando de generación del reporte tras una ejecución
- **THEN** se obtiene un reporte HTML navegable con los escenarios y su resultado

### Requirement: Pasos legibles
El reporte SHALL mostrar los pasos de cada escenario con el texto de Gherkin y las acciones de negocio relevantes.

#### Scenario: Escenario aprobado
- **WHEN** se abre un escenario aprobado en el reporte
- **THEN** se ven sus steps en el orden en que se ejecutaron

### Requirement: Captura de pantalla en fallos
Cuando un escenario falle, el sistema SHALL adjuntar una captura de pantalla del navegador al reporte, una sola vez por fallo.

#### Scenario: Fallo de un step
- **WHEN** un step falla
- **THEN** el reporte muestra una captura adjunta tomada en el momento del fallo

#### Scenario: Escenario aprobado sin captura
- **WHEN** un escenario termina bien
- **THEN** no se adjunta captura

### Requirement: Clasificación de escenarios
El reporte SHALL permitir distinguir escenarios de componente y E2E.

#### Scenario: Filtrar por tipo
- **WHEN** se consulta el reporte
- **THEN** los escenarios pueden agruparse o filtrarse por `@component` y `@e2e`

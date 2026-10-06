# Spec Delta

## Purpose

Define cómo se escriben, organizan y ejecutan los escenarios Gherkin para que personas no técnicas puedan leerlos y escribirlos, y para que las pruebas de componente y las E2E puedan correrse por separado.

## ADDED Requirements

### Requirement: Escenarios en Gherkin en inglés
Todos los escenarios de prueba SHALL escribirse en archivos Gherkin en inglés usando Given, When y Then.

#### Scenario: Idioma del escenario
- **WHEN** se agrega un escenario nuevo
- **THEN** el archivo usa palabras clave en inglés y el texto de los steps en inglés

### Requirement: Steps delgados
Cada step SHALL delegar su trabajo a las acciones y SHALL NOT contener lógica de interacción con la página.

#### Scenario: Step de una acción
- **WHEN** un step ejecuta una acción de negocio
- **THEN** contiene una llamada a Actions y, si corresponde, un assert

### Requirement: Estado compartido entre steps de un escenario
Los steps de un mismo escenario SHALL compartir el navegador y las acciones sin variables estáticas globales.

#### Scenario: Escenario con steps en clases distintas
- **WHEN** los steps de un escenario están repartidos en varias clases
- **THEN** todos operan sobre el mismo navegador del escenario

### Requirement: Etiquetas de tipo de prueba
Todo escenario SHALL llevar la etiqueta `@component` o `@e2e` y estar ubicado en la carpeta correspondiente.

#### Scenario: Escenario sin etiqueta
- **WHEN** un escenario no lleva ninguna de las dos etiquetas
- **THEN** la verificación del proyecto lo reporta como error

### Requirement: Una aplicación por ejecución
Cada ejecución SHALL correr los escenarios de una sola aplicación, elegida por configuración, de modo que los steps y la configuración de una aplicación no se mezclen con los de otra.

#### Scenario: Aplicación por defecto
- **WHEN** se ejecutan las pruebas sin indicar aplicación
- **THEN** corren únicamente los escenarios de la aplicación por defecto

#### Scenario: Otra aplicación
- **WHEN** se ejecutan las pruebas indicando otra aplicación
- **THEN** corren únicamente los escenarios de esa aplicación, aunque otra defina steps con el mismo texto

### Requirement: Ejecución selectiva por tipo
El proyecto SHALL permitir ejecutar solo las pruebas de componente, solo las E2E o todas, mediante comandos de Gradle.

#### Scenario: Solo componente
- **WHEN** se ejecuta la tarea de pruebas de componente
- **THEN** corren únicamente los escenarios `@component`

#### Scenario: Solo E2E
- **WHEN** se ejecuta la tarea de pruebas E2E
- **THEN** corren únicamente los escenarios `@e2e`

### Requirement: Navegador abierto y cerrado por escenario
Cada escenario SHALL iniciar con un navegador nuevo y SHALL cerrarlo al terminar.

#### Scenario: Escenario fallido
- **WHEN** un escenario falla
- **THEN** el navegador igualmente se cierra antes de iniciar el siguiente

### Requirement: Convención de redacción de escenarios
La documentación SHALL definir una convención para redactar steps orientados al negocio, reutilizables y sin detalles técnicos.

#### Scenario: Consulta de la convención
- **WHEN** una persona no técnica va a escribir un escenario
- **THEN** encuentra ejemplos de buenas y malas redacciones en la guía

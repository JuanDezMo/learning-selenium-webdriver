# Spec Delta

## Purpose

Establece la separación de responsabilidades por capas (Locators, Actions, Core) y las reglas de dependencia entre ellas, de modo que un equipo junior pueda mantener la base y agregar nuevas aplicaciones sin modificar el núcleo.

## ADDED Requirements

### Requirement: Separación Locators y Actions por pantalla
Cada pantalla o componente SHALL tener una clase de localizadores que solo declara cómo ubicar elementos y una clase de acciones que solo expone lo que se puede hacer en esa pantalla.

#### Scenario: Cambio en el HTML
- **WHEN** cambia el selector de un elemento sin cambiar el comportamiento
- **THEN** solo se modifica la clase de localizadores de esa pantalla

#### Scenario: Cambio en el flujo
- **WHEN** cambia la secuencia de acciones de una pantalla sin cambiar sus selectores
- **THEN** solo se modifica la clase de acciones de esa pantalla

### Requirement: Dirección de las dependencias entre capas
Las capas SHALL depender únicamente de la capa inmediatamente inferior: Steps usan Actions, Actions usan Locators y Core, y Core usa el gestor de navegador.

#### Scenario: Steps sin acceso directo al navegador
- **WHEN** se revisa el código de los steps
- **THEN** ninguno usa localizadores ni el navegador de forma directa

#### Scenario: Core independiente de las aplicaciones
- **WHEN** se revisa el código del núcleo
- **THEN** no referencia ninguna aplicación concreta

### Requirement: Asserts fuera de Actions
Las clases de acciones SHALL exponer acciones y consultas de estado, y los asserts SHALL vivir únicamente en los steps.

#### Scenario: Validación de un resultado
- **WHEN** un escenario debe verificar un mensaje en pantalla
- **THEN** el step consulta el valor mediante Actions y realiza el assert

### Requirement: Esperas explícitas
Toda interacción con elementos SHALL esperar de forma explícita a que el elemento esté listo y SHALL NOT usar pausas fijas.

#### Scenario: Elemento que tarda en aparecer
- **WHEN** un elemento aparece después de una carga asíncrona
- **THEN** la acción espera hasta el tiempo máximo configurado y continúa en cuanto el elemento está listo

#### Scenario: Elemento que nunca aparece
- **WHEN** el elemento no aparece dentro del tiempo máximo
- **THEN** la acción falla con un mensaje que indica el localizador y el tiempo esperado

### Requirement: Alta de una aplicación nueva sin tocar el núcleo
Agregar una aplicación SHALL requerir solo crear sus localizadores, acciones, steps, features, archivo de configuración y runner, sin modificar el núcleo, el gestor de navegador ni otras aplicaciones.

#### Scenario: Segunda aplicación
- **WHEN** se agrega una aplicación adicional siguiendo la guía
- **THEN** ningún archivo del núcleo ni de las aplicaciones existentes cambia

### Requirement: Guía del patrón en español
El proyecto SHALL incluir documentación técnica en español que explique el POM tradicional, la separación en Actions y Locators, la interacción entre capas, las reglas SOLID básicas (S, O, D) y la regla de idiomas.

#### Scenario: Nota inicial del patrón
- **WHEN** una persona abre la guía del patrón
- **THEN** lo primero que encuentra es la comparación entre el POM tradicional y esta separación en dos capas

#### Scenario: Reglas de idioma
- **WHEN** una persona consulta la guía
- **THEN** encuentra que el código y el Gherkin van en inglés y que los comentarios y la documentación van en español

# Proposal

## Why

No existe un proyecto base para automatizar pruebas funcionales web. Cada aplicación que se quiera probar tendría que empezar de cero, con estilos distintos y sin una forma común de ejecutar, reportar ni integrar en CI. El equipo tiene un nivel de Java muy junior y personas no técnicas deben poder leer y escribir los escenarios, por lo que hace falta una base simple, estructurada y fácil de escalar a nuevas aplicaciones.

## What Changes

- Nuevo proyecto Gradle (con Gradle Wrapper) en Java 21 con Selenium WebDriver 4.x, JUnit 5 y Cucumber (Gherkin en inglés) como motor de escenarios.
- Arquitectura por capas que separa responsabilidades: `.feature` → Steps → Actions → Locators, con Core (`BaseActions`, esperas explícitas) y Driver (`DriverManager`) por debajo. Sin `PageFactory`; los localizadores son constantes `By` en clases `*Locators` y las acciones de negocio viven en clases `*Actions`.
- Gestión del navegador con modo local y modo remoto (`RemoteWebDriver`), un driver por hilo (`ThreadLocal`) y configuración por aplicación y ambiente.
- Suite de ejemplo sobre saucedemo.com con escenarios `@component` (una pantalla o widget, sin recorrer todo el flujo) y `@e2e` (flujos completos), ejecutables por separado con tareas de Gradle.
- Reporte con Allure y captura de pantalla automática en fallos, mediante un único punto de enganche en los hooks de Cucumber.
- Ejecución en contenedores con Docker Compose (runner + `selenium/standalone-chrome`), disponible desde temprano como alternativa al modo local.
- Ejecución en paralelo de escenarios, Selenium Grid y un Jenkins local en contenedores como CI.
- Documentación técnica en español: guía del patrón (POM tradicional frente a la separación en Actions y Locators, interacción entre capas), reglas SOLID básicas (S, O, D), convención de redacción de Gherkin y reglas de idioma (código en inglés, comentarios y documentación en español).
- Se implementa por iteraciones, cada una validable de forma independiente.

No hay cambios **BREAKING**: el proyecto parte de cero.

## Capabilities

### New Capabilities
- `browser-driver-management`: creación, ciclo de vida y cierre del WebDriver (Chrome o Firefox; modo local o remoto; headless), aislado por hilo y configurado por ambiente.
- `pom-layered-architecture`: estructura por capas Locators / Actions / Core, reglas de dependencia entre capas, esperas explícitas y convención para agregar una aplicación nueva sin tocar el core.
- `bdd-scenarios`: ejecución de escenarios Gherkin en inglés con Cucumber sobre JUnit 5, steps delgados, inyección de dependencias entre steps, etiquetas `@component` y `@e2e` y tareas de Gradle por tipo de prueba.
- `saucedemo-sample-suite`: conjunto de escenarios de ejemplo sobre saucedemo.com que demuestra el uso de la base, con la distinción entre pruebas de componente y E2E.
- `allure-reporting`: generación de resultados y reporte Allure, pasos legibles y captura de pantalla adjunta en caso de fallo.
- `containerized-execution`: ejecución de las pruebas con Docker Compose (runner y navegador remoto), con caché de dependencias y salida de reportes al host, y Selenium Grid como topología escalable.
- `parallel-execution`: ejecución de escenarios en paralelo sin interferencia de estado entre hilos.
- `ci-pipeline`: pipeline en un Jenkins local en contenedores que ejecuta las pruebas y publica el reporte Allure.

### Modified Capabilities
<!-- Ninguna: el proyecto no tiene specs previas. -->

## Impact

- **Código**: se crea toda la estructura del proyecto (`build.gradle`, `settings.gradle`, Gradle Wrapper, `src/test/java`, `src/test/resources`, `Dockerfile`, `docker-compose.yml`, `Jenkinsfile`, documentación).
- **Dependencias**: Selenium 4.x, JUnit 5, Cucumber (`cucumber-java`, `cucumber-junit-platform-engine`, `cucumber-picocontainer`), Allure (adaptador de Cucumber y plugin de Gradle), SLF4J.
- **Sistemas externos**: saucedemo.com como aplicación de ejemplo, imágenes de Docker de Selenium y de Jenkins.
- **Entorno**: se requiere Java 21 y Docker con Compose (ya instalados y verificados); no se asume Gradle instalado, se usa el wrapper.
- **Fuera de alcance**: ExtentReports (se decidió usar solo Allure), pruebas de API o móviles, y soporte de navegadores distintos a Chrome y Firefox en la primera versión.

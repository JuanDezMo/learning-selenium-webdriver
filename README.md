# POM Base Project

Proyecto base para pruebas funcionales web con **Selenium WebDriver**, **Java 21**, **Gradle**, **JUnit 5** y **Cucumber** (Gherkin en inglés).

> Estado: en construcción por iteraciones. Este README crece con cada iteración.

## Requisitos

- Java 21 (`java -version` debe mostrar 21)
- No necesitas instalar Gradle: el proyecto incluye el **Gradle Wrapper** (`./gradlew`)

## Ejecutar las pruebas

```bash
./gradlew test             # pruebas unitarias y todos los escenarios
./gradlew componentTest    # solo los escenarios de componente
./gradlew e2eTest          # solo los escenarios E2E
```

Agrega `-Dheadless=true` para no ver el navegador y `-Dapp=<nombre>` para elegir otra aplicación (por defecto `saucedemo`).

Para ver la versión de Gradle que usa el proyecto:

```bash
./gradlew --version
```

## Configuración

Los valores se leen en este orden de prioridad (el primero que exista gana):

1. Propiedad de sistema: `./gradlew test -Dbrowser=firefox`
2. Variable de entorno: prefijo `TEST_`, nombre en mayúsculas y `_` en lugar de `.` (por ejemplo `TEST_BASE_URL` o `TEST_BROWSER`)
3. Archivos en `src/test/resources/config/`:
   `default.properties` → `{app}.properties` → `{app}-{env}.properties`

La aplicación se elige con `-Dapp=<nombre>` (por defecto `saucedemo`) y el ambiente con `-Denv=<nombre>` (por defecto `local`).

Más detalle del navegador, las propiedades disponibles y ejemplos en [docs/driver-y-configuracion.md](docs/driver-y-configuracion.md).

## Ejecución en contenedores

```bash
docker compose up --build --abort-on-container-exit --exit-code-from runner
```

Corre los escenarios `@component` con el navegador en un contenedor y deja los resultados en `build/`. Detalle, ver el navegador remoto y cuándo usar local o contenedores: [docs/docker-y-contenedores.md](docs/docker-y-contenedores.md).

## Reporte Allure

```bash
./gradlew componentTest
./gradlew allureServe
```

Detalle (HTML, contenedores, capturas, filtros por tipo): [docs/reportes-allure.md](docs/reportes-allure.md).

## Paralelismo

```bash
./gradlew test -Dparallel=true -Dthreads=3
```

Desactivado por defecto. Detalle y reglas para los steps: [docs/paralelismo.md](docs/paralelismo.md).

## Guía del patrón

Cómo está organizado el código (Page Object Model en dos capas, reglas SOLID básicas y reglas de idioma): [docs/guia-patron-pom.md](docs/guia-patron-pom.md). **Léela antes de agregar código.**

## Estructura

```
src/test/java/com/empresa/
  config/    lectura de configuración
  core/      acciones base con esperas explícitas
  driver/    creación y cierre del navegador (uno por hilo)
  pages/     por aplicación: locators/ y actions/
  steps/     steps de Cucumber (delgados): common/ (hooks) y un paquete por aplicación
  runner/    un punto de entrada de Cucumber por aplicación (Run<Aplicacion>Test)
src/test/resources/
  config/    archivos de propiedades
  features/  escenarios Gherkin (en inglés)
```

## Reglas de idioma

- Todo lo que es código va en **inglés**: clases, métodos, variables y los archivos `.feature`.
- Los **comentarios** y la **documentación técnica** van en **español**.

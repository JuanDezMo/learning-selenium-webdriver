# Reportes con Allure

Allure es el único reporte del proyecto. Muestra cada escenario con sus steps en orden, las acciones de negocio que ejecutó cada step y una captura de pantalla cuando el escenario falla.

## Generar y consultar el reporte

**En local**

```bash
./gradlew componentTest        # o e2eTest / test
./gradlew allureReport         # genera el HTML
./gradlew allureServe          # genera y abre el reporte en el navegador (Ctrl+C para terminar)
```

- Resultados crudos: `build/allure-results/` (se borran al iniciar cada ejecución de pruebas).
- Reporte HTML: `build/reports/allure-report/allureReport/` (se regenera desde cero cada vez).
- Si las pruebas fallan, Gradle se detiene antes de generar el reporte: ejecuta `./gradlew allureReport` aparte.
- La primera vez Gradle descarga la herramienta de línea de comandos de Allure.

**En contenedores**

```bash
docker compose up --build --abort-on-container-exit --exit-code-from runner
```

El ejecutor genera el reporte siempre, aunque las pruebas fallen, y queda en tu carpeta del proyecto en la misma ruta de arriba. Para verlo: `./gradlew allureServe` (o abre `build/reports/allure-report/allureReport/index.html` con un servidor local; abrir el archivo directamente no funciona por restricciones del navegador).

## Qué se ve en el reporte

- **Steps de Gherkin**: el texto del escenario, en el orden de ejecución.
- **Acciones de negocio**: bajo cada step aparecen las acciones de `Actions` anotadas con `@Step` (por ejemplo "Log in as standard_user"). Nunca se escriben contraseñas en el texto.
- **Captura en fallos**: el hook `Hooks.attachScreenshotOnFailure` toma la captura del navegador del escenario solo si falla, antes de cerrarlo. Se ve en la sección *Tear down* del escenario. Los escenarios aprobados no llevan captura.
- **Tipo de prueba**: las etiquetas `@component` y `@e2e` aparecen como *tags*. En la pestaña *Suites* o *Behaviors* puedes agrupar y filtrar por ellas (campo de búsqueda: `component` o `e2e`).

## Cómo anotar una acción nueva

En la clase `*Actions`, sobre el método de negocio:

```java
@Step("Add {productName} to the cart")
public void addToCart(String productName) { ... }
```

El texto va en inglés. Se anotan las acciones de negocio, no los métodos de `BaseActions` ni las consultas (`get...`/`is...`).

## Problemas conocidos

- `Target directory ... is already in use`: Allure no sobrescribe carpetas; el proyecto ya borra la anterior, pero si generas el reporte con otra herramienta usa `--clean`.
- Si ejecutas dos tareas de pruebas seguidas, los resultados de la primera se borran al iniciar la segunda.

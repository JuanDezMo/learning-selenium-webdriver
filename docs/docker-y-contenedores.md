# Ejecución en contenedores (Docker)

## Cuándo usar local y cuándo contenedores

| Situación | Usar |
|---|---|
| Escribir o depurar un escenario (ver el navegador, pausar, ajustar) | **Local**: `./gradlew componentTest` |
| Comprobar que la suite pasa "como en CI", sin instalar navegadores | **Contenedores** |
| Máquina sin Chrome o con versiones distintas | **Contenedores** |

El modo local sigue siendo el camino de desarrollo y no necesita Docker.

## Qué se levanta

`docker-compose.yml` define dos servicios:

- `selenium`: navegador Chrome oficial de Selenium (`selenium/standalone-chrome`). Tiene una comprobación de salud; el ejecutor no arranca hasta que esté sano.
- `runner`: JDK 21 con el wrapper del proyecto (`docker/Dockerfile`). Monta el proyecto en `/workspace` y ejecuta `./gradlew` con `-Denv=docker`.

El ambiente `docker` (`src/test/resources/config/saucedemo-docker.properties`) pone `mode=remote` y `remote.url=http://selenium:4444`: el ejecutor encuentra al navegador por el nombre del servicio. El código de las pruebas no cambia.

## Comando único

```bash
docker compose up --build --abort-on-container-exit --exit-code-from runner
```

- Corre los escenarios `@component` (tarea por defecto).
- Al terminar, los dos contenedores quedan detenidos y el comando devuelve el código de salida de las pruebas (0 = todo pasó).
- Para limpiar contenedores y red: `docker compose down`.

Otras tareas con la variable `TEST_TASK`:

```bash
TEST_TASK=e2eTest docker compose up --abort-on-container-exit --exit-code-from runner
TEST_TASK=test    docker compose up --abort-on-container-exit --exit-code-from runner
```

## Ver el navegador remoto

Mientras corre la ejecución abre <http://localhost:7900> (noVNC, sin contraseña). El ambiente usa `headless=false` para que haya algo que ver. Las pruebas son rápidas, así que conviene abrir la página antes de lanzar los escenarios.

## Resultados y caché

- **Resultados**: el proyecto se monta dentro del contenedor, así que `build/test-results/`, `build/allure-results/` y el reporte Allure (`build/reports/allure-report/`, ver [reportes-allure.md](reportes-allure.md)) quedan en tu carpeta del proyecto, con tu usuario (no `root`).
- **Caché**: el volumen `gradle-cache` guarda las dependencias y la distribución de Gradle. La primera ejecución descarga (~1 min); las siguientes no descargan nada.
- Gradle no repite pruebas cuyo código no cambió. Para forzar la repetición: `TEST_TASK="componentTest --rerun" docker compose up --abort-on-container-exit --exit-code-from runner`.

## Problemas conocidos

- Si tu usuario no es el 1000, define `HOST_UID` y `HOST_GID` (`HOST_UID=$(id -u) HOST_GID=$(id -g) docker compose up ...`).
- Los puertos 4444 y 7900 deben estar libres en la máquina.
- Las pruebas necesitan salida a internet (saucedemo es un sitio público).

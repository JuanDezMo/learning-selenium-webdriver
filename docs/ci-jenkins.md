# CI local con Jenkins

Jenkins corre en un contenedor, lee el pipeline del repositorio (`Jenkinsfile`), ejecuta la suite en contenedores y publica el reporte Allure. **Es solo para uso local**: no tiene usuarios ni contraseñas y solo se publica en `localhost`.

## Requisitos

- Docker y Compose (ver [docker-y-contenedores.md](docker-y-contenedores.md)).
- Tu usuario en el grupo `docker`. El grupo del anfitrión se pasa con `DOCKER_GID` (por defecto 998; consúltalo con `getent group docker`).
- Los puertos 8080 (Jenkins), 4444 y 7900 (navegador de las pruebas) libres.

## Levantar el CI

Desde la raíz del proyecto:

```bash
DOCKER_GID=$(getent group docker | cut -d: -f3) docker compose -f docker-compose.ci.yml up -d --build
```

Abre <http://localhost:8080>. Ya existe el job `pom-base-project` (configurado en `ci/jenkins.yaml`). La primera vez tarda unos minutos en construir la imagen.

Para detenerlo: `docker compose -f docker-compose.ci.yml down` (el historial se conserva en el volumen `jenkins-home`; con `down -v` se borra).

## Lanzar el pipeline

1. **La primera vez** pulsa *Construir ahora* (*Build Now*): Jenkins lee el `Jenkinsfile` y aprende su parámetro. Esa primera corrida usa `component`.
2. Después usa *Construir con parámetros* y elige `TEST_TYPE`:
   - `component`: solo escenarios `@component`.
   - `e2e`: solo `@e2e`.
   - `all`: ambos (tarea `functionalTest`, sin pruebas unitarias).

El pipeline corre `docker compose up` con el `docker-compose.yml` del proyecto (navegador remoto + ejecutor). Si falla un escenario, el build queda en **FAILURE**; si todos pasan, en **SUCCESS**. Los contenedores se detienen siempre.

## Ver resultados

- Estado y log: la página del build (*Console Output*).
- Reporte Allure: enlace **Allure Report** en la página del job o del build. Se publica siempre, también cuando hay fallos (con la captura del escenario fallido).
- Resultados crudos de Allure: artefactos del build.

## Cómo funciona (y qué tener presente)

- **Jenkins lee el repositorio por Git (`file:///repo`)**: solo ve lo que está **commiteado** en la rama `master`. Si cambias el `Jenkinsfile` o las pruebas, haz commit antes de lanzar el build. Un cambio commiteado en el `Jenkinsfile` se aplica en el siguiente build.
- Para usar otro repositorio local: `REPO_PATH=/ruta/al/repo docker compose -f docker-compose.ci.yml up -d`. Por defecto es la carpeta desde la que ejecutas el comando.
- Jenkins usa el Docker del anfitrión por el socket. Por eso su carpeta de trabajo (`/tmp/pom-jenkins-workspace`, cambiable con `CI_WORKSPACE`) está montada con la misma ruta dentro y fuera del contenedor. Acceder al socket equivale a tener permisos de administrador sobre Docker: aceptable en local, no en un servidor compartido.
- No hay secretos en el repositorio. Si algún día el CI necesita credenciales, deben guardarse en el almacén de credenciales de Jenkins, nunca en archivos versionados.

## Problemas conocidos

- `Checkout ... aborted because it references a local directory`: ya está permitido con `-Dhudson.plugins.git.GitSCM.ALLOW_LOCAL_CHECKOUT=true` en `docker-compose.ci.yml`.
- El build falla antes de las pruebas por permisos del docker.sock: revisa `DOCKER_GID`.
- Si el puerto 4444 está ocupado por otro Compose del proyecto, detenlo (`docker compose down`).
- Lanzar el build con la API requiere el token CSRF de Jenkins; desde la interfaz web no hace falta.

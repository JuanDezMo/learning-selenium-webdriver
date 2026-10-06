# Selenium Grid

El Grid reparte las sesiones de navegador entre varios nodos, de modo que el paralelismo (ver [paralelismo.md](paralelismo.md)) sí acelera la suite en contenedores. Usa su propio archivo de Compose: `docker-compose.grid.yml`.

## Qué se levanta

- `selenium-hub`: recibe las peticiones de las pruebas y las reparte. Consola: <http://localhost:4444/ui>.
- `chrome-node` (2 por defecto): cada nodo es un Chrome que atiende **una sesión a la vez**.
- `runner`: espera a que se registren los nodos y corre la suite con `-Denv=grid -Dparallel=true` y un hilo por nodo.

El ambiente `grid` (`src/test/resources/config/saucedemo-grid.properties`) pone `mode=remote` y `remote.url=http://selenium-hub:4444`. El código de las pruebas es el mismo que en local o con un solo navegador remoto.

## Comando único

```bash
docker compose -f docker-compose.grid.yml up --build --abort-on-container-exit --exit-code-from runner
docker compose -f docker-compose.grid.yml down
```

Al terminar, el reporte Allure queda en el proyecto igual que en contenedores simples (ver [reportes-allure.md](reportes-allure.md)).

## Escalar nodos

```bash
GRID_NODES=3 docker compose -f docker-compose.grid.yml up --abort-on-container-exit --exit-code-from runner
```

`GRID_NODES` define a la vez cuántos nodos se crean y cuántos hilos usa la suite. Cada nodo consume unos 500 MB de memoria o más: no pases del número de núcleos ni de lo que aguante tu RAM.

Otras tareas: `TEST_TASK=e2eTest` o `TEST_TASK=test` antes del comando.

## Comprobar el reparto de sesiones

- Mientras corre: <http://localhost:4444/ui> muestra los nodos y sus sesiones activas.
- Después, desde el hub (con los contenedores aún detenidos, antes de `down`):

```bash
docker logs pom_base_project-selenium-hub-1 2>&1 | grep -o "Deleted session from local Session Map.*Node: http://[0-9.]*:5555" | grep -o "Node: .*" | sort | uniq -c
```

Con 13 escenarios y 2 nodos se obtuvo un reparto de 6 y 7 sesiones; con 3 nodos, de 3, 5 y 5. El nombre del contenedor del hub depende del nombre de la carpeta del proyecto.

## Apuntar las pruebas a un Grid propio

Sin Docker de este proyecto, basta con `./gradlew test -Dmode=remote -Dremote.url=http://<host>:4444 -Dparallel=true -Dthreads=N`.

## Problemas conocidos

- No uses a la vez `docker-compose.yml` y `docker-compose.grid.yml`: ambos publican el puerto 4444.
- Si el ejecutor se queda en "Esperando nodos del Grid...", revisa que haya memoria disponible y que `docker compose -f docker-compose.grid.yml ps` muestre los nodos en marcha.
- En los nodos no hay visualización noVNC (se habría necesitado un puerto por nodo); para ver un navegador usa `docker-compose.yml`.

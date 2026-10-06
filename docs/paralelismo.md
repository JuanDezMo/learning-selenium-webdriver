# Ejecución en paralelo

Los escenarios pueden correr al mismo tiempo, cada uno con su propio navegador. Por defecto está **desactivado**.

## Activarlo

```bash
./gradlew test -Dparallel=true -Dthreads=3
./gradlew componentTest -Dparallel=true            # 2 hilos por defecto
```

- `-Dparallel=true|false`: activa o desactiva (por defecto `false`: escenarios de uno en uno).
- `-Dthreads=N`: máximo de escenarios simultáneos (por defecto 2).
- Funciona igual con `-Dheadless=true`.

## Cuántos hilos usar

Cada hilo abre un Chrome. Medido en esta máquina (8 núcleos, 8 GB) con los 13 escenarios `@component`:

| Hilos | Tiempo de los escenarios |
|---|---|
| secuencial | ~25 s |
| 3 | ~12 s |

Recomendación: **2 o 3 hilos** en un equipo de desarrollo, y no más hilos que núcleos. Si ves fallos por lentitud (tiempos de espera), baja el número de hilos.

## Por qué funciona

- `DriverManager` guarda el navegador en un `ThreadLocal`: cada hilo tiene el suyo y `Hooks` lo cierra al terminar el escenario.
- Cucumber crea clases de steps nuevas para cada escenario (PicoContainer), así que el estado de los steps no se comparte.
- En Allure, cada escenario conserva sus steps y su captura: en una prueba con un fallo provocado mientras otros 12 corrían, la única captura quedó asociada al escenario fallido.

## Reglas para escribir steps sin estado compartido

1. **Prohibido `static` mutable** (listas, mapas, contadores, "el usuario actual") en steps, Actions o Locators. Las constantes `static final` inmutables (como los `By`) sí están bien.
2. El estado entre steps de un escenario va en campos de instancia o en una clase compartida inyectada por constructor (PicoContainer), nunca en variables estáticas.
3. Nunca crear el navegador con `new ChromeDriver()`: siempre `DriverManager.getDriver()`.
4. Los escenarios no pueden depender del orden ni de datos que dejó otro escenario. Cada uno prepara lo que necesita (por ejemplo con `Given the user is logged in as "standard"`).
5. Cuidado con datos que se modifican en el servidor y se comparten (el mismo usuario editando el mismo registro): en saucedemo no pasa porque el estado vive en el navegador.
6. Sin `Thread.sleep`: usa las esperas explícitas de `BaseActions`.

## Con navegador remoto

Cada hilo pide su propia sesión al servidor remoto. El navegador único de `docker-compose.yml` atiende **una sesión a la vez** por defecto, así que ahí el paralelismo no acelera nada; para sesiones simultáneas se usa Selenium Grid (ver [selenium-grid.md](selenium-grid.md)).

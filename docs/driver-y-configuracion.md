# Driver y configuración

Esta guía explica cómo se abre el navegador y cómo se configura. Los ejemplos están pensados para copiarse y ejecutarse tal cual.

## Regla de oro

Nadie escribe `new ChromeDriver()` en las pruebas, steps ni acciones. El navegador siempre se pide a `DriverManager`:

```java
WebDriver driver = DriverManager.getDriver();
```

## Las piezas

```
DriverManager   guarda un navegador por hilo y lo cierra (quitDriver)
     |
     v
DriverFactory   decide cómo crearlo: Chrome o Firefox, local o remoto, headless
     |
     v
BrowserType     lista de navegadores soportados (chrome, firefox)
```

- **Un navegador por hilo:** `DriverManager` usa un `ThreadLocal`, así dos hilos nunca comparten navegador. Esto permitirá ejecutar escenarios en paralelo más adelante sin cambiar nada.
- **Un navegador nuevo por escenario:** el navegador se abre la primera vez que un step lo pide y `Hooks` lo cierra siempre al terminar el escenario, haya pasado o fallado.
- **Sin instalar drivers:** en modo local, Selenium Manager descarga el driver que necesita.

## Propiedades de configuración

| Propiedad | Valores | Por defecto | Para qué sirve |
|---|---|---|---|
| `browser` | `chrome`, `firefox` | `chrome` | Navegador a usar |
| `mode` | `local`, `remote` | `local` | Navegador en esta máquina o en un servidor remoto |
| `remote.url` | URL | `http://localhost:4444` | Servidor de navegadores cuando `mode=remote` |
| `headless` | `true`, `false` | `false` | Ejecutar sin ventana visible |
| `timeout.seconds` | número entero | `10` | Tiempo máximo de las esperas explícitas |
| `base.url` | URL | (la define cada aplicación) | URL base de la aplicación; es obligatoria |

Orden de prioridad (el primero que exista gana):

1. Propiedad de sistema: `./gradlew test -Dbrowser=firefox`
2. Variable de entorno: nombre en mayúsculas con `_` en lugar de `.` (por ejemplo `REMOTE_URL`)
3. Archivos en `src/test/resources/config/`: `default.properties`, luego `{app}.properties`, luego `{app}-{env}.properties`

La aplicación se elige con `-Dapp` (por defecto `saucedemo`) y el ambiente con `-Denv` (por defecto `local`).

## Ejemplos

Ejecutar solo los escenarios `@component` con Chrome sin ventana visible:

```bash
./gradlew test -Dheadless=true -Dcucumber.filter.tags=@component
```

Lo mismo con Firefox:

```bash
./gradlew test -Dheadless=true -Dbrowser=firefox -Dcucumber.filter.tags=@component
```

Probar el mensaje de error de un servidor remoto inalcanzable (la prueba falla a propósito e indica la URL usada):

```bash
./gradlew test -Dmode=remote -Dremote.url=http://localhost:1 -Dcucumber.filter.tags=@component
```

Cambiar el tiempo máximo de espera a 20 segundos:

```bash
./gradlew test -Dtimeout.seconds=20
```

## Errores que verás y qué significan

- `Navegador no soportado: 'safari'. Valores permitidos: chrome, firefox`: el valor de `browser` no es válido. Falla antes de abrir nada.
- `Modo de ejecución no soportado: ...`: el valor de `mode` no es `local` ni `remote`.
- `No se pudo crear la sesión en el servidor remoto de navegadores: <url>`: el servidor remoto no responde en esa URL.
- `Falta la propiedad obligatoria 'base.url'...`: falta definir la propiedad en `config/` o con `-D`.
- `El elemento By.id: x no fue visible después de N segundos`: una acción esperó el tiempo máximo y el elemento no apareció.

## Esperas explícitas

`BaseActions` (capa `core`) ofrece `click`, `type`, `getText`, `isVisible` y `waitUntilVisible`. Todas esperan a que el elemento esté listo, hasta `timeout.seconds`. **No se usan pausas fijas** (`Thread.sleep`): si algo tarda, se espera por la condición, no por tiempo.

`isVisible` es la excepción: responde de inmediato, sin esperar, para poder comprobar que algo NO está en pantalla sin gastar el tiempo máximo.

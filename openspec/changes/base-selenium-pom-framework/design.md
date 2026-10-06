# Design

## Context

Proyecto nuevo, sin código previo. El entorno tiene Java 21, Docker 29 con Compose v5, Chrome y Firefox; Gradle y Maven no están instalados, por lo que el build debe arrancar con el Gradle Wrapper. El equipo es de nivel Java muy junior y habrá personas no técnicas que lean y escriban escenarios. Ver proposal.md (Why) para la motivación.

## Goals / Non-Goals

**Goals:**
- Una estructura donde cada archivo tiene un lugar obvio y una sola razón para cambiar.
- Que agregar una aplicación nueva sea copiar una plantilla de carpetas, sin tocar el núcleo.
- Que el modo de ejecución (local, contenedor, Grid, CI) cambie solo por configuración.
- Dejar el aislamiento por hilo resuelto desde el inicio, aunque el paralelismo se active al final.

**Non-Goals:**
- Pruebas de API, móviles o de rendimiento.
- Soporte de navegadores distintos a Chrome y Firefox.
- ExtentReports u otro reporte adicional a Allure.
- Interfaces y abstracciones "por si acaso": no se aplican L ni I de SOLID de forma forzada.

## Decisions

### 1. Capas y regla de dependencia

```
  .feature (Gherkin, inglés)
      |
      v
  Steps          delgados: llaman a Actions y hacen los asserts
      |
      v
  Actions        acciones de negocio de una pantalla (login(), addToCart())
      |  \
      |   +--> Locators   solo constantes By, sin driver
      v
  Core           BaseActions: click/type/getText con espera explícita
      |
      v
  Driver         DriverManager: crea, entrega por hilo y cierra el navegador
```

Cada capa solo conoce a la inmediata inferior. Se descartó `PageFactory`: es una convención desaconsejada, mezcla localizador y driver (impide separar Locators de Actions), dificulta esperas explícitas y localizadores dinámicos. Se descartó también una tercera clase "Page" intermedia: aportaría abstracción sin beneficio para un junior.

Nombres: `LoginLocators` y `LoginActions` (sufijos fijos). La guía incluye una nota inicial que explica el POM tradicional (una clase con selectores y acciones) frente a esta separación en dos responsabilidades.

### 2. SOLID aplicado de forma básica
- **S**: una clase por pantalla y por responsabilidad; asserts solo en steps.
- **O**: nueva aplicación o navegador = clases nuevas, el núcleo no se edita.
- **D**: los steps reciben sus Actions inyectadas y piden el navegador al `DriverManager`; nadie hace `new ChromeDriver()` fuera de la creación del driver.
- **L e I** se documentan como referencia breve, sin imponer interfaces. Se admite una sola interfaz pequeña si hace falta para los navegadores.

### 3. Build: Gradle con Wrapper, siempre
El wrapper fija la versión de Gradle en la máquina del dev, en el contenedor y en Jenkins. Todo comando documentado, de contenedor y de CI usa `./gradlew`. Como no hay Gradle instalado, el wrapper se genera una vez con una instalación temporal de Gradle o descargando sus archivos. Alternativa descartada: Maven (el usuario prefirió Gradle) y ejecutar solo dentro de Docker sin wrapper (pierde el fijado de versión y no simplifica nada).

### 4. Motor de pruebas: JUnit 5 + Cucumber
Cucumber corre sobre JUnit Platform con el motor de Cucumber y una suite que selecciona features y etiquetas. Los hooks de Cucumber (`Before`/`After`) abren y cierran el navegador y toman la captura; reemplazan a una clase base de test. El estado compartido entre steps se resuelve con inyección por constructor mediante PicoContainer, por ser lo más simple para un junior. Todos los escenarios están en Gherkin (componente y E2E) para tener un solo estilo; JUnit queda solo como motor.

Etiquetas `@component` y `@e2e` más carpetas `features/<app>/component` y `features/<app>/e2e`. Tareas de Gradle `componentTest`, `e2eTest` y `test` (todas) filtran por etiqueta. Un chequeo de consistencia verifica que ningún escenario carezca de etiqueta de tipo.

### 5. Driver: local o remoto, un navegador por hilo desde el día uno
`DriverManager` guarda el navegador en un `ThreadLocal` y decide local o remoto por configuración. En modo local se apoya en Selenium Manager (Selenium 4.x), sin gestionar binarios. En modo remoto usa `RemoteWebDriver` contra la URL configurada. Aunque el paralelismo se active en una iteración posterior, hacerlo desde el inicio casi no cuesta y evita reescribir después.

### 6. Configuración
Archivos de propiedades por aplicación (`<app>.properties`) y ambiente, con sobrescritura por línea de comandos o variable de entorno. Un lector de configuración único. Las propiedades obligatorias que falten hacen fallar al inicio con un mensaje claro.

### 7. Pruebas de componente frente a E2E sobre saucedemo
- **Componente**: una pantalla o widget (login, inventario, carrito, formulario de checkout, menú). Estado previo por atajo cuando sea posible.
- **E2E**: pocos flujos completos (compra completa; agregar, quitar y cerrar sesión), con login por interfaz.
- La precondición de sesión se resuelve con un step reutilizable (`Given the user is logged in as "<user>"`) que llama a la misma acción de login de `LoginActions` que usan los escenarios de login. No se usan atajos por cookie ni se duplica lógica de login.

### 8. Reporte: solo Allure, un único punto de enganche
Plugin de Gradle de Allure y adaptador de Cucumber. La captura de pantalla se toma en el hook posterior a cada escenario cuando falló, con el navegador del hilo actual. Las acciones de negocio se anotan con pasos de Allure para que el reporte sea legible. ExtentReports queda fuera de alcance, lo que evita mantener dos reportes.

### 9. Docker temprano, con dos contenedores
Se usa un contenedor ejecutor (JDK 21 + el wrapper del proyecto) y otro con el navegador oficial de Selenium con visualización por noVNC, orquestados con Compose. Se descartó un contenedor único con Chrome incluido: imagen propia más pesada y más mantenimiento. Volúmenes para la caché de Gradle y para los resultados de Allure. El modo local sigue siendo el camino de desarrollo (navegador visible, depuración cómoda); Docker es el camino "como corre en CI". El modo remoto del driver es la misma pieza que usa el Grid, por lo que no hay trabajo duplicado.

### 10. Paralelismo, Grid y Jenkins al final
- Paralelismo a nivel de escenario por configuración de JUnit Platform; funciona porque el driver ya es por hilo.
- Selenium Grid con hub y varios nodos mediante Compose; el modo remoto apunta al hub.
- Jenkins local en contenedor con un pipeline versionado en el repositorio que corre la suite en contenedores y publica Allure.

### 11. Idioma
Código y Gherkin en inglés; comentarios y documentación técnica en español. Los artefactos de OpenSpec también en español. Queda escrito en la guía y en el contexto del proyecto.

### 12. Iteraciones
```
It.1  Esqueleto: wrapper, Java 21, JUnit 5 + Cucumber, Selenium, configuración
It.2  Driver (local y remoto, por hilo) + Core (BaseActions, esperas)
It.3  Login completo (Locators, Actions, Steps, escenarios) + guía del patrón
It.4  Inventario, carrito, checkout y menú (escenarios @component)
It.5  Escenarios @e2e + tareas por etiqueta + chequeo de etiquetas + alta de app nueva
It.6  Docker: ejecutor + navegador remoto con Compose
It.7  Allure + captura en fallo
It.8  Paralelismo
It.9  Selenium Grid
It.10 Jenkins local en contenedores
```
Cada iteración tiene un criterio de salida medible (un comando o resultado concreto) y se valida de forma independiente antes de pasar a la siguiente.

### 13. Forma de trabajo
- Las personas del equipo hacen los commits; el agente no los hace.
- Al terminar cada iteración, el agente se detiene, informa el resultado de su criterio de salida y espera la indicación para continuar con la siguiente.

## Risks / Trade-offs

- [Dependencias de Cucumber, Allure y Selenium con versiones incompatibles] → fijar versiones juntas desde It.1 y validar con una prueba mínima antes de crecer.
- [Steps con lógica o duplicados que degraden el patrón] → reglas escritas en la guía y revisión de código; steps de una o dos líneas.
- [Escenarios de saucedemo inestables si el sitio público cambia o se cae] → mantener pocos E2E, usar solo funciones estables y documentar que depende de un sitio externo.
- [Docker agrega una barrera para un junior] → el modo local sigue siendo el camino por defecto, con comandos documentados y un solo comando para contenedores.
- [Pertenecer al grupo `docker` equivale a permisos de administrador local] → aceptable en desarrollo local; se documenta.
- [Captura o reporte asociados al escenario equivocado en paralelo] → tomar la captura con el navegador del hilo actual y probarlo explícitamente en la iteración de paralelismo.
- [Esperas insuficientes en ambientes lentos como contenedores o Grid] → tiempos de espera configurables por ambiente.
- [Generar el wrapper sin Gradle instalado] → generarlo una vez con un Gradle temporal o descargarlo; no se instala Gradle de forma permanente.

## Open Questions

- Versiones exactas de Selenium, Cucumber y Allure: se fijan en It.1 según la última estable compatible.
- Estrategia de datos de usuarios de saucedemo (archivo de propiedades o archivo de datos): se decide en It.3 sin afectar las specs.
- Ubicación exacta de la guía y del chequeo de etiquetas en el árbol del proyecto: se decide en It.1 y It.3.

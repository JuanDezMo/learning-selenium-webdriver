# Tasks

## Reglas de trabajo

- Los commits los hace el usuario; el agente no hace commits.
- Todo comando usa el Gradle Wrapper (`./gradlew`).
- Al terminar cada iteración se avisa al usuario con un resumen y el resultado de su criterio de salida, y no se empieza la siguiente hasta que lo indique.

## 1. Iteración 1: Esqueleto del proyecto

Criterio de salida: `./gradlew test` ejecuta 1 escenario mínimo en verde, sin Gradle instalado en la máquina.

- [x] 1.1 Generar el Gradle Wrapper (con un Gradle temporal o descargando sus archivos) y verificar que `./gradlew --version` funciona sin Gradle instalado
- [x] 1.2 Crear `settings.gradle` y `build.gradle` con Java 21 (toolchain), repositorios y versiones fijadas de Selenium 4.x, JUnit 5, Cucumber (java, junit-platform-engine, picocontainer) y SLF4J; verificar con `./gradlew build` sin errores
- [x] 1.3 Crear la estructura de carpetas (`core`, `driver`, `config`, `pages/<app>/locators`, `pages/<app>/actions`, `steps`, `features`) y verificar que existe el árbol esperado
- [x] 1.4 Crear el lector de configuración con archivos de propiedades por aplicación y ambiente, sobrescritura por línea de comandos o variable de entorno y error claro si falta una propiedad obligatoria; verificar con pruebas unitarias de precedencia y de propiedad faltante
- [x] 1.5 Agregar un escenario mínimo `.feature` con su runner que corra con `./gradlew test` y verificar que el motor de Cucumber lo descubre y lo ejecuta
- [x] 1.6 Crear `.gitignore` y un `README.md` inicial en español con requisitos y comando de ejecución; verificar que los comandos documentados funcionan tal cual están escritos
- [x] 1.7 Avisar al usuario el cierre de la iteración 1 con el resultado del criterio de salida y detenerse para su commit

## 2. Iteración 2: Driver y Core

Criterio de salida: `./gradlew test` abre y cierra Chrome en un escenario de prueba y las pruebas unitarias del driver y de las esperas pasan.

- [x] 2.1 Implementar `DriverManager` con un navegador por hilo (`ThreadLocal`), selección Chrome o Firefox por configuración, headless y cierre seguro; verificar con una prueba que abre y cierra el navegador y que dos hilos reciben instancias distintas
- [x] 2.2 Implementar el modo remoto (`RemoteWebDriver`) con la URL configurable y mensaje claro si no responde; verificar la rama con una prueba que apunta a una URL inválida y comprueba el mensaje
- [x] 2.3 Fallar antes de abrir ningún navegador cuando la configuración indique uno no soportado, listando los valores permitidos; verificar con prueba unitaria
- [x] 2.4 Implementar `BaseActions` con click, escribir, leer texto y estado de visibilidad usando esperas explícitas con tiempo máximo configurable y mensaje de fallo con localizador y tiempo; verificar con pruebas contra una página local o estable, incluido el caso de elemento que nunca aparece
- [x] 2.5 Agregar hooks de Cucumber que abran el navegador antes de cada escenario y lo cierren después, incluso si falla; verificar con un escenario que falla a propósito y comprobar que no quedan procesos de navegador
- [x] 2.6 Documentar en español el funcionamiento del driver y la configuración, con comentarios de código en español; verificar que los ejemplos de configuración de la documentación funcionan
- [x] 2.7 Avisar al usuario el cierre de la iteración 2 con el resultado del criterio de salida y detenerse para su commit

## 3. Iteración 3: Login completo y guía del patrón

Criterio de salida: `./gradlew test` ejecuta los 3 escenarios `@component` de login en verde y la guía del patrón existe.

- [x] 3.1 Escribir la guía del patrón en español (`docs/`) que empieza con la nota del POM tradicional frente a la separación Locators y Actions, el diagrama de dependencias entre capas, las reglas SOLID básicas (S, O, D) y la regla de idiomas; verificar que la nota inicial es lo primero que aparece
- [x] 3.2 Implementar `LoginLocators` y `LoginActions` y verificar con una prueba que `login()` lleva a la lista de productos con un usuario válido
- [x] 3.3 Escribir los steps y los escenarios `@component` de login (válido, usuario bloqueado, campos vacíos) y verificar que los 3 pasan
- [x] 3.4 Crear el step reutilizable `Given the user is logged in as "<user>"` que llama a `LoginActions.login()`; verificar que un escenario de prueba lo usa como precondición sin repetir pasos de login
- [x] 3.5 Mover usuarios y datos de prueba a configuración o archivo de datos fuera de los steps; verificar que cambiar un usuario requiere editar un solo lugar
- [x] 3.6 Avisar al usuario el cierre de la iteración 3 con el resultado del criterio de salida y detenerse para su commit

## 4. Iteración 4: Inventario, carrito, checkout y menú

Criterio de salida: `./gradlew test` ejecuta en verde todos los escenarios `@component` de inventario, carrito, checkout y menú.

- [x] 4.1 Implementar localizadores, acciones y steps de inventario; verificar con los escenarios `@component` de ordenar por precio y de agregar y quitar un producto con el contador del carrito
- [x] 4.2 Implementar localizadores, acciones y steps de carrito; verificar con el escenario `@component` de producto en el carrito
- [x] 4.3 Implementar localizadores, acciones y steps del formulario de checkout; verificar con el escenario `@component` de datos obligatorios de checkout
- [x] 4.4 Implementar el menú lateral compartido (cerrar sesión); verificar con el escenario `@component` de cierre de sesión
- [x] 4.5 Actualizar la guía con las pantallas agregadas; verificar que los ejemplos de la guía corresponden al código existente
- [x] 4.6 Avisar al usuario el cierre de la iteración 4 con el resultado del criterio de salida y detenerse para su commit

## 5. Iteración 5: E2E, ejecución por tipo y alta de aplicación

Criterio de salida: `./gradlew componentTest` y `./gradlew e2eTest` ejecutan cada uno solo lo suyo y todo pasa en verde.

- [x] 5.1 Escribir los escenarios `@e2e` (compra completa; agregar, quitar, carrito vacío y cierre de sesión); verificar que pasan de extremo a extremo
- [x] 5.2 Definir tareas de Gradle `componentTest`, `e2eTest` y `test` que filtren por etiqueta; verificar que cada tarea ejecuta solo lo suyo
- [x] 5.3 Crear un chequeo que falle si un escenario carece de `@component` o `@e2e`; verificar que falla con un escenario sin etiqueta y pasa sin él
- [x] 5.4 Documentar en la guía la convención de redacción de Gherkin (buenos y malos ejemplos) y el procedimiento para dar de alta una aplicación nueva; verificar siguiendo el procedimiento con una aplicación ficticia y comprobando que no se modifica el núcleo, y luego descartarla
- [x] 5.5 Avisar al usuario el cierre de la iteración 5 con el resultado del criterio de salida y detenerse para su commit

## 6. Iteración 6: Docker

Criterio de salida: un solo comando de Compose ejecuta un escenario `@component` en contenedores y deja los resultados en el host.

- [x] 6.1 Crear el contenedor ejecutor (JDK 21 con el wrapper del proyecto) y verificar que ejecuta `./gradlew --version` dentro del contenedor
- [x] 6.2 Crear `docker-compose.yml` con el ejecutor y el navegador remoto de Selenium con visualización, espera de disponibilidad por salud del navegador y volúmenes para caché de Gradle y resultados; verificar que `docker compose up` deja ambos servicios sanos
- [x] 6.3 Configurar un perfil de ambiente para modo remoto y headless apuntando al servicio de navegador; verificar que un escenario `@component` corre en contenedores y termina con los contenedores detenidos
- [x] 6.4 Verificar que la segunda ejecución reutiliza la caché y que los resultados quedan en el directorio del proyecto del anfitrión
- [x] 6.5 Documentar en español el comando único de ejecución en contenedores, cómo ver el navegador remoto y cuándo usar local o contenedor; verificar ejecutando los comandos tal como están escritos
- [ ] 6.6 Avisar al usuario el cierre de la iteración 6 con el resultado del criterio de salida y detenerse para su commit

## 7. Iteración 7: Allure

Criterio de salida: tras una ejecución, el reporte Allure se genera con escenarios, steps y una captura adjunta en un fallo provocado.

- [ ] 7.1 Agregar el plugin de Gradle de Allure y el adaptador de Cucumber con versiones compatibles y verificar que una ejecución genera resultados en el directorio esperado
- [ ] 7.2 Agregar el comando de Gradle para generar y consultar el reporte HTML; verificar que el reporte muestra escenarios y sus steps en orden
- [ ] 7.3 Anotar las acciones de negocio con pasos de Allure; verificar que el reporte muestra los pasos relevantes de una prueba aprobada
- [ ] 7.4 Tomar la captura de pantalla en el hook posterior al escenario solo si falló, con el navegador del hilo actual, y adjuntarla una sola vez; verificar con un escenario que falla a propósito (con captura) y con uno aprobado (sin captura)
- [ ] 7.5 Clasificar los escenarios por `@component` y `@e2e` en el reporte; verificar que se pueden filtrar
- [ ] 7.6 Verificar que el reporte generado en contenedores queda en el host y documentar en español cómo generarlo y consultarlo en local y en contenedores
- [ ] 7.7 Avisar al usuario el cierre de la iteración 7 con el resultado del criterio de salida y detenerse para su commit

## 8. Iteración 8: Paralelismo

Criterio de salida: la suite completa en paralelo con 2 o más hilos da los mismos resultados por escenario que en secuencial.

- [ ] 8.1 Habilitar la ejecución de escenarios en paralelo con configuración de activación y número de hilos; verificar que con paralelismo desactivado corren de uno en uno y con él activado corren concurrentemente
- [ ] 8.2 Revisar que no exista estado compartido entre escenarios (variables estáticas, datos mutables, sesión) y corregirlo; verificar con una prueba de dos escenarios concurrentes que no se afectan
- [ ] 8.3 Verificar que capturas y pasos de Allure se asocian al escenario correcto en paralelo, con un fallo provocado mientras otros corren
- [ ] 8.4 Comparar resultados por escenario entre ejecución secuencial y paralela de la suite completa y verificar que son equivalentes
- [ ] 8.5 Documentar en español cómo activar el paralelismo, el número de hilos recomendado y las reglas para escribir pasos sin estado compartido
- [ ] 8.6 Avisar al usuario el cierre de la iteración 8 con el resultado del criterio de salida y detenerse para su commit

## 9. Iteración 9: Selenium Grid

Criterio de salida: la suite corre en paralelo contra un Grid con 2 o más nodos y las sesiones se reparten entre ellos.

- [ ] 9.1 Agregar a Compose la topología con hub y varios nodos de navegador y verificar que el hub reporta los nodos registrados
- [ ] 9.2 Apuntar el modo remoto del driver al hub mediante configuración y verificar que una prueba corre en el Grid
- [ ] 9.3 Ejecutar la suite en paralelo contra el Grid y verificar que cada hilo recibe una sesión independiente y que las sesiones se distribuyen entre nodos
- [ ] 9.4 Documentar en español cómo levantar el Grid, escalar nodos y apuntar las pruebas; verificar ejecutando los comandos tal como están escritos
- [ ] 9.5 Avisar al usuario el cierre de la iteración 9 con el resultado del criterio de salida y detenerse para su commit

## 10. Iteración 10: Jenkins local en contenedores

Criterio de salida: Jenkins local ejecuta el pipeline versionado, falla ante un escenario fallido y publica el reporte Allure.

- [ ] 10.1 Crear la definición de Jenkins en contenedor con configuración precargada y volumen persistente; verificar que `docker compose up` deja Jenkins accesible en la URL local documentada
- [ ] 10.2 Escribir el `Jenkinsfile` versionado con parámetro de tipo de prueba (componente, E2E o ambas); verificar que Jenkins lo toma del repositorio y que un cambio en él se refleja en la siguiente ejecución
- [ ] 10.3 Hacer que el pipeline ejecute la suite en contenedores y falle si algún escenario falla; verificar con una corrida exitosa y con una fallida provocada
- [ ] 10.4 Publicar el reporte Allure en cada ejecución, incluso con fallos; verificar que se consulta desde Jenkins en ambos casos
- [ ] 10.5 Revisar que ningún archivo del CI contenga credenciales reales y verificarlo con una búsqueda de contraseñas y tokens en el repositorio
- [ ] 10.6 Documentar en español cómo levantar el CI, lanzar el pipeline y consultar resultados; verificar siguiendo la guía desde cero
- [ ] 10.7 Avisar al usuario el cierre de la iteración 10 con el resultado del criterio de salida y detenerse para su commit

## 11. Verificación integral

- [ ] 11.1 Ejecutar de punta a punta, en una máquina limpia siguiendo solo la documentación, el modo local, contenedores, paralelismo, Grid y Jenkins, y verificar que cada ruta cumple sus specs
- [ ] 11.2 Revisar que el código esté en inglés y que comentarios y documentación estén en español, y verificar que no queden mezclas
- [ ] 11.3 Ejecutar `openspec validate base-selenium-pom-framework --strict` y verificar que no reporta errores
- [ ] 11.4 Avisar al usuario el cierre de la verificación integral

## Workflow follow-up

- Archivar el cambio cuando se cumplan los requisitos de revisión del proyecto.
- Verificar el resultado archivado y las specs principales.

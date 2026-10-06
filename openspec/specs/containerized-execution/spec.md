# containerized-execution Specification

## Purpose

Permite ejecutar la suite en contenedores con un entorno reproducible, y escalar a Selenium Grid, sin que la persona que desarrolla tenga que instalar navegadores ni versiones específicas de herramientas.

## Requirements

### Requirement: Ejecución con un solo comando
El proyecto SHALL permitir ejecutar la suite en contenedores con un único comando documentado.

#### Scenario: Ejecución en contenedores
- **WHEN** se ejecuta el comando de contenedores en una máquina con Docker y Compose
- **THEN** se levantan el ejecutor de pruebas y el navegador remoto, corren los escenarios y los contenedores se detienen al terminar

### Requirement: Navegador remoto en contenedor
El navegador SHALL ejecutarse en un contenedor separado al que el ejecutor de pruebas se conecta por nombre de servicio.

#### Scenario: Conexión entre contenedores
- **WHEN** el ejecutor de pruebas inicia
- **THEN** espera a que el navegador remoto esté listo antes de abrir sesiones

### Requirement: Modo local sigue disponible
La ejecución local con navegador visible SHALL seguir funcionando sin Docker para facilitar el desarrollo y la depuración.

#### Scenario: Desarrollo local
- **WHEN** una persona ejecuta las pruebas sin contenedores
- **THEN** el navegador se abre localmente y las pruebas corren igual

### Requirement: Caché de dependencias
El entorno de contenedores SHALL reutilizar las dependencias descargadas entre ejecuciones.

#### Scenario: Segunda ejecución
- **WHEN** se ejecuta por segunda vez sin cambios de dependencias
- **THEN** no se vuelven a descargar las dependencias

### Requirement: Resultados disponibles en el host
Los resultados y reportes generados dentro del contenedor SHALL quedar accesibles en la máquina anfitriona.

#### Scenario: Consulta del reporte tras la ejecución
- **WHEN** termina la ejecución en contenedores
- **THEN** los resultados de Allure están en el directorio del proyecto del anfitrión

### Requirement: Observación del navegador remoto
El entorno SHALL permitir ver el navegador remoto durante la ejecución para depurar.

#### Scenario: Ver la ejecución
- **WHEN** se ejecuta con la visualización activada
- **THEN** una persona puede ver el navegador remoto desde su navegador web

### Requirement: Selenium Grid
El entorno SHALL ofrecer una topología con Selenium Grid (hub y varios nodos de navegador) como alternativa al navegador único.

#### Scenario: Ejecución contra el Grid
- **WHEN** la suite se ejecuta contra el Grid
- **THEN** las sesiones se distribuyen entre los nodos disponibles

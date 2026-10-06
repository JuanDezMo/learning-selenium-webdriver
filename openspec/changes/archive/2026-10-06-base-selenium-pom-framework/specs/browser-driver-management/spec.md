# Spec Delta

## Purpose

Define cómo la base crea, entrega y cierra el navegador de prueba, en modo local o remoto, de forma aislada por hilo y configurable por ambiente, para que las pruebas nunca instancien el navegador directamente.

## ADDED Requirements

### Requirement: Selección de navegador por configuración
El sistema SHALL crear el navegador indicado por configuración (Chrome o Firefox) sin que las pruebas lo especifiquen en su código.

#### Scenario: Navegador por defecto
- **WHEN** no se indica navegador en la configuración
- **THEN** el sistema usa Chrome

#### Scenario: Navegador no soportado
- **WHEN** la configuración indica un navegador que no es Chrome ni Firefox
- **THEN** el sistema falla antes de abrir ningún navegador con un mensaje que lista los valores permitidos

### Requirement: Modo local y modo remoto
El sistema SHALL ejecutar el navegador de forma local o conectarse a un navegador remoto según la configuración, sin cambios en las pruebas.

#### Scenario: Ejecución local
- **WHEN** el modo configurado es local
- **THEN** el navegador se abre en la propia máquina de ejecución sin requerir instalar manualmente un binario de driver

#### Scenario: Ejecución remota
- **WHEN** el modo configurado es remoto y se indica la URL del servidor de navegadores
- **THEN** la sesión se crea en ese servidor remoto

#### Scenario: Servidor remoto inalcanzable
- **WHEN** el modo es remoto y la URL no responde
- **THEN** el sistema falla con un mensaje que indica la URL usada

### Requirement: Modo headless
El sistema SHALL permitir ejecutar el navegador sin interfaz gráfica mediante configuración.

#### Scenario: Headless activado
- **WHEN** la configuración activa el modo headless
- **THEN** el navegador se ejecuta sin ventana visible

### Requirement: Un navegador por hilo
El sistema SHALL entregar a cada hilo de ejecución su propia instancia de navegador y cerrarla al terminar el escenario.

#### Scenario: Cierre al terminar
- **WHEN** termina un escenario, con éxito o con fallo
- **THEN** el navegador de ese escenario se cierra y no quedan procesos huérfanos

#### Scenario: Aislamiento entre hilos
- **WHEN** dos hilos piden el navegador al mismo tiempo
- **THEN** cada uno recibe una instancia distinta

### Requirement: Configuración por aplicación y ambiente
El sistema SHALL leer la configuración (URL base, navegador, modo, headless, tiempos de espera) desde archivos de propiedades por aplicación y ambiente, permitiendo sobrescribir valores desde la línea de comandos o variables de entorno.

#### Scenario: Valor sobrescrito
- **WHEN** un valor se indica por línea de comandos y también en el archivo de propiedades
- **THEN** prevalece el valor de la línea de comandos

#### Scenario: Propiedad obligatoria ausente
- **WHEN** falta una propiedad obligatoria como la URL base
- **THEN** el sistema falla al iniciar con un mensaje que nombra la propiedad faltante

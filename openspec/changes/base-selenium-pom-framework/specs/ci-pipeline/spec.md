# Spec Delta

## Purpose

Provee un pipeline de integración continua local, en contenedores, que ejecuta la suite y publica sus resultados, para validar la base y servir de modelo de CI para equipos nuevos.

## ADDED Requirements

### Requirement: Jenkins local en contenedor
El proyecto SHALL permitir levantar un Jenkins local en contenedor con un comando documentado, con la configuración necesaria precargada.

#### Scenario: Levantar el CI
- **WHEN** se ejecuta el comando de arranque del CI
- **THEN** Jenkins queda accesible en una URL local con el pipeline del proyecto disponible

### Requirement: Pipeline versionado
La definición del pipeline SHALL estar en el repositorio.

#### Scenario: Cambio del pipeline
- **WHEN** se modifica la definición del pipeline en el repositorio
- **THEN** la siguiente ejecución usa la definición nueva

### Requirement: Ejecución de la suite en el pipeline
El pipeline SHALL ejecutar la suite en contenedores y fallar si algún escenario falla.

#### Scenario: Escenario fallido
- **WHEN** falla un escenario durante la ejecución
- **THEN** el pipeline termina en estado fallido

#### Scenario: Todos aprobados
- **WHEN** todos los escenarios pasan
- **THEN** el pipeline termina en estado exitoso

### Requirement: Reporte Allure publicado
El pipeline SHALL publicar el reporte Allure de cada ejecución, incluso cuando haya fallos.

#### Scenario: Consulta del reporte
- **WHEN** termina una ejecución, exitosa o no
- **THEN** el reporte Allure puede consultarse desde Jenkins

### Requirement: Ejecución por tipo de prueba
El pipeline SHALL permitir elegir si ejecuta pruebas de componente, E2E o ambas.

#### Scenario: Solo componente
- **WHEN** se lanza el pipeline con el tipo componente
- **THEN** solo se ejecutan los escenarios `@component`

### Requirement: Sin secretos en el repositorio
La configuración del CI SHALL NOT incluir credenciales reales en el repositorio.

#### Scenario: Revisión del repositorio
- **WHEN** se revisan los archivos del CI
- **THEN** no contienen contraseñas ni tokens reales

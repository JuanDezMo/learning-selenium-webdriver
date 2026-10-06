# Spec Delta

## Purpose

Permite ejecutar escenarios en paralelo para reducir el tiempo total de la suite sin que los escenarios se interfieran entre sí.

## ADDED Requirements

### Requirement: Paralelismo configurable
El proyecto SHALL permitir activar o desactivar la ejecución en paralelo y definir el número de hilos mediante configuración.

#### Scenario: Paralelismo desactivado
- **WHEN** el paralelismo está desactivado
- **THEN** los escenarios corren de uno en uno

#### Scenario: Paralelismo activado
- **WHEN** se activa con un número de hilos
- **THEN** los escenarios corren concurrentemente hasta ese número

### Requirement: Aislamiento entre escenarios concurrentes
Los escenarios concurrentes SHALL NOT compartir navegador, sesión ni estado de ejecución.

#### Scenario: Dos escenarios en paralelo
- **WHEN** dos escenarios corren al mismo tiempo
- **THEN** cada uno opera sobre su propio navegador y sus acciones no afectan al otro

### Requirement: Resultados correctos en paralelo
El reporte SHALL asociar cada captura y cada paso al escenario correcto aun con ejecución concurrente.

#### Scenario: Fallo con captura en paralelo
- **WHEN** falla un escenario mientras otros corren
- **THEN** la captura adjunta pertenece al escenario fallido

### Requirement: Resultado equivalente al secuencial
La ejecución en paralelo SHALL producir el mismo resultado por escenario que la ejecución secuencial.

#### Scenario: Comparación de resultados
- **WHEN** se ejecuta la misma suite en secuencial y en paralelo
- **THEN** el conjunto de escenarios aprobados y fallidos es el mismo

### Requirement: Compatibilidad con navegador remoto y Grid
El paralelismo SHALL funcionar tanto con navegador local como remoto y con Grid.

#### Scenario: Paralelo contra Grid
- **WHEN** se ejecuta en paralelo contra el Grid
- **THEN** cada hilo recibe una sesión independiente

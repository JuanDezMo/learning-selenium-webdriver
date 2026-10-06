# Spec Delta

## Purpose

Provee una suite de ejemplo sobre saucedemo.com que valida la base de extremo a extremo y sirve como referencia de cómo escribir pruebas de componente y E2E con las capas definidas.

## ADDED Requirements

### Requirement: Pruebas de componente de login
La suite SHALL cubrir el formulario de login como componente aislado.

#### Scenario: Credenciales válidas
- **WHEN** el usuario ingresa credenciales válidas
- **THEN** accede a la lista de productos

#### Scenario: Usuario bloqueado
- **WHEN** ingresa con el usuario bloqueado
- **THEN** ve un mensaje de error indicando que está bloqueado

#### Scenario: Campos vacíos
- **WHEN** envía el formulario sin usuario o sin contraseña
- **THEN** ve un mensaje que indica el campo obligatorio faltante

### Requirement: Pruebas de componente de inventario
La suite SHALL cubrir la lista de productos como componente aislado.

#### Scenario: Ordenar por precio
- **WHEN** el usuario ordena los productos por precio ascendente
- **THEN** la lista queda ordenada de menor a mayor precio

#### Scenario: Agregar y quitar un producto
- **WHEN** el usuario agrega un producto y luego lo quita
- **THEN** el contador del carrito aumenta y luego vuelve a su valor inicial

### Requirement: Pruebas de componente de carrito y checkout
La suite SHALL cubrir el carrito y el formulario de datos de checkout como componentes aislados.

#### Scenario: Producto en el carrito
- **WHEN** el usuario agregó un producto y abre el carrito
- **THEN** el producto aparece en el carrito

#### Scenario: Datos de checkout obligatorios
- **WHEN** envía el formulario de checkout con algún campo vacío
- **THEN** ve el mensaje del campo obligatorio faltante

### Requirement: Pruebas de componente del menú
La suite SHALL cubrir el menú lateral.

#### Scenario: Cerrar sesión
- **WHEN** el usuario abre el menú y elige cerrar sesión
- **THEN** vuelve a la pantalla de login

### Requirement: Pruebas E2E
La suite SHALL incluir pocos flujos E2E que recorran varias pantallas partiendo desde el login por interfaz.

#### Scenario: Compra completa
- **WHEN** el usuario inicia sesión, agrega dos productos, completa el checkout y lo confirma
- **THEN** ve el mensaje de confirmación de la compra

#### Scenario: Carrito vacío y cierre de sesión
- **WHEN** el usuario agrega un producto, lo quita, verifica el carrito vacío y cierra sesión
- **THEN** vuelve a la pantalla de login

### Requirement: Precondición de sesión reutilizando el login
Las pruebas que requieren un usuario autenticado SHALL preparar la sesión con un único paso reutilizable que usa la misma acción de login que las pruebas de login, sin duplicar pasos de login.

#### Scenario: Sesión previa
- **WHEN** un escenario requiere un usuario autenticado
- **THEN** su primer paso inicia sesión con el paso reutilizable y el escenario continúa desde la lista de productos

### Requirement: Datos de prueba fuera de los steps
Los usuarios y datos de la suite SHALL leerse de configuración o datos de prueba y no estar escritos dentro de las clases de steps.

#### Scenario: Cambio de usuario de prueba
- **WHEN** cambia un usuario de prueba
- **THEN** se modifica en un único lugar de datos o configuración

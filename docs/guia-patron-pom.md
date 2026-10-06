# Guía del patrón: Page Object Model en dos capas

## Nota inicial: el POM tradicional y lo que hacemos distinto

**El patrón Page Object Model (POM) tradicional** crea una clase por pantalla (`LoginPage`) que mezcla dos responsabilidades:

1. **Dónde** está cada elemento (los selectores).
2. **Qué se puede hacer** en la pantalla (iniciar sesión, leer un error).

```
POM TRADICIONAL                           ESTA BASE (dos responsabilidades, dos capas)

+-------------------------------+         +------------------+     +--------------------+
| LoginPage                     |         | LoginLocators    |     | LoginActions       |
|  - By / @FindBy   (DÓNDE)     |  ---->  |  DÓNDE está cada |<----|  QUÉ se puede hacer|
|  - login(), error() (QUÉ)     |         |  elemento (By)   |     |  en la pantalla    |
|  Todo en una sola clase       |         +------------------+     +--------------------+
+-------------------------------+
 Si cambia el HTML o el flujo,            Cambia el HTML    -> solo se toca Locators
 se toca la misma clase                   Cambia el flujo   -> solo se toca Actions
```

En esta base **separamos esas dos responsabilidades en dos capas**:

- **Locators** (`LoginLocators`): solo constantes `By`. No tienen lógica ni usan el navegador.
- **Actions** (`LoginActions`): las acciones de negocio de la pantalla. Usan los localizadores, pero nunca escriben un selector.

Tampoco usamos `PageFactory` (`@FindBy`): enlaza cada elemento con el navegador, lo que impide separar los localizadores de las acciones, y hace incómodas las esperas explícitas y los localizadores dinámicos. Un `By` no depende del navegador, y por eso encaja con esta separación.

## Las capas y cómo se hablan

```
  .feature (Gherkin, inglés)     Lo leen y escriben personas no técnicas
        |
        v
  Steps                          Delgados: llaman a Actions y hacen los asserts
        |
        v
  Actions                        Acciones de negocio de una pantalla (login(), ...)
        |         \
        |          +--------->   Locators   Solo constantes By
        v
  Core (BaseActions)             click / type / getText con espera explícita
        |
        v
  Driver (DriverManager)         Crea, entrega (uno por hilo) y cierra el navegador
```

**Regla de dependencia:** cada capa solo conoce a la que tiene inmediatamente debajo.

| Capa | Puede usar | NO puede |
|---|---|---|
| `.feature` | Frases en inglés orientadas al negocio | Detalles técnicos (ids, selectores) |
| Steps | Actions | Usar localizadores o el navegador directamente |
| Actions | Locators y `BaseActions` | Escribir selectores, hacer asserts |
| Locators | `By` | Lógica, navegador |
| Core | `DriverManager` | Conocer una aplicación concreta |
| Driver | Selenium | Conocer pantallas o escenarios |

## Un ejemplo completo: el login

**1. El escenario** (`features/saucedemo/component/login.feature`):

```gherkin
@component
Feature: Login

  Scenario: A user with valid credentials can log in
    Given the user is on the login page
    When the user logs in as "standard"
    Then the products page is displayed
```

**2. El step** (`steps/saucedemo/LoginSteps.java`): delgado, llama a la acción y hace el assert.

```java
@When("the user logs in as {string}")
public void theUserLogsInAs(String userAlias) {
    loginActions.login(users.username(userAlias), users.password(userAlias));
}

@Then("the products page is displayed")
public void theProductsPageIsDisplayed() {
    assertEquals("Products", inventoryActions.getPageTitle());
}
```

**3. La acción** (`pages/saucedemo/actions/LoginActions.java`): qué se puede hacer.

```java
public void login(String username, String password) {
    type(USERNAME_INPUT, username);
    type(PASSWORD_INPUT, password);
    click(LOGIN_BUTTON);
}
```

**4. Los localizadores** (`pages/saucedemo/locators/LoginLocators.java`): dónde está cada elemento.

```java
public static final By USERNAME_INPUT = By.id("user-name");
public static final By PASSWORD_INPUT = By.id("password");
public static final By LOGIN_BUTTON = By.id("login-button");
```

**5. El núcleo** (`core/BaseActions.java`): `type` y `click` esperan a que el elemento esté listo; si no aparece, fallan indicando el localizador y el tiempo esperado.

## Precondición de sesión reutilizable

Muchos escenarios necesitan un usuario ya autenticado. No se repiten los pasos de login: se usa el step compartido, que llama a la misma acción de login que los escenarios de login.

```gherkin
Given the user is logged in as "standard"
```

Está en `steps/saucedemo/CommonSteps.java` y usa `LoginActions.openAndLogin(...)`. Si el login falla, el error aparece en ese step y no en uno posterior.

## Pantallas y componentes de saucedemo

Cada pantalla tiene su pareja `*Locators` + `*Actions` y sus steps:

| Pantalla o componente | Locators y Actions | Steps |
|---|---|---|
| Login | `LoginLocators`, `LoginActions` | `LoginSteps` |
| Inventario (lista de productos) | `InventoryLocators`, `InventoryActions` | `InventorySteps` |
| Carrito | `CartLocators`, `CartActions` | `CartSteps` |
| Checkout (formulario de datos) | `CheckoutLocators`, `CheckoutActions` | `CheckoutSteps` |
| Cabecera y menú lateral (**compartido**) | `HeaderLocators`, `HeaderActions` | `InventorySteps` (contador) y `MenuSteps` (cerrar sesión) |

**Componente compartido:** el contador del carrito y el menú lateral aparecen en todas las pantallas después del login. Por eso viven en `HeaderActions` y no se repiten en cada pantalla. Los steps de cualquier pantalla pueden recibirlo por el constructor.

### Localizadores dinámicos

Cuando el selector depende de un dato (por ejemplo el nombre del producto), el `*Locators` ofrece un método que arma el `By`. Los selectores siguen viviendo en esa clase:

```java
public static By addToCartButton(String productName) {
    return By.cssSelector("[data-test='add-to-cart-" + toSlug(productName) + "']");
}
```

Y la acción lo usa sin escribir ningún selector:

```java
public void addToCart(String productName) {
    click(addToCartButton(productName));
}
```

### Esperas que desaparecen y varios elementos

`BaseActions` también ofrece `getTexts` (texto de varios elementos, por ejemplo la lista de precios), `selectByVisibleText` (listas desplegables) y `waitUntilGone` (esperar a que algo desaparezca, como el contador del carrito al quitar el último producto).

### Estado común con `Background`

Cuando todos los escenarios de un `.feature` empiezan igual, se usa `Background`. Por ejemplo, el checkout siempre empieza con un usuario autenticado y un producto en el carrito:

```gherkin
Background:
  Given the user is logged in as "standard"
  And the user adds "Sauce Labs Backpack" to the cart
  And the user opens the cart
  And the user starts the checkout
```

## Usuarios y datos de prueba

Los usuarios **no se escriben en los steps ni en los `.feature`**. Los escenarios usan un alias (`"standard"`, `"locked"`) y las credenciales viven en `src/test/resources/config/saucedemo.properties`:

```properties
user.standard.name=standard_user
user.standard.password=secret_sauce
```

`TestUsers` las lee por alias. Para cambiar un usuario o agregar otro, solo se edita ese archivo.

Los demás datos de prueba (por ejemplo la persona compradora del checkout: `checkout.first.name`, `checkout.last.name`, `checkout.postal.code`) también están en ese archivo y los steps los leen con `ConfigReader`.

## SOLID, en versión básica

No se usa teoría: son reglas que se pueden revisar a simple vista. Nos limitamos a tres, que se cumplen con la estructura de carpetas.

| Principio | Regla para el equipo | Dónde se ve |
|---|---|---|
| **S** (una responsabilidad) | Una clase por pantalla y por responsabilidad: los selectores en `*Locators`, las acciones en `*Actions`. Los asserts van solo en los steps. | `LoginLocators` y `LoginActions` |
| **O** (abierto/cerrado) | Para agregar una aplicación o un navegador se agregan clases nuevas; no se edita el núcleo. | `BrowserType` y `DriverFactory` para un navegador nuevo; una carpeta `pages/<app>` nueva para una aplicación |
| **D** (depender de abstracciones) | Los steps reciben sus Actions por el constructor y el navegador se pide a `DriverManager`. Nadie escribe `new ChromeDriver()` fuera de `DriverFactory`. | Constructor de `LoginSteps`; `DriverManager.getDriver()` |

Los principios **L** (sustitución) e **I** (interfaces pequeñas) no se imponen: no se crean interfaces que nadie necesita. La única regla práctica es que toda clase de acciones extiende `BaseActions` y no cambia su comportamiento heredado.

## Reglas de idioma

- Todo lo que es **código** va en **inglés**: nombres de clases, métodos y variables, y los archivos `.feature` con sus steps (`Given`, `When`, `Then`).
- Los **comentarios** y la **documentación técnica** van en **español**.

## Qué se agrega en cada situación

| Quiero... | Creo o modifico... |
|---|---|
| Cambiar un selector | Solo el `*Locators` de esa pantalla |
| Cambiar el flujo de una pantalla | Solo el `*Actions` de esa pantalla |
| Agregar una pantalla | Un `*Locators`, un `*Actions` y sus steps |
| Agregar un escenario | Un `.feature` con la etiqueta `@component` o `@e2e` |
| Agregar un usuario de prueba | `config/<app>.properties` |
| Usar otro navegador | Una propiedad: `-Dbrowser=firefox` (ver [driver y configuración](driver-y-configuracion.md)) |

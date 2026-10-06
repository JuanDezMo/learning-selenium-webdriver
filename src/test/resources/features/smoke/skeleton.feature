# Escenario temporal que valida que el esqueleto del proyecto funciona.
# Se elimina cuando existan los escenarios reales de saucedemo.
@smoke
Feature: Project skeleton

  Scenario: The project configuration is available
    Given the project configuration is loaded
    Then the base url is "https://www.saucedemo.com"

# Escenario temporal que valida que el navegador se abre y se cierra por escenario.
# Se elimina cuando existan los escenarios reales de saucedemo.
@smoke
Feature: Browser lifecycle

  Scenario: The browser opens for a scenario
    Given a browser is opened on a blank page
    Then the browser is available

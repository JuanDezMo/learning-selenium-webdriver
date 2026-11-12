@component
Feature: Session
  @screenshots
  Scenario: The cart is kept after logging out and logging in again
    Given the user is logged in as "standard"
    When the user adds "Sauce Labs Backpack" to the cart
    And the user opens the cart
    Then the cart contains "Sauce Labs Backpack"
    When the user logs out from the menu
    Then the login page is displayed
    When the user logs in as "standard"
    And the user opens the cart
    Then the cart contains "Sauce Labs Backpack"

@component
Feature: Cart

  Scenario: A product added to the cart is shown in the cart
    Given the user is logged in as "standard"
    And the user adds "Sauce Labs Backpack" to the cart
    When the user opens the cart
    Then the cart contains "Sauce Labs Backpack"

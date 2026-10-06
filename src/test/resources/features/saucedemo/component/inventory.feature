@component
Feature: Inventory

  Background:
    Given the user is logged in as "standard"

  Scenario: Products can be sorted by price
    When the user sorts the products by "Price (low to high)"
    Then the products are listed from the lowest to the highest price

  Scenario: Products can be sorted by name in reverse order
    When the user sorts the products by "Name (Z to A)"
    Then the products are listed by name from Z to A

  Scenario: A product can be added to and removed from the cart
    When the user adds "Sauce Labs Backpack" to the cart
    Then the cart badge shows 1
    When the user removes "Sauce Labs Backpack" from the cart
    Then the cart badge is not displayed

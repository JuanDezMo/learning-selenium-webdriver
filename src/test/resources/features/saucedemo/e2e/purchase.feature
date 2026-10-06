@e2e
Feature: Purchase flow

  Scenario: A user can buy two products
    Given the user is on the login page
    When the user logs in as "standard"
    And the user adds "Sauce Labs Backpack" to the cart
    And the user adds "Sauce Labs Bike Light" to the cart
    Then the cart badge shows 2
    When the user opens the cart
    And the user starts the checkout
    And the user continues the checkout with valid information
    And the user finishes the purchase
    Then the order confirmation message is "Thank you for your order!"

  Scenario: A user can empty the cart and log out
    Given the user is on the login page
    When the user logs in as "standard"
    And the user adds "Sauce Labs Backpack" to the cart
    And the user removes "Sauce Labs Backpack" from the cart
    And the user opens the cart
    Then the cart is empty
    When the user logs out from the menu
    Then the login page is displayed

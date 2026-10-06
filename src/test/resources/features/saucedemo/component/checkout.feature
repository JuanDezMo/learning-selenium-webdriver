@component
Feature: Checkout information

  Background:
    Given the user is logged in as "standard"
    And the user adds "Sauce Labs Backpack" to the cart
    And the user opens the cart
    And the user starts the checkout

  Scenario Outline: The checkout requires all the personal information
    When the user continues the checkout leaving the "<field>" empty
    Then the checkout error message is "<message>"

    Examples:
      | field       | message                        |
      | first name  | Error: First Name is required  |
      | last name   | Error: Last Name is required   |
      | postal code | Error: Postal Code is required |

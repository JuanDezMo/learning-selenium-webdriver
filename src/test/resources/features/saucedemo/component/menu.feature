@component
Feature: Side menu

  Scenario: A logged in user can log out from the menu
    Given the user is logged in as "standard"
    When the user logs out from the menu
    Then the login page is displayed

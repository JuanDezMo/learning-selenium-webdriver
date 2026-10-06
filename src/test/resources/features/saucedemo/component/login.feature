@component
Feature: Login

  Scenario: A user with valid credentials can log in
    Given the user is on the login page
    When the user logs in as "standard"
    Then the products page is displayed

  Scenario: A locked out user cannot log in
    Given the user is on the login page
    When the user logs in as "locked"
    Then the login error message is "Epic sadface: Sorry, this user has been locked out."

  Scenario Outline: The login requires username and password
    Given the user is on the login page
    When the user logs in leaving the "<field>" empty
    Then the login error message is "<message>"

    Examples:
      | field    | message                            |
      | username | Epic sadface: Username is required |
      | password | Epic sadface: Password is required |

  # Muestra la precondición reutilizable que usan los demás escenarios
  Scenario: A scenario can start with an already logged in user
    Given the user is logged in as "standard"
    Then the products page is displayed

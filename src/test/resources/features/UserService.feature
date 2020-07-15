Feature: To validate Employee Rest API

  @regression
  Scenario Outline: As a user, I should validate the user with all operations like create, get, update, patch and delete.
    Given Setting up user Rest API
    When I send a request to create user:
      | first_name | <first_name> |
      | last_name  | <last_name>  |
      | gender     | <gender>     |
      | email      | <email>      |
      | status     | <status>     |
    Then I should validate the created user
    When I send a request to get user
    Then I should validate the get user
    When I send a request to update user:
      | last_name | Updated |
      | gender    | female  |
    Then I should validate the updated user
    When I send a request to patch user:
      | last_name | Patched |
      | gender    | male  |
    Then I should validate the patched user
    When I send a request to delete user
    Then I should validate the deleted user
    Examples:
      | first_name | last_name | gender | email               | status |
      | Bright     | Lastname  | male   | xyz111abc@gmail.com | active |
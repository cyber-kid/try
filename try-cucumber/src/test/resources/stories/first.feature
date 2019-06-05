Feature: First
  Try out cucumber framework.

  Scenario Outline: Sunday isn't Friday
    Given today is "<day>"
    When I ask whether it's Friday yet
    Then I should be told "<answer>"

  Examples:
    | day | answer |
    | Friday | Yeap |
    | Sunday | Nope |
    | anything else! | Nope |
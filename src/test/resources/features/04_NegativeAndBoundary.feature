@NegativeAndBoundary
Feature: Boundary Value and Negative Test Scenarios
  As a QA engineer
  I want to execute negative and edge-case scenarios on Ulta search and recommendations
  So that I ensure system resilience against unexpected or malicious inputs

  Background:
    Given the user launches the Ulta website

  @NegativeSearch
  Scenario: Search with Non-Existent Product Query
    When the user searches for "xyznonexistentbeautybrand1234"
    Then search results display 0 products or appropriate no-results message

  @EmptySearchBoundary
  Scenario: Search with Empty Query
    When the user searches for ""
    Then the system gracefully maintains current view or prompts for input

  @BoundaryLengthSearch
  Scenario: Search with 256 Character Boundary Payload
    When the user enters an excessively long search string of 256 characters
    Then the application handles the input without unhandled errors

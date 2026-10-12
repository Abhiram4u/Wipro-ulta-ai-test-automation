@SearchAndCart
Feature: Ulta Product Search and Shopping Cart Management
  As a beauty shopper
  I want to search for specific product categories and add them to my bag
  So that I can verify search precision and bag tracking

  Background:
    Given the user launches the Ulta website

  @DataDrivenSearch
  Scenario Outline: Data-Driven Product Category Search Verification
    When the user searches for "<category>"
    Then search results are displayed with at least 1 product
    And the first product title should be relevant to "<category>"

    Examples:
      | category        |
      | moisturizer     |
      | mascara         |
      | vitamin c serum |

  @CartCounterTracking
  Scenario: Real-Time Cart Counter Badge Incrementation
    When the user searches for "moisturizer"
    And the user clicks on the first product
    And the user selects shade or size if required and clicks "Add to Bag"
    Then the cart icon count is updated

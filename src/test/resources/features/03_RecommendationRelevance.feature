@AIRelevance
Feature: Algorithmic Recommendation Relevance and Personalization Verification
  As a shopper and QA engineer
  I want to mathematically evaluate Ulta's GenAI and ML recommendation algorithms
  So that I ensure personalized relevance meets statistical quality thresholds

  Background:
    Given the user launches the Ulta website

  @AC5 @PDPRelevance
  Scenario: AC5 - High-Precision Keyword Density in Product Detail Recommendations
    When the user searches for "moisturizer"
    And the user clicks on the first product
    Then the product detail page is displayed
    When the recommendations section loads
    Then at least 30 percent of recommended products contain keywords relevant to the viewed product

  @AC6 @CartRelevance
  Scenario: AC6 - Cross-Category Contextual Relevance in Cart Page Recommendations
    Given the user has "moisturizer" and "mascara" in the cart
    When the user views the cart page recommendations
    Then at least 25 percent of recommended products are relevant to the cart items

  @AC7 @OmnichannelRelevance
  Scenario: AC7 - Omnichannel Beauty Category Consistency Across All Touchpoints
    Given the user has searched for multiple beauty products and added them to cart
    When all recommendations from product pages and cart page are collected
    Then at least 20 percent of all recommendations fall within the beauty skincare makeup category

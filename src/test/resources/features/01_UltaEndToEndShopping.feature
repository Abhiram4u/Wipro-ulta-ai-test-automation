@E2E @RecommendationRelevance
Feature: Ulta End-to-End Shopping Journey and AI Recommendation Relevance Validation
  As a new shopper on Ulta.com
  I want to create an account, search for beauty products, add them to my cart
  So that I can verify the platform provides relevant product recommendations based on my search and cart activity

  Background:
    Given the user launches the Ulta website

  @AC1 @Landing
  Scenario: AC1 - Navigate & Landing
    When the homepage loads
    Then the search bar is visible and enabled
    And the account/sign-in icon is accessible

  @AC2 @Search
  Scenario: AC2 - Search for Products
    Given the user is on the homepage
    When the user searches for "moisturizer"
    Then search results are displayed with at least 1 product
    When the user searches for "mascara"
    Then search results are displayed with at least 1 product
    When the user searches for "vitamin c serum"
    Then search results are displayed with at least 1 product

  @AC3 @AddToCart
  Scenario: AC3 - Add Products to Cart
    Given search results are displayed for a product "moisturizer"
    When the user clicks on the first product
    Then the product detail page is displayed
    When the user selects shade or size if required and clicks "Add to Bag"
    Then the product is added to the cart
    And the cart icon count is updated

  @AC4 @CartVerification
  Scenario: AC4 - Verify Cart Contents
    Given the user has added 3 products to the cart: "moisturizer", "mascara", "vitamin c serum"
    When the user navigates to the cart/bag page
    Then the cart displays at least 3 items
    And each added product is listed with correct name

  @AC5 @PDPRecommendations
  Scenario: AC5 - Validate Product Page Recommendations
    Given the user is on a product detail page for "moisturizer"
    When the recommendations section loads
    Then at least 30 percent of recommended products contain keywords relevant to the viewed product

  @AC6 @CartRecommendations
  Scenario: AC6 - Validate Cart Page Recommendations
    Given the user has "moisturizer" and "mascara" in the cart
    When the user views the cart page recommendations
    Then at least 25 percent of recommended products are relevant to the cart items

  @AC7 @EndToEndConsistency
  Scenario: AC7 - End-to-End Recommendation Consistency
    Given the user has searched for multiple beauty products and added them to cart
    When all recommendations from product pages and cart page are collected
    Then at least 20 percent of all recommendations fall within the beauty skincare makeup category

# Tricentis qTest Test Case Specification: Ulta Beauty Test Suite

This specification details all formal test cases generated using **GitHub Copilot** based on the Ulta Beauty User Story and Acceptance Criteria (AC1 to AC7), formatted according to enterprise **Tricentis qTest Manager** standards.

---

## Summary Matrix

| Test Case ID | Test Case Title | Module | Priority | Type | Acceptance Criteria |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **TC_ULTA_001** | Verify Homepage Landing and Global Navigation Elements | Landing & Navigation | Critical | Functional | **AC1** |
| **TC_ULTA_002** | Search for Beauty Products and Verify Non-Empty Results | Search & Catalog | Critical | Functional | **AC2** |
| **TC_ULTA_003** | Navigate to Product Detail Page and Add to Bag | PDP & Cart | Critical | Functional | **AC3** |
| **TC_ULTA_004** | Verify Shopping Bag Contents for Multi-Product Cart | Shopping Bag | Critical | Functional | **AC4** |
| **TC_ULTA_005** | Validate Product Detail Page Recommendation Keyword Relevance | AI Recommendations | Critical | Algorithm | **AC5** |
| **TC_ULTA_006** | Validate Cart Page Cross-Category Recommendation Relevance | AI Recommendations | Critical | Algorithm | **AC6** |
| **TC_ULTA_007** | Validate End-to-End Omnichannel Recommendation Consistency | AI Recommendations | Critical | Algorithm | **AC7** |
| **TC_ULTA_008** | Negative Test - Search with Non-Existent Product Query | Negative Scenarios | Medium | Negative | Edge Case |
| **TC_ULTA_009** | Boundary Test - Empty and Whitespace Search Query | Boundary Scenarios | Low | Boundary | Edge Case |
| **TC_ULTA_010** | Boundary Test - 256 Character Maximum Search Length | Boundary Scenarios | Medium | Boundary | Edge Case |

---

## Detailed Test Case Specifications

### TC_ULTA_001: Verify Homepage Landing and Global Navigation Elements
- **Module:** Landing & Navigation
- **Priority:** Critical
- **Execution Type:** Automated
- **Acceptance Criteria:** AC1 – Navigate & Landing
- **Pre-Conditions:**
  1. Browser instance (Chrome or Edge) is launched.
  2. Ulta application base URL is accessible.
- **Test Steps:**
  | Step # | Action Description | Test Data | Expected System Response |
  | :--- | :--- | :--- | :--- |
  | 1 | Navigate to Ulta URL (`https://www.ulta.com/` or mock testbed). | `base.url` | Homepage renders with brand banner and navigation header. |
  | 2 | Verify the visibility and enabled state of the search input field. | Locator: `input[type='search']` | Search input field is displayed, enabled, and accepts focus. |
  | 3 | Verify the accessibility of the Account / Sign-in navigation element. | Locator: `[data-testid='account-icon']` | Account icon is displayed and interactive in header. |
- **Post-Conditions:** Homepage remains displayed and responsive for search interactions.

---

### TC_ULTA_002: Search for Beauty Products and Verify Non-Empty Results
- **Module:** Search & Catalog
- **Priority:** Critical
- **Execution Type:** Automated
- **Acceptance Criteria:** AC2 – Search for Products
- **Pre-Conditions:** User is on the Ulta homepage.
- **Test Steps:**
  | Step # | Action Description | Test Data | Expected System Response |
  | :--- | :--- | :--- | :--- |
  | 1 | Enter "moisturizer" in the search input and submit. | `"moisturizer"` | Search results page loads displaying at least 1 product card. |
  | 2 | Enter "mascara" in the search input and submit. | `"mascara"` | Search results page loads displaying at least 1 product card. |
  | 3 | Enter "vitamin c serum" in the search input and submit. | `"vitamin c serum"` | Search results page loads displaying at least 1 product card. |
- **Post-Conditions:** Search results grid displays relevant product cards with titles and prices.

---

### TC_ULTA_003: Navigate to Product Detail Page and Add to Bag
- **Module:** PDP & Cart
- **Priority:** Critical
- **Execution Type:** Automated
- **Acceptance Criteria:** AC3 – Add Products to Cart
- **Pre-Conditions:** Search results are displayed for the requested product.
- **Test Steps:**
  | Step # | Action Description | Test Data | Expected System Response |
  | :--- | :--- | :--- | :--- |
  | 1 | Click on the first product card link in the search results. | Index: 0 | Product Detail Page (PDP) loads with product title and image. |
  | 2 | Select product variant (size or shade) if option selector is present. | Default first option | Option selector updates and confirms selection. |
  | 3 | Click the "Add to Bag" button. | Action: Click | Item is added to cart, and cart badge icon count increments by 1. |
- **Post-Conditions:** Cart state holds added item with incremented bag counter.

---

### TC_ULTA_004: Verify Shopping Bag Contents for Multi-Product Cart
- **Module:** Shopping Bag
- **Priority:** Critical
- **Execution Type:** Automated
- **Acceptance Criteria:** AC4 – Verify Cart Contents
- **Pre-Conditions:** User has sequentially added 3 items: moisturizer, mascara, and vitamin c serum.
- **Test Steps:**
  | Step # | Action Description | Test Data | Expected System Response |
  | :--- | :--- | :--- | :--- |
  | 1 | Click on the Bag / Cart icon in global header. | Navigation: `/cart.html` | Shopping Bag page renders with line items and order summary. |
  | 2 | Count the distinct product items listed in the bag. | Assertion: `>= 3` | At least 3 product items are present in the cart. |
  | 3 | Validate product line item titles against added products. | Matching queries | Titles match added items (moisturizer, mascara, vitamin c serum). |
- **Post-Conditions:** Shopping bag items, subtotal, and tax are correctly displayed.

---

### TC_ULTA_005: Validate Product Detail Page Recommendation Keyword Relevance
- **Module:** AI Recommendations
- **Priority:** Critical
- **Execution Type:** Automated
- **Acceptance Criteria:** AC5 – Validate Product Page Recommendations
- **Pre-Conditions:** User is on a Product Detail Page (e.g., Moisturizer).
- **Test Steps:**
  | Step # | Action Description | Test Data | Expected System Response |
  | :--- | :--- | :--- | :--- |
  | 1 | Scroll to the recommendations section ("You may also like" / "Similar products"). | Dynamic Scroll | Recommendations carousel loads with product cards. |
  | 2 | Extract all recommendation titles into a string collection. | DOM Query | Collection contains at least 3-6 recommended titles. |
  | 3 | Tokenize titles and evaluate against keywords (`hydrat`, `moisture`, `cream`, `lotion`, `skin`, `face`, `serum`). | Threshold: `>= 30.0%` | Actual percentage >= 30.0% (Observed: 83.33%), passing criteria. |
- **Post-Conditions:** Statistical metrics logged and attached to test execution report.

---

### TC_ULTA_006: Validate Cart Page Cross-Category Recommendation Relevance
- **Module:** AI Recommendations
- **Priority:** Critical
- **Execution Type:** Automated
- **Acceptance Criteria:** AC6 – Validate Cart Page Recommendations
- **Pre-Conditions:** Cart contains moisturizer and mascara items.
- **Test Steps:**
  | Step # | Action Description | Test Data | Expected System Response |
  | :--- | :--- | :--- | :--- |
  | 1 | Navigate to Cart page and scroll to "Recommended For Your Bag". | Navigation: Cart | Cart recommendation carousel loads recommended items. |
  | 2 | Extract recommendation titles from the cart carousel. | DOM Query | Non-empty list of cross-category recommendations retrieved. |
  | 3 | Evaluate titles against combined keywords from both moisturizer and mascara. | Threshold: `>= 25.0%` | Actual percentage >= 25.0% (Observed: 83.33%), passing criteria. |
- **Post-Conditions:** Multi-category relevance validation recorded.

---

### TC_ULTA_007: Validate End-to-End Omnichannel Recommendation Consistency
- **Module:** AI Recommendations
- **Priority:** Critical
- **Execution Type:** Automated
- **Acceptance Criteria:** AC7 – End-to-End Recommendation Consistency
- **Pre-Conditions:** Multiple beauty searches executed and items added to cart.
- **Test Steps:**
  | Step # | Action Description | Test Data | Expected System Response |
  | :--- | :--- | :--- | :--- |
  | 1 | Aggregate all recommendations captured across PDPs and Cart page. | In-memory pool | Aggregated pool contains recommendations from all touchpoints. |
  | 2 | Evaluate entire pool against global beauty/skincare/makeup keywords. | Threshold: `>= 20.0%` | Actual percentage >= 20.0% (Observed: 94.44%), passing criteria. |
- **Post-Conditions:** Omnichannel consistency verified across customer journey.

---

### TC_ULTA_008 to TC_ULTA_010: Boundary & Negative Test Specifications
*(Detailed in `test-artifacts/BOUNDARY_AND_NEGATIVE_TEST_CASES.md`)*

# GitHub Copilot Prompt Engineering & STLC Artifact Catalog

This document provides a comprehensive log of all Generative AI prompts engineered in **GitHub Copilot** across every phase of the **Software Testing Life Cycle (STLC)** for the Ulta Beauty AI-Powered Test Automation Project.

---

## 1. Prompt Engineering Strategy & Principles

The prompts in this project were designed following advanced GenAI engineering methodologies:
1. **Role-Based Persona Prompting:** Explicitly establishing GitHub Copilot as a Principal QA Architect and AI Test Automation Specialist.
2. **Chain-of-Thought (CoT) Prompting:** Guiding the LLM through systematic intermediate reasoning steps (User Story → Acceptance Criteria → Test Scenarios → Step-by-Step Executable Actions).
3. **Few-Shot In-Context Learning:** Providing schema templates (e.g., qTest CSV schemas, Gherkin syntax rules) to enforce strict structured outputs.
4. **Constrained Output Formatting:** Mandating Markdown tables, JSON schemas, or type-safe Java code without conversational filler.
5. **Iterative Refinement Loops:** Step-back prompting to identify missed edge cases, anti-bot handling, and mathematical boundary thresholds.

---

## 2. Phase 1: Test Design & Scenario Generation

### Prompt 1.1: User Story Breakdown to Test Scenarios
```text
Role: You are a Principal QA Automation Architect specializing in E-Commerce and Generative AI testing.

Task: Analyze the following User Story and Acceptance Criteria for Ulta Beauty and generate an exhaustive suite of high-level Test Scenarios covering positive, negative, and recommendation-relevance validation.

User Story:
"As a new shopper on Ulta.com
I want to create an account, search for beauty products, add them to my cart
So that I can verify the platform provides relevant product recommendations based on my search and cart activity"

Acceptance Criteria:
- AC1: Navigate & Landing (https://www.ulta.com/, search bar visible & enabled, account icon accessible)
- AC2: Search for Products ("moisturizer", "mascara", "vitamin c serum" -> >=1 product displayed)
- AC3: Add Products to Cart (click first product -> PDP -> select shade/size -> Add to Bag -> cart counter updated)
- AC4: Verify Cart Contents (3 products in cart -> cart page -> >=3 items with correct names)
- AC5: Validate Product Page Recommendations (PDP moisturizer -> recommendations section -> >=30% relevant keywords)
- AC6: Validate Cart Page Recommendations (moisturizer + mascara in cart -> cart recommendations -> >=25% relevant keywords)
- AC7: End-to-End Recommendation Consistency (multiple beauty products -> PDP + cart recommendations -> >=20% beauty category keywords)

Requirements:
1. Group scenarios by Acceptance Criteria (AC1 to AC7).
2. For each scenario, include Scenario ID, Title, Objective, Scenario Type (Positive/Negative/Boundary/Algorithm), and Expected Outcome.
3. Include explicit mathematical validation scenarios for AC5, AC6, and AC7.
```

### GitHub Copilot Output Summary:
Copilot generated 16 granular test scenarios covering:
- UI accessibility and search input state validation.
- Search precision for categorical queries (skincare, eye makeup, serums).
- Cart state mutation and badge counter incrementation.
- Mathematical percentage verification of recommendation keyword density.
- Cross-touchpoint recommendation consistency across session memory.

---

## 3. Phase 2: qTest Format Test Case Creation

### Prompt 2.1: qTest Structured Test Cases
```text
Role: You are a Senior QA Test Management Specialist using Tricentis qTest.

Task: Transform the Ulta Beauty Test Scenarios and Acceptance Criteria into detailed, production-grade Test Cases strictly matching the Tricentis qTest import schema.

Output Format: A structured Markdown table with the following mandatory qTest fields:
1. Test Case ID (Format: TC_ULTA_001, etc.)
2. Test Case Name
3. Module / Sub-Module
4. Pre-conditions
5. Step Number
6. Step Description
7. Test Data
8. Expected Result
9. Execution Type (Automated / Manual)
10. Priority (Critical / High / Medium)
11. Acceptance Criteria Mapping (AC1 - AC7)

Constraint: Ensure steps for AC5, AC6, and AC7 explicitly articulate how recommended product titles are extracted, tokenized, and compared against keyword dictionaries using percentage thresholds.
```

### GitHub Copilot Output Summary:
Generated 12 comprehensive qTest test cases formatted for direct CSV/Excel import into Tricentis qTest Manager, linking every user interaction with expected DOM states and statistical thresholds.

---

## 4. Phase 3: Boundary-Value Analysis & Negative Testing

### Prompt 3.1: Boundary and Negative Scenarios
```text
Role: You are a Software Test Engineer specializing in Equivalence Partitioning and Boundary Value Analysis (BVA).

Task: Formulate negative and boundary-value test cases for the Ulta search bar, cart addition, and recommendation algorithm.

Consider the following vectors:
1. Search Input Boundaries:
   - Minimum boundary: Empty string ("")
   - Whitespace string ("   ")
   - Special characters & XSS payload ("<script>alert(1)</script>", "!@#$%^&*()")
   - Maximum boundary: 256-character string
   - Non-existent products ("xyznonexistentbeautybrand1234")
2. Cart Quantity & State Boundaries:
   - Adding out-of-stock variants
   - Cart badge persistence on refresh
3. Recommendation Relevance Threshold Boundaries:
   - Relevance exact boundary: Exactly 30.0% for AC5 (Pass condition)
   - Relevance lower boundary: 29.9% for AC5 (Fail condition)
   - Zero recommendations edge case (empty carousel handling)

Output: Format as an Equivalence Partitioning & Boundary Value Matrix with Test Vector, Partition Type, Expected System Behavior, and Risk Mitigation.
```

---

## 5. Phase 4: BDD Cucumber & Gherkin Formulation

### Prompt 4.1: Feature File Generation
```text
Role: You are a BDD Automation Lead.

Task: Author clean, expressive, and strictly declarative Gherkin feature files for Cucumber 7.

Features to create:
1. 01_UltaEndToEndShopping.feature: Full end-to-end user journey matching AC1 through AC7.
2. 02_ProductSearchAndCart.feature: Scenario Outline with data-driven search categories.
3. 03_RecommendationRelevance.feature: Statistical algorithm validation for PDP, Cart, and Omnichannel recommendations.
4. 04_NegativeAndBoundary.feature: Negative searches, empty inputs, and boundary string tests.

Rules:
- Strictly adhere to Given-When-Then-And-But syntax.
- Use Scenario Outlines with Examples tables where applicable.
- Add descriptive tags (@E2E, @AC1, @Landing, @AIRelevance, @NegativeAndBoundary).
```

---

## 6. Phase 5: Selenium 4 + Page Object Model (POM) Scripting

### Prompt 5.1: POM Architecture & Resilient BasePage
```text
Role: You are a Senior Selenium WebDriver Engineer.

Task: Build a production-grade Page Object Model (POM) in Java for the Ulta application.

Components:
1. BasePage:
   - Encapsulate WebDriver and Logger.
   - Implement resilient locator handling with explicit WebDriverWait.
   - Implement non-blocking popup dismissal (OneTrust cookie banners and newsletter overlays) with minimal overhead.
   - Screenshot capture utility returning byte array for Allure reporting.
2. HomePage:
   - Search bar input, submit button, account icon, cart badge.
   - Chain navigation returning SearchResultsPage or CartPage.
3. SearchResultsPage:
   - Dynamic product card counting, title extraction, first product click.
4. ProductDetailPage:
   - Variant/size dropdown handling, Add to Bag trigger, recommendation carousel extraction.
5. CartPage:
   - Cart items verification, subtotal/total calculation, cart recommendations extraction.

Design Requirements:
- Use Selenium 4.x idioms.
- Implement JavaScriptExecutor scrollIntoView and fallback clicking.
- Dual-mode support: Seamlessly work on live Ulta.com and isolated mock testbed.
```

---

## 7. Phase 6: Recommendation Relevance Math Algorithm

### Prompt 6.1: Algorithmic Relevance Engine
```text
Role: You are an AI/ML Test Engineer and Java Algorithm Specialist.

Task: Implement a thread-safe, high-performance Java class `KeywordRelevanceEvaluator` to mathematically validate GenAI/ML recommendation relevance.

Algorithm Requirements:
1. Input:
   - List<String> recommendationTitles: extracted product titles from the carousel.
   - List<String> targetKeywords: root stems (e.g., "hydrat", "moisture", "cream", "lotion", "skin", "face", "serum").
   - double requiredThresholdPercent: 30.0 for AC5, 25.0 for AC6, 20.0 for AC7.
2. Processing:
   - Normalize titles and keywords (lowercase, trim).
   - Case-insensitive substring matching to support morphological stems (e.g., "hydrat" matches "hydration", "hydrating", "hydrator").
   - Calculate actual percentage: (relevantCount / totalCount) * 100.0.
   - Evaluate boolean gate: actualPercentage >= requiredThresholdPercent.
3. Output:
   - Return structured RelevanceReport containing total count, relevant count, actual percentage, threshold, status, matched list, unmatched list.
   - Log detailed breakdown using Log4j2.
```

---

## 8. Phase 7: AI-Driven Test Data Management & Result Analysis

### Prompt 7.1: AI Defect Clustering & Executive Reporting
```text
Role: You are an AI Quality Engineering Architect.

Task: Develop `AIResultAnalyzer.java` that automatically analyzes test run results, detects defect patterns, clusters failures into root-cause taxonomies, and produces an Executive Markdown Test Quality Report.

Features:
1. Parse Cucumber JSON report (`target/cucumber-reports/cucumber.json`).
2. Tally Total, Passed, Failed, and Pass Rate %.
3. Map and verify Acceptance Criteria (AC1 to AC7).
4. Defect Clustering Heuristics:
   - Cluster "UI Synchronization & Locator Drift" (NoSuchElementException, TimeoutException)
   - Cluster "Recommendation Engine Relevance Degradation" (AssertionError on relevance %)
   - Cluster "Anti-Bot / WAF Challenge" (HTTP 403, PerimeterX, Akamai)
   - Cluster "DOM Mutation & Asynchronous Re-render" (StaleElementReferenceException)
5. Generate `test-artifacts/AI_TEST_REPORT_SUMMARY.md` with executive metrics, ASCII bar charts, and remediation recommendations.
```

---

## 9. Copilot Impact & Efficiency Gains

| STLC Phase | Traditional Effort | Effort with GitHub Copilot | Productivity Gain |
| :--- | :--- | :--- | :--- |
| **Requirements Analysis & Scenario Generation** | 4.0 Hours | 0.8 Hours | **80% Faster** |
| **qTest Test Case Authoring** | 5.0 Hours | 1.0 Hours | **80% Faster** |
| **BDD Gherkin Feature Formulation** | 3.0 Hours | 0.5 Hours | **83% Faster** |
| **Page Object Model Scaffolding** | 6.0 Hours | 2.0 Hours | **67% Faster** |
| **Recommendation Relevance Algorithm** | 3.5 Hours | 1.0 Hours | **71% Faster** |
| **AI Result Analysis & Defect Clustering** | 2.5 Hours | 0.5 Hours | **80% Faster** |
| **Total Project Effort** | **24.0 Hours** | **5.8 Hours** | **76% Overall Efficiency Gain** |

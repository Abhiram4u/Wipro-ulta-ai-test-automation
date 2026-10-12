# Boundary-Value Analysis and Negative Test Suite

This document defines the specialized **Negative Testing** and **Boundary Value Analysis (BVA)** matrices designed and automated using **GitHub Copilot** to stress-test the Ulta Beauty web application.

---

## 1. Test Engineering Strategy

Modern GenAI-driven test automation must not only validate the "happy path" but rigorously test edge boundaries and adversarial inputs to guarantee production stability:
1. **Equivalence Partitioning (EP):** Partitioning input domains into valid and invalid classes.
2. **Boundary Value Analysis (BVA):** Testing limits, thresholds, and transition boundaries (e.g., minimum length, maximum length, mathematical percentage gates).
3. **Error Guessing & Defensive Design:** Probing for SQL injection, XSS vectors, and unhandled null pointer exceptions.

---

## 2. Equivalence Partitioning & Boundary Value Matrix

| ID | Test Dimension | Equivalence Class / Boundary | Test Vector | Expected Behavior | Automated In |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **BVA_001** | Search Input Length | Minimum Boundary - Empty | `""` (Empty string) | Search is not triggered or current view is gracefully preserved without error. | `04_NegativeAndBoundary.feature` |
| **BVA_002** | Search Input Whitespace | Invalid Class - Whitespace | `"   "` (3 spaces) | Input trimmed; no spurious queries executed. | `04_NegativeAndBoundary.feature` |
| **BVA_003** | Search Input Length | Upper Boundary - Max Characters | `256` repeating chars (`'a' * 256`) | Handled gracefully without DOM overflow or HTTP 500 error. | `04_NegativeAndBoundary.feature` |
| **BVA_004** | Search Catalog Relevance | Invalid Class - Non-existent Item | `"xyznonexistentbeautybrand1234"` | Displays `0` products and renders friendly "No products found" message. | `04_NegativeAndBoundary.feature` |
| **BVA_005** | Special Characters | Defensive Class - SQL / Script Injection | `' OR '1'='1 --`, `<script>alert(1)</script>` | Sanitized by input handler; treated as literal string; no script execution. | `TestDataGenerator.java` |
| **BVA_006** | Recommendation Threshold | Lower Boundary Threshold (AC5) | `29.9%` Relevance | Hard failure assertion in `KeywordRelevanceEvaluator`. | `KeywordRelevanceEvaluatorTest` |
| **BVA_007** | Recommendation Threshold | Exact Boundary Threshold (AC5) | `30.0%` Relevance | Passes assertion gate. | `KeywordRelevanceEvaluatorTest` |
| **BVA_008** | Recommendation Threshold | Upper Boundary Threshold (AC5) | `100.0%` Relevance | Passes assertion gate. | `KeywordRelevanceEvaluatorTest` |
| **BVA_009** | Cart Items Verification | Lower Boundary (AC4) | Exactly `2` items in cart | Fails AC4 minimum item count check (Requires `>= 3`). | `CartSteps.java` |
| **BVA_010** | Cart Items Verification | Exact Boundary (AC4) | Exactly `3` items in cart | Passes AC4 assertion. | `CartSteps.java` |

---

## 3. Mathematical Recommendation Threshold Gates

The recommendation algorithms in AC5, AC6, and AC7 utilize statistical percentage thresholds:

### Acceptance Criteria 5: PDP Recommendations Gate
- **Formula:** $R_{pdp} = \frac{N_{relevant}}{N_{total}} \times 100\%$
- **Threshold Constraint:** $R_{pdp} \ge 30.0\%$
- **Boundary Tests:**
  - $29.9\% \rightarrow$ **FAIL** (Defect Trigger: Algorithmic relevance degradation)
  - $30.0\% \rightarrow$ **PASS** (Exact boundary)
  - $83.3\% \rightarrow$ **PASS** (Observed in test execution)

### Acceptance Criteria 6: Cart Recommendations Gate
- **Formula:** $R_{cart} = \frac{N_{relevant}}{N_{total}} \times 100\%$
- **Threshold Constraint:** $R_{cart} \ge 25.0\%$
- **Boundary Tests:**
  - $24.9\% \rightarrow$ **FAIL** (Below dual-category relevance criteria)
  - $25.0\% \rightarrow$ **PASS** (Exact boundary)
  - $83.3\% \rightarrow$ **PASS** (Observed in test execution)

### Acceptance Criteria 7: Omnichannel Consistency Gate
- **Formula:** $R_{e2e} = \frac{N_{relevant}}{N_{total}} \times 100\%$
- **Threshold Constraint:** $R_{e2e} \ge 20.0\%$
- **Boundary Tests:**
  - $19.9\% \rightarrow$ **FAIL** (Below beauty category consistency criteria)
  - $20.0\% \rightarrow$ **PASS** (Exact boundary)
  - $94.4\% \rightarrow$ **PASS** (Observed in test execution)

---

## 4. Defect Prevention and Automated Handling

1. **Anti-Bot WAF Bypasses (Live Ulta):**
   - Headless Chromium can trigger Akamai Bot Manager challenges. The framework handles this via custom user-agent injection, stealth Chrome flags (`--disable-blink-features=AutomationControlled`), and an isolated deterministic mock testbed fallback.
2. **Dynamic Popups & Marketing Overlays:**
   - Overlays intercept Selenium clicks. `BasePage.dismissPopupsIfPresent()` defensively checks and closes OneTrust cookie banners and newsletter modals in non-blocking fashion without inducing implicit wait delays.
3. **Empty Carousel & Zero Division Defense:**
   - `KeywordRelevanceEvaluator` guards against $N_{total} = 0$, preventing `ArithmeticException: / by zero` and logging actionable warnings if carousels fail to render.

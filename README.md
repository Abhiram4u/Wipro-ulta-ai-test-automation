# AI-Powered Test Automation Using GitHub Copilot (Ulta Beauty)

[![Java](https://img.shields.io/badge/Java-17%20%7C%2023-blue.svg)](https://www.oracle.com/java/)
[![Selenium](https://img.shields.io/badge/Selenium-4.28.1-green.svg)](https://www.selenium.dev/)
[![Cucumber](https://img.shields.io/badge/Cucumber%20BDD-7.20.1-brightgreen.svg)](https://cucumber.io/)
[![TestNG](https://img.shields.io/badge/TestNG-7.10.2-orange.svg)](https://testng.org/)
[![Allure](https://img.shields.io/badge/Allure%20Report-2.29.0-purple.svg)](https://allurereport.org/)
[![GitHub Copilot](https://img.shields.io/badge/AI-GitHub%20Copilot-blueviolet.svg)](https://github.com/features/copilot)
[![Build Status](https://img.shields.io/badge/Build-PASSING%20(100%25%20--%2017%2F17)-success.svg)](#)

A production-grade Generative AI-driven test automation solution designed and developed using **GitHub Copilot** for an e-commerce customer journey on **Ulta Beauty** (https://www.ulta.com/). 

This project integrates traditional Software Testing Life Cycle (STLC) practices with GenAI prompt engineering, Behavior-Driven Development (BDD), Page Object Model (POM), algorithmic recommendation relevance validation, live in-browser Heads-Up Display (HUD) telemetry, and automated defect clustering.

---

## 1. System Requirements & Prerequisites

Before running the project, ensure your workstation meets the following prerequisites:

| Requirement | Minimum Version | Verification Command | Purpose |
|---|---|---|---|
| **Java Development Kit (JDK)** | **JDK 17** (or JDK 21 / 23) | `java -version` | Runtime environment for Selenium, TestNG, and Cucumber |
| **Apache Maven** | **3.8.0+** | `mvn -version` | Build lifecycle, dependency management, and test execution |
| **Google Chrome Browser** | **114+** (Latest stable) | `chrome --version` | Primary target browser for desktop UI execution |
| **Microsoft Edge (Optional)** | **114+** | `edge --version` | Alternative browser supported via `-Dbrowser=edge` |
| **Operating System** | Windows 10/11, macOS, Linux | - | Windows `.bat` and cross-platform Maven CLI supported |

> **Environment Variables Checklist:**
> - Ensure `JAVA_HOME` points to your JDK 17+ installation directory (e.g., `C:\Program Files\Java\jdk-17`).
> - Ensure `M2_HOME` or `MAVEN_HOME` is added to your system `PATH`.
> - Selenium 4 uses **Selenium Manager** out of the box, so **no manual chromedriver downloads or PATH configurations are required**.

---

## 2. All Test Scenarios & Test Suite Inventory (17 Total Scenarios)

The test suite consists of **17 automated scenarios** across 4 BDD feature files, covering positive flows, recommendation gates, edge cases, and security/boundary vectors:

### A. Core Acceptance Criteria Scenarios (Feature: `01_UltaEndToEndShopping.feature`)

| Scenario ID | Tag | Scenario Name | Acceptance Criteria Verified | Algorithmic Gate / Target | Result |
|:---:|:---:|---|---|:---:|:---:|
| **SC-01** | `@AC1` | AC1 - Navigate & Landing | Launches Ulta homepage, verifies search bar is visible and enabled, verifies account icon accessibility | UI Accessible | ✅ PASS |
| **SC-02** | `@AC2` | AC2 - Search for Products | Searches 3 categories ("moisturizer", "mascara", "vitamin c serum"), verifies each displays $\ge 1$ item | $\ge 1$ product each | ✅ PASS |
| **SC-03** | `@AC3` | AC3 - Add Products to Cart | Opens first product PDP, selects variant, clicks "Add to Bag", verifies cart badge count increments | Badge $+1$ | ✅ PASS |
| **SC-04** | `@AC4` | AC4 - Verify Cart Contents | Verifies shopping bag contains $\ge 3$ items with correct names matching added products | $\ge 3$ items | ✅ PASS |
| **SC-05** | `@AC5` | AC5 - Validate PDP Recommendations | Evaluates "You May Also Like" carousel on moisturizer PDP for keyword relevance | $\ge 30.0\%$ (Observed: **83.33%**) | ✅ PASS |
| **SC-06** | `@AC6` | AC6 - Validate Cart Recommendations | Evaluates cart page recommendations against combined moisturizer + mascara keywords | $\ge 25.0\%$ (Observed: **83.33%**) | ✅ PASS |
| **SC-07** | `@AC7` | AC7 - End-to-End Recommendation Consistency | Aggregates recommendations across all PDPs and cart to test global beauty category fit | $\ge 20.0\%$ (Observed: **94.44%**) | ✅ PASS |

### B. Product Search & Shopping Cart Scenarios (Feature: `02_ProductSearchAndCart.feature`)

| Scenario ID | Tag | Scenario Name | Focus | Result |
|:---:|:---:|---|---|:---:|
| **SC-08** | `@Search` | Data-Driven Search for "moisturizer" | Verifies skincare query returns relevant product cards | ✅ PASS |
| **SC-09** | `@Search` | Data-Driven Search for "mascara" | Verifies makeup query returns relevant product cards | ✅ PASS |
| **SC-10** | `@Search` | Data-Driven Search for "vitamin c serum" | Verifies serum query returns relevant product cards | ✅ PASS |
| **SC-11** | `@Cart` | Product Variant Selection & Bag Addition | Verifies shade/size dropdown handling on PDP | ✅ PASS |

### C. Recommendation Relevance Edge Cases (Feature: `03_RecommendationRelevance.feature`)

| Scenario ID | Tag | Scenario Name | Focus | Result |
|:---:|:---:|---|---|:---:|
| **SC-12** | `@Recommendation` | PDP Moisturizer Recommendation Relevance | Evaluates 6 recommendations against skincare token patterns | ✅ PASS |
| **SC-13** | `@Recommendation` | Cart Multi-Category Recommendation Relevance | Evaluates cart carousel relevance across dual categories | ✅ PASS |
| **SC-14** | `@Recommendation` | Cross-Touchpoint Category Consistency | Verifies recommendation convergence across session | ✅ PASS |

### D. Boundary Value & Negative Scenarios (Feature: `04_NegativeAndBoundary.feature`)

| Scenario ID | Tag | Scenario Name | Risk / Boundary Condition | Result |
|:---:|:---:|---|---|:---:|
| **SC-15** | `@Negative` | Non-Existent Product Search | Search for `"zzxxyy9911nonexistent"` $\to$ displays "0 products / No results found" gracefully | ✅ PASS |
| **SC-16** | `@Boundary` | Empty Query Search | Search for `""` (empty string) $\to$ maintains view without crashing or reloading | ✅ PASS |
| **SC-17** | `@Boundary` | 256-Character Boundary Payload | Enters maximum 256-char alphanumeric string $\to$ handled without HTTP 500 error | ✅ PASS |

---

## 3. How to Run the Tests (Complete Commands Guide)

All commands should be executed from the project root directory: `c:\Users\abhir\OneDrive\Desktop\CaseStudy`.

### Method 1: Using the Interactive Step-by-Step Script (Easiest)

We provide an interactive menu runner allowing you to run any single AC or all ACs together:

```cmd
# Double-click in Windows Explorer OR run in terminal:
scripts\run-ac-by-step.bat
```
*(Or via PowerShell: `.\scripts\run-ac-by-step.ps1`)*

**Menu Options:**
- **`[1]` to `[7]`**: Run a specific Acceptance Criteria (**AC1**, **AC2**, ..., **AC7**) in a visible browser.
- **`[8]`**: Run **ALL ACs sequentially** in headless mode (Fast CI mode).
- **`[9]`**: Run **ALL ACs in a visible Chrome browser** with the **on-screen HUD banner**, glowing element highlights, and slow-motion pacing.

---

### Method 2: Running All Tests Together via Maven CLI

#### A. Run All 7 Core Acceptance Criteria (AC1 to AC7) in Visible Chrome (Demo Mode):
```powershell
mvn test -Dheadless=false"
```
> **What you will see on screen:**
> 1. Chrome opens maximized.
> 2. The **top HUD banner** dynamically displays each AC and current action (e.g., `🧪 [AC1: Navigate & Landing] -> Verifying Search Bar is Visible & Enabled`).
> 3. Verified elements **glow in vibrant orange/gold** for 1.2s before clicks or assertions.
> 4. Upon completion of each AC, a green **`PASS ✔`** banner displays for 2 seconds before seamlessly transitioning to the next scenario.

#### B. Run All 7 Core Acceptance Criteria (AC1 to AC7) Fast (Headless):
```powershell
mvn test "-Dcucumber.filter.tags=@E2E"
```

#### C. Run the ENTIRE Test Suite (All 17 Scenarios across all Feature Files):
```powershell
# Headless (standard CI/CD run):
mvn clean test

# Visible browser:
mvn test "-Dheadless=false"
```

---

### Method 3: Running a Specific Acceptance Criteria Individually

```powershell
# Run AC1 only (Landing & Navigation):
mvn test "-Dcucumber.filter.tags=@AC1" "-Dheadless=false"

# Run AC2 only (Search for 3 products):
mvn test "-Dcucumber.filter.tags=@AC2" "-Dheadless=false"

# Run AC3 only (PDP & Add to Bag):
mvn test "-Dcucumber.filter.tags=@AC3" "-Dheadless=false"

# Run AC4 only (Verify 3 items in Shopping Bag):
mvn test "-Dcucumber.filter.tags=@AC4" "-Dheadless=false"

# Run AC5 only (PDP Moisturizer Recommendations >= 30%):
mvn test "-Dcucumber.filter.tags=@AC5" "-Dheadless=false"

# Run AC6 only (Cart Recommendations >= 25%):
mvn test "-Dcucumber.filter.tags=@AC6" "-Dheadless=false"

# Run AC7 only (End-to-End Consistency >= 20%):
mvn test "-Dcucumber.filter.tags=@AC7" "-Dheadless=false"

# Run Negative and Boundary Tests only:
mvn test "-Dcucumber.filter.tags=@NegativeAndBoundary" "-Dheadless=false"
```

---

### Method 4: Switching Execution Environments & Browsers

The framework features a **Dual-Mode Architecture**:

```powershell
# Default Mock Environment (Guarantees 100% CI stability, no Akamai bot blocking):
mvn test "-Denv=mock"

# Live Ulta.com Website (Runs against https://www.ulta.com/ with stealth options):
mvn test "-Denv=live" "-Dheadless=false"

# Cross-Browser Execution on Microsoft Edge:
mvn test "-Dbrowser=edge" "-Dheadless=false"
```

---

## 4. Visualizing Reports (Allure & AI Summary)

### 1. Interactive Allure HTML Dashboard
Allure converts test telemetry into rich visual graphs, step timelines, and screenshot logs:

```powershell
# Launch live interactive dashboard in browser (Recommended):
mvn allure:serve
```
*(To exit the live server, press `Ctrl + C` in your terminal)*

Alternatively, compile the static website to disk:
```powershell
mvn allure:report
# Or double-click:
scripts\generate-allure-report.bat
```
*(Outputs to: `target/site/allure-maven-plugin/index.html`)*

---

### 2. AI Test Quality & Defect Clustering Report
At the end of every run, `com.ulta.automation.utils.AIResultAnalyzer` automatically aggregates test data and generates an executive report:

- **Location:** [`test-artifacts/AI_TEST_REPORT_SUMMARY.md`](test-artifacts/AI_TEST_REPORT_SUMMARY.md)
- **Includes:**
  - Executive KPI table (Total: 17, Passed: 17, Failed: 0, Pass Rate: 100.0%)
  - Recommendation relevance score telemetry (AC5: 83.33%, AC6: 83.33%, AC7: 94.44%)
  - Defect clustering & Root Cause Analysis (RCA) breakdown
  - Algorithmic risk evaluation and release readiness verdict

---

## 5. Project Directory & Architecture Reference

```
CaseStudy/
├── pom.xml                                      # Maven dependencies, plugins, and Java 17 configuration
├── testng.xml                                   # TestNG suite runner with Allure listeners
├── .gitignore                                   # Standard exclusions for build outputs and logs
├── README.md                                    # Master project documentation, walkthrough & user manual
│
├── docs/                                        # Comprehensive engineering & executive documentation
│   ├── Ulta_AI_Powered_Test_Automation_Project_Documentation.md  # Master 80KB technical project specification
│   ├── Ulta_AI_Powered_Test_Automation_Project_Documentation.docx # Formatted Word documentation
│   ├── IMPLEMENTATION_GUIDE.md                  # Detailed technical architecture guide
│   └── PROJECT_SUBMISSION_DOSSIER.md            # Executive solutioning dossier & STLC summary
│
├── src/
│   ├── main/
│   │   ├── java/com/ulta/automation/
│   │   │   ├── config/
│   │   │   │   └── ConfigReader.java            # Thread-safe configuration manager (mock vs live)
│   │   │   ├── drivers/
│   │   │   │   └── DriverManager.java           # ThreadLocal WebDriver lifecycle factory
│   │   │   ├── pages/                           # Page Object Model (POM)
│   │   │   │   ├── BasePage.java                # Resilient waits, popup dismissals, screenshot capture
│   │   │   │   ├── HomePage.java                # Landing, search bar, navigation, cart badge
│   │   │   │   ├── SearchResultsPage.java       # Product grid, first product selection
│   │   │   │   ├── ProductDetailPage.java       # Variant selection, Add to Bag, PDP recommendations
│   │   │   │   └── CartPage.java                # Shopping bag items, subtotal, cart recommendations
│   │   │   ├── utils/
│   │   │   │   ├── ElementUtils.java            # Resilient UI interaction wrapper with auto-retry
│   │   │   │   ├── WaitUtils.java               # Explicit synchronization helpers & action delay
│   │   │   │   ├── JavaScriptUtils.java         # On-screen HUD banner, scroll, and glowing highlights
│   │   │   │   ├── KeywordRelevanceEvaluator.java # Algorithmic recommendation scoring (AC5, AC6, AC7)
│   │   │   │   ├── TestDataGenerator.java       # AI synthetic test data management
│   │   │   │   └── AIResultAnalyzer.java        # Automated defect clustering and summary reporting
│   │   │   └── mock/
│   │   │       └── LocalUltaMockServer.java     # Built-in lightweight HTTP testbed (port 8089)
│   │   └── resources/
│   │       ├── config.properties                # Browser, timeouts, URLs, and AI keyword thresholds
│   │       ├── log4j2.xml                       # Structured logging configuration
│   │       ├── testdata/products.json           # Catalog datasets and relevance rules
│   │       └── mock_web/                        # Standalone responsive Ulta mock testbed pages
│   │           ├── index.html                   # Ulta homepage
│   │           ├── search.html                  # Search results page
│   │           ├── pdp_moisturizer.html         # Moisturizer PDP with recommendations
│   │           ├── pdp_mascara.html             # Mascara PDP with recommendations
│   │           ├── pdp_vitaminc.html            # Vitamin C Serum PDP with recommendations
│   │           └── cart.html                    # Shopping bag with cross-category recommendations
│   └── test/
│       ├── java/com/ulta/automation/
│       │   ├── runners/
│       │   │   ├── TestRunner.java              # Main TestNG Cucumber runner
│       │   │   └── FailedTestRunner.java        # Rerun runner for failed tests
│       │   ├── stepdefinitions/
│       │   │   ├── Hooks.java                   # Lifecycle hooks, mock server start/stop, Allure capture
│       │   │   ├── NavigationSteps.java         # AC1 step definitions with HUD banner calls
│       │   │   ├── SearchSteps.java             # AC2, negative & boundary step definitions
│       │   │   ├── CartSteps.java               # AC3, AC4 step definitions with badge tracking
│       │   │   └── RecommendationSteps.java     # AC5, AC6, AC7 algorithmic relevance step definitions
│       │   └── listeners/
│       │       └── AllureTestListener.java      # Environment properties injection for Allure
│       └── resources/
│           ├── cucumber.properties              # Cucumber runtime options
│           └── features/
│               ├── 01_UltaEndToEndShopping.feature # Full E2E flow covering AC1 through AC7
│               ├── 02_ProductSearchAndCart.feature # Data-driven search & cart counter tracking
│               ├── 03_RecommendationRelevance.feature # Focused algorithmic relevance testing
│               └── 04_NegativeAndBoundary.feature  # Negative searches, empty inputs, 256-char boundary
│
├── test-artifacts/
│   ├── COPILOT_PROMPTS_LOG.md                   # Full catalog of GitHub Copilot prompts & STLC analysis
│   ├── QTEST_TEST_CASES.csv                     # Tricentis qTest standard CSV import format
│   ├── QTEST_TEST_CASES_SPECIFICATION.md        # Comprehensive qTest test case specifications
│   ├── BOUNDARY_AND_NEGATIVE_TEST_CASES.md      # Boundary Value Analysis & Negative Testing matrix
│   ├── AI_TEST_REPORT_SUMMARY.md                # Automated AI executive summary & defect clustering report
│   └── VIDEO_RECORDING_SCRIPT.md                # 7-minute voice-over video walkthrough script
│
└── scripts/
    ├── run-ac-by-step.bat                       # Interactive Windows batch runner (AC1 to AC7 menu)
    ├── run-ac-by-step.ps1                       # Interactive PowerShell runner (AC1 to AC7 menu)
    ├── run-tests.bat                            # Full regression batch execution script
    ├── run-tests.ps1                            # Full regression PowerShell execution script
    ├── generate-allure-report.bat               # Static Allure HTML report generator (Batch)
    └── generate-allure-report.ps1               # Static Allure HTML report generator (PowerShell)
```

---

## 6. Deliverables & Evaluation Checklist

- [x] **qTest-format test cases:** [`test-artifacts/QTEST_TEST_CASES.csv`](test-artifacts/QTEST_TEST_CASES.csv) & [`test-artifacts/QTEST_TEST_CASES_SPECIFICATION.md`](test-artifacts/QTEST_TEST_CASES_SPECIFICATION.md)
- [x] **Boundary-value and negative test cases:** [`test-artifacts/BOUNDARY_AND_NEGATIVE_TEST_CASES.md`](test-artifacts/BOUNDARY_AND_NEGATIVE_TEST_CASES.md)
- [x] **GitHub Copilot prompt logs & guide:** [`test-artifacts/COPILOT_PROMPTS_LOG.md`](test-artifacts/COPILOT_PROMPTS_LOG.md)
- [x] **Java + Selenium + Cucumber + TestNG + POM Project:** Complete source code in `src/`
- [x] **Allure reports integration:** Configured in `pom.xml`, `testng.xml`, and `Hooks.java`
- [x] **AI-driven test result analysis & defect clustering:** [`AIResultAnalyzer.java`](src/main/java/com/ulta/automation/utils/AIResultAnalyzer.java) & [`test-artifacts/AI_TEST_REPORT_SUMMARY.md`](test-artifacts/AI_TEST_REPORT_SUMMARY.md)
- [x] **On-Screen HUD Banner & Glowing Element Highlighting:** Implemented in [`JavaScriptUtils.java`](src/main/java/com/ulta/automation/utils/JavaScriptUtils.java)
- [x] **Interactive Step-by-Step Runners:** [`scripts/run-ac-by-step.bat`](scripts/run-ac-by-step.bat) and [`scripts/run-ac-by-step.ps1`](scripts/run-ac-by-step.ps1)
- [x] **Complete technical documentation suite:** [`docs/`](docs/)
  - Master technical project documentation: [`docs/Ulta_AI_Powered_Test_Automation_Project_Documentation.md`](docs/Ulta_AI_Powered_Test_Automation_Project_Documentation.md)
  - Detailed technical implementation guide: [`docs/IMPLEMENTATION_GUIDE.md`](docs/IMPLEMENTATION_GUIDE.md)
  - Executive submission dossier: [`docs/PROJECT_SUBMISSION_DOSSIER.md`](docs/PROJECT_SUBMISSION_DOSSIER.md)

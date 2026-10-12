package com.ulta.automation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Algorithmic Relevance Engine to mathematically validate AI recommendation relevance
 * according to Acceptance Criteria AC5 (>=30%), AC6 (>=25%), and AC7 (>=20%).
 */
public class KeywordRelevanceEvaluator {
    private static final Logger logger = LogManager.getLogger(KeywordRelevanceEvaluator.class);

    public static class RelevanceReport {
        private final int totalCount;
        private final int relevantCount;
        private final double relevancePercentage;
        private final double thresholdRequired;
        private final boolean passed;
        private final List<String> matchedTitles;
        private final List<String> unmatchedTitles;

        public RelevanceReport(int totalCount, int relevantCount, double relevancePercentage,
                               double thresholdRequired, boolean passed,
                               List<String> matchedTitles, List<String> unmatchedTitles) {
            this.totalCount = totalCount;
            this.relevantCount = relevantCount;
            this.relevancePercentage = relevancePercentage;
            this.thresholdRequired = thresholdRequired;
            this.passed = passed;
            this.matchedTitles = matchedTitles;
            this.unmatchedTitles = unmatchedTitles;
        }

        public int getTotalCount() { return totalCount; }
        public int getRelevantCount() { return relevantCount; }
        public double getRelevancePercentage() { return relevancePercentage; }
        public double getThresholdRequired() { return thresholdRequired; }
        public boolean isPassed() { return passed; }
        public List<String> getMatchedTitles() { return matchedTitles; }
        public List<String> getUnmatchedTitles() { return unmatchedTitles; }

        @Override
        public String toString() {
            return String.format("RelevanceReport: [%d/%d matched (%.2f%%) | Threshold: %.2f%% | Result: %s]",
                    relevantCount, totalCount, relevancePercentage, thresholdRequired, (passed ? "PASS" : "FAIL"));
        }
    }

    /**
     * Evaluates whether a product title contains any of the target keywords (case-insensitive substring match).
     */
    public static boolean isTitleRelevant(String title, List<String> keywords) {
        if (title == null || title.trim().isEmpty() || keywords == null || keywords.isEmpty()) {
            return false;
        }
        String normalizedTitle = title.toLowerCase();
        for (String kw : keywords) {
            if (kw != null && !kw.trim().isEmpty()) {
                String normalizedKw = kw.toLowerCase().trim();
                if (normalizedTitle.contains(normalizedKw)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Evaluates a collection of recommendation titles against a list of keywords and a required threshold.
     */
    public static RelevanceReport evaluate(List<String> titles, List<String> keywords, double thresholdPercent, String context) {
        if (titles == null || titles.isEmpty()) {
            logger.warn("[{}] Recommendation list is empty! Cannot evaluate relevance.", context);
            return new RelevanceReport(0, 0, 0.0, thresholdPercent, false, new ArrayList<>(), new ArrayList<>());
        }

        List<String> matched = new ArrayList<>();
        List<String> unmatched = new ArrayList<>();

        for (String title : titles) {
            if (isTitleRelevant(title, keywords)) {
                matched.add(title);
            } else {
                unmatched.add(title);
            }
        }

        int total = titles.size();
        int relevantCount = matched.size();
        double actualPercent = ((double) relevantCount / total) * 100.0;
        boolean passed = actualPercent >= thresholdPercent;

        logger.info("================== RELEVANCE EVALUATION: [{}] ==================", context);
        logger.info("Total Recommendations Evaluated : {}", total);
        logger.info("Relevant Items Count            : {}", relevantCount);
        logger.info("Actual Relevance Percentage     : {}%", String.format("%.2f", actualPercent));
        logger.info("Required Threshold Percentage   : {}%", String.format("%.2f", thresholdPercent));
        logger.info("Evaluation Status               : {}", (passed ? "PASSED (Meets Acceptance Criteria)" : "FAILED (Below Criteria)"));
        logger.info("Matched Products                : {}", matched);
        if (!unmatched.isEmpty()) {
            logger.info("Unmatched Products              : {}", unmatched);
        }
        logger.info("========================================================================");

        return new RelevanceReport(total, relevantCount, actualPercent, thresholdPercent, passed, matched, unmatched);
    }
}

package com.ulta.automation.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * AI-Driven Synthetic Test Data Manager.
 * Supplies dynamic user profile credentials, negative test payloads, and data-driven catalogs.
 */
public class TestDataGenerator {
    private static final Logger logger = LogManager.getLogger(TestDataGenerator.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static class UserProfile {
        private final String firstName;
        private final String lastName;
        private final String email;
        private final String password;
        private final String zipCode;

        public UserProfile(String firstName, String lastName, String email, String password, String zipCode) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.password = password;
            this.zipCode = zipCode;
        }

        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getEmail() { return email; }
        public String getPassword() { return password; }
        public String getZipCode() { return zipCode; }
    }

    /**
     * Generates a unique, valid synthetic shopper account for registration testing.
     */
    public static UserProfile generateShopper() {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        return new UserProfile(
                "Shopper",
                "Tester" + uniqueId,
                "shopper_" + uniqueId + "@ultademo-ai.com",
                "UltaSecure!2026",
                "60601"
        );
    }

    /**
     * Retrieves test products configured in products.json.
     */
    public static List<String> getSearchQueries() {
        List<String> queries = new ArrayList<>();
        try (InputStream is = TestDataGenerator.class.getClassLoader().getResourceAsStream("testdata/products.json")) {
            if (is != null) {
                JsonNode root = mapper.readTree(is);
                JsonNode products = root.path("testProducts");
                for (JsonNode prod : products) {
                    queries.add(prod.path("searchQuery").asText());
                }
            }
        } catch (Exception e) {
            logger.warn("Failed to read products.json, defaulting to standard queries.", e);
        }
        if (queries.isEmpty()) {
            queries.add("moisturizer");
            queries.add("mascara");
            queries.add("vitamin c serum");
        }
        return queries;
    }

    /**
     * Returns negative & boundary search strings for robust validation.
     */
    public static List<String> getBoundarySearchPayloads() {
        return List.of(
                "",                             // Empty search
                "   ",                          // Whitespace only
                "~!@#$%^&*()_+{}|:\"<>?",        // Special character injection
                "a".repeat(256),                // Boundary length (256 characters)
                "xyznonexistentbeautybrand1234" // Non-existent product
        );
    }
}

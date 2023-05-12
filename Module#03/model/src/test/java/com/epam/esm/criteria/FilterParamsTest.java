package com.epam.esm.criteria;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FilterParamsTest {

    @Test
    @DisplayName("Test value method returns result for valid name")
    void testValueWithValidNames() {
        String name = "tags";
        FilterParams result = FilterParams.value(name);
        assertEquals(FilterParams.TAGS, result);
    }

    @Test
    @DisplayName("Test value method returns null for invalid name")
    void testValueWithInvalidName() {
        String name = "invalid";
        FilterParams result = FilterParams.value(name);
        assertNull(result);
    }

    @Test
    @DisplayName("Test key method returns TAGS for valid name")
    void testKey() {
        assertEquals("tags", FilterParams.TAGS.getKey());
    }

    @Test
    @DisplayName("Test value method returns TAGS for valid name")
    void testValue() {
        assertEquals(0, FilterParams.TAGS.getValue());
    }
}

package com.epam.esm;

import com.epam.esm.criteria.SortField;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestSort {
    @ParameterizedTest
    @EnumSource(SortField.class)
    @DisplayName("Test SortField values")
     void testSortField(SortField sortField) {
        String expectedField;
        switch (sortField) {
            case DATE:
                expectedField = "c.last_update_date";
                break;
            case NAME:
                expectedField = "c.name";
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + sortField);
        }
        assertEquals(expectedField, sortField.getField());
    }
}

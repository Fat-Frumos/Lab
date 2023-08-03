package com.epam.esm.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class CriteriaTest {

    @Test
    @DisplayName("Given name, description, and tagNames, when Criteria is built, then name, description, and tagNames are set correctly")
    void testCriteriaBuilder() {
        Criteria criteria = Criteria.builder()
                .name("Year")
                .description("Season")
                .tagNames(List.of("Summer", "Fall"))
                .build();

        assertEquals("Year", criteria.getName());
        assertEquals("Season", criteria.getDescription());
        assertEquals(List.of("Summer", "Fall"), criteria.getTagNames());
    }
}

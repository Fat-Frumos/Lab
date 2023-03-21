package com.epam.esm;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.epam.esm.StringUtils.isPositiveNumber;
import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {
    @Test
    @DisplayName("Checking for positive number and returning false")
    void testPositiveNumberReturnFalse() {
        assertFalse(isPositiveNumber("-1"));
        assertFalse(isPositiveNumber("qwerty"));
        assertFalse(isPositiveNumber("0"));
        assertFalse(isPositiveNumber("+1"));
    }

    @Test
    @DisplayName("Checking for positive number and returning true")
    void testPositiveNumberReturnTrue() {
        assertTrue(isPositiveNumber("1"));
        assertTrue(isPositiveNumber("12"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3"})
    @DisplayName("Parameterized check for a positive number and return true using @ValueSource")
    void parameterizedTestIsPositiveNumber(String input) {
        assertTrue(isPositiveNumber(input));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Parameterized test for the positive number to null and empty using @NullAndEmptySource")
    void parameterizedTestIsPositiveNumberIsNullAndEmptySource(String input) {
        assertFalse(isPositiveNumber(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1", "qwerty", "1.1"})
    @DisplayName("Parameterized check for a positive number and return false using @ValueSource")
    void parameterizedTestIsPositiveNumberAndReturnFalse(String input) {
        assertFalse(isPositiveNumber(input));
    }

    @ParameterizedTest(name = "Test pair: {0}")
    @CsvSource({"123,true", "-123,false", "abc,false", "0,false"})
    @DisplayName("Parameterized check for a positive number with flag using @CsvSource")
    void parameterizedTestIsPositiveNumber(String input, boolean expected) {
        assertEquals(expected, isPositiveNumber(input));
    }
}

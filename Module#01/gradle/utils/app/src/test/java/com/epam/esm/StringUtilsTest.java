package com.epam.esm;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import static com.epam.esm.StringUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {
    @Test
    @DisplayName("Checking for positive number and returning false")
    public void testPositiveNumberReturnFalse() {
        assertFalse(isPositiveNumber("-1"));
        assertFalse(isPositiveNumber("qwerty"));
        assertFalse(isPositiveNumber("0"));
        assertFalse(isPositiveNumber("+1"));
    }

    @Test
    @DisplayName("Checking for positive number and returning true")
    public void testPositiveNumberReturnTrue() {
        assertTrue(isPositiveNumber("1"));
        assertTrue(isPositiveNumber("12"));
    }

    @Test
    @DisplayName("Checking for positive numbers and returning false")
    public void testPositiveNumbersReturnFalse() {
        assertFalse(isPositiveNumbers("-1", "2", "3"));
        assertFalse(isPositiveNumbers("1", "2", "qwerty"));
        assertFalse(isPositiveNumbers("1", "0", "2"));
    }

    @Test
    @DisplayName("Checking for positive numbers and returning true")
    public void testPositiveNumbersReturnTrue() {
        assertTrue(isPositiveNumbers("123", "312", "321"));
        assertTrue(isPositiveNumbers("1", "2", "3"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3"})
    @DisplayName("Parameterized check for a positive number and return true using @ValueSource")
    public void parameterizedTestIsPositiveNumber(String input) {
        assertTrue(isPositiveNumber(input));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Parameterized test for the positive number to null and empty using @NullAndEmptySource")
    public void parameterizedTestIsPositiveNumberIsNullAndEmptySource(String input) {
        assertFalse(isPositiveNumber(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1", "qwerty", "1.1"})
    @DisplayName("Parameterized check for a positive number and return false using @ValueSource")
    public void parameterizedTestIsPositiveNumberAndReturnFalse(String input) {
        assertFalse(isPositiveNumber(input));
    }

    @ParameterizedTest(name = "Test pair: {0}")
    @CsvSource({"123,true", "-123,false", "abc,false", "0,false"})
    @DisplayName("Parameterized check for a positive number with flag using @CsvSource")
    public void parameterizedTestIsPositiveNumber(String input, boolean expected) {
        assertEquals(expected, isPositiveNumber(input));
    }

    @ParameterizedTest(name = "Test numbers: {0}")
    @CsvSource({"1 2 3", "11 22 33", "111 222 333"})
    @DisplayName("Parameterized check for a positive number in array and split and return true using @CsvSource")
    void parameterizedTestAndReturnTrueForAllPositiveNumbers(String array) {
        assertTrue(isPositiveNumbers(array.split(" ")));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Parameterized check for a positive number in array and return false using @NullSource")
    void parameterizedTestAndReturnFalseForNullSourceArray(String... array) {
        assertFalse(isPositiveNumbers(array));
    }

    @ParameterizedTest
    @EmptySource
    @DisplayName("Parameterized check for a positive number if array is empty and return false using @EmptySource")
    void parameterizedTestAndReturnFalseForEmptyArray(String... array) {
        assertFalse(isPositiveNumbers(array));
    }

    @ParameterizedTest(name = "Test invalid value: {0}")
    @ValueSource(strings = {"+1", "-2", "-", "abc"})
    @DisplayName("Parameterized check for a positive number and return false for non positive number")
    void parameterizedTestAndReturnFalseNonPositiveNumber(String array) {
        assertFalse(isPositiveNumbers(array));
    }

    @ParameterizedTest(name = "Test value: {0}")
    @CsvSource({"1,2,3", "4,5,6", "7,8,9"})
    @DisplayName("Parameterized test for a positive number and return true using @CsvSource")
    void parameterizedTestIsAllPositiveNumbersAndReturnTrueForCsvSource(String a, String b, String c) {
        assertTrue(isPositiveNumbers(a, b, c));
    }

    @ParameterizedTest(name = "Test valid value: {0}")
    @MethodSource("positiveNumbersProvider")
    @DisplayName("Parameterized test for a positive number and return true using @MethodSource")
    void parameterizedTestIsAllPositiveNumbersAndReturnTrueForMethodSource(String a, String b, String c) {
        assertTrue(isPositiveNumbers(a, b, c));
    }

    static Stream<Arguments> positiveNumbersProvider() {
        return Stream.of(
                Arguments.of("1", "2", "3"),
                Arguments.of("4", "5", "6"),
                Arguments.of("7", "8", "9")
        );
    }
}

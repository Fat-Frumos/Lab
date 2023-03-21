package com.epam.esm.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {
    @Test
    @DisplayName("Checking for positive numbers and returning false")
    public void testPositiveNumbersReturnFalse() {
        assertFalse(Utils.isAllPositiveNumbers("-1", "2", "3"));
        assertFalse(Utils.isAllPositiveNumbers("1", "2", "qwerty"));
        assertFalse(Utils.isAllPositiveNumbers("1", "0", "2"));
    }

    @Test
    @DisplayName("Checking for positive numbers and returning true")
    public void testPositiveNumbersReturnTrue() {
        Assertions.assertTrue(Utils.isAllPositiveNumbers("123", "312", "321"));
        Assertions.assertTrue(Utils.isAllPositiveNumbers("1", "2", "3"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3"})
    @DisplayName("Parameterized check for a positive number and return true using @ValueSource")
    public void parameterizedTestIsPositiveNumber(String input) {
        assertTrue(Utils.isAllPositiveNumbers(input));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Parameterized test for the positive number to null and empty using @NullAndEmptySource")
    public void parameterizedTestIsPositiveNumberIsNullAndEmptySource(String input) {
        assertFalse(Utils.isAllPositiveNumbers(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1", "qwerty", "1.1"})
    @DisplayName("Parameterized check for a positive number and return false using @ValueSource")
    public void parameterizedTestIsPositiveNumberAndReturnFalse(String input) {
        assertFalse(Utils.isAllPositiveNumbers(input));
    }

    @ParameterizedTest(name = "Test pair: {0}")
    @CsvSource({"123,true", "-123,false", "abc,false", "0,false"})
    @DisplayName("Parameterized check for a positive number with flag using @CsvSource")
    public void parameterizedTestIsPositiveNumber(String input, boolean expected) {
        assertEquals(expected, Utils.isAllPositiveNumbers(input));
    }

    @ParameterizedTest(name = "Test numbers: {0}")
    @CsvSource({"1 2 3", "11 22 33", "111 222 333"})
    @DisplayName("Parameterized check for a positive number in array and split and return true using @CsvSource")
    void parameterizedTestAndReturnTrueForAllPositiveNumbers(String array) {
        assertTrue(Utils.isAllPositiveNumbers(array.split(" ")));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Parameterized check for a positive number in array and return false using @NullSource")
    void parameterizedTestAndReturnFalseForNullSourceArray(String... array) {
        assertFalse(Utils.isAllPositiveNumbers(array));
    }

    @ParameterizedTest
    @EmptySource
    @DisplayName("Parameterized check for a positive number if array is empty and return false using @EmptySource")
    void parameterizedTestAndReturnFalseForEmptyArray(String... array) {
        assertFalse(Utils.isAllPositiveNumbers(array));
    }

    @ParameterizedTest(name = "Test invalid value: {0}")
    @ValueSource(strings = {"+1", "-2", "-", "abc"})
    @DisplayName("Parameterized check for a positive number and return false for non positive number")
    void parameterizedTestAndReturnFalseNonPositiveNumber(String array) {
        assertFalse(Utils.isAllPositiveNumbers(array));
    }

    @ParameterizedTest(name = "Test value: {0}")
    @CsvSource({"1,2,3", "4,5,6", "7,8,9"})
    @DisplayName("Parameterized test for a positive number and return true using @CsvSource")
    void parameterizedTestIsAllPositiveNumbersAndReturnTrueForCsvSource(String a, String b, String c) {
        assertTrue(Utils.isAllPositiveNumbers(a, b, c));
    }

    @ParameterizedTest(name = "Test valid value: {0}")
    @MethodSource("positiveNumbersProvider")
    @DisplayName("Parameterized test for a positive number and return true using @MethodSource")
    void parameterizedTestIsAllPositiveNumbersAndReturnTrueForMethodSource(String a, String b, String c) {
        assertTrue(Utils.isAllPositiveNumbers(a, b, c));
    }

    static Stream<Arguments> positiveNumbersProvider() {
        return Stream.of(
                Arguments.of("1", "2", "3"),
                Arguments.of("4", "5", "6"),
                Arguments.of("7", "8", "9")
        );
    }
}

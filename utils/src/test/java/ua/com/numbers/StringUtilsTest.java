package ua.com.numbers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ua.com.numbers.StringUtils.isPositiveNumber;

class StringUtilsTest {
    @Test
    public void testPositiveNumberReturnFalse() {
        assertFalse(isPositiveNumber("-1"));
        assertFalse(isPositiveNumber("qwerty"));
        assertFalse(isPositiveNumber("0"));
        assertFalse(isPositiveNumber("+1"));
    }

    @Test
    public void testPositiveNumberReturnTrue() {
        assertTrue(isPositiveNumber("1"));
        assertTrue(isPositiveNumber("12"));
    }
}

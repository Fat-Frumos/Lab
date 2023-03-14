package test.java.ua.com.numbers;

import main.java.ua.com.numbers.Utils;
import org.junit.jupiter.api.Test;
import ua.com.numbers.StringUtils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UtilsTest {
    @Test
    public void testPositiveNumbersReturnFalse() {
        assertFalse(Utils.isAllPositiveNumbers("-1", "2", "3"));
        assertFalse(Utils.isAllPositiveNumbers("1", "2", "qwerty"));
        assertFalse(Utils.isAllPositiveNumbers("1", "0", "2"));
    }
    @Test
    public void testPositiveNumbersReturnTrue() {
        assertTrue(Utils.isAllPositiveNumbers("123", "312", "321"));
        assertTrue(Utils.isAllPositiveNumbers("1", "2", "3"));
    }

    @Test
    public void testPositiveNumberReturnFalse() {
        assertFalse(StringUtils.isPositiveNumber("-1"));
        assertFalse(StringUtils.isPositiveNumber("qwerty"));
        assertFalse(StringUtils.isPositiveNumber("0"));
    }
    @Test
    public void testPositiveNumberReturnTrue() {
        assertTrue(StringUtils.isPositiveNumber("1"));
        assertTrue(StringUtils.isPositiveNumber("10"));
    }

}
package main.java.ua.com.numbers;

import ua.com.numbers.StringUtils;

import java.util.Arrays;

public class Utils {
    public static boolean isAllPositiveNumbers(String... array) {
        return Arrays.stream(array).allMatch(StringUtils::isPositiveNumber);
    }
}
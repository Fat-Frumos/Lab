package com.epam.esm.core;

import ua.com.numbers.StringUtils;

import java.util.Arrays;

public class Utils {
    public static boolean isAllPositiveNumbers(String... array) {
        return array != null && array.length != 0 &&
                Arrays.stream(array).allMatch(StringUtils::isPositiveNumber);
    }
}
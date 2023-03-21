package com.epam.esm;

import java.util.Arrays;

import static org.apache.commons.lang3.StringUtils.isNumeric;

public class StringUtils {
    public static void main(String[] args) {
        System.out.println(isPositiveNumbers(args));
    }

    protected static boolean isPositiveNumbers(String... args) {
        return args != null && args.length > 0 && Arrays.stream(args)
                .allMatch(StringUtils::isPositiveNumber);
    }

    public static boolean isPositiveNumber(String str) {
        return isNumeric(str) && Integer.parseInt(str) > 0;
    }
}
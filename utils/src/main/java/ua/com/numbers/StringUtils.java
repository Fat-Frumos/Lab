package ua.com.numbers;

import static org.apache.commons.lang3.StringUtils.isNumeric;

public class StringUtils {
    public static void main(String[] args) {
        System.out.println(StringUtils.isPositiveNumber(args[0]));
    }

    public static boolean isPositiveNumber(String str) {
        return isNumeric(str) && Integer.parseInt(str) > 0;
    }
}
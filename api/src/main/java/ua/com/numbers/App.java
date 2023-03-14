package ua.com.numbers;

import main.java.ua.com.numbers.Utils;

public class App {
    public static void main(String[] args) {
        boolean result = Utils.isAllPositiveNumbers("12", "79");
        System.out.printf("All positive numbers are %s%n", result);
    }
}
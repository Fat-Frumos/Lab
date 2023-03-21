package com.epam.esm.api;

import com.epam.esm.core.Utils;

public class App {
    public static void main(String[] args) {
        boolean result = Utils.isAllPositiveNumbers("12", "79");
        System.out.printf("All positive numbers are %s%n", result);
    }
}
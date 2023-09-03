package com.balash.banking.service.util;

public class Utils {

    private static final String DOTS_COMMAS_REGEXP = "[.,]";

    private static final Utils INSTANCE = new Utils();

    private Utils(){}

    public static Utils getInstance(){
        return INSTANCE;
    }

    public int convertToCents(String dollarsAndCents) {
        if (dollarsAndCents == null || dollarsAndCents.isEmpty()) {
            throw new IllegalArgumentException("Input string is empty or null");
        }

        String[] parts = dollarsAndCents.split(DOTS_COMMAS_REGEXP);

        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid input format. Expected dollars and cents separated by a dot.");
        }
        try {
            int dollars = Integer.parseInt(parts[0]);
            int cents = Integer.parseInt(parts[1]);
            if (cents < 0 || cents > 99) {
                throw new IllegalArgumentException("Cents must be between 0 and 99.");
            }
            int totalCents = dollars * 100 + cents;
            return totalCents;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format in the input string.");
        }
    }

    public String convertToDollarsAndCents(long cents){
        long dollars = cents / 100;
        long remainingCents = cents % 100;
        String result = dollars + "." + (remainingCents < 10 ? "0" : "") + remainingCents;
        return result;
    }

}

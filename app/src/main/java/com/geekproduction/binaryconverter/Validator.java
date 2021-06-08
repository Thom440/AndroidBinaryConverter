package com.geekproduction.binaryconverter;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Validator {
    public static boolean validBigDecimal(String number) {
        try {
            if (number.contains(".") && number.indexOf(".") != number.length() - 1) {
                BigDecimal bigDecimal = new BigDecimal(number);
                return true;
            }
        }
        catch (NumberFormatException ex) {
            return false;
        }
        return false;
    }

    public static boolean validBigInteger(String number) {
        try {
            BigInteger bigInt = new BigInteger(number);
            return true;
        }
        catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean validShort(String number) {
        try {
            short negativeNumber = Short.parseShort(number);
            if (negativeNumber >= Short.MIN_VALUE && negativeNumber < 0) {
                return true;
            }
            return false;
        }
        catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean validInt(String number) {
        try {
            int negativeNumber = Integer.parseInt(number);
            if (negativeNumber >= Integer.MIN_VALUE && negativeNumber < 0) {
                return true;
            }
            return false;
        }
        catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean validLong(String number) {
        try {
            long negativeNumber = Long.parseLong(number);
            if (negativeNumber >= Long.MIN_VALUE && negativeNumber <0) {
                return true;
            }
            return false;
        }
        catch (NumberFormatException ex) {
            return false;
        }
    }
}
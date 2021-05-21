package com.geekproduction.binaryconverter;

import java.math.BigInteger;

public class Convert {
    public static String decimalToOctal(BigInteger bigInt) {
        BigInteger constantEight = new BigInteger("8");
        BigInteger i = valueOfI(bigInt);

        String octal = "";

        do {
            octal += bigInt.divide(i);
            bigInt = bigInt.mod(i);
            i = i.divide(constantEight);
        }
        while (i.compareTo(BigInteger.ZERO) != 0);
        return octal;
    }

    private static BigInteger valueOfI(BigInteger bigInt) {
        BigInteger i = BigInteger.ONE;
        BigInteger constantEight = new BigInteger("8");
        while (i.multiply(constantEight).compareTo(bigInt) <= 0) {
            i = i.multiply(constantEight);
        }
        return i;
    }

    public static String decimalToBinary(BigInteger bigInt) {
        BigInteger remainder;
        BigInteger constantTwo = new BigInteger("2");
        String binary = "";

        do {
            remainder = bigInt.mod(constantTwo);
            bigInt = bigInt.divide(constantTwo);
            binary = remainder + binary;
        }
        while (bigInt.compareTo(BigInteger.ZERO) > 0);
        return binary;
    }

    public static String decimalToHex(BigInteger bigInt) {
        String[] hexArray = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};

        String hex = "";

        do {
            int temp = bigInt.mod(BigInteger.valueOf(16)).intValue();
            hex = hexArray[temp] + hex;
            bigInt = bigInt.divide(BigInteger.valueOf(16));
        }
        while (bigInt.compareTo(BigInteger.ZERO) > 0);
        return hex;
    }

    public static String binaryToDecimal(BigInteger bigInt) {
        int i = 0;
        BigInteger constantTwo = new BigInteger("2");
        BigInteger finalNumber = BigInteger.ZERO;

        while (bigInt.compareTo(BigInteger.ZERO) > 0) {
            BigInteger decimalNumber = bigInt.mod(BigInteger.valueOf(10)).multiply(constantTwo.pow(i));
            finalNumber = finalNumber.add(decimalNumber);
            bigInt = bigInt.divide(BigInteger.valueOf(10));
            i++;
        }
        return finalNumber.toString();
    }

    public static String findTwosComplement(String decimalText) {
        if (Validator.validBigInteger(decimalText)) {
            BigInteger bigInt = new BigInteger(decimalText);
            BigInteger intMaxTwosComplement = new BigInteger("4294967295");
            BigInteger intMinTwosComplement = new BigInteger("2147483648");
            BigInteger longTwosComplement = new BigInteger("18446744073709551615");
            if (bigInt.compareTo(BigInteger.valueOf(65535)) <= 0 && bigInt.compareTo(BigInteger.valueOf(32768)) >= 0) {
                BigInteger newDecimal = bigInt.add(BigInteger.valueOf(Short.MIN_VALUE))
                        .subtract(BigInteger.valueOf(Short.MAX_VALUE))
                        .subtract(BigInteger.ONE);
                return newDecimal.toString();
            }
            else if (bigInt.compareTo(intMaxTwosComplement) <= 0 && bigInt.compareTo(intMinTwosComplement) >= 0) {
                BigInteger newDecimal = bigInt.add(BigInteger.valueOf(Integer.MIN_VALUE))
                        .subtract(BigInteger.valueOf(Integer.MAX_VALUE))
                        .subtract(BigInteger.ONE);
                return newDecimal.toString();
            }
        }
        return "N/A";
    }
}

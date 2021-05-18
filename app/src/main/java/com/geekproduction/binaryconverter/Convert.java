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
}

package com.geekproduction.binaryconverter;

import java.math.BigInteger;

public class Convert {
    public static String decimalToOctal(BigInteger bigInt) {
        BigInteger constantEight = new BigInteger("8");
        BigInteger newBigInt = bigInt;
        BigInteger i = valueOfI(bigInt);

        String octal = "";

        do {
            octal += newBigInt.divide(i);
            newBigInt = newBigInt.mod(i);
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
        BigInteger remainder = BigInteger.ZERO;
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
}

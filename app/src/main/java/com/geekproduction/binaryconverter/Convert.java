package com.geekproduction.binaryconverter;

import java.math.BigInteger;

public class Convert {

    private static final BigInteger SHORT_MAX_TWOS_COMPLEMENT = new BigInteger("65535");
    private static final BigInteger SHORT_MIN_TWOS_COMPLEMENT = new BigInteger("32768");
    private static final BigInteger INT_MAX_TWOS_COMPLEMENT = new BigInteger("4294967295");
    private static final BigInteger INT_MIN_TWOS_COMPLEMENT = new BigInteger("2147483648");
    private static final BigInteger LONG_MAX_TWOS_COMPLEMENT = new BigInteger("18446744073709551615");
    private static final BigInteger LONG_MIN_TWOS_COMPLEMENT = new BigInteger("9223372036854775808");
    private static final BigInteger CONSTANT_EIGHT = new BigInteger("8");
    private static final BigInteger CONSTANT_TWO = new BigInteger("2");

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

        while (i.multiply(CONSTANT_EIGHT).compareTo(bigInt) <= 0) {
            i = i.multiply(CONSTANT_EIGHT);
        }
        return i;
    }

    public static String decimalToBinary(BigInteger bigInt) {
        BigInteger remainder;

        String binary = "";

        do {
            remainder = bigInt.mod(CONSTANT_TWO);
            bigInt = bigInt.divide(CONSTANT_TWO);
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

            if (bigInt.compareTo(SHORT_MAX_TWOS_COMPLEMENT) <= 0 && bigInt.compareTo(SHORT_MIN_TWOS_COMPLEMENT) >= 0) {
                BigInteger newDecimal = bigInt.add(BigInteger.valueOf(Short.MIN_VALUE))
                        .subtract(BigInteger.valueOf(Short.MAX_VALUE))
                        .subtract(BigInteger.ONE);
                return newDecimal.toString();
            }
            else if (bigInt.compareTo(INT_MAX_TWOS_COMPLEMENT) <= 0 && bigInt.compareTo(INT_MIN_TWOS_COMPLEMENT) >= 0) {
                BigInteger newDecimal = bigInt.add(BigInteger.valueOf(Integer.MIN_VALUE))
                        .subtract(BigInteger.valueOf(Integer.MAX_VALUE))
                        .subtract(BigInteger.ONE);
                return newDecimal.toString();
            }
            else if (bigInt.compareTo(LONG_MAX_TWOS_COMPLEMENT) <= 0 && bigInt.compareTo(LONG_MIN_TWOS_COMPLEMENT) >= 0) {
                BigInteger newDecimal = bigInt.add(BigInteger.valueOf(Long.MIN_VALUE))
                        .subtract(BigInteger.valueOf(Long.MAX_VALUE))
                        .subtract(BigInteger.ONE);
                return newDecimal.toString();
            }
        }
        return "N/A";
    }

    public static String octalToDecimal(BigInteger bigInt) {
        BigInteger number;
        BigInteger decimalNumber = BigInteger.ZERO;

        int i = 0;

        do {
            number = bigInt.mod(BigInteger.valueOf(10)).multiply((BigInteger.valueOf(8)).pow(i));
            bigInt = bigInt.divide(BigInteger.valueOf(10));
            decimalNumber = decimalNumber.add(number);
            i++;
        }
        while (bigInt.compareTo(BigInteger.ZERO) > 0);
        return decimalNumber.toString();
    }
}

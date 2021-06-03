package com.geekproduction.binaryconverter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

public class Convert {

    private static final BigInteger SHORT_MAX_TWOS_COMPLEMENT = new BigInteger("65535");
    private static final BigInteger SHORT_MIN_TWOS_COMPLEMENT = new BigInteger("32768");
    private static final BigInteger INT_MAX_TWOS_COMPLEMENT = new BigInteger("4294967295");
    private static final BigInteger INT_MIN_TWOS_COMPLEMENT = new BigInteger("2147483648");
    private static final BigInteger LONG_MAX_TWOS_COMPLEMENT = new BigInteger("18446744073709551615");
    private static final BigInteger LONG_MIN_TWOS_COMPLEMENT = new BigInteger("9223372036854775808");
    private static final BigInteger CONSTANT_EIGHT = new BigInteger("8");
    private static final BigInteger CONSTANT_TWO = new BigInteger("2");
    private static final BigInteger CONSTANT_TEN = new BigInteger("10");
    private static final BigInteger CONSTANT_SIXTEEN = new BigInteger("16");

    private static final BigDecimal DECIMAL_CONSTANT_EIGHT = new BigDecimal("8");
    private static final BigDecimal DECIMAL_CONSTANT_SIXTEEN = new BigDecimal("16");

    private static final String[] HEX_ARRAY = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};

    public static String decimalToOctal(BigInteger bigInt) {
        BigInteger i = valueOfI(bigInt);

        String octal = "";

        do {
            octal += bigInt.divide(i);
            bigInt = bigInt.mod(i);
            i = i.divide(CONSTANT_EIGHT);
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

        String hex = "";

        do {
            int temp = bigInt.mod(CONSTANT_SIXTEEN).intValue();
            hex = HEX_ARRAY[temp] + hex;
            bigInt = bigInt.divide(CONSTANT_SIXTEEN);
        }
        while (bigInt.compareTo(BigInteger.ZERO) > 0);
        return hex;
    }

    public static String binaryToDecimal(BigInteger bigInt) {
        int i = 0;
        BigInteger finalNumber = BigInteger.ZERO;

        while (bigInt.compareTo(BigInteger.ZERO) > 0) {
            BigInteger decimalNumber = bigInt.mod(CONSTANT_TEN).multiply(CONSTANT_TWO.pow(i));
            finalNumber = finalNumber.add(decimalNumber);
            bigInt = bigInt.divide(CONSTANT_TEN);
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
        BigInteger decimalNumber = BigInteger.ZERO;

        int i = 0;

        do {
            decimalNumber = decimalNumber.add(bigInt.mod(CONSTANT_TEN).multiply((CONSTANT_EIGHT).pow(i)));
            bigInt = bigInt.divide(CONSTANT_TEN);
            i++;
        }
        while (bigInt.compareTo(BigInteger.ZERO) > 0);
        return decimalNumber.toString();
    }

    public static String decimalPointToBinary(BigDecimal bigDecimal) {
        int k = 10;

        String bigDecimalString = bigDecimal.toString();

        int subStringLength = bigDecimalString.substring(bigDecimalString.indexOf(".") + 1).length();
        if (subStringLength > k) {
            k = subStringLength;
        }

        bigDecimalString = bigDecimalString.substring(0, bigDecimalString.indexOf("."));

        BigDecimal fractionalPart = bigDecimal.subtract(new BigDecimal(bigDecimalString));

        BigInteger bigInt = bigDecimal.toBigInteger();

        String binary = decimalToBinary(bigInt);

        binary += ".";

        while (k != 0 && fractionalPart.compareTo(BigDecimal.ZERO) > 0) {
            String integerPart = fractionalPart.multiply(BigDecimal.valueOf(2)).toBigInteger().toString();
            binary += integerPart;
            BigDecimal temp = fractionalPart.multiply(BigDecimal.valueOf(2));
            fractionalPart = temp.subtract(new BigDecimal(integerPart));
            k--;
        }
        return binary;
    }

    public static String decimalPointToOctal(BigDecimal bigDecimal) {
        int k = 10;

        BigInteger bigInt = bigDecimal.toBigInteger();
        String octal = decimalToOctal(bigInt);

        octal += ".";

        String bigDecimalString = bigDecimal.toString();

        int subStringLength = bigDecimalString.substring(bigDecimalString.indexOf(".") + 1).length();
        if (subStringLength > k) {
            k = subStringLength;
        }

        bigDecimalString = bigDecimalString.substring(0, bigDecimalString.indexOf("."));
        BigDecimal fractionPart = bigDecimal.subtract(new BigDecimal(bigDecimalString));

        do {
            BigDecimal product = fractionPart.multiply(DECIMAL_CONSTANT_EIGHT);
            String integerPart = product.toBigInteger().toString();
            octal += integerPart;
            fractionPart = product.subtract(new BigDecimal(integerPart));
            k--;
        }
        while (fractionPart.compareTo(BigDecimal.ZERO) > 0 && k != 0);
        return octal;
    }

    public static String decimalPointToHex(BigDecimal bigDecimal) {
        int k = 10;

        BigInteger bigInt = bigDecimal.toBigInteger();
        String hex = decimalToHex(bigInt);

        String bigDecimalString = bigDecimal.toString();

        int subStringLength = bigDecimalString.substring(bigDecimalString.indexOf(".") + 1).length();
        if (subStringLength > k) {
            k = subStringLength;
        }

        hex += ".";

        bigDecimalString = bigDecimalString.substring(0, bigDecimalString.indexOf("."));
        BigDecimal fractionPart = bigDecimal.subtract(new BigDecimal(bigDecimalString));

        do {
            BigDecimal product = fractionPart.multiply(DECIMAL_CONSTANT_SIXTEEN);
            String integerPart = product.toBigInteger().toString();
            int integer = Integer.parseInt(integerPart);
            hex += HEX_ARRAY[integer];
            fractionPart = product.subtract(new BigDecimal(integerPart));
            k--;
        }
        while (fractionPart.compareTo(BigDecimal.ZERO) > 0 && k != 0);
        return hex;
    }

    public static String hexToDecimal(String hex) {
        BigInteger bigInt = BigInteger.ZERO;
        BigInteger otherBigInt;
        BigInteger temp;

        for (int i = 0; i < hex.length(); i++) {
            bigInt = bigInt.add(BigInteger.valueOf(Arrays.asList(HEX_ARRAY).indexOf(hex.substring(hex.length() - i - 1, hex.length() - i)))
                                                                .multiply(CONSTANT_SIXTEEN.pow(i)));
            //otherBigInt = temp.multiply(CONSTANT_SIXTEEN.pow(i));
            //temp = temp.multiply(CONSTANT_SIXTEEN.pow(i));
            //bigInt = bigInt.add(temp);
        }
        return bigInt.toString();
    }
}

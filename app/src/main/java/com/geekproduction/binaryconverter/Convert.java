package com.geekproduction.binaryconverter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
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
    private static final BigDecimal DECIMAL_CONSTANT_TWO = new BigDecimal("2");

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
        int k = 20;

        bigDecimal = bigDecimal.abs();

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
            String integerPart = fractionalPart.multiply(DECIMAL_CONSTANT_TWO).toBigInteger().toString();
            binary += integerPart;
            BigDecimal temp = fractionalPart.multiply(DECIMAL_CONSTANT_TWO);
            fractionalPart = temp.subtract(new BigDecimal(integerPart));
            k--;
        }
        return binary;
    }

    public static String decimalPointToOctal(BigDecimal bigDecimal) {
        int k = 20;

        bigDecimal = bigDecimal.abs();

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
        int k = 20;

        bigDecimal = bigDecimal.abs();

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

        for (int i = 0; i < hex.length(); i++) {
            bigInt = bigInt.add(BigInteger.valueOf(Arrays.asList(HEX_ARRAY)
                    .indexOf(hex.substring(hex.length() - i - 1, hex.length() - i)))
                    .multiply(CONSTANT_SIXTEEN.pow(i)));
        }
        return bigInt.toString();
    }

    public static String binaryDecimalPointToDecimal(BigDecimal bigDecimal) {
        BigInteger bigInt = bigDecimal.toBigInteger();

        String binaryString = bigDecimal.toString();

        int point = binaryString.indexOf(".");

        String decimal = binaryToDecimal(bigInt);

        decimal = decimal + ".";

        BigDecimal multipleOfTwo = new BigDecimal("2");

        BigDecimal fractional = new BigDecimal("0.0");

        for (int i = point + 1; i < binaryString.length(); i++) {
            long charAtIndex = Long.parseLong(Character.toString(binaryString.charAt(i)));
            if (charAtIndex != 0) {
                fractional = fractional.add(BigDecimal.valueOf(charAtIndex).divide(multipleOfTwo));
            }
            multipleOfTwo = multipleOfTwo.multiply(DECIMAL_CONSTANT_TWO);
        }
        String fractionalString = fractional.toString();
        return decimal + fractionalString.substring(2);
    }

    public static String octalDecimalPointToDecimal(BigDecimal bigDecimal) {
        int k = 10;
        BigInteger bigInt = bigDecimal.toBigInteger();

        String decimal = octalToDecimal(bigInt);

        decimal = decimal + ".";

        String bigDecimalString = bigDecimal.toString();

        bigDecimalString = bigDecimalString.substring(0, bigDecimalString.indexOf('.'));

        BigDecimal fractionalPart = bigDecimal.subtract(new BigDecimal(bigDecimalString));
        String fractionalPartString = fractionalPart.toString();
        fractionalPartString = fractionalPartString.substring(2);
        if (fractionalPartString.length() > k) {
            k = fractionalPartString.length();
        }
        BigDecimal newFraction = new BigDecimal("0.0");

        for (int i = 0; i < fractionalPartString.length(); i++) {
            int negativeIndex = (i + 1) * -1;
            long singleNumber = Long.parseLong(fractionalPartString.substring(i, i + 1));
            if (singleNumber != 0) {
                newFraction = newFraction
                        .add(BigDecimal
                                .valueOf(singleNumber)
                                .multiply(DECIMAL_CONSTANT_EIGHT
                                        .pow(negativeIndex, new MathContext(k))));
            }
        }
        String newFractionString = newFraction.toString();
        return decimal + newFractionString.substring(2);
    }

    public static String hexDecimalPointToDecimal(String hex) {
        int k = 15;
        String decimal = hexToDecimal(hex.substring(0, hex.indexOf(".")));

        decimal = decimal + ".";

        String fractionalPart = hex.substring(hex.indexOf(".") + 1);
        if (fractionalPart.length() > k) {
            k = fractionalPart.length();
        }

        BigDecimal fraction = new BigDecimal("0.0");
        BigDecimal temp;

        for (int i = 0; i < fractionalPart.length(); i++) {
            String index = fractionalPart.substring(i, i + 1);
            int negativeIndex = (i + 1) * -1;
            long singleNumber = Arrays.asList(HEX_ARRAY).indexOf(index);
            if (singleNumber != 0) {
                temp = BigDecimal.valueOf(singleNumber).multiply(DECIMAL_CONSTANT_SIXTEEN.pow(negativeIndex, new MathContext(k)));
                fraction = fraction.add(temp);
            }
        }
        String fractionString = fraction.toString();
        return decimal + fractionString.substring(2);
    }
}
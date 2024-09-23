package dev.crossvas.jadexic2c.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Formatter {

    /**
     * Stolen from IC2Classic - The One Probe Module
     */

    public static final DecimalFormatSymbols US;
    public static final DecimalFormat THERMAL_GEN;
    public static final DecimalFormat SOLAR_TURBINE;
    public static final DecimalFormat NATURAL;
    public static final DecimalFormat DECIMAL;
    public static final DecimalFormat EU_FORMAT;
    public static final DecimalFormat EU_READER_FORMAT;
    public static final DecimalFormat CABLE_LOSS_FORMAT;

    public static String formatNumber(double number, int digits) {
        return formatNumber(number, digits, false);
    }

    public static String formatInt(int number, int digits) {
        return formatInt(number, digits, false);
    }

    public static String formatInt(int number, int digits, boolean fixedLength) {
        return formatNumber((double) number, digits, fixedLength);
    }

    public static String formatNumber(double number, int digits, boolean fixedLength) {
        String suffix = "";
        boolean allow = (number >= 1.0E9 ? String.valueOf((long) number) : String.valueOf(number)).length() > digits;
        double outputNumber = number;

        int actualDigits;
        for (actualDigits = 0; actualDigits < "kmbt".length() && outputNumber >= 1000.0 && allow; ++actualDigits) {
            outputNumber /= 1000.0;
            suffix = Character.toString("kmbt".charAt(actualDigits));
        }

        actualDigits = digits - suffix.length();
        if (outputNumber % 1.0 == 1.0) {
            ++actualDigits;
        }

        int naturalLength = NATURAL.format((long) ((int) outputNumber)).length();
        int decimalLength = DECIMAL.format(outputNumber - (double) ((int) outputNumber)).length();
        StringBuilder patternBuilder = new StringBuilder();

        for (int i = 1; actualDigits > 1 && naturalLength > 1; ++i) {
            patternBuilder.insert(0, "#");
            --actualDigits;
            --naturalLength;
            if (i % 2 == 0 && actualDigits > 1 && naturalLength > 1) {
                if (actualDigits == 2 || naturalLength == 2) {
                    break;
                }

                patternBuilder.insert(0, ",");
                --actualDigits;
                --naturalLength;
            }
        }

        patternBuilder.append("0");
        if (actualDigits > 1 && decimalLength > 0) {
            patternBuilder.append(".");
            --actualDigits;

            while (actualDigits > 0 && decimalLength > 0) {
                patternBuilder.append("#");
                --actualDigits;
                --decimalLength;
            }
        }

        String pattern = patternBuilder.toString();
        String output = (new DecimalFormat(pattern + suffix, US)).format(outputNumber);
        String fill = "";
        int length = output.length();
        if (fixedLength && output.length() < digits) {
            for (int i = 0; i < digits - length; ++i) {
                fill = fill.concat(" ");
            }

            output = fill + output;
        }

        return output;
    }


    static {
        US = new DecimalFormatSymbols(Locale.US);
        THERMAL_GEN = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.US));
        SOLAR_TURBINE = new DecimalFormat("#00.00", new DecimalFormatSymbols(Locale.US));
        NATURAL = new DecimalFormat("###,##0", new DecimalFormatSymbols(Locale.US));
        DECIMAL = new DecimalFormat(".#########", new DecimalFormatSymbols(Locale.US));
        EU_FORMAT = new DecimalFormat("###,###", new DecimalFormatSymbols(Locale.US));
        EU_READER_FORMAT = new DecimalFormat("###,###.##", new DecimalFormatSymbols(Locale.US));
        CABLE_LOSS_FORMAT = new DecimalFormat("0.####", new DecimalFormatSymbols(Locale.US));
    }
}
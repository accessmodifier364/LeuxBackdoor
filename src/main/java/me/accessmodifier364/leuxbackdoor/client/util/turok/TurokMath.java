/*
 * Decompiled with CFR 0.151.
 */
package me.accessmodifier364.leuxbackdoor.client.util.turok;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TurokMath {
    public static int clamp(int value, int minimum, int maximum) {
        return value <= minimum ? minimum : (value >= maximum ? maximum : value);
    }

    public static double clamp(double value, double minimum, double maximum) {
        return value <= minimum ? minimum : (value >= maximum ? maximum : value);
    }

    public static float clamp(float value, float minimum, float maximum) {
        return value <= minimum ? minimum : (value >= maximum ? maximum : value);
    }

    public static double round(double vDouble) {
        BigDecimal decimal = new BigDecimal(vDouble);
        decimal = decimal.setScale(2, RoundingMode.HALF_UP);
        return decimal.doubleValue();
    }

    public static float lerp(int a, int b, float t) {
        return (float) a + (float) (b - a) * t;
    }

    public static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    public static double lerp(double a, double b, float ticks) {
        return (a + (b - a) * ticks);
    }

    public static int normalize(int... value) {
        int normalizedValue = 0;
        int cachedValue = 0;
        int[] nArray = value;
        int n = nArray.length;
        for (int i = 0; i < n; ++i) {
            int values;
            cachedValue = values = nArray[i];
            normalizedValue = values / cachedValue * cachedValue;
        }
        return normalizedValue;
    }

    public static double normalize(double... value) {
        double normalizedValue = 0.0;
        double cachedValue = 0.0;
        double[] dArray = value;
        int n = dArray.length;
        for (int i = 0; i < n; ++i) {
            double values;
            cachedValue = values = dArray[i];
            normalizedValue = values / cachedValue * cachedValue;
        }
        return normalizedValue;
    }

    public static float normalize(float... value) {
        float normalizedValue = 0.0f;
        float cachedValue = 0.0f;
        float[] fArray = value;
        int n = fArray.length;
        for (int i = 0; i < n; ++i) {
            float values;
            cachedValue = values = fArray[i];
            normalizedValue = values / cachedValue * cachedValue;
        }
        return normalizedValue;
    }

    public static int ceiling(double value) {
        int valueInt = (int) value;
        return value >= (double) valueInt ? valueInt + 1 : valueInt;
    }

    public static int ceiling(float value) {
        int valueInt = (int) value;
        return value >= (float) valueInt ? valueInt + 1 : valueInt;
    }

    public static double sqrt(double a) {
        return Math.sqrt(a);
    }

    public static float sqrt(float a) {
        return (float) Math.sqrt(a);
    }

    public static int sqrt(int a) {
        return (int) Math.sqrt(a);
    }
}


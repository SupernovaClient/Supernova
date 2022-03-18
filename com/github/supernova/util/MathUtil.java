package com.github.supernova.util;

public class MathUtil {
    public static int linearInterpolate(int a, int b, double percent) {
        int gap;
        if(a > b) {
            gap = a - b;
            return (int) (b + (gap * percent));
        } else {
            gap = b - a;
            return (int) (a + (gap * percent));
        }
    }
    public static int bpsToMillis(double bps) {
        return (int)((1d / bps)*1000);
    }
    public static double millisToBPS(int millis) {
        return 1000d / millis;
    }
}

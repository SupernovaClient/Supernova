package me.royalty.supernova.util;

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
}

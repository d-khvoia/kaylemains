package com.cybertournaments.kaylemains.util;

public class NumberHelper {
    public static boolean isPowerOfTwo(int number) {
        return (Math.log(number) / Math.log(2)) % 1 == 0;
    }

    public static int getNearestPowerOfTwo(int number) {
        int result  = 1;

        while (result <= number)
            result *= 2;

        return result / 2;
    }
}

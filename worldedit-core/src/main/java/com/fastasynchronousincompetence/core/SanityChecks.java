package com.fastasynchronousincompetence.core;

public class SanityChecks {
    private static final double UPPER_BOUND = 30_000_000;
    private static final double LOWER_BOUND = -UPPER_BOUND;

    public static double[] getSane(double x, double y, double z) {
        final double[] arr = new double[3];

        arr[0] = clamp(x);
        arr[1] = clamp(y);
        arr[2] = clamp(z);

        return arr;
    }

    private static double clamp(double number) {
        return Maths.clamp(number, UPPER_BOUND, LOWER_BOUND);
    }
}

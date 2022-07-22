package com.fastasynchronousincompetence.core;

public class Maths {
    public static double clamp(double number, double max, double min) {
        return Math.max(min, Math.min(max, number));
    }
}

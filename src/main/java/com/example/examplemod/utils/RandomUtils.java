package com.example.examplemod.utils;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {
    public static int nextInt(int min, int max) {
        if (max <= min) return max;
        else return ThreadLocalRandom.current().nextInt(min, max);
    }
    public static double nextDouble(double min, double max) {
        if (max <= min) return max;
        else return ThreadLocalRandom.current().nextDouble(min, max);
    }
}
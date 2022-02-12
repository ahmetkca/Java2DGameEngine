package com.ahmetkca.utils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class MyRandomGenerator {

    public static int getRandomNumberInRange(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("max must be greater than min. min = " + min + " max = " + max);
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static Set<Integer> getUniqueRandomNumberSet(int len, int min, int max) {
        if (min > max)
            throw new IllegalArgumentException("max must be greater than min. min = "+ min+" max = " + max);
        Set<Integer> uniqueSet = new HashSet<>();
        while (uniqueSet.size() != len) {
            uniqueSet.add(ThreadLocalRandom.current().nextInt(min, max));
        }
        return uniqueSet;
    }
}

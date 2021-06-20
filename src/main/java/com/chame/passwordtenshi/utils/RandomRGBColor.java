package com.chame.passwordtenshi.utils;

import java.util.Random;

public class RandomRGBColor {
    private static Random random = new Random();

    public static int getColor() {
        int rgb = random.nextInt(255);
        rgb = (rgb << 8) + random.nextInt(255);
        rgb = (rgb << 8) + random.nextInt(255);
        return rgb;
    }
}

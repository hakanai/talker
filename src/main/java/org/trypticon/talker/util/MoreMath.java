package org.trypticon.talker.util;

public class MoreMath {
    private MoreMath() {}

    public static double acosh(double x) {
        return Math.log(x + Math.sqrt(x * x - 1.0));
    }
}

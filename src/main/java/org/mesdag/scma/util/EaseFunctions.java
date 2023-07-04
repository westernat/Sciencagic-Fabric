package org.mesdag.scma.util;

public class EaseFunctions {
    public static double sigmoid(double x) {
        // [-5, 5]
        return 1 / (1 + Math.exp(-x));
    }

    public static double doubleSigmoid(double x) {
        // [-10, 10]
        if (x < 0) return sigmoid(x + 5);
        else return 1 - sigmoid(x - 5);
    }
}

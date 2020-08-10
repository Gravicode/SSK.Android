package org.apache.commons.math3.random;

import java.util.Random;

public class JDKRandomGenerator extends Random implements RandomGenerator {
    private static final long serialVersionUID = -7745277476784028798L;

    public JDKRandomGenerator() {
    }

    public JDKRandomGenerator(int seed) {
        setSeed(seed);
    }

    public void setSeed(int seed) {
        setSeed((long) seed);
    }

    public void setSeed(int[] seed) {
        setSeed(RandomGeneratorFactory.convertToLong(seed));
    }
}

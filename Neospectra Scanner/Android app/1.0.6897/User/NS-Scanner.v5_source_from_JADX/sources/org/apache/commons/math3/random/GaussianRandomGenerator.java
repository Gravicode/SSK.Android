package org.apache.commons.math3.random;

public class GaussianRandomGenerator implements NormalizedRandomGenerator {
    private final RandomGenerator generator;

    public GaussianRandomGenerator(RandomGenerator generator2) {
        this.generator = generator2;
    }

    public double nextNormalizedDouble() {
        return this.generator.nextGaussian();
    }
}

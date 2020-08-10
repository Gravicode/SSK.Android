package org.apache.commons.math3.random;

import org.apache.commons.math3.util.FastMath;

public class UniformRandomGenerator implements NormalizedRandomGenerator {
    private static final double SQRT3 = FastMath.sqrt(3.0d);
    private final RandomGenerator generator;

    public UniformRandomGenerator(RandomGenerator generator2) {
        this.generator = generator2;
    }

    public double nextNormalizedDouble() {
        return SQRT3 * ((this.generator.nextDouble() * 2.0d) - 1.0d);
    }
}

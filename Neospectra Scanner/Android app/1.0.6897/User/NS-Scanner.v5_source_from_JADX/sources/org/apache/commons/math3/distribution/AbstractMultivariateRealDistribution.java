package org.apache.commons.math3.distribution;

import java.lang.reflect.Array;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;

public abstract class AbstractMultivariateRealDistribution implements MultivariateRealDistribution {
    private final int dimension;
    protected final RandomGenerator random;

    public abstract double[] sample();

    protected AbstractMultivariateRealDistribution(RandomGenerator rng, int n) {
        this.random = rng;
        this.dimension = n;
    }

    public void reseedRandomGenerator(long seed) {
        this.random.setSeed(seed);
    }

    public int getDimension() {
        return this.dimension;
    }

    public double[][] sample(int sampleSize) {
        if (sampleSize <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES, Integer.valueOf(sampleSize));
        }
        double[][] out = (double[][]) Array.newInstance(double.class, new int[]{sampleSize, this.dimension});
        for (int i = 0; i < sampleSize; i++) {
            out[i] = sample();
        }
        return out;
    }
}

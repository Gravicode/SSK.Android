package org.apache.commons.math3.distribution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotANumberException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;

public class EnumeratedIntegerDistribution extends AbstractIntegerDistribution {
    private static final long serialVersionUID = 20130308;
    protected final EnumeratedDistribution<Integer> innerDistribution;

    public EnumeratedIntegerDistribution(int[] singletons, double[] probabilities) throws DimensionMismatchException, NotPositiveException, MathArithmeticException, NotFiniteNumberException, NotANumberException {
        this(new Well19937c(), singletons, probabilities);
    }

    public EnumeratedIntegerDistribution(RandomGenerator rng, int[] singletons, double[] probabilities) throws DimensionMismatchException, NotPositiveException, MathArithmeticException, NotFiniteNumberException, NotANumberException {
        super(rng);
        this.innerDistribution = new EnumeratedDistribution<>(rng, createDistribution(singletons, probabilities));
    }

    public EnumeratedIntegerDistribution(RandomGenerator rng, int[] data) {
        int[] arr$;
        super(rng);
        Map<Integer, Integer> dataMap = new HashMap<>();
        for (int value : data) {
            Integer count = (Integer) dataMap.get(Integer.valueOf(value));
            if (count == null) {
                count = Integer.valueOf(0);
            }
            Integer valueOf = Integer.valueOf(value);
            Integer valueOf2 = Integer.valueOf(count.intValue() + 1);
            Integer count2 = valueOf2;
            dataMap.put(valueOf, valueOf2);
        }
        int massPoints = dataMap.size();
        double denom = (double) data.length;
        int[] values = new int[massPoints];
        double[] probabilities = new double[massPoints];
        int index = 0;
        for (Entry<Integer, Integer> entry : dataMap.entrySet()) {
            values[index] = ((Integer) entry.getKey()).intValue();
            probabilities[index] = ((double) ((Integer) entry.getValue()).intValue()) / denom;
            index++;
        }
        this.innerDistribution = new EnumeratedDistribution<>(rng, createDistribution(values, probabilities));
    }

    public EnumeratedIntegerDistribution(int[] data) {
        this((RandomGenerator) new Well19937c(), data);
    }

    private static List<Pair<Integer, Double>> createDistribution(int[] singletons, double[] probabilities) {
        if (singletons.length != probabilities.length) {
            throw new DimensionMismatchException(probabilities.length, singletons.length);
        }
        List<Pair<Integer, Double>> samples = new ArrayList<>(singletons.length);
        for (int i = 0; i < singletons.length; i++) {
            samples.add(new Pair(Integer.valueOf(singletons[i]), Double.valueOf(probabilities[i])));
        }
        return samples;
    }

    public double probability(int x) {
        return this.innerDistribution.probability(Integer.valueOf(x));
    }

    public double cumulativeProbability(int x) {
        double probability = 0.0d;
        for (Pair<Integer, Double> sample : this.innerDistribution.getPmf()) {
            if (((Integer) sample.getKey()).intValue() <= x) {
                probability += ((Double) sample.getValue()).doubleValue();
            }
        }
        return probability;
    }

    public double getNumericalMean() {
        double mean = 0.0d;
        for (Pair<Integer, Double> sample : this.innerDistribution.getPmf()) {
            mean += ((Double) sample.getValue()).doubleValue() * ((double) ((Integer) sample.getKey()).intValue());
        }
        return mean;
    }

    public double getNumericalVariance() {
        double mean = 0.0d;
        double meanOfSquares = 0.0d;
        for (Pair<Integer, Double> sample : this.innerDistribution.getPmf()) {
            mean += ((Double) sample.getValue()).doubleValue() * ((double) ((Integer) sample.getKey()).intValue());
            meanOfSquares += ((Double) sample.getValue()).doubleValue() * ((double) ((Integer) sample.getKey()).intValue()) * ((double) ((Integer) sample.getKey()).intValue());
        }
        return meanOfSquares - (mean * mean);
    }

    public int getSupportLowerBound() {
        int min = Integer.MAX_VALUE;
        for (Pair<Integer, Double> sample : this.innerDistribution.getPmf()) {
            if (((Integer) sample.getKey()).intValue() < min && ((Double) sample.getValue()).doubleValue() > 0.0d) {
                min = ((Integer) sample.getKey()).intValue();
            }
        }
        return min;
    }

    public int getSupportUpperBound() {
        int max = Integer.MIN_VALUE;
        for (Pair<Integer, Double> sample : this.innerDistribution.getPmf()) {
            if (((Integer) sample.getKey()).intValue() > max && ((Double) sample.getValue()).doubleValue() > 0.0d) {
                max = ((Integer) sample.getKey()).intValue();
            }
        }
        return max;
    }

    public boolean isSupportConnected() {
        return true;
    }

    public int sample() {
        return ((Integer) this.innerDistribution.sample()).intValue();
    }
}

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
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;

public class EnumeratedRealDistribution extends AbstractRealDistribution {
    private static final long serialVersionUID = 20130308;
    protected final EnumeratedDistribution<Double> innerDistribution;

    public EnumeratedRealDistribution(double[] singletons, double[] probabilities) throws DimensionMismatchException, NotPositiveException, MathArithmeticException, NotFiniteNumberException, NotANumberException {
        this(new Well19937c(), singletons, probabilities);
    }

    public EnumeratedRealDistribution(RandomGenerator rng, double[] singletons, double[] probabilities) throws DimensionMismatchException, NotPositiveException, MathArithmeticException, NotFiniteNumberException, NotANumberException {
        super(rng);
        this.innerDistribution = new EnumeratedDistribution<>(rng, createDistribution(singletons, probabilities));
    }

    public EnumeratedRealDistribution(RandomGenerator rng, double[] data) {
        double[] arr$;
        super(rng);
        Map<Double, Integer> dataMap = new HashMap<>();
        for (double value : data) {
            Integer count = (Integer) dataMap.get(Double.valueOf(value));
            if (count == null) {
                count = Integer.valueOf(0);
            }
            Double valueOf = Double.valueOf(value);
            Integer valueOf2 = Integer.valueOf(count.intValue() + 1);
            Integer count2 = valueOf2;
            dataMap.put(valueOf, valueOf2);
        }
        int massPoints = dataMap.size();
        double denom = (double) data.length;
        double[] values = new double[massPoints];
        double[] probabilities = new double[massPoints];
        int index = 0;
        for (Entry<Double, Integer> entry : dataMap.entrySet()) {
            values[index] = ((Double) entry.getKey()).doubleValue();
            probabilities[index] = ((double) ((Integer) entry.getValue()).intValue()) / denom;
            index++;
        }
        this.innerDistribution = new EnumeratedDistribution<>(rng, createDistribution(values, probabilities));
    }

    public EnumeratedRealDistribution(double[] data) {
        this((RandomGenerator) new Well19937c(), data);
    }

    private static List<Pair<Double, Double>> createDistribution(double[] singletons, double[] probabilities) {
        if (singletons.length != probabilities.length) {
            throw new DimensionMismatchException(probabilities.length, singletons.length);
        }
        List<Pair<Double, Double>> samples = new ArrayList<>(singletons.length);
        for (int i = 0; i < singletons.length; i++) {
            samples.add(new Pair(Double.valueOf(singletons[i]), Double.valueOf(probabilities[i])));
        }
        return samples;
    }

    public double probability(double x) {
        return this.innerDistribution.probability(Double.valueOf(x));
    }

    public double density(double x) {
        return probability(x);
    }

    public double cumulativeProbability(double x) {
        double probability = 0.0d;
        for (Pair<Double, Double> sample : this.innerDistribution.getPmf()) {
            if (((Double) sample.getKey()).doubleValue() <= x) {
                probability += ((Double) sample.getValue()).doubleValue();
            }
        }
        return probability;
    }

    public double inverseCumulativeProbability(double p) throws OutOfRangeException {
        if (p < 0.0d || p > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(p), Integer.valueOf(0), Integer.valueOf(1));
        }
        double probability = 0.0d;
        double x = getSupportLowerBound();
        for (Pair<Double, Double> sample : this.innerDistribution.getPmf()) {
            if (((Double) sample.getValue()).doubleValue() != 0.0d) {
                probability += ((Double) sample.getValue()).doubleValue();
                x = ((Double) sample.getKey()).doubleValue();
                if (probability >= p) {
                    break;
                }
            }
        }
        return x;
    }

    public double getNumericalMean() {
        double mean = 0.0d;
        for (Pair<Double, Double> sample : this.innerDistribution.getPmf()) {
            mean += ((Double) sample.getValue()).doubleValue() * ((Double) sample.getKey()).doubleValue();
        }
        return mean;
    }

    public double getNumericalVariance() {
        double mean = 0.0d;
        double meanOfSquares = 0.0d;
        for (Pair<Double, Double> sample : this.innerDistribution.getPmf()) {
            mean += ((Double) sample.getValue()).doubleValue() * ((Double) sample.getKey()).doubleValue();
            meanOfSquares += ((Double) sample.getValue()).doubleValue() * ((Double) sample.getKey()).doubleValue() * ((Double) sample.getKey()).doubleValue();
        }
        return meanOfSquares - (mean * mean);
    }

    public double getSupportLowerBound() {
        double min = Double.POSITIVE_INFINITY;
        for (Pair<Double, Double> sample : this.innerDistribution.getPmf()) {
            if (((Double) sample.getKey()).doubleValue() < min && ((Double) sample.getValue()).doubleValue() > 0.0d) {
                min = ((Double) sample.getKey()).doubleValue();
            }
        }
        return min;
    }

    public double getSupportUpperBound() {
        double max = Double.NEGATIVE_INFINITY;
        for (Pair<Double, Double> sample : this.innerDistribution.getPmf()) {
            if (((Double) sample.getKey()).doubleValue() > max && ((Double) sample.getValue()).doubleValue() > 0.0d) {
                max = ((Double) sample.getKey()).doubleValue();
            }
        }
        return max;
    }

    public boolean isSupportLowerBoundInclusive() {
        return true;
    }

    public boolean isSupportUpperBoundInclusive() {
        return true;
    }

    public boolean isSupportConnected() {
        return true;
    }

    public double sample() {
        return ((Double) this.innerDistribution.sample()).doubleValue();
    }
}

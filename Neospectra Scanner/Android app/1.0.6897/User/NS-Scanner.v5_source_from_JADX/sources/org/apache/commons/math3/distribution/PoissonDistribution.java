package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.apache.commons.math3.util.FastMath;

public class PoissonDistribution extends AbstractIntegerDistribution {
    public static final double DEFAULT_EPSILON = 1.0E-12d;
    public static final int DEFAULT_MAX_ITERATIONS = 10000000;
    private static final long serialVersionUID = -3349935121172596109L;
    private final double epsilon;
    private final ExponentialDistribution exponential;
    private final int maxIterations;
    private final double mean;
    private final NormalDistribution normal;

    public PoissonDistribution(double p) throws NotStrictlyPositiveException {
        this(p, 1.0E-12d, DEFAULT_MAX_ITERATIONS);
    }

    public PoissonDistribution(double p, double epsilon2, int maxIterations2) throws NotStrictlyPositiveException {
        this(new Well19937c(), p, epsilon2, maxIterations2);
    }

    public PoissonDistribution(RandomGenerator rng, double p, double epsilon2, int maxIterations2) throws NotStrictlyPositiveException {
        double d = p;
        super(rng);
        if (d <= 0.0d) {
            throw new NotStrictlyPositiveException(LocalizedFormats.MEAN, Double.valueOf(p));
        }
        this.mean = d;
        this.epsilon = epsilon2;
        this.maxIterations = maxIterations2;
        NormalDistribution normalDistribution = new NormalDistribution(rng, d, FastMath.sqrt(p), 1.0E-9d);
        this.normal = normalDistribution;
        ExponentialDistribution exponentialDistribution = new ExponentialDistribution(rng, 1.0d, 1.0E-9d);
        this.exponential = exponentialDistribution;
    }

    public PoissonDistribution(double p, double epsilon2) throws NotStrictlyPositiveException {
        this(p, epsilon2, DEFAULT_MAX_ITERATIONS);
    }

    public PoissonDistribution(double p, int maxIterations2) {
        this(p, 1.0E-12d, maxIterations2);
    }

    public double getMean() {
        return this.mean;
    }

    public double probability(int x) {
        double logProbability = logProbability(x);
        if (logProbability == Double.NEGATIVE_INFINITY) {
            return 0.0d;
        }
        return FastMath.exp(logProbability);
    }

    public double logProbability(int x) {
        if (x < 0 || x == Integer.MAX_VALUE) {
            return Double.NEGATIVE_INFINITY;
        }
        if (x == 0) {
            return -this.mean;
        }
        return (((-SaddlePointExpansion.getStirlingError((double) x)) - SaddlePointExpansion.getDeviancePart((double) x, this.mean)) - (FastMath.log(6.283185307179586d) * 0.5d)) - (FastMath.log((double) x) * 0.5d);
    }

    public double cumulativeProbability(int x) {
        if (x < 0) {
            return 0.0d;
        }
        if (x == Integer.MAX_VALUE) {
            return 1.0d;
        }
        return Gamma.regularizedGammaQ(((double) x) + 1.0d, this.mean, this.epsilon, this.maxIterations);
    }

    public double normalApproximateProbability(int x) {
        return this.normal.cumulativeProbability(((double) x) + 0.5d);
    }

    public double getNumericalMean() {
        return getMean();
    }

    public double getNumericalVariance() {
        return getMean();
    }

    public int getSupportLowerBound() {
        return 0;
    }

    public int getSupportUpperBound() {
        return Integer.MAX_VALUE;
    }

    public boolean isSupportConnected() {
        return true;
    }

    public int sample() {
        return (int) FastMath.min(nextPoisson(this.mean), 2147483647L);
    }

    private long nextPoisson(double meanPoisson) {
        double y;
        double logLambdaFactorial;
        double aSum;
        double a1;
        double v;
        double y2;
        double x;
        double x2;
        int a;
        PoissonDistribution poissonDistribution = this;
        double d = meanPoisson;
        double rnd = 1.0d;
        if (d < 40.0d) {
            double p = FastMath.exp(-d);
            long n = 0;
            double r = 1.0d;
            while (true) {
                double d2 = rnd;
                if (((double) n) >= 1000.0d * d) {
                    return n;
                }
                rnd = poissonDistribution.random.nextDouble();
                r *= rnd;
                if (r < p) {
                    return n;
                }
                n++;
            }
        } else {
            double lambda = FastMath.floor(meanPoisson);
            double lambdaFractional = d - lambda;
            double logLambda = FastMath.log(lambda);
            double logLambdaFactorial2 = CombinatoricsUtils.factorialLog((int) lambda);
            long y22 = lambdaFractional < Double.MIN_VALUE ? 0 : poissonDistribution.nextPoisson(lambdaFractional);
            double delta = FastMath.sqrt(FastMath.log(((32.0d * lambda) / 3.141592653589793d) + 1.0d) * lambda);
            double halfDelta = delta / 2.0d;
            double twolpd = (lambda * 2.0d) + delta;
            double a12 = FastMath.sqrt(twolpd * 3.141592653589793d) * FastMath.exp(1.0d / (lambda * 8.0d));
            double d3 = lambdaFractional;
            double a2 = (twolpd / delta) * FastMath.exp(((-delta) * (delta + 1.0d)) / twolpd);
            double aSum2 = a12 + a2 + 1.0d;
            double p1 = a12 / aSum2;
            double p2 = a2 / aSum2;
            double c1 = 1.0d / (8.0d * lambda);
            int a3 = 0;
            double qr = 0.0d;
            while (true) {
                double a22 = a2;
                double u = poissonDistribution.random.nextDouble();
                if (u <= p1) {
                    a1 = a12;
                    double n2 = poissonDistribution.random.nextGaussian();
                    aSum = aSum2;
                    double x3 = (FastMath.sqrt(lambda + halfDelta) * n2) - 0.5d;
                    if (x3 <= delta) {
                        logLambdaFactorial = logLambdaFactorial2;
                        if (x3 < (-lambda)) {
                            a = a3;
                            x2 = x3;
                        } else {
                            int i = a3;
                            v = ((-poissonDistribution.exponential.sample()) - ((n2 * n2) / 2.0d)) + c1;
                            x = x3;
                            y2 = x3 < 0.0d ? FastMath.floor(x3) : FastMath.ceil(x3);
                        }
                    } else {
                        a = a3;
                        x2 = x3;
                        logLambdaFactorial = logLambdaFactorial2;
                    }
                    a2 = a22;
                    a12 = a1;
                    aSum2 = aSum;
                    logLambdaFactorial2 = logLambdaFactorial;
                    a3 = a;
                    double d4 = x2;
                } else {
                    a1 = a12;
                    int a4 = a3;
                    aSum = aSum2;
                    logLambdaFactorial = logLambdaFactorial2;
                    if (u > p1 + p2) {
                        double d5 = delta;
                        y = lambda;
                        double y3 = qr;
                        int i2 = a4;
                        break;
                    }
                    double x4 = ((twolpd / delta) * poissonDistribution.exponential.sample()) + delta;
                    x = x4;
                    y2 = FastMath.ceil(x4);
                    v = (-poissonDistribution.exponential.sample()) - (((x4 + 1.0d) * delta) / twolpd);
                }
                a3 = x < 0.0d ? 1 : 0;
                double t = ((y2 + 1.0d) * y2) / (lambda * 2.0d);
                if (v < (-t) && a3 == 0) {
                    double d6 = delta;
                    y = lambda + y2;
                    break;
                }
                double qr2 = t * ((((y2 * 2.0d) + 1.0d) / (6.0d * lambda)) - 1.0d);
                double delta2 = delta;
                if (v < qr2 - ((t * t) / (((((double) a3) * (y2 + 1.0d)) + lambda) * 3.0d))) {
                    y = lambda + y2;
                    break;
                } else if (v <= qr2 && v < ((y2 * logLambda) - CombinatoricsUtils.factorialLog((int) (y2 + lambda))) + logLambdaFactorial) {
                    y = lambda + y2;
                    break;
                } else {
                    qr = t;
                    a2 = a22;
                    a12 = a1;
                    aSum2 = aSum;
                    logLambdaFactorial2 = logLambdaFactorial;
                    delta = delta2;
                    poissonDistribution = this;
                }
            }
            return y22 + ((long) y);
        }
    }
}

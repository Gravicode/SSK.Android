package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public abstract class ContinuedFraction {
    private static final double DEFAULT_EPSILON = 1.0E-8d;

    /* access modifiers changed from: protected */
    public abstract double getA(int i, double d);

    /* access modifiers changed from: protected */
    public abstract double getB(int i, double d);

    protected ContinuedFraction() {
    }

    public double evaluate(double x) throws ConvergenceException {
        return evaluate(x, 1.0E-8d, Integer.MAX_VALUE);
    }

    public double evaluate(double x, double epsilon) throws ConvergenceException {
        return evaluate(x, epsilon, Integer.MAX_VALUE);
    }

    public double evaluate(double x, int maxIterations) throws ConvergenceException, MaxCountExceededException {
        return evaluate(x, 1.0E-8d, maxIterations);
    }

    public double evaluate(double x, double epsilon, int maxIterations) throws ConvergenceException, MaxCountExceededException {
        double hN;
        ContinuedFraction continuedFraction = this;
        double d = x;
        int i = maxIterations;
        double hPrev = continuedFraction.getA(0, d);
        if (Precision.equals(hPrev, 0.0d, 1.0E-50d)) {
            hPrev = 1.0E-50d;
        }
        int n = 1;
        double dPrev = 0.0d;
        double cPrev = hPrev;
        double hPrev2 = hPrev;
        while (true) {
            hN = hPrev;
            if (n >= i) {
                double d2 = hPrev2;
                break;
            }
            double a = continuedFraction.getA(n, d);
            double b = continuedFraction.getB(n, d);
            double dN = a + (b * dPrev);
            if (Precision.equals(dN, 0.0d, 1.0E-50d)) {
                dN = 1.0E-50d;
            }
            double cN = a + (b / cPrev);
            if (Precision.equals(cN, 0.0d, 1.0E-50d)) {
                cN = 1.0E-50d;
            }
            double dN2 = 1.0d / dN;
            double deltaN = cN * dN2;
            hN = hPrev2 * deltaN;
            if (Double.isInfinite(hN)) {
                throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, Double.valueOf(x));
            } else if (Double.isNaN(hN)) {
                throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_NAN_DIVERGENCE, Double.valueOf(x));
            } else {
                double d3 = hPrev2;
                if (FastMath.abs(deltaN - 1.0d) < epsilon) {
                    break;
                }
                dPrev = dN2;
                cPrev = cN;
                hPrev2 = hN;
                n++;
                hPrev = hN;
                continuedFraction = this;
            }
        }
        if (n < i) {
            return hN;
        }
        throw new MaxCountExceededException(LocalizedFormats.NON_CONVERGENT_CONTINUED_FRACTION, Integer.valueOf(maxIterations), Double.valueOf(x));
    }
}

package org.apache.commons.math3.analysis.integration;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.gauss.GaussIntegratorFactory;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;

public class IterativeLegendreGaussIntegrator extends BaseAbstractUnivariateIntegrator {
    private static final GaussIntegratorFactory FACTORY = new GaussIntegratorFactory();
    private final int numberOfPoints;

    public IterativeLegendreGaussIntegrator(int n, double relativeAccuracy, double absoluteAccuracy, int minimalIterationCount, int maximalIterationCount) throws NotStrictlyPositiveException, NumberIsTooSmallException {
        super(relativeAccuracy, absoluteAccuracy, minimalIterationCount, maximalIterationCount);
        if (n <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_POINTS, Integer.valueOf(n));
        }
        this.numberOfPoints = n;
    }

    public IterativeLegendreGaussIntegrator(int n, double relativeAccuracy, double absoluteAccuracy) throws NotStrictlyPositiveException {
        this(n, relativeAccuracy, absoluteAccuracy, 3, Integer.MAX_VALUE);
    }

    public IterativeLegendreGaussIntegrator(int n, int minimalIterationCount, int maximalIterationCount) throws NotStrictlyPositiveException, NumberIsTooSmallException {
        this(n, 1.0E-6d, 1.0E-15d, minimalIterationCount, maximalIterationCount);
    }

    /* access modifiers changed from: protected */
    public double doIntegrate() throws MathIllegalArgumentException, TooManyEvaluationsException, MaxCountExceededException {
        int i = 1;
        double oldt = stage(1);
        int n = 2;
        while (true) {
            double t = stage(n);
            double delta = FastMath.abs(t - oldt);
            double limit = FastMath.max(getAbsoluteAccuracy(), getRelativeAccuracy() * (FastMath.abs(oldt) + FastMath.abs(t)) * 0.5d);
            if (getIterations() + i >= getMinimalIterationCount() && delta <= limit) {
                return t;
            }
            double d = oldt;
            n = FastMath.max((int) (((double) n) * FastMath.min(4.0d, FastMath.pow(delta / limit, 0.5d / ((double) this.numberOfPoints)))), n + 1);
            double oldt2 = t;
            incrementCount();
            oldt = oldt2;
            i = 1;
        }
    }

    private double stage(int n) throws TooManyEvaluationsException {
        int i = n;
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) throws MathIllegalArgumentException, TooManyEvaluationsException {
                return IterativeLegendreGaussIntegrator.this.computeObjectiveValue(x);
            }
        };
        double min = getMin();
        double step = (getMax() - min) / ((double) i);
        double sum = 0.0d;
        for (int i2 = 0; i2 < i; i2++) {
            double a = (((double) i2) * step) + min;
            sum += FACTORY.legendreHighPrecision(this.numberOfPoints, a, a + step).integrate(f);
        }
        return sum;
    }
}

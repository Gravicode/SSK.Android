package org.apache.commons.math3.analysis.integration;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.util.FastMath;

public class TrapezoidIntegrator extends BaseAbstractUnivariateIntegrator {
    public static final int TRAPEZOID_MAX_ITERATIONS_COUNT = 64;

    /* renamed from: s */
    private double f515s;

    public TrapezoidIntegrator(double relativeAccuracy, double absoluteAccuracy, int minimalIterationCount, int maximalIterationCount) throws NotStrictlyPositiveException, NumberIsTooSmallException, NumberIsTooLargeException {
        super(relativeAccuracy, absoluteAccuracy, minimalIterationCount, maximalIterationCount);
        if (maximalIterationCount > 64) {
            throw new NumberIsTooLargeException(Integer.valueOf(maximalIterationCount), Integer.valueOf(64), false);
        }
    }

    public TrapezoidIntegrator(int minimalIterationCount, int maximalIterationCount) throws NotStrictlyPositiveException, NumberIsTooSmallException, NumberIsTooLargeException {
        super(minimalIterationCount, maximalIterationCount);
        if (maximalIterationCount > 64) {
            throw new NumberIsTooLargeException(Integer.valueOf(maximalIterationCount), Integer.valueOf(64), false);
        }
    }

    public TrapezoidIntegrator() {
        super(3, 64);
    }

    /* access modifiers changed from: 0000 */
    public double stage(BaseAbstractUnivariateIntegrator baseIntegrator, int n) throws TooManyEvaluationsException {
        BaseAbstractUnivariateIntegrator baseAbstractUnivariateIntegrator = baseIntegrator;
        if (n == 0) {
            double max = baseIntegrator.getMax();
            double min = baseIntegrator.getMin();
            this.f515s = (max - min) * 0.5d * (baseAbstractUnivariateIntegrator.computeObjectiveValue(min) + baseAbstractUnivariateIntegrator.computeObjectiveValue(max));
            return this.f515s;
        }
        long np = 1 << (n - 1);
        double sum = 0.0d;
        double max2 = baseIntegrator.getMax();
        double min2 = baseIntegrator.getMin();
        double spacing = (max2 - min2) / ((double) np);
        double x = (spacing * 0.5d) + min2;
        for (long i = 0; i < np; i++) {
            sum += baseAbstractUnivariateIntegrator.computeObjectiveValue(x);
            x += spacing;
        }
        this.f515s = (this.f515s + (sum * spacing)) * 0.5d;
        return this.f515s;
    }

    /* access modifiers changed from: protected */
    public double doIntegrate() throws MathIllegalArgumentException, TooManyEvaluationsException, MaxCountExceededException {
        double t;
        double oldt = stage(this, 0);
        incrementCount();
        while (true) {
            int i = getIterations();
            t = stage(this, i);
            if (i >= getMinimalIterationCount()) {
                double delta = FastMath.abs(t - oldt);
                if (delta <= getRelativeAccuracy() * (FastMath.abs(oldt) + FastMath.abs(t)) * 0.5d || delta <= getAbsoluteAccuracy()) {
                    return t;
                }
            }
            oldt = t;
            incrementCount();
        }
        return t;
    }
}

package org.apache.commons.math3.analysis.integration;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.util.FastMath;

public class MidPointIntegrator extends BaseAbstractUnivariateIntegrator {
    public static final int MIDPOINT_MAX_ITERATIONS_COUNT = 64;

    public MidPointIntegrator(double relativeAccuracy, double absoluteAccuracy, int minimalIterationCount, int maximalIterationCount) throws NotStrictlyPositiveException, NumberIsTooSmallException, NumberIsTooLargeException {
        super(relativeAccuracy, absoluteAccuracy, minimalIterationCount, maximalIterationCount);
        if (maximalIterationCount > 64) {
            throw new NumberIsTooLargeException(Integer.valueOf(maximalIterationCount), Integer.valueOf(64), false);
        }
    }

    public MidPointIntegrator(int minimalIterationCount, int maximalIterationCount) throws NotStrictlyPositiveException, NumberIsTooSmallException, NumberIsTooLargeException {
        super(minimalIterationCount, maximalIterationCount);
        if (maximalIterationCount > 64) {
            throw new NumberIsTooLargeException(Integer.valueOf(maximalIterationCount), Integer.valueOf(64), false);
        }
    }

    public MidPointIntegrator() {
        super(3, 64);
    }

    private double stage(int n, double previousStageResult, double min, double diffMaxMin) throws TooManyEvaluationsException {
        long np = 1 << (n - 1);
        double sum = 0.0d;
        double spacing = diffMaxMin / ((double) np);
        double x = min + (spacing * 0.5d);
        for (long i = 0; i < np; i++) {
            sum += computeObjectiveValue(x);
            x += spacing;
        }
        return (previousStageResult + (sum * spacing)) * 0.5d;
    }

    /* access modifiers changed from: protected */
    public double doIntegrate() throws MathIllegalArgumentException, TooManyEvaluationsException, MaxCountExceededException {
        long j;
        double min = getMin();
        double diff = getMax() - min;
        MidPointIntegrator midPointIntegrator = this;
        double t = midPointIntegrator.computeObjectiveValue(min + (diff * 0.5d)) * diff;
        while (true) {
            double oldt = t;
            incrementCount();
            int i = getIterations();
            double d = oldt;
            double oldt2 = oldt;
            double oldt3 = min;
            double min2 = min;
            int i2 = i;
            t = midPointIntegrator.stage(i, d, oldt3, diff);
            if (i2 >= getMinimalIterationCount()) {
                double delta = FastMath.abs(t - oldt2);
                j = 4602678819172646912L;
                if (delta <= getRelativeAccuracy() * (FastMath.abs(oldt2) + FastMath.abs(t)) * 0.5d || delta <= getAbsoluteAccuracy()) {
                    return t;
                }
            } else {
                j = 4602678819172646912L;
            }
            long j2 = j;
            min = min2;
            midPointIntegrator = this;
        }
        return t;
    }
}

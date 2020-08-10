package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.util.FastMath;

public class RiddersSolver extends AbstractUnivariateSolver {
    private static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-6d;

    public RiddersSolver() {
        this(1.0E-6d);
    }

    public RiddersSolver(double absoluteAccuracy) {
        super(absoluteAccuracy);
    }

    public RiddersSolver(double relativeAccuracy, double absoluteAccuracy) {
        super(relativeAccuracy, absoluteAccuracy);
    }

    /* access modifiers changed from: protected */
    public double doSolve() throws TooManyEvaluationsException, NoBracketingException {
        double x2;
        double y2;
        double x22;
        double min = getMin();
        double max = getMax();
        double x1 = min;
        double y1 = computeObjectiveValue(x1);
        double x23 = max;
        double y22 = computeObjectiveValue(x23);
        if (y1 == 0.0d) {
            return min;
        }
        if (y22 == 0.0d) {
            return max;
        }
        verifyBracketing(min, max);
        double y23 = getAbsoluteAccuracy();
        double functionValueAccuracy = getFunctionValueAccuracy();
        double relativeAccuracy = getRelativeAccuracy();
        double oldx = Double.POSITIVE_INFINITY;
        while (true) {
            double min2 = min;
            double min3 = (x1 + x23) * 0.5d;
            double max2 = max;
            double max3 = computeObjectiveValue(min3);
            if (FastMath.abs(max3) <= functionValueAccuracy) {
                return min3;
            }
            double x24 = x23;
            double x25 = 1.0d - ((y1 * y22) / (max3 * max3));
            double correction = ((FastMath.signum(y22) * FastMath.signum(max3)) * (min3 - x1)) / FastMath.sqrt(x25);
            double x12 = x1;
            double x13 = min3 - correction;
            double d = x25;
            double y = computeObjectiveValue(x13);
            double y24 = y22;
            double absoluteAccuracy = y23;
            if (FastMath.abs(x13 - oldx) <= FastMath.max(relativeAccuracy * FastMath.abs(x13), y23) || FastMath.abs(y) <= functionValueAccuracy) {
                return x13;
            }
            if (correction <= 0.0d) {
                x22 = y24;
                if (FastMath.signum(x22) + FastMath.signum(y) == 0.0d) {
                    y2 = x13;
                    y1 = y;
                    x2 = x24;
                } else {
                    y2 = min3;
                    x2 = x13;
                    y1 = max3;
                    x22 = y;
                }
            } else if (FastMath.signum(y1) + FastMath.signum(y) == 0.0d) {
                x2 = x13;
                x22 = y;
                y2 = x12;
            } else {
                y1 = y;
                x22 = max3;
                x2 = min3;
                y2 = x13;
            }
            oldx = x13;
            y22 = x22;
            x1 = y2;
            min = min2;
            max = max2;
            x23 = x2;
            y23 = absoluteAccuracy;
        }
    }
}

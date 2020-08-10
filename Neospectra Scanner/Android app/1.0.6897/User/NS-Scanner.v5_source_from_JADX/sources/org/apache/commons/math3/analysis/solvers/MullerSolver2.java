package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.util.FastMath;

public class MullerSolver2 extends AbstractUnivariateSolver {
    private static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-6d;

    public MullerSolver2() {
        this(1.0E-6d);
    }

    public MullerSolver2(double absoluteAccuracy) {
        super(absoluteAccuracy);
    }

    public MullerSolver2(double relativeAccuracy, double absoluteAccuracy) {
        super(relativeAccuracy, absoluteAccuracy);
    }

    /* access modifiers changed from: protected */
    public double doSolve() throws TooManyEvaluationsException, NumberIsTooLargeException, NoBracketingException {
        double functionValueAccuracy;
        double y1;
        double dplus;
        double x;
        double min = getMin();
        double max = getMax();
        verifyInterval(min, max);
        double relativeAccuracy = getRelativeAccuracy();
        double y12 = getAbsoluteAccuracy();
        double functionValueAccuracy2 = getFunctionValueAccuracy();
        double x0 = min;
        double y0 = computeObjectiveValue(x0);
        if (FastMath.abs(y0) < functionValueAccuracy2) {
            return x0;
        }
        double min2 = min;
        double relativeAccuracy2 = relativeAccuracy;
        double x1 = max;
        double y13 = computeObjectiveValue(x1);
        if (FastMath.abs(y13) < functionValueAccuracy2) {
            return x1;
        }
        if (y0 * y13 > 0.0d) {
            double d = x0;
            NoBracketingException noBracketingException = new NoBracketingException(x0, x1, y0, y13);
            throw noBracketingException;
        }
        double y02 = y0;
        double x02 = x0;
        double x2 = (x02 + x1) * 0.5d;
        double y2 = computeObjectiveValue(x2);
        double oldx = Double.POSITIVE_INFINITY;
        while (true) {
            double q = (x2 - x1) / (x1 - x02);
            double b = ((((q * 2.0d) + 1.0d) * y2) - (((q + 1.0d) * (q + 1.0d)) * y13)) + (q * q * y02);
            double c = (q + 1.0d) * y2;
            double y22 = y2;
            double delta = (b * b) - ((4.0d * (((y2 - ((q + 1.0d) * y13)) + (q * y02)) * q)) * c);
            if (delta >= 0.0d) {
                y1 = y13;
                double dplus2 = b + FastMath.sqrt(delta);
                functionValueAccuracy = functionValueAccuracy2;
                double functionValueAccuracy3 = b - FastMath.sqrt(delta);
                dplus = FastMath.abs(dplus2) > FastMath.abs(functionValueAccuracy3) ? dplus2 : functionValueAccuracy3;
            } else {
                y1 = y13;
                functionValueAccuracy = functionValueAccuracy2;
                dplus = FastMath.sqrt((b * b) - delta);
            }
            if (dplus != 0.0d) {
                x = x2 - (((2.0d * c) * (x2 - x1)) / dplus);
                while (true) {
                    if (x != x1 && x != x2) {
                        break;
                    }
                    x += y12;
                }
            } else {
                x = min2 + (FastMath.random() * (max - min2));
                oldx = Double.POSITIVE_INFINITY;
            }
            double d2 = dplus;
            double y = computeObjectiveValue(x);
            double max2 = max;
            double absoluteAccuracy = y12;
            if (FastMath.abs(x - oldx) <= FastMath.max(relativeAccuracy2 * FastMath.abs(x), y12) || FastMath.abs(y) <= functionValueAccuracy) {
                return x;
            }
            x02 = x1;
            y02 = y1;
            x1 = x2;
            x2 = x;
            double y23 = y;
            oldx = x;
            y13 = y22;
            y2 = y23;
            functionValueAccuracy2 = functionValueAccuracy;
            max = max2;
            y12 = absoluteAccuracy;
        }
        return x;
    }
}

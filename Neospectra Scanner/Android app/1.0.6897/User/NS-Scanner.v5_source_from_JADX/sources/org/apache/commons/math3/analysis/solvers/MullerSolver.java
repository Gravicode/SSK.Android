package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.util.FastMath;

public class MullerSolver extends AbstractUnivariateSolver {
    private static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-6d;

    public MullerSolver() {
        this(1.0E-6d);
    }

    public MullerSolver(double absoluteAccuracy) {
        super(absoluteAccuracy);
    }

    public MullerSolver(double relativeAccuracy, double absoluteAccuracy) {
        super(relativeAccuracy, absoluteAccuracy);
    }

    /* access modifiers changed from: protected */
    public double doSolve() throws TooManyEvaluationsException, NumberIsTooLargeException, NoBracketingException {
        double min = getMin();
        double max = getMax();
        double initial = getStartValue();
        double functionValueAccuracy = getFunctionValueAccuracy();
        verifySequence(min, initial, max);
        double fMin = computeObjectiveValue(min);
        if (FastMath.abs(fMin) < functionValueAccuracy) {
            return min;
        }
        double fMax = computeObjectiveValue(max);
        if (FastMath.abs(fMax) < functionValueAccuracy) {
            return max;
        }
        double fInitial = computeObjectiveValue(initial);
        if (FastMath.abs(fInitial) < functionValueAccuracy) {
            return initial;
        }
        verifyBracketing(min, max);
        if (isBracketing(min, initial)) {
            double d = fMax;
            double d2 = fMin;
            return solve(min, initial, fMin, fInitial);
        }
        double d3 = fMin;
        return solve(initial, max, fInitial, fMax);
    }

    private double solve(double min, double max, double fMin, double fMax) throws TooManyEvaluationsException {
        double x;
        double relativeAccuracy = getRelativeAccuracy();
        double absoluteAccuracy = getAbsoluteAccuracy();
        double functionValueAccuracy = getFunctionValueAccuracy();
        double x0 = min;
        double y0 = fMin;
        double x2 = max;
        double y2 = fMax;
        double x02 = x0;
        double x1 = (x0 + x2) * 0.5d;
        double y1 = computeObjectiveValue(x1);
        double oldx = Double.POSITIVE_INFINITY;
        double x22 = x2;
        double y22 = y2;
        double x23 = x02;
        double x12 = x1;
        double y02 = y0;
        while (true) {
            double d01 = (y1 - y02) / (x12 - x23);
            double d012 = (((y22 - y1) / (x22 - x12)) - d01) / (x22 - x23);
            double c1 = d01 + ((x12 - x23) * d012);
            double delta = (c1 * c1) - ((4.0d * y1) * d012);
            double xplus = x12 + ((y1 * -2.0d) / (c1 + FastMath.sqrt(delta)));
            double d = delta;
            x = isSequence(x23, xplus, x22) ? xplus : x12 + ((-2.0d * y1) / (c1 - FastMath.sqrt(delta)));
            double y = computeObjectiveValue(x);
            double relativeAccuracy2 = relativeAccuracy;
            if (FastMath.abs(x - oldx) <= FastMath.max(FastMath.abs(x) * relativeAccuracy, absoluteAccuracy)) {
                double d2 = y;
                break;
            } else if (FastMath.abs(y) <= functionValueAccuracy) {
                double d3 = y;
                break;
            } else {
                if (!((x < x12 && x12 - x23 > (x22 - x23) * 0.95d) || (x > x12 && x22 - x12 > (x22 - x23) * 0.95d) || x == x12)) {
                    double x03 = x < x12 ? x23 : x12;
                    y02 = x < x12 ? y02 : y1;
                    double x24 = x > x12 ? x22 : x12;
                    y22 = x > x12 ? y22 : y1;
                    x12 = x;
                    y1 = y;
                    oldx = x;
                    x22 = x24;
                    x23 = x03;
                } else {
                    double xm = (x23 + x22) * 0.5d;
                    double d4 = y;
                    double y3 = computeObjectiveValue(xm);
                    if (FastMath.signum(y02) + FastMath.signum(y3) == 0.0d) {
                        x22 = xm;
                        y22 = y3;
                    } else {
                        x23 = xm;
                        y02 = y3;
                    }
                    double d5 = y3;
                    double x13 = (x23 + x22) * 0.5d;
                    y1 = computeObjectiveValue(x13);
                    oldx = Double.POSITIVE_INFINITY;
                    x12 = x13;
                }
                relativeAccuracy = relativeAccuracy2;
            }
        }
        return x;
    }
}

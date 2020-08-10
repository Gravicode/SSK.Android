package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import java.lang.reflect.Array;
import org.apache.commons.math3.exception.MathUnsupportedOperationException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.LineSearch;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

public class PowellOptimizer extends MultivariateOptimizer {
    private static final double MIN_RELATIVE_TOLERANCE = (FastMath.ulp(1.0d) * 2.0d);
    private final double absoluteThreshold;
    private final LineSearch line;
    private final double relativeThreshold;

    public PowellOptimizer(double rel, double abs, ConvergenceChecker<PointValuePair> checker) {
        this(rel, abs, FastMath.sqrt(rel), FastMath.sqrt(abs), checker);
    }

    public PowellOptimizer(double rel, double abs, double lineRel, double lineAbs, ConvergenceChecker<PointValuePair> checker) {
        double d = rel;
        double d2 = abs;
        super(checker);
        if (d < MIN_RELATIVE_TOLERANCE) {
            throw new NumberIsTooSmallException(Double.valueOf(rel), Double.valueOf(MIN_RELATIVE_TOLERANCE), true);
        } else if (d2 <= 0.0d) {
            throw new NotStrictlyPositiveException(Double.valueOf(abs));
        } else {
            this.relativeThreshold = d;
            this.absoluteThreshold = d2;
            LineSearch lineSearch = new LineSearch(this, lineRel, lineAbs, 1.0d);
            this.line = lineSearch;
        }
    }

    public PowellOptimizer(double rel, double abs) {
        this(rel, abs, null);
    }

    public PowellOptimizer(double rel, double abs, double lineRel, double lineAbs) {
        this(rel, abs, lineRel, lineAbs, null);
    }

    /* access modifiers changed from: protected */
    public PointValuePair doOptimize() {
        double fX;
        PointValuePair previous;
        PointValuePair current;
        GoalType goal;
        checkParameters();
        GoalType goal2 = getGoalType();
        double[] guess = getStartPoint();
        int n = guess.length;
        double[][] direc = (double[][]) Array.newInstance(double.class, new int[]{n, n});
        for (int i = 0; i < n; i++) {
            direc[i][i] = 1.0d;
        }
        ConvergenceChecker<PointValuePair> checker = getConvergenceChecker();
        double[] x = guess;
        double fVal = computeObjectiveValue(x);
        double[] x1 = (double[]) x.clone();
        while (true) {
            incrementIterationCount();
            fX = fVal;
            double fX2 = 0.0d;
            int bigInd = 0;
            double delta = 0.0d;
            double[] x2 = x;
            int i2 = 0;
            while (i2 < n) {
                double[] d = MathArrays.copyOf(direc[i2]);
                fX2 = fVal;
                double[] guess2 = guess;
                UnivariatePointValuePair optimum = this.line.search(x2, d);
                fVal = optimum.getValue();
                int n2 = n;
                double[][] direc2 = direc;
                double alphaMin = optimum.getPoint();
                x2 = newPointAndDirection(x2, d, alphaMin)[0];
                if (fX2 - fVal > delta) {
                    delta = fX2 - fVal;
                    bigInd = i2;
                }
                i2++;
                double d2 = alphaMin;
                guess = guess2;
                n = n2;
                direc = direc2;
            }
            double[] guess3 = guess;
            int n3 = n;
            double[][] direc3 = direc;
            double d3 = fX2;
            boolean stop = (fX - fVal) * 2.0d <= (this.relativeThreshold * (FastMath.abs(fX) + FastMath.abs(fVal))) + this.absoluteThreshold;
            previous = new PointValuePair(x1, fX);
            current = new PointValuePair(x2, fVal);
            if (!stop && checker != null) {
                stop = checker.converged(getIterations(), previous, current);
            }
            if (stop) {
                break;
            }
            int n4 = n3;
            double[] d4 = new double[n4];
            double[] x22 = new double[n4];
            int i3 = 0;
            while (true) {
                int i4 = i3;
                if (i4 >= n4) {
                    break;
                }
                d4[i4] = x2[i4] - x1[i4];
                x22[i4] = (x2[i4] * 2.0d) - x1[i4];
                i3 = i4 + 1;
            }
            x1 = (double[]) x2.clone();
            double fX22 = computeObjectiveValue(x22);
            if (fX > fX22) {
                double temp = (fX - fVal) - delta;
                double temp2 = fX - fX22;
                if (((((fX + fX22) - (fVal * 2.0d)) * 2.0d) * (temp * temp)) - ((delta * temp2) * temp2) < 0.0d) {
                    UnivariatePointValuePair optimum2 = this.line.search(x2, d4);
                    fVal = optimum2.getValue();
                    goal = goal2;
                    boolean z = stop;
                    double[][] result = newPointAndDirection(x2, d4, optimum2.getPoint());
                    x2 = result[0];
                    int lastInd = n4 - 1;
                    direc3[bigInd] = direc3[lastInd];
                    direc3[lastInd] = result[1];
                    n = n4;
                    x = x2;
                    guess = guess3;
                    direc = direc3;
                    goal2 = goal;
                }
            }
            goal = goal2;
            n = n4;
            x = x2;
            guess = guess3;
            direc = direc3;
            goal2 = goal;
        }
        if (goal2 == GoalType.MINIMIZE) {
            return fVal < fX ? current : previous;
        }
        return fVal > fX ? current : previous;
    }

    private double[][] newPointAndDirection(double[] p, double[] d, double optimum) {
        int n = p.length;
        double[] nP = new double[n];
        double[] nD = new double[n];
        for (int i = 0; i < n; i++) {
            nD[i] = d[i] * optimum;
            nP[i] = p[i] + nD[i];
        }
        return new double[][]{nP, nD};
    }

    private void checkParameters() {
        if (getLowerBound() != null || getUpperBound() != null) {
            throw new MathUnsupportedOperationException(LocalizedFormats.CONSTRAINT, new Object[0]);
        }
    }
}

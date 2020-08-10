package org.apache.commons.math3.optimization.direct;

import java.lang.reflect.Array;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

@Deprecated
public class PowellOptimizer extends BaseAbstractMultivariateOptimizer<MultivariateFunction> implements MultivariateOptimizer {
    private static final double MIN_RELATIVE_TOLERANCE = (FastMath.ulp(1.0d) * 2.0d);
    private final double absoluteThreshold;
    private final LineSearch line;
    private final double relativeThreshold;

    private class LineSearch extends BrentOptimizer {
        private static final double ABS_TOL_UNUSED = Double.MIN_VALUE;
        private static final double REL_TOL_UNUSED = 1.0E-15d;
        private final BracketFinder bracket = new BracketFinder();

        LineSearch(double rel, double abs) {
            super(1.0E-15d, ABS_TOL_UNUSED, new SimpleUnivariateValueChecker(rel, abs));
        }

        public UnivariatePointValuePair search(double[] p, double[] d) {
            final double[] dArr = p;
            final int n = dArr.length;
            final double[] dArr2 = d;
            C10241 r14 = new UnivariateFunction() {
                public double value(double alpha) {
                    double[] x = new double[n];
                    for (int i = 0; i < n; i++) {
                        x[i] = dArr[i] + (dArr2[i] * alpha);
                    }
                    return PowellOptimizer.this.computeObjectiveValue(x);
                }
            };
            GoalType goal = PowellOptimizer.this.getGoalType();
            this.bracket.search(r14, goal, 0.0d, 1.0d);
            return optimize(Integer.MAX_VALUE, r14, goal, this.bracket.getLo(), this.bracket.getHi(), this.bracket.getMid());
        }
    }

    public PowellOptimizer(double rel, double abs, ConvergenceChecker<PointValuePair> checker) {
        this(rel, abs, FastMath.sqrt(rel), FastMath.sqrt(abs), checker);
    }

    public PowellOptimizer(double rel, double abs, double lineRel, double lineAbs, ConvergenceChecker<PointValuePair> checker) {
        double d = rel;
        double d2 = abs;
        super(checker);
        if (d < MIN_RELATIVE_TOLERANCE) {
            throw new NumberIsTooSmallException(Double.valueOf(d), Double.valueOf(MIN_RELATIVE_TOLERANCE), true);
        } else if (d2 <= 0.0d) {
            throw new NotStrictlyPositiveException(Double.valueOf(abs));
        } else {
            this.relativeThreshold = d;
            this.absoluteThreshold = d2;
            LineSearch lineSearch = new LineSearch(lineRel, lineAbs);
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
        double fVal;
        PointValuePair previous;
        PointValuePair current;
        GoalType goal = getGoalType();
        double[] guess = getStartPoint();
        int n = guess.length;
        double[][] direc = (double[][]) Array.newInstance(double.class, new int[]{n, n});
        for (int i = 0; i < n; i++) {
            direc[i][i] = 1.0d;
        }
        ConvergenceChecker<PointValuePair> checker = getConvergenceChecker();
        double[] x = guess;
        double fVal2 = computeObjectiveValue(x);
        double[] x1 = (double[]) x.clone();
        double[] x2 = x;
        int iter = 0;
        while (true) {
            int iter2 = iter + 1;
            fX = fVal2;
            double delta = 0.0d;
            int bigInd = 0;
            double[] x3 = x2;
            fVal = fVal2;
            int i2 = 0;
            while (i2 < n) {
                double[] d = MathArrays.copyOf(direc[i2]);
                double fX2 = fVal;
                double[] guess2 = guess;
                UnivariatePointValuePair optimum = this.line.search(x3, d);
                fVal = optimum.getValue();
                int n2 = n;
                double[][] direc2 = direc;
                double alphaMin = optimum.getPoint();
                x3 = newPointAndDirection(x3, d, alphaMin)[0];
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
            int iter3 = iter2;
            boolean stop = (fX - fVal) * 2.0d <= (this.relativeThreshold * (FastMath.abs(fX) + FastMath.abs(fVal))) + this.absoluteThreshold;
            previous = new PointValuePair(x1, fX);
            current = new PointValuePair(x3, fVal);
            if (stop || checker == null) {
                iter = iter3;
            } else {
                iter = iter3;
                stop = checker.converged(iter, previous, current);
            }
            if (stop) {
                break;
            }
            int n4 = n3;
            double[] d3 = new double[n4];
            GoalType goal2 = goal;
            double[] x22 = new double[n4];
            int i3 = 0;
            while (true) {
                boolean stop2 = stop;
                int i4 = i3;
                if (i4 >= n4) {
                    break;
                }
                d3[i4] = x3[i4] - x1[i4];
                x22[i4] = (x3[i4] * 2.0d) - x1[i4];
                i3 = i4 + 1;
                stop = stop2;
            }
            x1 = (double[]) x3.clone();
            double fX22 = computeObjectiveValue(x22);
            if (fX > fX22) {
                double temp = (fX - fVal) - delta;
                double temp2 = fX - fX22;
                if (((((fX + fX22) - (fVal * 2.0d)) * 2.0d) * (temp * temp)) - ((delta * temp2) * temp2) < 0.0d) {
                    UnivariatePointValuePair optimum2 = this.line.search(x3, d3);
                    fVal = optimum2.getValue();
                    PointValuePair pointValuePair = previous;
                    PointValuePair pointValuePair2 = current;
                    double[][] result = newPointAndDirection(x3, d3, optimum2.getPoint());
                    x3 = result[0];
                    int lastInd = n4 - 1;
                    direc3[bigInd] = direc3[lastInd];
                    direc3[lastInd] = result[1];
                    fVal2 = fVal;
                    x2 = x3;
                    n = n4;
                    guess = guess3;
                    direc = direc3;
                    goal = goal2;
                }
            }
            fVal2 = fVal;
            x2 = x3;
            n = n4;
            guess = guess3;
            direc = direc3;
            goal = goal2;
        }
        if (goal == GoalType.MINIMIZE) {
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
}

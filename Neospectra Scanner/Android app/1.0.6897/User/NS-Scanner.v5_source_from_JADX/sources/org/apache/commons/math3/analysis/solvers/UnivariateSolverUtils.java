package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;

public class UnivariateSolverUtils {
    private UnivariateSolverUtils() {
    }

    public static double solve(UnivariateFunction function, double x0, double x1) throws NullArgumentException, NoBracketingException {
        if (function != null) {
            return new BrentSolver().solve(Integer.MAX_VALUE, function, x0, x1);
        }
        throw new NullArgumentException(LocalizedFormats.FUNCTION, new Object[0]);
    }

    public static double solve(UnivariateFunction function, double x0, double x1, double absoluteAccuracy) throws NullArgumentException, NoBracketingException {
        if (function != null) {
            return new BrentSolver(absoluteAccuracy).solve(Integer.MAX_VALUE, function, x0, x1);
        }
        throw new NullArgumentException(LocalizedFormats.FUNCTION, new Object[0]);
    }

    public static double forceSide(int maxEval, UnivariateFunction f, BracketedUnivariateSolver<UnivariateFunction> bracketing, double baseRoot, double min, double max, AllowedSolution allowedSolution) throws NoBracketingException {
        int remainingEval;
        double d;
        UnivariateFunction univariateFunction = f;
        double d2 = min;
        double d3 = max;
        if (allowedSolution == AllowedSolution.ANY_SIDE) {
            return baseRoot;
        }
        double step = FastMath.max(bracketing.getAbsoluteAccuracy(), FastMath.abs(bracketing.getRelativeAccuracy() * baseRoot));
        double xLo = FastMath.max(d2, baseRoot - step);
        double fLo = univariateFunction.value(xLo);
        double xHi = FastMath.min(d3, baseRoot + step);
        int remainingEval2 = maxEval - 2;
        double xLo2 = xLo;
        double fLo2 = fLo;
        double xHi2 = xHi;
        double fHi = univariateFunction.value(xHi);
        while (true) {
            remainingEval = remainingEval2;
            if (remainingEval <= 0) {
                double d4 = d3;
                NoBracketingException noBracketingException = new NoBracketingException(LocalizedFormats.FAILED_BRACKETING, xLo2, xHi2, fLo2, fHi, Integer.valueOf(maxEval - remainingEval), Integer.valueOf(maxEval), Double.valueOf(baseRoot), Double.valueOf(min), Double.valueOf(max));
                throw noBracketingException;
            } else if ((fLo2 < 0.0d || fHi > 0.0d) && (fLo2 > 0.0d || fHi < 0.0d)) {
                boolean changeLo = false;
                boolean changeHi = false;
                if (fLo2 < fHi) {
                    if (fLo2 >= 0.0d) {
                        changeLo = true;
                    } else {
                        changeHi = true;
                    }
                } else if (fLo2 <= fHi) {
                    changeLo = true;
                    changeHi = true;
                } else if (fLo2 <= 0.0d) {
                    changeLo = true;
                } else {
                    changeHi = true;
                }
                if (changeLo) {
                    double xLo3 = FastMath.max(d2, xLo2 - step);
                    remainingEval--;
                    xLo2 = xLo3;
                    fLo2 = univariateFunction.value(xLo3);
                }
                if (changeHi) {
                    d = max;
                    double xHi3 = FastMath.min(d, xHi2 + step);
                    remainingEval--;
                    xHi2 = xHi3;
                    fHi = univariateFunction.value(xHi3);
                } else {
                    d = max;
                }
                remainingEval2 = remainingEval;
                AllowedSolution allowedSolution2 = allowedSolution;
                d3 = d;
            }
        }
        return bracketing.solve(remainingEval, univariateFunction, xLo2, xHi2, baseRoot, allowedSolution);
    }

    public static double[] bracket(UnivariateFunction function, double initial, double lowerBound, double upperBound) throws NullArgumentException, NotStrictlyPositiveException, NoBracketingException {
        return bracket(function, initial, lowerBound, upperBound, 1.0d, 1.0d, Integer.MAX_VALUE);
    }

    public static double[] bracket(UnivariateFunction function, double initial, double lowerBound, double upperBound, int maximumIterations) throws NullArgumentException, NotStrictlyPositiveException, NoBracketingException {
        return bracket(function, initial, lowerBound, upperBound, 1.0d, 1.0d, maximumIterations);
    }

    public static double[] bracket(UnivariateFunction function, double initial, double lowerBound, double upperBound, double q, double r, int maximumIterations) throws NoBracketingException {
        UnivariateFunction univariateFunction = function;
        double d = lowerBound;
        double d2 = upperBound;
        int i = maximumIterations;
        if (univariateFunction == null) {
            throw new NullArgumentException(LocalizedFormats.FUNCTION, new Object[0]);
        } else if (q <= 0.0d) {
            throw new NotStrictlyPositiveException(Double.valueOf(q));
        } else if (i <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.INVALID_MAX_ITERATIONS, Integer.valueOf(maximumIterations));
        } else {
            double b = initial;
            verifySequence(d, b, d2);
            double fa = Double.NaN;
            double fb = Double.NaN;
            double delta = 0.0d;
            double b2 = b;
            double a = initial;
            for (int numIterations = 0; numIterations < i && (a > d || b2 < d2); numIterations++) {
                double previousA = a;
                double previousFa = fa;
                double previousB = b2;
                double previousFb = fb;
                delta = (r * delta) + q;
                a = FastMath.max(initial - delta, d);
                b2 = FastMath.min(initial + delta, d2);
                fa = univariateFunction.value(a);
                fb = univariateFunction.value(b2);
                if (numIterations == 0) {
                    if (fa * fb <= 0.0d) {
                        return new double[]{a, b2};
                    }
                } else if (fa * previousFa <= 0.0d) {
                    return new double[]{a, previousA};
                } else if (fb * previousFb <= 0.0d) {
                    return new double[]{previousB, b2};
                }
            }
            NoBracketingException noBracketingException = new NoBracketingException(a, b2, fa, fb);
            throw noBracketingException;
        }
    }

    public static double midpoint(double a, double b) {
        return (a + b) * 0.5d;
    }

    public static boolean isBracketing(UnivariateFunction function, double lower, double upper) throws NullArgumentException {
        if (function == null) {
            throw new NullArgumentException(LocalizedFormats.FUNCTION, new Object[0]);
        }
        double fLo = function.value(lower);
        double fHi = function.value(upper);
        if ((fLo < 0.0d || fHi > 0.0d) && (fLo > 0.0d || fHi < 0.0d)) {
            return false;
        }
        return true;
    }

    public static boolean isSequence(double start, double mid, double end) {
        return start < mid && mid < end;
    }

    public static void verifyInterval(double lower, double upper) throws NumberIsTooLargeException {
        if (lower >= upper) {
            throw new NumberIsTooLargeException(LocalizedFormats.ENDPOINTS_NOT_AN_INTERVAL, Double.valueOf(lower), Double.valueOf(upper), false);
        }
    }

    public static void verifySequence(double lower, double initial, double upper) throws NumberIsTooLargeException {
        verifyInterval(lower, initial);
        verifyInterval(initial, upper);
    }

    public static void verifyBracketing(UnivariateFunction function, double lower, double upper) throws NullArgumentException, NoBracketingException {
        if (function == null) {
            throw new NullArgumentException(LocalizedFormats.FUNCTION, new Object[0]);
        }
        verifyInterval(lower, upper);
        if (!isBracketing(function, lower, upper)) {
            NoBracketingException noBracketingException = new NoBracketingException(lower, upper, function.value(lower), function.value(upper));
            throw noBracketingException;
        }
    }
}

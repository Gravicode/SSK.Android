package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexUtils;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;

public class LaguerreSolver extends AbstractPolynomialSolver {
    private static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-6d;
    private final ComplexSolver complexSolver;

    private class ComplexSolver {
        private ComplexSolver() {
        }

        public boolean isRoot(double min, double max, Complex z) {
            boolean z2 = false;
            if (!LaguerreSolver.this.isSequence(min, z.getReal(), max)) {
                return false;
            }
            if (FastMath.abs(z.getImaginary()) <= FastMath.max(LaguerreSolver.this.getRelativeAccuracy() * z.abs(), LaguerreSolver.this.getAbsoluteAccuracy()) || z.abs() <= LaguerreSolver.this.getFunctionValueAccuracy()) {
                z2 = true;
            }
            return z2;
        }

        public Complex[] solveAll(Complex[] coefficients, Complex initial) throws NullArgumentException, NoDataException, TooManyEvaluationsException {
            if (coefficients == null) {
                throw new NullArgumentException();
            }
            int n = coefficients.length - 1;
            if (n == 0) {
                throw new NoDataException(LocalizedFormats.POLYNOMIAL);
            }
            Complex[] c = new Complex[(n + 1)];
            for (int i = 0; i <= n; i++) {
                c[i] = coefficients[i];
            }
            Complex[] root = new Complex[n];
            for (int i2 = 0; i2 < n; i2++) {
                Complex[] subarray = new Complex[((n - i2) + 1)];
                System.arraycopy(c, 0, subarray, 0, subarray.length);
                root[i2] = solve(subarray, initial);
                Complex newc = c[n - i2];
                for (int j = (n - i2) - 1; j >= 0; j--) {
                    Complex oldc = c[j];
                    c[j] = newc;
                    newc = oldc.add(newc.multiply(root[i2]));
                }
            }
            return root;
        }

        public Complex solve(Complex[] coefficients, Complex initial) throws NullArgumentException, NoDataException, TooManyEvaluationsException {
            long j;
            Complex[] complexArr = coefficients;
            if (complexArr == null) {
                throw new NullArgumentException();
            }
            int n = complexArr.length - 1;
            if (n == 0) {
                throw new NoDataException(LocalizedFormats.POLYNOMIAL);
            }
            double absoluteAccuracy = LaguerreSolver.this.getAbsoluteAccuracy();
            double relativeAccuracy = LaguerreSolver.this.getRelativeAccuracy();
            double functionValueAccuracy = LaguerreSolver.this.getFunctionValueAccuracy();
            Complex nC = new Complex((double) n, 0.0d);
            Complex n1C = new Complex((double) (n - 1), 0.0d);
            Complex z = initial;
            Complex oldz = new Complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
            while (true) {
                int j2 = n - 1;
                Complex pv = complexArr[n];
                Complex dv = Complex.ZERO;
                Complex d2v = Complex.ZERO;
                while (true) {
                    int j3 = j2;
                    if (j3 < 0) {
                        break;
                    }
                    int n2 = n;
                    d2v = dv.add(z.multiply(d2v));
                    dv = pv.add(z.multiply(dv));
                    pv = complexArr[j3].add(z.multiply(pv));
                    j2 = j3 - 1;
                    n = n2;
                    complexArr = coefficients;
                }
                int n3 = n;
                Complex nC2 = nC;
                Complex n1C2 = n1C;
                double functionValueAccuracy2 = functionValueAccuracy;
                Complex d2v2 = d2v.multiply(new Complex(2.0d, 0.0d));
                if (z.subtract(oldz).abs() <= FastMath.max(z.abs() * relativeAccuracy, absoluteAccuracy) || pv.abs() <= functionValueAccuracy2) {
                    return z;
                }
                Complex G = dv.divide(pv);
                Complex G2 = G.multiply(G);
                Complex complex = d2v2;
                Complex nC3 = nC2;
                double relativeAccuracy2 = relativeAccuracy;
                Complex n1C3 = n1C2;
                Complex delta = n1C3.multiply(nC3.multiply(G2.subtract(d2v2.divide(pv))).subtract(G2));
                Complex deltaSqrt = delta.sqrt();
                Complex complex2 = delta;
                Complex delta2 = G.add(deltaSqrt);
                Complex n1C4 = n1C3;
                Complex dminus = G.subtract(deltaSqrt);
                Complex complex3 = delta2;
                Complex complex4 = dminus;
                Complex complex5 = deltaSqrt;
                Complex denominator = delta2.abs() > dminus.abs() ? delta2 : dminus;
                if (denominator.equals(new Complex(0.0d, 0.0d))) {
                    Complex z2 = z.add(new Complex(absoluteAccuracy, absoluteAccuracy));
                    Complex complex6 = G;
                    j = 9218868437227405312L;
                    oldz = new Complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
                    z = z2;
                } else {
                    j = 9218868437227405312L;
                    Complex oldz2 = z;
                    z = z.subtract(nC3.divide(denominator));
                    oldz = oldz2;
                }
                LaguerreSolver.this.incrementEvaluationCount();
                nC = nC3;
                functionValueAccuracy = functionValueAccuracy2;
                relativeAccuracy = relativeAccuracy2;
                n1C = n1C4;
                long j4 = j;
                n = n3;
                complexArr = coefficients;
            }
        }
    }

    public LaguerreSolver() {
        this(1.0E-6d);
    }

    public LaguerreSolver(double absoluteAccuracy) {
        super(absoluteAccuracy);
        this.complexSolver = new ComplexSolver();
    }

    public LaguerreSolver(double relativeAccuracy, double absoluteAccuracy) {
        super(relativeAccuracy, absoluteAccuracy);
        this.complexSolver = new ComplexSolver();
    }

    public LaguerreSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy) {
        super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);
        this.complexSolver = new ComplexSolver();
    }

    public double doSolve() throws TooManyEvaluationsException, NumberIsTooLargeException, NoBracketingException {
        double min = getMin();
        double max = getMax();
        double initial = getStartValue();
        double functionValueAccuracy = getFunctionValueAccuracy();
        verifySequence(min, initial, max);
        double yInitial = computeObjectiveValue(initial);
        if (FastMath.abs(yInitial) <= functionValueAccuracy) {
            return initial;
        }
        double yMin = computeObjectiveValue(min);
        if (FastMath.abs(yMin) <= functionValueAccuracy) {
            return min;
        }
        if (yInitial * yMin < 0.0d) {
            double d = yMin;
            double d2 = yInitial;
            return laguerre(min, initial, yMin, yInitial);
        }
        double yMin2 = yMin;
        double yInitial2 = yInitial;
        double yMax = computeObjectiveValue(max);
        if (FastMath.abs(yMax) <= functionValueAccuracy) {
            return max;
        }
        if (yInitial2 * yMax < 0.0d) {
            double d3 = yMax;
            return laguerre(initial, max, yInitial2, yMax);
        }
        NoBracketingException noBracketingException = new NoBracketingException(min, max, yMin2, yMax);
        throw noBracketingException;
    }

    @Deprecated
    public double laguerre(double lo, double hi, double fLo, double fHi) {
        Complex[] c = ComplexUtils.convertToComplex(getCoefficients());
        Complex initial = new Complex((lo + hi) * 0.5d, 0.0d);
        Complex z = this.complexSolver.solve(c, initial);
        if (this.complexSolver.isRoot(lo, hi, z)) {
            return z.getReal();
        }
        double r = Double.NaN;
        Complex[] root = this.complexSolver.solveAll(c, initial);
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= root.length) {
                break;
            }
            int i3 = i2;
            Complex[] root2 = root;
            if (this.complexSolver.isRoot(lo, hi, root[i2])) {
                r = root2[i3].getReal();
                break;
            }
            i = i3 + 1;
            root = root2;
        }
        return r;
    }

    public Complex[] solveAllComplex(double[] coefficients, double initial) throws NullArgumentException, NoDataException, TooManyEvaluationsException {
        return solveAllComplex(coefficients, initial, Integer.MAX_VALUE);
    }

    public Complex[] solveAllComplex(double[] coefficients, double initial, int maxEval) throws NullArgumentException, NoDataException, TooManyEvaluationsException {
        setup(maxEval, new PolynomialFunction(coefficients), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, initial);
        return this.complexSolver.solveAll(ComplexUtils.convertToComplex(coefficients), new Complex(initial, 0.0d));
    }

    public Complex solveComplex(double[] coefficients, double initial) throws NullArgumentException, NoDataException, TooManyEvaluationsException {
        return solveComplex(coefficients, initial, Integer.MAX_VALUE);
    }

    public Complex solveComplex(double[] coefficients, double initial, int maxEval) throws NullArgumentException, NoDataException, TooManyEvaluationsException {
        setup(maxEval, new PolynomialFunction(coefficients), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, initial);
        return this.complexSolver.solve(ComplexUtils.convertToComplex(coefficients), new Complex(initial, 0.0d));
    }
}

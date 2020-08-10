package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.util.FastMath;

public class SecantSolver extends AbstractUnivariateSolver {
    protected static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-6d;

    public SecantSolver() {
        super(1.0E-6d);
    }

    public SecantSolver(double absoluteAccuracy) {
        super(absoluteAccuracy);
    }

    public SecantSolver(double relativeAccuracy, double absoluteAccuracy) {
        super(relativeAccuracy, absoluteAccuracy);
    }

    /* access modifiers changed from: protected */
    public final double doSolve() throws TooManyEvaluationsException, NoBracketingException {
        SecantSolver secantSolver = this;
        double x0 = getMin();
        double x1 = getMax();
        double f0 = secantSolver.computeObjectiveValue(x0);
        double f1 = secantSolver.computeObjectiveValue(x1);
        if (f0 == 0.0d) {
            return x0;
        }
        if (f1 == 0.0d) {
            return x1;
        }
        secantSolver.verifyBracketing(x0, x1);
        double ftol = getFunctionValueAccuracy();
        double atol = getAbsoluteAccuracy();
        double rtol = getRelativeAccuracy();
        while (true) {
            double x = x1 - (((x1 - x0) * f1) / (f1 - f0));
            double fx = secantSolver.computeObjectiveValue(x);
            if (fx == 0.0d) {
                return x;
            }
            double x02 = x1;
            double f02 = f1;
            x1 = x;
            f1 = fx;
            if (FastMath.abs(f1) <= ftol) {
                return x1;
            }
            double f03 = f02;
            double x03 = x02;
            if (FastMath.abs(x1 - x02) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
                return x1;
            }
            f0 = f03;
            x0 = x03;
            secantSolver = this;
        }
    }
}

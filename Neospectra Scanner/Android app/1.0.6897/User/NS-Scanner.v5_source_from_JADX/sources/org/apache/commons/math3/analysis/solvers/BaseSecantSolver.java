package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.util.FastMath;

public abstract class BaseSecantSolver extends AbstractUnivariateSolver implements BracketedUnivariateSolver<UnivariateFunction> {
    protected static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-6d;
    private AllowedSolution allowed = AllowedSolution.ANY_SIDE;
    private final Method method;

    protected enum Method {
        REGULA_FALSI,
        ILLINOIS,
        PEGASUS
    }

    protected BaseSecantSolver(double absoluteAccuracy, Method method2) {
        super(absoluteAccuracy);
        this.method = method2;
    }

    protected BaseSecantSolver(double relativeAccuracy, double absoluteAccuracy, Method method2) {
        super(relativeAccuracy, absoluteAccuracy);
        this.method = method2;
    }

    protected BaseSecantSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy, Method method2) {
        super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);
        this.method = method2;
    }

    public double solve(int maxEval, UnivariateFunction f, double min, double max, AllowedSolution allowedSolution) {
        return solve(maxEval, f, min, max, min + ((max - min) * 0.5d), allowedSolution);
    }

    public double solve(int maxEval, UnivariateFunction f, double min, double max, double startValue, AllowedSolution allowedSolution) {
        this.allowed = allowedSolution;
        return super.solve(maxEval, f, min, max, startValue);
    }

    public double solve(int maxEval, UnivariateFunction f, double min, double max, double startValue) {
        return solve(maxEval, f, min, max, startValue, AllowedSolution.ANY_SIDE);
    }

    /* access modifiers changed from: protected */
    public final double doSolve() throws ConvergenceException {
        double x0 = getMin();
        double x1 = getMax();
        double f0 = computeObjectiveValue(x0);
        double f1 = computeObjectiveValue(x1);
        if (f0 == 0.0d) {
            return x0;
        }
        if (f1 == 0.0d) {
            return x1;
        }
        verifyBracketing(x0, x1);
        double ftol = getFunctionValueAccuracy();
        double atol = getAbsoluteAccuracy();
        double rtol = getRelativeAccuracy();
        double f02 = f0;
        double x02 = x0;
        boolean inverted = false;
        while (true) {
            double x = x1 - (((x1 - x02) * f1) / (f1 - f02));
            double fx = computeObjectiveValue(x);
            if (fx == 0.0d) {
                return x;
            }
            if (f1 * fx < 0.0d) {
                x02 = x1;
                f02 = f1;
                inverted = !inverted;
            } else {
                boolean inverted2 = inverted;
                switch (this.method) {
                    case ILLINOIS:
                        f02 *= 0.5d;
                        break;
                    case PEGASUS:
                        f02 *= f1 / (f1 + fx);
                        break;
                    case REGULA_FALSI:
                        if (x == x1) {
                            throw new ConvergenceException();
                        }
                        break;
                    default:
                        double d = x;
                        throw new MathInternalError();
                }
                inverted = inverted2;
            }
            x1 = x;
            f1 = fx;
            if (FastMath.abs(f1) <= ftol) {
                double d2 = x;
                switch (this.allowed) {
                    case ANY_SIDE:
                        return x1;
                    case LEFT_SIDE:
                        if (inverted) {
                            return x1;
                        }
                        break;
                    case RIGHT_SIDE:
                        if (!inverted) {
                            return x1;
                        }
                        break;
                    case BELOW_SIDE:
                        if (f1 <= 0.0d) {
                            return x1;
                        }
                        break;
                    case ABOVE_SIDE:
                        if (f1 >= 0.0d) {
                            return x1;
                        }
                        break;
                    default:
                        throw new MathInternalError();
                }
            }
            double x03 = x02;
            if (FastMath.abs(x1 - x02) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
                switch (this.allowed) {
                    case ANY_SIDE:
                        return x1;
                    case LEFT_SIDE:
                        return inverted ? x1 : x03;
                    case RIGHT_SIDE:
                        return inverted ? x03 : x1;
                    case BELOW_SIDE:
                        return f1 <= 0.0d ? x1 : x03;
                    case ABOVE_SIDE:
                        return f1 >= 0.0d ? x1 : x03;
                    default:
                        throw new MathInternalError();
                }
            } else {
                x02 = x03;
            }
        }
    }
}

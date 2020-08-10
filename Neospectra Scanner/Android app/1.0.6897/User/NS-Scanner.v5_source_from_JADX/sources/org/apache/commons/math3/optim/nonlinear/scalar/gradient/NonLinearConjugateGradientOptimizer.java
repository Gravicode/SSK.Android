package org.apache.commons.math3.optim.nonlinear.scalar.gradient;

import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.MathUnsupportedOperationException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GradientMultivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.LineSearch;

public class NonLinearConjugateGradientOptimizer extends GradientMultivariateOptimizer {
    private final LineSearch line;
    private final Preconditioner preconditioner;
    private final Formula updateFormula;

    @Deprecated
    public static class BracketingStep implements OptimizationData {
        private final double initialStep;

        public BracketingStep(double step) {
            this.initialStep = step;
        }

        public double getBracketingStep() {
            return this.initialStep;
        }
    }

    public enum Formula {
        FLETCHER_REEVES,
        POLAK_RIBIERE
    }

    public static class IdentityPreconditioner implements Preconditioner {
        public double[] precondition(double[] variables, double[] r) {
            return (double[]) r.clone();
        }
    }

    public NonLinearConjugateGradientOptimizer(Formula updateFormula2, ConvergenceChecker<PointValuePair> checker) {
        this(updateFormula2, checker, 1.0E-8d, 1.0E-8d, 1.0E-8d, new IdentityPreconditioner());
    }

    @Deprecated
    public NonLinearConjugateGradientOptimizer(Formula updateFormula2, ConvergenceChecker<PointValuePair> checker, UnivariateSolver lineSearchSolver) {
        this(updateFormula2, checker, lineSearchSolver, new IdentityPreconditioner());
    }

    public NonLinearConjugateGradientOptimizer(Formula updateFormula2, ConvergenceChecker<PointValuePair> checker, double relativeTolerance, double absoluteTolerance, double initialBracketingRange) {
        this(updateFormula2, checker, relativeTolerance, absoluteTolerance, initialBracketingRange, new IdentityPreconditioner());
    }

    @Deprecated
    public NonLinearConjugateGradientOptimizer(Formula updateFormula2, ConvergenceChecker<PointValuePair> checker, UnivariateSolver lineSearchSolver, Preconditioner preconditioner2) {
        this(updateFormula2, checker, lineSearchSolver.getRelativeAccuracy(), lineSearchSolver.getAbsoluteAccuracy(), lineSearchSolver.getAbsoluteAccuracy(), preconditioner2);
    }

    public NonLinearConjugateGradientOptimizer(Formula updateFormula2, ConvergenceChecker<PointValuePair> checker, double relativeTolerance, double absoluteTolerance, double initialBracketingRange, Preconditioner preconditioner2) {
        super(checker);
        this.updateFormula = updateFormula2;
        this.preconditioner = preconditioner2;
        LineSearch lineSearch = new LineSearch(this, relativeTolerance, absoluteTolerance, initialBracketingRange);
        this.line = lineSearch;
    }

    public PointValuePair optimize(OptimizationData... optData) throws TooManyEvaluationsException {
        return super.optimize(optData);
    }

    /* access modifiers changed from: protected */
    public PointValuePair doOptimize() {
        double delta;
        double beta;
        ConvergenceChecker convergenceChecker = getConvergenceChecker();
        double[] point = getStartPoint();
        GoalType goal = getGoalType();
        int n = point.length;
        double[] r = computeObjectiveGradient(point);
        if (goal == GoalType.MINIMIZE) {
            for (int i = 0; i < n; i++) {
                r[i] = -r[i];
            }
        }
        double[] steepestDescent = this.preconditioner.precondition(point, r);
        double[] searchDirection = (double[]) steepestDescent.clone();
        double delta2 = 0.0d;
        for (int i2 = 0; i2 < n; i2++) {
            delta2 = delta + (r[i2] * searchDirection[i2]);
        }
        PointValuePair current = null;
        while (true) {
            incrementIterationCount();
            double objective = computeObjectiveValue(point);
            PointValuePair previous = current;
            current = new PointValuePair(point, objective);
            if (previous != null && convergenceChecker.converged(getIterations(), previous, current)) {
                return current;
            }
            double step = this.line.search(point, searchDirection).getPoint();
            int i3 = 0;
            while (true) {
                ConvergenceChecker convergenceChecker2 = convergenceChecker;
                int i4 = i3;
                if (i4 < point.length) {
                    point[i4] = point[i4] + (searchDirection[i4] * step);
                    i3 = i4 + 1;
                    convergenceChecker = convergenceChecker2;
                } else {
                    double[] r2 = computeObjectiveGradient(point);
                    if (goal == GoalType.MINIMIZE) {
                        int i5 = 0;
                        while (i5 < n) {
                            double objective2 = objective;
                            r2[i5] = -r2[i5];
                            i5++;
                            objective = objective2;
                        }
                    }
                    double objective3 = delta;
                    double[] newSteepestDescent = this.preconditioner.precondition(point, r2);
                    delta = 0.0d;
                    for (int i6 = 0; i6 < n; i6++) {
                        delta += r2[i6] * newSteepestDescent[i6];
                    }
                    double[] point2 = point;
                    switch (this.updateFormula) {
                        case FLETCHER_REEVES:
                            beta = delta / objective3;
                            break;
                        case POLAK_RIBIERE:
                            double deltaMid = 0.0d;
                            for (int i7 = 0; i7 < r2.length; i7++) {
                                deltaMid += r2[i7] * steepestDescent[i7];
                            }
                            beta = (delta - deltaMid) / objective3;
                            break;
                        default:
                            throw new MathInternalError();
                    }
                    double beta2 = beta;
                    steepestDescent = newSteepestDescent;
                    if (getIterations() % n == 0 || beta2 < 0.0d) {
                        searchDirection = (double[]) steepestDescent.clone();
                    } else {
                        for (int i8 = 0; i8 < n; i8++) {
                            searchDirection[i8] = steepestDescent[i8] + (searchDirection[i8] * beta2);
                        }
                    }
                    convergenceChecker = convergenceChecker2;
                    point = point2;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void parseOptimizationData(OptimizationData... optData) {
        super.parseOptimizationData(optData);
        checkParameters();
    }

    private void checkParameters() {
        if (getLowerBound() != null || getUpperBound() != null) {
            throw new MathUnsupportedOperationException(LocalizedFormats.CONSTRAINT, new Object[0]);
        }
    }
}

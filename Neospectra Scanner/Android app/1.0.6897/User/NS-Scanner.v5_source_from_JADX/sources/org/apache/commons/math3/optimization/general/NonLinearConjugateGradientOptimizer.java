package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.util.FastMath;

@Deprecated
public class NonLinearConjugateGradientOptimizer extends AbstractScalarDifferentiableOptimizer {
    private double initialStep;
    /* access modifiers changed from: private */
    public double[] point;
    private final Preconditioner preconditioner;
    private final UnivariateSolver solver;
    private final ConjugateGradientFormula updateFormula;

    public static class IdentityPreconditioner implements Preconditioner {
        public double[] precondition(double[] variables, double[] r) {
            return (double[]) r.clone();
        }
    }

    private class LineSearchFunction implements UnivariateFunction {
        private final double[] searchDirection;

        LineSearchFunction(double[] searchDirection2) {
            this.searchDirection = searchDirection2;
        }

        public double value(double x) {
            double[] shiftedPoint = (double[]) NonLinearConjugateGradientOptimizer.this.point.clone();
            for (int i = 0; i < shiftedPoint.length; i++) {
                shiftedPoint[i] = shiftedPoint[i] + (this.searchDirection[i] * x);
            }
            double[] gradient = NonLinearConjugateGradientOptimizer.this.computeObjectiveGradient(shiftedPoint);
            double dotProduct = 0.0d;
            for (int i2 = 0; i2 < gradient.length; i2++) {
                dotProduct += gradient[i2] * this.searchDirection[i2];
            }
            return dotProduct;
        }
    }

    @Deprecated
    public NonLinearConjugateGradientOptimizer(ConjugateGradientFormula updateFormula2) {
        this(updateFormula2, new SimpleValueChecker());
    }

    public NonLinearConjugateGradientOptimizer(ConjugateGradientFormula updateFormula2, ConvergenceChecker<PointValuePair> checker) {
        this(updateFormula2, checker, new BrentSolver(), new IdentityPreconditioner());
    }

    public NonLinearConjugateGradientOptimizer(ConjugateGradientFormula updateFormula2, ConvergenceChecker<PointValuePair> checker, UnivariateSolver lineSearchSolver) {
        this(updateFormula2, checker, lineSearchSolver, new IdentityPreconditioner());
    }

    public NonLinearConjugateGradientOptimizer(ConjugateGradientFormula updateFormula2, ConvergenceChecker<PointValuePair> checker, UnivariateSolver lineSearchSolver, Preconditioner preconditioner2) {
        super(checker);
        this.updateFormula = updateFormula2;
        this.solver = lineSearchSolver;
        this.preconditioner = preconditioner2;
        this.initialStep = 1.0d;
    }

    public void setInitialStep(double initialStep2) {
        if (initialStep2 <= 0.0d) {
            this.initialStep = 1.0d;
        } else {
            this.initialStep = initialStep2;
        }
    }

    /* access modifiers changed from: protected */
    public PointValuePair doOptimize() {
        double beta;
        ConvergenceChecker<PointValuePair> checker = getConvergenceChecker();
        this.point = getStartPoint();
        GoalType goal = getGoalType();
        int n = this.point.length;
        double[] r = computeObjectiveGradient(this.point);
        if (goal == GoalType.MINIMIZE) {
            for (int i = 0; i < n; i++) {
                r[i] = -r[i];
            }
        }
        double[] steepestDescent = this.preconditioner.precondition(this.point, r);
        double[] searchDirection = (double[]) steepestDescent.clone();
        double delta = 0.0d;
        for (int i2 = 0; i2 < n; i2++) {
            delta += r[i2] * searchDirection[i2];
        }
        PointValuePair current = null;
        int iter = 0;
        int maxEval = getMaxEvaluations();
        double[] dArr = r;
        double[] steepestDescent2 = steepestDescent;
        double[] searchDirection2 = searchDirection;
        double delta2 = delta;
        while (true) {
            int iter2 = iter + 1;
            double objective = computeObjectiveValue(this.point);
            PointValuePair previous = current;
            PointValuePair current2 = new PointValuePair(this.point, objective);
            if (previous != null && checker.converged(iter2, previous, current2)) {
                return current2;
            }
            LineSearchFunction lineSearchFunction = new LineSearchFunction(searchDirection2);
            int iter3 = iter2;
            PointValuePair pointValuePair = previous;
            PointValuePair current3 = current2;
            double d = objective;
            double uB = findUpperBound(lineSearchFunction, 0.0d, this.initialStep);
            double step = this.solver.solve(maxEval, lineSearchFunction, 0.0d, uB, 1.0E-15d);
            maxEval -= this.solver.getEvaluations();
            for (int i3 = 0; i3 < this.point.length; i3++) {
                double[] dArr2 = this.point;
                dArr2[i3] = dArr2[i3] + (searchDirection2[i3] * step);
            }
            double[] r2 = computeObjectiveGradient(this.point);
            if (goal == GoalType.MINIMIZE) {
                for (int i4 = 0; i4 < n; i4++) {
                    r2[i4] = -r2[i4];
                }
            }
            double deltaOld = delta2;
            LineSearchFunction lineSearchFunction2 = lineSearchFunction;
            double[] newSteepestDescent = this.preconditioner.precondition(this.point, r2);
            delta2 = 0.0d;
            for (int i5 = 0; i5 < n; i5++) {
                delta2 += r2[i5] * newSteepestDescent[i5];
            }
            double d2 = uB;
            if (this.updateFormula == ConjugateGradientFormula.FLETCHER_REEVES) {
                beta = delta2 / deltaOld;
                double d3 = step;
            } else {
                double deltaMid = 0.0d;
                int i6 = 0;
                while (true) {
                    double step2 = step;
                    if (i6 >= r2.length) {
                        break;
                    }
                    deltaMid += r2[i6] * steepestDescent2[i6];
                    i6++;
                    step = step2;
                }
                beta = (delta2 - deltaMid) / deltaOld;
            }
            steepestDescent2 = newSteepestDescent;
            if (iter3 % n == 0 || beta < 0.0d) {
                searchDirection2 = (double[]) steepestDescent2.clone();
            } else {
                for (int i7 = 0; i7 < n; i7++) {
                    searchDirection2[i7] = steepestDescent2[i7] + (searchDirection2[i7] * beta);
                }
            }
            iter = iter3;
            current = current3;
        }
    }

    private double findUpperBound(UnivariateFunction f, double a, double h) {
        double yA = f.value(a);
        double d = yA;
        double step = h;
        while (step < Double.MAX_VALUE) {
            double b = a + step;
            double yB = f.value(b);
            if (yA * yB <= 0.0d) {
                return b;
            }
            step *= FastMath.max(2.0d, yA / yB);
        }
        UnivariateFunction univariateFunction = f;
        throw new MathIllegalStateException(LocalizedFormats.UNABLE_TO_BRACKET_OPTIMUM_IN_LINE_SEARCH, new Object[0]);
    }
}

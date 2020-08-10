package org.apache.commons.math3.optim.nonlinear.scalar;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.univariate.BracketFinder;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.SearchInterval;
import org.apache.commons.math3.optim.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optim.univariate.UnivariateObjectiveFunction;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;

public class LineSearch {
    private static final double ABS_TOL_UNUSED = Double.MIN_VALUE;
    private static final double REL_TOL_UNUSED = 1.0E-15d;
    private final BracketFinder bracket = new BracketFinder();
    private final double initialBracketingRange;
    private final UnivariateOptimizer lineOptimizer;
    /* access modifiers changed from: private */
    public final MultivariateOptimizer mainOptimizer;

    public LineSearch(MultivariateOptimizer optimizer, double relativeTolerance, double absoluteTolerance, double initialBracketingRange2) {
        this.mainOptimizer = optimizer;
        BrentOptimizer brentOptimizer = new BrentOptimizer(1.0E-15d, ABS_TOL_UNUSED, new SimpleUnivariateValueChecker(relativeTolerance, absoluteTolerance));
        this.lineOptimizer = brentOptimizer;
        this.initialBracketingRange = initialBracketingRange2;
    }

    public UnivariatePointValuePair search(double[] startPoint, double[] direction) {
        final double[] dArr = startPoint;
        final int n = dArr.length;
        final double[] dArr2 = direction;
        C10141 r3 = new UnivariateFunction() {
            public double value(double alpha) {
                double[] x = new double[n];
                for (int i = 0; i < n; i++) {
                    x[i] = dArr[i] + (dArr2[i] * alpha);
                }
                return LineSearch.this.mainOptimizer.computeObjectiveValue(x);
            }
        };
        GoalType goal = this.mainOptimizer.getGoalType();
        this.bracket.search(r3, goal, 0.0d, this.initialBracketingRange);
        UnivariateOptimizer univariateOptimizer = this.lineOptimizer;
        SearchInterval searchInterval = new SearchInterval(this.bracket.getLo(), this.bracket.getHi(), this.bracket.getMid());
        return univariateOptimizer.optimize(new OptimizationData[]{new MaxEval(Integer.MAX_VALUE), new UnivariateObjectiveFunction(r3), goal, searchInterval});
    }
}

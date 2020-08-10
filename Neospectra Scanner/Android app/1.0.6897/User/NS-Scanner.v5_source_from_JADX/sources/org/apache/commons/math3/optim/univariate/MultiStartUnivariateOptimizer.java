package org.apache.commons.math3.optim.univariate;

import java.util.Arrays;
import java.util.Comparator;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.random.RandomGenerator;

public class MultiStartUnivariateOptimizer extends UnivariateOptimizer {
    private RandomGenerator generator;
    private int maxEvalIndex = -1;
    private OptimizationData[] optimData;
    private UnivariatePointValuePair[] optima;
    private final UnivariateOptimizer optimizer;
    private int searchIntervalIndex = -1;
    private int starts;
    private int totalEvaluations;

    public MultiStartUnivariateOptimizer(UnivariateOptimizer optimizer2, int starts2, RandomGenerator generator2) {
        super(optimizer2.getConvergenceChecker());
        if (starts2 < 1) {
            throw new NotStrictlyPositiveException(Integer.valueOf(starts2));
        }
        this.optimizer = optimizer2;
        this.starts = starts2;
        this.generator = generator2;
    }

    public int getEvaluations() {
        return this.totalEvaluations;
    }

    public UnivariatePointValuePair[] getOptima() {
        if (this.optima != null) {
            return (UnivariatePointValuePair[]) this.optima.clone();
        }
        throw new MathIllegalStateException(LocalizedFormats.NO_OPTIMUM_COMPUTED_YET, new Object[0]);
    }

    public UnivariatePointValuePair optimize(OptimizationData... optData) {
        this.optimData = optData;
        return super.optimize(optData);
    }

    /* access modifiers changed from: protected */
    public UnivariatePointValuePair doOptimize() {
        for (int i = 0; i < this.optimData.length; i++) {
            if (this.optimData[i] instanceof MaxEval) {
                this.optimData[i] = null;
                this.maxEvalIndex = i;
            } else if (this.optimData[i] instanceof SearchInterval) {
                this.optimData[i] = null;
                this.searchIntervalIndex = i;
            }
        }
        if (this.maxEvalIndex == -1) {
            throw new MathIllegalStateException();
        } else if (this.searchIntervalIndex == -1) {
            throw new MathIllegalStateException();
        } else {
            this.optima = new UnivariatePointValuePair[this.starts];
            this.totalEvaluations = 0;
            int maxEval = getMaxEvaluations();
            double min = getMin();
            double max = getMax();
            double startValue = getStartValue();
            Throwable th = null;
            int i2 = 0;
            while (i2 < this.starts) {
                try {
                    this.optimData[this.maxEvalIndex] = new MaxEval(maxEval - this.totalEvaluations);
                    double s = i2 == 0 ? startValue : (this.generator.nextDouble() * (max - min)) + min;
                    OptimizationData[] optimizationDataArr = this.optimData;
                    int i3 = this.searchIntervalIndex;
                    SearchInterval searchInterval = new SearchInterval(min, max, s);
                    optimizationDataArr[i3] = searchInterval;
                    this.optima[i2] = this.optimizer.optimize(this.optimData);
                } catch (RuntimeException e) {
                    RuntimeException lastException = e;
                    this.optima[i2] = null;
                    th = lastException;
                }
                this.totalEvaluations += this.optimizer.getEvaluations();
                i2++;
            }
            sortPairs(getGoalType());
            if (this.optima[0] != null) {
                return this.optima[0];
            }
            throw th;
        }
    }

    private void sortPairs(final GoalType goal) {
        Arrays.sort(this.optima, new Comparator<UnivariatePointValuePair>() {
            public int compare(UnivariatePointValuePair o1, UnivariatePointValuePair o2) {
                if (o1 == null) {
                    return o2 == null ? 0 : 1;
                } else if (o2 == null) {
                    return -1;
                } else {
                    double v1 = o1.getValue();
                    double v2 = o2.getValue();
                    return goal == GoalType.MINIMIZE ? Double.compare(v1, v2) : Double.compare(v2, v1);
                }
            }
        });
    }
}

package org.apache.commons.math3.optimization;

import java.util.Arrays;
import java.util.Comparator;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomVectorGenerator;

@Deprecated
public class BaseMultivariateMultiStartOptimizer<FUNC extends MultivariateFunction> implements BaseMultivariateOptimizer<FUNC> {
    private RandomVectorGenerator generator;
    private int maxEvaluations;
    private PointValuePair[] optima;
    private final BaseMultivariateOptimizer<FUNC> optimizer;
    private int starts;
    private int totalEvaluations;

    protected BaseMultivariateMultiStartOptimizer(BaseMultivariateOptimizer<FUNC> optimizer2, int starts2, RandomVectorGenerator generator2) {
        if (optimizer2 == null || generator2 == null) {
            throw new NullArgumentException();
        } else if (starts2 < 1) {
            throw new NotStrictlyPositiveException(Integer.valueOf(starts2));
        } else {
            this.optimizer = optimizer2;
            this.starts = starts2;
            this.generator = generator2;
        }
    }

    public PointValuePair[] getOptima() {
        if (this.optima != null) {
            return (PointValuePair[]) this.optima.clone();
        }
        throw new MathIllegalStateException(LocalizedFormats.NO_OPTIMUM_COMPUTED_YET, new Object[0]);
    }

    public int getMaxEvaluations() {
        return this.maxEvaluations;
    }

    public int getEvaluations() {
        return this.totalEvaluations;
    }

    public ConvergenceChecker<PointValuePair> getConvergenceChecker() {
        return this.optimizer.getConvergenceChecker();
    }

    public PointValuePair optimize(int maxEval, FUNC f, GoalType goal, double[] startPoint) {
        this.maxEvaluations = maxEval;
        this.optima = new PointValuePair[this.starts];
        this.totalEvaluations = 0;
        Throwable th = null;
        int i = 0;
        while (i < this.starts) {
            try {
                this.optima[i] = this.optimizer.optimize(maxEval - this.totalEvaluations, f, goal, i == 0 ? startPoint : this.generator.nextVector());
            } catch (RuntimeException mue) {
                th = mue;
                this.optima[i] = null;
            }
            this.totalEvaluations += this.optimizer.getEvaluations();
            i++;
        }
        sortPairs(goal);
        if (this.optima[0] != null) {
            return this.optima[0];
        }
        throw th;
    }

    private void sortPairs(final GoalType goal) {
        Arrays.sort(this.optima, new Comparator<PointValuePair>() {
            public int compare(PointValuePair o1, PointValuePair o2) {
                if (o1 == null) {
                    return o2 == null ? 0 : 1;
                } else if (o2 == null) {
                    return -1;
                } else {
                    double v1 = ((Double) o1.getValue()).doubleValue();
                    double v2 = ((Double) o2.getValue()).doubleValue();
                    return goal == GoalType.MINIMIZE ? Double.compare(v1, v2) : Double.compare(v2, v1);
                }
            }
        });
    }
}

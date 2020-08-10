package org.apache.commons.math3.optimization.univariate;

import java.util.Arrays;
import java.util.Comparator;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.random.RandomGenerator;

@Deprecated
public class UnivariateMultiStartOptimizer<FUNC extends UnivariateFunction> implements BaseUnivariateOptimizer<FUNC> {
    private RandomGenerator generator;
    private int maxEvaluations;
    private UnivariatePointValuePair[] optima;
    private final BaseUnivariateOptimizer<FUNC> optimizer;
    private int starts;
    private int totalEvaluations;

    public UnivariateMultiStartOptimizer(BaseUnivariateOptimizer<FUNC> optimizer2, int starts2, RandomGenerator generator2) {
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

    public ConvergenceChecker<UnivariatePointValuePair> getConvergenceChecker() {
        return this.optimizer.getConvergenceChecker();
    }

    public int getMaxEvaluations() {
        return this.maxEvaluations;
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

    public UnivariatePointValuePair optimize(int maxEval, FUNC f, GoalType goal, double min, double max) {
        return optimize(maxEval, f, goal, min, max, min + ((max - min) * 0.5d));
    }

    public UnivariatePointValuePair optimize(int maxEval, FUNC f, GoalType goal, double min, double max, double startValue) {
        int i;
        RuntimeException mue;
        double s;
        this.optima = new UnivariatePointValuePair[this.starts];
        this.totalEvaluations = 0;
        Throwable th = null;
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 >= this.starts) {
                break;
            }
            if (i3 == 0) {
                s = startValue;
            } else {
                try {
                    s = min + (this.generator.nextDouble() * (max - min));
                } catch (RuntimeException e) {
                    i = i3;
                    mue = e;
                    RuntimeException lastException = mue;
                    this.optima[i] = null;
                    th = lastException;
                    this.totalEvaluations += this.optimizer.getEvaluations();
                    i2 = i + 1;
                }
            }
            i = i3;
            try {
                this.optima[i] = this.optimizer.optimize(maxEval - this.totalEvaluations, f, goal, min, max, s);
            } catch (RuntimeException e2) {
                mue = e2;
            }
            this.totalEvaluations += this.optimizer.getEvaluations();
            i2 = i + 1;
        }
        sortPairs(goal);
        if (this.optima[0] != null) {
            return this.optima[0];
        }
        throw th;
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

package org.apache.commons.math3.optim;

import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.random.RandomVectorGenerator;

public abstract class BaseMultiStartMultivariateOptimizer<PAIR> extends BaseMultivariateOptimizer<PAIR> {
    private RandomVectorGenerator generator;
    private int initialGuessIndex = -1;
    private int maxEvalIndex = -1;
    private OptimizationData[] optimData;
    private final BaseMultivariateOptimizer<PAIR> optimizer;
    private int starts;
    private int totalEvaluations;

    /* access modifiers changed from: protected */
    public abstract void clear();

    public abstract PAIR[] getOptima();

    /* access modifiers changed from: protected */
    public abstract void store(PAIR pair);

    public BaseMultiStartMultivariateOptimizer(BaseMultivariateOptimizer<PAIR> optimizer2, int starts2, RandomVectorGenerator generator2) {
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

    public PAIR optimize(OptimizationData... optData) {
        this.optimData = optData;
        return super.optimize(optData);
    }

    /* access modifiers changed from: protected */
    public PAIR doOptimize() {
        double[] s;
        for (int i = 0; i < this.optimData.length; i++) {
            if (this.optimData[i] instanceof MaxEval) {
                this.optimData[i] = null;
                this.maxEvalIndex = i;
            }
            if (this.optimData[i] instanceof InitialGuess) {
                this.optimData[i] = null;
                this.initialGuessIndex = i;
            }
        }
        if (this.maxEvalIndex == -1) {
            throw new MathIllegalStateException();
        } else if (this.initialGuessIndex == -1) {
            throw new MathIllegalStateException();
        } else {
            this.totalEvaluations = 0;
            clear();
            int maxEval = getMaxEvaluations();
            double[] min = getLowerBound();
            double[] max = getUpperBound();
            double[] startPoint = getStartPoint();
            Throwable th = null;
            for (int i2 = 0; i2 < this.starts; i2++) {
                try {
                    this.optimData[this.maxEvalIndex] = new MaxEval(maxEval - this.totalEvaluations);
                    if (i2 == 0) {
                        s = startPoint;
                    } else {
                        double[] s2 = null;
                        int k = 0;
                        while (s2 == null) {
                            int attempts = k + 1;
                            if (k >= getMaxEvaluations()) {
                                throw new TooManyEvaluationsException(Integer.valueOf(getMaxEvaluations()));
                            }
                            s2 = this.generator.nextVector();
                            int k2 = 0;
                            while (s2 != null && k2 < s2.length) {
                                if ((min != null && s2[k2] < min[k2]) || (max != null && s2[k2] > max[k2])) {
                                    s2 = null;
                                }
                                k2++;
                            }
                            k = attempts;
                        }
                        s = s2;
                    }
                    this.optimData[this.initialGuessIndex] = new InitialGuess(s);
                    store(this.optimizer.optimize(this.optimData));
                } catch (RuntimeException mue) {
                    th = mue;
                }
                this.totalEvaluations += this.optimizer.getEvaluations();
            }
            PAIR[] optima = getOptima();
            if (optima.length != 0) {
                return optima[0];
            }
            throw th;
        }
    }
}

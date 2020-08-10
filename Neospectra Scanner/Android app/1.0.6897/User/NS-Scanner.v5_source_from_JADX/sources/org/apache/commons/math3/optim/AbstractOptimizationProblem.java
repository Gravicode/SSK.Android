package org.apache.commons.math3.optim;

import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.util.Incrementor;
import org.apache.commons.math3.util.Incrementor.MaxCountExceededCallback;

public abstract class AbstractOptimizationProblem<PAIR> implements OptimizationProblem<PAIR> {
    private static final MaxEvalCallback MAX_EVAL_CALLBACK = new MaxEvalCallback();
    private static final MaxIterCallback MAX_ITER_CALLBACK = new MaxIterCallback();
    private final ConvergenceChecker<PAIR> checker;
    private final int maxEvaluations;
    private final int maxIterations;

    private static class MaxEvalCallback implements MaxCountExceededCallback {
        private MaxEvalCallback() {
        }

        public void trigger(int max) {
            throw new TooManyEvaluationsException(Integer.valueOf(max));
        }
    }

    private static class MaxIterCallback implements MaxCountExceededCallback {
        private MaxIterCallback() {
        }

        public void trigger(int max) {
            throw new TooManyIterationsException(Integer.valueOf(max));
        }
    }

    protected AbstractOptimizationProblem(int maxEvaluations2, int maxIterations2, ConvergenceChecker<PAIR> checker2) {
        this.maxEvaluations = maxEvaluations2;
        this.maxIterations = maxIterations2;
        this.checker = checker2;
    }

    public Incrementor getEvaluationCounter() {
        return new Incrementor(this.maxEvaluations, MAX_EVAL_CALLBACK);
    }

    public Incrementor getIterationCounter() {
        return new Incrementor(this.maxIterations, MAX_ITER_CALLBACK);
    }

    public ConvergenceChecker<PAIR> getConvergenceChecker() {
        return this.checker;
    }
}

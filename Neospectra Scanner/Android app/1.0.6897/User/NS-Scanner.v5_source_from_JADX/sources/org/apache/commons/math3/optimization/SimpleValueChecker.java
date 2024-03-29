package org.apache.commons.math3.optimization;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;

@Deprecated
public class SimpleValueChecker extends AbstractConvergenceChecker<PointValuePair> {
    private static final int ITERATION_CHECK_DISABLED = -1;
    private final int maxIterationCount;

    @Deprecated
    public SimpleValueChecker() {
        this.maxIterationCount = -1;
    }

    public SimpleValueChecker(double relativeThreshold, double absoluteThreshold) {
        super(relativeThreshold, absoluteThreshold);
        this.maxIterationCount = -1;
    }

    public SimpleValueChecker(double relativeThreshold, double absoluteThreshold, int maxIter) {
        super(relativeThreshold, absoluteThreshold);
        if (maxIter <= 0) {
            throw new NotStrictlyPositiveException(Integer.valueOf(maxIter));
        }
        this.maxIterationCount = maxIter;
    }

    public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
        boolean z = true;
        if (this.maxIterationCount != -1 && iteration >= this.maxIterationCount) {
            return true;
        }
        double p = ((Double) previous.getValue()).doubleValue();
        double c = ((Double) current.getValue()).doubleValue();
        double difference = FastMath.abs(p - c);
        if (difference > getRelativeThreshold() * FastMath.max(FastMath.abs(p), FastMath.abs(c)) && difference > getAbsoluteThreshold()) {
            z = false;
        }
        return z;
    }
}

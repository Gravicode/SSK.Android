package org.apache.commons.math3.optimization;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;

@Deprecated
public class SimpleVectorValueChecker extends AbstractConvergenceChecker<PointVectorValuePair> {
    private static final int ITERATION_CHECK_DISABLED = -1;
    private final int maxIterationCount;

    @Deprecated
    public SimpleVectorValueChecker() {
        this.maxIterationCount = -1;
    }

    public SimpleVectorValueChecker(double relativeThreshold, double absoluteThreshold) {
        super(relativeThreshold, absoluteThreshold);
        this.maxIterationCount = -1;
    }

    public SimpleVectorValueChecker(double relativeThreshold, double absoluteThreshold, int maxIter) {
        super(relativeThreshold, absoluteThreshold);
        if (maxIter <= 0) {
            throw new NotStrictlyPositiveException(Integer.valueOf(maxIter));
        }
        this.maxIterationCount = maxIter;
    }

    public boolean converged(int iteration, PointVectorValuePair previous, PointVectorValuePair current) {
        if (this.maxIterationCount == -1) {
            int i = iteration;
        } else if (iteration >= this.maxIterationCount) {
            return true;
        }
        double[] p = previous.getValueRef();
        double[] c = current.getValueRef();
        int i2 = 0;
        while (i2 < p.length) {
            double pi = p[i2];
            double ci = c[i2];
            double difference = FastMath.abs(pi - ci);
            if (difference > getRelativeThreshold() * FastMath.max(FastMath.abs(pi), FastMath.abs(ci)) && difference > getAbsoluteThreshold()) {
                return false;
            }
            i2++;
            int i3 = iteration;
        }
        return true;
    }
}

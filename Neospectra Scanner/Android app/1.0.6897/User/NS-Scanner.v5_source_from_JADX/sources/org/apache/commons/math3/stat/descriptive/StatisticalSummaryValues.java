package org.apache.commons.math3.stat.descriptive;

import java.io.Serializable;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;

public class StatisticalSummaryValues implements Serializable, StatisticalSummary {
    private static final long serialVersionUID = -5108854841843722536L;
    private final double max;
    private final double mean;
    private final double min;

    /* renamed from: n */
    private final long f776n;
    private final double sum;
    private final double variance;

    public StatisticalSummaryValues(double mean2, double variance2, long n, double max2, double min2, double sum2) {
        this.mean = mean2;
        this.variance = variance2;
        this.f776n = n;
        this.max = max2;
        this.min = min2;
        this.sum = sum2;
    }

    public double getMax() {
        return this.max;
    }

    public double getMean() {
        return this.mean;
    }

    public double getMin() {
        return this.min;
    }

    public long getN() {
        return this.f776n;
    }

    public double getSum() {
        return this.sum;
    }

    public double getStandardDeviation() {
        return FastMath.sqrt(this.variance);
    }

    public double getVariance() {
        return this.variance;
    }

    public boolean equals(Object object) {
        boolean z = true;
        if (object == this) {
            return true;
        }
        if (!(object instanceof StatisticalSummaryValues)) {
            return false;
        }
        StatisticalSummaryValues stat = (StatisticalSummaryValues) object;
        if (!Precision.equalsIncludingNaN(stat.getMax(), getMax()) || !Precision.equalsIncludingNaN(stat.getMean(), getMean()) || !Precision.equalsIncludingNaN(stat.getMin(), getMin()) || !Precision.equalsIncludingNaN((float) stat.getN(), (float) getN()) || !Precision.equalsIncludingNaN(stat.getSum(), getSum()) || !Precision.equalsIncludingNaN(stat.getVariance(), getVariance())) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return ((((((((((MathUtils.hash(getMax()) + 31) * 31) + MathUtils.hash(getMean())) * 31) + MathUtils.hash(getMin())) * 31) + MathUtils.hash((double) getN())) * 31) + MathUtils.hash(getSum())) * 31) + MathUtils.hash(getVariance());
    }

    public String toString() {
        StringBuffer outBuffer = new StringBuffer();
        String endl = "\n";
        outBuffer.append("StatisticalSummaryValues:");
        outBuffer.append(endl);
        outBuffer.append("n: ");
        outBuffer.append(getN());
        outBuffer.append(endl);
        outBuffer.append("min: ");
        outBuffer.append(getMin());
        outBuffer.append(endl);
        outBuffer.append("max: ");
        outBuffer.append(getMax());
        outBuffer.append(endl);
        outBuffer.append("mean: ");
        outBuffer.append(getMean());
        outBuffer.append(endl);
        outBuffer.append("std dev: ");
        outBuffer.append(getStandardDeviation());
        outBuffer.append(endl);
        outBuffer.append("variance: ");
        outBuffer.append(getVariance());
        outBuffer.append(endl);
        outBuffer.append("sum: ");
        outBuffer.append(getSum());
        outBuffer.append(endl);
        return outBuffer.toString();
    }
}

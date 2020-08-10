package org.apache.commons.math3.stat.descriptive;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.poi.p009ss.usermodel.ShapeTypes;

public class AggregateSummaryStatistics implements StatisticalSummary, Serializable {
    private static final long serialVersionUID = -8207112444016386906L;
    private final SummaryStatistics statistics;
    private final SummaryStatistics statisticsPrototype;

    private static class AggregatingSummaryStatistics extends SummaryStatistics {
        private static final long serialVersionUID = 1;
        private final SummaryStatistics aggregateStatistics;

        AggregatingSummaryStatistics(SummaryStatistics aggregateStatistics2) {
            this.aggregateStatistics = aggregateStatistics2;
        }

        public void addValue(double value) {
            super.addValue(value);
            synchronized (this.aggregateStatistics) {
                this.aggregateStatistics.addValue(value);
            }
        }

        public boolean equals(Object object) {
            boolean z = true;
            if (object == this) {
                return true;
            }
            if (!(object instanceof AggregatingSummaryStatistics)) {
                return false;
            }
            AggregatingSummaryStatistics stat = (AggregatingSummaryStatistics) object;
            if (!super.equals(stat) || !this.aggregateStatistics.equals(stat.aggregateStatistics)) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return super.hashCode() + ShapeTypes.RIBBON_2 + this.aggregateStatistics.hashCode();
        }
    }

    public AggregateSummaryStatistics() {
        this(new SummaryStatistics());
    }

    public AggregateSummaryStatistics(SummaryStatistics prototypeStatistics) throws NullArgumentException {
        this(prototypeStatistics, prototypeStatistics == null ? null : new SummaryStatistics(prototypeStatistics));
    }

    public AggregateSummaryStatistics(SummaryStatistics prototypeStatistics, SummaryStatistics initialStatistics) {
        this.statisticsPrototype = prototypeStatistics == null ? new SummaryStatistics() : prototypeStatistics;
        this.statistics = initialStatistics == null ? new SummaryStatistics() : initialStatistics;
    }

    public double getMax() {
        double max;
        synchronized (this.statistics) {
            max = this.statistics.getMax();
        }
        return max;
    }

    public double getMean() {
        double mean;
        synchronized (this.statistics) {
            mean = this.statistics.getMean();
        }
        return mean;
    }

    public double getMin() {
        double min;
        synchronized (this.statistics) {
            min = this.statistics.getMin();
        }
        return min;
    }

    public long getN() {
        long n;
        synchronized (this.statistics) {
            n = this.statistics.getN();
        }
        return n;
    }

    public double getStandardDeviation() {
        double standardDeviation;
        synchronized (this.statistics) {
            standardDeviation = this.statistics.getStandardDeviation();
        }
        return standardDeviation;
    }

    public double getSum() {
        double sum;
        synchronized (this.statistics) {
            sum = this.statistics.getSum();
        }
        return sum;
    }

    public double getVariance() {
        double variance;
        synchronized (this.statistics) {
            variance = this.statistics.getVariance();
        }
        return variance;
    }

    public double getSumOfLogs() {
        double sumOfLogs;
        synchronized (this.statistics) {
            sumOfLogs = this.statistics.getSumOfLogs();
        }
        return sumOfLogs;
    }

    public double getGeometricMean() {
        double geometricMean;
        synchronized (this.statistics) {
            geometricMean = this.statistics.getGeometricMean();
        }
        return geometricMean;
    }

    public double getSumsq() {
        double sumsq;
        synchronized (this.statistics) {
            sumsq = this.statistics.getSumsq();
        }
        return sumsq;
    }

    public double getSecondMoment() {
        double secondMoment;
        synchronized (this.statistics) {
            secondMoment = this.statistics.getSecondMoment();
        }
        return secondMoment;
    }

    public StatisticalSummary getSummary() {
        StatisticalSummaryValues statisticalSummaryValues;
        synchronized (this.statistics) {
            statisticalSummaryValues = new StatisticalSummaryValues(getMean(), getVariance(), getN(), getMax(), getMin(), getSum());
        }
        return statisticalSummaryValues;
    }

    public SummaryStatistics createContributingStatistics() {
        SummaryStatistics contributingStatistics = new AggregatingSummaryStatistics(this.statistics);
        SummaryStatistics.copy(this.statisticsPrototype, contributingStatistics);
        return contributingStatistics;
    }

    public static StatisticalSummaryValues aggregate(Collection<? extends StatisticalSummary> statistics2) {
        double d;
        if (statistics2 == null) {
            return null;
        }
        Iterator it = statistics2.iterator();
        if (!it.hasNext()) {
            return null;
        }
        StatisticalSummary current = (StatisticalSummary) it.next();
        long n = current.getN();
        double min = current.getMin();
        double sum = current.getSum();
        double max = current.getMax();
        double var = current.getVariance();
        double m2 = (((double) n) - 1.0d) * var;
        double mean = current.getMean();
        while (it.hasNext()) {
            StatisticalSummary current2 = (StatisticalSummary) it.next();
            if (current2.getMin() < min || Double.isNaN(min)) {
                min = current2.getMin();
            }
            if (current2.getMax() > max || Double.isNaN(max)) {
                max = current2.getMax();
            }
            sum += current2.getSum();
            double oldN = (double) n;
            double min2 = min;
            double curN = (double) current2.getN();
            double max2 = max;
            n = (long) (((double) n) + curN);
            double meanDiff = current2.getMean() - mean;
            mean = sum / ((double) n);
            double d2 = oldN;
            m2 = m2 + (current2.getVariance() * (curN - 1.0d)) + ((((meanDiff * meanDiff) * oldN) * curN) / ((double) n));
            it = it;
            min = min2;
            max = max2;
            var = var;
            StatisticalSummary statisticalSummary = current2;
        }
        Iterator it2 = it;
        double d3 = var;
        if (n == 0) {
            d = Double.NaN;
        } else if (n == 1) {
            d = 0.0d;
        } else {
            d = m2 / ((double) (n - 1));
        }
        StatisticalSummaryValues statisticalSummaryValues = new StatisticalSummaryValues(mean, d, n, max, min, sum);
        return statisticalSummaryValues;
    }
}

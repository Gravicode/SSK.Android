package org.apache.commons.math3.stat.descriptive;

import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.descriptive.moment.GeometricMean;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.VectorialCovariance;
import org.apache.commons.math3.stat.descriptive.rank.Max;
import org.apache.commons.math3.stat.descriptive.rank.Min;
import org.apache.commons.math3.stat.descriptive.summary.Sum;
import org.apache.commons.math3.stat.descriptive.summary.SumOfLogs;
import org.apache.commons.math3.stat.descriptive.summary.SumOfSquares;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;

public class MultivariateSummaryStatistics implements StatisticalMultivariateSummary, Serializable {
    private static final long serialVersionUID = 2271900808994826718L;
    private VectorialCovariance covarianceImpl;
    private StorelessUnivariateStatistic[] geoMeanImpl;

    /* renamed from: k */
    private int f774k;
    private StorelessUnivariateStatistic[] maxImpl;
    private StorelessUnivariateStatistic[] meanImpl;
    private StorelessUnivariateStatistic[] minImpl;

    /* renamed from: n */
    private long f775n = 0;
    private StorelessUnivariateStatistic[] sumImpl;
    private StorelessUnivariateStatistic[] sumLogImpl;
    private StorelessUnivariateStatistic[] sumSqImpl;

    public MultivariateSummaryStatistics(int k, boolean isCovarianceBiasCorrected) {
        this.f774k = k;
        this.sumImpl = new StorelessUnivariateStatistic[k];
        this.sumSqImpl = new StorelessUnivariateStatistic[k];
        this.minImpl = new StorelessUnivariateStatistic[k];
        this.maxImpl = new StorelessUnivariateStatistic[k];
        this.sumLogImpl = new StorelessUnivariateStatistic[k];
        this.geoMeanImpl = new StorelessUnivariateStatistic[k];
        this.meanImpl = new StorelessUnivariateStatistic[k];
        for (int i = 0; i < k; i++) {
            this.sumImpl[i] = new Sum();
            this.sumSqImpl[i] = new SumOfSquares();
            this.minImpl[i] = new Min();
            this.maxImpl[i] = new Max();
            this.sumLogImpl[i] = new SumOfLogs();
            this.geoMeanImpl[i] = new GeometricMean();
            this.meanImpl[i] = new Mean();
        }
        this.covarianceImpl = new VectorialCovariance(k, isCovarianceBiasCorrected);
    }

    public void addValue(double[] value) throws DimensionMismatchException {
        checkDimension(value.length);
        for (int i = 0; i < this.f774k; i++) {
            double v = value[i];
            this.sumImpl[i].increment(v);
            this.sumSqImpl[i].increment(v);
            this.minImpl[i].increment(v);
            this.maxImpl[i].increment(v);
            this.sumLogImpl[i].increment(v);
            this.geoMeanImpl[i].increment(v);
            this.meanImpl[i].increment(v);
        }
        this.covarianceImpl.increment(value);
        this.f775n++;
    }

    public int getDimension() {
        return this.f774k;
    }

    public long getN() {
        return this.f775n;
    }

    private double[] getResults(StorelessUnivariateStatistic[] stats) {
        double[] results = new double[stats.length];
        for (int i = 0; i < results.length; i++) {
            results[i] = stats[i].getResult();
        }
        return results;
    }

    public double[] getSum() {
        return getResults(this.sumImpl);
    }

    public double[] getSumSq() {
        return getResults(this.sumSqImpl);
    }

    public double[] getSumLog() {
        return getResults(this.sumLogImpl);
    }

    public double[] getMean() {
        return getResults(this.meanImpl);
    }

    public double[] getStandardDeviation() {
        double[] stdDev = new double[this.f774k];
        if (getN() < 1) {
            Arrays.fill(stdDev, Double.NaN);
        } else if (getN() < 2) {
            Arrays.fill(stdDev, 0.0d);
        } else {
            RealMatrix matrix = this.covarianceImpl.getResult();
            for (int i = 0; i < this.f774k; i++) {
                stdDev[i] = FastMath.sqrt(matrix.getEntry(i, i));
            }
        }
        return stdDev;
    }

    public RealMatrix getCovariance() {
        return this.covarianceImpl.getResult();
    }

    public double[] getMax() {
        return getResults(this.maxImpl);
    }

    public double[] getMin() {
        return getResults(this.minImpl);
    }

    public double[] getGeometricMean() {
        return getResults(this.geoMeanImpl);
    }

    public String toString() {
        String str = ", ";
        String suffix = System.getProperty("line.separator");
        StringBuilder outBuffer = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        sb.append("MultivariateSummaryStatistics:");
        sb.append(suffix);
        outBuffer.append(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("n: ");
        sb2.append(getN());
        sb2.append(suffix);
        outBuffer.append(sb2.toString());
        StringBuilder sb3 = outBuffer;
        String str2 = suffix;
        append(sb3, getMin(), "min: ", ", ", str2);
        append(sb3, getMax(), "max: ", ", ", str2);
        append(sb3, getMean(), "mean: ", ", ", str2);
        append(sb3, getGeometricMean(), "geometric mean: ", ", ", str2);
        append(sb3, getSumSq(), "sum of squares: ", ", ", str2);
        append(sb3, getSumLog(), "sum of logarithms: ", ", ", str2);
        append(sb3, getStandardDeviation(), "standard deviation: ", ", ", str2);
        StringBuilder sb4 = new StringBuilder();
        sb4.append("covariance: ");
        sb4.append(getCovariance().toString());
        sb4.append(suffix);
        outBuffer.append(sb4.toString());
        return outBuffer.toString();
    }

    private void append(StringBuilder buffer, double[] data, String prefix, String separator, String suffix) {
        buffer.append(prefix);
        for (int i = 0; i < data.length; i++) {
            if (i > 0) {
                buffer.append(separator);
            }
            buffer.append(data[i]);
        }
        buffer.append(suffix);
    }

    public void clear() {
        this.f775n = 0;
        for (int i = 0; i < this.f774k; i++) {
            this.minImpl[i].clear();
            this.maxImpl[i].clear();
            this.sumImpl[i].clear();
            this.sumLogImpl[i].clear();
            this.sumSqImpl[i].clear();
            this.geoMeanImpl[i].clear();
            this.meanImpl[i].clear();
        }
        this.covarianceImpl.clear();
    }

    public boolean equals(Object object) {
        boolean z = true;
        if (object == this) {
            return true;
        }
        if (!(object instanceof MultivariateSummaryStatistics)) {
            return false;
        }
        MultivariateSummaryStatistics stat = (MultivariateSummaryStatistics) object;
        if (!MathArrays.equalsIncludingNaN(stat.getGeometricMean(), getGeometricMean()) || !MathArrays.equalsIncludingNaN(stat.getMax(), getMax()) || !MathArrays.equalsIncludingNaN(stat.getMean(), getMean()) || !MathArrays.equalsIncludingNaN(stat.getMin(), getMin()) || !Precision.equalsIncludingNaN((float) stat.getN(), (float) getN()) || !MathArrays.equalsIncludingNaN(stat.getSum(), getSum()) || !MathArrays.equalsIncludingNaN(stat.getSumSq(), getSumSq()) || !MathArrays.equalsIncludingNaN(stat.getSumLog(), getSumLog()) || !stat.getCovariance().equals(getCovariance())) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return ((((((((((((((((((MathUtils.hash(getGeometricMean()) + 31) * 31) + MathUtils.hash(getGeometricMean())) * 31) + MathUtils.hash(getMax())) * 31) + MathUtils.hash(getMean())) * 31) + MathUtils.hash(getMin())) * 31) + MathUtils.hash((double) getN())) * 31) + MathUtils.hash(getSum())) * 31) + MathUtils.hash(getSumSq())) * 31) + MathUtils.hash(getSumLog())) * 31) + getCovariance().hashCode();
    }

    private void setImpl(StorelessUnivariateStatistic[] newImpl, StorelessUnivariateStatistic[] oldImpl) throws MathIllegalStateException, DimensionMismatchException {
        checkEmpty();
        checkDimension(newImpl.length);
        System.arraycopy(newImpl, 0, oldImpl, 0, newImpl.length);
    }

    public StorelessUnivariateStatistic[] getSumImpl() {
        return (StorelessUnivariateStatistic[]) this.sumImpl.clone();
    }

    public void setSumImpl(StorelessUnivariateStatistic[] sumImpl2) throws MathIllegalStateException, DimensionMismatchException {
        setImpl(sumImpl2, this.sumImpl);
    }

    public StorelessUnivariateStatistic[] getSumsqImpl() {
        return (StorelessUnivariateStatistic[]) this.sumSqImpl.clone();
    }

    public void setSumsqImpl(StorelessUnivariateStatistic[] sumsqImpl) throws MathIllegalStateException, DimensionMismatchException {
        setImpl(sumsqImpl, this.sumSqImpl);
    }

    public StorelessUnivariateStatistic[] getMinImpl() {
        return (StorelessUnivariateStatistic[]) this.minImpl.clone();
    }

    public void setMinImpl(StorelessUnivariateStatistic[] minImpl2) throws MathIllegalStateException, DimensionMismatchException {
        setImpl(minImpl2, this.minImpl);
    }

    public StorelessUnivariateStatistic[] getMaxImpl() {
        return (StorelessUnivariateStatistic[]) this.maxImpl.clone();
    }

    public void setMaxImpl(StorelessUnivariateStatistic[] maxImpl2) throws MathIllegalStateException, DimensionMismatchException {
        setImpl(maxImpl2, this.maxImpl);
    }

    public StorelessUnivariateStatistic[] getSumLogImpl() {
        return (StorelessUnivariateStatistic[]) this.sumLogImpl.clone();
    }

    public void setSumLogImpl(StorelessUnivariateStatistic[] sumLogImpl2) throws MathIllegalStateException, DimensionMismatchException {
        setImpl(sumLogImpl2, this.sumLogImpl);
    }

    public StorelessUnivariateStatistic[] getGeoMeanImpl() {
        return (StorelessUnivariateStatistic[]) this.geoMeanImpl.clone();
    }

    public void setGeoMeanImpl(StorelessUnivariateStatistic[] geoMeanImpl2) throws MathIllegalStateException, DimensionMismatchException {
        setImpl(geoMeanImpl2, this.geoMeanImpl);
    }

    public StorelessUnivariateStatistic[] getMeanImpl() {
        return (StorelessUnivariateStatistic[]) this.meanImpl.clone();
    }

    public void setMeanImpl(StorelessUnivariateStatistic[] meanImpl2) throws MathIllegalStateException, DimensionMismatchException {
        setImpl(meanImpl2, this.meanImpl);
    }

    private void checkEmpty() throws MathIllegalStateException {
        if (this.f775n > 0) {
            throw new MathIllegalStateException(LocalizedFormats.VALUES_ADDED_BEFORE_CONFIGURING_STATISTIC, Long.valueOf(this.f775n));
        }
    }

    private void checkDimension(int dimension) throws DimensionMismatchException {
        if (dimension != this.f774k) {
            throw new DimensionMismatchException(dimension, this.f774k);
        }
    }
}

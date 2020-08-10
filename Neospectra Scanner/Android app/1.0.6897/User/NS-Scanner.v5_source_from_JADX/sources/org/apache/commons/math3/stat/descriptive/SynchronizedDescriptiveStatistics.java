package org.apache.commons.math3.stat.descriptive;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.MathUtils;

public class SynchronizedDescriptiveStatistics extends DescriptiveStatistics {
    private static final long serialVersionUID = 1;

    public SynchronizedDescriptiveStatistics() {
        this(-1);
    }

    public SynchronizedDescriptiveStatistics(int window) throws MathIllegalArgumentException {
        super(window);
    }

    public SynchronizedDescriptiveStatistics(SynchronizedDescriptiveStatistics original) throws NullArgumentException {
        copy(original, this);
    }

    public synchronized void addValue(double v) {
        super.addValue(v);
    }

    public synchronized double apply(UnivariateStatistic stat) {
        return super.apply(stat);
    }

    public synchronized void clear() {
        super.clear();
    }

    public synchronized double getElement(int index) {
        return super.getElement(index);
    }

    public synchronized long getN() {
        return super.getN();
    }

    public synchronized double getStandardDeviation() {
        return super.getStandardDeviation();
    }

    public synchronized double getQuadraticMean() {
        return super.getQuadraticMean();
    }

    public synchronized double[] getValues() {
        return super.getValues();
    }

    public synchronized int getWindowSize() {
        return super.getWindowSize();
    }

    public synchronized void setWindowSize(int windowSize) throws MathIllegalArgumentException {
        super.setWindowSize(windowSize);
    }

    public synchronized String toString() {
        return super.toString();
    }

    public synchronized SynchronizedDescriptiveStatistics copy() {
        SynchronizedDescriptiveStatistics result;
        result = new SynchronizedDescriptiveStatistics();
        copy(this, result);
        return result;
    }

    public static void copy(SynchronizedDescriptiveStatistics source, SynchronizedDescriptiveStatistics dest) throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        synchronized (source) {
            synchronized (dest) {
                DescriptiveStatistics.copy(source, dest);
            }
        }
    }
}

package org.apache.commons.math3.stat;

import java.util.List;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.UnivariateStatistic;
import org.apache.commons.math3.stat.descriptive.moment.GeometricMean;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.stat.descriptive.rank.Max;
import org.apache.commons.math3.stat.descriptive.rank.Min;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.apache.commons.math3.stat.descriptive.summary.Sum;
import org.apache.commons.math3.stat.descriptive.summary.SumOfLogs;
import org.apache.commons.math3.stat.descriptive.summary.SumOfSquares;

public final class StatUtils {
    private static final GeometricMean GEOMETRIC_MEAN = new GeometricMean();
    private static final UnivariateStatistic MAX = new Max();
    private static final UnivariateStatistic MEAN = new Mean();
    private static final UnivariateStatistic MIN = new Min();
    private static final Percentile PERCENTILE = new Percentile();
    private static final UnivariateStatistic PRODUCT = new Product();
    private static final UnivariateStatistic SUM = new Sum();
    private static final UnivariateStatistic SUM_OF_LOGS = new SumOfLogs();
    private static final UnivariateStatistic SUM_OF_SQUARES = new SumOfSquares();
    private static final Variance VARIANCE = new Variance();

    private StatUtils() {
    }

    public static double sum(double[] values) throws MathIllegalArgumentException {
        return SUM.evaluate(values);
    }

    public static double sum(double[] values, int begin, int length) throws MathIllegalArgumentException {
        return SUM.evaluate(values, begin, length);
    }

    public static double sumSq(double[] values) throws MathIllegalArgumentException {
        return SUM_OF_SQUARES.evaluate(values);
    }

    public static double sumSq(double[] values, int begin, int length) throws MathIllegalArgumentException {
        return SUM_OF_SQUARES.evaluate(values, begin, length);
    }

    public static double product(double[] values) throws MathIllegalArgumentException {
        return PRODUCT.evaluate(values);
    }

    public static double product(double[] values, int begin, int length) throws MathIllegalArgumentException {
        return PRODUCT.evaluate(values, begin, length);
    }

    public static double sumLog(double[] values) throws MathIllegalArgumentException {
        return SUM_OF_LOGS.evaluate(values);
    }

    public static double sumLog(double[] values, int begin, int length) throws MathIllegalArgumentException {
        return SUM_OF_LOGS.evaluate(values, begin, length);
    }

    public static double mean(double[] values) throws MathIllegalArgumentException {
        return MEAN.evaluate(values);
    }

    public static double mean(double[] values, int begin, int length) throws MathIllegalArgumentException {
        return MEAN.evaluate(values, begin, length);
    }

    public static double geometricMean(double[] values) throws MathIllegalArgumentException {
        return GEOMETRIC_MEAN.evaluate(values);
    }

    public static double geometricMean(double[] values, int begin, int length) throws MathIllegalArgumentException {
        return GEOMETRIC_MEAN.evaluate(values, begin, length);
    }

    public static double variance(double[] values) throws MathIllegalArgumentException {
        return VARIANCE.evaluate(values);
    }

    public static double variance(double[] values, int begin, int length) throws MathIllegalArgumentException {
        return VARIANCE.evaluate(values, begin, length);
    }

    public static double variance(double[] values, double mean, int begin, int length) throws MathIllegalArgumentException {
        return VARIANCE.evaluate(values, mean, begin, length);
    }

    public static double variance(double[] values, double mean) throws MathIllegalArgumentException {
        return VARIANCE.evaluate(values, mean);
    }

    public static double populationVariance(double[] values) throws MathIllegalArgumentException {
        return new Variance(false).evaluate(values);
    }

    public static double populationVariance(double[] values, int begin, int length) throws MathIllegalArgumentException {
        return new Variance(false).evaluate(values, begin, length);
    }

    public static double populationVariance(double[] values, double mean, int begin, int length) throws MathIllegalArgumentException {
        return new Variance(false).evaluate(values, mean, begin, length);
    }

    public static double populationVariance(double[] values, double mean) throws MathIllegalArgumentException {
        return new Variance(false).evaluate(values, mean);
    }

    public static double max(double[] values) throws MathIllegalArgumentException {
        return MAX.evaluate(values);
    }

    public static double max(double[] values, int begin, int length) throws MathIllegalArgumentException {
        return MAX.evaluate(values, begin, length);
    }

    public static double min(double[] values) throws MathIllegalArgumentException {
        return MIN.evaluate(values);
    }

    public static double min(double[] values, int begin, int length) throws MathIllegalArgumentException {
        return MIN.evaluate(values, begin, length);
    }

    public static double percentile(double[] values, double p) throws MathIllegalArgumentException {
        return PERCENTILE.evaluate(values, p);
    }

    public static double percentile(double[] values, int begin, int length, double p) throws MathIllegalArgumentException {
        return PERCENTILE.evaluate(values, begin, length, p);
    }

    public static double sumDifference(double[] sample1, double[] sample2) throws DimensionMismatchException, NoDataException {
        int n = sample1.length;
        if (n != sample2.length) {
            throw new DimensionMismatchException(n, sample2.length);
        } else if (n <= 0) {
            throw new NoDataException(LocalizedFormats.INSUFFICIENT_DIMENSION);
        } else {
            double result = 0.0d;
            for (int i = 0; i < n; i++) {
                result += sample1[i] - sample2[i];
            }
            return result;
        }
    }

    public static double meanDifference(double[] sample1, double[] sample2) throws DimensionMismatchException, NoDataException {
        return sumDifference(sample1, sample2) / ((double) sample1.length);
    }

    public static double varianceDifference(double[] sample1, double[] sample2, double meanDifference) throws DimensionMismatchException, NumberIsTooSmallException {
        double sum1 = 0.0d;
        double sum2 = 0.0d;
        int n = sample1.length;
        if (n != sample2.length) {
            throw new DimensionMismatchException(n, sample2.length);
        } else if (n < 2) {
            throw new NumberIsTooSmallException(Integer.valueOf(n), Integer.valueOf(2), true);
        } else {
            for (int i = 0; i < n; i++) {
                double diff = sample1[i] - sample2[i];
                sum1 += (diff - meanDifference) * (diff - meanDifference);
                sum2 += diff - meanDifference;
            }
            return (sum1 - ((sum2 * sum2) / ((double) n))) / ((double) (n - 1));
        }
    }

    public static double[] normalize(double[] sample) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (double addValue : sample) {
            stats.addValue(addValue);
        }
        double mean = stats.getMean();
        double standardDeviation = stats.getStandardDeviation();
        double[] standardizedSample = new double[sample.length];
        for (int i = 0; i < sample.length; i++) {
            standardizedSample[i] = (sample[i] - mean) / standardDeviation;
        }
        return standardizedSample;
    }

    public static double[] mode(double[] sample) throws MathIllegalArgumentException {
        if (sample != null) {
            return getMode(sample, 0, sample.length);
        }
        throw new NullArgumentException(LocalizedFormats.INPUT_ARRAY, new Object[0]);
    }

    public static double[] mode(double[] sample, int begin, int length) {
        if (sample == null) {
            throw new NullArgumentException(LocalizedFormats.INPUT_ARRAY, new Object[0]);
        } else if (begin < 0) {
            throw new NotPositiveException(LocalizedFormats.START_POSITION, Integer.valueOf(begin));
        } else if (length >= 0) {
            return getMode(sample, begin, length);
        } else {
            throw new NotPositiveException(LocalizedFormats.LENGTH, Integer.valueOf(length));
        }
    }

    private static double[] getMode(double[] values, int begin, int length) {
        Frequency freq = new Frequency();
        for (int i = begin; i < begin + length; i++) {
            double value = values[i];
            if (!Double.isNaN(value)) {
                freq.addValue((Comparable<?>) Double.valueOf(value));
            }
        }
        List<Comparable<?>> list = freq.getMode();
        double[] modes = new double[list.size()];
        int i2 = 0;
        for (Comparable<?> c : list) {
            int i3 = i2 + 1;
            modes[i2] = ((Double) c).doubleValue();
            i2 = i3;
        }
        return modes;
    }
}

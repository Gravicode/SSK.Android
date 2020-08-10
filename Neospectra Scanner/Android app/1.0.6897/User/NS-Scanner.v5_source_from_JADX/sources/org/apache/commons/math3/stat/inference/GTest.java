package org.apache.commons.math3.stat.inference;

import java.lang.reflect.Array;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

public class GTest {
    /* renamed from: g */
    public double mo19571g(double[] expected, long[] observed) throws NotPositiveException, NotStrictlyPositiveException, DimensionMismatchException {
        double d;
        double d2;
        double[] dArr = expected;
        long[] jArr = observed;
        if (dArr.length < 2) {
            throw new DimensionMismatchException(dArr.length, 2);
        } else if (dArr.length != jArr.length) {
            throw new DimensionMismatchException(dArr.length, jArr.length);
        } else {
            MathArrays.checkPositive(expected);
            MathArrays.checkNonNegative(observed);
            int i = 0;
            double sumObserved = 0.0d;
            double sumExpected = 0.0d;
            for (int i2 = 0; i2 < jArr.length; i2++) {
                sumExpected += dArr[i2];
                sumObserved += (double) jArr[i2];
            }
            double ratio = 1.0d;
            boolean rescale = false;
            if (FastMath.abs(sumExpected - sumObserved) > 1.0E-5d) {
                ratio = sumObserved / sumExpected;
                rescale = true;
            }
            double sum = 0.0d;
            while (true) {
                int i3 = i;
                if (i3 < jArr.length) {
                    if (rescale) {
                        d = (double) jArr[i3];
                        d2 = dArr[i3] * ratio;
                    } else {
                        d = (double) jArr[i3];
                        d2 = dArr[i3];
                    }
                    sum += ((double) jArr[i3]) * FastMath.log(d / d2);
                    i = i3 + 1;
                    sumExpected = sumExpected;
                    rescale = rescale;
                } else {
                    double d3 = sumExpected;
                    return 2.0d * sum;
                }
            }
        }
    }

    public double gTest(double[] expected, long[] observed) throws NotPositiveException, NotStrictlyPositiveException, DimensionMismatchException, MaxCountExceededException {
        return 1.0d - new ChiSquaredDistribution((RandomGenerator) null, ((double) expected.length) - 1.0d).cumulativeProbability(mo19571g(expected, observed));
    }

    public double gTestIntrinsic(double[] expected, long[] observed) throws NotPositiveException, NotStrictlyPositiveException, DimensionMismatchException, MaxCountExceededException {
        return 1.0d - new ChiSquaredDistribution((RandomGenerator) null, ((double) expected.length) - 2.0d).cumulativeProbability(mo19571g(expected, observed));
    }

    public boolean gTest(double[] expected, long[] observed, double alpha) throws NotPositiveException, NotStrictlyPositiveException, DimensionMismatchException, OutOfRangeException, MaxCountExceededException {
        if (alpha <= 0.0d || alpha > 0.5d) {
            throw new OutOfRangeException(LocalizedFormats.OUT_OF_BOUND_SIGNIFICANCE_LEVEL, Double.valueOf(alpha), Integer.valueOf(0), Double.valueOf(0.5d));
        } else if (gTest(expected, observed) < alpha) {
            return true;
        } else {
            return false;
        }
    }

    private double entropy(long[][] k) {
        double sum_k = 0.0d;
        for (int i = 0; i < k.length; i++) {
            for (long j : k[i]) {
                sum_k += (double) j;
            }
        }
        double h = 0.0d;
        int i2 = 0;
        while (i2 < k.length) {
            double h2 = h;
            for (int j2 = 0; j2 < k[i2].length; j2++) {
                if (k[i2][j2] != 0) {
                    double p_ij = ((double) k[i2][j2]) / sum_k;
                    h2 += FastMath.log(p_ij) * p_ij;
                }
            }
            i2++;
            h = h2;
        }
        return -h;
    }

    private double entropy(long[] k) {
        double h = 0.0d;
        int i = 0;
        double sum_k = 0.0d;
        for (long j : k) {
            sum_k += (double) j;
        }
        while (true) {
            int i2 = i;
            if (i2 >= k.length) {
                return -h;
            }
            if (k[i2] != 0) {
                double p_i = ((double) k[i2]) / sum_k;
                h += FastMath.log(p_i) * p_i;
            }
            i = i2 + 1;
        }
    }

    public double gDataSetsComparison(long[] observed1, long[] observed2) throws DimensionMismatchException, NotPositiveException, ZeroException {
        long[] jArr = observed1;
        long[] jArr2 = observed2;
        if (jArr.length < 2) {
            throw new DimensionMismatchException(jArr.length, 2);
        } else if (jArr.length != jArr2.length) {
            throw new DimensionMismatchException(jArr.length, jArr2.length);
        } else {
            MathArrays.checkNonNegative(observed1);
            MathArrays.checkNonNegative(observed2);
            long[] collSums = new long[jArr.length];
            long[][] k = (long[][]) Array.newInstance(long.class, new int[]{2, jArr.length});
            long countSum2 = 0;
            long countSum1 = 0;
            for (int i = 0; i < jArr.length; i++) {
                if (jArr[i] == 0 && jArr2[i] == 0) {
                    throw new ZeroException(LocalizedFormats.OBSERVED_COUNTS_BOTTH_ZERO_FOR_ENTRY, Integer.valueOf(i));
                }
                countSum1 += jArr[i];
                countSum2 += jArr2[i];
                collSums[i] = jArr[i] + jArr2[i];
                k[0][i] = jArr[i];
                k[1][i] = jArr2[i];
            }
            if (countSum1 == 0 || countSum2 == 0) {
                throw new ZeroException();
            }
            return 2.0d * (((double) countSum1) + ((double) countSum2)) * ((entropy(new long[]{countSum1, countSum2}) + entropy(collSums)) - entropy(k));
        }
    }

    public double rootLogLikelihoodRatio(long k11, long k12, long k21, long k22) {
        long j = k11;
        long j2 = k21;
        double llr = gDataSetsComparison(new long[]{j, k12}, new long[]{j2, k22});
        double sqrt = FastMath.sqrt(llr);
        double d = llr;
        if (((double) j) / ((double) (j + k12)) < ((double) j2) / ((double) (j2 + k22))) {
            return -sqrt;
        }
        return sqrt;
    }

    public double gTestDataSetsComparison(long[] observed1, long[] observed2) throws DimensionMismatchException, NotPositiveException, ZeroException, MaxCountExceededException {
        return 1.0d - new ChiSquaredDistribution((RandomGenerator) null, ((double) observed1.length) - 1.0d).cumulativeProbability(gDataSetsComparison(observed1, observed2));
    }

    public boolean gTestDataSetsComparison(long[] observed1, long[] observed2, double alpha) throws DimensionMismatchException, NotPositiveException, ZeroException, OutOfRangeException, MaxCountExceededException {
        if (alpha <= 0.0d || alpha > 0.5d) {
            throw new OutOfRangeException(LocalizedFormats.OUT_OF_BOUND_SIGNIFICANCE_LEVEL, Double.valueOf(alpha), Integer.valueOf(0), Double.valueOf(0.5d));
        } else if (gTestDataSetsComparison(observed1, observed2) < alpha) {
            return true;
        } else {
            return false;
        }
    }
}

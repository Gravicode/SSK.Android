package org.apache.commons.math3.stat.inference;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

public class ChiSquareTest {
    public double chiSquare(double[] expected, long[] observed) throws NotPositiveException, NotStrictlyPositiveException, DimensionMismatchException {
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
            double sumSq = 0.0d;
            while (true) {
                int i3 = i;
                if (i3 >= jArr.length) {
                    return sumSq;
                }
                if (rescale) {
                    double dev = ((double) jArr[i3]) - (dArr[i3] * ratio);
                    d2 = dev * dev;
                    d = dArr[i3] * ratio;
                } else {
                    double dev2 = ((double) jArr[i3]) - dArr[i3];
                    d2 = dev2 * dev2;
                    d = dArr[i3];
                }
                sumSq += d2 / d;
                i = i3 + 1;
            }
        }
    }

    public double chiSquareTest(double[] expected, long[] observed) throws NotPositiveException, NotStrictlyPositiveException, DimensionMismatchException, MaxCountExceededException {
        return 1.0d - new ChiSquaredDistribution((RandomGenerator) null, ((double) expected.length) - 1.0d).cumulativeProbability(chiSquare(expected, observed));
    }

    public boolean chiSquareTest(double[] expected, long[] observed, double alpha) throws NotPositiveException, NotStrictlyPositiveException, DimensionMismatchException, OutOfRangeException, MaxCountExceededException {
        if (alpha <= 0.0d || alpha > 0.5d) {
            throw new OutOfRangeException(LocalizedFormats.OUT_OF_BOUND_SIGNIFICANCE_LEVEL, Double.valueOf(alpha), Integer.valueOf(0), Double.valueOf(0.5d));
        } else if (chiSquareTest(expected, observed) < alpha) {
            return true;
        } else {
            return false;
        }
    }

    public double chiSquare(long[][] counts) throws NullArgumentException, NotPositiveException, DimensionMismatchException {
        long[][] jArr = counts;
        checkArray(counts);
        int nRows = jArr.length;
        int nCols = jArr[0].length;
        double[] rowSum = new double[nRows];
        double[] colSum = new double[nCols];
        double total = 0.0d;
        int row = 0;
        while (row < nRows) {
            double total2 = total;
            for (int col = 0; col < nCols; col++) {
                rowSum[row] = rowSum[row] + ((double) jArr[row][col]);
                colSum[col] = colSum[col] + ((double) jArr[row][col]);
                total2 += (double) jArr[row][col];
            }
            row++;
            total = total2;
        }
        double sumSq = 0.0d;
        int row2 = 0;
        while (row2 < nRows) {
            double sumSq2 = sumSq;
            int col2 = 0;
            while (col2 < nCols) {
                double expected = (rowSum[row2] * colSum[col2]) / total;
                sumSq2 += ((((double) jArr[row2][col2]) - expected) * (((double) jArr[row2][col2]) - expected)) / expected;
                col2++;
                nCols = nCols;
                nRows = nRows;
                jArr = counts;
            }
            int i = nCols;
            row2++;
            sumSq = sumSq2;
            jArr = counts;
        }
        int i2 = nCols;
        return sumSq;
    }

    public double chiSquareTest(long[][] counts) throws NullArgumentException, DimensionMismatchException, NotPositiveException, MaxCountExceededException {
        checkArray(counts);
        return 1.0d - new ChiSquaredDistribution((((double) counts.length) - 1.0d) * (((double) counts[0].length) - 1.0d)).cumulativeProbability(chiSquare(counts));
    }

    public boolean chiSquareTest(long[][] counts, double alpha) throws NullArgumentException, DimensionMismatchException, NotPositiveException, OutOfRangeException, MaxCountExceededException {
        if (alpha <= 0.0d || alpha > 0.5d) {
            throw new OutOfRangeException(LocalizedFormats.OUT_OF_BOUND_SIGNIFICANCE_LEVEL, Double.valueOf(alpha), Integer.valueOf(0), Double.valueOf(0.5d));
        } else if (chiSquareTest(counts) < alpha) {
            return true;
        } else {
            return false;
        }
    }

    public double chiSquareDataSetsComparison(long[] observed1, long[] observed2) throws DimensionMismatchException, NotPositiveException, ZeroException {
        double dev;
        long[] jArr = observed1;
        long[] jArr2 = observed2;
        if (jArr.length < 2) {
            throw new DimensionMismatchException(jArr.length, 2);
        } else if (jArr.length != jArr2.length) {
            throw new DimensionMismatchException(jArr.length, jArr2.length);
        } else {
            MathArrays.checkNonNegative(observed1);
            MathArrays.checkNonNegative(observed2);
            double weight = 0.0d;
            char c = 0;
            long countSum2 = 0;
            long countSum1 = 0;
            for (int i = 0; i < jArr.length; i++) {
                countSum1 += jArr[i];
                countSum2 += jArr2[i];
            }
            long j = 0;
            if (countSum1 == 0) {
            } else if (countSum2 == 0) {
                long j2 = countSum2;
            } else {
                boolean unequalCounts = countSum1 != countSum2;
                if (unequalCounts) {
                    weight = FastMath.sqrt(((double) countSum1) / ((double) countSum2));
                }
                double sumSq = 0.0d;
                int i2 = 0;
                while (i2 < jArr.length) {
                    if (jArr[i2] == j && jArr2[i2] == j) {
                        LocalizedFormats localizedFormats = LocalizedFormats.OBSERVED_COUNTS_BOTTH_ZERO_FOR_ENTRY;
                        Object[] objArr = new Object[1];
                        objArr[c] = Integer.valueOf(i2);
                        throw new ZeroException(localizedFormats, objArr);
                    }
                    long countSum22 = countSum2;
                    double obs1 = (double) jArr[i2];
                    double obs2 = (double) jArr2[i2];
                    if (unequalCounts) {
                        dev = (obs1 / weight) - (obs2 * weight);
                    } else {
                        dev = obs1 - obs2;
                    }
                    sumSq += (dev * dev) / (obs1 + obs2);
                    i2++;
                    double d = obs1;
                    double d2 = obs2;
                    countSum2 = countSum22;
                    c = 0;
                    j = 0;
                }
                return sumSq;
            }
            throw new ZeroException();
        }
    }

    public double chiSquareTestDataSetsComparison(long[] observed1, long[] observed2) throws DimensionMismatchException, NotPositiveException, ZeroException, MaxCountExceededException {
        return 1.0d - new ChiSquaredDistribution((RandomGenerator) null, ((double) observed1.length) - 1.0d).cumulativeProbability(chiSquareDataSetsComparison(observed1, observed2));
    }

    public boolean chiSquareTestDataSetsComparison(long[] observed1, long[] observed2, double alpha) throws DimensionMismatchException, NotPositiveException, ZeroException, OutOfRangeException, MaxCountExceededException {
        if (alpha <= 0.0d || alpha > 0.5d) {
            throw new OutOfRangeException(LocalizedFormats.OUT_OF_BOUND_SIGNIFICANCE_LEVEL, Double.valueOf(alpha), Integer.valueOf(0), Double.valueOf(0.5d));
        } else if (chiSquareTestDataSetsComparison(observed1, observed2) < alpha) {
            return true;
        } else {
            return false;
        }
    }

    private void checkArray(long[][] in) throws NullArgumentException, DimensionMismatchException, NotPositiveException {
        if (in.length < 2) {
            throw new DimensionMismatchException(in.length, 2);
        } else if (in[0].length < 2) {
            throw new DimensionMismatchException(in[0].length, 2);
        } else {
            MathArrays.checkRectangular(in);
            MathArrays.checkNonNegative(in);
        }
    }
}

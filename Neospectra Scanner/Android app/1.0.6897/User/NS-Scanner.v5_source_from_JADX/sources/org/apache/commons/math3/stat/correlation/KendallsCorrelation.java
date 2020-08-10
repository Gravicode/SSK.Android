package org.apache.commons.math3.stat.correlation;

import java.util.Arrays;
import java.util.Comparator;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Pair;

public class KendallsCorrelation {
    private final RealMatrix correlationMatrix;

    public KendallsCorrelation() {
        this.correlationMatrix = null;
    }

    public KendallsCorrelation(double[][] data) {
        this(MatrixUtils.createRealMatrix(data));
    }

    public KendallsCorrelation(RealMatrix matrix) {
        this.correlationMatrix = computeCorrelationMatrix(matrix);
    }

    public RealMatrix getCorrelationMatrix() {
        return this.correlationMatrix;
    }

    public RealMatrix computeCorrelationMatrix(RealMatrix matrix) {
        int nVars = matrix.getColumnDimension();
        RealMatrix outMatrix = new BlockRealMatrix(nVars, nVars);
        for (int i = 0; i < nVars; i++) {
            for (int j = 0; j < i; j++) {
                double corr = correlation(matrix.getColumn(i), matrix.getColumn(j));
                outMatrix.setEntry(i, j, corr);
                outMatrix.setEntry(j, i, corr);
            }
            outMatrix.setEntry(i, i, 1.0d);
        }
        return outMatrix;
    }

    public RealMatrix computeCorrelationMatrix(double[][] matrix) {
        return computeCorrelationMatrix((RealMatrix) new BlockRealMatrix(matrix));
    }

    public double correlation(double[] xArray, double[] yArray) throws DimensionMismatchException {
        long consecutiveXTies;
        int jEnd;
        int offset;
        double[] dArr = xArray;
        double[] dArr2 = yArray;
        if (dArr.length != dArr2.length) {
            throw new DimensionMismatchException(dArr.length, dArr2.length);
        }
        int n = dArr.length;
        long numPairs = sum((long) (n - 1));
        Pair[] pairArr = new Pair[n];
        for (int i = 0; i < n; i++) {
            pairArr[i] = new Pair(Double.valueOf(dArr[i]), Double.valueOf(dArr2[i]));
        }
        Arrays.sort(pairArr, new Comparator<Pair<Double, Double>>() {
            public int compare(Pair<Double, Double> pair1, Pair<Double, Double> pair2) {
                int compareFirst = ((Double) pair1.getFirst()).compareTo((Double) pair2.getFirst());
                return compareFirst != 0 ? compareFirst : ((Double) pair1.getSecond()).compareTo((Double) pair2.getSecond());
            }
        });
        long consecutiveXTies2 = 1;
        int i2 = 1;
        long consecutiveXYTies = 1;
        long tiedXYPairs = 0;
        long tiedXPairs = 0;
        Pair pair = pairArr[0];
        int i3 = 1;
        while (i3 < n) {
            Pair pair2 = pairArr[i3];
            if (((Double) pair2.getFirst()).equals(pair.getFirst())) {
                consecutiveXTies2++;
                if (((Double) pair2.getSecond()).equals(pair.getSecond())) {
                    consecutiveXYTies++;
                } else {
                    tiedXYPairs += sum(consecutiveXYTies - 1);
                    consecutiveXYTies = 1;
                }
            } else {
                tiedXPairs += sum(consecutiveXTies2 - 1);
                tiedXYPairs += sum(consecutiveXYTies - 1);
                consecutiveXYTies = 1;
                consecutiveXTies2 = 1;
            }
            pair = pair2;
            i3++;
            double[] dArr3 = xArray;
            double[] dArr4 = yArray;
        }
        long tiedXPairs2 = tiedXPairs + sum(consecutiveXTies2 - 1);
        long tiedXYPairs2 = tiedXYPairs + sum(consecutiveXYTies - 1);
        Pair[] pairArr2 = new Pair[n];
        long swaps = 0;
        int segmentSize = 1;
        while (segmentSize < n) {
            long swaps2 = swaps;
            int offset2 = 0;
            while (offset2 < n) {
                int i4 = offset2;
                int iEnd = FastMath.min(i4 + segmentSize, n);
                int j = iEnd;
                int i5 = i4;
                int jEnd2 = FastMath.min(j + segmentSize, n);
                Pair pair3 = pair;
                int j2 = j;
                int i6 = i5;
                int copyLocation = offset2;
                while (true) {
                    if (i6 >= iEnd && j2 >= jEnd2) {
                        break;
                    }
                    if (i6 >= iEnd) {
                        offset = offset2;
                        jEnd = jEnd2;
                        pairArr2[copyLocation] = pairArr[j2];
                        j2++;
                    } else if (j2 < jEnd2) {
                        offset = offset2;
                        jEnd = jEnd2;
                        if (((Double) pairArr[i6].getSecond()).compareTo((Double) pairArr[j2].getSecond()) <= 0) {
                            pairArr2[copyLocation] = pairArr[i6];
                            i6++;
                        } else {
                            pairArr2[copyLocation] = pairArr[j2];
                            j2++;
                            swaps2 += (long) (iEnd - i6);
                        }
                    } else {
                        offset = offset2;
                        jEnd = jEnd2;
                        pairArr2[copyLocation] = pairArr[i6];
                        i6++;
                    }
                    copyLocation++;
                    offset2 = offset;
                    jEnd2 = jEnd;
                }
                offset2 += segmentSize * 2;
                pair = pair3;
            }
            Pair pair4 = pair;
            Pair[] pairArr3 = pairArr;
            pairArr = pairArr2;
            pairArr2 = pairArr3;
            segmentSize <<= 1;
            swaps = swaps2;
        }
        Pair pair5 = pair;
        long tiedYPairs = 0;
        long consecutiveYTies = 1;
        Pair pair6 = pairArr[0];
        while (true) {
            int i7 = i2;
            if (i7 < n) {
                Pair[] pairArr4 = pairArr2;
                Pair pair7 = pairArr[i7];
                int n2 = n;
                Pair[] pairArr5 = pairArr;
                if (((Double) pair7.getSecond()).equals(pair6.getSecond())) {
                    consecutiveYTies++;
                    consecutiveXTies = consecutiveXTies2;
                } else {
                    consecutiveXTies = consecutiveXTies2;
                    tiedYPairs += sum(consecutiveYTies - 1);
                    consecutiveYTies = 1;
                }
                pair6 = pair7;
                i2 = i7 + 1;
                pairArr2 = pairArr4;
                n = n2;
                pairArr = pairArr5;
                consecutiveXTies2 = consecutiveXTies;
            } else {
                Pair[] pairArr6 = pairArr2;
                int i8 = n;
                Pair[] pairArr7 = pairArr;
                long j3 = consecutiveXTies2;
                long tiedYPairs2 = tiedYPairs + sum(consecutiveYTies - 1);
                long j4 = swaps;
                return ((double) ((((numPairs - tiedXPairs2) - tiedYPairs2) + tiedXYPairs2) - (2 * swaps))) / FastMath.sqrt(((double) (numPairs - tiedXPairs2)) * ((double) (numPairs - tiedYPairs2)));
            }
        }
    }

    private static long sum(long n) {
        return ((1 + n) * n) / 2;
    }
}

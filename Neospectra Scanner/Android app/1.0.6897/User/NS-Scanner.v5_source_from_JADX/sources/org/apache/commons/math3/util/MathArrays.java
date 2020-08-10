package org.apache.commons.math3.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NotANumberException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

public class MathArrays {

    public interface Function {
        double evaluate(double[] dArr);

        double evaluate(double[] dArr, int i, int i2);
    }

    public enum OrderDirection {
        INCREASING,
        DECREASING
    }

    private static class PairDoubleInteger {
        private final double key;
        private final int value;

        PairDoubleInteger(double key2, int value2) {
            this.key = key2;
            this.value = value2;
        }

        public double getKey() {
            return this.key;
        }

        public int getValue() {
            return this.value;
        }
    }

    public enum Position {
        HEAD,
        TAIL
    }

    private MathArrays() {
    }

    public static double[] scale(double val, double[] arr) {
        double[] newArr = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            newArr[i] = arr[i] * val;
        }
        return newArr;
    }

    public static void scaleInPlace(double val, double[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i] * val;
        }
    }

    public static double[] ebeAdd(double[] a, double[] b) throws DimensionMismatchException {
        checkEqualLength(a, b);
        double[] result = (double[]) a.clone();
        for (int i = 0; i < a.length; i++) {
            result[i] = result[i] + b[i];
        }
        return result;
    }

    public static double[] ebeSubtract(double[] a, double[] b) throws DimensionMismatchException {
        checkEqualLength(a, b);
        double[] result = (double[]) a.clone();
        for (int i = 0; i < a.length; i++) {
            result[i] = result[i] - b[i];
        }
        return result;
    }

    public static double[] ebeMultiply(double[] a, double[] b) throws DimensionMismatchException {
        checkEqualLength(a, b);
        double[] result = (double[]) a.clone();
        for (int i = 0; i < a.length; i++) {
            result[i] = result[i] * b[i];
        }
        return result;
    }

    public static double[] ebeDivide(double[] a, double[] b) throws DimensionMismatchException {
        checkEqualLength(a, b);
        double[] result = (double[]) a.clone();
        for (int i = 0; i < a.length; i++) {
            result[i] = result[i] / b[i];
        }
        return result;
    }

    public static double distance1(double[] p1, double[] p2) throws DimensionMismatchException {
        checkEqualLength(p1, p2);
        double sum = 0.0d;
        for (int i = 0; i < p1.length; i++) {
            sum += FastMath.abs(p1[i] - p2[i]);
        }
        return sum;
    }

    public static int distance1(int[] p1, int[] p2) throws DimensionMismatchException {
        checkEqualLength(p1, p2);
        int sum = 0;
        for (int i = 0; i < p1.length; i++) {
            sum += FastMath.abs(p1[i] - p2[i]);
        }
        return sum;
    }

    public static double distance(double[] p1, double[] p2) throws DimensionMismatchException {
        checkEqualLength(p1, p2);
        double sum = 0.0d;
        for (int i = 0; i < p1.length; i++) {
            double dp = p1[i] - p2[i];
            sum += dp * dp;
        }
        return FastMath.sqrt(sum);
    }

    public static double cosAngle(double[] v1, double[] v2) {
        return linearCombination(v1, v2) / (safeNorm(v1) * safeNorm(v2));
    }

    public static double distance(int[] p1, int[] p2) throws DimensionMismatchException {
        checkEqualLength(p1, p2);
        double sum = 0.0d;
        for (int i = 0; i < p1.length; i++) {
            double dp = (double) (p1[i] - p2[i]);
            sum += dp * dp;
        }
        return FastMath.sqrt(sum);
    }

    public static double distanceInf(double[] p1, double[] p2) throws DimensionMismatchException {
        checkEqualLength(p1, p2);
        double max = 0.0d;
        for (int i = 0; i < p1.length; i++) {
            max = FastMath.max(max, FastMath.abs(p1[i] - p2[i]));
        }
        return max;
    }

    public static int distanceInf(int[] p1, int[] p2) throws DimensionMismatchException {
        checkEqualLength(p1, p2);
        int max = 0;
        for (int i = 0; i < p1.length; i++) {
            max = FastMath.max(max, FastMath.abs(p1[i] - p2[i]));
        }
        return max;
    }

    public static <T extends Comparable<? super T>> boolean isMonotonic(T[] val, OrderDirection dir, boolean strict) {
        T previous = val[0];
        int max = val.length;
        T previous2 = previous;
        for (int i = 1; i < max; i++) {
            switch (dir) {
                case INCREASING:
                    int comp = previous2.compareTo(val[i]);
                    if (!strict) {
                        if (comp <= 0) {
                            break;
                        } else {
                            return false;
                        }
                    } else if (comp < 0) {
                        break;
                    } else {
                        return false;
                    }
                case DECREASING:
                    int comp2 = val[i].compareTo(previous2);
                    if (!strict) {
                        if (comp2 <= 0) {
                            break;
                        } else {
                            return false;
                        }
                    } else if (comp2 < 0) {
                        break;
                    } else {
                        return false;
                    }
                default:
                    throw new MathInternalError();
            }
            previous2 = val[i];
        }
        return true;
    }

    public static boolean isMonotonic(double[] val, OrderDirection dir, boolean strict) {
        return checkOrder(val, dir, strict, false);
    }

    public static boolean checkEqualLength(double[] a, double[] b, boolean abort) {
        if (a.length == b.length) {
            return true;
        }
        if (!abort) {
            return false;
        }
        throw new DimensionMismatchException(a.length, b.length);
    }

    public static void checkEqualLength(double[] a, double[] b) {
        checkEqualLength(a, b, true);
    }

    public static boolean checkEqualLength(int[] a, int[] b, boolean abort) {
        if (a.length == b.length) {
            return true;
        }
        if (!abort) {
            return false;
        }
        throw new DimensionMismatchException(a.length, b.length);
    }

    public static void checkEqualLength(int[] a, int[] b) {
        checkEqualLength(a, b, true);
    }

    public static boolean checkOrder(double[] val, OrderDirection dir, boolean strict, boolean abort) throws NonMonotonicSequenceException {
        int index;
        double[] dArr = val;
        double previous = dArr[0];
        int max = dArr.length;
        double previous2 = previous;
        int index2 = 1;
        while (true) {
            index = index2;
            if (index < max) {
                switch (dir) {
                    case INCREASING:
                        if (!strict) {
                            if (dArr[index] >= previous2) {
                                break;
                            } else {
                                break;
                            }
                        } else if (dArr[index] <= previous2) {
                            break;
                        } else {
                            continue;
                        }
                    case DECREASING:
                        if (!strict) {
                            if (dArr[index] <= previous2) {
                                break;
                            } else {
                                break;
                            }
                        } else if (dArr[index] >= previous2) {
                            break;
                        } else {
                            continue;
                        }
                    default:
                        throw new MathInternalError();
                }
            }
            previous2 = dArr[index];
            index2 = index + 1;
        }
        if (index == max) {
            return true;
        }
        if (!abort) {
            return false;
        }
        NonMonotonicSequenceException nonMonotonicSequenceException = new NonMonotonicSequenceException(Double.valueOf(dArr[index]), Double.valueOf(previous2), index, dir, strict);
        throw nonMonotonicSequenceException;
    }

    public static void checkOrder(double[] val, OrderDirection dir, boolean strict) throws NonMonotonicSequenceException {
        checkOrder(val, dir, strict, true);
    }

    public static void checkOrder(double[] val) throws NonMonotonicSequenceException {
        checkOrder(val, OrderDirection.INCREASING, true);
    }

    public static void checkRectangular(long[][] in) throws NullArgumentException, DimensionMismatchException {
        MathUtils.checkNotNull(in);
        for (int i = 1; i < in.length; i++) {
            if (in[i].length != in[0].length) {
                throw new DimensionMismatchException(LocalizedFormats.DIFFERENT_ROWS_LENGTHS, in[i].length, in[0].length);
            }
        }
    }

    public static void checkPositive(double[] in) throws NotStrictlyPositiveException {
        for (int i = 0; i < in.length; i++) {
            if (in[i] <= 0.0d) {
                throw new NotStrictlyPositiveException(Double.valueOf(in[i]));
            }
        }
    }

    public static void checkNotNaN(double[] in) throws NotANumberException {
        for (double isNaN : in) {
            if (Double.isNaN(isNaN)) {
                throw new NotANumberException();
            }
        }
    }

    public static void checkNonNegative(long[] in) throws NotPositiveException {
        for (int i = 0; i < in.length; i++) {
            if (in[i] < 0) {
                throw new NotPositiveException(Long.valueOf(in[i]));
            }
        }
    }

    public static void checkNonNegative(long[][] in) throws NotPositiveException {
        for (int i = 0; i < in.length; i++) {
            for (int j = 0; j < in[i].length; j++) {
                if (in[i][j] < 0) {
                    throw new NotPositiveException(Long.valueOf(in[i][j]));
                }
            }
        }
    }

    public static double safeNorm(double[] v) {
        double[] dArr = v;
        long j = 4892772783196988160L;
        double s2 = 0.0d;
        double x3max = 0.0d;
        double x1max = 0.0d;
        double x3max2 = 0.0d;
        double s1 = 0.0d;
        double agiant = 1.304E19d / ((double) dArr.length);
        int i = 0;
        while (true) {
            long j2 = j;
            if (i >= dArr.length) {
                break;
            }
            double xabs = FastMath.abs(dArr[i]);
            if (xabs >= 3.834E-20d && xabs <= agiant) {
                s2 += xabs * xabs;
            } else if (xabs > 3.834E-20d) {
                if (xabs > x1max) {
                    double r = x1max / xabs;
                    x1max = xabs;
                    s1 = (s1 * r * r) + 1.0d;
                } else {
                    double r2 = xabs / x1max;
                    s1 += r2 * r2;
                }
            } else if (xabs > x3max2) {
                double r3 = x3max2 / xabs;
                x3max2 = xabs;
                x3max = (x3max * r3 * r3) + 1.0d;
            } else if (xabs != 0.0d) {
                double r4 = xabs / x3max2;
                x3max += r4 * r4;
            }
            i++;
            j = j2;
        }
        if (s1 != 0.0d) {
            return Math.sqrt(s1 + ((s2 / x1max) / x1max)) * x1max;
        }
        if (s2 == 0.0d) {
            return Math.sqrt(x3max) * x3max2;
        }
        if (s2 >= x3max2) {
            return Math.sqrt((((x3max2 / s2) * x3max2 * x3max) + 1.0d) * s2);
        }
        return Math.sqrt(((s2 / x3max2) + (x3max2 * x3max)) * x3max2);
    }

    public static void sortInPlace(double[] x, double[]... yList) throws DimensionMismatchException, NullArgumentException {
        sortInPlace(x, OrderDirection.INCREASING, yList);
    }

    public static void sortInPlace(double[] x, OrderDirection dir, double[]... yList) throws NullArgumentException, DimensionMismatchException {
        if (x == null) {
            throw new NullArgumentException();
        }
        int len = x.length;
        int j = 0;
        while (j < yListLen) {
            double[] y = yList[j];
            if (y == null) {
                throw new NullArgumentException();
            } else if (y.length != len) {
                throw new DimensionMismatchException(y.length, len);
            } else {
                j++;
            }
        }
        List<PairDoubleInteger> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            list.add(new PairDoubleInteger(x[i], i));
        }
        Collections.sort(list, dir == OrderDirection.INCREASING ? new Comparator<PairDoubleInteger>() {
            public int compare(PairDoubleInteger o1, PairDoubleInteger o2) {
                return Double.compare(o1.getKey(), o2.getKey());
            }
        } : new Comparator<PairDoubleInteger>() {
            public int compare(PairDoubleInteger o1, PairDoubleInteger o2) {
                return Double.compare(o2.getKey(), o1.getKey());
            }
        });
        int[] indices = new int[len];
        for (int i2 = 0; i2 < len; i2++) {
            PairDoubleInteger e = (PairDoubleInteger) list.get(i2);
            x[i2] = e.getKey();
            indices[i2] = e.getValue();
        }
        for (double[] yInPlace : yList) {
            double[] yOrig = (double[]) yInPlace.clone();
            for (int i3 = 0; i3 < len; i3++) {
                yInPlace[i3] = yOrig[indices[i3]];
            }
        }
    }

    public static int[] copyOf(int[] source) {
        return copyOf(source, source.length);
    }

    public static double[] copyOf(double[] source) {
        return copyOf(source, source.length);
    }

    public static int[] copyOf(int[] source, int len) {
        int[] output = new int[len];
        System.arraycopy(source, 0, output, 0, FastMath.min(len, source.length));
        return output;
    }

    public static double[] copyOf(double[] source, int len) {
        double[] output = new double[len];
        System.arraycopy(source, 0, output, 0, FastMath.min(len, source.length));
        return output;
    }

    public static double[] copyOfRange(double[] source, int from, int to) {
        int len = to - from;
        double[] output = new double[len];
        System.arraycopy(source, from, output, 0, FastMath.min(len, source.length - from));
        return output;
    }

    public static double linearCombination(double[] a, double[] b) throws DimensionMismatchException {
        double[] dArr = a;
        checkEqualLength(a, b);
        int len = dArr.length;
        if (len == 1) {
            return dArr[0] * b[0];
        }
        double[] prodHigh = new double[len];
        double prodLowSum = 0.0d;
        for (int i = 0; i < len; i++) {
            double ai = dArr[i];
            double aHigh = Double.longBitsToDouble(Double.doubleToRawLongBits(ai) & -134217728);
            double aLow = ai - aHigh;
            double bi = b[i];
            double bHigh = Double.longBitsToDouble(Double.doubleToRawLongBits(bi) & -134217728);
            double bLow = bi - bHigh;
            prodHigh[i] = ai * bi;
            prodLowSum += (aLow * bLow) - (((prodHigh[i] - (aHigh * bHigh)) - (aLow * bHigh)) - (aHigh * bLow));
        }
        double prodHighCur = prodHigh[0];
        double prodHighNext = prodHigh[1];
        double sHighPrev = prodHighCur + prodHighNext;
        double sPrime = sHighPrev - prodHighNext;
        double sLowSum = (prodHighNext - (sHighPrev - sPrime)) + (prodHighCur - sPrime);
        int lenMinusOne = len - 1;
        for (int i2 = 1; i2 < lenMinusOne; i2++) {
            double prodHighNext2 = prodHigh[i2 + 1];
            double sHighCur = sHighPrev + prodHighNext2;
            double sPrime2 = sHighCur - prodHighNext2;
            sLowSum += (prodHighNext2 - (sHighCur - sPrime2)) + (sHighPrev - sPrime2);
            sHighPrev = sHighCur;
        }
        double result = sHighPrev + prodLowSum + sLowSum;
        if (Double.isNaN(result)) {
            result = 0.0d;
            int i3 = 0;
            while (true) {
                double[] prodHigh2 = prodHigh;
                int i4 = i3;
                if (i4 >= len) {
                    break;
                }
                result += dArr[i4] * b[i4];
                i3 = i4 + 1;
                prodHigh = prodHigh2;
            }
        }
        return result;
    }

    public static double linearCombination(double a1, double b1, double a2, double b2) {
        double a1High = Double.longBitsToDouble(Double.doubleToRawLongBits(a1) & -134217728);
        double a1Low = a1 - a1High;
        double b1High = Double.longBitsToDouble(Double.doubleToRawLongBits(b1) & -134217728);
        double b1Low = b1 - b1High;
        double prod1High = a1 * b1;
        double prod1Low = (a1Low * b1Low) - (((prod1High - (a1High * b1High)) - (a1Low * b1High)) - (a1High * b1Low));
        double d = a1High;
        double a1High2 = Double.longBitsToDouble(Double.doubleToRawLongBits(a2) & -134217728);
        double a2Low = a2 - a1High2;
        double b2High = Double.longBitsToDouble(Double.doubleToRawLongBits(b2) & -134217728);
        double b2Low = b2 - b2High;
        double prod2High = a2 * b2;
        double s12High = prod1High + prod2High;
        double s12Prime = s12High - prod2High;
        double s12Low = (prod2High - (s12High - s12Prime)) + (prod1High - s12Prime);
        double d2 = a1High2;
        double result = s12High + prod1Low + ((a2Low * b2Low) - (((prod2High - (a1High2 * b2High)) - (a2Low * b2High)) - (a1High2 * b2Low))) + s12Low;
        if (Double.isNaN(result)) {
            return (a1 * b1) + (a2 * b2);
        }
        return result;
    }

    public static double linearCombination(double a1, double b1, double a2, double b2, double a3, double b3) {
        double a1High = Double.longBitsToDouble(Double.doubleToRawLongBits(a1) & -134217728);
        double a1Low = a1 - a1High;
        double b1High = Double.longBitsToDouble(Double.doubleToRawLongBits(b1) & -134217728);
        double b1Low = b1 - b1High;
        double prod1High = a1 * b1;
        double prod1Low = (a1Low * b1Low) - (((prod1High - (a1High * b1High)) - (a1Low * b1High)) - (a1High * b1Low));
        double d = b1High;
        double b1High2 = Double.longBitsToDouble(Double.doubleToRawLongBits(a2) & -134217728);
        double a2Low = a2 - b1High2;
        double d2 = a1High;
        double a1High2 = Double.longBitsToDouble(Double.doubleToRawLongBits(b2) & -134217728);
        double b2Low = b2 - a1High2;
        double prod2High = a2 * b2;
        double prod2Low = (a2Low * b2Low) - (((prod2High - (b1High2 * a1High2)) - (a2Low * a1High2)) - (b1High2 * b2Low));
        double d3 = b1High2;
        double a3High = Double.longBitsToDouble(Double.doubleToRawLongBits(a3) & -134217728);
        double a3Low = a3 - a3High;
        double b3High = Double.longBitsToDouble(Double.doubleToRawLongBits(b3) & -134217728);
        double b3Low = b3 - b3High;
        double prod3High = a3 * b3;
        double s12High = prod1High + prod2High;
        double s12Prime = s12High - prod2High;
        double s12Low = (prod2High - (s12High - s12Prime)) + (prod1High - s12Prime);
        double s123High = s12High + prod3High;
        double s123Prime = s123High - prod3High;
        double s123Low = (prod3High - (s123High - s123Prime)) + (s12High - s123Prime);
        double d4 = a3High;
        double result = s123High + prod1Low + prod2Low + ((a3Low * b3Low) - (((prod3High - (a3High * b3High)) - (a3Low * b3High)) - (a3High * b3Low))) + s12Low + s123Low;
        if (Double.isNaN(result)) {
            return (a1 * b1) + (a2 * b2) + (a3 * b3);
        }
        return result;
    }

    public static double linearCombination(double a1, double b1, double a2, double b2, double a3, double b3, double a4, double b4) {
        double a1High = Double.longBitsToDouble(Double.doubleToRawLongBits(a1) & -134217728);
        double a1Low = a1 - a1High;
        double b1High = Double.longBitsToDouble(Double.doubleToRawLongBits(b1) & -134217728);
        double b1Low = b1 - b1High;
        double prod1High = a1 * b1;
        double prod1Low = (a1Low * b1Low) - (((prod1High - (a1High * b1High)) - (a1Low * b1High)) - (a1High * b1Low));
        double d = b1High;
        double b1High2 = Double.longBitsToDouble(Double.doubleToRawLongBits(a2) & -134217728);
        double a2Low = a2 - b1High2;
        double d2 = a1High;
        double a1High2 = Double.longBitsToDouble(Double.doubleToRawLongBits(b2) & -134217728);
        double b2Low = b2 - a1High2;
        double prod2High = a2 * b2;
        double prod2Low = (a2Low * b2Low) - (((prod2High - (b1High2 * a1High2)) - (a2Low * a1High2)) - (b1High2 * b2Low));
        double d3 = b1High2;
        double a3High = Double.longBitsToDouble(Double.doubleToRawLongBits(a3) & -134217728);
        double a3Low = a3 - a3High;
        double d4 = a1High2;
        double b3High = Double.longBitsToDouble(Double.doubleToRawLongBits(b3) & -134217728);
        double b3Low = b3 - b3High;
        double prod3High = a3 * b3;
        double prod3Low = (a3Low * b3Low) - (((prod3High - (a3High * b3High)) - (a3Low * b3High)) - (a3High * b3Low));
        double d5 = a3High;
        double a4High = Double.longBitsToDouble(Double.doubleToRawLongBits(a4) & -134217728);
        double a4Low = a4 - a4High;
        double d6 = b3High;
        double b3High2 = Double.longBitsToDouble(Double.doubleToRawLongBits(b4) & -134217728);
        double b4Low = b4 - b3High2;
        double prod4High = a4 * b4;
        double s12High = prod1High + prod2High;
        double s12Prime = s12High - prod2High;
        double s12Low = (prod2High - (s12High - s12Prime)) + (prod1High - s12Prime);
        double s123High = s12High + prod3High;
        double s123Prime = s123High - prod3High;
        double s123Low = (prod3High - (s123High - s123Prime)) + (s12High - s123Prime);
        double s1234High = s123High + prod4High;
        double s1234Prime = s1234High - prod4High;
        double s1234Low = (prod4High - (s1234High - s1234Prime)) + (s123High - s1234Prime);
        double d7 = a4High;
        double result = s1234High + prod1Low + prod2Low + prod3Low + ((a4Low * b4Low) - (((prod4High - (a4High * b3High2)) - (a4Low * b3High2)) - (a4High * b4Low))) + s12Low + s123Low + s1234Low;
        if (Double.isNaN(result)) {
            return (a1 * b1) + (a2 * b2) + (a3 * b3) + (a4 * b4);
        }
        return result;
    }

    public static boolean equals(float[] x, float[] y) {
        boolean z = true;
        if (x == null || y == null) {
            if ((x == null) ^ (y == null)) {
                z = false;
            }
            return z;
        } else if (x.length != y.length) {
            return false;
        } else {
            for (int i = 0; i < x.length; i++) {
                if (!Precision.equals(x[i], y[i])) {
                    return false;
                }
            }
            return true;
        }
    }

    public static boolean equalsIncludingNaN(float[] x, float[] y) {
        boolean z = true;
        if (x == null || y == null) {
            if ((x == null) ^ (y == null)) {
                z = false;
            }
            return z;
        } else if (x.length != y.length) {
            return false;
        } else {
            for (int i = 0; i < x.length; i++) {
                if (!Precision.equalsIncludingNaN(x[i], y[i])) {
                    return false;
                }
            }
            return true;
        }
    }

    public static boolean equals(double[] x, double[] y) {
        boolean z = true;
        if (x == null || y == null) {
            if ((x == null) ^ (y == null)) {
                z = false;
            }
            return z;
        } else if (x.length != y.length) {
            return false;
        } else {
            for (int i = 0; i < x.length; i++) {
                if (!Precision.equals(x[i], y[i])) {
                    return false;
                }
            }
            return true;
        }
    }

    public static boolean equalsIncludingNaN(double[] x, double[] y) {
        boolean z = true;
        if (x == null || y == null) {
            if ((x == null) ^ (y == null)) {
                z = false;
            }
            return z;
        } else if (x.length != y.length) {
            return false;
        } else {
            for (int i = 0; i < x.length; i++) {
                if (!Precision.equalsIncludingNaN(x[i], y[i])) {
                    return false;
                }
            }
            return true;
        }
    }

    public static double[] normalizeArray(double[] values, double normalizedSum) throws MathIllegalArgumentException, MathArithmeticException {
        if (Double.isInfinite(normalizedSum)) {
            throw new MathIllegalArgumentException(LocalizedFormats.NORMALIZE_INFINITE, new Object[0]);
        } else if (Double.isNaN(normalizedSum)) {
            throw new MathIllegalArgumentException(LocalizedFormats.NORMALIZE_NAN, new Object[0]);
        } else {
            int len = values.length;
            double[] out = new double[len];
            double sum = 0.0d;
            for (int i = 0; i < len; i++) {
                if (Double.isInfinite(values[i])) {
                    throw new MathIllegalArgumentException(LocalizedFormats.INFINITE_ARRAY_ELEMENT, Double.valueOf(values[i]), Integer.valueOf(i));
                }
                if (!Double.isNaN(values[i])) {
                    sum += values[i];
                }
            }
            if (sum == 0.0d) {
                throw new MathArithmeticException(LocalizedFormats.ARRAY_SUMS_TO_ZERO, new Object[0]);
            }
            for (int i2 = 0; i2 < len; i2++) {
                if (Double.isNaN(values[i2])) {
                    out[i2] = Double.NaN;
                } else {
                    out[i2] = (values[i2] * normalizedSum) / sum;
                }
            }
            return out;
        }
    }

    public static <T> T[] buildArray(Field<T> field, int length) {
        T[] array = (Object[]) Array.newInstance(field.getRuntimeClass(), length);
        Arrays.fill(array, field.getZero());
        return array;
    }

    public static <T> T[][] buildArray(Field<T> field, int rows, int columns) {
        if (columns < 0) {
            return (Object[][]) Array.newInstance(buildArray(field, 0).getClass(), rows);
        }
        T[][] array = (Object[][]) Array.newInstance(field.getRuntimeClass(), new int[]{rows, columns});
        for (int i = 0; i < rows; i++) {
            Arrays.fill(array[i], field.getZero());
        }
        return array;
    }

    public static double[] convolve(double[] x, double[] h) throws NullArgumentException, NoDataException {
        double[] dArr = x;
        double[] dArr2 = h;
        MathUtils.checkNotNull(x);
        MathUtils.checkNotNull(h);
        int xLen = dArr.length;
        int hLen = dArr2.length;
        if (xLen == 0 || hLen == 0) {
            throw new NoDataException();
        }
        int totalLength = (xLen + hLen) - 1;
        double[] y = new double[totalLength];
        for (int n = 0; n < totalLength; n++) {
            double yn = 0.0d;
            int k = FastMath.max(0, (n + 1) - xLen);
            int k2 = n - k;
            while (k < hLen && k2 >= 0) {
                int j = k2 - 1;
                double d = dArr[k2];
                yn += d * dArr2[k];
                k++;
                k2 = j;
            }
            y[n] = yn;
        }
        return y;
    }

    public static void shuffle(int[] list, int start, Position pos) {
        shuffle(list, start, pos, new Well19937c());
    }

    public static void shuffle(int[] list, int start, Position pos, RandomGenerator rng) {
        int target;
        int target2;
        switch (pos) {
            case TAIL:
                for (int i = list.length - 1; i >= start; i--) {
                    if (i == start) {
                        target = start;
                    } else {
                        target = new UniformIntegerDistribution(rng, start, i).sample();
                    }
                    int temp = list[target];
                    list[target] = list[i];
                    list[i] = temp;
                }
                return;
            case HEAD:
                for (int i2 = 0; i2 <= start; i2++) {
                    if (i2 == start) {
                        target2 = start;
                    } else {
                        target2 = new UniformIntegerDistribution(rng, i2, start).sample();
                    }
                    int temp2 = list[target2];
                    list[target2] = list[i2];
                    list[i2] = temp2;
                }
                return;
            default:
                throw new MathInternalError();
        }
    }

    public static void shuffle(int[] list, RandomGenerator rng) {
        shuffle(list, 0, Position.TAIL, rng);
    }

    public static void shuffle(int[] list) {
        shuffle(list, new Well19937c());
    }

    public static int[] natural(int n) {
        return sequence(n, 0, 1);
    }

    public static int[] sequence(int size, int start, int stride) {
        int[] a = new int[size];
        for (int i = 0; i < size; i++) {
            a[i] = (i * stride) + start;
        }
        return a;
    }

    public static boolean verifyValues(double[] values, int begin, int length) throws MathIllegalArgumentException {
        return verifyValues(values, begin, length, false);
    }

    public static boolean verifyValues(double[] values, int begin, int length, boolean allowEmpty) throws MathIllegalArgumentException {
        if (values == null) {
            throw new NullArgumentException(LocalizedFormats.INPUT_ARRAY, new Object[0]);
        } else if (begin < 0) {
            throw new NotPositiveException(LocalizedFormats.START_POSITION, Integer.valueOf(begin));
        } else if (length < 0) {
            throw new NotPositiveException(LocalizedFormats.LENGTH, Integer.valueOf(length));
        } else if (begin + length > values.length) {
            throw new NumberIsTooLargeException(LocalizedFormats.SUBARRAY_ENDS_AFTER_ARRAY_END, Integer.valueOf(begin + length), Integer.valueOf(values.length), true);
        } else if (length != 0 || allowEmpty) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean verifyValues(double[] values, double[] weights, int begin, int length) throws MathIllegalArgumentException {
        return verifyValues(values, weights, begin, length, false);
    }

    public static boolean verifyValues(double[] values, double[] weights, int begin, int length, boolean allowEmpty) throws MathIllegalArgumentException {
        if (weights == null || values == null) {
            throw new NullArgumentException(LocalizedFormats.INPUT_ARRAY, new Object[0]);
        }
        checkEqualLength(weights, values);
        boolean containsPositiveWeight = false;
        int i = begin;
        while (i < begin + length) {
            double weight = weights[i];
            if (Double.isNaN(weight)) {
                throw new MathIllegalArgumentException(LocalizedFormats.NAN_ELEMENT_AT_INDEX, Integer.valueOf(i));
            } else if (Double.isInfinite(weight)) {
                throw new MathIllegalArgumentException(LocalizedFormats.INFINITE_ARRAY_ELEMENT, Double.valueOf(weight), Integer.valueOf(i));
            } else if (weight < 0.0d) {
                throw new MathIllegalArgumentException(LocalizedFormats.NEGATIVE_ELEMENT_AT_INDEX, Integer.valueOf(i), Double.valueOf(weight));
            } else {
                if (!containsPositiveWeight && weight > 0.0d) {
                    containsPositiveWeight = true;
                }
                i++;
            }
        }
        if (containsPositiveWeight) {
            return verifyValues(values, begin, length, allowEmpty);
        }
        throw new MathIllegalArgumentException(LocalizedFormats.WEIGHT_AT_LEAST_ONE_NON_ZERO, new Object[0]);
    }

    public static double[] concatenate(double[]... x) {
        int combinedLength = 0;
        for (double[] a : x) {
            combinedLength += a.length;
        }
        double[] combined = new double[combinedLength];
        int offset = 0;
        for (int i = 0; i < x.length; i++) {
            int curLength = x[i].length;
            System.arraycopy(x[i], 0, combined, offset, curLength);
            offset += curLength;
        }
        return combined;
    }

    public static double[] unique(double[] data) {
        TreeSet<Double> values = new TreeSet<>();
        int i = 0;
        for (double valueOf : data) {
            values.add(Double.valueOf(valueOf));
        }
        int count = values.size();
        double[] out = new double[count];
        Iterator<Double> iterator = values.iterator();
        while (iterator.hasNext()) {
            i++;
            out[count - i] = ((Double) iterator.next()).doubleValue();
        }
        return out;
    }
}

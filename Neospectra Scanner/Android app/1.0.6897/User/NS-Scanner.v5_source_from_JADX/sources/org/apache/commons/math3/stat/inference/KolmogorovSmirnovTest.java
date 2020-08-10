package org.apache.commons.math3.stat.inference;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import org.apache.commons.math3.distribution.EnumeratedRealDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.InsufficientDataException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

public class KolmogorovSmirnovTest {
    protected static final double KS_SUM_CAUCHY_CRITERION = 1.0E-20d;
    protected static final int LARGE_SAMPLE_PRODUCT = 10000;
    protected static final int MAXIMUM_PARTIAL_SUM_COUNT = 100000;
    @Deprecated
    protected static final int MONTE_CARLO_ITERATIONS = 1000000;
    protected static final double PG_SUM_RELATIVE_ERROR = 1.0E-10d;
    @Deprecated
    protected static final int SMALL_SAMPLE_PRODUCT = 200;
    private final RandomGenerator rng;

    public KolmogorovSmirnovTest() {
        this.rng = new Well19937c();
    }

    @Deprecated
    public KolmogorovSmirnovTest(RandomGenerator rng2) {
        this.rng = rng2;
    }

    public double kolmogorovSmirnovTest(RealDistribution distribution, double[] data, boolean exact) {
        return 1.0d - cdf(kolmogorovSmirnovStatistic(distribution, data), data.length, exact);
    }

    public double kolmogorovSmirnovStatistic(RealDistribution distribution, double[] data) {
        checkArray(data);
        int n = data.length;
        double nd = (double) n;
        double[] dataCopy = new double[n];
        System.arraycopy(data, 0, dataCopy, 0, n);
        Arrays.sort(dataCopy);
        double d = 0.0d;
        for (int i = 1; i <= n; i++) {
            double yi = distribution.cumulativeProbability(dataCopy[i - 1]);
            double currD = FastMath.max(yi - (((double) (i - 1)) / nd), (((double) i) / nd) - yi);
            if (currD > d) {
                d = currD;
            }
        }
        return d;
    }

    public double kolmogorovSmirnovTest(double[] x, double[] y, boolean strict) {
        double[] ya;
        double[] xa;
        long lengthProduct = ((long) x.length) * ((long) y.length);
        if (lengthProduct >= 10000 || !hasTies(x, y)) {
            xa = x;
            ya = y;
        } else {
            xa = MathArrays.copyOf(x);
            ya = MathArrays.copyOf(y);
            fixTies(xa, ya);
        }
        if (lengthProduct >= 10000) {
            return approximateP(kolmogorovSmirnovStatistic(x, y), x.length, y.length);
        }
        return exactP(kolmogorovSmirnovStatistic(xa, ya), x.length, y.length, strict);
    }

    public double kolmogorovSmirnovTest(double[] x, double[] y) {
        return kolmogorovSmirnovTest(x, y, true);
    }

    public double kolmogorovSmirnovStatistic(double[] x, double[] y) {
        return ((double) integralKolmogorovSmirnovStatistic(x, y)) / ((double) (((long) x.length) * ((long) y.length)));
    }

    private long integralKolmogorovSmirnovStatistic(double[] x, double[] y) {
        checkArray(x);
        checkArray(y);
        double[] sx = MathArrays.copyOf(x);
        double[] sy = MathArrays.copyOf(y);
        Arrays.sort(sx);
        Arrays.sort(sy);
        int n = sx.length;
        int m = sy.length;
        int rankX = 0;
        int rankY = 0;
        long curD = 0;
        long supD = 0;
        do {
            double z = Double.compare(sx[rankX], sy[rankY]) <= 0 ? sx[rankX] : sy[rankY];
            while (rankX < n && Double.compare(sx[rankX], z) == 0) {
                rankX++;
                curD += (long) m;
            }
            while (rankY < m && Double.compare(sy[rankY], z) == 0) {
                rankY++;
                curD -= (long) n;
            }
            if (curD > supD) {
                supD = curD;
            } else if ((-curD) > supD) {
                supD = -curD;
            }
            if (rankX >= n) {
                break;
            }
        } while (rankY < m);
        return supD;
    }

    public double kolmogorovSmirnovTest(RealDistribution distribution, double[] data) {
        return kolmogorovSmirnovTest(distribution, data, false);
    }

    public boolean kolmogorovSmirnovTest(RealDistribution distribution, double[] data, double alpha) {
        if (alpha <= 0.0d || alpha > 0.5d) {
            throw new OutOfRangeException(LocalizedFormats.OUT_OF_BOUND_SIGNIFICANCE_LEVEL, Double.valueOf(alpha), Integer.valueOf(0), Double.valueOf(0.5d));
        } else if (kolmogorovSmirnovTest(distribution, data) < alpha) {
            return true;
        } else {
            return false;
        }
    }

    public double bootstrap(double[] x, double[] y, int iterations, boolean strict) {
        double[] dArr = x;
        double[] dArr2 = y;
        int i = iterations;
        int xLength = dArr.length;
        int yLength = dArr2.length;
        double[] combined = new double[(xLength + yLength)];
        System.arraycopy(dArr, 0, combined, 0, xLength);
        System.arraycopy(dArr2, 0, combined, xLength, yLength);
        EnumeratedRealDistribution dist = new EnumeratedRealDistribution(this.rng, combined);
        long d = integralKolmogorovSmirnovStatistic(x, y);
        int greaterCount = 0;
        int equalCount = 0;
        for (int i2 = 0; i2 < i; i2++) {
            long curD = integralKolmogorovSmirnovStatistic(dist.sample(xLength), dist.sample(yLength));
            if (curD > d) {
                greaterCount++;
            } else if (curD == d) {
                equalCount++;
            }
        }
        return strict ? ((double) greaterCount) / ((double) i) : ((double) (greaterCount + equalCount)) / ((double) i);
    }

    public double bootstrap(double[] x, double[] y, int iterations) {
        return bootstrap(x, y, iterations, true);
    }

    public double cdf(double d, int n) throws MathArithmeticException {
        return cdf(d, n, false);
    }

    public double cdfExact(double d, int n) throws MathArithmeticException {
        return cdf(d, n, true);
    }

    public double cdf(double d, int n, boolean exact) throws MathArithmeticException {
        int i = n;
        double ninv = 1.0d / ((double) i);
        double ninvhalf = 0.5d * ninv;
        if (d <= ninvhalf) {
            return 0.0d;
        }
        if (ninvhalf < d && d <= ninv) {
            double res = 1.0d;
            double f = (d * 2.0d) - ninv;
            for (int i2 = 1; i2 <= i; i2++) {
                res *= ((double) i2) * f;
            }
            return res;
        } else if (1.0d - ninv <= d && d < 1.0d) {
            return 1.0d - (Math.pow(1.0d - d, (double) i) * 2.0d);
        } else {
            if (1.0d <= d) {
                return 1.0d;
            }
            if (exact) {
                return exactK(d, n);
            }
            if (i <= 140) {
                return roundedK(d, n);
            }
            return pelzGood(d, n);
        }
    }

    private double exactK(double d, int n) throws MathArithmeticException {
        int k = (int) Math.ceil(((double) n) * d);
        BigFraction pFrac = (BigFraction) createExactH(d, n).power(n).getEntry(k - 1, k - 1);
        for (int i = 1; i <= n; i++) {
            pFrac = pFrac.multiply(i).divide(n);
        }
        return pFrac.bigDecimalValue(20, 4).doubleValue();
    }

    private double roundedK(double d, int n) {
        int k = (int) Math.ceil(((double) n) * d);
        double pFrac = createRoundedH(d, n).power(n).getEntry(k - 1, k - 1);
        for (int i = 1; i <= n; i++) {
            pFrac *= ((double) i) / ((double) n);
        }
        return pFrac;
    }

    public double pelzGood(double d, int n) {
        int i;
        double z8;
        int k;
        double sqrtN;
        double z6;
        double increment;
        double sum;
        double z4;
        double ret;
        int i2 = n;
        double sqrtN2 = FastMath.sqrt((double) i2);
        double z = d * sqrtN2;
        double z2 = d * d * ((double) i2);
        double increment2 = z2 * z2;
        double kTerm = increment2 * z2;
        double z82 = increment2 * increment2;
        long j = 0;
        double increment3 = 0.0d;
        double kTerm2 = 0.0d;
        double increment4 = 9.869604401089358d / (8.0d * z2);
        double sum2 = 0.0d;
        int k2 = 1;
        while (true) {
            int k3 = k2;
            long j2 = j;
            i = MAXIMUM_PARTIAL_SUM_COUNT;
            z8 = z82;
            k = k3;
            if (k >= MAXIMUM_PARTIAL_SUM_COUNT) {
                sqrtN = sqrtN2;
                z6 = kTerm;
                double z62 = kTerm2;
                break;
            }
            z6 = kTerm;
            double kTerm3 = (double) ((k * 2) - 1);
            sqrtN = sqrtN2;
            increment3 = FastMath.exp((-increment4) * kTerm3 * kTerm3);
            sum2 += increment3;
            if (increment3 <= sum2 * 1.0E-10d) {
                break;
            }
            k2 = k + 1;
            kTerm2 = kTerm3;
            j = j2;
            z82 = z8;
            kTerm = z6;
            sqrtN2 = sqrtN;
            int i3 = n;
        }
        if (k == MAXIMUM_PARTIAL_SUM_COUNT) {
            throw new TooManyIterationsException(Integer.valueOf(MAXIMUM_PARTIAL_SUM_COUNT));
        }
        double ret2 = (FastMath.sqrt(6.283185307179586d) * sum2) / z;
        double twoZ2 = z2 * 2.0d;
        double sum3 = 0.0d;
        int k4 = 0;
        while (true) {
            if (k4 >= MAXIMUM_PARTIAL_SUM_COUNT) {
                double z2Term = increment4;
                increment = increment3;
                break;
            }
            double z2Term2 = increment4;
            double kTerm4 = ((double) k4) + 0.5d;
            double kTerm22 = kTerm4 * kTerm4;
            increment = ((kTerm22 * 9.869604401089358d) - z2) * FastMath.exp((kTerm22 * -9.869604401089358d) / twoZ2);
            double sum4 = sum3 + increment;
            if (FastMath.abs(increment) < FastMath.abs(sum4) * 1.0E-10d) {
                sum3 = sum4;
                break;
            }
            k4++;
            increment3 = increment;
            sum3 = sum4;
            increment4 = z2Term2;
        }
        if (k4 == MAXIMUM_PARTIAL_SUM_COUNT) {
            throw new TooManyIterationsException(Integer.valueOf(MAXIMUM_PARTIAL_SUM_COUNT));
        }
        double sqrtHalfPi = FastMath.sqrt(1.5707963267948966d);
        double ret3 = ret2 + ((sum3 * sqrtHalfPi) / ((3.0d * increment2) * sqrtN));
        double z4Term = 2.0d * increment2;
        double z6Term = 6.0d * z6;
        double z2Term3 = z2 * 5.0d;
        double sum5 = 0.0d;
        double sum6 = 0.0d;
        int k5 = 0;
        while (true) {
            if (k5 >= MAXIMUM_PARTIAL_SUM_COUNT) {
                double d2 = increment;
                double d3 = sum6;
                sum = sum5;
                break;
            }
            double d4 = increment;
            double kTerm5 = ((double) k5) + 0.5d;
            double kTerm23 = kTerm5 * kTerm5;
            double kTerm6 = kTerm5;
            increment = (z6Term + z4Term + ((z4Term - z2Term3) * 9.869604401089358d * kTerm23) + ((1.0d - twoZ2) * 97.40909103400243d * kTerm23 * kTerm23)) * FastMath.exp((kTerm23 * -9.869604401089358d) / twoZ2);
            sum = sum5 + increment;
            if (FastMath.abs(increment) < FastMath.abs(sum) * 1.0E-10d) {
                break;
            }
            k5++;
            sum5 = sum;
            sum6 = kTerm6;
        }
        if (k5 == MAXIMUM_PARTIAL_SUM_COUNT) {
            double d5 = increment;
            throw new TooManyIterationsException(Integer.valueOf(MAXIMUM_PARTIAL_SUM_COUNT));
        }
        double sum22 = 0.0d;
        double kTerm24 = 0.0d;
        int k6 = 1;
        while (true) {
            if (k6 >= i) {
                z4 = increment2;
                break;
            }
            z4 = increment2;
            double z42 = (double) (k6 * k6);
            double kTerm25 = z42;
            double increment5 = FastMath.exp((z42 * -9.869604401089358d) / twoZ2) * z42 * 9.869604401089358d;
            sum22 += increment5;
            if (FastMath.abs(increment5) < FastMath.abs(sum22) * 1.0E-10d) {
                double d6 = increment5;
                kTerm24 = kTerm25;
                break;
            }
            k6++;
            double d7 = increment5;
            increment2 = z4;
            kTerm24 = kTerm25;
            i = MAXIMUM_PARTIAL_SUM_COUNT;
        }
        if (k6 == MAXIMUM_PARTIAL_SUM_COUNT) {
            throw new TooManyIterationsException(Integer.valueOf(MAXIMUM_PARTIAL_SUM_COUNT));
        }
        int i4 = n;
        double increment6 = ret3 + ((sqrtHalfPi / ((double) i4)) * ((sum / ((((36.0d * z2) * z2) * z2) * z)) - (sum22 / ((18.0d * z2) * z))));
        double sum7 = 0.0d;
        int k7 = 0;
        while (true) {
            if (k7 >= MAXIMUM_PARTIAL_SUM_COUNT) {
                double d8 = z;
                break;
            }
            double sum23 = sum22;
            double kTerm7 = ((double) k7) + 0.5d;
            kTerm24 = kTerm7 * kTerm7;
            double kTerm42 = kTerm24 * kTerm24;
            double z3 = z;
            double increment7 = ((((((961.3891935753043d * (kTerm42 * kTerm24)) * (5.0d - (z2 * 30.0d))) + ((kTerm42 * 97.40909103400243d) * ((-60.0d * z2) + (212.0d * z4)))) + ((kTerm24 * 9.869604401089358d) * ((135.0d * z4) - (96.0d * z6)))) - (30.0d * z6)) - (90.0d * z8)) * FastMath.exp((kTerm24 * -9.869604401089358d) / twoZ2);
            sum7 += increment7;
            if (FastMath.abs(increment7) < FastMath.abs(sum7) * 1.0E-10d) {
                double d9 = increment7;
                break;
            }
            k7++;
            double d10 = increment7;
            sum22 = sum23;
            z = z3;
        }
        int i5 = MAXIMUM_PARTIAL_SUM_COUNT;
        if (k7 == MAXIMUM_PARTIAL_SUM_COUNT) {
            throw new TooManyIterationsException(Integer.valueOf(MAXIMUM_PARTIAL_SUM_COUNT));
        }
        double sum24 = 0.0d;
        int k8 = 1;
        while (true) {
            if (k8 >= i5) {
                ret = increment6;
                double d11 = kTerm24;
                break;
            }
            ret = increment6;
            double ret4 = (double) (k8 * k8);
            double kTerm26 = ret4;
            double increment8 = FastMath.exp((ret4 * -9.869604401089358d) / twoZ2) * ((-97.40909103400243d * ret4 * ret4) + (29.608813203268074d * ret4 * z2));
            sum24 += increment8;
            if (FastMath.abs(increment8) < FastMath.abs(sum24) * 1.0E-10d) {
                double d12 = increment8;
                break;
            }
            k8++;
            double d13 = increment8;
            increment6 = ret;
            kTerm24 = kTerm26;
            i5 = MAXIMUM_PARTIAL_SUM_COUNT;
        }
        if (k8 != MAXIMUM_PARTIAL_SUM_COUNT) {
            return ret + ((sqrtHalfPi / (sqrtN * ((double) i4))) * ((sum7 / ((3240.0d * z6) * z4)) + (sum24 / (108.0d * z6))));
        }
        throw new TooManyIterationsException(Integer.valueOf(MAXIMUM_PARTIAL_SUM_COUNT));
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0076  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0098 A[LOOP:2: B:31:0x0096->B:32:0x0098, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00a8 A[LOOP:3: B:34:0x00a6->B:35:0x00a8, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00d7  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00f6  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private org.apache.commons.math3.linear.FieldMatrix<org.apache.commons.math3.fraction.BigFraction> createExactH(double r20, int r22) throws org.apache.commons.math3.exception.NumberIsTooLargeException, org.apache.commons.math3.fraction.FractionConversionException {
        /*
            r19 = this;
            r1 = r22
            double r2 = (double) r1
            double r2 = r2 * r20
            double r2 = java.lang.Math.ceil(r2)
            int r2 = (int) r2
            int r3 = r2 * 2
            r4 = 1
            int r3 = r3 - r4
            double r5 = (double) r2
            double r7 = (double) r1
            double r7 = r7 * r20
            double r5 = r5 - r7
            r7 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            int r9 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            r12 = 0
            if (r9 < 0) goto L_0x0028
            org.apache.commons.math3.exception.NumberIsTooLargeException r4 = new org.apache.commons.math3.exception.NumberIsTooLargeException
            java.lang.Double r9 = java.lang.Double.valueOf(r5)
            java.lang.Double r7 = java.lang.Double.valueOf(r7)
            r4.<init>(r9, r7, r12)
            throw r4
        L_0x0028:
            r7 = 0
            org.apache.commons.math3.fraction.BigFraction r8 = new org.apache.commons.math3.fraction.BigFraction     // Catch:{ FractionConversionException -> 0x003f }
            r13 = 4307583784117748259(0x3bc79ca10c924223, double:1.0E-20)
            r15 = 10000(0x2710, float:1.4013E-41)
            r9 = r8
            r10 = r5
            r16 = 0
            r12 = r13
            r14 = r15
            r9.<init>(r10, r12, r14)     // Catch:{ FractionConversionException -> 0x003d }
            r7 = r8
            goto L_0x0067
        L_0x003d:
            r0 = move-exception
            goto L_0x0042
        L_0x003f:
            r0 = move-exception
            r16 = 0
        L_0x0042:
            r8 = r0
            org.apache.commons.math3.fraction.BigFraction r15 = new org.apache.commons.math3.fraction.BigFraction     // Catch:{ FractionConversionException -> 0x0053 }
            r12 = 4457293557087583675(0x3ddb7cdfd9d7bdbb, double:1.0E-10)
            r14 = 10000(0x2710, float:1.4013E-41)
            r9 = r15
            r10 = r5
            r9.<init>(r10, r12, r14)     // Catch:{ FractionConversionException -> 0x0053 }
            r7 = r15
            goto L_0x0067
        L_0x0053:
            r0 = move-exception
            r9 = r0
            r15 = r9
            org.apache.commons.math3.fraction.BigFraction r17 = new org.apache.commons.math3.fraction.BigFraction
            r12 = 4532020583610935537(0x3ee4f8b588e368f1, double:1.0E-5)
            r14 = 10000(0x2710, float:1.4013E-41)
            r9 = r17
            r10 = r5
            r9.<init>(r10, r12, r14)
            r7 = r17
        L_0x0067:
            int[] r8 = new int[]{r3, r3}
            java.lang.Class<org.apache.commons.math3.fraction.BigFraction> r9 = org.apache.commons.math3.fraction.BigFraction.class
            java.lang.Object r8 = java.lang.reflect.Array.newInstance(r9, r8)
            org.apache.commons.math3.fraction.BigFraction[][] r8 = (org.apache.commons.math3.fraction.BigFraction[][]) r8
            r9 = 0
        L_0x0074:
            if (r9 >= r3) goto L_0x0091
            r10 = 0
        L_0x0077:
            if (r10 >= r3) goto L_0x008e
            int r11 = r9 - r10
            int r11 = r11 + r4
            if (r11 >= 0) goto L_0x0085
            r11 = r8[r9]
            org.apache.commons.math3.fraction.BigFraction r12 = org.apache.commons.math3.fraction.BigFraction.ZERO
            r11[r10] = r12
            goto L_0x008b
        L_0x0085:
            r11 = r8[r9]
            org.apache.commons.math3.fraction.BigFraction r12 = org.apache.commons.math3.fraction.BigFraction.ONE
            r11[r10] = r12
        L_0x008b:
            int r10 = r10 + 1
            goto L_0x0077
        L_0x008e:
            int r9 = r9 + 1
            goto L_0x0074
        L_0x0091:
            org.apache.commons.math3.fraction.BigFraction[] r9 = new org.apache.commons.math3.fraction.BigFraction[r3]
            r9[r16] = r7
            r10 = 1
        L_0x0096:
            if (r10 >= r3) goto L_0x00a5
            int r11 = r10 + -1
            r11 = r9[r11]
            org.apache.commons.math3.fraction.BigFraction r11 = r7.multiply(r11)
            r9[r10] = r11
            int r10 = r10 + 1
            goto L_0x0096
        L_0x00a5:
            r10 = 0
        L_0x00a6:
            if (r10 >= r3) goto L_0x00ce
            r11 = r8[r10]
            r12 = r8[r10]
            r12 = r12[r16]
            r13 = r9[r10]
            org.apache.commons.math3.fraction.BigFraction r12 = r12.subtract(r13)
            r11[r16] = r12
            int r11 = r3 + -1
            r11 = r8[r11]
            int r12 = r3 + -1
            r12 = r8[r12]
            r12 = r12[r10]
            int r13 = r3 - r10
            int r13 = r13 - r4
            r13 = r9[r13]
            org.apache.commons.math3.fraction.BigFraction r12 = r12.subtract(r13)
            r11[r10] = r12
            int r10 = r10 + 1
            goto L_0x00a6
        L_0x00ce:
            org.apache.commons.math3.fraction.BigFraction r10 = org.apache.commons.math3.fraction.BigFraction.ONE_HALF
            int r10 = r7.compareTo(r10)
            r11 = 2
            if (r10 != r4) goto L_0x00f3
            int r10 = r3 + -1
            r10 = r8[r10]
            int r12 = r3 + -1
            r12 = r8[r12]
            r12 = r12[r16]
            org.apache.commons.math3.fraction.BigFraction r13 = r7.multiply(r11)
            org.apache.commons.math3.fraction.BigFraction r13 = r13.subtract(r4)
            org.apache.commons.math3.fraction.BigFraction r13 = r13.pow(r3)
            org.apache.commons.math3.fraction.BigFraction r12 = r12.add(r13)
            r10[r16] = r12
        L_0x00f3:
            r10 = 0
        L_0x00f4:
            if (r10 >= r3) goto L_0x0122
            r12 = 0
        L_0x00f7:
            int r13 = r10 + 1
            if (r12 >= r13) goto L_0x011d
            int r13 = r10 - r12
            int r13 = r13 + r4
            if (r13 <= 0) goto L_0x0118
            r13 = 2
        L_0x0101:
            int r14 = r10 - r12
            int r11 = r14 + 1
            if (r13 > r11) goto L_0x0118
            r11 = r8[r10]
            r14 = r8[r10]
            r4 = r14[r12]
            org.apache.commons.math3.fraction.BigFraction r4 = r4.divide(r13)
            r11[r12] = r4
            int r13 = r13 + 1
            r4 = 1
            r11 = 2
            goto L_0x0101
        L_0x0118:
            int r12 = r12 + 1
            r4 = 1
            r11 = 2
            goto L_0x00f7
        L_0x011d:
            int r10 = r10 + 1
            r4 = 1
            r11 = 2
            goto L_0x00f4
        L_0x0122:
            org.apache.commons.math3.linear.Array2DRowFieldMatrix r4 = new org.apache.commons.math3.linear.Array2DRowFieldMatrix
            org.apache.commons.math3.fraction.BigFractionField r10 = org.apache.commons.math3.fraction.BigFractionField.getInstance()
            r4.<init>(r10, (T[][]) r8)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest.createExactH(double, int):org.apache.commons.math3.linear.FieldMatrix");
    }

    private RealMatrix createRoundedH(double d, int n) throws NumberIsTooLargeException {
        int i = n;
        int k = (int) Math.ceil(((double) i) * d);
        int i2 = 1;
        int m = (k * 2) - 1;
        double h = ((double) k) - (((double) i) * d);
        if (h >= 1.0d) {
            throw new NumberIsTooLargeException(Double.valueOf(h), Double.valueOf(1.0d), false);
        }
        double[][] Hdata = (double[][]) Array.newInstance(double.class, new int[]{m, m});
        for (int i3 = 0; i3 < m; i3++) {
            for (int j = 0; j < m; j++) {
                if ((i3 - j) + 1 < 0) {
                    Hdata[i3][j] = 0.0d;
                } else {
                    Hdata[i3][j] = 1.0d;
                }
            }
        }
        double[] hPowers = new double[m];
        hPowers[0] = h;
        for (int i4 = 1; i4 < m; i4++) {
            hPowers[i4] = hPowers[i4 - 1] * h;
        }
        for (int i5 = 0; i5 < m; i5++) {
            Hdata[i5][0] = Hdata[i5][0] - hPowers[i5];
            double[] dArr = Hdata[m - 1];
            dArr[i5] = dArr[i5] - hPowers[(m - i5) - 1];
        }
        if (Double.compare(h, 0.5d) > 0) {
            double[] dArr2 = Hdata[m - 1];
            dArr2[0] = dArr2[0] + FastMath.pow((2.0d * h) - 1.0d, m);
        }
        int i6 = 0;
        while (i6 < m) {
            int j2 = 0;
            while (j2 < i6 + 1) {
                if ((i6 - j2) + i2 > 0) {
                    int g = 2;
                    while (g <= (i6 - j2) + i2) {
                        double[] dArr3 = Hdata[i6];
                        double h2 = h;
                        dArr3[j2] = dArr3[j2] / ((double) g);
                        g++;
                        h = h2;
                        i2 = 1;
                    }
                }
                j2++;
                h = h;
                i2 = 1;
            }
            i6++;
            i2 = 1;
        }
        return MatrixUtils.createRealMatrix(Hdata);
    }

    private void checkArray(double[] array) {
        if (array == null) {
            throw new NullArgumentException(LocalizedFormats.NULL_NOT_ALLOWED, new Object[0]);
        } else if (array.length < 2) {
            throw new InsufficientDataException(LocalizedFormats.INSUFFICIENT_OBSERVED_POINTS_IN_SAMPLE, Integer.valueOf(array.length), Integer.valueOf(2));
        }
    }

    public double ksSum(double t, double tolerance, int maxIterations) {
        int i = maxIterations;
        if (t == 0.0d) {
            return 0.0d;
        }
        double x = -2.0d * t * t;
        int sign = -1;
        long i2 = 1;
        double partialSum = 0.5d;
        double delta = 1.0d;
        while (delta > tolerance && i2 < ((long) i)) {
            delta = FastMath.exp(((double) i2) * x * ((double) i2));
            partialSum += ((double) sign) * delta;
            sign *= -1;
            i2++;
        }
        if (i2 != ((long) i)) {
            return 2.0d * partialSum;
        }
        throw new TooManyIterationsException(Integer.valueOf(maxIterations));
    }

    private static long calculateIntegralD(double d, int n, int m, boolean strict) {
        long nm = ((long) n) * ((long) m);
        long upperBound = (long) FastMath.ceil((d - 1.0E-12d) * ((double) nm));
        long lowerBound = (long) FastMath.floor((1.0E-12d + d) * ((double) nm));
        if (!strict || lowerBound != upperBound) {
            return upperBound;
        }
        return 1 + upperBound;
    }

    public double exactP(double d, int n, int m, boolean strict) {
        return 1.0d - (m50n(m, n, m, n, calculateIntegralD(d, m, n, strict), strict) / CombinatoricsUtils.binomialCoefficientDouble(n + m, m));
    }

    public double approximateP(double d, int n, int m) {
        double dm = (double) m;
        double dn = (double) n;
        return 1.0d - ksSum(d * FastMath.sqrt((dm * dn) / (dm + dn)), KS_SUM_CAUCHY_CRITERION, MAXIMUM_PARTIAL_SUM_COUNT);
    }

    static void fillBooleanArrayRandomlyWithFixedNumberTrueValues(boolean[] b, int numberOfTrueValues, RandomGenerator rng2) {
        Arrays.fill(b, true);
        for (int k = numberOfTrueValues; k < b.length; k++) {
            int r = rng2.nextInt(k + 1);
            b[b[r] ? r : k] = false;
        }
    }

    public double monteCarloP(double d, int n, int m, boolean strict, int iterations) {
        return integralMonteCarloP(calculateIntegralD(d, n, m, strict), n, m, iterations);
    }

    private double integralMonteCarloP(long d, int n, int m, int iterations) {
        long j = d;
        int i = iterations;
        int nn = FastMath.max(n, m);
        int mm = FastMath.min(n, m);
        boolean[] b = new boolean[(nn + mm)];
        int tail = 0;
        for (int i2 = 0; i2 < i; i2++) {
            fillBooleanArrayRandomlyWithFixedNumberTrueValues(b, nn, this.rng);
            long curD = 0;
            int j2 = 0;
            while (true) {
                if (j2 >= b.length) {
                    break;
                }
                if (b[j2]) {
                    curD += (long) mm;
                    if (curD >= j) {
                        tail++;
                        break;
                    }
                } else {
                    curD -= (long) nn;
                    if (curD <= (-j)) {
                        tail++;
                        break;
                    }
                }
                j2++;
            }
        }
        return ((double) tail) / ((double) i);
    }

    private static void fixTies(double[] x, double[] y) {
        boolean ties;
        double[] dArr = x;
        double[] dArr2 = y;
        double[] values = MathArrays.unique(MathArrays.concatenate(dArr, dArr2));
        if (values.length != dArr.length + dArr2.length) {
            double minDelta = 1.0d;
            double prev = values[0];
            for (int i = 1; i < values.length; i++) {
                double delta = prev - values[i];
                if (delta < minDelta) {
                    minDelta = delta;
                }
                prev = values[i];
            }
            double minDelta2 = minDelta / 2.0d;
            UniformRealDistribution uniformRealDistribution = new UniformRealDistribution((RandomGenerator) new JDKRandomGenerator(100), -minDelta2, minDelta2);
            int ct = 0;
            do {
                jitter(dArr, uniformRealDistribution);
                jitter(dArr2, uniformRealDistribution);
                ties = hasTies(x, y);
                ct++;
                if (!ties) {
                    break;
                }
            } while (ct < 1000);
            if (ties) {
                throw new MathInternalError();
            }
        }
    }

    private static boolean hasTies(double[] x, double[] y) {
        HashSet<Double> values = new HashSet<>();
        for (double valueOf : x) {
            if (!values.add(Double.valueOf(valueOf))) {
                return true;
            }
        }
        for (double valueOf2 : y) {
            if (!values.add(Double.valueOf(valueOf2))) {
                return true;
            }
        }
        return false;
    }

    private static void jitter(double[] data, RealDistribution dist) {
        for (int i = 0; i < data.length; i++) {
            data[i] = data[i] + dist.sample();
        }
    }

    /* renamed from: c */
    private static int m49c(int i, int j, int m, int n, long cmn, boolean strict) {
        int i2 = 0;
        if (strict) {
            if (FastMath.abs((((long) i) * ((long) n)) - (((long) j) * ((long) m))) <= cmn) {
                i2 = 1;
            }
            return i2;
        }
        if (FastMath.abs((((long) i) * ((long) n)) - (((long) j) * ((long) m))) < cmn) {
            i2 = 1;
        }
        return i2;
    }

    /* renamed from: n */
    private static double m50n(int i, int j, int m, int n, long cnm, boolean strict) {
        int i2 = n;
        double[] lag = new double[i2];
        double last = 0.0d;
        int k = 0;
        while (true) {
            int k2 = k;
            if (k2 >= i2) {
                break;
            }
            lag[k2] = (double) m49c(0, k2 + 1, m, i2, cnm, strict);
            k = k2 + 1;
        }
        int l = 1;
        while (true) {
            int k3 = l;
            if (k3 <= i) {
                last = (double) m49c(k3, 0, m, i2, cnm, strict);
                int l2 = 1;
                while (true) {
                    int l3 = l2;
                    if (l3 > j) {
                        break;
                    }
                    lag[l3 - 1] = ((double) m49c(k3, l3, m, i2, cnm, strict)) * (lag[l3 - 1] + last);
                    last = lag[l3 - 1];
                    l2 = l3 + 1;
                }
                l = k3 + 1;
            } else {
                int i3 = j;
                return last;
            }
        }
    }
}

package org.apache.commons.math3.distribution;

import java.io.Serializable;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.FastMath;

public class KolmogorovSmirnovDistribution implements Serializable {
    private static final long serialVersionUID = -4670676796862967187L;

    /* renamed from: n */
    private int f549n;

    public KolmogorovSmirnovDistribution(int n) throws NotStrictlyPositiveException {
        if (n <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.NOT_POSITIVE_NUMBER_OF_SAMPLES, Integer.valueOf(n));
        }
        this.f549n = n;
    }

    public double cdf(double d) throws MathArithmeticException {
        return cdf(d, false);
    }

    public double cdfExact(double d) throws MathArithmeticException {
        return cdf(d, true);
    }

    public double cdf(double d, boolean exact) throws MathArithmeticException {
        double ninv = 1.0d / ((double) this.f549n);
        double ninvhalf = 0.5d * ninv;
        if (d <= ninvhalf) {
            return 0.0d;
        }
        if (ninvhalf < d && d <= ninv) {
            double res = 1.0d;
            double f = (2.0d * d) - ninv;
            for (int i = 1; i <= this.f549n; i++) {
                res *= ((double) i) * f;
            }
            return res;
        } else if (1.0d - ninv <= d && d < 1.0d) {
            return 1.0d - (FastMath.pow(1.0d - d, this.f549n) * 2.0d);
        } else {
            if (1.0d <= d) {
                return 1.0d;
            }
            return exact ? exactK(d) : roundedK(d);
        }
    }

    private double exactK(double d) throws MathArithmeticException {
        int k = (int) FastMath.ceil(((double) this.f549n) * d);
        BigFraction pFrac = (BigFraction) createH(d).power(this.f549n).getEntry(k - 1, k - 1);
        for (int i = 1; i <= this.f549n; i++) {
            pFrac = pFrac.multiply(i).divide(this.f549n);
        }
        return pFrac.bigDecimalValue(20, 4).doubleValue();
    }

    private double roundedK(double d) throws MathArithmeticException {
        int k = (int) FastMath.ceil(((double) this.f549n) * d);
        FieldMatrix<BigFraction> HBigFraction = createH(d);
        int m = HBigFraction.getRowDimension();
        RealMatrix H = new Array2DRowRealMatrix(m, m);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                H.setEntry(i, j, ((BigFraction) HBigFraction.getEntry(i, j)).doubleValue());
            }
        }
        double pFrac = H.power(this.f549n).getEntry(k - 1, k - 1);
        for (int i2 = 1; i2 <= this.f549n; i2++) {
            pFrac *= ((double) i2) / ((double) this.f549n);
        }
        return pFrac;
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x007a  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x009c A[LOOP:2: B:31:0x009a->B:32:0x009c, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00ac A[LOOP:3: B:34:0x00aa->B:35:0x00ac, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00db  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00fa  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private org.apache.commons.math3.linear.FieldMatrix<org.apache.commons.math3.fraction.BigFraction> createH(double r20) throws org.apache.commons.math3.exception.NumberIsTooLargeException, org.apache.commons.math3.fraction.FractionConversionException {
        /*
            r19 = this;
            r1 = r19
            int r2 = r1.f549n
            double r2 = (double) r2
            double r2 = r2 * r20
            double r2 = org.apache.commons.math3.util.FastMath.ceil(r2)
            int r2 = (int) r2
            int r3 = r2 * 2
            r4 = 1
            int r3 = r3 - r4
            double r5 = (double) r2
            int r7 = r1.f549n
            double r7 = (double) r7
            double r7 = r7 * r20
            double r5 = r5 - r7
            r7 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            int r9 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            r12 = 0
            if (r9 < 0) goto L_0x002c
            org.apache.commons.math3.exception.NumberIsTooLargeException r4 = new org.apache.commons.math3.exception.NumberIsTooLargeException
            java.lang.Double r9 = java.lang.Double.valueOf(r5)
            java.lang.Double r7 = java.lang.Double.valueOf(r7)
            r4.<init>(r9, r7, r12)
            throw r4
        L_0x002c:
            r7 = 0
            org.apache.commons.math3.fraction.BigFraction r8 = new org.apache.commons.math3.fraction.BigFraction     // Catch:{ FractionConversionException -> 0x0043 }
            r13 = 4307583784117748259(0x3bc79ca10c924223, double:1.0E-20)
            r15 = 10000(0x2710, float:1.4013E-41)
            r9 = r8
            r10 = r5
            r16 = 0
            r12 = r13
            r14 = r15
            r9.<init>(r10, r12, r14)     // Catch:{ FractionConversionException -> 0x0041 }
            r7 = r8
            goto L_0x006b
        L_0x0041:
            r0 = move-exception
            goto L_0x0046
        L_0x0043:
            r0 = move-exception
            r16 = 0
        L_0x0046:
            r8 = r0
            org.apache.commons.math3.fraction.BigFraction r15 = new org.apache.commons.math3.fraction.BigFraction     // Catch:{ FractionConversionException -> 0x0057 }
            r12 = 4457293557087583675(0x3ddb7cdfd9d7bdbb, double:1.0E-10)
            r14 = 10000(0x2710, float:1.4013E-41)
            r9 = r15
            r10 = r5
            r9.<init>(r10, r12, r14)     // Catch:{ FractionConversionException -> 0x0057 }
            r7 = r15
            goto L_0x006b
        L_0x0057:
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
        L_0x006b:
            int[] r8 = new int[]{r3, r3}
            java.lang.Class<org.apache.commons.math3.fraction.BigFraction> r9 = org.apache.commons.math3.fraction.BigFraction.class
            java.lang.Object r8 = java.lang.reflect.Array.newInstance(r9, r8)
            org.apache.commons.math3.fraction.BigFraction[][] r8 = (org.apache.commons.math3.fraction.BigFraction[][]) r8
            r9 = 0
        L_0x0078:
            if (r9 >= r3) goto L_0x0095
            r10 = 0
        L_0x007b:
            if (r10 >= r3) goto L_0x0092
            int r11 = r9 - r10
            int r11 = r11 + r4
            if (r11 >= 0) goto L_0x0089
            r11 = r8[r9]
            org.apache.commons.math3.fraction.BigFraction r12 = org.apache.commons.math3.fraction.BigFraction.ZERO
            r11[r10] = r12
            goto L_0x008f
        L_0x0089:
            r11 = r8[r9]
            org.apache.commons.math3.fraction.BigFraction r12 = org.apache.commons.math3.fraction.BigFraction.ONE
            r11[r10] = r12
        L_0x008f:
            int r10 = r10 + 1
            goto L_0x007b
        L_0x0092:
            int r9 = r9 + 1
            goto L_0x0078
        L_0x0095:
            org.apache.commons.math3.fraction.BigFraction[] r9 = new org.apache.commons.math3.fraction.BigFraction[r3]
            r9[r16] = r7
            r10 = 1
        L_0x009a:
            if (r10 >= r3) goto L_0x00a9
            int r11 = r10 + -1
            r11 = r9[r11]
            org.apache.commons.math3.fraction.BigFraction r11 = r7.multiply(r11)
            r9[r10] = r11
            int r10 = r10 + 1
            goto L_0x009a
        L_0x00a9:
            r10 = 0
        L_0x00aa:
            if (r10 >= r3) goto L_0x00d2
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
            goto L_0x00aa
        L_0x00d2:
            org.apache.commons.math3.fraction.BigFraction r10 = org.apache.commons.math3.fraction.BigFraction.ONE_HALF
            int r10 = r7.compareTo(r10)
            r11 = 2
            if (r10 != r4) goto L_0x00f7
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
        L_0x00f7:
            r10 = 0
        L_0x00f8:
            if (r10 >= r3) goto L_0x0126
            r12 = 0
        L_0x00fb:
            int r13 = r10 + 1
            if (r12 >= r13) goto L_0x0121
            int r13 = r10 - r12
            int r13 = r13 + r4
            if (r13 <= 0) goto L_0x011c
            r13 = 2
        L_0x0105:
            int r14 = r10 - r12
            int r11 = r14 + 1
            if (r13 > r11) goto L_0x011c
            r11 = r8[r10]
            r14 = r8[r10]
            r4 = r14[r12]
            org.apache.commons.math3.fraction.BigFraction r4 = r4.divide(r13)
            r11[r12] = r4
            int r13 = r13 + 1
            r4 = 1
            r11 = 2
            goto L_0x0105
        L_0x011c:
            int r12 = r12 + 1
            r4 = 1
            r11 = 2
            goto L_0x00fb
        L_0x0121:
            int r10 = r10 + 1
            r4 = 1
            r11 = 2
            goto L_0x00f8
        L_0x0126:
            org.apache.commons.math3.linear.Array2DRowFieldMatrix r4 = new org.apache.commons.math3.linear.Array2DRowFieldMatrix
            org.apache.commons.math3.fraction.BigFractionField r10 = org.apache.commons.math3.fraction.BigFractionField.getInstance()
            r4.<init>(r10, (T[][]) r8)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.distribution.KolmogorovSmirnovDistribution.createH(double):org.apache.commons.math3.linear.FieldMatrix");
    }
}

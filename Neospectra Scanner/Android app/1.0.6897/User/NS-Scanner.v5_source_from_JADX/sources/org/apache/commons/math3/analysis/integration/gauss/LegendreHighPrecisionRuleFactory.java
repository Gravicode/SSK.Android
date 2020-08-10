package org.apache.commons.math3.analysis.integration.gauss;

import java.math.BigDecimal;
import java.math.MathContext;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.Pair;

public class LegendreHighPrecisionRuleFactory extends BaseRuleFactory<BigDecimal> {
    private final MathContext mContext;
    private final BigDecimal minusOne;
    private final BigDecimal oneHalf;
    private final BigDecimal two;

    public LegendreHighPrecisionRuleFactory() {
        this(MathContext.DECIMAL128);
    }

    public LegendreHighPrecisionRuleFactory(MathContext mContext2) {
        this.mContext = mContext2;
        this.two = new BigDecimal("2", mContext2);
        this.minusOne = new BigDecimal("-1", mContext2);
        this.oneHalf = new BigDecimal("0.5", mContext2);
    }

    /* access modifiers changed from: protected */
    public Pair<BigDecimal[], BigDecimal[]> computeRule(int numberOfPoints) throws DimensionMismatchException {
        BigDecimal a;
        BigDecimal b;
        int i = numberOfPoints;
        int i2 = 1;
        if (i == 1) {
            return new Pair<>(new BigDecimal[]{BigDecimal.ZERO}, new BigDecimal[]{this.two});
        }
        BigDecimal[] previousPoints = (BigDecimal[]) getRuleInternal(i - 1).getFirst();
        BigDecimal[] points = new BigDecimal[i];
        BigDecimal[] weights = new BigDecimal[i];
        int iMax = i / 2;
        int i3 = 0;
        while (i3 < iMax) {
            BigDecimal a2 = i3 == 0 ? this.minusOne : previousPoints[i3 - 1];
            BigDecimal b2 = iMax == i2 ? BigDecimal.ONE : previousPoints[i3];
            BigDecimal pma = BigDecimal.ONE;
            BigDecimal pa = a2;
            BigDecimal pb = b2;
            BigDecimal pmb = BigDecimal.ONE;
            BigDecimal ppb = pma;
            int j = 1;
            while (j < i) {
                BigDecimal[] previousPoints2 = previousPoints;
                BigDecimal b_two_j_p_1 = new BigDecimal((j * 2) + 1, this.mContext);
                BigDecimal b_j = new BigDecimal(j, this.mContext);
                int iMax2 = iMax;
                BigDecimal[] weights2 = weights;
                BigDecimal b_j_p_1 = new BigDecimal(j + 1, this.mContext);
                BigDecimal tmp1 = pa.multiply(a2.multiply(b_two_j_p_1, this.mContext), this.mContext);
                BigDecimal bigDecimal = ppb;
                BigDecimal bigDecimal2 = tmp1;
                BigDecimal ppa = tmp1.subtract(ppb.multiply(b_j, this.mContext), this.mContext).divide(b_j_p_1, this.mContext);
                BigDecimal bigDecimal3 = b_two_j_p_1;
                BigDecimal bigDecimal4 = pmb;
                ppb = pa;
                pa = ppa;
                BigDecimal pmb2 = pb;
                pb = pb.multiply(b2.multiply(b_two_j_p_1, this.mContext), this.mContext).subtract(pmb.multiply(b_j, this.mContext), this.mContext).divide(b_j_p_1, this.mContext);
                j++;
                pmb = pmb2;
                previousPoints = previousPoints2;
                iMax = iMax2;
                weights = weights2;
            }
            BigDecimal bigDecimal5 = pmb;
            BigDecimal[] previousPoints3 = previousPoints;
            BigDecimal[] weights3 = weights;
            int iMax3 = iMax;
            BigDecimal bigDecimal6 = ppb;
            BigDecimal c = a2.add(b2, this.mContext).multiply(this.oneHalf, this.mContext);
            BigDecimal pc = c;
            BigDecimal pc2 = BigDecimal.ONE;
            BigDecimal c2 = c;
            boolean done = false;
            while (!done) {
                BigDecimal tmp12 = b2.subtract(a2, this.mContext);
                BigDecimal tmp2 = c2.ulp().multiply(BigDecimal.TEN, this.mContext);
                done = tmp12.compareTo(tmp2) <= 0;
                pc = c2;
                BigDecimal tmp13 = tmp2;
                BigDecimal pmc = BigDecimal.ONE;
                int j2 = 1;
                while (j2 < i) {
                    BigDecimal bigDecimal7 = tmp12;
                    BigDecimal a3 = a2;
                    BigDecimal b_two_j_p_12 = new BigDecimal((j2 * 2) + 1, this.mContext);
                    BigDecimal b_j2 = new BigDecimal(j2, this.mContext);
                    BigDecimal b3 = b2;
                    BigDecimal bigDecimal8 = tmp13;
                    BigDecimal b_j_p_12 = new BigDecimal(j2 + 1, this.mContext);
                    BigDecimal tmp14 = pc.multiply(c2.multiply(b_two_j_p_12, this.mContext), this.mContext);
                    BigDecimal tmp22 = pmc.multiply(b_j2, this.mContext);
                    BigDecimal bigDecimal9 = b_two_j_p_12;
                    BigDecimal bigDecimal10 = b_j2;
                    pmc = pc;
                    pc = tmp14.subtract(tmp22, this.mContext).divide(b_j_p_12, this.mContext);
                    j2++;
                    tmp12 = tmp14;
                    tmp13 = tmp22;
                    a2 = a3;
                    b2 = b3;
                }
                BigDecimal bigDecimal11 = tmp12;
                BigDecimal a4 = a2;
                BigDecimal b4 = b2;
                BigDecimal bigDecimal12 = tmp13;
                if (!done) {
                    if (pa.signum() * pc.signum() <= 0) {
                        b = c2;
                        BigDecimal pmb3 = pmc;
                        BigDecimal pb2 = pc;
                        a = a4;
                    } else {
                        a = c2;
                        BigDecimal pma2 = pmc;
                        pa = pc;
                        b = b4;
                    }
                    c2 = a.add(b, this.mContext).multiply(this.oneHalf, this.mContext);
                } else {
                    a = a4;
                    b = b4;
                }
                pc2 = pmc;
            }
            BigDecimal bigDecimal13 = a2;
            BigDecimal bigDecimal14 = b2;
            BigDecimal tmp23 = BigDecimal.ONE.subtract(c2.pow(2, this.mContext), this.mContext).multiply(this.two, this.mContext).divide(pc2.subtract(c2.multiply(pc, this.mContext), this.mContext).multiply(new BigDecimal(i, this.mContext)).pow(2, this.mContext), this.mContext);
            points[i3] = c2;
            weights3[i3] = tmp23;
            int idx = (i - i3) - 1;
            points[idx] = c2.negate(this.mContext);
            weights3[idx] = tmp23;
            i3++;
            previousPoints = previousPoints3;
            iMax = iMax3;
            weights = weights3;
            i2 = 1;
        }
        BigDecimal[] weights4 = weights;
        int iMax4 = iMax;
        int j3 = 1;
        if (i % 2 != 0) {
            BigDecimal pmc2 = BigDecimal.ONE;
            while (true) {
                int j4 = j3;
                if (j4 >= i) {
                    break;
                }
                pmc2 = pmc2.multiply(new BigDecimal(j4, this.mContext), this.mContext).divide(new BigDecimal(j4 + 1, this.mContext), this.mContext).negate(this.mContext);
                j3 = j4 + 2;
            }
            BigDecimal tmp24 = this.two.divide(pmc2.multiply(new BigDecimal(i, this.mContext), this.mContext).pow(2, this.mContext), this.mContext);
            points[iMax4] = BigDecimal.ZERO;
            weights4[iMax4] = tmp24;
        }
        return new Pair<>(points, weights4);
    }
}

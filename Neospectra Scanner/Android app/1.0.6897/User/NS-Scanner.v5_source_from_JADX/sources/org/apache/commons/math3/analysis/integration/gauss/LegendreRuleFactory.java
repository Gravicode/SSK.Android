package org.apache.commons.math3.analysis.integration.gauss;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.Pair;

public class LegendreRuleFactory extends BaseRuleFactory<Double> {
    /* access modifiers changed from: protected */
    public Pair<Double[], Double[]> computeRule(int numberOfPoints) throws DimensionMismatchException {
        int i = numberOfPoints;
        int i2 = 1;
        if (i == 1) {
            return new Pair<>(new Double[]{Double.valueOf(0.0d)}, new Double[]{Double.valueOf(2.0d)});
        }
        Double[] previousPoints = (Double[]) getRuleInternal(i - 1).getFirst();
        Double[] points = new Double[i];
        Double[] weights = new Double[i];
        int iMax = i / 2;
        int i3 = 0;
        while (i3 < iMax) {
            double a = i3 == 0 ? -1.0d : previousPoints[i3 - 1].doubleValue();
            double b = iMax == i2 ? 1.0d : previousPoints[i3].doubleValue();
            double pma = 1.0d;
            double pa = a;
            double pmb = 1.0d;
            double pb = b;
            int j = 1;
            while (j < i) {
                int two_j_p_1 = (j * 2) + 1;
                int j_p_1 = j + 1;
                double ppa = (((((double) two_j_p_1) * a) * pa) - (((double) j) * pma)) / ((double) j_p_1);
                pma = pa;
                pa = ppa;
                pmb = pb;
                pb = (((((double) two_j_p_1) * b) * pb) - (((double) j) * pmb)) / ((double) j_p_1);
                j++;
                previousPoints = previousPoints;
                weights = weights;
                iMax = iMax;
            }
            Double[] previousPoints2 = previousPoints;
            Double[] weights2 = weights;
            int iMax2 = iMax;
            double c = (a + b) * 0.5d;
            double d = pb;
            double pb2 = pmb;
            double pa2 = pa;
            double pa3 = pma;
            double pc = c;
            double pmc = 1.0d;
            double c2 = c;
            boolean done = false;
            while (!done) {
                done = b - a <= Math.ulp(c2);
                pmc = 1.0d;
                pc = c2;
                int j2 = 1;
                while (j2 < i) {
                    pmc = pc;
                    pc = (((((double) ((j2 * 2) + 1)) * c2) * pc) - (((double) j2) * pmc)) / ((double) (j2 + 1));
                    j2++;
                }
                if (!done) {
                    if (pa2 * pc <= 0.0d) {
                        double d2 = pmc;
                        double d3 = pc;
                        b = c2;
                    } else {
                        double d4 = pmc;
                        pa2 = pc;
                        a = c2;
                    }
                    c2 = (a + b) * 0.5d;
                }
            }
            double d5 = ((double) i) * (pmc - (c2 * pc));
            double w = ((1.0d - (c2 * c2)) * 2.0d) / (d5 * d5);
            points[i3] = Double.valueOf(c2);
            weights2[i3] = Double.valueOf(w);
            int idx = (i - i3) - 1;
            boolean z = done;
            double d6 = d5;
            points[idx] = Double.valueOf(-c2);
            weights2[idx] = Double.valueOf(w);
            i3++;
            previousPoints = previousPoints2;
            weights = weights2;
            iMax = iMax2;
            i2 = 1;
        }
        Double[] weights3 = weights;
        int iMax3 = iMax;
        int j3 = 1;
        if (i % 2 != 0) {
            double pmc2 = 1.0d;
            while (true) {
                int j4 = j3;
                if (j4 >= i) {
                    break;
                }
                pmc2 = (((double) (-j4)) * pmc2) / ((double) (j4 + 1));
                j3 = j4 + 2;
            }
            double d7 = ((double) i) * pmc2;
            double w2 = 2.0d / (d7 * d7);
            points[iMax3] = Double.valueOf(0.0d);
            weights3[iMax3] = Double.valueOf(w2);
        }
        return new Pair<>(points, weights3);
    }
}

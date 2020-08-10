package org.apache.commons.math3.analysis.integration.gauss;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Pair;

public class HermiteRuleFactory extends BaseRuleFactory<Double> {

    /* renamed from: H0 */
    private static final double f516H0 = 0.7511255444649425d;

    /* renamed from: H1 */
    private static final double f517H1 = 1.0622519320271968d;
    private static final double SQRT_PI = 1.772453850905516d;

    /* access modifiers changed from: protected */
    public Pair<Double[], Double[]> computeRule(int numberOfPoints) throws DimensionMismatchException {
        Double[] points;
        int i = numberOfPoints;
        int i2 = 1;
        if (i == 1) {
            return new Pair<>(new Double[]{Double.valueOf(0.0d)}, new Double[]{Double.valueOf(SQRT_PI)});
        }
        int lastNumPoints = i - 1;
        Double[] previousPoints = (Double[]) getRuleInternal(lastNumPoints).getFirst();
        Double[] points2 = new Double[i];
        Double[] weights = new Double[i];
        double sqrtTwoTimesLastNumPoints = FastMath.sqrt((double) (lastNumPoints * 2));
        double sqrtTwoTimesNumPoints = FastMath.sqrt((double) (i * 2));
        int iMax = i / 2;
        int i3 = 0;
        while (i3 < iMax) {
            double a = i3 == 0 ? -sqrtTwoTimesLastNumPoints : previousPoints[i3 - 1].doubleValue();
            double b = iMax == i2 ? -0.5d : previousPoints[i3].doubleValue();
            double hma = f516H0;
            double ha = a * f517H1;
            double hmb = f516H0;
            double hb = b * f517H1;
            int j = 1;
            while (j < i) {
                Double[] previousPoints2 = previousPoints;
                double jp1 = (double) (j + 1);
                double sqrtTwoTimesLastNumPoints2 = sqrtTwoTimesLastNumPoints;
                double s = FastMath.sqrt(2.0d / jp1);
                int lastNumPoints2 = lastNumPoints;
                double sm = FastMath.sqrt(((double) j) / jp1);
                double hpa = ((s * a) * ha) - (sm * hma);
                hma = ha;
                ha = hpa;
                hmb = hb;
                hb = ((s * b) * hb) - (sm * hmb);
                j++;
                previousPoints = previousPoints2;
                sqrtTwoTimesLastNumPoints = sqrtTwoTimesLastNumPoints2;
                lastNumPoints = lastNumPoints2;
            }
            int lastNumPoints3 = lastNumPoints;
            Double[] previousPoints3 = previousPoints;
            double sqrtTwoTimesLastNumPoints3 = sqrtTwoTimesLastNumPoints;
            double c = (a + b) * 0.5d;
            double d = c * f517H1;
            double hc = hb;
            double hb2 = hmb;
            double ha2 = ha;
            double ha3 = hma;
            double hmc = 0.7511255444649425d;
            double c2 = c;
            double a2 = a;
            boolean done = false;
            while (!done) {
                done = b - a2 <= Math.ulp(c2);
                hmc = f516H0;
                double hc2 = c2 * f517H1;
                int j2 = 1;
                while (j2 < i) {
                    double jp12 = (double) (j2 + 1);
                    hmc = hc2;
                    hc2 = ((FastMath.sqrt(2.0d / jp12) * c2) * hc2) - (FastMath.sqrt(((double) j2) / jp12) * hmc);
                    j2++;
                    a2 = a2;
                    points = points;
                }
                double a3 = a2;
                Double[] points3 = points;
                if (!done) {
                    if (ha2 * hc2 < 0.0d) {
                        b = c2;
                        double d2 = hmc;
                        double d3 = hc2;
                        a2 = a3;
                    } else {
                        a2 = c2;
                        double d4 = hmc;
                        ha2 = hc2;
                    }
                    c2 = (a2 + b) * 0.5d;
                    points = points3;
                } else {
                    a2 = a3;
                    points = points3;
                }
            }
            Double[] points4 = points;
            double a4 = sqrtTwoTimesNumPoints * hmc;
            double w = 2.0d / (a4 * a4);
            points4[i3] = Double.valueOf(c2);
            weights[i3] = Double.valueOf(w);
            int idx = lastNumPoints3 - i3;
            points4[idx] = Double.valueOf(-c2);
            weights[idx] = Double.valueOf(w);
            i3++;
            previousPoints = previousPoints3;
            sqrtTwoTimesLastNumPoints = sqrtTwoTimesLastNumPoints3;
            lastNumPoints = lastNumPoints3;
            points2 = points4;
            i2 = 1;
        }
        Double[] dArr = previousPoints;
        Double[] points5 = points;
        double d5 = sqrtTwoTimesLastNumPoints;
        if (i % 2 != 0) {
            double hm = f516H0;
            int j3 = 1;
            while (true) {
                int j4 = j3;
                if (j4 >= i) {
                    break;
                }
                hm *= -FastMath.sqrt(((double) j4) / ((double) (j4 + 1)));
                j3 = j4 + 2;
            }
            double d6 = sqrtTwoTimesNumPoints * hm;
            double w2 = 2.0d / (d6 * d6);
            points5[iMax] = Double.valueOf(0.0d);
            weights[iMax] = Double.valueOf(w2);
        }
        return new Pair<>(points5, weights);
    }
}

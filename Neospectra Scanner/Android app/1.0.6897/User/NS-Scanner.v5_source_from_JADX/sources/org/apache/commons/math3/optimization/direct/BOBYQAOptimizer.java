package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.util.FastMath;

@Deprecated
public class BOBYQAOptimizer extends BaseAbstractMultivariateSimpleBoundsOptimizer<MultivariateFunction> implements MultivariateOptimizer {
    public static final double DEFAULT_INITIAL_RADIUS = 10.0d;
    public static final double DEFAULT_STOPPING_RADIUS = 1.0E-8d;
    private static final double HALF = 0.5d;
    public static final int MINIMUM_PROBLEM_DIMENSION = 2;
    private static final double MINUS_ONE = -1.0d;
    private static final double ONE = 1.0d;
    private static final double ONE_OVER_A_THOUSAND = 0.001d;
    private static final double ONE_OVER_EIGHT = 0.125d;
    private static final double ONE_OVER_FOUR = 0.25d;
    private static final double ONE_OVER_TEN = 0.1d;
    private static final double SIXTEEN = 16.0d;
    private static final double TEN = 10.0d;
    private static final double TWO = 2.0d;
    private static final double TWO_HUNDRED_FIFTY = 250.0d;
    private static final double ZERO = 0.0d;
    private ArrayRealVector alternativeNewPoint;
    private Array2DRowRealMatrix bMatrix;
    private double[] boundDifference;
    private ArrayRealVector currentBest;
    private ArrayRealVector fAtInterpolationPoints;
    private ArrayRealVector gradientAtTrustRegionCenter;
    private double initialTrustRegionRadius;
    private Array2DRowRealMatrix interpolationPoints;
    private boolean isMinimize;
    private ArrayRealVector lagrangeValuesAtNewPoint;
    private ArrayRealVector lowerDifference;
    private ArrayRealVector modelSecondDerivativesParameters;
    private ArrayRealVector modelSecondDerivativesValues;
    private ArrayRealVector newPoint;
    private final int numberOfInterpolationPoints;
    private ArrayRealVector originShift;
    private final double stoppingTrustRegionRadius;
    private ArrayRealVector trialStepPoint;
    private int trustRegionCenterInterpolationPointIndex;
    private ArrayRealVector trustRegionCenterOffset;
    private ArrayRealVector upperDifference;
    private Array2DRowRealMatrix zMatrix;

    private static class PathIsExploredException extends RuntimeException {
        private static final String PATH_IS_EXPLORED = "If this exception is thrown, just remove it from the code";
        private static final long serialVersionUID = 745350979634801853L;

        PathIsExploredException() {
            StringBuilder sb = new StringBuilder();
            sb.append("If this exception is thrown, just remove it from the code ");
            sb.append(BOBYQAOptimizer.caller(3));
            super(sb.toString());
        }
    }

    public BOBYQAOptimizer(int numberOfInterpolationPoints2) {
        this(numberOfInterpolationPoints2, 10.0d, 1.0E-8d);
    }

    public BOBYQAOptimizer(int numberOfInterpolationPoints2, double initialTrustRegionRadius2, double stoppingTrustRegionRadius2) {
        super(null);
        this.numberOfInterpolationPoints = numberOfInterpolationPoints2;
        this.initialTrustRegionRadius = initialTrustRegionRadius2;
        this.stoppingTrustRegionRadius = stoppingTrustRegionRadius2;
    }

    /* access modifiers changed from: protected */
    public PointValuePair doOptimize() {
        double[] lowerBound = getLowerBound();
        double[] upperBound = getUpperBound();
        setup(lowerBound, upperBound);
        this.isMinimize = getGoalType() == GoalType.MINIMIZE;
        this.currentBest = new ArrayRealVector(getStartPoint());
        double value = bobyqa(lowerBound, upperBound);
        return new PointValuePair(this.currentBest.getDataRef(), this.isMinimize ? value : -value);
    }

    private double bobyqa(double[] lowerBound, double[] upperBound) {
        printMethod();
        int n = this.currentBest.getDimension();
        for (int j = 0; j < n; j++) {
            double boundDiff = this.boundDifference[j];
            this.lowerDifference.setEntry(j, lowerBound[j] - this.currentBest.getEntry(j));
            this.upperDifference.setEntry(j, upperBound[j] - this.currentBest.getEntry(j));
            if (this.lowerDifference.getEntry(j) >= (-this.initialTrustRegionRadius)) {
                if (this.lowerDifference.getEntry(j) >= 0.0d) {
                    this.currentBest.setEntry(j, lowerBound[j]);
                    this.lowerDifference.setEntry(j, 0.0d);
                    this.upperDifference.setEntry(j, boundDiff);
                } else {
                    this.currentBest.setEntry(j, lowerBound[j] + this.initialTrustRegionRadius);
                    this.lowerDifference.setEntry(j, -this.initialTrustRegionRadius);
                    this.upperDifference.setEntry(j, FastMath.max(upperBound[j] - this.currentBest.getEntry(j), this.initialTrustRegionRadius));
                }
            } else if (this.upperDifference.getEntry(j) <= this.initialTrustRegionRadius) {
                if (this.upperDifference.getEntry(j) <= 0.0d) {
                    this.currentBest.setEntry(j, upperBound[j]);
                    this.lowerDifference.setEntry(j, -boundDiff);
                    this.upperDifference.setEntry(j, 0.0d);
                } else {
                    this.currentBest.setEntry(j, upperBound[j] - this.initialTrustRegionRadius);
                    this.lowerDifference.setEntry(j, FastMath.min(lowerBound[j] - this.currentBest.getEntry(j), -this.initialTrustRegionRadius));
                    this.upperDifference.setEntry(j, this.initialTrustRegionRadius);
                }
            }
        }
        return bobyqb(lowerBound, upperBound);
    }

    /* JADX WARNING: Removed duplicated region for block: B:101:0x0443  */
    /* JADX WARNING: Removed duplicated region for block: B:103:0x0455  */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x0531  */
    /* JADX WARNING: Removed duplicated region for block: B:136:0x0537  */
    /* JADX WARNING: Removed duplicated region for block: B:154:0x05f4  */
    /* JADX WARNING: Removed duplicated region for block: B:268:0x09fd  */
    /* JADX WARNING: Removed duplicated region for block: B:276:0x0a23  */
    /* JADX WARNING: Removed duplicated region for block: B:285:0x0a6a  */
    /* JADX WARNING: Removed duplicated region for block: B:298:0x0ab5  */
    /* JADX WARNING: Removed duplicated region for block: B:306:0x0af2  */
    /* JADX WARNING: Removed duplicated region for block: B:386:0x0e2d  */
    /* JADX WARNING: Removed duplicated region for block: B:433:0x1081  */
    /* JADX WARNING: Removed duplicated region for block: B:435:0x1089  */
    /* JADX WARNING: Removed duplicated region for block: B:438:0x10a3  */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x03d6  */
    /* JADX WARNING: Removed duplicated region for block: B:98:0x043c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private double bobyqb(double[] r199, double[] r200) {
        /*
            r198 = this;
            r8 = r198
            printMethod()
            org.apache.commons.math3.linear.ArrayRealVector r0 = r8.currentBest
            int r11 = r0.getDimension()
            int r12 = r8.numberOfInterpolationPoints
            int r13 = r11 + 1
            int r14 = r12 - r13
            int r0 = r11 * r13
            int r15 = r0 / 2
            org.apache.commons.math3.linear.ArrayRealVector r0 = new org.apache.commons.math3.linear.ArrayRealVector
            r0.<init>(r11)
            r7 = r0
            org.apache.commons.math3.linear.ArrayRealVector r0 = new org.apache.commons.math3.linear.ArrayRealVector
            r0.<init>(r12)
            r6 = r0
            org.apache.commons.math3.linear.ArrayRealVector r0 = new org.apache.commons.math3.linear.ArrayRealVector
            r0.<init>(r12)
            r5 = r0
            r0 = 9221120237041090560(0x7ff8000000000000, double:NaN)
            r2 = 9221120237041090560(0x7ff8000000000000, double:NaN)
            r16 = 9221120237041090560(0x7ff8000000000000, double:NaN)
            r18 = 9221120237041090560(0x7ff8000000000000, double:NaN)
            r4 = 0
            r8.trustRegionCenterInterpolationPointIndex = r4
            r198.prelim(r199, r200)
            r20 = 0
            r21 = r20
            r20 = 0
        L_0x003b:
            r23 = r20
            r4 = r23
            if (r4 >= r11) goto L_0x0064
            r25 = r0
            org.apache.commons.math3.linear.ArrayRealVector r0 = r8.trustRegionCenterOffset
            org.apache.commons.math3.linear.Array2DRowRealMatrix r1 = r8.interpolationPoints
            r27 = r2
            int r2 = r8.trustRegionCenterInterpolationPointIndex
            double r1 = r1.getEntry(r2, r4)
            r0.setEntry(r4, r1)
            org.apache.commons.math3.linear.ArrayRealVector r0 = r8.trustRegionCenterOffset
            double r0 = r0.getEntry(r4)
            double r2 = r0 * r0
            double r21 = r21 + r2
            int r20 = r4 + 1
            r0 = r25
            r2 = r27
            r4 = 0
            goto L_0x003b
        L_0x0064:
            r25 = r0
            r27 = r2
            org.apache.commons.math3.linear.ArrayRealVector r0 = r8.fAtInterpolationPoints
            r1 = 0
            double r2 = r0.getEntry(r1)
            r20 = 0
            r0 = 0
            r1 = 0
            r4 = 0
            int r23 = r198.getEvaluations()
            r29 = r0
            r30 = r1
            double r0 = r8.initialTrustRegionRadius
            r31 = r0
            r33 = 0
            r35 = 0
            r37 = 0
            r39 = 0
            r41 = 0
            r43 = 0
            r45 = 0
            r47 = 0
            r49 = 0
            r51 = 0
            r53 = 0
            r55 = 0
            r57 = r13
            r13 = 20
            r58 = r0
            r1 = r4
            r60 = r33
            r62 = r35
            r64 = r37
            r66 = r43
            r68 = r49
            r0 = 20
            r33 = r31
            r31 = r2
            r3 = r29
        L_0x00b1:
            r4 = r0
            r0 = 60
            r35 = 4621819117588971520(0x4024000000000000, double:10.0)
            r70 = r3
            r37 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            r2 = 1
            if (r4 == r13) goto L_0x0bcb
            if (r4 == r0) goto L_0x0bb2
            r3 = 90
            if (r4 == r3) goto L_0x0b91
            r3 = 230(0xe6, float:3.22E-43)
            r13 = 210(0xd2, float:2.94E-43)
            r72 = r1
            r43 = 4591870180066957722(0x3fb999999999999a, double:0.1)
            if (r4 == r13) goto L_0x014e
            if (r4 == r3) goto L_0x0147
            r1 = 360(0x168, float:5.04E-43)
            if (r4 == r1) goto L_0x0137
            r1 = 650(0x28a, float:9.11E-43)
            if (r4 == r1) goto L_0x011f
            r1 = 680(0x2a8, float:9.53E-43)
            if (r4 == r1) goto L_0x0107
            r0 = 720(0x2d0, float:1.009E-42)
            if (r4 == r0) goto L_0x00f1
            org.apache.commons.math3.exception.MathIllegalStateException r0 = new org.apache.commons.math3.exception.MathIllegalStateException
            org.apache.commons.math3.exception.util.LocalizedFormats r1 = org.apache.commons.math3.exception.util.LocalizedFormats.SIMPLE_MESSAGE
            java.lang.Object[] r2 = new java.lang.Object[r2]
            java.lang.String r3 = "bobyqb"
            r13 = 0
            r2[r13] = r3
            r0.<init>(r1, r2)
            throw r0
        L_0x00f1:
            r24 = r4
            r145 = r5
            r143 = r7
            r144 = r14
            r9 = r15
            r76 = r66
            r13 = r68
            r10 = r70
            r29 = 0
            r15 = r6
            r6 = r58
            goto L_0x0b10
        L_0x0107:
            r24 = r4
            r145 = r5
            r143 = r7
            r144 = r14
            r9 = r15
            r76 = r66
            r13 = r68
            r10 = r70
            r1 = r72
            r29 = 0
            r15 = r6
            r6 = r58
            goto L_0x0aaa
        L_0x011f:
            r24 = r4
            r145 = r5
            r143 = r7
            r144 = r14
            r9 = r15
            r0 = r33
            r116 = r58
            r76 = r66
            r114 = r68
            r146 = r70
            r29 = 0
            r15 = r6
            goto L_0x09f1
        L_0x0137:
            r73 = r4
            r82 = r5
            r84 = r6
            r83 = r7
            r85 = r15
            r76 = r66
            r1 = r72
            goto L_0x03ce
        L_0x0147:
            r73 = r4
            r76 = r66
            r1 = r72
            goto L_0x018a
        L_0x014e:
            printState(r13)
            r73 = r4
            r3 = r66
            r1 = r72
            double[] r13 = r8.altmov(r1, r3)
            r24 = 0
            r27 = r13[r24]
            r25 = r13[r2]
            r29 = 0
        L_0x0163:
            r75 = r29
            r2 = r75
            if (r2 >= r11) goto L_0x0188
            org.apache.commons.math3.linear.ArrayRealVector r0 = r8.trialStepPoint
            r76 = r3
            org.apache.commons.math3.linear.ArrayRealVector r3 = r8.newPoint
            double r3 = r3.getEntry(r2)
            r78 = r13
            org.apache.commons.math3.linear.ArrayRealVector r13 = r8.trustRegionCenterOffset
            double r49 = r13.getEntry(r2)
            double r3 = r3 - r49
            r0.setEntry(r2, r3)
            int r29 = r2 + 1
            r3 = r76
            r13 = r78
            r2 = 1
            goto L_0x0163
        L_0x0188:
            r76 = r3
        L_0x018a:
            r0 = 230(0xe6, float:3.22E-43)
            printState(r0)
            r0 = 0
        L_0x0190:
            if (r0 >= r12) goto L_0x01ea
            r2 = 0
            r49 = 0
            r66 = 0
            r3 = r2
            r9 = r66
            r2 = 0
        L_0x019c:
            if (r2 >= r11) goto L_0x01d1
            org.apache.commons.math3.linear.Array2DRowRealMatrix r13 = r8.interpolationPoints
            double r66 = r13.getEntry(r0, r2)
            org.apache.commons.math3.linear.ArrayRealVector r13 = r8.trialStepPoint
            double r74 = r13.getEntry(r2)
            double r66 = r66 * r74
            double r3 = r3 + r66
            org.apache.commons.math3.linear.Array2DRowRealMatrix r13 = r8.interpolationPoints
            double r66 = r13.getEntry(r0, r2)
            org.apache.commons.math3.linear.ArrayRealVector r13 = r8.trustRegionCenterOffset
            double r74 = r13.getEntry(r2)
            double r66 = r66 * r74
            double r49 = r49 + r66
            org.apache.commons.math3.linear.Array2DRowRealMatrix r13 = r8.bMatrix
            double r66 = r13.getEntry(r0, r2)
            org.apache.commons.math3.linear.ArrayRealVector r13 = r8.trialStepPoint
            double r74 = r13.getEntry(r2)
            double r66 = r66 * r74
            double r9 = r9 + r66
            int r2 = r2 + 1
            goto L_0x019c
        L_0x01d1:
            double r66 = r3 * r37
            r2 = 0
            double r66 = r66 + r49
            r79 = r1
            double r1 = r3 * r66
            r5.setEntry(r0, r1)
            org.apache.commons.math3.linear.ArrayRealVector r1 = r8.lagrangeValuesAtNewPoint
            r1.setEntry(r0, r9)
            r6.setEntry(r0, r3)
            int r0 = r0 + 1
            r1 = r79
            goto L_0x0190
        L_0x01ea:
            r79 = r1
            r0 = 0
            r1 = r0
            r0 = 0
        L_0x01f0:
            if (r0 >= r14) goto L_0x0231
            r3 = 0
            r9 = r3
            r3 = 0
        L_0x01f6:
            if (r3 >= r12) goto L_0x0209
            org.apache.commons.math3.linear.Array2DRowRealMatrix r4 = r8.zMatrix
            double r41 = r4.getEntry(r3, r0)
            double r49 = r5.getEntry(r3)
            double r41 = r41 * r49
            double r9 = r9 + r41
            int r3 = r3 + 1
            goto L_0x01f6
        L_0x0209:
            double r3 = r9 * r9
            r13 = 0
            double r1 = r1 - r3
            r3 = 0
        L_0x020e:
            if (r3 >= r12) goto L_0x022c
            org.apache.commons.math3.linear.ArrayRealVector r4 = r8.lagrangeValuesAtNewPoint
            org.apache.commons.math3.linear.ArrayRealVector r13 = r8.lagrangeValuesAtNewPoint
            double r41 = r13.getEntry(r3)
            org.apache.commons.math3.linear.Array2DRowRealMatrix r13 = r8.zMatrix
            double r49 = r13.getEntry(r3, r0)
            double r49 = r49 * r9
            r80 = r1
            double r1 = r41 + r49
            r4.setEntry(r3, r1)
            int r3 = r3 + 1
            r1 = r80
            goto L_0x020e
        L_0x022c:
            r80 = r1
            int r0 = r0 + 1
            goto L_0x01f0
        L_0x0231:
            r3 = 0
            r9 = 0
            r16 = 0
            r0 = 0
            r196 = r3
            r3 = r16
            r16 = r196
        L_0x023e:
            if (r0 >= r11) goto L_0x02bc
            org.apache.commons.math3.linear.ArrayRealVector r13 = r8.trialStepPoint
            double r41 = r13.getEntry(r0)
            double r49 = r41 * r41
            double r16 = r16 + r49
            r49 = 0
            r13 = 0
        L_0x024d:
            if (r13 >= r12) goto L_0x0264
            double r66 = r5.getEntry(r13)
            r82 = r5
            org.apache.commons.math3.linear.Array2DRowRealMatrix r5 = r8.bMatrix
            double r74 = r5.getEntry(r13, r0)
            double r66 = r66 * r74
            double r49 = r49 + r66
            int r13 = r13 + 1
            r5 = r82
            goto L_0x024d
        L_0x0264:
            r82 = r5
            org.apache.commons.math3.linear.ArrayRealVector r5 = r8.trialStepPoint
            double r66 = r5.getEntry(r0)
            double r66 = r66 * r49
            double r9 = r9 + r66
            int r5 = r12 + r0
            r84 = r6
            r83 = r7
            r6 = r49
            r13 = 0
        L_0x0279:
            if (r13 >= r11) goto L_0x0292
            r85 = r15
            org.apache.commons.math3.linear.Array2DRowRealMatrix r15 = r8.bMatrix
            double r49 = r15.getEntry(r5, r13)
            org.apache.commons.math3.linear.ArrayRealVector r15 = r8.trialStepPoint
            double r66 = r15.getEntry(r13)
            double r49 = r49 * r66
            double r6 = r6 + r49
            int r13 = r13 + 1
            r15 = r85
            goto L_0x0279
        L_0x0292:
            r85 = r15
            org.apache.commons.math3.linear.ArrayRealVector r13 = r8.lagrangeValuesAtNewPoint
            r13.setEntry(r5, r6)
            org.apache.commons.math3.linear.ArrayRealVector r13 = r8.trialStepPoint
            double r49 = r13.getEntry(r0)
            double r49 = r49 * r6
            double r9 = r9 + r49
            org.apache.commons.math3.linear.ArrayRealVector r13 = r8.trialStepPoint
            double r49 = r13.getEntry(r0)
            org.apache.commons.math3.linear.ArrayRealVector r13 = r8.trustRegionCenterOffset
            double r66 = r13.getEntry(r0)
            double r49 = r49 * r66
            double r3 = r3 + r49
            int r0 = r0 + 1
            r5 = r82
            r7 = r83
            r6 = r84
            goto L_0x023e
        L_0x02bc:
            r82 = r5
            r84 = r6
            r83 = r7
            r85 = r15
            double r5 = r3 * r3
            r0 = 0
            double r41 = r21 + r3
            double r41 = r41 + r3
            double r49 = r16 * r37
            double r41 = r41 + r49
            double r41 = r41 * r16
            double r5 = r5 + r41
            double r5 = r5 + r1
            double r41 = r5 - r9
            org.apache.commons.math3.linear.ArrayRealVector r0 = r8.lagrangeValuesAtNewPoint
            int r1 = r8.trustRegionCenterInterpolationPointIndex
            org.apache.commons.math3.linear.ArrayRealVector r2 = r8.lagrangeValuesAtNewPoint
            int r5 = r8.trustRegionCenterInterpolationPointIndex
            double r5 = r2.getEntry(r5)
            r49 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r5 = r5 + r49
            r0.setEntry(r1, r5)
            if (r70 != 0) goto L_0x0339
            org.apache.commons.math3.linear.ArrayRealVector r0 = r8.lagrangeValuesAtNewPoint
            r1 = r79
            double r5 = r0.getEntry(r1)
            double r49 = r5 * r5
            double r66 = r27 * r41
            double r45 = r49 + r66
            int r0 = (r45 > r25 ? 1 : (r45 == r25 ? 0 : -1))
            if (r0 >= 0) goto L_0x0335
            r49 = 0
            int r0 = (r25 > r49 ? 1 : (r25 == r49 ? 0 : -1))
            if (r0 <= 0) goto L_0x0335
            r0 = 0
        L_0x0304:
            if (r0 >= r11) goto L_0x032b
            org.apache.commons.math3.linear.ArrayRealVector r2 = r8.newPoint
            org.apache.commons.math3.linear.ArrayRealVector r7 = r8.alternativeNewPoint
            r86 = r3
            double r3 = r7.getEntry(r0)
            r2.setEntry(r0, r3)
            org.apache.commons.math3.linear.ArrayRealVector r2 = r8.trialStepPoint
            org.apache.commons.math3.linear.ArrayRealVector r3 = r8.newPoint
            double r3 = r3.getEntry(r0)
            org.apache.commons.math3.linear.ArrayRealVector r7 = r8.trustRegionCenterOffset
            double r35 = r7.getEntry(r0)
            double r3 = r3 - r35
            r2.setEntry(r0, r3)
            int r0 = r0 + 1
            r3 = r86
            goto L_0x0304
        L_0x032b:
            r86 = r3
            r25 = 0
            r0 = 230(0xe6, float:3.22E-43)
            r3 = r70
            goto L_0x0449
        L_0x0335:
            r86 = r3
            goto L_0x03ce
        L_0x0339:
            r86 = r3
            r1 = r79
            double r2 = r33 * r33
            r4 = 0
            r6 = 0
            r0 = 0
            r1 = r0
            r0 = 0
        L_0x0346:
            if (r0 >= r12) goto L_0x03c8
            int r13 = r8.trustRegionCenterInterpolationPointIndex
            if (r0 != r13) goto L_0x0353
            r88 = r2
            r91 = r9
            goto L_0x03c0
        L_0x0353:
            r49 = 0
            r13 = 0
        L_0x0356:
            if (r13 >= r14) goto L_0x0365
            org.apache.commons.math3.linear.Array2DRowRealMatrix r15 = r8.zMatrix
            double r51 = r15.getEntry(r0, r13)
            double r53 = r51 * r51
            double r49 = r49 + r53
            int r13 = r13 + 1
            goto L_0x0356
        L_0x0365:
            org.apache.commons.math3.linear.ArrayRealVector r13 = r8.lagrangeValuesAtNewPoint
            double r51 = r13.getEntry(r0)
            double r53 = r41 * r49
            double r66 = r51 * r51
            double r53 = r53 + r66
            r55 = 0
            r13 = 0
        L_0x0374:
            if (r13 >= r11) goto L_0x038b
            org.apache.commons.math3.linear.Array2DRowRealMatrix r15 = r8.interpolationPoints
            double r66 = r15.getEntry(r0, r13)
            org.apache.commons.math3.linear.ArrayRealVector r15 = r8.trustRegionCenterOffset
            double r74 = r15.getEntry(r13)
            double r66 = r66 - r74
            double r74 = r66 * r66
            double r55 = r55 + r74
            int r13 = r13 + 1
            goto L_0x0374
        L_0x038b:
            double r66 = r55 / r2
            r90 = r1
            r88 = r2
            double r1 = r66 * r66
            r91 = r9
            r9 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r1 = org.apache.commons.math3.util.FastMath.max(r9, r1)
            double r9 = r1 * r53
            int r3 = (r9 > r4 ? 1 : (r9 == r4 ? 0 : -1))
            if (r3 <= 0) goto L_0x03a7
            double r4 = r1 * r53
            r3 = r0
            r45 = r53
            goto L_0x03a9
        L_0x03a7:
            r3 = r90
        L_0x03a9:
            org.apache.commons.math3.linear.ArrayRealVector r9 = r8.lagrangeValuesAtNewPoint
            double r9 = r9.getEntry(r0)
            double r74 = r9 * r9
            r95 = r3
            r93 = r4
            double r3 = r1 * r74
            double r1 = org.apache.commons.math3.util.FastMath.max(r6, r3)
            r6 = r1
            r4 = r93
            r1 = r95
        L_0x03c0:
            int r0 = r0 + 1
            r2 = r88
            r9 = r91
            goto L_0x0346
        L_0x03c8:
            r90 = r1
            r51 = r4
            r53 = r6
        L_0x03ce:
            r0 = 360(0x168, float:5.04E-43)
            printState(r0)
            r0 = 0
        L_0x03d4:
            if (r0 >= r11) goto L_0x042e
            r2 = r199[r0]
            org.apache.commons.math3.linear.ArrayRealVector r4 = r8.originShift
            double r4 = r4.getEntry(r0)
            org.apache.commons.math3.linear.ArrayRealVector r6 = r8.newPoint
            double r6 = r6.getEntry(r0)
            double r4 = r4 + r6
            double r6 = org.apache.commons.math3.util.FastMath.max(r2, r4)
            r96 = r2
            r2 = r200[r0]
            org.apache.commons.math3.linear.ArrayRealVector r13 = r8.currentBest
            r98 = r4
            double r4 = org.apache.commons.math3.util.FastMath.min(r6, r2)
            r13.setEntry(r0, r4)
            org.apache.commons.math3.linear.ArrayRealVector r4 = r8.newPoint
            double r4 = r4.getEntry(r0)
            org.apache.commons.math3.linear.ArrayRealVector r13 = r8.lowerDifference
            double r49 = r13.getEntry(r0)
            int r4 = (r4 > r49 ? 1 : (r4 == r49 ? 0 : -1))
            if (r4 != 0) goto L_0x0412
            org.apache.commons.math3.linear.ArrayRealVector r4 = r8.currentBest
            r100 = r2
            r2 = r199[r0]
            r4.setEntry(r0, r2)
            goto L_0x0414
        L_0x0412:
            r100 = r2
        L_0x0414:
            org.apache.commons.math3.linear.ArrayRealVector r2 = r8.newPoint
            double r2 = r2.getEntry(r0)
            org.apache.commons.math3.linear.ArrayRealVector r4 = r8.upperDifference
            double r4 = r4.getEntry(r0)
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 != 0) goto L_0x042b
            org.apache.commons.math3.linear.ArrayRealVector r2 = r8.currentBest
            r3 = r200[r0]
            r2.setEntry(r0, r3)
        L_0x042b:
            int r0 = r0 + 1
            goto L_0x03d4
        L_0x042e:
            org.apache.commons.math3.linear.ArrayRealVector r0 = r8.currentBest
            double[] r0 = r0.toArray()
            double r2 = r8.computeObjectiveValue(r0)
            boolean r0 = r8.isMinimize
            if (r0 != 0) goto L_0x043d
            double r2 = -r2
        L_0x043d:
            r6 = r2
            r3 = r70
            r0 = -1
            if (r3 != r0) goto L_0x0455
            r31 = r6
            r0 = 720(0x2d0, float:1.009E-42)
            r39 = r6
        L_0x0449:
            r66 = r76
            r5 = r82
            r7 = r83
            r6 = r84
            r15 = r85
            goto L_0x109f
        L_0x0455:
            org.apache.commons.math3.linear.ArrayRealVector r2 = r8.fAtInterpolationPoints
            int r4 = r8.trustRegionCenterInterpolationPointIndex
            double r39 = r2.getEntry(r4)
            r4 = 0
            r2 = 0
            r13 = r2
            r2 = 0
        L_0x0462:
            if (r2 >= r11) goto L_0x049f
            org.apache.commons.math3.linear.ArrayRealVector r15 = r8.trialStepPoint
            double r49 = r15.getEntry(r2)
            org.apache.commons.math3.linear.ArrayRealVector r15 = r8.gradientAtTrustRegionCenter
            double r66 = r15.getEntry(r2)
            double r49 = r49 * r66
            double r4 = r4 + r49
            r49 = r4
            r4 = 0
        L_0x0477:
            if (r4 > r2) goto L_0x049a
            org.apache.commons.math3.linear.ArrayRealVector r5 = r8.trialStepPoint
            double r66 = r5.getEntry(r4)
            org.apache.commons.math3.linear.ArrayRealVector r5 = r8.trialStepPoint
            double r70 = r5.getEntry(r2)
            double r66 = r66 * r70
            if (r4 != r2) goto L_0x048b
            double r66 = r66 * r37
        L_0x048b:
            org.apache.commons.math3.linear.ArrayRealVector r5 = r8.modelSecondDerivativesValues
            double r70 = r5.getEntry(r13)
            double r70 = r70 * r66
            double r49 = r49 + r70
            int r13 = r13 + 1
            int r4 = r4 + 1
            goto L_0x0477
        L_0x049a:
            int r2 = r2 + 1
            r4 = r49
            goto L_0x0462
        L_0x049f:
            r2 = 0
        L_0x04a0:
            if (r2 >= r12) goto L_0x04ba
            r15 = r84
            double r49 = r15.getEntry(r2)
            double r66 = r49 * r49
            org.apache.commons.math3.linear.ArrayRealVector r0 = r8.modelSecondDerivativesParameters
            double r70 = r0.getEntry(r2)
            double r70 = r70 * r37
            double r70 = r70 * r66
            double r4 = r4 + r70
            int r2 = r2 + 1
            r0 = -1
            goto L_0x04a0
        L_0x04ba:
            r15 = r84
            r0 = 0
            double r49 = r6 - r39
            double r9 = r49 - r4
            r64 = r62
            r62 = r60
            double r60 = org.apache.commons.math3.util.FastMath.abs(r9)
            r102 = r9
            r105 = r13
            r104 = r14
            r9 = r58
            r13 = r68
            int r0 = (r13 > r9 ? 1 : (r13 == r9 ? 0 : -1))
            if (r0 <= 0) goto L_0x04dd
            int r0 = r198.getEvaluations()
            r23 = r0
        L_0x04dd:
            if (r3 <= 0) goto L_0x0600
            r49 = 0
            int r0 = (r4 > r49 ? 1 : (r4 == r49 ? 0 : -1))
            if (r0 < 0) goto L_0x04fa
            org.apache.commons.math3.exception.MathIllegalStateException r0 = new org.apache.commons.math3.exception.MathIllegalStateException
            org.apache.commons.math3.exception.util.LocalizedFormats r2 = org.apache.commons.math3.exception.util.LocalizedFormats.TRUST_REGION_STEP_FAILED
            r106 = r3
            r3 = 1
            java.lang.Object[] r3 = new java.lang.Object[r3]
            java.lang.Double r29 = java.lang.Double.valueOf(r4)
            r24 = 0
            r3[r24] = r29
            r0.<init>(r2, r3)
            throw r0
        L_0x04fa:
            r106 = r3
            r24 = 0
            r0 = 0
            double r2 = r6 - r39
            double r2 = r2 / r4
            r107 = r4
            double r4 = r33 * r37
            int r0 = (r2 > r43 ? 1 : (r2 == r43 ? 0 : -1))
            if (r0 > 0) goto L_0x0511
            double r33 = org.apache.commons.math3.util.FastMath.min(r4, r13)
        L_0x050e:
            r109 = r2
            goto L_0x0529
        L_0x0511:
            r47 = 4604480259023595110(0x3fe6666666666666, double:0.7)
            int r0 = (r2 > r47 ? 1 : (r2 == r47 ? 0 : -1))
            if (r0 > 0) goto L_0x051f
            double r33 = org.apache.commons.math3.util.FastMath.max(r4, r13)
            goto L_0x050e
        L_0x051f:
            r47 = 4611686018427387904(0x4000000000000000, double:2.0)
            r109 = r2
            double r2 = r13 * r47
            double r33 = org.apache.commons.math3.util.FastMath.max(r4, r2)
        L_0x0529:
            r2 = 4609434218613702656(0x3ff8000000000000, double:1.5)
            double r58 = r9 * r2
            int r0 = (r33 > r58 ? 1 : (r33 == r58 ? 0 : -1))
            if (r0 > 0) goto L_0x0533
            r33 = r9
        L_0x0533:
            int r0 = (r6 > r39 ? 1 : (r6 == r39 ? 0 : -1))
            if (r0 >= 0) goto L_0x05f4
            r0 = r1
            r2 = r45
            double r47 = r33 * r33
            r51 = 0
            r53 = 0
            r1 = 0
            r29 = r1
            r111 = r4
            r4 = r53
            r1 = 0
        L_0x0548:
            if (r1 >= r12) goto L_0x05d7
            r53 = 0
            r58 = r53
            r53 = 0
        L_0x0550:
            r113 = r53
            r114 = r13
            r13 = r104
            r14 = r113
            if (r14 >= r13) goto L_0x056f
            r116 = r9
            org.apache.commons.math3.linear.Array2DRowRealMatrix r9 = r8.zMatrix
            double r9 = r9.getEntry(r1, r14)
            double r53 = r9 * r9
            double r58 = r58 + r53
            int r53 = r14 + 1
            r104 = r13
            r13 = r114
            r9 = r116
            goto L_0x0550
        L_0x056f:
            r116 = r9
            org.apache.commons.math3.linear.ArrayRealVector r9 = r8.lagrangeValuesAtNewPoint
            double r9 = r9.getEntry(r1)
            double r53 = r41 * r58
            double r66 = r9 * r9
            double r53 = r53 + r66
            r55 = 0
            r14 = 0
        L_0x0580:
            if (r14 >= r11) goto L_0x059f
            r118 = r9
            org.apache.commons.math3.linear.Array2DRowRealMatrix r9 = r8.interpolationPoints
            double r9 = r9.getEntry(r1, r14)
            r120 = r15
            org.apache.commons.math3.linear.ArrayRealVector r15 = r8.newPoint
            double r66 = r15.getEntry(r14)
            double r9 = r9 - r66
            double r66 = r9 * r9
            double r55 = r55 + r66
            int r14 = r14 + 1
            r9 = r118
            r15 = r120
            goto L_0x0580
        L_0x059f:
            r118 = r9
            r120 = r15
            double r9 = r55 / r47
            double r14 = r9 * r9
            r121 = r9
            r9 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r14 = org.apache.commons.math3.util.FastMath.max(r9, r14)
            double r66 = r14 * r53
            int r66 = (r66 > r51 ? 1 : (r66 == r51 ? 0 : -1))
            if (r66 <= 0) goto L_0x05bb
            double r51 = r14 * r53
            r29 = r1
            r45 = r53
        L_0x05bb:
            org.apache.commons.math3.linear.ArrayRealVector r9 = r8.lagrangeValuesAtNewPoint
            double r9 = r9.getEntry(r1)
            double r66 = r9 * r9
            r123 = r9
            double r9 = r14 * r66
            double r4 = org.apache.commons.math3.util.FastMath.max(r4, r9)
            int r1 = r1 + 1
            r104 = r13
            r13 = r114
            r9 = r116
            r15 = r120
            goto L_0x0548
        L_0x05d7:
            r116 = r9
            r114 = r13
            r120 = r15
            r13 = r104
            double r9 = r4 * r37
            int r1 = (r51 > r9 ? 1 : (r51 == r9 ? 0 : -1))
            if (r1 > 0) goto L_0x05ed
            r1 = r0
            r9 = r1
            r45 = r2
            r53 = r4
            goto L_0x05f1
        L_0x05ed:
            r53 = r4
            r9 = r29
        L_0x05f1:
            r47 = r109
            goto L_0x0611
        L_0x05f4:
            r116 = r9
            r114 = r13
            r120 = r15
            r13 = r104
            r9 = r1
            r47 = r109
            goto L_0x0611
        L_0x0600:
            r106 = r3
            r107 = r4
            r116 = r9
            r114 = r13
            r120 = r15
            r13 = r104
            r24 = 0
            r49 = 0
            r9 = r1
        L_0x0611:
            r10 = -1
            r0 = r8
            r14 = r49
            r1 = r41
            r24 = r73
            r5 = r106
            r49 = r107
            r29 = 0
            r3 = r45
            r125 = r5
            r10 = r82
            r5 = r9
            r0.update(r1, r3, r5)
            r0 = 0
            org.apache.commons.math3.linear.ArrayRealVector r1 = r8.modelSecondDerivativesParameters
            double r1 = r1.getEntry(r9)
            org.apache.commons.math3.linear.ArrayRealVector r3 = r8.modelSecondDerivativesParameters
            r3.setEntry(r9, r14)
            r3 = r0
            r0 = 0
        L_0x0637:
            if (r0 >= r11) goto L_0x0671
            org.apache.commons.math3.linear.Array2DRowRealMatrix r4 = r8.interpolationPoints
            double r4 = r4.getEntry(r9, r0)
            double r4 = r4 * r1
            r14 = r3
            r3 = 0
        L_0x0643:
            if (r3 > r0) goto L_0x0667
            org.apache.commons.math3.linear.ArrayRealVector r15 = r8.modelSecondDerivativesValues
            r126 = r1
            org.apache.commons.math3.linear.ArrayRealVector r1 = r8.modelSecondDerivativesValues
            double r1 = r1.getEntry(r14)
            r128 = r10
            org.apache.commons.math3.linear.Array2DRowRealMatrix r10 = r8.interpolationPoints
            double r58 = r10.getEntry(r9, r3)
            double r58 = r58 * r4
            double r1 = r1 + r58
            r15.setEntry(r14, r1)
            int r14 = r14 + 1
            int r3 = r3 + 1
            r1 = r126
            r10 = r128
            goto L_0x0643
        L_0x0667:
            r126 = r1
            r128 = r10
            int r0 = r0 + 1
            r3 = r14
            r14 = 0
            goto L_0x0637
        L_0x0671:
            r126 = r1
            r128 = r10
            r0 = 0
        L_0x0676:
            if (r0 >= r13) goto L_0x069e
            org.apache.commons.math3.linear.Array2DRowRealMatrix r1 = r8.zMatrix
            double r1 = r1.getEntry(r9, r0)
            double r1 = r1 * r102
            r4 = 0
        L_0x0681:
            if (r4 >= r12) goto L_0x069b
            org.apache.commons.math3.linear.ArrayRealVector r5 = r8.modelSecondDerivativesParameters
            org.apache.commons.math3.linear.ArrayRealVector r10 = r8.modelSecondDerivativesParameters
            double r14 = r10.getEntry(r4)
            org.apache.commons.math3.linear.Array2DRowRealMatrix r10 = r8.zMatrix
            double r58 = r10.getEntry(r4, r0)
            double r58 = r58 * r1
            double r14 = r14 + r58
            r5.setEntry(r4, r14)
            int r4 = r4 + 1
            goto L_0x0681
        L_0x069b:
            int r0 = r0 + 1
            goto L_0x0676
        L_0x069e:
            org.apache.commons.math3.linear.ArrayRealVector r0 = r8.fAtInterpolationPoints
            r0.setEntry(r9, r6)
            r0 = 0
        L_0x06a4:
            if (r0 >= r11) goto L_0x06bf
            org.apache.commons.math3.linear.Array2DRowRealMatrix r1 = r8.interpolationPoints
            org.apache.commons.math3.linear.ArrayRealVector r2 = r8.newPoint
            double r4 = r2.getEntry(r0)
            r1.setEntry(r9, r0, r4)
            org.apache.commons.math3.linear.Array2DRowRealMatrix r1 = r8.bMatrix
            double r1 = r1.getEntry(r9, r0)
            r10 = r83
            r10.setEntry(r0, r1)
            int r0 = r0 + 1
            goto L_0x06a4
        L_0x06bf:
            r10 = r83
            r0 = 0
        L_0x06c2:
            if (r0 >= r12) goto L_0x0720
            r1 = 0
            r4 = r1
            r1 = 0
        L_0x06c8:
            if (r1 >= r13) goto L_0x06dc
            org.apache.commons.math3.linear.Array2DRowRealMatrix r2 = r8.zMatrix
            double r14 = r2.getEntry(r9, r1)
            org.apache.commons.math3.linear.Array2DRowRealMatrix r2 = r8.zMatrix
            double r58 = r2.getEntry(r0, r1)
            double r14 = r14 * r58
            double r4 = r4 + r14
            int r1 = r1 + 1
            goto L_0x06c8
        L_0x06dc:
            r1 = 0
            r14 = r1
            r1 = 0
        L_0x06e0:
            if (r1 >= r11) goto L_0x06f5
            org.apache.commons.math3.linear.Array2DRowRealMatrix r2 = r8.interpolationPoints
            double r58 = r2.getEntry(r0, r1)
            org.apache.commons.math3.linear.ArrayRealVector r2 = r8.trustRegionCenterOffset
            double r66 = r2.getEntry(r1)
            double r58 = r58 * r66
            double r14 = r14 + r58
            int r1 = r1 + 1
            goto L_0x06e0
        L_0x06f5:
            double r1 = r4 * r14
            r58 = 0
        L_0x06f9:
            r129 = r58
            r130 = r3
            r3 = r129
            if (r3 >= r11) goto L_0x071b
            double r58 = r10.getEntry(r3)
            r131 = r4
            org.apache.commons.math3.linear.Array2DRowRealMatrix r4 = r8.interpolationPoints
            double r4 = r4.getEntry(r0, r3)
            double r4 = r4 * r1
            double r4 = r58 + r4
            r10.setEntry(r3, r4)
            int r58 = r3 + 1
            r3 = r130
            r4 = r131
            goto L_0x06f9
        L_0x071b:
            int r0 = r0 + 1
            r3 = r130
            goto L_0x06c2
        L_0x0720:
            r130 = r3
            r0 = 0
        L_0x0723:
            if (r0 >= r11) goto L_0x073a
            org.apache.commons.math3.linear.ArrayRealVector r1 = r8.gradientAtTrustRegionCenter
            org.apache.commons.math3.linear.ArrayRealVector r2 = r8.gradientAtTrustRegionCenter
            double r2 = r2.getEntry(r0)
            double r4 = r10.getEntry(r0)
            double r4 = r4 * r102
            double r2 = r2 + r4
            r1.setEntry(r0, r2)
            int r0 = r0 + 1
            goto L_0x0723
        L_0x073a:
            int r0 = (r6 > r39 ? 1 : (r6 == r39 ? 0 : -1))
            if (r0 >= 0) goto L_0x0806
            r8.trustRegionCenterInterpolationPointIndex = r9
            r0 = 0
            r2 = 0
            r3 = r2
            r1 = r0
            r0 = 0
        L_0x0746:
            if (r0 >= r11) goto L_0x07b7
            org.apache.commons.math3.linear.ArrayRealVector r4 = r8.trustRegionCenterOffset
            org.apache.commons.math3.linear.ArrayRealVector r5 = r8.newPoint
            double r14 = r5.getEntry(r0)
            r4.setEntry(r0, r14)
            org.apache.commons.math3.linear.ArrayRealVector r4 = r8.trustRegionCenterOffset
            double r4 = r4.getEntry(r0)
            double r14 = r4 * r4
            double r1 = r1 + r14
            r14 = r3
            r3 = 0
        L_0x075e:
            if (r3 > r0) goto L_0x07af
            if (r3 >= r0) goto L_0x0783
            org.apache.commons.math3.linear.ArrayRealVector r15 = r8.gradientAtTrustRegionCenter
            r133 = r1
            org.apache.commons.math3.linear.ArrayRealVector r1 = r8.gradientAtTrustRegionCenter
            double r1 = r1.getEntry(r0)
            r135 = r4
            org.apache.commons.math3.linear.ArrayRealVector r4 = r8.modelSecondDerivativesValues
            double r4 = r4.getEntry(r14)
            r137 = r9
            org.apache.commons.math3.linear.ArrayRealVector r9 = r8.trialStepPoint
            double r21 = r9.getEntry(r3)
            double r4 = r4 * r21
            double r1 = r1 + r4
            r15.setEntry(r0, r1)
            goto L_0x0789
        L_0x0783:
            r133 = r1
            r135 = r4
            r137 = r9
        L_0x0789:
            org.apache.commons.math3.linear.ArrayRealVector r1 = r8.gradientAtTrustRegionCenter
            org.apache.commons.math3.linear.ArrayRealVector r2 = r8.gradientAtTrustRegionCenter
            double r4 = r2.getEntry(r3)
            org.apache.commons.math3.linear.ArrayRealVector r2 = r8.modelSecondDerivativesValues
            double r21 = r2.getEntry(r14)
            org.apache.commons.math3.linear.ArrayRealVector r2 = r8.trialStepPoint
            double r58 = r2.getEntry(r0)
            double r21 = r21 * r58
            double r4 = r4 + r21
            r1.setEntry(r3, r4)
            int r14 = r14 + 1
            int r3 = r3 + 1
            r1 = r133
            r4 = r135
            r9 = r137
            goto L_0x075e
        L_0x07af:
            r133 = r1
            r137 = r9
            int r0 = r0 + 1
            r3 = r14
            goto L_0x0746
        L_0x07b7:
            r137 = r9
            r0 = 0
        L_0x07ba:
            if (r0 >= r12) goto L_0x0801
            r4 = 0
            r14 = r4
            r4 = 0
        L_0x07c0:
            if (r4 >= r11) goto L_0x07d5
            org.apache.commons.math3.linear.Array2DRowRealMatrix r5 = r8.interpolationPoints
            double r21 = r5.getEntry(r0, r4)
            org.apache.commons.math3.linear.ArrayRealVector r5 = r8.trialStepPoint
            double r58 = r5.getEntry(r4)
            double r21 = r21 * r58
            double r14 = r14 + r21
            int r4 = r4 + 1
            goto L_0x07c0
        L_0x07d5:
            org.apache.commons.math3.linear.ArrayRealVector r4 = r8.modelSecondDerivativesParameters
            double r4 = r4.getEntry(r0)
            double r14 = r14 * r4
            r4 = 0
        L_0x07de:
            if (r4 >= r11) goto L_0x07fc
            org.apache.commons.math3.linear.ArrayRealVector r5 = r8.gradientAtTrustRegionCenter
            org.apache.commons.math3.linear.ArrayRealVector r9 = r8.gradientAtTrustRegionCenter
            double r21 = r9.getEntry(r4)
            org.apache.commons.math3.linear.Array2DRowRealMatrix r9 = r8.interpolationPoints
            double r58 = r9.getEntry(r0, r4)
            double r58 = r58 * r14
            r138 = r1
            double r1 = r21 + r58
            r5.setEntry(r4, r1)
            int r4 = r4 + 1
            r1 = r138
            goto L_0x07de
        L_0x07fc:
            r138 = r1
            int r0 = r0 + 1
            goto L_0x07ba
        L_0x0801:
            r138 = r1
            r21 = r138
            goto L_0x080a
        L_0x0806:
            r137 = r9
            r3 = r130
        L_0x080a:
            r9 = r125
            if (r9 <= 0) goto L_0x09a8
            r0 = 0
        L_0x080f:
            if (r0 >= r12) goto L_0x082f
            org.apache.commons.math3.linear.ArrayRealVector r1 = r8.lagrangeValuesAtNewPoint
            org.apache.commons.math3.linear.ArrayRealVector r2 = r8.fAtInterpolationPoints
            double r4 = r2.getEntry(r0)
            org.apache.commons.math3.linear.ArrayRealVector r2 = r8.fAtInterpolationPoints
            int r14 = r8.trustRegionCenterInterpolationPointIndex
            double r14 = r2.getEntry(r14)
            double r4 = r4 - r14
            r1.setEntry(r0, r4)
            r14 = r128
            r1 = 0
            r14.setEntry(r0, r1)
            int r0 = r0 + 1
            goto L_0x080f
        L_0x082f:
            r14 = r128
            r0 = 0
        L_0x0832:
            if (r0 >= r13) goto L_0x086d
            r1 = 0
            r4 = r1
            r1 = 0
        L_0x0838:
            if (r1 >= r12) goto L_0x084d
            org.apache.commons.math3.linear.Array2DRowRealMatrix r2 = r8.zMatrix
            double r58 = r2.getEntry(r1, r0)
            org.apache.commons.math3.linear.ArrayRealVector r2 = r8.lagrangeValuesAtNewPoint
            double r66 = r2.getEntry(r1)
            double r58 = r58 * r66
            double r4 = r4 + r58
            int r1 = r1 + 1
            goto L_0x0838
        L_0x084d:
            r1 = 0
        L_0x084e:
            if (r1 >= r12) goto L_0x0868
            double r58 = r14.getEntry(r1)
            org.apache.commons.math3.linear.Array2DRowRealMatrix r2 = r8.zMatrix
            double r66 = r2.getEntry(r1, r0)
            double r66 = r66 * r4
            r140 = r3
            double r2 = r58 + r66
            r14.setEntry(r1, r2)
            int r1 = r1 + 1
            r3 = r140
            goto L_0x084e
        L_0x0868:
            r140 = r3
            int r0 = r0 + 1
            goto L_0x0832
        L_0x086d:
            r140 = r3
            r0 = 0
        L_0x0870:
            if (r0 >= r12) goto L_0x089f
            r1 = 0
            r2 = r1
            r1 = 0
        L_0x0876:
            if (r1 >= r11) goto L_0x088a
            org.apache.commons.math3.linear.Array2DRowRealMatrix r4 = r8.interpolationPoints
            double r4 = r4.getEntry(r0, r1)
            org.apache.commons.math3.linear.ArrayRealVector r15 = r8.trustRegionCenterOffset
            double r58 = r15.getEntry(r1)
            double r4 = r4 * r58
            double r2 = r2 + r4
            int r1 = r1 + 1
            goto L_0x0876
        L_0x088a:
            double r4 = r14.getEntry(r0)
            r15 = r120
            r15.setEntry(r0, r4)
            double r4 = r14.getEntry(r0)
            double r4 = r4 * r2
            r14.setEntry(r0, r4)
            int r0 = r0 + 1
            goto L_0x0870
        L_0x089f:
            r15 = r120
            r0 = 0
            r2 = 0
            r3 = r2
            r1 = r0
            r0 = 0
        L_0x08a8:
            if (r0 >= r11) goto L_0x0957
            r58 = 0
            r141 = r6
            r6 = r58
            r5 = 0
        L_0x08b1:
            if (r5 >= r12) goto L_0x08d8
            r143 = r10
            org.apache.commons.math3.linear.Array2DRowRealMatrix r10 = r8.bMatrix
            double r58 = r10.getEntry(r5, r0)
            org.apache.commons.math3.linear.ArrayRealVector r10 = r8.lagrangeValuesAtNewPoint
            double r66 = r10.getEntry(r5)
            double r58 = r58 * r66
            org.apache.commons.math3.linear.Array2DRowRealMatrix r10 = r8.interpolationPoints
            double r66 = r10.getEntry(r5, r0)
            double r68 = r14.getEntry(r5)
            double r66 = r66 * r68
            double r58 = r58 + r66
            double r6 = r6 + r58
            int r5 = r5 + 1
            r10 = r143
            goto L_0x08b1
        L_0x08d8:
            r143 = r10
            org.apache.commons.math3.linear.ArrayRealVector r5 = r8.trustRegionCenterOffset
            double r58 = r5.getEntry(r0)
            org.apache.commons.math3.linear.ArrayRealVector r5 = r8.lowerDifference
            double r66 = r5.getEntry(r0)
            int r5 = (r58 > r66 ? 1 : (r58 == r66 ? 0 : -1))
            if (r5 != 0) goto L_0x0908
            org.apache.commons.math3.linear.ArrayRealVector r5 = r8.gradientAtTrustRegionCenter
            r144 = r13
            r145 = r14
            double r13 = r5.getEntry(r0)
            r146 = r9
            r9 = 0
            double r13 = org.apache.commons.math3.util.FastMath.min(r9, r13)
            double r58 = r13 * r13
            double r1 = r1 + r58
            double r58 = org.apache.commons.math3.util.FastMath.min(r9, r6)
            double r9 = r58 * r58
            double r3 = r3 + r9
            goto L_0x0942
        L_0x0908:
            r146 = r9
            r144 = r13
            r145 = r14
            org.apache.commons.math3.linear.ArrayRealVector r5 = r8.trustRegionCenterOffset
            double r9 = r5.getEntry(r0)
            org.apache.commons.math3.linear.ArrayRealVector r5 = r8.upperDifference
            double r13 = r5.getEntry(r0)
            int r5 = (r9 > r13 ? 1 : (r9 == r13 ? 0 : -1))
            if (r5 != 0) goto L_0x0936
            org.apache.commons.math3.linear.ArrayRealVector r5 = r8.gradientAtTrustRegionCenter
            double r9 = r5.getEntry(r0)
            r13 = 0
            double r9 = org.apache.commons.math3.util.FastMath.max(r13, r9)
            double r58 = r9 * r9
            double r1 = r1 + r58
            double r58 = org.apache.commons.math3.util.FastMath.max(r13, r6)
            double r13 = r58 * r58
            double r3 = r3 + r13
            goto L_0x0942
        L_0x0936:
            org.apache.commons.math3.linear.ArrayRealVector r5 = r8.gradientAtTrustRegionCenter
            double r9 = r5.getEntry(r0)
            double r13 = r9 * r9
            double r1 = r1 + r13
            double r13 = r6 * r6
            double r3 = r3 + r13
        L_0x0942:
            org.apache.commons.math3.linear.ArrayRealVector r5 = r8.lagrangeValuesAtNewPoint
            int r9 = r12 + r0
            r5.setEntry(r9, r6)
            int r0 = r0 + 1
            r6 = r141
            r10 = r143
            r13 = r144
            r14 = r145
            r9 = r146
            goto L_0x08a8
        L_0x0957:
            r141 = r6
            r146 = r9
            r143 = r10
            r144 = r13
            r145 = r14
            int r30 = r30 + 1
            double r5 = r3 * r35
            int r0 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
            if (r0 >= 0) goto L_0x096b
            r30 = 0
        L_0x096b:
            r0 = r30
            r5 = 3
            if (r0 < r5) goto L_0x09a3
            r5 = 0
            r9 = r85
            int r6 = org.apache.commons.math3.util.FastMath.max(r12, r9)
        L_0x0977:
            if (r5 >= r6) goto L_0x09a0
            if (r5 >= r11) goto L_0x0988
            org.apache.commons.math3.linear.ArrayRealVector r7 = r8.gradientAtTrustRegionCenter
            org.apache.commons.math3.linear.ArrayRealVector r10 = r8.lagrangeValuesAtNewPoint
            int r13 = r12 + r5
            double r13 = r10.getEntry(r13)
            r7.setEntry(r5, r13)
        L_0x0988:
            if (r5 >= r12) goto L_0x0993
            org.apache.commons.math3.linear.ArrayRealVector r7 = r8.modelSecondDerivativesParameters
            double r13 = r15.getEntry(r5)
            r7.setEntry(r5, r13)
        L_0x0993:
            if (r5 >= r9) goto L_0x099c
            org.apache.commons.math3.linear.ArrayRealVector r7 = r8.modelSecondDerivativesValues
            r13 = 0
            r7.setEntry(r5, r13)
        L_0x099c:
            r0 = 0
            int r5 = r5 + 1
            goto L_0x0977
        L_0x09a0:
            r30 = r0
            goto L_0x09b8
        L_0x09a3:
            r9 = r85
            r30 = r0
            goto L_0x09b8
        L_0x09a8:
            r140 = r3
            r141 = r6
            r146 = r9
            r143 = r10
            r144 = r13
            r9 = r85
            r15 = r120
            r145 = r128
        L_0x09b8:
            if (r146 != 0) goto L_0x09d1
            r0 = 60
        L_0x09bc:
            r6 = r15
            r66 = r76
            r68 = r114
            r58 = r116
            r1 = r137
            r39 = r141
            r7 = r143
            r14 = r144
            r5 = r145
            r3 = r146
            goto L_0x0b09
        L_0x09d1:
            double r4 = r49 * r43
            r0 = 0
            double r4 = r39 + r4
            int r0 = (r141 > r4 ? 1 : (r141 == r4 ? 0 : -1))
            if (r0 > 0) goto L_0x09dd
            r0 = 60
            goto L_0x09bc
        L_0x09dd:
            r0 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r0 = r0 * r33
            double r58 = r116 * r35
            double r2 = r0 * r0
            double r4 = r58 * r58
            double r55 = org.apache.commons.math3.util.FastMath.max(r2, r4)
            r0 = r33
            r72 = r137
            r39 = r141
        L_0x09f1:
            r2 = 650(0x28a, float:9.11E-43)
            printState(r2)
            r2 = -1
            r3 = r2
            r4 = r55
            r2 = 0
        L_0x09fb:
            if (r2 >= r12) goto L_0x0a21
            r6 = 0
            r13 = r6
            r6 = 0
        L_0x0a01:
            if (r6 >= r11) goto L_0x0a18
            org.apache.commons.math3.linear.Array2DRowRealMatrix r7 = r8.interpolationPoints
            double r33 = r7.getEntry(r2, r6)
            org.apache.commons.math3.linear.ArrayRealVector r7 = r8.trustRegionCenterOffset
            double r35 = r7.getEntry(r6)
            double r33 = r33 - r35
            double r35 = r33 * r33
            double r13 = r13 + r35
            int r6 = r6 + 1
            goto L_0x0a01
        L_0x0a18:
            int r6 = (r13 > r4 ? 1 : (r13 == r4 ? 0 : -1))
            if (r6 <= 0) goto L_0x0a1e
            r3 = r2
            r4 = r13
        L_0x0a1e:
            int r2 = r2 + 1
            goto L_0x09fb
        L_0x0a21:
            if (r3 < 0) goto L_0x0a6a
            double r6 = org.apache.commons.math3.util.FastMath.sqrt(r4)
            r10 = r146
            r2 = -1
            if (r10 != r2) goto L_0x0a41
            double r13 = r0 * r43
            r147 = r3
            double r2 = r6 * r37
            double r0 = org.apache.commons.math3.util.FastMath.min(r13, r2)
            r2 = 4609434218613702656(0x3ff8000000000000, double:1.5)
            double r58 = r116 * r2
            int r2 = (r0 > r58 ? 1 : (r0 == r58 ? 0 : -1))
            if (r2 > 0) goto L_0x0a43
            r0 = r116
            goto L_0x0a43
        L_0x0a41:
            r147 = r3
        L_0x0a43:
            r3 = 0
            double r13 = r6 * r43
            double r13 = org.apache.commons.math3.util.FastMath.min(r13, r0)
            r148 = r6
            r6 = r116
            double r66 = org.apache.commons.math3.util.FastMath.max(r13, r6)
            double r16 = r66 * r66
            r2 = 90
            r33 = r0
            r0 = r2
            r55 = r4
            r58 = r6
            r6 = r15
        L_0x0a5e:
            r68 = r114
        L_0x0a60:
            r7 = r143
            r14 = r144
            r5 = r145
            r1 = r147
            goto L_0x0b09
        L_0x0a6a:
            r147 = r3
            r6 = r116
            r10 = r146
            r2 = -1
            if (r10 != r2) goto L_0x0a81
            r2 = 680(0x2a8, float:9.53E-43)
        L_0x0a75:
            r33 = r0
            r0 = r2
            r55 = r4
            r58 = r6
            r3 = r10
            r6 = r15
            r66 = r76
            goto L_0x0a5e
        L_0x0a81:
            r2 = 0
            int r2 = (r47 > r2 ? 1 : (r47 == r2 ? 0 : -1))
            if (r2 <= 0) goto L_0x0a8a
            r2 = 60
            goto L_0x0a75
        L_0x0a8a:
            r13 = r114
            double r2 = org.apache.commons.math3.util.FastMath.max(r0, r13)
            int r2 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r2 <= 0) goto L_0x0aa4
            r2 = 60
            r33 = r0
            r0 = r2
            r55 = r4
            r58 = r6
            r3 = r10
            r68 = r13
            r6 = r15
            r66 = r76
            goto L_0x0a60
        L_0x0aa4:
            r33 = r0
            r55 = r4
            r1 = r147
        L_0x0aaa:
            r0 = 680(0x2a8, float:9.53E-43)
            printState(r0)
            double r2 = r8.stoppingTrustRegionRadius
            int r0 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1))
            if (r0 <= 0) goto L_0x0af2
            double r2 = r6 * r37
            double r4 = r8.stoppingTrustRegionRadius
            double r4 = r6 / r4
            r33 = 4625196817309499392(0x4030000000000000, double:16.0)
            int r0 = (r4 > r33 ? 1 : (r4 == r33 ? 0 : -1))
            if (r0 > 0) goto L_0x0ac6
            double r6 = r8.stoppingTrustRegionRadius
            r150 = r1
            goto L_0x0ae2
        L_0x0ac6:
            r33 = 4643000109586448384(0x406f400000000000, double:250.0)
            int r0 = (r4 > r33 ? 1 : (r4 == r33 ? 0 : -1))
            if (r0 > 0) goto L_0x0adc
            double r33 = org.apache.commons.math3.util.FastMath.sqrt(r4)
            r150 = r1
            double r0 = r8.stoppingTrustRegionRadius
            double r33 = r33 * r0
            r6 = r33
            goto L_0x0ae2
        L_0x0adc:
            r150 = r1
            double r58 = r6 * r43
            r6 = r58
        L_0x0ae2:
            double r33 = org.apache.commons.math3.util.FastMath.max(r2, r6)
            r3 = 0
            int r23 = r198.getEvaluations()
            r0 = 60
            r47 = r4
            r58 = r6
            goto L_0x0afc
        L_0x0af2:
            r150 = r1
            r0 = -1
            if (r10 != r0) goto L_0x0b0e
            r0 = 360(0x168, float:5.04E-43)
            r58 = r6
            r3 = r10
        L_0x0afc:
            r68 = r13
            r6 = r15
            r66 = r76
            r7 = r143
            r14 = r144
            r5 = r145
            r1 = r150
        L_0x0b09:
            r13 = 20
            r15 = r9
            goto L_0x00b1
        L_0x0b0e:
            r72 = r150
        L_0x0b10:
            r0 = 720(0x2d0, float:1.009E-42)
            printState(r0)
            org.apache.commons.math3.linear.ArrayRealVector r0 = r8.fAtInterpolationPoints
            int r1 = r8.trustRegionCenterInterpolationPointIndex
            double r0 = r0.getEntry(r1)
            int r0 = (r0 > r31 ? 1 : (r0 == r31 ? 0 : -1))
            if (r0 > 0) goto L_0x0b8e
        L_0x0b22:
            r0 = r29
            if (r0 >= r11) goto L_0x0b83
            r1 = r199[r0]
            org.apache.commons.math3.linear.ArrayRealVector r3 = r8.originShift
            double r3 = r3.getEntry(r0)
            org.apache.commons.math3.linear.ArrayRealVector r5 = r8.trustRegionCenterOffset
            double r35 = r5.getEntry(r0)
            double r3 = r3 + r35
            r151 = r6
            double r5 = org.apache.commons.math3.util.FastMath.max(r1, r3)
            r153 = r1
            r1 = r200[r0]
            org.apache.commons.math3.linear.ArrayRealVector r7 = r8.currentBest
            r155 = r3
            double r3 = org.apache.commons.math3.util.FastMath.min(r5, r1)
            r7.setEntry(r0, r3)
            org.apache.commons.math3.linear.ArrayRealVector r3 = r8.trustRegionCenterOffset
            double r3 = r3.getEntry(r0)
            org.apache.commons.math3.linear.ArrayRealVector r7 = r8.lowerDifference
            double r35 = r7.getEntry(r0)
            int r3 = (r3 > r35 ? 1 : (r3 == r35 ? 0 : -1))
            if (r3 != 0) goto L_0x0b65
            org.apache.commons.math3.linear.ArrayRealVector r3 = r8.currentBest
            r157 = r1
            r1 = r199[r0]
            r3.setEntry(r0, r1)
            goto L_0x0b67
        L_0x0b65:
            r157 = r1
        L_0x0b67:
            org.apache.commons.math3.linear.ArrayRealVector r1 = r8.trustRegionCenterOffset
            double r1 = r1.getEntry(r0)
            org.apache.commons.math3.linear.ArrayRealVector r3 = r8.upperDifference
            double r3 = r3.getEntry(r0)
            int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r1 != 0) goto L_0x0b7e
            org.apache.commons.math3.linear.ArrayRealVector r1 = r8.currentBest
            r2 = r200[r0]
            r1.setEntry(r0, r2)
        L_0x0b7e:
            int r29 = r0 + 1
            r6 = r151
            goto L_0x0b22
        L_0x0b83:
            r151 = r6
            org.apache.commons.math3.linear.ArrayRealVector r0 = r8.fAtInterpolationPoints
            int r1 = r8.trustRegionCenterInterpolationPointIndex
            double r39 = r0.getEntry(r1)
            goto L_0x0b90
        L_0x0b8e:
            r151 = r6
        L_0x0b90:
            return r39
        L_0x0b91:
            r24 = r4
            r145 = r5
            r144 = r14
            r9 = r15
            r76 = r66
            r13 = r68
            r10 = r70
            r29 = 0
            r147 = r1
            r15 = r7
            r1 = r8
            r160 = r9
            r3 = r10
            r116 = r58
            r13 = r60
            r172 = r64
            r49 = 20
            r8 = r6
            goto L_0x0e1d
        L_0x0bb2:
            r24 = r4
            r145 = r5
            r143 = r7
            r144 = r14
            r9 = r15
            r151 = r58
            r76 = r66
            r13 = r68
            r10 = r70
            r3 = 1
            r29 = 0
            r15 = r6
            r159 = r1
            goto L_0x0c9c
        L_0x0bcb:
            r24 = r4
            r145 = r5
            r143 = r7
            r144 = r14
            r9 = r15
            r151 = r58
            r76 = r66
            r13 = r68
            r10 = r70
            r2 = 90
            r3 = 1
            r29 = 0
            r15 = r6
            r7 = 20
            printState(r7)
            int r4 = r8.trustRegionCenterInterpolationPointIndex
            if (r4 == 0) goto L_0x0c9a
            r4 = 0
            r5 = r4
            r4 = 0
        L_0x0bee:
            if (r4 >= r11) goto L_0x0c48
            r6 = r5
            r5 = 0
        L_0x0bf2:
            if (r5 > r4) goto L_0x0c3d
            if (r5 >= r4) goto L_0x0c14
            org.apache.commons.math3.linear.ArrayRealVector r2 = r8.gradientAtTrustRegionCenter
            org.apache.commons.math3.linear.ArrayRealVector r3 = r8.gradientAtTrustRegionCenter
            double r49 = r3.getEntry(r4)
            org.apache.commons.math3.linear.ArrayRealVector r3 = r8.modelSecondDerivativesValues
            double r58 = r3.getEntry(r6)
            org.apache.commons.math3.linear.ArrayRealVector r3 = r8.trustRegionCenterOffset
            double r66 = r3.getEntry(r5)
            double r58 = r58 * r66
            r159 = r1
            double r0 = r49 + r58
            r2.setEntry(r4, r0)
            goto L_0x0c16
        L_0x0c14:
            r159 = r1
        L_0x0c16:
            org.apache.commons.math3.linear.ArrayRealVector r0 = r8.gradientAtTrustRegionCenter
            org.apache.commons.math3.linear.ArrayRealVector r1 = r8.gradientAtTrustRegionCenter
            double r1 = r1.getEntry(r5)
            org.apache.commons.math3.linear.ArrayRealVector r3 = r8.modelSecondDerivativesValues
            double r49 = r3.getEntry(r6)
            org.apache.commons.math3.linear.ArrayRealVector r3 = r8.trustRegionCenterOffset
            double r58 = r3.getEntry(r4)
            double r49 = r49 * r58
            double r1 = r1 + r49
            r0.setEntry(r5, r1)
            int r6 = r6 + 1
            int r5 = r5 + 1
            r1 = r159
            r0 = 60
            r2 = 90
            r3 = 1
            goto L_0x0bf2
        L_0x0c3d:
            r159 = r1
            int r4 = r4 + 1
            r5 = r6
            r0 = 60
            r2 = 90
            r3 = 1
            goto L_0x0bee
        L_0x0c48:
            r159 = r1
            int r0 = r198.getEvaluations()
            if (r0 <= r12) goto L_0x0c9c
            r0 = 0
        L_0x0c51:
            if (r0 >= r12) goto L_0x0c9c
            r1 = 0
            r2 = r1
            r1 = 0
        L_0x0c57:
            if (r1 >= r11) goto L_0x0c6c
            org.apache.commons.math3.linear.Array2DRowRealMatrix r4 = r8.interpolationPoints
            double r49 = r4.getEntry(r0, r1)
            org.apache.commons.math3.linear.ArrayRealVector r4 = r8.trustRegionCenterOffset
            double r58 = r4.getEntry(r1)
            double r49 = r49 * r58
            double r2 = r2 + r49
            int r1 = r1 + 1
            goto L_0x0c57
        L_0x0c6c:
            org.apache.commons.math3.linear.ArrayRealVector r1 = r8.modelSecondDerivativesParameters
            double r49 = r1.getEntry(r0)
            double r2 = r2 * r49
            r1 = 0
        L_0x0c75:
            if (r1 >= r11) goto L_0x0c93
            org.apache.commons.math3.linear.ArrayRealVector r4 = r8.gradientAtTrustRegionCenter
            org.apache.commons.math3.linear.ArrayRealVector r6 = r8.gradientAtTrustRegionCenter
            double r49 = r6.getEntry(r1)
            org.apache.commons.math3.linear.Array2DRowRealMatrix r6 = r8.interpolationPoints
            double r58 = r6.getEntry(r0, r1)
            double r58 = r58 * r2
            double r7 = r49 + r58
            r4.setEntry(r1, r7)
            int r1 = r1 + 1
            r7 = 20
            r8 = r198
            goto L_0x0c75
        L_0x0c93:
            int r0 = r0 + 1
            r7 = 20
            r8 = r198
            goto L_0x0c51
        L_0x0c9a:
            r159 = r1
        L_0x0c9c:
            r0 = 60
            printState(r0)
            org.apache.commons.math3.linear.ArrayRealVector r3 = new org.apache.commons.math3.linear.ArrayRealVector
            r3.<init>(r11)
            r1 = 1
            r2 = 90
            org.apache.commons.math3.linear.ArrayRealVector r4 = new org.apache.commons.math3.linear.ArrayRealVector
            r4.<init>(r11)
            org.apache.commons.math3.linear.ArrayRealVector r5 = new org.apache.commons.math3.linear.ArrayRealVector
            r5.<init>(r11)
            org.apache.commons.math3.linear.ArrayRealVector r6 = new org.apache.commons.math3.linear.ArrayRealVector
            r6.<init>(r11)
            r8 = r15
            r116 = r151
            org.apache.commons.math3.linear.ArrayRealVector r7 = new org.apache.commons.math3.linear.ArrayRealVector
            r7.<init>(r11)
            r15 = r143
            r49 = 20
            r0 = r198
            r160 = r9
            r147 = r159
            r9 = 90
            r50 = 1
            r1 = r33
            double[] r0 = r0.trsbox(r1, r3, r4, r5, r6, r7)
            r1 = r0[r29]
            r18 = r0[r50]
            r161 = r33
            r163 = r10
            double r9 = org.apache.commons.math3.util.FastMath.sqrt(r1)
            r164 = r0
            r165 = r1
            r0 = r161
            double r68 = org.apache.commons.math3.util.FastMath.min(r0, r9)
            double r58 = r116 * r37
            int r2 = (r68 > r58 ? 1 : (r68 == r58 ? 0 : -1))
            if (r2 >= 0) goto L_0x0e0c
            r2 = -1
            double r58 = r116 * r35
            double r55 = r58 * r58
            int r0 = r198.getEvaluations()
            int r1 = r23 + 2
            if (r0 > r1) goto L_0x0d16
            r0 = 650(0x28a, float:9.11E-43)
            r3 = r2
            r6 = r8
            r7 = r15
            r66 = r76
            r58 = r116
            r14 = r144
            r5 = r145
            r1 = r147
            r15 = r160
            r16 = r165
        L_0x0d10:
            r13 = 20
            r8 = r198
            goto L_0x00b1
        L_0x0d16:
            r168 = r2
            r167 = r3
            r13 = r60
            r0 = r62
            double r2 = org.apache.commons.math3.util.FastMath.max(r13, r0)
            r169 = r0
            r0 = r64
            double r16 = org.apache.commons.math3.util.FastMath.max(r2, r0)
            r35 = 4593671619917905920(0x3fc0000000000000, double:0.125)
            double r58 = r116 * r35
            double r58 = r58 * r116
            r35 = 0
            int r35 = (r18 > r35 ? 1 : (r18 == r35 ? 0 : -1))
            if (r35 <= 0) goto L_0x0d59
            double r35 = r58 * r18
            int r35 = (r16 > r35 ? 1 : (r16 == r35 ? 0 : -1))
            if (r35 <= 0) goto L_0x0d59
            r24 = 650(0x28a, float:9.11E-43)
            r64 = r0
            r6 = r8
            r60 = r13
            r7 = r15
            r0 = r24
            r66 = r76
            r58 = r116
            r14 = r144
            r5 = r145
            r1 = r147
            r15 = r160
            r16 = r165
            r3 = r168
            r62 = r169
            goto L_0x0d10
        L_0x0d59:
            double r35 = r16 / r116
            r50 = 0
        L_0x0d5d:
            r171 = r50
            r172 = r0
            r0 = r171
            if (r0 >= r11) goto L_0x0de3
            r60 = r35
            r1 = r198
            r174 = r2
            org.apache.commons.math3.linear.ArrayRealVector r2 = r1.newPoint
            double r2 = r2.getEntry(r0)
            r176 = r4
            org.apache.commons.math3.linear.ArrayRealVector r4 = r1.lowerDifference
            double r62 = r4.getEntry(r0)
            int r2 = (r2 > r62 ? 1 : (r2 == r62 ? 0 : -1))
            if (r2 != 0) goto L_0x0d81
            double r60 = r15.getEntry(r0)
        L_0x0d81:
            org.apache.commons.math3.linear.ArrayRealVector r2 = r1.newPoint
            double r2 = r2.getEntry(r0)
            org.apache.commons.math3.linear.ArrayRealVector r4 = r1.upperDifference
            double r62 = r4.getEntry(r0)
            int r2 = (r2 > r62 ? 1 : (r2 == r62 ? 0 : -1))
            if (r2 != 0) goto L_0x0d98
            double r2 = r15.getEntry(r0)
            double r2 = -r2
            r60 = r2
        L_0x0d98:
            int r2 = (r60 > r35 ? 1 : (r60 == r35 ? 0 : -1))
            if (r2 >= 0) goto L_0x0dd5
            org.apache.commons.math3.linear.ArrayRealVector r2 = r1.modelSecondDerivativesValues
            int r171 = r0 * r0
            int r171 = r0 + r171
            int r3 = r171 / 2
            double r2 = r2.getEntry(r3)
            r3 = r2
            r2 = 0
        L_0x0daa:
            if (r2 >= r12) goto L_0x0dc5
            r177 = r5
            org.apache.commons.math3.linear.Array2DRowRealMatrix r5 = r1.interpolationPoints
            double r62 = r5.getEntry(r2, r0)
            org.apache.commons.math3.linear.ArrayRealVector r5 = r1.modelSecondDerivativesParameters
            double r64 = r5.getEntry(r2)
            double r66 = r62 * r62
            double r64 = r64 * r66
            double r3 = r3 + r64
            int r2 = r2 + 1
            r5 = r177
            goto L_0x0daa
        L_0x0dc5:
            r177 = r5
            double r62 = r3 * r37
            double r62 = r62 * r116
            r2 = 0
            double r60 = r60 + r62
            int r2 = (r60 > r35 ? 1 : (r60 == r35 ? 0 : -1))
            if (r2 >= 0) goto L_0x0dd7
            r2 = 650(0x28a, float:9.11E-43)
            goto L_0x0ded
        L_0x0dd5:
            r177 = r5
        L_0x0dd7:
            int r50 = r0 + 1
            r0 = r172
            r2 = r174
            r4 = r176
            r5 = r177
            goto L_0x0d5d
        L_0x0de3:
            r1 = r198
            r174 = r2
            r176 = r4
            r177 = r5
            r2 = r24
        L_0x0ded:
            r0 = 680(0x2a8, float:9.53E-43)
            r6 = r8
            r60 = r13
            r7 = r15
            r66 = r76
            r58 = r116
            r14 = r144
            r5 = r145
            r15 = r160
            r16 = r165
            r3 = r168
            r62 = r169
            r64 = r172
            r13 = 20
            r8 = r1
            r1 = r147
            goto L_0x00b1
        L_0x0e0c:
            r167 = r3
            r176 = r4
            r177 = r5
            r13 = r60
            r172 = r64
            r2 = r0
            r1 = r198
            int r3 = r163 + 1
            r16 = r165
        L_0x0e1d:
            r0 = 90
            printState(r0)
            r4 = 4562254508917369340(0x3f50624dd2f1a9fc, double:0.001)
            double r4 = r4 * r21
            int r0 = (r16 > r4 ? 1 : (r16 == r4 ? 0 : -1))
            if (r0 > 0) goto L_0x1081
            r4 = 4598175219545276416(0x3fd0000000000000, double:0.25)
            double r4 = r4 * r21
            r6 = 0
            r0 = 0
        L_0x0e34:
            if (r0 >= r12) goto L_0x0ed0
            org.apache.commons.math3.linear.ArrayRealVector r2 = r1.modelSecondDerivativesParameters
            double r9 = r2.getEntry(r0)
            double r6 = r6 + r9
            r9 = -4620693217682128896(0xbfe0000000000000, double:-0.5)
            double r9 = r9 * r21
            r2 = 0
        L_0x0e42:
            if (r2 >= r11) goto L_0x0e5e
            r178 = r6
            org.apache.commons.math3.linear.Array2DRowRealMatrix r6 = r1.interpolationPoints
            double r6 = r6.getEntry(r0, r2)
            r180 = r13
            org.apache.commons.math3.linear.ArrayRealVector r13 = r1.trustRegionCenterOffset
            double r13 = r13.getEntry(r2)
            double r6 = r6 * r13
            double r9 = r9 + r6
            int r2 = r2 + 1
            r6 = r178
            r13 = r180
            goto L_0x0e42
        L_0x0e5e:
            r178 = r6
            r180 = r13
            r8.setEntry(r0, r9)
            double r6 = r9 * r37
            double r6 = r4 - r6
            r2 = 0
        L_0x0e6a:
            if (r2 >= r11) goto L_0x0ec8
            org.apache.commons.math3.linear.Array2DRowRealMatrix r13 = r1.bMatrix
            double r13 = r13.getEntry(r0, r2)
            r15.setEntry(r2, r13)
            org.apache.commons.math3.linear.ArrayRealVector r13 = r1.lagrangeValuesAtNewPoint
            org.apache.commons.math3.linear.Array2DRowRealMatrix r14 = r1.interpolationPoints
            double r35 = r14.getEntry(r0, r2)
            double r35 = r35 * r9
            org.apache.commons.math3.linear.ArrayRealVector r14 = r1.trustRegionCenterOffset
            double r58 = r14.getEntry(r2)
            double r58 = r58 * r6
            r182 = r6
            double r6 = r35 + r58
            r13.setEntry(r2, r6)
            int r6 = r12 + r2
            r7 = 0
        L_0x0e91:
            if (r7 > r2) goto L_0x0ec1
            org.apache.commons.math3.linear.Array2DRowRealMatrix r13 = r1.bMatrix
            org.apache.commons.math3.linear.Array2DRowRealMatrix r14 = r1.bMatrix
            double r35 = r14.getEntry(r6, r7)
            double r58 = r15.getEntry(r2)
            org.apache.commons.math3.linear.ArrayRealVector r14 = r1.lagrangeValuesAtNewPoint
            double r60 = r14.getEntry(r7)
            double r58 = r58 * r60
            double r35 = r35 + r58
            org.apache.commons.math3.linear.ArrayRealVector r14 = r1.lagrangeValuesAtNewPoint
            double r58 = r14.getEntry(r2)
            double r60 = r15.getEntry(r7)
            double r58 = r58 * r60
            r184 = r9
            double r9 = r35 + r58
            r13.setEntry(r6, r7, r9)
            int r7 = r7 + 1
            r9 = r184
            goto L_0x0e91
        L_0x0ec1:
            r184 = r9
            int r2 = r2 + 1
            r6 = r182
            goto L_0x0e6a
        L_0x0ec8:
            int r0 = r0 + 1
            r6 = r178
            r13 = r180
            goto L_0x0e34
        L_0x0ed0:
            r180 = r13
            r0 = 0
        L_0x0ed3:
            r2 = r144
            if (r0 >= r2) goto L_0x0f9e
            r9 = 0
            r13 = 0
            r35 = r13
            r13 = r9
            r9 = 0
        L_0x0edf:
            if (r9 >= r12) goto L_0x0f0d
            org.apache.commons.math3.linear.Array2DRowRealMatrix r10 = r1.zMatrix
            double r58 = r10.getEntry(r9, r0)
            double r13 = r13 + r58
            org.apache.commons.math3.linear.ArrayRealVector r10 = r1.lagrangeValuesAtNewPoint
            double r58 = r8.getEntry(r9)
            r186 = r2
            org.apache.commons.math3.linear.Array2DRowRealMatrix r2 = r1.zMatrix
            double r60 = r2.getEntry(r9, r0)
            r187 = r13
            double r13 = r58 * r60
            r10.setEntry(r9, r13)
            org.apache.commons.math3.linear.ArrayRealVector r2 = r1.lagrangeValuesAtNewPoint
            double r13 = r2.getEntry(r9)
            double r35 = r35 + r13
            int r9 = r9 + 1
            r2 = r186
            r13 = r187
            goto L_0x0edf
        L_0x0f0d:
            r186 = r2
            r2 = 0
        L_0x0f10:
            if (r2 >= r11) goto L_0x0f65
            double r9 = r4 * r13
            double r58 = r35 * r37
            double r9 = r9 - r58
            r189 = r4
            org.apache.commons.math3.linear.ArrayRealVector r4 = r1.trustRegionCenterOffset
            double r4 = r4.getEntry(r2)
            double r9 = r9 * r4
            r4 = 0
        L_0x0f23:
            if (r4 >= r12) goto L_0x0f38
            org.apache.commons.math3.linear.ArrayRealVector r5 = r1.lagrangeValuesAtNewPoint
            double r58 = r5.getEntry(r4)
            org.apache.commons.math3.linear.Array2DRowRealMatrix r5 = r1.interpolationPoints
            double r60 = r5.getEntry(r4, r2)
            double r58 = r58 * r60
            double r9 = r9 + r58
            int r4 = r4 + 1
            goto L_0x0f23
        L_0x0f38:
            r15.setEntry(r2, r9)
            r4 = 0
        L_0x0f3c:
            if (r4 >= r12) goto L_0x0f5e
            org.apache.commons.math3.linear.Array2DRowRealMatrix r5 = r1.bMatrix
            r191 = r8
            org.apache.commons.math3.linear.Array2DRowRealMatrix r8 = r1.bMatrix
            double r58 = r8.getEntry(r4, r2)
            org.apache.commons.math3.linear.Array2DRowRealMatrix r8 = r1.zMatrix
            double r60 = r8.getEntry(r4, r0)
            double r60 = r60 * r9
            r192 = r9
            double r8 = r58 + r60
            r5.setEntry(r4, r2, r8)
            int r4 = r4 + 1
            r8 = r191
            r9 = r192
            goto L_0x0f3c
        L_0x0f5e:
            r191 = r8
            int r2 = r2 + 1
            r4 = r189
            goto L_0x0f10
        L_0x0f65:
            r189 = r4
            r191 = r8
            r2 = 0
        L_0x0f6a:
            if (r2 >= r11) goto L_0x0f94
            int r4 = r2 + r12
            double r8 = r15.getEntry(r2)
            r5 = 0
        L_0x0f73:
            if (r5 > r2) goto L_0x0f8f
            org.apache.commons.math3.linear.Array2DRowRealMatrix r10 = r1.bMatrix
            r194 = r13
            org.apache.commons.math3.linear.Array2DRowRealMatrix r13 = r1.bMatrix
            double r13 = r13.getEntry(r4, r5)
            double r58 = r15.getEntry(r5)
            double r58 = r58 * r8
            double r13 = r13 + r58
            r10.setEntry(r4, r5, r13)
            int r5 = r5 + 1
            r13 = r194
            goto L_0x0f73
        L_0x0f8f:
            r194 = r13
            int r2 = r2 + 1
            goto L_0x0f6a
        L_0x0f94:
            int r0 = r0 + 1
            r144 = r186
            r4 = r189
            r8 = r191
            goto L_0x0ed3
        L_0x0f9e:
            r186 = r2
            r189 = r4
            r191 = r8
            r0 = 0
            r2 = r0
            r0 = 0
        L_0x0fa7:
            if (r0 >= r11) goto L_0x1027
            r4 = -4620693217682128896(0xbfe0000000000000, double:-0.5)
            double r4 = r4 * r6
            org.apache.commons.math3.linear.ArrayRealVector r8 = r1.trustRegionCenterOffset
            double r8 = r8.getEntry(r0)
            double r4 = r4 * r8
            r15.setEntry(r0, r4)
            r4 = 0
        L_0x0fb9:
            if (r4 >= r12) goto L_0x0fe6
            double r8 = r15.getEntry(r0)
            org.apache.commons.math3.linear.ArrayRealVector r5 = r1.modelSecondDerivativesParameters
            double r13 = r5.getEntry(r4)
            org.apache.commons.math3.linear.Array2DRowRealMatrix r5 = r1.interpolationPoints
            double r35 = r5.getEntry(r4, r0)
            double r13 = r13 * r35
            double r8 = r8 + r13
            r15.setEntry(r0, r8)
            org.apache.commons.math3.linear.Array2DRowRealMatrix r5 = r1.interpolationPoints
            org.apache.commons.math3.linear.Array2DRowRealMatrix r8 = r1.interpolationPoints
            double r8 = r8.getEntry(r4, r0)
            org.apache.commons.math3.linear.ArrayRealVector r10 = r1.trustRegionCenterOffset
            double r13 = r10.getEntry(r0)
            double r8 = r8 - r13
            r5.setEntry(r4, r0, r8)
            int r4 = r4 + 1
            goto L_0x0fb9
        L_0x0fe6:
            r4 = r2
            r2 = 0
        L_0x0fe8:
            if (r2 > r0) goto L_0x1023
            org.apache.commons.math3.linear.ArrayRealVector r5 = r1.modelSecondDerivativesValues
            org.apache.commons.math3.linear.ArrayRealVector r8 = r1.modelSecondDerivativesValues
            double r8 = r8.getEntry(r4)
            double r13 = r15.getEntry(r2)
            org.apache.commons.math3.linear.ArrayRealVector r10 = r1.trustRegionCenterOffset
            double r35 = r10.getEntry(r0)
            double r13 = r13 * r35
            double r8 = r8 + r13
            org.apache.commons.math3.linear.ArrayRealVector r10 = r1.trustRegionCenterOffset
            double r13 = r10.getEntry(r2)
            double r35 = r15.getEntry(r0)
            double r13 = r13 * r35
            double r8 = r8 + r13
            r5.setEntry(r4, r8)
            org.apache.commons.math3.linear.Array2DRowRealMatrix r5 = r1.bMatrix
            int r8 = r12 + r2
            org.apache.commons.math3.linear.Array2DRowRealMatrix r9 = r1.bMatrix
            int r10 = r12 + r0
            double r9 = r9.getEntry(r10, r2)
            r5.setEntry(r8, r0, r9)
            int r4 = r4 + 1
            int r2 = r2 + 1
            goto L_0x0fe8
        L_0x1023:
            int r0 = r0 + 1
            r2 = r4
            goto L_0x0fa7
        L_0x1027:
            r0 = 0
        L_0x1028:
            if (r0 >= r11) goto L_0x107c
            org.apache.commons.math3.linear.ArrayRealVector r4 = r1.originShift
            org.apache.commons.math3.linear.ArrayRealVector r5 = r1.originShift
            double r8 = r5.getEntry(r0)
            org.apache.commons.math3.linear.ArrayRealVector r5 = r1.trustRegionCenterOffset
            double r13 = r5.getEntry(r0)
            double r8 = r8 + r13
            r4.setEntry(r0, r8)
            org.apache.commons.math3.linear.ArrayRealVector r4 = r1.newPoint
            org.apache.commons.math3.linear.ArrayRealVector r5 = r1.newPoint
            double r8 = r5.getEntry(r0)
            org.apache.commons.math3.linear.ArrayRealVector r5 = r1.trustRegionCenterOffset
            double r13 = r5.getEntry(r0)
            double r8 = r8 - r13
            r4.setEntry(r0, r8)
            org.apache.commons.math3.linear.ArrayRealVector r4 = r1.lowerDifference
            org.apache.commons.math3.linear.ArrayRealVector r5 = r1.lowerDifference
            double r8 = r5.getEntry(r0)
            org.apache.commons.math3.linear.ArrayRealVector r5 = r1.trustRegionCenterOffset
            double r13 = r5.getEntry(r0)
            double r8 = r8 - r13
            r4.setEntry(r0, r8)
            org.apache.commons.math3.linear.ArrayRealVector r4 = r1.upperDifference
            org.apache.commons.math3.linear.ArrayRealVector r5 = r1.upperDifference
            double r8 = r5.getEntry(r0)
            org.apache.commons.math3.linear.ArrayRealVector r5 = r1.trustRegionCenterOffset
            double r13 = r5.getEntry(r0)
            double r8 = r8 - r13
            r4.setEntry(r0, r8)
            org.apache.commons.math3.linear.ArrayRealVector r4 = r1.trustRegionCenterOffset
            r8 = 0
            r4.setEntry(r0, r8)
            int r0 = r0 + 1
            goto L_0x1028
        L_0x107c:
            r4 = 0
            r21 = r4
            goto L_0x1087
        L_0x1081:
            r191 = r8
            r180 = r13
            r186 = r144
        L_0x1087:
            if (r3 != 0) goto L_0x10a3
            r0 = 210(0xd2, float:2.94E-43)
        L_0x108b:
            r8 = r1
            r7 = r15
            r66 = r76
            r58 = r116
            r5 = r145
            r1 = r147
            r15 = r160
            r64 = r172
            r60 = r180
            r14 = r186
            r6 = r191
        L_0x109f:
            r13 = 20
            goto L_0x00b1
        L_0x10a3:
            r0 = 230(0xe6, float:3.22E-43)
            goto L_0x108b
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.optimization.direct.BOBYQAOptimizer.bobyqb(double[], double[]):double");
    }

    /* JADX WARNING: Removed duplicated region for block: B:151:0x05a5  */
    /* JADX WARNING: Removed duplicated region for block: B:181:0x05df A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private double[] altmov(int r81, double r82) {
        /*
            r80 = this;
            r0 = r80
            r1 = r81
            printMethod()
            org.apache.commons.math3.linear.ArrayRealVector r4 = r0.currentBest
            int r4 = r4.getDimension()
            int r5 = r0.numberOfInterpolationPoints
            org.apache.commons.math3.linear.ArrayRealVector r6 = new org.apache.commons.math3.linear.ArrayRealVector
            r6.<init>(r4)
            org.apache.commons.math3.linear.ArrayRealVector r7 = new org.apache.commons.math3.linear.ArrayRealVector
            r7.<init>(r5)
            org.apache.commons.math3.linear.ArrayRealVector r8 = new org.apache.commons.math3.linear.ArrayRealVector
            r8.<init>(r4)
            org.apache.commons.math3.linear.ArrayRealVector r9 = new org.apache.commons.math3.linear.ArrayRealVector
            r9.<init>(r4)
            r11 = 0
        L_0x0024:
            r12 = 0
            if (r11 >= r5) goto L_0x002e
            r7.setEntry(r11, r12)
            int r11 = r11 + 1
            goto L_0x0024
        L_0x002e:
            r11 = 0
            int r14 = r5 - r4
            r15 = 1
            int r14 = r14 - r15
        L_0x0033:
            if (r11 >= r14) goto L_0x005b
            org.apache.commons.math3.linear.Array2DRowRealMatrix r10 = r0.zMatrix
            double r16 = r10.getEntry(r1, r11)
            r10 = 0
        L_0x003c:
            if (r10 >= r5) goto L_0x0055
            double r18 = r7.getEntry(r10)
            org.apache.commons.math3.linear.Array2DRowRealMatrix r15 = r0.zMatrix
            double r21 = r15.getEntry(r10, r11)
            double r21 = r21 * r16
            double r12 = r18 + r21
            r7.setEntry(r10, r12)
            int r10 = r10 + 1
            r12 = 0
            r15 = 1
            goto L_0x003c
        L_0x0055:
            int r11 = r11 + 1
            r12 = 0
            r15 = 1
            goto L_0x0033
        L_0x005b:
            double r10 = r7.getEntry(r1)
            r12 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            double r14 = r10 * r12
            r16 = 0
        L_0x0065:
            r25 = r16
            r12 = r25
            if (r12 >= r4) goto L_0x0081
            org.apache.commons.math3.linear.Array2DRowRealMatrix r13 = r0.bMatrix
            r27 = r9
            r28 = r10
            double r9 = r13.getEntry(r1, r12)
            r6.setEntry(r12, r9)
            int r16 = r12 + 1
            r9 = r27
            r10 = r28
            r12 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            goto L_0x0065
        L_0x0081:
            r27 = r9
            r28 = r10
            r9 = 0
        L_0x0086:
            if (r9 >= r5) goto L_0x00c5
            r10 = 0
            r11 = r10
            r10 = 0
        L_0x008c:
            if (r10 >= r4) goto L_0x00a1
            org.apache.commons.math3.linear.Array2DRowRealMatrix r13 = r0.interpolationPoints
            double r16 = r13.getEntry(r9, r10)
            org.apache.commons.math3.linear.ArrayRealVector r13 = r0.trustRegionCenterOffset
            double r18 = r13.getEntry(r10)
            double r16 = r16 * r18
            double r11 = r11 + r16
            int r10 = r10 + 1
            goto L_0x008c
        L_0x00a1:
            double r16 = r7.getEntry(r9)
            double r11 = r11 * r16
            r10 = 0
        L_0x00a8:
            if (r10 >= r4) goto L_0x00c2
            double r16 = r6.getEntry(r10)
            org.apache.commons.math3.linear.Array2DRowRealMatrix r13 = r0.interpolationPoints
            double r18 = r13.getEntry(r9, r10)
            double r18 = r18 * r11
            r30 = r11
            double r11 = r16 + r18
            r6.setEntry(r10, r11)
            int r10 = r10 + 1
            r11 = r30
            goto L_0x00a8
        L_0x00c2:
            int r9 = r9 + 1
            goto L_0x0086
        L_0x00c5:
            r9 = 0
            r11 = 9221120237041090560(0x7ff8000000000000, double:NaN)
            r13 = 0
            r16 = 0
            r17 = 0
            r21 = r9
            r10 = r16
            r9 = 0
        L_0x00d3:
            r32 = r11
            if (r9 >= r5) goto L_0x02cb
            int r11 = r0.trustRegionCenterInterpolationPointIndex
            if (r9 != r11) goto L_0x00ea
            r42 = r5
            r43 = r6
            r36 = r7
            r37 = r8
            r50 = r14
            r11 = r32
            goto L_0x02bb
        L_0x00ea:
            r11 = 0
            r30 = 0
            r36 = r7
            r37 = r8
            r7 = r30
            r30 = r11
            r11 = 0
        L_0x00f7:
            if (r11 >= r4) goto L_0x0116
            org.apache.commons.math3.linear.Array2DRowRealMatrix r12 = r0.interpolationPoints
            double r38 = r12.getEntry(r9, r11)
            org.apache.commons.math3.linear.ArrayRealVector r12 = r0.trustRegionCenterOffset
            double r40 = r12.getEntry(r11)
            double r38 = r38 - r40
            double r40 = r6.getEntry(r11)
            double r40 = r40 * r38
            double r30 = r30 + r40
            double r40 = r38 * r38
            double r7 = r7 + r40
            int r11 = r11 + 1
            goto L_0x00f7
        L_0x0116:
            double r11 = org.apache.commons.math3.util.FastMath.sqrt(r7)
            double r11 = r82 / r11
            r42 = r5
            r43 = r6
            double r5 = -r11
            r16 = 0
            r19 = 0
            r44 = r5
            r5 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r2 = org.apache.commons.math3.util.FastMath.min(r5, r11)
            r5 = 0
        L_0x012e:
            if (r5 >= r4) goto L_0x0207
            org.apache.commons.math3.linear.Array2DRowRealMatrix r6 = r0.interpolationPoints
            double r38 = r6.getEntry(r9, r5)
            org.apache.commons.math3.linear.ArrayRealVector r6 = r0.trustRegionCenterOffset
            double r40 = r6.getEntry(r5)
            double r38 = r38 - r40
            r23 = 0
            int r6 = (r38 > r23 ? 1 : (r38 == r23 ? 0 : -1))
            if (r6 <= 0) goto L_0x01a3
            double r40 = r44 * r38
            org.apache.commons.math3.linear.ArrayRealVector r6 = r0.lowerDifference
            double r46 = r6.getEntry(r5)
            org.apache.commons.math3.linear.ArrayRealVector r6 = r0.trustRegionCenterOffset
            double r48 = r6.getEntry(r5)
            double r46 = r46 - r48
            int r6 = (r40 > r46 ? 1 : (r40 == r46 ? 0 : -1))
            if (r6 >= 0) goto L_0x016d
            org.apache.commons.math3.linear.ArrayRealVector r6 = r0.lowerDifference
            double r40 = r6.getEntry(r5)
            org.apache.commons.math3.linear.ArrayRealVector r6 = r0.trustRegionCenterOffset
            double r46 = r6.getEntry(r5)
            double r40 = r40 - r46
            double r44 = r40 / r38
            int r6 = -r5
            r20 = 1
            int r16 = r6 + -1
        L_0x016d:
            double r40 = r11 * r38
            org.apache.commons.math3.linear.ArrayRealVector r6 = r0.upperDifference
            double r46 = r6.getEntry(r5)
            org.apache.commons.math3.linear.ArrayRealVector r6 = r0.trustRegionCenterOffset
            double r48 = r6.getEntry(r5)
            double r46 = r46 - r48
            int r6 = (r40 > r46 ? 1 : (r40 == r46 ? 0 : -1))
            if (r6 <= 0) goto L_0x019e
            org.apache.commons.math3.linear.ArrayRealVector r6 = r0.upperDifference
            double r40 = r6.getEntry(r5)
            org.apache.commons.math3.linear.ArrayRealVector r6 = r0.trustRegionCenterOffset
            double r46 = r6.getEntry(r5)
            double r40 = r40 - r46
            r52 = r13
            r50 = r14
            double r13 = r40 / r38
            double r11 = org.apache.commons.math3.util.FastMath.max(r2, r13)
            int r6 = r5 + 1
        L_0x019b:
            r19 = r6
            goto L_0x01ff
        L_0x019e:
            r52 = r13
            r50 = r14
            goto L_0x01ff
        L_0x01a3:
            r52 = r13
            r50 = r14
            r13 = 0
            int r6 = (r38 > r13 ? 1 : (r38 == r13 ? 0 : -1))
            if (r6 >= 0) goto L_0x01ff
            double r13 = r44 * r38
            org.apache.commons.math3.linear.ArrayRealVector r6 = r0.upperDifference
            double r40 = r6.getEntry(r5)
            org.apache.commons.math3.linear.ArrayRealVector r6 = r0.trustRegionCenterOffset
            double r46 = r6.getEntry(r5)
            double r40 = r40 - r46
            int r6 = (r13 > r40 ? 1 : (r13 == r40 ? 0 : -1))
            if (r6 <= 0) goto L_0x01d3
            org.apache.commons.math3.linear.ArrayRealVector r6 = r0.upperDifference
            double r13 = r6.getEntry(r5)
            org.apache.commons.math3.linear.ArrayRealVector r6 = r0.trustRegionCenterOffset
            double r40 = r6.getEntry(r5)
            double r13 = r13 - r40
            double r44 = r13 / r38
            int r16 = r5 + 1
        L_0x01d3:
            double r13 = r11 * r38
            org.apache.commons.math3.linear.ArrayRealVector r6 = r0.lowerDifference
            double r40 = r6.getEntry(r5)
            org.apache.commons.math3.linear.ArrayRealVector r6 = r0.trustRegionCenterOffset
            double r46 = r6.getEntry(r5)
            double r40 = r40 - r46
            int r6 = (r13 > r40 ? 1 : (r13 == r40 ? 0 : -1))
            if (r6 >= 0) goto L_0x01ff
            org.apache.commons.math3.linear.ArrayRealVector r6 = r0.lowerDifference
            double r13 = r6.getEntry(r5)
            org.apache.commons.math3.linear.ArrayRealVector r6 = r0.trustRegionCenterOffset
            double r40 = r6.getEntry(r5)
            double r13 = r13 - r40
            double r13 = r13 / r38
            double r11 = org.apache.commons.math3.util.FastMath.max(r2, r13)
            int r6 = -r5
            r13 = 1
            int r6 = r6 - r13
            goto L_0x019b
        L_0x01ff:
            int r5 = r5 + 1
            r14 = r50
            r13 = r52
            goto L_0x012e
        L_0x0207:
            r52 = r13
            r50 = r14
            r5 = r44
            r13 = r16
            r14 = 9221120237041090560(0x7ff8000000000000, double:NaN)
            if (r9 != r1) goto L_0x0261
            r32 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r38 = r30 - r32
            double r32 = r44 * r38
            double r32 = r30 - r32
            double r14 = r44 * r32
            double r32 = r11 * r38
            double r32 = r30 - r32
            r53 = r2
            double r1 = r11 * r32
            double r32 = org.apache.commons.math3.util.FastMath.abs(r1)
            double r40 = org.apache.commons.math3.util.FastMath.abs(r14)
            int r3 = (r32 > r40 ? 1 : (r32 == r40 ? 0 : -1))
            if (r3 <= 0) goto L_0x0235
            r5 = r11
            r14 = r1
            r13 = r19
        L_0x0235:
            r25 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            double r32 = r30 * r25
            double r40 = r38 * r44
            r3 = 0
            double r40 = r32 - r40
            double r46 = r38 * r11
            double r46 = r32 - r46
            double r48 = r40 * r46
            r23 = 0
            int r3 = (r48 > r23 ? 1 : (r48 == r23 ? 0 : -1))
            if (r3 >= 0) goto L_0x0260
            double r48 = r32 * r32
            r55 = r1
            double r1 = r48 / r38
            double r48 = org.apache.commons.math3.util.FastMath.abs(r1)
            double r57 = org.apache.commons.math3.util.FastMath.abs(r14)
            int r3 = (r48 > r57 ? 1 : (r48 == r57 ? 0 : -1))
            if (r3 <= 0) goto L_0x0260
            double r5 = r32 / r38
            r14 = r1
            r13 = 0
        L_0x0260:
            goto L_0x0296
        L_0x0261:
            r53 = r2
            r1 = 0
            r1 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r32 = r1 - r44
            double r14 = r44 * r32
            double r32 = r1 - r11
            double r1 = r11 * r32
            double r32 = org.apache.commons.math3.util.FastMath.abs(r1)
            double r38 = org.apache.commons.math3.util.FastMath.abs(r14)
            int r3 = (r32 > r38 ? 1 : (r32 == r38 ? 0 : -1))
            if (r3 <= 0) goto L_0x027e
            r5 = r11
            r14 = r1
            r13 = r19
        L_0x027e:
            r25 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            int r3 = (r11 > r25 ? 1 : (r11 == r25 ? 0 : -1))
            if (r3 <= 0) goto L_0x0294
            double r32 = org.apache.commons.math3.util.FastMath.abs(r14)
            r38 = 4598175219545276416(0x3fd0000000000000, double:0.25)
            int r3 = (r32 > r38 ? 1 : (r32 == r38 ? 0 : -1))
            if (r3 >= 0) goto L_0x0294
            r5 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            r14 = 4598175219545276416(0x3fd0000000000000, double:0.25)
            r3 = 0
            r13 = r3
        L_0x0294:
            double r14 = r14 * r30
        L_0x0296:
            r1 = 0
            r1 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r1 = r1 - r5
            double r1 = r1 * r5
            double r1 = r1 * r7
            double r32 = r14 * r14
            double r34 = r14 * r14
            double r38 = r50 * r1
            double r38 = r38 * r1
            double r34 = r34 + r38
            double r32 = r32 * r34
            int r3 = (r32 > r21 ? 1 : (r32 == r21 ? 0 : -1))
            if (r3 <= 0) goto L_0x02b8
            r21 = r32
            r3 = r9
            r17 = r5
            r1 = r13
            r10 = r1
            r13 = r3
            r11 = r5
            goto L_0x02bb
        L_0x02b8:
            r11 = r5
            r13 = r52
        L_0x02bb:
            int r9 = r9 + 1
            r7 = r36
            r8 = r37
            r5 = r42
            r6 = r43
            r14 = r50
            r1 = r81
            goto L_0x00d3
        L_0x02cb:
            r42 = r5
            r43 = r6
            r36 = r7
            r37 = r8
            r52 = r13
            r50 = r14
            r1 = 0
        L_0x02d8:
            if (r1 >= r4) goto L_0x030e
            org.apache.commons.math3.linear.ArrayRealVector r2 = r0.trustRegionCenterOffset
            double r2 = r2.getEntry(r1)
            org.apache.commons.math3.linear.Array2DRowRealMatrix r5 = r0.interpolationPoints
            r13 = r52
            double r5 = r5.getEntry(r13, r1)
            org.apache.commons.math3.linear.ArrayRealVector r7 = r0.trustRegionCenterOffset
            double r7 = r7.getEntry(r1)
            double r5 = r5 - r7
            double r5 = r5 * r17
            double r2 = r2 + r5
            org.apache.commons.math3.linear.ArrayRealVector r5 = r0.newPoint
            org.apache.commons.math3.linear.ArrayRealVector r6 = r0.lowerDifference
            double r6 = r6.getEntry(r1)
            org.apache.commons.math3.linear.ArrayRealVector r8 = r0.upperDifference
            double r8 = r8.getEntry(r1)
            double r8 = org.apache.commons.math3.util.FastMath.min(r8, r2)
            double r6 = org.apache.commons.math3.util.FastMath.max(r6, r8)
            r5.setEntry(r1, r6)
            int r1 = r1 + 1
            goto L_0x02d8
        L_0x030e:
            r13 = r52
            if (r10 >= 0) goto L_0x0322
            org.apache.commons.math3.linear.ArrayRealVector r1 = r0.newPoint
            int r2 = -r10
            r3 = 1
            int r2 = r2 - r3
            org.apache.commons.math3.linear.ArrayRealVector r5 = r0.lowerDifference
            int r6 = -r10
            int r6 = r6 - r3
            double r5 = r5.getEntry(r6)
            r1.setEntry(r2, r5)
        L_0x0322:
            if (r10 <= 0) goto L_0x0333
            org.apache.commons.math3.linear.ArrayRealVector r1 = r0.newPoint
            int r2 = r10 + -1
            org.apache.commons.math3.linear.ArrayRealVector r3 = r0.upperDifference
            int r5 = r10 + -1
            double r5 = r3.getEntry(r5)
            r1.setEntry(r2, r5)
        L_0x0333:
            r1 = 0
            double r5 = r82 + r82
            r3 = 0
            r7 = 9221120237041090560(0x7ff8000000000000, double:NaN)
            r11 = r7
            r7 = 0
        L_0x033c:
            r14 = 0
            r30 = 0
            r9 = 0
        L_0x0341:
            if (r9 >= r4) goto L_0x03a0
            r59 = r10
            r60 = r11
            r10 = r43
            double r11 = r10.getEntry(r9)
            r63 = r7
            r62 = r13
            r13 = r37
            r7 = 0
            r13.setEntry(r9, r7)
            org.apache.commons.math3.linear.ArrayRealVector r7 = r0.trustRegionCenterOffset
            double r7 = r7.getEntry(r9)
            r65 = r3
            org.apache.commons.math3.linear.ArrayRealVector r3 = r0.lowerDifference
            double r23 = r3.getEntry(r9)
            double r7 = r7 - r23
            double r7 = org.apache.commons.math3.util.FastMath.min(r7, r11)
            r23 = 0
            int r3 = (r7 > r23 ? 1 : (r7 == r23 ? 0 : -1))
            if (r3 > 0) goto L_0x0388
            org.apache.commons.math3.linear.ArrayRealVector r3 = r0.trustRegionCenterOffset
            double r7 = r3.getEntry(r9)
            org.apache.commons.math3.linear.ArrayRealVector r3 = r0.upperDifference
            double r37 = r3.getEntry(r9)
            double r7 = r7 - r37
            double r7 = org.apache.commons.math3.util.FastMath.max(r7, r11)
            int r3 = (r7 > r23 ? 1 : (r7 == r23 ? 0 : -1))
            if (r3 >= 0) goto L_0x038f
        L_0x0388:
            r13.setEntry(r9, r5)
            double r7 = r11 * r11
            double r30 = r30 + r7
        L_0x038f:
            int r9 = r9 + 1
            r43 = r10
            r37 = r13
            r10 = r59
            r11 = r60
            r13 = r62
            r7 = r63
            r3 = r65
            goto L_0x0341
        L_0x03a0:
            r65 = r3
            r63 = r7
            r59 = r10
            r60 = r11
            r62 = r13
            r13 = r37
            r10 = r43
            r7 = 0
            int r3 = (r30 > r7 ? 1 : (r30 == r7 ? 0 : -1))
            r9 = 2
            if (r3 != 0) goto L_0x03be
            double[] r3 = new double[r9]
            r9 = 0
            r3[r9] = r28
            r9 = 1
            r3[r9] = r7
            return r3
        L_0x03be:
            double r11 = r82 * r82
            r3 = 0
            double r11 = r11 - r14
            int r3 = (r11 > r7 ? 1 : (r11 == r7 ? 0 : -1))
            if (r3 <= 0) goto L_0x043b
            double r7 = r11 / r30
            double r7 = org.apache.commons.math3.util.FastMath.sqrt(r7)
            r30 = 0
            r3 = 0
        L_0x03cf:
            if (r3 >= r4) goto L_0x043d
            double r32 = r13.getEntry(r3)
            int r16 = (r32 > r5 ? 1 : (r32 == r5 ? 0 : -1))
            if (r16 != 0) goto L_0x0437
            org.apache.commons.math3.linear.ArrayRealVector r9 = r0.trustRegionCenterOffset
            double r32 = r9.getEntry(r3)
            double r37 = r10.getEntry(r3)
            double r37 = r37 * r7
            double r32 = r32 - r37
            org.apache.commons.math3.linear.ArrayRealVector r9 = r0.lowerDifference
            double r37 = r9.getEntry(r3)
            int r9 = (r32 > r37 ? 1 : (r32 == r37 ? 0 : -1))
            if (r9 > 0) goto L_0x040b
            org.apache.commons.math3.linear.ArrayRealVector r9 = r0.lowerDifference
            double r37 = r9.getEntry(r3)
            org.apache.commons.math3.linear.ArrayRealVector r9 = r0.trustRegionCenterOffset
            double r39 = r9.getEntry(r3)
            double r1 = r37 - r39
            r13.setEntry(r3, r1)
            double r1 = r13.getEntry(r3)
            double r37 = r1 * r1
            double r14 = r14 + r37
            goto L_0x0437
        L_0x040b:
            org.apache.commons.math3.linear.ArrayRealVector r1 = r0.upperDifference
            double r1 = r1.getEntry(r3)
            int r1 = (r32 > r1 ? 1 : (r32 == r1 ? 0 : -1))
            if (r1 < 0) goto L_0x042f
            org.apache.commons.math3.linear.ArrayRealVector r1 = r0.upperDifference
            double r1 = r1.getEntry(r3)
            org.apache.commons.math3.linear.ArrayRealVector r9 = r0.trustRegionCenterOffset
            double r37 = r9.getEntry(r3)
            double r1 = r1 - r37
            r13.setEntry(r3, r1)
            double r1 = r13.getEntry(r3)
            double r37 = r1 * r1
            double r14 = r14 + r37
            goto L_0x0437
        L_0x042f:
            double r1 = r10.getEntry(r3)
            double r37 = r1 * r1
            double r30 = r30 + r37
        L_0x0437:
            int r3 = r3 + 1
            r9 = 2
            goto L_0x03cf
        L_0x043b:
            r7 = r32
        L_0x043d:
            r1 = 0
            r2 = r1
            r1 = 0
        L_0x0441:
            if (r1 >= r4) goto L_0x04c8
            double r32 = r10.getEntry(r1)
            double r37 = r13.getEntry(r1)
            int r9 = (r37 > r5 ? 1 : (r37 == r5 ? 0 : -1))
            if (r9 != 0) goto L_0x0482
            r66 = r5
            double r5 = -r7
            double r5 = r5 * r32
            r13.setEntry(r1, r5)
            org.apache.commons.math3.linear.ArrayRealVector r5 = r0.upperDifference
            double r5 = r5.getEntry(r1)
            org.apache.commons.math3.linear.ArrayRealVector r9 = r0.trustRegionCenterOffset
            double r37 = r9.getEntry(r1)
            double r39 = r13.getEntry(r1)
            r68 = r7
            double r7 = r37 + r39
            double r5 = org.apache.commons.math3.util.FastMath.min(r5, r7)
            org.apache.commons.math3.linear.ArrayRealVector r7 = r0.alternativeNewPoint
            org.apache.commons.math3.linear.ArrayRealVector r8 = r0.lowerDifference
            double r8 = r8.getEntry(r1)
            double r8 = org.apache.commons.math3.util.FastMath.max(r8, r5)
            r7.setEntry(r1, r8)
        L_0x047f:
            r5 = 0
            goto L_0x04b9
        L_0x0482:
            r66 = r5
            r68 = r7
            double r5 = r13.getEntry(r1)
            r7 = 0
            int r5 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r5 != 0) goto L_0x049c
            org.apache.commons.math3.linear.ArrayRealVector r5 = r0.alternativeNewPoint
            org.apache.commons.math3.linear.ArrayRealVector r6 = r0.trustRegionCenterOffset
            double r7 = r6.getEntry(r1)
            r5.setEntry(r1, r7)
            goto L_0x047f
        L_0x049c:
            r5 = 0
            int r7 = (r32 > r5 ? 1 : (r32 == r5 ? 0 : -1))
            if (r7 <= 0) goto L_0x04ae
            org.apache.commons.math3.linear.ArrayRealVector r7 = r0.alternativeNewPoint
            org.apache.commons.math3.linear.ArrayRealVector r8 = r0.lowerDifference
            double r8 = r8.getEntry(r1)
            r7.setEntry(r1, r8)
            goto L_0x04b9
        L_0x04ae:
            org.apache.commons.math3.linear.ArrayRealVector r7 = r0.alternativeNewPoint
            org.apache.commons.math3.linear.ArrayRealVector r8 = r0.upperDifference
            double r8 = r8.getEntry(r1)
            r7.setEntry(r1, r8)
        L_0x04b9:
            double r7 = r13.getEntry(r1)
            double r7 = r7 * r32
            double r2 = r2 + r7
            int r1 = r1 + 1
            r5 = r66
            r7 = r68
            goto L_0x0441
        L_0x04c8:
            r66 = r5
            r68 = r7
            r5 = 0
            r7 = 0
            r1 = 0
        L_0x04d1:
            r9 = r42
            if (r1 >= r9) goto L_0x0505
            r23 = 0
            r16 = 0
        L_0x04d9:
            r70 = r16
            r5 = r70
            if (r5 >= r4) goto L_0x04f2
            org.apache.commons.math3.linear.Array2DRowRealMatrix r6 = r0.interpolationPoints
            double r32 = r6.getEntry(r1, r5)
            double r37 = r13.getEntry(r5)
            double r32 = r32 * r37
            double r23 = r23 + r32
            int r16 = r5 + 1
            r5 = 0
            goto L_0x04d9
        L_0x04f2:
            r5 = r36
            double r32 = r5.getEntry(r1)
            double r32 = r32 * r23
            double r32 = r32 * r23
            double r7 = r7 + r32
            int r1 = r1 + 1
            r42 = r9
            r5 = 0
            goto L_0x04d1
        L_0x0505:
            r5 = r36
            r1 = r65
            r6 = 1
            if (r1 != r6) goto L_0x050d
            double r7 = -r7
        L_0x050d:
            r71 = r5
            double r5 = -r2
            int r5 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
            if (r5 <= 0) goto L_0x058c
            double r5 = -r2
            r72 = r11
            r11 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r11 = org.apache.commons.math3.util.FastMath.sqrt(r11)
            r23 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r11 = r11 + r23
            double r5 = r5 * r11
            int r5 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
            if (r5 >= 0) goto L_0x057f
            double r5 = -r2
            double r5 = r5 / r7
            r11 = 0
        L_0x052a:
            if (r11 >= r4) goto L_0x056c
            org.apache.commons.math3.linear.ArrayRealVector r12 = r0.trustRegionCenterOffset
            double r32 = r12.getEntry(r11)
            double r34 = r13.getEntry(r11)
            double r34 = r34 * r5
            r74 = r13
            double r12 = r32 + r34
            r75 = r9
            org.apache.commons.math3.linear.ArrayRealVector r9 = r0.alternativeNewPoint
            r76 = r14
            org.apache.commons.math3.linear.ArrayRealVector r14 = r0.lowerDifference
            double r14 = r14.getEntry(r11)
            r78 = r10
            org.apache.commons.math3.linear.ArrayRealVector r10 = r0.upperDifference
            r79 = r1
            double r0 = r10.getEntry(r11)
            double r0 = org.apache.commons.math3.util.FastMath.min(r0, r12)
            double r0 = org.apache.commons.math3.util.FastMath.max(r14, r0)
            r9.setEntry(r11, r0)
            int r11 = r11 + 1
            r13 = r74
            r9 = r75
            r14 = r76
            r10 = r78
            r1 = r79
            r0 = r80
            goto L_0x052a
        L_0x056c:
            r79 = r1
            r75 = r9
            r78 = r10
            r74 = r13
            r76 = r14
            r0 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            double r12 = r2 * r0
            double r12 = r12 * r5
            double r12 = r12 * r12
            goto L_0x05a2
        L_0x057f:
            r79 = r1
            r75 = r9
            r78 = r10
            r74 = r13
            r76 = r14
            r0 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            goto L_0x059c
        L_0x058c:
            r79 = r1
            r75 = r9
            r78 = r10
            r72 = r11
            r74 = r13
            r76 = r14
            r0 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            r23 = 4607182418800017408(0x3ff0000000000000, double:1.0)
        L_0x059c:
            double r12 = r7 * r0
            r5 = 0
            double r12 = r12 + r2
            double r12 = r12 * r12
        L_0x05a2:
            r11 = r12
            if (r79 != 0) goto L_0x05df
            r5 = 0
        L_0x05a6:
            if (r5 >= r4) goto L_0x05c2
            r6 = r78
            double r9 = r6.getEntry(r5)
            double r9 = -r9
            r6.setEntry(r5, r9)
            r9 = r80
            org.apache.commons.math3.linear.ArrayRealVector r10 = r9.alternativeNewPoint
            double r13 = r10.getEntry(r5)
            r10 = r27
            r10.setEntry(r5, r13)
            int r5 = r5 + 1
            goto L_0x05a6
        L_0x05c2:
            r10 = r27
            r6 = r78
            r9 = r80
            r13 = r11
            r3 = 1
            r43 = r6
            r0 = r9
            r7 = r13
            r10 = r59
            r13 = r62
            r5 = r66
            r32 = r68
            r36 = r71
            r37 = r74
            r42 = r75
            goto L_0x033c
        L_0x05df:
            r10 = r27
            r6 = r78
            r9 = r80
            int r0 = (r63 > r11 ? 1 : (r63 == r11 ? 0 : -1))
            if (r0 <= 0) goto L_0x05fa
            r0 = 0
        L_0x05ea:
            if (r0 >= r4) goto L_0x05f8
            org.apache.commons.math3.linear.ArrayRealVector r1 = r9.alternativeNewPoint
            double r2 = r10.getEntry(r0)
            r1.setEntry(r0, r2)
            int r0 = r0 + 1
            goto L_0x05ea
        L_0x05f8:
            r11 = r63
        L_0x05fa:
            r0 = 2
            double[] r0 = new double[r0]
            r1 = 0
            r0[r1] = r28
            r1 = 1
            r0[r1] = r11
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.optimization.direct.BOBYQAOptimizer.altmov(int, double):double[]");
    }

    private void prelim(double[] lowerBound, double[] upperBound) {
        int jpt;
        double rhosq;
        int nfxm;
        int np;
        double stepa;
        int ipt;
        double stepb;
        double fbeg;
        int ipt2;
        int n;
        double fbeg2;
        int jpt2;
        double recip;
        double stepa2;
        double tmp;
        int jpt3;
        double stepb2;
        printMethod();
        int n2 = this.currentBest.getDimension();
        int npt = this.numberOfInterpolationPoints;
        int ndim = this.bMatrix.getRowDimension();
        double rhosq2 = this.initialTrustRegionRadius * this.initialTrustRegionRadius;
        double recip2 = ONE / rhosq2;
        int np2 = n2 + 1;
        int j = 0;
        while (j < n2) {
            this.originShift.setEntry(j, this.currentBest.getEntry(j));
            int k = 0;
            while (k < npt) {
                double recip3 = recip2;
                this.interpolationPoints.setEntry(k, j, 0.0d);
                k++;
                recip2 = recip3;
            }
            double recip4 = recip2;
            for (int i = 0; i < ndim; i++) {
                this.bMatrix.setEntry(i, j, 0.0d);
            }
            j++;
            recip2 = recip4;
        }
        double recip5 = recip2;
        int max = (n2 * np2) / 2;
        for (int i2 = 0; i2 < max; i2++) {
            this.modelSecondDerivativesValues.setEntry(i2, 0.0d);
        }
        for (int k2 = 0; k2 < npt; k2++) {
            double d = 0.0d;
            this.modelSecondDerivativesParameters.setEntry(k2, 0.0d);
            int j2 = 0;
            int max2 = npt - np2;
            while (j2 < max2) {
                this.zMatrix.setEntry(k2, j2, d);
                j2++;
                d = 0.0d;
            }
        }
        int ipt3 = 0;
        int jpt4 = 0;
        double fbeg3 = Double.NaN;
        while (true) {
            int nfm = getEvaluations();
            int nfx = nfm - n2;
            int nfmm = nfm - 1;
            int ndim2 = ndim;
            int ndim3 = nfx - 1;
            int ipt4 = ipt3;
            double fbeg4 = fbeg3;
            if (nfm > n2 * 2) {
                int tmp1 = (nfm - np2) / n2;
                jpt = (nfm - (tmp1 * n2)) - n2;
                int ipt5 = jpt + tmp1;
                if (ipt5 > n2) {
                    int tmp2 = jpt;
                    jpt = ipt5 - n2;
                    ipt5 = tmp2;
                }
                int iptMinus1 = ipt5 - 1;
                int i3 = tmp1;
                int jptMinus1 = jpt - 1;
                np = np2;
                nfxm = ndim3;
                rhosq = rhosq2;
                this.interpolationPoints.setEntry(nfm, iptMinus1, this.interpolationPoints.getEntry(ipt5, iptMinus1));
                this.interpolationPoints.setEntry(nfm, jptMinus1, this.interpolationPoints.getEntry(jpt, jptMinus1));
                ipt = ipt5;
                stepa = 0.0d;
                stepb = 0.0d;
            } else if (nfm >= 1 && nfm <= n2) {
                stepa = this.initialTrustRegionRadius;
                if (this.upperDifference.getEntry(nfmm) == 0.0d) {
                    stepa = -stepa;
                }
                this.interpolationPoints.setEntry(nfm, nfmm, stepa);
                nfxm = ndim3;
                rhosq = rhosq2;
                np = np2;
                stepb = 0.0d;
                ipt = ipt4;
            } else if (nfm > n2) {
                double stepa3 = this.interpolationPoints.getEntry(nfx, ndim3);
                double stepb3 = -this.initialTrustRegionRadius;
                if (this.lowerDifference.getEntry(ndim3) == 0.0d) {
                    jpt3 = jpt;
                    double d2 = stepb3;
                    stepb3 = FastMath.min(this.initialTrustRegionRadius * TWO, this.upperDifference.getEntry(ndim3));
                } else {
                    jpt3 = jpt;
                    double d3 = stepb3;
                }
                if (this.upperDifference.getEntry(ndim3) == 0.0d) {
                    double d4 = stepb3;
                    stepb2 = FastMath.max(this.initialTrustRegionRadius * -2.0d, this.lowerDifference.getEntry(ndim3));
                } else {
                    stepb2 = stepb3;
                }
                this.interpolationPoints.setEntry(nfm, ndim3, stepb2);
                nfxm = ndim3;
                rhosq = rhosq2;
                stepb = stepb2;
                np = np2;
                stepa = stepa3;
                ipt = ipt4;
                jpt = jpt3;
            } else {
                nfxm = ndim3;
                rhosq = rhosq2;
                np = np2;
                stepa = 0.0d;
                stepb = 0.0d;
                ipt = ipt4;
            }
            int j3 = 0;
            while (j3 < n2) {
                int ipt6 = ipt;
                int jpt5 = jpt;
                double stepb4 = stepb;
                int nfx2 = nfx;
                this.currentBest.setEntry(j3, FastMath.min(FastMath.max(lowerBound[j3], this.originShift.getEntry(j3) + this.interpolationPoints.getEntry(nfm, j3)), upperBound[j3]));
                if (this.interpolationPoints.getEntry(nfm, j3) == this.lowerDifference.getEntry(j3)) {
                    this.currentBest.setEntry(j3, lowerBound[j3]);
                }
                if (this.interpolationPoints.getEntry(nfm, j3) == this.upperDifference.getEntry(j3)) {
                    this.currentBest.setEntry(j3, upperBound[j3]);
                }
                j3++;
                ipt = ipt6;
                jpt = jpt5;
                stepb = stepb4;
                nfx = nfx2;
            }
            double stepb5 = stepb;
            int ipt7 = ipt;
            int jpt6 = jpt;
            int nfx3 = nfx;
            double objectiveValue = computeObjectiveValue(this.currentBest.toArray());
            double f = this.isMinimize ? objectiveValue : -objectiveValue;
            int numEval = getEvaluations();
            this.fAtInterpolationPoints.setEntry(nfm, f);
            if (numEval == 1) {
                fbeg = f;
                this.trustRegionCenterInterpolationPointIndex = 0;
            } else {
                if (f < this.fAtInterpolationPoints.getEntry(this.trustRegionCenterInterpolationPointIndex)) {
                    this.trustRegionCenterInterpolationPointIndex = nfm;
                }
                fbeg = fbeg4;
            }
            if (numEval > (n2 * 2) + 1) {
                double d5 = objectiveValue;
                double f2 = f;
                int i4 = numEval;
                fbeg2 = fbeg;
                int nfxm2 = nfxm;
                double objectiveValue2 = stepb5;
                double stepa4 = stepa;
                recip = recip5;
                this.zMatrix.setEntry(0, nfxm2, recip);
                this.zMatrix.setEntry(nfm, nfxm2, recip);
                double d6 = stepa4;
                int ipt8 = ipt7;
                this.zMatrix.setEntry(ipt8, nfxm2, -recip);
                jpt2 = jpt6;
                this.zMatrix.setEntry(jpt2, nfxm2, -recip);
                ipt2 = ipt8;
                n = n2;
                this.modelSecondDerivativesValues.setEntry(((((ipt8 - 1) * ipt8) / 2) + jpt2) - 1, (((fbeg2 - this.fAtInterpolationPoints.getEntry(ipt8)) - this.fAtInterpolationPoints.getEntry(jpt2)) + f2) / (this.interpolationPoints.getEntry(nfm, ipt8 - 1) * this.interpolationPoints.getEntry(nfm, jpt2 - 1)));
            } else if (numEval < 2 || numEval > n2 + 1) {
                double d7 = objectiveValue;
                double stepa5 = stepa;
                if (numEval >= n2 + 2) {
                    int ih = (((nfx3 + 1) * nfx3) / 2) - 1;
                    double tmp3 = (f - fbeg) / stepb5;
                    double diff = stepb5 - stepa5;
                    int i5 = numEval;
                    int nfxm3 = nfxm;
                    fbeg2 = fbeg;
                    this.modelSecondDerivativesValues.setEntry(ih, ((tmp3 - this.gradientAtTrustRegionCenter.getEntry(nfxm3)) * TWO) / diff);
                    this.gradientAtTrustRegionCenter.setEntry(nfxm3, ((this.gradientAtTrustRegionCenter.getEntry(nfxm3) * stepb5) - (tmp3 * stepa5)) / diff);
                    if (stepa5 * stepb5 >= 0.0d || f >= this.fAtInterpolationPoints.getEntry(nfm - n2)) {
                        tmp = stepb5;
                        stepa2 = stepa5;
                    } else {
                        this.fAtInterpolationPoints.setEntry(nfm, this.fAtInterpolationPoints.getEntry(nfm - n2));
                        this.fAtInterpolationPoints.setEntry(nfm - n2, f);
                        if (this.trustRegionCenterInterpolationPointIndex == nfm) {
                            this.trustRegionCenterInterpolationPointIndex = nfm - n2;
                        }
                        double d8 = tmp3;
                        tmp = stepb5;
                        this.interpolationPoints.setEntry(nfm - n2, nfxm3, tmp);
                        stepa2 = stepa5;
                        this.interpolationPoints.setEntry(nfm, nfxm3, stepa2);
                    }
                    double d9 = diff;
                    int i6 = ih;
                    this.bMatrix.setEntry(0, nfxm3, (-(stepa2 + tmp)) / (stepa2 * tmp));
                    this.bMatrix.setEntry(nfm, nfxm3, -0.5d / this.interpolationPoints.getEntry(nfm - n2, nfxm3));
                    double d10 = f;
                    this.bMatrix.setEntry(nfm - n2, nfxm3, (-this.bMatrix.getEntry(0, nfxm3)) - this.bMatrix.getEntry(nfm, nfxm3));
                    this.zMatrix.setEntry(0, nfxm3, FastMath.sqrt(TWO) / (stepa2 * tmp));
                    this.zMatrix.setEntry(nfm, nfxm3, FastMath.sqrt(HALF) / rhosq);
                    this.zMatrix.setEntry(nfm - n2, nfxm3, (-this.zMatrix.getEntry(0, nfxm3)) - this.zMatrix.getEntry(nfm, nfxm3));
                    n = n2;
                    recip = recip5;
                    ipt2 = ipt7;
                    jpt2 = jpt6;
                } else {
                    fbeg2 = fbeg;
                    n = n2;
                    recip = recip5;
                    ipt2 = ipt7;
                    jpt2 = jpt6;
                }
            } else {
                this.gradientAtTrustRegionCenter.setEntry(nfmm, (f - fbeg) / stepa);
                if (npt < numEval + n2) {
                    double d11 = objectiveValue;
                    double oneOverStepA = ONE / stepa;
                    double d12 = stepa;
                    this.bMatrix.setEntry(0, nfmm, -oneOverStepA);
                    this.bMatrix.setEntry(nfm, nfmm, oneOverStepA);
                    this.bMatrix.setEntry(npt + nfmm, nfmm, rhosq * -0.5d);
                }
                n = n2;
                fbeg2 = fbeg;
                recip = recip5;
                ipt2 = ipt7;
                jpt2 = jpt6;
            }
            if (getEvaluations() < npt) {
                recip5 = recip;
                jpt4 = jpt2;
                ndim = ndim2;
                np2 = np;
                rhosq2 = rhosq;
                fbeg3 = fbeg2;
                n2 = n;
                ipt3 = ipt2;
            } else {
                return;
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:104:0x03c9  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x00ad  */
    /* JADX WARNING: Removed duplicated region for block: B:228:0x0800  */
    /* JADX WARNING: Removed duplicated region for block: B:231:0x0815  */
    /* JADX WARNING: Removed duplicated region for block: B:243:0x08a6  */
    /* JADX WARNING: Removed duplicated region for block: B:253:0x08e0  */
    /* JADX WARNING: Removed duplicated region for block: B:255:0x08f1  */
    /* JADX WARNING: Removed duplicated region for block: B:265:0x00af A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:269:0x03d3 A[EDGE_INSN: B:269:0x03d3->B:105:0x03d3 ?: BREAK  , SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private double[] trsbox(double r127, org.apache.commons.math3.linear.ArrayRealVector r129, org.apache.commons.math3.linear.ArrayRealVector r130, org.apache.commons.math3.linear.ArrayRealVector r131, org.apache.commons.math3.linear.ArrayRealVector r132, org.apache.commons.math3.linear.ArrayRealVector r133) {
        /*
            r126 = this;
            r0 = r126
            r1 = r129
            r2 = r130
            r3 = r131
            r4 = r132
            r5 = r133
            printMethod()
            org.apache.commons.math3.linear.ArrayRealVector r6 = r0.currentBest
            int r6 = r6.getDimension()
            int r7 = r0.numberOfInterpolationPoints
            r8 = 9221120237041090560(0x7ff8000000000000, double:NaN)
            r10 = 9221120237041090560(0x7ff8000000000000, double:NaN)
            r12 = 0
            r14 = -1
            r15 = 0
            r16 = 0
            r18 = 0
            r20 = 0
            r22 = 0
            r24 = 0
            r26 = 0
            r28 = 0
            r30 = 0
            r32 = 0
            r34 = 0
            r36 = 0
            r38 = 0
            r40 = 0
            r42 = 0
            r44 = 0
            r46 = 0
            r48 = 0
            r50 = 0
            r51 = 0
            r53 = 0
            r55 = 0
            r57 = 0
            r59 = 0
            r60 = 0
            r15 = 0
            r61 = 0
            r62 = r15
            r15 = 0
        L_0x0055:
            r63 = r8
            r8 = 0
            if (r15 >= r6) goto L_0x00c4
            r2.setEntry(r15, r8)
            org.apache.commons.math3.linear.ArrayRealVector r8 = r0.trustRegionCenterOffset
            double r8 = r8.getEntry(r15)
            r71 = r10
            org.apache.commons.math3.linear.ArrayRealVector r10 = r0.lowerDifference
            double r10 = r10.getEntry(r15)
            int r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r8 > 0) goto L_0x0084
            org.apache.commons.math3.linear.ArrayRealVector r8 = r0.gradientAtTrustRegionCenter
            double r8 = r8.getEntry(r15)
            r10 = 0
            int r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r8 < 0) goto L_0x0081
            r8 = -4616189618054758400(0xbff0000000000000, double:-1.0)
            r2.setEntry(r15, r8)
        L_0x0081:
            r10 = 0
            goto L_0x00a5
        L_0x0084:
            org.apache.commons.math3.linear.ArrayRealVector r8 = r0.trustRegionCenterOffset
            double r8 = r8.getEntry(r15)
            org.apache.commons.math3.linear.ArrayRealVector r10 = r0.upperDifference
            double r10 = r10.getEntry(r15)
            int r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r8 < 0) goto L_0x0081
            org.apache.commons.math3.linear.ArrayRealVector r8 = r0.gradientAtTrustRegionCenter
            double r8 = r8.getEntry(r15)
            r10 = 0
            int r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r8 > 0) goto L_0x00a5
            r8 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            r2.setEntry(r15, r8)
        L_0x00a5:
            double r8 = r2.getEntry(r15)
            int r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r8 == 0) goto L_0x00af
            int r62 = r62 + 1
        L_0x00af:
            org.apache.commons.math3.linear.ArrayRealVector r8 = r0.trialStepPoint
            r8.setEntry(r15, r10)
            org.apache.commons.math3.linear.ArrayRealVector r8 = r0.gradientAtTrustRegionCenter
            double r8 = r8.getEntry(r15)
            r1.setEntry(r15, r8)
            int r15 = r15 + 1
            r8 = r63
            r10 = r71
            goto L_0x0055
        L_0x00c4:
            r71 = r10
            double r10 = r127 * r127
            r32 = 0
            r71 = -4616189618054758400(0xbff0000000000000, double:-1.0)
            r15 = 20
            r74 = r20
            r73 = r59
            r8 = r71
            r20 = r18
            r58 = r57
            r18 = r16
            r56 = r55
            r16 = r12
            r13 = r50
            r54 = r53
            r12 = r60
            r52 = r51
            r50 = r48
            r48 = r46
            r46 = r44
            r44 = r42
            r42 = r40
            r40 = r38
            r38 = r36
            r36 = r34
            r34 = r10
            r11 = r62
            r10 = 20
        L_0x00fc:
            r76 = r11
            r11 = 30
            if (r10 == r15) goto L_0x087e
            if (r10 == r11) goto L_0x0866
            r15 = 90
            r11 = 50
            r71 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            if (r10 == r11) goto L_0x05f2
            if (r10 == r15) goto L_0x05dc
            r11 = 100
            if (r10 == r11) goto L_0x05c8
            r11 = 120(0x78, float:1.68E-43)
            if (r10 == r11) goto L_0x045f
            r11 = 190(0xbe, float:2.66E-43)
            r15 = 150(0x96, float:2.1E-43)
            r78 = r14
            r14 = 1
            if (r10 == r15) goto L_0x0202
            if (r10 == r11) goto L_0x01f1
            r11 = 210(0xd2, float:2.94E-43)
            if (r10 == r11) goto L_0x0133
            org.apache.commons.math3.exception.MathIllegalStateException r11 = new org.apache.commons.math3.exception.MathIllegalStateException
            org.apache.commons.math3.exception.util.LocalizedFormats r15 = org.apache.commons.math3.exception.util.LocalizedFormats.SIMPLE_MESSAGE
            java.lang.Object[] r14 = new java.lang.Object[r14]
            java.lang.String r60 = "trsbox"
            r14[r61] = r60
            r11.<init>(r15, r14)
            throw r11
        L_0x0133:
            printState(r11)
            r11 = 0
            r14 = r11
            r11 = 0
        L_0x0139:
            if (r11 >= r6) goto L_0x018d
            r79 = r14
            r14 = 0
            r4.setEntry(r11, r14)
            r15 = r79
            r14 = 0
        L_0x0145:
            if (r14 > r11) goto L_0x0183
            if (r14 >= r11) goto L_0x0161
            double r71 = r4.getEntry(r11)
            r80 = r10
            org.apache.commons.math3.linear.ArrayRealVector r10 = r0.modelSecondDerivativesValues
            double r81 = r10.getEntry(r15)
            double r83 = r3.getEntry(r14)
            double r81 = r81 * r83
            double r1 = r71 + r81
            r4.setEntry(r11, r1)
            goto L_0x0163
        L_0x0161:
            r80 = r10
        L_0x0163:
            double r1 = r4.getEntry(r14)
            org.apache.commons.math3.linear.ArrayRealVector r10 = r0.modelSecondDerivativesValues
            double r71 = r10.getEntry(r15)
            double r81 = r3.getEntry(r11)
            double r71 = r71 * r81
            double r1 = r1 + r71
            r4.setEntry(r14, r1)
            int r15 = r15 + 1
            int r14 = r14 + 1
            r10 = r80
            r1 = r129
            r2 = r130
            goto L_0x0145
        L_0x0183:
            r80 = r10
            int r11 = r11 + 1
            r14 = r15
            r1 = r129
            r2 = r130
            goto L_0x0139
        L_0x018d:
            r80 = r10
            r79 = r14
            org.apache.commons.math3.linear.Array2DRowRealMatrix r1 = r0.interpolationPoints
            org.apache.commons.math3.linear.RealVector r1 = r1.operate(r3)
            org.apache.commons.math3.linear.ArrayRealVector r2 = r0.modelSecondDerivativesParameters
            org.apache.commons.math3.linear.RealVector r1 = r1.ebeMultiply(r2)
            r2 = 0
        L_0x019e:
            if (r2 >= r7) goto L_0x01ca
            org.apache.commons.math3.linear.ArrayRealVector r10 = r0.modelSecondDerivativesParameters
            double r10 = r10.getEntry(r2)
            r14 = 0
            int r10 = (r10 > r14 ? 1 : (r10 == r14 ? 0 : -1))
            if (r10 == 0) goto L_0x01c7
            r10 = 0
        L_0x01ad:
            if (r10 >= r6) goto L_0x01c7
            double r14 = r4.getEntry(r10)
            double r71 = r1.getEntry(r2)
            org.apache.commons.math3.linear.Array2DRowRealMatrix r11 = r0.interpolationPoints
            double r81 = r11.getEntry(r2, r10)
            double r71 = r71 * r81
            double r14 = r14 + r71
            r4.setEntry(r10, r14)
            int r10 = r10 + 1
            goto L_0x01ad
        L_0x01c7:
            int r2 = r2 + 1
            goto L_0x019e
        L_0x01ca:
            r10 = 0
            int r2 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r2 == 0) goto L_0x01dc
            r10 = 50
        L_0x01d2:
            r11 = r76
            r14 = r78
            r1 = r129
            r2 = r130
            goto L_0x0496
        L_0x01dc:
            if (r12 <= r13) goto L_0x01e1
            r10 = 150(0x96, float:2.1E-43)
            goto L_0x01d2
        L_0x01e1:
            r2 = 0
        L_0x01e2:
            if (r2 >= r6) goto L_0x01ee
            double r10 = r4.getEntry(r2)
            r5.setEntry(r2, r10)
            int r2 = r2 + 1
            goto L_0x01e2
        L_0x01ee:
            r10 = 120(0x78, float:1.68E-43)
            goto L_0x01d2
        L_0x01f1:
            r80 = r10
            r94 = r7
            r95 = r8
            r93 = r12
            r97 = r13
            r8 = r74
            r13 = r78
            r7 = r1
            goto L_0x03d3
        L_0x0202:
            r80 = r10
            printState(r15)
            r1 = 0
            r81 = 0
            r83 = 0
            r87 = r83
            r83 = r81
            r81 = r1
            r1 = 0
        L_0x0214:
            if (r1 >= r6) goto L_0x024d
            r2 = r130
            double r89 = r2.getEntry(r1)
            r69 = 0
            int r10 = (r89 > r69 ? 1 : (r89 == r69 ? 0 : -1))
            if (r10 != 0) goto L_0x024a
            double r89 = r3.getEntry(r1)
            double r91 = r4.getEntry(r1)
            double r89 = r89 * r91
            double r81 = r81 + r89
            org.apache.commons.math3.linear.ArrayRealVector r10 = r0.trialStepPoint
            double r89 = r10.getEntry(r1)
            double r91 = r4.getEntry(r1)
            double r89 = r89 * r91
            double r83 = r83 + r89
            org.apache.commons.math3.linear.ArrayRealVector r10 = r0.trialStepPoint
            double r89 = r10.getEntry(r1)
            double r91 = r5.getEntry(r1)
            double r89 = r89 * r91
            double r87 = r87 + r89
        L_0x024a:
            int r1 = r1 + 1
            goto L_0x0214
        L_0x024d:
            r2 = r130
            r42 = 0
            r1 = -1
            r46 = 0
            r89 = 4625478292286210048(0x4031000000000000, double:17.0)
            double r89 = r89 * r24
            r91 = 4614162998222441677(0x4008cccccccccccd, double:3.1)
            double r14 = r89 + r91
            int r10 = (int) r14
            r14 = r1
            r1 = 0
        L_0x0262:
            if (r1 >= r10) goto L_0x02ad
            r93 = r12
            double r11 = (double) r1
            double r11 = r11 * r24
            r94 = r7
            r95 = r8
            double r7 = (double) r10
            double r18 = r11 / r7
            double r7 = r18 + r18
            double r11 = r18 * r18
            r67 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r11 = r11 + r67
            double r7 = r7 / r11
            double r11 = r18 * r87
            double r11 = r11 - r83
            double r11 = r11 - r83
            double r11 = r11 * r18
            double r20 = r81 + r11
            double r11 = r18 * r26
            double r11 = r11 - r28
            double r89 = r7 * r71
            double r89 = r89 * r20
            double r11 = r11 - r89
            double r50 = r7 * r11
            int r9 = (r50 > r42 ? 1 : (r50 == r42 ? 0 : -1))
            if (r9 <= 0) goto L_0x029a
            r42 = r50
            r9 = r1
            r52 = r46
            r14 = r9
            goto L_0x02a0
        L_0x029a:
            int r9 = r14 + 1
            if (r1 != r9) goto L_0x02a0
            r54 = r50
        L_0x02a0:
            r46 = r50
            int r1 = r1 + 1
            r12 = r93
            r7 = r94
            r8 = r95
            r11 = 190(0xbe, float:2.66E-43)
            goto L_0x0262
        L_0x02ad:
            r94 = r7
            r95 = r8
            r93 = r12
            if (r14 >= 0) goto L_0x02c6
            r1 = 190(0xbe, float:2.66E-43)
        L_0x02b7:
            r10 = r1
            r11 = r76
            r14 = r78
            r12 = r93
            r7 = r94
            r8 = r95
            r1 = r129
            goto L_0x0496
        L_0x02c6:
            if (r14 >= r10) goto L_0x02dc
            double r7 = r54 - r52
            double r11 = r42 + r42
            double r11 = r11 - r52
            double r11 = r11 - r54
            double r20 = r7 / r11
            double r7 = (double) r14
            double r11 = r20 * r71
            double r7 = r7 + r11
            double r7 = r7 * r24
            double r11 = (double) r10
            double r7 = r7 / r11
            r18 = r7
        L_0x02dc:
            double r7 = r18 * r18
            r1 = 0
            r11 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r8 = r11 - r7
            double r67 = r18 * r18
            double r67 = r67 + r11
            double r8 = r8 / r67
            double r89 = r18 + r18
            double r67 = r18 * r18
            double r91 = r67 + r11
            double r89 = r89 / r91
            double r11 = r18 * r87
            double r11 = r11 - r83
            double r11 = r11 - r83
            double r11 = r11 * r18
            double r20 = r81 + r11
            double r11 = r18 * r26
            double r11 = r11 - r28
            double r71 = r71 * r89
            double r71 = r71 * r20
            double r11 = r11 - r71
            double r11 = r11 * r89
            r69 = 0
            int r1 = (r11 > r69 ? 1 : (r11 == r69 ? 0 : -1))
            if (r1 > 0) goto L_0x0310
            r1 = 190(0xbe, float:2.66E-43)
            goto L_0x02b7
        L_0x0310:
            r26 = 0
            r48 = 0
            r1 = 0
        L_0x0315:
            if (r1 >= r6) goto L_0x0385
            r7 = r129
            double r71 = r7.getEntry(r1)
            r67 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r85 = r8 - r67
            double r91 = r5.getEntry(r1)
            double r85 = r85 * r91
            double r71 = r71 + r85
            double r85 = r4.getEntry(r1)
            double r85 = r85 * r89
            r97 = r13
            r98 = r14
            double r13 = r71 + r85
            r7.setEntry(r1, r13)
            double r13 = r2.getEntry(r1)
            r69 = 0
            int r13 = (r13 > r69 ? 1 : (r13 == r69 ? 0 : -1))
            if (r13 != 0) goto L_0x036d
            org.apache.commons.math3.linear.ArrayRealVector r13 = r0.trialStepPoint
            org.apache.commons.math3.linear.ArrayRealVector r14 = r0.trialStepPoint
            double r14 = r14.getEntry(r1)
            double r14 = r14 * r8
            double r71 = r3.getEntry(r1)
            double r71 = r71 * r89
            double r14 = r14 + r71
            r13.setEntry(r1, r14)
            org.apache.commons.math3.linear.ArrayRealVector r13 = r0.trialStepPoint
            double r13 = r13.getEntry(r1)
            double r71 = r7.getEntry(r1)
            double r13 = r13 * r71
            double r26 = r26 + r13
            double r13 = r7.getEntry(r1)
            double r71 = r13 * r13
            double r48 = r48 + r71
        L_0x036d:
            double r13 = r5.getEntry(r1)
            double r13 = r13 * r8
            double r71 = r4.getEntry(r1)
            double r71 = r71 * r89
            double r13 = r13 + r71
            r5.setEntry(r1, r13)
            int r1 = r1 + 1
            r13 = r97
            r14 = r98
            goto L_0x0315
        L_0x0385:
            r97 = r13
            r98 = r14
            r7 = r129
            r1 = 0
            double r32 = r32 + r11
            if (r78 < 0) goto L_0x03b6
            r14 = r98
            if (r14 != r10) goto L_0x03af
            int r1 = r76 + 1
            r99 = r8
            r8 = r74
            r13 = r78
            r2.setEntry(r13, r8)
            r15 = 100
            r11 = r1
            r1 = r7
            r14 = r13
            r10 = r15
        L_0x03a5:
            r12 = r93
            r7 = r94
            r8 = r95
            r13 = r97
            goto L_0x0496
        L_0x03af:
            r99 = r8
            r8 = r74
            r13 = r78
            goto L_0x03be
        L_0x03b6:
            r99 = r8
            r8 = r74
            r13 = r78
            r14 = r98
        L_0x03be:
            r71 = 4576918229304087675(0x3f847ae147ae147b, double:0.01)
            double r71 = r71 * r32
            int r1 = (r11 > r71 ? 1 : (r11 == r71 ? 0 : -1))
            if (r1 <= 0) goto L_0x03d3
            r1 = 120(0x78, float:1.68E-43)
            r10 = r1
            r1 = r7
            r74 = r8
            r14 = r13
            r11 = r76
            goto L_0x03a5
        L_0x03d3:
            r1 = 190(0xbe, float:2.66E-43)
            printState(r1)
            r10 = 0
            r1 = 0
        L_0x03db:
            if (r1 >= r6) goto L_0x0454
            org.apache.commons.math3.linear.ArrayRealVector r12 = r0.trustRegionCenterOffset
            double r14 = r12.getEntry(r1)
            org.apache.commons.math3.linear.ArrayRealVector r12 = r0.trialStepPoint
            double r62 = r12.getEntry(r1)
            double r14 = r14 + r62
            org.apache.commons.math3.linear.ArrayRealVector r12 = r0.upperDifference
            r101 = r8
            double r8 = r12.getEntry(r1)
            double r8 = org.apache.commons.math3.util.FastMath.min(r14, r8)
            org.apache.commons.math3.linear.ArrayRealVector r12 = r0.newPoint
            org.apache.commons.math3.linear.ArrayRealVector r14 = r0.lowerDifference
            double r14 = r14.getEntry(r1)
            double r14 = org.apache.commons.math3.util.FastMath.max(r8, r14)
            r12.setEntry(r1, r14)
            double r14 = r2.getEntry(r1)
            r62 = -4616189618054758400(0xbff0000000000000, double:-1.0)
            int r12 = (r14 > r62 ? 1 : (r14 == r62 ? 0 : -1))
            if (r12 != 0) goto L_0x041b
            org.apache.commons.math3.linear.ArrayRealVector r12 = r0.newPoint
            org.apache.commons.math3.linear.ArrayRealVector r14 = r0.lowerDifference
            double r14 = r14.getEntry(r1)
            r12.setEntry(r1, r14)
        L_0x041b:
            double r14 = r2.getEntry(r1)
            r62 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            int r12 = (r14 > r62 ? 1 : (r14 == r62 ? 0 : -1))
            if (r12 != 0) goto L_0x0430
            org.apache.commons.math3.linear.ArrayRealVector r12 = r0.newPoint
            org.apache.commons.math3.linear.ArrayRealVector r14 = r0.upperDifference
            double r14 = r14.getEntry(r1)
            r12.setEntry(r1, r14)
        L_0x0430:
            org.apache.commons.math3.linear.ArrayRealVector r12 = r0.trialStepPoint
            org.apache.commons.math3.linear.ArrayRealVector r14 = r0.newPoint
            double r14 = r14.getEntry(r1)
            org.apache.commons.math3.linear.ArrayRealVector r5 = r0.trustRegionCenterOffset
            double r62 = r5.getEntry(r1)
            double r14 = r14 - r62
            r12.setEntry(r1, r14)
            org.apache.commons.math3.linear.ArrayRealVector r5 = r0.trialStepPoint
            double r14 = r5.getEntry(r1)
            double r62 = r14 * r14
            double r10 = r10 + r62
            int r1 = r1 + 1
            r8 = r101
            r5 = r133
            goto L_0x03db
        L_0x0454:
            r101 = r8
            r1 = 2
            double[] r1 = new double[r1]
            r1[r61] = r10
            r5 = 1
            r1[r5] = r95
            return r1
        L_0x045f:
            r94 = r7
            r95 = r8
            r80 = r10
            r93 = r12
            r97 = r13
            r13 = r14
            r101 = r74
            r7 = r1
            printState(r11)
            int r12 = r93 + 1
            double r8 = r48 * r44
            double r10 = r26 * r26
            double r8 = r8 - r10
            r10 = 4547007122018943789(0x3f1a36e2eb1c432d, double:1.0E-4)
            double r10 = r10 * r32
            double r10 = r10 * r32
            int r1 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r1 > 0) goto L_0x049a
            r10 = 190(0xbe, float:2.66E-43)
            r1 = r7
            r20 = r8
            r14 = r13
            r11 = r76
        L_0x048c:
            r7 = r94
            r8 = r95
            r13 = r97
            r74 = r101
        L_0x0494:
            r5 = r133
        L_0x0496:
            r15 = 20
            goto L_0x00fc
        L_0x049a:
            double r8 = org.apache.commons.math3.util.FastMath.sqrt(r8)
            r1 = 0
        L_0x049f:
            if (r1 >= r6) goto L_0x04c7
            double r10 = r2.getEntry(r1)
            r14 = 0
            int r5 = (r10 > r14 ? 1 : (r10 == r14 ? 0 : -1))
            if (r5 != 0) goto L_0x04bf
            org.apache.commons.math3.linear.ArrayRealVector r5 = r0.trialStepPoint
            double r10 = r5.getEntry(r1)
            double r10 = r10 * r26
            double r14 = r7.getEntry(r1)
            double r14 = r14 * r44
            double r10 = r10 - r14
            double r10 = r10 / r8
            r3.setEntry(r1, r10)
            goto L_0x04c4
        L_0x04bf:
            r10 = 0
            r3.setEntry(r1, r10)
        L_0x04c4:
            int r1 = r1 + 1
            goto L_0x049f
        L_0x04c7:
            double r10 = -r8
            r14 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            r1 = -1
            r20 = r8
            r24 = r14
            r74 = r101
            r14 = r1
            r1 = 0
        L_0x04d3:
            if (r1 >= r6) goto L_0x05b5
            double r8 = r2.getEntry(r1)
            r28 = 0
            int r5 = (r8 > r28 ? 1 : (r8 == r28 ? 0 : -1))
            if (r5 != 0) goto L_0x05ad
            org.apache.commons.math3.linear.ArrayRealVector r5 = r0.trustRegionCenterOffset
            double r8 = r5.getEntry(r1)
            org.apache.commons.math3.linear.ArrayRealVector r5 = r0.trialStepPoint
            double r28 = r5.getEntry(r1)
            double r8 = r8 + r28
            org.apache.commons.math3.linear.ArrayRealVector r5 = r0.lowerDifference
            double r28 = r5.getEntry(r1)
            double r8 = r8 - r28
            org.apache.commons.math3.linear.ArrayRealVector r5 = r0.upperDifference
            double r28 = r5.getEntry(r1)
            org.apache.commons.math3.linear.ArrayRealVector r5 = r0.trustRegionCenterOffset
            double r38 = r5.getEntry(r1)
            double r28 = r28 - r38
            org.apache.commons.math3.linear.ArrayRealVector r5 = r0.trialStepPoint
            double r38 = r5.getEntry(r1)
            double r28 = r28 - r38
            r38 = 0
            int r5 = (r8 > r38 ? 1 : (r8 == r38 ? 0 : -1))
            if (r5 > 0) goto L_0x0523
            int r5 = r76 + 1
            r103 = r10
            r10 = -4616189618054758400(0xbff0000000000000, double:-1.0)
            r2.setEntry(r1, r10)
            r10 = 100
            r11 = r5
            r38 = r8
            r40 = r28
            goto L_0x05bb
        L_0x0523:
            r103 = r10
            int r5 = (r28 > r38 ? 1 : (r28 == r38 ? 0 : -1))
            if (r5 > 0) goto L_0x053c
            int r11 = r76 + 1
            r105 = r11
            r10 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            r2.setEntry(r1, r10)
            r10 = 100
            r38 = r8
            r40 = r28
            r11 = r105
            goto L_0x05bb
        L_0x053c:
            org.apache.commons.math3.linear.ArrayRealVector r5 = r0.trialStepPoint
            double r10 = r5.getEntry(r1)
            double r38 = r3.getEntry(r1)
            double r40 = r10 * r10
            double r71 = r38 * r38
            double r40 = r40 + r71
            org.apache.commons.math3.linear.ArrayRealVector r5 = r0.trustRegionCenterOffset
            double r71 = r5.getEntry(r1)
            org.apache.commons.math3.linear.ArrayRealVector r5 = r0.lowerDifference
            double r77 = r5.getEntry(r1)
            double r71 = r71 - r77
            double r10 = r71 * r71
            double r10 = r40 - r10
            r20 = 0
            int r5 = (r10 > r20 ? 1 : (r10 == r20 ? 0 : -1))
            if (r5 <= 0) goto L_0x0579
            double r20 = org.apache.commons.math3.util.FastMath.sqrt(r10)
            double r77 = r3.getEntry(r1)
            double r10 = r20 - r77
            double r20 = r24 * r10
            int r5 = (r20 > r8 ? 1 : (r20 == r8 ? 0 : -1))
            if (r5 <= 0) goto L_0x0579
            double r24 = r8 / r10
            r14 = r1
            r74 = -4616189618054758400(0xbff0000000000000, double:-1.0)
        L_0x0579:
            org.apache.commons.math3.linear.ArrayRealVector r5 = r0.upperDifference
            double r20 = r5.getEntry(r1)
            org.apache.commons.math3.linear.ArrayRealVector r5 = r0.trustRegionCenterOffset
            double r77 = r5.getEntry(r1)
            double r20 = r20 - r77
            double r71 = r20 * r20
            double r10 = r40 - r71
            r69 = 0
            int r5 = (r10 > r69 ? 1 : (r10 == r69 ? 0 : -1))
            if (r5 <= 0) goto L_0x05a6
            double r71 = org.apache.commons.math3.util.FastMath.sqrt(r10)
            double r77 = r3.getEntry(r1)
            double r10 = r71 + r77
            double r71 = r24 * r10
            int r5 = (r71 > r28 ? 1 : (r71 == r28 ? 0 : -1))
            if (r5 <= 0) goto L_0x05a6
            double r24 = r28 / r10
            r14 = r1
            r74 = 4607182418800017408(0x3ff0000000000000, double:1.0)
        L_0x05a6:
            r38 = r8
            r20 = r10
            r40 = r28
            goto L_0x05af
        L_0x05ad:
            r103 = r10
        L_0x05af:
            int r1 = r1 + 1
            r10 = r103
            goto L_0x04d3
        L_0x05b5:
            r103 = r10
            r11 = r76
            r10 = r80
        L_0x05bb:
            r10 = 210(0xd2, float:2.94E-43)
            r1 = r7
            r7 = r94
            r8 = r95
            r13 = r97
            r28 = r103
            goto L_0x0494
        L_0x05c8:
            r94 = r7
            r95 = r8
            r80 = r10
            r93 = r12
            r97 = r13
            r13 = r14
            r101 = r74
            r7 = r1
            r5 = r73
            r11 = -4616189618054758400(0xbff0000000000000, double:-1.0)
            goto L_0x07f5
        L_0x05dc:
            r94 = r7
            r95 = r8
            r80 = r10
            r93 = r12
            r97 = r13
            r13 = r14
            r101 = r74
            r7 = r1
            r5 = r73
            r121 = r95
            r11 = -4616189618054758400(0xbff0000000000000, double:-1.0)
            goto L_0x07ef
        L_0x05f2:
            r94 = r7
            r95 = r8
            r80 = r10
            r93 = r12
            r97 = r13
            r13 = r14
            r101 = r74
            r7 = r1
            printState(r11)
            r8 = r34
            r10 = 0
            r30 = 0
            r1 = 0
            r124 = r8
            r8 = r30
            r30 = r124
        L_0x0610:
            if (r1 >= r6) goto L_0x0643
            double r74 = r2.getEntry(r1)
            r69 = 0
            int r5 = (r74 > r69 ? 1 : (r74 == r69 ? 0 : -1))
            if (r5 != 0) goto L_0x0640
            org.apache.commons.math3.linear.ArrayRealVector r5 = r0.trialStepPoint
            double r74 = r5.getEntry(r1)
            double r78 = r74 * r74
            double r30 = r30 - r78
            double r78 = r3.getEntry(r1)
            org.apache.commons.math3.linear.ArrayRealVector r5 = r0.trialStepPoint
            double r81 = r5.getEntry(r1)
            double r78 = r78 * r81
            double r10 = r10 + r78
            double r78 = r3.getEntry(r1)
            double r81 = r4.getEntry(r1)
            double r78 = r78 * r81
            double r8 = r8 + r78
        L_0x0640:
            int r1 = r1 + 1
            goto L_0x0610
        L_0x0643:
            r69 = 0
            int r1 = (r30 > r69 ? 1 : (r30 == r69 ? 0 : -1))
            if (r1 > 0) goto L_0x0654
            r1 = 90
            r10 = r1
            r1 = r7
            r14 = r13
            r11 = r76
            r12 = r93
            goto L_0x048c
        L_0x0654:
            double r74 = r58 * r30
            double r78 = r10 * r10
            r1 = 0
            r106 = r13
            double r12 = r74 + r78
            double r12 = org.apache.commons.math3.util.FastMath.sqrt(r12)
            r20 = 0
            int r1 = (r10 > r20 ? 1 : (r10 == r20 ? 0 : -1))
            if (r1 >= 0) goto L_0x066c
            double r20 = r12 - r10
            double r20 = r20 / r58
            goto L_0x0671
        L_0x066c:
            r1 = 0
            double r20 = r12 + r10
            double r20 = r30 / r20
        L_0x0671:
            r107 = r20
            r56 = 0
            int r1 = (r8 > r56 ? 1 : (r8 == r56 ? 0 : -1))
            if (r1 <= 0) goto L_0x0687
            r109 = r10
            double r10 = r48 / r8
            r111 = r12
            r12 = r107
            double r20 = org.apache.commons.math3.util.FastMath.min(r12, r10)
            goto L_0x068d
        L_0x0687:
            r109 = r10
            r111 = r12
            r12 = r107
        L_0x068d:
            r1 = -1
            r14 = r1
            r56 = r20
            r1 = 0
        L_0x0692:
            if (r1 >= r6) goto L_0x06de
            double r10 = r3.getEntry(r1)
            r20 = 0
            int r5 = (r10 > r20 ? 1 : (r10 == r20 ? 0 : -1))
            if (r5 == 0) goto L_0x06db
            org.apache.commons.math3.linear.ArrayRealVector r5 = r0.trustRegionCenterOffset
            double r10 = r5.getEntry(r1)
            org.apache.commons.math3.linear.ArrayRealVector r5 = r0.trialStepPoint
            double r69 = r5.getEntry(r1)
            double r22 = r10 + r69
            double r10 = r3.getEntry(r1)
            int r5 = (r10 > r20 ? 1 : (r10 == r20 ? 0 : -1))
            if (r5 <= 0) goto L_0x06c3
            org.apache.commons.math3.linear.ArrayRealVector r5 = r0.upperDifference
            double r10 = r5.getEntry(r1)
            double r10 = r10 - r22
            double r20 = r3.getEntry(r1)
            double r10 = r10 / r20
        L_0x06c2:
            goto L_0x06d2
        L_0x06c3:
            org.apache.commons.math3.linear.ArrayRealVector r5 = r0.lowerDifference
            double r10 = r5.getEntry(r1)
            double r10 = r10 - r22
            double r20 = r3.getEntry(r1)
            double r10 = r10 / r20
            goto L_0x06c2
        L_0x06d2:
            int r5 = (r10 > r56 ? 1 : (r10 == r56 ? 0 : -1))
            if (r5 >= 0) goto L_0x06d9
            r56 = r10
            r14 = r1
        L_0x06d9:
            r111 = r10
        L_0x06db:
            int r1 = r1 + 1
            goto L_0x0692
        L_0x06de:
            r10 = 0
            r20 = 0
            int r1 = (r56 > r20 ? 1 : (r56 == r20 ? 0 : -1))
            if (r1 <= 0) goto L_0x076d
            int r1 = r93 + 1
            r113 = r10
            double r10 = r8 / r58
            r5 = -1
            if (r14 != r5) goto L_0x0703
            int r5 = (r10 > r20 ? 1 : (r10 == r20 ? 0 : -1))
            if (r5 <= 0) goto L_0x0703
            r115 = r12
            r12 = r95
            double r12 = org.apache.commons.math3.util.FastMath.min(r12, r10)
            r20 = -4616189618054758400(0xbff0000000000000, double:-1.0)
            int r5 = (r12 > r20 ? 1 : (r12 == r20 ? 0 : -1))
            if (r5 != 0) goto L_0x0707
            r12 = r10
            goto L_0x0707
        L_0x0703:
            r115 = r12
            r12 = r95
        L_0x0707:
            r20 = r48
            r36 = 0
            r5 = 0
        L_0x070c:
            if (r5 >= r6) goto L_0x074d
            double r48 = r7.getEntry(r5)
            double r74 = r4.getEntry(r5)
            double r74 = r74 * r56
            r117 = r10
            double r10 = r48 + r74
            r7.setEntry(r5, r10)
            double r10 = r2.getEntry(r5)
            r48 = 0
            int r10 = (r10 > r48 ? 1 : (r10 == r48 ? 0 : -1))
            if (r10 != 0) goto L_0x0731
            double r10 = r7.getEntry(r5)
            double r48 = r10 * r10
            double r36 = r36 + r48
        L_0x0731:
            org.apache.commons.math3.linear.ArrayRealVector r10 = r0.trialStepPoint
            org.apache.commons.math3.linear.ArrayRealVector r11 = r0.trialStepPoint
            double r48 = r11.getEntry(r5)
            double r74 = r3.getEntry(r5)
            double r74 = r74 * r56
            r119 = r12
            double r11 = r48 + r74
            r10.setEntry(r5, r11)
            int r5 = r5 + 1
            r10 = r117
            r12 = r119
            goto L_0x070c
        L_0x074d:
            r117 = r10
            r119 = r12
            double r71 = r71 * r56
            double r71 = r71 * r8
            r5 = 0
            double r10 = r20 - r71
            double r10 = r10 * r56
            r12 = 0
            double r48 = org.apache.commons.math3.util.FastMath.max(r10, r12)
            double r32 = r32 + r48
            r113 = r48
            r12 = r119
            r48 = r36
            r36 = r20
            r20 = r117
            goto L_0x0777
        L_0x076d:
            r113 = r10
            r115 = r12
            r12 = r95
            r1 = r93
            r20 = r111
        L_0x0777:
            if (r14 < 0) goto L_0x07b9
            int r11 = r76 + 1
            r123 = r11
            r121 = r12
            r11 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            r2.setEntry(r14, r11)
            double r67 = r3.getEntry(r14)
            r69 = 0
            int r5 = (r67 > r69 ? 1 : (r67 == r69 ? 0 : -1))
            if (r5 >= 0) goto L_0x0794
            r11 = -4616189618054758400(0xbff0000000000000, double:-1.0)
            r2.setEntry(r14, r11)
            goto L_0x0796
        L_0x0794:
            r11 = -4616189618054758400(0xbff0000000000000, double:-1.0)
        L_0x0796:
            org.apache.commons.math3.linear.ArrayRealVector r5 = r0.trialStepPoint
            double r65 = r5.getEntry(r14)
            double r67 = r65 * r65
            double r34 = r34 - r67
            r67 = 0
            int r5 = (r34 > r67 ? 1 : (r34 == r67 ? 0 : -1))
            if (r5 > 0) goto L_0x07b6
            r10 = 190(0xbe, float:2.66E-43)
        L_0x07a8:
            r12 = r1
            r1 = r7
            r7 = r94
            r13 = r97
            r74 = r101
            r8 = r121
            r11 = r123
            goto L_0x0494
        L_0x07b6:
            r10 = 20
            goto L_0x07a8
        L_0x07b9:
            r121 = r12
            r11 = -4616189618054758400(0xbff0000000000000, double:-1.0)
            int r5 = (r56 > r115 ? 1 : (r56 == r115 ? 0 : -1))
            if (r5 >= 0) goto L_0x07ea
            r5 = r73
            if (r1 != r5) goto L_0x07d7
            r10 = 190(0xbe, float:2.66E-43)
        L_0x07c7:
            r12 = r1
            r73 = r5
            r1 = r7
            r11 = r76
            r7 = r94
            r13 = r97
            r74 = r101
            r8 = r121
            goto L_0x0494
        L_0x07d7:
            r65 = 4576918229304087675(0x3f847ae147ae147b, double:0.01)
            double r65 = r65 * r32
            int r10 = (r113 > r65 ? 1 : (r113 == r65 ? 0 : -1))
            if (r10 > 0) goto L_0x07e5
            r10 = 190(0xbe, float:2.66E-43)
            goto L_0x07c7
        L_0x07e5:
            double r16 = r48 / r36
            r10 = 30
            goto L_0x07c7
        L_0x07ea:
            r5 = r73
            r93 = r1
            r13 = r14
        L_0x07ef:
            printState(r15)
            r8 = 0
            r14 = r13
        L_0x07f5:
            r1 = 100
            printState(r1)
            int r1 = r6 + -1
            r10 = r76
            if (r10 < r1) goto L_0x0815
            r1 = 190(0xbe, float:2.66E-43)
            r73 = r5
            r11 = r10
            r12 = r93
            r13 = r97
        L_0x0809:
            r74 = r101
        L_0x080b:
            r5 = r133
            r15 = 20
            r10 = r1
            r1 = r7
            r7 = r94
            goto L_0x00fc
        L_0x0815:
            r44 = 0
            r26 = 0
            r48 = 0
            r1 = 0
        L_0x081c:
            if (r1 >= r6) goto L_0x085c
            double r65 = r2.getEntry(r1)
            r67 = 0
            int r13 = (r65 > r67 ? 1 : (r65 == r67 ? 0 : -1))
            if (r13 != 0) goto L_0x0852
            org.apache.commons.math3.linear.ArrayRealVector r13 = r0.trialStepPoint
            double r65 = r13.getEntry(r1)
            double r67 = r65 * r65
            double r44 = r44 + r67
            org.apache.commons.math3.linear.ArrayRealVector r13 = r0.trialStepPoint
            double r67 = r13.getEntry(r1)
            double r71 = r7.getEntry(r1)
            double r67 = r67 * r71
            double r26 = r26 + r67
            double r65 = r7.getEntry(r1)
            double r67 = r65 * r65
            double r48 = r48 + r67
            org.apache.commons.math3.linear.ArrayRealVector r13 = r0.trialStepPoint
            double r11 = r13.getEntry(r1)
            r3.setEntry(r1, r11)
            goto L_0x0857
        L_0x0852:
            r11 = 0
            r3.setEntry(r1, r11)
        L_0x0857:
            int r1 = r1 + 1
            r11 = -4616189618054758400(0xbff0000000000000, double:-1.0)
            goto L_0x081c
        L_0x085c:
            r13 = r93
            r1 = 210(0xd2, float:2.94E-43)
            r73 = r5
            r11 = r10
            r12 = r93
            goto L_0x0809
        L_0x0866:
            r94 = r7
            r95 = r8
            r80 = r10
            r93 = r12
            r97 = r13
            r106 = r14
            r5 = r73
            r101 = r74
            r10 = r76
            r7 = r1
            r1 = 30
            r8 = 20
            goto L_0x089c
        L_0x087e:
            r94 = r7
            r95 = r8
            r80 = r10
            r93 = r12
            r97 = r13
            r106 = r14
            r5 = r73
            r101 = r74
            r10 = r76
            r7 = r1
            r1 = 30
            r8 = 20
            printState(r8)
            r11 = 0
            r16 = r11
        L_0x089c:
            printState(r1)
            r11 = 0
            r58 = r11
            r1 = 0
        L_0x08a4:
            if (r1 >= r6) goto L_0x08da
            double r11 = r2.getEntry(r1)
            r13 = 0
            int r9 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1))
            if (r9 == 0) goto L_0x08b4
            r3.setEntry(r1, r13)
            goto L_0x08cf
        L_0x08b4:
            int r9 = (r16 > r13 ? 1 : (r16 == r13 ? 0 : -1))
            if (r9 != 0) goto L_0x08c1
            double r11 = r7.getEntry(r1)
            double r11 = -r11
            r3.setEntry(r1, r11)
            goto L_0x08cf
        L_0x08c1:
            double r11 = r3.getEntry(r1)
            double r11 = r11 * r16
            double r13 = r7.getEntry(r1)
            double r11 = r11 - r13
            r3.setEntry(r1, r11)
        L_0x08cf:
            double r11 = r3.getEntry(r1)
            double r13 = r11 * r11
            double r58 = r58 + r13
            int r1 = r1 + 1
            goto L_0x08a4
        L_0x08da:
            r11 = 0
            int r1 = (r58 > r11 ? 1 : (r58 == r11 ? 0 : -1))
            if (r1 != 0) goto L_0x08f1
            r1 = 190(0xbe, float:2.66E-43)
            r73 = r5
        L_0x08e4:
            r11 = r10
            r12 = r93
            r8 = r95
            r13 = r97
            r74 = r101
            r14 = r106
            goto L_0x080b
        L_0x08f1:
            int r1 = (r16 > r11 ? 1 : (r16 == r11 ? 0 : -1))
            if (r1 != 0) goto L_0x08ff
            r13 = r58
            int r1 = r93 + r6
            int r1 = r1 - r10
            r73 = r1
            r48 = r13
            goto L_0x0901
        L_0x08ff:
            r73 = r5
        L_0x0901:
            double r13 = r48 * r34
            r65 = 4547007122018943789(0x3f1a36e2eb1c432d, double:1.0E-4)
            double r65 = r65 * r32
            double r65 = r65 * r32
            int r1 = (r13 > r65 ? 1 : (r13 == r65 ? 0 : -1))
            if (r1 > 0) goto L_0x0913
            r1 = 190(0xbe, float:2.66E-43)
        L_0x0912:
            goto L_0x08e4
        L_0x0913:
            r1 = 210(0xd2, float:2.94E-43)
            goto L_0x0912
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.optimization.direct.BOBYQAOptimizer.trsbox(double, org.apache.commons.math3.linear.ArrayRealVector, org.apache.commons.math3.linear.ArrayRealVector, org.apache.commons.math3.linear.ArrayRealVector, org.apache.commons.math3.linear.ArrayRealVector, org.apache.commons.math3.linear.ArrayRealVector):double[]");
    }

    private void update(double beta, double denom, int knew) {
        int nptm;
        int i = knew;
        printMethod();
        int n = this.currentBest.getDimension();
        int npt = this.numberOfInterpolationPoints;
        int j = 1;
        int nptm2 = (npt - n) - 1;
        ArrayRealVector work = new ArrayRealVector(npt + n);
        int i2 = 0;
        double ztest = 0.0d;
        for (int k = 0; k < npt; k++) {
            for (int j2 = 0; j2 < nptm2; j2++) {
                ztest = FastMath.max(ztest, FastMath.abs(this.zMatrix.getEntry(k, j2)));
            }
        }
        double ztest2 = ztest * 1.0E-20d;
        while (j < nptm2) {
            double d1 = this.zMatrix.getEntry(i, j);
            if (FastMath.abs(d1) > ztest2) {
                double d2 = this.zMatrix.getEntry(i, i2);
                double d3 = this.zMatrix.getEntry(i, j);
                double d = d1;
                double d4 = FastMath.sqrt((d2 * d2) + (d3 * d3));
                nptm = nptm2;
                double d5 = this.zMatrix.getEntry(i, 0) / d4;
                double d6 = this.zMatrix.getEntry(i, j) / d4;
                int i3 = 0;
                while (i3 < npt) {
                    double d42 = d4;
                    double d7 = (this.zMatrix.getEntry(i3, 0) * d5) + (this.zMatrix.getEntry(i3, j) * d6);
                    double ztest3 = ztest2;
                    double d22 = d2;
                    this.zMatrix.setEntry(i3, j, (this.zMatrix.getEntry(i3, j) * d5) - (this.zMatrix.getEntry(i3, 0) * d6));
                    this.zMatrix.setEntry(i3, 0, d7);
                    i3++;
                    d4 = d42;
                    ztest2 = ztest3;
                    d2 = d22;
                }
            } else {
                nptm = nptm2;
                double d8 = d1;
            }
            double ztest4 = ztest2;
            this.zMatrix.setEntry(i, j, 0.0d);
            j++;
            nptm2 = nptm;
            ztest2 = ztest4;
            i2 = 0;
        }
        double d9 = ztest2;
        for (int i4 = 0; i4 < npt; i4++) {
            work.setEntry(i4, this.zMatrix.getEntry(i, 0) * this.zMatrix.getEntry(i4, 0));
        }
        double alpha = work.getEntry(i);
        double tau = this.lagrangeValuesAtNewPoint.getEntry(i);
        this.lagrangeValuesAtNewPoint.setEntry(i, this.lagrangeValuesAtNewPoint.getEntry(i) - ONE);
        double sqrtDenom = FastMath.sqrt(denom);
        double d12 = tau / sqrtDenom;
        double d23 = this.zMatrix.getEntry(i, 0) / sqrtDenom;
        int i5 = 0;
        while (i5 < npt) {
            double sqrtDenom2 = sqrtDenom;
            double d13 = d12;
            this.zMatrix.setEntry(i5, 0, (this.zMatrix.getEntry(i5, 0) * d12) - (this.lagrangeValuesAtNewPoint.getEntry(i5) * d23));
            i5++;
            sqrtDenom = sqrtDenom2;
            d12 = d13;
        }
        double d10 = d12;
        int j3 = 0;
        while (j3 < n) {
            int jp = npt + j3;
            work.setEntry(jp, this.bMatrix.getEntry(i, j3));
            double d32 = ((this.lagrangeValuesAtNewPoint.getEntry(jp) * alpha) - (work.getEntry(jp) * tau)) / denom;
            int j4 = j3;
            int n2 = n;
            double d43 = (((-beta) * work.getEntry(jp)) - (this.lagrangeValuesAtNewPoint.getEntry(jp) * tau)) / denom;
            int i6 = 0;
            while (i6 <= jp) {
                double alpha2 = alpha;
                int j5 = j4;
                double d44 = d43;
                this.bMatrix.setEntry(i6, j5, this.bMatrix.getEntry(i6, j5) + (this.lagrangeValuesAtNewPoint.getEntry(i6) * d32) + (work.getEntry(i6) * d43));
                if (i6 >= npt) {
                    this.bMatrix.setEntry(jp, i6 - npt, this.bMatrix.getEntry(i6, j5));
                }
                i6++;
                j4 = j5;
                alpha = alpha2;
                d43 = d44;
                double d11 = beta;
            }
            j3 = j4 + 1;
            n = n2;
            alpha = alpha;
            i = knew;
        }
        double d14 = alpha;
    }

    private void setup(double[] lowerBound, double[] upperBound) {
        printMethod();
        int dimension = getStartPoint().length;
        if (dimension < 2) {
            throw new NumberIsTooSmallException(Integer.valueOf(dimension), Integer.valueOf(2), true);
        }
        int i = 0;
        int[] nPointsInterval = {dimension + 2, ((dimension + 2) * (dimension + 1)) / 2};
        if (this.numberOfInterpolationPoints < nPointsInterval[0] || this.numberOfInterpolationPoints > nPointsInterval[1]) {
            throw new OutOfRangeException(LocalizedFormats.NUMBER_OF_INTERPOLATION_POINTS, Integer.valueOf(this.numberOfInterpolationPoints), Integer.valueOf(nPointsInterval[0]), Integer.valueOf(nPointsInterval[1]));
        }
        this.boundDifference = new double[dimension];
        double requiredMinDiff = this.initialTrustRegionRadius * TWO;
        double minDiff = Double.POSITIVE_INFINITY;
        while (true) {
            int i2 = i;
            if (i2 >= dimension) {
                break;
            }
            this.boundDifference[i2] = upperBound[i2] - lowerBound[i2];
            minDiff = FastMath.min(minDiff, this.boundDifference[i2]);
            i = i2 + 1;
        }
        if (minDiff < requiredMinDiff) {
            this.initialTrustRegionRadius = minDiff / 3.0d;
        }
        this.bMatrix = new Array2DRowRealMatrix(this.numberOfInterpolationPoints + dimension, dimension);
        this.zMatrix = new Array2DRowRealMatrix(this.numberOfInterpolationPoints, (this.numberOfInterpolationPoints - dimension) - 1);
        this.interpolationPoints = new Array2DRowRealMatrix(this.numberOfInterpolationPoints, dimension);
        this.originShift = new ArrayRealVector(dimension);
        this.fAtInterpolationPoints = new ArrayRealVector(this.numberOfInterpolationPoints);
        this.trustRegionCenterOffset = new ArrayRealVector(dimension);
        this.gradientAtTrustRegionCenter = new ArrayRealVector(dimension);
        this.lowerDifference = new ArrayRealVector(dimension);
        this.upperDifference = new ArrayRealVector(dimension);
        this.modelSecondDerivativesParameters = new ArrayRealVector(this.numberOfInterpolationPoints);
        this.newPoint = new ArrayRealVector(dimension);
        this.alternativeNewPoint = new ArrayRealVector(dimension);
        this.trialStepPoint = new ArrayRealVector(dimension);
        this.lagrangeValuesAtNewPoint = new ArrayRealVector(this.numberOfInterpolationPoints + dimension);
        this.modelSecondDerivativesValues = new ArrayRealVector(((dimension + 1) * dimension) / 2);
    }

    /* access modifiers changed from: private */
    public static String caller(int n) {
        StackTraceElement e = new Throwable().getStackTrace()[n];
        StringBuilder sb = new StringBuilder();
        sb.append(e.getMethodName());
        sb.append(" (at line ");
        sb.append(e.getLineNumber());
        sb.append(")");
        return sb.toString();
    }

    private static void printState(int s) {
    }

    private static void printMethod() {
    }
}

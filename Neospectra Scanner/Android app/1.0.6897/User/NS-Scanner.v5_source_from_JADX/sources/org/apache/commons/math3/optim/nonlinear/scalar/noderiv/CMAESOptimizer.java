package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateOptimizer;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

public class CMAESOptimizer extends MultivariateOptimizer {

    /* renamed from: B */
    private RealMatrix f707B;

    /* renamed from: BD */
    private RealMatrix f708BD;

    /* renamed from: C */
    private RealMatrix f709C;

    /* renamed from: D */
    private RealMatrix f710D;

    /* renamed from: cc */
    private double f711cc;
    private double ccov1;
    private double ccov1Sep;
    private double ccovmu;
    private double ccovmuSep;
    private final int checkFeasableCount;
    private double chiN;

    /* renamed from: cs */
    private double f712cs;
    private double damps;
    private RealMatrix diagC;
    private RealMatrix diagD;
    private int diagonalOnly;
    private int dimension;
    private double[] fitnessHistory;
    private final boolean generateStatistics;
    private int historySize;
    private double[] inputSigma;
    private final boolean isActiveCMA;
    /* access modifiers changed from: private */
    public boolean isMinimize = true;
    private int iterations;
    private int lambda;
    private double logMu2;
    private final int maxIterations;

    /* renamed from: mu */
    private int f713mu;
    private double mueff;
    private double normps;

    /* renamed from: pc */
    private RealMatrix f714pc;

    /* renamed from: ps */
    private RealMatrix f715ps;
    private final RandomGenerator random;
    private double sigma;
    private final List<RealMatrix> statisticsDHistory = new ArrayList();
    private final List<Double> statisticsFitnessHistory = new ArrayList();
    private final List<RealMatrix> statisticsMeanHistory = new ArrayList();
    private final List<Double> statisticsSigmaHistory = new ArrayList();
    private final double stopFitness;
    private double stopTolFun;
    private double stopTolHistFun;
    private double stopTolUpX;
    private double stopTolX;
    private RealMatrix weights;
    private RealMatrix xmean;

    private static class DoubleIndex implements Comparable<DoubleIndex> {
        /* access modifiers changed from: private */
        public final int index;
        private final double value;

        DoubleIndex(double value2, int index2) {
            this.value = value2;
            this.index = index2;
        }

        public int compareTo(DoubleIndex o) {
            return Double.compare(this.value, o.value);
        }

        public boolean equals(Object other) {
            boolean z = true;
            if (this == other) {
                return true;
            }
            if (!(other instanceof DoubleIndex)) {
                return false;
            }
            if (Double.compare(this.value, ((DoubleIndex) other).value) != 0) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            long bits = Double.doubleToLongBits(this.value);
            return (int) ((((bits >>> 32) ^ 1438542) ^ bits) & -1);
        }
    }

    private class FitnessFunction {
        private final boolean isRepairMode = true;

        FitnessFunction() {
        }

        public ValuePenaltyPair value(double[] point) {
            double value;
            double penalty = 0.0d;
            if (this.isRepairMode) {
                double[] repaired = repair(point);
                value = CMAESOptimizer.this.computeObjectiveValue(repaired);
                penalty = penalty(point, repaired);
            } else {
                value = CMAESOptimizer.this.computeObjectiveValue(point);
            }
            double value2 = value;
            return new ValuePenaltyPair(CMAESOptimizer.this.isMinimize ? value2 : -value2, CMAESOptimizer.this.isMinimize ? penalty : -penalty);
        }

        public boolean isFeasible(double[] x) {
            double[] lB = CMAESOptimizer.this.getLowerBound();
            double[] uB = CMAESOptimizer.this.getUpperBound();
            for (int i = 0; i < x.length; i++) {
                if (x[i] < lB[i] || x[i] > uB[i]) {
                    return false;
                }
            }
            return true;
        }

        /* access modifiers changed from: private */
        public double[] repair(double[] x) {
            double[] lB = CMAESOptimizer.this.getLowerBound();
            double[] uB = CMAESOptimizer.this.getUpperBound();
            double[] repaired = new double[x.length];
            for (int i = 0; i < x.length; i++) {
                if (x[i] < lB[i]) {
                    repaired[i] = lB[i];
                } else if (x[i] > uB[i]) {
                    repaired[i] = uB[i];
                } else {
                    repaired[i] = x[i];
                }
            }
            return repaired;
        }

        private double penalty(double[] x, double[] repaired) {
            double penalty = 0.0d;
            for (int i = 0; i < x.length; i++) {
                penalty += FastMath.abs(x[i] - repaired[i]);
            }
            return CMAESOptimizer.this.isMinimize ? penalty : -penalty;
        }
    }

    public static class PopulationSize implements OptimizationData {
        private final int lambda;

        public PopulationSize(int size) throws NotStrictlyPositiveException {
            if (size <= 0) {
                throw new NotStrictlyPositiveException(Integer.valueOf(size));
            }
            this.lambda = size;
        }

        public int getPopulationSize() {
            return this.lambda;
        }
    }

    public static class Sigma implements OptimizationData {
        private final double[] sigma;

        public Sigma(double[] s) throws NotPositiveException {
            for (int i = 0; i < s.length; i++) {
                if (s[i] < 0.0d) {
                    throw new NotPositiveException(Double.valueOf(s[i]));
                }
            }
            this.sigma = (double[]) s.clone();
        }

        public double[] getSigma() {
            return (double[]) this.sigma.clone();
        }
    }

    private static class ValuePenaltyPair {
        /* access modifiers changed from: private */
        public double penalty;
        /* access modifiers changed from: private */
        public double value;

        ValuePenaltyPair(double value2, double penalty2) {
            this.value = value2;
            this.penalty = penalty2;
        }
    }

    public CMAESOptimizer(int maxIterations2, double stopFitness2, boolean isActiveCMA2, int diagonalOnly2, int checkFeasableCount2, RandomGenerator random2, boolean generateStatistics2, ConvergenceChecker<PointValuePair> checker) {
        super(checker);
        this.maxIterations = maxIterations2;
        this.stopFitness = stopFitness2;
        this.isActiveCMA = isActiveCMA2;
        this.diagonalOnly = diagonalOnly2;
        this.checkFeasableCount = checkFeasableCount2;
        this.random = random2;
        this.generateStatistics = generateStatistics2;
    }

    public List<Double> getStatisticsSigmaHistory() {
        return this.statisticsSigmaHistory;
    }

    public List<RealMatrix> getStatisticsMeanHistory() {
        return this.statisticsMeanHistory;
    }

    public List<Double> getStatisticsFitnessHistory() {
        return this.statisticsFitnessHistory;
    }

    public List<RealMatrix> getStatisticsDHistory() {
        return this.statisticsDHistory;
    }

    public PointValuePair optimize(OptimizationData... optData) throws TooManyEvaluationsException, DimensionMismatchException {
        return super.optimize(optData);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:100:0x0315, code lost:
        if (r34 != r21[r38[(int) ((((double) r7.lambda) / 4.0d) + 0.1d)]]) goto L_0x0331;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:101:0x0317, code lost:
        r45 = r4;
        r46 = r6;
        r7.sigma *= org.apache.commons.math3.util.FastMath.exp((r7.f712cs / r7.damps) + 0.2d);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:102:0x0331, code lost:
        r45 = r4;
        r46 = r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:104:0x0338, code lost:
        if (r7.iterations <= 2) goto L_0x035c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:106:0x0345, code lost:
        if ((org.apache.commons.math3.util.FastMath.max(r10, r1) - org.apache.commons.math3.util.FastMath.min(r8, r1)) != 0.0d) goto L_0x035c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:107:0x0347, code lost:
        r7.sigma *= org.apache.commons.math3.util.FastMath.exp((r7.f712cs / r7.damps) + 0.2d);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:108:0x035c, code lost:
        push(r7.fitnessHistory, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:109:0x0363, code lost:
        if (r7.generateStatistics == false) goto L_0x0398;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:110:0x0365, code lost:
        r7.statisticsSigmaHistory.add(java.lang.Double.valueOf(r7.sigma));
        r7.statisticsFitnessHistory.add(java.lang.Double.valueOf(r1));
        r7.statisticsMeanHistory.add(r7.xmean.transpose());
        r7.statisticsDHistory.add(r7.diagD.transpose().scalarMultiply(100000.0d));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:111:0x0398, code lost:
        r7.iterations++;
        r9 = r18;
        r11 = r19;
        r13 = r34;
        r8 = r36;
        r12 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x0263, code lost:
        r8 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x0266, code lost:
        if (r8 >= r7.dimension) goto L_0x0279;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x0272, code lost:
        if ((r7.sigma * r6[r8]) <= r7.stopTolUpX) goto L_0x0276;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x0276, code lost:
        r8 = r8 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x0279, code lost:
        r8 = min(r7.fitnessHistory);
        r10 = max(r7.fitnessHistory);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x0288, code lost:
        if (r7.iterations <= 2) goto L_0x029b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x0298, code lost:
        if ((org.apache.commons.math3.util.FastMath.max(r10, r3) - org.apache.commons.math3.util.FastMath.min(r8, r1)) >= r7.stopTolFun) goto L_0x029b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x02a0, code lost:
        if (r7.iterations <= r7.fitnessHistory.length) goto L_0x02ad;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x02a2, code lost:
        r42 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x02aa, code lost:
        if ((r10 - r8) >= r7.stopTolHistFun) goto L_0x02af;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x02ad, code lost:
        r42 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x02c3, code lost:
        if ((max(r7.diagD) / min(r7.diagD)) <= 1.0E7d) goto L_0x02c6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x02ca, code lost:
        if (getConvergenceChecker() == null) goto L_0x02fc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x02cc, code lost:
        r4 = r37;
        r13 = r4.getColumn(0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x02d7, code lost:
        if (r7.isMinimize == false) goto L_0x02dd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x02d9, code lost:
        r44 = r13;
        r12 = r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:91:0x02dd, code lost:
        r44 = r13;
        r12 = -r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x02e0, code lost:
        r3 = new org.apache.commons.math3.optim.PointValuePair(r44, r12);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x02e5, code lost:
        if (r5 == null) goto L_0x02f8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x02f1, code lost:
        if (getConvergenceChecker().converged(r7.iterations, r3, r5) == false) goto L_0x02f8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x02f8, code lost:
        r16 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x02fc, code lost:
        r4 = r37;
        r16 = r5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.apache.commons.math3.optim.PointValuePair doOptimize() {
        /*
            r47 = this;
            r7 = r47
            org.apache.commons.math3.optim.nonlinear.scalar.GoalType r1 = r47.getGoalType()
            org.apache.commons.math3.optim.nonlinear.scalar.GoalType r2 = org.apache.commons.math3.optim.nonlinear.scalar.GoalType.MINIMIZE
            boolean r1 = r1.equals(r2)
            r7.isMinimize = r1
            org.apache.commons.math3.optim.nonlinear.scalar.noderiv.CMAESOptimizer$FitnessFunction r1 = new org.apache.commons.math3.optim.nonlinear.scalar.noderiv.CMAESOptimizer$FitnessFunction
            r1.<init>()
            r8 = r1
            double[] r9 = r47.getStartPoint()
            int r1 = r9.length
            r7.dimension = r1
            r7.initializeCMA(r9)
            r10 = 0
            r7.iterations = r10
            org.apache.commons.math3.optim.nonlinear.scalar.noderiv.CMAESOptimizer$ValuePenaltyPair r11 = r8.value(r9)
            double r1 = r11.value
            double r3 = r11.penalty
            double r1 = r1 + r3
            double[] r3 = r7.fitnessHistory
            push(r3, r1)
            org.apache.commons.math3.optim.PointValuePair r3 = new org.apache.commons.math3.optim.PointValuePair
            double[] r4 = r47.getStartPoint()
            boolean r5 = r7.isMinimize
            if (r5 == 0) goto L_0x003f
            r5 = r1
            goto L_0x0040
        L_0x003f:
            double r5 = -r1
        L_0x0040:
            r3.<init>(r4, r5)
            r4 = 0
            r12 = 1
            r7.iterations = r12
            r13 = r1
            r15 = r3
            r16 = r4
        L_0x004b:
            int r1 = r7.iterations
            int r2 = r7.maxIterations
            if (r1 > r2) goto L_0x03aa
            r47.incrementIterationCount()
            int r1 = r7.dimension
            int r2 = r7.lambda
            org.apache.commons.math3.linear.RealMatrix r6 = r7.randn1(r1, r2)
            int r1 = r7.dimension
            int r2 = r7.lambda
            org.apache.commons.math3.linear.RealMatrix r5 = zeros(r1, r2)
            int r1 = r7.lambda
            double[] r4 = new double[r1]
            int r1 = r7.lambda
            org.apache.commons.math3.optim.nonlinear.scalar.noderiv.CMAESOptimizer$ValuePenaltyPair[] r3 = new org.apache.commons.math3.optim.nonlinear.scalar.noderiv.CMAESOptimizer.ValuePenaltyPair[r1]
            r1 = 0
        L_0x006d:
            int r2 = r7.lambda
            if (r1 >= r2) goto L_0x00fe
            r2 = 0
            r17 = r2
            r2 = 0
        L_0x0075:
            int r10 = r7.checkFeasableCount
            int r10 = r10 + r12
            if (r2 >= r10) goto L_0x00d8
            int r10 = r7.diagonalOnly
            if (r10 > 0) goto L_0x0099
            org.apache.commons.math3.linear.RealMatrix r10 = r7.xmean
            org.apache.commons.math3.linear.RealMatrix r12 = r7.f708BD
            r18 = r9
            org.apache.commons.math3.linear.RealMatrix r9 = r6.getColumnMatrix(r1)
            org.apache.commons.math3.linear.RealMatrix r9 = r12.multiply(r9)
            r19 = r11
            double r11 = r7.sigma
            org.apache.commons.math3.linear.RealMatrix r9 = r9.scalarMultiply(r11)
            org.apache.commons.math3.linear.RealMatrix r9 = r10.add(r9)
        L_0x0098:
            goto L_0x00b4
        L_0x0099:
            r18 = r9
            r19 = r11
            org.apache.commons.math3.linear.RealMatrix r9 = r7.xmean
            org.apache.commons.math3.linear.RealMatrix r10 = r7.diagD
            org.apache.commons.math3.linear.RealMatrix r11 = r6.getColumnMatrix(r1)
            org.apache.commons.math3.linear.RealMatrix r10 = times(r10, r11)
            double r11 = r7.sigma
            org.apache.commons.math3.linear.RealMatrix r10 = r10.scalarMultiply(r11)
            org.apache.commons.math3.linear.RealMatrix r9 = r9.add(r10)
            goto L_0x0098
        L_0x00b4:
            int r10 = r7.checkFeasableCount
            if (r2 >= r10) goto L_0x00de
            r10 = 0
            double[] r11 = r9.getColumn(r10)
            boolean r10 = r8.isFeasible(r11)
            if (r10 == 0) goto L_0x00c4
            goto L_0x00de
        L_0x00c4:
            int r10 = r7.dimension
            double[] r10 = r7.randn(r10)
            r6.setColumn(r1, r10)
            int r2 = r2 + 1
            r17 = r9
            r9 = r18
            r11 = r19
            r10 = 0
            r12 = 1
            goto L_0x0075
        L_0x00d8:
            r18 = r9
            r19 = r11
            r9 = r17
        L_0x00de:
            r2 = 0
            copyColumn(r9, r2, r5, r1)
            double[] r2 = r5.getColumn(r1)     // Catch:{ TooManyEvaluationsException -> 0x00f7 }
            org.apache.commons.math3.optim.nonlinear.scalar.noderiv.CMAESOptimizer$ValuePenaltyPair r2 = r8.value(r2)     // Catch:{ TooManyEvaluationsException -> 0x00f7 }
            r3[r1] = r2     // Catch:{ TooManyEvaluationsException -> 0x00f7 }
            int r1 = r1 + 1
            r9 = r18
            r11 = r19
            r10 = 0
            r12 = 1
            goto L_0x006d
        L_0x00f7:
            r0 = move-exception
            r2 = r0
            r36 = r8
            goto L_0x03b0
        L_0x00fe:
            r18 = r9
            r19 = r11
            double r9 = r7.valueRange(r3)
            r1 = 0
        L_0x0107:
            int r2 = r3.length
            if (r1 >= r2) goto L_0x011f
            r2 = r3[r1]
            double r11 = r2.value
            r2 = r3[r1]
            double r20 = r2.penalty
            double r20 = r20 * r9
            double r11 = r11 + r20
            r4[r1] = r11
            int r1 = r1 + 1
            goto L_0x0107
        L_0x011f:
            int[] r11 = r7.sortedIndices(r4)
            org.apache.commons.math3.linear.RealMatrix r12 = r7.xmean
            int r1 = r7.f713mu
            int[] r1 = org.apache.commons.math3.util.MathArrays.copyOf(r11, r1)
            org.apache.commons.math3.linear.RealMatrix r2 = selectColumns(r5, r1)
            org.apache.commons.math3.linear.RealMatrix r1 = r7.weights
            org.apache.commons.math3.linear.RealMatrix r1 = r2.multiply(r1)
            r7.xmean = r1
            int r1 = r7.f713mu
            int[] r1 = org.apache.commons.math3.util.MathArrays.copyOf(r11, r1)
            org.apache.commons.math3.linear.RealMatrix r1 = selectColumns(r6, r1)
            r22 = r2
            org.apache.commons.math3.linear.RealMatrix r2 = r7.weights
            org.apache.commons.math3.linear.RealMatrix r2 = r1.multiply(r2)
            r23 = r9
            boolean r9 = r7.updateEvolutionPaths(r2, r12)
            int r10 = r7.diagonalOnly
            if (r10 > 0) goto L_0x016b
            r10 = r1
            r1 = r7
            r17 = r2
            r25 = r22
            r2 = r9
            r20 = r3
            r3 = r25
            r21 = r4
            r4 = r6
            r22 = r5
            r5 = r11
            r26 = r6
            r6 = r12
            r1.updateCovariance(r2, r3, r4, r5, r6)
            goto L_0x017b
        L_0x016b:
            r10 = r1
            r17 = r2
            r20 = r3
            r21 = r4
            r26 = r6
            r25 = r22
            r22 = r5
            r7.updateCovarianceDiagonalOnly(r9, r10)
        L_0x017b:
            double r1 = r7.sigma
            double r3 = r7.normps
            double r5 = r7.chiN
            double r3 = r3 / r5
            r5 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r3 = r3 - r5
            double r5 = r7.f712cs
            double r3 = r3 * r5
            double r5 = r7.damps
            double r3 = r3 / r5
            r5 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r3 = org.apache.commons.math3.util.FastMath.min(r5, r3)
            double r3 = org.apache.commons.math3.util.FastMath.exp(r3)
            double r1 = r1 * r3
            r7.sigma = r1
            r1 = 0
            r2 = r11[r1]
            r1 = r21[r2]
            int r3 = r11.length
            r4 = 1
            int r3 = r3 - r4
            r3 = r11[r3]
            r3 = r21[r3]
            int r5 = (r13 > r1 ? 1 : (r13 == r1 ? 0 : -1))
            if (r5 <= 0) goto L_0x01ec
            r13 = r1
            r5 = r15
            org.apache.commons.math3.optim.PointValuePair r6 = new org.apache.commons.math3.optim.PointValuePair
            r29 = r9
            r30 = r10
            r31 = r12
            r9 = r25
            r10 = 0
            double[] r12 = r9.getColumn(r10)
            double[] r10 = r8.repair(r12)
            boolean r12 = r7.isMinimize
            if (r12 == 0) goto L_0x01c7
            r32 = r13
            r12 = r1
            goto L_0x01ca
        L_0x01c7:
            r32 = r13
            double r12 = -r1
        L_0x01ca:
            r6.<init>(r10, r12)
            r15 = r6
            org.apache.commons.math3.optim.ConvergenceChecker r6 = r47.getConvergenceChecker()
            if (r6 == 0) goto L_0x01e9
            if (r5 == 0) goto L_0x01e9
            org.apache.commons.math3.optim.ConvergenceChecker r6 = r47.getConvergenceChecker()
            int r10 = r7.iterations
            boolean r6 = r6.converged(r10, r15, r5)
            if (r6 == 0) goto L_0x01e9
            r36 = r8
            r13 = r32
            goto L_0x03b2
        L_0x01e9:
            r13 = r32
            goto L_0x01f6
        L_0x01ec:
            r29 = r9
            r30 = r10
            r31 = r12
            r9 = r25
            r5 = r16
        L_0x01f6:
            r34 = r13
            double r12 = r7.stopFitness
            r27 = 0
            int r6 = (r12 > r27 ? 1 : (r12 == r27 ? 0 : -1))
            if (r6 == 0) goto L_0x0213
            boolean r6 = r7.isMinimize
            if (r6 == 0) goto L_0x0207
            double r12 = r7.stopFitness
            goto L_0x020a
        L_0x0207:
            double r12 = r7.stopFitness
            double r12 = -r12
        L_0x020a:
            int r6 = (r1 > r12 ? 1 : (r1 == r12 ? 0 : -1))
            if (r6 >= 0) goto L_0x0213
            r36 = r8
            goto L_0x02f4
        L_0x0213:
            org.apache.commons.math3.linear.RealMatrix r6 = r7.diagC
            org.apache.commons.math3.linear.RealMatrix r6 = sqrt(r6)
            r10 = 0
            double[] r6 = r6.getColumn(r10)
            org.apache.commons.math3.linear.RealMatrix r12 = r7.f714pc
            double[] r12 = r12.getColumn(r10)
            r10 = 0
        L_0x0225:
            int r13 = r7.dimension
            if (r10 >= r13) goto L_0x025b
            double r13 = r7.sigma
            r36 = r8
            r37 = r9
            r8 = r12[r10]
            double r8 = org.apache.commons.math3.util.FastMath.abs(r8)
            r38 = r11
            r39 = r12
            r11 = r6[r10]
            double r8 = org.apache.commons.math3.util.FastMath.max(r8, r11)
            double r13 = r13 * r8
            double r8 = r7.stopTolX
            int r8 = (r13 > r8 ? 1 : (r13 == r8 ? 0 : -1))
            if (r8 <= 0) goto L_0x0248
            goto L_0x0263
        L_0x0248:
            int r8 = r7.dimension
            r9 = 1
            int r8 = r8 - r9
            if (r10 < r8) goto L_0x0250
            goto L_0x02f4
        L_0x0250:
            int r10 = r10 + 1
            r8 = r36
            r9 = r37
            r11 = r38
            r12 = r39
            goto L_0x0225
        L_0x025b:
            r36 = r8
            r37 = r9
            r38 = r11
            r39 = r12
        L_0x0263:
            r8 = 0
        L_0x0264:
            int r9 = r7.dimension
            if (r8 >= r9) goto L_0x0279
            double r9 = r7.sigma
            r11 = r6[r8]
            double r9 = r9 * r11
            double r11 = r7.stopTolUpX
            int r9 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1))
            if (r9 <= 0) goto L_0x0276
            goto L_0x02f4
        L_0x0276:
            int r8 = r8 + 1
            goto L_0x0264
        L_0x0279:
            double[] r8 = r7.fitnessHistory
            double r8 = min(r8)
            double[] r10 = r7.fitnessHistory
            double r10 = max(r10)
            int r12 = r7.iterations
            r13 = 2
            if (r12 <= r13) goto L_0x029b
            double r32 = org.apache.commons.math3.util.FastMath.max(r10, r3)
            double r40 = org.apache.commons.math3.util.FastMath.min(r8, r1)
            double r32 = r32 - r40
            double r13 = r7.stopTolFun
            int r12 = (r32 > r13 ? 1 : (r32 == r13 ? 0 : -1))
            if (r12 >= 0) goto L_0x029b
            goto L_0x02f4
        L_0x029b:
            int r12 = r7.iterations
            double[] r13 = r7.fitnessHistory
            int r13 = r13.length
            if (r12 <= r13) goto L_0x02ad
            double r12 = r10 - r8
            r42 = r3
            double r3 = r7.stopTolHistFun
            int r3 = (r12 > r3 ? 1 : (r12 == r3 ? 0 : -1))
            if (r3 >= 0) goto L_0x02af
            goto L_0x02f4
        L_0x02ad:
            r42 = r3
        L_0x02af:
            org.apache.commons.math3.linear.RealMatrix r3 = r7.diagD
            double r3 = max(r3)
            org.apache.commons.math3.linear.RealMatrix r12 = r7.diagD
            double r12 = min(r12)
            double r3 = r3 / r12
            r12 = 4711630319722168320(0x416312d000000000, double:1.0E7)
            int r3 = (r3 > r12 ? 1 : (r3 == r12 ? 0 : -1))
            if (r3 <= 0) goto L_0x02c6
            goto L_0x02f4
        L_0x02c6:
            org.apache.commons.math3.optim.ConvergenceChecker r3 = r47.getConvergenceChecker()
            if (r3 == 0) goto L_0x02fc
            org.apache.commons.math3.optim.PointValuePair r3 = new org.apache.commons.math3.optim.PointValuePair
            r4 = r37
            r12 = 0
            double[] r13 = r4.getColumn(r12)
            boolean r14 = r7.isMinimize
            if (r14 == 0) goto L_0x02dd
            r44 = r13
            r12 = r1
            goto L_0x02e0
        L_0x02dd:
            r44 = r13
            double r12 = -r1
        L_0x02e0:
            r14 = r44
            r3.<init>(r14, r12)
            if (r5 == 0) goto L_0x02f8
            org.apache.commons.math3.optim.ConvergenceChecker r12 = r47.getConvergenceChecker()
            int r13 = r7.iterations
            boolean r12 = r12.converged(r13, r3, r5)
            if (r12 == 0) goto L_0x02f8
        L_0x02f4:
            r13 = r34
            goto L_0x03b2
        L_0x02f8:
            r16 = r3
            goto L_0x0300
        L_0x02fc:
            r4 = r37
            r16 = r5
        L_0x0300:
            int r3 = r7.lambda
            double r12 = (double) r3
            r32 = 4616189618054758400(0x4010000000000000, double:4.0)
            double r12 = r12 / r32
            r32 = 4591870180066957722(0x3fb999999999999a, double:0.1)
            double r12 = r12 + r32
            int r3 = (int) r12
            r3 = r38[r3]
            r12 = r21[r3]
            int r3 = (r34 > r12 ? 1 : (r34 == r12 ? 0 : -1))
            if (r3 != 0) goto L_0x0331
            double r12 = r7.sigma
            r45 = r4
            double r3 = r7.f712cs
            r46 = r6
            double r5 = r7.damps
            double r3 = r3 / r5
            r5 = 4596373779694328218(0x3fc999999999999a, double:0.2)
            double r3 = r3 + r5
            double r3 = org.apache.commons.math3.util.FastMath.exp(r3)
            double r12 = r12 * r3
            r7.sigma = r12
            goto L_0x0335
        L_0x0331:
            r45 = r4
            r46 = r6
        L_0x0335:
            int r3 = r7.iterations
            r4 = 2
            if (r3 <= r4) goto L_0x035c
            double r3 = org.apache.commons.math3.util.FastMath.max(r10, r1)
            double r5 = org.apache.commons.math3.util.FastMath.min(r8, r1)
            double r3 = r3 - r5
            int r3 = (r3 > r27 ? 1 : (r3 == r27 ? 0 : -1))
            if (r3 != 0) goto L_0x035c
            double r3 = r7.sigma
            double r5 = r7.f712cs
            double r12 = r7.damps
            double r5 = r5 / r12
            r12 = 4596373779694328218(0x3fc999999999999a, double:0.2)
            double r5 = r5 + r12
            double r5 = org.apache.commons.math3.util.FastMath.exp(r5)
            double r3 = r3 * r5
            r7.sigma = r3
        L_0x035c:
            double[] r3 = r7.fitnessHistory
            push(r3, r1)
            boolean r3 = r7.generateStatistics
            if (r3 == 0) goto L_0x0398
            java.util.List<java.lang.Double> r3 = r7.statisticsSigmaHistory
            double r4 = r7.sigma
            java.lang.Double r4 = java.lang.Double.valueOf(r4)
            r3.add(r4)
            java.util.List<java.lang.Double> r3 = r7.statisticsFitnessHistory
            java.lang.Double r4 = java.lang.Double.valueOf(r1)
            r3.add(r4)
            java.util.List<org.apache.commons.math3.linear.RealMatrix> r3 = r7.statisticsMeanHistory
            org.apache.commons.math3.linear.RealMatrix r4 = r7.xmean
            org.apache.commons.math3.linear.RealMatrix r4 = r4.transpose()
            r3.add(r4)
            java.util.List<org.apache.commons.math3.linear.RealMatrix> r3 = r7.statisticsDHistory
            org.apache.commons.math3.linear.RealMatrix r4 = r7.diagD
            org.apache.commons.math3.linear.RealMatrix r4 = r4.transpose()
            r5 = 4681608360884174848(0x40f86a0000000000, double:100000.0)
            org.apache.commons.math3.linear.RealMatrix r4 = r4.scalarMultiply(r5)
            r3.add(r4)
        L_0x0398:
            int r1 = r7.iterations
            r2 = 1
            int r1 = r1 + r2
            r7.iterations = r1
            r9 = r18
            r11 = r19
            r13 = r34
            r8 = r36
            r10 = 0
            r12 = 1
            goto L_0x004b
        L_0x03aa:
            r36 = r8
            r18 = r9
            r19 = r11
        L_0x03b0:
            r5 = r16
        L_0x03b2:
            return r15
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.optim.nonlinear.scalar.noderiv.CMAESOptimizer.doOptimize():org.apache.commons.math3.optim.PointValuePair");
    }

    /* access modifiers changed from: protected */
    public void parseOptimizationData(OptimizationData... optData) {
        OptimizationData[] arr$;
        super.parseOptimizationData(optData);
        for (OptimizationData data : optData) {
            if (data instanceof Sigma) {
                this.inputSigma = ((Sigma) data).getSigma();
            } else if (data instanceof PopulationSize) {
                this.lambda = ((PopulationSize) data).getPopulationSize();
            }
        }
        checkParameters();
    }

    private void checkParameters() {
        double[] init = getStartPoint();
        double[] lB = getLowerBound();
        double[] uB = getUpperBound();
        if (this.inputSigma == null) {
            return;
        }
        if (this.inputSigma.length != init.length) {
            throw new DimensionMismatchException(this.inputSigma.length, init.length);
        }
        for (int i = 0; i < init.length; i++) {
            if (this.inputSigma[i] > uB[i] - lB[i]) {
                throw new OutOfRangeException(Double.valueOf(this.inputSigma[i]), Integer.valueOf(0), Double.valueOf(uB[i] - lB[i]));
            }
        }
    }

    private void initializeCMA(double[] guess) {
        double[] dArr = guess;
        if (this.lambda <= 0) {
            throw new NotStrictlyPositiveException(Integer.valueOf(this.lambda));
        }
        double[][] sigmaArray = (double[][]) Array.newInstance(double.class, new int[]{dArr.length, 1});
        for (int i = 0; i < dArr.length; i++) {
            sigmaArray[i][0] = this.inputSigma[i];
        }
        Array2DRowRealMatrix array2DRowRealMatrix = new Array2DRowRealMatrix(sigmaArray, false);
        this.sigma = max((RealMatrix) array2DRowRealMatrix);
        this.stopTolUpX = max((RealMatrix) array2DRowRealMatrix) * 1000.0d;
        this.stopTolX = max((RealMatrix) array2DRowRealMatrix) * 1.0E-11d;
        this.stopTolFun = 1.0E-12d;
        this.stopTolHistFun = 1.0E-13d;
        this.f713mu = this.lambda / 2;
        this.logMu2 = FastMath.log(((double) this.f713mu) + 0.5d);
        this.weights = log(sequence(1.0d, (double) this.f713mu, 1.0d)).scalarMultiply(-1.0d).scalarAdd(this.logMu2);
        double sumwq = 0.0d;
        double sumw = 0.0d;
        for (int i2 = 0; i2 < this.f713mu; i2++) {
            double w = this.weights.getEntry(i2, 0);
            sumw += w;
            sumwq += w * w;
        }
        this.weights = this.weights.scalarMultiply(1.0d / sumw);
        this.mueff = (sumw * sumw) / sumwq;
        Array2DRowRealMatrix array2DRowRealMatrix2 = array2DRowRealMatrix;
        double d = sumw;
        this.f711cc = ((this.mueff / ((double) this.dimension)) + 4.0d) / (((double) (this.dimension + 4)) + ((this.mueff * 2.0d) / ((double) this.dimension)));
        this.f712cs = (this.mueff + 2.0d) / ((((double) this.dimension) + this.mueff) + 3.0d);
        this.damps = (((FastMath.max(0.0d, FastMath.sqrt((this.mueff - 1.0d) / ((double) (this.dimension + 1))) - 1.0d) * 2.0d) + 1.0d) * FastMath.max(0.3d, 1.0d - (((double) this.dimension) / (((double) this.maxIterations) + 1.0E-6d)))) + this.f712cs;
        this.ccov1 = 2.0d / (((((double) this.dimension) + 1.3d) * (((double) this.dimension) + 1.3d)) + this.mueff);
        this.ccovmu = FastMath.min(1.0d - this.ccov1, (((this.mueff - 2.0d) + (1.0d / this.mueff)) * 2.0d) / (((double) ((this.dimension + 2) * (this.dimension + 2))) + this.mueff));
        this.ccov1Sep = FastMath.min(1.0d, (this.ccov1 * (((double) this.dimension) + 1.5d)) / 3.0d);
        this.ccovmuSep = FastMath.min(1.0d - this.ccov1, (this.ccovmu * (((double) this.dimension) + 1.5d)) / 3.0d);
        this.chiN = FastMath.sqrt((double) this.dimension) * ((1.0d - (1.0d / (((double) this.dimension) * 4.0d))) + (1.0d / ((((double) this.dimension) * 21.0d) * ((double) this.dimension))));
        this.xmean = MatrixUtils.createColumnRealMatrix(guess);
        this.diagD = array2DRowRealMatrix2.scalarMultiply(1.0d / this.sigma);
        this.diagC = square(this.diagD);
        this.f714pc = zeros(this.dimension, 1);
        this.f715ps = zeros(this.dimension, 1);
        this.normps = this.f715ps.getFrobeniusNorm();
        this.f707B = eye(this.dimension, this.dimension);
        this.f710D = ones(this.dimension, 1);
        this.f708BD = times(this.f707B, repmat(this.diagD.transpose(), this.dimension, 1));
        this.f709C = this.f707B.multiply(diag(square(this.f710D)).multiply(this.f707B.transpose()));
        this.historySize = ((int) (((double) (this.dimension * 30)) / ((double) this.lambda))) + 10;
        this.fitnessHistory = new double[this.historySize];
        int i3 = 0;
        while (true) {
            int i4 = i3;
            if (i4 < this.historySize) {
                this.fitnessHistory[i4] = Double.MAX_VALUE;
                i3 = i4 + 1;
            } else {
                return;
            }
        }
    }

    private boolean updateEvolutionPaths(RealMatrix zmean, RealMatrix xold) {
        this.f715ps = this.f715ps.scalarMultiply(1.0d - this.f712cs).add(this.f707B.multiply(zmean).scalarMultiply(FastMath.sqrt(this.f712cs * (2.0d - this.f712cs) * this.mueff)));
        this.normps = this.f715ps.getFrobeniusNorm();
        boolean hsig = (this.normps / FastMath.sqrt(1.0d - FastMath.pow(1.0d - this.f712cs, this.iterations * 2))) / this.chiN < (2.0d / (((double) this.dimension) + 1.0d)) + 1.4d;
        this.f714pc = this.f714pc.scalarMultiply(1.0d - this.f711cc);
        if (hsig) {
            this.f714pc = this.f714pc.add(this.xmean.subtract(xold).scalarMultiply(FastMath.sqrt((this.f711cc * (2.0d - this.f711cc)) * this.mueff) / this.sigma));
        }
        return hsig;
    }

    private void updateCovarianceDiagonalOnly(boolean hsig, RealMatrix bestArz) {
        this.diagC = this.diagC.scalarMultiply((hsig ? 0.0d : this.ccov1Sep * this.f711cc * (2.0d - this.f711cc)) + ((1.0d - this.ccov1Sep) - this.ccovmuSep)).add(square(this.f714pc).scalarMultiply(this.ccov1Sep)).add(times(this.diagC, square(bestArz).multiply(this.weights)).scalarMultiply(this.ccovmuSep));
        this.diagD = sqrt(this.diagC);
        if (this.diagonalOnly > 1 && this.iterations > this.diagonalOnly) {
            this.diagonalOnly = 0;
            this.f707B = eye(this.dimension, this.dimension);
            this.f708BD = diag(this.diagD);
            this.f709C = diag(this.diagC);
        }
    }

    private void updateCovariance(boolean hsig, RealMatrix bestArx, RealMatrix arz, int[] arindex, RealMatrix xold) {
        double negccov;
        double oldFac = 0.0d;
        if (this.ccov1 + this.ccovmu > 0.0d) {
            RealMatrix realMatrix = bestArx;
            RealMatrix arpos = realMatrix.subtract(repmat(xold, 1, this.f713mu)).scalarMultiply(1.0d / this.sigma);
            RealMatrix roneu = this.f714pc.multiply(this.f714pc.transpose()).scalarMultiply(this.ccov1);
            if (!hsig) {
                oldFac = this.ccov1 * this.f711cc * (2.0d - this.f711cc);
            }
            double oldFac2 = oldFac + ((1.0d - this.ccov1) - this.ccovmu);
            if (this.isActiveCMA) {
                double oldFac3 = oldFac2;
                negccov = (((1.0d - this.ccovmu) * 0.25d) * this.mueff) / (FastMath.pow((double) (this.dimension + 2), 1.5d) + (this.mueff * 2.0d));
                int[] arReverseIndex = reverse(arindex);
                RealMatrix arzneg = selectColumns(arz, MathArrays.copyOf(arReverseIndex, this.f713mu));
                RealMatrix arnorms = sqrt(sumRows(square(arzneg)));
                int[] idxnorms = sortedIndices(arnorms.getRow(0));
                RealMatrix arnormsSorted = selectColumns(arnorms, idxnorms);
                int[] idxReverse = reverse(idxnorms);
                RealMatrix arnormsReverse = selectColumns(arnorms, idxReverse);
                RealMatrix arnorms2 = divide(arnormsReverse, arnormsSorted);
                RealMatrix realMatrix2 = arnormsSorted;
                int[] idxInv = inverse(idxnorms);
                int[] iArr = idxnorms;
                RealMatrix arnormsInv = selectColumns(arnorms2, idxInv);
                int[] iArr2 = idxInv;
                int[] iArr3 = idxReverse;
                double negcovMax = 0.33999999999999997d / square(arnormsInv).multiply(this.weights).getEntry(0, 0);
                if (negccov > negcovMax) {
                    negccov = negcovMax;
                }
                RealMatrix arzneg2 = times(arzneg, repmat(arnormsInv, this.dimension, 1));
                RealMatrix artmp = this.f708BD.multiply(arzneg2);
                RealMatrix realMatrix3 = arzneg2;
                RealMatrix Cneg = artmp.multiply(diag(this.weights)).multiply(artmp.transpose());
                RealMatrix realMatrix4 = artmp;
                RealMatrix realMatrix5 = arnormsReverse;
                double oldFac4 = oldFac3 + (negccov * 0.5d);
                double d = oldFac4;
                RealMatrix realMatrix6 = arnormsInv;
                int[] iArr4 = arReverseIndex;
                this.f709C = this.f709C.scalarMultiply(oldFac4).add(roneu).add(arpos.scalarMultiply(this.ccovmu + (0.5d * negccov)).multiply(times(repmat(this.weights, 1, this.dimension), arpos.transpose()))).subtract(Cneg.scalarMultiply(negccov));
                updateBD(negccov);
            }
            RealMatrix realMatrix7 = arz;
            double d2 = oldFac2;
            this.f709C = this.f709C.scalarMultiply(oldFac2).add(roneu).add(arpos.scalarMultiply(this.ccovmu).multiply(times(repmat(this.weights, 1, this.dimension), arpos.transpose())));
        } else {
            RealMatrix realMatrix8 = bestArx;
            RealMatrix realMatrix9 = arz;
            RealMatrix realMatrix10 = xold;
        }
        negccov = 0.0d;
        updateBD(negccov);
    }

    private void updateBD(double negccov) {
        if (this.ccov1 + this.ccovmu + negccov > 0.0d && (((((double) this.iterations) % 1.0d) / ((this.ccov1 + this.ccovmu) + negccov)) / ((double) this.dimension)) / 10.0d < 1.0d) {
            this.f709C = triu(this.f709C, 0).add(triu(this.f709C, 1).transpose());
            EigenDecomposition eig = new EigenDecomposition(this.f709C);
            this.f707B = eig.getV();
            this.f710D = eig.getD();
            this.diagD = diag(this.f710D);
            if (min(this.diagD) <= 0.0d) {
                for (int i = 0; i < this.dimension; i++) {
                    if (this.diagD.getEntry(i, 0) < 0.0d) {
                        this.diagD.setEntry(i, 0, 0.0d);
                    }
                }
                double tfac = max(this.diagD) / 1.0E14d;
                this.f709C = this.f709C.add(eye(this.dimension, this.dimension).scalarMultiply(tfac));
                this.diagD = this.diagD.add(ones(this.dimension, 1).scalarMultiply(tfac));
            }
            if (max(this.diagD) > min(this.diagD) * 1.0E14d) {
                double tfac2 = (max(this.diagD) / 1.0E14d) - min(this.diagD);
                this.f709C = this.f709C.add(eye(this.dimension, this.dimension).scalarMultiply(tfac2));
                this.diagD = this.diagD.add(ones(this.dimension, 1).scalarMultiply(tfac2));
            }
            this.diagC = diag(this.f709C);
            this.diagD = sqrt(this.diagD);
            this.f708BD = times(this.f707B, repmat(this.diagD.transpose(), this.dimension, 1));
        }
    }

    private static void push(double[] vals, double val) {
        for (int i = vals.length - 1; i > 0; i--) {
            vals[i] = vals[i - 1];
        }
        vals[0] = val;
    }

    private int[] sortedIndices(double[] doubles) {
        DoubleIndex[] dis = new DoubleIndex[doubles.length];
        for (int i = 0; i < doubles.length; i++) {
            dis[i] = new DoubleIndex(doubles[i], i);
        }
        Arrays.sort(dis);
        int[] indices = new int[doubles.length];
        for (int i2 = 0; i2 < doubles.length; i2++) {
            indices[i2] = dis[i2].index;
        }
        return indices;
    }

    private double valueRange(ValuePenaltyPair[] vpPairs) {
        ValuePenaltyPair[] arr$;
        double max = Double.NEGATIVE_INFINITY;
        double min = Double.MAX_VALUE;
        for (ValuePenaltyPair vpPair : vpPairs) {
            if (vpPair.value > max) {
                max = vpPair.value;
            }
            if (vpPair.value < min) {
                min = vpPair.value;
            }
        }
        return max - min;
    }

    private static RealMatrix log(RealMatrix m) {
        double[][] d = (double[][]) Array.newInstance(double.class, new int[]{m.getRowDimension(), m.getColumnDimension()});
        for (int r = 0; r < m.getRowDimension(); r++) {
            for (int c = 0; c < m.getColumnDimension(); c++) {
                d[r][c] = FastMath.log(m.getEntry(r, c));
            }
        }
        return new Array2DRowRealMatrix(d, false);
    }

    private static RealMatrix sqrt(RealMatrix m) {
        double[][] d = (double[][]) Array.newInstance(double.class, new int[]{m.getRowDimension(), m.getColumnDimension()});
        for (int r = 0; r < m.getRowDimension(); r++) {
            for (int c = 0; c < m.getColumnDimension(); c++) {
                d[r][c] = FastMath.sqrt(m.getEntry(r, c));
            }
        }
        return new Array2DRowRealMatrix(d, false);
    }

    private static RealMatrix square(RealMatrix m) {
        double[][] d = (double[][]) Array.newInstance(double.class, new int[]{m.getRowDimension(), m.getColumnDimension()});
        for (int r = 0; r < m.getRowDimension(); r++) {
            for (int c = 0; c < m.getColumnDimension(); c++) {
                double e = m.getEntry(r, c);
                d[r][c] = e * e;
            }
        }
        return new Array2DRowRealMatrix(d, false);
    }

    private static RealMatrix times(RealMatrix m, RealMatrix n) {
        double[][] d = (double[][]) Array.newInstance(double.class, new int[]{m.getRowDimension(), m.getColumnDimension()});
        for (int r = 0; r < m.getRowDimension(); r++) {
            for (int c = 0; c < m.getColumnDimension(); c++) {
                d[r][c] = m.getEntry(r, c) * n.getEntry(r, c);
            }
        }
        return new Array2DRowRealMatrix(d, false);
    }

    private static RealMatrix divide(RealMatrix m, RealMatrix n) {
        double[][] d = (double[][]) Array.newInstance(double.class, new int[]{m.getRowDimension(), m.getColumnDimension()});
        for (int r = 0; r < m.getRowDimension(); r++) {
            for (int c = 0; c < m.getColumnDimension(); c++) {
                d[r][c] = m.getEntry(r, c) / n.getEntry(r, c);
            }
        }
        return new Array2DRowRealMatrix(d, false);
    }

    private static RealMatrix selectColumns(RealMatrix m, int[] cols) {
        double[][] d = (double[][]) Array.newInstance(double.class, new int[]{m.getRowDimension(), cols.length});
        for (int r = 0; r < m.getRowDimension(); r++) {
            for (int c = 0; c < cols.length; c++) {
                d[r][c] = m.getEntry(r, cols[c]);
            }
        }
        return new Array2DRowRealMatrix(d, false);
    }

    private static RealMatrix triu(RealMatrix m, int k) {
        double[][] d = (double[][]) Array.newInstance(double.class, new int[]{m.getRowDimension(), m.getColumnDimension()});
        int r = 0;
        while (r < m.getRowDimension()) {
            int c = 0;
            while (c < m.getColumnDimension()) {
                d[r][c] = r <= c - k ? m.getEntry(r, c) : 0.0d;
                c++;
            }
            r++;
        }
        return new Array2DRowRealMatrix(d, false);
    }

    private static RealMatrix sumRows(RealMatrix m) {
        double[][] d = (double[][]) Array.newInstance(double.class, new int[]{1, m.getColumnDimension()});
        for (int c = 0; c < m.getColumnDimension(); c++) {
            double sum = 0.0d;
            for (int r = 0; r < m.getRowDimension(); r++) {
                sum += m.getEntry(r, c);
            }
            d[0][c] = sum;
        }
        return new Array2DRowRealMatrix(d, false);
    }

    private static RealMatrix diag(RealMatrix m) {
        if (m.getColumnDimension() == 1) {
            double[][] d = (double[][]) Array.newInstance(double.class, new int[]{m.getRowDimension(), m.getRowDimension()});
            for (int i = 0; i < m.getRowDimension(); i++) {
                d[i][i] = m.getEntry(i, 0);
            }
            return new Array2DRowRealMatrix(d, false);
        }
        double[][] d2 = (double[][]) Array.newInstance(double.class, new int[]{m.getRowDimension(), 1});
        for (int i2 = 0; i2 < m.getColumnDimension(); i2++) {
            d2[i2][0] = m.getEntry(i2, i2);
        }
        return new Array2DRowRealMatrix(d2, false);
    }

    private static void copyColumn(RealMatrix m1, int col1, RealMatrix m2, int col2) {
        for (int i = 0; i < m1.getRowDimension(); i++) {
            m2.setEntry(i, col2, m1.getEntry(i, col1));
        }
    }

    private static RealMatrix ones(int n, int m) {
        double[][] d = (double[][]) Array.newInstance(double.class, new int[]{n, m});
        for (int r = 0; r < n; r++) {
            Arrays.fill(d[r], 1.0d);
        }
        return new Array2DRowRealMatrix(d, false);
    }

    private static RealMatrix eye(int n, int m) {
        double[][] d = (double[][]) Array.newInstance(double.class, new int[]{n, m});
        for (int r = 0; r < n; r++) {
            if (r < m) {
                d[r][r] = 1.0d;
            }
        }
        return new Array2DRowRealMatrix(d, false);
    }

    private static RealMatrix zeros(int n, int m) {
        return new Array2DRowRealMatrix(n, m);
    }

    private static RealMatrix repmat(RealMatrix mat, int n, int m) {
        int rd = mat.getRowDimension();
        int cd = mat.getColumnDimension();
        double[][] d = (double[][]) Array.newInstance(double.class, new int[]{n * rd, m * cd});
        for (int r = 0; r < n * rd; r++) {
            for (int c = 0; c < m * cd; c++) {
                d[r][c] = mat.getEntry(r % rd, c % cd);
            }
        }
        return new Array2DRowRealMatrix(d, false);
    }

    private static RealMatrix sequence(double start, double end, double step) {
        int size = (int) (((end - start) / step) + 1.0d);
        double[][] d = (double[][]) Array.newInstance(double.class, new int[]{size, 1});
        double value = start;
        for (int r = 0; r < size; r++) {
            d[r][0] = value;
            value += step;
        }
        return new Array2DRowRealMatrix(d, false);
    }

    private static double max(RealMatrix m) {
        double max = -1.7976931348623157E308d;
        for (int r = 0; r < m.getRowDimension(); r++) {
            for (int c = 0; c < m.getColumnDimension(); c++) {
                double e = m.getEntry(r, c);
                if (max < e) {
                    max = e;
                }
            }
        }
        return max;
    }

    private static double min(RealMatrix m) {
        double min = Double.MAX_VALUE;
        for (int r = 0; r < m.getRowDimension(); r++) {
            for (int c = 0; c < m.getColumnDimension(); c++) {
                double e = m.getEntry(r, c);
                if (min > e) {
                    min = e;
                }
            }
        }
        return min;
    }

    private static double max(double[] m) {
        double max = -1.7976931348623157E308d;
        for (int r = 0; r < m.length; r++) {
            if (max < m[r]) {
                max = m[r];
            }
        }
        return max;
    }

    private static double min(double[] m) {
        double min = Double.MAX_VALUE;
        for (int r = 0; r < m.length; r++) {
            if (min > m[r]) {
                min = m[r];
            }
        }
        return min;
    }

    private static int[] inverse(int[] indices) {
        int[] inverse = new int[indices.length];
        for (int i = 0; i < indices.length; i++) {
            inverse[indices[i]] = i;
        }
        return inverse;
    }

    private static int[] reverse(int[] indices) {
        int[] reverse = new int[indices.length];
        for (int i = 0; i < indices.length; i++) {
            reverse[i] = indices[(indices.length - i) - 1];
        }
        return reverse;
    }

    private double[] randn(int size) {
        double[] randn = new double[size];
        for (int i = 0; i < size; i++) {
            randn[i] = this.random.nextGaussian();
        }
        return randn;
    }

    private RealMatrix randn1(int size, int popSize) {
        double[][] d = (double[][]) Array.newInstance(double.class, new int[]{size, popSize});
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < popSize; c++) {
                d[r][c] = this.random.nextGaussian();
            }
        }
        return new Array2DRowRealMatrix(d, false);
    }
}

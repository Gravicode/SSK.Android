package org.apache.commons.math3.optimization.direct;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

@Deprecated
public class CMAESOptimizer extends BaseAbstractMultivariateSimpleBoundsOptimizer<MultivariateFunction> implements MultivariateOptimizer {
    public static final int DEFAULT_CHECKFEASABLECOUNT = 0;
    public static final int DEFAULT_DIAGONALONLY = 0;
    public static final boolean DEFAULT_ISACTIVECMA = true;
    public static final int DEFAULT_MAXITERATIONS = 30000;
    public static final RandomGenerator DEFAULT_RANDOMGENERATOR = new MersenneTwister();
    public static final double DEFAULT_STOPFITNESS = 0.0d;

    /* renamed from: B */
    private RealMatrix f718B;

    /* renamed from: BD */
    private RealMatrix f719BD;

    /* renamed from: C */
    private RealMatrix f720C;

    /* renamed from: D */
    private RealMatrix f721D;

    /* renamed from: cc */
    private double f722cc;
    private double ccov1;
    private double ccov1Sep;
    private double ccovmu;
    private double ccovmuSep;
    private int checkFeasableCount;
    private double chiN;

    /* renamed from: cs */
    private double f723cs;
    private double damps;
    private RealMatrix diagC;
    private RealMatrix diagD;
    private int diagonalOnly;
    private int dimension;
    private double[] fitnessHistory;
    private boolean generateStatistics;
    private int historySize;
    private double[] inputSigma;
    private boolean isActiveCMA;
    /* access modifiers changed from: private */
    public boolean isMinimize;
    private int iterations;
    private int lambda;
    private double logMu2;
    private int maxIterations;

    /* renamed from: mu */
    private int f724mu;
    private double mueff;
    private double normps;

    /* renamed from: pc */
    private RealMatrix f725pc;

    /* renamed from: ps */
    private RealMatrix f726ps;
    private RandomGenerator random;
    private double sigma;
    private List<RealMatrix> statisticsDHistory;
    private List<Double> statisticsFitnessHistory;
    private List<RealMatrix> statisticsMeanHistory;
    private List<Double> statisticsSigmaHistory;
    private double stopFitness;
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
        private double valueRange = 1.0d;

        FitnessFunction() {
        }

        public double value(double[] point) {
            double value;
            if (this.isRepairMode) {
                double[] repaired = repair(point);
                value = CMAESOptimizer.this.computeObjectiveValue(repaired) + penalty(point, repaired);
            } else {
                value = CMAESOptimizer.this.computeObjectiveValue(point);
            }
            double value2 = value;
            return CMAESOptimizer.this.isMinimize ? value2 : -value2;
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

        public void setValueRange(double valueRange2) {
            this.valueRange = valueRange2;
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
                penalty += this.valueRange * FastMath.abs(x[i] - repaired[i]);
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

    @Deprecated
    public CMAESOptimizer() {
        this(0);
    }

    @Deprecated
    public CMAESOptimizer(int lambda2) {
        this(lambda2, null, DEFAULT_MAXITERATIONS, 0.0d, true, 0, 0, DEFAULT_RANDOMGENERATOR, false, null);
    }

    @Deprecated
    public CMAESOptimizer(int lambda2, double[] inputSigma2) {
        this(lambda2, inputSigma2, DEFAULT_MAXITERATIONS, 0.0d, true, 0, 0, DEFAULT_RANDOMGENERATOR, false);
    }

    @Deprecated
    public CMAESOptimizer(int lambda2, double[] inputSigma2, int maxIterations2, double stopFitness2, boolean isActiveCMA2, int diagonalOnly2, int checkFeasableCount2, RandomGenerator random2, boolean generateStatistics2) {
        this(lambda2, inputSigma2, maxIterations2, stopFitness2, isActiveCMA2, diagonalOnly2, checkFeasableCount2, random2, generateStatistics2, new SimpleValueChecker());
    }

    @Deprecated
    public CMAESOptimizer(int lambda2, double[] inputSigma2, int maxIterations2, double stopFitness2, boolean isActiveCMA2, int diagonalOnly2, int checkFeasableCount2, RandomGenerator random2, boolean generateStatistics2, ConvergenceChecker<PointValuePair> checker) {
        super(checker);
        this.diagonalOnly = 0;
        this.isMinimize = true;
        this.generateStatistics = false;
        this.statisticsSigmaHistory = new ArrayList();
        this.statisticsMeanHistory = new ArrayList();
        this.statisticsFitnessHistory = new ArrayList();
        this.statisticsDHistory = new ArrayList();
        this.lambda = lambda2;
        this.inputSigma = inputSigma2 == null ? null : (double[]) inputSigma2.clone();
        this.maxIterations = maxIterations2;
        this.stopFitness = stopFitness2;
        this.isActiveCMA = isActiveCMA2;
        this.diagonalOnly = diagonalOnly2;
        this.checkFeasableCount = checkFeasableCount2;
        this.random = random2;
        this.generateStatistics = generateStatistics2;
    }

    public CMAESOptimizer(int maxIterations2, double stopFitness2, boolean isActiveCMA2, int diagonalOnly2, int checkFeasableCount2, RandomGenerator random2, boolean generateStatistics2, ConvergenceChecker<PointValuePair> checker) {
        super(checker);
        this.diagonalOnly = 0;
        this.isMinimize = true;
        this.generateStatistics = false;
        this.statisticsSigmaHistory = new ArrayList();
        this.statisticsMeanHistory = new ArrayList();
        this.statisticsFitnessHistory = new ArrayList();
        this.statisticsDHistory = new ArrayList();
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

    /* access modifiers changed from: protected */
    public PointValuePair optimizeInternal(int maxEval, MultivariateFunction f, GoalType goalType, OptimizationData... optData) {
        parseOptimizationData(optData);
        return super.optimizeInternal(maxEval, f, goalType, optData);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:101:0x0311, code lost:
        if (r7.iterations <= 2) goto L_0x033d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:102:0x0313, code lost:
        r10 = r39;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:103:0x0320, code lost:
        if ((org.apache.commons.math3.util.FastMath.max(r12, r2) - org.apache.commons.math3.util.FastMath.min(r10, r2)) != 0.0d) goto L_0x033a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:104:0x0322, code lost:
        r42 = r10;
        r7.sigma *= org.apache.commons.math3.util.FastMath.exp((r7.f723cs / r7.damps) + 0.2d);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:105:0x033a, code lost:
        r42 = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:106:0x033d, code lost:
        r42 = r39;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:107:0x033f, code lost:
        push(r7.fitnessHistory, r2);
        r10 = r30;
        r10.setValueRange(r4 - r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:108:0x034d, code lost:
        if (r7.generateStatistics == false) goto L_0x0382;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:109:0x034f, code lost:
        r7.statisticsSigmaHistory.add(java.lang.Double.valueOf(r7.sigma));
        r7.statisticsFitnessHistory.add(java.lang.Double.valueOf(r2));
        r7.statisticsMeanHistory.add(r7.xmean.transpose());
        r7.statisticsDHistory.add(r7.diagD.transpose().scalarMultiply(100000.0d));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:110:0x0382, code lost:
        r7.iterations++;
        r13 = r1;
        r8 = r10;
        r9 = r20;
        r12 = r31;
        r14 = r32;
        r11 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x023d, code lost:
        r1 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x0240, code lost:
        if (r1 >= r7.dimension) goto L_0x0252;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x024c, code lost:
        if ((r7.sigma * r6[r1]) <= r7.stopTolUpX) goto L_0x024f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x024f, code lost:
        r1 = r1 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x0252, code lost:
        r10 = min(r7.fitnessHistory);
        r12 = max(r7.fitnessHistory);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x0261, code lost:
        if (r7.iterations <= 2) goto L_0x0276;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x0263, code lost:
        r38 = r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x0273, code lost:
        if ((org.apache.commons.math3.util.FastMath.max(r12, r4) - org.apache.commons.math3.util.FastMath.min(r10, r2)) >= r7.stopTolFun) goto L_0x0278;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x0276, code lost:
        r38 = r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x027d, code lost:
        if (r7.iterations <= r7.fitnessHistory.length) goto L_0x0288;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x0285, code lost:
        if ((r12 - r10) >= r7.stopTolHistFun) goto L_0x0288;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x029c, code lost:
        if ((max(r7.diagD) / min(r7.diagD)) <= 1.0E7d) goto L_0x029f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x02a3, code lost:
        if (getConvergenceChecker() == null) goto L_0x02d7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x02a5, code lost:
        r8 = r36;
        r14 = r8.getColumn(0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x02b0, code lost:
        if (r7.isMinimize == false) goto L_0x02b6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x02b2, code lost:
        r39 = r10;
        r9 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x02b6, code lost:
        r39 = r10;
        r9 = -r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x02b9, code lost:
        r1 = new org.apache.commons.math3.optimization.PointValuePair(r14, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x02bc, code lost:
        if (r37 == null) goto L_0x02d4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:91:0x02be, code lost:
        r11 = r37;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x02ca, code lost:
        if (getConvergenceChecker().converged(r7.iterations, r1, r11) == false) goto L_0x02de;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x02d4, code lost:
        r11 = r37;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x02d7, code lost:
        r39 = r10;
        r8 = r36;
        r1 = r37;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x02f2, code lost:
        if (r32 != r18[r34[(int) ((((double) r7.lambda) / 4.0d) + 0.1d)]]) goto L_0x030c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x02f4, code lost:
        r41 = r8;
        r7.sigma *= org.apache.commons.math3.util.FastMath.exp((r7.f723cs / r7.damps) + 0.2d);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:99:0x030c, code lost:
        r41 = r8;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.apache.commons.math3.optimization.PointValuePair doOptimize() {
        /*
            r44 = this;
            r7 = r44
            r44.checkParameters()
            org.apache.commons.math3.optimization.GoalType r1 = r44.getGoalType()
            org.apache.commons.math3.optimization.GoalType r2 = org.apache.commons.math3.optimization.GoalType.MINIMIZE
            boolean r1 = r1.equals(r2)
            r7.isMinimize = r1
            org.apache.commons.math3.optimization.direct.CMAESOptimizer$FitnessFunction r1 = new org.apache.commons.math3.optimization.direct.CMAESOptimizer$FitnessFunction
            r1.<init>()
            r8 = r1
            double[] r9 = r44.getStartPoint()
            int r1 = r9.length
            r7.dimension = r1
            r7.initializeCMA(r9)
            r10 = 0
            r7.iterations = r10
            double r1 = r8.value(r9)
            double[] r3 = r7.fitnessHistory
            push(r3, r1)
            org.apache.commons.math3.optimization.PointValuePair r3 = new org.apache.commons.math3.optimization.PointValuePair
            double[] r4 = r44.getStartPoint()
            boolean r5 = r7.isMinimize
            if (r5 == 0) goto L_0x0039
            r5 = r1
            goto L_0x003a
        L_0x0039:
            double r5 = -r1
        L_0x003a:
            r3.<init>(r4, r5)
            r4 = 0
            r11 = 1
            r7.iterations = r11
            r14 = r1
            r12 = r3
            r13 = r4
        L_0x0044:
            int r1 = r7.iterations
            int r2 = r7.maxIterations
            if (r1 > r2) goto L_0x0394
            int r1 = r7.dimension
            int r2 = r7.lambda
            org.apache.commons.math3.linear.RealMatrix r6 = r7.randn1(r1, r2)
            int r1 = r7.dimension
            int r2 = r7.lambda
            org.apache.commons.math3.linear.RealMatrix r5 = zeros(r1, r2)
            int r1 = r7.lambda
            double[] r4 = new double[r1]
            r1 = 0
        L_0x005f:
            int r2 = r7.lambda
            if (r1 >= r2) goto L_0x00e7
            r2 = 0
            r3 = r2
            r2 = 0
        L_0x0066:
            int r10 = r7.checkFeasableCount
            int r10 = r10 + r11
            if (r2 >= r10) goto L_0x00c5
            int r10 = r7.diagonalOnly
            if (r10 > 0) goto L_0x008a
            org.apache.commons.math3.linear.RealMatrix r10 = r7.xmean
            org.apache.commons.math3.linear.RealMatrix r11 = r7.f719BD
            r16 = r3
            org.apache.commons.math3.linear.RealMatrix r3 = r6.getColumnMatrix(r1)
            org.apache.commons.math3.linear.RealMatrix r3 = r11.multiply(r3)
            r17 = r12
            double r11 = r7.sigma
            org.apache.commons.math3.linear.RealMatrix r3 = r3.scalarMultiply(r11)
            org.apache.commons.math3.linear.RealMatrix r3 = r10.add(r3)
        L_0x0089:
            goto L_0x00a5
        L_0x008a:
            r16 = r3
            r17 = r12
            org.apache.commons.math3.linear.RealMatrix r3 = r7.xmean
            org.apache.commons.math3.linear.RealMatrix r10 = r7.diagD
            org.apache.commons.math3.linear.RealMatrix r11 = r6.getColumnMatrix(r1)
            org.apache.commons.math3.linear.RealMatrix r10 = times(r10, r11)
            double r11 = r7.sigma
            org.apache.commons.math3.linear.RealMatrix r10 = r10.scalarMultiply(r11)
            org.apache.commons.math3.linear.RealMatrix r3 = r3.add(r10)
            goto L_0x0089
        L_0x00a5:
            int r10 = r7.checkFeasableCount
            if (r2 >= r10) goto L_0x00c9
            r10 = 0
            double[] r11 = r3.getColumn(r10)
            boolean r10 = r8.isFeasible(r11)
            if (r10 == 0) goto L_0x00b5
            goto L_0x00c9
        L_0x00b5:
            int r10 = r7.dimension
            double[] r10 = r7.randn(r10)
            r6.setColumn(r1, r10)
            int r2 = r2 + 1
            r12 = r17
            r10 = 0
            r11 = 1
            goto L_0x0066
        L_0x00c5:
            r16 = r3
            r17 = r12
        L_0x00c9:
            r2 = 0
            copyColumn(r3, r2, r5, r1)
            double[] r2 = r5.getColumn(r1)     // Catch:{ TooManyEvaluationsException -> 0x00df }
            double r10 = r8.value(r2)     // Catch:{ TooManyEvaluationsException -> 0x00df }
            r4[r1] = r10     // Catch:{ TooManyEvaluationsException -> 0x00df }
            int r1 = r1 + 1
            r12 = r17
            r10 = 0
            r11 = 1
            goto L_0x005f
        L_0x00df:
            r0 = move-exception
            r2 = r0
            r10 = r8
            r20 = r9
            goto L_0x0399
        L_0x00e7:
            r17 = r12
            int[] r10 = r7.sortedIndices(r4)
            org.apache.commons.math3.linear.RealMatrix r11 = r7.xmean
            int r1 = r7.f724mu
            int[] r1 = org.apache.commons.math3.util.MathArrays.copyOf(r10, r1)
            org.apache.commons.math3.linear.RealMatrix r12 = selectColumns(r5, r1)
            org.apache.commons.math3.linear.RealMatrix r1 = r7.weights
            org.apache.commons.math3.linear.RealMatrix r1 = r12.multiply(r1)
            r7.xmean = r1
            int r1 = r7.f724mu
            int[] r1 = org.apache.commons.math3.util.MathArrays.copyOf(r10, r1)
            org.apache.commons.math3.linear.RealMatrix r3 = selectColumns(r6, r1)
            org.apache.commons.math3.linear.RealMatrix r1 = r7.weights
            org.apache.commons.math3.linear.RealMatrix r2 = r3.multiply(r1)
            boolean r1 = r7.updateEvolutionPaths(r2, r11)
            r18 = r1
            int r1 = r7.diagonalOnly
            if (r1 > 0) goto L_0x0135
            r19 = r18
            r1 = r7
            r16 = r2
            r2 = r19
            r20 = r9
            r9 = r3
            r3 = r12
            r18 = r4
            r4 = r6
            r21 = r5
            r5 = r10
            r22 = r6
            r6 = r11
            r1.updateCovariance(r2, r3, r4, r5, r6)
            r1 = r19
            goto L_0x0147
        L_0x0135:
            r16 = r2
            r21 = r5
            r22 = r6
            r20 = r9
            r19 = r18
            r9 = r3
            r18 = r4
            r1 = r19
            r7.updateCovarianceDiagonalOnly(r1, r9)
        L_0x0147:
            double r2 = r7.sigma
            double r4 = r7.normps
            r23 = r8
            r24 = r9
            double r8 = r7.chiN
            double r4 = r4 / r8
            r8 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r4 = r4 - r8
            double r8 = r7.f723cs
            double r4 = r4 * r8
            double r8 = r7.damps
            double r4 = r4 / r8
            r8 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r4 = org.apache.commons.math3.util.FastMath.min(r8, r4)
            double r4 = org.apache.commons.math3.util.FastMath.exp(r4)
            double r2 = r2 * r4
            r7.sigma = r2
            r2 = 0
            r3 = r10[r2]
            r2 = r18[r3]
            int r4 = r10.length
            r5 = 1
            int r4 = r4 - r5
            r4 = r10[r4]
            r4 = r18[r4]
            int r6 = (r14 > r2 ? 1 : (r14 == r2 ? 0 : -1))
            if (r6 <= 0) goto L_0x01ba
            r14 = r2
            r13 = r17
            org.apache.commons.math3.optimization.PointValuePair r6 = new org.apache.commons.math3.optimization.PointValuePair
            r8 = 0
            double[] r9 = r12.getColumn(r8)
            r8 = r23
            double[] r9 = r8.repair(r9)
            r27 = r1
            boolean r1 = r7.isMinimize
            if (r1 == 0) goto L_0x0194
            r28 = r14
            r14 = r2
            goto L_0x0197
        L_0x0194:
            r28 = r14
            double r14 = -r2
        L_0x0197:
            r6.<init>(r9, r14)
            r1 = r6
            org.apache.commons.math3.optimization.ConvergenceChecker r6 = r44.getConvergenceChecker()
            if (r6 == 0) goto L_0x01b7
            if (r13 == 0) goto L_0x01b7
            org.apache.commons.math3.optimization.ConvergenceChecker r6 = r44.getConvergenceChecker()
            int r9 = r7.iterations
            boolean r6 = r6.converged(r9, r1, r13)
            if (r6 == 0) goto L_0x01b7
            r31 = r1
            r10 = r8
            r14 = r28
            goto L_0x039b
        L_0x01b7:
            r14 = r28
            goto L_0x01c0
        L_0x01ba:
            r27 = r1
            r8 = r23
            r1 = r17
        L_0x01c0:
            r30 = r8
            double r8 = r7.stopFitness
            r25 = 0
            int r6 = (r8 > r25 ? 1 : (r8 == r25 ? 0 : -1))
            if (r6 == 0) goto L_0x01e0
            boolean r6 = r7.isMinimize
            if (r6 == 0) goto L_0x01d1
            double r8 = r7.stopFitness
            goto L_0x01d4
        L_0x01d1:
            double r8 = r7.stopFitness
            double r8 = -r8
        L_0x01d4:
            int r6 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1))
            if (r6 >= 0) goto L_0x01e0
            r31 = r1
            r11 = r13
            r32 = r14
            goto L_0x02cd
        L_0x01e0:
            org.apache.commons.math3.linear.RealMatrix r6 = r7.diagC
            org.apache.commons.math3.linear.RealMatrix r6 = sqrt(r6)
            r8 = 0
            double[] r6 = r6.getColumn(r8)
            org.apache.commons.math3.linear.RealMatrix r9 = r7.f725pc
            double[] r9 = r9.getColumn(r8)
            r8 = 0
        L_0x01f2:
            r31 = r1
            int r1 = r7.dimension
            if (r8 >= r1) goto L_0x0233
            r32 = r14
            double r14 = r7.sigma
            r34 = r10
            r35 = r11
            r10 = r9[r8]
            double r10 = org.apache.commons.math3.util.FastMath.abs(r10)
            r36 = r12
            r37 = r13
            r12 = r6[r8]
            double r10 = org.apache.commons.math3.util.FastMath.max(r10, r12)
            double r14 = r14 * r10
            double r10 = r7.stopTolX
            int r1 = (r14 > r10 ? 1 : (r14 == r10 ? 0 : -1))
            if (r1 <= 0) goto L_0x0219
            goto L_0x023d
        L_0x0219:
            int r1 = r7.dimension
            r10 = 1
            int r1 = r1 - r10
            if (r8 < r1) goto L_0x0224
        L_0x0220:
            r11 = r37
            goto L_0x02cd
        L_0x0224:
            int r8 = r8 + 1
            r1 = r31
            r14 = r32
            r10 = r34
            r11 = r35
            r12 = r36
            r13 = r37
            goto L_0x01f2
        L_0x0233:
            r34 = r10
            r35 = r11
            r36 = r12
            r37 = r13
            r32 = r14
        L_0x023d:
            r1 = 0
        L_0x023e:
            int r8 = r7.dimension
            if (r1 >= r8) goto L_0x0252
            double r10 = r7.sigma
            r12 = r6[r1]
            double r10 = r10 * r12
            double r12 = r7.stopTolUpX
            int r8 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
            if (r8 <= 0) goto L_0x024f
            goto L_0x0220
        L_0x024f:
            int r1 = r1 + 1
            goto L_0x023e
        L_0x0252:
            double[] r1 = r7.fitnessHistory
            double r10 = min(r1)
            double[] r1 = r7.fitnessHistory
            double r12 = max(r1)
            int r1 = r7.iterations
            r8 = 2
            if (r1 <= r8) goto L_0x0276
            double r14 = org.apache.commons.math3.util.FastMath.max(r12, r4)
            double r28 = org.apache.commons.math3.util.FastMath.min(r10, r2)
            double r14 = r14 - r28
            r38 = r9
            double r8 = r7.stopTolFun
            int r1 = (r14 > r8 ? 1 : (r14 == r8 ? 0 : -1))
            if (r1 >= 0) goto L_0x0278
            goto L_0x0220
        L_0x0276:
            r38 = r9
        L_0x0278:
            int r1 = r7.iterations
            double[] r8 = r7.fitnessHistory
            int r8 = r8.length
            if (r1 <= r8) goto L_0x0288
            double r8 = r12 - r10
            double r14 = r7.stopTolHistFun
            int r1 = (r8 > r14 ? 1 : (r8 == r14 ? 0 : -1))
            if (r1 >= 0) goto L_0x0288
            goto L_0x0220
        L_0x0288:
            org.apache.commons.math3.linear.RealMatrix r1 = r7.diagD
            double r8 = max(r1)
            org.apache.commons.math3.linear.RealMatrix r1 = r7.diagD
            double r14 = min(r1)
            double r8 = r8 / r14
            r14 = 4711630319722168320(0x416312d000000000, double:1.0E7)
            int r1 = (r8 > r14 ? 1 : (r8 == r14 ? 0 : -1))
            if (r1 <= 0) goto L_0x029f
            goto L_0x0220
        L_0x029f:
            org.apache.commons.math3.optimization.ConvergenceChecker r1 = r44.getConvergenceChecker()
            if (r1 == 0) goto L_0x02d7
            org.apache.commons.math3.optimization.PointValuePair r1 = new org.apache.commons.math3.optimization.PointValuePair
            r8 = r36
            r9 = 0
            double[] r14 = r8.getColumn(r9)
            boolean r15 = r7.isMinimize
            if (r15 == 0) goto L_0x02b6
            r39 = r10
            r9 = r2
            goto L_0x02b9
        L_0x02b6:
            r39 = r10
            double r9 = -r2
        L_0x02b9:
            r1.<init>(r14, r9)
            if (r37 == 0) goto L_0x02d4
            org.apache.commons.math3.optimization.ConvergenceChecker r9 = r44.getConvergenceChecker()
            int r10 = r7.iterations
            r11 = r37
            boolean r9 = r9.converged(r10, r1, r11)
            if (r9 == 0) goto L_0x02d6
        L_0x02cd:
            r13 = r11
            r10 = r30
            r14 = r32
            goto L_0x039b
        L_0x02d4:
            r11 = r37
        L_0x02d6:
            goto L_0x02de
        L_0x02d7:
            r39 = r10
            r8 = r36
            r11 = r37
            r1 = r11
        L_0x02de:
            r9 = 4591870180066957722(0x3fb999999999999a, double:0.1)
            int r11 = r7.lambda
            double r14 = (double) r11
            r28 = 4616189618054758400(0x4010000000000000, double:4.0)
            double r14 = r14 / r28
            double r14 = r14 + r9
            int r9 = (int) r14
            r9 = r34[r9]
            r9 = r18[r9]
            int r9 = (r32 > r9 ? 1 : (r32 == r9 ? 0 : -1))
            if (r9 != 0) goto L_0x030c
            double r14 = r7.sigma
            double r10 = r7.f723cs
            r41 = r8
            double r8 = r7.damps
            double r10 = r10 / r8
            r8 = 4596373779694328218(0x3fc999999999999a, double:0.2)
            double r10 = r10 + r8
            double r8 = org.apache.commons.math3.util.FastMath.exp(r10)
            double r14 = r14 * r8
            r7.sigma = r14
            goto L_0x030e
        L_0x030c:
            r41 = r8
        L_0x030e:
            int r8 = r7.iterations
            r9 = 2
            if (r8 <= r9) goto L_0x033d
            double r8 = org.apache.commons.math3.util.FastMath.max(r12, r2)
            r10 = r39
            double r14 = org.apache.commons.math3.util.FastMath.min(r10, r2)
            double r8 = r8 - r14
            int r8 = (r8 > r25 ? 1 : (r8 == r25 ? 0 : -1))
            if (r8 != 0) goto L_0x033a
            double r8 = r7.sigma
            double r14 = r7.f723cs
            r42 = r10
            double r10 = r7.damps
            double r14 = r14 / r10
            r10 = 4596373779694328218(0x3fc999999999999a, double:0.2)
            double r14 = r14 + r10
            double r10 = org.apache.commons.math3.util.FastMath.exp(r14)
            double r8 = r8 * r10
            r7.sigma = r8
            goto L_0x033f
        L_0x033a:
            r42 = r10
            goto L_0x033f
        L_0x033d:
            r42 = r39
        L_0x033f:
            double[] r8 = r7.fitnessHistory
            push(r8, r2)
            double r8 = r4 - r2
            r10 = r30
            r10.setValueRange(r8)
            boolean r8 = r7.generateStatistics
            if (r8 == 0) goto L_0x0382
            java.util.List<java.lang.Double> r8 = r7.statisticsSigmaHistory
            double r14 = r7.sigma
            java.lang.Double r9 = java.lang.Double.valueOf(r14)
            r8.add(r9)
            java.util.List<java.lang.Double> r8 = r7.statisticsFitnessHistory
            java.lang.Double r9 = java.lang.Double.valueOf(r2)
            r8.add(r9)
            java.util.List<org.apache.commons.math3.linear.RealMatrix> r8 = r7.statisticsMeanHistory
            org.apache.commons.math3.linear.RealMatrix r9 = r7.xmean
            org.apache.commons.math3.linear.RealMatrix r9 = r9.transpose()
            r8.add(r9)
            java.util.List<org.apache.commons.math3.linear.RealMatrix> r8 = r7.statisticsDHistory
            org.apache.commons.math3.linear.RealMatrix r9 = r7.diagD
            org.apache.commons.math3.linear.RealMatrix r9 = r9.transpose()
            r14 = 4681608360884174848(0x40f86a0000000000, double:100000.0)
            org.apache.commons.math3.linear.RealMatrix r9 = r9.scalarMultiply(r14)
            r8.add(r9)
        L_0x0382:
            int r2 = r7.iterations
            r3 = 1
            int r2 = r2 + r3
            r7.iterations = r2
            r13 = r1
            r8 = r10
            r9 = r20
            r12 = r31
            r14 = r32
            r10 = 0
            r11 = 1
            goto L_0x0044
        L_0x0394:
            r10 = r8
            r20 = r9
            r17 = r12
        L_0x0399:
            r31 = r17
        L_0x039b:
            return r31
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.optimization.direct.CMAESOptimizer.doOptimize():org.apache.commons.math3.optimization.PointValuePair");
    }

    private void parseOptimizationData(OptimizationData... optData) {
        OptimizationData[] arr$;
        for (OptimizationData data : optData) {
            if (data instanceof Sigma) {
                this.inputSigma = ((Sigma) data).getSigma();
            } else if (data instanceof PopulationSize) {
                this.lambda = ((PopulationSize) data).getPopulationSize();
            }
        }
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
        int i = 0;
        while (i < init.length) {
            if (this.inputSigma[i] < 0.0d) {
                throw new NotPositiveException(Double.valueOf(this.inputSigma[i]));
            } else if (this.inputSigma[i] > uB[i] - lB[i]) {
                throw new OutOfRangeException(Double.valueOf(this.inputSigma[i]), Integer.valueOf(0), Double.valueOf(uB[i] - lB[i]));
            } else {
                i++;
            }
        }
    }

    private void initializeCMA(double[] guess) {
        double[] dArr = guess;
        if (this.lambda <= 0) {
            this.lambda = ((int) (FastMath.log((double) this.dimension) * 3.0d)) + 4;
        }
        double[][] sigmaArray = (double[][]) Array.newInstance(double.class, new int[]{dArr.length, 1});
        int i = 0;
        while (i < dArr.length) {
            sigmaArray[i][0] = this.inputSigma == null ? 0.3d : this.inputSigma[i];
            i++;
        }
        Array2DRowRealMatrix array2DRowRealMatrix = new Array2DRowRealMatrix(sigmaArray, false);
        this.sigma = max((RealMatrix) array2DRowRealMatrix);
        this.stopTolUpX = max((RealMatrix) array2DRowRealMatrix) * 1000.0d;
        this.stopTolX = max((RealMatrix) array2DRowRealMatrix) * 1.0E-11d;
        this.stopTolFun = 1.0E-12d;
        this.stopTolHistFun = 1.0E-13d;
        this.f724mu = this.lambda / 2;
        this.logMu2 = FastMath.log(((double) this.f724mu) + 0.5d);
        this.weights = log(sequence(1.0d, (double) this.f724mu, 1.0d)).scalarMultiply(-1.0d).scalarAdd(this.logMu2);
        double sumw = 0.0d;
        double sumwq = 0.0d;
        for (int i2 = 0; i2 < this.f724mu; i2++) {
            double w = this.weights.getEntry(i2, 0);
            sumw += w;
            sumwq += w * w;
        }
        Array2DRowRealMatrix array2DRowRealMatrix2 = array2DRowRealMatrix;
        this.weights = this.weights.scalarMultiply(1.0d / sumw);
        this.mueff = (sumw * sumw) / sumwq;
        double d = sumw;
        this.f722cc = ((this.mueff / ((double) this.dimension)) + 4.0d) / (((double) (this.dimension + 4)) + ((this.mueff * 2.0d) / ((double) this.dimension)));
        this.f723cs = (this.mueff + 2.0d) / ((((double) this.dimension) + this.mueff) + 3.0d);
        this.damps = (((FastMath.max(0.0d, FastMath.sqrt((this.mueff - 1.0d) / ((double) (this.dimension + 1))) - 1.0d) * 2.0d) + 1.0d) * FastMath.max(0.3d, 1.0d - (((double) this.dimension) / (((double) this.maxIterations) + 1.0E-6d)))) + this.f723cs;
        this.ccov1 = 2.0d / (((((double) this.dimension) + 1.3d) * (((double) this.dimension) + 1.3d)) + this.mueff);
        this.ccovmu = FastMath.min(1.0d - this.ccov1, (((this.mueff - 2.0d) + (1.0d / this.mueff)) * 2.0d) / (((double) ((this.dimension + 2) * (this.dimension + 2))) + this.mueff));
        this.ccov1Sep = FastMath.min(1.0d, (this.ccov1 * (((double) this.dimension) + 1.5d)) / 3.0d);
        this.ccovmuSep = FastMath.min(1.0d - this.ccov1, (this.ccovmu * (((double) this.dimension) + 1.5d)) / 3.0d);
        this.chiN = FastMath.sqrt((double) this.dimension) * ((1.0d - (1.0d / (((double) this.dimension) * 4.0d))) + (1.0d / ((((double) this.dimension) * 21.0d) * ((double) this.dimension))));
        this.xmean = MatrixUtils.createColumnRealMatrix(guess);
        this.diagD = array2DRowRealMatrix2.scalarMultiply(1.0d / this.sigma);
        this.diagC = square(this.diagD);
        this.f725pc = zeros(this.dimension, 1);
        this.f726ps = zeros(this.dimension, 1);
        this.normps = this.f726ps.getFrobeniusNorm();
        this.f718B = eye(this.dimension, this.dimension);
        this.f721D = ones(this.dimension, 1);
        this.f719BD = times(this.f718B, repmat(this.diagD.transpose(), this.dimension, 1));
        this.f720C = this.f718B.multiply(diag(square(this.f721D)).multiply(this.f718B.transpose()));
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
        this.f726ps = this.f726ps.scalarMultiply(1.0d - this.f723cs).add(this.f718B.multiply(zmean).scalarMultiply(FastMath.sqrt(this.f723cs * (2.0d - this.f723cs) * this.mueff)));
        this.normps = this.f726ps.getFrobeniusNorm();
        boolean hsig = (this.normps / FastMath.sqrt(1.0d - FastMath.pow(1.0d - this.f723cs, this.iterations * 2))) / this.chiN < (2.0d / (((double) this.dimension) + 1.0d)) + 1.4d;
        this.f725pc = this.f725pc.scalarMultiply(1.0d - this.f722cc);
        if (hsig) {
            this.f725pc = this.f725pc.add(this.xmean.subtract(xold).scalarMultiply(FastMath.sqrt((this.f722cc * (2.0d - this.f722cc)) * this.mueff) / this.sigma));
        }
        return hsig;
    }

    private void updateCovarianceDiagonalOnly(boolean hsig, RealMatrix bestArz) {
        this.diagC = this.diagC.scalarMultiply((hsig ? 0.0d : this.ccov1Sep * this.f722cc * (2.0d - this.f722cc)) + ((1.0d - this.ccov1Sep) - this.ccovmuSep)).add(square(this.f725pc).scalarMultiply(this.ccov1Sep)).add(times(this.diagC, square(bestArz).multiply(this.weights)).scalarMultiply(this.ccovmuSep));
        this.diagD = sqrt(this.diagC);
        if (this.diagonalOnly > 1 && this.iterations > this.diagonalOnly) {
            this.diagonalOnly = 0;
            this.f718B = eye(this.dimension, this.dimension);
            this.f719BD = diag(this.diagD);
            this.f720C = diag(this.diagC);
        }
    }

    private void updateCovariance(boolean hsig, RealMatrix bestArx, RealMatrix arz, int[] arindex, RealMatrix xold) {
        double negccov;
        double oldFac = 0.0d;
        if (this.ccov1 + this.ccovmu > 0.0d) {
            RealMatrix realMatrix = bestArx;
            RealMatrix arpos = realMatrix.subtract(repmat(xold, 1, this.f724mu)).scalarMultiply(1.0d / this.sigma);
            RealMatrix roneu = this.f725pc.multiply(this.f725pc.transpose()).scalarMultiply(this.ccov1);
            if (!hsig) {
                oldFac = this.ccov1 * this.f722cc * (2.0d - this.f722cc);
            }
            double oldFac2 = oldFac + ((1.0d - this.ccov1) - this.ccovmu);
            if (this.isActiveCMA) {
                double oldFac3 = oldFac2;
                negccov = (((1.0d - this.ccovmu) * 0.25d) * this.mueff) / (FastMath.pow((double) (this.dimension + 2), 1.5d) + (this.mueff * 2.0d));
                int[] arReverseIndex = reverse(arindex);
                RealMatrix arzneg = selectColumns(arz, MathArrays.copyOf(arReverseIndex, this.f724mu));
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
                RealMatrix artmp = this.f719BD.multiply(arzneg2);
                RealMatrix realMatrix3 = arzneg2;
                RealMatrix Cneg = artmp.multiply(diag(this.weights)).multiply(artmp.transpose());
                RealMatrix realMatrix4 = artmp;
                RealMatrix realMatrix5 = arnormsReverse;
                double oldFac4 = oldFac3 + (negccov * 0.5d);
                double d = oldFac4;
                RealMatrix realMatrix6 = arnormsInv;
                int[] iArr4 = arReverseIndex;
                this.f720C = this.f720C.scalarMultiply(oldFac4).add(roneu).add(arpos.scalarMultiply(this.ccovmu + (0.5d * negccov)).multiply(times(repmat(this.weights, 1, this.dimension), arpos.transpose()))).subtract(Cneg.scalarMultiply(negccov));
                updateBD(negccov);
            }
            RealMatrix realMatrix7 = arz;
            double d2 = oldFac2;
            this.f720C = this.f720C.scalarMultiply(oldFac2).add(roneu).add(arpos.scalarMultiply(this.ccovmu).multiply(times(repmat(this.weights, 1, this.dimension), arpos.transpose())));
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
            this.f720C = triu(this.f720C, 0).add(triu(this.f720C, 1).transpose());
            EigenDecomposition eig = new EigenDecomposition(this.f720C);
            this.f718B = eig.getV();
            this.f721D = eig.getD();
            this.diagD = diag(this.f721D);
            if (min(this.diagD) <= 0.0d) {
                for (int i = 0; i < this.dimension; i++) {
                    if (this.diagD.getEntry(i, 0) < 0.0d) {
                        this.diagD.setEntry(i, 0, 0.0d);
                    }
                }
                double tfac = max(this.diagD) / 1.0E14d;
                this.f720C = this.f720C.add(eye(this.dimension, this.dimension).scalarMultiply(tfac));
                this.diagD = this.diagD.add(ones(this.dimension, 1).scalarMultiply(tfac));
            }
            if (max(this.diagD) > min(this.diagD) * 1.0E14d) {
                double tfac2 = (max(this.diagD) / 1.0E14d) - min(this.diagD);
                this.f720C = this.f720C.add(eye(this.dimension, this.dimension).scalarMultiply(tfac2));
                this.diagD = this.diagD.add(ones(this.dimension, 1).scalarMultiply(tfac2));
            }
            this.diagC = diag(this.f720C);
            this.diagD = sqrt(this.diagD);
            this.f719BD = times(this.f718B, repmat(this.diagD.transpose(), this.dimension, 1));
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

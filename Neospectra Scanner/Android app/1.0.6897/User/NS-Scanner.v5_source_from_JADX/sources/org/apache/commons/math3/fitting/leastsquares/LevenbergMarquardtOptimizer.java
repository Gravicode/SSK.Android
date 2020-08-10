package org.apache.commons.math3.fitting.leastsquares;

import java.util.Arrays;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer.Optimum;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem.Evaluation;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Incrementor;
import org.apache.commons.math3.util.Precision;

public class LevenbergMarquardtOptimizer implements LeastSquaresOptimizer {
    private static final double TWO_EPS = (Precision.EPSILON * 2.0d);
    private final double costRelativeTolerance;
    private final double initialStepBoundFactor;
    private final double orthoTolerance;
    private final double parRelativeTolerance;
    private final double qrRankingThreshold;

    private static class InternalData {
        /* access modifiers changed from: private */
        public final double[] beta;
        /* access modifiers changed from: private */
        public final double[] diagR;
        /* access modifiers changed from: private */
        public final double[] jacNorm;
        /* access modifiers changed from: private */
        public final int[] permutation;
        /* access modifiers changed from: private */
        public final int rank;
        /* access modifiers changed from: private */
        public final double[][] weightedJacobian;

        InternalData(double[][] weightedJacobian2, int[] permutation2, int rank2, double[] diagR2, double[] jacNorm2, double[] beta2) {
            this.weightedJacobian = weightedJacobian2;
            this.permutation = permutation2;
            this.rank = rank2;
            this.diagR = diagR2;
            this.jacNorm = jacNorm2;
            this.beta = beta2;
        }
    }

    public LevenbergMarquardtOptimizer() {
        this(100.0d, 1.0E-10d, 1.0E-10d, 1.0E-10d, Precision.SAFE_MIN);
    }

    public LevenbergMarquardtOptimizer(double initialStepBoundFactor2, double costRelativeTolerance2, double parRelativeTolerance2, double orthoTolerance2, double qrRankingThreshold2) {
        this.initialStepBoundFactor = initialStepBoundFactor2;
        this.costRelativeTolerance = costRelativeTolerance2;
        this.parRelativeTolerance = parRelativeTolerance2;
        this.orthoTolerance = orthoTolerance2;
        this.qrRankingThreshold = qrRankingThreshold2;
    }

    public LevenbergMarquardtOptimizer withInitialStepBoundFactor(double newInitialStepBoundFactor) {
        LevenbergMarquardtOptimizer levenbergMarquardtOptimizer = new LevenbergMarquardtOptimizer(newInitialStepBoundFactor, this.costRelativeTolerance, this.parRelativeTolerance, this.orthoTolerance, this.qrRankingThreshold);
        return levenbergMarquardtOptimizer;
    }

    public LevenbergMarquardtOptimizer withCostRelativeTolerance(double newCostRelativeTolerance) {
        LevenbergMarquardtOptimizer levenbergMarquardtOptimizer = new LevenbergMarquardtOptimizer(this.initialStepBoundFactor, newCostRelativeTolerance, this.parRelativeTolerance, this.orthoTolerance, this.qrRankingThreshold);
        return levenbergMarquardtOptimizer;
    }

    public LevenbergMarquardtOptimizer withParameterRelativeTolerance(double newParRelativeTolerance) {
        LevenbergMarquardtOptimizer levenbergMarquardtOptimizer = new LevenbergMarquardtOptimizer(this.initialStepBoundFactor, this.costRelativeTolerance, newParRelativeTolerance, this.orthoTolerance, this.qrRankingThreshold);
        return levenbergMarquardtOptimizer;
    }

    public LevenbergMarquardtOptimizer withOrthoTolerance(double newOrthoTolerance) {
        LevenbergMarquardtOptimizer levenbergMarquardtOptimizer = new LevenbergMarquardtOptimizer(this.initialStepBoundFactor, this.costRelativeTolerance, this.parRelativeTolerance, newOrthoTolerance, this.qrRankingThreshold);
        return levenbergMarquardtOptimizer;
    }

    public LevenbergMarquardtOptimizer withRankingThreshold(double newQRRankingThreshold) {
        LevenbergMarquardtOptimizer levenbergMarquardtOptimizer = new LevenbergMarquardtOptimizer(this.initialStepBoundFactor, this.costRelativeTolerance, this.parRelativeTolerance, this.orthoTolerance, newQRRankingThreshold);
        return levenbergMarquardtOptimizer;
    }

    public double getInitialStepBoundFactor() {
        return this.initialStepBoundFactor;
    }

    public double getCostRelativeTolerance() {
        return this.costRelativeTolerance;
    }

    public double getParameterRelativeTolerance() {
        return this.parRelativeTolerance;
    }

    public double getOrthoTolerance() {
        return this.orthoTolerance;
    }

    public double getRankingThreshold() {
        return this.qrRankingThreshold;
    }

    public Optimum optimize(LeastSquaresProblem problem) {
        double[] currentResiduals;
        InternalData internalData;
        int nR;
        Incrementor evaluationCounter;
        Evaluation current;
        ConvergenceChecker convergenceChecker;
        double currentCost;
        Evaluation previous;
        int solvedCols;
        Incrementor iterationCounter;
        int nC;
        double[] qtf;
        boolean firstIteration;
        double xNorm;
        double d;
        LevenbergMarquardtOptimizer levenbergMarquardtOptimizer = this;
        LeastSquaresProblem leastSquaresProblem = problem;
        int nR2 = problem.getObservationSize();
        int solvedCols2 = problem.getParameterSize();
        Incrementor iterationCounter2 = problem.getIterationCounter();
        Incrementor evaluationCounter2 = problem.getEvaluationCounter();
        ConvergenceChecker<Evaluation> checker = problem.getConvergenceChecker();
        int solvedCols3 = FastMath.min(nR2, solvedCols2);
        double xNorm2 = 0.0d;
        double[] diag = new double[solvedCols2];
        ConvergenceChecker convergenceChecker2 = checker;
        double[] oldX = new double[solvedCols2];
        double[] oldRes = new double[nR2];
        double[] qtf2 = new double[nR2];
        double[] lmDir = new double[solvedCols2];
        double[] work1 = new double[solvedCols2];
        double[] work2 = new double[solvedCols2];
        double[] work3 = new double[solvedCols2];
        evaluationCounter2.incrementCount();
        double[] oldRes2 = oldRes;
        Evaluation current2 = leastSquaresProblem.evaluate(problem.getStart());
        double delta = 0.0d;
        double[] currentResiduals2 = current2.getResiduals().toArray();
        double currentCost2 = current2.getCost();
        double lmPar = 0.0d;
        double[] currentPoint = current2.getPoint().toArray();
        double[] currentPoint2 = currentResiduals2;
        Evaluation current3 = current2;
        boolean firstIteration2 = true;
        loop0:
        while (true) {
            iterationCounter2.incrementCount();
            Evaluation previous2 = current3;
            InternalData internalData2 = levenbergMarquardtOptimizer.qrDecomposition(current3.getJacobian(), solvedCols3);
            double[][] weightedJacobian = internalData2.weightedJacobian;
            int[] permutation = internalData2.permutation;
            double[] diagR = internalData2.diagR;
            double[] jacNorm = internalData2.jacNorm;
            double[] weightedResidual = currentPoint2;
            int i = 0;
            while (true) {
                currentResiduals = currentPoint2;
                int i2 = i;
                if (i2 >= nR2) {
                    break;
                }
                qtf2[i2] = weightedResidual[i2];
                i = i2 + 1;
                currentPoint2 = currentResiduals;
            }
            levenbergMarquardtOptimizer.qTy(qtf2, internalData2);
            for (int k = 0; k < solvedCols3; k++) {
                int pk = permutation[k];
                weightedJacobian[k][pk] = diagR[pk];
            }
            if (firstIteration2) {
                double xNorm3 = 0.0d;
                for (int k2 = 0; k2 < solvedCols2; k2++) {
                    double dk = jacNorm[k2];
                    if (dk == 0.0d) {
                        dk = 1.0d;
                    }
                    double xk = currentPoint[k2] * dk;
                    xNorm3 += xk * xk;
                    diag[k2] = dk;
                }
                xNorm2 = FastMath.sqrt(xNorm3);
                if (xNorm2 == 0.0d) {
                    internalData = internalData2;
                    nR = nR2;
                    d = levenbergMarquardtOptimizer.initialStepBoundFactor;
                } else {
                    internalData = internalData2;
                    nR = nR2;
                    d = levenbergMarquardtOptimizer.initialStepBoundFactor * xNorm2;
                }
                delta = d;
            } else {
                internalData = internalData2;
                nR = nR2;
            }
            double maxCosine = 0.0d;
            if (currentCost2 != 0.0d) {
                int j = 0;
                while (j < solvedCols3) {
                    int pj = permutation[j];
                    double s = jacNorm[pj];
                    if (s != 0.0d) {
                        xNorm = xNorm2;
                        double sum = 0.0d;
                        int i3 = 0;
                        while (true) {
                            firstIteration = firstIteration2;
                            int i4 = i3;
                            if (i4 > j) {
                                break;
                            }
                            sum += weightedJacobian[i4][pj] * qtf2[i4];
                            i3 = i4 + 1;
                            firstIteration2 = firstIteration;
                        }
                        qtf = qtf2;
                        maxCosine = FastMath.max(maxCosine, FastMath.abs(sum) / (s * currentCost2));
                    } else {
                        firstIteration = firstIteration2;
                        qtf = qtf2;
                        xNorm = xNorm2;
                    }
                    j++;
                    xNorm2 = xNorm;
                    firstIteration2 = firstIteration;
                    qtf2 = qtf;
                }
            }
            boolean firstIteration3 = firstIteration2;
            double[] qtf3 = qtf2;
            double xNorm4 = xNorm2;
            if (maxCosine <= levenbergMarquardtOptimizer.orthoTolerance) {
                return new OptimumImpl(current3, evaluationCounter2.getCount(), iterationCounter2.getCount());
            }
            int j2 = 0;
            while (j2 < solvedCols2) {
                Evaluation current4 = current3;
                diag[j2] = FastMath.max(diag[j2], jacNorm[j2]);
                j2++;
                current3 = current4;
            }
            Evaluation current5 = current3;
            double[] currentPoint3 = currentPoint;
            double delta2 = delta;
            double ratio = 0.0d;
            while (ratio < 1.0E-4d) {
                for (int j3 = 0; j3 < solvedCols3; j3++) {
                    int pj2 = permutation[j3];
                    oldX[pj2] = currentPoint3[pj2];
                }
                double previousCost = currentCost2;
                double[] tmpVec = weightedResidual;
                weightedResidual = oldRes2;
                oldRes2 = tmpVec;
                double[] qtf4 = qtf3;
                double maxCosine2 = maxCosine;
                double[] currentPoint4 = currentPoint3;
                double delta3 = delta2;
                double[] diag2 = diag;
                double[] work32 = work3;
                int solvedCols4 = solvedCols3;
                ConvergenceChecker convergenceChecker3 = convergenceChecker2;
                double[] oldX2 = oldX;
                Incrementor evaluationCounter3 = evaluationCounter2;
                int nC2 = solvedCols2;
                Incrementor iterationCounter3 = iterationCounter2;
                double lmPar2 = levenbergMarquardtOptimizer.determineLMParameter(qtf4, delta2, diag, internalData, solvedCols3, work1, work2, work32, lmDir, lmPar);
                double lmNorm = 0.0d;
                for (int j4 = 0; j4 < solvedCols4; j4++) {
                    int pj3 = permutation[j4];
                    lmDir[pj3] = -lmDir[pj3];
                    currentPoint4[pj3] = oldX2[pj3] + lmDir[pj3];
                    double s2 = diag2[pj3] * lmDir[pj3];
                    lmNorm += s2 * s2;
                }
                double lmNorm2 = FastMath.sqrt(lmNorm);
                if (firstIteration3) {
                    delta2 = FastMath.min(delta3, lmNorm2);
                } else {
                    delta2 = delta3;
                }
                evaluationCounter = evaluationCounter3;
                evaluationCounter.incrementCount();
                current = problem.evaluate(new ArrayRealVector(currentPoint4));
                currentResiduals = current.getResiduals().toArray();
                double currentCost3 = current.getCost();
                double[] currentPoint5 = current.getPoint().toArray();
                double actRed = -1.0d;
                if (currentCost3 * 0.1d < previousCost) {
                    double r = currentCost3 / previousCost;
                    actRed = 1.0d - (r * r);
                }
                double currentCost4 = currentCost3;
                double actRed2 = actRed;
                int j5 = 0;
                while (j5 < solvedCols4) {
                    int pj4 = permutation[j5];
                    double dirJ = lmDir[pj4];
                    work1[j5] = 0.0d;
                    int i5 = 0;
                    while (true) {
                        int i6 = i5;
                        if (i6 > j5) {
                            break;
                        }
                        work1[i6] = work1[i6] + (weightedJacobian[i6][pj4] * dirJ);
                        i5 = i6 + 1;
                        LeastSquaresProblem leastSquaresProblem2 = problem;
                    }
                    j5++;
                    LeastSquaresProblem leastSquaresProblem3 = problem;
                }
                double coeff1 = 0.0d;
                for (int j6 = 0; j6 < solvedCols4; j6++) {
                    coeff1 += work1[j6] * work1[j6];
                }
                double pc2 = previousCost * previousCost;
                double coeff12 = coeff1 / pc2;
                double coeff2 = ((lmPar2 * lmNorm2) * lmNorm2) / pc2;
                double preRed = coeff12 + (coeff2 * 2.0d);
                int solvedCols5 = solvedCols4;
                double dirDer = -(coeff12 + coeff2);
                double ratio2 = preRed == 0.0d ? 0.0d : actRed2 / preRed;
                double tmp = 0.5d;
                if (ratio2 <= 0.25d) {
                    if (actRed2 < 0.0d) {
                        tmp = (dirDer * 0.5d) / (dirDer + (0.5d * actRed2));
                    }
                    if (currentCost4 * 0.1d >= previousCost || tmp < 0.1d) {
                        tmp = 0.1d;
                    }
                    double d2 = dirDer;
                    lmPar = lmPar2 / tmp;
                    delta2 = FastMath.min(delta2, lmNorm2 * 10.0d) * tmp;
                } else {
                    if (lmPar2 == 0.0d || ratio2 >= 0.75d) {
                        delta2 = lmNorm2 * 2.0d;
                        lmPar2 *= 0.5d;
                    }
                    lmPar = lmPar2;
                }
                if (ratio2 >= 1.0E-4d) {
                    firstIteration3 = false;
                    double xNorm5 = 0.0d;
                    int k3 = 0;
                    while (true) {
                        nC = nC2;
                        if (k3 >= nC) {
                            break;
                        }
                        double xK = diag2[k3] * currentPoint5[k3];
                        xNorm5 += xK * xK;
                        k3++;
                        nC2 = nC;
                    }
                    xNorm4 = FastMath.sqrt(xNorm5);
                    ConvergenceChecker convergenceChecker4 = convergenceChecker3;
                    if (convergenceChecker4 != null) {
                        iterationCounter = iterationCounter3;
                        previous = previous2;
                        if (convergenceChecker4.converged(iterationCounter.getCount(), previous, current)) {
                            ConvergenceChecker convergenceChecker5 = convergenceChecker4;
                            return new OptimumImpl(current, evaluationCounter.getCount(), iterationCounter.getCount());
                        }
                        convergenceChecker = convergenceChecker4;
                    } else {
                        convergenceChecker = convergenceChecker4;
                        previous = previous2;
                        iterationCounter = iterationCounter3;
                    }
                    currentCost = currentCost4;
                    solvedCols = solvedCols5;
                } else {
                    previous = previous2;
                    convergenceChecker = convergenceChecker3;
                    nC = nC2;
                    iterationCounter = iterationCounter3;
                    currentCost = previousCost;
                    int j7 = 0;
                    while (true) {
                        solvedCols = solvedCols5;
                        if (j7 >= solvedCols) {
                            break;
                        }
                        int pj5 = permutation[j7];
                        currentPoint5[pj5] = oldX2[pj5];
                        j7++;
                        solvedCols5 = solvedCols;
                    }
                    current = previous;
                    weightedResidual = oldRes2;
                    oldRes2 = weightedResidual;
                }
                int nC3 = nC;
                double d3 = lmNorm2;
                if ((FastMath.abs(actRed2) > this.costRelativeTolerance || preRed > this.costRelativeTolerance || ratio2 > 2.0d) && delta2 > this.parRelativeTolerance * xNorm4) {
                    if (FastMath.abs(actRed2) > TWO_EPS || preRed > TWO_EPS || ratio2 > 2.0d) {
                        Incrementor evaluationCounter4 = evaluationCounter;
                        Evaluation current6 = current;
                        if (delta2 <= TWO_EPS * xNorm4) {
                            throw new ConvergenceException(LocalizedFormats.TOO_SMALL_PARAMETERS_RELATIVE_TOLERANCE, Double.valueOf(this.parRelativeTolerance));
                        } else if (maxCosine2 <= TWO_EPS) {
                            throw new ConvergenceException(LocalizedFormats.TOO_SMALL_ORTHOGONALITY_TOLERANCE, Double.valueOf(this.orthoTolerance));
                        } else {
                            currentPoint3 = currentPoint5;
                            diag = diag2;
                            oldX = oldX2;
                            currentCost2 = currentCost;
                            qtf3 = qtf4;
                            work3 = work32;
                            maxCosine = maxCosine2;
                            convergenceChecker2 = convergenceChecker;
                            evaluationCounter2 = evaluationCounter4;
                            current5 = current6;
                            iterationCounter2 = iterationCounter;
                            solvedCols3 = solvedCols;
                            previous2 = previous;
                            solvedCols2 = nC3;
                            levenbergMarquardtOptimizer = this;
                            ratio = ratio2;
                        }
                    } else {
                        Incrementor incrementor = evaluationCounter;
                        Evaluation evaluation = current;
                        throw new ConvergenceException(LocalizedFormats.TOO_SMALL_COST_RELATIVE_TOLERANCE, Double.valueOf(this.costRelativeTolerance));
                    }
                }
            }
            Object obj = diag;
            double[] dArr = work3;
            Incrementor incrementor2 = evaluationCounter2;
            int nC4 = solvedCols2;
            LevenbergMarquardtOptimizer levenbergMarquardtOptimizer2 = levenbergMarquardtOptimizer;
            ConvergenceChecker convergenceChecker6 = convergenceChecker2;
            int solvedCols6 = solvedCols3;
            double[] dArr2 = oldX;
            Incrementor incrementor3 = iterationCounter2;
            delta = delta2;
            solvedCols3 = solvedCols6;
            currentPoint = currentPoint3;
            currentPoint2 = currentResiduals;
            qtf2 = qtf3;
            nR2 = nR;
            xNorm2 = xNorm4;
            firstIteration2 = firstIteration3;
            current3 = current5;
            convergenceChecker2 = convergenceChecker6;
            solvedCols2 = nC4;
        }
        return new OptimumImpl(current, evaluationCounter.getCount(), iterationCounter.getCount());
    }

    private double determineLMParameter(double[] qy, double delta, double[] diag, InternalData internalData, int solvedCols, double[] work1, double[] work2, double[] work3, double[] lmDir, double lmPar) {
        int nC;
        double parl;
        double parl2;
        double parl3;
        double lmPar2;
        double lmPar3;
        double previousFP;
        int rank;
        int nC2;
        double d = delta;
        int i = solvedCols;
        double[][] weightedJacobian = internalData.weightedJacobian;
        int[] permutation = internalData.permutation;
        int i2 = internalData.rank;
        double[] diagR = internalData.diagR;
        int i3 = weightedJacobian[0].length;
        for (int j = 0; j < i2; j++) {
            lmDir[permutation[j]] = qy[j];
        }
        for (int j2 = i2; j2 < i3; j2++) {
            lmDir[permutation[j2]] = 0.0d;
        }
        for (int k = i2 - 1; k >= 0; k--) {
            int pk = permutation[k];
            double ypk = lmDir[pk] / diagR[pk];
            for (int i4 = 0; i4 < k; i4++) {
                int i5 = permutation[i4];
                lmDir[i5] = lmDir[i5] - (weightedJacobian[i4][pk] * ypk);
            }
            lmDir[pk] = ypk;
        }
        double dxNorm = 0.0d;
        for (int j3 = 0; j3 < i; j3++) {
            int pj = permutation[j3];
            double s = diag[pj] * lmDir[pj];
            work1[pj] = s;
            dxNorm += s * s;
        }
        double dxNorm2 = FastMath.sqrt(dxNorm);
        double fp = dxNorm2 - d;
        if (fp <= d * 0.1d) {
            return 0.0d;
        }
        if (i2 == i) {
            int j4 = 0;
            while (true) {
                int j5 = j4;
                if (j5 >= i) {
                    break;
                }
                int pj2 = permutation[j5];
                work1[pj2] = work1[pj2] * (diag[pj2] / dxNorm2);
                j4 = j5 + 1;
            }
            double sum2 = 0.0d;
            int j6 = 0;
            while (j6 < i) {
                int pj3 = permutation[j6];
                double sum = 0.0d;
                int i6 = 0;
                while (true) {
                    nC2 = i3;
                    int i7 = i6;
                    if (i7 >= j6) {
                        break;
                    }
                    sum += weightedJacobian[i7][pj3] * work1[permutation[i7]];
                    i6 = i7 + 1;
                    i3 = nC2;
                }
                double s2 = (work1[pj3] - sum) / diagR[pj3];
                work1[pj3] = s2;
                sum2 += s2 * s2;
                j6++;
                i3 = nC2;
            }
            nC = i3;
            parl = fp / (d * sum2);
        } else {
            nC = i3;
            parl = 0.0d;
        }
        double fp2 = fp;
        double sum22 = 0.0d;
        int j7 = 0;
        while (j7 < i) {
            int pj4 = permutation[j7];
            double sum3 = 0.0d;
            int i8 = 0;
            while (true) {
                rank = i2;
                int i9 = i8;
                if (i9 > j7) {
                    break;
                }
                sum3 += weightedJacobian[i9][pj4] * qy[i9];
                i8 = i9 + 1;
                i2 = rank;
            }
            double sum4 = sum3 / diag[pj4];
            sum22 += sum4 * sum4;
            j7++;
            i2 = rank;
        }
        int rank2 = i2;
        double gNorm = FastMath.sqrt(sum22);
        double paru = gNorm / d;
        if (paru == 0.0d) {
            double d2 = paru;
            paru = Precision.SAFE_MIN / FastMath.min(d, 0.1d);
        } else {
            double d3 = paru;
        }
        double d4 = sum22;
        double lmPar4 = FastMath.min(paru, FastMath.max(lmPar, parl));
        if (lmPar4 == 0.0d) {
            lmPar4 = gNorm / dxNorm2;
        }
        int countdown = 10;
        double d5 = dxNorm2;
        double d6 = parl;
        double paru2 = paru;
        double paru3 = d6;
        while (countdown >= 0) {
            if (lmPar4 == 0.0d) {
                parl2 = paru3;
                parl3 = FastMath.max(Precision.SAFE_MIN, paru2 * 0.001d);
            } else {
                parl2 = paru3;
                parl3 = lmPar4;
            }
            double sPar = FastMath.sqrt(parl3);
            for (int j8 = 0; j8 < i; j8++) {
                int pj5 = permutation[j8];
                work1[pj5] = diag[pj5] * sPar;
            }
            int countdown2 = countdown;
            double lmPar5 = parl3;
            double paru4 = paru2;
            int nC3 = nC;
            double parl4 = parl2;
            int rank3 = rank2;
            determineLMDirection(qy, work1, work2, internalData, i, work3, lmDir);
            double dxNorm3 = 0.0d;
            for (int j9 = 0; j9 < i; j9++) {
                int pj6 = permutation[j9];
                double s3 = diag[pj6] * lmDir[pj6];
                work3[pj6] = s3;
                dxNorm3 += s3 * s3;
            }
            double dxNorm4 = FastMath.sqrt(dxNorm3);
            double previousFP2 = fp2;
            double d7 = delta;
            double fp3 = dxNorm4 - d7;
            if (FastMath.abs(fp3) <= d7 * 0.1d) {
                double d8 = previousFP2;
                double d9 = fp3;
                double d10 = paru4;
                lmPar2 = lmPar5;
            } else if (parl4 != 0.0d || fp3 > previousFP2 || previousFP2 >= 0.0d) {
                for (int j10 = 0; j10 < i; j10++) {
                    int pj7 = permutation[j10];
                    work1[pj7] = (work3[pj7] * diag[pj7]) / dxNorm4;
                }
                int j11 = 0;
                while (j11 < i) {
                    int pj8 = permutation[j11];
                    work1[pj8] = work1[pj8] / work2[j11];
                    double tmp = work1[pj8];
                    int i10 = j11 + 1;
                    while (true) {
                        previousFP = previousFP2;
                        int i11 = i10;
                        if (i11 >= i) {
                            break;
                        }
                        int i12 = permutation[i11];
                        work1[i12] = work1[i12] - (weightedJacobian[i11][pj8] * tmp);
                        i10 = i11 + 1;
                        previousFP2 = previousFP;
                    }
                    j11++;
                    previousFP2 = previousFP;
                }
                double sum23 = 0.0d;
                for (int j12 = 0; j12 < i; j12++) {
                    double s4 = work1[permutation[j12]];
                    sum23 += s4 * s4;
                }
                double correction = fp3 / (d7 * sum23);
                if (fp3 > 0.0d) {
                    lmPar3 = lmPar5;
                    parl4 = FastMath.max(parl4, lmPar3);
                    paru2 = paru4;
                } else {
                    lmPar3 = lmPar5;
                    paru2 = fp3 < 0.0d ? FastMath.min(paru4, lmPar3) : paru4;
                }
                double fp4 = fp3;
                lmPar4 = FastMath.max(parl4, lmPar3 + correction);
                paru3 = parl4;
                nC = nC3;
                rank2 = rank3;
                fp2 = fp4;
                countdown = countdown2 - 1;
            } else {
                double d11 = previousFP2;
                double d12 = fp3;
                double d13 = paru4;
                lmPar2 = lmPar5;
            }
            return lmPar2;
        }
        double parl5 = paru3;
        int i13 = nC;
        int i14 = rank2;
        return lmPar4;
    }

    private void determineLMDirection(double[] qy, double[] diag, double[] lmDiag, InternalData internalData, int solvedCols, double[] work, double[] lmDir) {
        double[] diagR;
        double dpj;
        int pj;
        double sin;
        double cotan;
        double qtbpj;
        double[] dArr = lmDiag;
        int i = solvedCols;
        double[] dArr2 = lmDir;
        int[] permutation = internalData.permutation;
        double[][] weightedJacobian = internalData.weightedJacobian;
        double[] diagR2 = internalData.diagR;
        for (int j = 0; j < i; j++) {
            int pj2 = permutation[j];
            for (int i2 = j + 1; i2 < i; i2++) {
                weightedJacobian[i2][pj2] = weightedJacobian[j][permutation[i2]];
            }
            dArr2[j] = diagR2[pj2];
            work[j] = qy[j];
        }
        int j2 = 0;
        while (true) {
            double d = 0.0d;
            if (j2 >= i) {
                break;
            }
            int pj3 = permutation[j2];
            double dpj2 = diag[pj3];
            if (dpj2 != 0.0d) {
                diagR = diagR2;
                Arrays.fill(dArr, j2 + 1, dArr.length, 0.0d);
            } else {
                diagR = diagR2;
            }
            dArr[j2] = dpj2;
            double qtbpj2 = 0.0d;
            int k = j2;
            while (k < i) {
                int pk = permutation[k];
                if (dArr[k] != d) {
                    double rkk = weightedJacobian[k][pk];
                    pj = pj3;
                    if (FastMath.abs(rkk) < FastMath.abs(dArr[k])) {
                        double cotan2 = rkk / dArr[k];
                        dpj = dpj2;
                        sin = 1.0d / FastMath.sqrt((cotan2 * cotan2) + 1.0d);
                        cotan = cotan2 * sin;
                    } else {
                        dpj = dpj2;
                        double tan = dArr[k] / rkk;
                        double cos = 1.0d / FastMath.sqrt((tan * tan) + 1.0d);
                        sin = cos * tan;
                        cotan = cos;
                    }
                    double sin2 = sin;
                    weightedJacobian[k][pk] = (cotan * rkk) + (dArr[k] * sin2);
                    double d2 = rkk;
                    double qtbpj3 = ((-sin2) * work[k]) + (cotan * qtbpj2);
                    work[k] = (work[k] * cotan) + (sin2 * qtbpj2);
                    int i3 = k + 1;
                    while (true) {
                        qtbpj = qtbpj3;
                        int i4 = i3;
                        if (i4 >= i) {
                            break;
                        }
                        double rik = weightedJacobian[i4][pk];
                        double temp2 = (cotan * rik) + (dArr[i4] * sin2);
                        dArr[i4] = ((-sin2) * rik) + (dArr[i4] * cotan);
                        weightedJacobian[i4][pk] = temp2;
                        i3 = i4 + 1;
                        qtbpj3 = qtbpj;
                        i = solvedCols;
                    }
                    qtbpj2 = qtbpj;
                } else {
                    pj = pj3;
                    dpj = dpj2;
                }
                k++;
                pj3 = pj;
                dpj2 = dpj;
                i = solvedCols;
                d = 0.0d;
            }
            double d3 = dpj2;
            dArr[j2] = weightedJacobian[j2][permutation[j2]];
            weightedJacobian[j2][permutation[j2]] = dArr2[j2];
            j2++;
            diagR2 = diagR;
            i = solvedCols;
        }
        int i5 = solvedCols;
        int nSing = i5;
        for (int j3 = 0; j3 < i5; j3++) {
            if (dArr[j3] == 0.0d && nSing == i5) {
                nSing = j3;
            }
            if (nSing < i5) {
                work[j3] = 0.0d;
            }
        }
        if (nSing > 0) {
            for (int j4 = nSing - 1; j4 >= 0; j4--) {
                int pj4 = permutation[j4];
                double sum = 0.0d;
                for (int i6 = j4 + 1; i6 < nSing; i6++) {
                    sum += weightedJacobian[i6][pj4] * work[i6];
                }
                work[j4] = (work[j4] - sum) / dArr[j4];
            }
        }
        int j5 = 0;
        while (true) {
            int j6 = j5;
            if (j6 < dArr2.length) {
                dArr2[permutation[j6]] = work[j6];
                j5 = j6 + 1;
            } else {
                return;
            }
        }
    }

    private InternalData qrDecomposition(RealMatrix jacobian, int solvedCols) throws ConvergenceException {
        double ak2;
        int nR;
        double[][] weightedJacobian = jacobian.scalarMultiply(-1.0d).getData();
        int nC = weightedJacobian[0].length;
        int[] permutation = new int[nC];
        double[] diagR = new double[nC];
        double[] jacNorm = new double[nC];
        double[] beta = new double[nC];
        for (int k = 0; k < nC; k++) {
            permutation[k] = k;
            double norm2 = 0.0d;
            for (double[] dArr : weightedJacobian) {
                double akk = dArr[k];
                norm2 += akk * akk;
            }
            jacNorm[k] = FastMath.sqrt(norm2);
        }
        int k2 = 0;
        loop2:
        while (true) {
            int k3 = k2;
            if (k3 < nC) {
                int nextColumn = -1;
                double ak22 = Double.NEGATIVE_INFINITY;
                int i = k3;
                while (i < nC) {
                    double norm22 = 0.0d;
                    for (int j = k3; j < nR; j++) {
                        double aki = weightedJacobian[j][permutation[i]];
                        norm22 += aki * aki;
                    }
                    if (Double.isInfinite(norm22) == 0 && !Double.isNaN(norm22)) {
                        if (norm22 > ak22) {
                            nextColumn = i;
                            ak22 = norm22;
                        }
                        i++;
                    }
                }
                if (ak22 <= this.qrRankingThreshold) {
                    int i2 = nR;
                    double d = ak22;
                    InternalData internalData = new InternalData(weightedJacobian, permutation, k3, diagR, jacNorm, beta);
                    return internalData;
                }
                int nR2 = nR;
                double ak23 = ak22;
                int pk = permutation[nextColumn];
                permutation[nextColumn] = permutation[k3];
                permutation[k3] = pk;
                double akk2 = weightedJacobian[k3][pk];
                double alpha = akk2 > 0.0d ? -FastMath.sqrt(ak23) : FastMath.sqrt(ak23);
                double betak = 1.0d / (ak23 - (akk2 * alpha));
                beta[pk] = betak;
                diagR[pk] = alpha;
                double[] dArr2 = weightedJacobian[k3];
                dArr2[pk] = dArr2[pk] - alpha;
                int dk = (nC - 1) - k3;
                while (dk > 0) {
                    double gamma = 0.0d;
                    int j2 = k3;
                    while (true) {
                        ak2 = ak23;
                        nR = nR2;
                        if (j2 >= nR) {
                            break;
                        }
                        gamma += weightedJacobian[j2][pk] * weightedJacobian[j2][permutation[k3 + dk]];
                        j2++;
                        nR2 = nR;
                        ak23 = ak2;
                    }
                    double gamma2 = gamma * betak;
                    for (int j3 = k3; j3 < nR; j3++) {
                        double[] dArr3 = weightedJacobian[j3];
                        int i3 = permutation[k3 + dk];
                        dArr3[i3] = dArr3[i3] - (weightedJacobian[j3][pk] * gamma2);
                    }
                    dk--;
                    nR2 = nR;
                    ak23 = ak2;
                }
                nR = nR2;
                k2 = k3 + 1;
                RealMatrix realMatrix = jacobian;
            } else {
                InternalData internalData2 = new InternalData(weightedJacobian, permutation, solvedCols, diagR, jacNorm, beta);
                return internalData2;
            }
        }
        throw new ConvergenceException(LocalizedFormats.UNABLE_TO_PERFORM_QR_DECOMPOSITION_ON_JACOBIAN, Integer.valueOf(nR), Integer.valueOf(nC));
    }

    private void qTy(double[] y, InternalData internalData) {
        double[][] weightedJacobian = internalData.weightedJacobian;
        int[] permutation = internalData.permutation;
        double[] beta = internalData.beta;
        int nR = weightedJacobian.length;
        int nC = weightedJacobian[0].length;
        for (int k = 0; k < nC; k++) {
            int pk = permutation[k];
            double gamma = 0.0d;
            for (int i = k; i < nR; i++) {
                gamma += weightedJacobian[i][pk] * y[i];
            }
            double gamma2 = gamma * beta[pk];
            for (int i2 = k; i2 < nR; i2++) {
                y[i2] = y[i2] - (weightedJacobian[i2][pk] * gamma2);
            }
        }
    }
}

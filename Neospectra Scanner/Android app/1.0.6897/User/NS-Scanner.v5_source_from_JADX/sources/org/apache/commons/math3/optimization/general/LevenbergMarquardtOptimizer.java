package org.apache.commons.math3.optimization.general;

import java.util.Arrays;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

@Deprecated
public class LevenbergMarquardtOptimizer extends AbstractLeastSquaresOptimizer {
    private double[] beta;
    private final double costRelativeTolerance;
    private double[] diagR;
    private final double initialStepBoundFactor;
    private double[] jacNorm;
    private double[] lmDir;
    private double lmPar;
    private final double orthoTolerance;
    private final double parRelativeTolerance;
    private int[] permutation;
    private final double qrRankingThreshold;
    private int rank;
    private int solvedCols;
    private double[][] weightedJacobian;
    private double[] weightedResidual;

    public LevenbergMarquardtOptimizer() {
        this(100.0d, 1.0E-10d, 1.0E-10d, 1.0E-10d, Precision.SAFE_MIN);
    }

    public LevenbergMarquardtOptimizer(ConvergenceChecker<PointVectorValuePair> checker) {
        this(100.0d, checker, 1.0E-10d, 1.0E-10d, 1.0E-10d, Precision.SAFE_MIN);
    }

    public LevenbergMarquardtOptimizer(double initialStepBoundFactor2, ConvergenceChecker<PointVectorValuePair> checker, double costRelativeTolerance2, double parRelativeTolerance2, double orthoTolerance2, double threshold) {
        super(checker);
        this.initialStepBoundFactor = initialStepBoundFactor2;
        this.costRelativeTolerance = costRelativeTolerance2;
        this.parRelativeTolerance = parRelativeTolerance2;
        this.orthoTolerance = orthoTolerance2;
        this.qrRankingThreshold = threshold;
    }

    public LevenbergMarquardtOptimizer(double costRelativeTolerance2, double parRelativeTolerance2, double orthoTolerance2) {
        this(100.0d, costRelativeTolerance2, parRelativeTolerance2, orthoTolerance2, Precision.SAFE_MIN);
    }

    public LevenbergMarquardtOptimizer(double initialStepBoundFactor2, double costRelativeTolerance2, double parRelativeTolerance2, double orthoTolerance2, double threshold) {
        super(null);
        this.initialStepBoundFactor = initialStepBoundFactor2;
        this.costRelativeTolerance = costRelativeTolerance2;
        this.parRelativeTolerance = parRelativeTolerance2;
        this.orthoTolerance = orthoTolerance2;
        this.qrRankingThreshold = threshold;
    }

    /* access modifiers changed from: protected */
    public PointVectorValuePair doOptimize() {
        double[] work1;
        double[] currentPoint;
        int nR;
        boolean firstIteration;
        RealMatrix weightMatrixSqrt;
        double[] work2;
        double d;
        double maxCosine;
        double currentCost;
        double[] currentResiduals;
        double[] currentObjective;
        double delta;
        int iter;
        ConvergenceChecker convergenceChecker;
        PointVectorValuePair current;
        PointVectorValuePair previous;
        double[] currentPoint2;
        int iter2;
        double[] work12;
        double[] currentPoint3;
        int nR2;
        int nR3 = getTarget().length;
        double[] currentPoint4 = getStartPoint();
        int nC = currentPoint4.length;
        this.solvedCols = FastMath.min(nR3, nC);
        this.diagR = new double[nC];
        this.jacNorm = new double[nC];
        this.beta = new double[nC];
        this.permutation = new int[nC];
        this.lmDir = new double[nC];
        double[] diag = new double[nC];
        double[] oldX = new double[nC];
        double[] oldRes = new double[nR3];
        double[] oldObj = new double[nR3];
        double[] qtf = new double[nR3];
        double[] work13 = new double[nC];
        double[] work22 = new double[nC];
        double[] work3 = new double[nC];
        RealMatrix weightMatrixSqrt2 = getWeightSquareRoot();
        double delta2 = 0.0d;
        double[] currentObjective2 = computeObjectiveValue(currentPoint4);
        double[] currentResiduals2 = computeResiduals(currentObjective2);
        double xNorm = 0.0d;
        PointVectorValuePair current2 = new PointVectorValuePair(currentPoint4, currentObjective2);
        double currentCost2 = computeCost(currentResiduals2);
        double[] oldRes2 = oldRes;
        double[] oldObj2 = oldObj;
        this.lmPar = 0.0d;
        boolean firstIteration2 = true;
        int iter3 = 0;
        ConvergenceChecker convergenceChecker2 = getConvergenceChecker();
        double currentCost3 = currentCost2;
        loop0:
        while (true) {
            ConvergenceChecker convergenceChecker3 = convergenceChecker2;
            int iter4 = iter3 + 1;
            PointVectorValuePair previous2 = current2;
            qrDecomposition(computeWeightedJacobian(currentPoint4));
            this.weightedResidual = weightMatrixSqrt2.operate(currentResiduals2);
            int i = 0;
            while (i < nR3) {
                double[] currentObjective3 = currentObjective2;
                qtf[i] = this.weightedResidual[i];
                i++;
                currentObjective2 = currentObjective3;
            }
            double[] currentObjective4 = currentObjective2;
            qTy(qtf);
            int k = 0;
            while (k < this.solvedCols) {
                int pk = this.permutation[k];
                double[] currentResiduals3 = currentResiduals2;
                int iter5 = iter4;
                this.weightedJacobian[k][pk] = this.diagR[pk];
                k++;
                currentResiduals2 = currentResiduals3;
                iter4 = iter5;
            }
            double[] currentResiduals4 = currentResiduals2;
            int iter6 = iter4;
            if (firstIteration2) {
                double xNorm2 = 0.0d;
                for (int k2 = 0; k2 < nC; k2++) {
                    double dk = this.jacNorm[k2];
                    if (dk == 0.0d) {
                        dk = 1.0d;
                    }
                    double xk = currentPoint4[k2] * dk;
                    xNorm2 += xk * xk;
                    diag[k2] = dk;
                }
                xNorm = FastMath.sqrt(xNorm2);
                delta2 = xNorm == 0.0d ? this.initialStepBoundFactor : this.initialStepBoundFactor * xNorm;
            }
            double currentCost4 = currentCost3;
            if (currentCost4 != 0.0d) {
                weightMatrixSqrt = weightMatrixSqrt2;
                work2 = work22;
                double maxCosine2 = 0.0d;
                int j = 0;
                while (j < this.solvedCols) {
                    int pj = this.permutation[j];
                    boolean firstIteration3 = firstIteration2;
                    double s = this.jacNorm[pj];
                    if (s != 0.0d) {
                        nR2 = nR3;
                        currentPoint3 = currentPoint4;
                        double sum = 0.0d;
                        int i2 = 0;
                        while (i2 <= j) {
                            sum += this.weightedJacobian[i2][pj] * qtf[i2];
                            i2++;
                            work13 = work13;
                        }
                        work12 = work13;
                        double d2 = sum;
                        maxCosine2 = FastMath.max(maxCosine2, FastMath.abs(sum) / (s * currentCost4));
                    } else {
                        nR2 = nR3;
                        currentPoint3 = currentPoint4;
                        work12 = work13;
                    }
                    j++;
                    firstIteration2 = firstIteration3;
                    nR3 = nR2;
                    currentPoint4 = currentPoint3;
                    work13 = work12;
                }
                firstIteration = firstIteration2;
                nR = nR3;
                currentPoint = currentPoint4;
                work1 = work13;
                d = 0.0d;
                maxCosine = maxCosine2;
            } else {
                firstIteration = firstIteration2;
                weightMatrixSqrt = weightMatrixSqrt2;
                work2 = work22;
                nR = nR3;
                currentPoint = currentPoint4;
                work1 = work13;
                d = 0.0d;
                maxCosine = 0.0d;
            }
            if (maxCosine <= this.orthoTolerance) {
                setCost(currentCost4);
                this.point = current2.getPoint();
                return current2;
            }
            int j2 = 0;
            while (j2 < nC) {
                PointVectorValuePair current3 = current2;
                diag[j2] = FastMath.max(diag[j2], this.jacNorm[j2]);
                j2++;
                current2 = current3;
            }
            PointVectorValuePair current4 = current2;
            double delta3 = delta2;
            double[] oldRes3 = oldRes2;
            double ratio = d;
            double delta4 = currentCost4;
            while (ratio < 1.0E-4d) {
                for (int j3 = 0; j3 < this.solvedCols; j3++) {
                    int pj2 = this.permutation[j3];
                    oldX[pj2] = currentPoint[pj2];
                }
                double previousCost = delta4;
                double[] tmpVec = this.weightedResidual;
                this.weightedResidual = oldRes3;
                double[] tmpVec2 = currentObjective4;
                double[] currentObjective5 = oldObj2;
                oldObj2 = tmpVec2;
                double maxCosine3 = maxCosine;
                double[] qtf2 = qtf;
                int iter7 = iter6;
                double[] oldRes4 = tmpVec;
                double[] work32 = work3;
                RealMatrix weightMatrixSqrt3 = weightMatrixSqrt;
                double delta5 = delta3;
                double[] work23 = work2;
                determineLMParameter(qtf, delta3, diag, work1, work2, work32);
                double lmNorm = 0.0d;
                for (int j4 = 0; j4 < this.solvedCols; j4++) {
                    int pj3 = this.permutation[j4];
                    this.lmDir[pj3] = -this.lmDir[pj3];
                    currentPoint[pj3] = oldX[pj3] + this.lmDir[pj3];
                    double s2 = diag[pj3] * this.lmDir[pj3];
                    lmNorm += s2 * s2;
                }
                double lmNorm2 = FastMath.sqrt(lmNorm);
                if (firstIteration) {
                    delta5 = FastMath.min(delta5, lmNorm2);
                }
                double[] currentPoint5 = currentPoint;
                double[] currentObjective6 = computeObjectiveValue(currentPoint5);
                double[] currentResiduals5 = computeResiduals(currentObjective6);
                PointVectorValuePair current5 = new PointVectorValuePair(currentPoint5, currentObjective6);
                currentCost = computeCost(currentResiduals5);
                double actRed = -1.0d;
                if (currentCost * 0.1d < previousCost) {
                    double r = currentCost / previousCost;
                    actRed = 1.0d - (r * r);
                }
                double[] work33 = work32;
                double actRed2 = actRed;
                int j5 = 0;
                while (true) {
                    currentResiduals = currentResiduals5;
                    currentObjective = currentObjective6;
                    int j6 = j5;
                    if (j6 >= this.solvedCols) {
                        break;
                    }
                    int pj4 = this.permutation[j6];
                    double[] oldX2 = oldX;
                    double dirJ = this.lmDir[pj4];
                    work1[j6] = d;
                    int i3 = 0;
                    while (i3 <= j6) {
                        PointVectorValuePair current6 = current5;
                        work1[i3] = work1[i3] + (this.weightedJacobian[i3][pj4] * dirJ);
                        i3++;
                        current5 = current6;
                    }
                    j5 = j6 + 1;
                    currentResiduals5 = currentResiduals;
                    currentObjective6 = currentObjective;
                    oldX = oldX2;
                }
                PointVectorValuePair current7 = current5;
                double[] oldX3 = oldX;
                double coeff1 = 0.0d;
                for (int j7 = 0; j7 < this.solvedCols; j7++) {
                    coeff1 += work1[j7] * work1[j7];
                }
                double pc2 = previousCost * previousCost;
                double coeff12 = coeff1 / pc2;
                double[] currentPoint6 = currentPoint5;
                double coeff2 = ((this.lmPar * lmNorm2) * lmNorm2) / pc2;
                double preRed = coeff12 + (coeff2 * 2.0d);
                double[] diag2 = diag;
                double dirDer = -(coeff12 + coeff2);
                double ratio2 = preRed == d ? d : actRed2 / preRed;
                double tmp = 0.5d;
                if (ratio2 <= 0.25d) {
                    if (actRed2 < d) {
                        tmp = (dirDer * 0.5d) / (dirDer + (0.5d * actRed2));
                    }
                    if (currentCost * 0.1d >= previousCost || tmp < 0.1d) {
                        tmp = 0.1d;
                    }
                    double d3 = coeff2;
                    delta = FastMath.min(delta5, lmNorm2 * 10.0d) * tmp;
                    this.lmPar /= tmp;
                } else {
                    if (this.lmPar == d || ratio2 >= 0.75d) {
                        delta = lmNorm2 * 2.0d;
                        this.lmPar *= 0.5d;
                    } else {
                        delta = delta5;
                    }
                }
                if (ratio2 >= 1.0E-4d) {
                    firstIteration = false;
                    double d4 = lmNorm2;
                    double xNorm3 = 0.0d;
                    for (int k3 = 0; k3 < nC; k3++) {
                        double xK = diag2[k3] * currentPoint6[k3];
                        xNorm3 += xK * xK;
                    }
                    xNorm = FastMath.sqrt(xNorm3);
                    ConvergenceChecker convergenceChecker4 = convergenceChecker3;
                    if (convergenceChecker4 != null) {
                        previous = previous2;
                        iter2 = iter7;
                        current = current7;
                        if (convergenceChecker4.converged(iter2, previous, current)) {
                            setCost(currentCost);
                            ConvergenceChecker convergenceChecker5 = convergenceChecker4;
                            this.point = current.getPoint();
                            return current;
                        }
                        convergenceChecker = convergenceChecker4;
                    } else {
                        convergenceChecker = convergenceChecker4;
                        previous = previous2;
                        iter2 = iter7;
                        current = current7;
                    }
                    iter = iter2;
                    double d5 = coeff12;
                    currentObjective4 = currentObjective;
                    currentPoint2 = currentPoint6;
                } else {
                    convergenceChecker = convergenceChecker3;
                    previous = previous2;
                    int iter8 = iter7;
                    PointVectorValuePair pointVectorValuePair = current7;
                    currentCost = previousCost;
                    int j8 = 0;
                    while (true) {
                        iter = iter8;
                        if (j8 >= this.solvedCols) {
                            break;
                        }
                        int pj5 = this.permutation[j8];
                        currentPoint6[pj5] = oldX3[pj5];
                        j8++;
                        iter8 = iter;
                    }
                    double[] tmpVec3 = this.weightedResidual;
                    this.weightedResidual = oldRes4;
                    double[] oldRes5 = tmpVec3;
                    double[] tmpVec4 = oldObj2;
                    oldObj2 = currentObjective;
                    double[] oldRes6 = oldRes5;
                    double d6 = coeff12;
                    currentPoint2 = currentPoint6;
                    currentObjective4 = tmpVec4;
                    current = new PointVectorValuePair(currentPoint2, tmpVec4);
                    oldRes4 = oldRes6;
                }
                double[] currentPoint7 = currentPoint2;
                if ((FastMath.abs(actRed2) > this.costRelativeTolerance || preRed > this.costRelativeTolerance || ratio2 > 2.0d) && delta > this.parRelativeTolerance * xNorm) {
                    if (FastMath.abs(actRed2) > 2.2204E-16d || preRed > 2.2204E-16d || ratio2 > 2.0d) {
                        double currentCost5 = currentCost;
                        if (delta <= xNorm * 2.2204E-16d) {
                            throw new ConvergenceException(LocalizedFormats.TOO_SMALL_PARAMETERS_RELATIVE_TOLERANCE, Double.valueOf(this.parRelativeTolerance));
                        } else if (maxCosine3 <= 2.2204E-16d) {
                            throw new ConvergenceException(LocalizedFormats.TOO_SMALL_ORTHOGONALITY_TOLERANCE, Double.valueOf(this.orthoTolerance));
                        } else {
                            delta3 = delta;
                            previous2 = previous;
                            current4 = current;
                            weightMatrixSqrt = weightMatrixSqrt3;
                            ratio = ratio2;
                            work2 = work23;
                            qtf = qtf2;
                            maxCosine = maxCosine3;
                            oldRes3 = oldRes4;
                            work3 = work33;
                            currentResiduals4 = currentResiduals;
                            oldX = oldX3;
                            diag = diag2;
                            convergenceChecker3 = convergenceChecker;
                            iter6 = iter;
                            currentPoint = currentPoint7;
                            delta4 = currentCost5;
                        }
                    } else {
                        double d7 = currentCost;
                        throw new ConvergenceException(LocalizedFormats.TOO_SMALL_COST_RELATIVE_TOLERANCE, Double.valueOf(this.costRelativeTolerance));
                    }
                }
            }
            double[] dArr = oldX;
            double[] qtf3 = qtf;
            double delta6 = delta3;
            oldRes2 = oldRes3;
            weightMatrixSqrt2 = weightMatrixSqrt;
            double d8 = d;
            work22 = work2;
            currentObjective2 = currentObjective4;
            currentResiduals2 = currentResiduals4;
            firstIteration2 = firstIteration;
            nR3 = nR;
            current2 = current4;
            work3 = work3;
            convergenceChecker2 = convergenceChecker3;
            iter3 = iter6;
            currentPoint4 = currentPoint;
            currentCost3 = delta4;
            delta2 = delta6;
            work13 = work1;
            qtf = qtf3;
        }
        setCost(currentCost);
        this.point = current.getPoint();
        return current;
    }

    private void determineLMParameter(double[] qy, double delta, double[] diag, double[] work1, double[] work2, double[] work3) {
        double parl;
        double gNorm;
        double fp;
        int nC;
        double[] dArr = qy;
        double d = delta;
        double[] dArr2 = work1;
        double[] dArr3 = work2;
        double[] dArr4 = work3;
        int i = this.weightedJacobian[0].length;
        for (int j = 0; j < this.rank; j++) {
            this.lmDir[this.permutation[j]] = dArr[j];
        }
        for (int j2 = this.rank; j2 < i; j2++) {
            this.lmDir[this.permutation[j2]] = 0.0d;
        }
        for (int k = this.rank - 1; k >= 0; k--) {
            int pk = this.permutation[k];
            double ypk = this.lmDir[pk] / this.diagR[pk];
            for (int i2 = 0; i2 < k; i2++) {
                double[] dArr5 = this.lmDir;
                int i3 = this.permutation[i2];
                dArr5[i3] = dArr5[i3] - (this.weightedJacobian[i2][pk] * ypk);
            }
            this.lmDir[pk] = ypk;
        }
        double dxNorm = 0.0d;
        for (int j3 = 0; j3 < this.solvedCols; j3++) {
            int pj = this.permutation[j3];
            double s = diag[pj] * this.lmDir[pj];
            dArr2[pj] = s;
            dxNorm += s * s;
        }
        double dxNorm2 = FastMath.sqrt(dxNorm);
        double fp2 = dxNorm2 - d;
        if (fp2 <= d * 0.1d) {
            this.lmPar = 0.0d;
            return;
        }
        long j4 = 0;
        if (this.rank == this.solvedCols) {
            for (int j5 = 0; j5 < this.solvedCols; j5++) {
                int pj2 = this.permutation[j5];
                dArr2[pj2] = dArr2[pj2] * (diag[pj2] / dxNorm2);
            }
            double sum2 = 0.0d;
            int j6 = 0;
            while (j6 < this.solvedCols) {
                int pj3 = this.permutation[j6];
                double sum = 0.0d;
                int i4 = 0;
                while (true) {
                    nC = i;
                    int i5 = i4;
                    if (i5 >= j6) {
                        break;
                    }
                    sum += this.weightedJacobian[i5][pj3] * dArr2[this.permutation[i5]];
                    i4 = i5 + 1;
                    i = nC;
                    j4 = j4;
                }
                long j7 = j4;
                double s2 = (dArr2[pj3] - sum) / this.diagR[pj3];
                dArr2[pj3] = s2;
                sum2 += s2 * s2;
                j6++;
                i = nC;
                j4 = j7;
            }
            int nC2 = i;
            long j8 = j4;
            parl = fp2 / (d * sum2);
        } else {
            int nC3 = i;
            parl = 0.0d;
        }
        double fp3 = fp2;
        double sum22 = 0.0d;
        int j9 = 0;
        while (j9 < this.solvedCols) {
            int pj4 = this.permutation[j9];
            double sum3 = 0.0d;
            int i6 = 0;
            while (true) {
                int i7 = i6;
                if (i7 > j9) {
                    break;
                }
                sum3 += this.weightedJacobian[i7][pj4] * dArr[i7];
                i6 = i7 + 1;
                double[] dArr6 = work2;
                double[] dArr7 = work3;
            }
            double sum4 = sum3 / diag[pj4];
            sum22 += sum4 * sum4;
            j9++;
            double[] dArr8 = work2;
            double[] dArr9 = work3;
        }
        double gNorm2 = FastMath.sqrt(sum22);
        double paru = gNorm2 / d;
        double d2 = sum22;
        if (paru == 0.0d) {
            paru = 2.2251E-308d / FastMath.min(d, 0.1d);
        }
        double fp4 = paru;
        this.lmPar = FastMath.min(fp4, FastMath.max(this.lmPar, parl));
        if (this.lmPar == 0.0d) {
            this.lmPar = gNorm2 / dxNorm2;
        }
        int countdown = 10;
        while (countdown >= 0) {
            if (this.lmPar == 0.0d) {
                gNorm = gNorm2;
                this.lmPar = FastMath.max(2.2251E-308d, 0.001d * fp4);
            } else {
                gNorm = gNorm2;
            }
            double sPar = FastMath.sqrt(this.lmPar);
            int j10 = 0;
            while (j10 < this.solvedCols) {
                int pj5 = this.permutation[j10];
                double dxNorm3 = dxNorm2;
                work1[pj5] = diag[pj5] * sPar;
                j10++;
                dxNorm2 = dxNorm3;
            }
            double[] dArr10 = work1;
            double[] dArr11 = work2;
            double[] dArr12 = work3;
            determineLMDirection(dArr, dArr10, dArr11, dArr12);
            double d3 = sPar;
            double dxNorm4 = 0.0d;
            int j11 = 0;
            while (j11 < this.solvedCols) {
                int pj6 = this.permutation[j11];
                int countdown2 = countdown;
                double s3 = diag[pj6] * this.lmDir[pj6];
                dArr12[pj6] = s3;
                dxNorm4 += s3 * s3;
                j11++;
                countdown = countdown2;
                double[] dArr13 = qy;
            }
            int countdown3 = countdown;
            double dxNorm5 = FastMath.sqrt(dxNorm4);
            double previousFP = fp3;
            double paru2 = fp4;
            double paru3 = dxNorm5 - d;
            if (FastMath.abs(paru3) <= d * 0.1d) {
                double fp5 = paru3;
                double d4 = paru2;
            } else if (parl != 0.0d || paru3 > previousFP || previousFP >= 0.0d) {
                for (int j12 = 0; j12 < this.solvedCols; j12++) {
                    int pj7 = this.permutation[j12];
                    dArr10[pj7] = (dArr12[pj7] * diag[pj7]) / dxNorm5;
                }
                int j13 = 0;
                while (j13 < this.solvedCols) {
                    int pj8 = this.permutation[j13];
                    dArr10[pj8] = dArr10[pj8] / dArr11[j13];
                    double tmp = dArr10[pj8];
                    int i8 = j13 + 1;
                    while (true) {
                        int i9 = i8;
                        if (i9 >= this.solvedCols) {
                            break;
                        }
                        int i10 = this.permutation[i9];
                        dArr10[i10] = dArr10[i10] - (this.weightedJacobian[i9][pj8] * tmp);
                        i8 = i9 + 1;
                        double[] dArr14 = work2;
                        double[] dArr15 = work3;
                    }
                    j13++;
                    dArr11 = work2;
                    double[] dArr16 = work3;
                }
                double sum23 = 0.0d;
                for (int j14 = 0; j14 < this.solvedCols; j14++) {
                    double s4 = dArr10[this.permutation[j14]];
                    sum23 += s4 * s4;
                }
                double correction = paru3 / (d * sum23);
                if (paru3 > 0.0d) {
                    parl = FastMath.max(parl, this.lmPar);
                    fp = paru3;
                    fp4 = paru2;
                } else if (paru3 < 0.0d) {
                    fp = paru3;
                    fp4 = FastMath.min(paru2, this.lmPar);
                } else {
                    fp = paru3;
                    fp4 = paru2;
                }
                this.lmPar = FastMath.max(parl, this.lmPar + correction);
                countdown = countdown3 - 1;
                dxNorm2 = dxNorm5;
                gNorm2 = gNorm;
                fp3 = fp;
                dArr = qy;
                d = delta;
            } else {
                double d5 = paru3;
                double d6 = paru2;
            }
            return;
        }
        double paru4 = fp4;
        double d7 = gNorm2;
        double d8 = dxNorm2;
        double[] dArr17 = work1;
    }

    private void determineLMDirection(double[] qy, double[] diag, double[] lmDiag, double[] work) {
        int j;
        double dpj;
        int pj;
        double cotan;
        double sin;
        double[] dArr = lmDiag;
        for (int j2 = 0; j2 < this.solvedCols; j2++) {
            int pj2 = this.permutation[j2];
            for (int i = j2 + 1; i < this.solvedCols; i++) {
                this.weightedJacobian[i][pj2] = this.weightedJacobian[j2][this.permutation[i]];
            }
            this.lmDir[j2] = this.diagR[pj2];
            work[j2] = qy[j2];
        }
        int j3 = 0;
        while (true) {
            double d = 0.0d;
            if (j3 >= this.solvedCols) {
                break;
            }
            int pj3 = this.permutation[j3];
            double dpj2 = diag[pj3];
            if (dpj2 != 0.0d) {
                Arrays.fill(dArr, j3 + 1, dArr.length, 0.0d);
            }
            dArr[j3] = dpj2;
            double qtbpj = 0.0d;
            int k = j3;
            while (k < this.solvedCols) {
                int pk = this.permutation[k];
                if (dArr[k] != d) {
                    double rkk = this.weightedJacobian[k][pk];
                    pj = pj3;
                    if (FastMath.abs(rkk) < FastMath.abs(dArr[k])) {
                        double cotan2 = rkk / dArr[k];
                        dpj = dpj2;
                        sin = 1.0d / FastMath.sqrt((cotan2 * cotan2) + 1.0d);
                        cotan = cotan2 * sin;
                        j = j3;
                    } else {
                        dpj = dpj2;
                        double tan = dArr[k] / rkk;
                        j = j3;
                        cotan = 1.0d / FastMath.sqrt((tan * tan) + 1.0d);
                        sin = cotan * tan;
                    }
                    double sin2 = sin;
                    this.weightedJacobian[k][pk] = (cotan * rkk) + (dArr[k] * sin2);
                    double temp = (work[k] * cotan) + (sin2 * qtbpj);
                    double qtbpj2 = ((-sin2) * work[k]) + (cotan * qtbpj);
                    work[k] = temp;
                    int i2 = k + 1;
                    while (i2 < this.solvedCols) {
                        double rik = this.weightedJacobian[i2][pk];
                        double temp2 = (cotan * rik) + (dArr[i2] * sin2);
                        double temp3 = temp;
                        dArr[i2] = ((-sin2) * rik) + (dArr[i2] * cotan);
                        this.weightedJacobian[i2][pk] = temp2;
                        i2++;
                        temp = temp3;
                    }
                    qtbpj = qtbpj2;
                } else {
                    j = j3;
                    pj = pj3;
                    dpj = dpj2;
                }
                k++;
                pj3 = pj;
                dpj2 = dpj;
                j3 = j;
                d = 0.0d;
            }
            int j4 = j3;
            int i3 = pj3;
            double d2 = dpj2;
            dArr[j4] = this.weightedJacobian[j4][this.permutation[j4]];
            this.weightedJacobian[j4][this.permutation[j4]] = this.lmDir[j4];
            j3 = j4 + 1;
        }
        int nSing = this.solvedCols;
        for (int j5 = 0; j5 < this.solvedCols; j5++) {
            if (dArr[j5] == 0.0d && nSing == this.solvedCols) {
                nSing = j5;
            }
            if (nSing < this.solvedCols) {
                work[j5] = 0.0d;
            }
        }
        if (nSing > 0) {
            for (int j6 = nSing - 1; j6 >= 0; j6--) {
                int pj4 = this.permutation[j6];
                double sum = 0.0d;
                for (int i4 = j6 + 1; i4 < nSing; i4++) {
                    sum += this.weightedJacobian[i4][pj4] * work[i4];
                }
                work[j6] = (work[j6] - sum) / dArr[j6];
            }
        }
        int j7 = 0;
        while (true) {
            int j8 = j7;
            if (j8 < this.lmDir.length) {
                this.lmDir[this.permutation[j8]] = work[j8];
                j7 = j8 + 1;
            } else {
                return;
            }
        }
    }

    private void qrDecomposition(RealMatrix jacobian) throws ConvergenceException {
        this.weightedJacobian = jacobian.scalarMultiply(-1.0d).getData();
        char c = 0;
        int nC = this.weightedJacobian[0].length;
        for (int k = 0; k < nC; k++) {
            this.permutation[k] = k;
            double norm2 = 0.0d;
            for (double[] dArr : this.weightedJacobian) {
                double akk = dArr[k];
                norm2 += akk * akk;
            }
            this.jacNorm[k] = FastMath.sqrt(norm2);
        }
        int k2 = 0;
        while (k2 < nC) {
            double ak2 = Double.NEGATIVE_INFINITY;
            int nextColumn = -1;
            for (int i = k2; i < nC; i++) {
                double norm22 = 0.0d;
                for (int j = k2; j < nR; j++) {
                    double aki = this.weightedJacobian[j][this.permutation[i]];
                    norm22 += aki * aki;
                }
                if (Double.isInfinite(norm22) != 0 || Double.isNaN(norm22)) {
                    LocalizedFormats localizedFormats = LocalizedFormats.UNABLE_TO_PERFORM_QR_DECOMPOSITION_ON_JACOBIAN;
                    Object[] objArr = new Object[2];
                    objArr[c] = Integer.valueOf(nR);
                    objArr[1] = Integer.valueOf(nC);
                    throw new ConvergenceException(localizedFormats, objArr);
                }
                if (norm22 > ak2) {
                    nextColumn = i;
                    ak2 = norm22;
                }
            }
            if (ak2 <= this.qrRankingThreshold) {
                this.rank = k2;
                return;
            }
            int pk = this.permutation[nextColumn];
            this.permutation[nextColumn] = this.permutation[k2];
            this.permutation[k2] = pk;
            double akk2 = this.weightedJacobian[k2][pk];
            double alpha = akk2 > 0.0d ? -FastMath.sqrt(ak2) : FastMath.sqrt(ak2);
            double betak = 1.0d / (ak2 - (akk2 * alpha));
            this.beta[pk] = betak;
            this.diagR[pk] = alpha;
            double[] dArr2 = this.weightedJacobian[k2];
            dArr2[pk] = dArr2[pk] - alpha;
            int dk = (nC - 1) - k2;
            while (dk > 0) {
                double gamma = 0.0d;
                int j2 = k2;
                while (true) {
                    int j3 = j2;
                    if (j3 >= nR) {
                        break;
                    }
                    gamma += this.weightedJacobian[j3][pk] * this.weightedJacobian[j3][this.permutation[k2 + dk]];
                    j2 = j3 + 1;
                    nC = nC;
                    RealMatrix realMatrix = jacobian;
                }
                int nC2 = nC;
                double gamma2 = gamma * betak;
                int j4 = k2;
                while (j4 < nR) {
                    double[] dArr3 = this.weightedJacobian[j4];
                    int i2 = this.permutation[k2 + dk];
                    int nR = nR;
                    dArr3[i2] = dArr3[i2] - (this.weightedJacobian[j4][pk] * gamma2);
                    j4++;
                    nR = nR;
                }
                dk--;
                nC = nC2;
                RealMatrix realMatrix2 = jacobian;
            }
            int i3 = nC;
            k2++;
            RealMatrix realMatrix3 = jacobian;
            c = 0;
        }
        int i4 = nC;
        this.rank = this.solvedCols;
    }

    private void qTy(double[] y) {
        int nR = this.weightedJacobian.length;
        int nC = this.weightedJacobian[0].length;
        for (int k = 0; k < nC; k++) {
            int pk = this.permutation[k];
            double gamma = 0.0d;
            for (int i = k; i < nR; i++) {
                gamma += this.weightedJacobian[i][pk] * y[i];
            }
            double gamma2 = gamma * this.beta[pk];
            for (int i2 = k; i2 < nR; i2++) {
                y[i2] = y[i2] - (this.weightedJacobian[i2][pk] * gamma2);
            }
        }
    }
}

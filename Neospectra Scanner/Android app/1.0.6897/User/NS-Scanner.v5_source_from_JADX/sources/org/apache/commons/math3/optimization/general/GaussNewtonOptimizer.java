package org.apache.commons.math3.optimization.general;

import java.lang.reflect.Array;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;

@Deprecated
public class GaussNewtonOptimizer extends AbstractLeastSquaresOptimizer {
    private final boolean useLU;

    @Deprecated
    public GaussNewtonOptimizer() {
        this(true);
    }

    public GaussNewtonOptimizer(ConvergenceChecker<PointVectorValuePair> checker) {
        this(true, checker);
    }

    @Deprecated
    public GaussNewtonOptimizer(boolean useLU2) {
        this(useLU2, new SimpleVectorValueChecker());
    }

    public GaussNewtonOptimizer(boolean useLU2, ConvergenceChecker<PointVectorValuePair> checker) {
        super(checker);
        this.useLU = useLU2;
    }

    public PointVectorValuePair doOptimize() {
        DecompositionSolver solver;
        ConvergenceChecker convergenceChecker;
        int nR;
        double[] residualsWeights;
        ConvergenceChecker convergenceChecker2 = getConvergenceChecker();
        if (convergenceChecker2 == null) {
            throw new NullArgumentException();
        }
        double[] targetValues = getTarget();
        int j = targetValues.length;
        RealMatrix weightMatrix = getWeight();
        double[] residualsWeights2 = new double[j];
        for (int i = 0; i < j; i++) {
            residualsWeights2[i] = weightMatrix.getEntry(i, i);
        }
        double[] currentPoint = getStartPoint();
        int nC = currentPoint.length;
        int iter = 0;
        PointVectorValuePair current = null;
        boolean converged = false;
        while (!converged) {
            iter++;
            PointVectorValuePair previous = current;
            double[] currentObjective = computeObjectiveValue(currentPoint);
            double[] currentResiduals = computeResiduals(currentObjective);
            RealMatrix weightedJacobian = computeWeightedJacobian(currentPoint);
            double[] targetValues2 = targetValues;
            current = new PointVectorValuePair(currentPoint, currentObjective);
            double[] b = new double[nC];
            RealMatrix weightMatrix2 = weightMatrix;
            boolean converged2 = converged;
            double[][] a = (double[][]) Array.newInstance(double.class, new int[]{nC, nC});
            int i2 = 0;
            while (i2 < j) {
                double[] grad = weightedJacobian.getRow(i2);
                double weight = residualsWeights2[i2];
                double wr = weight * currentResiduals[i2];
                int j2 = 0;
                while (true) {
                    nR = j;
                    int j3 = j2;
                    if (j3 >= nC) {
                        break;
                    }
                    b[j3] = b[j3] + (grad[j3] * wr);
                    j2 = j3 + 1;
                    j = nR;
                }
                int k = 0;
                while (k < nC) {
                    double[] ak = a[k];
                    double wgk = grad[k] * weight;
                    int l = 0;
                    while (true) {
                        residualsWeights = residualsWeights2;
                        int l2 = l;
                        if (l2 >= nC) {
                            break;
                        }
                        ak[l2] = ak[l2] + (grad[l2] * wgk);
                        l = l2 + 1;
                        residualsWeights2 = residualsWeights;
                    }
                    k++;
                    residualsWeights2 = residualsWeights;
                }
                i2++;
                j = nR;
            }
            int nR2 = j;
            double[] residualsWeights3 = residualsWeights2;
            try {
                BlockRealMatrix blockRealMatrix = new BlockRealMatrix(a);
                if (this.useLU) {
                    try {
                        solver = new LUDecomposition(blockRealMatrix).getSolver();
                    } catch (SingularMatrixException e) {
                        ConvergenceChecker convergenceChecker3 = convergenceChecker2;
                        double[] dArr = b;
                        SingularMatrixException singularMatrixException = e;
                        throw new ConvergenceException(LocalizedFormats.UNABLE_TO_SOLVE_SINGULAR_PROBLEM, new Object[0]);
                    }
                } else {
                    solver = new QRDecomposition(blockRealMatrix).getSolver();
                }
                BlockRealMatrix blockRealMatrix2 = blockRealMatrix;
                double[] dX = solver.solve((RealVector) new ArrayRealVector(b, false)).toArray();
                for (int i3 = 0; i3 < nC; i3++) {
                    currentPoint[i3] = currentPoint[i3] + dX[i3];
                }
                if (previous != null) {
                    boolean converged3 = convergenceChecker2.converged(iter, previous, current);
                    if (converged3) {
                        ConvergenceChecker convergenceChecker4 = convergenceChecker2;
                        double[] dArr2 = b;
                        this.cost = computeCost(currentResiduals);
                        this.point = current.getPoint();
                        return current;
                    }
                    convergenceChecker = convergenceChecker2;
                    converged = converged3;
                } else {
                    convergenceChecker = convergenceChecker2;
                    converged = converged2;
                }
                targetValues = targetValues2;
                weightMatrix = weightMatrix2;
                j = nR2;
                residualsWeights2 = residualsWeights3;
                convergenceChecker2 = convergenceChecker;
            } catch (SingularMatrixException e2) {
                ConvergenceChecker convergenceChecker5 = convergenceChecker2;
                double[] dArr3 = b;
                SingularMatrixException singularMatrixException2 = e2;
                throw new ConvergenceException(LocalizedFormats.UNABLE_TO_SOLVE_SINGULAR_PROBLEM, new Object[0]);
            }
        }
        ConvergenceChecker convergenceChecker6 = convergenceChecker2;
        double[] dArr4 = targetValues;
        int i4 = j;
        RealMatrix realMatrix = weightMatrix;
        double[] dArr5 = residualsWeights2;
        throw new MathInternalError();
    }
}

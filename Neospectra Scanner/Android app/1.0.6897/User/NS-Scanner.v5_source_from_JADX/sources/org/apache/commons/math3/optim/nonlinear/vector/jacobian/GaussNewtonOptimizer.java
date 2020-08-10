package org.apache.commons.math3.optim.nonlinear.vector.jacobian;

import java.lang.reflect.Array;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.MathUnsupportedOperationException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.PointVectorValuePair;

@Deprecated
public class GaussNewtonOptimizer extends AbstractLeastSquaresOptimizer {
    private final boolean useLU;

    public GaussNewtonOptimizer(ConvergenceChecker<PointVectorValuePair> checker) {
        this(true, checker);
    }

    public GaussNewtonOptimizer(boolean useLU2, ConvergenceChecker<PointVectorValuePair> checker) {
        super(checker);
        this.useLU = useLU2;
    }

    public PointVectorValuePair doOptimize() {
        int nR;
        double[] residualsWeights;
        GaussNewtonOptimizer gaussNewtonOptimizer = this;
        checkParameters();
        ConvergenceChecker<PointVectorValuePair> checker = getConvergenceChecker();
        if (checker == null) {
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
        PointVectorValuePair current = null;
        boolean converged = false;
        while (!converged) {
            incrementIterationCount();
            PointVectorValuePair previous = current;
            double[] currentObjective = gaussNewtonOptimizer.computeObjectiveValue(currentPoint);
            double[] currentResiduals = gaussNewtonOptimizer.computeResiduals(currentObjective);
            RealMatrix weightedJacobian = gaussNewtonOptimizer.computeWeightedJacobian(currentPoint);
            current = new PointVectorValuePair(currentPoint, currentObjective);
            double[] b = new double[nC];
            double[] targetValues2 = targetValues;
            RealMatrix weightMatrix2 = weightMatrix;
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
            if (previous != null) {
                boolean converged2 = checker.converged(getIterations(), previous, current);
                if (converged2) {
                    gaussNewtonOptimizer.setCost(gaussNewtonOptimizer.computeCost(currentResiduals));
                    return current;
                }
                converged = converged2;
            }
            try {
                RealMatrix mA = new BlockRealMatrix(a);
                double[] dX = (gaussNewtonOptimizer.useLU ? new LUDecomposition(mA).getSolver() : new QRDecomposition(mA).getSolver()).solve((RealVector) new ArrayRealVector(b, false)).toArray();
                for (int i3 = 0; i3 < nC; i3++) {
                    currentPoint[i3] = currentPoint[i3] + dX[i3];
                }
                targetValues = targetValues2;
                weightMatrix = weightMatrix2;
                j = nR2;
                residualsWeights2 = residualsWeights3;
                gaussNewtonOptimizer = this;
            } catch (SingularMatrixException e) {
                SingularMatrixException singularMatrixException = e;
                throw new ConvergenceException(LocalizedFormats.UNABLE_TO_SOLVE_SINGULAR_PROBLEM, new Object[0]);
            }
        }
        int i4 = j;
        RealMatrix realMatrix = weightMatrix;
        double[] dArr = residualsWeights2;
        throw new MathInternalError();
    }

    private void checkParameters() {
        if (getLowerBound() != null || getUpperBound() != null) {
            throw new MathUnsupportedOperationException(LocalizedFormats.CONSTRAINT, new Object[0]);
        }
    }
}

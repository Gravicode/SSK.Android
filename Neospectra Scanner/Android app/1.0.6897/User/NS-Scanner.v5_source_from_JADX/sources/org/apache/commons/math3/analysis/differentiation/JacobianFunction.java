package org.apache.commons.math3.analysis.differentiation;

import java.lang.reflect.Array;
import org.apache.commons.math3.analysis.MultivariateMatrixFunction;

public class JacobianFunction implements MultivariateMatrixFunction {

    /* renamed from: f */
    private final MultivariateDifferentiableVectorFunction f502f;

    public JacobianFunction(MultivariateDifferentiableVectorFunction f) {
        this.f502f = f;
    }

    public double[][] value(double[] point) {
        DerivativeStructure[] dsX = new DerivativeStructure[point.length];
        for (int i = 0; i < point.length; i++) {
            DerivativeStructure derivativeStructure = new DerivativeStructure(point.length, 1, i, point[i]);
            dsX[i] = derivativeStructure;
        }
        DerivativeStructure[] dsY = this.f502f.value(dsX);
        double[][] y = (double[][]) Array.newInstance(double.class, new int[]{dsY.length, point.length});
        int[] orders = new int[point.length];
        for (int i2 = 0; i2 < dsY.length; i2++) {
            for (int j = 0; j < point.length; j++) {
                orders[j] = 1;
                y[i2][j] = dsY[i2].getPartialDerivative(orders);
                orders[j] = 0;
            }
        }
        return y;
    }
}

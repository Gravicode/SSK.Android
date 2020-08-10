package org.apache.commons.math3.analysis.differentiation;

import java.io.Serializable;
import java.lang.reflect.Array;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateMatrixFunction;
import org.apache.commons.math3.analysis.UnivariateVectorFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.FastMath;

public class FiniteDifferencesDifferentiator implements UnivariateFunctionDifferentiator, UnivariateVectorFunctionDifferentiator, UnivariateMatrixFunctionDifferentiator, Serializable {
    private static final long serialVersionUID = 20120917;
    /* access modifiers changed from: private */
    public final double halfSampleSpan;
    /* access modifiers changed from: private */
    public final int nbPoints;
    /* access modifiers changed from: private */
    public final double stepSize;
    /* access modifiers changed from: private */
    public final double tMax;
    /* access modifiers changed from: private */
    public final double tMin;

    public FiniteDifferencesDifferentiator(int nbPoints2, double stepSize2) throws NotPositiveException, NumberIsTooSmallException {
        this(nbPoints2, stepSize2, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public FiniteDifferencesDifferentiator(int nbPoints2, double stepSize2, double tLower, double tUpper) throws NotPositiveException, NumberIsTooSmallException, NumberIsTooLargeException {
        if (nbPoints2 <= 1) {
            throw new NumberIsTooSmallException(Double.valueOf(stepSize2), Integer.valueOf(1), false);
        }
        this.nbPoints = nbPoints2;
        if (stepSize2 <= 0.0d) {
            throw new NotPositiveException(Double.valueOf(stepSize2));
        }
        this.stepSize = stepSize2;
        this.halfSampleSpan = 0.5d * stepSize2 * ((double) (nbPoints2 - 1));
        if (this.halfSampleSpan * 2.0d >= tUpper - tLower) {
            throw new NumberIsTooLargeException(Double.valueOf(this.halfSampleSpan * 2.0d), Double.valueOf(tUpper - tLower), false);
        }
        double safety = FastMath.ulp(this.halfSampleSpan);
        this.tMin = this.halfSampleSpan + tLower + safety;
        this.tMax = (tUpper - this.halfSampleSpan) - safety;
    }

    public int getNbPoints() {
        return this.nbPoints;
    }

    public double getStepSize() {
        return this.stepSize;
    }

    /* access modifiers changed from: private */
    public DerivativeStructure evaluate(DerivativeStructure t, double t0, double[] y) throws NumberIsTooLargeException {
        double[] top = new double[this.nbPoints];
        double[] bottom = new double[this.nbPoints];
        for (int i = 0; i < this.nbPoints; i++) {
            bottom[i] = y[i];
            for (int j = 1; j <= i; j++) {
                bottom[i - j] = (bottom[(i - j) + 1] - bottom[i - j]) / (((double) j) * this.stepSize);
            }
            top[i] = bottom[0];
        }
        int order = t.getOrder();
        int parameters = t.getFreeParameters();
        double[] derivatives = t.getAllDerivatives();
        double dt0 = t.getValue() - t0;
        DerivativeStructure monomial = null;
        DerivativeStructure interpolation = new DerivativeStructure(parameters, order, 0.0d);
        for (int i2 = 0; i2 < this.nbPoints; i2++) {
            if (i2 == 0) {
                monomial = new DerivativeStructure(parameters, order, 1.0d);
            } else {
                int order2 = order;
                derivatives[0] = dt0 - (((double) (i2 - 1)) * this.stepSize);
                order = order2;
                monomial = monomial.multiply(new DerivativeStructure(parameters, order, derivatives));
            }
            interpolation = interpolation.add(monomial.multiply(top[i2]));
        }
        return interpolation;
    }

    public UnivariateDifferentiableFunction differentiate(final UnivariateFunction function) {
        return new UnivariateDifferentiableFunction() {
            public double value(double x) throws MathIllegalArgumentException {
                return function.value(x);
            }

            public DerivativeStructure value(DerivativeStructure t) throws MathIllegalArgumentException {
                if (t.getOrder() >= FiniteDifferencesDifferentiator.this.nbPoints) {
                    throw new NumberIsTooLargeException(Integer.valueOf(t.getOrder()), Integer.valueOf(FiniteDifferencesDifferentiator.this.nbPoints), false);
                }
                double t0 = FastMath.max(FastMath.min(t.getValue(), FiniteDifferencesDifferentiator.this.tMax), FiniteDifferencesDifferentiator.this.tMin) - FiniteDifferencesDifferentiator.this.halfSampleSpan;
                double[] y = new double[FiniteDifferencesDifferentiator.this.nbPoints];
                for (int i = 0; i < FiniteDifferencesDifferentiator.this.nbPoints; i++) {
                    y[i] = function.value((((double) i) * FiniteDifferencesDifferentiator.this.stepSize) + t0);
                }
                return FiniteDifferencesDifferentiator.this.evaluate(t, t0, y);
            }
        };
    }

    public UnivariateDifferentiableVectorFunction differentiate(final UnivariateVectorFunction function) {
        return new UnivariateDifferentiableVectorFunction() {
            public double[] value(double x) throws MathIllegalArgumentException {
                return function.value(x);
            }

            public DerivativeStructure[] value(DerivativeStructure t) throws MathIllegalArgumentException {
                if (t.getOrder() >= FiniteDifferencesDifferentiator.this.nbPoints) {
                    throw new NumberIsTooLargeException(Integer.valueOf(t.getOrder()), Integer.valueOf(FiniteDifferencesDifferentiator.this.nbPoints), false);
                }
                double t0 = FastMath.max(FastMath.min(t.getValue(), FiniteDifferencesDifferentiator.this.tMax), FiniteDifferencesDifferentiator.this.tMin) - FiniteDifferencesDifferentiator.this.halfSampleSpan;
                double[][] y = null;
                for (int i = 0; i < FiniteDifferencesDifferentiator.this.nbPoints; i++) {
                    double[] v = function.value((((double) i) * FiniteDifferencesDifferentiator.this.stepSize) + t0);
                    if (i == 0) {
                        y = (double[][]) Array.newInstance(double.class, new int[]{v.length, FiniteDifferencesDifferentiator.this.nbPoints});
                    }
                    for (int j = 0; j < v.length; j++) {
                        y[j][i] = v[j];
                    }
                }
                DerivativeStructure[] value = new DerivativeStructure[y.length];
                for (int j2 = 0; j2 < value.length; j2++) {
                    value[j2] = FiniteDifferencesDifferentiator.this.evaluate(t, t0, y[j2]);
                }
                return value;
            }
        };
    }

    public UnivariateDifferentiableMatrixFunction differentiate(final UnivariateMatrixFunction function) {
        return new UnivariateDifferentiableMatrixFunction() {
            public double[][] value(double x) throws MathIllegalArgumentException {
                return function.value(x);
            }

            public DerivativeStructure[][] value(DerivativeStructure t) throws MathIllegalArgumentException {
                if (t.getOrder() >= FiniteDifferencesDifferentiator.this.nbPoints) {
                    throw new NumberIsTooLargeException(Integer.valueOf(t.getOrder()), Integer.valueOf(FiniteDifferencesDifferentiator.this.nbPoints), false);
                }
                double t0 = FastMath.max(FastMath.min(t.getValue(), FiniteDifferencesDifferentiator.this.tMax), FiniteDifferencesDifferentiator.this.tMin) - FiniteDifferencesDifferentiator.this.halfSampleSpan;
                double[][][] y = null;
                for (int i = 0; i < FiniteDifferencesDifferentiator.this.nbPoints; i++) {
                    double[][] v = function.value((((double) i) * FiniteDifferencesDifferentiator.this.stepSize) + t0);
                    if (i == 0) {
                        y = (double[][][]) Array.newInstance(double.class, new int[]{v.length, v[0].length, FiniteDifferencesDifferentiator.this.nbPoints});
                    }
                    for (int j = 0; j < v.length; j++) {
                        for (int k = 0; k < v[j].length; k++) {
                            y[j][k][i] = v[j][k];
                        }
                    }
                }
                DerivativeStructure[][] value = (DerivativeStructure[][]) Array.newInstance(DerivativeStructure.class, new int[]{y.length, y[0].length});
                for (int j2 = 0; j2 < value.length; j2++) {
                    for (int k2 = 0; k2 < y[j2].length; k2++) {
                        value[j2][k2] = FiniteDifferencesDifferentiator.this.evaluate(t, t0, y[j2][k2]);
                    }
                }
                return value;
            }
        };
    }
}

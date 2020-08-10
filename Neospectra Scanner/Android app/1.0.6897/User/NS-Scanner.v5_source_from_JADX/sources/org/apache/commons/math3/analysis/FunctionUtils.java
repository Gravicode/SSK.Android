package org.apache.commons.math3.analysis;

import java.lang.reflect.Array;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.MultivariateDifferentiableFunction;
import org.apache.commons.math3.analysis.differentiation.MultivariateDifferentiableVectorFunction;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
import org.apache.commons.math3.analysis.function.Identity;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class FunctionUtils {
    private FunctionUtils() {
    }

    public static UnivariateFunction compose(final UnivariateFunction... f) {
        return new UnivariateFunction() {
            public double value(double x) {
                double r = x;
                for (int i = f.length - 1; i >= 0; i--) {
                    r = f[i].value(r);
                }
                return r;
            }
        };
    }

    public static UnivariateDifferentiableFunction compose(final UnivariateDifferentiableFunction... f) {
        return new UnivariateDifferentiableFunction() {
            public double value(double t) {
                double r = t;
                for (int i = f.length - 1; i >= 0; i--) {
                    r = f[i].value(r);
                }
                return r;
            }

            public DerivativeStructure value(DerivativeStructure t) {
                DerivativeStructure r = t;
                for (int i = f.length - 1; i >= 0; i--) {
                    r = f[i].value(r);
                }
                return r;
            }
        };
    }

    @Deprecated
    public static DifferentiableUnivariateFunction compose(final DifferentiableUnivariateFunction... f) {
        return new DifferentiableUnivariateFunction() {
            public double value(double x) {
                double r = x;
                for (int i = f.length - 1; i >= 0; i--) {
                    r = f[i].value(r);
                }
                return r;
            }

            public UnivariateFunction derivative() {
                return new UnivariateFunction() {
                    public double value(double x) {
                        double p = 1.0d;
                        double r = x;
                        for (int i = f.length - 1; i >= 0; i--) {
                            p *= f[i].derivative().value(r);
                            r = f[i].value(r);
                        }
                        return p;
                    }
                };
            }
        };
    }

    public static UnivariateFunction add(final UnivariateFunction... f) {
        return new UnivariateFunction() {
            public double value(double x) {
                double r = f[0].value(x);
                for (int i = 1; i < f.length; i++) {
                    r += f[i].value(x);
                }
                return r;
            }
        };
    }

    public static UnivariateDifferentiableFunction add(final UnivariateDifferentiableFunction... f) {
        return new UnivariateDifferentiableFunction() {
            public double value(double t) {
                double r = f[0].value(t);
                for (int i = 1; i < f.length; i++) {
                    r += f[i].value(t);
                }
                return r;
            }

            public DerivativeStructure value(DerivativeStructure t) throws DimensionMismatchException {
                DerivativeStructure r = f[0].value(t);
                for (int i = 1; i < f.length; i++) {
                    r = r.add(f[i].value(t));
                }
                return r;
            }
        };
    }

    @Deprecated
    public static DifferentiableUnivariateFunction add(final DifferentiableUnivariateFunction... f) {
        return new DifferentiableUnivariateFunction() {
            public double value(double x) {
                double r = f[0].value(x);
                for (int i = 1; i < f.length; i++) {
                    r += f[i].value(x);
                }
                return r;
            }

            public UnivariateFunction derivative() {
                return new UnivariateFunction() {
                    public double value(double x) {
                        double r = f[0].derivative().value(x);
                        for (int i = 1; i < f.length; i++) {
                            r += f[i].derivative().value(x);
                        }
                        return r;
                    }
                };
            }
        };
    }

    public static UnivariateFunction multiply(final UnivariateFunction... f) {
        return new UnivariateFunction() {
            public double value(double x) {
                double r = f[0].value(x);
                for (int i = 1; i < f.length; i++) {
                    r *= f[i].value(x);
                }
                return r;
            }
        };
    }

    public static UnivariateDifferentiableFunction multiply(final UnivariateDifferentiableFunction... f) {
        return new UnivariateDifferentiableFunction() {
            public double value(double t) {
                double r = f[0].value(t);
                for (int i = 1; i < f.length; i++) {
                    r *= f[i].value(t);
                }
                return r;
            }

            public DerivativeStructure value(DerivativeStructure t) {
                DerivativeStructure r = f[0].value(t);
                for (int i = 1; i < f.length; i++) {
                    r = r.multiply(f[i].value(t));
                }
                return r;
            }
        };
    }

    @Deprecated
    public static DifferentiableUnivariateFunction multiply(final DifferentiableUnivariateFunction... f) {
        return new DifferentiableUnivariateFunction() {
            public double value(double x) {
                double r = f[0].value(x);
                for (int i = 1; i < f.length; i++) {
                    r *= f[i].value(x);
                }
                return r;
            }

            public UnivariateFunction derivative() {
                return new UnivariateFunction() {
                    public double value(double x) {
                        double sum = 0.0d;
                        for (int i = 0; i < f.length; i++) {
                            double prod = f[i].derivative().value(x);
                            for (int j = 0; j < f.length; j++) {
                                if (i != j) {
                                    prod *= f[j].value(x);
                                }
                            }
                            sum += prod;
                        }
                        return sum;
                    }
                };
            }
        };
    }

    public static UnivariateFunction combine(final BivariateFunction combiner, final UnivariateFunction f, final UnivariateFunction g) {
        return new UnivariateFunction() {
            public double value(double x) {
                return combiner.value(f.value(x), g.value(x));
            }
        };
    }

    public static MultivariateFunction collector(final BivariateFunction combiner, final UnivariateFunction f, final double initialValue) {
        return new MultivariateFunction() {
            public double value(double[] point) {
                double result = combiner.value(initialValue, f.value(point[0]));
                for (int i = 1; i < point.length; i++) {
                    result = combiner.value(result, f.value(point[i]));
                }
                return result;
            }
        };
    }

    public static MultivariateFunction collector(BivariateFunction combiner, double initialValue) {
        return collector(combiner, new Identity(), initialValue);
    }

    public static UnivariateFunction fix1stArgument(final BivariateFunction f, final double fixed) {
        return new UnivariateFunction() {
            public double value(double x) {
                return f.value(fixed, x);
            }
        };
    }

    public static UnivariateFunction fix2ndArgument(final BivariateFunction f, final double fixed) {
        return new UnivariateFunction() {
            public double value(double x) {
                return f.value(x, fixed);
            }
        };
    }

    public static double[] sample(UnivariateFunction f, double min, double max, int n) throws NumberIsTooLargeException, NotStrictlyPositiveException {
        if (n <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.NOT_POSITIVE_NUMBER_OF_SAMPLES, Integer.valueOf(n));
        }
        if (min >= max) {
            throw new NumberIsTooLargeException(Double.valueOf(min), Double.valueOf(max), false);
        }
        double[] s = new double[n];
        double h = (max - min) / ((double) n);
        for (int i = 0; i < n; i++) {
            s[i] = f.value((((double) i) * h) + min);
        }
        return s;
    }

    @Deprecated
    public static DifferentiableUnivariateFunction toDifferentiableUnivariateFunction(final UnivariateDifferentiableFunction f) {
        return new DifferentiableUnivariateFunction() {
            public double value(double x) {
                return f.value(x);
            }

            public UnivariateFunction derivative() {
                return new UnivariateFunction() {
                    public double value(double x) {
                        UnivariateDifferentiableFunction univariateDifferentiableFunction = f;
                        DerivativeStructure derivativeStructure = new DerivativeStructure(1, 1, 0, x);
                        return univariateDifferentiableFunction.value(derivativeStructure).getPartialDerivative(1);
                    }
                };
            }
        };
    }

    @Deprecated
    public static UnivariateDifferentiableFunction toUnivariateDifferential(final DifferentiableUnivariateFunction f) {
        return new UnivariateDifferentiableFunction() {
            public double value(double x) {
                return f.value(x);
            }

            public DerivativeStructure value(DerivativeStructure t) throws NumberIsTooLargeException {
                switch (t.getOrder()) {
                    case 0:
                        return new DerivativeStructure(t.getFreeParameters(), 0, f.value(t.getValue()));
                    case 1:
                        int parameters = t.getFreeParameters();
                        double[] derivatives = new double[(parameters + 1)];
                        derivatives[0] = f.value(t.getValue());
                        double fPrime = f.derivative().value(t.getValue());
                        int[] orders = new int[parameters];
                        for (int i = 0; i < parameters; i++) {
                            orders[i] = 1;
                            derivatives[i + 1] = t.getPartialDerivative(orders) * fPrime;
                            orders[i] = 0;
                        }
                        return new DerivativeStructure(parameters, 1, derivatives);
                    default:
                        throw new NumberIsTooLargeException(Integer.valueOf(t.getOrder()), Integer.valueOf(1), true);
                }
            }
        };
    }

    @Deprecated
    public static DifferentiableMultivariateFunction toDifferentiableMultivariateFunction(final MultivariateDifferentiableFunction f) {
        return new DifferentiableMultivariateFunction() {
            public double value(double[] x) {
                return f.value(x);
            }

            public MultivariateFunction partialDerivative(final int k) {
                return new MultivariateFunction() {
                    public double value(double[] x) {
                        int n = x.length;
                        DerivativeStructure[] dsX = new DerivativeStructure[n];
                        for (int i = 0; i < n; i++) {
                            if (i == k) {
                                DerivativeStructure derivativeStructure = new DerivativeStructure(1, 1, 0, x[i]);
                                dsX[i] = derivativeStructure;
                            } else {
                                dsX[i] = new DerivativeStructure(1, 1, x[i]);
                            }
                        }
                        return f.value(dsX).getPartialDerivative(1);
                    }
                };
            }

            public MultivariateVectorFunction gradient() {
                return new MultivariateVectorFunction() {
                    public double[] value(double[] x) {
                        int n = x.length;
                        DerivativeStructure[] dsX = new DerivativeStructure[n];
                        int i = 0;
                        while (true) {
                            int i2 = i;
                            if (i2 >= n) {
                                break;
                            }
                            DerivativeStructure derivativeStructure = new DerivativeStructure(n, 1, i2, x[i2]);
                            dsX[i2] = derivativeStructure;
                            i = i2 + 1;
                        }
                        DerivativeStructure y = f.value(dsX);
                        double[] gradient = new double[n];
                        int[] orders = new int[n];
                        for (int i3 = 0; i3 < n; i3++) {
                            orders[i3] = 1;
                            gradient[i3] = y.getPartialDerivative(orders);
                            orders[i3] = 0;
                        }
                        return gradient;
                    }
                };
            }
        };
    }

    @Deprecated
    public static MultivariateDifferentiableFunction toMultivariateDifferentiableFunction(final DifferentiableMultivariateFunction f) {
        return new MultivariateDifferentiableFunction() {
            public double value(double[] x) {
                return f.value(x);
            }

            public DerivativeStructure value(DerivativeStructure[] t) throws DimensionMismatchException, NumberIsTooLargeException {
                DerivativeStructure[] derivativeStructureArr = t;
                int parameters = derivativeStructureArr[0].getFreeParameters();
                int order = derivativeStructureArr[0].getOrder();
                int n = derivativeStructureArr.length;
                int i = 1;
                if (order > 1) {
                    throw new NumberIsTooLargeException(Integer.valueOf(order), Integer.valueOf(1), true);
                }
                int i2 = 0;
                while (i2 < n) {
                    if (derivativeStructureArr[i2].getFreeParameters() != parameters) {
                        throw new DimensionMismatchException(derivativeStructureArr[i2].getFreeParameters(), parameters);
                    } else if (derivativeStructureArr[i2].getOrder() != order) {
                        throw new DimensionMismatchException(derivativeStructureArr[i2].getOrder(), order);
                    } else {
                        i2++;
                    }
                }
                double[] point = new double[n];
                for (int i3 = 0; i3 < n; i3++) {
                    point[i3] = derivativeStructureArr[i3].getValue();
                }
                double value = f.value(point);
                double[] gradient = f.gradient().value(point);
                double[] derivatives = new double[(parameters + 1)];
                derivatives[0] = value;
                int[] orders = new int[parameters];
                int i4 = 0;
                while (i4 < parameters) {
                    orders[i4] = i;
                    for (int j = 0; j < n; j++) {
                        int i5 = i4 + 1;
                        derivatives[i5] = derivatives[i5] + (gradient[j] * derivativeStructureArr[j].getPartialDerivative(orders));
                    }
                    orders[i4] = 0;
                    i4++;
                    i = 1;
                }
                return new DerivativeStructure(parameters, order, derivatives);
            }
        };
    }

    @Deprecated
    public static DifferentiableMultivariateVectorFunction toDifferentiableMultivariateVectorFunction(final MultivariateDifferentiableVectorFunction f) {
        return new DifferentiableMultivariateVectorFunction() {
            public double[] value(double[] x) {
                return f.value(x);
            }

            public MultivariateMatrixFunction jacobian() {
                return new MultivariateMatrixFunction() {
                    public double[][] value(double[] x) {
                        int n = x.length;
                        DerivativeStructure[] dsX = new DerivativeStructure[n];
                        int i = 0;
                        while (true) {
                            int i2 = i;
                            if (i2 >= n) {
                                break;
                            }
                            DerivativeStructure derivativeStructure = new DerivativeStructure(n, 1, i2, x[i2]);
                            dsX[i2] = derivativeStructure;
                            i = i2 + 1;
                        }
                        DerivativeStructure[] y = f.value(dsX);
                        double[][] jacobian = (double[][]) Array.newInstance(double.class, new int[]{y.length, n});
                        int[] orders = new int[n];
                        for (int i3 = 0; i3 < y.length; i3++) {
                            for (int j = 0; j < n; j++) {
                                orders[j] = 1;
                                jacobian[i3][j] = y[i3].getPartialDerivative(orders);
                                orders[j] = 0;
                            }
                        }
                        return jacobian;
                    }
                };
            }
        };
    }

    @Deprecated
    public static MultivariateDifferentiableVectorFunction toMultivariateDifferentiableVectorFunction(final DifferentiableMultivariateVectorFunction f) {
        return new MultivariateDifferentiableVectorFunction() {
            public double[] value(double[] x) {
                return f.value(x);
            }

            public DerivativeStructure[] value(DerivativeStructure[] t) throws DimensionMismatchException, NumberIsTooLargeException {
                DerivativeStructure[] derivativeStructureArr = t;
                int parameters = derivativeStructureArr[0].getFreeParameters();
                int order = derivativeStructureArr[0].getOrder();
                int n = derivativeStructureArr.length;
                int i = 1;
                if (order > 1) {
                    throw new NumberIsTooLargeException(Integer.valueOf(order), Integer.valueOf(1), true);
                }
                int i2 = 0;
                while (i2 < n) {
                    if (derivativeStructureArr[i2].getFreeParameters() != parameters) {
                        throw new DimensionMismatchException(derivativeStructureArr[i2].getFreeParameters(), parameters);
                    } else if (derivativeStructureArr[i2].getOrder() != order) {
                        throw new DimensionMismatchException(derivativeStructureArr[i2].getOrder(), order);
                    } else {
                        i2++;
                    }
                }
                double[] point = new double[n];
                for (int i3 = 0; i3 < n; i3++) {
                    point[i3] = derivativeStructureArr[i3].getValue();
                }
                double[] value = f.value(point);
                double[][] jacobian = f.jacobian().value(point);
                DerivativeStructure[] merged = new DerivativeStructure[value.length];
                int k = 0;
                while (k < merged.length) {
                    double[] derivatives = new double[(parameters + 1)];
                    derivatives[0] = value[k];
                    int[] orders = new int[parameters];
                    int i4 = 0;
                    while (i4 < parameters) {
                        orders[i4] = i;
                        for (int j = 0; j < n; j++) {
                            int i5 = i4 + 1;
                            derivatives[i5] = derivatives[i5] + (jacobian[k][j] * derivativeStructureArr[j].getPartialDerivative(orders));
                        }
                        orders[i4] = 0;
                        i4++;
                        i = 1;
                    }
                    merged[k] = new DerivativeStructure(parameters, order, derivatives);
                    k++;
                    i = 1;
                }
                return merged;
            }
        };
    }
}

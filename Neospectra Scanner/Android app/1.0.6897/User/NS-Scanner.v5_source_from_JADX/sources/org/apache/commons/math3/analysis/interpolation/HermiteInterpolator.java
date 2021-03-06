package org.apache.commons.math3.analysis.interpolation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableVectorFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.CombinatoricsUtils;

public class HermiteInterpolator implements UnivariateDifferentiableVectorFunction {
    private final List<Double> abscissae = new ArrayList();
    private final List<double[]> bottomDiagonal = new ArrayList();
    private final List<double[]> topDiagonal = new ArrayList();

    public void addSamplePoint(double x, double[]... value) throws ZeroException, MathArithmeticException {
        double[][] dArr = value;
        char c = 0;
        int i = 0;
        while (i < dArr.length) {
            double[] y = (double[]) dArr[i].clone();
            double d = 1.0d;
            if (i > 1) {
                double inv = 1.0d / ((double) CombinatoricsUtils.factorial(i));
                for (int j = 0; j < y.length; j++) {
                    y[j] = y[j] * inv;
                }
            }
            int n = this.abscissae.size();
            this.bottomDiagonal.add(n - i, y);
            double[] bottom0 = y;
            int j2 = i;
            while (j2 < n) {
                double[] bottom1 = (double[]) this.bottomDiagonal.get(n - (j2 + 1));
                double inv2 = d / (x - ((Double) this.abscissae.get(n - (j2 + 1))).doubleValue());
                if (Double.isInfinite(inv2)) {
                    LocalizedFormats localizedFormats = LocalizedFormats.DUPLICATED_ABSCISSA_DIVISION_BY_ZERO;
                    Object[] objArr = new Object[1];
                    objArr[c] = Double.valueOf(x);
                    throw new ZeroException(localizedFormats, objArr);
                }
                int k = 0;
                while (true) {
                    int k2 = k;
                    if (k2 >= y.length) {
                        break;
                    }
                    bottom1[k2] = (bottom0[k2] - bottom1[k2]) * inv2;
                    k = k2 + 1;
                }
                bottom0 = bottom1;
                j2++;
                c = 0;
                d = 1.0d;
            }
            this.topDiagonal.add(bottom0.clone());
            this.abscissae.add(Double.valueOf(x));
            i++;
            c = 0;
        }
    }

    public PolynomialFunction[] getPolynomials() throws NoDataException {
        checkInterpolation();
        PolynomialFunction zero = polynomial(0.0d);
        PolynomialFunction[] polynomials = new PolynomialFunction[((double[]) this.topDiagonal.get(0)).length];
        for (int i = 0; i < polynomials.length; i++) {
            polynomials[i] = zero;
        }
        PolynomialFunction coeff = polynomial(1.0d);
        for (int i2 = 0; i2 < this.topDiagonal.size(); i2++) {
            double[] tdi = (double[]) this.topDiagonal.get(i2);
            for (int k = 0; k < polynomials.length; k++) {
                polynomials[k] = polynomials[k].add(coeff.multiply(polynomial(tdi[k])));
            }
            coeff = coeff.multiply(polynomial(-((Double) this.abscissae.get(i2)).doubleValue(), 1.0d));
        }
        return polynomials;
    }

    public double[] value(double x) throws NoDataException {
        checkInterpolation();
        double[] value = new double[((double[]) this.topDiagonal.get(0)).length];
        double valueCoeff = 1.0d;
        for (int i = 0; i < this.topDiagonal.size(); i++) {
            double[] dividedDifference = (double[]) this.topDiagonal.get(i);
            for (int k = 0; k < value.length; k++) {
                value[k] = value[k] + (dividedDifference[k] * valueCoeff);
            }
            valueCoeff *= x - ((Double) this.abscissae.get(i)).doubleValue();
        }
        return value;
    }

    public DerivativeStructure[] value(DerivativeStructure x) throws NoDataException {
        checkInterpolation();
        DerivativeStructure[] value = new DerivativeStructure[((double[]) this.topDiagonal.get(0)).length];
        Arrays.fill(value, x.getField().getZero());
        DerivativeStructure valueCoeff = (DerivativeStructure) x.getField().getOne();
        for (int i = 0; i < this.topDiagonal.size(); i++) {
            double[] dividedDifference = (double[]) this.topDiagonal.get(i);
            for (int k = 0; k < value.length; k++) {
                value[k] = value[k].add(valueCoeff.multiply(dividedDifference[k]));
            }
            valueCoeff = valueCoeff.multiply(x.subtract(((Double) this.abscissae.get(i)).doubleValue()));
        }
        return value;
    }

    private void checkInterpolation() throws NoDataException {
        if (this.abscissae.isEmpty()) {
            throw new NoDataException(LocalizedFormats.EMPTY_INTERPOLATION_SAMPLE);
        }
    }

    private PolynomialFunction polynomial(double... c) {
        return new PolynomialFunction(c);
    }
}

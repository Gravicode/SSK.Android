package org.apache.commons.math3.stat.descriptive.summary;

import java.io.Serializable;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
import org.apache.commons.math3.stat.descriptive.WeightedEvaluation;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;

public class Product extends AbstractStorelessUnivariateStatistic implements Serializable, WeightedEvaluation {
    private static final long serialVersionUID = 2824226005990582538L;

    /* renamed from: n */
    private long f787n;
    private double value;

    public Product() {
        this.f787n = 0;
        this.value = 1.0d;
    }

    public Product(Product original) throws NullArgumentException {
        copy(original, this);
    }

    public void increment(double d) {
        this.value *= d;
        this.f787n++;
    }

    public double getResult() {
        return this.value;
    }

    public long getN() {
        return this.f787n;
    }

    public void clear() {
        this.value = 1.0d;
        this.f787n = 0;
    }

    public double evaluate(double[] values, int begin, int length) throws MathIllegalArgumentException {
        if (!test(values, begin, length, true)) {
            return Double.NaN;
        }
        double product = 1.0d;
        for (int i = begin; i < begin + length; i++) {
            product *= values[i];
        }
        return product;
    }

    public double evaluate(double[] values, double[] weights, int begin, int length) throws MathIllegalArgumentException {
        if (!test(values, weights, begin, length, true)) {
            return Double.NaN;
        }
        double product = 1.0d;
        for (int i = begin; i < begin + length; i++) {
            product *= FastMath.pow(values[i], weights[i]);
        }
        return product;
    }

    public double evaluate(double[] values, double[] weights) throws MathIllegalArgumentException {
        return evaluate(values, weights, 0, values.length);
    }

    public Product copy() {
        Product result = new Product();
        copy(this, result);
        return result;
    }

    public static void copy(Product source, Product dest) throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        dest.setData(source.getDataRef());
        dest.f787n = source.f787n;
        dest.value = source.value;
    }
}

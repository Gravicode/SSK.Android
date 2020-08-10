package org.apache.commons.math3.analysis.differentiation;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.poi.p009ss.usermodel.ShapeTypes;

public class SparseGradient implements RealFieldElement<SparseGradient>, Serializable {
    private static final long serialVersionUID = 20131025;
    private final Map<Integer, Double> derivatives = new HashMap();
    private double value;

    private SparseGradient(double value2, Map<Integer, Double> derivatives2) {
        this.value = value2;
        if (derivatives2 != null) {
            this.derivatives.putAll(derivatives2);
        }
    }

    private SparseGradient(double value2, double scale, Map<Integer, Double> derivatives2) {
        this.value = value2;
        if (derivatives2 != null) {
            for (Entry<Integer, Double> entry : derivatives2.entrySet()) {
                this.derivatives.put(entry.getKey(), Double.valueOf(((Double) entry.getValue()).doubleValue() * scale));
            }
        }
    }

    public static SparseGradient createConstant(double value2) {
        return new SparseGradient(value2, Collections.emptyMap());
    }

    public static SparseGradient createVariable(int idx, double value2) {
        return new SparseGradient(value2, Collections.singletonMap(Integer.valueOf(idx), Double.valueOf(1.0d)));
    }

    public int numVars() {
        return this.derivatives.size();
    }

    public double getDerivative(int index) {
        Double out = (Double) this.derivatives.get(Integer.valueOf(index));
        if (out == null) {
            return 0.0d;
        }
        return out.doubleValue();
    }

    public double getValue() {
        return this.value;
    }

    public double getReal() {
        return this.value;
    }

    public SparseGradient add(SparseGradient a) {
        SparseGradient out = new SparseGradient(this.value + a.value, this.derivatives);
        for (Entry<Integer, Double> entry : a.derivatives.entrySet()) {
            int id = ((Integer) entry.getKey()).intValue();
            Double old = (Double) out.derivatives.get(Integer.valueOf(id));
            if (old == null) {
                out.derivatives.put(Integer.valueOf(id), entry.getValue());
            } else {
                out.derivatives.put(Integer.valueOf(id), Double.valueOf(old.doubleValue() + ((Double) entry.getValue()).doubleValue()));
            }
        }
        return out;
    }

    public void addInPlace(SparseGradient a) {
        this.value += a.value;
        for (Entry<Integer, Double> entry : a.derivatives.entrySet()) {
            int id = ((Integer) entry.getKey()).intValue();
            Double old = (Double) this.derivatives.get(Integer.valueOf(id));
            if (old == null) {
                this.derivatives.put(Integer.valueOf(id), entry.getValue());
            } else {
                this.derivatives.put(Integer.valueOf(id), Double.valueOf(old.doubleValue() + ((Double) entry.getValue()).doubleValue()));
            }
        }
    }

    public SparseGradient add(double c) {
        return new SparseGradient(this.value + c, this.derivatives);
    }

    public SparseGradient subtract(SparseGradient a) {
        SparseGradient out = new SparseGradient(this.value - a.value, this.derivatives);
        for (Entry<Integer, Double> entry : a.derivatives.entrySet()) {
            int id = ((Integer) entry.getKey()).intValue();
            Double old = (Double) out.derivatives.get(Integer.valueOf(id));
            if (old == null) {
                out.derivatives.put(Integer.valueOf(id), Double.valueOf(-((Double) entry.getValue()).doubleValue()));
            } else {
                out.derivatives.put(Integer.valueOf(id), Double.valueOf(old.doubleValue() - ((Double) entry.getValue()).doubleValue()));
            }
        }
        return out;
    }

    public SparseGradient subtract(double c) {
        return new SparseGradient(this.value - c, this.derivatives);
    }

    public SparseGradient multiply(SparseGradient a) {
        SparseGradient out = new SparseGradient(this.value * a.value, Collections.emptyMap());
        for (Entry<Integer, Double> entry : this.derivatives.entrySet()) {
            out.derivatives.put(entry.getKey(), Double.valueOf(a.value * ((Double) entry.getValue()).doubleValue()));
        }
        for (Entry<Integer, Double> entry2 : a.derivatives.entrySet()) {
            int id = ((Integer) entry2.getKey()).intValue();
            Double old = (Double) out.derivatives.get(Integer.valueOf(id));
            if (old == null) {
                out.derivatives.put(Integer.valueOf(id), Double.valueOf(this.value * ((Double) entry2.getValue()).doubleValue()));
            } else {
                out.derivatives.put(Integer.valueOf(id), Double.valueOf(old.doubleValue() + (this.value * ((Double) entry2.getValue()).doubleValue())));
            }
        }
        return out;
    }

    public void multiplyInPlace(SparseGradient a) {
        for (Entry<Integer, Double> entry : this.derivatives.entrySet()) {
            this.derivatives.put(entry.getKey(), Double.valueOf(a.value * ((Double) entry.getValue()).doubleValue()));
        }
        for (Entry<Integer, Double> entry2 : a.derivatives.entrySet()) {
            int id = ((Integer) entry2.getKey()).intValue();
            Double old = (Double) this.derivatives.get(Integer.valueOf(id));
            if (old == null) {
                this.derivatives.put(Integer.valueOf(id), Double.valueOf(this.value * ((Double) entry2.getValue()).doubleValue()));
            } else {
                this.derivatives.put(Integer.valueOf(id), Double.valueOf(old.doubleValue() + (this.value * ((Double) entry2.getValue()).doubleValue())));
            }
        }
        this.value *= a.value;
    }

    public SparseGradient multiply(double c) {
        SparseGradient sparseGradient = new SparseGradient(this.value * c, c, this.derivatives);
        return sparseGradient;
    }

    public SparseGradient multiply(int n) {
        SparseGradient sparseGradient = new SparseGradient(this.value * ((double) n), (double) n, this.derivatives);
        return sparseGradient;
    }

    public SparseGradient divide(SparseGradient a) {
        SparseGradient out = new SparseGradient(this.value / a.value, Collections.emptyMap());
        for (Entry<Integer, Double> entry : this.derivatives.entrySet()) {
            out.derivatives.put(entry.getKey(), Double.valueOf(((Double) entry.getValue()).doubleValue() / a.value));
        }
        for (Entry<Integer, Double> entry2 : a.derivatives.entrySet()) {
            int id = ((Integer) entry2.getKey()).intValue();
            Double old = (Double) out.derivatives.get(Integer.valueOf(id));
            if (old == null) {
                out.derivatives.put(Integer.valueOf(id), Double.valueOf(((-out.value) / a.value) * ((Double) entry2.getValue()).doubleValue()));
            } else {
                out.derivatives.put(Integer.valueOf(id), Double.valueOf(old.doubleValue() - ((out.value / a.value) * ((Double) entry2.getValue()).doubleValue())));
            }
        }
        return out;
    }

    public SparseGradient divide(double c) {
        SparseGradient sparseGradient = new SparseGradient(this.value / c, 1.0d / c, this.derivatives);
        return sparseGradient;
    }

    public SparseGradient negate() {
        SparseGradient sparseGradient = new SparseGradient(-this.value, -1.0d, this.derivatives);
        return sparseGradient;
    }

    public Field<SparseGradient> getField() {
        return new Field<SparseGradient>() {
            public SparseGradient getZero() {
                return SparseGradient.createConstant(0.0d);
            }

            public SparseGradient getOne() {
                return SparseGradient.createConstant(1.0d);
            }

            public Class<? extends FieldElement<SparseGradient>> getRuntimeClass() {
                return SparseGradient.class;
            }
        };
    }

    public SparseGradient remainder(double a) {
        return new SparseGradient(FastMath.IEEEremainder(this.value, a), this.derivatives);
    }

    public SparseGradient remainder(SparseGradient a) {
        return subtract(a.multiply(FastMath.rint((this.value - FastMath.IEEEremainder(this.value, a.value)) / a.value)));
    }

    public SparseGradient abs() {
        if (Double.doubleToLongBits(this.value) < 0) {
            return negate();
        }
        return this;
    }

    public SparseGradient ceil() {
        return createConstant(FastMath.ceil(this.value));
    }

    public SparseGradient floor() {
        return createConstant(FastMath.floor(this.value));
    }

    public SparseGradient rint() {
        return createConstant(FastMath.rint(this.value));
    }

    public long round() {
        return FastMath.round(this.value);
    }

    public SparseGradient signum() {
        return createConstant(FastMath.signum(this.value));
    }

    public SparseGradient copySign(SparseGradient sign) {
        long m = Double.doubleToLongBits(this.value);
        long s = Double.doubleToLongBits(sign.value);
        if ((m < 0 || s < 0) && (m >= 0 || s >= 0)) {
            return negate();
        }
        return this;
    }

    public SparseGradient copySign(double sign) {
        long m = Double.doubleToLongBits(this.value);
        long s = Double.doubleToLongBits(sign);
        if ((m < 0 || s < 0) && (m >= 0 || s >= 0)) {
            return negate();
        }
        return this;
    }

    public SparseGradient scalb(int n) {
        SparseGradient out = new SparseGradient(FastMath.scalb(this.value, n), Collections.emptyMap());
        for (Entry<Integer, Double> entry : this.derivatives.entrySet()) {
            out.derivatives.put(entry.getKey(), Double.valueOf(FastMath.scalb(((Double) entry.getValue()).doubleValue(), n)));
        }
        return out;
    }

    public SparseGradient hypot(SparseGradient y) {
        if (Double.isInfinite(this.value) || Double.isInfinite(y.value)) {
            return createConstant(Double.POSITIVE_INFINITY);
        }
        if (Double.isNaN(this.value) || Double.isNaN(y.value)) {
            return createConstant(Double.NaN);
        }
        int expX = FastMath.getExponent(this.value);
        int expY = FastMath.getExponent(y.value);
        if (expX > expY + 27) {
            return abs();
        }
        if (expY > expX + 27) {
            return y.abs();
        }
        int middleExp = (expX + expY) / 2;
        SparseGradient scaledX = scalb(-middleExp);
        SparseGradient scaledY = y.scalb(-middleExp);
        return scaledX.multiply(scaledX).add(scaledY.multiply(scaledY)).sqrt().scalb(middleExp);
    }

    public static SparseGradient hypot(SparseGradient x, SparseGradient y) {
        return x.hypot(y);
    }

    public SparseGradient reciprocal() {
        SparseGradient sparseGradient = new SparseGradient(1.0d / this.value, -1.0d / (this.value * this.value), this.derivatives);
        return sparseGradient;
    }

    public SparseGradient sqrt() {
        double sqrt = FastMath.sqrt(this.value);
        SparseGradient sparseGradient = new SparseGradient(sqrt, 0.5d / sqrt, this.derivatives);
        return sparseGradient;
    }

    public SparseGradient cbrt() {
        double cbrt = FastMath.cbrt(this.value);
        SparseGradient sparseGradient = new SparseGradient(cbrt, 1.0d / ((3.0d * cbrt) * cbrt), this.derivatives);
        return sparseGradient;
    }

    public SparseGradient rootN(int n) {
        if (n == 2) {
            return sqrt();
        }
        if (n == 3) {
            return cbrt();
        }
        double root = FastMath.pow(this.value, 1.0d / ((double) n));
        SparseGradient sparseGradient = new SparseGradient(root, 1.0d / (((double) n) * FastMath.pow(root, n - 1)), this.derivatives);
        return sparseGradient;
    }

    public SparseGradient pow(double p) {
        SparseGradient sparseGradient = new SparseGradient(FastMath.pow(this.value, p), FastMath.pow(this.value, p - 1.0d) * p, this.derivatives);
        return sparseGradient;
    }

    public SparseGradient pow(int n) {
        if (n == 0) {
            return (SparseGradient) getField().getOne();
        }
        double valueNm1 = FastMath.pow(this.value, n - 1);
        SparseGradient sparseGradient = new SparseGradient(this.value * valueNm1, ((double) n) * valueNm1, this.derivatives);
        return sparseGradient;
    }

    public SparseGradient pow(SparseGradient e) {
        return log().multiply(e).exp();
    }

    public static SparseGradient pow(double a, SparseGradient x) {
        if (a != 0.0d) {
            double ax = FastMath.pow(a, x.value);
            SparseGradient sparseGradient = new SparseGradient(ax, ax * FastMath.log(a), x.derivatives);
            return sparseGradient;
        } else if (x.value == 0.0d) {
            return x.compose(1.0d, Double.NEGATIVE_INFINITY);
        } else {
            if (x.value < 0.0d) {
                return x.compose(Double.NaN, Double.NaN);
            }
            return (SparseGradient) x.getField().getZero();
        }
    }

    public SparseGradient exp() {
        double e = FastMath.exp(this.value);
        SparseGradient sparseGradient = new SparseGradient(e, e, this.derivatives);
        return sparseGradient;
    }

    public SparseGradient expm1() {
        SparseGradient sparseGradient = new SparseGradient(FastMath.expm1(this.value), FastMath.exp(this.value), this.derivatives);
        return sparseGradient;
    }

    public SparseGradient log() {
        SparseGradient sparseGradient = new SparseGradient(FastMath.log(this.value), 1.0d / this.value, this.derivatives);
        return sparseGradient;
    }

    public SparseGradient log10() {
        SparseGradient sparseGradient = new SparseGradient(FastMath.log10(this.value), 1.0d / (FastMath.log(10.0d) * this.value), this.derivatives);
        return sparseGradient;
    }

    public SparseGradient log1p() {
        SparseGradient sparseGradient = new SparseGradient(FastMath.log1p(this.value), 1.0d / (this.value + 1.0d), this.derivatives);
        return sparseGradient;
    }

    public SparseGradient cos() {
        SparseGradient sparseGradient = new SparseGradient(FastMath.cos(this.value), -FastMath.sin(this.value), this.derivatives);
        return sparseGradient;
    }

    public SparseGradient sin() {
        SparseGradient sparseGradient = new SparseGradient(FastMath.sin(this.value), FastMath.cos(this.value), this.derivatives);
        return sparseGradient;
    }

    public SparseGradient tan() {
        double t = FastMath.tan(this.value);
        SparseGradient sparseGradient = new SparseGradient(t, (t * t) + 1.0d, this.derivatives);
        return sparseGradient;
    }

    public SparseGradient acos() {
        SparseGradient sparseGradient = new SparseGradient(FastMath.acos(this.value), -1.0d / FastMath.sqrt(1.0d - (this.value * this.value)), this.derivatives);
        return sparseGradient;
    }

    public SparseGradient asin() {
        SparseGradient sparseGradient = new SparseGradient(FastMath.asin(this.value), 1.0d / FastMath.sqrt(1.0d - (this.value * this.value)), this.derivatives);
        return sparseGradient;
    }

    public SparseGradient atan() {
        SparseGradient sparseGradient = new SparseGradient(FastMath.atan(this.value), 1.0d / ((this.value * this.value) + 1.0d), this.derivatives);
        return sparseGradient;
    }

    public SparseGradient atan2(SparseGradient x) {
        SparseGradient tmp;
        SparseGradient r = multiply(this).add(x.multiply(x)).sqrt();
        if (x.value >= 0.0d) {
            tmp = divide(r.add(x)).atan().multiply(2);
        } else {
            SparseGradient tmp2 = divide(r.subtract(x)).atan().multiply(-2);
            tmp = tmp2.add(tmp2.value <= 0.0d ? -3.141592653589793d : 3.141592653589793d);
        }
        tmp.value = FastMath.atan2(this.value, x.value);
        return tmp;
    }

    public static SparseGradient atan2(SparseGradient y, SparseGradient x) {
        return y.atan2(x);
    }

    public SparseGradient cosh() {
        SparseGradient sparseGradient = new SparseGradient(FastMath.cosh(this.value), FastMath.sinh(this.value), this.derivatives);
        return sparseGradient;
    }

    public SparseGradient sinh() {
        SparseGradient sparseGradient = new SparseGradient(FastMath.sinh(this.value), FastMath.cosh(this.value), this.derivatives);
        return sparseGradient;
    }

    public SparseGradient tanh() {
        double t = FastMath.tanh(this.value);
        SparseGradient sparseGradient = new SparseGradient(t, 1.0d - (t * t), this.derivatives);
        return sparseGradient;
    }

    public SparseGradient acosh() {
        SparseGradient sparseGradient = new SparseGradient(FastMath.acosh(this.value), 1.0d / FastMath.sqrt((this.value * this.value) - 1.0d), this.derivatives);
        return sparseGradient;
    }

    public SparseGradient asinh() {
        SparseGradient sparseGradient = new SparseGradient(FastMath.asinh(this.value), 1.0d / FastMath.sqrt((this.value * this.value) + 1.0d), this.derivatives);
        return sparseGradient;
    }

    public SparseGradient atanh() {
        SparseGradient sparseGradient = new SparseGradient(FastMath.atanh(this.value), 1.0d / (1.0d - (this.value * this.value)), this.derivatives);
        return sparseGradient;
    }

    public SparseGradient toDegrees() {
        SparseGradient sparseGradient = new SparseGradient(FastMath.toDegrees(this.value), FastMath.toDegrees(1.0d), this.derivatives);
        return sparseGradient;
    }

    public SparseGradient toRadians() {
        SparseGradient sparseGradient = new SparseGradient(FastMath.toRadians(this.value), FastMath.toRadians(1.0d), this.derivatives);
        return sparseGradient;
    }

    public double taylor(double... delta) {
        double y = this.value;
        for (int i = 0; i < delta.length; i++) {
            y += delta[i] * getDerivative(i);
        }
        return y;
    }

    public SparseGradient compose(double f0, double f1) {
        SparseGradient sparseGradient = new SparseGradient(f0, f1, this.derivatives);
        return sparseGradient;
    }

    public SparseGradient linearCombination(SparseGradient[] a, SparseGradient[] b) throws DimensionMismatchException {
        SparseGradient out = (SparseGradient) a[0].getField().getZero();
        for (int i = 0; i < a.length; i++) {
            out = out.add(a[i].multiply(b[i]));
        }
        double[] aDouble = new double[a.length];
        for (int i2 = 0; i2 < a.length; i2++) {
            aDouble[i2] = a[i2].getValue();
        }
        double[] bDouble = new double[b.length];
        for (int i3 = 0; i3 < b.length; i3++) {
            bDouble[i3] = b[i3].getValue();
        }
        out.value = MathArrays.linearCombination(aDouble, bDouble);
        return out;
    }

    public SparseGradient linearCombination(double[] a, SparseGradient[] b) {
        SparseGradient out = (SparseGradient) b[0].getField().getZero();
        for (int i = 0; i < a.length; i++) {
            out = out.add(b[i].multiply(a[i]));
        }
        double[] bDouble = new double[b.length];
        for (int i2 = 0; i2 < b.length; i2++) {
            bDouble[i2] = b[i2].getValue();
        }
        out.value = MathArrays.linearCombination(a, bDouble);
        return out;
    }

    public SparseGradient linearCombination(SparseGradient a1, SparseGradient b1, SparseGradient a2, SparseGradient b2) {
        SparseGradient out = a1.multiply(b1).add(a2.multiply(b2));
        out.value = MathArrays.linearCombination(a1.value, b1.value, a2.value, b2.value);
        return out;
    }

    public SparseGradient linearCombination(double a1, SparseGradient b1, double a2, SparseGradient b2) {
        SparseGradient out = b1.multiply(a1).add(b2.multiply(a2));
        out.value = MathArrays.linearCombination(a1, b1.value, a2, b2.value);
        return out;
    }

    public SparseGradient linearCombination(SparseGradient a1, SparseGradient b1, SparseGradient a2, SparseGradient b2, SparseGradient a3, SparseGradient b3) {
        SparseGradient out = a1.multiply(b1).add(a2.multiply(b2)).add(a3.multiply(b3));
        out.value = MathArrays.linearCombination(a1.value, b1.value, a2.value, b2.value, a3.value, b3.value);
        return out;
    }

    public SparseGradient linearCombination(double a1, SparseGradient b1, double a2, SparseGradient b2, double a3, SparseGradient b3) {
        SparseGradient sparseGradient = b1;
        SparseGradient sparseGradient2 = b2;
        SparseGradient sparseGradient3 = b3;
        double d = a1;
        double d2 = a2;
        SparseGradient out = sparseGradient.multiply(d).add(sparseGradient2.multiply(d2)).add(sparseGradient3.multiply(a3));
        double d3 = sparseGradient.value;
        double d4 = sparseGradient2.value;
        double d5 = d4;
        out.value = MathArrays.linearCombination(d, d3, d2, d5, a3, sparseGradient3.value);
        return out;
    }

    public SparseGradient linearCombination(SparseGradient a1, SparseGradient b1, SparseGradient a2, SparseGradient b2, SparseGradient a3, SparseGradient b3, SparseGradient a4, SparseGradient b4) {
        SparseGradient out = a1.multiply(b1).add(a2.multiply(b2)).add(a3.multiply(b3)).add(a4.multiply(b4));
        out.value = MathArrays.linearCombination(a1.value, b1.value, a2.value, b2.value, a3.value, b3.value, a4.value, b4.value);
        return out;
    }

    public SparseGradient linearCombination(double a1, SparseGradient b1, double a2, SparseGradient b2, double a3, SparseGradient b3, double a4, SparseGradient b4) {
        SparseGradient sparseGradient = b1;
        SparseGradient sparseGradient2 = b2;
        SparseGradient sparseGradient3 = b3;
        SparseGradient sparseGradient4 = b4;
        double d = a1;
        double d2 = a2;
        SparseGradient out = sparseGradient.multiply(d).add(sparseGradient2.multiply(d2)).add(sparseGradient3.multiply(a3)).add(sparseGradient4.multiply(a4));
        double d3 = sparseGradient.value;
        double d4 = sparseGradient2.value;
        double d5 = sparseGradient3.value;
        SparseGradient out2 = out;
        out2.value = MathArrays.linearCombination(d, d3, d2, d4, a3, d5, a4, sparseGradient4.value);
        return out2;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SparseGradient)) {
            return false;
        }
        SparseGradient rhs = (SparseGradient) other;
        if (!Precision.equals(this.value, rhs.value, 1) || this.derivatives.size() != rhs.derivatives.size()) {
            return false;
        }
        for (Entry<Integer, Double> entry : this.derivatives.entrySet()) {
            if (!rhs.derivatives.containsKey(entry.getKey())) {
                return false;
            }
            if (!Precision.equals(((Double) entry.getValue()).doubleValue(), ((Double) rhs.derivatives.get(entry.getKey())).doubleValue(), 1)) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        return (MathUtils.hash(this.value) * 809) + 743 + (this.derivatives.hashCode() * ShapeTypes.ACTION_BUTTON_END);
    }
}

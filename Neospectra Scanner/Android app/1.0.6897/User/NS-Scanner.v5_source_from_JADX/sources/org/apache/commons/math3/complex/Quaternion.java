package org.apache.commons.math3.complex;

import java.io.Serializable;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;

public final class Quaternion implements Serializable {

    /* renamed from: I */
    public static final Quaternion f538I;
    public static final Quaternion IDENTITY;

    /* renamed from: J */
    public static final Quaternion f539J;

    /* renamed from: K */
    public static final Quaternion f540K;
    public static final Quaternion ZERO;
    private static final long serialVersionUID = 20092012;

    /* renamed from: q0 */
    private final double f541q0;

    /* renamed from: q1 */
    private final double f542q1;

    /* renamed from: q2 */
    private final double f543q2;

    /* renamed from: q3 */
    private final double f544q3;

    static {
        Quaternion quaternion = new Quaternion(1.0d, 0.0d, 0.0d, 0.0d);
        IDENTITY = quaternion;
        Quaternion quaternion2 = new Quaternion(0.0d, 0.0d, 0.0d, 0.0d);
        ZERO = quaternion2;
        Quaternion quaternion3 = new Quaternion(0.0d, 1.0d, 0.0d, 0.0d);
        f538I = quaternion3;
        Quaternion quaternion4 = new Quaternion(0.0d, 0.0d, 1.0d, 0.0d);
        f539J = quaternion4;
        Quaternion quaternion5 = new Quaternion(0.0d, 0.0d, 0.0d, 1.0d);
        f540K = quaternion5;
    }

    public Quaternion(double a, double b, double c, double d) {
        this.f541q0 = a;
        this.f542q1 = b;
        this.f543q2 = c;
        this.f544q3 = d;
    }

    public Quaternion(double scalar, double[] v) throws DimensionMismatchException {
        if (v.length != 3) {
            throw new DimensionMismatchException(v.length, 3);
        }
        this.f541q0 = scalar;
        this.f542q1 = v[0];
        this.f543q2 = v[1];
        this.f544q3 = v[2];
    }

    public Quaternion(double[] v) {
        this(0.0d, v);
    }

    public Quaternion getConjugate() {
        Quaternion quaternion = new Quaternion(this.f541q0, -this.f542q1, -this.f543q2, -this.f544q3);
        return quaternion;
    }

    public static Quaternion multiply(Quaternion q1, Quaternion q2) {
        double q1a = q1.getQ0();
        double q1b = q1.getQ1();
        double q1c = q1.getQ2();
        double q1d = q1.getQ3();
        double q2a = q2.getQ0();
        double q2b = q2.getQ1();
        double q2c = q2.getQ2();
        double q2d = q2.getQ3();
        Quaternion quaternion = new Quaternion((((q1a * q2a) - (q1b * q2b)) - (q1c * q2c)) - (q1d * q2d), (((q1a * q2b) + (q1b * q2a)) + (q1c * q2d)) - (q1d * q2c), ((q1a * q2c) - (q1b * q2d)) + (q1c * q2a) + (q1d * q2b), (((q1a * q2d) + (q1b * q2c)) - (q1c * q2b)) + (q1d * q2a));
        return quaternion;
    }

    public Quaternion multiply(Quaternion q) {
        return multiply(this, q);
    }

    public static Quaternion add(Quaternion q1, Quaternion q2) {
        Quaternion quaternion = new Quaternion(q1.getQ0() + q2.getQ0(), q1.getQ1() + q2.getQ1(), q1.getQ2() + q2.getQ2(), q1.getQ3() + q2.getQ3());
        return quaternion;
    }

    public Quaternion add(Quaternion q) {
        return add(this, q);
    }

    public static Quaternion subtract(Quaternion q1, Quaternion q2) {
        Quaternion quaternion = new Quaternion(q1.getQ0() - q2.getQ0(), q1.getQ1() - q2.getQ1(), q1.getQ2() - q2.getQ2(), q1.getQ3() - q2.getQ3());
        return quaternion;
    }

    public Quaternion subtract(Quaternion q) {
        return subtract(this, q);
    }

    public static double dotProduct(Quaternion q1, Quaternion q2) {
        return (q1.getQ0() * q2.getQ0()) + (q1.getQ1() * q2.getQ1()) + (q1.getQ2() * q2.getQ2()) + (q1.getQ3() * q2.getQ3());
    }

    public double dotProduct(Quaternion q) {
        return dotProduct(this, q);
    }

    public double getNorm() {
        return FastMath.sqrt((this.f541q0 * this.f541q0) + (this.f542q1 * this.f542q1) + (this.f543q2 * this.f543q2) + (this.f544q3 * this.f544q3));
    }

    public Quaternion normalize() {
        double norm = getNorm();
        if (norm < Precision.SAFE_MIN) {
            throw new ZeroException(LocalizedFormats.NORM, Double.valueOf(norm));
        }
        Quaternion quaternion = new Quaternion(this.f541q0 / norm, this.f542q1 / norm, this.f543q2 / norm, this.f544q3 / norm);
        return quaternion;
    }

    public boolean equals(Object other) {
        boolean z = true;
        if (this == other) {
            return true;
        }
        if (!(other instanceof Quaternion)) {
            return false;
        }
        Quaternion q = (Quaternion) other;
        if (!(this.f541q0 == q.getQ0() && this.f542q1 == q.getQ1() && this.f543q2 == q.getQ2() && this.f544q3 == q.getQ3())) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result = 17;
        int i$ = 0;
        double[] arr$ = {this.f541q0, this.f542q1, this.f543q2, this.f544q3};
        while (true) {
            int i$2 = i$;
            if (i$2 >= arr$.length) {
                return result;
            }
            result = (result * 31) + MathUtils.hash(arr$[i$2]);
            i$ = i$2 + 1;
        }
    }

    public boolean equals(Quaternion q, double eps) {
        return Precision.equals(this.f541q0, q.getQ0(), eps) && Precision.equals(this.f542q1, q.getQ1(), eps) && Precision.equals(this.f543q2, q.getQ2(), eps) && Precision.equals(this.f544q3, q.getQ3(), eps);
    }

    public boolean isUnitQuaternion(double eps) {
        return Precision.equals(getNorm(), 1.0d, eps);
    }

    public boolean isPureQuaternion(double eps) {
        return FastMath.abs(getQ0()) <= eps;
    }

    public Quaternion getPositivePolarForm() {
        if (getQ0() >= 0.0d) {
            return normalize();
        }
        Quaternion unitQ = normalize();
        Quaternion quaternion = new Quaternion(-unitQ.getQ0(), -unitQ.getQ1(), -unitQ.getQ2(), -unitQ.getQ3());
        return quaternion;
    }

    public Quaternion getInverse() {
        double squareNorm = (this.f541q0 * this.f541q0) + (this.f542q1 * this.f542q1) + (this.f543q2 * this.f543q2) + (this.f544q3 * this.f544q3);
        if (squareNorm < Precision.SAFE_MIN) {
            throw new ZeroException(LocalizedFormats.NORM, Double.valueOf(squareNorm));
        }
        Quaternion quaternion = new Quaternion(this.f541q0 / squareNorm, (-this.f542q1) / squareNorm, (-this.f543q2) / squareNorm, (-this.f544q3) / squareNorm);
        return quaternion;
    }

    public double getQ0() {
        return this.f541q0;
    }

    public double getQ1() {
        return this.f542q1;
    }

    public double getQ2() {
        return this.f543q2;
    }

    public double getQ3() {
        return this.f544q3;
    }

    public double getScalarPart() {
        return getQ0();
    }

    public double[] getVectorPart() {
        return new double[]{getQ1(), getQ2(), getQ3()};
    }

    public Quaternion multiply(double alpha) {
        Quaternion quaternion = new Quaternion(alpha * this.f541q0, this.f542q1 * alpha, this.f543q2 * alpha, this.f544q3 * alpha);
        return quaternion;
    }

    public String toString() {
        String str = " ";
        StringBuilder s = new StringBuilder();
        s.append("[");
        s.append(this.f541q0);
        s.append(" ");
        s.append(this.f542q1);
        s.append(" ");
        s.append(this.f543q2);
        s.append(" ");
        s.append(this.f544q3);
        s.append("]");
        return s.toString();
    }
}

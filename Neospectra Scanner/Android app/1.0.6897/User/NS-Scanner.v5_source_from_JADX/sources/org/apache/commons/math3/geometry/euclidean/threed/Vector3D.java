package org.apache.commons.math3.geometry.euclidean.threed;

import java.io.Serializable;
import java.text.NumberFormat;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.Space;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;

public class Vector3D implements Serializable, Vector<Euclidean3D> {
    public static final Vector3D MINUS_I;
    public static final Vector3D MINUS_J;
    public static final Vector3D MINUS_K;
    public static final Vector3D NEGATIVE_INFINITY;
    public static final Vector3D NaN;
    public static final Vector3D PLUS_I;
    public static final Vector3D PLUS_J;
    public static final Vector3D PLUS_K;
    public static final Vector3D POSITIVE_INFINITY;
    public static final Vector3D ZERO;
    private static final long serialVersionUID = 1313493323784566947L;

    /* renamed from: x */
    private final double f598x;

    /* renamed from: y */
    private final double f599y;

    /* renamed from: z */
    private final double f600z;

    static {
        Vector3D vector3D = new Vector3D(0.0d, 0.0d, 0.0d);
        ZERO = vector3D;
        Vector3D vector3D2 = new Vector3D(1.0d, 0.0d, 0.0d);
        PLUS_I = vector3D2;
        Vector3D vector3D3 = new Vector3D(-1.0d, 0.0d, 0.0d);
        MINUS_I = vector3D3;
        Vector3D vector3D4 = new Vector3D(0.0d, 1.0d, 0.0d);
        PLUS_J = vector3D4;
        Vector3D vector3D5 = new Vector3D(0.0d, -1.0d, 0.0d);
        MINUS_J = vector3D5;
        Vector3D vector3D6 = new Vector3D(0.0d, 0.0d, 1.0d);
        PLUS_K = vector3D6;
        Vector3D vector3D7 = new Vector3D(0.0d, 0.0d, -1.0d);
        MINUS_K = vector3D7;
        Vector3D vector3D8 = new Vector3D(Double.NaN, Double.NaN, Double.NaN);
        NaN = vector3D8;
        Vector3D vector3D9 = new Vector3D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        POSITIVE_INFINITY = vector3D9;
        Vector3D vector3D10 = new Vector3D(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        NEGATIVE_INFINITY = vector3D10;
    }

    public Vector3D(double x, double y, double z) {
        this.f598x = x;
        this.f599y = y;
        this.f600z = z;
    }

    public Vector3D(double[] v) throws DimensionMismatchException {
        if (v.length != 3) {
            throw new DimensionMismatchException(v.length, 3);
        }
        this.f598x = v[0];
        this.f599y = v[1];
        this.f600z = v[2];
    }

    public Vector3D(double alpha, double delta) {
        double cosDelta = FastMath.cos(delta);
        this.f598x = FastMath.cos(alpha) * cosDelta;
        this.f599y = FastMath.sin(alpha) * cosDelta;
        this.f600z = FastMath.sin(delta);
    }

    public Vector3D(double a, Vector3D u) {
        this.f598x = u.f598x * a;
        this.f599y = u.f599y * a;
        this.f600z = u.f600z * a;
    }

    public Vector3D(double a1, Vector3D u1, double a2, Vector3D u2) {
        Vector3D vector3D = u1;
        Vector3D vector3D2 = u2;
        this.f598x = MathArrays.linearCombination(a1, vector3D.f598x, a2, vector3D2.f598x);
        double d = a1;
        double d2 = a2;
        this.f599y = MathArrays.linearCombination(d, vector3D.f599y, d2, vector3D2.f599y);
        this.f600z = MathArrays.linearCombination(d, vector3D.f600z, d2, vector3D2.f600z);
    }

    public Vector3D(double a1, Vector3D u1, double a2, Vector3D u2, double a3, Vector3D u3) {
        Vector3D vector3D = u1;
        Vector3D vector3D2 = u2;
        Vector3D vector3D3 = u3;
        this.f598x = MathArrays.linearCombination(a1, vector3D.f598x, a2, vector3D2.f598x, a3, vector3D3.f598x);
        double d = a1;
        double d2 = a2;
        double d3 = a3;
        this.f599y = MathArrays.linearCombination(d, vector3D.f599y, d2, vector3D2.f599y, d3, vector3D3.f599y);
        this.f600z = MathArrays.linearCombination(d, vector3D.f600z, d2, vector3D2.f600z, d3, vector3D3.f600z);
    }

    public Vector3D(double a1, Vector3D u1, double a2, Vector3D u2, double a3, Vector3D u3, double a4, Vector3D u4) {
        Vector3D vector3D = u1;
        Vector3D vector3D2 = u2;
        Vector3D vector3D3 = u3;
        Vector3D vector3D4 = u4;
        double d = vector3D.f598x;
        double d2 = vector3D2.f598x;
        double d3 = vector3D3.f598x;
        double d4 = d3;
        this.f598x = MathArrays.linearCombination(a1, d, a2, d2, a3, d4, a4, vector3D4.f598x);
        double d5 = a1;
        double d6 = a3;
        double d7 = a4;
        this.f599y = MathArrays.linearCombination(d5, vector3D.f599y, a2, vector3D2.f599y, d6, vector3D3.f599y, d7, vector3D4.f599y);
        this.f600z = MathArrays.linearCombination(d5, vector3D.f600z, a2, vector3D2.f600z, d6, vector3D3.f600z, d7, vector3D4.f600z);
    }

    public double getX() {
        return this.f598x;
    }

    public double getY() {
        return this.f599y;
    }

    public double getZ() {
        return this.f600z;
    }

    public double[] toArray() {
        return new double[]{this.f598x, this.f599y, this.f600z};
    }

    public Space getSpace() {
        return Euclidean3D.getInstance();
    }

    public Vector3D getZero() {
        return ZERO;
    }

    public double getNorm1() {
        return FastMath.abs(this.f598x) + FastMath.abs(this.f599y) + FastMath.abs(this.f600z);
    }

    public double getNorm() {
        return FastMath.sqrt((this.f598x * this.f598x) + (this.f599y * this.f599y) + (this.f600z * this.f600z));
    }

    public double getNormSq() {
        return (this.f598x * this.f598x) + (this.f599y * this.f599y) + (this.f600z * this.f600z);
    }

    public double getNormInf() {
        return FastMath.max(FastMath.max(FastMath.abs(this.f598x), FastMath.abs(this.f599y)), FastMath.abs(this.f600z));
    }

    public double getAlpha() {
        return FastMath.atan2(this.f599y, this.f598x);
    }

    public double getDelta() {
        return FastMath.asin(this.f600z / getNorm());
    }

    public Vector3D add(Vector<Euclidean3D> v) {
        Vector3D v3 = (Vector3D) v;
        Vector3D vector3D = new Vector3D(this.f598x + v3.f598x, this.f599y + v3.f599y, this.f600z + v3.f600z);
        return vector3D;
    }

    public Vector3D add(double factor, Vector<Euclidean3D> v) {
        Vector3D vector3D = new Vector3D(1.0d, this, factor, (Vector3D) v);
        return vector3D;
    }

    public Vector3D subtract(Vector<Euclidean3D> v) {
        Vector3D v3 = (Vector3D) v;
        Vector3D vector3D = new Vector3D(this.f598x - v3.f598x, this.f599y - v3.f599y, this.f600z - v3.f600z);
        return vector3D;
    }

    public Vector3D subtract(double factor, Vector<Euclidean3D> v) {
        Vector3D vector3D = new Vector3D(1.0d, this, -factor, (Vector3D) v);
        return vector3D;
    }

    public Vector3D normalize() throws MathArithmeticException {
        double s = getNorm();
        if (s != 0.0d) {
            return scalarMultiply(1.0d / s);
        }
        throw new MathArithmeticException(LocalizedFormats.CANNOT_NORMALIZE_A_ZERO_NORM_VECTOR, new Object[0]);
    }

    public Vector3D orthogonal() throws MathArithmeticException {
        double threshold = getNorm() * 0.6d;
        if (threshold == 0.0d) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NORM, new Object[0]);
        } else if (FastMath.abs(this.f598x) <= threshold) {
            double inverse = 1.0d / FastMath.sqrt((this.f599y * this.f599y) + (this.f600z * this.f600z));
            Vector3D vector3D = new Vector3D(0.0d, this.f600z * inverse, (-inverse) * this.f599y);
            return vector3D;
        } else if (FastMath.abs(this.f599y) <= threshold) {
            double inverse2 = 1.0d / FastMath.sqrt((this.f598x * this.f598x) + (this.f600z * this.f600z));
            Vector3D vector3D2 = new Vector3D((-inverse2) * this.f600z, 0.0d, this.f598x * inverse2);
            return vector3D2;
        } else {
            double inverse3 = 1.0d / FastMath.sqrt((this.f598x * this.f598x) + (this.f599y * this.f599y));
            Vector3D vector3D3 = new Vector3D(inverse3 * this.f599y, (-inverse3) * this.f598x, 0.0d);
            return vector3D3;
        }
    }

    public static double angle(Vector3D v1, Vector3D v2) throws MathArithmeticException {
        double normProduct = v1.getNorm() * v2.getNorm();
        if (normProduct == 0.0d) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NORM, new Object[0]);
        }
        double dot = v1.dotProduct(v2);
        double threshold = 0.9999d * normProduct;
        if (dot >= (-threshold) && dot <= threshold) {
            return FastMath.acos(dot / normProduct);
        }
        Vector3D v3 = crossProduct(v1, v2);
        if (dot >= 0.0d) {
            return FastMath.asin(v3.getNorm() / normProduct);
        }
        return 3.141592653589793d - FastMath.asin(v3.getNorm() / normProduct);
    }

    public Vector3D negate() {
        Vector3D vector3D = new Vector3D(-this.f598x, -this.f599y, -this.f600z);
        return vector3D;
    }

    public Vector3D scalarMultiply(double a) {
        Vector3D vector3D = new Vector3D(a * this.f598x, this.f599y * a, this.f600z * a);
        return vector3D;
    }

    public boolean isNaN() {
        return Double.isNaN(this.f598x) || Double.isNaN(this.f599y) || Double.isNaN(this.f600z);
    }

    public boolean isInfinite() {
        return !isNaN() && (Double.isInfinite(this.f598x) || Double.isInfinite(this.f599y) || Double.isInfinite(this.f600z));
    }

    public boolean equals(Object other) {
        boolean z = true;
        if (this == other) {
            return true;
        }
        if (!(other instanceof Vector3D)) {
            return false;
        }
        Vector3D rhs = (Vector3D) other;
        if (rhs.isNaN()) {
            return isNaN();
        }
        if (!(this.f598x == rhs.f598x && this.f599y == rhs.f599y && this.f600z == rhs.f600z)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        if (isNaN()) {
            return 642;
        }
        return ((MathUtils.hash(this.f598x) * 164) + (MathUtils.hash(this.f599y) * 3) + MathUtils.hash(this.f600z)) * 643;
    }

    public double dotProduct(Vector<Euclidean3D> v) {
        Vector3D v3 = (Vector3D) v;
        return MathArrays.linearCombination(this.f598x, v3.f598x, this.f599y, v3.f599y, this.f600z, v3.f600z);
    }

    public Vector3D crossProduct(Vector<Euclidean3D> v) {
        Vector3D v3 = (Vector3D) v;
        Vector3D vector3D = new Vector3D(MathArrays.linearCombination(this.f599y, v3.f600z, -this.f600z, v3.f599y), MathArrays.linearCombination(this.f600z, v3.f598x, -this.f598x, v3.f600z), MathArrays.linearCombination(this.f598x, v3.f599y, -this.f599y, v3.f598x));
        return vector3D;
    }

    public double distance1(Vector<Euclidean3D> v) {
        Vector3D v3 = (Vector3D) v;
        return FastMath.abs(v3.f598x - this.f598x) + FastMath.abs(v3.f599y - this.f599y) + FastMath.abs(v3.f600z - this.f600z);
    }

    public double distance(Vector<Euclidean3D> v) {
        return distance((Point<Euclidean3D>) v);
    }

    public double distance(Point<Euclidean3D> v) {
        Vector3D v3 = (Vector3D) v;
        double dx = v3.f598x - this.f598x;
        double dy = v3.f599y - this.f599y;
        double dz = v3.f600z - this.f600z;
        return FastMath.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    }

    public double distanceInf(Vector<Euclidean3D> v) {
        Vector3D v3 = (Vector3D) v;
        double dx = FastMath.abs(v3.f598x - this.f598x);
        double dy = FastMath.abs(v3.f599y - this.f599y);
        return FastMath.max(FastMath.max(dx, dy), FastMath.abs(v3.f600z - this.f600z));
    }

    public double distanceSq(Vector<Euclidean3D> v) {
        Vector3D v3 = (Vector3D) v;
        double dx = v3.f598x - this.f598x;
        double dy = v3.f599y - this.f599y;
        double dz = v3.f600z - this.f600z;
        return (dx * dx) + (dy * dy) + (dz * dz);
    }

    public static double dotProduct(Vector3D v1, Vector3D v2) {
        return v1.dotProduct(v2);
    }

    public static Vector3D crossProduct(Vector3D v1, Vector3D v2) {
        return v1.crossProduct(v2);
    }

    public static double distance1(Vector3D v1, Vector3D v2) {
        return v1.distance1(v2);
    }

    public static double distance(Vector3D v1, Vector3D v2) {
        return v1.distance((Vector<Euclidean3D>) v2);
    }

    public static double distanceInf(Vector3D v1, Vector3D v2) {
        return v1.distanceInf(v2);
    }

    public static double distanceSq(Vector3D v1, Vector3D v2) {
        return v1.distanceSq(v2);
    }

    public String toString() {
        return Vector3DFormat.getInstance().format(this);
    }

    public String toString(NumberFormat format) {
        return new Vector3DFormat(format).format(this);
    }
}

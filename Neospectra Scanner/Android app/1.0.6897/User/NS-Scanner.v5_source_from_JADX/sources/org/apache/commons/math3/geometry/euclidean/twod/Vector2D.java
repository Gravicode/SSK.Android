package org.apache.commons.math3.geometry.euclidean.twod;

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
import org.apache.poi.p009ss.usermodel.ShapeTypes;

public class Vector2D implements Vector<Euclidean2D> {
    public static final Vector2D NEGATIVE_INFINITY = new Vector2D(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
    public static final Vector2D NaN = new Vector2D(Double.NaN, Double.NaN);
    public static final Vector2D POSITIVE_INFINITY = new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    public static final Vector2D ZERO = new Vector2D(0.0d, 0.0d);
    private static final long serialVersionUID = 266938651998679754L;

    /* renamed from: x */
    private final double f601x;

    /* renamed from: y */
    private final double f602y;

    public Vector2D(double x, double y) {
        this.f601x = x;
        this.f602y = y;
    }

    public Vector2D(double[] v) throws DimensionMismatchException {
        if (v.length != 2) {
            throw new DimensionMismatchException(v.length, 2);
        }
        this.f601x = v[0];
        this.f602y = v[1];
    }

    public Vector2D(double a, Vector2D u) {
        this.f601x = u.f601x * a;
        this.f602y = u.f602y * a;
    }

    public Vector2D(double a1, Vector2D u1, double a2, Vector2D u2) {
        this.f601x = (u1.f601x * a1) + (u2.f601x * a2);
        this.f602y = (u1.f602y * a1) + (u2.f602y * a2);
    }

    public Vector2D(double a1, Vector2D u1, double a2, Vector2D u2, double a3, Vector2D u3) {
        this.f601x = (u1.f601x * a1) + (u2.f601x * a2) + (u3.f601x * a3);
        this.f602y = (u1.f602y * a1) + (u2.f602y * a2) + (u3.f602y * a3);
    }

    public Vector2D(double a1, Vector2D u1, double a2, Vector2D u2, double a3, Vector2D u3, double a4, Vector2D u4) {
        Vector2D vector2D = u1;
        Vector2D vector2D2 = u2;
        Vector2D vector2D3 = u3;
        Vector2D vector2D4 = u4;
        this.f601x = (vector2D.f601x * a1) + (vector2D2.f601x * a2) + (vector2D3.f601x * a3) + (vector2D4.f601x * a4);
        this.f602y = (vector2D.f602y * a1) + (vector2D2.f602y * a2) + (vector2D3.f602y * a3) + (vector2D4.f602y * a4);
    }

    public double getX() {
        return this.f601x;
    }

    public double getY() {
        return this.f602y;
    }

    public double[] toArray() {
        return new double[]{this.f601x, this.f602y};
    }

    public Space getSpace() {
        return Euclidean2D.getInstance();
    }

    public Vector2D getZero() {
        return ZERO;
    }

    public double getNorm1() {
        return FastMath.abs(this.f601x) + FastMath.abs(this.f602y);
    }

    public double getNorm() {
        return FastMath.sqrt((this.f601x * this.f601x) + (this.f602y * this.f602y));
    }

    public double getNormSq() {
        return (this.f601x * this.f601x) + (this.f602y * this.f602y);
    }

    public double getNormInf() {
        return FastMath.max(FastMath.abs(this.f601x), FastMath.abs(this.f602y));
    }

    public Vector2D add(Vector<Euclidean2D> v) {
        Vector2D v2 = (Vector2D) v;
        return new Vector2D(this.f601x + v2.getX(), this.f602y + v2.getY());
    }

    public Vector2D add(double factor, Vector<Euclidean2D> v) {
        Vector2D v2 = (Vector2D) v;
        return new Vector2D(this.f601x + (v2.getX() * factor), this.f602y + (v2.getY() * factor));
    }

    public Vector2D subtract(Vector<Euclidean2D> p) {
        Vector2D p3 = (Vector2D) p;
        return new Vector2D(this.f601x - p3.f601x, this.f602y - p3.f602y);
    }

    public Vector2D subtract(double factor, Vector<Euclidean2D> v) {
        Vector2D v2 = (Vector2D) v;
        return new Vector2D(this.f601x - (v2.getX() * factor), this.f602y - (v2.getY() * factor));
    }

    public Vector2D normalize() throws MathArithmeticException {
        double s = getNorm();
        if (s != 0.0d) {
            return scalarMultiply(1.0d / s);
        }
        throw new MathArithmeticException(LocalizedFormats.CANNOT_NORMALIZE_A_ZERO_NORM_VECTOR, new Object[0]);
    }

    public static double angle(Vector2D v1, Vector2D v2) throws MathArithmeticException {
        Vector2D vector2D = v1;
        Vector2D vector2D2 = v2;
        double normProduct = v1.getNorm() * v2.getNorm();
        if (normProduct == 0.0d) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NORM, new Object[0]);
        }
        double dot = v1.dotProduct(v2);
        double threshold = 0.9999d * normProduct;
        if (dot >= (-threshold) && dot <= threshold) {
            return FastMath.acos(dot / normProduct);
        }
        double n = FastMath.abs(MathArrays.linearCombination(vector2D.f601x, vector2D2.f602y, -vector2D.f602y, vector2D2.f601x));
        if (dot >= 0.0d) {
            return FastMath.asin(n / normProduct);
        }
        return 3.141592653589793d - FastMath.asin(n / normProduct);
    }

    public Vector2D negate() {
        return new Vector2D(-this.f601x, -this.f602y);
    }

    public Vector2D scalarMultiply(double a) {
        return new Vector2D(this.f601x * a, this.f602y * a);
    }

    public boolean isNaN() {
        return Double.isNaN(this.f601x) || Double.isNaN(this.f602y);
    }

    public boolean isInfinite() {
        return !isNaN() && (Double.isInfinite(this.f601x) || Double.isInfinite(this.f602y));
    }

    public double distance1(Vector<Euclidean2D> p) {
        Vector2D p3 = (Vector2D) p;
        return FastMath.abs(p3.f601x - this.f601x) + FastMath.abs(p3.f602y - this.f602y);
    }

    public double distance(Vector<Euclidean2D> p) {
        return distance((Point<Euclidean2D>) p);
    }

    public double distance(Point<Euclidean2D> p) {
        Vector2D p3 = (Vector2D) p;
        double dx = p3.f601x - this.f601x;
        double dy = p3.f602y - this.f602y;
        return FastMath.sqrt((dx * dx) + (dy * dy));
    }

    public double distanceInf(Vector<Euclidean2D> p) {
        Vector2D p3 = (Vector2D) p;
        return FastMath.max(FastMath.abs(p3.f601x - this.f601x), FastMath.abs(p3.f602y - this.f602y));
    }

    public double distanceSq(Vector<Euclidean2D> p) {
        Vector2D p3 = (Vector2D) p;
        double dx = p3.f601x - this.f601x;
        double dy = p3.f602y - this.f602y;
        return (dx * dx) + (dy * dy);
    }

    public double dotProduct(Vector<Euclidean2D> v) {
        Vector2D v2 = (Vector2D) v;
        return MathArrays.linearCombination(this.f601x, v2.f601x, this.f602y, v2.f602y);
    }

    public double crossProduct(Vector2D p1, Vector2D p2) {
        return MathArrays.linearCombination(p2.getX() - p1.getX(), getY() - p1.getY(), -(getX() - p1.getX()), p2.getY() - p1.getY());
    }

    public static double distance(Vector2D p1, Vector2D p2) {
        return p1.distance((Vector<Euclidean2D>) p2);
    }

    public static double distanceInf(Vector2D p1, Vector2D p2) {
        return p1.distanceInf(p2);
    }

    public static double distanceSq(Vector2D p1, Vector2D p2) {
        return p1.distanceSq(p2);
    }

    public boolean equals(Object other) {
        boolean z = true;
        if (this == other) {
            return true;
        }
        if (!(other instanceof Vector2D)) {
            return false;
        }
        Vector2D rhs = (Vector2D) other;
        if (rhs.isNaN()) {
            return isNaN();
        }
        if (!(this.f601x == rhs.f601x && this.f602y == rhs.f602y)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        if (isNaN()) {
            return 542;
        }
        return ((MathUtils.hash(this.f601x) * 76) + MathUtils.hash(this.f602y)) * ShapeTypes.RIBBON;
    }

    public String toString() {
        return Vector2DFormat.getInstance().format(this);
    }

    public String toString(NumberFormat format) {
        return new Vector2DFormat(format).format(this);
    }
}

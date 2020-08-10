package org.apache.commons.math3.geometry.euclidean.oned;

import java.text.NumberFormat;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.Space;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;

public class Vector1D implements Vector<Euclidean1D> {
    public static final Vector1D NEGATIVE_INFINITY = new Vector1D(Double.NEGATIVE_INFINITY);
    public static final Vector1D NaN = new Vector1D(Double.NaN);
    public static final Vector1D ONE = new Vector1D(1.0d);
    public static final Vector1D POSITIVE_INFINITY = new Vector1D(Double.POSITIVE_INFINITY);
    public static final Vector1D ZERO = new Vector1D(0.0d);
    private static final long serialVersionUID = 7556674948671647925L;

    /* renamed from: x */
    private final double f572x;

    public Vector1D(double x) {
        this.f572x = x;
    }

    public Vector1D(double a, Vector1D u) {
        this.f572x = u.f572x * a;
    }

    public Vector1D(double a1, Vector1D u1, double a2, Vector1D u2) {
        this.f572x = (u1.f572x * a1) + (u2.f572x * a2);
    }

    public Vector1D(double a1, Vector1D u1, double a2, Vector1D u2, double a3, Vector1D u3) {
        this.f572x = (u1.f572x * a1) + (u2.f572x * a2) + (u3.f572x * a3);
    }

    public Vector1D(double a1, Vector1D u1, double a2, Vector1D u2, double a3, Vector1D u3, double a4, Vector1D u4) {
        this.f572x = (u1.f572x * a1) + (u2.f572x * a2) + (u3.f572x * a3) + (u4.f572x * a4);
    }

    public double getX() {
        return this.f572x;
    }

    public Space getSpace() {
        return Euclidean1D.getInstance();
    }

    public Vector1D getZero() {
        return ZERO;
    }

    public double getNorm1() {
        return FastMath.abs(this.f572x);
    }

    public double getNorm() {
        return FastMath.abs(this.f572x);
    }

    public double getNormSq() {
        return this.f572x * this.f572x;
    }

    public double getNormInf() {
        return FastMath.abs(this.f572x);
    }

    public Vector1D add(Vector<Euclidean1D> v) {
        return new Vector1D(this.f572x + ((Vector1D) v).getX());
    }

    public Vector1D add(double factor, Vector<Euclidean1D> v) {
        return new Vector1D(this.f572x + (((Vector1D) v).getX() * factor));
    }

    public Vector1D subtract(Vector<Euclidean1D> p) {
        return new Vector1D(this.f572x - ((Vector1D) p).f572x);
    }

    public Vector1D subtract(double factor, Vector<Euclidean1D> v) {
        return new Vector1D(this.f572x - (((Vector1D) v).getX() * factor));
    }

    public Vector1D normalize() throws MathArithmeticException {
        double s = getNorm();
        if (s != 0.0d) {
            return scalarMultiply(1.0d / s);
        }
        throw new MathArithmeticException(LocalizedFormats.CANNOT_NORMALIZE_A_ZERO_NORM_VECTOR, new Object[0]);
    }

    public Vector1D negate() {
        return new Vector1D(-this.f572x);
    }

    public Vector1D scalarMultiply(double a) {
        return new Vector1D(this.f572x * a);
    }

    public boolean isNaN() {
        return Double.isNaN(this.f572x);
    }

    public boolean isInfinite() {
        return !isNaN() && Double.isInfinite(this.f572x);
    }

    public double distance1(Vector<Euclidean1D> p) {
        return FastMath.abs(((Vector1D) p).f572x - this.f572x);
    }

    @Deprecated
    public double distance(Vector<Euclidean1D> p) {
        return distance((Point<Euclidean1D>) p);
    }

    public double distance(Point<Euclidean1D> p) {
        return FastMath.abs(((Vector1D) p).f572x - this.f572x);
    }

    public double distanceInf(Vector<Euclidean1D> p) {
        return FastMath.abs(((Vector1D) p).f572x - this.f572x);
    }

    public double distanceSq(Vector<Euclidean1D> p) {
        double dx = ((Vector1D) p).f572x - this.f572x;
        return dx * dx;
    }

    public double dotProduct(Vector<Euclidean1D> v) {
        return this.f572x * ((Vector1D) v).f572x;
    }

    public static double distance(Vector1D p1, Vector1D p2) {
        return p1.distance((Vector<Euclidean1D>) p2);
    }

    public static double distanceInf(Vector1D p1, Vector1D p2) {
        return p1.distanceInf(p2);
    }

    public static double distanceSq(Vector1D p1, Vector1D p2) {
        return p1.distanceSq(p2);
    }

    public boolean equals(Object other) {
        boolean z = true;
        if (this == other) {
            return true;
        }
        if (!(other instanceof Vector1D)) {
            return false;
        }
        Vector1D rhs = (Vector1D) other;
        if (rhs.isNaN()) {
            return isNaN();
        }
        if (this.f572x != rhs.f572x) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        if (isNaN()) {
            return 7785;
        }
        return MathUtils.hash(this.f572x) * 997;
    }

    public String toString() {
        return Vector1DFormat.getInstance().format(this);
    }

    public String toString(NumberFormat format) {
        return new Vector1DFormat(format).format(this);
    }
}

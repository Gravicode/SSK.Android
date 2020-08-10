package org.apache.commons.math3.geometry.euclidean.threed;

import java.io.Serializable;
import java.text.NumberFormat;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

public class FieldVector3D<T extends RealFieldElement<T>> implements Serializable {
    private static final long serialVersionUID = 20130224;

    /* renamed from: x */
    private final T f577x;

    /* renamed from: y */
    private final T f578y;

    /* renamed from: z */
    private final T f579z;

    public FieldVector3D(T x, T y, T z) {
        this.f577x = x;
        this.f578y = y;
        this.f579z = z;
    }

    public FieldVector3D(T[] v) throws DimensionMismatchException {
        if (v.length != 3) {
            throw new DimensionMismatchException(v.length, 3);
        }
        this.f577x = v[0];
        this.f578y = v[1];
        this.f579z = v[2];
    }

    public FieldVector3D(T alpha, T delta) {
        T cosDelta = (RealFieldElement) delta.cos();
        this.f577x = (RealFieldElement) ((RealFieldElement) alpha.cos()).multiply(cosDelta);
        this.f578y = (RealFieldElement) ((RealFieldElement) alpha.sin()).multiply(cosDelta);
        this.f579z = (RealFieldElement) delta.sin();
    }

    public FieldVector3D(T a, FieldVector3D<T> u) {
        this.f577x = (RealFieldElement) a.multiply(u.f577x);
        this.f578y = (RealFieldElement) a.multiply(u.f578y);
        this.f579z = (RealFieldElement) a.multiply(u.f579z);
    }

    public FieldVector3D(T a, Vector3D u) {
        this.f577x = (RealFieldElement) a.multiply(u.getX());
        this.f578y = (RealFieldElement) a.multiply(u.getY());
        this.f579z = (RealFieldElement) a.multiply(u.getZ());
    }

    public FieldVector3D(double a, FieldVector3D<T> u) {
        this.f577x = (RealFieldElement) u.f577x.multiply(a);
        this.f578y = (RealFieldElement) u.f578y.multiply(a);
        this.f579z = (RealFieldElement) u.f579z.multiply(a);
    }

    public FieldVector3D(T a1, FieldVector3D<T> u1, T a2, FieldVector3D<T> u2) {
        T prototype = a1;
        this.f577x = (RealFieldElement) prototype.linearCombination(a1, u1.getX(), a2, u2.getX());
        this.f578y = (RealFieldElement) prototype.linearCombination(a1, u1.getY(), a2, u2.getY());
        this.f579z = (RealFieldElement) prototype.linearCombination(a1, u1.getZ(), a2, u2.getZ());
    }

    public FieldVector3D(T a1, Vector3D u1, T a2, Vector3D u2) {
        T prototype = a1;
        this.f577x = (RealFieldElement) prototype.linearCombination(u1.getX(), a1, u2.getX(), a2);
        T t = a1;
        T t2 = a2;
        this.f578y = (RealFieldElement) prototype.linearCombination(u1.getY(), t, u2.getY(), t2);
        this.f579z = (RealFieldElement) prototype.linearCombination(u1.getZ(), t, u2.getZ(), t2);
    }

    public FieldVector3D(double a1, FieldVector3D<T> u1, double a2, FieldVector3D<T> u2) {
        T prototype = u1.getX();
        double d = a1;
        double d2 = a2;
        this.f577x = (RealFieldElement) prototype.linearCombination(d, u1.getX(), d2, u2.getX());
        this.f578y = (RealFieldElement) prototype.linearCombination(d, u1.getY(), d2, u2.getY());
        this.f579z = (RealFieldElement) prototype.linearCombination(d, u1.getZ(), d2, u2.getZ());
    }

    public FieldVector3D(T a1, FieldVector3D<T> u1, T a2, FieldVector3D<T> u2, T a3, FieldVector3D<T> u3) {
        T prototype = a1;
        this.f577x = (RealFieldElement) prototype.linearCombination(a1, u1.getX(), a2, u2.getX(), a3, u3.getX());
        T t = a1;
        T t2 = a2;
        T t3 = a3;
        this.f578y = (RealFieldElement) prototype.linearCombination(t, u1.getY(), t2, u2.getY(), t3, u3.getY());
        this.f579z = (RealFieldElement) prototype.linearCombination(t, u1.getZ(), t2, u2.getZ(), t3, u3.getZ());
    }

    public FieldVector3D(T a1, Vector3D u1, T a2, Vector3D u2, T a3, Vector3D u3) {
        T prototype = a1;
        this.f577x = (RealFieldElement) prototype.linearCombination(u1.getX(), a1, u2.getX(), a2, u3.getX(), a3);
        T t = a1;
        T t2 = a2;
        T t3 = a3;
        this.f578y = (RealFieldElement) prototype.linearCombination(u1.getY(), t, u2.getY(), t2, u3.getY(), t3);
        this.f579z = (RealFieldElement) prototype.linearCombination(u1.getZ(), t, u2.getZ(), t2, u3.getZ(), t3);
    }

    public FieldVector3D(double a1, FieldVector3D<T> u1, double a2, FieldVector3D<T> u2, double a3, FieldVector3D<T> u3) {
        T prototype = u1.getX();
        double d = a1;
        double d2 = a2;
        double d3 = a3;
        this.f577x = (RealFieldElement) prototype.linearCombination(d, u1.getX(), d2, u2.getX(), d3, u3.getX());
        this.f578y = (RealFieldElement) prototype.linearCombination(d, u1.getY(), d2, u2.getY(), d3, u3.getY());
        this.f579z = (RealFieldElement) prototype.linearCombination(d, u1.getZ(), d2, u2.getZ(), d3, u3.getZ());
    }

    public FieldVector3D(T a1, FieldVector3D<T> u1, T a2, FieldVector3D<T> u2, T a3, FieldVector3D<T> u3, T a4, FieldVector3D<T> u4) {
        T prototype = a1;
        this.f577x = (RealFieldElement) prototype.linearCombination(a1, u1.getX(), a2, u2.getX(), a3, u3.getX(), a4, u4.getX());
        T t = a1;
        T t2 = a2;
        T t3 = a3;
        T t4 = a4;
        this.f578y = (RealFieldElement) prototype.linearCombination(t, u1.getY(), t2, u2.getY(), t3, u3.getY(), t4, u4.getY());
        this.f579z = (RealFieldElement) prototype.linearCombination(t, u1.getZ(), t2, u2.getZ(), t3, u3.getZ(), t4, u4.getZ());
    }

    public FieldVector3D(T a1, Vector3D u1, T a2, Vector3D u2, T a3, Vector3D u3, T a4, Vector3D u4) {
        T prototype = a1;
        this.f577x = (RealFieldElement) prototype.linearCombination(u1.getX(), a1, u2.getX(), a2, u3.getX(), a3, u4.getX(), a4);
        T t = a1;
        T t2 = a2;
        T t3 = a3;
        T t4 = a4;
        this.f578y = (RealFieldElement) prototype.linearCombination(u1.getY(), t, u2.getY(), t2, u3.getY(), t3, u4.getY(), t4);
        this.f579z = (RealFieldElement) prototype.linearCombination(u1.getZ(), t, u2.getZ(), t2, u3.getZ(), t3, u4.getZ(), t4);
    }

    public FieldVector3D(double a1, FieldVector3D<T> u1, double a2, FieldVector3D<T> u2, double a3, FieldVector3D<T> u3, double a4, FieldVector3D<T> u4) {
        T prototype = u1.getX();
        double d = a1;
        double d2 = a2;
        double d3 = a3;
        double d4 = a4;
        this.f577x = (RealFieldElement) prototype.linearCombination(d, u1.getX(), d2, u2.getX(), d3, u3.getX(), d4, u4.getX());
        this.f578y = (RealFieldElement) prototype.linearCombination(d, u1.getY(), d2, u2.getY(), d3, u3.getY(), d4, u4.getY());
        this.f579z = (RealFieldElement) prototype.linearCombination(d, u1.getZ(), d2, u2.getZ(), d3, u3.getZ(), d4, u4.getZ());
    }

    public T getX() {
        return this.f577x;
    }

    public T getY() {
        return this.f578y;
    }

    public T getZ() {
        return this.f579z;
    }

    public T[] toArray() {
        T[] array = (RealFieldElement[]) MathArrays.buildArray(this.f577x.getField(), 3);
        array[0] = this.f577x;
        array[1] = this.f578y;
        array[2] = this.f579z;
        return array;
    }

    public Vector3D toVector3D() {
        Vector3D vector3D = new Vector3D(this.f577x.getReal(), this.f578y.getReal(), this.f579z.getReal());
        return vector3D;
    }

    public T getNorm1() {
        return (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f577x.abs()).add(this.f578y.abs())).add(this.f579z.abs());
    }

    public T getNorm() {
        return (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f577x.multiply(this.f577x)).add(this.f578y.multiply(this.f578y))).add(this.f579z.multiply(this.f579z))).sqrt();
    }

    public T getNormSq() {
        return (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f577x.multiply(this.f577x)).add(this.f578y.multiply(this.f578y))).add(this.f579z.multiply(this.f579z));
    }

    public T getNormInf() {
        T xAbs = (RealFieldElement) this.f577x.abs();
        T yAbs = (RealFieldElement) this.f578y.abs();
        T zAbs = (RealFieldElement) this.f579z.abs();
        if (xAbs.getReal() <= yAbs.getReal()) {
            if (yAbs.getReal() <= zAbs.getReal()) {
                return zAbs;
            }
            return yAbs;
        } else if (xAbs.getReal() <= zAbs.getReal()) {
            return zAbs;
        } else {
            return xAbs;
        }
    }

    public T getAlpha() {
        return (RealFieldElement) this.f578y.atan2(this.f577x);
    }

    public T getDelta() {
        return (RealFieldElement) ((RealFieldElement) this.f579z.divide(getNorm())).asin();
    }

    public FieldVector3D<T> add(FieldVector3D<T> v) {
        return new FieldVector3D<>((RealFieldElement) this.f577x.add(v.f577x), (RealFieldElement) this.f578y.add(v.f578y), (RealFieldElement) this.f579z.add(v.f579z));
    }

    public FieldVector3D<T> add(Vector3D v) {
        return new FieldVector3D<>((RealFieldElement) this.f577x.add(v.getX()), (RealFieldElement) this.f578y.add(v.getY()), (RealFieldElement) this.f579z.add(v.getZ()));
    }

    public FieldVector3D<T> add(T factor, FieldVector3D<T> v) {
        return new FieldVector3D<>((T) (RealFieldElement) this.f577x.getField().getOne(), this, factor, v);
    }

    public FieldVector3D<T> add(T factor, Vector3D v) {
        return new FieldVector3D<>((RealFieldElement) this.f577x.add(factor.multiply(v.getX())), (RealFieldElement) this.f578y.add(factor.multiply(v.getY())), (RealFieldElement) this.f579z.add(factor.multiply(v.getZ())));
    }

    public FieldVector3D<T> add(double factor, FieldVector3D<T> v) {
        FieldVector3D fieldVector3D = new FieldVector3D(1.0d, this, factor, v);
        return fieldVector3D;
    }

    public FieldVector3D<T> add(double factor, Vector3D v) {
        return new FieldVector3D<>((RealFieldElement) this.f577x.add(v.getX() * factor), (RealFieldElement) this.f578y.add(v.getY() * factor), (RealFieldElement) this.f579z.add(v.getZ() * factor));
    }

    public FieldVector3D<T> subtract(FieldVector3D<T> v) {
        return new FieldVector3D<>((RealFieldElement) this.f577x.subtract(v.f577x), (RealFieldElement) this.f578y.subtract(v.f578y), (RealFieldElement) this.f579z.subtract(v.f579z));
    }

    public FieldVector3D<T> subtract(Vector3D v) {
        return new FieldVector3D<>((RealFieldElement) this.f577x.subtract(v.getX()), (RealFieldElement) this.f578y.subtract(v.getY()), (RealFieldElement) this.f579z.subtract(v.getZ()));
    }

    public FieldVector3D<T> subtract(T factor, FieldVector3D<T> v) {
        return new FieldVector3D<>((T) (RealFieldElement) this.f577x.getField().getOne(), this, (T) (RealFieldElement) factor.negate(), v);
    }

    public FieldVector3D<T> subtract(T factor, Vector3D v) {
        return new FieldVector3D<>((RealFieldElement) this.f577x.subtract(factor.multiply(v.getX())), (RealFieldElement) this.f578y.subtract(factor.multiply(v.getY())), (RealFieldElement) this.f579z.subtract(factor.multiply(v.getZ())));
    }

    public FieldVector3D<T> subtract(double factor, FieldVector3D<T> v) {
        FieldVector3D fieldVector3D = new FieldVector3D(1.0d, this, -factor, v);
        return fieldVector3D;
    }

    public FieldVector3D<T> subtract(double factor, Vector3D v) {
        return new FieldVector3D<>((RealFieldElement) this.f577x.subtract(v.getX() * factor), (RealFieldElement) this.f578y.subtract(v.getY() * factor), (RealFieldElement) this.f579z.subtract(v.getZ() * factor));
    }

    public FieldVector3D<T> normalize() throws MathArithmeticException {
        T s = getNorm();
        if (s.getReal() != 0.0d) {
            return scalarMultiply((T) (RealFieldElement) s.reciprocal());
        }
        throw new MathArithmeticException(LocalizedFormats.CANNOT_NORMALIZE_A_ZERO_NORM_VECTOR, new Object[0]);
    }

    public FieldVector3D<T> orthogonal() throws MathArithmeticException {
        double threshold = getNorm().getReal() * 0.6d;
        if (threshold == 0.0d) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NORM, new Object[0]);
        } else if (FastMath.abs(this.f577x.getReal()) <= threshold) {
            T inverse = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f578y.multiply(this.f578y)).add(this.f579z.multiply(this.f579z))).sqrt()).reciprocal();
            return new FieldVector3D<>((RealFieldElement) inverse.getField().getZero(), (RealFieldElement) inverse.multiply(this.f579z), (RealFieldElement) ((RealFieldElement) inverse.multiply(this.f578y)).negate());
        } else if (FastMath.abs(this.f578y.getReal()) <= threshold) {
            T inverse2 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f577x.multiply(this.f577x)).add(this.f579z.multiply(this.f579z))).sqrt()).reciprocal();
            return new FieldVector3D<>((RealFieldElement) ((RealFieldElement) inverse2.multiply(this.f579z)).negate(), (RealFieldElement) inverse2.getField().getZero(), (RealFieldElement) inverse2.multiply(this.f577x));
        } else {
            T inverse3 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f577x.multiply(this.f577x)).add(this.f578y.multiply(this.f578y))).sqrt()).reciprocal();
            return new FieldVector3D<>((RealFieldElement) inverse3.multiply(this.f578y), (RealFieldElement) ((RealFieldElement) inverse3.multiply(this.f577x)).negate(), (RealFieldElement) inverse3.getField().getZero());
        }
    }

    public static <T extends RealFieldElement<T>> T angle(FieldVector3D<T> v1, FieldVector3D<T> v2) throws MathArithmeticException {
        T normProduct = (RealFieldElement) v1.getNorm().multiply(v2.getNorm());
        if (normProduct.getReal() == 0.0d) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NORM, new Object[0]);
        }
        T dot = dotProduct(v1, v2);
        double threshold = normProduct.getReal() * 0.9999d;
        if (dot.getReal() >= (-threshold) && dot.getReal() <= threshold) {
            return (RealFieldElement) ((RealFieldElement) dot.divide(normProduct)).acos();
        }
        FieldVector3D<T> v3 = crossProduct(v1, v2);
        if (dot.getReal() >= 0.0d) {
            return (RealFieldElement) ((RealFieldElement) v3.getNorm().divide(normProduct)).asin();
        }
        return (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) v3.getNorm().divide(normProduct)).asin()).subtract(3.141592653589793d)).negate();
    }

    public static <T extends RealFieldElement<T>> T angle(FieldVector3D<T> v1, Vector3D v2) throws MathArithmeticException {
        T normProduct = (RealFieldElement) v1.getNorm().multiply(v2.getNorm());
        if (normProduct.getReal() == 0.0d) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NORM, new Object[0]);
        }
        T dot = dotProduct(v1, v2);
        double threshold = normProduct.getReal() * 0.9999d;
        if (dot.getReal() >= (-threshold) && dot.getReal() <= threshold) {
            return (RealFieldElement) ((RealFieldElement) dot.divide(normProduct)).acos();
        }
        FieldVector3D<T> v3 = crossProduct(v1, v2);
        if (dot.getReal() >= 0.0d) {
            return (RealFieldElement) ((RealFieldElement) v3.getNorm().divide(normProduct)).asin();
        }
        return (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) v3.getNorm().divide(normProduct)).asin()).subtract(3.141592653589793d)).negate();
    }

    public static <T extends RealFieldElement<T>> T angle(Vector3D v1, FieldVector3D<T> v2) throws MathArithmeticException {
        return angle(v2, v1);
    }

    public FieldVector3D<T> negate() {
        return new FieldVector3D<>((RealFieldElement) this.f577x.negate(), (RealFieldElement) this.f578y.negate(), (RealFieldElement) this.f579z.negate());
    }

    public FieldVector3D<T> scalarMultiply(T a) {
        return new FieldVector3D<>((RealFieldElement) this.f577x.multiply(a), (RealFieldElement) this.f578y.multiply(a), (RealFieldElement) this.f579z.multiply(a));
    }

    public FieldVector3D<T> scalarMultiply(double a) {
        return new FieldVector3D<>((RealFieldElement) this.f577x.multiply(a), (RealFieldElement) this.f578y.multiply(a), (RealFieldElement) this.f579z.multiply(a));
    }

    public boolean isNaN() {
        return Double.isNaN(this.f577x.getReal()) || Double.isNaN(this.f578y.getReal()) || Double.isNaN(this.f579z.getReal());
    }

    public boolean isInfinite() {
        return !isNaN() && (Double.isInfinite(this.f577x.getReal()) || Double.isInfinite(this.f578y.getReal()) || Double.isInfinite(this.f579z.getReal()));
    }

    public boolean equals(Object other) {
        boolean z = true;
        if (this == other) {
            return true;
        }
        if (!(other instanceof FieldVector3D)) {
            return false;
        }
        FieldVector3D<T> rhs = (FieldVector3D) other;
        if (rhs.isNaN()) {
            return isNaN();
        }
        if (!this.f577x.equals(rhs.f577x) || !this.f578y.equals(rhs.f578y) || !this.f579z.equals(rhs.f579z)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        if (isNaN()) {
            return 409;
        }
        return ((this.f577x.hashCode() * 107) + (this.f578y.hashCode() * 83) + this.f579z.hashCode()) * 311;
    }

    public T dotProduct(FieldVector3D<T> v) {
        return (RealFieldElement) this.f577x.linearCombination(this.f577x, v.f577x, this.f578y, v.f578y, this.f579z, v.f579z);
    }

    public T dotProduct(Vector3D v) {
        return (RealFieldElement) this.f577x.linearCombination(v.getX(), this.f577x, v.getY(), this.f578y, v.getZ(), this.f579z);
    }

    public FieldVector3D<T> crossProduct(FieldVector3D<T> v) {
        return new FieldVector3D<>((RealFieldElement) this.f577x.linearCombination(this.f578y, v.f579z, this.f579z.negate(), v.f578y), (RealFieldElement) this.f578y.linearCombination(this.f579z, v.f577x, this.f577x.negate(), v.f579z), (RealFieldElement) this.f579z.linearCombination(this.f577x, v.f578y, this.f578y.negate(), v.f577x));
    }

    public FieldVector3D<T> crossProduct(Vector3D v) {
        return new FieldVector3D<>((RealFieldElement) this.f577x.linearCombination(v.getZ(), this.f578y, -v.getY(), this.f579z), (RealFieldElement) this.f578y.linearCombination(v.getX(), this.f579z, -v.getZ(), this.f577x), (RealFieldElement) this.f579z.linearCombination(v.getY(), this.f577x, -v.getX(), this.f578y));
    }

    public T distance1(FieldVector3D<T> v) {
        return (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) v.f577x.subtract(this.f577x)).abs()).add((RealFieldElement) ((RealFieldElement) v.f578y.subtract(this.f578y)).abs())).add((RealFieldElement) ((RealFieldElement) v.f579z.subtract(this.f579z)).abs());
    }

    public T distance1(Vector3D v) {
        return (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f577x.subtract(v.getX())).abs()).add((RealFieldElement) ((RealFieldElement) this.f578y.subtract(v.getY())).abs())).add((RealFieldElement) ((RealFieldElement) this.f579z.subtract(v.getZ())).abs());
    }

    public T distance(FieldVector3D<T> v) {
        T dx = (RealFieldElement) v.f577x.subtract(this.f577x);
        T dy = (RealFieldElement) v.f578y.subtract(this.f578y);
        T dz = (RealFieldElement) v.f579z.subtract(this.f579z);
        return (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) dx.multiply(dx)).add(dy.multiply(dy))).add(dz.multiply(dz))).sqrt();
    }

    public T distance(Vector3D v) {
        T dx = (RealFieldElement) this.f577x.subtract(v.getX());
        T dy = (RealFieldElement) this.f578y.subtract(v.getY());
        T dz = (RealFieldElement) this.f579z.subtract(v.getZ());
        return (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) dx.multiply(dx)).add(dy.multiply(dy))).add(dz.multiply(dz))).sqrt();
    }

    public T distanceInf(FieldVector3D<T> v) {
        T dx = (RealFieldElement) ((RealFieldElement) v.f577x.subtract(this.f577x)).abs();
        T dy = (RealFieldElement) ((RealFieldElement) v.f578y.subtract(this.f578y)).abs();
        T dz = (RealFieldElement) ((RealFieldElement) v.f579z.subtract(this.f579z)).abs();
        if (dx.getReal() <= dy.getReal()) {
            if (dy.getReal() <= dz.getReal()) {
                return dz;
            }
            return dy;
        } else if (dx.getReal() <= dz.getReal()) {
            return dz;
        } else {
            return dx;
        }
    }

    public T distanceInf(Vector3D v) {
        T dx = (RealFieldElement) ((RealFieldElement) this.f577x.subtract(v.getX())).abs();
        T dy = (RealFieldElement) ((RealFieldElement) this.f578y.subtract(v.getY())).abs();
        T dz = (RealFieldElement) ((RealFieldElement) this.f579z.subtract(v.getZ())).abs();
        if (dx.getReal() <= dy.getReal()) {
            if (dy.getReal() <= dz.getReal()) {
                return dz;
            }
            return dy;
        } else if (dx.getReal() <= dz.getReal()) {
            return dz;
        } else {
            return dx;
        }
    }

    public T distanceSq(FieldVector3D<T> v) {
        T dx = (RealFieldElement) v.f577x.subtract(this.f577x);
        T dy = (RealFieldElement) v.f578y.subtract(this.f578y);
        T dz = (RealFieldElement) v.f579z.subtract(this.f579z);
        return (RealFieldElement) ((RealFieldElement) ((RealFieldElement) dx.multiply(dx)).add(dy.multiply(dy))).add(dz.multiply(dz));
    }

    public T distanceSq(Vector3D v) {
        T dx = (RealFieldElement) this.f577x.subtract(v.getX());
        T dy = (RealFieldElement) this.f578y.subtract(v.getY());
        T dz = (RealFieldElement) this.f579z.subtract(v.getZ());
        return (RealFieldElement) ((RealFieldElement) ((RealFieldElement) dx.multiply(dx)).add(dy.multiply(dy))).add(dz.multiply(dz));
    }

    public static <T extends RealFieldElement<T>> T dotProduct(FieldVector3D<T> v1, FieldVector3D<T> v2) {
        return v1.dotProduct(v2);
    }

    public static <T extends RealFieldElement<T>> T dotProduct(FieldVector3D<T> v1, Vector3D v2) {
        return v1.dotProduct(v2);
    }

    public static <T extends RealFieldElement<T>> T dotProduct(Vector3D v1, FieldVector3D<T> v2) {
        return v2.dotProduct(v1);
    }

    public static <T extends RealFieldElement<T>> FieldVector3D<T> crossProduct(FieldVector3D<T> v1, FieldVector3D<T> v2) {
        return v1.crossProduct(v2);
    }

    public static <T extends RealFieldElement<T>> FieldVector3D<T> crossProduct(FieldVector3D<T> v1, Vector3D v2) {
        return v1.crossProduct(v2);
    }

    public static <T extends RealFieldElement<T>> FieldVector3D<T> crossProduct(Vector3D v1, FieldVector3D<T> v2) {
        return new FieldVector3D<>((RealFieldElement) v2.f577x.linearCombination(v1.getY(), v2.f579z, -v1.getZ(), v2.f578y), (RealFieldElement) v2.f578y.linearCombination(v1.getZ(), v2.f577x, -v1.getX(), v2.f579z), (RealFieldElement) v2.f579z.linearCombination(v1.getX(), v2.f578y, -v1.getY(), v2.f577x));
    }

    public static <T extends RealFieldElement<T>> T distance1(FieldVector3D<T> v1, FieldVector3D<T> v2) {
        return v1.distance1(v2);
    }

    public static <T extends RealFieldElement<T>> T distance1(FieldVector3D<T> v1, Vector3D v2) {
        return v1.distance1(v2);
    }

    public static <T extends RealFieldElement<T>> T distance1(Vector3D v1, FieldVector3D<T> v2) {
        return v2.distance1(v1);
    }

    public static <T extends RealFieldElement<T>> T distance(FieldVector3D<T> v1, FieldVector3D<T> v2) {
        return v1.distance(v2);
    }

    public static <T extends RealFieldElement<T>> T distance(FieldVector3D<T> v1, Vector3D v2) {
        return v1.distance(v2);
    }

    public static <T extends RealFieldElement<T>> T distance(Vector3D v1, FieldVector3D<T> v2) {
        return v2.distance(v1);
    }

    public static <T extends RealFieldElement<T>> T distanceInf(FieldVector3D<T> v1, FieldVector3D<T> v2) {
        return v1.distanceInf(v2);
    }

    public static <T extends RealFieldElement<T>> T distanceInf(FieldVector3D<T> v1, Vector3D v2) {
        return v1.distanceInf(v2);
    }

    public static <T extends RealFieldElement<T>> T distanceInf(Vector3D v1, FieldVector3D<T> v2) {
        return v2.distanceInf(v1);
    }

    public static <T extends RealFieldElement<T>> T distanceSq(FieldVector3D<T> v1, FieldVector3D<T> v2) {
        return v1.distanceSq(v2);
    }

    public static <T extends RealFieldElement<T>> T distanceSq(FieldVector3D<T> v1, Vector3D v2) {
        return v1.distanceSq(v2);
    }

    public static <T extends RealFieldElement<T>> T distanceSq(Vector3D v1, FieldVector3D<T> v2) {
        return v2.distanceSq(v1);
    }

    public String toString() {
        return Vector3DFormat.getInstance().format(toVector3D());
    }

    public String toString(NumberFormat format) {
        return new Vector3DFormat(format).format(toVector3D());
    }
}

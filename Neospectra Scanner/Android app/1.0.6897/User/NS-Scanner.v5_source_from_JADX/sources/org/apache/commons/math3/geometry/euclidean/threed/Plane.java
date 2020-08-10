package org.apache.commons.math3.geometry.euclidean.threed;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.PolygonsSet;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.Embedding;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.util.FastMath;

public class Plane implements Hyperplane<Euclidean3D>, Embedding<Euclidean3D, Euclidean2D> {
    private static final double DEFAULT_TOLERANCE = 1.0E-10d;
    private Vector3D origin;
    private double originOffset;
    private final double tolerance;

    /* renamed from: u */
    private Vector3D f583u;

    /* renamed from: v */
    private Vector3D f584v;

    /* renamed from: w */
    private Vector3D f585w;

    public Plane(Vector3D normal, double tolerance2) throws MathArithmeticException {
        setNormal(normal);
        this.tolerance = tolerance2;
        this.originOffset = 0.0d;
        setFrame();
    }

    public Plane(Vector3D p, Vector3D normal, double tolerance2) throws MathArithmeticException {
        setNormal(normal);
        this.tolerance = tolerance2;
        this.originOffset = -p.dotProduct(this.f585w);
        setFrame();
    }

    public Plane(Vector3D p1, Vector3D p2, Vector3D p3, double tolerance2) throws MathArithmeticException {
        this(p1, p2.subtract((Vector) p1).crossProduct(p3.subtract((Vector) p1)), tolerance2);
    }

    @Deprecated
    public Plane(Vector3D normal) throws MathArithmeticException {
        this(normal, 1.0E-10d);
    }

    @Deprecated
    public Plane(Vector3D p, Vector3D normal) throws MathArithmeticException {
        this(p, normal, 1.0E-10d);
    }

    @Deprecated
    public Plane(Vector3D p1, Vector3D p2, Vector3D p3) throws MathArithmeticException {
        this(p1, p2, p3, 1.0E-10d);
    }

    public Plane(Plane plane) {
        this.originOffset = plane.originOffset;
        this.origin = plane.origin;
        this.f583u = plane.f583u;
        this.f584v = plane.f584v;
        this.f585w = plane.f585w;
        this.tolerance = plane.tolerance;
    }

    public Plane copySelf() {
        return new Plane(this);
    }

    public void reset(Vector3D p, Vector3D normal) throws MathArithmeticException {
        setNormal(normal);
        this.originOffset = -p.dotProduct(this.f585w);
        setFrame();
    }

    public void reset(Plane original) {
        this.originOffset = original.originOffset;
        this.origin = original.origin;
        this.f583u = original.f583u;
        this.f584v = original.f584v;
        this.f585w = original.f585w;
    }

    private void setNormal(Vector3D normal) throws MathArithmeticException {
        double norm = normal.getNorm();
        if (norm < 1.0E-10d) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NORM, new Object[0]);
        }
        this.f585w = new Vector3D(1.0d / norm, normal);
    }

    private void setFrame() {
        this.origin = new Vector3D(-this.originOffset, this.f585w);
        this.f583u = this.f585w.orthogonal();
        this.f584v = Vector3D.crossProduct(this.f585w, this.f583u);
    }

    public Vector3D getOrigin() {
        return this.origin;
    }

    public Vector3D getNormal() {
        return this.f585w;
    }

    public Vector3D getU() {
        return this.f583u;
    }

    public Vector3D getV() {
        return this.f584v;
    }

    public Point<Euclidean3D> project(Point<Euclidean3D> point) {
        return toSpace((Vector<Euclidean2D>) toSubSpace((Point) point));
    }

    public double getTolerance() {
        return this.tolerance;
    }

    public void revertSelf() {
        Vector3D tmp = this.f583u;
        this.f583u = this.f584v;
        this.f584v = tmp;
        this.f585w = this.f585w.negate();
        this.originOffset = -this.originOffset;
    }

    public Vector2D toSubSpace(Vector<Euclidean3D> vector) {
        return toSubSpace((Point) vector);
    }

    public Vector3D toSpace(Vector<Euclidean2D> vector) {
        return toSpace((Point) vector);
    }

    public Vector2D toSubSpace(Point<Euclidean3D> point) {
        Vector3D p3D = (Vector3D) point;
        return new Vector2D(p3D.dotProduct(this.f583u), p3D.dotProduct(this.f584v));
    }

    public Vector3D toSpace(Point<Euclidean2D> point) {
        Vector2D p2D = (Vector2D) point;
        Vector3D vector3D = new Vector3D(p2D.getX(), this.f583u, p2D.getY(), this.f584v, -this.originOffset, this.f585w);
        return vector3D;
    }

    public Vector3D getPointAt(Vector2D inPlane, double offset) {
        Vector3D vector3D = new Vector3D(inPlane.getX(), this.f583u, inPlane.getY(), this.f584v, offset - this.originOffset, this.f585w);
        return vector3D;
    }

    public boolean isSimilarTo(Plane plane) {
        double angle = Vector3D.angle(this.f585w, plane.f585w);
        return (angle < 1.0E-10d && FastMath.abs(this.originOffset - plane.originOffset) < this.tolerance) || (angle > 3.141592653489793d && FastMath.abs(this.originOffset + plane.originOffset) < this.tolerance);
    }

    public Plane rotate(Vector3D center, Rotation rotation) {
        Plane plane = new Plane(center.add((Vector) rotation.applyTo(this.origin.subtract((Vector) center))), rotation.applyTo(this.f585w), this.tolerance);
        plane.f583u = rotation.applyTo(this.f583u);
        plane.f584v = rotation.applyTo(this.f584v);
        return plane;
    }

    public Plane translate(Vector3D translation) {
        Plane plane = new Plane(this.origin.add((Vector) translation), this.f585w, this.tolerance);
        plane.f583u = this.f583u;
        plane.f584v = this.f584v;
        return plane;
    }

    public Vector3D intersection(Line line) {
        Vector3D direction = line.getDirection();
        double dot = this.f585w.dotProduct(direction);
        if (FastMath.abs(dot) < 1.0E-10d) {
            return null;
        }
        Vector3D point = line.toSpace((Point) Vector1D.ZERO);
        Vector3D vector3D = new Vector3D(1.0d, point, (-(this.originOffset + this.f585w.dotProduct(point))) / dot, direction);
        return vector3D;
    }

    public Line intersection(Plane other) {
        Vector3D direction = Vector3D.crossProduct(this.f585w, other.f585w);
        if (direction.getNorm() < this.tolerance) {
            return null;
        }
        Vector3D point = intersection(this, other, new Plane(direction, this.tolerance));
        return new Line(point, point.add((Vector) direction), this.tolerance);
    }

    public static Vector3D intersection(Plane plane1, Plane plane2, Plane plane3) {
        Plane plane = plane1;
        Plane plane4 = plane2;
        Plane plane5 = plane3;
        double a1 = plane.f585w.getX();
        double b1 = plane.f585w.getY();
        double c1 = plane.f585w.getZ();
        double d1 = plane.originOffset;
        double a2 = plane4.f585w.getX();
        double b2 = plane4.f585w.getY();
        double c2 = plane4.f585w.getZ();
        double d12 = d1;
        double d2 = plane4.originOffset;
        double a3 = plane5.f585w.getX();
        double b3 = plane5.f585w.getY();
        double c3 = plane5.f585w.getZ();
        double d3 = plane5.originOffset;
        double d32 = (b2 * c3) - (b3 * c2);
        double d22 = d2;
        double d23 = (c2 * a3) - (c3 * a2);
        double a22 = a2;
        double c23 = (a2 * b3) - (a3 * b2);
        double c232 = c23;
        double determinant = (a1 * d32) + (b1 * d23) + (c1 * c23);
        if (FastMath.abs(determinant) < 1.0E-10d) {
            return null;
        }
        double r = 1.0d / determinant;
        double d = determinant;
        double d4 = d32;
        Vector3D vector3D = new Vector3D(((((-d32) * d12) - (((c1 * b3) - (c3 * b1)) * d22)) - (((c2 * b1) - (c1 * b2)) * d3)) * r, ((((-d23) * d12) - (((c3 * a1) - (c1 * a3)) * d22)) - (((c1 * a22) - (c2 * a1)) * d3)) * r, ((((-c232) * d12) - (((b1 * a3) - (b3 * a1)) * d22)) - (((b2 * a1) - (b1 * a22)) * d3)) * r);
        return vector3D;
    }

    public SubPlane wholeHyperplane() {
        return new SubPlane(this, new PolygonsSet(this.tolerance));
    }

    public PolyhedronsSet wholeSpace() {
        return new PolyhedronsSet(this.tolerance);
    }

    public boolean contains(Vector3D p) {
        return FastMath.abs(getOffset((Vector<Euclidean3D>) p)) < this.tolerance;
    }

    public double getOffset(Plane plane) {
        return this.originOffset + (sameOrientationAs(plane) ? -plane.originOffset : plane.originOffset);
    }

    public double getOffset(Vector<Euclidean3D> vector) {
        return getOffset((Point<Euclidean3D>) vector);
    }

    public double getOffset(Point<Euclidean3D> point) {
        return ((Vector3D) point).dotProduct(this.f585w) + this.originOffset;
    }

    public boolean sameOrientationAs(Hyperplane<Euclidean3D> other) {
        return ((Plane) other).f585w.dotProduct(this.f585w) > 0.0d;
    }
}

package org.apache.commons.math3.geometry.spherical.twod;

import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.partitioning.Embedding;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.Transform;
import org.apache.commons.math3.geometry.spherical.oned.Arc;
import org.apache.commons.math3.geometry.spherical.oned.ArcsSet;
import org.apache.commons.math3.geometry.spherical.oned.S1Point;
import org.apache.commons.math3.geometry.spherical.oned.Sphere1D;
import org.apache.commons.math3.util.FastMath;

public class Circle implements Hyperplane<Sphere2D>, Embedding<Sphere2D, Sphere1D> {
    /* access modifiers changed from: private */
    public Vector3D pole;
    /* access modifiers changed from: private */
    public final double tolerance;
    /* access modifiers changed from: private */

    /* renamed from: x */
    public Vector3D f605x;
    /* access modifiers changed from: private */

    /* renamed from: y */
    public Vector3D f606y;

    private static class CircleTransform implements Transform<Sphere2D, Sphere1D> {
        private final Rotation rotation;

        CircleTransform(Rotation rotation2) {
            this.rotation = rotation2;
        }

        public S2Point apply(Point<Sphere2D> point) {
            return new S2Point(this.rotation.applyTo(((S2Point) point).getVector()));
        }

        public Circle apply(Hyperplane<Sphere2D> hyperplane) {
            Circle circle = (Circle) hyperplane;
            Circle circle2 = new Circle(this.rotation.applyTo(circle.pole), this.rotation.applyTo(circle.f605x), this.rotation.applyTo(circle.f606y), circle.tolerance);
            return circle2;
        }

        public SubHyperplane<Sphere1D> apply(SubHyperplane<Sphere1D> sub, Hyperplane<Sphere2D> hyperplane, Hyperplane<Sphere2D> hyperplane2) {
            return sub;
        }
    }

    public Circle(Vector3D pole2, double tolerance2) {
        reset(pole2);
        this.tolerance = tolerance2;
    }

    public Circle(S2Point first, S2Point second, double tolerance2) {
        reset(first.getVector().crossProduct(second.getVector()));
        this.tolerance = tolerance2;
    }

    private Circle(Vector3D pole2, Vector3D x, Vector3D y, double tolerance2) {
        this.pole = pole2;
        this.f605x = x;
        this.f606y = y;
        this.tolerance = tolerance2;
    }

    public Circle(Circle circle) {
        this(circle.pole, circle.f605x, circle.f606y, circle.tolerance);
    }

    public Circle copySelf() {
        return new Circle(this);
    }

    public void reset(Vector3D newPole) {
        this.pole = newPole.normalize();
        this.f605x = newPole.orthogonal();
        this.f606y = Vector3D.crossProduct(newPole, this.f605x).normalize();
    }

    public void revertSelf() {
        this.f606y = this.f606y.negate();
        this.pole = this.pole.negate();
    }

    public Circle getReverse() {
        Circle circle = new Circle(this.pole.negate(), this.f605x, this.f606y.negate(), this.tolerance);
        return circle;
    }

    public Point<Sphere2D> project(Point<Sphere2D> point) {
        return toSpace((Point) toSubSpace((Point) point));
    }

    public double getTolerance() {
        return this.tolerance;
    }

    public S1Point toSubSpace(Point<Sphere2D> point) {
        return new S1Point(getPhase(((S2Point) point).getVector()));
    }

    public double getPhase(Vector3D direction) {
        return FastMath.atan2(-direction.dotProduct(this.f606y), -direction.dotProduct(this.f605x)) + 3.141592653589793d;
    }

    public S2Point toSpace(Point<Sphere1D> point) {
        return new S2Point(getPointAt(((S1Point) point).getAlpha()));
    }

    public Vector3D getPointAt(double alpha) {
        Vector3D vector3D = new Vector3D(FastMath.cos(alpha), this.f605x, FastMath.sin(alpha), this.f606y);
        return vector3D;
    }

    public Vector3D getXAxis() {
        return this.f605x;
    }

    public Vector3D getYAxis() {
        return this.f606y;
    }

    public Vector3D getPole() {
        return this.pole;
    }

    public Arc getInsideArc(Circle other) {
        double alpha = getPhase(other.pole);
        Arc arc = new Arc(alpha - 1.5707963267948966d, alpha + 1.5707963267948966d, this.tolerance);
        return arc;
    }

    public SubCircle wholeHyperplane() {
        return new SubCircle(this, new ArcsSet(this.tolerance));
    }

    public SphericalPolygonsSet wholeSpace() {
        return new SphericalPolygonsSet(this.tolerance);
    }

    public double getOffset(Point<Sphere2D> point) {
        return getOffset(((S2Point) point).getVector());
    }

    public double getOffset(Vector3D direction) {
        return Vector3D.angle(this.pole, direction) - 1.5707963267948966d;
    }

    public boolean sameOrientationAs(Hyperplane<Sphere2D> other) {
        return Vector3D.dotProduct(this.pole, ((Circle) other).pole) >= 0.0d;
    }

    public static Transform<Sphere2D, Sphere1D> getTransform(Rotation rotation) {
        return new CircleTransform(rotation);
    }
}

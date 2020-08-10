package org.apache.commons.math3.geometry.euclidean.threed;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.partitioning.Embedding;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

public class Line implements Embedding<Euclidean3D, Euclidean1D> {
    private static final double DEFAULT_TOLERANCE = 1.0E-10d;
    private Vector3D direction;
    private final double tolerance;
    private Vector3D zero;

    public Line(Vector3D p1, Vector3D p2, double tolerance2) throws MathIllegalArgumentException {
        reset(p1, p2);
        this.tolerance = tolerance2;
    }

    public Line(Line line) {
        this.direction = line.direction;
        this.zero = line.zero;
        this.tolerance = line.tolerance;
    }

    @Deprecated
    public Line(Vector3D p1, Vector3D p2) throws MathIllegalArgumentException {
        this(p1, p2, 1.0E-10d);
    }

    public void reset(Vector3D p1, Vector3D p2) throws MathIllegalArgumentException {
        Vector3D delta = p2.subtract((Vector) p1);
        double norm2 = delta.getNormSq();
        if (norm2 == 0.0d) {
            throw new MathIllegalArgumentException(LocalizedFormats.ZERO_NORM, new Object[0]);
        }
        this.direction = new Vector3D(1.0d / FastMath.sqrt(norm2), delta);
        Vector3D vector3D = new Vector3D(1.0d, p1, (-p1.dotProduct(delta)) / norm2, delta);
        this.zero = vector3D;
    }

    public double getTolerance() {
        return this.tolerance;
    }

    public Line revert() {
        Line reverted = new Line(this);
        reverted.direction = reverted.direction.negate();
        return reverted;
    }

    public Vector3D getDirection() {
        return this.direction;
    }

    public Vector3D getOrigin() {
        return this.zero;
    }

    public double getAbscissa(Vector3D point) {
        return point.subtract((Vector) this.zero).dotProduct(this.direction);
    }

    public Vector3D pointAt(double abscissa) {
        Vector3D vector3D = new Vector3D(1.0d, this.zero, abscissa, this.direction);
        return vector3D;
    }

    public Vector1D toSubSpace(Vector<Euclidean3D> vector) {
        return toSubSpace((Point) vector);
    }

    public Vector3D toSpace(Vector<Euclidean1D> vector) {
        return toSpace((Point) vector);
    }

    public Vector1D toSubSpace(Point<Euclidean3D> point) {
        return new Vector1D(getAbscissa((Vector3D) point));
    }

    public Vector3D toSpace(Point<Euclidean1D> point) {
        return pointAt(((Vector1D) point).getX());
    }

    public boolean isSimilarTo(Line line) {
        double angle = Vector3D.angle(this.direction, line.direction);
        return (angle < this.tolerance || angle > 3.141592653589793d - this.tolerance) && contains(line.zero);
    }

    public boolean contains(Vector3D p) {
        return distance(p) < this.tolerance;
    }

    public double distance(Vector3D p) {
        Vector3D d = p.subtract((Vector) this.zero);
        Vector3D n = new Vector3D(1.0d, d, -d.dotProduct(this.direction), this.direction);
        return n.getNorm();
    }

    public double distance(Line line) {
        Vector3D normal = Vector3D.crossProduct(this.direction, line.direction);
        double n = normal.getNorm();
        if (n < Precision.SAFE_MIN) {
            return distance(line.zero);
        }
        return FastMath.abs(line.zero.subtract((Vector) this.zero).dotProduct(normal) / n);
    }

    public Vector3D closestPoint(Line line) {
        Line line2 = line;
        double cos = this.direction.dotProduct(line2.direction);
        double n = 1.0d - (cos * cos);
        if (n < Precision.EPSILON) {
            return this.zero;
        }
        Vector3D delta0 = line2.zero.subtract((Vector) this.zero);
        Vector3D vector3D = new Vector3D(1.0d, this.zero, (delta0.dotProduct(this.direction) - (delta0.dotProduct(line2.direction) * cos)) / n, this.direction);
        return vector3D;
    }

    public Vector3D intersection(Line line) {
        Vector3D closest = closestPoint(line);
        if (line.contains(closest)) {
            return closest;
        }
        return null;
    }

    public SubLine wholeLine() {
        return new SubLine(this, new IntervalsSet(this.tolerance));
    }
}

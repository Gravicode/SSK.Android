package org.apache.commons.math3.geometry.euclidean.twod;

import java.awt.geom.AffineTransform;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.euclidean.oned.OrientedPoint;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.partitioning.Embedding;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.Transform;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;

public class Line implements Hyperplane<Euclidean2D>, Embedding<Euclidean2D, Euclidean1D> {
    private static final double DEFAULT_TOLERANCE = 1.0E-10d;
    private double angle;
    /* access modifiers changed from: private */
    public double cos;
    /* access modifiers changed from: private */
    public double originOffset;
    private Line reverse;
    /* access modifiers changed from: private */
    public double sin;
    /* access modifiers changed from: private */
    public final double tolerance;

    private static class LineTransform implements Transform<Euclidean2D, Euclidean1D> {
        private double c11;
        private double c1X;
        private double c1Y;
        private double cX1;
        private double cXX;
        private double cXY;
        private double cY1;
        private double cYX;
        private double cYY;

        LineTransform(double cXX2, double cYX2, double cXY2, double cYY2, double cX12, double cY12) throws MathIllegalArgumentException {
            double d = cYX2;
            double d2 = cYY2;
            double d3 = cXX2;
            this.cXX = d3;
            this.cYX = d;
            double d4 = cXY2;
            this.cXY = d4;
            this.cYY = d2;
            this.cX1 = cX12;
            double d5 = cY12;
            this.cY1 = d5;
            double d6 = cX12;
            this.c1Y = MathArrays.linearCombination(d4, d5, -d2, d6);
            this.c1X = MathArrays.linearCombination(d3, d5, -d, d6);
            this.c11 = MathArrays.linearCombination(d3, d2, -d, cXY2);
            if (FastMath.abs(this.c11) < 1.0E-20d) {
                throw new MathIllegalArgumentException(LocalizedFormats.NON_INVERTIBLE_TRANSFORM, new Object[0]);
            }
        }

        public Vector2D apply(Point<Euclidean2D> point) {
            Vector2D p2D = (Vector2D) point;
            double x = p2D.getX();
            double d = x;
            double y = p2D.getY();
            Vector2D vector2D = new Vector2D(MathArrays.linearCombination(this.cXX, d, this.cXY, y, this.cX1, 1.0d), MathArrays.linearCombination(this.cYX, d, this.cYY, y, this.cY1, 1.0d));
            return vector2D;
        }

        public Line apply(Hyperplane<Euclidean2D> hyperplane) {
            Line line = (Line) hyperplane;
            double rOffset = MathArrays.linearCombination(this.c1X, line.cos, this.c1Y, line.sin, this.c11, line.originOffset);
            double rCos = MathArrays.linearCombination(this.cXX, line.cos, this.cXY, line.sin);
            double rSin = MathArrays.linearCombination(this.cYX, line.cos, this.cYY, line.sin);
            double inv = 1.0d / FastMath.sqrt((rSin * rSin) + (rCos * rCos));
            Line line2 = new Line(FastMath.atan2(-rSin, -rCos) + 3.141592653589793d, inv * rCos, inv * rSin, inv * rOffset, line.tolerance);
            return line2;
        }

        public SubHyperplane<Euclidean1D> apply(SubHyperplane<Euclidean1D> sub, Hyperplane<Euclidean2D> original, Hyperplane<Euclidean2D> transformed) {
            OrientedPoint op = (OrientedPoint) sub.getHyperplane();
            Line originalLine = (Line) original;
            return new OrientedPoint(((Line) transformed).toSubSpace((Vector<Euclidean2D>) apply((Point) originalLine.toSpace((Vector<Euclidean1D>) op.getLocation()))), op.isDirect(), originalLine.tolerance).wholeHyperplane();
        }
    }

    public Line(Vector2D p1, Vector2D p2, double tolerance2) {
        reset(p1, p2);
        this.tolerance = tolerance2;
    }

    public Line(Vector2D p, double angle2, double tolerance2) {
        reset(p, angle2);
        this.tolerance = tolerance2;
    }

    private Line(double angle2, double cos2, double sin2, double originOffset2, double tolerance2) {
        this.angle = angle2;
        this.cos = cos2;
        this.sin = sin2;
        this.originOffset = originOffset2;
        this.tolerance = tolerance2;
        this.reverse = null;
    }

    @Deprecated
    public Line(Vector2D p1, Vector2D p2) {
        this(p1, p2, 1.0E-10d);
    }

    @Deprecated
    public Line(Vector2D p, double angle2) {
        this(p, angle2, 1.0E-10d);
    }

    public Line(Line line) {
        this.angle = MathUtils.normalizeAngle(line.angle, 3.141592653589793d);
        this.cos = line.cos;
        this.sin = line.sin;
        this.originOffset = line.originOffset;
        this.tolerance = line.tolerance;
        this.reverse = null;
    }

    public Line copySelf() {
        return new Line(this);
    }

    public void reset(Vector2D p1, Vector2D p2) {
        unlinkReverse();
        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();
        double d = FastMath.hypot(dx, dy);
        if (d == 0.0d) {
            this.angle = 0.0d;
            this.cos = 1.0d;
            this.sin = 0.0d;
            this.originOffset = p1.getY();
            return;
        }
        this.angle = FastMath.atan2(-dy, -dx) + 3.141592653589793d;
        this.cos = dx / d;
        this.sin = dy / d;
        this.originOffset = MathArrays.linearCombination(p2.getX(), p1.getY(), -p1.getX(), p2.getY()) / d;
    }

    public void reset(Vector2D p, double alpha) {
        unlinkReverse();
        this.angle = MathUtils.normalizeAngle(alpha, 3.141592653589793d);
        this.cos = FastMath.cos(this.angle);
        this.sin = FastMath.sin(this.angle);
        this.originOffset = MathArrays.linearCombination(this.cos, p.getY(), -this.sin, p.getX());
    }

    public void revertSelf() {
        unlinkReverse();
        if (this.angle < 3.141592653589793d) {
            this.angle += 3.141592653589793d;
        } else {
            this.angle -= 3.141592653589793d;
        }
        this.cos = -this.cos;
        this.sin = -this.sin;
        this.originOffset = -this.originOffset;
    }

    private void unlinkReverse() {
        if (this.reverse != null) {
            this.reverse.reverse = null;
        }
        this.reverse = null;
    }

    public Line getReverse() {
        if (this.reverse == null) {
            Line line = new Line(this.angle < 3.141592653589793d ? this.angle + 3.141592653589793d : this.angle - 3.141592653589793d, -this.cos, -this.sin, -this.originOffset, this.tolerance);
            this.reverse = line;
            this.reverse.reverse = this;
        }
        return this.reverse;
    }

    public Vector1D toSubSpace(Vector<Euclidean2D> vector) {
        return toSubSpace((Point) vector);
    }

    public Vector2D toSpace(Vector<Euclidean1D> vector) {
        return toSpace((Point) vector);
    }

    public Vector1D toSubSpace(Point<Euclidean2D> point) {
        Vector2D p2 = (Vector2D) point;
        return new Vector1D(MathArrays.linearCombination(this.cos, p2.getX(), this.sin, p2.getY()));
    }

    public Vector2D toSpace(Point<Euclidean1D> point) {
        double x = ((Vector1D) point).getX();
        return new Vector2D(MathArrays.linearCombination(x, this.cos, -this.originOffset, this.sin), MathArrays.linearCombination(x, this.sin, this.originOffset, this.cos));
    }

    public Vector2D intersection(Line other) {
        double d = MathArrays.linearCombination(this.sin, other.cos, -other.sin, this.cos);
        if (FastMath.abs(d) < this.tolerance) {
            return null;
        }
        return new Vector2D(MathArrays.linearCombination(this.cos, other.originOffset, -other.cos, this.originOffset) / d, MathArrays.linearCombination(this.sin, other.originOffset, -other.sin, this.originOffset) / d);
    }

    public Point<Euclidean2D> project(Point<Euclidean2D> point) {
        return toSpace((Vector<Euclidean1D>) toSubSpace((Point) point));
    }

    public double getTolerance() {
        return this.tolerance;
    }

    public SubLine wholeHyperplane() {
        return new SubLine((Hyperplane<Euclidean2D>) this, (Region<Euclidean1D>) new IntervalsSet<Euclidean1D>(this.tolerance));
    }

    public PolygonsSet wholeSpace() {
        return new PolygonsSet(this.tolerance);
    }

    public double getOffset(Line line) {
        return this.originOffset + (MathArrays.linearCombination(this.cos, line.cos, this.sin, line.sin) > 0.0d ? -line.originOffset : line.originOffset);
    }

    public double getOffset(Vector<Euclidean2D> vector) {
        return getOffset((Point<Euclidean2D>) vector);
    }

    public double getOffset(Point<Euclidean2D> point) {
        Vector2D p2 = (Vector2D) point;
        return MathArrays.linearCombination(this.sin, p2.getX(), -this.cos, p2.getY(), 1.0d, this.originOffset);
    }

    public boolean sameOrientationAs(Hyperplane<Euclidean2D> other) {
        Line otherL = (Line) other;
        return MathArrays.linearCombination(this.sin, otherL.sin, this.cos, otherL.cos) >= 0.0d;
    }

    public Vector2D getPointAt(Vector1D abscissa, double offset) {
        double x = abscissa.getX();
        double dOffset = offset - this.originOffset;
        double d = x;
        return new Vector2D(MathArrays.linearCombination(x, this.cos, dOffset, this.sin), MathArrays.linearCombination(x, this.sin, -dOffset, this.cos));
    }

    public boolean contains(Vector2D p) {
        return FastMath.abs(getOffset((Vector<Euclidean2D>) p)) < this.tolerance;
    }

    public double distance(Vector2D p) {
        return FastMath.abs(getOffset((Vector<Euclidean2D>) p));
    }

    public boolean isParallelTo(Line line) {
        return FastMath.abs(MathArrays.linearCombination(this.sin, line.cos, -this.cos, line.sin)) < this.tolerance;
    }

    public void translateToPoint(Vector2D p) {
        this.originOffset = MathArrays.linearCombination(this.cos, p.getY(), -this.sin, p.getX());
    }

    public double getAngle() {
        return MathUtils.normalizeAngle(this.angle, 3.141592653589793d);
    }

    public void setAngle(double angle2) {
        unlinkReverse();
        this.angle = MathUtils.normalizeAngle(angle2, 3.141592653589793d);
        this.cos = FastMath.cos(this.angle);
        this.sin = FastMath.sin(this.angle);
    }

    public double getOriginOffset() {
        return this.originOffset;
    }

    public void setOriginOffset(double offset) {
        unlinkReverse();
        this.originOffset = offset;
    }

    @Deprecated
    public static Transform<Euclidean2D, Euclidean1D> getTransform(AffineTransform transform) throws MathIllegalArgumentException {
        double[] m = new double[6];
        transform.getMatrix(m);
        LineTransform lineTransform = new LineTransform(m[0], m[1], m[2], m[3], m[4], m[5]);
        return lineTransform;
    }

    public static Transform<Euclidean2D, Euclidean1D> getTransform(double cXX, double cYX, double cXY, double cYY, double cX1, double cY1) throws MathIllegalArgumentException {
        LineTransform lineTransform = new LineTransform(cXX, cYX, cXY, cYY, cX1, cY1);
        return lineTransform;
    }
}

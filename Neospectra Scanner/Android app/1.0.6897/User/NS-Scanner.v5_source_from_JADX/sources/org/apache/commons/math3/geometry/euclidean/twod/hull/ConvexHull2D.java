package org.apache.commons.math3.geometry.euclidean.twod.hull;

import java.io.Serializable;
import org.apache.commons.math3.exception.InsufficientDataException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.hull.ConvexHull;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.RegionFactory;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;

public class ConvexHull2D implements ConvexHull<Euclidean2D, Vector2D>, Serializable {
    private static final long serialVersionUID = 20140129;
    private transient Segment[] lineSegments;
    private final double tolerance;
    private final Vector2D[] vertices;

    public ConvexHull2D(Vector2D[] vertices2, double tolerance2) throws MathIllegalArgumentException {
        this.tolerance = tolerance2;
        if (!isConvex(vertices2)) {
            throw new MathIllegalArgumentException(LocalizedFormats.NOT_CONVEX, new Object[0]);
        }
        this.vertices = (Vector2D[]) vertices2.clone();
    }

    private boolean isConvex(Vector2D[] hullVertices) {
        Vector2D[] vector2DArr = hullVertices;
        if (vector2DArr.length < 3) {
            return true;
        }
        int sign = 0;
        int i = 0;
        while (i < vector2DArr.length) {
            Vector2D p1 = vector2DArr[i == 0 ? vector2DArr.length - 1 : i - 1];
            Vector2D p2 = vector2DArr[i];
            Vector2D p3 = vector2DArr[i == vector2DArr.length - 1 ? 0 : i + 1];
            Vector2D d1 = p2.subtract((Vector) p1);
            Vector2D d2 = p3.subtract((Vector) p2);
            int cmp = Precision.compareTo(MathArrays.linearCombination(d1.getX(), d2.getY(), -d1.getY(), d2.getX()), 0.0d, this.tolerance);
            if (((double) cmp) != 0.0d) {
                if (((double) sign) != 0.0d && cmp != sign) {
                    return false;
                }
                sign = cmp;
            }
            i++;
        }
        return true;
    }

    public Vector2D[] getVertices() {
        return (Vector2D[]) this.vertices.clone();
    }

    public Segment[] getLineSegments() {
        return (Segment[]) retrieveLineSegments().clone();
    }

    private Segment[] retrieveLineSegments() {
        Vector2D[] arr$;
        if (this.lineSegments == null) {
            int size = this.vertices.length;
            if (size <= 1) {
                this.lineSegments = new Segment[0];
            } else if (size == 2) {
                this.lineSegments = new Segment[1];
                Vector2D p1 = this.vertices[0];
                Vector2D p2 = this.vertices[1];
                this.lineSegments[0] = new Segment(p1, p2, new Line(p1, p2, this.tolerance));
            } else {
                this.lineSegments = new Segment[size];
                Vector2D firstPoint = null;
                Vector2D lastPoint = null;
                int index = 0;
                for (Vector2D point : this.vertices) {
                    if (lastPoint == null) {
                        firstPoint = point;
                        lastPoint = point;
                    } else {
                        int index2 = index + 1;
                        this.lineSegments[index] = new Segment(lastPoint, point, new Line(lastPoint, point, this.tolerance));
                        lastPoint = point;
                        index = index2;
                    }
                }
                this.lineSegments[index] = new Segment(lastPoint, firstPoint, new Line(lastPoint, firstPoint, this.tolerance));
            }
        }
        return this.lineSegments;
    }

    public Region<Euclidean2D> createRegion() throws InsufficientDataException {
        if (this.vertices.length < 3) {
            throw new InsufficientDataException();
        }
        RegionFactory<Euclidean2D> factory = new RegionFactory<>();
        Segment[] segments = retrieveLineSegments();
        Line[] lineArray = new Line[segments.length];
        for (int i = 0; i < segments.length; i++) {
            lineArray[i] = segments[i].getLine();
        }
        return factory.buildConvex(lineArray);
    }
}

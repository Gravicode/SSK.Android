package org.apache.commons.math3.geometry.euclidean.threed;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.PolygonsSet;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor;
import org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor.Order;
import org.apache.commons.math3.geometry.partitioning.BoundaryAttribute;
import org.apache.commons.math3.geometry.partitioning.RegionFactory;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.util.FastMath;

public class OutlineExtractor {
    /* access modifiers changed from: private */

    /* renamed from: u */
    public Vector3D f580u;
    /* access modifiers changed from: private */

    /* renamed from: v */
    public Vector3D f581v;
    /* access modifiers changed from: private */

    /* renamed from: w */
    public Vector3D f582w;

    private class BoundaryProjector implements BSPTreeVisitor<Euclidean3D> {
        private PolygonsSet projected;
        private final double tolerance;

        BoundaryProjector(double tolerance2) {
            this.projected = new PolygonsSet(new BSPTree<>(Boolean.FALSE), tolerance2);
            this.tolerance = tolerance2;
        }

        public Order visitOrder(BSPTree<Euclidean3D> bSPTree) {
            return Order.MINUS_SUB_PLUS;
        }

        public void visitInternalNode(BSPTree<Euclidean3D> node) {
            BoundaryAttribute<Euclidean3D> attribute = (BoundaryAttribute) node.getAttribute();
            if (attribute.getPlusOutside() != null) {
                addContribution(attribute.getPlusOutside(), false);
            }
            if (attribute.getPlusInside() != null) {
                addContribution(attribute.getPlusInside(), true);
            }
        }

        public void visitLeafNode(BSPTree<Euclidean3D> bSPTree) {
        }

        private void addContribution(SubHyperplane<Euclidean3D> facet, boolean reversed) {
            AbstractSubHyperplane abstractSubHyperplane = (AbstractSubHyperplane) facet;
            Plane plane = (Plane) facet.getHyperplane();
            double scal = plane.getNormal().dotProduct(OutlineExtractor.this.f582w);
            if (FastMath.abs(scal) > 0.001d) {
                Vector2D[][] vertices = ((PolygonsSet) abstractSubHyperplane.getRemainingRegion()).getVertices();
                char c = 0;
                int i = 1;
                if ((scal < 0.0d) ^ reversed) {
                    Vector2D[][] newVertices = new Vector2D[vertices.length][];
                    for (int i2 = 0; i2 < vertices.length; i2++) {
                        Vector2D[] loop = vertices[i2];
                        Vector2D[] newLoop = new Vector2D[loop.length];
                        if (loop[0] == null) {
                            newLoop[0] = null;
                            for (int j = 1; j < loop.length; j++) {
                                newLoop[j] = loop[loop.length - j];
                            }
                        } else {
                            for (int j2 = 0; j2 < loop.length; j2++) {
                                newLoop[j2] = loop[loop.length - (j2 + 1)];
                            }
                        }
                        newVertices[i2] = newLoop;
                    }
                    vertices = newVertices;
                }
                ArrayList<SubHyperplane<Euclidean2D>> edges = new ArrayList<>();
                Vector2D[][] arr$ = vertices;
                int len$ = arr$.length;
                int i$ = 0;
                while (i$ < len$) {
                    Vector2D[] loop2 = arr$[i$];
                    boolean closed = loop2[c] != null;
                    int previous = closed ? loop2.length - i : 1;
                    Vector3D previous3D = plane.toSpace((Point) loop2[previous]);
                    int current = (previous + 1) % loop2.length;
                    AbstractSubHyperplane abstractSubHyperplane2 = abstractSubHyperplane;
                    double scal2 = scal;
                    Vector2D[][] arr$2 = arr$;
                    Vector2D pPoint = new Vector2D(previous3D.dotProduct(OutlineExtractor.this.f580u), previous3D.dotProduct(OutlineExtractor.this.f581v));
                    int current2 = current;
                    while (current2 < loop2.length) {
                        Vector3D current3D = plane.toSpace((Point) loop2[current2]);
                        Plane plane2 = plane;
                        int len$2 = len$;
                        int i$2 = i$;
                        Vector2D cPoint = new Vector2D(current3D.dotProduct(OutlineExtractor.this.f580u), current3D.dotProduct(OutlineExtractor.this.f581v));
                        Line line = new Line(pPoint, cPoint, this.tolerance);
                        SubHyperplane<Euclidean2D> edge = line.wholeHyperplane();
                        if (closed || previous != 1) {
                            Line line2 = new Line(pPoint, line.getAngle() + 1.5707963267948966d, this.tolerance);
                            edge = edge.split(line2).getPlus();
                        }
                        if (!closed) {
                            if (current2 == loop2.length - 1) {
                                edges.add(edge);
                                previous = current2;
                                Vector3D previous3D2 = current3D;
                                pPoint = cPoint;
                                current2++;
                                plane = plane2;
                                len$ = len$2;
                                i$ = i$2;
                            }
                        }
                        Line line3 = new Line(cPoint, line.getAngle() + 1.5707963267948966d, this.tolerance);
                        edge = edge.split(line3).getMinus();
                        edges.add(edge);
                        previous = current2;
                        Vector3D previous3D22 = current3D;
                        pPoint = cPoint;
                        current2++;
                        plane = plane2;
                        len$ = len$2;
                        i$ = i$2;
                    }
                    int i3 = len$;
                    i$++;
                    abstractSubHyperplane = abstractSubHyperplane2;
                    scal = scal2;
                    arr$ = arr$2;
                    c = 0;
                    i = 1;
                }
                AbstractSubHyperplane abstractSubHyperplane3 = abstractSubHyperplane;
                Plane plane3 = plane;
                double d = scal;
                this.projected = (PolygonsSet) new RegionFactory().union(this.projected, new PolygonsSet((Collection<SubHyperplane<Euclidean2D>>) edges, this.tolerance));
                return;
            }
            AbstractSubHyperplane abstractSubHyperplane4 = abstractSubHyperplane;
            Plane plane4 = plane;
            double d2 = scal;
        }

        public PolygonsSet getProjected() {
            return this.projected;
        }
    }

    public OutlineExtractor(Vector3D u, Vector3D v) {
        this.f580u = u;
        this.f581v = v;
        this.f582w = Vector3D.crossProduct(u, v);
    }

    public Vector2D[][] getOutline(PolyhedronsSet polyhedronsSet) {
        BoundaryProjector projector = new BoundaryProjector(polyhedronsSet.getTolerance());
        polyhedronsSet.getTree(true).visit(projector);
        Vector2D[][] outline = projector.getProjected().getVertices();
        for (int i = 0; i < outline.length; i++) {
            Vector2D[] rawLoop = outline[i];
            int end = rawLoop.length;
            int j = 0;
            while (j < end) {
                if (pointIsBetween(rawLoop, end, j)) {
                    for (int k = j; k < end - 1; k++) {
                        rawLoop[k] = rawLoop[k + 1];
                    }
                    end--;
                } else {
                    j++;
                }
            }
            if (end != rawLoop.length) {
                outline[i] = new Vector2D[end];
                System.arraycopy(rawLoop, 0, outline[i], 0, end);
            }
        }
        return outline;
    }

    private boolean pointIsBetween(Vector2D[] loop, int n, int i) {
        Vector2D previous = loop[((i + n) - 1) % n];
        Vector2D current = loop[i];
        Vector2D next = loop[(i + 1) % n];
        double dx1 = current.getX() - previous.getX();
        double dy1 = current.getY() - previous.getY();
        double dx2 = next.getX() - current.getX();
        double dy2 = next.getY() - current.getY();
        Vector2D vector2D = current;
        return FastMath.abs((dx1 * dy2) - (dx2 * dy1)) <= 1.0E-6d * FastMath.sqrt(((dx1 * dx1) + (dy1 * dy1)) * ((dx2 * dx2) + (dy2 * dy2))) && (dx1 * dx2) + (dy1 * dy2) >= 0.0d;
    }
}

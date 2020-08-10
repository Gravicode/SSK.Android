package org.apache.commons.math3.geometry.spherical.twod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.enclosing.EnclosingBall;
import org.apache.commons.math3.geometry.enclosing.WelzlEncloser;
import org.apache.commons.math3.geometry.euclidean.threed.Euclidean3D;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.SphereGenerator;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.partitioning.AbstractRegion;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.BoundaryProjection;
import org.apache.commons.math3.geometry.partitioning.RegionFactory;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.spherical.oned.Sphere1D;
import org.apache.commons.math3.util.FastMath;

public class SphericalPolygonsSet extends AbstractRegion<Sphere2D, Sphere1D> {
    private List<Vertex> loops;

    public SphericalPolygonsSet(double tolerance) {
        super(tolerance);
    }

    public SphericalPolygonsSet(Vector3D pole, double tolerance) {
        super(new BSPTree<>(new Circle(pole, tolerance).wholeHyperplane(), new BSPTree(Boolean.FALSE), new BSPTree(Boolean.TRUE), null), tolerance);
    }

    public SphericalPolygonsSet(Vector3D center, Vector3D meridian, double outsideRadius, int n, double tolerance) {
        this(tolerance, createRegularPolygonVertices(center, meridian, outsideRadius, n));
    }

    public SphericalPolygonsSet(BSPTree<Sphere2D> tree, double tolerance) {
        super(tree, tolerance);
    }

    public SphericalPolygonsSet(Collection<SubHyperplane<Sphere2D>> boundary, double tolerance) {
        super(boundary, tolerance);
    }

    public SphericalPolygonsSet(double hyperplaneThickness, S2Point... vertices) {
        super(verticesToTree(hyperplaneThickness, vertices), hyperplaneThickness);
    }

    private static S2Point[] createRegularPolygonVertices(Vector3D center, Vector3D meridian, double outsideRadius, int n) {
        S2Point[] array = new S2Point[n];
        array[0] = new S2Point(new Rotation(Vector3D.crossProduct(center, meridian), outsideRadius, RotationConvention.VECTOR_OPERATOR).applyTo(center));
        Rotation r = new Rotation(center, 6.283185307179586d / ((double) n), RotationConvention.VECTOR_OPERATOR);
        for (int i = 1; i < n; i++) {
            array[i] = new S2Point(r.applyTo(array[i - 1].getVector()));
        }
        return array;
    }

    private static BSPTree<Sphere2D> verticesToTree(double hyperplaneThickness, S2Point... vertices) {
        int n;
        Circle circle;
        double d = hyperplaneThickness;
        S2Point[] s2PointArr = vertices;
        int n2 = s2PointArr.length;
        if (n2 == 0) {
            return new BSPTree<>(Boolean.TRUE);
        }
        Vertex[] vArray = new Vertex[n2];
        for (int i = 0; i < n2; i++) {
            vArray[i] = new Vertex(s2PointArr[i]);
        }
        List<Edge> edges = new ArrayList<>(n2);
        Vertex end = vArray[n2 - 1];
        int i2 = 0;
        while (i2 < n2) {
            Vertex start = end;
            end = vArray[i2];
            Circle circle2 = start.sharedCircleWith(end);
            if (circle2 == null) {
                circle2 = new Circle(start.getLocation(), end.getLocation(), d);
            }
            Circle circle3 = circle2;
            Edge edge = r9;
            Circle circle4 = circle3;
            Edge edge2 = new Edge(start, end, Vector3D.angle(start.getLocation().getVector(), end.getLocation().getVector()), circle3);
            edges.add(edge);
            Vertex[] arr$ = vArray;
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                Vertex vertex = arr$[i$];
                if (vertex == start || vertex == end) {
                    n = n2;
                    circle = circle4;
                } else {
                    n = n2;
                    circle = circle4;
                    if (FastMath.abs(circle.getOffset((Point<Sphere2D>) vertex.getLocation())) <= d) {
                        vertex.bindWith(circle);
                    }
                }
                i$++;
                circle4 = circle;
                n2 = n;
                S2Point[] s2PointArr2 = vertices;
            }
            i2++;
            S2Point[] s2PointArr3 = vertices;
        }
        BSPTree<Sphere2D> tree = new BSPTree<>();
        insertEdges(d, tree, edges);
        return tree;
    }

    private static void insertEdges(double hyperplaneThickness, BSPTree<Sphere2D> node, List<Edge> edges) {
        int index = 0;
        Edge inserted = null;
        while (inserted == null && index < edges.size()) {
            int index2 = index + 1;
            inserted = (Edge) edges.get(index);
            if (!node.insertCut(inserted.getCircle())) {
                inserted = null;
            }
            index = index2;
        }
        if (inserted == null) {
            BSPTree<Sphere2D> parent = node.getParent();
            if (parent == null || node == parent.getMinus()) {
                node.setAttribute(Boolean.TRUE);
            } else {
                node.setAttribute(Boolean.FALSE);
            }
            return;
        }
        List<Edge> outsideList = new ArrayList<>();
        List<Edge> insideList = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge != inserted) {
                edge.split(inserted.getCircle(), outsideList, insideList);
            }
        }
        if (!outsideList.isEmpty()) {
            insertEdges(hyperplaneThickness, node.getPlus(), outsideList);
        } else {
            node.getPlus().setAttribute(Boolean.FALSE);
        }
        if (!insideList.isEmpty()) {
            insertEdges(hyperplaneThickness, node.getMinus(), insideList);
        } else {
            node.getMinus().setAttribute(Boolean.TRUE);
        }
    }

    public SphericalPolygonsSet buildNew(BSPTree<Sphere2D> tree) {
        return new SphericalPolygonsSet(tree, getTolerance());
    }

    /* access modifiers changed from: protected */
    public void computeGeometricalProperties() throws MathIllegalStateException {
        BSPTree<Sphere2D> tree = getTree(true);
        if (tree.getCut() != null) {
            PropertiesComputer pc = new PropertiesComputer(getTolerance());
            tree.visit(pc);
            setSize(pc.getArea());
            setBarycenter((Point<S>) pc.getBarycenter());
        } else if (tree.getCut() != null || !((Boolean) tree.getAttribute()).booleanValue()) {
            setSize(0.0d);
            setBarycenter((Point<S>) S2Point.NaN);
        } else {
            setSize(12.566370614359172d);
            setBarycenter((Point<S>) new S2Point<S>(0.0d, 0.0d));
        }
    }

    public List<Vertex> getBoundaryLoops() throws MathIllegalStateException {
        if (this.loops == null) {
            if (getTree(false).getCut() == null) {
                this.loops = Collections.emptyList();
            } else {
                BSPTree<Sphere2D> root = getTree(true);
                EdgesBuilder visitor = new EdgesBuilder(root, getTolerance());
                root.visit(visitor);
                List<Edge> edges = visitor.getEdges();
                this.loops = new ArrayList();
                while (!edges.isEmpty()) {
                    Edge edge = (Edge) edges.get(0);
                    Vertex startVertex = edge.getStart();
                    this.loops.add(startVertex);
                    do {
                        Iterator<Edge> iterator = edges.iterator();
                        while (true) {
                            if (iterator.hasNext()) {
                                if (iterator.next() == edge) {
                                    iterator.remove();
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                        edge = edge.getEnd().getOutgoing();
                    } while (edge.getStart() != startVertex);
                }
            }
        }
        return Collections.unmodifiableList(this.loops);
    }

    public EnclosingBall<Sphere2D, S2Point> getEnclosingCap() {
        Iterator i$;
        SphericalPolygonsSet sphericalPolygonsSet = this;
        if (isEmpty()) {
            return new EnclosingBall<>(S2Point.PLUS_K, Double.NEGATIVE_INFINITY, new S2Point[0]);
        }
        if (isFull()) {
            return new EnclosingBall<>(S2Point.PLUS_K, Double.POSITIVE_INFINITY, new S2Point[0]);
        }
        BSPTree<Sphere2D> root = sphericalPolygonsSet.getTree(false);
        if (sphericalPolygonsSet.isEmpty(root.getMinus()) && sphericalPolygonsSet.isFull(root.getPlus())) {
            return new EnclosingBall<>(new S2Point(((Circle) root.getCut().getHyperplane()).getPole()).negate(), 1.5707963267948966d, new S2Point[0]);
        }
        if (sphericalPolygonsSet.isFull(root.getMinus()) && sphericalPolygonsSet.isEmpty(root.getPlus())) {
            return new EnclosingBall<>(new S2Point(((Circle) root.getCut().getHyperplane()).getPole()), 1.5707963267948966d, new S2Point[0]);
        }
        List<Vector3D> points = getInsidePoints();
        for (Vertex loopStart : getBoundaryLoops()) {
            int count = 0;
            Vertex v = loopStart;
            while (true) {
                if (count == 0 || v != loopStart) {
                    count++;
                    points.add(v.getLocation().getVector());
                    v = v.getOutgoing().getEnd();
                }
            }
        }
        EnclosingBall<Euclidean3D, Vector3D> enclosing3D = new WelzlEncloser<>(getTolerance(), new SphereGenerator()).enclose(points);
        Vector3D[] support3D = (Vector3D[]) enclosing3D.getSupport();
        double r = enclosing3D.getRadius();
        double h = ((Vector3D) enclosing3D.getCenter()).getNorm();
        if (h < getTolerance()) {
            BSPTree bSPTree = root;
            List list = points;
            EnclosingBall enclosingBall = new EnclosingBall(S2Point.PLUS_K, Double.POSITIVE_INFINITY, new S2Point[0]);
            Iterator i$2 = getOutsidePoints().iterator();
            while (i$2.hasNext()) {
                Vector3D outsidePoint = (Vector3D) i$2.next();
                S2Point outsideS2 = new S2Point(outsidePoint);
                BoundaryProjection<Sphere2D> projection = sphericalPolygonsSet.projectToBoundary(outsideS2);
                if (3.141592653589793d - projection.getOffset() < enclosingBall.getRadius()) {
                    i$ = i$2;
                    Vector3D vector3D = outsidePoint;
                    S2Point s2Point = outsideS2;
                    enclosingBall = new EnclosingBall(outsideS2.negate(), 3.141592653589793d - projection.getOffset(), (S2Point) projection.getProjected());
                } else {
                    i$ = i$2;
                }
                i$2 = i$;
                sphericalPolygonsSet = this;
            }
            return enclosingBall;
        }
        List list2 = points;
        int i = 0;
        S2Point[] support = new S2Point[support3D.length];
        while (true) {
            int i2 = i;
            if (i2 >= support3D.length) {
                return new EnclosingBall<>(new S2Point((Vector3D) enclosing3D.getCenter()), FastMath.acos((((h * h) + 1.0d) - (r * r)) / (2.0d * h)), support);
            }
            support[i2] = new S2Point(support3D[i2]);
            i = i2 + 1;
        }
    }

    private List<Vector3D> getInsidePoints() {
        PropertiesComputer pc = new PropertiesComputer(getTolerance());
        getTree(true).visit(pc);
        return pc.getConvexCellsInsidePoints();
    }

    private List<Vector3D> getOutsidePoints() {
        SphericalPolygonsSet complement = (SphericalPolygonsSet) new RegionFactory().getComplement(this);
        PropertiesComputer pc = new PropertiesComputer(getTolerance());
        complement.getTree(true).visit(pc);
        return pc.getConvexCellsInsidePoints();
    }
}

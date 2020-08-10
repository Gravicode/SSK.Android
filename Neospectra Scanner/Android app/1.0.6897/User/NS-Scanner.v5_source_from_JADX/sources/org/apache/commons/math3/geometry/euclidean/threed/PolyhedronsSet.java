package org.apache.commons.math3.geometry.euclidean.threed;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.PolygonsSet;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractRegion;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor;
import org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor.Order;
import org.apache.commons.math3.geometry.partitioning.BoundaryAttribute;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Region.Location;
import org.apache.commons.math3.geometry.partitioning.RegionFactory;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.Transform;
import org.apache.commons.math3.util.FastMath;

public class PolyhedronsSet extends AbstractRegion<Euclidean3D, Euclidean2D> {
    private static final double DEFAULT_TOLERANCE = 1.0E-10d;

    private class FacetsContributionVisitor implements BSPTreeVisitor<Euclidean3D> {
        FacetsContributionVisitor() {
            PolyhedronsSet.this.setSize(0.0d);
            Vector3D vector3D = new Vector3D(0.0d, 0.0d, 0.0d);
            PolyhedronsSet.this.setBarycenter((Point<S>) vector3D);
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
            Region<Euclidean2D> polygon = ((SubPlane) facet).getRemainingRegion();
            double area = polygon.getSize();
            if (Double.isInfinite(area)) {
                PolyhedronsSet.this.setSize(Double.POSITIVE_INFINITY);
                PolyhedronsSet.this.setBarycenter((Point<S>) Vector3D.NaN);
                return;
            }
            Plane plane = (Plane) facet.getHyperplane();
            Vector3D facetB = plane.toSpace(polygon.getBarycenter());
            double scaled = facetB.dotProduct(plane.getNormal()) * area;
            if (reversed) {
                scaled = -scaled;
            }
            double scaled2 = scaled;
            PolyhedronsSet.this.setSize(PolyhedronsSet.this.getSize() + scaled2);
            Vector3D vector3D = r6;
            PolyhedronsSet polyhedronsSet = PolyhedronsSet.this;
            Vector3D vector3D2 = new Vector3D(1.0d, (Vector3D) PolyhedronsSet.this.getBarycenter(), scaled2, facetB);
            polyhedronsSet.setBarycenter((Point<S>) vector3D);
        }
    }

    private static class RotationTransform implements Transform<Euclidean3D, Euclidean2D> {
        private Plane cachedOriginal;
        private Transform<Euclidean2D, Euclidean1D> cachedTransform;
        private Vector3D center;
        private Rotation rotation;

        RotationTransform(Vector3D center2, Rotation rotation2) {
            this.center = center2;
            this.rotation = rotation2;
        }

        public Vector3D apply(Point<Euclidean3D> point) {
            Vector3D vector3D = new Vector3D(1.0d, this.center, 1.0d, this.rotation.applyTo(((Vector3D) point).subtract((Vector) this.center)));
            return vector3D;
        }

        public Plane apply(Hyperplane<Euclidean3D> hyperplane) {
            return ((Plane) hyperplane).rotate(this.center, this.rotation);
        }

        public SubHyperplane<Euclidean2D> apply(SubHyperplane<Euclidean2D> sub, Hyperplane<Euclidean3D> original, Hyperplane<Euclidean3D> transformed) {
            Hyperplane<Euclidean3D> hyperplane = original;
            if (hyperplane != this.cachedOriginal) {
                Plane oPlane = (Plane) hyperplane;
                Plane tPlane = (Plane) transformed;
                Vector3D p00 = oPlane.getOrigin();
                Vector3D p10 = oPlane.toSpace((Point) new Vector2D(1.0d, 0.0d));
                Vector3D p01 = oPlane.toSpace((Point) new Vector2D(0.0d, 1.0d));
                Vector2D tP00 = tPlane.toSubSpace((Point) apply((Point) p00));
                Vector2D tP10 = tPlane.toSubSpace((Point) apply((Point) p10));
                Vector2D tP01 = tPlane.toSubSpace((Point) apply((Point) p01));
                this.cachedOriginal = (Plane) hyperplane;
                this.cachedTransform = Line.getTransform(tP10.getX() - tP00.getX(), tP10.getY() - tP00.getY(), tP01.getX() - tP00.getX(), tP01.getY() - tP00.getY(), tP00.getX(), tP00.getY());
            }
            return ((SubLine) sub).applyTransform(this.cachedTransform);
        }
    }

    private static class TranslationTransform implements Transform<Euclidean3D, Euclidean2D> {
        private Plane cachedOriginal;
        private Transform<Euclidean2D, Euclidean1D> cachedTransform;
        private Vector3D translation;

        TranslationTransform(Vector3D translation2) {
            this.translation = translation2;
        }

        public Vector3D apply(Point<Euclidean3D> point) {
            Vector3D vector3D = new Vector3D(1.0d, (Vector3D) point, 1.0d, this.translation);
            return vector3D;
        }

        public Plane apply(Hyperplane<Euclidean3D> hyperplane) {
            return ((Plane) hyperplane).translate(this.translation);
        }

        public SubHyperplane<Euclidean2D> apply(SubHyperplane<Euclidean2D> sub, Hyperplane<Euclidean3D> original, Hyperplane<Euclidean3D> transformed) {
            Hyperplane<Euclidean3D> hyperplane = original;
            if (hyperplane != this.cachedOriginal) {
                Vector2D shift = ((Plane) transformed).toSubSpace((Point) apply((Point) ((Plane) hyperplane).getOrigin()));
                this.cachedOriginal = (Plane) hyperplane;
                this.cachedTransform = Line.getTransform(1.0d, 0.0d, 0.0d, 1.0d, shift.getX(), shift.getY());
            }
            return ((SubLine) sub).applyTransform(this.cachedTransform);
        }
    }

    public PolyhedronsSet(double tolerance) {
        super(tolerance);
    }

    public PolyhedronsSet(BSPTree<Euclidean3D> tree, double tolerance) {
        super(tree, tolerance);
    }

    public PolyhedronsSet(Collection<SubHyperplane<Euclidean3D>> boundary, double tolerance) {
        super(boundary, tolerance);
    }

    public PolyhedronsSet(List<Vector3D> vertices, List<int[]> facets, double tolerance) {
        super((Collection<SubHyperplane<S>>) buildBoundary(vertices, facets, tolerance), tolerance);
    }

    public PolyhedronsSet(double xMin, double xMax, double yMin, double yMax, double zMin, double zMax, double tolerance) {
        super(buildBoundary(xMin, xMax, yMin, yMax, zMin, zMax, tolerance), tolerance);
    }

    @Deprecated
    public PolyhedronsSet() {
        this(1.0E-10d);
    }

    @Deprecated
    public PolyhedronsSet(BSPTree<Euclidean3D> tree) {
        this(tree, 1.0E-10d);
    }

    @Deprecated
    public PolyhedronsSet(Collection<SubHyperplane<Euclidean3D>> boundary) {
        this(boundary, 1.0E-10d);
    }

    @Deprecated
    public PolyhedronsSet(double xMin, double xMax, double yMin, double yMax, double zMin, double zMax) {
        this(xMin, xMax, yMin, yMax, zMin, zMax, 1.0E-10d);
    }

    private static BSPTree<Euclidean3D> buildBoundary(double xMin, double xMax, double yMin, double yMax, double zMin, double zMax, double tolerance) {
        double d = tolerance;
        if (xMin >= xMax - d || yMin >= yMax - d || zMin >= zMax - d) {
            return new BSPTree<>(Boolean.FALSE);
        }
        Vector3D vector3D = new Vector3D(xMin, 0.0d, 0.0d);
        Plane pxMin = new Plane(vector3D, Vector3D.MINUS_I, d);
        Vector3D vector3D2 = new Vector3D(xMax, 0.0d, 0.0d);
        Plane pxMax = new Plane(vector3D2, Vector3D.PLUS_I, d);
        Vector3D vector3D3 = new Vector3D(0.0d, yMin, 0.0d);
        Plane pyMin = new Plane(vector3D3, Vector3D.MINUS_J, d);
        Vector3D vector3D4 = new Vector3D(0.0d, yMax, 0.0d);
        Plane pyMax = new Plane(vector3D4, Vector3D.PLUS_J, d);
        Vector3D vector3D5 = new Vector3D(0.0d, 0.0d, zMin);
        Plane pzMin = new Plane(vector3D5, Vector3D.MINUS_K, d);
        Vector3D vector3D6 = new Vector3D(0.0d, 0.0d, zMax);
        Plane pzMax = new Plane(vector3D6, Vector3D.PLUS_K, d);
        return new RegionFactory().buildConvex(pxMin, pxMax, pyMin, pyMax, pzMin, pzMax).getTree(false);
    }

    private static List<SubHyperplane<Euclidean3D>> buildBoundary(List<Vector3D> vertices, List<int[]> facets, double tolerance) {
        List<Vector3D> list = vertices;
        double d = tolerance;
        for (int i = 0; i < vertices.size() - 1; i++) {
            Vector3D vi = (Vector3D) list.get(i);
            for (int j = i + 1; j < vertices.size(); j++) {
                if (Vector3D.distance(vi, (Vector3D) list.get(j)) <= d) {
                    throw new MathIllegalArgumentException(LocalizedFormats.CLOSE_VERTICES, Double.valueOf(vi.getX()), Double.valueOf(vi.getY()), Double.valueOf(vi.getZ()));
                }
            }
        }
        int[][] successors = successors(list, facets, findReferences(vertices, facets));
        int vA = 0;
        while (vA < vertices.size()) {
            int[] arr$ = successors[vA];
            int i$ = 0;
            for (int len$ = arr$.length; i$ < len$; len$ = len$) {
                int vB = arr$[i$];
                if (vB >= 0) {
                    int[] arr$2 = successors[vB];
                    int len$2 = arr$2.length;
                    boolean found = false;
                    for (int i$2 = 0; i$2 < len$2; i$2++) {
                        found = found || arr$2[i$2] == vA;
                    }
                    if (!found) {
                        Vector3D start = (Vector3D) list.get(vA);
                        Vector3D end = (Vector3D) list.get(vB);
                        int[] iArr = arr$;
                        int i2 = len$;
                        int i3 = vB;
                        Vector3D vector3D = start;
                        throw new MathIllegalArgumentException(LocalizedFormats.EDGE_CONNECTED_TO_ONE_FACET, Double.valueOf(start.getX()), Double.valueOf(start.getY()), Double.valueOf(start.getZ()), Double.valueOf(end.getX()), Double.valueOf(end.getY()), Double.valueOf(end.getZ()));
                    }
                }
                i$++;
                arr$ = arr$;
            }
            vA++;
        }
        List<SubHyperplane<Euclidean3D>> boundary = new ArrayList<>();
        Iterator i$3 = facets.iterator();
        while (true) {
            Iterator i$4 = i$3;
            if (!i$4.hasNext()) {
                return boundary;
            }
            int[] facet = (int[]) i$4.next();
            Plane plane = new Plane((Vector3D) list.get(facet[0]), (Vector3D) list.get(facet[1]), (Vector3D) list.get(facet[2]), d);
            Vector2D[] two2Points = new Vector2D[facet.length];
            int i4 = 0;
            while (i4 < facet.length) {
                Vector3D v = (Vector3D) list.get(facet[i4]);
                if (!plane.contains(v)) {
                    Iterator it = i$4;
                    int[] iArr2 = facet;
                    throw new MathIllegalArgumentException(LocalizedFormats.OUT_OF_PLANE, Double.valueOf(v.getX()), Double.valueOf(v.getY()), Double.valueOf(v.getZ()));
                }
                int[] facet2 = facet;
                two2Points[i4] = plane.toSubSpace((Vector<Euclidean3D>) v);
                i4++;
                facet = facet2;
            }
            Iterator i$5 = i$4;
            int[] iArr3 = facet;
            boundary.add(new SubPlane(plane, new PolygonsSet(d, two2Points)));
            i$3 = i$5;
        }
    }

    private static int[][] findReferences(List<Vector3D> vertices, List<int[]> facets) {
        int[] arr$;
        int[] nbFacets = new int[vertices.size()];
        int maxFacets = 0;
        Iterator i$ = facets.iterator();
        while (true) {
            if (i$.hasNext()) {
                int[] facet = (int[]) i$.next();
                if (facet.length < 3) {
                    throw new NumberIsTooSmallException(LocalizedFormats.WRONG_NUMBER_OF_POINTS, Integer.valueOf(3), Integer.valueOf(facet.length), true);
                }
                for (int index : facet) {
                    int i = nbFacets[index] + 1;
                    nbFacets[index] = i;
                    maxFacets = FastMath.max(maxFacets, i);
                }
            } else {
                int[][] references = (int[][]) Array.newInstance(int.class, new int[]{vertices.size(), maxFacets});
                for (int[] r : references) {
                    Arrays.fill(r, -1);
                }
                for (int f = 0; f < facets.size(); f++) {
                    int[] arr$2 = (int[]) facets.get(f);
                    int len$ = arr$2.length;
                    for (int i$2 = 0; i$2 < len$; i$2++) {
                        int v = arr$2[i$2];
                        int k = 0;
                        while (k < maxFacets && references[v][k] >= 0) {
                            k++;
                        }
                        references[v][k] = f;
                    }
                }
                return references;
            }
        }
    }

    private static int[][] successors(List<Vector3D> vertices, List<int[]> facets, int[][] references) {
        List<Vector3D> list = vertices;
        int[][] successors = (int[][]) Array.newInstance(int.class, new int[]{vertices.size(), references[0].length});
        for (int[] s : successors) {
            Arrays.fill(s, -1);
        }
        int v = 0;
        while (v < vertices.size()) {
            int k = 0;
            while (k < successors[v].length && references[v][k] >= 0) {
                int[] facet = (int[]) facets.get(references[v][k]);
                int i = 0;
                while (i < facet.length && facet[i] != v) {
                    i++;
                }
                successors[v][k] = facet[(i + 1) % facet.length];
                int l = 0;
                while (l < k) {
                    if (successors[v][l] == successors[v][k]) {
                        Vector3D start = (Vector3D) list.get(v);
                        Vector3D end = (Vector3D) list.get(successors[v][k]);
                        int[] iArr = facet;
                        throw new MathIllegalArgumentException(LocalizedFormats.FACET_ORIENTATION_MISMATCH, Double.valueOf(start.getX()), Double.valueOf(start.getY()), Double.valueOf(start.getZ()), Double.valueOf(end.getX()), Double.valueOf(end.getY()), Double.valueOf(end.getZ()));
                    }
                    l++;
                    list = vertices;
                    List<int[]> list2 = facets;
                }
                k++;
                list = vertices;
            }
            v++;
            list = vertices;
        }
        return successors;
    }

    public PolyhedronsSet buildNew(BSPTree<Euclidean3D> tree) {
        return new PolyhedronsSet(tree, getTolerance());
    }

    /* access modifiers changed from: protected */
    public void computeGeometricalProperties() {
        getTree(true).visit(new FacetsContributionVisitor());
        if (getSize() < 0.0d) {
            setSize(Double.POSITIVE_INFINITY);
            setBarycenter((Point<S>) Vector3D.NaN);
            return;
        }
        setSize(getSize() / 3.0d);
        setBarycenter((Point<S>) new Vector3D<S>(1.0d / (getSize() * 4.0d), (Vector3D) getBarycenter()));
    }

    public SubHyperplane<Euclidean3D> firstIntersection(Vector3D point, Line line) {
        return recurseFirstIntersection(getTree(true), point, line);
    }

    private SubHyperplane<Euclidean3D> recurseFirstIntersection(BSPTree<Euclidean3D> node, Vector3D point, Line line) {
        BSPTree<Euclidean3D> far;
        BSPTree<Euclidean3D> near;
        BSPTree<Euclidean3D> bSPTree = node;
        Vector3D vector3D = point;
        Line line2 = line;
        SubHyperplane<Euclidean3D> cut = node.getCut();
        if (cut == null) {
            return null;
        }
        BSPTree<Euclidean3D> minus = node.getMinus();
        BSPTree<Euclidean3D> plus = node.getPlus();
        Plane plane = (Plane) cut.getHyperplane();
        double offset = plane.getOffset((Point<Euclidean3D>) vector3D);
        boolean in = FastMath.abs(offset) < getTolerance();
        if (offset < 0.0d) {
            near = minus;
            far = plus;
        } else {
            near = plus;
            far = minus;
        }
        if (in) {
            SubHyperplane<Euclidean3D> facet = boundaryFacet(vector3D, bSPTree);
            if (facet != null) {
                return facet;
            }
        }
        SubHyperplane<Euclidean3D> crossed = recurseFirstIntersection(near, vector3D, line2);
        if (crossed != null) {
            return crossed;
        }
        if (!in) {
            Vector3D hit3D = plane.intersection(line2);
            if (hit3D != null && line2.getAbscissa(hit3D) > line2.getAbscissa(vector3D)) {
                SubHyperplane<Euclidean3D> facet2 = boundaryFacet(hit3D, bSPTree);
                if (facet2 != null) {
                    return facet2;
                }
            }
        }
        return recurseFirstIntersection(far, vector3D, line2);
    }

    private SubHyperplane<Euclidean3D> boundaryFacet(Vector3D point, BSPTree<Euclidean3D> node) {
        Vector2D point2D = ((Plane) node.getCut().getHyperplane()).toSubSpace((Point) point);
        BoundaryAttribute<Euclidean3D> attribute = (BoundaryAttribute) node.getAttribute();
        if (attribute.getPlusOutside() != null && ((SubPlane) attribute.getPlusOutside()).getRemainingRegion().checkPoint(point2D) == Location.INSIDE) {
            return attribute.getPlusOutside();
        }
        if (attribute.getPlusInside() == null || ((SubPlane) attribute.getPlusInside()).getRemainingRegion().checkPoint(point2D) != Location.INSIDE) {
            return null;
        }
        return attribute.getPlusInside();
    }

    public PolyhedronsSet rotate(Vector3D center, Rotation rotation) {
        return (PolyhedronsSet) applyTransform(new RotationTransform(center, rotation));
    }

    public PolyhedronsSet translate(Vector3D translation) {
        return (PolyhedronsSet) applyTransform(new TranslationTransform(translation));
    }
}

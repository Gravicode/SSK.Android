package org.apache.commons.math3.geometry.partitioning;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.Space;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor.Order;
import org.apache.commons.math3.geometry.partitioning.Region.Location;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane.SplitSubHyperplane;

public abstract class AbstractRegion<S extends Space, T extends Space> implements Region<S> {
    private Point<S> barycenter;
    private double size;
    private final double tolerance;
    private BSPTree<S> tree;

    public abstract AbstractRegion<S, T> buildNew(BSPTree<S> bSPTree);

    /* access modifiers changed from: protected */
    public abstract void computeGeometricalProperties();

    protected AbstractRegion(double tolerance2) {
        this.tree = new BSPTree<>(Boolean.TRUE);
        this.tolerance = tolerance2;
    }

    protected AbstractRegion(BSPTree<S> tree2, double tolerance2) {
        this.tree = tree2;
        this.tolerance = tolerance2;
    }

    protected AbstractRegion(Collection<SubHyperplane<S>> boundary, double tolerance2) {
        this.tolerance = tolerance2;
        if (boundary.size() == 0) {
            this.tree = new BSPTree<>(Boolean.TRUE);
            return;
        }
        TreeSet<SubHyperplane<S>> ordered = new TreeSet<>(new Comparator<SubHyperplane<S>>() {
            public int compare(SubHyperplane<S> o1, SubHyperplane<S> o2) {
                if (o2.getSize() < o1.getSize()) {
                    return -1;
                }
                return o1 == o2 ? 0 : 1;
            }
        });
        ordered.addAll(boundary);
        this.tree = new BSPTree<>();
        insertCuts(this.tree, ordered);
        this.tree.visit(new BSPTreeVisitor<S>() {
            public Order visitOrder(BSPTree<S> bSPTree) {
                return Order.PLUS_SUB_MINUS;
            }

            public void visitInternalNode(BSPTree<S> bSPTree) {
            }

            public void visitLeafNode(BSPTree<S> node) {
                if (node.getParent() == null || node == node.getParent().getMinus()) {
                    node.setAttribute(Boolean.TRUE);
                } else {
                    node.setAttribute(Boolean.FALSE);
                }
            }
        });
    }

    public AbstractRegion(Hyperplane<S>[] hyperplanes, double tolerance2) {
        this.tolerance = tolerance2;
        if (hyperplanes == null || hyperplanes.length == 0) {
            this.tree = new BSPTree<>(Boolean.FALSE);
            return;
        }
        this.tree = hyperplanes[0].wholeSpace().getTree(false);
        BSPTree<S> node = this.tree;
        node.setAttribute(Boolean.TRUE);
        for (Hyperplane<S> hyperplane : hyperplanes) {
            if (node.insertCut(hyperplane)) {
                node.setAttribute(null);
                node.getPlus().setAttribute(Boolean.FALSE);
                node = node.getMinus();
                node.setAttribute(Boolean.TRUE);
            }
        }
    }

    public double getTolerance() {
        return this.tolerance;
    }

    private void insertCuts(BSPTree<S> node, Collection<SubHyperplane<S>> boundary) {
        Iterator<SubHyperplane<S>> iterator = boundary.iterator();
        Hyperplane<S> inserted = null;
        while (inserted == null && iterator.hasNext()) {
            inserted = ((SubHyperplane) iterator.next()).getHyperplane();
            if (!node.insertCut(inserted.copySelf())) {
                inserted = null;
            }
        }
        if (iterator.hasNext()) {
            ArrayList<SubHyperplane<S>> plusList = new ArrayList<>();
            ArrayList<SubHyperplane<S>> minusList = new ArrayList<>();
            while (iterator.hasNext()) {
                SubHyperplane<S> other = (SubHyperplane) iterator.next();
                SplitSubHyperplane<S> split = other.split(inserted);
                switch (split.getSide()) {
                    case PLUS:
                        plusList.add(other);
                        break;
                    case MINUS:
                        minusList.add(other);
                        break;
                    case BOTH:
                        plusList.add(split.getPlus());
                        minusList.add(split.getMinus());
                        break;
                }
            }
            insertCuts(node.getPlus(), plusList);
            insertCuts(node.getMinus(), minusList);
        }
    }

    public AbstractRegion<S, T> copySelf() {
        return buildNew(this.tree.copySelf());
    }

    public boolean isEmpty() {
        return isEmpty(this.tree);
    }

    public boolean isEmpty(BSPTree<S> node) {
        boolean z = true;
        if (node.getCut() == null) {
            return !((Boolean) node.getAttribute()).booleanValue();
        }
        if (!isEmpty(node.getMinus()) || !isEmpty(node.getPlus())) {
            z = false;
        }
        return z;
    }

    public boolean isFull() {
        return isFull(this.tree);
    }

    public boolean isFull(BSPTree<S> node) {
        if (node.getCut() == null) {
            return ((Boolean) node.getAttribute()).booleanValue();
        }
        return isFull(node.getMinus()) && isFull(node.getPlus());
    }

    public boolean contains(Region<S> region) {
        return new RegionFactory().difference(region, this).isEmpty();
    }

    public BoundaryProjection<S> projectToBoundary(Point<S> point) {
        BoundaryProjector<S, T> projector = new BoundaryProjector<>(point);
        getTree(true).visit(projector);
        return projector.getProjection();
    }

    public Location checkPoint(Vector<S> point) {
        return checkPoint((Point<S>) point);
    }

    public Location checkPoint(Point<S> point) {
        return checkPoint(this.tree, point);
    }

    /* access modifiers changed from: protected */
    public Location checkPoint(BSPTree<S> node, Vector<S> point) {
        return checkPoint(node, (Point<S>) point);
    }

    /* access modifiers changed from: protected */
    public Location checkPoint(BSPTree<S> node, Point<S> point) {
        BSPTree<S> cell = node.getCell(point, this.tolerance);
        if (cell.getCut() == null) {
            return ((Boolean) cell.getAttribute()).booleanValue() ? Location.INSIDE : Location.OUTSIDE;
        }
        Location minusCode = checkPoint(cell.getMinus(), point);
        return minusCode == checkPoint(cell.getPlus(), point) ? minusCode : Location.BOUNDARY;
    }

    public BSPTree<S> getTree(boolean includeBoundaryAttributes) {
        if (includeBoundaryAttributes && this.tree.getCut() != null && this.tree.getAttribute() == null) {
            this.tree.visit(new BoundaryBuilder());
        }
        return this.tree;
    }

    public double getBoundarySize() {
        BoundarySizeVisitor<S> visitor = new BoundarySizeVisitor<>();
        getTree(true).visit(visitor);
        return visitor.getSize();
    }

    public double getSize() {
        if (this.barycenter == null) {
            computeGeometricalProperties();
        }
        return this.size;
    }

    /* access modifiers changed from: protected */
    public void setSize(double size2) {
        this.size = size2;
    }

    public Point<S> getBarycenter() {
        if (this.barycenter == null) {
            computeGeometricalProperties();
        }
        return this.barycenter;
    }

    /* access modifiers changed from: protected */
    public void setBarycenter(Vector<S> barycenter2) {
        setBarycenter((Point<S>) barycenter2);
    }

    /* access modifiers changed from: protected */
    public void setBarycenter(Point<S> barycenter2) {
        this.barycenter = barycenter2;
    }

    @Deprecated
    public Side side(Hyperplane<S> hyperplane) {
        InsideFinder<S> finder = new InsideFinder<>(this);
        finder.recurseSides(this.tree, hyperplane.wholeHyperplane());
        return finder.plusFound() ? finder.minusFound() ? Side.BOTH : Side.PLUS : finder.minusFound() ? Side.MINUS : Side.HYPER;
    }

    public SubHyperplane<S> intersection(SubHyperplane<S> sub) {
        return recurseIntersection(this.tree, sub);
    }

    private SubHyperplane<S> recurseIntersection(BSPTree<S> node, SubHyperplane<S> sub) {
        if (node.getCut() == null) {
            return ((Boolean) node.getAttribute()).booleanValue() ? sub.copySelf() : null;
        }
        SplitSubHyperplane<S> split = sub.split(node.getCut().getHyperplane());
        if (split.getPlus() != null) {
            if (split.getMinus() == null) {
                return recurseIntersection(node.getPlus(), sub);
            }
            SubHyperplane<S> plus = recurseIntersection(node.getPlus(), split.getPlus());
            SubHyperplane<S> minus = recurseIntersection(node.getMinus(), split.getMinus());
            if (plus == null) {
                return minus;
            }
            if (minus == null) {
                return plus;
            }
            return plus.reunite(minus);
        } else if (split.getMinus() != null) {
            return recurseIntersection(node.getMinus(), sub);
        } else {
            return recurseIntersection(node.getPlus(), recurseIntersection(node.getMinus(), sub));
        }
    }

    public AbstractRegion<S, T> applyTransform(Transform<S, T> transform) {
        Map<BSPTree<S>, BSPTree<S>> map = new HashMap<>();
        BSPTree<S> transformedTree = recurseTransform(getTree(false), transform, map);
        for (Entry<BSPTree<S>, BSPTree<S>> entry : map.entrySet()) {
            if (((BSPTree) entry.getKey()).getCut() != null) {
                BoundaryAttribute<S> original = (BoundaryAttribute) ((BSPTree) entry.getKey()).getAttribute();
                if (original != null) {
                    BoundaryAttribute<S> transformed = (BoundaryAttribute) ((BSPTree) entry.getValue()).getAttribute();
                    Iterator i$ = original.getSplitters().iterator();
                    while (i$.hasNext()) {
                        transformed.getSplitters().add((BSPTree) map.get((BSPTree) i$.next()));
                    }
                }
            }
        }
        return buildNew(transformedTree);
    }

    private BSPTree<S> recurseTransform(BSPTree<S> node, Transform<S, T> transform, Map<BSPTree<S>, BSPTree<S>> map) {
        BSPTree<S> transformedNode;
        if (node.getCut() == null) {
            transformedNode = new BSPTree<>(node.getAttribute());
        } else {
            SubHyperplane<S> tSub = ((AbstractSubHyperplane) node.getCut()).applyTransform(transform);
            BoundaryAttribute boundaryAttribute = (BoundaryAttribute) node.getAttribute();
            if (boundaryAttribute != null) {
                SubHyperplane<S> tPI = null;
                SubHyperplane applyTransform = boundaryAttribute.getPlusOutside() == null ? null : ((AbstractSubHyperplane) boundaryAttribute.getPlusOutside()).applyTransform(transform);
                if (boundaryAttribute.getPlusInside() != null) {
                    tPI = ((AbstractSubHyperplane) boundaryAttribute.getPlusInside()).applyTransform(transform);
                }
                boundaryAttribute = new BoundaryAttribute(applyTransform, tPI, new NodesSet());
            }
            transformedNode = new BSPTree<>(tSub, recurseTransform(node.getPlus(), transform, map), recurseTransform(node.getMinus(), transform, map), boundaryAttribute);
        }
        map.put(node, transformedNode);
        return transformedNode;
    }
}

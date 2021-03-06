package org.apache.commons.math3.geometry.euclidean.oned;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.partitioning.AbstractRegion;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.BoundaryProjection;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.util.Precision;

public class IntervalsSet extends AbstractRegion<Euclidean1D, Euclidean1D> implements Iterable<double[]> {
    private static final double DEFAULT_TOLERANCE = 1.0E-10d;

    private class SubIntervalsIterator implements Iterator<double[]> {
        private BSPTree<Euclidean1D> current;
        private double[] pending;

        SubIntervalsIterator() {
            this.current = IntervalsSet.this.getFirstIntervalBoundary();
            if (this.current == null) {
                if (((Boolean) IntervalsSet.this.getFirstLeaf(IntervalsSet.this.getTree(false)).getAttribute()).booleanValue()) {
                    this.pending = new double[]{Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY};
                } else {
                    this.pending = null;
                }
            } else if (IntervalsSet.this.isIntervalEnd(this.current)) {
                this.pending = new double[]{Double.NEGATIVE_INFINITY, IntervalsSet.this.getAngle(this.current)};
            } else {
                selectPending();
            }
        }

        private void selectPending() {
            BSPTree<Euclidean1D> start = this.current;
            while (start != null && !IntervalsSet.this.isIntervalStart(start)) {
                start = IntervalsSet.this.nextInternalNode(start);
            }
            if (start == null) {
                this.current = null;
                this.pending = null;
                return;
            }
            BSPTree<Euclidean1D> end = start;
            while (end != null && !IntervalsSet.this.isIntervalEnd(end)) {
                end = IntervalsSet.this.nextInternalNode(end);
            }
            if (end != null) {
                this.pending = new double[]{IntervalsSet.this.getAngle(start), IntervalsSet.this.getAngle(end)};
                this.current = end;
            } else {
                this.pending = new double[]{IntervalsSet.this.getAngle(start), Double.POSITIVE_INFINITY};
                this.current = null;
            }
        }

        public boolean hasNext() {
            return this.pending != null;
        }

        public double[] next() {
            if (this.pending == null) {
                throw new NoSuchElementException();
            }
            double[] next = this.pending;
            selectPending();
            return next;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public IntervalsSet(double tolerance) {
        super(tolerance);
    }

    public IntervalsSet(double lower, double upper, double tolerance) {
        super(buildTree(lower, upper, tolerance), tolerance);
    }

    public IntervalsSet(BSPTree<Euclidean1D> tree, double tolerance) {
        super(tree, tolerance);
    }

    public IntervalsSet(Collection<SubHyperplane<Euclidean1D>> boundary, double tolerance) {
        super(boundary, tolerance);
    }

    @Deprecated
    public IntervalsSet() {
        this(1.0E-10d);
    }

    @Deprecated
    public IntervalsSet(double lower, double upper) {
        this(lower, upper, 1.0E-10d);
    }

    @Deprecated
    public IntervalsSet(BSPTree<Euclidean1D> tree) {
        this(tree, 1.0E-10d);
    }

    @Deprecated
    public IntervalsSet(Collection<SubHyperplane<Euclidean1D>> boundary) {
        this(boundary, 1.0E-10d);
    }

    private static BSPTree<Euclidean1D> buildTree(double lower, double upper, double tolerance) {
        if (!Double.isInfinite(lower) || lower >= 0.0d) {
            SubHyperplane<Euclidean1D> lowerCut = new OrientedPoint(new Vector1D(lower), false, tolerance).wholeHyperplane();
            if (Double.isInfinite(upper) && upper > 0.0d) {
                return new BSPTree<>(lowerCut, new BSPTree(Boolean.FALSE), new BSPTree(Boolean.TRUE), null);
            }
            return new BSPTree<>(lowerCut, new BSPTree(Boolean.FALSE), new BSPTree(new OrientedPoint(new Vector1D(upper), true, tolerance).wholeHyperplane(), new BSPTree(Boolean.FALSE), new BSPTree(Boolean.TRUE), null), null);
        } else if (!Double.isInfinite(upper) || upper <= 0.0d) {
            return new BSPTree<>(new OrientedPoint(new Vector1D(upper), true, tolerance).wholeHyperplane(), new BSPTree(Boolean.FALSE), new BSPTree(Boolean.TRUE), null);
        } else {
            return new BSPTree<>(Boolean.TRUE);
        }
    }

    public IntervalsSet buildNew(BSPTree<Euclidean1D> tree) {
        return new IntervalsSet(tree, getTolerance());
    }

    /* access modifiers changed from: protected */
    public void computeGeometricalProperties() {
        if (getTree(false).getCut() == null) {
            setBarycenter((Point<S>) Vector1D.NaN);
            setSize(((Boolean) getTree(false).getAttribute()).booleanValue() ? Double.POSITIVE_INFINITY : 0.0d);
            return;
        }
        double size = 0.0d;
        double sum = 0.0d;
        for (Interval interval : asList()) {
            size += interval.getSize();
            sum += interval.getSize() * interval.getBarycenter();
        }
        setSize(size);
        if (Double.isInfinite(size)) {
            setBarycenter((Point<S>) Vector1D.NaN);
        } else if (size >= Precision.SAFE_MIN) {
            setBarycenter((Point<S>) new Vector1D<S>(sum / size));
        } else {
            setBarycenter((Point<S>) ((OrientedPoint) getTree(false).getCut().getHyperplane()).getLocation());
        }
    }

    public double getInf() {
        BSPTree tree = getTree(false);
        double inf = Double.POSITIVE_INFINITY;
        while (tree.getCut() != null) {
            OrientedPoint op = (OrientedPoint) tree.getCut().getHyperplane();
            inf = op.getLocation().getX();
            tree = op.isDirect() ? tree.getMinus() : tree.getPlus();
        }
        if (((Boolean) tree.getAttribute()).booleanValue()) {
            return Double.NEGATIVE_INFINITY;
        }
        return inf;
    }

    public double getSup() {
        BSPTree tree = getTree(false);
        double sup = Double.NEGATIVE_INFINITY;
        while (tree.getCut() != null) {
            OrientedPoint op = (OrientedPoint) tree.getCut().getHyperplane();
            sup = op.getLocation().getX();
            tree = op.isDirect() ? tree.getPlus() : tree.getMinus();
        }
        if (((Boolean) tree.getAttribute()).booleanValue()) {
            return Double.POSITIVE_INFINITY;
        }
        return sup;
    }

    public BoundaryProjection<Euclidean1D> projectToBoundary(Point<Euclidean1D> point) {
        double x = ((Vector1D) point).getX();
        double previous = Double.NEGATIVE_INFINITY;
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            double[] a = (double[]) i$.next();
            if (x < a[0]) {
                double previousOffset = x - previous;
                double currentOffset = a[0] - x;
                if (previousOffset < currentOffset) {
                    return new BoundaryProjection<>(point, finiteOrNullPoint(previous), previousOffset);
                }
                return new BoundaryProjection<>(point, finiteOrNullPoint(a[0]), currentOffset);
            } else if (x <= a[1]) {
                double offset0 = a[0] - x;
                double offset1 = x - a[1];
                if (offset0 < offset1) {
                    return new BoundaryProjection<>(point, finiteOrNullPoint(a[1]), offset1);
                }
                return new BoundaryProjection<>(point, finiteOrNullPoint(a[0]), offset0);
            } else {
                previous = a[1];
            }
        }
        return new BoundaryProjection<>(point, finiteOrNullPoint(previous), x - previous);
    }

    private Vector1D finiteOrNullPoint(double x) {
        if (Double.isInfinite(x)) {
            return null;
        }
        return new Vector1D(x);
    }

    public List<Interval> asList() {
        List<Interval> list = new ArrayList<>();
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            double[] a = (double[]) i$.next();
            list.add(new Interval(a[0], a[1]));
        }
        return list;
    }

    /* access modifiers changed from: private */
    public BSPTree<Euclidean1D> getFirstLeaf(BSPTree<Euclidean1D> root) {
        if (root.getCut() == null) {
            return root;
        }
        BSPTree<Euclidean1D> smallest = null;
        BSPTree<Euclidean1D> n = root;
        while (n != null) {
            smallest = n;
            n = previousInternalNode(n);
        }
        return leafBefore(smallest);
    }

    /* access modifiers changed from: private */
    public BSPTree<Euclidean1D> getFirstIntervalBoundary() {
        BSPTree<Euclidean1D> node = getTree(false);
        if (node.getCut() == null) {
            return null;
        }
        BSPTree<Euclidean1D> node2 = getFirstLeaf(node).getParent();
        while (node2 != null && !isIntervalStart(node2) && !isIntervalEnd(node2)) {
            node2 = nextInternalNode(node2);
        }
        return node2;
    }

    /* access modifiers changed from: private */
    public boolean isIntervalStart(BSPTree<Euclidean1D> node) {
        if (!((Boolean) leafBefore(node).getAttribute()).booleanValue() && ((Boolean) leafAfter(node).getAttribute()).booleanValue()) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public boolean isIntervalEnd(BSPTree<Euclidean1D> node) {
        if (((Boolean) leafBefore(node).getAttribute()).booleanValue() && !((Boolean) leafAfter(node).getAttribute()).booleanValue()) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public BSPTree<Euclidean1D> nextInternalNode(BSPTree<Euclidean1D> node) {
        if (childAfter(node).getCut() != null) {
            return leafAfter(node).getParent();
        }
        while (isAfterParent(node)) {
            node = node.getParent();
        }
        return node.getParent();
    }

    private BSPTree<Euclidean1D> previousInternalNode(BSPTree<Euclidean1D> node) {
        if (childBefore(node).getCut() != null) {
            return leafBefore(node).getParent();
        }
        while (isBeforeParent(node)) {
            node = node.getParent();
        }
        return node.getParent();
    }

    private BSPTree<Euclidean1D> leafBefore(BSPTree<Euclidean1D> node) {
        BSPTree<Euclidean1D> node2 = childBefore(node);
        while (node2.getCut() != null) {
            node2 = childAfter(node2);
        }
        return node2;
    }

    private BSPTree<Euclidean1D> leafAfter(BSPTree<Euclidean1D> node) {
        BSPTree<Euclidean1D> node2 = childAfter(node);
        while (node2.getCut() != null) {
            node2 = childBefore(node2);
        }
        return node2;
    }

    private boolean isBeforeParent(BSPTree<Euclidean1D> node) {
        BSPTree<Euclidean1D> parent = node.getParent();
        boolean z = false;
        if (parent == null) {
            return false;
        }
        if (node == childBefore(parent)) {
            z = true;
        }
        return z;
    }

    private boolean isAfterParent(BSPTree<Euclidean1D> node) {
        BSPTree<Euclidean1D> parent = node.getParent();
        boolean z = false;
        if (parent == null) {
            return false;
        }
        if (node == childAfter(parent)) {
            z = true;
        }
        return z;
    }

    private BSPTree<Euclidean1D> childBefore(BSPTree<Euclidean1D> node) {
        if (isDirect(node)) {
            return node.getMinus();
        }
        return node.getPlus();
    }

    private BSPTree<Euclidean1D> childAfter(BSPTree<Euclidean1D> node) {
        if (isDirect(node)) {
            return node.getPlus();
        }
        return node.getMinus();
    }

    private boolean isDirect(BSPTree<Euclidean1D> node) {
        return ((OrientedPoint) node.getCut().getHyperplane()).isDirect();
    }

    /* access modifiers changed from: private */
    public double getAngle(BSPTree<Euclidean1D> node) {
        return ((OrientedPoint) node.getCut().getHyperplane()).getLocation().getX();
    }

    public Iterator<double[]> iterator() {
        return new SubIntervalsIterator();
    }
}

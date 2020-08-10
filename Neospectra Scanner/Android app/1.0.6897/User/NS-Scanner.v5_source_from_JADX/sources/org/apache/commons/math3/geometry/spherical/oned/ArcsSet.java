package org.apache.commons.math3.geometry.spherical.oned;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.partitioning.AbstractRegion;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.BoundaryProjection;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;

public class ArcsSet extends AbstractRegion<Sphere1D, Sphere1D> implements Iterable<double[]> {

    public static class InconsistentStateAt2PiWrapping extends MathIllegalArgumentException {
        private static final long serialVersionUID = 20140107;

        public InconsistentStateAt2PiWrapping() {
            super(LocalizedFormats.INCONSISTENT_STATE_AT_2_PI_WRAPPING, new Object[0]);
        }
    }

    public static class Split {
        private final ArcsSet minus;
        private final ArcsSet plus;

        private Split(ArcsSet plus2, ArcsSet minus2) {
            this.plus = plus2;
            this.minus = minus2;
        }

        public ArcsSet getPlus() {
            return this.plus;
        }

        public ArcsSet getMinus() {
            return this.minus;
        }

        public Side getSide() {
            if (this.plus != null) {
                if (this.minus != null) {
                    return Side.BOTH;
                }
                return Side.PLUS;
            } else if (this.minus != null) {
                return Side.MINUS;
            } else {
                return Side.HYPER;
            }
        }
    }

    private class SubArcsIterator implements Iterator<double[]> {
        private BSPTree<Sphere1D> current = this.firstStart;
        private final BSPTree<Sphere1D> firstStart;
        private double[] pending;

        SubArcsIterator() {
            this.firstStart = ArcsSet.this.getFirstArcStart();
            if (this.firstStart != null) {
                selectPending();
            } else if (((Boolean) ArcsSet.this.getFirstLeaf(ArcsSet.this.getTree(false)).getAttribute()).booleanValue()) {
                this.pending = new double[]{0.0d, 6.283185307179586d};
            } else {
                this.pending = null;
            }
        }

        private void selectPending() {
            BSPTree<Sphere1D> start = this.current;
            while (start != null && !ArcsSet.this.isArcStart(start)) {
                start = ArcsSet.this.nextInternalNode(start);
            }
            if (start == null) {
                this.current = null;
                this.pending = null;
                return;
            }
            BSPTree<Sphere1D> end = start;
            while (end != null && !ArcsSet.this.isArcEnd(end)) {
                end = ArcsSet.this.nextInternalNode(end);
            }
            if (end != null) {
                this.pending = new double[]{ArcsSet.this.getAngle(start), ArcsSet.this.getAngle(end)};
                this.current = end;
            } else {
                BSPTree<Sphere1D> end2 = this.firstStart;
                while (end2 != null && !ArcsSet.this.isArcEnd(end2)) {
                    end2 = ArcsSet.this.previousInternalNode(end2);
                }
                if (end2 == null) {
                    throw new MathInternalError();
                }
                this.pending = new double[]{ArcsSet.this.getAngle(start), ArcsSet.this.getAngle(end2) + 6.283185307179586d};
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

    public ArcsSet(double tolerance) {
        super(tolerance);
    }

    public ArcsSet(double lower, double upper, double tolerance) throws NumberIsTooLargeException {
        super(buildTree(lower, upper, tolerance), tolerance);
    }

    public ArcsSet(BSPTree<Sphere1D> tree, double tolerance) throws InconsistentStateAt2PiWrapping {
        super(tree, tolerance);
        check2PiConsistency();
    }

    public ArcsSet(Collection<SubHyperplane<Sphere1D>> boundary, double tolerance) throws InconsistentStateAt2PiWrapping {
        super(boundary, tolerance);
        check2PiConsistency();
    }

    private static BSPTree<Sphere1D> buildTree(double lower, double upper, double tolerance) throws NumberIsTooLargeException {
        double d = lower;
        double d2 = upper;
        double d3 = tolerance;
        if (Precision.equals(d, d2, 0) || d2 - d >= 6.283185307179586d) {
            return new BSPTree<>(Boolean.TRUE);
        }
        if (d > d2) {
            throw new NumberIsTooLargeException(LocalizedFormats.ENDPOINTS_NOT_AN_INTERVAL, Double.valueOf(lower), Double.valueOf(upper), true);
        }
        double normalizedLower = MathUtils.normalizeAngle(d, 3.141592653589793d);
        double normalizedUpper = (d2 - d) + normalizedLower;
        SubHyperplane<Sphere1D> lowerCut = new LimitAngle(new S1Point(normalizedLower), false, d3).wholeHyperplane();
        if (normalizedUpper > 6.283185307179586d) {
            return new BSPTree<>(lowerCut, new BSPTree(new LimitAngle(new S1Point(normalizedUpper - 6.283185307179586d), true, d3).wholeHyperplane(), new BSPTree(Boolean.FALSE), new BSPTree(Boolean.TRUE), null), new BSPTree(Boolean.TRUE), null);
        }
        return new BSPTree<>(lowerCut, new BSPTree(Boolean.FALSE), new BSPTree(new LimitAngle(new S1Point(normalizedUpper), true, d3).wholeHyperplane(), new BSPTree(Boolean.FALSE), new BSPTree(Boolean.TRUE), null), null);
    }

    private void check2PiConsistency() throws InconsistentStateAt2PiWrapping {
        BSPTree<Sphere1D> root = getTree(false);
        if (root.getCut() != null) {
            if (((Boolean) getFirstLeaf(root).getAttribute()).booleanValue() ^ ((Boolean) getLastLeaf(root).getAttribute()).booleanValue()) {
                throw new InconsistentStateAt2PiWrapping();
            }
        }
    }

    /* access modifiers changed from: private */
    public BSPTree<Sphere1D> getFirstLeaf(BSPTree<Sphere1D> root) {
        if (root.getCut() == null) {
            return root;
        }
        BSPTree<Sphere1D> smallest = null;
        BSPTree<Sphere1D> n = root;
        while (n != null) {
            smallest = n;
            n = previousInternalNode(n);
        }
        return leafBefore(smallest);
    }

    private BSPTree<Sphere1D> getLastLeaf(BSPTree<Sphere1D> root) {
        if (root.getCut() == null) {
            return root;
        }
        BSPTree<Sphere1D> largest = null;
        BSPTree<Sphere1D> n = root;
        while (n != null) {
            largest = n;
            n = nextInternalNode(n);
        }
        return leafAfter(largest);
    }

    /* access modifiers changed from: private */
    public BSPTree<Sphere1D> getFirstArcStart() {
        BSPTree<Sphere1D> node = getTree(false);
        if (node.getCut() == null) {
            return null;
        }
        BSPTree<Sphere1D> node2 = getFirstLeaf(node).getParent();
        while (node2 != null && !isArcStart(node2)) {
            node2 = nextInternalNode(node2);
        }
        return node2;
    }

    /* access modifiers changed from: private */
    public boolean isArcStart(BSPTree<Sphere1D> node) {
        if (!((Boolean) leafBefore(node).getAttribute()).booleanValue() && ((Boolean) leafAfter(node).getAttribute()).booleanValue()) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public boolean isArcEnd(BSPTree<Sphere1D> node) {
        if (((Boolean) leafBefore(node).getAttribute()).booleanValue() && !((Boolean) leafAfter(node).getAttribute()).booleanValue()) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public BSPTree<Sphere1D> nextInternalNode(BSPTree<Sphere1D> node) {
        if (childAfter(node).getCut() != null) {
            return leafAfter(node).getParent();
        }
        while (isAfterParent(node)) {
            node = node.getParent();
        }
        return node.getParent();
    }

    /* access modifiers changed from: private */
    public BSPTree<Sphere1D> previousInternalNode(BSPTree<Sphere1D> node) {
        if (childBefore(node).getCut() != null) {
            return leafBefore(node).getParent();
        }
        while (isBeforeParent(node)) {
            node = node.getParent();
        }
        return node.getParent();
    }

    private BSPTree<Sphere1D> leafBefore(BSPTree<Sphere1D> node) {
        BSPTree<Sphere1D> node2 = childBefore(node);
        while (node2.getCut() != null) {
            node2 = childAfter(node2);
        }
        return node2;
    }

    private BSPTree<Sphere1D> leafAfter(BSPTree<Sphere1D> node) {
        BSPTree<Sphere1D> node2 = childAfter(node);
        while (node2.getCut() != null) {
            node2 = childBefore(node2);
        }
        return node2;
    }

    private boolean isBeforeParent(BSPTree<Sphere1D> node) {
        BSPTree<Sphere1D> parent = node.getParent();
        boolean z = false;
        if (parent == null) {
            return false;
        }
        if (node == childBefore(parent)) {
            z = true;
        }
        return z;
    }

    private boolean isAfterParent(BSPTree<Sphere1D> node) {
        BSPTree<Sphere1D> parent = node.getParent();
        boolean z = false;
        if (parent == null) {
            return false;
        }
        if (node == childAfter(parent)) {
            z = true;
        }
        return z;
    }

    private BSPTree<Sphere1D> childBefore(BSPTree<Sphere1D> node) {
        if (isDirect(node)) {
            return node.getMinus();
        }
        return node.getPlus();
    }

    private BSPTree<Sphere1D> childAfter(BSPTree<Sphere1D> node) {
        if (isDirect(node)) {
            return node.getPlus();
        }
        return node.getMinus();
    }

    private boolean isDirect(BSPTree<Sphere1D> node) {
        return ((LimitAngle) node.getCut().getHyperplane()).isDirect();
    }

    /* access modifiers changed from: private */
    public double getAngle(BSPTree<Sphere1D> node) {
        return ((LimitAngle) node.getCut().getHyperplane()).getLocation().getAlpha();
    }

    public ArcsSet buildNew(BSPTree<Sphere1D> tree) {
        return new ArcsSet(tree, getTolerance());
    }

    /* access modifiers changed from: protected */
    public void computeGeometricalProperties() {
        double d = 6.283185307179586d;
        if (getTree(false).getCut() == null) {
            setBarycenter((Point<S>) S1Point.NaN);
            if (!((Boolean) getTree(false).getAttribute()).booleanValue()) {
                d = 0.0d;
            }
            setSize(d);
            return;
        }
        double size = 0.0d;
        double sum = 0.0d;
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            double[] a = (double[]) i$.next();
            double length = a[1] - a[0];
            size += length;
            sum += (a[0] + a[1]) * length;
        }
        setSize(size);
        if (Precision.equals(size, 6.283185307179586d, 0)) {
            setBarycenter((Point<S>) S1Point.NaN);
        } else if (size >= Precision.SAFE_MIN) {
            setBarycenter((Point<S>) new S1Point<S>(sum / (2.0d * size)));
        } else {
            setBarycenter((Point<S>) ((LimitAngle) getTree(false).getCut().getHyperplane()).getLocation());
        }
    }

    public BoundaryProjection<Sphere1D> projectToBoundary(Point<Sphere1D> point) {
        double alpha;
        double first;
        Point<Sphere1D> point2 = point;
        double alpha2 = ((S1Point) point2).getAlpha();
        boolean wrapFirst = false;
        double first2 = Double.NaN;
        double previous = Double.NaN;
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            double[] a = (double[]) i$.next();
            if (Double.isNaN(first2)) {
                first2 = a[0];
            }
            if (wrapFirst) {
                alpha = alpha2;
                first = first2;
            } else if (alpha2 >= a[0]) {
                first = first2;
                if (alpha2 <= a[1]) {
                    double offset0 = a[0] - alpha2;
                    double offset1 = alpha2 - a[1];
                    if (offset0 < offset1) {
                        double d = alpha2;
                        return new BoundaryProjection<>(point2, new S1Point(a[1]), offset1);
                    }
                    return new BoundaryProjection<>(point2, new S1Point(a[0]), offset0);
                }
                alpha = alpha2;
            } else if (Double.isNaN(previous)) {
                wrapFirst = true;
                alpha = alpha2;
                first = first2;
            } else {
                double previousOffset = alpha2 - previous;
                double currentOffset = a[0] - alpha2;
                if (previousOffset < currentOffset) {
                    return new BoundaryProjection<>(point2, new S1Point(previous), previousOffset);
                }
                double d2 = first2;
                double d3 = previousOffset;
                return new BoundaryProjection<>(point2, new S1Point(a[0]), currentOffset);
            }
            previous = a[1];
            first2 = first;
            alpha2 = alpha;
        }
        double alpha3 = alpha2;
        if (Double.isNaN(previous)) {
            return new BoundaryProjection<>(point2, null, 6.283185307179586d);
        }
        if (wrapFirst) {
            double previousOffset2 = alpha3 - (previous - 6.283185307179586d);
            double currentOffset2 = first2 - alpha3;
            if (previousOffset2 < currentOffset2) {
                return new BoundaryProjection<>(point2, new S1Point(previous), previousOffset2);
            }
            return new BoundaryProjection<>(point2, new S1Point(first2), currentOffset2);
        }
        double previousOffset3 = alpha3 - previous;
        double currentOffset3 = (6.283185307179586d + first2) - alpha3;
        if (previousOffset3 < currentOffset3) {
            return new BoundaryProjection<>(point2, new S1Point(previous), previousOffset3);
        }
        return new BoundaryProjection<>(point2, new S1Point(first2), currentOffset3);
    }

    public List<Arc> asList() {
        List<Arc> list = new ArrayList<>();
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            double[] a = (double[]) i$.next();
            Arc arc = new Arc(a[0], a[1], getTolerance());
            list.add(arc);
        }
        return list;
    }

    public Iterator<double[]> iterator() {
        return new SubArcsIterator();
    }

    @Deprecated
    public Side side(Arc arc) {
        return split(arc).getSide();
    }

    public Split split(Arc arc) {
        double reference;
        List<Double> minus = new ArrayList<>();
        List<Double> plus = new ArrayList<>();
        double reference2 = arc.getInf() + 3.141592653589793d;
        double arcLength = arc.getSup() - arc.getInf();
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            double[] a = (double[]) i$.next();
            double syncedStart = MathUtils.normalizeAngle(a[0], reference2) - arc.getInf();
            double arcOffset = a[0] - syncedStart;
            double syncedEnd = a[1] - arcOffset;
            if (syncedStart < arcLength) {
                double syncedEnd2 = syncedEnd;
                minus.add(Double.valueOf(a[0]));
                if (syncedEnd2 > arcLength) {
                    double minusToPlus = arcLength + arcOffset;
                    minus.add(Double.valueOf(minusToPlus));
                    plus.add(Double.valueOf(minusToPlus));
                    if (syncedEnd2 > 6.283185307179586d) {
                        reference = reference2;
                        double plusToMinus = arcOffset + 6.283185307179586d;
                        plus.add(Double.valueOf(plusToMinus));
                        minus.add(Double.valueOf(plusToMinus));
                        double d = plusToMinus;
                        minus.add(Double.valueOf(a[1]));
                    } else {
                        reference = reference2;
                        plus.add(Double.valueOf(a[1]));
                    }
                } else {
                    reference = reference2;
                    minus.add(Double.valueOf(a[1]));
                }
            } else {
                reference = reference2;
                double syncedEnd3 = syncedEnd;
                plus.add(Double.valueOf(a[0]));
                if (syncedEnd3 > 6.283185307179586d) {
                    double plusToMinus2 = arcOffset + 6.283185307179586d;
                    plus.add(Double.valueOf(plusToMinus2));
                    minus.add(Double.valueOf(plusToMinus2));
                    if (syncedEnd3 > arcLength + 6.283185307179586d) {
                        double minusToPlus2 = arcLength + 6.283185307179586d + arcOffset;
                        minus.add(Double.valueOf(minusToPlus2));
                        plus.add(Double.valueOf(minusToPlus2));
                        double d2 = plusToMinus2;
                        plus.add(Double.valueOf(a[1]));
                    } else {
                        minus.add(Double.valueOf(a[1]));
                    }
                } else {
                    plus.add(Double.valueOf(a[1]));
                }
            }
            reference2 = reference;
        }
        return new Split(createSplitPart(minus));
    }

    private void addArcLimit(BSPTree<Sphere1D> tree, double alpha, boolean isStart) {
        LimitAngle limit = new LimitAngle(new S1Point(alpha), !isStart, getTolerance());
        BSPTree<Sphere1D> node = tree.getCell(limit.getLocation(), getTolerance());
        if (node.getCut() != null) {
            throw new MathInternalError();
        }
        node.insertCut(limit);
        node.setAttribute(null);
        node.getPlus().setAttribute(Boolean.FALSE);
        node.getMinus().setAttribute(Boolean.TRUE);
    }

    private ArcsSet createSplitPart(List<Double> limits) {
        int i;
        List<Double> list = limits;
        if (limits.isEmpty()) {
            return null;
        }
        int i2 = 0;
        while (i2 < limits.size()) {
            int j = (i2 + 1) % limits.size();
            double lA = ((Double) list.get(i2)).doubleValue();
            if (FastMath.abs(MathUtils.normalizeAngle(((Double) list.get(j)).doubleValue(), lA) - lA) > getTolerance()) {
                i = i2;
            } else if (j > 0) {
                list.remove(j);
                list.remove(i2);
                i = i2 - 1;
            } else {
                double lEnd = ((Double) list.remove(limits.size() - 1)).doubleValue();
                double lStart = ((Double) list.remove(0)).doubleValue();
                if (!limits.isEmpty()) {
                    i = i2;
                    int i3 = j;
                    list.add(Double.valueOf(((Double) list.remove(0)).doubleValue() + 6.283185307179586d));
                } else if (lEnd - lStart > 3.141592653589793d) {
                    int i4 = i2;
                    int i5 = j;
                    return new ArcsSet(new BSPTree<>(Boolean.TRUE), getTolerance());
                } else {
                    int i6 = j;
                    return null;
                }
            }
            i2 = i + 1;
        }
        BSPTree<Sphere1D> tree = new BSPTree<>(Boolean.FALSE);
        for (int i7 = 0; i7 < limits.size() - 1; i7 += 2) {
            addArcLimit(tree, ((Double) list.get(i7)).doubleValue(), true);
            addArcLimit(tree, ((Double) list.get(i7 + 1)).doubleValue(), false);
        }
        if (tree.getCut() == null) {
            return null;
        }
        return new ArcsSet(tree, getTolerance());
    }
}

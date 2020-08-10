package org.apache.commons.math3.geometry.partitioning;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.math3.geometry.Space;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane.SplitSubHyperplane;

public abstract class AbstractSubHyperplane<S extends Space, T extends Space> implements SubHyperplane<S> {
    private final Hyperplane<S> hyperplane;
    private final Region<T> remainingRegion;

    /* access modifiers changed from: protected */
    public abstract AbstractSubHyperplane<S, T> buildNew(Hyperplane<S> hyperplane2, Region<T> region);

    public abstract SplitSubHyperplane<S> split(Hyperplane<S> hyperplane2);

    protected AbstractSubHyperplane(Hyperplane<S> hyperplane2, Region<T> remainingRegion2) {
        this.hyperplane = hyperplane2;
        this.remainingRegion = remainingRegion2;
    }

    public AbstractSubHyperplane<S, T> copySelf() {
        return buildNew(this.hyperplane.copySelf(), this.remainingRegion);
    }

    public Hyperplane<S> getHyperplane() {
        return this.hyperplane;
    }

    public Region<T> getRemainingRegion() {
        return this.remainingRegion;
    }

    public double getSize() {
        return this.remainingRegion.getSize();
    }

    public AbstractSubHyperplane<S, T> reunite(SubHyperplane<S> other) {
        return buildNew(this.hyperplane, new RegionFactory().union(this.remainingRegion, ((AbstractSubHyperplane) other).remainingRegion));
    }

    public AbstractSubHyperplane<S, T> applyTransform(Transform<S, T> transform) {
        Hyperplane<S> tHyperplane = transform.apply(this.hyperplane);
        Map<BSPTree<T>, BSPTree<T>> map = new HashMap<>();
        BSPTree<T> tTree = recurseTransform(this.remainingRegion.getTree(false), tHyperplane, transform, map);
        for (Entry<BSPTree<T>, BSPTree<T>> entry : map.entrySet()) {
            if (((BSPTree) entry.getKey()).getCut() != null) {
                BoundaryAttribute<T> original = (BoundaryAttribute) ((BSPTree) entry.getKey()).getAttribute();
                if (original != null) {
                    BoundaryAttribute<T> transformed = (BoundaryAttribute) ((BSPTree) entry.getValue()).getAttribute();
                    Iterator i$ = original.getSplitters().iterator();
                    while (i$.hasNext()) {
                        transformed.getSplitters().add((BSPTree) map.get((BSPTree) i$.next()));
                    }
                }
            }
        }
        return buildNew(tHyperplane, this.remainingRegion.buildNew(tTree));
    }

    private BSPTree<T> recurseTransform(BSPTree<T> node, Hyperplane<S> transformed, Transform<S, T> transform, Map<BSPTree<T>, BSPTree<T>> map) {
        BSPTree<T> transformedNode;
        if (node.getCut() == null) {
            transformedNode = new BSPTree<>(node.getAttribute());
        } else {
            BoundaryAttribute boundaryAttribute = (BoundaryAttribute) node.getAttribute();
            if (boundaryAttribute != null) {
                SubHyperplane<T> tPI = null;
                SubHyperplane apply = boundaryAttribute.getPlusOutside() == null ? null : transform.apply(boundaryAttribute.getPlusOutside(), this.hyperplane, transformed);
                if (boundaryAttribute.getPlusInside() != null) {
                    tPI = transform.apply(boundaryAttribute.getPlusInside(), this.hyperplane, transformed);
                }
                boundaryAttribute = new BoundaryAttribute(apply, tPI, new NodesSet());
            }
            transformedNode = new BSPTree<>(transform.apply(node.getCut(), this.hyperplane, transformed), recurseTransform(node.getPlus(), transformed, transform, map), recurseTransform(node.getMinus(), transformed, transform, map), boundaryAttribute);
        }
        map.put(node, transformedNode);
        return transformedNode;
    }

    @Deprecated
    public Side side(Hyperplane<S> hyper) {
        return split(hyper).getSide();
    }

    public boolean isEmpty() {
        return this.remainingRegion.isEmpty();
    }
}

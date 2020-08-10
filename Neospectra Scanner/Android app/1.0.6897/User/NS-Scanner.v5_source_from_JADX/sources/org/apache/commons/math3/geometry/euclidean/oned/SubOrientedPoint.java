package org.apache.commons.math3.geometry.euclidean.oned;

import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane.SplitSubHyperplane;

public class SubOrientedPoint extends AbstractSubHyperplane<Euclidean1D, Euclidean1D> {
    public SubOrientedPoint(Hyperplane<Euclidean1D> hyperplane, Region<Euclidean1D> remainingRegion) {
        super(hyperplane, remainingRegion);
    }

    public double getSize() {
        return 0.0d;
    }

    public boolean isEmpty() {
        return false;
    }

    /* access modifiers changed from: protected */
    public AbstractSubHyperplane<Euclidean1D, Euclidean1D> buildNew(Hyperplane<Euclidean1D> hyperplane, Region<Euclidean1D> remainingRegion) {
        return new SubOrientedPoint(hyperplane, remainingRegion);
    }

    public SplitSubHyperplane<Euclidean1D> split(Hyperplane<Euclidean1D> hyperplane) {
        double global = hyperplane.getOffset(((OrientedPoint) getHyperplane()).getLocation());
        if (global < -1.0E-10d) {
            return new SplitSubHyperplane<>(null, this);
        }
        if (global > 1.0E-10d) {
            return new SplitSubHyperplane<>(this, null);
        }
        return new SplitSubHyperplane<>(null, null);
    }
}

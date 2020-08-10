package org.apache.commons.math3.geometry.spherical.twod;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane.SplitSubHyperplane;
import org.apache.commons.math3.geometry.spherical.oned.ArcsSet;
import org.apache.commons.math3.geometry.spherical.oned.ArcsSet.Split;
import org.apache.commons.math3.geometry.spherical.oned.Sphere1D;

public class SubCircle extends AbstractSubHyperplane<Sphere2D, Sphere1D> {
    public SubCircle(Hyperplane<Sphere2D> hyperplane, Region<Sphere1D> remainingRegion) {
        super(hyperplane, remainingRegion);
    }

    /* access modifiers changed from: protected */
    public AbstractSubHyperplane<Sphere2D, Sphere1D> buildNew(Hyperplane<Sphere2D> hyperplane, Region<Sphere1D> remainingRegion) {
        return new SubCircle(hyperplane, remainingRegion);
    }

    public SplitSubHyperplane<Sphere2D> split(Hyperplane<Sphere2D> hyperplane) {
        Circle thisCircle = (Circle) getHyperplane();
        Circle otherCircle = (Circle) hyperplane;
        double angle = Vector3D.angle(thisCircle.getPole(), otherCircle.getPole());
        SubCircle subCircle = null;
        if (angle < thisCircle.getTolerance() || angle > 3.141592653589793d - thisCircle.getTolerance()) {
            return new SplitSubHyperplane<>(null, null);
        }
        Split split = ((ArcsSet) getRemainingRegion()).split(thisCircle.getInsideArc(otherCircle));
        ArcsSet plus = split.getPlus();
        ArcsSet minus = split.getMinus();
        SubHyperplane subCircle2 = plus == null ? null : new SubCircle(thisCircle.copySelf(), plus);
        if (minus != null) {
            subCircle = new SubCircle(thisCircle.copySelf(), minus);
        }
        return new SplitSubHyperplane<>(subCircle2, subCircle);
    }
}

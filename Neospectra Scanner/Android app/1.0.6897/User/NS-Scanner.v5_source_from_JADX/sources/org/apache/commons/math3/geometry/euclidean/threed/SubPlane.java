package org.apache.commons.math3.geometry.euclidean.threed;

import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.PolygonsSet;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane.SplitSubHyperplane;

public class SubPlane extends AbstractSubHyperplane<Euclidean3D, Euclidean2D> {
    public SubPlane(Hyperplane<Euclidean3D> hyperplane, Region<Euclidean2D> remainingRegion) {
        super(hyperplane, remainingRegion);
    }

    /* access modifiers changed from: protected */
    public AbstractSubHyperplane<Euclidean3D, Euclidean2D> buildNew(Hyperplane<Euclidean3D> hyperplane, Region<Euclidean2D> remainingRegion) {
        return new SubPlane(hyperplane, remainingRegion);
    }

    public SplitSubHyperplane<Euclidean3D> split(Hyperplane<Euclidean3D> hyperplane) {
        Plane otherPlane = (Plane) hyperplane;
        Plane thisPlane = (Plane) getHyperplane();
        Line inter = otherPlane.intersection(thisPlane);
        double tolerance = thisPlane.getTolerance();
        if (inter == null) {
            double global = otherPlane.getOffset(thisPlane);
            if (global < (-tolerance)) {
                return new SplitSubHyperplane<>(null, this);
            }
            if (global > tolerance) {
                return new SplitSubHyperplane<>(this, null);
            }
            return new SplitSubHyperplane<>(null, null);
        }
        Vector2D p = thisPlane.toSubSpace((Point) inter.toSpace((Point) Vector1D.ZERO));
        Vector2D q = thisPlane.toSubSpace((Point) inter.toSpace((Point) Vector1D.ONE));
        if (Vector3D.crossProduct(inter.getDirection(), thisPlane.getNormal()).dotProduct(otherPlane.getNormal()) < 0.0d) {
            Vector2D tmp = p;
            p = q;
            q = tmp;
        }
        SubHyperplane<Euclidean2D> l2DMinus = new Line(p, q, tolerance).wholeHyperplane();
        SubHyperplane<Euclidean2D> l2DPlus = new Line(q, p, tolerance).wholeHyperplane();
        BSPTree<Euclidean2D> splitTree = getRemainingRegion().getTree(false).split(l2DMinus);
        BSPTree bSPTree = getRemainingRegion().isEmpty(splitTree.getPlus()) ? new BSPTree(Boolean.FALSE) : new BSPTree(l2DPlus, new BSPTree(Boolean.FALSE), splitTree.getPlus(), null);
        Plane plane = otherPlane;
        BSPTree bSPTree2 = bSPTree;
        return new SplitSubHyperplane<>(new SubPlane(thisPlane.copySelf(), new PolygonsSet(bSPTree, tolerance)), new SubPlane(thisPlane.copySelf(), new PolygonsSet(getRemainingRegion().isEmpty(splitTree.getMinus()) ? new BSPTree<>(Boolean.FALSE) : new BSPTree<>(l2DMinus, new BSPTree(Boolean.FALSE), splitTree.getMinus(), null), tolerance)));
    }
}

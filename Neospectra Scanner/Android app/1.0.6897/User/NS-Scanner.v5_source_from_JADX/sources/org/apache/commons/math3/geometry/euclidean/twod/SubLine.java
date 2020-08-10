package org.apache.commons.math3.geometry.euclidean.twod;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.euclidean.oned.OrientedPoint;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Region.Location;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane.SplitSubHyperplane;
import org.apache.commons.math3.util.FastMath;

public class SubLine extends AbstractSubHyperplane<Euclidean2D, Euclidean1D> {
    private static final double DEFAULT_TOLERANCE = 1.0E-10d;

    public SubLine(Hyperplane<Euclidean2D> hyperplane, Region<Euclidean1D> remainingRegion) {
        super(hyperplane, remainingRegion);
    }

    public SubLine(Vector2D start, Vector2D end, double tolerance) {
        super(new Line(start, end, tolerance), buildIntervalSet(start, end, tolerance));
    }

    @Deprecated
    public SubLine(Vector2D start, Vector2D end) {
        this(start, end, 1.0E-10d);
    }

    public SubLine(Segment segment) {
        super(segment.getLine(), buildIntervalSet(segment.getStart(), segment.getEnd(), segment.getLine().getTolerance()));
    }

    public List<Segment> getSegments() {
        Line line = (Line) getHyperplane();
        List<Interval> list = ((IntervalsSet) getRemainingRegion()).asList();
        List<Segment> segments = new ArrayList<>(list.size());
        for (Interval interval : list) {
            segments.add(new Segment(line.toSpace((Point) new Vector1D(interval.getInf())), line.toSpace((Point) new Vector1D(interval.getSup())), line));
        }
        return segments;
    }

    public Vector2D intersection(SubLine subLine, boolean includeEndPoints) {
        Line line1 = (Line) getHyperplane();
        Line line2 = (Line) subLine.getHyperplane();
        Vector2D v2D = line1.intersection(line2);
        Vector2D vector2D = null;
        if (v2D == null) {
            return null;
        }
        Location loc1 = getRemainingRegion().checkPoint(line1.toSubSpace((Point) v2D));
        Location loc2 = subLine.getRemainingRegion().checkPoint(line2.toSubSpace((Point) v2D));
        if (includeEndPoints) {
            if (!(loc1 == Location.OUTSIDE || loc2 == Location.OUTSIDE)) {
                vector2D = v2D;
            }
            return vector2D;
        }
        if (loc1 == Location.INSIDE && loc2 == Location.INSIDE) {
            vector2D = v2D;
        }
        return vector2D;
    }

    private static IntervalsSet buildIntervalSet(Vector2D start, Vector2D end, double tolerance) {
        Line line = new Line(start, end, tolerance);
        IntervalsSet intervalsSet = new IntervalsSet(line.toSubSpace((Point) start).getX(), line.toSubSpace((Point) end).getX(), tolerance);
        return intervalsSet;
    }

    /* access modifiers changed from: protected */
    public AbstractSubHyperplane<Euclidean2D, Euclidean1D> buildNew(Hyperplane<Euclidean2D> hyperplane, Region<Euclidean1D> remainingRegion) {
        return new SubLine(hyperplane, remainingRegion);
    }

    public SplitSubHyperplane<Euclidean2D> split(Hyperplane<Euclidean2D> hyperplane) {
        Line thisLine = (Line) getHyperplane();
        Line otherLine = (Line) hyperplane;
        Vector2D crossing = thisLine.intersection(otherLine);
        double tolerance = thisLine.getTolerance();
        if (crossing == null) {
            double global = otherLine.getOffset(thisLine);
            if (global < (-tolerance)) {
                return new SplitSubHyperplane<>(null, this);
            }
            if (global > tolerance) {
                return new SplitSubHyperplane<>(this, null);
            }
            return new SplitSubHyperplane<>(null, null);
        }
        boolean z = true;
        boolean direct = FastMath.sin(thisLine.getAngle() - otherLine.getAngle()) < 0.0d;
        Vector1D x = thisLine.toSubSpace((Point) crossing);
        if (direct) {
            z = false;
        }
        SubHyperplane<Euclidean1D> subPlus = new OrientedPoint(x, z, tolerance).wholeHyperplane();
        SubHyperplane<Euclidean1D> subMinus = new OrientedPoint(x, direct, tolerance).wholeHyperplane();
        BSPTree<Euclidean1D> splitTree = getRemainingRegion().getTree(false).split(subMinus);
        Line line = thisLine;
        return new SplitSubHyperplane<>(new SubLine((Hyperplane<Euclidean2D>) thisLine.copySelf(), (Region<Euclidean1D>) new IntervalsSet<Euclidean1D>(getRemainingRegion().isEmpty(splitTree.getPlus()) ? new BSPTree<>(Boolean.FALSE) : new BSPTree<>(subPlus, new BSPTree(Boolean.FALSE), splitTree.getPlus(), null), tolerance)), new SubLine((Hyperplane<Euclidean2D>) thisLine.copySelf(), (Region<Euclidean1D>) new IntervalsSet<Euclidean1D>(getRemainingRegion().isEmpty(splitTree.getMinus()) ? new BSPTree<>(Boolean.FALSE) : new BSPTree<>(subMinus, new BSPTree(Boolean.FALSE), splitTree.getMinus(), null), tolerance)));
    }
}

package org.apache.commons.math3.geometry.euclidean.threed;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.partitioning.Region.Location;

public class SubLine {
    private static final double DEFAULT_TOLERANCE = 1.0E-10d;
    private final Line line;
    private final IntervalsSet remainingRegion;

    public SubLine(Line line2, IntervalsSet remainingRegion2) {
        this.line = line2;
        this.remainingRegion = remainingRegion2;
    }

    public SubLine(Vector3D start, Vector3D end, double tolerance) throws MathIllegalArgumentException {
        this(new Line(start, end, tolerance), buildIntervalSet(start, end, tolerance));
    }

    public SubLine(Vector3D start, Vector3D end) throws MathIllegalArgumentException {
        this(start, end, 1.0E-10d);
    }

    public SubLine(Segment segment) throws MathIllegalArgumentException {
        this(segment.getLine(), buildIntervalSet(segment.getStart(), segment.getEnd(), segment.getLine().getTolerance()));
    }

    public List<Segment> getSegments() {
        List<Interval> list = this.remainingRegion.asList();
        List<Segment> segments = new ArrayList<>(list.size());
        for (Interval interval : list) {
            segments.add(new Segment(this.line.toSpace((Point) new Vector1D(interval.getInf())), this.line.toSpace((Point) new Vector1D(interval.getSup())), this.line));
        }
        return segments;
    }

    public Vector3D intersection(SubLine subLine, boolean includeEndPoints) {
        Vector3D v1D = this.line.intersection(subLine.line);
        Vector3D vector3D = null;
        if (v1D == null) {
            return null;
        }
        Location loc1 = this.remainingRegion.checkPoint((Point<S>) this.line.toSubSpace((Point) v1D));
        Location loc2 = subLine.remainingRegion.checkPoint((Point<S>) subLine.line.toSubSpace((Point) v1D));
        if (includeEndPoints) {
            if (!(loc1 == Location.OUTSIDE || loc2 == Location.OUTSIDE)) {
                vector3D = v1D;
            }
            return vector3D;
        }
        if (loc1 == Location.INSIDE && loc2 == Location.INSIDE) {
            vector3D = v1D;
        }
        return vector3D;
    }

    private static IntervalsSet buildIntervalSet(Vector3D start, Vector3D end, double tolerance) throws MathIllegalArgumentException {
        Line line2 = new Line(start, end, tolerance);
        IntervalsSet intervalsSet = new IntervalsSet(line2.toSubSpace((Point) start).getX(), line2.toSubSpace((Point) end).getX(), tolerance);
        return intervalsSet;
    }
}

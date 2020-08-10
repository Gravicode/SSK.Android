package org.apache.commons.math3.geometry.euclidean.twod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.RegionFactory;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;

class NestedLoops {
    private Vector2D[] loop;
    private boolean originalIsClockwise;
    private Region<Euclidean2D> polygon;
    private List<NestedLoops> surrounded;
    private final double tolerance;

    NestedLoops(double tolerance2) {
        this.surrounded = new ArrayList();
        this.tolerance = tolerance2;
    }

    private NestedLoops(Vector2D[] loop2, double tolerance2) throws MathIllegalArgumentException {
        Vector2D[] vector2DArr = loop2;
        double d = tolerance2;
        if (vector2DArr[0] == null) {
            throw new MathIllegalArgumentException(LocalizedFormats.OUTLINE_BOUNDARY_LOOP_OPEN, new Object[0]);
        }
        this.loop = vector2DArr;
        this.surrounded = new ArrayList();
        this.tolerance = d;
        ArrayList arrayList = new ArrayList();
        Vector2D current = vector2DArr[vector2DArr.length - 1];
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= vector2DArr.length) {
                break;
            }
            Vector2D previous = current;
            Vector2D current2 = vector2DArr[i2];
            Line line = new Line(previous, current2, d);
            Vector2D current3 = current2;
            Line line2 = line;
            IntervalsSet region = new IntervalsSet(line.toSubSpace((Point) previous).getX(), line.toSubSpace((Point) current2).getX(), d);
            arrayList.add(new SubLine((Hyperplane<Euclidean2D>) line2, (Region<Euclidean1D>) region));
            i = i2 + 1;
            current = current3;
        }
        this.polygon = new PolygonsSet((Collection<SubHyperplane<Euclidean2D>>) arrayList, d);
        if (Double.isInfinite(this.polygon.getSize())) {
            this.polygon = new RegionFactory().getComplement(this.polygon);
            this.originalIsClockwise = false;
            return;
        }
        this.originalIsClockwise = true;
    }

    public void add(Vector2D[] bLoop) throws MathIllegalArgumentException {
        add(new NestedLoops(bLoop, this.tolerance));
    }

    private void add(NestedLoops node) throws MathIllegalArgumentException {
        for (NestedLoops child : this.surrounded) {
            if (child.polygon.contains(node.polygon)) {
                child.add(node);
                return;
            }
        }
        Iterator<NestedLoops> iterator = this.surrounded.iterator();
        while (iterator.hasNext()) {
            NestedLoops child2 = (NestedLoops) iterator.next();
            if (node.polygon.contains(child2.polygon)) {
                node.surrounded.add(child2);
                iterator.remove();
            }
        }
        RegionFactory<Euclidean2D> factory = new RegionFactory<>();
        for (NestedLoops child3 : this.surrounded) {
            if (!factory.intersection(node.polygon, child3.polygon).isEmpty()) {
                throw new MathIllegalArgumentException(LocalizedFormats.CROSSING_BOUNDARY_LOOPS, new Object[0]);
            }
        }
        this.surrounded.add(node);
    }

    public void correctOrientation() {
        for (NestedLoops child : this.surrounded) {
            child.setClockWise(true);
        }
    }

    private void setClockWise(boolean clockwise) {
        if (this.originalIsClockwise ^ clockwise) {
            int min = -1;
            int max = this.loop.length;
            while (true) {
                min++;
                max--;
                if (min >= max) {
                    break;
                }
                Vector2D tmp = this.loop[min];
                this.loop[min] = this.loop[max];
                this.loop[max] = tmp;
            }
        }
        for (NestedLoops child : this.surrounded) {
            child.setClockWise(!clockwise);
        }
    }
}

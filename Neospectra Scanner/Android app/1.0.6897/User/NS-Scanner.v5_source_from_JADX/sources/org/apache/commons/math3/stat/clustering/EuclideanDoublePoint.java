package org.apache.commons.math3.stat.clustering;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.math3.util.MathArrays;

@Deprecated
public class EuclideanDoublePoint implements Clusterable<EuclideanDoublePoint>, Serializable {
    private static final long serialVersionUID = 8026472786091227632L;
    private final double[] point;

    public EuclideanDoublePoint(double[] point2) {
        this.point = point2;
    }

    public EuclideanDoublePoint centroidOf(Collection<EuclideanDoublePoint> points) {
        int i;
        double[] centroid = new double[getPoint().length];
        Iterator i$ = points.iterator();
        while (true) {
            i = 0;
            if (!i$.hasNext()) {
                break;
            }
            EuclideanDoublePoint p = (EuclideanDoublePoint) i$.next();
            while (i < centroid.length) {
                centroid[i] = centroid[i] + p.getPoint()[i];
                i++;
            }
        }
        while (true) {
            int i2 = i;
            if (i2 >= centroid.length) {
                return new EuclideanDoublePoint(centroid);
            }
            centroid[i2] = centroid[i2] / ((double) points.size());
            i = i2 + 1;
        }
    }

    public double distanceFrom(EuclideanDoublePoint p) {
        return MathArrays.distance(this.point, p.getPoint());
    }

    public boolean equals(Object other) {
        if (!(other instanceof EuclideanDoublePoint)) {
            return false;
        }
        return Arrays.equals(this.point, ((EuclideanDoublePoint) other).point);
    }

    public double[] getPoint() {
        return this.point;
    }

    public int hashCode() {
        return Arrays.hashCode(this.point);
    }

    public String toString() {
        return Arrays.toString(this.point);
    }
}

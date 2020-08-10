package org.apache.commons.math3.stat.clustering;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.math3.util.MathArrays;

@Deprecated
public class EuclideanIntegerPoint implements Clusterable<EuclideanIntegerPoint>, Serializable {
    private static final long serialVersionUID = 3946024775784901369L;
    private final int[] point;

    public EuclideanIntegerPoint(int[] point2) {
        this.point = point2;
    }

    public int[] getPoint() {
        return this.point;
    }

    public double distanceFrom(EuclideanIntegerPoint p) {
        return MathArrays.distance(this.point, p.getPoint());
    }

    public EuclideanIntegerPoint centroidOf(Collection<EuclideanIntegerPoint> points) {
        int i;
        int[] centroid = new int[getPoint().length];
        Iterator i$ = points.iterator();
        while (true) {
            i = 0;
            if (!i$.hasNext()) {
                break;
            }
            EuclideanIntegerPoint p = (EuclideanIntegerPoint) i$.next();
            while (i < centroid.length) {
                centroid[i] = centroid[i] + p.getPoint()[i];
                i++;
            }
        }
        while (true) {
            int i2 = i;
            if (i2 >= centroid.length) {
                return new EuclideanIntegerPoint(centroid);
            }
            centroid[i2] = centroid[i2] / points.size();
            i = i2 + 1;
        }
    }

    public boolean equals(Object other) {
        if (!(other instanceof EuclideanIntegerPoint)) {
            return false;
        }
        return Arrays.equals(this.point, ((EuclideanIntegerPoint) other).point);
    }

    public int hashCode() {
        return Arrays.hashCode(this.point);
    }

    public String toString() {
        return Arrays.toString(this.point);
    }
}

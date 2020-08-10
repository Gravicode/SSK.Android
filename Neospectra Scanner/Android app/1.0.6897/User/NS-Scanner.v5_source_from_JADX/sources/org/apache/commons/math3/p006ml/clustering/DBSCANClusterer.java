package org.apache.commons.math3.p006ml.clustering;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.p006ml.clustering.Clusterable;
import org.apache.commons.math3.p006ml.distance.DistanceMeasure;
import org.apache.commons.math3.p006ml.distance.EuclideanDistance;
import org.apache.commons.math3.util.MathUtils;

/* renamed from: org.apache.commons.math3.ml.clustering.DBSCANClusterer */
public class DBSCANClusterer<T extends Clusterable> extends Clusterer<T> {
    private final double eps;
    private final int minPts;

    /* renamed from: org.apache.commons.math3.ml.clustering.DBSCANClusterer$PointStatus */
    private enum PointStatus {
        NOISE,
        PART_OF_CLUSTER
    }

    public DBSCANClusterer(double eps2, int minPts2) throws NotPositiveException {
        this(eps2, minPts2, new EuclideanDistance());
    }

    public DBSCANClusterer(double eps2, int minPts2, DistanceMeasure measure) throws NotPositiveException {
        super(measure);
        if (eps2 < 0.0d) {
            throw new NotPositiveException(Double.valueOf(eps2));
        } else if (minPts2 < 0) {
            throw new NotPositiveException(Integer.valueOf(minPts2));
        } else {
            this.eps = eps2;
            this.minPts = minPts2;
        }
    }

    public double getEps() {
        return this.eps;
    }

    public int getMinPts() {
        return this.minPts;
    }

    public List<Cluster<T>> cluster(Collection<T> points) throws NullArgumentException {
        MathUtils.checkNotNull(points);
        List<Cluster<T>> clusters = new ArrayList<>();
        HashMap hashMap = new HashMap();
        Iterator i$ = points.iterator();
        while (true) {
            Iterator i$2 = i$;
            if (!i$2.hasNext()) {
                return clusters;
            }
            T point = (Clusterable) i$2.next();
            if (hashMap.get(point) == null) {
                List<T> neighbors = getNeighbors(point, points);
                if (neighbors.size() >= this.minPts) {
                    clusters.add(expandCluster(new Cluster<>(), point, neighbors, points, hashMap));
                } else {
                    hashMap.put(point, PointStatus.NOISE);
                }
            }
            i$ = i$2;
        }
    }

    private Cluster<T> expandCluster(Cluster<T> cluster, T point, List<T> neighbors, Collection<T> points, Map<Clusterable, PointStatus> visited) {
        cluster.addPoint(point);
        visited.put(point, PointStatus.PART_OF_CLUSTER);
        List<T> seeds = new ArrayList<>(neighbors);
        for (int index = 0; index < seeds.size(); index++) {
            T current = (Clusterable) seeds.get(index);
            PointStatus pStatus = (PointStatus) visited.get(current);
            if (pStatus == null) {
                List<T> currentNeighbors = getNeighbors(current, points);
                if (currentNeighbors.size() >= this.minPts) {
                    seeds = merge(seeds, currentNeighbors);
                }
            }
            if (pStatus != PointStatus.PART_OF_CLUSTER) {
                visited.put(current, PointStatus.PART_OF_CLUSTER);
                cluster.addPoint(current);
            }
        }
        return cluster;
    }

    private List<T> getNeighbors(T point, Collection<T> points) {
        List<T> neighbors = new ArrayList<>();
        for (T neighbor : points) {
            if (point != neighbor && distance(neighbor, point) <= this.eps) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    private List<T> merge(List<T> one, List<T> two) {
        Set<T> oneSet = new HashSet<>(one);
        for (T item : two) {
            if (!oneSet.contains(item)) {
                one.add(item);
            }
        }
        return one;
    }
}

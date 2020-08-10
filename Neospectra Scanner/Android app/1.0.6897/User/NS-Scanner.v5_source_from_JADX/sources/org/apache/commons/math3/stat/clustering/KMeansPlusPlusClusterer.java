package org.apache.commons.math3.stat.clustering;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.util.MathUtils;

@Deprecated
public class KMeansPlusPlusClusterer<T extends Clusterable<T>> {
    private final EmptyClusterStrategy emptyStrategy;
    private final Random random;

    public enum EmptyClusterStrategy {
        LARGEST_VARIANCE,
        LARGEST_POINTS_NUMBER,
        FARTHEST_POINT,
        ERROR
    }

    public KMeansPlusPlusClusterer(Random random2) {
        this(random2, EmptyClusterStrategy.LARGEST_VARIANCE);
    }

    public KMeansPlusPlusClusterer(Random random2, EmptyClusterStrategy emptyStrategy2) {
        this.random = random2;
        this.emptyStrategy = emptyStrategy2;
    }

    public List<Cluster<T>> cluster(Collection<T> points, int k, int numTrials, int maxIterationsPerTrial) throws MathIllegalArgumentException, ConvergenceException {
        List<Cluster<T>> best = null;
        double bestVarianceSum = Double.POSITIVE_INFINITY;
        for (int i = 0; i < numTrials; i++) {
            List<Cluster<T>> clusters = cluster(points, k, maxIterationsPerTrial);
            double varianceSum = 0.0d;
            for (Cluster<T> cluster : clusters) {
                if (!cluster.getPoints().isEmpty()) {
                    T center = cluster.getCenter();
                    Variance stat = new Variance();
                    Iterator i$ = cluster.getPoints().iterator();
                    while (i$.hasNext()) {
                        Iterator i$2 = i$;
                        stat.increment(((Clusterable) i$.next()).distanceFrom(center));
                        i$ = i$2;
                        Collection<T> collection = points;
                    }
                    varianceSum += stat.getResult();
                }
                int i2 = numTrials;
                Collection<T> collection2 = points;
            }
            if (varianceSum <= bestVarianceSum) {
                best = clusters;
                bestVarianceSum = varianceSum;
            }
        }
        int i3 = k;
        int i4 = maxIterationsPerTrial;
        return best;
    }

    public List<Cluster<T>> cluster(Collection<T> points, int k, int maxIterations) throws MathIllegalArgumentException, ConvergenceException {
        T newCenter;
        MathUtils.checkNotNull(points);
        if (points.size() < k) {
            throw new NumberIsTooSmallException(Integer.valueOf(points.size()), Integer.valueOf(k), false);
        }
        List<Cluster<T>> clusters = chooseInitialCenters(points, k, this.random);
        int[] assignments = new int[points.size()];
        assignPointsToClusters(clusters, points, assignments);
        int max = maxIterations < 0 ? Integer.MAX_VALUE : maxIterations;
        List<Cluster<T>> clusters2 = clusters;
        for (int count = 0; count < max; count++) {
            boolean emptyCluster = false;
            List<Cluster<T>> newClusters = new ArrayList<>();
            for (Cluster<T> cluster : clusters2) {
                if (cluster.getPoints().isEmpty()) {
                    switch (this.emptyStrategy) {
                        case LARGEST_VARIANCE:
                            newCenter = getPointFromLargestVarianceCluster(clusters2);
                            break;
                        case LARGEST_POINTS_NUMBER:
                            newCenter = getPointFromLargestNumberCluster(clusters2);
                            break;
                        case FARTHEST_POINT:
                            newCenter = getFarthestPoint(clusters2);
                            break;
                        default:
                            throw new ConvergenceException(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS, new Object[0]);
                    }
                    emptyCluster = true;
                } else {
                    newCenter = (Clusterable) cluster.getCenter().centroidOf(cluster.getPoints());
                }
                newClusters.add(new Cluster(newCenter));
            }
            clusters2 = newClusters;
            if (assignPointsToClusters(newClusters, points, assignments) == 0 && !emptyCluster) {
                return clusters2;
            }
        }
        return clusters2;
    }

    private static <T extends Clusterable<T>> int assignPointsToClusters(List<Cluster<T>> clusters, Collection<T> points, int[] assignments) {
        int assignedDifferently = 0;
        int pointIndex = 0;
        for (T p : points) {
            int clusterIndex = getNearestCluster(clusters, p);
            if (clusterIndex != assignments[pointIndex]) {
                assignedDifferently++;
            }
            ((Cluster) clusters.get(clusterIndex)).addPoint(p);
            int pointIndex2 = pointIndex + 1;
            assignments[pointIndex] = clusterIndex;
            pointIndex = pointIndex2;
        }
        return assignedDifferently;
    }

    private static <T extends Clusterable<T>> List<Cluster<T>> chooseInitialCenters(Collection<T> points, int k, Random random2) {
        int i = k;
        List<T> pointList = Collections.unmodifiableList(new ArrayList(points));
        int numPoints = pointList.size();
        boolean[] taken = new boolean[numPoints];
        List<Cluster<T>> resultSet = new ArrayList<>();
        int firstPointIndex = random2.nextInt(numPoints);
        T firstPoint = (Clusterable) pointList.get(firstPointIndex);
        resultSet.add(new Cluster(firstPoint));
        taken[firstPointIndex] = true;
        double[] minDistSquared = new double[numPoints];
        for (int i2 = 0; i2 < numPoints; i2++) {
            if (i2 != firstPointIndex) {
                double d = firstPoint.distanceFrom(pointList.get(i2));
                minDistSquared[i2] = d * d;
            }
        }
        while (resultSet.size() < i) {
            double distSqSum = 0.0d;
            for (int i3 = 0; i3 < numPoints; i3++) {
                if (!taken[i3]) {
                    distSqSum += minDistSquared[i3];
                }
            }
            double r = random2.nextDouble() * distSqSum;
            int nextPointIndex = -1;
            double sum = 0.0d;
            int i4 = 0;
            while (true) {
                int i5 = i4;
                if (i5 >= numPoints) {
                    break;
                }
                if (!taken[i5]) {
                    sum += minDistSquared[i5];
                    if (sum >= r) {
                        nextPointIndex = i5;
                        break;
                    }
                }
                i4 = i5 + 1;
            }
            if (nextPointIndex == -1) {
                int i6 = numPoints - 1;
                while (true) {
                    if (i6 < 0) {
                        break;
                    } else if (!taken[i6]) {
                        nextPointIndex = i6;
                        break;
                    } else {
                        i6--;
                    }
                }
            }
            if (nextPointIndex < 0) {
                break;
            }
            T p = (Clusterable) pointList.get(nextPointIndex);
            resultSet.add(new Cluster(p));
            taken[nextPointIndex] = true;
            if (resultSet.size() < i) {
                int j = 0;
                while (j < numPoints) {
                    if (!taken[j]) {
                        double d2 = p.distanceFrom(pointList.get(j));
                        double d22 = d2 * d2;
                        if (d22 < minDistSquared[j]) {
                            minDistSquared[j] = d22;
                        }
                    }
                    j++;
                    int i7 = k;
                }
            }
            i = k;
        }
        return resultSet;
    }

    private T getPointFromLargestVarianceCluster(Collection<Cluster<T>> clusters) throws ConvergenceException {
        double maxVariance = Double.NEGATIVE_INFINITY;
        Cluster<T> selected = null;
        for (Cluster<T> cluster : clusters) {
            if (!cluster.getPoints().isEmpty()) {
                T center = cluster.getCenter();
                Variance stat = new Variance();
                for (T point : cluster.getPoints()) {
                    stat.increment(point.distanceFrom(center));
                }
                double variance = stat.getResult();
                if (variance > maxVariance) {
                    maxVariance = variance;
                    selected = cluster;
                }
            }
        }
        if (selected == null) {
            throw new ConvergenceException(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS, new Object[0]);
        }
        List<T> selectedPoints = selected.getPoints();
        return (Clusterable) selectedPoints.remove(this.random.nextInt(selectedPoints.size()));
    }

    private T getPointFromLargestNumberCluster(Collection<Cluster<T>> clusters) throws ConvergenceException {
        int maxNumber = 0;
        Cluster<T> selected = null;
        for (Cluster<T> cluster : clusters) {
            int number = cluster.getPoints().size();
            if (number > maxNumber) {
                maxNumber = number;
                selected = cluster;
            }
        }
        if (selected == null) {
            throw new ConvergenceException(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS, new Object[0]);
        }
        List<T> selectedPoints = selected.getPoints();
        return (Clusterable) selectedPoints.remove(this.random.nextInt(selectedPoints.size()));
    }

    private T getFarthestPoint(Collection<Cluster<T>> clusters) throws ConvergenceException {
        double maxDistance = Double.NEGATIVE_INFINITY;
        Cluster<T> selectedCluster = null;
        int selectedPoint = -1;
        Iterator i$ = clusters.iterator();
        while (true) {
            if (!i$.hasNext()) {
                break;
            }
            Cluster<T> cluster = (Cluster) i$.next();
            T center = cluster.getCenter();
            List<T> points = cluster.getPoints();
            for (int i = 0; i < points.size(); i++) {
                double distance = ((Clusterable) points.get(i)).distanceFrom(center);
                if (distance > maxDistance) {
                    maxDistance = distance;
                    selectedCluster = cluster;
                    selectedPoint = i;
                }
            }
        }
        if (selectedCluster != null) {
            return (Clusterable) selectedCluster.getPoints().remove(selectedPoint);
        }
        throw new ConvergenceException(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS, new Object[0]);
    }

    private static <T extends Clusterable<T>> int getNearestCluster(Collection<Cluster<T>> clusters, T point) {
        double minDistance = Double.MAX_VALUE;
        int clusterIndex = 0;
        int minCluster = 0;
        for (Cluster<T> c : clusters) {
            double distance = point.distanceFrom(c.getCenter());
            if (distance < minDistance) {
                minDistance = distance;
                minCluster = clusterIndex;
            }
            clusterIndex++;
        }
        return minCluster;
    }
}

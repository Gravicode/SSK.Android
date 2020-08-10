package org.apache.commons.math3.p006ml.neuralnet.twod.util;

import java.lang.reflect.Array;
import org.apache.commons.math3.p006ml.distance.DistanceMeasure;
import org.apache.commons.math3.p006ml.neuralnet.MapUtils;
import org.apache.commons.math3.p006ml.neuralnet.twod.NeuronSquareMesh2D;
import org.apache.commons.math3.p006ml.neuralnet.twod.util.LocationFinder.Location;

/* renamed from: org.apache.commons.math3.ml.neuralnet.twod.util.HitHistogram */
public class HitHistogram implements MapDataVisualization {
    private final DistanceMeasure distance;
    private final boolean normalizeCount;

    public HitHistogram(boolean normalizeCount2, DistanceMeasure distance2) {
        this.normalizeCount = normalizeCount2;
        this.distance = distance2;
    }

    public double[][] computeImage(NeuronSquareMesh2D map, Iterable<double[]> data) {
        NeuronSquareMesh2D neuronSquareMesh2D = map;
        int nR = map.getNumberOfRows();
        int nC = map.getNumberOfColumns();
        LocationFinder finder = new LocationFinder(neuronSquareMesh2D);
        int numSamples = 0;
        double[][] hit = (double[][]) Array.newInstance(double.class, new int[]{nR, nC});
        for (double[] sample : data) {
            Location loc = finder.getLocation(MapUtils.findBest(sample, neuronSquareMesh2D, this.distance));
            int row = loc.getRow();
            int col = loc.getColumn();
            double[] dArr = hit[row];
            dArr[col] = dArr[col] + 1.0d;
            numSamples++;
        }
        if (this.normalizeCount) {
            for (int r = 0; r < nR; r++) {
                for (int c = 0; c < nC; c++) {
                    double[] dArr2 = hit[r];
                    dArr2[c] = dArr2[c] / ((double) numSamples);
                }
            }
        }
        return hit;
    }
}

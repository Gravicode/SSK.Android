package org.apache.commons.math3.p006ml.neuralnet.twod.util;

import java.lang.reflect.Array;
import org.apache.commons.math3.p006ml.distance.DistanceMeasure;
import org.apache.commons.math3.p006ml.neuralnet.MapUtils;
import org.apache.commons.math3.p006ml.neuralnet.Network;
import org.apache.commons.math3.p006ml.neuralnet.Neuron;
import org.apache.commons.math3.p006ml.neuralnet.twod.NeuronSquareMesh2D;
import org.apache.commons.math3.p006ml.neuralnet.twod.util.LocationFinder.Location;
import org.apache.commons.math3.util.Pair;

/* renamed from: org.apache.commons.math3.ml.neuralnet.twod.util.TopographicErrorHistogram */
public class TopographicErrorHistogram implements MapDataVisualization {
    private final DistanceMeasure distance;
    private final boolean relativeCount;

    public TopographicErrorHistogram(boolean relativeCount2, DistanceMeasure distance2) {
        this.relativeCount = relativeCount2;
        this.distance = distance2;
    }

    public double[][] computeImage(NeuronSquareMesh2D map, Iterable<double[]> data) {
        NeuronSquareMesh2D neuronSquareMesh2D = map;
        int nR = map.getNumberOfRows();
        int nC = map.getNumberOfColumns();
        Network net = map.getNetwork();
        LocationFinder finder = new LocationFinder(neuronSquareMesh2D);
        int[][] hit = (int[][]) Array.newInstance(int.class, new int[]{nR, nC});
        double[][] error = (double[][]) Array.newInstance(double.class, new int[]{nR, nC});
        for (double[] sample : data) {
            Pair<Neuron, Neuron> p = MapUtils.findBestAndSecondBest(sample, neuronSquareMesh2D, this.distance);
            Neuron best = (Neuron) p.getFirst();
            Location loc = finder.getLocation(best);
            int row = loc.getRow();
            int col = loc.getColumn();
            int[] iArr = hit[row];
            iArr[col] = iArr[col] + 1;
            if (!net.getNeighbours(best).contains(p.getSecond())) {
                double[] dArr = error[row];
                dArr[col] = dArr[col] + 1.0d;
            }
            neuronSquareMesh2D = map;
        }
        if (this.relativeCount) {
            for (int r = 0; r < nR; r++) {
                for (int c = 0; c < nC; c++) {
                    double[] dArr2 = error[r];
                    dArr2[c] = dArr2[c] / ((double) hit[r][c]);
                }
            }
        }
        return error;
    }
}

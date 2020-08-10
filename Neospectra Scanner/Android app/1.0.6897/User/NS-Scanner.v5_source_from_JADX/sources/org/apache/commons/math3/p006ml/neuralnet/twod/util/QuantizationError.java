package org.apache.commons.math3.p006ml.neuralnet.twod.util;

import java.lang.reflect.Array;
import org.apache.commons.math3.p006ml.distance.DistanceMeasure;
import org.apache.commons.math3.p006ml.neuralnet.MapUtils;
import org.apache.commons.math3.p006ml.neuralnet.Neuron;
import org.apache.commons.math3.p006ml.neuralnet.twod.NeuronSquareMesh2D;
import org.apache.commons.math3.p006ml.neuralnet.twod.util.LocationFinder.Location;

/* renamed from: org.apache.commons.math3.ml.neuralnet.twod.util.QuantizationError */
public class QuantizationError implements MapDataVisualization {
    private final DistanceMeasure distance;

    public QuantizationError(DistanceMeasure distance2) {
        this.distance = distance2;
    }

    public double[][] computeImage(NeuronSquareMesh2D map, Iterable<double[]> data) {
        QuantizationError quantizationError = this;
        NeuronSquareMesh2D neuronSquareMesh2D = map;
        int nR = map.getNumberOfRows();
        int nC = map.getNumberOfColumns();
        LocationFinder finder = new LocationFinder(neuronSquareMesh2D);
        int[][] hit = (int[][]) Array.newInstance(int.class, new int[]{nR, nC});
        double[][] error = (double[][]) Array.newInstance(double.class, new int[]{nR, nC});
        for (double[] sample : data) {
            Neuron best = MapUtils.findBest(sample, neuronSquareMesh2D, quantizationError.distance);
            Location loc = finder.getLocation(best);
            int row = loc.getRow();
            int col = loc.getColumn();
            int[] iArr = hit[row];
            iArr[col] = iArr[col] + 1;
            double[] dArr = error[row];
            dArr[col] = dArr[col] + quantizationError.distance.compute(sample, best.getFeatures());
            quantizationError = this;
            neuronSquareMesh2D = map;
        }
        for (int r = 0; r < nR; r++) {
            for (int c = 0; c < nC; c++) {
                int count = hit[r][c];
                if (count != 0) {
                    double[] dArr2 = error[r];
                    dArr2[c] = dArr2[c] / ((double) count);
                }
            }
        }
        return error;
    }
}

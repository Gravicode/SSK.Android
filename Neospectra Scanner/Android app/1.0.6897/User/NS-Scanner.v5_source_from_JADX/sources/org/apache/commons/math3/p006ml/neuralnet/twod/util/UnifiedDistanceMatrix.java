package org.apache.commons.math3.p006ml.neuralnet.twod.util;

import java.lang.reflect.Array;
import java.util.Collection;
import org.apache.commons.math3.p006ml.distance.DistanceMeasure;
import org.apache.commons.math3.p006ml.neuralnet.Network;
import org.apache.commons.math3.p006ml.neuralnet.Neuron;
import org.apache.commons.math3.p006ml.neuralnet.twod.NeuronSquareMesh2D;
import org.apache.commons.math3.p006ml.neuralnet.twod.NeuronSquareMesh2D.HorizontalDirection;
import org.apache.commons.math3.p006ml.neuralnet.twod.NeuronSquareMesh2D.VerticalDirection;

/* renamed from: org.apache.commons.math3.ml.neuralnet.twod.util.UnifiedDistanceMatrix */
public class UnifiedDistanceMatrix implements MapVisualization {
    private final DistanceMeasure distance;
    private final boolean individualDistances;

    public UnifiedDistanceMatrix(boolean individualDistances2, DistanceMeasure distance2) {
        this.individualDistances = individualDistances2;
        this.distance = distance2;
    }

    public double[][] computeImage(NeuronSquareMesh2D map) {
        if (this.individualDistances) {
            return individualDistances(map);
        }
        return averageDistances(map);
    }

    private double[][] individualDistances(NeuronSquareMesh2D map) {
        UnifiedDistanceMatrix unifiedDistanceMatrix = this;
        NeuronSquareMesh2D neuronSquareMesh2D = map;
        int numRows = map.getNumberOfRows();
        int numCols = map.getNumberOfColumns();
        double[][] uMatrix = (double[][]) Array.newInstance(double.class, new int[]{(numRows * 2) + 1, (numCols * 2) + 1});
        for (int i = 0; i < numRows; i++) {
            int iR = (i * 2) + 1;
            for (int j = 0; j < numCols; j++) {
                int jR = (j * 2) + 1;
                double[] current = neuronSquareMesh2D.getNeuron(i, j).getFeatures();
                Neuron neighbour = neuronSquareMesh2D.getNeuron(i, j, HorizontalDirection.RIGHT, VerticalDirection.CENTER);
                if (neighbour != null) {
                    uMatrix[iR][jR + 1] = unifiedDistanceMatrix.distance.compute(current, neighbour.getFeatures());
                }
                Neuron neighbour2 = neuronSquareMesh2D.getNeuron(i, j, HorizontalDirection.CENTER, VerticalDirection.DOWN);
                if (neighbour2 != null) {
                    uMatrix[iR + 1][jR] = unifiedDistanceMatrix.distance.compute(current, neighbour2.getFeatures());
                }
            }
        }
        int i2 = 0;
        while (i2 < numRows) {
            int iR2 = (i2 * 2) + 1;
            int j2 = 0;
            while (j2 < numCols) {
                int jR2 = (j2 * 2) + 1;
                Neuron current2 = neuronSquareMesh2D.getNeuron(i2, j2);
                Neuron right = neuronSquareMesh2D.getNeuron(i2, j2, HorizontalDirection.RIGHT, VerticalDirection.CENTER);
                Neuron bottom = neuronSquareMesh2D.getNeuron(i2, j2, HorizontalDirection.CENTER, VerticalDirection.DOWN);
                Neuron bottomRight = neuronSquareMesh2D.getNeuron(i2, j2, HorizontalDirection.RIGHT, VerticalDirection.DOWN);
                uMatrix[iR2 + 1][jR2 + 1] = ((bottomRight == null ? 0.0d : unifiedDistanceMatrix.distance.compute(current2.getFeatures(), bottomRight.getFeatures())) + ((right == null || bottom == null) ? 0.0d : unifiedDistanceMatrix.distance.compute(right.getFeatures(), bottom.getFeatures()))) * 0.5d;
                j2++;
                unifiedDistanceMatrix = this;
                neuronSquareMesh2D = map;
            }
            i2++;
            unifiedDistanceMatrix = this;
            neuronSquareMesh2D = map;
        }
        int lastRow = uMatrix.length - 1;
        uMatrix[0] = uMatrix[lastRow];
        int lastCol = uMatrix[0].length - 1;
        for (int r = 0; r < lastRow; r++) {
            uMatrix[r][0] = uMatrix[r][lastCol];
        }
        return uMatrix;
    }

    private double[][] averageDistances(NeuronSquareMesh2D map) {
        int numRows = map.getNumberOfRows();
        int numCols = map.getNumberOfColumns();
        double[][] uMatrix = (double[][]) Array.newInstance(double.class, new int[]{numRows, numCols});
        Network net = map.getNetwork();
        for (int i = 0; i < numRows; i++) {
            int j = 0;
            while (j < numCols) {
                Neuron neuron = map.getNeuron(i, j);
                Collection<Neuron> neighbours = net.getNeighbours(neuron);
                double[] features = neuron.getFeatures();
                double d = 0.0d;
                int count = 0;
                for (Neuron n : neighbours) {
                    count++;
                    d += this.distance.compute(features, n.getFeatures());
                    numRows = numRows;
                    numCols = numCols;
                }
                int numRows2 = numRows;
                int i2 = numCols;
                uMatrix[i][j] = d / ((double) count);
                j++;
                numRows = numRows2;
            }
            NeuronSquareMesh2D neuronSquareMesh2D = map;
            int i3 = numRows;
            int i4 = numCols;
        }
        NeuronSquareMesh2D neuronSquareMesh2D2 = map;
        int i5 = numRows;
        int i6 = numCols;
        return uMatrix;
    }
}

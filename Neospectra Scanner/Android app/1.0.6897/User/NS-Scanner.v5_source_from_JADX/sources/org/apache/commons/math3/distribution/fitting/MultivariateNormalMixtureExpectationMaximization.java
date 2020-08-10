package org.apache.commons.math3.distribution.fitting;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Pair;

public class MultivariateNormalMixtureExpectationMaximization {
    private static final int DEFAULT_MAX_ITERATIONS = 1000;
    private static final double DEFAULT_THRESHOLD = 1.0E-5d;
    private final double[][] data;
    private MixtureMultivariateNormalDistribution fittedModel;
    private double logLikelihood = 0.0d;

    private static class DataRow implements Comparable<DataRow> {
        private Double mean = Double.valueOf(0.0d);
        private final double[] row;

        DataRow(double[] data) {
            this.row = data;
            for (double doubleValue : data) {
                this.mean = Double.valueOf(this.mean.doubleValue() + doubleValue);
            }
            this.mean = Double.valueOf(this.mean.doubleValue() / ((double) data.length));
        }

        public int compareTo(DataRow other) {
            return this.mean.compareTo(other.mean);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other instanceof DataRow) {
                return MathArrays.equals(this.row, ((DataRow) other).row);
            }
            return false;
        }

        public int hashCode() {
            return Arrays.hashCode(this.row);
        }

        public double[] getRow() {
            return this.row;
        }
    }

    public MultivariateNormalMixtureExpectationMaximization(double[][] data2) throws NotStrictlyPositiveException, DimensionMismatchException, NumberIsTooSmallException {
        if (data2.length < 1) {
            throw new NotStrictlyPositiveException(Integer.valueOf(data2.length));
        }
        this.data = (double[][]) Array.newInstance(double.class, new int[]{data2.length, data2[0].length});
        int i = 0;
        while (i < data2.length) {
            if (data2[i].length != data2[0].length) {
                throw new DimensionMismatchException(data2[i].length, data2[0].length);
            } else if (data2[i].length < 2) {
                throw new NumberIsTooSmallException(LocalizedFormats.NUMBER_TOO_SMALL, Integer.valueOf(data2[i].length), Integer.valueOf(2), true);
            } else {
                this.data[i] = MathArrays.copyOf(data2[i], data2[i].length);
                i++;
            }
        }
    }

    public void fit(MixtureMultivariateNormalDistribution initialMixture, int maxIterations, double threshold) throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        int numIterations;
        double sumLogLikelihood;
        int i = maxIterations;
        if (i < 1) {
            throw new NotStrictlyPositiveException(Integer.valueOf(maxIterations));
        } else if (threshold < Double.MIN_VALUE) {
            throw new NotStrictlyPositiveException(Double.valueOf(threshold));
        } else {
            int n = this.data.length;
            int numCols = this.data[0].length;
            int k = initialMixture.getComponents().size();
            int numMeanColumns = ((MultivariateNormalDistribution) ((Pair) initialMixture.getComponents().get(0)).getSecond()).getMeans().length;
            if (numMeanColumns != numCols) {
                throw new DimensionMismatchException(numMeanColumns, numCols);
            }
            int numIterations2 = 0;
            double previousLogLikelihood = 0.0d;
            this.logLikelihood = Double.NEGATIVE_INFINITY;
            this.fittedModel = new MixtureMultivariateNormalDistribution(initialMixture.getComponents());
            while (true) {
                numIterations = numIterations2 + 1;
                if (numIterations2 > i || FastMath.abs(previousLogLikelihood - this.logLikelihood) <= threshold) {
                    int i2 = numMeanColumns;
                    int i3 = numIterations;
                } else {
                    double previousLogLikelihood2 = this.logLikelihood;
                    double sumLogLikelihood2 = 0.0d;
                    List<Pair<Double, MultivariateNormalDistribution>> components = this.fittedModel.getComponents();
                    double[] weights = new double[k];
                    MultivariateNormalDistribution[] mvns = new MultivariateNormalDistribution[k];
                    int j = 0;
                    while (true) {
                        int j2 = j;
                        if (j2 >= k) {
                            break;
                        }
                        int numMeanColumns2 = numMeanColumns;
                        weights[j2] = ((Double) ((Pair) components.get(j2)).getFirst()).doubleValue();
                        mvns[j2] = (MultivariateNormalDistribution) ((Pair) components.get(j2)).getSecond();
                        j = j2 + 1;
                        numMeanColumns = numMeanColumns2;
                        int i4 = maxIterations;
                    }
                    int numMeanColumns3 = numMeanColumns;
                    double[][] gamma = (double[][]) Array.newInstance(double.class, new int[]{n, k});
                    double[] gammaSums = new double[k];
                    List list = components;
                    double previousLogLikelihood3 = previousLogLikelihood2;
                    double[][] gammaDataProdSums = (double[][]) Array.newInstance(double.class, new int[]{k, numCols});
                    int i5 = 0;
                    while (i5 < n) {
                        int numIterations3 = numIterations;
                        double rowDensity = this.fittedModel.density(this.data[i5]);
                        double sumLogLikelihood3 = sumLogLikelihood2 + FastMath.log(rowDensity);
                        int j3 = 0;
                        while (true) {
                            sumLogLikelihood = sumLogLikelihood3;
                            int j4 = j3;
                            if (j4 >= k) {
                                break;
                            }
                            double[] weights2 = weights;
                            MultivariateNormalDistribution[] mvns2 = mvns;
                            gamma[i5][j4] = (weights[j4] * mvns[j4].density(this.data[i5])) / rowDensity;
                            gammaSums[j4] = gammaSums[j4] + gamma[i5][j4];
                            for (int col = 0; col < numCols; col++) {
                                double[] dArr = gammaDataProdSums[j4];
                                dArr[col] = dArr[col] + (gamma[i5][j4] * this.data[i5][col]);
                            }
                            j3 = j4 + 1;
                            sumLogLikelihood3 = sumLogLikelihood;
                            weights = weights2;
                            mvns = mvns2;
                        }
                        double[] dArr2 = weights;
                        i5++;
                        numIterations = numIterations3;
                        sumLogLikelihood2 = sumLogLikelihood;
                    }
                    int numIterations4 = numIterations;
                    double[] dArr3 = weights;
                    this.logLikelihood = sumLogLikelihood2 / ((double) n);
                    double[] newWeights = new double[k];
                    double[][] newMeans = (double[][]) Array.newInstance(double.class, new int[]{k, numCols});
                    int j5 = 0;
                    while (j5 < k) {
                        double sumLogLikelihood4 = sumLogLikelihood2;
                        newWeights[j5] = gammaSums[j5] / ((double) n);
                        for (int col2 = 0; col2 < numCols; col2++) {
                            newMeans[j5][col2] = gammaDataProdSums[j5][col2] / gammaSums[j5];
                        }
                        j5++;
                        sumLogLikelihood2 = sumLogLikelihood4;
                    }
                    RealMatrix[] newCovMats = new RealMatrix[k];
                    for (int j6 = 0; j6 < k; j6++) {
                        newCovMats[j6] = new Array2DRowRealMatrix(numCols, numCols);
                    }
                    for (int i6 = 0; i6 < n; i6++) {
                        int j7 = 0;
                        while (j7 < k) {
                            int n2 = n;
                            RealMatrix vec = new Array2DRowRealMatrix(MathArrays.ebeSubtract(this.data[i6], newMeans[j7]));
                            double[][] gamma2 = gamma;
                            newCovMats[j7] = newCovMats[j7].add(vec.multiply(vec.transpose()).scalarMultiply(gamma[i6][j7]));
                            j7++;
                            n = n2;
                            gamma = gamma2;
                        }
                        int i7 = n;
                    }
                    int n3 = n;
                    double[][][] newCovMatArrays = (double[][][]) Array.newInstance(double.class, new int[]{k, numCols, numCols});
                    for (int j8 = 0; j8 < k; j8++) {
                        newCovMats[j8] = newCovMats[j8].scalarMultiply(1.0d / gammaSums[j8]);
                        newCovMatArrays[j8] = newCovMats[j8].getData();
                    }
                    this.fittedModel = new MixtureMultivariateNormalDistribution(newWeights, newMeans, newCovMatArrays);
                    numMeanColumns = numMeanColumns3;
                    previousLogLikelihood = previousLogLikelihood3;
                    numIterations2 = numIterations4;
                    n = n3;
                    i = maxIterations;
                }
            }
            int i22 = numMeanColumns;
            int i32 = numIterations;
            if (FastMath.abs(previousLogLikelihood - this.logLikelihood) > threshold) {
                throw new ConvergenceException();
            }
        }
    }

    public void fit(MixtureMultivariateNormalDistribution initialMixture) throws SingularMatrixException, NotStrictlyPositiveException {
        fit(initialMixture, 1000, 1.0E-5d);
    }

    public static MixtureMultivariateNormalDistribution estimate(double[][] data2, int numComponents) throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] dArr = data2;
        int i = numComponents;
        if (dArr.length < 2) {
            throw new NotStrictlyPositiveException(Integer.valueOf(dArr.length));
        } else if (i < 2) {
            throw new NumberIsTooSmallException(Integer.valueOf(numComponents), Integer.valueOf(2), true);
        } else if (i > dArr.length) {
            throw new NumberIsTooLargeException(Integer.valueOf(numComponents), Integer.valueOf(dArr.length), true);
        } else {
            int numRows = dArr.length;
            int numCols = dArr[0].length;
            DataRow[] sortedData = new DataRow[numRows];
            for (int i2 = 0; i2 < numRows; i2++) {
                sortedData[i2] = new DataRow(dArr[i2]);
            }
            Arrays.sort(sortedData);
            double weight = 1.0d / ((double) i);
            List<Pair<Double, MultivariateNormalDistribution>> components = new ArrayList<>(i);
            int binIndex = 0;
            while (binIndex < i) {
                int minIndex = (binIndex * numRows) / i;
                int maxIndex = ((binIndex + 1) * numRows) / i;
                int numBinRows = maxIndex - minIndex;
                double[][] binData = (double[][]) Array.newInstance(double.class, new int[]{numBinRows, numCols});
                double[] columnMeans = new double[numCols];
                int i3 = minIndex;
                int i4 = 0;
                while (true) {
                    int iBin = i4;
                    if (i3 >= maxIndex) {
                        break;
                    }
                    int j = 0;
                    while (true) {
                        int j2 = j;
                        if (j2 >= numCols) {
                            break;
                        }
                        double val = sortedData[i3].getRow()[j2];
                        columnMeans[j2] = columnMeans[j2] + val;
                        binData[iBin][j2] = val;
                        j = j2 + 1;
                        double[][] dArr2 = data2;
                        int i5 = numComponents;
                    }
                    i3++;
                    i4 = iBin + 1;
                    double[][] dArr3 = data2;
                    int i6 = numComponents;
                }
                MathArrays.scaleInPlace(1.0d / ((double) numBinRows), columnMeans);
                components.add(new Pair(Double.valueOf(weight), new MultivariateNormalDistribution(columnMeans, new Covariance(binData).getCovarianceMatrix().getData())));
                binIndex++;
                double[][] dArr4 = data2;
                i = numComponents;
            }
            return new MixtureMultivariateNormalDistribution(components);
        }
    }

    public double getLogLikelihood() {
        return this.logLikelihood;
    }

    public MixtureMultivariateNormalDistribution getFittedModel() {
        return new MixtureMultivariateNormalDistribution(this.fittedModel.getComponents());
    }
}

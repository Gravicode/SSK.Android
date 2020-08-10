package org.apache.commons.math3.stat.regression;

import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

public class RegressionResults implements Serializable {
    private static final int ADJRSQ_IDX = 4;
    private static final int MSE_IDX = 3;
    private static final int RSQ_IDX = 2;
    private static final int SSE_IDX = 0;
    private static final int SST_IDX = 1;
    private static final long serialVersionUID = 1;
    private final boolean containsConstant;
    private final double[] globalFitInfo;
    private final boolean isSymmetricVCD;
    private final long nobs;
    private final double[] parameters;
    private final int rank;
    private final double[][] varCovData;

    private RegressionResults() {
        this.parameters = null;
        this.varCovData = null;
        this.rank = -1;
        this.nobs = -1;
        this.containsConstant = false;
        this.isSymmetricVCD = false;
        this.globalFitInfo = null;
    }

    public RegressionResults(double[] parameters2, double[][] varcov, boolean isSymmetricCompressed, long nobs2, int rank2, double sumy, double sumysq, double sse, boolean containsConstant2, boolean copyData) {
        double[][] dArr = varcov;
        long j = nobs2;
        int i = rank2;
        boolean z = containsConstant2;
        if (copyData) {
            this.parameters = MathArrays.copyOf(parameters2);
            this.varCovData = new double[dArr.length][];
            for (int i2 = 0; i2 < dArr.length; i2++) {
                this.varCovData[i2] = MathArrays.copyOf(dArr[i2]);
            }
            double[] dArr2 = parameters2;
        } else {
            this.parameters = parameters2;
            this.varCovData = dArr;
        }
        this.isSymmetricVCD = isSymmetricCompressed;
        this.nobs = j;
        this.rank = i;
        this.containsConstant = z;
        this.globalFitInfo = new double[5];
        Arrays.fill(this.globalFitInfo, Double.NaN);
        if (i > 0) {
            this.globalFitInfo[1] = z ? sumysq - ((sumy * sumy) / ((double) j)) : sumysq;
        }
        this.globalFitInfo[0] = sse;
        this.globalFitInfo[3] = this.globalFitInfo[0] / ((double) (j - ((long) i)));
        this.globalFitInfo[2] = 1.0d - (this.globalFitInfo[0] / this.globalFitInfo[1]);
        if (!z) {
            this.globalFitInfo[4] = 1.0d - ((1.0d - this.globalFitInfo[2]) * (((double) j) / ((double) (j - ((long) i)))));
        } else {
            this.globalFitInfo[4] = 1.0d - (((((double) j) - 1.0d) * sse) / (this.globalFitInfo[1] * ((double) (j - ((long) i)))));
        }
    }

    public double getParameterEstimate(int index) throws OutOfRangeException {
        if (this.parameters == null) {
            return Double.NaN;
        }
        if (index >= 0 && index < this.parameters.length) {
            return this.parameters[index];
        }
        throw new OutOfRangeException(Integer.valueOf(index), Integer.valueOf(0), Integer.valueOf(this.parameters.length - 1));
    }

    public double[] getParameterEstimates() {
        if (this.parameters == null) {
            return null;
        }
        return MathArrays.copyOf(this.parameters);
    }

    public double getStdErrorOfEstimate(int index) throws OutOfRangeException {
        if (this.parameters == null) {
            return Double.NaN;
        }
        if (index < 0 || index >= this.parameters.length) {
            throw new OutOfRangeException(Integer.valueOf(index), Integer.valueOf(0), Integer.valueOf(this.parameters.length - 1));
        }
        double var = getVcvElement(index, index);
        if (Double.isNaN(var) || var <= Double.MIN_VALUE) {
            return Double.NaN;
        }
        return FastMath.sqrt(var);
    }

    public double[] getStdErrorOfEstimates() {
        if (this.parameters == null) {
            return null;
        }
        double[] se = new double[this.parameters.length];
        for (int i = 0; i < this.parameters.length; i++) {
            double var = getVcvElement(i, i);
            if (Double.isNaN(var) || var <= Double.MIN_VALUE) {
                se[i] = Double.NaN;
            } else {
                se[i] = FastMath.sqrt(var);
            }
        }
        return se;
    }

    public double getCovarianceOfParameters(int i, int j) throws OutOfRangeException {
        if (this.parameters == null) {
            return Double.NaN;
        }
        if (i < 0 || i >= this.parameters.length) {
            throw new OutOfRangeException(Integer.valueOf(i), Integer.valueOf(0), Integer.valueOf(this.parameters.length - 1));
        } else if (j >= 0 && j < this.parameters.length) {
            return getVcvElement(i, j);
        } else {
            throw new OutOfRangeException(Integer.valueOf(j), Integer.valueOf(0), Integer.valueOf(this.parameters.length - 1));
        }
    }

    public int getNumberOfParameters() {
        if (this.parameters == null) {
            return -1;
        }
        return this.parameters.length;
    }

    public long getN() {
        return this.nobs;
    }

    public double getTotalSumSquares() {
        return this.globalFitInfo[1];
    }

    public double getRegressionSumSquares() {
        return this.globalFitInfo[1] - this.globalFitInfo[0];
    }

    public double getErrorSumSquares() {
        return this.globalFitInfo[0];
    }

    public double getMeanSquareError() {
        return this.globalFitInfo[3];
    }

    public double getRSquared() {
        return this.globalFitInfo[2];
    }

    public double getAdjustedRSquared() {
        return this.globalFitInfo[4];
    }

    public boolean hasIntercept() {
        return this.containsConstant;
    }

    private double getVcvElement(int i, int j) {
        if (!this.isSymmetricVCD) {
            return this.varCovData[i][j];
        }
        if (this.varCovData.length > 1) {
            if (i == j) {
                return this.varCovData[i][i];
            }
            if (i >= this.varCovData[j].length) {
                return this.varCovData[i][j];
            }
            return this.varCovData[j][i];
        } else if (i > j) {
            return this.varCovData[0][(((i + 1) * i) / 2) + j];
        } else {
            return this.varCovData[0][(((j + 1) * j) / 2) + i];
        }
    }
}

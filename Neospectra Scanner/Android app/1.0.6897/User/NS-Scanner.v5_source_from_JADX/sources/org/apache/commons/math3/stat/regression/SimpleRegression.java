package org.apache.commons.math3.stat.regression;

import java.io.Serializable;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

public class SimpleRegression implements Serializable, UpdatingMultipleLinearRegression {
    private static final long serialVersionUID = -3004689053607543335L;
    private final boolean hasIntercept;

    /* renamed from: n */
    private long f796n;
    private double sumX;
    private double sumXX;
    private double sumXY;
    private double sumY;
    private double sumYY;
    private double xbar;
    private double ybar;

    public SimpleRegression() {
        this(true);
    }

    public SimpleRegression(boolean includeIntercept) {
        this.sumX = 0.0d;
        this.sumXX = 0.0d;
        this.sumY = 0.0d;
        this.sumYY = 0.0d;
        this.sumXY = 0.0d;
        this.f796n = 0;
        this.xbar = 0.0d;
        this.ybar = 0.0d;
        this.hasIntercept = includeIntercept;
    }

    public void addData(double x, double y) {
        double d = x;
        double d2 = y;
        if (this.f796n == 0) {
            this.xbar = d;
            this.ybar = d2;
        } else if (this.hasIntercept) {
            double fact1 = ((double) this.f796n) + 1.0d;
            double fact2 = ((double) this.f796n) / (((double) this.f796n) + 1.0d);
            double dx = d - this.xbar;
            double dy = d2 - this.ybar;
            this.sumXX += dx * dx * fact2;
            this.sumYY += dy * dy * fact2;
            this.sumXY += dx * dy * fact2;
            this.xbar += dx / fact1;
            this.ybar += dy / fact1;
        }
        if (!this.hasIntercept) {
            this.sumXX += d * d;
            this.sumYY += d2 * d2;
            this.sumXY += d * d2;
        }
        this.sumX += d;
        this.sumY += d2;
        this.f796n++;
    }

    public void append(SimpleRegression reg) {
        if (this.f796n == 0) {
            this.xbar = reg.xbar;
            this.ybar = reg.ybar;
            this.sumXX = reg.sumXX;
            this.sumYY = reg.sumYY;
            this.sumXY = reg.sumXY;
        } else if (this.hasIntercept) {
            double fact1 = ((double) reg.f796n) / ((double) (reg.f796n + this.f796n));
            double fact2 = ((double) (this.f796n * reg.f796n)) / ((double) (reg.f796n + this.f796n));
            double dx = reg.xbar - this.xbar;
            double dy = reg.ybar - this.ybar;
            this.sumXX += reg.sumXX + (dx * dx * fact2);
            this.sumYY += reg.sumYY + (dy * dy * fact2);
            this.sumXY += reg.sumXY + (dx * dy * fact2);
            this.xbar += dx * fact1;
            this.ybar += dy * fact1;
        } else {
            this.sumXX += reg.sumXX;
            this.sumYY += reg.sumYY;
            this.sumXY += reg.sumXY;
        }
        this.sumX += reg.sumX;
        this.sumY += reg.sumY;
        this.f796n += reg.f796n;
    }

    public void removeData(double x, double y) {
        if (this.f796n > 0) {
            if (this.hasIntercept) {
                double fact1 = ((double) this.f796n) - 1.0d;
                double fact2 = ((double) this.f796n) / (((double) this.f796n) - 1.0d);
                double dx = x - this.xbar;
                double dy = y - this.ybar;
                this.sumXX -= (dx * dx) * fact2;
                this.sumYY -= (dy * dy) * fact2;
                this.sumXY -= (dx * dy) * fact2;
                this.xbar -= dx / fact1;
                this.ybar -= dy / fact1;
            } else {
                double fact12 = ((double) this.f796n) - 1.0d;
                this.sumXX -= x * x;
                this.sumYY -= y * y;
                this.sumXY -= x * y;
                this.xbar -= x / fact12;
                this.ybar -= y / fact12;
            }
            this.sumX -= x;
            this.sumY -= y;
            this.f796n--;
        }
    }

    public void addData(double[][] data) throws ModelSpecificationException {
        for (int i = 0; i < data.length; i++) {
            if (data[i].length < 2) {
                throw new ModelSpecificationException(LocalizedFormats.INVALID_REGRESSION_OBSERVATION, Integer.valueOf(data[i].length), Integer.valueOf(2));
            }
            addData(data[i][0], data[i][1]);
        }
    }

    public void addObservation(double[] x, double y) throws ModelSpecificationException {
        if (x == null || x.length == 0) {
            LocalizedFormats localizedFormats = LocalizedFormats.INVALID_REGRESSION_OBSERVATION;
            Object[] objArr = new Object[2];
            objArr[0] = Integer.valueOf(x != null ? x.length : 0);
            objArr[1] = Integer.valueOf(1);
            throw new ModelSpecificationException(localizedFormats, objArr);
        }
        addData(x[0], y);
    }

    public void addObservations(double[][] x, double[] y) throws ModelSpecificationException {
        int i = 0;
        if (x == null || y == null || x.length != y.length) {
            LocalizedFormats localizedFormats = LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE;
            Object[] objArr = new Object[2];
            objArr[0] = Integer.valueOf(x == null ? 0 : x.length);
            if (y != null) {
                i = y.length;
            }
            objArr[1] = Integer.valueOf(i);
            throw new ModelSpecificationException(localizedFormats, objArr);
        }
        boolean obsOk = true;
        for (int i2 = 0; i2 < x.length; i2++) {
            if (x[i2] == null || x[i2].length == 0) {
                obsOk = false;
            }
        }
        if (!obsOk) {
            throw new ModelSpecificationException(LocalizedFormats.NOT_ENOUGH_DATA_FOR_NUMBER_OF_PREDICTORS, Integer.valueOf(0), Integer.valueOf(1));
        }
        for (int i3 = 0; i3 < x.length; i3++) {
            addData(x[i3][0], y[i3]);
        }
    }

    public void removeData(double[][] data) {
        for (int i = 0; i < data.length && this.f796n > 0; i++) {
            removeData(data[i][0], data[i][1]);
        }
    }

    public void clear() {
        this.sumX = 0.0d;
        this.sumXX = 0.0d;
        this.sumY = 0.0d;
        this.sumYY = 0.0d;
        this.sumXY = 0.0d;
        this.f796n = 0;
    }

    public long getN() {
        return this.f796n;
    }

    public double predict(double x) {
        double b1 = getSlope();
        if (this.hasIntercept) {
            return getIntercept(b1) + (b1 * x);
        }
        return b1 * x;
    }

    public double getIntercept() {
        if (this.hasIntercept) {
            return getIntercept(getSlope());
        }
        return 0.0d;
    }

    public boolean hasIntercept() {
        return this.hasIntercept;
    }

    public double getSlope() {
        if (this.f796n >= 2 && FastMath.abs(this.sumXX) >= 4.9E-323d) {
            return this.sumXY / this.sumXX;
        }
        return Double.NaN;
    }

    public double getSumSquaredErrors() {
        return FastMath.max(0.0d, this.sumYY - ((this.sumXY * this.sumXY) / this.sumXX));
    }

    public double getTotalSumSquares() {
        if (this.f796n < 2) {
            return Double.NaN;
        }
        return this.sumYY;
    }

    public double getXSumSquares() {
        if (this.f796n < 2) {
            return Double.NaN;
        }
        return this.sumXX;
    }

    public double getSumOfCrossProducts() {
        return this.sumXY;
    }

    public double getRegressionSumSquares() {
        return getRegressionSumSquares(getSlope());
    }

    public double getMeanSquareError() {
        double sumSquaredErrors;
        long j;
        long j2;
        if (this.f796n < 3) {
            return Double.NaN;
        }
        if (this.hasIntercept) {
            sumSquaredErrors = getSumSquaredErrors();
            j = this.f796n;
            j2 = 2;
        } else {
            sumSquaredErrors = getSumSquaredErrors();
            j = this.f796n;
            j2 = 1;
        }
        return sumSquaredErrors / ((double) (j - j2));
    }

    public double getR() {
        double b1 = getSlope();
        double result = FastMath.sqrt(getRSquare());
        if (b1 < 0.0d) {
            return -result;
        }
        return result;
    }

    public double getRSquare() {
        double ssto = getTotalSumSquares();
        return (ssto - getSumSquaredErrors()) / ssto;
    }

    public double getInterceptStdErr() {
        if (!this.hasIntercept) {
            return Double.NaN;
        }
        return FastMath.sqrt(getMeanSquareError() * ((1.0d / ((double) this.f796n)) + ((this.xbar * this.xbar) / this.sumXX)));
    }

    public double getSlopeStdErr() {
        return FastMath.sqrt(getMeanSquareError() / this.sumXX);
    }

    public double getSlopeConfidenceInterval() throws OutOfRangeException {
        return getSlopeConfidenceInterval(0.05d);
    }

    public double getSlopeConfidenceInterval(double alpha) throws OutOfRangeException {
        if (this.f796n < 3) {
            return Double.NaN;
        }
        if (alpha >= 1.0d || alpha <= 0.0d) {
            throw new OutOfRangeException(LocalizedFormats.SIGNIFICANCE_LEVEL, Double.valueOf(alpha), Integer.valueOf(0), Integer.valueOf(1));
        }
        return getSlopeStdErr() * new TDistribution((double) (this.f796n - 2)).inverseCumulativeProbability(1.0d - (alpha / 2.0d));
    }

    public double getSignificance() {
        if (this.f796n < 3) {
            return Double.NaN;
        }
        return (1.0d - new TDistribution((double) (this.f796n - 2)).cumulativeProbability(FastMath.abs(getSlope()) / getSlopeStdErr())) * 2.0d;
    }

    private double getIntercept(double slope) {
        if (this.hasIntercept) {
            return (this.sumY - (this.sumX * slope)) / ((double) this.f796n);
        }
        return 0.0d;
    }

    private double getRegressionSumSquares(double slope) {
        return slope * slope * this.sumXX;
    }

    public RegressionResults regress() throws ModelSpecificationException, NoDataException {
        if (this.hasIntercept) {
            if (this.f796n < 3) {
                throw new NoDataException(LocalizedFormats.NOT_ENOUGH_DATA_REGRESSION);
            } else if (FastMath.abs(this.sumXX) > Precision.SAFE_MIN) {
                double[] params = {getIntercept(), getSlope()};
                double mse = getMeanSquareError();
                double _syy = this.sumYY + ((this.sumY * this.sumY) / ((double) this.f796n));
                RegressionResults regressionResults = new RegressionResults(params, new double[][]{new double[]{(((this.xbar * this.xbar) / this.sumXX) + (1.0d / ((double) this.f796n))) * mse, ((-this.xbar) * mse) / this.sumXX, mse / this.sumXX}}, true, this.f796n, 2, this.sumY, _syy, getSumSquaredErrors(), true, false);
                return regressionResults;
            } else {
                double[] params2 = {this.sumY / ((double) this.f796n), Double.NaN};
                double[][] dArr = {new double[]{this.ybar / (((double) this.f796n) - 1.0d), Double.NaN, Double.NaN}};
                long j = this.f796n;
                double d = this.sumY;
                RegressionResults regressionResults2 = new RegressionResults(params2, dArr, true, j, 1, d, this.sumYY, getSumSquaredErrors(), true, false);
                return regressionResults2;
            }
        } else if (this.f796n < 2) {
            throw new NoDataException(LocalizedFormats.NOT_ENOUGH_DATA_REGRESSION);
        } else if (!Double.isNaN(this.sumXX)) {
            RegressionResults regressionResults3 = new RegressionResults(new double[]{this.sumXY / this.sumXX}, new double[][]{new double[]{getMeanSquareError() / this.sumXX}}, true, this.f796n, 1, this.sumY, this.sumYY, getSumSquaredErrors(), false, false);
            return regressionResults3;
        } else {
            RegressionResults regressionResults4 = new RegressionResults(new double[]{Double.NaN}, new double[][]{new double[]{Double.NaN}}, true, this.f796n, 1, Double.NaN, Double.NaN, Double.NaN, false, false);
            return regressionResults4;
        }
    }

    public RegressionResults regress(int[] variablesToInclude) throws MathIllegalArgumentException {
        int[] iArr = variablesToInclude;
        if (iArr == null || iArr.length == 0) {
            throw new MathIllegalArgumentException(LocalizedFormats.ARRAY_ZERO_LENGTH_OR_NULL_NOT_ALLOWED, new Object[0]);
        }
        int i = 2;
        if (iArr.length > 2 || (iArr.length > 1 && !this.hasIntercept)) {
            LocalizedFormats localizedFormats = LocalizedFormats.ARRAY_SIZE_EXCEEDS_MAX_VARIABLES;
            Object[] objArr = new Object[1];
            if (iArr.length > 1 && !this.hasIntercept) {
                i = 1;
            }
            objArr[0] = Integer.valueOf(i);
            throw new ModelSpecificationException(localizedFormats, objArr);
        } else if (this.hasIntercept) {
            if (iArr.length == 2) {
                if (iArr[0] == 1) {
                    throw new ModelSpecificationException(LocalizedFormats.NOT_INCREASING_SEQUENCE, new Object[0]);
                } else if (iArr[0] != 0) {
                    throw new OutOfRangeException(Integer.valueOf(iArr[0]), Integer.valueOf(0), Integer.valueOf(1));
                } else if (iArr[1] == 1) {
                    return regress();
                } else {
                    throw new OutOfRangeException(Integer.valueOf(iArr[0]), Integer.valueOf(0), Integer.valueOf(1));
                }
            } else if (iArr[0] == 1 || iArr[0] == 0) {
                double _mean = (this.sumY * this.sumY) / ((double) this.f796n);
                double _syy = this.sumYY + _mean;
                if (iArr[0] == 0) {
                    double[] params = {this.ybar};
                    double[][] dArr = {new double[]{this.sumYY / ((double) ((this.f796n - 1) * this.f796n))}};
                    long j = this.f796n;
                    long j2 = j;
                    RegressionResults regressionResults = new RegressionResults(params, dArr, true, j2, 1, this.sumY, _syy + _mean, this.sumYY, true, false);
                    return regressionResults;
                } else if (iArr[0] != 1) {
                    return null;
                } else {
                    double d = _mean;
                    double _sxx = this.sumXX + ((this.sumX * this.sumX) / ((double) this.f796n));
                    double _sxy = this.sumXY + ((this.sumX * this.sumY) / ((double) this.f796n));
                    double _sse = FastMath.max(0.0d, _syy - ((_sxy * _sxy) / _sxx));
                    double _mse = _sse / ((double) (this.f796n - 1));
                    if (!Double.isNaN(_sxx)) {
                        RegressionResults regressionResults2 = new RegressionResults(new double[]{_sxy / _sxx}, new double[][]{new double[]{_mse / _sxx}}, true, this.f796n, 1, this.sumY, _syy, _sse, false, false);
                        return regressionResults2;
                    }
                    RegressionResults regressionResults3 = new RegressionResults(new double[]{Double.NaN}, new double[][]{new double[]{Double.NaN}}, true, this.f796n, 1, Double.NaN, Double.NaN, Double.NaN, false, false);
                    return regressionResults3;
                }
            } else {
                throw new OutOfRangeException(Integer.valueOf(iArr[0]), Integer.valueOf(0), Integer.valueOf(1));
            }
        } else if (iArr[0] == 0) {
            return regress();
        } else {
            throw new OutOfRangeException(Integer.valueOf(iArr[0]), Integer.valueOf(0), Integer.valueOf(0));
        }
    }
}

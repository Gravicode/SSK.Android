package org.apache.commons.math3.analysis.interpolation;

import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;

public class LoessInterpolator implements UnivariateInterpolator, Serializable {
    public static final double DEFAULT_ACCURACY = 1.0E-12d;
    public static final double DEFAULT_BANDWIDTH = 0.3d;
    public static final int DEFAULT_ROBUSTNESS_ITERS = 2;
    private static final long serialVersionUID = 5204927143605193821L;
    private final double accuracy;
    private final double bandwidth;
    private final int robustnessIters;

    public LoessInterpolator() {
        this.bandwidth = 0.3d;
        this.robustnessIters = 2;
        this.accuracy = 1.0E-12d;
    }

    public LoessInterpolator(double bandwidth2, int robustnessIters2) {
        this(bandwidth2, robustnessIters2, 1.0E-12d);
    }

    public LoessInterpolator(double bandwidth2, int robustnessIters2, double accuracy2) throws OutOfRangeException, NotPositiveException {
        if (bandwidth2 < 0.0d || bandwidth2 > 1.0d) {
            throw new OutOfRangeException(LocalizedFormats.BANDWIDTH, Double.valueOf(bandwidth2), Integer.valueOf(0), Integer.valueOf(1));
        }
        this.bandwidth = bandwidth2;
        if (robustnessIters2 < 0) {
            throw new NotPositiveException(LocalizedFormats.ROBUSTNESS_ITERATIONS, Integer.valueOf(robustnessIters2));
        }
        this.robustnessIters = robustnessIters2;
        this.accuracy = accuracy2;
    }

    public final PolynomialSplineFunction interpolate(double[] xval, double[] yval) throws NonMonotonicSequenceException, DimensionMismatchException, NoDataException, NotFiniteNumberException, NumberIsTooSmallException {
        return new SplineInterpolator().interpolate(xval, smooth(xval, yval));
    }

    public final double[] smooth(double[] xval, double[] yval, double[] weights) throws NonMonotonicSequenceException, DimensionMismatchException, NoDataException, NotFiniteNumberException, NumberIsTooSmallException {
        int edge;
        int bandwidthInPoints;
        double beta;
        double[] residuals = xval;
        double[] sortedResiduals = yval;
        double[] dArr = weights;
        if (residuals.length != sortedResiduals.length) {
            throw new DimensionMismatchException(residuals.length, sortedResiduals.length);
        }
        int n = residuals.length;
        if (n == 0) {
            throw new NoDataException();
        }
        checkAllFiniteReal(xval);
        checkAllFiniteReal(yval);
        checkAllFiniteReal(weights);
        MathArrays.checkOrder(xval);
        int i = 0;
        char c = 1;
        if (n == 1) {
            return new double[]{sortedResiduals[0]};
        }
        int i2 = 2;
        if (n == 2) {
            return new double[]{sortedResiduals[0], sortedResiduals[1]};
        }
        int k = (int) (this.bandwidth * ((double) n));
        if (k < 2) {
            throw new NumberIsTooSmallException(LocalizedFormats.BANDWIDTH, Integer.valueOf(k), Integer.valueOf(2), true);
        }
        double[] res = new double[n];
        double[] residuals2 = new double[n];
        double[] sortedResiduals2 = new double[n];
        double[] robustnessWeights = new double[n];
        Arrays.fill(robustnessWeights, 1.0d);
        int iter = 0;
        while (true) {
            if (iter > this.robustnessIters) {
                int bandwidthInPoints2 = k;
                double[] dArr2 = residuals2;
                double[] dArr3 = sortedResiduals2;
                break;
            }
            int[] bandwidthInterval = new int[i2];
            bandwidthInterval[i] = i;
            bandwidthInterval[c] = k - 1;
            int i3 = 0;
            while (i3 < n) {
                double x = residuals[i3];
                if (i3 > 0) {
                    updateBandwidthInterval(residuals, dArr, i3, bandwidthInterval);
                }
                int ileft = bandwidthInterval[i];
                int iright = bandwidthInterval[c];
                if (residuals[i3] - residuals[ileft] > residuals[iright] - residuals[i3]) {
                    edge = ileft;
                } else {
                    edge = iright;
                }
                double sumX = 0.0d;
                double sumY = 0.0d;
                double sumXY = 0.0d;
                double denom = FastMath.abs(1.0d / (residuals[edge] - x));
                double sumXSquared = 0.0d;
                double sumWeights = 0.0d;
                int k2 = ileft;
                while (true) {
                    bandwidthInPoints = k;
                    int k3 = k2;
                    if (k3 > iright) {
                        break;
                    }
                    double xk = residuals[k3];
                    double yk = sortedResiduals[k3];
                    double[] residuals3 = residuals2;
                    double w = tricube((k3 < i3 ? x - xk : xk - x) * denom) * robustnessWeights[k3] * dArr[k3];
                    double xkw = xk * w;
                    sumWeights += w;
                    sumX += xkw;
                    sumXSquared += xk * xkw;
                    sumY += yk * w;
                    sumXY += yk * xkw;
                    k2 = k3 + 1;
                    k = bandwidthInPoints;
                    residuals2 = residuals3;
                    sortedResiduals2 = sortedResiduals2;
                }
                double[] residuals4 = residuals2;
                double[] sortedResiduals3 = sortedResiduals2;
                double meanX = sumX / sumWeights;
                double meanY = sumY / sumWeights;
                double meanXY = sumXY / sumWeights;
                double meanXSquared = sumXSquared / sumWeights;
                double d = denom;
                int i4 = iright;
                if (FastMath.sqrt(FastMath.abs(meanXSquared - (meanX * meanX))) < this.accuracy) {
                    beta = 0.0d;
                } else {
                    beta = (meanXY - (meanX * meanY)) / (meanXSquared - (meanX * meanX));
                }
                res[i3] = (beta * x) + (meanY - (beta * meanX));
                residuals4[i3] = FastMath.abs(sortedResiduals[i3] - res[i3]);
                i3++;
                k = bandwidthInPoints;
                residuals2 = residuals4;
                sortedResiduals2 = sortedResiduals3;
                residuals = xval;
                sortedResiduals = yval;
                i = 0;
                c = 1;
            }
            int bandwidthInPoints3 = k;
            double[] residuals5 = residuals2;
            double[] sortedResiduals4 = sortedResiduals2;
            if (iter == this.robustnessIters) {
                double[] dArr4 = residuals5;
                double[] dArr5 = sortedResiduals4;
                break;
            }
            double[] residuals6 = residuals5;
            double[] sortedResiduals5 = sortedResiduals4;
            System.arraycopy(residuals6, 0, sortedResiduals5, 0, n);
            Arrays.sort(sortedResiduals5);
            double medianResidual = sortedResiduals5[n / 2];
            double medianResidual2 = medianResidual;
            if (FastMath.abs(medianResidual) < this.accuracy) {
                break;
            }
            for (int i5 = 0; i5 < n; i5++) {
                double arg = residuals6[i5] / (6.0d * medianResidual2);
                if (arg >= 1.0d) {
                    robustnessWeights[i5] = 0.0d;
                } else {
                    double w2 = 1.0d - (arg * arg);
                    robustnessWeights[i5] = w2 * w2;
                }
            }
            iter++;
            k = bandwidthInPoints3;
            i = 0;
            c = 1;
            i2 = 2;
            residuals2 = residuals6;
            sortedResiduals2 = sortedResiduals5;
            residuals = xval;
            sortedResiduals = yval;
        }
        return res;
    }

    public final double[] smooth(double[] xval, double[] yval) throws NonMonotonicSequenceException, DimensionMismatchException, NoDataException, NotFiniteNumberException, NumberIsTooSmallException {
        if (xval.length != yval.length) {
            throw new DimensionMismatchException(xval.length, yval.length);
        }
        double[] unitWeights = new double[xval.length];
        Arrays.fill(unitWeights, 1.0d);
        return smooth(xval, yval, unitWeights);
    }

    private static void updateBandwidthInterval(double[] xval, double[] weights, int i, int[] bandwidthInterval) {
        int left = bandwidthInterval[0];
        int nextRight = nextNonzero(weights, bandwidthInterval[1]);
        if (nextRight < xval.length && xval[nextRight] - xval[i] < xval[i] - xval[left]) {
            bandwidthInterval[0] = nextNonzero(weights, bandwidthInterval[0]);
            bandwidthInterval[1] = nextRight;
        }
    }

    private static int nextNonzero(double[] weights, int i) {
        int j = i + 1;
        while (j < weights.length && weights[j] == 0.0d) {
            j++;
        }
        return j;
    }

    private static double tricube(double x) {
        double absX = FastMath.abs(x);
        if (absX >= 1.0d) {
            return 0.0d;
        }
        double tmp = 1.0d - ((absX * absX) * absX);
        return tmp * tmp * tmp;
    }

    private static void checkAllFiniteReal(double[] values) {
        for (double checkFinite : values) {
            MathUtils.checkFinite(checkFinite);
        }
    }
}

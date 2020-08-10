package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import java.util.Comparator;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;

public class NelderMeadSimplex extends AbstractSimplex {
    private static final double DEFAULT_GAMMA = 0.5d;
    private static final double DEFAULT_KHI = 2.0d;
    private static final double DEFAULT_RHO = 1.0d;
    private static final double DEFAULT_SIGMA = 0.5d;
    private final double gamma;
    private final double khi;
    private final double rho;
    private final double sigma;

    public NelderMeadSimplex(int n) {
        this(n, DEFAULT_RHO);
    }

    public NelderMeadSimplex(int n, double sideLength) {
        this(n, sideLength, DEFAULT_RHO, DEFAULT_KHI, 0.5d, 0.5d);
    }

    public NelderMeadSimplex(int n, double sideLength, double rho2, double khi2, double gamma2, double sigma2) {
        super(n, sideLength);
        this.rho = rho2;
        this.khi = khi2;
        this.gamma = gamma2;
        this.sigma = sigma2;
    }

    public NelderMeadSimplex(int n, double rho2, double khi2, double gamma2, double sigma2) {
        this(n, DEFAULT_RHO, rho2, khi2, gamma2, sigma2);
    }

    public NelderMeadSimplex(double[] steps) {
        this(steps, (double) DEFAULT_RHO, (double) DEFAULT_KHI, 0.5d, 0.5d);
    }

    public NelderMeadSimplex(double[] steps, double rho2, double khi2, double gamma2, double sigma2) {
        super(steps);
        this.rho = rho2;
        this.khi = khi2;
        this.gamma = gamma2;
        this.sigma = sigma2;
    }

    public NelderMeadSimplex(double[][] referenceSimplex) {
        this(referenceSimplex, (double) DEFAULT_RHO, (double) DEFAULT_KHI, 0.5d, 0.5d);
    }

    public NelderMeadSimplex(double[][] referenceSimplex, double rho2, double khi2, double gamma2, double sigma2) {
        super(referenceSimplex);
        this.rho = rho2;
        this.khi = khi2;
        this.gamma = gamma2;
        this.sigma = sigma2;
    }

    public void iterate(MultivariateFunction evaluationFunction, Comparator<PointValuePair> comparator) {
        int i;
        MultivariateFunction multivariateFunction = evaluationFunction;
        Comparator<PointValuePair> comparator2 = comparator;
        int n = getDimension();
        PointValuePair best = getPoint(0);
        PointValuePair secondBest = getPoint(n - 1);
        PointValuePair worst = getPoint(n);
        double[] xWorst = worst.getPointRef();
        double[] centroid = new double[n];
        for (int i2 = 0; i2 < n; i2++) {
            double[] x = getPoint(i2).getPointRef();
            for (int j = 0; j < n; j++) {
                centroid[j] = centroid[j] + x[j];
            }
        }
        double scaling = DEFAULT_RHO / ((double) n);
        for (int j2 = 0; j2 < n; j2++) {
            centroid[j2] = centroid[j2] * scaling;
        }
        double[] xR = new double[n];
        int j3 = 0;
        while (j3 < n) {
            PointValuePair best2 = best;
            xR[j3] = centroid[j3] + (this.rho * (centroid[j3] - xWorst[j3]));
            j3++;
            best = best2;
        }
        PointValuePair best3 = best;
        PointValuePair reflected = new PointValuePair(xR, multivariateFunction.value(xR), false);
        PointValuePair best4 = best3;
        if (comparator2.compare(best4, reflected) <= 0 && comparator2.compare(reflected, secondBest) < 0) {
            replaceWorstPoint(reflected, comparator2);
            int i3 = n;
            PointValuePair pointValuePair = reflected;
            PointValuePair pointValuePair2 = best4;
            PointValuePair pointValuePair3 = secondBest;
        } else if (comparator2.compare(reflected, best4) < 0) {
            double[] xE = new double[n];
            int j4 = 0;
            while (j4 < n) {
                PointValuePair best5 = best4;
                PointValuePair secondBest2 = secondBest;
                xE[j4] = centroid[j4] + (this.khi * (xR[j4] - centroid[j4]));
                j4++;
                secondBest = secondBest2;
                best4 = best5;
            }
            PointValuePair pointValuePair4 = secondBest;
            PointValuePair expanded = new PointValuePair(xE, multivariateFunction.value(xE), false);
            if (comparator2.compare(expanded, reflected) < 0) {
                replaceWorstPoint(expanded, comparator2);
            } else {
                replaceWorstPoint(reflected, comparator2);
            }
            int i4 = n;
            PointValuePair pointValuePair5 = reflected;
        } else {
            PointValuePair pointValuePair6 = secondBest;
            if (comparator2.compare(reflected, worst) < 0) {
                double[] xC = new double[n];
                int j5 = 0;
                while (j5 < n) {
                    double scaling2 = scaling;
                    xC[j5] = centroid[j5] + (this.gamma * (xR[j5] - centroid[j5]));
                    j5++;
                    scaling = scaling2;
                }
                PointValuePair outContracted = new PointValuePair(xC, multivariateFunction.value(xC), false);
                if (comparator2.compare(outContracted, reflected) <= 0) {
                    replaceWorstPoint(outContracted, comparator2);
                    return;
                }
                i = 0;
            } else {
                double[] xC2 = new double[n];
                for (int j6 = 0; j6 < n; j6++) {
                    xC2[j6] = centroid[j6] - (this.gamma * (centroid[j6] - xWorst[j6]));
                }
                i = 0;
                PointValuePair inContracted = new PointValuePair(xC2, multivariateFunction.value(xC2), false);
                if (comparator2.compare(inContracted, worst) < 0) {
                    replaceWorstPoint(inContracted, comparator2);
                    return;
                }
            }
            double[] xSmallest = getPoint(i).getPointRef();
            int i5 = 1;
            while (i5 <= n) {
                double[] x2 = getPoint(i5).getPoint();
                int j7 = 0;
                while (j7 < n) {
                    int n2 = n;
                    PointValuePair reflected2 = reflected;
                    x2[j7] = xSmallest[j7] + (this.sigma * (x2[j7] - xSmallest[j7]));
                    j7++;
                    n = n2;
                    reflected = reflected2;
                }
                int n3 = n;
                PointValuePair reflected3 = reflected;
                setPoint(i5, new PointValuePair(x2, Double.NaN, false));
                i5++;
                n = n3;
                reflected = reflected3;
            }
            PointValuePair pointValuePair7 = reflected;
            evaluate(evaluationFunction, comparator);
        }
    }
}

package org.apache.commons.math3.stat.correlation;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

class StorelessBivariateCovariance {
    private boolean biasCorrected;
    private double covarianceNumerator;
    private double meanX;
    private double meanY;

    /* renamed from: n */
    private double f773n;

    StorelessBivariateCovariance() {
        this(true);
    }

    StorelessBivariateCovariance(boolean biasCorrection) {
        this.meanY = 0.0d;
        this.meanX = 0.0d;
        this.f773n = 0.0d;
        this.covarianceNumerator = 0.0d;
        this.biasCorrected = biasCorrection;
    }

    public void increment(double x, double y) {
        this.f773n += 1.0d;
        double deltaX = x - this.meanX;
        double deltaY = y - this.meanY;
        this.meanX += deltaX / this.f773n;
        this.meanY += deltaY / this.f773n;
        this.covarianceNumerator += ((this.f773n - 1.0d) / this.f773n) * deltaX * deltaY;
    }

    public void append(StorelessBivariateCovariance cov) {
        double oldN = this.f773n;
        this.f773n += cov.f773n;
        double deltaX = cov.meanX - this.meanX;
        double deltaY = cov.meanY - this.meanY;
        this.meanX += (cov.f773n * deltaX) / this.f773n;
        this.meanY += (cov.f773n * deltaY) / this.f773n;
        this.covarianceNumerator += cov.covarianceNumerator + (((cov.f773n * oldN) / this.f773n) * deltaX * deltaY);
    }

    public double getN() {
        return this.f773n;
    }

    public double getResult() throws NumberIsTooSmallException {
        if (this.f773n < 2.0d) {
            throw new NumberIsTooSmallException(LocalizedFormats.INSUFFICIENT_DIMENSION, Double.valueOf(this.f773n), Integer.valueOf(2), true);
        } else if (this.biasCorrected) {
            return this.covarianceNumerator / (this.f773n - 1.0d);
        } else {
            return this.covarianceNumerator / this.f773n;
        }
    }
}

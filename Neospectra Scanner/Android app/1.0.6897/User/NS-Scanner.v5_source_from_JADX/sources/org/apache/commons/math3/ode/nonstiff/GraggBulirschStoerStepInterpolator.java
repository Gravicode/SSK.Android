package org.apache.commons.math3.ode.nonstiff;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import org.apache.commons.math3.ode.EquationsMapper;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.util.FastMath;

class GraggBulirschStoerStepInterpolator extends AbstractStepInterpolator {
    private static final long serialVersionUID = 20110928;
    private int currentDegree;
    private double[] errfac;
    private double[][] polynomials;
    private double[] y0Dot;

    /* renamed from: y1 */
    private double[] f691y1;
    private double[] y1Dot;
    private double[][] yMidDots;

    public GraggBulirschStoerStepInterpolator() {
        this.y0Dot = null;
        this.f691y1 = null;
        this.y1Dot = null;
        this.yMidDots = null;
        resetTables(-1);
    }

    GraggBulirschStoerStepInterpolator(double[] y, double[] y0Dot2, double[] y1, double[] y1Dot2, double[][] yMidDots2, boolean forward, EquationsMapper primaryMapper, EquationsMapper[] secondaryMappers) {
        super(y, forward, primaryMapper, secondaryMappers);
        this.y0Dot = y0Dot2;
        this.f691y1 = y1;
        this.y1Dot = y1Dot2;
        this.yMidDots = yMidDots2;
        resetTables(yMidDots2.length + 4);
    }

    GraggBulirschStoerStepInterpolator(GraggBulirschStoerStepInterpolator interpolator) {
        super(interpolator);
        int dimension = this.currentState.length;
        this.y0Dot = null;
        this.f691y1 = null;
        this.y1Dot = null;
        double[][] dArr = null;
        this.yMidDots = dArr;
        if (interpolator.polynomials == null) {
            this.polynomials = dArr;
            this.currentDegree = -1;
            return;
        }
        resetTables(interpolator.currentDegree);
        for (int i = 0; i < this.polynomials.length; i++) {
            this.polynomials[i] = new double[dimension];
            System.arraycopy(interpolator.polynomials[i], 0, this.polynomials[i], 0, dimension);
        }
        this.currentDegree = interpolator.currentDegree;
    }

    private void resetTables(int maxDegree) {
        if (maxDegree < 0) {
            this.polynomials = null;
            this.errfac = null;
            this.currentDegree = -1;
            return;
        }
        double[][] newPols = new double[(maxDegree + 1)][];
        if (this.polynomials != null) {
            System.arraycopy(this.polynomials, 0, newPols, 0, this.polynomials.length);
            for (int i = this.polynomials.length; i < newPols.length; i++) {
                newPols[i] = new double[this.currentState.length];
            }
        } else {
            for (int i2 = 0; i2 < newPols.length; i2++) {
                newPols[i2] = new double[this.currentState.length];
            }
        }
        this.polynomials = newPols;
        if (maxDegree <= 4) {
            this.errfac = null;
        } else {
            this.errfac = new double[(maxDegree - 4)];
            for (int i3 = 0; i3 < this.errfac.length; i3++) {
                int ip5 = i3 + 5;
                this.errfac[i3] = 1.0d / ((double) (ip5 * ip5));
                double e = FastMath.sqrt(((double) (i3 + 1)) / ((double) ip5)) * 0.5d;
                for (int j = 0; j <= i3; j++) {
                    double[] dArr = this.errfac;
                    dArr[i3] = dArr[i3] * (e / ((double) (j + 1)));
                }
            }
        }
        this.currentDegree = 0;
    }

    /* access modifiers changed from: protected */
    public StepInterpolator doCopy() {
        return new GraggBulirschStoerStepInterpolator(this);
    }

    public void computeCoefficients(int mu, double h) {
        int i = mu;
        if (this.polynomials == null || this.polynomials.length <= i + 4) {
            resetTables(i + 4);
        }
        this.currentDegree = i + 4;
        char c = 0;
        int i2 = 0;
        while (i2 < this.currentState.length) {
            double yp0 = h * this.y0Dot[i2];
            double yp1 = h * this.y1Dot[i2];
            double ydiff = this.f691y1[i2] - this.currentState[i2];
            double aspl = ydiff - yp1;
            double bspl = yp0 - ydiff;
            this.polynomials[c][i2] = this.currentState[i2];
            this.polynomials[1][i2] = ydiff;
            this.polynomials[2][i2] = aspl;
            this.polynomials[3][i2] = bspl;
            if (i >= 0) {
                int j = 4;
                this.polynomials[4][i2] = (this.yMidDots[0][i2] - (((this.currentState[i2] + this.f691y1[i2]) * 0.5d) + ((aspl + bspl) * 0.125d))) * 16.0d;
                if (i > 0) {
                    this.polynomials[5][i2] = (this.yMidDots[1][i2] - (ydiff + ((aspl - bspl) * 0.25d))) * 16.0d;
                    if (i > 1) {
                        double ph2 = yp1 - yp0;
                        double d = yp0;
                        this.polynomials[6][i2] = ((this.yMidDots[2][i2] - ph2) + this.polynomials[4][i2]) * 16.0d;
                        if (i > 2) {
                            this.polynomials[7][i2] = ((this.yMidDots[3][i2] - ((bspl - aspl) * 6.0d)) + (this.polynomials[5][i2] * 3.0d)) * 16.0d;
                            while (true) {
                                int j2 = j;
                                if (j2 > i) {
                                    break;
                                }
                                double ph22 = ph2;
                                double fac1 = ((double) j2) * 0.5d * ((double) (j2 - 1));
                                double yp12 = yp1;
                                this.polynomials[j2 + 4][i2] = ((this.yMidDots[j2][i2] + (this.polynomials[j2 + 2][i2] * fac1)) - (this.polynomials[j2][i2] * (((2.0d * fac1) * ((double) (j2 - 2))) * ((double) (j2 - 3))))) * 16.0d;
                                j = j2 + 1;
                                ph2 = ph22;
                                yp1 = yp12;
                                i = mu;
                            }
                        }
                    }
                }
                i2++;
                i = mu;
                c = 0;
            } else {
                return;
            }
        }
    }

    public double estimateError(double[] scale) {
        double error = 0.0d;
        if (this.currentDegree < 5) {
            return 0.0d;
        }
        for (int i = 0; i < scale.length; i++) {
            double e = this.polynomials[this.currentDegree][i] / scale[i];
            error += e * e;
        }
        return FastMath.sqrt(error / ((double) scale.length)) * this.errfac[this.currentDegree - 5];
    }

    /* access modifiers changed from: protected */
    public void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) {
        double dot3;
        int dimension = this.currentState.length;
        double oneMinusTheta = 1.0d - theta;
        double theta05 = theta - 0.5d;
        double tOmT = theta * oneMinusTheta;
        double t4 = tOmT * tOmT;
        double t4Dot = tOmT * 2.0d * (1.0d - (theta * 2.0d));
        double dot1 = 1.0d / this.f701h;
        double dot2 = ((2.0d - (theta * 3.0d)) * theta) / this.f701h;
        double d = tOmT;
        double dot32 = ((((theta * 3.0d) - 4.0d) * theta) + 1.0d) / this.f701h;
        char c = 0;
        int i = 0;
        while (i < dimension) {
            int dimension2 = dimension;
            double p0 = this.polynomials[c][i];
            double p1 = this.polynomials[1][i];
            double p2 = this.polynomials[2][i];
            double p3 = this.polynomials[3][i];
            this.interpolatedState[i] = p0 + ((p1 + (((p2 * theta) + (p3 * oneMinusTheta)) * oneMinusTheta)) * theta);
            this.interpolatedDerivatives[i] = (dot1 * p1) + (dot2 * p2) + (dot32 * p3);
            if (this.currentDegree > 3) {
                double cDot = 0.0d;
                double c2 = this.polynomials[this.currentDegree][i];
                int j = this.currentDegree;
                while (true) {
                    j--;
                    if (j <= 3) {
                        break;
                    }
                    double d2 = 1.0d / ((double) (j - 3));
                    cDot = d2 * ((theta05 * cDot) + c2);
                    c2 = this.polynomials[j][i] + (c2 * d2 * theta05);
                }
                double[] dArr = this.interpolatedState;
                dArr[i] = dArr[i] + (t4 * c2);
                double[] dArr2 = this.interpolatedDerivatives;
                dot3 = dot32;
                dArr2[i] = dArr2[i] + (((t4 * cDot) + (t4Dot * c2)) / this.f701h);
            } else {
                dot3 = dot32;
            }
            i++;
            dimension = dimension2;
            dot32 = dot3;
            c = 0;
        }
        int dimension3 = dimension;
        double d3 = dot32;
        if (this.f701h == 0.0d) {
            System.arraycopy(this.yMidDots[1], 0, this.interpolatedDerivatives, 0, dimension3);
        } else {
            int i2 = dimension3;
        }
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        int dimension = this.currentState == null ? -1 : this.currentState.length;
        writeBaseExternal(out);
        out.writeInt(this.currentDegree);
        for (int k = 0; k <= this.currentDegree; k++) {
            for (int l = 0; l < dimension; l++) {
                out.writeDouble(this.polynomials[k][l]);
            }
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        double t = readBaseExternal(in);
        int dimension = this.currentState == null ? -1 : this.currentState.length;
        int degree = in.readInt();
        resetTables(degree);
        this.currentDegree = degree;
        for (int k = 0; k <= this.currentDegree; k++) {
            for (int l = 0; l < dimension; l++) {
                this.polynomials[k][l] = in.readDouble();
            }
        }
        setInterpolatedTime(t);
    }
}

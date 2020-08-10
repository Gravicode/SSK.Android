package org.apache.commons.math3.stat.regression;

import java.util.Arrays;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;

public class MillerUpdatingRegression implements UpdatingMultipleLinearRegression {

    /* renamed from: d */
    private final double[] f793d;
    private final double epsilon;
    private boolean hasIntercept;
    private final boolean[] lindep;
    private long nobs;
    private final int nvars;

    /* renamed from: r */
    private final double[] f794r;
    private final double[] rhs;
    private final double[] rss;
    private boolean rss_set;
    private double sserr;
    private double sumsqy;
    private double sumy;
    private final double[] tol;
    private boolean tol_set;
    private final int[] vorder;
    private final double[] work_sing;
    private final double[] work_tolset;
    private final double[] x_sing;

    private MillerUpdatingRegression() {
        this(-1, false, Double.NaN);
    }

    public MillerUpdatingRegression(int numberOfVariables, boolean includeConstant, double errorTolerance) throws ModelSpecificationException {
        this.nobs = 0;
        this.sserr = 0.0d;
        int i = 0;
        this.rss_set = false;
        this.tol_set = false;
        this.sumy = 0.0d;
        this.sumsqy = 0.0d;
        if (numberOfVariables < 1) {
            throw new ModelSpecificationException(LocalizedFormats.NO_REGRESSORS, new Object[0]);
        }
        if (includeConstant) {
            this.nvars = numberOfVariables + 1;
        } else {
            this.nvars = numberOfVariables;
        }
        this.hasIntercept = includeConstant;
        this.nobs = 0;
        this.f793d = new double[this.nvars];
        this.rhs = new double[this.nvars];
        this.f794r = new double[((this.nvars * (this.nvars - 1)) / 2)];
        this.tol = new double[this.nvars];
        this.rss = new double[this.nvars];
        this.vorder = new int[this.nvars];
        this.x_sing = new double[this.nvars];
        this.work_sing = new double[this.nvars];
        this.work_tolset = new double[this.nvars];
        this.lindep = new boolean[this.nvars];
        while (true) {
            int i2 = i;
            if (i2 >= this.nvars) {
                break;
            }
            this.vorder[i2] = i2;
            i = i2 + 1;
        }
        if (errorTolerance > 0.0d) {
            this.epsilon = errorTolerance;
        } else {
            this.epsilon = -errorTolerance;
        }
    }

    public MillerUpdatingRegression(int numberOfVariables, boolean includeConstant) throws ModelSpecificationException {
        this(numberOfVariables, includeConstant, Precision.EPSILON);
    }

    public boolean hasIntercept() {
        return this.hasIntercept;
    }

    public long getN() {
        return this.nobs;
    }

    public void addObservation(double[] x, double y) throws ModelSpecificationException {
        if ((this.hasIntercept || x.length == this.nvars) && (!this.hasIntercept || x.length + 1 == this.nvars)) {
            if (!this.hasIntercept) {
                include(MathArrays.copyOf(x, x.length), 1.0d, y);
            } else {
                double[] tmp = new double[(x.length + 1)];
                System.arraycopy(x, 0, tmp, 1, x.length);
                tmp[0] = 1.0d;
                include(tmp, 1.0d, y);
            }
            this.nobs++;
            return;
        }
        throw new ModelSpecificationException(LocalizedFormats.INVALID_REGRESSION_OBSERVATION, Integer.valueOf(x.length), Integer.valueOf(this.nvars));
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
        } else if (x.length == 0) {
            throw new ModelSpecificationException(LocalizedFormats.NO_DATA, new Object[0]);
        } else if (x[0].length + 1 > x.length) {
            throw new ModelSpecificationException(LocalizedFormats.NOT_ENOUGH_DATA_FOR_NUMBER_OF_PREDICTORS, Integer.valueOf(x.length), Integer.valueOf(x[0].length));
        } else {
            while (true) {
                int i2 = i;
                if (i2 < x.length) {
                    addObservation(x[i2], y[i2]);
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        }
    }

    private void include(double[] x, double wi, double yi) {
        int i;
        double dpi;
        double di;
        int i2;
        double[] dArr = x;
        double d = yi;
        int nextr = 0;
        double w = wi;
        double y = d;
        int i3 = 0;
        this.rss_set = false;
        this.sumy = smartAdd(d, this.sumy);
        this.sumsqy = smartAdd(this.sumsqy, d * d);
        while (i3 < dArr.length) {
            if (w != 0.0d) {
                double xi = dArr[i3];
                if (xi == 0.0d) {
                    nextr += (this.nvars - i3) - 1;
                    i = i3;
                } else {
                    double di2 = this.f793d[i3];
                    double wxi = w * xi;
                    double _w = w;
                    if (di2 != 0.0d) {
                        dpi = smartAdd(di2, wxi * xi);
                        if (FastMath.abs((wxi * xi) / di2) > Precision.EPSILON) {
                            w = (di2 * w) / dpi;
                        }
                    } else {
                        dpi = wxi * xi;
                        w = 0.0d;
                    }
                    this.f793d[i3] = dpi;
                    int k = i3 + 1;
                    while (k < this.nvars) {
                        double w2 = w;
                        double xk = dArr[k];
                        double y2 = y;
                        dArr[k] = smartAdd(xk, (-xi) * this.f794r[nextr]);
                        if (di2 != 0.0d) {
                            i2 = i3;
                            di = di2;
                            this.f794r[nextr] = smartAdd(di2 * this.f794r[nextr], (_w * xi) * xk) / dpi;
                        } else {
                            i2 = i3;
                            di = di2;
                            this.f794r[nextr] = xk / xi;
                        }
                        nextr++;
                        k++;
                        w = w2;
                        y = y2;
                        i3 = i2;
                        di2 = di;
                    }
                    double w3 = w;
                    i = i3;
                    double di3 = di2;
                    double xk2 = y;
                    y = smartAdd(xk2, (-xi) * this.rhs[i]);
                    if (di3 != 0.0d) {
                        this.rhs[i] = smartAdd(di3 * this.rhs[i], wxi * xk2) / dpi;
                    } else {
                        this.rhs[i] = xk2 / xi;
                    }
                    w = w3;
                }
                i3 = i + 1;
                double d2 = yi;
            } else {
                return;
            }
        }
        double y3 = y;
        this.sserr = smartAdd(this.sserr, w * y3 * y3);
    }

    private double smartAdd(double a, double b) {
        double _a = FastMath.abs(a);
        double _b = FastMath.abs(b);
        if (_a > _b) {
            if (_b > Precision.EPSILON * _a) {
                return a + b;
            }
            return a;
        } else if (_a > Precision.EPSILON * _b) {
            return a + b;
        } else {
            return b;
        }
    }

    public void clear() {
        Arrays.fill(this.f793d, 0.0d);
        Arrays.fill(this.rhs, 0.0d);
        Arrays.fill(this.f794r, 0.0d);
        Arrays.fill(this.tol, 0.0d);
        Arrays.fill(this.rss, 0.0d);
        Arrays.fill(this.work_tolset, 0.0d);
        Arrays.fill(this.work_sing, 0.0d);
        Arrays.fill(this.x_sing, 0.0d);
        Arrays.fill(this.lindep, false);
        for (int i = 0; i < this.nvars; i++) {
            this.vorder[i] = i;
        }
        this.nobs = 0;
        this.sserr = 0.0d;
        this.sumy = 0.0d;
        this.sumsqy = 0.0d;
        this.rss_set = false;
        this.tol_set = false;
    }

    private void tolset() {
        double eps = this.epsilon;
        for (int i = 0; i < this.nvars; i++) {
            this.work_tolset[i] = FastMath.sqrt(this.f793d[i]);
        }
        this.tol[0] = this.work_tolset[0] * eps;
        for (int col = 1; col < this.nvars; col++) {
            int pos = col - 1;
            double total = this.work_tolset[col];
            int pos2 = pos;
            for (int row = 0; row < col; row++) {
                total += FastMath.abs(this.f794r[pos2]) * this.work_tolset[row];
                pos2 += (this.nvars - row) - 2;
            }
            this.tol[col] = eps * total;
        }
        this.tol_set = true;
    }

    private double[] regcf(int nreq) throws ModelSpecificationException {
        if (nreq < 1) {
            throw new ModelSpecificationException(LocalizedFormats.NO_REGRESSORS, new Object[0]);
        } else if (nreq > this.nvars) {
            throw new ModelSpecificationException(LocalizedFormats.TOO_MANY_REGRESSORS, Integer.valueOf(nreq), Integer.valueOf(this.nvars));
        } else {
            if (!this.tol_set) {
                tolset();
            }
            double[] ret = new double[nreq];
            boolean rankProblem = false;
            for (int i = nreq - 1; i > -1; i--) {
                if (FastMath.sqrt(this.f793d[i]) < this.tol[i]) {
                    ret[i] = 0.0d;
                    this.f793d[i] = 0.0d;
                    rankProblem = true;
                } else {
                    ret[i] = this.rhs[i];
                    int nextr = ((((this.nvars + this.nvars) - i) - 1) * i) / 2;
                    for (int j = i + 1; j < nreq; j++) {
                        ret[i] = smartAdd(ret[i], (-this.f794r[nextr]) * ret[j]);
                        nextr++;
                    }
                }
            }
            if (rankProblem) {
                for (int i2 = 0; i2 < nreq; i2++) {
                    if (this.lindep[i2]) {
                        ret[i2] = Double.NaN;
                    }
                }
            }
            return ret;
        }
    }

    private void singcheck() {
        for (int i = 0; i < this.nvars; i++) {
            this.work_sing[i] = FastMath.sqrt(this.f793d[i]);
        }
        int col = 0;
        while (true) {
            int col2 = col;
            if (col2 < this.nvars) {
                double temp = this.tol[col2];
                int pos = col2 - 1;
                for (int row = 0; row < col2 - 1; row++) {
                    if (FastMath.abs(this.f794r[pos]) * this.work_sing[row] < temp) {
                        this.f794r[pos] = 0.0d;
                    }
                    pos += (this.nvars - row) - 2;
                }
                this.lindep[col2] = false;
                if (this.work_sing[col2] < temp) {
                    this.lindep[col2] = true;
                    if (col2 < this.nvars - 1) {
                        Arrays.fill(this.x_sing, 0.0d);
                        int _xi = col2 + 1;
                        int _pi = ((((this.nvars + this.nvars) - col2) - 1) * col2) / 2;
                        while (true) {
                            int _xi2 = _xi;
                            if (_xi2 >= this.nvars) {
                                break;
                            }
                            this.x_sing[_xi2] = this.f794r[_pi];
                            this.f794r[_pi] = 0.0d;
                            _xi = _xi2 + 1;
                            _pi++;
                        }
                        double y = this.rhs[col2];
                        double weight = this.f793d[col2];
                        this.f793d[col2] = 0.0d;
                        this.rhs[col2] = 0.0d;
                        include(this.x_sing, weight, y);
                    } else {
                        this.sserr += this.f793d[col2] * this.rhs[col2] * this.rhs[col2];
                    }
                }
                col = col2 + 1;
            } else {
                return;
            }
        }
    }

    /* renamed from: ss */
    private void m63ss() {
        double total = this.sserr;
        this.rss[this.nvars - 1] = this.sserr;
        for (int i = this.nvars - 1; i > 0; i--) {
            total += this.f793d[i] * this.rhs[i] * this.rhs[i];
            this.rss[i - 1] = total;
        }
        this.rss_set = true;
    }

    private double[] cov(int nreq) {
        double d;
        double rnk;
        double rnk2;
        double total;
        int i = nreq;
        if (this.nobs <= ((long) i)) {
            return null;
        }
        int row = 0;
        double rnk3 = 0.0d;
        int i2 = 0;
        while (true) {
            d = 1.0d;
            if (i2 >= i) {
                break;
            }
            if (!this.lindep[i2]) {
                rnk3 += 1.0d;
            }
            i2++;
        }
        double var = this.rss[i - 1] / (((double) this.nobs) - rnk3);
        double[] rinv = new double[(((i - 1) * i) / 2)];
        inverse(rinv, i);
        double[] covmat = new double[(((i + 1) * i) / 2)];
        Arrays.fill(covmat, Double.NaN);
        int start = 0;
        double total2 = 0.0d;
        while (row < i) {
            int pos2 = start;
            if (!this.lindep[row]) {
                double total3 = total2;
                int col = row;
                while (col < i) {
                    if (!this.lindep[col]) {
                        int pos1 = (start + col) - row;
                        if (row == col) {
                            rnk2 = rnk3;
                            total = d / this.f793d[col];
                        } else {
                            rnk2 = rnk3;
                            total = rinv[pos1 - 1] / this.f793d[col];
                        }
                        for (int k = col + 1; k < i; k++) {
                            if (!this.lindep[k]) {
                                total += (rinv[pos1] * rinv[pos2]) / this.f793d[k];
                            }
                            pos1++;
                            pos2++;
                        }
                        covmat[(((col + 1) * col) / 2) + row] = total * var;
                        total3 = total;
                    } else {
                        rnk2 = rnk3;
                        pos2 += (i - col) - 1;
                    }
                    col++;
                    rnk3 = rnk2;
                    d = 1.0d;
                }
                rnk = rnk3;
                total2 = total3;
            } else {
                rnk = rnk3;
            }
            start += (i - row) - 1;
            row++;
            rnk3 = rnk;
            d = 1.0d;
        }
        return covmat;
    }

    private void inverse(double[] rinv, int nreq) {
        int pos = (((nreq - 1) * nreq) / 2) - 1;
        int pos2 = -1;
        double total = 0.0d;
        Arrays.fill(rinv, Double.NaN);
        for (int row = nreq - 1; row > 0; row--) {
            if (!this.lindep[row]) {
                int start = ((row - 1) * ((this.nvars + this.nvars) - row)) / 2;
                double total2 = total;
                int pos22 = pos2;
                int pos3 = pos;
                int col = nreq;
                while (col > row) {
                    pos22 = pos3;
                    total2 = 0.0d;
                    int pos1 = start;
                    for (int k = row; k < col - 1; k++) {
                        pos22 += (nreq - k) - 1;
                        if (!this.lindep[k]) {
                            total2 += (-this.f794r[pos1]) * rinv[pos22];
                        }
                        pos1++;
                    }
                    rinv[pos3] = total2 - this.f794r[pos1];
                    pos3--;
                    col--;
                    int i = pos1;
                }
                pos = pos3;
                pos2 = pos22;
                total = total2;
            } else {
                pos -= nreq - row;
            }
        }
    }

    public double[] getPartialCorrelations(int in) {
        int i = in;
        double[] output = new double[((((this.nvars - i) + 1) * (this.nvars - i)) / 2)];
        int rms_off = -i;
        int wrk_off = -(i + 1);
        double[] rms = new double[(this.nvars - i)];
        double[] work = new double[((this.nvars - i) - 1)];
        int offXX = ((this.nvars - i) * ((this.nvars - i) - 1)) / 2;
        if (i < -1 || i >= this.nvars) {
            return null;
        }
        int nvm = this.nvars - 1;
        int base_pos = this.f794r.length - (((nvm - i) * ((nvm - i) + 1)) / 2);
        if (this.f793d[i] > 0.0d) {
            rms[i + rms_off] = 1.0d / FastMath.sqrt(this.f793d[i]);
        }
        for (int col = i + 1; col < this.nvars; col++) {
            int pos = ((base_pos + col) - 1) - i;
            double sumxx = this.f793d[col];
            int pos2 = pos;
            for (int row = i; row < col; row++) {
                sumxx += this.f793d[row] * this.f794r[pos2] * this.f794r[pos2];
                pos2 += (this.nvars - row) - 2;
            }
            if (sumxx > 0.0d) {
                rms[col + rms_off] = 1.0d / FastMath.sqrt(sumxx);
            } else {
                rms[col + rms_off] = 0.0d;
            }
        }
        double sumyy = this.sserr;
        for (int row2 = i; row2 < this.nvars; row2++) {
            sumyy += this.f793d[row2] * this.rhs[row2] * this.rhs[row2];
        }
        if (sumyy > 0.0d) {
            sumyy = 1.0d / FastMath.sqrt(sumyy);
        }
        int pos22 = 0;
        int col1 = i;
        while (col1 < this.nvars) {
            int pos3 = pos22;
            double sumxy = 0.0d;
            Arrays.fill(work, 0.0d);
            int pos1 = ((base_pos + col1) - i) - 1;
            for (int row3 = i; row3 < col1; row3++) {
                int pos23 = pos1 + 1;
                int col2 = col1 + 1;
                while (true) {
                    int col22 = col2;
                    if (col22 >= this.nvars) {
                        break;
                    }
                    int col23 = col22 + wrk_off;
                    work[col23] = work[col23] + (this.f793d[row3] * this.f794r[pos1] * this.f794r[pos23]);
                    pos23++;
                    col2 = col22 + 1;
                }
                sumxy += this.f793d[row3] * this.f794r[pos1] * this.rhs[row3];
                pos1 += (this.nvars - row3) - 2;
            }
            int pos24 = pos1 + 1;
            int col24 = col1 + 1;
            while (col24 < this.nvars) {
                int i2 = col24 + wrk_off;
                int nvm2 = nvm;
                work[i2] = work[i2] + (this.f793d[col1] * this.f794r[pos24]);
                pos24++;
                output[(((((col24 - 1) - i) * (col24 - i)) / 2) + col1) - i] = work[col24 + wrk_off] * rms[col1 + rms_off] * rms[col24 + rms_off];
                pos3++;
                col24++;
                nvm = nvm2;
            }
            int nvm3 = nvm;
            output[col1 + rms_off + offXX] = rms[col1 + rms_off] * (sumxy + (this.f793d[col1] * this.rhs[col1])) * sumyy;
            col1++;
            pos22 = pos3;
            nvm = nvm3;
        }
        int i3 = pos22;
        return output;
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x00fa  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void vmove(int r36, int r37) {
        /*
            r35 = this;
            r0 = r35
            r1 = r36
            r2 = r37
            r3 = 0
            if (r1 != r2) goto L_0x000a
            return
        L_0x000a:
            boolean r4 = r0.rss_set
            if (r4 != 0) goto L_0x0011
            r35.m63ss()
        L_0x0011:
            r4 = 0
            if (r1 >= r2) goto L_0x0019
            r5 = r1
            r6 = 1
            int r4 = r2 - r1
            goto L_0x001e
        L_0x0019:
            int r5 = r1 + -1
            r6 = -1
            int r4 = r1 - r2
        L_0x001e:
            r7 = r5
            r9 = r3
            r3 = 0
        L_0x0021:
            if (r3 >= r4) goto L_0x01ce
            int r10 = r0.nvars
            int r11 = r0.nvars
            int r10 = r10 + r11
            int r10 = r10 - r7
            int r10 = r10 + -1
            int r10 = r10 * r7
            int r10 = r10 / 2
            int r11 = r0.nvars
            int r11 = r11 + r10
            int r11 = r11 - r7
            int r11 = r11 + -1
            int r12 = r7 + 1
            double[] r13 = r0.f793d
            r14 = r13[r7]
            double[] r13 = r0.f793d
            r16 = r13[r12]
            r18 = r9
            double r8 = r0.epsilon
            int r8 = (r14 > r8 ? 1 : (r14 == r8 ? 0 : -1))
            if (r8 > 0) goto L_0x0056
            double r8 = r0.epsilon
            int r8 = (r16 > r8 ? 1 : (r16 == r8 ? 0 : -1))
            if (r8 <= 0) goto L_0x004e
            goto L_0x0056
        L_0x004e:
            r22 = r4
            r21 = r5
            r9 = r18
            goto L_0x0163
        L_0x0056:
            double[] r8 = r0.f794r
            r1 = r8[r10]
            double r8 = org.apache.commons.math3.util.FastMath.abs(r1)
            double r19 = org.apache.commons.math3.util.FastMath.sqrt(r14)
            double r8 = r8 * r19
            double[] r13 = r0.tol
            r19 = r13[r12]
            int r8 = (r8 > r19 ? 1 : (r8 == r19 ? 0 : -1))
            if (r8 >= 0) goto L_0x006e
            r1 = 0
        L_0x006e:
            double r8 = r0.epsilon
            int r8 = (r14 > r8 ? 1 : (r14 == r8 ? 0 : -1))
            if (r8 < 0) goto L_0x00b8
            double r8 = org.apache.commons.math3.util.FastMath.abs(r1)
            r22 = r4
            r21 = r5
            double r4 = r0.epsilon
            int r4 = (r8 > r4 ? 1 : (r8 == r4 ? 0 : -1))
            if (r4 >= 0) goto L_0x0083
            goto L_0x00bc
        L_0x0083:
            double r4 = r0.epsilon
            int r4 = (r16 > r4 ? 1 : (r16 == r4 ? 0 : -1))
            if (r4 >= 0) goto L_0x00b5
            double[] r4 = r0.f793d
            double r8 = r14 * r1
            double r8 = r8 * r1
            r4[r7] = r8
            double[] r4 = r0.f794r
            r8 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r8 = r8 / r1
            r4[r10] = r8
            int r4 = r10 + 1
        L_0x009a:
            int r5 = r0.nvars
            int r5 = r5 + r10
            int r5 = r5 - r7
            int r5 = r5 + -1
            if (r4 >= r5) goto L_0x00ac
            double[] r5 = r0.f794r
            r8 = r5[r4]
            double r8 = r8 / r1
            r5[r4] = r8
            int r4 = r4 + 1
            goto L_0x009a
        L_0x00ac:
            double[] r4 = r0.rhs
            r8 = r4[r7]
            double r8 = r8 / r1
            r4[r7] = r8
            r9 = 1
            goto L_0x00f8
        L_0x00b5:
            r9 = r18
            goto L_0x00f8
        L_0x00b8:
            r22 = r4
            r21 = r5
        L_0x00bc:
            double[] r4 = r0.f793d
            r4[r7] = r16
            double[] r4 = r0.f793d
            r4[r12] = r14
            double[] r4 = r0.f794r
            r8 = 0
            r4[r10] = r8
            int r4 = r7 + 2
        L_0x00cc:
            int r5 = r0.nvars
            if (r4 >= r5) goto L_0x00e7
            int r10 = r10 + 1
            double[] r5 = r0.f794r
            r1 = r5[r10]
            double[] r5 = r0.f794r
            double[] r8 = r0.f794r
            r19 = r8[r11]
            r5[r10] = r19
            double[] r5 = r0.f794r
            r5[r11] = r1
            int r11 = r11 + 1
            int r4 = r4 + 1
            goto L_0x00cc
        L_0x00e7:
            double[] r4 = r0.rhs
            r1 = r4[r7]
            double[] r4 = r0.rhs
            double[] r5 = r0.rhs
            r8 = r5[r12]
            r4[r7] = r8
            double[] r4 = r0.rhs
            r4[r12] = r1
            r9 = 1
        L_0x00f8:
            if (r9 != 0) goto L_0x0163
            double r4 = r14 * r1
            double r4 = r4 * r1
            double r4 = r16 + r4
            double r19 = r16 / r4
            double r23 = r1 * r14
            double r23 = r23 / r4
            double r25 = r14 * r19
            double[] r8 = r0.f793d
            r8[r7] = r4
            double[] r8 = r0.f793d
            r8[r12] = r25
            double[] r8 = r0.f794r
            r8[r10] = r23
            int r8 = r7 + 2
        L_0x0116:
            int r13 = r0.nvars
            if (r8 >= r13) goto L_0x0143
            int r10 = r10 + 1
            double[] r13 = r0.f794r
            r27 = r13[r10]
            double[] r13 = r0.f794r
            r29 = r4
            double[] r4 = r0.f794r
            r31 = r4[r11]
            double r31 = r31 * r19
            double r4 = r23 * r27
            double r31 = r31 + r4
            r13[r10] = r31
            double[] r4 = r0.f794r
            double[] r5 = r0.f794r
            r31 = r5[r11]
            double r31 = r31 * r1
            double r31 = r27 - r31
            r4[r11] = r31
            int r11 = r11 + 1
            int r8 = r8 + 1
            r4 = r29
            goto L_0x0116
        L_0x0143:
            r29 = r4
            double[] r4 = r0.rhs
            r27 = r4[r7]
            double[] r4 = r0.rhs
            double[] r5 = r0.rhs
            r31 = r5[r12]
            double r31 = r31 * r19
            double r33 = r23 * r27
            double r31 = r31 + r33
            r4[r7] = r31
            double[] r4 = r0.rhs
            double[] r5 = r0.rhs
            r31 = r5[r12]
            double r31 = r31 * r1
            double r31 = r27 - r31
            r4[r12] = r31
        L_0x0163:
            if (r7 <= 0) goto L_0x0187
            r1 = r7
            r2 = r1
            r1 = 0
        L_0x0168:
            if (r1 >= r7) goto L_0x0187
            double[] r4 = r0.f794r
            r19 = r4[r2]
            double[] r4 = r0.f794r
            double[] r5 = r0.f794r
            int r8 = r2 + -1
            r23 = r5[r8]
            r4[r2] = r23
            double[] r4 = r0.f794r
            int r5 = r2 + -1
            r4[r5] = r19
            int r4 = r0.nvars
            int r4 = r4 - r1
            int r4 = r4 + -2
            int r2 = r2 + r4
            int r1 = r1 + 1
            goto L_0x0168
        L_0x0187:
            int[] r1 = r0.vorder
            r1 = r1[r7]
            int[] r2 = r0.vorder
            int[] r4 = r0.vorder
            r4 = r4[r12]
            r2[r7] = r4
            int[] r2 = r0.vorder
            r2[r12] = r1
            double[] r2 = r0.tol
            r4 = r2[r7]
            double[] r2 = r0.tol
            double[] r8 = r0.tol
            r19 = r8[r12]
            r2[r7] = r19
            double[] r2 = r0.tol
            r2[r12] = r4
            double[] r2 = r0.rss
            double[] r8 = r0.rss
            r19 = r8[r12]
            double[] r8 = r0.f793d
            r23 = r8[r12]
            double[] r8 = r0.rhs
            r25 = r8[r12]
            double r23 = r23 * r25
            double[] r8 = r0.rhs
            r25 = r8[r12]
            double r23 = r23 * r25
            double r19 = r19 + r23
            r2[r7] = r19
            int r7 = r7 + r6
            int r3 = r3 + 1
            r5 = r21
            r4 = r22
            r1 = r36
            r2 = r37
            goto L_0x0021
        L_0x01ce:
            r22 = r4
            r21 = r5
            r18 = r9
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.stat.regression.MillerUpdatingRegression.vmove(int, int):void");
    }

    private int reorderRegressors(int[] list, int pos1) {
        if (list.length < 1 || list.length > (this.nvars + 1) - pos1) {
            return -1;
        }
        int i = pos1;
        int next = i;
        while (i < this.nvars) {
            int l = this.vorder[i];
            int j = 0;
            while (true) {
                if (j >= list.length) {
                    break;
                } else if (l != list[j] || i <= next) {
                    j++;
                } else {
                    vmove(i, next);
                    next++;
                    if (next >= list.length + pos1) {
                        return 0;
                    }
                }
            }
            i++;
        }
        return 0;
    }

    public double getDiagonalOfHatMatrix(double[] row_data) {
        double[] xrow;
        double[] dArr = row_data;
        double[] wk = new double[this.nvars];
        if (dArr.length > this.nvars) {
            return Double.NaN;
        }
        if (this.hasIntercept) {
            xrow = new double[(dArr.length + 1)];
            xrow[0] = 1.0d;
            System.arraycopy(dArr, 0, xrow, 1, dArr.length);
        } else {
            xrow = dArr;
        }
        double hii = 0.0d;
        for (int col = 0; col < xrow.length; col++) {
            if (FastMath.sqrt(this.f793d[col]) < this.tol[col]) {
                wk[col] = 0.0d;
            } else {
                int pos = col - 1;
                double total = xrow[col];
                int pos2 = pos;
                for (int row = 0; row < col; row++) {
                    total = smartAdd(total, (-wk[row]) * this.f794r[pos2]);
                    pos2 += (this.nvars - row) - 2;
                }
                wk[col] = total;
                hii = smartAdd(hii, (total * total) / this.f793d[col]);
            }
        }
        return hii;
    }

    public int[] getOrderOfRegressors() {
        return MathArrays.copyOf(this.vorder);
    }

    public RegressionResults regress() throws ModelSpecificationException {
        return regress(this.nvars);
    }

    public RegressionResults regress(int numberOfRegressors) throws ModelSpecificationException {
        int idx2;
        int i = numberOfRegressors;
        if (this.nobs <= ((long) i)) {
            throw new ModelSpecificationException(LocalizedFormats.NOT_ENOUGH_DATA_FOR_NUMBER_OF_PREDICTORS, Long.valueOf(this.nobs), Integer.valueOf(numberOfRegressors));
        } else if (i > this.nvars) {
            throw new ModelSpecificationException(LocalizedFormats.TOO_MANY_REGRESSORS, Integer.valueOf(numberOfRegressors), Integer.valueOf(this.nvars));
        } else {
            tolset();
            singcheck();
            double[] beta = regcf(numberOfRegressors);
            m63ss();
            double[] cov = cov(numberOfRegressors);
            int rnk = 0;
            for (boolean z : this.lindep) {
                if (!z) {
                    rnk++;
                }
            }
            boolean needsReorder = false;
            int i2 = 0;
            while (true) {
                if (i2 >= i) {
                    break;
                } else if (this.vorder[i2] != i2) {
                    needsReorder = true;
                    break;
                } else {
                    i2++;
                }
            }
            if (!needsReorder) {
                double[] dArr = beta;
                int i3 = rnk;
                RegressionResults regressionResults = new RegressionResults(dArr, new double[][]{cov}, true, this.nobs, i3, this.sumy, this.sumsqy, this.sserr, this.hasIntercept, false);
                return regressionResults;
            }
            double[] betaNew = new double[beta.length];
            double[] covNew = new double[cov.length];
            int[] newIndices = new int[beta.length];
            for (int i4 = 0; i4 < this.nvars; i4++) {
                for (int j = 0; j < i; j++) {
                    if (this.vorder[j] == i4) {
                        betaNew[i4] = beta[j];
                        newIndices[i4] = j;
                    }
                }
            }
            int idx1 = 0;
            for (int i5 = 0; i5 < beta.length; i5++) {
                int _i = newIndices[i5];
                int j2 = 0;
                while (j2 <= i5) {
                    int _j = newIndices[j2];
                    if (_i > _j) {
                        idx2 = (((_i + 1) * _i) / 2) + _j;
                    } else {
                        idx2 = (((_j + 1) * _j) / 2) + _i;
                    }
                    covNew[idx1] = cov[idx2];
                    j2++;
                    idx1++;
                }
            }
            double[][] dArr2 = {covNew};
            long j3 = this.nobs;
            int[] iArr = newIndices;
            double[] dArr3 = covNew;
            RegressionResults regressionResults2 = new RegressionResults(betaNew, dArr2, true, j3, rnk, this.sumy, this.sumsqy, this.sserr, this.hasIntercept, false);
            return regressionResults2;
        }
    }

    public RegressionResults regress(int[] variablesToInclude) throws ModelSpecificationException {
        int[] series;
        int idx2;
        int[] iArr = variablesToInclude;
        if (iArr.length > this.nvars) {
            throw new ModelSpecificationException(LocalizedFormats.TOO_MANY_REGRESSORS, Integer.valueOf(iArr.length), Integer.valueOf(this.nvars));
        } else if (this.nobs <= ((long) this.nvars)) {
            throw new ModelSpecificationException(LocalizedFormats.NOT_ENOUGH_DATA_FOR_NUMBER_OF_PREDICTORS, Long.valueOf(this.nobs), Integer.valueOf(this.nvars));
        } else {
            Arrays.sort(variablesToInclude);
            int iExclude = 0;
            for (int i = 0; i < iArr.length; i++) {
                if (i >= this.nvars) {
                    throw new ModelSpecificationException(LocalizedFormats.INDEX_LARGER_THAN_MAX, Integer.valueOf(i), Integer.valueOf(this.nvars));
                }
                if (i > 0 && iArr[i] == iArr[i - 1]) {
                    iArr[i] = -1;
                    iExclude++;
                }
            }
            if (iExclude > 0) {
                series = new int[(iArr.length - iExclude)];
                int j = 0;
                for (int i2 = 0; i2 < iArr.length; i2++) {
                    if (iArr[i2] > -1) {
                        series[j] = iArr[i2];
                        j++;
                    }
                }
            } else {
                series = iArr;
            }
            int[] series2 = series;
            reorderRegressors(series2, 0);
            tolset();
            singcheck();
            double[] beta = regcf(series2.length);
            m63ss();
            double[] cov = cov(series2.length);
            int rnk = 0;
            for (boolean z : this.lindep) {
                if (!z) {
                    rnk++;
                }
            }
            boolean needsReorder = false;
            int i3 = 0;
            while (true) {
                if (i3 >= this.nvars) {
                    break;
                } else if (this.vorder[i3] != series2[i3]) {
                    needsReorder = true;
                    break;
                } else {
                    i3++;
                }
            }
            if (!needsReorder) {
                double[][] dArr = {cov};
                long j2 = this.nobs;
                double[] dArr2 = cov;
                RegressionResults regressionResults = new RegressionResults(beta, dArr, true, j2, rnk, this.sumy, this.sumsqy, this.sserr, this.hasIntercept, false);
                return regressionResults;
            }
            double[] cov2 = cov;
            double[] betaNew = new double[beta.length];
            int[] newIndices = new int[beta.length];
            for (int i4 = 0; i4 < series2.length; i4++) {
                for (int j3 = 0; j3 < this.vorder.length; j3++) {
                    if (this.vorder[j3] == series2[i4]) {
                        betaNew[i4] = beta[j3];
                        newIndices[i4] = j3;
                    }
                }
            }
            double[] cov3 = cov2;
            double[] covNew = new double[cov3.length];
            int idx1 = 0;
            int i5 = 0;
            while (i5 < beta.length) {
                int _i = newIndices[i5];
                int idx12 = idx1;
                int j4 = 0;
                while (j4 <= i5) {
                    int _j = newIndices[j4];
                    if (_i > _j) {
                        idx2 = (((_i + 1) * _i) / 2) + _j;
                    } else {
                        idx2 = (((_j + 1) * _j) / 2) + _i;
                    }
                    covNew[idx12] = cov3[idx2];
                    j4++;
                    idx12++;
                }
                i5++;
                idx1 = idx12;
            }
            int[] iArr2 = series2;
            double[] dArr3 = beta;
            int[] iArr3 = newIndices;
            double[] dArr4 = cov3;
            RegressionResults regressionResults2 = new RegressionResults(betaNew, new double[][]{covNew}, true, this.nobs, rnk, this.sumy, this.sumsqy, this.sserr, this.hasIntercept, false);
            return regressionResults2;
        }
    }
}

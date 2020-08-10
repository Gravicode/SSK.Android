package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

class SchurTransformer {
    private static final int MAX_ITERATIONS = 100;
    private RealMatrix cachedP;
    private RealMatrix cachedPt;
    private RealMatrix cachedT;
    private final double epsilon = Precision.EPSILON;
    private final double[][] matrixP;
    private final double[][] matrixT;

    private static class ShiftInfo {
        double exShift;

        /* renamed from: w */
        double f620w;

        /* renamed from: x */
        double f621x;

        /* renamed from: y */
        double f622y;

        private ShiftInfo() {
        }
    }

    SchurTransformer(RealMatrix matrix) {
        if (!matrix.isSquare()) {
            throw new NonSquareMatrixException(matrix.getRowDimension(), matrix.getColumnDimension());
        }
        HessenbergTransformer transformer = new HessenbergTransformer(matrix);
        this.matrixT = transformer.getH().getData();
        this.matrixP = transformer.getP().getData();
        this.cachedT = null;
        this.cachedP = null;
        this.cachedPt = null;
        transform();
    }

    public RealMatrix getP() {
        if (this.cachedP == null) {
            this.cachedP = MatrixUtils.createRealMatrix(this.matrixP);
        }
        return this.cachedP;
    }

    public RealMatrix getPT() {
        if (this.cachedPt == null) {
            this.cachedPt = getP().transpose();
        }
        return this.cachedPt;
    }

    public RealMatrix getT() {
        if (this.cachedT == null) {
            this.cachedT = MatrixUtils.createRealMatrix(this.matrixT);
        }
        return this.cachedT;
    }

    private void transform() {
        int n;
        double norm;
        double z;
        int n2 = this.matrixT.length;
        double norm2 = getNorm();
        ShiftInfo shift = new ShiftInfo();
        int iteration = 0;
        int iu = n2 - 1;
        while (true) {
            int iu2 = iu;
            if (iu2 >= 0) {
                int il = findSmallSubDiagonalElement(iu2, norm2);
                if (il == iu2) {
                    double[] dArr = this.matrixT[iu2];
                    dArr[iu2] = dArr[iu2] + shift.exShift;
                    iu2--;
                    iteration = 0;
                    n = n2;
                    norm = norm2;
                } else if (il == iu2 - 1) {
                    double p = (this.matrixT[iu2 - 1][iu2 - 1] - this.matrixT[iu2][iu2]) / 2.0d;
                    double q = (p * p) + (this.matrixT[iu2][iu2 - 1] * this.matrixT[iu2 - 1][iu2]);
                    double[] dArr2 = this.matrixT[iu2];
                    double p2 = p;
                    dArr2[iu2] = dArr2[iu2] + shift.exShift;
                    double[] dArr3 = this.matrixT[iu2 - 1];
                    int i = iu2 - 1;
                    norm = norm2;
                    dArr3[i] = dArr3[i] + shift.exShift;
                    if (q >= 0.0d) {
                        double z2 = FastMath.sqrt(FastMath.abs(q));
                        if (p2 >= 0.0d) {
                            z = p2 + z2;
                        } else {
                            z = p2 - z2;
                        }
                        double x = this.matrixT[iu2][iu2 - 1];
                        double s = FastMath.abs(x) + FastMath.abs(z);
                        double p3 = x / s;
                        double q2 = z / s;
                        double d = s;
                        double r = FastMath.sqrt((p3 * p3) + (q2 * q2));
                        double p4 = p3 / r;
                        double q3 = q2 / r;
                        int j = iu2 - 1;
                        while (j < n2) {
                            double r2 = r;
                            double z3 = this.matrixT[iu2 - 1][j];
                            this.matrixT[iu2 - 1][j] = (q3 * z3) + (this.matrixT[iu2][j] * p4);
                            this.matrixT[iu2][j] = (this.matrixT[iu2][j] * q3) - (p4 * z3);
                            j++;
                            r = r2;
                        }
                        int i2 = 0;
                        while (i2 <= iu2) {
                            double z4 = this.matrixT[i2][iu2 - 1];
                            double x2 = x;
                            this.matrixT[i2][iu2 - 1] = (q3 * z4) + (this.matrixT[i2][iu2] * p4);
                            this.matrixT[i2][iu2] = (this.matrixT[i2][iu2] * q3) - (p4 * z4);
                            i2++;
                            x = x2;
                        }
                        int i3 = 0;
                        while (true) {
                            int i4 = i3;
                            if (i4 > n2 - 1) {
                                break;
                            }
                            double z5 = this.matrixP[i4][iu2 - 1];
                            int n3 = n2;
                            this.matrixP[i4][iu2 - 1] = (q3 * z5) + (this.matrixP[i4][iu2] * p4);
                            this.matrixP[i4][iu2] = (this.matrixP[i4][iu2] * q3) - (p4 * z5);
                            i3 = i4 + 1;
                            n2 = n3;
                        }
                        n = n2;
                    } else {
                        n = n2;
                        double d2 = p2;
                    }
                    iu2 -= 2;
                    iteration = 0;
                } else {
                    n = n2;
                    norm = norm2;
                    computeShift(il, iu2, iteration, shift);
                    int iteration2 = iteration + 1;
                    if (iteration2 > 100) {
                        throw new MaxCountExceededException(LocalizedFormats.CONVERGENCE_FAILED, Integer.valueOf(100), new Object[0]);
                    }
                    double[] hVec = new double[3];
                    performDoubleQRStep(il, initQRStep(il, iu2, shift, hVec), iu2, shift, hVec);
                    iteration = iteration2;
                }
                iu = iu2;
                norm2 = norm;
                n2 = n;
            } else {
                double d3 = norm2;
                return;
            }
        }
    }

    private double getNorm() {
        double norm = 0.0d;
        for (int i = 0; i < this.matrixT.length; i++) {
            for (int j = FastMath.max(i - 1, 0); j < this.matrixT.length; j++) {
                norm += FastMath.abs(this.matrixT[i][j]);
            }
        }
        return norm;
    }

    private int findSmallSubDiagonalElement(int startIdx, double norm) {
        int l = startIdx;
        while (l > 0) {
            double s = FastMath.abs(this.matrixT[l - 1][l - 1]) + FastMath.abs(this.matrixT[l][l]);
            if (s == 0.0d) {
                s = norm;
            }
            if (FastMath.abs(this.matrixT[l][l - 1]) < this.epsilon * s) {
                break;
            }
            l--;
        }
        return l;
    }

    private void computeShift(int l, int idx, int iteration, ShiftInfo shift) {
        int i = idx;
        int i2 = iteration;
        ShiftInfo shiftInfo = shift;
        shiftInfo.f621x = this.matrixT[i][i];
        shiftInfo.f620w = 0.0d;
        shiftInfo.f622y = 0.0d;
        if (l < i) {
            shiftInfo.f622y = this.matrixT[i - 1][i - 1];
            shiftInfo.f620w = this.matrixT[i][i - 1] * this.matrixT[i - 1][i];
        }
        if (i2 == 10) {
            shiftInfo.exShift += shiftInfo.f621x;
            for (int i3 = 0; i3 <= i; i3++) {
                double[] dArr = this.matrixT[i3];
                dArr[i3] = dArr[i3] - shiftInfo.f621x;
            }
            double s = FastMath.abs(this.matrixT[i][i - 1]) + FastMath.abs(this.matrixT[i - 1][i - 2]);
            shiftInfo.f621x = s * 0.75d;
            shiftInfo.f622y = 0.75d * s;
            shiftInfo.f620w = -0.4375d * s * s;
        }
        if (i2 == 30) {
            double s2 = (shiftInfo.f622y - shiftInfo.f621x) / 2.0d;
            double d = s2;
            double s3 = (s2 * s2) + shiftInfo.f620w;
            if (s3 > 0.0d) {
                double s4 = FastMath.sqrt(s3);
                if (shiftInfo.f622y < shiftInfo.f621x) {
                    s4 = -s4;
                }
                double s5 = shiftInfo.f621x - (shiftInfo.f620w / (((shiftInfo.f622y - shiftInfo.f621x) / 2.0d) + s4));
                int i4 = 0;
                while (true) {
                    int i5 = i4;
                    if (i5 <= idx) {
                        double[] dArr2 = this.matrixT[i5];
                        dArr2[i5] = dArr2[i5] - s5;
                        i4 = i5 + 1;
                    } else {
                        shiftInfo.exShift += s5;
                        shiftInfo.f620w = 0.964d;
                        shiftInfo.f622y = 0.964d;
                        shiftInfo.f621x = 0.964d;
                        return;
                    }
                }
            }
        }
    }

    private int initQRStep(int il, int iu, ShiftInfo shift, double[] hVec) {
        int i = il;
        ShiftInfo shiftInfo = shift;
        int im = iu - 2;
        while (im >= i) {
            double z = this.matrixT[im][im];
            double r = shiftInfo.f621x - z;
            double s = shiftInfo.f622y - z;
            double s2 = s;
            hVec[0] = (((r * s) - shiftInfo.f620w) / this.matrixT[im + 1][im]) + this.matrixT[im][im + 1];
            hVec[1] = ((this.matrixT[im + 1][im + 1] - z) - r) - s2;
            hVec[2] = this.matrixT[im + 2][im + 1];
            if (im == i) {
                break;
            }
            double z2 = z;
            double d = z2;
            if (FastMath.abs(this.matrixT[im][im - 1]) * (FastMath.abs(hVec[1]) + FastMath.abs(hVec[2])) < this.epsilon * FastMath.abs(hVec[0]) * (FastMath.abs(this.matrixT[im - 1][im - 1]) + FastMath.abs(z2) + FastMath.abs(this.matrixT[im + 1][im + 1]))) {
                break;
            }
            im--;
            i = il;
            shiftInfo = shift;
        }
        return im;
    }

    private void performDoubleQRStep(int il, int im, int iu, ShiftInfo shift, double[] hVec) {
        int n;
        int n2;
        double q;
        double r;
        int n3;
        int i = im;
        int i2 = iu;
        ShiftInfo shiftInfo = shift;
        int n4 = this.matrixT.length;
        double p = hVec[0];
        double q2 = hVec[1];
        double r2 = hVec[2];
        double q3 = q2;
        double p2 = p;
        int k = i;
        while (k <= i2 - 1) {
            boolean notlast = k != i2 + -1;
            if (k != i) {
                double p3 = this.matrixT[k][k - 1];
                q3 = this.matrixT[k + 1][k - 1];
                r2 = notlast ? this.matrixT[k + 2][k - 1] : 0.0d;
                double p4 = p3;
                shiftInfo.f621x = FastMath.abs(p3) + FastMath.abs(q3) + FastMath.abs(r2);
                n2 = n4;
                if (Precision.equals(shiftInfo.f621x, 0.0d, this.epsilon)) {
                    int i3 = il;
                    p2 = p4;
                    n = n2;
                    k++;
                    n4 = n;
                } else {
                    p2 = p4 / shiftInfo.f621x;
                    q3 /= shiftInfo.f621x;
                    r2 /= shiftInfo.f621x;
                }
            } else {
                n2 = n4;
            }
            double s = FastMath.sqrt((p2 * p2) + (q3 * q3) + (r2 * r2));
            if (p2 < 0.0d) {
                s = -s;
            }
            if (s != 0.0d) {
                if (k != i) {
                    r = r2;
                    q = q3;
                    this.matrixT[k][k - 1] = (-s) * shiftInfo.f621x;
                    int i4 = il;
                } else {
                    q = q3;
                    r = r2;
                    if (il != i) {
                        this.matrixT[k][k - 1] = -this.matrixT[k][k - 1];
                    }
                }
                double p5 = p2 + s;
                shiftInfo.f621x = p5 / s;
                shiftInfo.f622y = q / s;
                double z = r / s;
                double q4 = q / p5;
                double r3 = r / p5;
                int j = k;
                while (true) {
                    double s2 = s;
                    n3 = n2;
                    if (j >= n3) {
                        break;
                    }
                    double p6 = this.matrixT[k][j] + (this.matrixT[k + 1][j] * q4);
                    if (notlast) {
                        p6 += this.matrixT[k + 2][j] * r3;
                        double[] dArr = this.matrixT[k + 2];
                        dArr[j] = dArr[j] - (p6 * z);
                    }
                    p5 = p6;
                    double[] dArr2 = this.matrixT[k];
                    double q5 = q4;
                    dArr2[j] = dArr2[j] - (shiftInfo.f621x * p5);
                    double[] dArr3 = this.matrixT[k + 1];
                    double z2 = z;
                    dArr3[j] = dArr3[j] - (shiftInfo.f622y * p5);
                    j++;
                    n2 = n3;
                    s = s2;
                    q4 = q5;
                    z = z2;
                }
                double q6 = q4;
                double z3 = z;
                for (int i5 = 0; i5 <= FastMath.min(i2, k + 3); i5++) {
                    double p7 = (shiftInfo.f621x * this.matrixT[i5][k]) + (shiftInfo.f622y * this.matrixT[i5][k + 1]);
                    if (notlast) {
                        p7 += z3 * this.matrixT[i5][k + 2];
                        double[] dArr4 = this.matrixT[i5];
                        int i6 = k + 2;
                        dArr4[i6] = dArr4[i6] - (p7 * r3);
                    }
                    p5 = p7;
                    double[] dArr5 = this.matrixT[i5];
                    dArr5[k] = dArr5[k] - p5;
                    double[] dArr6 = this.matrixT[i5];
                    int i7 = k + 1;
                    dArr6[i7] = dArr6[i7] - (p5 * q6);
                }
                int high = this.matrixT.length - 1;
                double p8 = p5;
                int i8 = 0;
                while (i8 <= high) {
                    int n5 = n3;
                    int high2 = high;
                    double p9 = (shiftInfo.f621x * this.matrixP[i8][k]) + (shiftInfo.f622y * this.matrixP[i8][k + 1]);
                    if (notlast) {
                        p9 += z3 * this.matrixP[i8][k + 2];
                        double[] dArr7 = this.matrixP[i8];
                        int i9 = k + 2;
                        dArr7[i9] = dArr7[i9] - (p9 * r3);
                    }
                    p8 = p9;
                    double[] dArr8 = this.matrixP[i8];
                    dArr8[k] = dArr8[k] - p8;
                    double[] dArr9 = this.matrixP[i8];
                    int i10 = k + 1;
                    dArr9[i10] = dArr9[i10] - (p8 * q6);
                    i8++;
                    n3 = n5;
                    high = high2;
                }
                n = n3;
                p2 = p8;
                r2 = r3;
                q3 = q6;
            } else {
                int i11 = il;
                double d = q3;
                double d2 = r2;
                n = n2;
            }
            k++;
            n4 = n;
        }
        int i12 = il;
        int i13 = n4;
        for (int i14 = i + 2; i14 <= i2; i14++) {
            this.matrixT[i14][i14 - 2] = 0.0d;
            if (i14 > i + 2) {
                this.matrixT[i14][i14 - 3] = 0.0d;
            }
        }
    }
}

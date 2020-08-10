package Jama;

import Jama.util.Maths;
import java.io.Serializable;
import java.lang.reflect.Array;

public class EigenvalueDecomposition implements Serializable {
    private static final long serialVersionUID = 1;

    /* renamed from: H */
    private double[][] f2H;

    /* renamed from: V */
    private double[][] f3V = ((double[][]) Array.newInstance(double.class, new int[]{this.f6n, this.f6n}));
    private transient double cdivi;
    private transient double cdivr;

    /* renamed from: d */
    private double[] f4d = new double[this.f6n];

    /* renamed from: e */
    private double[] f5e = new double[this.f6n];
    private boolean issymmetric = true;

    /* renamed from: n */
    private int f6n;
    private double[] ort;

    private void tred2() {
        for (int i = 0; i < this.f6n; i++) {
            this.f4d[i] = this.f3V[this.f6n - 1][i];
        }
        int i2 = this.f6n;
        while (true) {
            i2--;
            double d = 0.0d;
            if (i2 <= 0) {
                break;
            }
            double d2 = 0.0d;
            for (int i3 = 0; i3 < i2; i3++) {
                d2 += Math.abs(this.f4d[i3]);
            }
            if (d2 == 0.0d) {
                int i4 = i2 - 1;
                this.f5e[i2] = this.f4d[i4];
                for (int i5 = 0; i5 < i2; i5++) {
                    this.f4d[i5] = this.f3V[i4][i5];
                    this.f3V[i2][i5] = 0.0d;
                    this.f3V[i5][i2] = 0.0d;
                }
            } else {
                double d3 = 0.0d;
                for (int i6 = 0; i6 < i2; i6++) {
                    double[] dArr = this.f4d;
                    dArr[i6] = dArr[i6] / d2;
                    d3 += this.f4d[i6] * this.f4d[i6];
                }
                int i7 = i2 - 1;
                double d4 = this.f4d[i7];
                double sqrt = Math.sqrt(d3);
                if (d4 > 0.0d) {
                    sqrt = -sqrt;
                }
                this.f5e[i2] = d2 * sqrt;
                double d5 = d3 - (d4 * sqrt);
                this.f4d[i7] = d4 - sqrt;
                for (int i8 = 0; i8 < i2; i8++) {
                    this.f5e[i8] = 0.0d;
                }
                int i9 = 0;
                while (i9 < i2) {
                    double d6 = this.f4d[i9];
                    this.f3V[i9][i2] = d6;
                    double d7 = this.f5e[i9] + (this.f3V[i9][i9] * d6);
                    int i10 = i9 + 1;
                    for (int i11 = i10; i11 <= i7; i11++) {
                        d7 += this.f3V[i11][i9] * this.f4d[i11];
                        double[] dArr2 = this.f5e;
                        dArr2[i11] = dArr2[i11] + (this.f3V[i11][i9] * d6);
                    }
                    this.f5e[i9] = d7;
                    i9 = i10;
                }
                double d8 = 0.0d;
                for (int i12 = 0; i12 < i2; i12++) {
                    double[] dArr3 = this.f5e;
                    dArr3[i12] = dArr3[i12] / d5;
                    d8 += this.f5e[i12] * this.f4d[i12];
                }
                double d9 = d8 / (d5 + d5);
                for (int i13 = 0; i13 < i2; i13++) {
                    double[] dArr4 = this.f5e;
                    dArr4[i13] = dArr4[i13] - (this.f4d[i13] * d9);
                }
                for (int i14 = 0; i14 < i2; i14++) {
                    double d10 = this.f4d[i14];
                    double d11 = this.f5e[i14];
                    for (int i15 = i14; i15 <= i7; i15++) {
                        double[] dArr5 = this.f3V[i15];
                        dArr5[i14] = dArr5[i14] - ((this.f5e[i15] * d10) + (this.f4d[i15] * d11));
                    }
                    this.f4d[i14] = this.f3V[i7][i14];
                    this.f3V[i2][i14] = 0.0d;
                }
                d = d5;
            }
            this.f4d[i2] = d;
        }
        int i16 = 0;
        while (i16 < this.f6n - 1) {
            this.f3V[this.f6n - 1][i16] = this.f3V[i16][i16];
            this.f3V[i16][i16] = 1.0d;
            int i17 = i16 + 1;
            double d12 = this.f4d[i17];
            if (d12 != 0.0d) {
                for (int i18 = 0; i18 <= i16; i18++) {
                    this.f4d[i18] = this.f3V[i18][i17] / d12;
                }
                for (int i19 = 0; i19 <= i16; i19++) {
                    double d13 = 0.0d;
                    for (int i20 = 0; i20 <= i16; i20++) {
                        d13 += this.f3V[i20][i17] * this.f3V[i20][i19];
                    }
                    for (int i21 = 0; i21 <= i16; i21++) {
                        double[] dArr6 = this.f3V[i21];
                        dArr6[i19] = dArr6[i19] - (this.f4d[i21] * d13);
                    }
                }
            }
            for (int i22 = 0; i22 <= i16; i22++) {
                this.f3V[i22][i17] = 0.0d;
            }
            i16 = i17;
        }
        for (int i23 = 0; i23 < this.f6n; i23++) {
            this.f4d[i23] = this.f3V[this.f6n - 1][i23];
            this.f3V[this.f6n - 1][i23] = 0.0d;
        }
        this.f3V[this.f6n - 1][this.f6n - 1] = 1.0d;
        this.f5e[0] = 0.0d;
    }

    private void tql2() {
        double d;
        double d2;
        long j;
        double d3;
        for (int i = 1; i < this.f6n; i++) {
            this.f5e[i - 1] = this.f5e[i];
        }
        this.f5e[this.f6n - 1] = 0.0d;
        double pow = Math.pow(2.0d, -52.0d);
        double d4 = 0.0d;
        double d5 = 0.0d;
        int i2 = 0;
        while (i2 < this.f6n) {
            double max = Math.max(d4, Math.abs(this.f4d[i2]) + Math.abs(this.f5e[i2]));
            int i3 = i2;
            while (i3 < this.f6n && Math.abs(this.f5e[i3]) > pow * max) {
                i3++;
            }
            if (i3 > i2) {
                while (true) {
                    double d6 = this.f4d[i2];
                    int i4 = i2 + 1;
                    j = 4611686018427387904L;
                    d2 = pow;
                    double d7 = (this.f4d[i4] - d6) / (this.f5e[i2] * 2.0d);
                    int i5 = i2;
                    d = max;
                    double hypot = Maths.hypot(d7, 1.0d);
                    if (d7 < 0.0d) {
                        hypot = -hypot;
                    }
                    double d8 = d7 + hypot;
                    this.f4d[i5] = this.f5e[i5] / d8;
                    this.f4d[i4] = this.f5e[i5] * d8;
                    double d9 = this.f4d[i4];
                    double d10 = d6 - this.f4d[i5];
                    for (int i6 = i5 + 2; i6 < this.f6n; i6++) {
                        double[] dArr = this.f4d;
                        dArr[i6] = dArr[i6] - d10;
                    }
                    double d11 = d5 + d10;
                    double d12 = this.f4d[i3];
                    double d13 = this.f5e[i4];
                    int i7 = i3 - 1;
                    double d14 = 1.0d;
                    double d15 = 1.0d;
                    d3 = d11;
                    double d16 = 0.0d;
                    double d17 = d12;
                    double d18 = 1.0d;
                    double d19 = 0.0d;
                    while (true) {
                        i2 = i5;
                        if (i7 < i2) {
                            break;
                        }
                        double d20 = this.f5e[i7] * d18;
                        double d21 = d18 * d17;
                        int i8 = i3;
                        double d22 = d18;
                        double hypot2 = Maths.hypot(d17, this.f5e[i7]);
                        int i9 = i7 + 1;
                        this.f5e[i9] = d19 * hypot2;
                        double d23 = this.f5e[i7] / hypot2;
                        d18 = d17 / hypot2;
                        d17 = (this.f4d[i7] * d18) - (d23 * d20);
                        this.f4d[i9] = d21 + (((d20 * d18) + (this.f4d[i7] * d23)) * d23);
                        int i10 = 0;
                        while (i10 < this.f6n) {
                            double d24 = this.f3V[i10][i9];
                            double d25 = d17;
                            this.f3V[i10][i9] = (this.f3V[i10][i7] * d23) + (d18 * d24);
                            this.f3V[i10][i7] = (this.f3V[i10][i7] * d18) - (d24 * d23);
                            i10++;
                            d17 = d25;
                        }
                        double d26 = d17;
                        i7--;
                        d16 = d19;
                        i5 = i2;
                        d14 = d15;
                        i3 = i8;
                        d15 = d22;
                        d19 = d23;
                    }
                    int i11 = i3;
                    double d27 = d18;
                    double d28 = (((((-d19) * d16) * d14) * d13) * this.f5e[i2]) / d9;
                    this.f5e[i2] = d19 * d28;
                    this.f4d[i2] = d27 * d28;
                    if (Math.abs(this.f5e[i2]) <= d2 * d) {
                        break;
                    }
                    pow = d2;
                    max = d;
                    d5 = d3;
                    i3 = i11;
                }
                d5 = d3;
            } else {
                d2 = pow;
                d = max;
                j = 4611686018427387904L;
            }
            this.f4d[i2] = this.f4d[i2] + d5;
            this.f5e[i2] = 0.0d;
            i2++;
            long j2 = j;
            pow = d2;
            d4 = d;
        }
        int i12 = 0;
        while (i12 < this.f6n - 1) {
            int i13 = i12 + 1;
            int i14 = i12;
            double d29 = this.f4d[i12];
            for (int i15 = i13; i15 < this.f6n; i15++) {
                if (this.f4d[i15] < d29) {
                    d29 = this.f4d[i15];
                    i14 = i15;
                }
            }
            if (i14 != i12) {
                this.f4d[i14] = this.f4d[i12];
                this.f4d[i12] = d29;
                for (int i16 = 0; i16 < this.f6n; i16++) {
                    double d30 = this.f3V[i16][i12];
                    this.f3V[i16][i12] = this.f3V[i16][i14];
                    this.f3V[i16][i14] = d30;
                }
            }
            i12 = i13;
        }
    }

    private void orthes() {
        int i;
        int i2 = this.f6n - 1;
        int i3 = 1;
        while (true) {
            i = i2 - 1;
            double d = 0.0d;
            if (i3 > i) {
                break;
            }
            double d2 = 0.0d;
            for (int i4 = i3; i4 <= i2; i4++) {
                d2 += Math.abs(this.f2H[i4][i3 - 1]);
            }
            if (d2 != 0.0d) {
                double d3 = 0.0d;
                for (int i5 = i2; i5 >= i3; i5--) {
                    this.ort[i5] = this.f2H[i5][i3 - 1] / d2;
                    d3 += this.ort[i5] * this.ort[i5];
                }
                double sqrt = Math.sqrt(d3);
                if (this.ort[i3] > 0.0d) {
                    sqrt = -sqrt;
                }
                double d4 = d3 - (this.ort[i3] * sqrt);
                this.ort[i3] = this.ort[i3] - sqrt;
                int i6 = i3;
                while (i6 < this.f6n) {
                    double d5 = d;
                    for (int i7 = i2; i7 >= i3; i7--) {
                        d5 += this.ort[i7] * this.f2H[i7][i6];
                    }
                    double d6 = d5 / d4;
                    for (int i8 = i3; i8 <= i2; i8++) {
                        double[] dArr = this.f2H[i8];
                        dArr[i6] = dArr[i6] - (this.ort[i8] * d6);
                    }
                    i6++;
                    d = 0.0d;
                }
                for (int i9 = 0; i9 <= i2; i9++) {
                    double d7 = 0.0d;
                    for (int i10 = i2; i10 >= i3; i10--) {
                        d7 += this.ort[i10] * this.f2H[i9][i10];
                    }
                    double d8 = d7 / d4;
                    for (int i11 = i3; i11 <= i2; i11++) {
                        double[] dArr2 = this.f2H[i9];
                        dArr2[i11] = dArr2[i11] - (this.ort[i11] * d8);
                    }
                }
                this.ort[i3] = this.ort[i3] * d2;
                this.f2H[i3][i3 - 1] = d2 * sqrt;
            }
            i3++;
        }
        int i12 = 0;
        while (i12 < this.f6n) {
            int i13 = 0;
            while (i13 < this.f6n) {
                this.f3V[i12][i13] = i12 == i13 ? 1.0d : 0.0d;
                i13++;
            }
            i12++;
        }
        while (i >= 1) {
            int i14 = i - 1;
            if (this.f2H[i][i14] != 0.0d) {
                for (int i15 = i + 1; i15 <= i2; i15++) {
                    this.ort[i15] = this.f2H[i15][i14];
                }
                for (int i16 = i; i16 <= i2; i16++) {
                    double d9 = 0.0d;
                    for (int i17 = i; i17 <= i2; i17++) {
                        d9 += this.ort[i17] * this.f3V[i17][i16];
                    }
                    double d10 = (d9 / this.ort[i]) / this.f2H[i][i14];
                    for (int i18 = i; i18 <= i2; i18++) {
                        double[] dArr3 = this.f3V[i18];
                        dArr3[i16] = dArr3[i16] + (this.ort[i18] * d10);
                    }
                }
            }
            i--;
        }
    }

    private void cdiv(double d, double d2, double d3, double d4) {
        if (Math.abs(d3) > Math.abs(d4)) {
            double d5 = d4 / d3;
            double d6 = d3 + (d4 * d5);
            this.cdivr = ((d5 * d2) + d) / d6;
            this.cdivi = (d2 - (d5 * d)) / d6;
            return;
        }
        double d7 = d3 / d4;
        double d8 = d4 + (d3 * d7);
        this.cdivr = ((d7 * d) + d2) / d8;
        this.cdivi = ((d7 * d2) - d) / d8;
    }

    private void hqr2() {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        double d;
        double d2;
        int i6;
        int i7;
        int i8;
        double d3;
        int i9;
        double d4;
        double d5;
        double d6;
        double d7;
        int i10;
        double d8;
        int i11;
        int i12;
        double d9;
        double d10;
        int i13;
        int i14;
        double d11;
        int i15;
        double d12;
        double d13;
        double d14;
        int i16;
        int i17;
        int i18;
        int i19;
        int i20;
        double d15;
        boolean z;
        int i21;
        double d16;
        double d17;
        double d18;
        double d19;
        double d20;
        double d21;
        EigenvalueDecomposition eigenvalueDecomposition = this;
        int i22 = eigenvalueDecomposition.f6n;
        int i23 = i22 - 1;
        double pow = Math.pow(2.0d, -52.0d);
        double d22 = 0.0d;
        int i24 = 0;
        while (true) {
            boolean z2 = true;
            if (i24 >= i22) {
                break;
            }
            boolean z3 = i24 < 0;
            if (i24 <= i23) {
                z2 = false;
            }
            if (z3 || z2) {
                eigenvalueDecomposition.f4d[i24] = eigenvalueDecomposition.f2H[i24][i24];
                eigenvalueDecomposition.f5e[i24] = 0.0d;
            }
            for (int max = Math.max(i24 - 1, 0); max < i22; max++) {
                d22 += Math.abs(eigenvalueDecomposition.f2H[i24][max]);
            }
            i24++;
        }
        int i25 = i23;
        double d23 = 0.0d;
        double d24 = 0.0d;
        double d25 = 0.0d;
        double d26 = 0.0d;
        double d27 = 0.0d;
        double d28 = 0.0d;
        int i26 = 0;
        while (i25 >= 0) {
            double d29 = d24;
            int i27 = i25;
            while (true) {
                if (i27 <= 0) {
                    break;
                }
                int i28 = i27 - 1;
                double abs = Math.abs(eigenvalueDecomposition.f2H[i28][i28]) + Math.abs(eigenvalueDecomposition.f2H[i27][i27]);
                if (abs == 0.0d) {
                    abs = d22;
                }
                if (Math.abs(eigenvalueDecomposition.f2H[i27][i28]) < pow * abs) {
                    d29 = abs;
                    break;
                } else {
                    i27--;
                    d29 = abs;
                }
            }
            if (i27 == i25) {
                eigenvalueDecomposition.f2H[i25][i25] = eigenvalueDecomposition.f2H[i25][i25] + d23;
                eigenvalueDecomposition.f4d[i25] = eigenvalueDecomposition.f2H[i25][i25];
                eigenvalueDecomposition.f5e[i25] = 0.0d;
                i25--;
                d24 = d29;
                i11 = i22;
                i12 = i23;
                d8 = pow;
            } else {
                int i29 = i25 - 1;
                if (i27 == i29) {
                    double d30 = eigenvalueDecomposition.f2H[i25][i29] * eigenvalueDecomposition.f2H[i29][i25];
                    double d31 = (eigenvalueDecomposition.f2H[i29][i29] - eigenvalueDecomposition.f2H[i25][i25]) / 2.0d;
                    double d32 = (d31 * d31) + d30;
                    double d33 = d29;
                    double sqrt = Math.sqrt(Math.abs(d32));
                    d8 = pow;
                    eigenvalueDecomposition.f2H[i25][i25] = eigenvalueDecomposition.f2H[i25][i25] + d23;
                    eigenvalueDecomposition.f2H[i29][i29] = eigenvalueDecomposition.f2H[i29][i29] + d23;
                    double d34 = eigenvalueDecomposition.f2H[i25][i25];
                    if (d32 >= 0.0d) {
                        if (d31 >= 0.0d) {
                            d21 = d31 + sqrt;
                        } else {
                            d21 = d31 - sqrt;
                        }
                        double d35 = d21;
                        eigenvalueDecomposition.f4d[i29] = d34 + d35;
                        eigenvalueDecomposition.f4d[i25] = eigenvalueDecomposition.f4d[i29];
                        if (d35 != 0.0d) {
                            eigenvalueDecomposition.f4d[i25] = d34 - (d30 / d35);
                        }
                        eigenvalueDecomposition.f5e[i29] = 0.0d;
                        eigenvalueDecomposition.f5e[i25] = 0.0d;
                        double d36 = eigenvalueDecomposition.f2H[i25][i29];
                        double abs2 = Math.abs(d36) + Math.abs(d35);
                        double d37 = d36 / abs2;
                        double d38 = d35 / abs2;
                        d20 = d35;
                        d26 = Math.sqrt((d37 * d37) + (d38 * d38));
                        d31 = d37 / d26;
                        d32 = d38 / d26;
                        for (int i30 = i29; i30 < i22; i30++) {
                            d20 = eigenvalueDecomposition.f2H[i29][i30];
                            eigenvalueDecomposition.f2H[i29][i30] = (d32 * d20) + (eigenvalueDecomposition.f2H[i25][i30] * d31);
                            eigenvalueDecomposition.f2H[i25][i30] = (eigenvalueDecomposition.f2H[i25][i30] * d32) - (d31 * d20);
                        }
                        for (int i31 = 0; i31 <= i25; i31++) {
                            d20 = eigenvalueDecomposition.f2H[i31][i29];
                            eigenvalueDecomposition.f2H[i31][i29] = (d32 * d20) + (eigenvalueDecomposition.f2H[i31][i25] * d31);
                            eigenvalueDecomposition.f2H[i31][i25] = (eigenvalueDecomposition.f2H[i31][i25] * d32) - (d31 * d20);
                        }
                        for (int i32 = 0; i32 <= i23; i32++) {
                            d20 = eigenvalueDecomposition.f3V[i32][i29];
                            eigenvalueDecomposition.f3V[i32][i29] = (d32 * d20) + (eigenvalueDecomposition.f3V[i32][i25] * d31);
                            eigenvalueDecomposition.f3V[i32][i25] = (eigenvalueDecomposition.f3V[i32][i25] * d32) - (d31 * d20);
                        }
                        d33 = abs2;
                    } else {
                        double d39 = d34 + d31;
                        eigenvalueDecomposition.f4d[i29] = d39;
                        eigenvalueDecomposition.f4d[i25] = d39;
                        eigenvalueDecomposition.f5e[i29] = sqrt;
                        eigenvalueDecomposition.f5e[i25] = -sqrt;
                        d20 = sqrt;
                    }
                    i25 -= 2;
                    d28 = d32;
                    i11 = i22;
                    i12 = i23;
                    d27 = d31;
                    d24 = d33;
                    d25 = d20;
                } else {
                    double d40 = d29;
                    d8 = pow;
                    double d41 = eigenvalueDecomposition.f2H[i25][i25];
                    if (i27 < i25) {
                        d9 = eigenvalueDecomposition.f2H[i29][i29];
                        d10 = eigenvalueDecomposition.f2H[i25][i29] * eigenvalueDecomposition.f2H[i29][i25];
                    } else {
                        d10 = 0.0d;
                        d9 = 0.0d;
                    }
                    if (i26 == 10) {
                        d23 += d41;
                        for (int i33 = 0; i33 <= i25; i33++) {
                            double[] dArr = eigenvalueDecomposition.f2H[i33];
                            dArr[i33] = dArr[i33] - d41;
                        }
                        double abs3 = Math.abs(eigenvalueDecomposition.f2H[i25][i29]) + Math.abs(eigenvalueDecomposition.f2H[i29][i25 - 2]);
                        d41 = 0.75d * abs3;
                        d40 = abs3;
                        d10 = -0.4375d * abs3 * abs3;
                        d9 = d41;
                    }
                    if (i26 == 30) {
                        double d42 = (d9 - d41) / 2.0d;
                        i14 = i22;
                        i13 = i23;
                        double d43 = (d42 * d42) + d10;
                        if (d43 > 0.0d) {
                            double sqrt2 = Math.sqrt(d43);
                            if (d9 < d41) {
                                sqrt2 = -sqrt2;
                            }
                            d40 = d41 - (d10 / (d42 + sqrt2));
                            for (int i34 = 0; i34 <= i25; i34++) {
                                double[] dArr2 = eigenvalueDecomposition.f2H[i34];
                                dArr2[i34] = dArr2[i34] - d40;
                            }
                            d23 += d40;
                            d9 = 0.964d;
                            d10 = 0.964d;
                            d41 = 0.964d;
                        } else {
                            d40 = d43;
                        }
                    } else {
                        i14 = i22;
                        i13 = i23;
                    }
                    int i35 = i26 + 1;
                    int i36 = i25 - 2;
                    while (true) {
                        if (i36 < i27) {
                            d11 = d23;
                            i15 = i35;
                            d12 = d41;
                            d13 = d27;
                            d14 = d28;
                            break;
                        }
                        d11 = d23;
                        double d44 = eigenvalueDecomposition.f2H[i36][i36];
                        double d45 = d41 - d44;
                        double d46 = d9 - d44;
                        i15 = i35;
                        int i37 = i36 + 1;
                        double d47 = d10;
                        double d48 = (((d45 * d46) - d10) / eigenvalueDecomposition.f2H[i37][i36]) + eigenvalueDecomposition.f2H[i36][i37];
                        double d49 = ((eigenvalueDecomposition.f2H[i37][i37] - d44) - d45) - d46;
                        d12 = d41;
                        double d50 = eigenvalueDecomposition.f2H[i36 + 2][i37];
                        d40 = Math.abs(d48) + Math.abs(d49) + Math.abs(d50);
                        double d51 = d48 / d40;
                        d14 = d49 / d40;
                        d18 = d50 / d40;
                        if (i36 == i27) {
                            d19 = d44;
                            d13 = d51;
                            break;
                        }
                        int i38 = i36 - 1;
                        double d52 = d9;
                        d13 = d51;
                        d19 = d44;
                        if (Math.abs(eigenvalueDecomposition.f2H[i36][i38]) * (Math.abs(d14) + Math.abs(d18)) < d8 * Math.abs(d51) * (Math.abs(eigenvalueDecomposition.f2H[i38][i38]) + Math.abs(d44) + Math.abs(eigenvalueDecomposition.f2H[i37][i37]))) {
                            break;
                        }
                        i36--;
                        d28 = d14;
                        d26 = d18;
                        d23 = d11;
                        i35 = i15;
                        d10 = d47;
                        d41 = d12;
                        d9 = d52;
                        d27 = d13;
                        d25 = d19;
                    }
                    d26 = d18;
                    d25 = d19;
                    int i39 = i36 + 2;
                    for (int i40 = i39; i40 <= i25; i40++) {
                        eigenvalueDecomposition.f2H[i40][i40 - 2] = 0.0d;
                        if (i40 > i39) {
                            eigenvalueDecomposition.f2H[i40][i40 - 3] = 0.0d;
                        }
                    }
                    int i41 = i36;
                    while (i41 <= i29) {
                        boolean z4 = i41 != i29;
                        if (i41 != i36) {
                            int i42 = i41 - 1;
                            double d53 = eigenvalueDecomposition.f2H[i41][i42];
                            double d54 = eigenvalueDecomposition.f2H[i41 + 1][i42];
                            if (z4) {
                                d17 = eigenvalueDecomposition.f2H[i41 + 2][i42];
                            } else {
                                d17 = 0.0d;
                            }
                            d12 = Math.abs(d53) + Math.abs(d54) + Math.abs(d17);
                            if (d12 == 0.0d) {
                                i16 = i25;
                                i17 = i27;
                                d26 = d17;
                                i18 = i29;
                                d13 = d53;
                                d14 = d54;
                                i19 = i14;
                                i20 = i13;
                                i41++;
                                i13 = i20;
                                i14 = i19;
                                i29 = i18;
                                i27 = i17;
                                i25 = i16;
                            } else {
                                d13 = d53 / d12;
                                d14 = d54 / d12;
                                d26 = d17 / d12;
                            }
                        }
                        double sqrt3 = Math.sqrt((d13 * d13) + (d14 * d14) + (d26 * d26));
                        if (d13 < 0.0d) {
                            sqrt3 = -sqrt3;
                        }
                        if (sqrt3 != 0.0d) {
                            if (i41 != i36) {
                                eigenvalueDecomposition.f2H[i41][i41 - 1] = (-sqrt3) * d12;
                            } else if (i27 != i36) {
                                int i43 = i41 - 1;
                                eigenvalueDecomposition.f2H[i41][i43] = -eigenvalueDecomposition.f2H[i41][i43];
                            }
                            double d55 = d13 + sqrt3;
                            double d56 = d55 / sqrt3;
                            double d57 = d14 / sqrt3;
                            d25 = d26 / sqrt3;
                            d14 /= d55;
                            d26 /= d55;
                            int i44 = i41;
                            while (true) {
                                i18 = i29;
                                i19 = i14;
                                if (i44 >= i19) {
                                    break;
                                }
                                int i45 = i27;
                                int i46 = i41 + 1;
                                double d58 = eigenvalueDecomposition.f2H[i41][i44] + (eigenvalueDecomposition.f2H[i46][i44] * d14);
                                if (z4) {
                                    int i47 = i41 + 2;
                                    d58 += eigenvalueDecomposition.f2H[i47][i44] * d26;
                                    d16 = sqrt3;
                                    eigenvalueDecomposition.f2H[i47][i44] = eigenvalueDecomposition.f2H[i47][i44] - (d58 * d25);
                                } else {
                                    d16 = sqrt3;
                                }
                                d55 = d58;
                                eigenvalueDecomposition.f2H[i41][i44] = eigenvalueDecomposition.f2H[i41][i44] - (d55 * d56);
                                eigenvalueDecomposition.f2H[i46][i44] = eigenvalueDecomposition.f2H[i46][i44] - (d55 * d57);
                                i44++;
                                i14 = i19;
                                i29 = i18;
                                i27 = i45;
                                sqrt3 = d16;
                            }
                            i17 = i27;
                            d15 = sqrt3;
                            int i48 = 0;
                            while (i48 <= Math.min(i25, i41 + 3)) {
                                int i49 = i41 + 1;
                                double d59 = (eigenvalueDecomposition.f2H[i48][i41] * d56) + (eigenvalueDecomposition.f2H[i48][i49] * d57);
                                if (z4) {
                                    int i50 = i41 + 2;
                                    d59 += eigenvalueDecomposition.f2H[i48][i50] * d25;
                                    i21 = i25;
                                    eigenvalueDecomposition.f2H[i48][i50] = eigenvalueDecomposition.f2H[i48][i50] - (d59 * d26);
                                } else {
                                    i21 = i25;
                                }
                                d55 = d59;
                                eigenvalueDecomposition.f2H[i48][i41] = eigenvalueDecomposition.f2H[i48][i41] - d55;
                                eigenvalueDecomposition.f2H[i48][i49] = eigenvalueDecomposition.f2H[i48][i49] - (d55 * d14);
                                i48++;
                                i25 = i21;
                            }
                            i16 = i25;
                            int i51 = 0;
                            while (true) {
                                i20 = i13;
                                if (i51 > i20) {
                                    break;
                                }
                                int i52 = i41 + 1;
                                double d60 = (eigenvalueDecomposition.f3V[i51][i41] * d56) + (eigenvalueDecomposition.f3V[i51][i52] * d57);
                                if (z4) {
                                    int i53 = i41 + 2;
                                    d60 += eigenvalueDecomposition.f3V[i51][i53] * d25;
                                    z = z4;
                                    eigenvalueDecomposition.f3V[i51][i53] = eigenvalueDecomposition.f3V[i51][i53] - (d60 * d26);
                                } else {
                                    z = z4;
                                }
                                d55 = d60;
                                eigenvalueDecomposition.f3V[i51][i41] = eigenvalueDecomposition.f3V[i51][i41] - d55;
                                eigenvalueDecomposition.f3V[i51][i52] = eigenvalueDecomposition.f3V[i51][i52] - (d55 * d14);
                                i51++;
                                i13 = i20;
                                z4 = z;
                            }
                            d12 = d56;
                        } else {
                            i16 = i25;
                            i17 = i27;
                            d15 = sqrt3;
                            i18 = i29;
                            i19 = i14;
                            i20 = i13;
                        }
                        d40 = d15;
                        i41++;
                        i13 = i20;
                        i14 = i19;
                        i29 = i18;
                        i27 = i17;
                        i25 = i16;
                    }
                    int i54 = i25;
                    i11 = i14;
                    i12 = i13;
                    d28 = d14;
                    d24 = d40;
                    d23 = d11;
                    i26 = i15;
                    d27 = d13;
                    i23 = i12;
                    i22 = i11;
                    pow = d8;
                }
            }
            i26 = 0;
            i23 = i12;
            i22 = i11;
            pow = d8;
        }
        int i55 = i22;
        int i56 = i23;
        double d61 = pow;
        if (d22 != 0.0d) {
            double d62 = d24;
            int i57 = i56;
            while (i57 >= 0) {
                double d63 = eigenvalueDecomposition.f4d[i57];
                double d64 = eigenvalueDecomposition.f5e[i57];
                if (d64 == 0.0d) {
                    eigenvalueDecomposition.f2H[i57][i57] = 1.0d;
                    int i58 = i57 - 1;
                    int i59 = i57;
                    double d65 = d25;
                    while (i58 >= 0) {
                        double d66 = eigenvalueDecomposition.f2H[i58][i58] - d63;
                        int i60 = i56;
                        double d67 = 0.0d;
                        for (int i61 = i59; i61 <= i57; i61++) {
                            d67 += eigenvalueDecomposition.f2H[i58][i61] * eigenvalueDecomposition.f2H[i61][i57];
                        }
                        if (eigenvalueDecomposition.f5e[i58] < 0.0d) {
                            d62 = d67;
                            i10 = i55;
                            d7 = d63;
                            d65 = d66;
                        } else {
                            if (eigenvalueDecomposition.f5e[i58] == 0.0d) {
                                if (d66 != 0.0d) {
                                    i10 = i55;
                                    eigenvalueDecomposition.f2H[i58][i57] = (-d67) / d66;
                                } else {
                                    i10 = i55;
                                    eigenvalueDecomposition.f2H[i58][i57] = (-d67) / (d61 * d22);
                                }
                                d7 = d63;
                            } else {
                                i10 = i55;
                                int i62 = i58 + 1;
                                double d68 = eigenvalueDecomposition.f2H[i58][i62];
                                double d69 = eigenvalueDecomposition.f2H[i62][i58];
                                double d70 = ((d68 * d62) - (d65 * d67)) / (((eigenvalueDecomposition.f4d[i58] - d63) * (eigenvalueDecomposition.f4d[i58] - d63)) + (eigenvalueDecomposition.f5e[i58] * eigenvalueDecomposition.f5e[i58]));
                                eigenvalueDecomposition.f2H[i58][i57] = d70;
                                if (Math.abs(d68) > Math.abs(d65)) {
                                    d7 = d63;
                                    eigenvalueDecomposition.f2H[i62][i57] = ((-d67) - (d66 * d70)) / d68;
                                } else {
                                    d7 = d63;
                                    eigenvalueDecomposition.f2H[i62][i57] = ((-d62) - (d69 * d70)) / d65;
                                }
                            }
                            double abs4 = Math.abs(eigenvalueDecomposition.f2H[i58][i57]);
                            if (d61 * abs4 * abs4 > 1.0d) {
                                for (int i63 = i58; i63 <= i57; i63++) {
                                    eigenvalueDecomposition.f2H[i63][i57] = eigenvalueDecomposition.f2H[i63][i57] / abs4;
                                }
                            }
                            i59 = i58;
                        }
                        i58--;
                        d26 = d67;
                        i56 = i60;
                        i55 = i10;
                        d63 = d7;
                    }
                    i5 = i56;
                    i4 = i55;
                    d25 = d65;
                    i3 = i57;
                } else {
                    i5 = i56;
                    i4 = i55;
                    double d71 = d63;
                    if (d64 < 0.0d) {
                        int i64 = i57 - 1;
                        if (Math.abs(eigenvalueDecomposition.f2H[i57][i64]) > Math.abs(eigenvalueDecomposition.f2H[i64][i57])) {
                            eigenvalueDecomposition.f2H[i64][i64] = d64 / eigenvalueDecomposition.f2H[i57][i64];
                            eigenvalueDecomposition.f2H[i64][i57] = (-(eigenvalueDecomposition.f2H[i57][i57] - d71)) / eigenvalueDecomposition.f2H[i57][i64];
                            d2 = d64;
                            d = d62;
                            i2 = i5;
                            i6 = i4;
                        } else {
                            d2 = d64;
                            i2 = i5;
                            d = d62;
                            i6 = i4;
                            eigenvalueDecomposition.cdiv(0.0d, -eigenvalueDecomposition.f2H[i64][i57], eigenvalueDecomposition.f2H[i64][i64] - d71, d2);
                            eigenvalueDecomposition.f2H[i64][i64] = eigenvalueDecomposition.cdivr;
                            eigenvalueDecomposition.f2H[i64][i57] = eigenvalueDecomposition.cdivi;
                        }
                        eigenvalueDecomposition.f2H[i57][i64] = 0.0d;
                        eigenvalueDecomposition.f2H[i57][i57] = 1.0d;
                        double d72 = d25;
                        double d73 = d26;
                        double d74 = d;
                        int i65 = i57 - 2;
                        int i66 = i64;
                        while (i65 >= 0) {
                            int i67 = i66;
                            double d75 = 0.0d;
                            double d76 = 0.0d;
                            while (i67 <= i57) {
                                d75 += eigenvalueDecomposition.f2H[i65][i67] * eigenvalueDecomposition.f2H[i67][i64];
                                d76 += eigenvalueDecomposition.f2H[i65][i67] * eigenvalueDecomposition.f2H[i67][i57];
                                i67++;
                                i64 = i64;
                                d74 = d74;
                            }
                            double d77 = d74;
                            int i68 = i64;
                            double d78 = d75;
                            d74 = d76;
                            double d79 = eigenvalueDecomposition.f2H[i65][i65] - d71;
                            int i69 = i66;
                            if (eigenvalueDecomposition.f5e[i65] < 0.0d) {
                                i8 = i57;
                                i7 = i6;
                                d73 = d78;
                                i66 = i69;
                            } else {
                                if (eigenvalueDecomposition.f5e[i65] == 0.0d) {
                                    i7 = i6;
                                    double d80 = d73;
                                    double d81 = d77;
                                    d5 = d72;
                                    eigenvalueDecomposition.cdiv(-d78, -d74, d79, d2);
                                    eigenvalueDecomposition.f2H[i65][i68] = eigenvalueDecomposition.cdivr;
                                    eigenvalueDecomposition.f2H[i65][i57] = eigenvalueDecomposition.cdivi;
                                    i9 = i57;
                                    d4 = d81;
                                    d3 = d80;
                                } else {
                                    double d82 = d79;
                                    i7 = i6;
                                    double d83 = d77;
                                    double d84 = d72;
                                    double d85 = d78;
                                    d5 = d84;
                                    int i70 = i65 + 1;
                                    double d86 = eigenvalueDecomposition.f2H[i65][i70];
                                    double d87 = d74;
                                    double d88 = eigenvalueDecomposition.f2H[i70][i65];
                                    double d89 = (((eigenvalueDecomposition.f4d[i65] - d71) * (eigenvalueDecomposition.f4d[i65] - d71)) + (eigenvalueDecomposition.f5e[i65] * eigenvalueDecomposition.f5e[i65])) - (d2 * d2);
                                    double d90 = (eigenvalueDecomposition.f4d[i65] - d71) * 2.0d * d2;
                                    if ((d89 == 0.0d) && (d90 == 0.0d)) {
                                        i9 = i57;
                                        d6 = d82;
                                        d89 = d61 * d22 * (Math.abs(d6) + Math.abs(d2) + Math.abs(d86) + Math.abs(d88) + Math.abs(d5));
                                    } else {
                                        i9 = i57;
                                        d6 = d82;
                                    }
                                    double d91 = ((d86 * d73) - (d5 * d85)) + (d2 * d87);
                                    double d92 = d6;
                                    double d93 = d83;
                                    double d94 = ((d86 * d93) - (d5 * d87)) - (d2 * d85);
                                    double d95 = d92;
                                    double d96 = d93;
                                    double d97 = d86;
                                    double d98 = d73;
                                    double d99 = d85;
                                    double d100 = d89;
                                    double d101 = d88;
                                    double d102 = d87;
                                    cdiv(d91, d94, d100, d90);
                                    this.f2H[i65][i68] = this.cdivr;
                                    this.f2H[i65][i9] = this.cdivi;
                                    if (Math.abs(d97) > Math.abs(d5) + Math.abs(d2)) {
                                        this.f2H[i70][i68] = (((-d99) - (d95 * this.f2H[i65][i68])) + (d2 * this.f2H[i65][i9])) / d97;
                                        this.f2H[i70][i9] = (((-d102) - (d95 * this.f2H[i65][i9])) - (d2 * this.f2H[i65][i68])) / d97;
                                        eigenvalueDecomposition = this;
                                        d4 = d96;
                                        d3 = d98;
                                    } else {
                                        double d103 = d98;
                                        double d104 = d96;
                                        d4 = d104;
                                        d3 = d103;
                                        eigenvalueDecomposition = this;
                                        cdiv((-d103) - (d101 * this.f2H[i65][i68]), (-d104) - (d101 * this.f2H[i65][i9]), d5, d2);
                                        eigenvalueDecomposition.f2H[i70][i68] = eigenvalueDecomposition.cdivr;
                                        eigenvalueDecomposition.f2H[i70][i9] = eigenvalueDecomposition.cdivi;
                                    }
                                }
                                double max2 = Math.max(Math.abs(eigenvalueDecomposition.f2H[i65][i68]), Math.abs(eigenvalueDecomposition.f2H[i65][i9]));
                                if (d61 * max2 * max2 > 1.0d) {
                                    int i71 = i65;
                                    while (true) {
                                        i8 = i9;
                                        if (i71 > i8) {
                                            break;
                                        }
                                        eigenvalueDecomposition.f2H[i71][i68] = eigenvalueDecomposition.f2H[i71][i68] / max2;
                                        eigenvalueDecomposition.f2H[i71][i8] = eigenvalueDecomposition.f2H[i71][i8] / max2;
                                        i71++;
                                        i9 = i8;
                                    }
                                } else {
                                    i8 = i9;
                                }
                                d79 = d5;
                                i66 = i65;
                                d74 = d4;
                                d73 = d3;
                            }
                            i65--;
                            i57 = i8;
                            i64 = i68;
                            i6 = i7;
                            d72 = d79;
                        }
                        i = i6;
                        i3 = i57;
                        d25 = d72;
                        d62 = d74;
                        d26 = d73;
                        i57 = i3 - 1;
                        i56 = i2;
                        i55 = i;
                    } else {
                        i3 = i57;
                        double d105 = d62;
                    }
                }
                i2 = i5;
                i = i4;
                i57 = i3 - 1;
                i56 = i2;
                i55 = i;
            }
            int i72 = i56;
            int i73 = i55;
            int i74 = 0;
            while (true) {
                int i75 = i73;
                if (i74 >= i75) {
                    break;
                }
                int i76 = i72;
                if ((i74 < 0) || (i74 > i76)) {
                    for (int i77 = i74; i77 < i75; i77++) {
                        eigenvalueDecomposition.f3V[i74][i77] = eigenvalueDecomposition.f2H[i74][i77];
                    }
                }
                i74++;
                i73 = i75;
                i72 = i76;
            }
            int i78 = i72;
            for (int i79 = i78; i79 >= 0; i79--) {
                for (int i80 = 0; i80 <= i78; i80++) {
                    double d106 = 0.0d;
                    for (int i81 = 0; i81 <= Math.min(i79, i78); i81++) {
                        d106 += eigenvalueDecomposition.f3V[i80][i81] * eigenvalueDecomposition.f2H[i81][i79];
                    }
                    eigenvalueDecomposition.f3V[i80][i79] = d106;
                }
            }
        }
    }

    public EigenvalueDecomposition(Matrix matrix) {
        double[][] array = matrix.getArray();
        this.f6n = matrix.getColumnDimension();
        int i = 0;
        while (true) {
            if (!(i < this.f6n) || !this.issymmetric) {
                break;
            }
            int i2 = 0;
            while (true) {
                if (!(i2 < this.f6n) || !this.issymmetric) {
                    break;
                }
                this.issymmetric = array[i2][i] == array[i][i2];
                i2++;
            }
            i++;
        }
        if (this.issymmetric) {
            for (int i3 = 0; i3 < this.f6n; i3++) {
                for (int i4 = 0; i4 < this.f6n; i4++) {
                    this.f3V[i3][i4] = array[i3][i4];
                }
            }
            tred2();
            tql2();
            return;
        }
        this.f2H = (double[][]) Array.newInstance(double.class, new int[]{this.f6n, this.f6n});
        this.ort = new double[this.f6n];
        for (int i5 = 0; i5 < this.f6n; i5++) {
            for (int i6 = 0; i6 < this.f6n; i6++) {
                this.f2H[i6][i5] = array[i6][i5];
            }
        }
        orthes();
        hqr2();
    }

    public Matrix getV() {
        return new Matrix(this.f3V, this.f6n, this.f6n);
    }

    public double[] getRealEigenvalues() {
        return this.f4d;
    }

    public double[] getImagEigenvalues() {
        return this.f5e;
    }

    public Matrix getD() {
        Matrix matrix = new Matrix(this.f6n, this.f6n);
        double[][] array = matrix.getArray();
        for (int i = 0; i < this.f6n; i++) {
            for (int i2 = 0; i2 < this.f6n; i2++) {
                array[i][i2] = 0.0d;
            }
            array[i][i] = this.f4d[i];
            if (this.f5e[i] > 0.0d) {
                array[i][i + 1] = this.f5e[i];
            } else if (this.f5e[i] < 0.0d) {
                array[i][i - 1] = this.f5e[i];
            }
        }
        return matrix;
    }
}

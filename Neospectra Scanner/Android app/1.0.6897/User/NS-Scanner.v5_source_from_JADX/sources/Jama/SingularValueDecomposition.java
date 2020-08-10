package Jama;

import Jama.util.Maths;
import java.io.Serializable;
import java.lang.reflect.Array;

public class SingularValueDecomposition implements Serializable {
    private static final long serialVersionUID = 1;

    /* renamed from: U */
    private double[][] f16U;

    /* renamed from: V */
    private double[][] f17V;

    /* renamed from: m */
    private int f18m;

    /* renamed from: n */
    private int f19n;

    /* renamed from: s */
    private double[] f20s;

    public SingularValueDecomposition(Matrix matrix) {
        double d;
        int i;
        SingularValueDecomposition singularValueDecomposition;
        char c;
        double[] dArr;
        double d2;
        double d3;
        int i2;
        long j;
        int i3;
        int i4;
        double d4;
        int i5;
        double[] dArr2;
        SingularValueDecomposition singularValueDecomposition2 = this;
        double[][] arrayCopy = matrix.getArrayCopy();
        singularValueDecomposition2.f18m = matrix.getRowDimension();
        singularValueDecomposition2.f19n = matrix.getColumnDimension();
        int min = Math.min(singularValueDecomposition2.f18m, singularValueDecomposition2.f19n);
        singularValueDecomposition2.f20s = new double[Math.min(singularValueDecomposition2.f18m + 1, singularValueDecomposition2.f19n)];
        singularValueDecomposition2.f16U = (double[][]) Array.newInstance(double.class, new int[]{singularValueDecomposition2.f18m, min});
        singularValueDecomposition2.f17V = (double[][]) Array.newInstance(double.class, new int[]{singularValueDecomposition2.f19n, singularValueDecomposition2.f19n});
        double[] dArr3 = new double[singularValueDecomposition2.f19n];
        double[] dArr4 = new double[singularValueDecomposition2.f18m];
        int min2 = Math.min(singularValueDecomposition2.f18m - 1, singularValueDecomposition2.f19n);
        int max = Math.max(0, Math.min(singularValueDecomposition2.f19n - 2, singularValueDecomposition2.f18m));
        int i6 = 0;
        while (true) {
            d = 0.0d;
            if (i6 >= Math.max(min2, max)) {
                break;
            }
            if (i6 < min2) {
                singularValueDecomposition2.f20s[i6] = 0.0d;
                int i7 = i6;
                while (i7 < singularValueDecomposition2.f18m) {
                    double[] dArr5 = dArr4;
                    singularValueDecomposition2.f20s[i6] = Maths.hypot(singularValueDecomposition2.f20s[i6], arrayCopy[i7][i6]);
                    i7++;
                    dArr4 = dArr5;
                }
                dArr2 = dArr4;
                if (singularValueDecomposition2.f20s[i6] != 0.0d) {
                    if (arrayCopy[i6][i6] < 0.0d) {
                        singularValueDecomposition2.f20s[i6] = -singularValueDecomposition2.f20s[i6];
                    }
                    for (int i8 = i6; i8 < singularValueDecomposition2.f18m; i8++) {
                        double[] dArr6 = arrayCopy[i8];
                        dArr6[i6] = dArr6[i6] / singularValueDecomposition2.f20s[i6];
                    }
                    double[] dArr7 = arrayCopy[i6];
                    dArr7[i6] = dArr7[i6] + 1.0d;
                }
                singularValueDecomposition2.f20s[i6] = -singularValueDecomposition2.f20s[i6];
            } else {
                dArr2 = dArr4;
            }
            int i9 = i6 + 1;
            for (int i10 = i9; i10 < singularValueDecomposition2.f19n; i10++) {
                if ((i6 < min2) && (singularValueDecomposition2.f20s[i6] != 0.0d)) {
                    double d5 = 0.0d;
                    for (int i11 = i6; i11 < singularValueDecomposition2.f18m; i11++) {
                        d5 += arrayCopy[i11][i6] * arrayCopy[i11][i10];
                    }
                    double d6 = (-d5) / arrayCopy[i6][i6];
                    for (int i12 = i6; i12 < singularValueDecomposition2.f18m; i12++) {
                        double[] dArr8 = arrayCopy[i12];
                        dArr8[i10] = dArr8[i10] + (arrayCopy[i12][i6] * d6);
                    }
                }
                dArr3[i10] = arrayCopy[i6][i10];
            }
            if ((i6 < min2) && true) {
                for (int i13 = i6; i13 < singularValueDecomposition2.f18m; i13++) {
                    singularValueDecomposition2.f16U[i13][i6] = arrayCopy[i13][i6];
                }
            }
            if (i6 < max) {
                dArr3[i6] = 0.0d;
                for (int i14 = i9; i14 < singularValueDecomposition2.f19n; i14++) {
                    dArr3[i6] = Maths.hypot(dArr3[i6], dArr3[i14]);
                }
                if (dArr3[i6] != 0.0d) {
                    if (dArr3[i9] < 0.0d) {
                        dArr3[i6] = -dArr3[i6];
                    }
                    for (int i15 = i9; i15 < singularValueDecomposition2.f19n; i15++) {
                        dArr3[i15] = dArr3[i15] / dArr3[i6];
                    }
                    dArr3[i9] = dArr3[i9] + 1.0d;
                }
                dArr3[i6] = -dArr3[i6];
                if ((i9 < singularValueDecomposition2.f18m) && (dArr3[i6] != 0.0d)) {
                    for (int i16 = i9; i16 < singularValueDecomposition2.f18m; i16++) {
                        dArr2[i16] = 0.0d;
                    }
                    for (int i17 = i9; i17 < singularValueDecomposition2.f19n; i17++) {
                        for (int i18 = i9; i18 < singularValueDecomposition2.f18m; i18++) {
                            dArr2[i18] = dArr2[i18] + (dArr3[i17] * arrayCopy[i18][i17]);
                        }
                    }
                    for (int i19 = i9; i19 < singularValueDecomposition2.f19n; i19++) {
                        double d7 = (-dArr3[i19]) / dArr3[i9];
                        for (int i20 = i9; i20 < singularValueDecomposition2.f18m; i20++) {
                            double[] dArr9 = arrayCopy[i20];
                            dArr9[i19] = dArr9[i19] + (dArr2[i20] * d7);
                        }
                    }
                }
                for (int i21 = i9; i21 < singularValueDecomposition2.f19n; i21++) {
                    singularValueDecomposition2.f17V[i21][i6] = dArr3[i21];
                }
            }
            i6 = i9;
            dArr4 = dArr2;
        }
        int min3 = Math.min(singularValueDecomposition2.f19n, singularValueDecomposition2.f18m + 1);
        if (min2 < singularValueDecomposition2.f19n) {
            singularValueDecomposition2.f20s[min2] = arrayCopy[min2][min2];
        }
        if (singularValueDecomposition2.f18m < min3) {
            singularValueDecomposition2.f20s[min3 - 1] = 0.0d;
        }
        if (max + 1 < min3) {
            dArr3[max] = arrayCopy[max][min3 - 1];
        }
        int i22 = min3 - 1;
        dArr3[i22] = 0.0d;
        for (int i23 = min2; i23 < min; i23++) {
            for (int i24 = 0; i24 < singularValueDecomposition2.f18m; i24++) {
                singularValueDecomposition2.f16U[i24][i23] = 0.0d;
            }
            singularValueDecomposition2.f16U[i23][i23] = 1.0d;
        }
        int i25 = min2 - 1;
        while (i25 >= 0) {
            if (singularValueDecomposition2.f20s[i25] != d) {
                int i26 = i25 + 1;
                while (i26 < min) {
                    double d8 = d;
                    for (int i27 = i25; i27 < singularValueDecomposition2.f18m; i27++) {
                        d8 += singularValueDecomposition2.f16U[i27][i25] * singularValueDecomposition2.f16U[i27][i26];
                    }
                    double d9 = (-d8) / singularValueDecomposition2.f16U[i25][i25];
                    for (int i28 = i25; i28 < singularValueDecomposition2.f18m; i28++) {
                        double[] dArr10 = singularValueDecomposition2.f16U[i28];
                        dArr10[i26] = dArr10[i26] + (singularValueDecomposition2.f16U[i28][i25] * d9);
                    }
                    i26++;
                    d = 0.0d;
                }
                for (int i29 = i25; i29 < singularValueDecomposition2.f18m; i29++) {
                    singularValueDecomposition2.f16U[i29][i25] = -singularValueDecomposition2.f16U[i29][i25];
                }
                singularValueDecomposition2.f16U[i25][i25] = singularValueDecomposition2.f16U[i25][i25] + 1.0d;
                for (int i30 = 0; i30 < i25 - 1; i30++) {
                    singularValueDecomposition2.f16U[i30][i25] = 0.0d;
                }
            } else {
                for (int i31 = 0; i31 < singularValueDecomposition2.f18m; i31++) {
                    singularValueDecomposition2.f16U[i31][i25] = 0.0d;
                }
                singularValueDecomposition2.f16U[i25][i25] = 1.0d;
            }
            i25--;
            d = 0.0d;
        }
        int i32 = singularValueDecomposition2.f19n - 1;
        while (i32 >= 0) {
            if ((i32 < max) && (dArr3[i32] != 0.0d)) {
                int i33 = i32 + 1;
                for (int i34 = i33; i34 < min; i34++) {
                    double d10 = 0.0d;
                    for (int i35 = i33; i35 < singularValueDecomposition2.f19n; i35++) {
                        d10 += singularValueDecomposition2.f17V[i35][i32] * singularValueDecomposition2.f17V[i35][i34];
                    }
                    double d11 = (-d10) / singularValueDecomposition2.f17V[i33][i32];
                    for (int i36 = i33; i36 < singularValueDecomposition2.f19n; i36++) {
                        double[] dArr11 = singularValueDecomposition2.f17V[i36];
                        dArr11[i34] = dArr11[i34] + (singularValueDecomposition2.f17V[i36][i32] * d11);
                    }
                }
            }
            for (int i37 = 0; i37 < singularValueDecomposition2.f19n; i37++) {
                singularValueDecomposition2.f17V[i37][i32] = 0.0d;
            }
            singularValueDecomposition2.f17V[i32][i32] = 1.0d;
            i32--;
        }
        double pow = Math.pow(2.0d, -52.0d);
        double pow2 = Math.pow(2.0d, -966.0d);
        while (min3 > 0) {
            int i38 = min3 - 2;
            int i39 = i38;
            while (true) {
                if (i39 < -1 || i39 == -1) {
                    i = i22;
                } else {
                    i = i22;
                    if (Math.abs(dArr3[i39]) <= ((Math.abs(singularValueDecomposition2.f20s[i39]) + Math.abs(singularValueDecomposition2.f20s[i39 + 1])) * pow) + pow2) {
                        dArr3[i39] = 0.0d;
                    } else {
                        i39--;
                        i22 = i;
                        singularValueDecomposition2 = this;
                    }
                }
            }
            if (i39 == i38) {
                c = 4;
                singularValueDecomposition = this;
            } else {
                int i40 = min3 - 1;
                int i41 = i40;
                while (true) {
                    if (i41 < i39 || i41 == i39) {
                        singularValueDecomposition = this;
                    } else {
                        singularValueDecomposition = this;
                        if (Math.abs(singularValueDecomposition.f20s[i41]) <= (((i41 != min3 ? Math.abs(dArr3[i41]) : 0.0d) + (i41 != i39 + 1 ? Math.abs(dArr3[i41 - 1]) : 0.0d)) * pow) + pow2) {
                            singularValueDecomposition.f20s[i41] = 0.0d;
                        } else {
                            i41--;
                        }
                    }
                }
                singularValueDecomposition = this;
                if (i41 == i39) {
                    c = 3;
                } else if (i41 == i40) {
                    c = 1;
                } else {
                    i39 = i41;
                    c = 2;
                }
            }
            int i42 = i39 + 1;
            switch (c) {
                case 1:
                    int i43 = i38;
                    dArr = dArr3;
                    d3 = pow;
                    d2 = pow2;
                    i4 = i;
                    j = 4611686018427387904L;
                    double d12 = dArr[i43];
                    dArr[i43] = 0.0d;
                    double d13 = d12;
                    int i44 = i43;
                    while (i44 >= i42) {
                        double hypot = Maths.hypot(singularValueDecomposition.f20s[i44], d13);
                        double d14 = singularValueDecomposition.f20s[i44] / hypot;
                        double d15 = d13 / hypot;
                        singularValueDecomposition.f20s[i44] = hypot;
                        if (i44 != i42) {
                            int i45 = i44 - 1;
                            d13 = (-d15) * dArr[i45];
                            dArr[i45] = dArr[i45] * d14;
                        }
                        int i46 = 0;
                        while (i46 < singularValueDecomposition.f19n) {
                            int i47 = min3 - 1;
                            double d16 = (singularValueDecomposition.f17V[i46][i44] * d14) + (singularValueDecomposition.f17V[i46][i47] * d15);
                            int i48 = min3;
                            singularValueDecomposition.f17V[i46][i47] = ((-d15) * singularValueDecomposition.f17V[i46][i44]) + (singularValueDecomposition.f17V[i46][i47] * d14);
                            singularValueDecomposition.f17V[i46][i44] = d16;
                            i46++;
                            min3 = i48;
                        }
                        i44--;
                        min3 = min3;
                    }
                    i3 = min3;
                    break;
                case 2:
                    dArr = dArr3;
                    d3 = pow;
                    d2 = pow2;
                    i4 = i;
                    j = 4611686018427387904L;
                    int i49 = i42 - 1;
                    double d17 = dArr[i49];
                    dArr[i49] = 0.0d;
                    while (i42 < min3) {
                        double hypot2 = Maths.hypot(singularValueDecomposition.f20s[i42], d17);
                        double d18 = singularValueDecomposition.f20s[i42] / hypot2;
                        double d19 = d17 / hypot2;
                        singularValueDecomposition.f20s[i42] = hypot2;
                        double d20 = -d19;
                        double d21 = dArr[i42] * d20;
                        dArr[i42] = dArr[i42] * d18;
                        int i50 = 0;
                        while (i50 < singularValueDecomposition.f18m) {
                            double d22 = (singularValueDecomposition.f16U[i50][i42] * d18) + (singularValueDecomposition.f16U[i50][i49] * d19);
                            double d23 = d19;
                            singularValueDecomposition.f16U[i50][i49] = (singularValueDecomposition.f16U[i50][i42] * d20) + (singularValueDecomposition.f16U[i50][i49] * d18);
                            singularValueDecomposition.f16U[i50][i42] = d22;
                            i50++;
                            d19 = d23;
                        }
                        i42++;
                        d17 = d21;
                    }
                    i3 = min3;
                    break;
                case 3:
                    int i51 = i;
                    int i52 = min3 - 1;
                    double max2 = Math.max(Math.max(Math.max(Math.max(Math.abs(singularValueDecomposition.f20s[i52]), Math.abs(singularValueDecomposition.f20s[i38])), Math.abs(dArr3[i38])), Math.abs(singularValueDecomposition.f20s[i42])), Math.abs(dArr3[i42]));
                    double d24 = singularValueDecomposition.f20s[i52] / max2;
                    double d25 = singularValueDecomposition.f20s[i38] / max2;
                    double d26 = dArr3[i38] / max2;
                    double d27 = singularValueDecomposition.f20s[i42] / max2;
                    double d28 = dArr3[i42] / max2;
                    double d29 = ((d25 + d24) * (d25 - d24)) + (d26 * d26);
                    j = 4611686018427387904L;
                    double d30 = d29 / 2.0d;
                    double d31 = d26 * d24;
                    double d32 = d31 * d31;
                    if ((d30 != 0.0d) || (d32 != 0.0d)) {
                        i2 = i51;
                        double sqrt = Math.sqrt((d30 * d30) + d32);
                        if (d30 < 0.0d) {
                            sqrt = -sqrt;
                        }
                        d4 = d32 / (d30 + sqrt);
                    } else {
                        i2 = i51;
                        d4 = 0.0d;
                    }
                    double d33 = ((d27 + d24) * (d27 - d24)) + d4;
                    int i53 = i42;
                    double d34 = d27 * d28;
                    while (i53 < i52) {
                        double hypot3 = Maths.hypot(d33, d34);
                        double d35 = d33 / hypot3;
                        double d36 = d34 / hypot3;
                        if (i53 != i42) {
                            dArr3[i53 - 1] = hypot3;
                        }
                        double d37 = pow;
                        double d38 = (singularValueDecomposition.f20s[i53] * d35) + (dArr3[i53] * d36);
                        dArr3[i53] = (dArr3[i53] * d35) - (singularValueDecomposition.f20s[i53] * d36);
                        int i54 = i53 + 1;
                        int i55 = i52;
                        double d39 = pow2;
                        double d40 = d36 * singularValueDecomposition.f20s[i54];
                        singularValueDecomposition.f20s[i54] = singularValueDecomposition.f20s[i54] * d35;
                        int i56 = 0;
                        while (i56 < singularValueDecomposition.f19n) {
                            double d41 = (singularValueDecomposition.f17V[i56][i53] * d35) + (singularValueDecomposition.f17V[i56][i54] * d36);
                            int i57 = i38;
                            double[] dArr12 = dArr3;
                            double d42 = d36;
                            singularValueDecomposition.f17V[i56][i54] = ((-d36) * singularValueDecomposition.f17V[i56][i53]) + (singularValueDecomposition.f17V[i56][i54] * d35);
                            singularValueDecomposition.f17V[i56][i53] = d41;
                            i56++;
                            dArr3 = dArr12;
                            i38 = i57;
                            d36 = d42;
                        }
                        int i58 = i38;
                        double[] dArr13 = dArr3;
                        double hypot4 = Maths.hypot(d38, d40);
                        double d43 = d38 / hypot4;
                        double d44 = d40 / hypot4;
                        singularValueDecomposition.f20s[i53] = hypot4;
                        d33 = (singularValueDecomposition.f20s[i54] * d44) + (dArr13[i53] * d43);
                        double d45 = -d44;
                        singularValueDecomposition.f20s[i54] = (dArr13[i53] * d45) + (singularValueDecomposition.f20s[i54] * d43);
                        d34 = dArr13[i54] * d44;
                        dArr13[i54] = dArr13[i54] * d43;
                        if (i53 < singularValueDecomposition.f18m - 1) {
                            int i59 = 0;
                            while (i59 < singularValueDecomposition.f18m) {
                                double d46 = (singularValueDecomposition.f16U[i59][i53] * d43) + (singularValueDecomposition.f16U[i59][i54] * d44);
                                double d47 = d44;
                                singularValueDecomposition.f16U[i59][i54] = (singularValueDecomposition.f16U[i59][i53] * d45) + (singularValueDecomposition.f16U[i59][i54] * d43);
                                singularValueDecomposition.f16U[i59][i53] = d46;
                                i59++;
                                d44 = d47;
                            }
                        }
                        i53 = i54;
                        pow = d37;
                        pow2 = d39;
                        i52 = i55;
                        dArr3 = dArr13;
                        i38 = i58;
                    }
                    dArr = dArr3;
                    d3 = pow;
                    d2 = pow2;
                    dArr[i38] = d33;
                    continue;
                case 4:
                    if (singularValueDecomposition.f20s[i42] <= 0.0d) {
                        singularValueDecomposition.f20s[i42] = singularValueDecomposition.f20s[i42] < 0.0d ? -singularValueDecomposition.f20s[i42] : 0.0d;
                        int i60 = 0;
                        while (true) {
                            i5 = i;
                            if (i60 <= i5) {
                                singularValueDecomposition.f17V[i60][i42] = -singularValueDecomposition.f17V[i60][i42];
                                i60++;
                                i = i5;
                            }
                        }
                    } else {
                        i5 = i;
                    }
                    while (i42 < i5) {
                        int i61 = i42 + 1;
                        if (singularValueDecomposition.f20s[i42] >= singularValueDecomposition.f20s[i61]) {
                            min3--;
                            i2 = i5;
                            dArr = dArr3;
                            d3 = pow;
                            d2 = pow2;
                            j = 4611686018427387904L;
                            continue;
                        } else {
                            double d48 = singularValueDecomposition.f20s[i42];
                            singularValueDecomposition.f20s[i42] = singularValueDecomposition.f20s[i61];
                            singularValueDecomposition.f20s[i61] = d48;
                            if (i42 < singularValueDecomposition.f19n - 1) {
                                for (int i62 = 0; i62 < singularValueDecomposition.f19n; i62++) {
                                    double d49 = singularValueDecomposition.f17V[i62][i61];
                                    singularValueDecomposition.f17V[i62][i61] = singularValueDecomposition.f17V[i62][i42];
                                    singularValueDecomposition.f17V[i62][i42] = d49;
                                }
                            }
                            if (i42 < singularValueDecomposition.f18m - 1) {
                                for (int i63 = 0; i63 < singularValueDecomposition.f18m; i63++) {
                                    double d50 = singularValueDecomposition.f16U[i63][i61];
                                    singularValueDecomposition.f16U[i63][i61] = singularValueDecomposition.f16U[i63][i42];
                                    singularValueDecomposition.f16U[i63][i42] = d50;
                                }
                            }
                            i42 = i61;
                        }
                    }
                    min3--;
                    i2 = i5;
                    dArr = dArr3;
                    d3 = pow;
                    d2 = pow2;
                    j = 4611686018427387904L;
                    continue;
                default:
                    dArr = dArr3;
                    i3 = min3;
                    d3 = pow;
                    d2 = pow2;
                    i4 = i;
                    j = 4611686018427387904L;
                    break;
            }
            min3 = i3;
            singularValueDecomposition2 = singularValueDecomposition;
            long j2 = j;
            i22 = i2;
            pow = d3;
            pow2 = d2;
            dArr3 = dArr;
        }
        SingularValueDecomposition singularValueDecomposition3 = singularValueDecomposition2;
    }

    public Matrix getU() {
        return new Matrix(this.f16U, this.f18m, Math.min(this.f18m + 1, this.f19n));
    }

    public Matrix getV() {
        return new Matrix(this.f17V, this.f19n, this.f19n);
    }

    public double[] getSingularValues() {
        return this.f20s;
    }

    public Matrix getS() {
        Matrix matrix = new Matrix(this.f19n, this.f19n);
        double[][] array = matrix.getArray();
        for (int i = 0; i < this.f19n; i++) {
            for (int i2 = 0; i2 < this.f19n; i2++) {
                array[i][i2] = 0.0d;
            }
            array[i][i] = this.f20s[i];
        }
        return matrix;
    }

    public double norm2() {
        return this.f20s[0];
    }

    public double cond() {
        return this.f20s[0] / this.f20s[Math.min(this.f18m, this.f19n) - 1];
    }

    public int rank() {
        double max = ((double) Math.max(this.f18m, this.f19n)) * this.f20s[0] * Math.pow(2.0d, -52.0d);
        int i = 0;
        for (int i2 = 0; i2 < this.f20s.length; i2++) {
            if (this.f20s[i2] > max) {
                i++;
            }
        }
        return i;
    }
}

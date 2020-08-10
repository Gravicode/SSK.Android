package Jama.examples;

import Jama.EigenvalueDecomposition;
import Jama.LUDecomposition;
import Jama.Matrix;
import Jama.QRDecomposition;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.Date;

public class MagicSquareExample {
    public static Matrix magic(int i) {
        double[][] dArr = (double[][]) Array.newInstance(double.class, new int[]{i, i});
        if (i % 2 == 1) {
            int i2 = i + 1;
            int i3 = i2 / 2;
            for (int i4 = 0; i4 < i; i4++) {
                for (int i5 = 0; i5 < i; i5++) {
                    dArr[i5][i4] = (double) (((((i5 + i4) + i3) % i) * i) + ((((i4 * 2) + i5) + i2) % i) + 1);
                }
            }
        } else if (i % 4 == 0) {
            for (int i6 = 0; i6 < i; i6++) {
                int i7 = 0;
                while (i7 < i) {
                    int i8 = i7 + 1;
                    if ((i8 / 2) % 2 == ((i6 + 1) / 2) % 2) {
                        dArr[i7][i6] = (double) (((i * i) - (i7 * i)) - i6);
                    } else {
                        dArr[i7][i6] = (double) ((i7 * i) + i6 + 1);
                    }
                    i7 = i8;
                }
            }
        } else {
            int i9 = i / 2;
            int i10 = (i - 2) / 4;
            Matrix magic = magic(i9);
            for (int i11 = 0; i11 < i9; i11++) {
                for (int i12 = 0; i12 < i9; i12++) {
                    double d = magic.get(i12, i11);
                    dArr[i12][i11] = d;
                    int i13 = i11 + i9;
                    dArr[i12][i13] = ((double) (i9 * 2 * i9)) + d;
                    int i14 = i12 + i9;
                    dArr[i14][i11] = ((double) (i9 * 3 * i9)) + d;
                    dArr[i14][i13] = d + ((double) (i9 * i9));
                }
            }
            for (int i15 = 0; i15 < i9; i15++) {
                for (int i16 = 0; i16 < i10; i16++) {
                    double d2 = dArr[i15][i16];
                    int i17 = i15 + i9;
                    dArr[i15][i16] = dArr[i17][i16];
                    dArr[i17][i16] = d2;
                }
                for (int i18 = (i - i10) + 1; i18 < i; i18++) {
                    double d3 = dArr[i15][i18];
                    int i19 = i15 + i9;
                    dArr[i15][i18] = dArr[i19][i18];
                    dArr[i19][i18] = d3;
                }
            }
            double d4 = dArr[i10][0];
            int i20 = i9 + i10;
            dArr[i10][0] = dArr[i20][0];
            dArr[i20][0] = d4;
            double d5 = dArr[i10][i10];
            dArr[i10][i10] = dArr[i20][i10];
            dArr[i20][i10] = d5;
        }
        return new Matrix(dArr);
    }

    private static void print(String str) {
        System.out.print(str);
    }

    public static String fixedWidthDoubletoString(double d, int i, int i2) {
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(i2);
        decimalFormat.setMinimumFractionDigits(i2);
        decimalFormat.setGroupingUsed(false);
        String format = decimalFormat.format(d);
        while (format.length() < i) {
            StringBuilder sb = new StringBuilder();
            sb.append(" ");
            sb.append(format);
            format = sb.toString();
        }
        return format;
    }

    public static String fixedWidthIntegertoString(int i, int i2) {
        String num = Integer.toString(i);
        while (num.length() < i2) {
            StringBuilder sb = new StringBuilder();
            sb.append(" ");
            sb.append(num);
            num = sb.toString();
        }
        return num;
    }

    public static void main(String[] strArr) {
        print("\n    Test of Matrix Class, using magic squares.\n");
        print("    See MagicSquareExample.main() for an explanation.\n");
        print("\n      n     trace       max_eig   rank        cond      lu_res      qr_res\n\n");
        Date date = new Date();
        double pow = Math.pow(2.0d, -52.0d);
        for (int i = 3; i <= 32; i++) {
            print(fixedWidthIntegertoString(i, 7));
            Matrix magic = magic(i);
            print(fixedWidthIntegertoString((int) magic.trace(), 10));
            int i2 = i - 1;
            print(fixedWidthDoubletoString(new EigenvalueDecomposition(magic.plus(magic.transpose()).times(0.5d)).getRealEigenvalues()[i2], 14, 3));
            print(fixedWidthIntegertoString(magic.rank(), 7));
            double cond = magic.cond();
            print(cond < 1.0d / pow ? fixedWidthDoubletoString(cond, 12, 3) : "         Inf");
            LUDecomposition lUDecomposition = new LUDecomposition(magic);
            double d = ((double) i) * pow;
            print(fixedWidthDoubletoString(lUDecomposition.getL().times(lUDecomposition.getU()).minus(magic.getMatrix(lUDecomposition.getPivot(), 0, i2)).norm1() / d, 12, 3));
            QRDecomposition qRDecomposition = new QRDecomposition(magic);
            print(fixedWidthDoubletoString(qRDecomposition.getQ().times(qRDecomposition.getR()).minus(magic).norm1() / d, 12, 3));
            print("\n");
        }
        double time = ((double) (new Date().getTime() - date.getTime())) / 1000.0d;
        StringBuilder sb = new StringBuilder();
        sb.append("\nElapsed Time = ");
        sb.append(fixedWidthDoubletoString(time, 12, 3));
        sb.append(" seconds\n");
        print(sb.toString());
        print("Adios\n");
    }
}

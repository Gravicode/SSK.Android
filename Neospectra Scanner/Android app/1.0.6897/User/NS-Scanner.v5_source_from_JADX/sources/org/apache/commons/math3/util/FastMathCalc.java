package org.apache.commons.math3.util;

import java.io.PrintStream;
import org.apache.commons.math3.exception.DimensionMismatchException;

class FastMathCalc {
    private static final double[] FACT = {1.0d, 1.0d, 2.0d, 6.0d, 24.0d, 120.0d, 720.0d, 5040.0d, 40320.0d, 362880.0d, 3628800.0d, 3.99168E7d, 4.790016E8d, 6.2270208E9d, 8.71782912E10d, 1.307674368E12d, 2.0922789888E13d, 3.55687428096E14d, 6.402373705728E15d, 1.21645100408832E17d};
    private static final long HEX_40000000 = 1073741824;
    private static final double[][] LN_SPLIT_COEF = {new double[]{2.0d, 0.0d}, new double[]{0.6666666269302368d, 3.9736429850260626E-8d}, new double[]{0.3999999761581421d, 2.3841857910019882E-8d}, new double[]{0.2857142686843872d, 1.7029898543501842E-8d}, new double[]{0.2222222089767456d, 1.3245471311735498E-8d}, new double[]{0.1818181574344635d, 2.4384203044354907E-8d}, new double[]{0.1538461446762085d, 9.140260083262505E-9d}, new double[]{0.13333332538604736d, 9.220590270857665E-9d}, new double[]{0.11764700710773468d, 1.2393345855018391E-8d}, new double[]{0.10526403784751892d, 8.251545029714408E-9d}, new double[]{0.0952233225107193d, 1.2675934823758863E-8d}, new double[]{0.08713622391223907d, 1.1430250008909141E-8d}, new double[]{0.07842259109020233d, 2.404307984052299E-9d}, new double[]{0.08371849358081818d, 1.176342548272881E-8d}, new double[]{0.030589580535888672d, 1.2958646899018938E-9d}, new double[]{0.14982303977012634d, 1.225743062930824E-8d}};
    private static final String TABLE_END_DECL = "    };";
    private static final String TABLE_START_DECL = "    {";

    private FastMathCalc() {
    }

    private static void buildSinCosTables(double[] SINE_TABLE_A, double[] SINE_TABLE_B, double[] COSINE_TABLE_A, double[] COSINE_TABLE_B, int SINE_TABLE_LEN, double[] TANGENT_TABLE_A, double[] TANGENT_TABLE_B) {
        int i;
        int i2;
        double[] result;
        int i3 = SINE_TABLE_LEN;
        int i4 = 2;
        double[] result2 = new double[2];
        int i5 = 0;
        while (true) {
            i = 7;
            i2 = 1;
            if (i5 >= 7) {
                break;
            }
            double x = ((double) i5) / 8.0d;
            slowSin(x, result2);
            SINE_TABLE_A[i5] = result2[0];
            SINE_TABLE_B[i5] = result2[1];
            slowCos(x, result2);
            COSINE_TABLE_A[i5] = result2[0];
            COSINE_TABLE_B[i5] = result2[1];
            i5++;
        }
        while (true) {
            int i6 = i;
            if (i6 >= i3) {
                break;
            }
            double[] xs = new double[i4];
            double[] ys = new double[i4];
            double[] as = new double[i4];
            double[] bs = new double[i4];
            double[] temps = new double[i4];
            if ((i6 & 1) == 0) {
                xs[0] = SINE_TABLE_A[i6 / 2];
                xs[i2] = SINE_TABLE_B[i6 / 2];
                ys[0] = COSINE_TABLE_A[i6 / 2];
                ys[i2] = COSINE_TABLE_B[i6 / 2];
                splitMult(xs, ys, result2);
                SINE_TABLE_A[i6] = result2[0] * 2.0d;
                SINE_TABLE_B[i6] = result2[i2] * 2.0d;
                splitMult(ys, ys, as);
                splitMult(xs, xs, temps);
                double[] result3 = result2;
                temps[0] = -temps[0];
                temps[i2] = -temps[i2];
                result = result3;
                splitAdd(as, temps, result);
                COSINE_TABLE_A[i6] = result[0];
                COSINE_TABLE_B[i6] = result[i2];
            } else {
                result = result2;
                xs[0] = SINE_TABLE_A[i6 / 2];
                xs[i2] = SINE_TABLE_B[i6 / 2];
                ys[0] = COSINE_TABLE_A[i6 / 2];
                ys[i2] = COSINE_TABLE_B[i6 / 2];
                as[0] = SINE_TABLE_A[(i6 / 2) + i2];
                as[i2] = SINE_TABLE_B[(i6 / 2) + i2];
                bs[0] = COSINE_TABLE_A[(i6 / 2) + i2];
                bs[i2] = COSINE_TABLE_B[(i6 / 2) + i2];
                splitMult(xs, bs, temps);
                splitMult(ys, as, result);
                splitAdd(result, temps, result);
                SINE_TABLE_A[i6] = result[0];
                SINE_TABLE_B[i6] = result[i2];
                splitMult(ys, bs, result);
                splitMult(xs, as, temps);
                double[] dArr = ys;
                temps[0] = -temps[0];
                temps[1] = -temps[1];
                splitAdd(result, temps, result);
                COSINE_TABLE_A[i6] = result[0];
                COSINE_TABLE_B[i6] = result[1];
            }
            i = i6 + 1;
            result2 = result;
            i4 = 2;
            i2 = 1;
        }
        for (int i7 = 0; i7 < i3; i7++) {
            double[] ys2 = new double[2];
            double[] as2 = {COSINE_TABLE_A[i7], COSINE_TABLE_B[i7]};
            splitReciprocal(as2, ys2);
            splitMult(new double[]{SINE_TABLE_A[i7], SINE_TABLE_B[i7]}, ys2, as2);
            TANGENT_TABLE_A[i7] = as2[0];
            TANGENT_TABLE_B[i7] = as2[1];
        }
    }

    static double slowCos(double x, double[] result) {
        double[] xs = new double[2];
        double[] ys = new double[2];
        double[] facts = new double[2];
        double[] as = new double[2];
        split(x, xs);
        ys[1] = 0.0d;
        ys[0] = 0.0d;
        for (int i = FACT.length - 1; i >= 0; i--) {
            splitMult(xs, ys, as);
            ys[0] = as[0];
            ys[1] = as[1];
            if ((i & 1) == 0) {
                split(FACT[i], as);
                splitReciprocal(as, facts);
                if ((i & 2) != 0) {
                    facts[0] = -facts[0];
                    facts[1] = -facts[1];
                }
                splitAdd(ys, facts, as);
                ys[0] = as[0];
                ys[1] = as[1];
            }
        }
        if (result != null) {
            result[0] = ys[0];
            result[1] = ys[1];
        }
        return ys[0] + ys[1];
    }

    static double slowSin(double x, double[] result) {
        double[] xs = new double[2];
        double[] ys = new double[2];
        double[] facts = new double[2];
        double[] as = new double[2];
        split(x, xs);
        ys[1] = 0.0d;
        ys[0] = 0.0d;
        for (int i = FACT.length - 1; i >= 0; i--) {
            splitMult(xs, ys, as);
            ys[0] = as[0];
            ys[1] = as[1];
            if ((i & 1) != 0) {
                split(FACT[i], as);
                splitReciprocal(as, facts);
                if ((i & 2) != 0) {
                    facts[0] = -facts[0];
                    facts[1] = -facts[1];
                }
                splitAdd(ys, facts, as);
                ys[0] = as[0];
                ys[1] = as[1];
            }
        }
        if (result != null) {
            result[0] = ys[0];
            result[1] = ys[1];
        }
        return ys[0] + ys[1];
    }

    static double slowexp(double x, double[] result) {
        double[] xs = new double[2];
        double[] ys = new double[2];
        double[] facts = new double[2];
        double[] as = new double[2];
        split(x, xs);
        ys[1] = 0.0d;
        ys[0] = 0.0d;
        for (int i = FACT.length - 1; i >= 0; i--) {
            splitMult(xs, ys, as);
            ys[0] = as[0];
            ys[1] = as[1];
            split(FACT[i], as);
            splitReciprocal(as, facts);
            splitAdd(ys, facts, as);
            ys[0] = as[0];
            ys[1] = as[1];
        }
        if (result != null) {
            result[0] = ys[0];
            result[1] = ys[1];
        }
        return ys[0] + ys[1];
    }

    private static void split(double d, double[] split) {
        if (d >= 8.0E298d || d <= -8.0E298d) {
            split[0] = ((d + (9.313225746154785E-10d * d)) - d) * 1.073741824E9d;
            split[1] = d - split[0];
            return;
        }
        double a = 1.073741824E9d * d;
        split[0] = (d + a) - a;
        split[1] = d - split[0];
    }

    private static void resplit(double[] a) {
        double c = a[0] + a[1];
        double d = -((c - a[0]) - a[1]);
        if (c >= 8.0E298d || c <= -8.0E298d) {
            a[0] = ((c + (9.313225746154785E-10d * c)) - c) * 1.073741824E9d;
            a[1] = (c - a[0]) + d;
            return;
        }
        double z = 1.073741824E9d * c;
        a[0] = (c + z) - z;
        a[1] = (c - a[0]) + d;
    }

    private static void splitMult(double[] a, double[] b, double[] ans) {
        ans[0] = a[0] * b[0];
        ans[1] = (a[0] * b[1]) + (a[1] * b[0]) + (a[1] * b[1]);
        resplit(ans);
    }

    private static void splitAdd(double[] a, double[] b, double[] ans) {
        ans[0] = a[0] + b[0];
        ans[1] = a[1] + b[1];
        resplit(ans);
    }

    static void splitReciprocal(double[] in, double[] result) {
        if (in[0] == 0.0d) {
            in[0] = in[1];
            in[1] = 0.0d;
        }
        result[0] = 0.9999997615814209d / in[0];
        result[1] = ((in[0] * 2.384185791015625E-7d) - (in[1] * 0.9999997615814209d)) / ((in[0] * in[0]) + (in[0] * in[1]));
        if (result[1] != result[1]) {
            result[1] = 0.0d;
        }
        resplit(result);
        for (int i = 0; i < 2; i++) {
            result[1] = result[1] + (((((1.0d - (result[0] * in[0])) - (result[0] * in[1])) - (result[1] * in[0])) - (result[1] * in[1])) * (result[0] + result[1]));
        }
    }

    private static void quadMult(double[] a, double[] b, double[] result) {
        double[] xs = new double[2];
        double[] ys = new double[2];
        double[] zs = new double[2];
        split(a[0], xs);
        split(b[0], ys);
        splitMult(xs, ys, zs);
        result[0] = zs[0];
        result[1] = zs[1];
        split(b[1], ys);
        splitMult(xs, ys, zs);
        double tmp = result[0] + zs[0];
        result[1] = result[1] - ((tmp - result[0]) - zs[0]);
        result[0] = tmp;
        double tmp2 = result[0] + zs[1];
        result[1] = result[1] - ((tmp2 - result[0]) - zs[1]);
        result[0] = tmp2;
        split(a[1], xs);
        split(b[0], ys);
        splitMult(xs, ys, zs);
        double tmp3 = result[0] + zs[0];
        result[1] = result[1] - ((tmp3 - result[0]) - zs[0]);
        result[0] = tmp3;
        double tmp4 = result[0] + zs[1];
        result[1] = result[1] - ((tmp4 - result[0]) - zs[1]);
        result[0] = tmp4;
        split(a[1], xs);
        split(b[1], ys);
        splitMult(xs, ys, zs);
        double tmp5 = result[0] + zs[0];
        result[1] = result[1] - ((tmp5 - result[0]) - zs[0]);
        result[0] = tmp5;
        double tmp6 = result[0] + zs[1];
        result[1] = result[1] - ((tmp6 - result[0]) - zs[1]);
        result[0] = tmp6;
    }

    static double expint(int p, double[] result) {
        double[] as = new double[2];
        double[] ys = new double[2];
        double[] xs = {2.718281828459045d, 1.4456468917292502E-16d};
        split(1.0d, ys);
        while (p > 0) {
            if ((p & 1) != 0) {
                quadMult(ys, xs, as);
                ys[0] = as[0];
                ys[1] = as[1];
            }
            quadMult(xs, xs, as);
            xs[0] = as[0];
            xs[1] = as[1];
            p >>= 1;
        }
        if (result != null) {
            result[0] = ys[0];
            result[1] = ys[1];
            resplit(result);
        }
        return ys[0] + ys[1];
    }

    static double[] slowLog(double xi) {
        double[] x = new double[2];
        double[] x2 = new double[2];
        double[] y = new double[2];
        double[] a = new double[2];
        split(xi, x);
        x[0] = x[0] + 1.0d;
        resplit(x);
        splitReciprocal(x, a);
        x[0] = x[0] - 2.0d;
        resplit(x);
        splitMult(x, a, y);
        x[0] = y[0];
        x[1] = y[1];
        splitMult(x, x, x2);
        y[0] = LN_SPLIT_COEF[LN_SPLIT_COEF.length - 1][0];
        y[1] = LN_SPLIT_COEF[LN_SPLIT_COEF.length - 1][1];
        int i = LN_SPLIT_COEF.length - 2;
        while (true) {
            int i2 = i;
            if (i2 >= 0) {
                splitMult(y, x2, a);
                y[0] = a[0];
                y[1] = a[1];
                splitAdd(y, LN_SPLIT_COEF[i2], a);
                y[0] = a[0];
                y[1] = a[1];
                i = i2 - 1;
            } else {
                splitMult(y, x, a);
                y[0] = a[0];
                y[1] = a[1];
                return y;
            }
        }
    }

    static void printarray(PrintStream out, String name, int expectedLen, double[][] array2d) {
        PrintStream printStream = out;
        out.println(name);
        double[][] dArr = array2d;
        checkLen(expectedLen, dArr.length);
        printStream.println("    { ");
        double[][] arr$ = dArr;
        int len$ = arr$.length;
        int i = 0;
        int i$ = 0;
        while (i$ < len$) {
            double[] array = arr$[i$];
            printStream.print("        {");
            for (double d : array) {
                printStream.printf("%-25.25s", new Object[]{format(d)});
            }
            StringBuilder sb = new StringBuilder();
            sb.append("}, // ");
            int i2 = i + 1;
            sb.append(i);
            printStream.println(sb.toString());
            i$++;
            i = i2;
        }
        printStream.println(TABLE_END_DECL);
    }

    static void printarray(PrintStream out, String name, int expectedLen, double[] array) {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append("=");
        out.println(sb.toString());
        checkLen(expectedLen, array.length);
        out.println(TABLE_START_DECL);
        for (double d : array) {
            out.printf("        %s%n", new Object[]{format(d)});
        }
        out.println(TABLE_END_DECL);
    }

    static String format(double d) {
        if (d != d) {
            return "Double.NaN,";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(d >= 0.0d ? "+" : "");
        sb.append(Double.toString(d));
        sb.append("d,");
        return sb.toString();
    }

    private static void checkLen(int expectedLen, int actual) throws DimensionMismatchException {
        if (expectedLen != actual) {
            throw new DimensionMismatchException(actual, expectedLen);
        }
    }
}

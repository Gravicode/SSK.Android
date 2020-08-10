package Jama;

import Jama.util.Maths;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StreamTokenizer;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Vector;

public class Matrix implements Cloneable, Serializable {
    private static final long serialVersionUID = 1;

    /* renamed from: A */
    private double[][] f10A;

    /* renamed from: m */
    private int f11m;

    /* renamed from: n */
    private int f12n;

    public Matrix(int i, int i2) {
        this.f11m = i;
        this.f12n = i2;
        this.f10A = (double[][]) Array.newInstance(double.class, new int[]{i, i2});
    }

    public Matrix(int i, int i2, double d) {
        this.f11m = i;
        this.f12n = i2;
        this.f10A = (double[][]) Array.newInstance(double.class, new int[]{i, i2});
        for (int i3 = 0; i3 < i; i3++) {
            for (int i4 = 0; i4 < i2; i4++) {
                this.f10A[i3][i4] = d;
            }
        }
    }

    public Matrix(double[][] dArr) {
        this.f11m = dArr.length;
        this.f12n = dArr[0].length;
        for (int i = 0; i < this.f11m; i++) {
            if (dArr[i].length != this.f12n) {
                throw new IllegalArgumentException("All rows must have the same length.");
            }
        }
        this.f10A = dArr;
    }

    public Matrix(double[][] dArr, int i, int i2) {
        this.f10A = dArr;
        this.f11m = i;
        this.f12n = i2;
    }

    public Matrix(double[] dArr, int i) {
        this.f11m = i;
        this.f12n = i != 0 ? dArr.length / i : 0;
        if (this.f12n * i != dArr.length) {
            throw new IllegalArgumentException("Array length must be a multiple of m.");
        }
        this.f10A = (double[][]) Array.newInstance(double.class, new int[]{i, this.f12n});
        for (int i2 = 0; i2 < i; i2++) {
            for (int i3 = 0; i3 < this.f12n; i3++) {
                this.f10A[i2][i3] = dArr[(i3 * i) + i2];
            }
        }
    }

    public static Matrix constructWithCopy(double[][] dArr) {
        int length = dArr.length;
        int length2 = dArr[0].length;
        Matrix matrix = new Matrix(length, length2);
        double[][] array = matrix.getArray();
        for (int i = 0; i < length; i++) {
            if (dArr[i].length != length2) {
                throw new IllegalArgumentException("All rows must have the same length.");
            }
            for (int i2 = 0; i2 < length2; i2++) {
                array[i][i2] = dArr[i][i2];
            }
        }
        return matrix;
    }

    public Matrix copy() {
        Matrix matrix = new Matrix(this.f11m, this.f12n);
        double[][] array = matrix.getArray();
        for (int i = 0; i < this.f11m; i++) {
            for (int i2 = 0; i2 < this.f12n; i2++) {
                array[i][i2] = this.f10A[i][i2];
            }
        }
        return matrix;
    }

    public Object clone() {
        return copy();
    }

    public double[][] getArray() {
        return this.f10A;
    }

    public double[][] getArrayCopy() {
        double[][] dArr = (double[][]) Array.newInstance(double.class, new int[]{this.f11m, this.f12n});
        for (int i = 0; i < this.f11m; i++) {
            for (int i2 = 0; i2 < this.f12n; i2++) {
                dArr[i][i2] = this.f10A[i][i2];
            }
        }
        return dArr;
    }

    public double[] getColumnPackedCopy() {
        double[] dArr = new double[(this.f11m * this.f12n)];
        for (int i = 0; i < this.f11m; i++) {
            for (int i2 = 0; i2 < this.f12n; i2++) {
                dArr[(this.f11m * i2) + i] = this.f10A[i][i2];
            }
        }
        return dArr;
    }

    public double[] getRowPackedCopy() {
        double[] dArr = new double[(this.f11m * this.f12n)];
        for (int i = 0; i < this.f11m; i++) {
            for (int i2 = 0; i2 < this.f12n; i2++) {
                dArr[(this.f12n * i) + i2] = this.f10A[i][i2];
            }
        }
        return dArr;
    }

    public int getRowDimension() {
        return this.f11m;
    }

    public int getColumnDimension() {
        return this.f12n;
    }

    public double get(int i, int i2) {
        return this.f10A[i][i2];
    }

    public Matrix getMatrix(int i, int i2, int i3, int i4) {
        Matrix matrix = new Matrix((i2 - i) + 1, (i4 - i3) + 1);
        double[][] array = matrix.getArray();
        for (int i5 = i; i5 <= i2; i5++) {
            int i6 = i3;
            while (i6 <= i4) {
                try {
                    array[i5 - i][i6 - i3] = this.f10A[i5][i6];
                    i6++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new ArrayIndexOutOfBoundsException("Submatrix indices");
                }
            }
        }
        return matrix;
    }

    public Matrix getMatrix(int[] iArr, int[] iArr2) {
        Matrix matrix = new Matrix(iArr.length, iArr2.length);
        double[][] array = matrix.getArray();
        int i = 0;
        while (i < iArr.length) {
            try {
                for (int i2 = 0; i2 < iArr2.length; i2++) {
                    array[i][i2] = this.f10A[iArr[i]][iArr2[i2]];
                }
                i++;
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ArrayIndexOutOfBoundsException("Submatrix indices");
            }
        }
        return matrix;
    }

    public Matrix getMatrix(int i, int i2, int[] iArr) {
        Matrix matrix = new Matrix((i2 - i) + 1, iArr.length);
        double[][] array = matrix.getArray();
        for (int i3 = i; i3 <= i2; i3++) {
            int i4 = 0;
            while (i4 < iArr.length) {
                try {
                    array[i3 - i][i4] = this.f10A[i3][iArr[i4]];
                    i4++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new ArrayIndexOutOfBoundsException("Submatrix indices");
                }
            }
        }
        return matrix;
    }

    public Matrix getMatrix(int[] iArr, int i, int i2) {
        Matrix matrix = new Matrix(iArr.length, (i2 - i) + 1);
        double[][] array = matrix.getArray();
        int i3 = 0;
        while (i3 < iArr.length) {
            try {
                for (int i4 = i; i4 <= i2; i4++) {
                    array[i3][i4 - i] = this.f10A[iArr[i3]][i4];
                }
                i3++;
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ArrayIndexOutOfBoundsException("Submatrix indices");
            }
        }
        return matrix;
    }

    public void set(int i, int i2, double d) {
        this.f10A[i][i2] = d;
    }

    public void setMatrix(int i, int i2, int i3, int i4, Matrix matrix) {
        for (int i5 = i; i5 <= i2; i5++) {
            int i6 = i3;
            while (i6 <= i4) {
                try {
                    this.f10A[i5][i6] = matrix.get(i5 - i, i6 - i3);
                    i6++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new ArrayIndexOutOfBoundsException("Submatrix indices");
                }
            }
        }
    }

    public void setMatrix(int[] iArr, int[] iArr2, Matrix matrix) {
        int i = 0;
        while (i < iArr.length) {
            try {
                for (int i2 = 0; i2 < iArr2.length; i2++) {
                    this.f10A[iArr[i]][iArr2[i2]] = matrix.get(i, i2);
                }
                i++;
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ArrayIndexOutOfBoundsException("Submatrix indices");
            }
        }
    }

    public void setMatrix(int[] iArr, int i, int i2, Matrix matrix) {
        int i3 = 0;
        while (i3 < iArr.length) {
            try {
                for (int i4 = i; i4 <= i2; i4++) {
                    this.f10A[iArr[i3]][i4] = matrix.get(i3, i4 - i);
                }
                i3++;
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ArrayIndexOutOfBoundsException("Submatrix indices");
            }
        }
    }

    public void setMatrix(int i, int i2, int[] iArr, Matrix matrix) {
        for (int i3 = i; i3 <= i2; i3++) {
            int i4 = 0;
            while (i4 < iArr.length) {
                try {
                    this.f10A[i3][iArr[i4]] = matrix.get(i3 - i, i4);
                    i4++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new ArrayIndexOutOfBoundsException("Submatrix indices");
                }
            }
        }
    }

    public Matrix transpose() {
        Matrix matrix = new Matrix(this.f12n, this.f11m);
        double[][] array = matrix.getArray();
        for (int i = 0; i < this.f11m; i++) {
            for (int i2 = 0; i2 < this.f12n; i2++) {
                array[i2][i] = this.f10A[i][i2];
            }
        }
        return matrix;
    }

    public double norm1() {
        double d = 0.0d;
        for (int i = 0; i < this.f12n; i++) {
            double d2 = 0.0d;
            for (int i2 = 0; i2 < this.f11m; i2++) {
                d2 += Math.abs(this.f10A[i2][i]);
            }
            d = Math.max(d, d2);
        }
        return d;
    }

    public double norm2() {
        return new SingularValueDecomposition(this).norm2();
    }

    public double normInf() {
        double d = 0.0d;
        for (int i = 0; i < this.f11m; i++) {
            double d2 = 0.0d;
            for (int i2 = 0; i2 < this.f12n; i2++) {
                d2 += Math.abs(this.f10A[i][i2]);
            }
            d = Math.max(d, d2);
        }
        return d;
    }

    public double normF() {
        double d = 0.0d;
        int i = 0;
        while (i < this.f11m) {
            double d2 = d;
            for (int i2 = 0; i2 < this.f12n; i2++) {
                d2 = Maths.hypot(d2, this.f10A[i][i2]);
            }
            i++;
            d = d2;
        }
        return d;
    }

    public Matrix uminus() {
        Matrix matrix = new Matrix(this.f11m, this.f12n);
        double[][] array = matrix.getArray();
        for (int i = 0; i < this.f11m; i++) {
            for (int i2 = 0; i2 < this.f12n; i2++) {
                array[i][i2] = -this.f10A[i][i2];
            }
        }
        return matrix;
    }

    public Matrix plus(Matrix matrix) {
        checkMatrixDimensions(matrix);
        Matrix matrix2 = new Matrix(this.f11m, this.f12n);
        double[][] array = matrix2.getArray();
        for (int i = 0; i < this.f11m; i++) {
            for (int i2 = 0; i2 < this.f12n; i2++) {
                array[i][i2] = this.f10A[i][i2] + matrix.f10A[i][i2];
            }
        }
        return matrix2;
    }

    public Matrix plusEquals(Matrix matrix) {
        checkMatrixDimensions(matrix);
        for (int i = 0; i < this.f11m; i++) {
            for (int i2 = 0; i2 < this.f12n; i2++) {
                this.f10A[i][i2] = this.f10A[i][i2] + matrix.f10A[i][i2];
            }
        }
        return this;
    }

    public Matrix minus(Matrix matrix) {
        checkMatrixDimensions(matrix);
        Matrix matrix2 = new Matrix(this.f11m, this.f12n);
        double[][] array = matrix2.getArray();
        for (int i = 0; i < this.f11m; i++) {
            for (int i2 = 0; i2 < this.f12n; i2++) {
                array[i][i2] = this.f10A[i][i2] - matrix.f10A[i][i2];
            }
        }
        return matrix2;
    }

    public Matrix minusEquals(Matrix matrix) {
        checkMatrixDimensions(matrix);
        for (int i = 0; i < this.f11m; i++) {
            for (int i2 = 0; i2 < this.f12n; i2++) {
                this.f10A[i][i2] = this.f10A[i][i2] - matrix.f10A[i][i2];
            }
        }
        return this;
    }

    public Matrix arrayTimes(Matrix matrix) {
        checkMatrixDimensions(matrix);
        Matrix matrix2 = new Matrix(this.f11m, this.f12n);
        double[][] array = matrix2.getArray();
        for (int i = 0; i < this.f11m; i++) {
            for (int i2 = 0; i2 < this.f12n; i2++) {
                array[i][i2] = this.f10A[i][i2] * matrix.f10A[i][i2];
            }
        }
        return matrix2;
    }

    public Matrix arrayTimesEquals(Matrix matrix) {
        checkMatrixDimensions(matrix);
        for (int i = 0; i < this.f11m; i++) {
            for (int i2 = 0; i2 < this.f12n; i2++) {
                this.f10A[i][i2] = this.f10A[i][i2] * matrix.f10A[i][i2];
            }
        }
        return this;
    }

    public Matrix arrayRightDivide(Matrix matrix) {
        checkMatrixDimensions(matrix);
        Matrix matrix2 = new Matrix(this.f11m, this.f12n);
        double[][] array = matrix2.getArray();
        for (int i = 0; i < this.f11m; i++) {
            for (int i2 = 0; i2 < this.f12n; i2++) {
                array[i][i2] = this.f10A[i][i2] / matrix.f10A[i][i2];
            }
        }
        return matrix2;
    }

    public Matrix arrayRightDivideEquals(Matrix matrix) {
        checkMatrixDimensions(matrix);
        for (int i = 0; i < this.f11m; i++) {
            for (int i2 = 0; i2 < this.f12n; i2++) {
                this.f10A[i][i2] = this.f10A[i][i2] / matrix.f10A[i][i2];
            }
        }
        return this;
    }

    public Matrix arrayLeftDivide(Matrix matrix) {
        checkMatrixDimensions(matrix);
        Matrix matrix2 = new Matrix(this.f11m, this.f12n);
        double[][] array = matrix2.getArray();
        for (int i = 0; i < this.f11m; i++) {
            for (int i2 = 0; i2 < this.f12n; i2++) {
                array[i][i2] = matrix.f10A[i][i2] / this.f10A[i][i2];
            }
        }
        return matrix2;
    }

    public Matrix arrayLeftDivideEquals(Matrix matrix) {
        checkMatrixDimensions(matrix);
        for (int i = 0; i < this.f11m; i++) {
            for (int i2 = 0; i2 < this.f12n; i2++) {
                this.f10A[i][i2] = matrix.f10A[i][i2] / this.f10A[i][i2];
            }
        }
        return this;
    }

    public Matrix times(double d) {
        Matrix matrix = new Matrix(this.f11m, this.f12n);
        double[][] array = matrix.getArray();
        for (int i = 0; i < this.f11m; i++) {
            for (int i2 = 0; i2 < this.f12n; i2++) {
                array[i][i2] = this.f10A[i][i2] * d;
            }
        }
        return matrix;
    }

    public Matrix timesEquals(double d) {
        for (int i = 0; i < this.f11m; i++) {
            for (int i2 = 0; i2 < this.f12n; i2++) {
                this.f10A[i][i2] = this.f10A[i][i2] * d;
            }
        }
        return this;
    }

    public Matrix times(Matrix matrix) {
        if (matrix.f11m != this.f12n) {
            throw new IllegalArgumentException("Matrix inner dimensions must agree.");
        }
        Matrix matrix2 = new Matrix(this.f11m, matrix.f12n);
        double[][] array = matrix2.getArray();
        double[] dArr = new double[this.f12n];
        for (int i = 0; i < matrix.f12n; i++) {
            for (int i2 = 0; i2 < this.f12n; i2++) {
                dArr[i2] = matrix.f10A[i2][i];
            }
            for (int i3 = 0; i3 < this.f11m; i3++) {
                double[] dArr2 = this.f10A[i3];
                double d = 0.0d;
                for (int i4 = 0; i4 < this.f12n; i4++) {
                    d += dArr2[i4] * dArr[i4];
                }
                array[i3][i] = d;
            }
        }
        return matrix2;
    }

    /* renamed from: lu */
    public LUDecomposition mo43lu() {
        return new LUDecomposition(this);
    }

    /* renamed from: qr */
    public QRDecomposition mo56qr() {
        return new QRDecomposition(this);
    }

    public CholeskyDecomposition chol() {
        return new CholeskyDecomposition(this);
    }

    public SingularValueDecomposition svd() {
        return new SingularValueDecomposition(this);
    }

    public EigenvalueDecomposition eig() {
        return new EigenvalueDecomposition(this);
    }

    public Matrix solve(Matrix matrix) {
        return this.f11m == this.f12n ? new LUDecomposition(this).solve(matrix) : new QRDecomposition(this).solve(matrix);
    }

    public Matrix solveTranspose(Matrix matrix) {
        return transpose().solve(matrix.transpose());
    }

    public Matrix inverse() {
        return solve(identity(this.f11m, this.f11m));
    }

    public double det() {
        return new LUDecomposition(this).det();
    }

    public int rank() {
        return new SingularValueDecomposition(this).rank();
    }

    public double cond() {
        return new SingularValueDecomposition(this).cond();
    }

    public double trace() {
        double d = 0.0d;
        for (int i = 0; i < Math.min(this.f11m, this.f12n); i++) {
            d += this.f10A[i][i];
        }
        return d;
    }

    public static Matrix random(int i, int i2) {
        Matrix matrix = new Matrix(i, i2);
        double[][] array = matrix.getArray();
        for (int i3 = 0; i3 < i; i3++) {
            for (int i4 = 0; i4 < i2; i4++) {
                array[i3][i4] = Math.random();
            }
        }
        return matrix;
    }

    public static Matrix identity(int i, int i2) {
        Matrix matrix = new Matrix(i, i2);
        double[][] array = matrix.getArray();
        int i3 = 0;
        while (i3 < i) {
            int i4 = 0;
            while (i4 < i2) {
                array[i3][i4] = i3 == i4 ? 1.0d : 0.0d;
                i4++;
            }
            i3++;
        }
        return matrix;
    }

    public void print(int i, int i2) {
        print(new PrintWriter(System.out, true), i, i2);
    }

    public void print(PrintWriter printWriter, int i, int i2) {
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        decimalFormat.setMinimumIntegerDigits(1);
        decimalFormat.setMaximumFractionDigits(i2);
        decimalFormat.setMinimumFractionDigits(i2);
        decimalFormat.setGroupingUsed(false);
        print(printWriter, (NumberFormat) decimalFormat, i + 2);
    }

    public void print(NumberFormat numberFormat, int i) {
        print(new PrintWriter(System.out, true), numberFormat, i);
    }

    public void print(PrintWriter printWriter, NumberFormat numberFormat, int i) {
        printWriter.println();
        for (int i2 = 0; i2 < this.f11m; i2++) {
            for (int i3 = 0; i3 < this.f12n; i3++) {
                String format = numberFormat.format(this.f10A[i2][i3]);
                int max = Math.max(1, i - format.length());
                for (int i4 = 0; i4 < max; i4++) {
                    printWriter.print(' ');
                }
                printWriter.print(format);
            }
            printWriter.println();
        }
        printWriter.println();
    }

    public static Matrix read(BufferedReader bufferedReader) throws IOException {
        StreamTokenizer streamTokenizer = new StreamTokenizer(bufferedReader);
        streamTokenizer.resetSyntax();
        streamTokenizer.wordChars(0, 255);
        streamTokenizer.whitespaceChars(0, 32);
        streamTokenizer.eolIsSignificant(true);
        Vector vector = new Vector();
        do {
        } while (streamTokenizer.nextToken() == 10);
        if (streamTokenizer.ttype == -1) {
            throw new IOException("Unexpected EOF on matrix read.");
        }
        do {
            vector.addElement(Double.valueOf(streamTokenizer.sval));
        } while (streamTokenizer.nextToken() == -3);
        int size = vector.size();
        double[] dArr = new double[size];
        for (int i = 0; i < size; i++) {
            dArr[i] = ((Double) vector.elementAt(i)).doubleValue();
        }
        Vector vector2 = new Vector();
        vector2.addElement(dArr);
        while (streamTokenizer.nextToken() == -3) {
            double[] dArr2 = new double[size];
            vector2.addElement(dArr2);
            int i2 = 0;
            while (i2 < size) {
                int i3 = i2 + 1;
                dArr2[i2] = Double.valueOf(streamTokenizer.sval).doubleValue();
                if (streamTokenizer.nextToken() == -3) {
                    i2 = i3;
                } else if (i3 < size) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Row ");
                    sb.append(vector2.size());
                    sb.append(" is too short.");
                    throw new IOException(sb.toString());
                }
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Row ");
            sb2.append(vector2.size());
            sb2.append(" is too long.");
            throw new IOException(sb2.toString());
        }
        double[][] dArr3 = new double[vector2.size()][];
        vector2.copyInto(dArr3);
        return new Matrix(dArr3);
    }

    private void checkMatrixDimensions(Matrix matrix) {
        if (matrix.f11m != this.f11m || matrix.f12n != this.f12n) {
            throw new IllegalArgumentException("Matrix dimensions must agree.");
        }
    }
}

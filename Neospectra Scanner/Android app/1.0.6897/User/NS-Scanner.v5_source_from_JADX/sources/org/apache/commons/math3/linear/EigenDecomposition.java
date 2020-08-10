package org.apache.commons.math3.linear;

import java.lang.reflect.Array;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathUnsupportedOperationException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import org.apache.poi.hssf.record.formula.IntPtg;

public class EigenDecomposition {
    private static final double EPSILON = 1.0E-12d;
    private RealMatrix cachedD;
    private RealMatrix cachedV;
    private RealMatrix cachedVt;
    private ArrayRealVector[] eigenvectors;
    private double[] imagEigenvalues;
    private final boolean isSymmetric;
    private double[] main;
    private byte maxIter;
    private double[] realEigenvalues;
    private double[] secondary;
    private TriDiagonalTransformer transformer;

    private static class Solver implements DecompositionSolver {
        private final ArrayRealVector[] eigenvectors;
        private double[] imagEigenvalues;
        private double[] realEigenvalues;

        private Solver(double[] realEigenvalues2, double[] imagEigenvalues2, ArrayRealVector[] eigenvectors2) {
            this.realEigenvalues = realEigenvalues2;
            this.imagEigenvalues = imagEigenvalues2;
            this.eigenvectors = eigenvectors2;
        }

        public RealVector solve(RealVector b) {
            if (!isNonSingular()) {
                throw new SingularMatrixException();
            }
            int m = this.realEigenvalues.length;
            if (b.getDimension() != m) {
                throw new DimensionMismatchException(b.getDimension(), m);
            }
            double[] bp = new double[m];
            for (int i = 0; i < m; i++) {
                ArrayRealVector v = this.eigenvectors[i];
                double[] vData = v.getDataRef();
                double s = v.dotProduct(b) / this.realEigenvalues[i];
                for (int j = 0; j < m; j++) {
                    bp[j] = bp[j] + (vData[j] * s);
                }
            }
            return new ArrayRealVector(bp, false);
        }

        public RealMatrix solve(RealMatrix b) {
            if (!isNonSingular()) {
                throw new SingularMatrixException();
            }
            int m = this.realEigenvalues.length;
            if (b.getRowDimension() != m) {
                throw new DimensionMismatchException(b.getRowDimension(), m);
            }
            int nColB = b.getColumnDimension();
            double[][] bp = (double[][]) Array.newInstance(double.class, new int[]{m, nColB});
            double[] tmpCol = new double[m];
            for (int k = 0; k < nColB; k++) {
                for (int i = 0; i < m; i++) {
                    tmpCol[i] = b.getEntry(i, k);
                    bp[i][k] = 0.0d;
                }
                RealMatrix realMatrix = b;
                for (int i2 = 0; i2 < m; i2++) {
                    ArrayRealVector v = this.eigenvectors[i2];
                    double[] vData = v.getDataRef();
                    double s = 0.0d;
                    for (int j = 0; j < m; j++) {
                        s += v.getEntry(j) * tmpCol[j];
                    }
                    double s2 = s / this.realEigenvalues[i2];
                    for (int j2 = 0; j2 < m; j2++) {
                        double[] dArr = bp[j2];
                        dArr[k] = dArr[k] + (vData[j2] * s2);
                    }
                }
            }
            RealMatrix realMatrix2 = b;
            return new Array2DRowRealMatrix(bp, false);
        }

        public boolean isNonSingular() {
            double largestEigenvalueNorm = 0.0d;
            for (int i = 0; i < this.realEigenvalues.length; i++) {
                largestEigenvalueNorm = FastMath.max(largestEigenvalueNorm, eigenvalueNorm(i));
            }
            if (largestEigenvalueNorm == 0.0d) {
                return false;
            }
            for (int i2 = 0; i2 < this.realEigenvalues.length; i2++) {
                if (Precision.equals(eigenvalueNorm(i2) / largestEigenvalueNorm, 0.0d, 1.0E-12d)) {
                    return false;
                }
            }
            return true;
        }

        private double eigenvalueNorm(int i) {
            double re = this.realEigenvalues[i];
            double im = this.imagEigenvalues[i];
            return FastMath.sqrt((re * re) + (im * im));
        }

        public RealMatrix getInverse() {
            if (!isNonSingular()) {
                throw new SingularMatrixException();
            }
            int m = this.realEigenvalues.length;
            double[][] invData = (double[][]) Array.newInstance(double.class, new int[]{m, m});
            for (int i = 0; i < m; i++) {
                double[] invI = invData[i];
                for (int j = 0; j < m; j++) {
                    double invIJ = 0.0d;
                    for (int k = 0; k < m; k++) {
                        double[] vK = this.eigenvectors[k].getDataRef();
                        invIJ += (vK[i] * vK[j]) / this.realEigenvalues[k];
                    }
                    invI[j] = invIJ;
                }
            }
            return MatrixUtils.createRealMatrix(invData);
        }
    }

    public EigenDecomposition(RealMatrix matrix) throws MathArithmeticException {
        this.maxIter = IntPtg.sid;
        this.isSymmetric = MatrixUtils.isSymmetric(matrix, ((double) (matrix.getRowDimension() * 10 * matrix.getColumnDimension())) * Precision.EPSILON);
        if (this.isSymmetric) {
            transformToTridiagonal(matrix);
            findEigenVectors(this.transformer.getQ().getData());
            return;
        }
        findEigenVectorsFromSchur(transformToSchur(matrix));
    }

    @Deprecated
    public EigenDecomposition(RealMatrix matrix, double splitTolerance) throws MathArithmeticException {
        this(matrix);
    }

    public EigenDecomposition(double[] main2, double[] secondary2) {
        this.maxIter = IntPtg.sid;
        this.isSymmetric = true;
        this.main = (double[]) main2.clone();
        this.secondary = (double[]) secondary2.clone();
        this.transformer = null;
        int size = main2.length;
        double[][] z = (double[][]) Array.newInstance(double.class, new int[]{size, size});
        for (int i = 0; i < size; i++) {
            z[i][i] = 1.0d;
        }
        findEigenVectors(z);
    }

    @Deprecated
    public EigenDecomposition(double[] main2, double[] secondary2, double splitTolerance) {
        this(main2, secondary2);
    }

    public RealMatrix getV() {
        if (this.cachedV == null) {
            int m = this.eigenvectors.length;
            this.cachedV = MatrixUtils.createRealMatrix(m, m);
            for (int k = 0; k < m; k++) {
                this.cachedV.setColumnVector(k, this.eigenvectors[k]);
            }
        }
        return this.cachedV;
    }

    public RealMatrix getD() {
        if (this.cachedD == null) {
            this.cachedD = MatrixUtils.createRealDiagonalMatrix(this.realEigenvalues);
            for (int i = 0; i < this.imagEigenvalues.length; i++) {
                if (Precision.compareTo(this.imagEigenvalues[i], 0.0d, 1.0E-12d) > 0) {
                    this.cachedD.setEntry(i, i + 1, this.imagEigenvalues[i]);
                } else if (Precision.compareTo(this.imagEigenvalues[i], 0.0d, 1.0E-12d) < 0) {
                    this.cachedD.setEntry(i, i - 1, this.imagEigenvalues[i]);
                }
            }
        }
        return this.cachedD;
    }

    public RealMatrix getVT() {
        if (this.cachedVt == null) {
            int m = this.eigenvectors.length;
            this.cachedVt = MatrixUtils.createRealMatrix(m, m);
            for (int k = 0; k < m; k++) {
                this.cachedVt.setRowVector(k, this.eigenvectors[k]);
            }
        }
        return this.cachedVt;
    }

    public boolean hasComplexEigenvalues() {
        for (double equals : this.imagEigenvalues) {
            if (!Precision.equals(equals, 0.0d, 1.0E-12d)) {
                return true;
            }
        }
        return false;
    }

    public double[] getRealEigenvalues() {
        return (double[]) this.realEigenvalues.clone();
    }

    public double getRealEigenvalue(int i) {
        return this.realEigenvalues[i];
    }

    public double[] getImagEigenvalues() {
        return (double[]) this.imagEigenvalues.clone();
    }

    public double getImagEigenvalue(int i) {
        return this.imagEigenvalues[i];
    }

    public RealVector getEigenvector(int i) {
        return this.eigenvectors[i].copy();
    }

    public double getDeterminant() {
        double determinant = 1.0d;
        for (double lambda : this.realEigenvalues) {
            determinant *= lambda;
        }
        return determinant;
    }

    public RealMatrix getSquareRoot() {
        if (!this.isSymmetric) {
            throw new MathUnsupportedOperationException();
        }
        double[] sqrtEigenValues = new double[this.realEigenvalues.length];
        for (int i = 0; i < this.realEigenvalues.length; i++) {
            double eigen = this.realEigenvalues[i];
            if (eigen <= 0.0d) {
                throw new MathUnsupportedOperationException();
            }
            sqrtEigenValues[i] = FastMath.sqrt(eigen);
        }
        RealMatrix sqrtEigen = MatrixUtils.createRealDiagonalMatrix(sqrtEigenValues);
        RealMatrix v = getV();
        return v.multiply(sqrtEigen).multiply(getVT());
    }

    public DecompositionSolver getSolver() {
        if (!hasComplexEigenvalues()) {
            return new Solver(this.realEigenvalues, this.imagEigenvalues, this.eigenvectors);
        }
        throw new MathUnsupportedOperationException();
    }

    private void transformToTridiagonal(RealMatrix matrix) {
        this.transformer = new TriDiagonalTransformer(matrix);
        this.main = this.transformer.getMainDiagonalRef();
        this.secondary = this.transformer.getSecondaryDiagonalRef();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:62:0x0212, code lost:
        r5 = r5 + 1;
        r8 = r35;
        r4 = 0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void findEigenVectors(double[][] r42) {
        /*
            r41 = this;
            r0 = r41
            java.lang.Object r1 = r42.clone()
            double[][] r1 = (double[][]) r1
            double[] r2 = r0.main
            int r2 = r2.length
            double[] r3 = new double[r2]
            r0.realEigenvalues = r3
            double[] r3 = new double[r2]
            r0.imagEigenvalues = r3
            double[] r3 = new double[r2]
            r4 = 0
            r5 = 0
        L_0x0017:
            int r6 = r2 + -1
            if (r5 >= r6) goto L_0x002c
            double[] r6 = r0.realEigenvalues
            double[] r7 = r0.main
            r8 = r7[r5]
            r6[r5] = r8
            double[] r6 = r0.secondary
            r7 = r6[r5]
            r3[r5] = r7
            int r5 = r5 + 1
            goto L_0x0017
        L_0x002c:
            double[] r5 = r0.realEigenvalues
            int r6 = r2 + -1
            double[] r7 = r0.main
            int r8 = r2 + -1
            r8 = r7[r8]
            r5[r6] = r8
            int r5 = r2 + -1
            r6 = 0
            r3[r5] = r6
            r8 = 0
            r5 = 0
        L_0x0041:
            if (r5 >= r2) goto L_0x006a
            double[] r10 = r0.realEigenvalues
            r11 = r10[r5]
            double r10 = org.apache.commons.math3.util.FastMath.abs(r11)
            int r10 = (r10 > r8 ? 1 : (r10 == r8 ? 0 : -1))
            if (r10 <= 0) goto L_0x0057
            double[] r10 = r0.realEigenvalues
            r11 = r10[r5]
            double r8 = org.apache.commons.math3.util.FastMath.abs(r11)
        L_0x0057:
            r10 = r3[r5]
            double r10 = org.apache.commons.math3.util.FastMath.abs(r10)
            int r10 = (r10 > r8 ? 1 : (r10 == r8 ? 0 : -1))
            if (r10 <= 0) goto L_0x0067
            r10 = r3[r5]
            double r8 = org.apache.commons.math3.util.FastMath.abs(r10)
        L_0x0067:
            int r5 = r5 + 1
            goto L_0x0041
        L_0x006a:
            int r5 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1))
            if (r5 == 0) goto L_0x0098
            r5 = 0
        L_0x006f:
            if (r5 >= r2) goto L_0x0098
            double[] r10 = r0.realEigenvalues
            r11 = r10[r5]
            double r10 = org.apache.commons.math3.util.FastMath.abs(r11)
            double r12 = org.apache.commons.math3.util.Precision.EPSILON
            double r12 = r12 * r8
            int r10 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
            if (r10 > 0) goto L_0x0085
            double[] r10 = r0.realEigenvalues
            r10[r5] = r6
        L_0x0085:
            r10 = r3[r5]
            double r10 = org.apache.commons.math3.util.FastMath.abs(r10)
            double r12 = org.apache.commons.math3.util.Precision.EPSILON
            double r12 = r12 * r8
            int r10 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
            if (r10 > 0) goto L_0x0095
            r3[r5] = r6
        L_0x0095:
            int r5 = r5 + 1
            goto L_0x006f
        L_0x0098:
            r5 = 0
        L_0x0099:
            if (r5 >= r2) goto L_0x0222
            r10 = 0
        L_0x009c:
            r11 = r5
        L_0x009d:
            int r12 = r2 + -1
            if (r11 >= r12) goto L_0x00c5
            double[] r12 = r0.realEigenvalues
            r13 = r12[r11]
            double r12 = org.apache.commons.math3.util.FastMath.abs(r13)
            double[] r14 = r0.realEigenvalues
            int r15 = r11 + 1
            r6 = r14[r15]
            double r6 = org.apache.commons.math3.util.FastMath.abs(r6)
            double r12 = r12 + r6
            r6 = r3[r11]
            double r6 = org.apache.commons.math3.util.FastMath.abs(r6)
            double r6 = r6 + r12
            int r6 = (r6 > r12 ? 1 : (r6 == r12 ? 0 : -1))
            if (r6 != 0) goto L_0x00c0
            goto L_0x00c5
        L_0x00c0:
            int r11 = r11 + 1
            r6 = 0
            goto L_0x009d
        L_0x00c5:
            if (r11 == r5) goto L_0x020e
            byte r6 = r0.maxIter
            if (r10 != r6) goto L_0x00db
            org.apache.commons.math3.exception.MaxCountExceededException r6 = new org.apache.commons.math3.exception.MaxCountExceededException
            org.apache.commons.math3.exception.util.LocalizedFormats r7 = org.apache.commons.math3.exception.util.LocalizedFormats.CONVERGENCE_FAILED
            byte r12 = r0.maxIter
            java.lang.Byte r12 = java.lang.Byte.valueOf(r12)
            java.lang.Object[] r4 = new java.lang.Object[r4]
            r6.<init>(r7, r12, r4)
            throw r6
        L_0x00db:
            int r10 = r10 + 1
            double[] r6 = r0.realEigenvalues
            int r7 = r5 + 1
            r12 = r6[r7]
            double[] r6 = r0.realEigenvalues
            r14 = r6[r5]
            double r12 = r12 - r14
            r6 = r3[r5]
            r14 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r6 = r6 * r14
            double r12 = r12 / r6
            double r6 = r12 * r12
            r18 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r6 = r6 + r18
            double r6 = org.apache.commons.math3.util.FastMath.sqrt(r6)
            r16 = 0
            int r20 = (r12 > r16 ? 1 : (r12 == r16 ? 0 : -1))
            if (r20 >= 0) goto L_0x0112
            double[] r4 = r0.realEigenvalues
            r21 = r4[r11]
            double[] r4 = r0.realEigenvalues
            r23 = r4[r5]
            double r21 = r21 - r23
            r23 = r3[r5]
            double r25 = r12 - r6
            double r23 = r23 / r25
            double r21 = r21 + r23
        L_0x0111:
            goto L_0x0125
        L_0x0112:
            double[] r4 = r0.realEigenvalues
            r21 = r4[r11]
            double[] r4 = r0.realEigenvalues
            r23 = r4[r5]
            double r21 = r21 - r23
            r23 = r3[r5]
            double r25 = r12 + r6
            double r23 = r23 / r25
            double r21 = r21 + r23
            goto L_0x0111
        L_0x0125:
            r12 = 0
            r23 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            r25 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            int r4 = r11 + -1
            r39 = r6
            r6 = r21
            r21 = r12
            r12 = r39
        L_0x0135:
            if (r4 < r5) goto L_0x01f2
            r27 = r3[r4]
            double r14 = r23 * r27
            r27 = r3[r4]
            double r27 = r27 * r25
            double r31 = org.apache.commons.math3.util.FastMath.abs(r14)
            double r33 = org.apache.commons.math3.util.FastMath.abs(r6)
            int r20 = (r31 > r33 ? 1 : (r31 == r33 ? 0 : -1))
            if (r20 < 0) goto L_0x0165
            double r25 = r6 / r14
            double r31 = r25 * r25
            r35 = r8
            double r8 = r31 + r18
            double r8 = org.apache.commons.math3.util.FastMath.sqrt(r8)
            int r12 = r4 + 1
            double r31 = r14 * r8
            r3[r12] = r31
            double r12 = r18 / r8
            double r25 = r25 * r12
            r23 = r12
            r12 = r8
            goto L_0x0183
        L_0x0165:
            r35 = r8
            double r8 = r14 / r6
            double r23 = r8 * r8
            r20 = 0
            r37 = r12
            double r12 = r23 + r18
            double r12 = org.apache.commons.math3.util.FastMath.sqrt(r12)
            int r20 = r4 + 1
            double r23 = r6 * r12
            r3[r20] = r23
            double r23 = r18 / r12
            double r8 = r8 * r23
            r25 = r23
            r23 = r8
        L_0x0183:
            int r8 = r4 + 1
            r8 = r3[r8]
            r16 = 0
            int r8 = (r8 > r16 ? 1 : (r8 == r16 ? 0 : -1))
            if (r8 != 0) goto L_0x019a
            double[] r8 = r0.realEigenvalues
            int r9 = r4 + 1
            r18 = r8[r9]
            double r18 = r18 - r21
            r8[r9] = r18
            r3[r11] = r16
            goto L_0x01f6
        L_0x019a:
            double[] r8 = r0.realEigenvalues
            int r9 = r4 + 1
            r31 = r8[r9]
            double r31 = r31 - r21
            double[] r6 = r0.realEigenvalues
            r7 = r6[r4]
            double r7 = r7 - r31
            double r7 = r7 * r23
            r29 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r33 = r25 * r29
            double r33 = r33 * r27
            double r12 = r7 + r33
            double r21 = r23 * r12
            double[] r6 = r0.realEigenvalues
            int r7 = r4 + 1
            double r8 = r31 + r21
            r6[r7] = r8
            double r6 = r25 * r12
            double r6 = r6 - r27
            r8 = 0
        L_0x01c1:
            if (r8 >= r2) goto L_0x01ea
            r9 = r1[r8]
            int r20 = r4 + 1
            r14 = r9[r20]
            r9 = r1[r8]
            int r20 = r4 + 1
            r31 = r1[r8]
            r32 = r31[r4]
            double r32 = r32 * r23
            double r37 = r25 * r14
            double r32 = r32 + r37
            r9[r20] = r32
            r9 = r1[r8]
            r20 = r1[r8]
            r31 = r20[r4]
            double r31 = r31 * r25
            double r33 = r23 * r14
            double r31 = r31 - r33
            r9[r4] = r31
            int r8 = r8 + 1
            goto L_0x01c1
        L_0x01ea:
            int r4 = r4 + -1
            r14 = r29
            r8 = r35
            goto L_0x0135
        L_0x01f2:
            r35 = r8
            r37 = r12
        L_0x01f6:
            r8 = 0
            int r14 = (r12 > r8 ? 1 : (r12 == r8 ? 0 : -1))
            if (r14 != 0) goto L_0x01ff
            if (r4 < r5) goto L_0x01ff
            goto L_0x0210
        L_0x01ff:
            double[] r8 = r0.realEigenvalues
            r14 = r8[r5]
            double r14 = r14 - r21
            r8[r5] = r14
            r3[r5] = r6
            r8 = 0
            r3[r11] = r8
            goto L_0x0210
        L_0x020e:
            r35 = r8
        L_0x0210:
            if (r11 != r5) goto L_0x021b
            int r5 = r5 + 1
            r8 = r35
            r4 = 0
            r6 = 0
            goto L_0x0099
        L_0x021b:
            r8 = r35
            r4 = 0
            r6 = 0
            goto L_0x009c
        L_0x0222:
            r35 = r8
            r4 = 0
        L_0x0225:
            if (r4 >= r2) goto L_0x0267
            r5 = r4
            double[] r6 = r0.realEigenvalues
            r7 = r6[r4]
            int r6 = r4 + 1
        L_0x022e:
            if (r6 >= r2) goto L_0x0240
            double[] r9 = r0.realEigenvalues
            r10 = r9[r6]
            int r9 = (r10 > r7 ? 1 : (r10 == r7 ? 0 : -1))
            if (r9 <= 0) goto L_0x023d
            r5 = r6
            double[] r9 = r0.realEigenvalues
            r7 = r9[r6]
        L_0x023d:
            int r6 = r6 + 1
            goto L_0x022e
        L_0x0240:
            if (r5 == r4) goto L_0x0264
            double[] r6 = r0.realEigenvalues
            double[] r9 = r0.realEigenvalues
            r10 = r9[r4]
            r6[r5] = r10
            double[] r6 = r0.realEigenvalues
            r6[r4] = r7
            r6 = 0
        L_0x024f:
            if (r6 >= r2) goto L_0x0264
            r9 = r1[r6]
            r7 = r9[r4]
            r9 = r1[r6]
            r10 = r1[r6]
            r11 = r10[r5]
            r9[r4] = r11
            r9 = r1[r6]
            r9[r5] = r7
            int r6 = r6 + 1
            goto L_0x024f
        L_0x0264:
            int r4 = r4 + 1
            goto L_0x0225
        L_0x0267:
            r4 = 0
            r5 = r4
            r4 = 0
        L_0x026b:
            if (r4 >= r2) goto L_0x0284
            double[] r7 = r0.realEigenvalues
            r8 = r7[r4]
            double r7 = org.apache.commons.math3.util.FastMath.abs(r8)
            int r7 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
            if (r7 <= 0) goto L_0x0281
            double[] r7 = r0.realEigenvalues
            r8 = r7[r4]
            double r5 = org.apache.commons.math3.util.FastMath.abs(r8)
        L_0x0281:
            int r4 = r4 + 1
            goto L_0x026b
        L_0x0284:
            r7 = 0
            int r4 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r4 == 0) goto L_0x02a9
            r4 = 0
        L_0x028b:
            if (r4 >= r2) goto L_0x02a9
            double[] r7 = r0.realEigenvalues
            r8 = r7[r4]
            double r7 = org.apache.commons.math3.util.FastMath.abs(r8)
            double r9 = org.apache.commons.math3.util.Precision.EPSILON
            double r9 = r9 * r5
            int r7 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r7 >= 0) goto L_0x02a4
            double[] r7 = r0.realEigenvalues
            r8 = 0
            r7[r4] = r8
            goto L_0x02a6
        L_0x02a4:
            r8 = 0
        L_0x02a6:
            int r4 = r4 + 1
            goto L_0x028b
        L_0x02a9:
            org.apache.commons.math3.linear.ArrayRealVector[] r4 = new org.apache.commons.math3.linear.ArrayRealVector[r2]
            r0.eigenvectors = r4
            double[] r4 = new double[r2]
            r7 = 0
        L_0x02b0:
            if (r7 >= r2) goto L_0x02ca
            r8 = 0
        L_0x02b3:
            if (r8 >= r2) goto L_0x02be
            r9 = r1[r8]
            r10 = r9[r7]
            r4[r8] = r10
            int r8 = r8 + 1
            goto L_0x02b3
        L_0x02be:
            org.apache.commons.math3.linear.ArrayRealVector[] r8 = r0.eigenvectors
            org.apache.commons.math3.linear.ArrayRealVector r9 = new org.apache.commons.math3.linear.ArrayRealVector
            r9.<init>(r4)
            r8[r7] = r9
            int r7 = r7 + 1
            goto L_0x02b0
        L_0x02ca:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.linear.EigenDecomposition.findEigenVectors(double[][]):void");
    }

    private SchurTransformer transformToSchur(RealMatrix matrix) {
        SchurTransformer schurTransform = new SchurTransformer(matrix);
        double[][] matT = schurTransform.getT().getData();
        this.realEigenvalues = new double[matT.length];
        this.imagEigenvalues = new double[matT.length];
        int i = 0;
        while (i < this.realEigenvalues.length) {
            if (i == this.realEigenvalues.length - 1 || Precision.equals(matT[i + 1][i], 0.0d, 1.0E-12d)) {
                this.realEigenvalues[i] = matT[i][i];
            } else {
                double x = matT[i + 1][i + 1];
                double p = (matT[i][i] - x) * 0.5d;
                double z = FastMath.sqrt(FastMath.abs((p * p) + (matT[i + 1][i] * matT[i][i + 1])));
                this.realEigenvalues[i] = x + p;
                this.imagEigenvalues[i] = z;
                this.realEigenvalues[i + 1] = x + p;
                this.imagEigenvalues[i + 1] = -z;
                i++;
            }
            i++;
        }
        return schurTransform;
    }

    private Complex cdiv(double xr, double xi, double yr, double yi) {
        return new Complex(xr, xi).divide(new Complex(yr, yi));
    }

    private void findEigenVectorsFromSchur(SchurTransformer schur) throws MathArithmeticException {
        double[][] matrixT;
        int n;
        double[][] matrixP;
        double norm;
        EigenDecomposition eigenDecomposition;
        int idx;
        int idx2;
        double q;
        int i;
        double z;
        int idx3;
        double r;
        double w;
        double vr;
        int n2;
        double[][] matrixP2;
        double q2;
        double z2;
        double s;
        EigenDecomposition eigenDecomposition2 = this;
        double[][] matrixT2 = schur.getT().getData();
        double[][] matrixP3 = schur.getP().getData();
        int n3 = matrixT2.length;
        double norm2 = 0.0d;
        for (int i2 = 0; i2 < n3; i2++) {
            for (int j = FastMath.max(i2 - 1, 0); j < n3; j++) {
                norm2 += FastMath.abs(matrixT2[i2][j]);
            }
        }
        if (Precision.equals(norm2, 0.0d, 1.0E-12d)) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NORM, new Object[0]);
        }
        int idx4 = n3 - 1;
        double r2 = 0;
        double s2 = 0.0d;
        double z3 = 0;
        while (true) {
            int idx5 = idx4;
            if (idx5 < 0) {
                break;
            }
            double p = eigenDecomposition2.realEigenvalues[idx5];
            double q3 = eigenDecomposition2.imagEigenvalues[idx5];
            double d = 0.0d;
            if (Precision.equals(q3, 0.0d)) {
                int l = idx5;
                matrixT2[idx5][idx5] = 4607182418800017408;
                int i3 = idx5 - 1;
                double z4 = z3;
                double s3 = s2;
                while (i3 >= 0) {
                    double w2 = matrixT2[i3][i3] - p;
                    double norm3 = norm2;
                    double r3 = 0.0d;
                    for (int j2 = l; j2 <= idx5; j2++) {
                        r3 += matrixT2[i3][j2] * matrixT2[j2][idx5];
                    }
                    if (Precision.compareTo(eigenDecomposition2.imagEigenvalues[i3], 0.0d, 1.0E-12d) < 0) {
                        matrixP2 = matrixP3;
                        n2 = n3;
                        z4 = w2;
                        s3 = r3;
                    } else {
                        l = i3;
                        double q4 = q3;
                        if (Precision.equals(eigenDecomposition2.imagEigenvalues[i3], d)) {
                            if (w2 != d) {
                                matrixT2[i3][idx5] = (-r3) / w2;
                            } else {
                                matrixT2[i3][idx5] = (-r3) / (Precision.EPSILON * norm3);
                            }
                            matrixP2 = matrixP3;
                            n2 = n3;
                            z2 = z4;
                            s = s3;
                            q2 = q4;
                        } else {
                            double x = matrixT2[i3][i3 + 1];
                            double y = matrixT2[i3 + 1][i3];
                            q2 = ((eigenDecomposition2.realEigenvalues[i3] - p) * (eigenDecomposition2.realEigenvalues[i3] - p)) + (eigenDecomposition2.imagEigenvalues[i3] * eigenDecomposition2.imagEigenvalues[i3]);
                            s = s3;
                            matrixP2 = matrixP3;
                            n2 = n3;
                            z2 = z4;
                            double t = ((x * s) - (z2 * r3)) / q2;
                            matrixT2[i3][idx5] = t;
                            if (FastMath.abs(x) > FastMath.abs(z2)) {
                                matrixT2[i3 + 1][idx5] = ((-r3) - (w2 * t)) / x;
                            } else {
                                matrixT2[i3 + 1][idx5] = ((-s) - (y * t)) / z2;
                            }
                        }
                        double t2 = FastMath.abs(matrixT2[i3][idx5]);
                        if (Precision.EPSILON * t2 * t2 > 1.0d) {
                            for (int j3 = i3; j3 <= idx5; j3++) {
                                double[] dArr = matrixT2[j3];
                                dArr[idx5] = dArr[idx5] / t2;
                            }
                        }
                        s3 = s;
                        z4 = z2;
                        q3 = q2;
                    }
                    i3--;
                    r2 = r3;
                    norm2 = norm3;
                    matrixP3 = matrixP2;
                    n3 = n2;
                    d = 0.0d;
                    eigenDecomposition2 = this;
                }
                double d2 = q3;
                matrixP = matrixP3;
                n = n3;
                norm = norm2;
                s2 = s3;
                idx = idx5;
                matrixT = matrixT2;
                z3 = z4;
                eigenDecomposition = this;
            } else {
                matrixP = matrixP3;
                n = n3;
                norm = norm2;
                if (q3 < 0.0d) {
                    int l2 = idx5 - 1;
                    if (FastMath.abs(matrixT2[idx5][idx5 - 1]) > FastMath.abs(matrixT2[idx5 - 1][idx5])) {
                        matrixT2[idx5 - 1][idx5 - 1] = q3 / matrixT2[idx5][idx5 - 1];
                        matrixT2[idx5 - 1][idx5] = (-(matrixT2[idx5][idx5] - p)) / matrixT2[idx5][idx5 - 1];
                        q = q3;
                        idx2 = idx5;
                        eigenDecomposition = this;
                    } else {
                        eigenDecomposition = this;
                        double d3 = -matrixT2[idx5 - 1][idx5];
                        q = q3;
                        idx2 = idx5;
                        Complex result = eigenDecomposition.cdiv(0.0d, d3, matrixT2[idx5 - 1][idx5 - 1] - p, q);
                        matrixT2[idx2 - 1][idx2 - 1] = result.getReal();
                        matrixT2[idx2 - 1][idx2] = result.getImaginary();
                    }
                    matrixT2[idx2][idx2 - 1] = 0;
                    matrixT2[idx2][idx2] = 4607182418800017408;
                    int i4 = idx2 - 2;
                    double z5 = z3;
                    double s4 = s2;
                    double r4 = r2;
                    while (true) {
                        int i5 = i4;
                        if (i5 < 0) {
                            break;
                        }
                        double ra = 0.0d;
                        double sa = 0.0d;
                        for (int j4 = l2; j4 <= idx2; j4++) {
                            ra += matrixT2[i5][j4] * matrixT2[j4][idx2 - 1];
                            sa += matrixT2[i5][j4] * matrixT2[j4][idx2];
                        }
                        int l3 = l2;
                        double[][] matrixT3 = matrixT2;
                        double w3 = matrixT2[i5][i5] - p;
                        if (Precision.compareTo(eigenDecomposition.imagEigenvalues[i5], 0.0d, 1.0E-12d) < 0) {
                            double z6 = w3;
                            s4 = sa;
                            i = i5;
                            r4 = ra;
                            l2 = l3;
                            double d4 = z6;
                            idx3 = idx2;
                            z = d4;
                        } else {
                            int l4 = i5;
                            double s5 = s4;
                            int idx6 = idx2;
                            i = i5;
                            if (Precision.equals(eigenDecomposition.imagEigenvalues[i5], 0.0d)) {
                                double s6 = s5;
                                double z7 = z5;
                                double sa2 = sa;
                                double ra2 = ra;
                                Complex c = eigenDecomposition.cdiv(-ra, -sa, w3, q);
                                matrixT3[i][idx6 - 1] = c.getReal();
                                matrixT3[i][idx6] = c.getImaginary();
                                double d5 = w3;
                                r = r4;
                                w = s6;
                                z = z7;
                                double d6 = sa2;
                                double d7 = ra2;
                            } else {
                                double z8 = z5;
                                double sa3 = sa;
                                double ra3 = ra;
                                double s7 = s5;
                                double x2 = matrixT3[i][i + 1];
                                double y2 = matrixT3[i + 1][i];
                                double vr2 = (((eigenDecomposition.realEigenvalues[i] - p) * (eigenDecomposition.realEigenvalues[i] - p)) + (eigenDecomposition.imagEigenvalues[i] * eigenDecomposition.imagEigenvalues[i])) - (q * q);
                                double vi = (eigenDecomposition.realEigenvalues[i] - p) * 2.0d * q;
                                if (!Precision.equals(vr2, 0.0d) || !Precision.equals(vi, 0.0d)) {
                                    z = z8;
                                    vr = vr2;
                                } else {
                                    z = z8;
                                    vr = Precision.EPSILON * norm * (FastMath.abs(w3) + FastMath.abs(q) + FastMath.abs(x2) + FastMath.abs(y2) + FastMath.abs(z));
                                }
                                double r5 = r4;
                                double w4 = w3;
                                double ra4 = ra3;
                                double y3 = y2;
                                double s8 = s7;
                                double r6 = r5;
                                double r7 = ((x2 * r5) - (z * ra4)) + (q * sa3);
                                double vi2 = vi;
                                double vi3 = ((x2 * s8) - (z * sa3)) - (q * ra4);
                                double s9 = s8;
                                double y4 = y3;
                                double ra5 = ra4;
                                double ra6 = x2;
                                Complex c2 = eigenDecomposition.cdiv(r7, vi3, vr, vi2);
                                matrixT3[i][idx6 - 1] = c2.getReal();
                                matrixT3[i][idx6] = c2.getImaginary();
                                if (FastMath.abs(ra6) > FastMath.abs(z) + FastMath.abs(q)) {
                                    double ra7 = ra5;
                                    matrixT3[i + 1][idx6 - 1] = (((-ra7) - (matrixT3[i][idx6 - 1] * w4)) + (matrixT3[i][idx6] * q)) / ra6;
                                    double sa4 = sa3;
                                    matrixT3[i + 1][idx6] = (((-sa4) - (matrixT3[i][idx6] * w4)) - (matrixT3[i][idx6 - 1] * q)) / ra6;
                                    double d8 = sa4;
                                    double d9 = ra7;
                                    r = r6;
                                    w = s9;
                                } else {
                                    double r8 = r6;
                                    double d10 = ra6;
                                    w = s9;
                                    r = r8;
                                    double d11 = (-r8) - (matrixT3[i][idx6 - 1] * y4);
                                    double d12 = sa3;
                                    double d13 = (-w) - (matrixT3[i][idx6] * y4);
                                    double d14 = ra5;
                                    Complex complex = c2;
                                    Complex c22 = eigenDecomposition.cdiv(d11, d13, z, q);
                                    matrixT3[i + 1][idx6 - 1] = c22.getReal();
                                    matrixT3[i + 1][idx6] = c22.getImaginary();
                                }
                            }
                            double t3 = FastMath.max(FastMath.abs(matrixT3[i][idx6 - 1]), FastMath.abs(matrixT3[i][idx6]));
                            if (Precision.EPSILON * t3 * t3 > 1.0d) {
                                int j5 = i;
                                while (true) {
                                    idx3 = idx6;
                                    if (j5 > idx3) {
                                        break;
                                    }
                                    double[] dArr2 = matrixT3[j5];
                                    int i6 = idx3 - 1;
                                    dArr2[i6] = dArr2[i6] / t3;
                                    double[] dArr3 = matrixT3[j5];
                                    dArr3[idx3] = dArr3[idx3] / t3;
                                    j5++;
                                    idx6 = idx3;
                                }
                            } else {
                                idx3 = idx6;
                            }
                            s4 = w;
                            l2 = l4;
                            r4 = r;
                        }
                        i4 = i - 1;
                        matrixT2 = matrixT3;
                        double d15 = z;
                        idx2 = idx3;
                        z5 = d15;
                    }
                    matrixT = matrixT2;
                    double d16 = z5;
                    idx = idx2;
                    s2 = s4;
                    z3 = d16;
                    r2 = r4;
                } else {
                    idx = idx5;
                    matrixT = matrixT2;
                    eigenDecomposition = this;
                }
            }
            idx4 = idx - 1;
            eigenDecomposition2 = eigenDecomposition;
            norm2 = norm;
            matrixP3 = matrixP;
            n3 = n;
            matrixT2 = matrixT;
        }
        double[][] matrixT4 = matrixT2;
        double[][] matrixP4 = matrixP3;
        int n4 = n3;
        double d17 = norm2;
        EigenDecomposition eigenDecomposition3 = eigenDecomposition2;
        int j6 = n4 - 1;
        while (true) {
            int j7 = j6;
            if (j7 < 0) {
                break;
            }
            for (int i7 = 0; i7 <= n4 - 1; i7++) {
                double z9 = 0.0d;
                for (int k = 0; k <= FastMath.min(j7, n4 - 1); k++) {
                    z9 += matrixP4[i7][k] * matrixT4[k][j7];
                }
                matrixP4[i7][j7] = z9;
            }
            j6 = j7 - 1;
        }
        int n5 = n4;
        eigenDecomposition3.eigenvectors = new ArrayRealVector[n5];
        double[] tmp = new double[n5];
        for (int i8 = 0; i8 < n5; i8++) {
            for (int j8 = 0; j8 < n5; j8++) {
                tmp[j8] = matrixP4[j8][i8];
            }
            eigenDecomposition3.eigenvectors[i8] = new ArrayRealVector(tmp);
        }
    }
}

package org.apache.commons.math3.ode.nonstiff;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
import org.apache.commons.math3.linear.ArrayFieldVector;
import org.apache.commons.math3.linear.FieldDecompositionSolver;
import org.apache.commons.math3.linear.FieldLUDecomposition;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.FieldVector;
import org.apache.commons.math3.util.MathArrays;

public class AdamsNordsieckFieldTransformer<T extends RealFieldElement<T>> {
    private static final Map<Integer, Map<Field<? extends RealFieldElement<?>>, AdamsNordsieckFieldTransformer<? extends RealFieldElement<?>>>> CACHE = new HashMap();

    /* renamed from: c1 */
    private final T[] f652c1;
    private final Field<T> field;
    private final Array2DRowFieldMatrix<T> update;

    private AdamsNordsieckFieldTransformer(Field<T> field2, int n) {
        this.field = field2;
        int rows = n - 1;
        FieldMatrix<T> bigP = buildP(rows);
        FieldDecompositionSolver<T> pSolver = new FieldLUDecomposition(bigP).getSolver();
        T[] u = (RealFieldElement[]) MathArrays.buildArray(field2, rows);
        Arrays.fill(u, field2.getOne());
        this.f652c1 = (RealFieldElement[]) pSolver.solve((FieldVector<T>) new ArrayFieldVector<T>(u, false)).toArray();
        T[][] shiftedP = (RealFieldElement[][]) bigP.getData();
        for (int i = shiftedP.length - 1; i > 0; i--) {
            shiftedP[i] = shiftedP[i - 1];
        }
        shiftedP[0] = (RealFieldElement[]) MathArrays.buildArray(field2, rows);
        Arrays.fill(shiftedP[0], field2.getZero());
        this.update = new Array2DRowFieldMatrix<>((T[][]) pSolver.solve((FieldMatrix<T>) new Array2DRowFieldMatrix<T>(shiftedP, false)).getData());
    }

    public static <T extends RealFieldElement<T>> AdamsNordsieckFieldTransformer<T> getInstance(Field<T> field2, int nSteps) {
        AdamsNordsieckFieldTransformer t;
        synchronized (CACHE) {
            Map map = (Map) CACHE.get(Integer.valueOf(nSteps));
            if (map == null) {
                map = new HashMap();
                CACHE.put(Integer.valueOf(nSteps), map);
            }
            t = (AdamsNordsieckFieldTransformer) map.get(field2);
            if (t == null) {
                t = new AdamsNordsieckFieldTransformer(field2, nSteps);
                map.put(field2, t);
            }
        }
        return t;
    }

    private FieldMatrix<T> buildP(int rows) {
        T[][] pData = (RealFieldElement[][]) MathArrays.buildArray(this.field, rows, rows);
        for (int i = 1; i <= pData.length; i++) {
            T[] pI = pData[i - 1];
            int factor = -i;
            T aj = (RealFieldElement) ((RealFieldElement) this.field.getZero()).add((double) factor);
            for (int j = 1; j <= pI.length; j++) {
                pI[j - 1] = (RealFieldElement) aj.multiply(j + 1);
                aj = (RealFieldElement) aj.multiply(factor);
            }
        }
        return new Array2DRowFieldMatrix(pData, false);
    }

    public Array2DRowFieldMatrix<T> initializeHighOrderDerivatives(T h, T[] t, T[][] y, T[][] yDot) {
        T[][] tArr = y;
        RealFieldElement[][] realFieldElementArr = (RealFieldElement[][]) MathArrays.buildArray(this.field, this.f652c1.length + 1, this.f652c1.length + 1);
        char c = 0;
        T[][] b = (RealFieldElement[][]) MathArrays.buildArray(this.field, this.f652c1.length + 1, tArr[0].length);
        T[] y0 = tArr[0];
        T[] yDot0 = yDot[0];
        int i = 1;
        while (i < tArr.length) {
            T di = (RealFieldElement) t[i].subtract(t[c]);
            T ratio = (RealFieldElement) di.divide(h);
            T dikM1Ohk = (RealFieldElement) h.reciprocal();
            RealFieldElement[] realFieldElementArr2 = realFieldElementArr[(i * 2) - 2];
            T[] tArr2 = null;
            RealFieldElement[] realFieldElementArr3 = (i * 2) + -1 < realFieldElementArr.length ? realFieldElementArr[(i * 2) - 1] : null;
            for (int j = 0; j < realFieldElementArr2.length; j++) {
                dikM1Ohk = (RealFieldElement) dikM1Ohk.multiply(ratio);
                realFieldElementArr2[j] = (RealFieldElement) di.multiply(dikM1Ohk);
                if (realFieldElementArr3 != null) {
                    realFieldElementArr3[j] = (RealFieldElement) dikM1Ohk.multiply(j + 2);
                }
            }
            T[] yI = tArr[i];
            T[] yDotI = yDot[i];
            T[] bI = b[(i * 2) - 2];
            if ((i * 2) - 1 < b.length) {
                tArr2 = b[(i * 2) - 1];
            }
            T[] bDotI = tArr2;
            int j2 = 0;
            while (true) {
                RealFieldElement[] realFieldElementArr4 = realFieldElementArr3;
                if (j2 >= yI.length) {
                    break;
                }
                T[] yI2 = yI;
                bI[j2] = (RealFieldElement) ((RealFieldElement) yI[j2].subtract(y0[j2])).subtract(di.multiply(yDot0[j2]));
                if (bDotI != null) {
                    bDotI[j2] = (RealFieldElement) yDotI[j2].subtract(yDot0[j2]);
                }
                j2++;
                realFieldElementArr3 = realFieldElementArr4;
                yI = yI2;
            }
            i++;
            tArr = y;
            c = 0;
        }
        T t2 = h;
        FieldMatrix<T> x = new FieldLUDecomposition<>(new Array2DRowFieldMatrix((T[][]) realFieldElementArr, false)).getSolver().solve((FieldMatrix<T>) new Array2DRowFieldMatrix<T>(b, false));
        Array2DRowFieldMatrix<T> truncatedX = new Array2DRowFieldMatrix<>(this.field, x.getRowDimension() - 1, x.getColumnDimension());
        for (int i2 = 0; i2 < truncatedX.getRowDimension(); i2++) {
            for (int j3 = 0; j3 < truncatedX.getColumnDimension(); j3++) {
                truncatedX.setEntry(i2, j3, x.getEntry(i2, j3));
            }
        }
        return truncatedX;
    }

    public Array2DRowFieldMatrix<T> updateHighOrderDerivativesPhase1(Array2DRowFieldMatrix<T> highOrder) {
        return this.update.multiply(highOrder);
    }

    public void updateHighOrderDerivativesPhase2(T[] start, T[] end, Array2DRowFieldMatrix<T> highOrder) {
        T[][] data = (RealFieldElement[][]) highOrder.getDataRef();
        for (int i = 0; i < data.length; i++) {
            T[] dataI = data[i];
            T c1I = this.f652c1[i];
            for (int j = 0; j < dataI.length; j++) {
                dataI[j] = (RealFieldElement) dataI[j].add(c1I.multiply(start[j].subtract(end[j])));
            }
        }
    }
}

package org.apache.commons.math3.analysis.differentiation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

public class DSCompiler {
    private static AtomicReference<DSCompiler[][]> compilers = new AtomicReference<>(null);
    private final int[][][] compIndirection;
    private final int[][] derivativesIndirection;
    private final int[] lowerIndirection;
    private final int[][][] multIndirection;
    private final int order;
    private final int parameters;
    private final int[][] sizes;

    private DSCompiler(int parameters2, int order2, DSCompiler valueCompiler, DSCompiler derivativeCompiler) throws NumberIsTooLargeException {
        this.parameters = parameters2;
        this.order = order2;
        this.sizes = compileSizes(parameters2, order2, valueCompiler);
        this.derivativesIndirection = compileDerivativesIndirection(parameters2, order2, valueCompiler, derivativeCompiler);
        this.lowerIndirection = compileLowerIndirection(parameters2, order2, valueCompiler, derivativeCompiler);
        this.multIndirection = compileMultiplicationIndirection(parameters2, order2, valueCompiler, derivativeCompiler, this.lowerIndirection);
        this.compIndirection = compileCompositionIndirection(parameters2, order2, valueCompiler, derivativeCompiler, this.sizes, this.derivativesIndirection);
    }

    public static DSCompiler getCompiler(int parameters2, int order2) throws NumberIsTooLargeException {
        DSCompiler[][] cache = (DSCompiler[][]) compilers.get();
        if (cache != null && cache.length > parameters2 && cache[parameters2].length > order2 && cache[parameters2][order2] != null) {
            return cache[parameters2][order2];
        }
        DSCompiler[][] newCache = (DSCompiler[][]) Array.newInstance(DSCompiler.class, new int[]{FastMath.max(parameters2, cache == null ? 0 : cache.length) + 1, FastMath.max(order2, cache == null ? 0 : cache[0].length) + 1});
        if (cache != null) {
            for (int i = 0; i < cache.length; i++) {
                System.arraycopy(cache[i], 0, newCache[i], 0, cache[i].length);
            }
        }
        for (int diag = 0; diag <= parameters2 + order2; diag++) {
            int o = FastMath.max(0, diag - parameters2);
            while (o <= FastMath.min(order2, diag)) {
                int p = diag - o;
                if (newCache[p][o] == null) {
                    DSCompiler derivativeCompiler = null;
                    DSCompiler valueCompiler = p == 0 ? null : newCache[p - 1][o];
                    if (o != 0) {
                        derivativeCompiler = newCache[p][o - 1];
                    }
                    newCache[p][o] = new DSCompiler(p, o, valueCompiler, derivativeCompiler);
                }
                o++;
            }
        }
        compilers.compareAndSet(cache, newCache);
        return newCache[parameters2][order2];
    }

    private static int[][] compileSizes(int parameters2, int order2, DSCompiler valueCompiler) {
        int[][] sizes2 = (int[][]) Array.newInstance(int.class, new int[]{parameters2 + 1, order2 + 1});
        if (parameters2 == 0) {
            Arrays.fill(sizes2[0], 1);
        } else {
            System.arraycopy(valueCompiler.sizes, 0, sizes2, 0, parameters2);
            sizes2[parameters2][0] = 1;
            for (int i = 0; i < order2; i++) {
                sizes2[parameters2][i + 1] = sizes2[parameters2][i] + sizes2[parameters2 - 1][i + 1];
            }
        }
        return sizes2;
    }

    private static int[][] compileDerivativesIndirection(int parameters2, int order2, DSCompiler valueCompiler, DSCompiler derivativeCompiler) {
        if (parameters2 == 0 || order2 == 0) {
            return (int[][]) Array.newInstance(int.class, new int[]{1, parameters2});
        }
        int vSize = valueCompiler.derivativesIndirection.length;
        int dSize = derivativeCompiler.derivativesIndirection.length;
        int[][] derivativesIndirection2 = (int[][]) Array.newInstance(int.class, new int[]{vSize + dSize, parameters2});
        for (int i = 0; i < vSize; i++) {
            System.arraycopy(valueCompiler.derivativesIndirection[i], 0, derivativesIndirection2[i], 0, parameters2 - 1);
        }
        for (int i2 = 0; i2 < dSize; i2++) {
            System.arraycopy(derivativeCompiler.derivativesIndirection[i2], 0, derivativesIndirection2[vSize + i2], 0, parameters2);
            int[] iArr = derivativesIndirection2[vSize + i2];
            int i3 = parameters2 - 1;
            iArr[i3] = iArr[i3] + 1;
        }
        return derivativesIndirection2;
    }

    private static int[] compileLowerIndirection(int parameters2, int order2, DSCompiler valueCompiler, DSCompiler derivativeCompiler) {
        if (parameters2 == 0 || order2 <= 1) {
            return new int[]{0};
        }
        int vSize = valueCompiler.lowerIndirection.length;
        int dSize = derivativeCompiler.lowerIndirection.length;
        int[] lowerIndirection2 = new int[(vSize + dSize)];
        System.arraycopy(valueCompiler.lowerIndirection, 0, lowerIndirection2, 0, vSize);
        for (int i = 0; i < dSize; i++) {
            lowerIndirection2[vSize + i] = valueCompiler.getSize() + derivativeCompiler.lowerIndirection[i];
        }
        return lowerIndirection2;
    }

    private static int[][][] compileMultiplicationIndirection(int parameters2, int order2, DSCompiler valueCompiler, DSCompiler derivativeCompiler, int[] lowerIndirection2) {
        DSCompiler dSCompiler = valueCompiler;
        DSCompiler dSCompiler2 = derivativeCompiler;
        int i = 3;
        char c = 1;
        char c2 = 0;
        if (parameters2 == 0 || order2 == 0) {
            return new int[][][]{new int[][]{new int[]{1, 0, 0}}};
        }
        int vSize = dSCompiler.multIndirection.length;
        int dSize = dSCompiler2.multIndirection.length;
        int[][][] multIndirection2 = new int[(vSize + dSize)][][];
        System.arraycopy(dSCompiler.multIndirection, 0, multIndirection2, 0, vSize);
        int i2 = 0;
        while (i2 < dSize) {
            int[][] dRow = dSCompiler2.multIndirection[i2];
            List<int[]> row = new ArrayList<>(dRow.length * 2);
            for (int j = 0; j < dRow.length; j++) {
                int[] iArr = new int[i];
                iArr[c2] = dRow[j][c2];
                iArr[c] = lowerIndirection2[dRow[j][c]];
                iArr[2] = dRow[j][2] + vSize;
                row.add(iArr);
                int[] iArr2 = new int[i];
                iArr2[c2] = dRow[j][c2];
                iArr2[c] = dRow[j][c] + vSize;
                iArr2[2] = lowerIndirection2[dRow[j][2]];
                row.add(iArr2);
            }
            List<int[]> combined = new ArrayList<>(row.size());
            int j2 = 0;
            while (j2 < row.size()) {
                int[] termJ = (int[]) row.get(j2);
                if (termJ[c2] > 0) {
                    int k = j2 + 1;
                    while (true) {
                        int k2 = k;
                        if (k2 >= row.size()) {
                            break;
                        }
                        int[] termK = (int[]) row.get(k2);
                        if (termJ[1] == termK[1]) {
                            if (termJ[2] == termK[2]) {
                                termJ[0] = termJ[0] + termK[0];
                                termK[0] = 0;
                            }
                        }
                        k = k2 + 1;
                        DSCompiler dSCompiler3 = valueCompiler;
                        DSCompiler dSCompiler4 = derivativeCompiler;
                    }
                    combined.add(termJ);
                }
                j2++;
                DSCompiler dSCompiler5 = valueCompiler;
                DSCompiler dSCompiler6 = derivativeCompiler;
                c2 = 0;
            }
            multIndirection2[vSize + i2] = (int[][]) combined.toArray(new int[combined.size()][]);
            i2++;
            DSCompiler dSCompiler7 = valueCompiler;
            dSCompiler2 = derivativeCompiler;
            i = 3;
            c = 1;
            c2 = 0;
        }
        return multIndirection2;
    }

    private static int[][][] compileCompositionIndirection(int parameters2, int order2, DSCompiler valueCompiler, DSCompiler derivativeCompiler, int[][] sizes2, int[][] derivativesIndirection2) throws NumberIsTooLargeException {
        int len$;
        int i = parameters2;
        int i2 = order2;
        DSCompiler dSCompiler = valueCompiler;
        DSCompiler dSCompiler2 = derivativeCompiler;
        int[][] iArr = sizes2;
        int i3 = 1;
        if (i == 0 || i2 == 0) {
            return new int[][][]{new int[][]{new int[]{1, 0}}};
        }
        int vSize = dSCompiler.compIndirection.length;
        int dSize = dSCompiler2.compIndirection.length;
        int[][][] compIndirection2 = new int[(vSize + dSize)][][];
        System.arraycopy(dSCompiler.compIndirection, 0, compIndirection2, 0, vSize);
        int i4 = 0;
        while (true) {
            int i5 = i4;
            if (i5 >= dSize) {
                return compIndirection2;
            }
            List<int[]> arrayList = new ArrayList<>();
            int[][] arr$ = dSCompiler2.compIndirection[i5];
            int len$2 = arr$.length;
            int l = 0;
            while (l < len$2) {
                int[] derivedTermG = arr$[l];
                int[] derivedTermF = new int[(derivedTermG.length + i3)];
                derivedTermF[0] = derivedTermG[0];
                derivedTermF[i3] = derivedTermG[i3] + 1;
                int[] orders = new int[i];
                orders[i - 1] = 1;
                int i$ = l;
                derivedTermF[derivedTermG.length] = getPartialDerivativeIndex(i, i2, iArr, orders);
                int j = 2;
                while (true) {
                    len$ = len$2;
                    if (j >= derivedTermG.length) {
                        break;
                    }
                    int[][] arr$2 = arr$;
                    int i$2 = i$;
                    int j2 = j;
                    ArrayList arrayList2 = arrayList;
                    int i6 = i5;
                    int[][][] compIndirection3 = compIndirection2;
                    derivedTermF[j2] = convertIndex(derivedTermG[j], i, dSCompiler2.derivativesIndirection, i, i2, iArr);
                    j = j2 + 1;
                    arrayList = arrayList2;
                    i$ = i$2;
                    len$2 = len$;
                    arr$ = arr$2;
                    i5 = i6;
                    compIndirection2 = compIndirection3;
                    DSCompiler dSCompiler3 = valueCompiler;
                }
                int[][] arr$3 = arr$;
                ArrayList arrayList3 = arrayList;
                int i7 = i5;
                int[][][] compIndirection4 = compIndirection2;
                int i$3 = i$;
                Arrays.sort(derivedTermF, 2, derivedTermF.length);
                arrayList3.add(derivedTermF);
                int l2 = 2;
                while (true) {
                    int l3 = l2;
                    if (l3 >= derivedTermG.length) {
                        break;
                    }
                    int[] derivedTermG2 = new int[derivedTermG.length];
                    derivedTermG2[0] = derivedTermG[0];
                    derivedTermG2[1] = derivedTermG[1];
                    int j3 = 2;
                    while (true) {
                        int j4 = j3;
                        if (j4 >= derivedTermG.length) {
                            break;
                        }
                        int i8 = derivedTermG[j4];
                        int[][] iArr2 = dSCompiler2.derivativesIndirection;
                        int j5 = j4;
                        int[] term = derivedTermG;
                        int[] term2 = derivedTermG2;
                        int[] derivedTermF2 = derivedTermF;
                        int l4 = l3;
                        term2[j5] = convertIndex(i8, i, iArr2, i, i2, iArr);
                        if (j5 == l4) {
                            System.arraycopy(derivativesIndirection2[term2[j5]], 0, orders, 0, i);
                            int i9 = i - 1;
                            orders[i9] = orders[i9] + 1;
                            term2[j5] = getPartialDerivativeIndex(i, i2, iArr, orders);
                        }
                        j3 = j5 + 1;
                        derivedTermG2 = term2;
                        l3 = l4;
                        derivedTermG = term;
                        derivedTermF = derivedTermF2;
                        dSCompiler2 = derivativeCompiler;
                    }
                    int[] term3 = derivedTermG;
                    int[] derivedTermF3 = derivedTermF;
                    int[] term4 = derivedTermG2;
                    int l5 = l3;
                    Arrays.sort(term4, 2, term4.length);
                    arrayList3.add(term4);
                    l2 = l5 + 1;
                    derivedTermG = term3;
                    derivedTermF = derivedTermF3;
                    dSCompiler2 = derivativeCompiler;
                }
                l = i$3 + 1;
                arrayList = arrayList3;
                len$2 = len$;
                arr$ = arr$3;
                i5 = i7;
                compIndirection2 = compIndirection4;
                DSCompiler dSCompiler4 = valueCompiler;
                dSCompiler2 = derivativeCompiler;
                i3 = 1;
            }
            List<int[]> row = arrayList;
            int i10 = i5;
            int[][][] compIndirection5 = compIndirection2;
            List<int[]> combined = new ArrayList<>(row.size());
            for (int j6 = 0; j6 < row.size(); j6++) {
                int[] termJ = (int[]) row.get(j6);
                if (termJ[0] > 0) {
                    for (int k = j6 + 1; k < row.size(); k++) {
                        int[] termK = (int[]) row.get(k);
                        boolean equals = termJ.length == termK.length;
                        int l6 = 1;
                        while (equals && l6 < termJ.length) {
                            equals &= termJ[l6] == termK[l6];
                            l6++;
                        }
                        if (equals) {
                            termJ[0] = termJ[0] + termK[0];
                            termK[0] = 0;
                        }
                    }
                    combined.add(termJ);
                }
            }
            compIndirection5[vSize + i10] = (int[][]) combined.toArray(new int[combined.size()][]);
            i4 = i10 + 1;
            compIndirection2 = compIndirection5;
            DSCompiler dSCompiler5 = valueCompiler;
            dSCompiler2 = derivativeCompiler;
            i3 = 1;
        }
    }

    public int getPartialDerivativeIndex(int... orders) throws DimensionMismatchException, NumberIsTooLargeException {
        if (orders.length == getFreeParameters()) {
            return getPartialDerivativeIndex(this.parameters, this.order, this.sizes, orders);
        }
        throw new DimensionMismatchException(orders.length, getFreeParameters());
    }

    private static int getPartialDerivativeIndex(int parameters2, int order2, int[][] sizes2, int... orders) throws NumberIsTooLargeException {
        int index = 0;
        int m = order2;
        int ordersSum = 0;
        for (int i = parameters2 - 1; i >= 0; i--) {
            int derivativeOrder = orders[i];
            ordersSum += derivativeOrder;
            if (ordersSum > order2) {
                throw new NumberIsTooLargeException(Integer.valueOf(ordersSum), Integer.valueOf(order2), true);
            }
            while (true) {
                int derivativeOrder2 = derivativeOrder - 1;
                if (derivativeOrder <= 0) {
                    break;
                }
                index += sizes2[i][m];
                derivativeOrder = derivativeOrder2;
                m--;
            }
        }
        return index;
    }

    private static int convertIndex(int index, int srcP, int[][] srcDerivativesIndirection, int destP, int destO, int[][] destSizes) throws NumberIsTooLargeException {
        int[] orders = new int[destP];
        System.arraycopy(srcDerivativesIndirection[index], 0, orders, 0, FastMath.min(srcP, destP));
        return getPartialDerivativeIndex(destP, destO, destSizes, orders);
    }

    public int[] getPartialDerivativeOrders(int index) {
        return this.derivativesIndirection[index];
    }

    public int getFreeParameters() {
        return this.parameters;
    }

    public int getOrder() {
        return this.order;
    }

    public int getSize() {
        return this.sizes[this.parameters][this.order];
    }

    public void linearCombination(double a1, double[] c1, int offset1, double a2, double[] c2, int offset2, double[] result, int resultOffset) {
        for (int i = 0; i < getSize(); i++) {
            result[resultOffset + i] = MathArrays.linearCombination(a1, c1[offset1 + i], a2, c2[offset2 + i]);
        }
    }

    public void linearCombination(double a1, double[] c1, int offset1, double a2, double[] c2, int offset2, double a3, double[] c3, int offset3, double[] result, int resultOffset) {
        for (int i = 0; i < getSize(); i++) {
            result[resultOffset + i] = MathArrays.linearCombination(a1, c1[offset1 + i], a2, c2[offset2 + i], a3, c3[offset3 + i]);
        }
    }

    public void linearCombination(double a1, double[] c1, int offset1, double a2, double[] c2, int offset2, double a3, double[] c3, int offset3, double a4, double[] c4, int offset4, double[] result, int resultOffset) {
        for (int i = 0; i < getSize(); i++) {
            result[resultOffset + i] = MathArrays.linearCombination(a1, c1[offset1 + i], a2, c2[offset2 + i], a3, c3[offset3 + i], a4, c4[offset4 + i]);
        }
    }

    public void add(double[] lhs, int lhsOffset, double[] rhs, int rhsOffset, double[] result, int resultOffset) {
        for (int i = 0; i < getSize(); i++) {
            result[resultOffset + i] = lhs[lhsOffset + i] + rhs[rhsOffset + i];
        }
    }

    public void subtract(double[] lhs, int lhsOffset, double[] rhs, int rhsOffset, double[] result, int resultOffset) {
        for (int i = 0; i < getSize(); i++) {
            result[resultOffset + i] = lhs[lhsOffset + i] - rhs[rhsOffset + i];
        }
    }

    public void multiply(double[] lhs, int lhsOffset, double[] rhs, int rhsOffset, double[] result, int resultOffset) {
        for (int i = 0; i < this.multIndirection.length; i++) {
            int[][] mappingI = this.multIndirection[i];
            double r = 0.0d;
            for (int j = 0; j < mappingI.length; j++) {
                r += ((double) mappingI[j][0]) * lhs[lhsOffset + mappingI[j][1]] * rhs[rhsOffset + mappingI[j][2]];
            }
            result[resultOffset + i] = r;
        }
    }

    public void divide(double[] lhs, int lhsOffset, double[] rhs, int rhsOffset, double[] result, int resultOffset) {
        double[] reciprocal = new double[getSize()];
        int i = lhsOffset;
        pow(rhs, i, -1, reciprocal, 0);
        multiply(lhs, i, reciprocal, 0, result, resultOffset);
    }

    public void remainder(double[] lhs, int lhsOffset, double[] rhs, int rhsOffset, double[] result, int resultOffset) {
        double rem = FastMath.IEEEremainder(lhs[lhsOffset], rhs[rhsOffset]);
        double k = FastMath.rint((lhs[lhsOffset] - rem) / rhs[rhsOffset]);
        result[resultOffset] = rem;
        for (int i = 1; i < getSize(); i++) {
            result[resultOffset + i] = lhs[lhsOffset + i] - (rhs[rhsOffset + i] * k);
        }
    }

    public void pow(double a, double[] operand, int operandOffset, double[] result, int resultOffset) {
        int i = 1;
        double[] function = new double[(this.order + 1)];
        if (a != 0.0d) {
            function[0] = FastMath.pow(a, operand[operandOffset]);
            double lnA = FastMath.log(a);
            while (i < function.length) {
                function[i] = function[i - 1] * lnA;
                i++;
            }
        } else if (operand[operandOffset] == 0.0d) {
            function[0] = 1.0d;
            double infinity = Double.POSITIVE_INFINITY;
            while (i < function.length) {
                infinity = -infinity;
                function[i] = infinity;
                i++;
            }
        } else if (operand[operandOffset] < 0.0d) {
            Arrays.fill(function, Double.NaN);
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void pow(double[] operand, int operandOffset, double p, double[] result, int resultOffset) {
        int i = 1;
        double[] function = new double[(this.order + 1)];
        double xk = FastMath.pow(operand[operandOffset], p - ((double) this.order));
        double xk2 = xk;
        for (int i2 = this.order; i2 > 0; i2--) {
            function[i2] = xk2;
            xk2 *= operand[operandOffset];
        }
        function[0] = xk2;
        double coefficient = p;
        while (true) {
            int i3 = i;
            if (i3 <= this.order) {
                function[i3] = function[i3] * coefficient;
                coefficient *= p - ((double) i3);
                i = i3 + 1;
            } else {
                compose(operand, operandOffset, function, result, resultOffset);
                return;
            }
        }
    }

    public void pow(double[] operand, int operandOffset, int n, double[] result, int resultOffset) {
        if (n == 0) {
            result[resultOffset] = 1.0d;
            Arrays.fill(result, resultOffset + 1, getSize() + resultOffset, 0.0d);
            return;
        }
        double[] function = new double[(this.order + 1)];
        if (n > 0) {
            int maxOrder = FastMath.min(this.order, n);
            double xk = FastMath.pow(operand[operandOffset], n - maxOrder);
            for (int i = maxOrder; i > 0; i--) {
                function[i] = xk;
                xk *= operand[operandOffset];
            }
            function[0] = xk;
        } else {
            double inv = 1.0d / operand[operandOffset];
            double xk2 = FastMath.pow(inv, -n);
            for (int i2 = 0; i2 <= this.order; i2++) {
                function[i2] = xk2;
                xk2 *= inv;
            }
        }
        double coefficient = (double) n;
        for (int i3 = 1; i3 <= this.order; i3++) {
            function[i3] = function[i3] * coefficient;
            coefficient *= (double) (n - i3);
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void pow(double[] x, int xOffset, double[] y, int yOffset, double[] result, int resultOffset) {
        double[] logX = new double[getSize()];
        log(x, xOffset, logX, 0);
        double[] yLogX = new double[getSize()];
        multiply(logX, 0, y, yOffset, yLogX, 0);
        exp(yLogX, 0, result, resultOffset);
    }

    public void rootN(double[] operand, int operandOffset, int n, double[] result, int resultOffset) {
        double xk;
        int i = n;
        int i2 = 1;
        double[] function = new double[(this.order + 1)];
        if (i == 2) {
            function[0] = FastMath.sqrt(operand[operandOffset]);
            xk = 0.5d / function[0];
        } else if (i == 3) {
            function[0] = FastMath.cbrt(operand[operandOffset]);
            xk = 1.0d / ((function[0] * 3.0d) * function[0]);
        } else {
            function[0] = FastMath.pow(operand[operandOffset], 1.0d / ((double) i));
            xk = 1.0d / (((double) i) * FastMath.pow(function[0], i - 1));
        }
        double nReciprocal = 1.0d / ((double) i);
        double xReciprocal = 1.0d / operand[operandOffset];
        double xk2 = xk;
        while (true) {
            int i3 = i2;
            if (i3 <= this.order) {
                function[i3] = xk2;
                xk2 *= (nReciprocal - ((double) i3)) * xReciprocal;
                i2 = i3 + 1;
            } else {
                compose(operand, operandOffset, function, result, resultOffset);
                return;
            }
        }
    }

    public void exp(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double[] function = new double[(this.order + 1)];
        Arrays.fill(function, FastMath.exp(operand[operandOffset]));
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void expm1(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double[] function = new double[(this.order + 1)];
        function[0] = FastMath.expm1(operand[operandOffset]);
        Arrays.fill(function, 1, this.order + 1, FastMath.exp(operand[operandOffset]));
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void log(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double[] function = new double[(this.order + 1)];
        function[0] = FastMath.log(operand[operandOffset]);
        if (this.order > 0) {
            double inv = 1.0d / operand[operandOffset];
            double xk = inv;
            for (int i = 1; i <= this.order; i++) {
                function[i] = xk;
                xk *= ((double) (-i)) * inv;
            }
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void log1p(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double[] function = new double[(this.order + 1)];
        function[0] = FastMath.log1p(operand[operandOffset]);
        if (this.order > 0) {
            double inv = 1.0d / (operand[operandOffset] + 1.0d);
            double xk = inv;
            for (int i = 1; i <= this.order; i++) {
                function[i] = xk;
                xk *= ((double) (-i)) * inv;
            }
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void log10(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double[] function = new double[(this.order + 1)];
        function[0] = FastMath.log10(operand[operandOffset]);
        if (this.order > 0) {
            double inv = 1.0d / operand[operandOffset];
            double xk = inv / FastMath.log(10.0d);
            for (int i = 1; i <= this.order; i++) {
                function[i] = xk;
                xk *= ((double) (-i)) * inv;
            }
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void cos(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double[] function = new double[(this.order + 1)];
        function[0] = FastMath.cos(operand[operandOffset]);
        if (this.order > 0) {
            function[1] = -FastMath.sin(operand[operandOffset]);
            for (int i = 2; i <= this.order; i++) {
                function[i] = -function[i - 2];
            }
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void sin(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double[] function = new double[(this.order + 1)];
        function[0] = FastMath.sin(operand[operandOffset]);
        if (this.order > 0) {
            function[1] = FastMath.cos(operand[operandOffset]);
            for (int i = 2; i <= this.order; i++) {
                function[i] = -function[i - 2];
            }
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void tan(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double v;
        double t2;
        double[] function = new double[(this.order + 1)];
        double t = FastMath.tan(operand[operandOffset]);
        function[0] = t;
        if (this.order > 0) {
            int i = 2;
            double[] p = new double[(this.order + 2)];
            p[1] = 1.0d;
            double t22 = t * t;
            int n = 1;
            while (n <= this.order) {
                double v2 = 0.0d;
                p[n + 1] = ((double) n) * p[n];
                int k = n + 1;
                while (k >= 0) {
                    double v3 = (v2 * t22) + p[k];
                    if (k > i) {
                        t2 = t22;
                        v = v3;
                        p[k - 2] = (((double) (k - 1)) * p[k - 1]) + (((double) (k - 3)) * p[k - 3]);
                    } else {
                        t2 = t22;
                        v = v3;
                        if (k == 2) {
                            p[0] = p[1];
                            k -= 2;
                            t22 = t2;
                            v2 = v;
                            i = 2;
                        }
                    }
                    k -= 2;
                    t22 = t2;
                    v2 = v;
                    i = 2;
                }
                double t23 = t22;
                if ((n & 1) == 0) {
                    v2 *= t;
                }
                function[n] = v2;
                n++;
                t22 = t23;
                i = 2;
            }
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void acos(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double x2;
        DSCompiler dSCompiler = this;
        double[] function = new double[(dSCompiler.order + 1)];
        double x = operand[operandOffset];
        function[0] = FastMath.acos(x);
        if (dSCompiler.order > 0) {
            double[] p = new double[dSCompiler.order];
            p[0] = -1.0d;
            double x22 = x * x;
            double f = 1.0d / (1.0d - x22);
            double coeff = FastMath.sqrt(f);
            function[1] = p[0] * coeff;
            double coeff2 = coeff;
            int n = 2;
            while (n <= dSCompiler.order) {
                double v = 0.0d;
                p[n - 1] = ((double) (n - 1)) * p[n - 2];
                int k = n - 1;
                while (k >= 0) {
                    v = (v * x22) + p[k];
                    if (k > 2) {
                        x2 = x22;
                        p[k - 2] = (((double) (k - 1)) * p[k - 1]) + (((double) ((n * 2) - k)) * p[k - 3]);
                    } else {
                        x2 = x22;
                        if (k == 2) {
                            p[0] = p[1];
                            k -= 2;
                            x22 = x2;
                        }
                    }
                    k -= 2;
                    x22 = x2;
                }
                double x23 = x22;
                if ((n & 1) == 0) {
                    v *= x;
                }
                coeff2 *= f;
                function[n] = coeff2 * v;
                n++;
                x22 = x23;
                dSCompiler = this;
            }
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void asin(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double x2;
        DSCompiler dSCompiler = this;
        double[] function = new double[(dSCompiler.order + 1)];
        double x = operand[operandOffset];
        function[0] = FastMath.asin(x);
        if (dSCompiler.order > 0) {
            double[] p = new double[dSCompiler.order];
            p[0] = 1.0d;
            double x22 = x * x;
            double f = 1.0d / (1.0d - x22);
            double coeff = FastMath.sqrt(f);
            function[1] = p[0] * coeff;
            double coeff2 = coeff;
            int n = 2;
            while (n <= dSCompiler.order) {
                double v = 0.0d;
                p[n - 1] = ((double) (n - 1)) * p[n - 2];
                int k = n - 1;
                while (k >= 0) {
                    v = (v * x22) + p[k];
                    if (k > 2) {
                        x2 = x22;
                        p[k - 2] = (((double) (k - 1)) * p[k - 1]) + (((double) ((n * 2) - k)) * p[k - 3]);
                    } else {
                        x2 = x22;
                        if (k == 2) {
                            p[0] = p[1];
                            k -= 2;
                            x22 = x2;
                        }
                    }
                    k -= 2;
                    x22 = x2;
                }
                double x23 = x22;
                if ((n & 1) == 0) {
                    v *= x;
                }
                coeff2 *= f;
                function[n] = coeff2 * v;
                n++;
                x22 = x23;
                dSCompiler = this;
            }
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void atan(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double x2;
        DSCompiler dSCompiler = this;
        double[] function = new double[(dSCompiler.order + 1)];
        double x = operand[operandOffset];
        function[0] = FastMath.atan(x);
        if (dSCompiler.order > 0) {
            double[] q = new double[dSCompiler.order];
            q[0] = 1.0d;
            double x22 = x * x;
            double f = 1.0d / (x22 + 1.0d);
            double coeff = f;
            function[1] = q[0] * coeff;
            double coeff2 = coeff;
            int n = 2;
            while (n <= dSCompiler.order) {
                double v = 0.0d;
                q[n - 1] = ((double) (-n)) * q[n - 2];
                int k = n - 1;
                while (k >= 0) {
                    v = (v * x22) + q[k];
                    if (k > 2) {
                        x2 = x22;
                        q[k - 2] = (((double) (k - 1)) * q[k - 1]) + (((double) ((k - 1) - (n * 2))) * q[k - 3]);
                    } else {
                        x2 = x22;
                        if (k == 2) {
                            q[0] = q[1];
                            k -= 2;
                            x22 = x2;
                        }
                    }
                    k -= 2;
                    x22 = x2;
                }
                double x23 = x22;
                if ((n & 1) == 0) {
                    v *= x;
                }
                coeff2 *= f;
                function[n] = coeff2 * v;
                n++;
                x22 = x23;
                dSCompiler = this;
            }
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void atan2(double[] y, int yOffset, double[] x, int xOffset, double[] result, int resultOffset) {
        double[] tmp1 = new double[getSize()];
        multiply(x, xOffset, x, xOffset, tmp1, 0);
        double[] tmp2 = new double[getSize()];
        double[] dArr = tmp2;
        multiply(y, yOffset, y, yOffset, dArr, 0);
        add(tmp1, 0, tmp2, 0, dArr, 0);
        rootN(tmp2, 0, 2, tmp1, 0);
        if (x[xOffset] >= 0.0d) {
            int i = 0;
            add(tmp1, 0, x, xOffset, tmp2, 0);
            divide(y, yOffset, tmp2, 0, tmp1, 0);
            atan(tmp1, 0, tmp2, 0);
            while (true) {
                int i2 = i;
                if (i2 >= tmp2.length) {
                    break;
                }
                result[resultOffset + i2] = tmp2[i2] * 2.0d;
                i = i2 + 1;
            }
        } else {
            subtract(tmp1, 0, x, xOffset, tmp2, 0);
            divide(y, yOffset, tmp2, 0, tmp1, 0);
            atan(tmp1, 0, tmp2, 0);
            result[resultOffset] = (tmp2[0] <= 0.0d ? -3.141592653589793d : 3.141592653589793d) - (tmp2[0] * 2.0d);
            for (int i3 = 1; i3 < tmp2.length; i3++) {
                result[resultOffset + i3] = tmp2[i3] * -2.0d;
            }
        }
        result[resultOffset] = FastMath.atan2(y[yOffset], x[xOffset]);
    }

    public void cosh(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double[] function = new double[(this.order + 1)];
        function[0] = FastMath.cosh(operand[operandOffset]);
        if (this.order > 0) {
            function[1] = FastMath.sinh(operand[operandOffset]);
            for (int i = 2; i <= this.order; i++) {
                function[i] = function[i - 2];
            }
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void sinh(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double[] function = new double[(this.order + 1)];
        function[0] = FastMath.sinh(operand[operandOffset]);
        if (this.order > 0) {
            function[1] = FastMath.cosh(operand[operandOffset]);
            for (int i = 2; i <= this.order; i++) {
                function[i] = function[i - 2];
            }
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void tanh(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double[] p;
        double[] function = new double[(this.order + 1)];
        double t = FastMath.tanh(operand[operandOffset]);
        function[0] = t;
        if (this.order > 0) {
            double[] p2 = new double[(this.order + 2)];
            p2[1] = 1.0d;
            double t2 = t * t;
            int n = 1;
            while (n <= this.order) {
                double v = 0.0d;
                double t22 = t2;
                p2[n + 1] = ((double) (-n)) * p2[n];
                int k = n + 1;
                while (k >= 0) {
                    v = (v * t22) + p2[k];
                    if (k > 2) {
                        p = p2;
                        p[k - 2] = (((double) (k - 1)) * p2[k - 1]) - (((double) (k - 3)) * p[k - 3]);
                    } else {
                        p = p2;
                        if (k == 2) {
                            p[0] = p[1];
                            k -= 2;
                            p2 = p;
                        }
                    }
                    k -= 2;
                    p2 = p;
                }
                double[] p3 = p2;
                if ((n & 1) == 0) {
                    v *= t;
                }
                function[n] = v;
                n++;
                t2 = t22;
                p2 = p3;
            }
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void acosh(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double x2;
        double[] function = new double[(this.order + 1)];
        double x = operand[operandOffset];
        function[0] = FastMath.acosh(x);
        if (this.order > 0) {
            double[] p = new double[this.order];
            p[0] = 1.0d;
            double x22 = x * x;
            double f = 1.0d / (x22 - 1.0d);
            double coeff = FastMath.sqrt(f);
            function[1] = p[0] * coeff;
            double coeff2 = coeff;
            int n = 2;
            while (n <= this.order) {
                double v = 0.0d;
                double[] p2 = p;
                p2[n - 1] = ((double) (1 - n)) * p2[n - 2];
                int k = n - 1;
                while (k >= 0) {
                    v = (v * x22) + p2[k];
                    if (k > 2) {
                        x2 = x22;
                        p2[k - 2] = (((double) (1 - k)) * p2[k - 1]) + (((double) (k - (n * 2))) * p2[k - 3]);
                    } else {
                        x2 = x22;
                        if (k == 2) {
                            p2[0] = -p2[1];
                            k -= 2;
                            x22 = x2;
                        }
                    }
                    k -= 2;
                    x22 = x2;
                }
                double x23 = x22;
                if ((n & 1) == 0) {
                    v *= x;
                }
                coeff2 *= f;
                function[n] = coeff2 * v;
                n++;
                p = p2;
                x22 = x23;
            }
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void asinh(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double x2;
        double[] function = new double[(this.order + 1)];
        double x = operand[operandOffset];
        function[0] = FastMath.asinh(x);
        if (this.order > 0) {
            double[] p = new double[this.order];
            p[0] = 1.0d;
            double x22 = x * x;
            double f = 1.0d / (x22 + 1.0d);
            double coeff = FastMath.sqrt(f);
            function[1] = p[0] * coeff;
            double coeff2 = coeff;
            int n = 2;
            while (n <= this.order) {
                double v = 0.0d;
                double[] p2 = p;
                p2[n - 1] = ((double) (1 - n)) * p2[n - 2];
                int k = n - 1;
                while (k >= 0) {
                    v = (v * x22) + p2[k];
                    if (k > 2) {
                        x2 = x22;
                        p2[k - 2] = (((double) (k - 1)) * p2[k - 1]) + (((double) (k - (n * 2))) * p2[k - 3]);
                    } else {
                        x2 = x22;
                        if (k == 2) {
                            p2[0] = p2[1];
                            k -= 2;
                            x22 = x2;
                        }
                    }
                    k -= 2;
                    x22 = x2;
                }
                double x23 = x22;
                if ((n & 1) == 0) {
                    v *= x;
                }
                coeff2 *= f;
                function[n] = coeff2 * v;
                n++;
                p = p2;
                x22 = x23;
            }
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void atanh(double[] operand, int operandOffset, double[] result, int resultOffset) {
        double x2;
        double[] q;
        DSCompiler dSCompiler = this;
        double[] function = new double[(dSCompiler.order + 1)];
        double x = operand[operandOffset];
        function[0] = FastMath.atanh(x);
        if (dSCompiler.order > 0) {
            double[] q2 = new double[dSCompiler.order];
            q2[0] = 1.0d;
            double x22 = x * x;
            double f = 1.0d / (1.0d - x22);
            double coeff = f;
            function[1] = q2[0] * coeff;
            double coeff2 = coeff;
            int n = 2;
            while (n <= dSCompiler.order) {
                double v = 0.0d;
                q2[n - 1] = ((double) n) * q2[n - 2];
                int k = n - 1;
                while (k >= 0) {
                    v = (v * x22) + q2[k];
                    if (k > 2) {
                        q = q2;
                        x2 = x22;
                        q[k - 2] = (((double) (k - 1)) * q[k - 1]) + (((double) (((n * 2) - k) + 1)) * q[k - 3]);
                    } else {
                        q = q2;
                        x2 = x22;
                        if (k == 2) {
                            q[0] = q[1];
                            k -= 2;
                            q2 = q;
                            x22 = x2;
                        }
                    }
                    k -= 2;
                    q2 = q;
                    x22 = x2;
                }
                double[] q3 = q2;
                double x23 = x22;
                if ((n & 1) == 0) {
                    v *= x;
                }
                coeff2 *= f;
                function[n] = coeff2 * v;
                n++;
                q2 = q3;
                x22 = x23;
                dSCompiler = this;
            }
        }
        compose(operand, operandOffset, function, result, resultOffset);
    }

    public void compose(double[] operand, int operandOffset, double[] f, double[] result, int resultOffset) {
        for (int i = 0; i < this.compIndirection.length; i++) {
            int[][] mappingI = this.compIndirection[i];
            double r = 0.0d;
            for (int[] mappingIJ : mappingI) {
                double product = ((double) mappingIJ[0]) * f[mappingIJ[1]];
                for (int k = 2; k < mappingIJ.length; k++) {
                    product *= operand[operandOffset + mappingIJ[k]];
                }
                r += product;
            }
            result[resultOffset + i] = r;
        }
    }

    public double taylor(double[] ds, int dsOffset, double... delta) throws MathArithmeticException {
        double value = 0.0d;
        for (int i = getSize() - 1; i >= 0; i--) {
            int[] orders = getPartialDerivativeOrders(i);
            double term = ds[dsOffset + i];
            for (int k = 0; k < orders.length; k++) {
                if (orders[k] > 0) {
                    try {
                        term *= FastMath.pow(delta[k], orders[k]) / ((double) CombinatoricsUtils.factorial(orders[k]));
                    } catch (NotPositiveException e) {
                        throw new MathInternalError(e);
                    }
                }
            }
            value += term;
        }
        return value;
    }

    public void checkCompatibility(DSCompiler compiler) throws DimensionMismatchException {
        if (this.parameters != compiler.parameters) {
            throw new DimensionMismatchException(this.parameters, compiler.parameters);
        } else if (this.order != compiler.order) {
            throw new DimensionMismatchException(this.order, compiler.order);
        }
    }
}

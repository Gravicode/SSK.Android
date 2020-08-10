package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathUtils;
import org.apache.poi.p009ss.usermodel.ShapeTypes;

public class HaltonSequenceGenerator implements RandomVectorGenerator {
    private static final int[] PRIMES = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, ShapeTypes.VERTICAL_SCROLL, ShapeTypes.PLUS, ShapeTypes.FLOW_CHART_DOCUMENT, ShapeTypes.FLOW_CHART_TERMINATOR, ShapeTypes.FLOW_CHART_SORT, ShapeTypes.FLOW_CHART_MERGE, ShapeTypes.FLOW_CHART_DISPLAY, ShapeTypes.ACTION_BUTTON_HELP, ShapeTypes.ACTION_BUTTON_END, ShapeTypes.GEAR_6};
    private static final int[] WEIGHTS = {1, 2, 3, 3, 8, 11, 12, 14, 7, 18, 12, 13, 17, 18, 29, 14, 18, 43, 41, 44, 40, 30, 47, 65, 71, 28, 40, 60, 79, 89, 56, 50, 52, 61, 108, 56, 66, 63, 60, 66};
    private final int[] base;
    private int count;
    private final int dimension;
    private final int[] weight;

    public HaltonSequenceGenerator(int dimension2) throws OutOfRangeException {
        this(dimension2, PRIMES, WEIGHTS);
    }

    public HaltonSequenceGenerator(int dimension2, int[] bases, int[] weights) throws NullArgumentException, OutOfRangeException, DimensionMismatchException {
        this.count = 0;
        MathUtils.checkNotNull(bases);
        if (dimension2 < 1 || dimension2 > bases.length) {
            throw new OutOfRangeException(Integer.valueOf(dimension2), Integer.valueOf(1), Integer.valueOf(PRIMES.length));
        } else if (weights == null || weights.length == bases.length) {
            this.dimension = dimension2;
            this.base = (int[]) bases.clone();
            this.weight = weights == null ? null : (int[]) weights.clone();
            this.count = 0;
        } else {
            throw new DimensionMismatchException(weights.length, bases.length);
        }
    }

    public double[] nextVector() {
        double[] v = new double[this.dimension];
        for (int i = 0; i < this.dimension; i++) {
            double f = 1.0d / ((double) this.base[i]);
            int index = this.count;
            while (index > 0) {
                v[i] = v[i] + (((double) scramble(i, 0, this.base[i], index % this.base[i])) * f);
                index /= this.base[i];
                f /= (double) this.base[i];
            }
        }
        this.count++;
        return v;
    }

    /* access modifiers changed from: protected */
    public int scramble(int i, int j, int b, int digit) {
        return this.weight != null ? (this.weight[i] * digit) % b : digit;
    }

    public double[] skipTo(int index) throws NotPositiveException {
        this.count = index;
        return nextVector();
    }

    public int getNextIndex() {
        return this.count;
    }
}

package org.apache.commons.math3.stat.descriptive.rank;

import java.io.Serializable;
import java.util.Arrays;
import java.util.BitSet;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathUnsupportedOperationException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.stat.descriptive.AbstractUnivariateStatistic;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.KthSelector;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.MedianOf3PivotingStrategy;
import org.apache.commons.math3.util.PivotingStrategyInterface;
import org.apache.commons.math3.util.Precision;

public class Percentile extends AbstractUnivariateStatistic implements Serializable {
    private static final int MAX_CACHED_LEVELS = 10;
    private static final int PIVOTS_HEAP_LENGTH = 512;
    private static final long serialVersionUID = -8091216485095130416L;
    private int[] cachedPivots;
    private final EstimationType estimationType;
    private final KthSelector kthSelector;
    private final NaNStrategy nanStrategy;
    private double quantile;

    public enum EstimationType {
        LEGACY("Legacy Apache Commons Math") {
            /* access modifiers changed from: protected */
            public double index(double p, int length) {
                if (Double.compare(p, 0.0d) == 0) {
                    return 0.0d;
                }
                return Double.compare(p, 1.0d) == 0 ? (double) length : ((double) (length + 1)) * p;
            }
        },
        R_1("R-1") {
            /* access modifiers changed from: protected */
            public double index(double p, int length) {
                if (Double.compare(p, 0.0d) == 0) {
                    return 0.0d;
                }
                return (((double) length) * p) + 0.5d;
            }

            /* access modifiers changed from: protected */
            public double estimate(double[] values, int[] pivotsHeap, double pos, int length, KthSelector selector) {
                return super.estimate(values, pivotsHeap, FastMath.ceil(pos - 0.5d), length, selector);
            }
        },
        R_2("R-2") {
            /* access modifiers changed from: protected */
            public double index(double p, int length) {
                if (Double.compare(p, 1.0d) == 0) {
                    return (double) length;
                }
                if (Double.compare(p, 0.0d) == 0) {
                    return 0.0d;
                }
                return (((double) length) * p) + 0.5d;
            }

            /* access modifiers changed from: protected */
            public double estimate(double[] values, int[] pivotsHeap, double pos, int length, KthSelector selector) {
                double[] dArr = values;
                int[] iArr = pivotsHeap;
                int i = length;
                KthSelector kthSelector = selector;
                return (super.estimate(dArr, iArr, FastMath.ceil(pos - 0.5d), i, kthSelector) + super.estimate(dArr, iArr, FastMath.floor(0.5d + pos), i, kthSelector)) / 2.0d;
            }
        },
        R_3("R-3") {
            /* access modifiers changed from: protected */
            public double index(double p, int length) {
                if (Double.compare(p, 0.5d / ((double) length)) <= 0) {
                    return 0.0d;
                }
                return FastMath.rint(((double) length) * p);
            }
        },
        R_4("R-4") {
            /* access modifiers changed from: protected */
            public double index(double p, int length) {
                if (Double.compare(p, 1.0d / ((double) length)) < 0) {
                    return 0.0d;
                }
                return Double.compare(p, 1.0d) == 0 ? (double) length : ((double) length) * p;
            }
        },
        R_5("R-5") {
            /* access modifiers changed from: protected */
            public double index(double p, int length) {
                double maxLimit = (((double) length) - 0.5d) / ((double) length);
                if (Double.compare(p, 0.5d / ((double) length)) < 0) {
                    return 0.0d;
                }
                return Double.compare(p, maxLimit) >= 0 ? (double) length : 0.5d + (((double) length) * p);
            }
        },
        R_6("R-6") {
            /* access modifiers changed from: protected */
            public double index(double p, int length) {
                double maxLimit = (((double) length) * 1.0d) / ((double) (length + 1));
                if (Double.compare(p, 1.0d / ((double) (length + 1))) < 0) {
                    return 0.0d;
                }
                return Double.compare(p, maxLimit) >= 0 ? (double) length : ((double) (length + 1)) * p;
            }
        },
        R_7("R-7") {
            /* access modifiers changed from: protected */
            public double index(double p, int length) {
                if (Double.compare(p, 0.0d) == 0) {
                    return 0.0d;
                }
                return Double.compare(p, 1.0d) == 0 ? (double) length : 1.0d + (((double) (length - 1)) * p);
            }
        },
        R_8("R-8") {
            /* access modifiers changed from: protected */
            public double index(double p, int length) {
                double maxLimit = (((double) length) - 0.3333333333333333d) / (((double) length) + 0.3333333333333333d);
                if (Double.compare(p, 0.6666666666666666d / (((double) length) + 0.3333333333333333d)) < 0) {
                    return 0.0d;
                }
                return Double.compare(p, maxLimit) >= 0 ? (double) length : 0.3333333333333333d + ((((double) length) + 0.3333333333333333d) * p);
            }
        },
        R_9("R-9") {
            /* access modifiers changed from: protected */
            public double index(double p, int length) {
                double maxLimit = (((double) length) - 0.375d) / (((double) length) + 0.25d);
                if (Double.compare(p, 0.625d / (((double) length) + 0.25d)) < 0) {
                    return 0.0d;
                }
                return Double.compare(p, maxLimit) >= 0 ? (double) length : ((((double) length) + 0.25d) * p) + 0.375d;
            }
        };
        
        private final String name;

        /* access modifiers changed from: protected */
        public abstract double index(double d, int i);

        private EstimationType(String type) {
            this.name = type;
        }

        /* access modifiers changed from: protected */
        public double estimate(double[] work, int[] pivotsHeap, double pos, int length, KthSelector selector) {
            double[] dArr = work;
            int[] iArr = pivotsHeap;
            int i = length;
            KthSelector kthSelector = selector;
            double fpos = FastMath.floor(pos);
            int intPos = (int) fpos;
            double dif = pos - fpos;
            if (pos < 1.0d) {
                return kthSelector.select(dArr, iArr, 0);
            }
            if (pos >= ((double) i)) {
                return kthSelector.select(dArr, iArr, i - 1);
            }
            double lower = kthSelector.select(dArr, iArr, intPos - 1);
            return ((kthSelector.select(dArr, iArr, intPos) - lower) * dif) + lower;
        }

        /* access modifiers changed from: protected */
        public double evaluate(double[] work, int[] pivotsHeap, double p, KthSelector selector) {
            MathUtils.checkNotNull(work);
            if (p > 100.0d || p <= 0.0d) {
                throw new OutOfRangeException(LocalizedFormats.OUT_OF_BOUNDS_QUANTILE_VALUE, Double.valueOf(p), Integer.valueOf(0), Integer.valueOf(100));
            }
            return estimate(work, pivotsHeap, index(p / 100.0d, work.length), work.length, selector);
        }

        public double evaluate(double[] work, double p, KthSelector selector) {
            return evaluate(work, null, p, selector);
        }

        /* access modifiers changed from: 0000 */
        public String getName() {
            return this.name;
        }
    }

    public Percentile() {
        this(50.0d);
    }

    public Percentile(double quantile2) throws MathIllegalArgumentException {
        EstimationType estimationType2 = EstimationType.LEGACY;
        NaNStrategy naNStrategy = NaNStrategy.REMOVED;
        KthSelector kthSelector2 = new KthSelector(new MedianOf3PivotingStrategy());
        this(quantile2, estimationType2, naNStrategy, kthSelector2);
    }

    public Percentile(Percentile original) throws NullArgumentException {
        MathUtils.checkNotNull(original);
        this.estimationType = original.getEstimationType();
        this.nanStrategy = original.getNaNStrategy();
        this.kthSelector = original.getKthSelector();
        setData(original.getDataRef());
        if (original.cachedPivots != null) {
            System.arraycopy(original.cachedPivots, 0, this.cachedPivots, 0, original.cachedPivots.length);
        }
        setQuantile(original.quantile);
    }

    protected Percentile(double quantile2, EstimationType estimationType2, NaNStrategy nanStrategy2, KthSelector kthSelector2) throws MathIllegalArgumentException {
        setQuantile(quantile2);
        this.cachedPivots = null;
        MathUtils.checkNotNull(estimationType2);
        MathUtils.checkNotNull(nanStrategy2);
        MathUtils.checkNotNull(kthSelector2);
        this.estimationType = estimationType2;
        this.nanStrategy = nanStrategy2;
        this.kthSelector = kthSelector2;
    }

    public void setData(double[] values) {
        if (values == null) {
            this.cachedPivots = null;
        } else {
            this.cachedPivots = new int[512];
            Arrays.fill(this.cachedPivots, -1);
        }
        super.setData(values);
    }

    public void setData(double[] values, int begin, int length) throws MathIllegalArgumentException {
        if (values == null) {
            this.cachedPivots = null;
        } else {
            this.cachedPivots = new int[512];
            Arrays.fill(this.cachedPivots, -1);
        }
        super.setData(values, begin, length);
    }

    public double evaluate(double p) throws MathIllegalArgumentException {
        return evaluate(getDataRef(), p);
    }

    public double evaluate(double[] values, double p) throws MathIllegalArgumentException {
        test(values, 0, 0);
        return evaluate(values, 0, values.length, p);
    }

    public double evaluate(double[] values, int start, int length) throws MathIllegalArgumentException {
        return evaluate(values, start, length, this.quantile);
    }

    public double evaluate(double[] values, int begin, int length, double p) throws MathIllegalArgumentException {
        test(values, begin, length);
        if (p > 100.0d || p <= 0.0d) {
            throw new OutOfRangeException(LocalizedFormats.OUT_OF_BOUNDS_QUANTILE_VALUE, Double.valueOf(p), Integer.valueOf(0), Integer.valueOf(100));
        }
        double d = Double.NaN;
        if (length == 0) {
            return Double.NaN;
        }
        if (length == 1) {
            return values[begin];
        }
        double[] work = getWorkArray(values, begin, length);
        int[] pivotsHeap = getPivots(values);
        if (work.length != 0) {
            d = this.estimationType.evaluate(work, pivotsHeap, p, this.kthSelector);
        }
        return d;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    public int medianOf3(double[] work, int begin, int end) {
        return new MedianOf3PivotingStrategy().pivotIndex(work, begin, end);
    }

    public double getQuantile() {
        return this.quantile;
    }

    public void setQuantile(double p) throws MathIllegalArgumentException {
        if (p <= 0.0d || p > 100.0d) {
            throw new OutOfRangeException(LocalizedFormats.OUT_OF_BOUNDS_QUANTILE_VALUE, Double.valueOf(p), Integer.valueOf(0), Integer.valueOf(100));
        }
        this.quantile = p;
    }

    public Percentile copy() {
        return new Percentile(this);
    }

    @Deprecated
    public static void copy(Percentile source, Percentile dest) throws MathUnsupportedOperationException {
        throw new MathUnsupportedOperationException();
    }

    /* access modifiers changed from: protected */
    public double[] getWorkArray(double[] values, int begin, int length) {
        if (values == getDataRef()) {
            return getDataRef();
        }
        switch (this.nanStrategy) {
            case MAXIMAL:
                return replaceAndSlice(values, begin, length, Double.NaN, Double.POSITIVE_INFINITY);
            case MINIMAL:
                return replaceAndSlice(values, begin, length, Double.NaN, Double.NEGATIVE_INFINITY);
            case REMOVED:
                return removeAndSlice(values, begin, length, Double.NaN);
            case FAILED:
                double[] work = copyOf(values, begin, length);
                MathArrays.checkNotNaN(work);
                return work;
            default:
                return copyOf(values, begin, length);
        }
    }

    private static double[] copyOf(double[] values, int begin, int length) {
        MathArrays.verifyValues(values, begin, length);
        return MathArrays.copyOfRange(values, begin, begin + length);
    }

    private static double[] replaceAndSlice(double[] values, int begin, int length, double original, double replacement) {
        double[] temp = copyOf(values, begin, length);
        int i = 0;
        while (i < length) {
            temp[i] = Precision.equalsIncludingNaN(original, temp[i]) ? replacement : temp[i];
            i++;
        }
        return temp;
    }

    private static double[] removeAndSlice(double[] values, int begin, int length, double removedValue) {
        MathArrays.verifyValues(values, begin, length);
        BitSet bits = new BitSet(length);
        for (int i = begin; i < begin + length; i++) {
            if (Precision.equalsIncludingNaN(removedValue, values[i])) {
                bits.set(i - begin);
            }
        }
        if (bits.isEmpty() != 0) {
            return copyOf(values, begin, length);
        }
        int bitSetPtr = 0;
        if (bits.cardinality() == length) {
            return new double[0];
        }
        double[] temp = new double[(length - bits.cardinality())];
        int start = begin;
        int dest = 0;
        while (true) {
            int nextSetBit = bits.nextSetBit(bitSetPtr);
            int nextOne = nextSetBit;
            if (nextSetBit == -1) {
                break;
            }
            int lengthToCopy = nextOne - bitSetPtr;
            System.arraycopy(values, start, temp, dest, lengthToCopy);
            dest += lengthToCopy;
            int nextClearBit = bits.nextClearBit(nextOne);
            bitSetPtr = nextClearBit;
            start = begin + nextClearBit;
        }
        if (start >= begin + length) {
            return temp;
        }
        System.arraycopy(values, start, temp, dest, (begin + length) - start);
        return temp;
    }

    private int[] getPivots(double[] values) {
        if (values == getDataRef()) {
            return this.cachedPivots;
        }
        int[] pivotsHeap = new int[512];
        Arrays.fill(pivotsHeap, -1);
        return pivotsHeap;
    }

    public EstimationType getEstimationType() {
        return this.estimationType;
    }

    public Percentile withEstimationType(EstimationType newEstimationType) {
        Percentile percentile = new Percentile(this.quantile, newEstimationType, this.nanStrategy, this.kthSelector);
        return percentile;
    }

    public NaNStrategy getNaNStrategy() {
        return this.nanStrategy;
    }

    public Percentile withNaNStrategy(NaNStrategy newNaNStrategy) {
        Percentile percentile = new Percentile(this.quantile, this.estimationType, newNaNStrategy, this.kthSelector);
        return percentile;
    }

    public KthSelector getKthSelector() {
        return this.kthSelector;
    }

    public PivotingStrategyInterface getPivotingStrategy() {
        return this.kthSelector.getPivotingStrategy();
    }

    public Percentile withKthSelector(KthSelector newKthSelector) {
        Percentile percentile = new Percentile(this.quantile, this.estimationType, this.nanStrategy, newKthSelector);
        return percentile;
    }
}

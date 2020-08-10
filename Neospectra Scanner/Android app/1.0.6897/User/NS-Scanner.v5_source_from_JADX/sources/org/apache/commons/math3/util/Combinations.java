package org.apache.commons.math3.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.OutOfRangeException;

public class Combinations implements Iterable<int[]> {
    private final IterationOrder iterationOrder;

    /* renamed from: k */
    private final int f798k;

    /* renamed from: n */
    private final int f799n;

    /* renamed from: org.apache.commons.math3.util.Combinations$1 */
    static /* synthetic */ class C10561 {

        /* renamed from: $SwitchMap$org$apache$commons$math3$util$Combinations$IterationOrder */
        static final /* synthetic */ int[] f800xf3d2afa = new int[IterationOrder.values().length];

        static {
            try {
                f800xf3d2afa[IterationOrder.LEXICOGRAPHIC.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
        }
    }

    private enum IterationOrder {
        LEXICOGRAPHIC
    }

    private static class LexicographicComparator implements Comparator<int[]>, Serializable {
        private static final long serialVersionUID = 20130906;

        /* renamed from: k */
        private final int f801k;

        /* renamed from: n */
        private final int f802n;

        LexicographicComparator(int n, int k) {
            this.f802n = n;
            this.f801k = k;
        }

        public int compare(int[] c1, int[] c2) {
            if (c1.length != this.f801k) {
                throw new DimensionMismatchException(c1.length, this.f801k);
            } else if (c2.length != this.f801k) {
                throw new DimensionMismatchException(c2.length, this.f801k);
            } else {
                int[] c1s = MathArrays.copyOf(c1);
                Arrays.sort(c1s);
                int[] c2s = MathArrays.copyOf(c2);
                Arrays.sort(c2s);
                long v1 = lexNorm(c1s);
                long v2 = lexNorm(c2s);
                if (v1 < v2) {
                    return -1;
                }
                if (v1 > v2) {
                    return 1;
                }
                return 0;
            }
        }

        private long lexNorm(int[] c) {
            long ret = 0;
            for (int i = 0; i < c.length; i++) {
                int digit = c[i];
                if (digit < 0 || digit >= this.f802n) {
                    throw new OutOfRangeException(Integer.valueOf(digit), Integer.valueOf(0), Integer.valueOf(this.f802n - 1));
                }
                ret += (long) (c[i] * ArithmeticUtils.pow(this.f802n, i));
            }
            return ret;
        }
    }

    private static class LexicographicIterator implements Iterator<int[]> {

        /* renamed from: c */
        private final int[] f803c;

        /* renamed from: j */
        private int f804j;

        /* renamed from: k */
        private final int f805k;
        private boolean more = true;

        LexicographicIterator(int n, int k) {
            this.f805k = k;
            this.f803c = new int[(k + 3)];
            if (k == 0 || k >= n) {
                this.more = false;
                return;
            }
            for (int i = 1; i <= k; i++) {
                this.f803c[i] = i - 1;
            }
            this.f803c[k + 1] = n;
            this.f803c[k + 2] = 0;
            this.f804j = k;
        }

        public boolean hasNext() {
            return this.more;
        }

        public int[] next() {
            if (!this.more) {
                throw new NoSuchElementException();
            }
            int[] ret = new int[this.f805k];
            System.arraycopy(this.f803c, 1, ret, 0, this.f805k);
            if (this.f804j > 0) {
                this.f803c[this.f804j] = this.f804j;
                this.f804j--;
                return ret;
            } else if (this.f803c[1] + 1 < this.f803c[2]) {
                int[] iArr = this.f803c;
                iArr[1] = iArr[1] + 1;
                return ret;
            } else {
                this.f804j = 2;
                int x = 0;
                boolean stepDone = false;
                while (!stepDone) {
                    this.f803c[this.f804j - 1] = this.f804j - 2;
                    x = this.f803c[this.f804j] + 1;
                    if (x == this.f803c[this.f804j + 1]) {
                        this.f804j++;
                    } else {
                        stepDone = true;
                    }
                }
                if (this.f804j > this.f805k) {
                    this.more = false;
                    return ret;
                }
                this.f803c[this.f804j] = x;
                this.f804j--;
                return ret;
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private static class SingletonIterator implements Iterator<int[]> {
        private boolean more = true;
        private final int[] singleton;

        SingletonIterator(int[] singleton2) {
            this.singleton = singleton2;
        }

        public boolean hasNext() {
            return this.more;
        }

        public int[] next() {
            if (this.more) {
                this.more = false;
                return this.singleton;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public Combinations(int n, int k) {
        this(n, k, IterationOrder.LEXICOGRAPHIC);
    }

    private Combinations(int n, int k, IterationOrder iterationOrder2) {
        CombinatoricsUtils.checkBinomial(n, k);
        this.f799n = n;
        this.f798k = k;
        this.iterationOrder = iterationOrder2;
    }

    public int getN() {
        return this.f799n;
    }

    public int getK() {
        return this.f798k;
    }

    public Iterator<int[]> iterator() {
        if (this.f798k == 0 || this.f798k == this.f799n) {
            return new SingletonIterator(MathArrays.natural(this.f798k));
        }
        if (C10561.f800xf3d2afa[this.iterationOrder.ordinal()] == 1) {
            return new LexicographicIterator(this.f799n, this.f798k);
        }
        throw new MathInternalError();
    }

    public Comparator<int[]> comparator() {
        return new LexicographicComparator(this.f799n, this.f798k);
    }
}

package org.apache.commons.math3.util;

import java.util.Iterator;
import org.apache.commons.math3.exception.MathUnsupportedOperationException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.ZeroException;

public class IntegerSequence {

    public static class Incrementor implements Iterator<Integer> {
        private static final MaxCountExceededCallback CALLBACK = new MaxCountExceededCallback() {
            public void trigger(int max) throws MaxCountExceededException {
                throw new MaxCountExceededException(Integer.valueOf(max));
            }
        };
        private int count = 0;
        private final int increment;
        private final int init;
        private final MaxCountExceededCallback maxCountCallback;
        private final int maximalCount;

        public interface MaxCountExceededCallback {
            void trigger(int i) throws MaxCountExceededException;
        }

        private Incrementor(int start, int max, int step, MaxCountExceededCallback cb) throws NullArgumentException {
            if (cb == null) {
                throw new NullArgumentException();
            }
            this.init = start;
            this.maximalCount = max;
            this.increment = step;
            this.maxCountCallback = cb;
            this.count = start;
        }

        public static Incrementor create() {
            return new Incrementor(0, 0, 1, CALLBACK);
        }

        public Incrementor withStart(int start) {
            return new Incrementor(start, this.maximalCount, this.increment, this.maxCountCallback);
        }

        public Incrementor withMaximalCount(int max) {
            return new Incrementor(this.init, max, this.increment, this.maxCountCallback);
        }

        public Incrementor withIncrement(int step) {
            if (step != 0) {
                return new Incrementor(this.init, this.maximalCount, step, this.maxCountCallback);
            }
            throw new ZeroException();
        }

        public Incrementor withCallback(MaxCountExceededCallback cb) {
            return new Incrementor(this.init, this.maximalCount, this.increment, cb);
        }

        public int getMaximalCount() {
            return this.maximalCount;
        }

        public int getCount() {
            return this.count;
        }

        public boolean canIncrement() {
            return canIncrement(1);
        }

        public boolean canIncrement(int nTimes) {
            int finalCount = this.count + (this.increment * nTimes);
            if (this.increment < 0) {
                if (finalCount <= this.maximalCount) {
                    return false;
                }
            } else if (finalCount >= this.maximalCount) {
                return false;
            }
            return true;
        }

        public void increment(int nTimes) throws MaxCountExceededException {
            if (nTimes <= 0) {
                throw new NotStrictlyPositiveException(Integer.valueOf(nTimes));
            }
            if (!canIncrement(0)) {
                this.maxCountCallback.trigger(this.maximalCount);
            }
            this.count += this.increment * nTimes;
        }

        public void increment() throws MaxCountExceededException {
            increment(1);
        }

        public boolean hasNext() {
            return canIncrement(0);
        }

        public Integer next() {
            int value = this.count;
            increment();
            return Integer.valueOf(value);
        }

        public void remove() {
            throw new MathUnsupportedOperationException();
        }
    }

    public static class Range implements Iterable<Integer> {
        private final int max;
        private final int size;
        private final int start;
        private final int step;

        public Range(int start2, int max2, int step2) {
            this.start = start2;
            this.max = max2;
            this.step = step2;
            int s = ((max2 - start2) / step2) + 1;
            this.size = s < 0 ? 0 : s;
        }

        public int size() {
            return this.size;
        }

        public Iterator<Integer> iterator() {
            return Incrementor.create().withStart(this.start).withMaximalCount(this.max + (this.step > 0 ? 1 : -1)).withIncrement(this.step);
        }
    }

    private IntegerSequence() {
    }

    public static Range range(int start, int end) {
        return range(start, end, 1);
    }

    public static Range range(int start, int max, int step) {
        return new Range(start, max, step);
    }
}

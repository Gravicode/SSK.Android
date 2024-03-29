package org.apache.commons.math3.util;

import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.math3.exception.NullArgumentException;

public class KthSelector implements Serializable {
    private static final int MIN_SELECT_SIZE = 15;
    private static final long serialVersionUID = 20140713;
    private final PivotingStrategyInterface pivotingStrategy;

    public KthSelector() {
        this.pivotingStrategy = new MedianOf3PivotingStrategy();
    }

    public KthSelector(PivotingStrategyInterface pivotingStrategy2) throws NullArgumentException {
        MathUtils.checkNotNull(pivotingStrategy2);
        this.pivotingStrategy = pivotingStrategy2;
    }

    public PivotingStrategyInterface getPivotingStrategy() {
        return this.pivotingStrategy;
    }

    public double select(double[] work, int[] pivotsHeap, int k) {
        int pivot;
        int begin = 0;
        int end = work.length;
        int node = 0;
        boolean usePivotsHeap = pivotsHeap != null;
        while (end - begin > 15) {
            if (!usePivotsHeap || node >= pivotsHeap.length || pivotsHeap[node] < 0) {
                pivot = partition(work, begin, end, this.pivotingStrategy.pivotIndex(work, begin, end));
                if (usePivotsHeap && node < pivotsHeap.length) {
                    pivotsHeap[node] = pivot;
                }
            } else {
                pivot = pivotsHeap[node];
            }
            if (k == pivot) {
                return work[k];
            }
            if (k < pivot) {
                end = pivot;
                node = FastMath.min((node * 2) + 1, usePivotsHeap ? pivotsHeap.length : end);
            } else {
                begin = pivot + 1;
                node = FastMath.min((node * 2) + 2, usePivotsHeap ? pivotsHeap.length : end);
            }
        }
        Arrays.sort(work, begin, end);
        return work[k];
    }

    private int partition(double[] work, int begin, int end, int pivot) {
        double value = work[pivot];
        work[pivot] = work[begin];
        int i = begin + 1;
        int j = end - 1;
        while (i < j) {
            while (i < j && work[j] > value) {
                j--;
            }
            while (i < j && work[i] < value) {
                i++;
            }
            if (i < j) {
                double tmp = work[i];
                int i2 = i + 1;
                work[i] = work[j];
                int j2 = j - 1;
                work[j] = tmp;
                j = j2;
                i = i2;
            }
        }
        if (i >= end || work[i] > value) {
            i--;
        }
        work[begin] = work[i];
        work[i] = value;
        return i;
    }
}

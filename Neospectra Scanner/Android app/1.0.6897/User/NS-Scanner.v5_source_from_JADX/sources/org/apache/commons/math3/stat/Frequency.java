package org.apache.commons.math3.stat;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathUtils;

public class Frequency implements Serializable {
    private static final long serialVersionUID = -3845586908418844111L;
    private final SortedMap<Comparable<?>, Long> freqTable;

    private static class NaturalComparator<T extends Comparable<T>> implements Comparator<Comparable<T>>, Serializable {
        private static final long serialVersionUID = -3852193713161395148L;

        private NaturalComparator() {
        }

        public int compare(Comparable<T> o1, Comparable<T> o2) {
            return o1.compareTo(o2);
        }
    }

    public Frequency() {
        this.freqTable = new TreeMap();
    }

    public Frequency(Comparator<?> comparator) {
        this.freqTable = new TreeMap(comparator);
    }

    public String toString() {
        NumberFormat nf = NumberFormat.getPercentInstance();
        StringBuilder outBuffer = new StringBuilder();
        outBuffer.append("Value \t Freq. \t Pct. \t Cum Pct. \n");
        for (Comparable<?> value : this.freqTable.keySet()) {
            outBuffer.append(value);
            outBuffer.append(9);
            outBuffer.append(getCount(value));
            outBuffer.append(9);
            outBuffer.append(nf.format(getPct(value)));
            outBuffer.append(9);
            outBuffer.append(nf.format(getCumPct(value)));
            outBuffer.append(10);
        }
        return outBuffer.toString();
    }

    public void addValue(Comparable<?> v) throws MathIllegalArgumentException {
        incrementValue(v, 1);
    }

    public void addValue(int v) throws MathIllegalArgumentException {
        addValue((Comparable<?>) Long.valueOf((long) v));
    }

    public void addValue(long v) throws MathIllegalArgumentException {
        addValue((Comparable<?>) Long.valueOf(v));
    }

    public void addValue(char v) throws MathIllegalArgumentException {
        addValue((Comparable<?>) Character.valueOf(v));
    }

    public void incrementValue(Comparable<?> v, long increment) throws MathIllegalArgumentException {
        Comparable<?> obj = v;
        if (v instanceof Integer) {
            obj = Long.valueOf(((Integer) v).longValue());
        }
        try {
            Long count = (Long) this.freqTable.get(obj);
            if (count == null) {
                this.freqTable.put(obj, Long.valueOf(increment));
            } else {
                this.freqTable.put(obj, Long.valueOf(count.longValue() + increment));
            }
        } catch (ClassCastException e) {
            throw new MathIllegalArgumentException(LocalizedFormats.INSTANCES_NOT_COMPARABLE_TO_EXISTING_VALUES, v.getClass().getName());
        }
    }

    public void incrementValue(int v, long increment) throws MathIllegalArgumentException {
        incrementValue((Comparable<?>) Long.valueOf((long) v), increment);
    }

    public void incrementValue(long v, long increment) throws MathIllegalArgumentException {
        incrementValue((Comparable<?>) Long.valueOf(v), increment);
    }

    public void incrementValue(char v, long increment) throws MathIllegalArgumentException {
        incrementValue((Comparable<?>) Character.valueOf(v), increment);
    }

    public void clear() {
        this.freqTable.clear();
    }

    public Iterator<Comparable<?>> valuesIterator() {
        return this.freqTable.keySet().iterator();
    }

    public Iterator<Entry<Comparable<?>, Long>> entrySetIterator() {
        return this.freqTable.entrySet().iterator();
    }

    public long getSumFreq() {
        long result = 0;
        for (Long longValue : this.freqTable.values()) {
            result += longValue.longValue();
        }
        return result;
    }

    public long getCount(Comparable<?> v) {
        if (v instanceof Integer) {
            return getCount(((Integer) v).longValue());
        }
        long result = 0;
        try {
            Long count = (Long) this.freqTable.get(v);
            if (count != null) {
                result = count.longValue();
            }
        } catch (ClassCastException e) {
        }
        return result;
    }

    public long getCount(int v) {
        return getCount((Comparable<?>) Long.valueOf((long) v));
    }

    public long getCount(long v) {
        return getCount((Comparable<?>) Long.valueOf(v));
    }

    public long getCount(char v) {
        return getCount((Comparable<?>) Character.valueOf(v));
    }

    public int getUniqueCount() {
        return this.freqTable.keySet().size();
    }

    public double getPct(Comparable<?> v) {
        long sumFreq = getSumFreq();
        if (sumFreq == 0) {
            return Double.NaN;
        }
        return ((double) getCount(v)) / ((double) sumFreq);
    }

    public double getPct(int v) {
        return getPct((Comparable<?>) Long.valueOf((long) v));
    }

    public double getPct(long v) {
        return getPct((Comparable<?>) Long.valueOf(v));
    }

    public double getPct(char v) {
        return getPct((Comparable<?>) Character.valueOf(v));
    }

    public long getCumFreq(Comparable<?> v) {
        if (getSumFreq() == 0) {
            return 0;
        }
        if (v instanceof Integer) {
            return getCumFreq(((Integer) v).longValue());
        }
        Comparator comparator = this.freqTable.comparator();
        if (comparator == null) {
            comparator = new NaturalComparator();
        }
        long result = 0;
        try {
            Long value = (Long) this.freqTable.get(v);
            if (value != null) {
                result = value.longValue();
            }
            if (comparator.compare(v, this.freqTable.firstKey()) < 0) {
                return 0;
            }
            if (comparator.compare(v, this.freqTable.lastKey()) >= 0) {
                return getSumFreq();
            }
            Iterator<Comparable<?>> values = valuesIterator();
            while (values.hasNext()) {
                Comparable<?> nextValue = (Comparable) values.next();
                if (comparator.compare(v, nextValue) <= 0) {
                    return result;
                }
                result += getCount(nextValue);
            }
            return result;
        } catch (ClassCastException e) {
            return 0;
        }
    }

    public long getCumFreq(int v) {
        return getCumFreq((Comparable<?>) Long.valueOf((long) v));
    }

    public long getCumFreq(long v) {
        return getCumFreq((Comparable<?>) Long.valueOf(v));
    }

    public long getCumFreq(char v) {
        return getCumFreq((Comparable<?>) Character.valueOf(v));
    }

    public double getCumPct(Comparable<?> v) {
        long sumFreq = getSumFreq();
        if (sumFreq == 0) {
            return Double.NaN;
        }
        return ((double) getCumFreq(v)) / ((double) sumFreq);
    }

    public double getCumPct(int v) {
        return getCumPct((Comparable<?>) Long.valueOf((long) v));
    }

    public double getCumPct(long v) {
        return getCumPct((Comparable<?>) Long.valueOf(v));
    }

    public double getCumPct(char v) {
        return getCumPct((Comparable<?>) Character.valueOf(v));
    }

    public List<Comparable<?>> getMode() {
        long mostPopular = 0;
        for (Long l : this.freqTable.values()) {
            long frequency = l.longValue();
            if (frequency > mostPopular) {
                mostPopular = frequency;
            }
        }
        List<Comparable<?>> modeList = new ArrayList<>();
        for (Entry<Comparable<?>, Long> ent : this.freqTable.entrySet()) {
            if (((Long) ent.getValue()).longValue() == mostPopular) {
                modeList.add(ent.getKey());
            }
        }
        return modeList;
    }

    public void merge(Frequency other) throws NullArgumentException {
        MathUtils.checkNotNull(other, LocalizedFormats.NULL_NOT_ALLOWED, new Object[0]);
        Iterator<Entry<Comparable<?>, Long>> iter = other.entrySetIterator();
        while (iter.hasNext()) {
            Entry<Comparable<?>, Long> entry = (Entry) iter.next();
            incrementValue((Comparable) entry.getKey(), ((Long) entry.getValue()).longValue());
        }
    }

    public void merge(Collection<Frequency> others) throws NullArgumentException {
        MathUtils.checkNotNull(others, LocalizedFormats.NULL_NOT_ALLOWED, new Object[0]);
        for (Frequency freq : others) {
            merge(freq);
        }
    }

    public int hashCode() {
        return (1 * 31) + (this.freqTable == null ? 0 : this.freqTable.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Frequency)) {
            return false;
        }
        Frequency other = (Frequency) obj;
        if (this.freqTable == null) {
            if (other.freqTable != null) {
                return false;
            }
        } else if (!this.freqTable.equals(other.freqTable)) {
            return false;
        }
        return true;
    }
}

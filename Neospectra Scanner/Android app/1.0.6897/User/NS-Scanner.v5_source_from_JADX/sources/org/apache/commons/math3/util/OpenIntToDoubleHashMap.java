package org.apache.commons.math3.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

public class OpenIntToDoubleHashMap implements Serializable {
    private static final int DEFAULT_EXPECTED_SIZE = 16;
    protected static final byte FREE = 0;
    protected static final byte FULL = 1;
    private static final float LOAD_FACTOR = 0.5f;
    private static final int PERTURB_SHIFT = 5;
    protected static final byte REMOVED = 2;
    private static final int RESIZE_MULTIPLIER = 2;
    private static final long serialVersionUID = -3646337053166149105L;
    /* access modifiers changed from: private */
    public transient int count;
    /* access modifiers changed from: private */
    public int[] keys;
    private int mask;
    private final double missingEntries;
    private int size;
    /* access modifiers changed from: private */
    public byte[] states;
    /* access modifiers changed from: private */
    public double[] values;

    public class Iterator {
        private int current;
        private int next;
        private final int referenceCount;

        private Iterator() {
            this.referenceCount = OpenIntToDoubleHashMap.this.count;
            this.next = -1;
            try {
                advance();
            } catch (NoSuchElementException e) {
            }
        }

        public boolean hasNext() {
            return this.next >= 0;
        }

        public int key() throws ConcurrentModificationException, NoSuchElementException {
            if (this.referenceCount != OpenIntToDoubleHashMap.this.count) {
                throw new ConcurrentModificationException();
            } else if (this.current >= 0) {
                return OpenIntToDoubleHashMap.this.keys[this.current];
            } else {
                throw new NoSuchElementException();
            }
        }

        public double value() throws ConcurrentModificationException, NoSuchElementException {
            if (this.referenceCount != OpenIntToDoubleHashMap.this.count) {
                throw new ConcurrentModificationException();
            } else if (this.current >= 0) {
                return OpenIntToDoubleHashMap.this.values[this.current];
            } else {
                throw new NoSuchElementException();
            }
        }

        public void advance() throws ConcurrentModificationException, NoSuchElementException {
            byte[] access$400;
            int i;
            if (this.referenceCount != OpenIntToDoubleHashMap.this.count) {
                throw new ConcurrentModificationException();
            }
            this.current = this.next;
            do {
                try {
                    access$400 = OpenIntToDoubleHashMap.this.states;
                    i = this.next + 1;
                    this.next = i;
                } catch (ArrayIndexOutOfBoundsException e) {
                    this.next = -2;
                    if (this.current < 0) {
                        throw new NoSuchElementException();
                    }
                    return;
                }
            } while (access$400[i] != 1);
        }
    }

    public OpenIntToDoubleHashMap() {
        this(16, Double.NaN);
    }

    public OpenIntToDoubleHashMap(double missingEntries2) {
        this(16, missingEntries2);
    }

    public OpenIntToDoubleHashMap(int expectedSize) {
        this(expectedSize, Double.NaN);
    }

    public OpenIntToDoubleHashMap(int expectedSize, double missingEntries2) {
        int capacity = computeCapacity(expectedSize);
        this.keys = new int[capacity];
        this.values = new double[capacity];
        this.states = new byte[capacity];
        this.missingEntries = missingEntries2;
        this.mask = capacity - 1;
    }

    public OpenIntToDoubleHashMap(OpenIntToDoubleHashMap source) {
        int length = source.keys.length;
        this.keys = new int[length];
        System.arraycopy(source.keys, 0, this.keys, 0, length);
        this.values = new double[length];
        System.arraycopy(source.values, 0, this.values, 0, length);
        this.states = new byte[length];
        System.arraycopy(source.states, 0, this.states, 0, length);
        this.missingEntries = source.missingEntries;
        this.size = source.size;
        this.mask = source.mask;
        this.count = source.count;
    }

    private static int computeCapacity(int expectedSize) {
        if (expectedSize == 0) {
            return 1;
        }
        int capacity = (int) FastMath.ceil((double) (((float) expectedSize) / LOAD_FACTOR));
        if (Integer.highestOneBit(capacity) == capacity) {
            return capacity;
        }
        return nextPowerOfTwo(capacity);
    }

    private static int nextPowerOfTwo(int i) {
        return Integer.highestOneBit(i) << 1;
    }

    public double get(int key) {
        int hash = hashOf(key);
        int index = this.mask & hash;
        if (containsKey(key, index)) {
            return this.values[index];
        }
        if (this.states[index] == 0) {
            return this.missingEntries;
        }
        int j = index;
        int perturb = perturb(hash);
        while (this.states[index] != 0) {
            j = probe(perturb, j);
            index = j & this.mask;
            if (containsKey(key, index)) {
                return this.values[index];
            }
            perturb >>= 5;
        }
        return this.missingEntries;
    }

    public boolean containsKey(int key) {
        int hash = hashOf(key);
        int index = this.mask & hash;
        if (containsKey(key, index)) {
            return true;
        }
        if (this.states[index] == 0) {
            return false;
        }
        int j = index;
        int perturb = perturb(hash);
        while (this.states[index] != 0) {
            j = probe(perturb, j);
            index = j & this.mask;
            if (containsKey(key, index)) {
                return true;
            }
            perturb >>= 5;
        }
        return false;
    }

    public Iterator iterator() {
        return new Iterator();
    }

    private static int perturb(int hash) {
        return Integer.MAX_VALUE & hash;
    }

    private int findInsertionIndex(int key) {
        return findInsertionIndex(this.keys, this.states, key, this.mask);
    }

    private static int findInsertionIndex(int[] keys2, byte[] states2, int key, int mask2) {
        int hash = hashOf(key);
        int index = hash & mask2;
        if (states2[index] == 0) {
            return index;
        }
        if (states2[index] == 1 && keys2[index] == key) {
            return changeIndexSign(index);
        }
        int perturb = perturb(hash);
        int j = index;
        if (states2[index] == 1) {
            do {
                j = probe(perturb, j);
                index = j & mask2;
                perturb >>= 5;
                if (states2[index] != 1) {
                    break;
                }
            } while (keys2[index] != key);
        }
        if (states2[index] == 0) {
            return index;
        }
        if (states2[index] == 1) {
            return changeIndexSign(index);
        }
        int i = index;
        while (true) {
            j = probe(perturb, j);
            int index2 = j & mask2;
            if (states2[index2] == 0) {
                return index;
            }
            if (states2[index2] == 1 && keys2[index2] == key) {
                return changeIndexSign(index2);
            }
            perturb >>= 5;
        }
    }

    private static int probe(int perturb, int j) {
        return (j << 2) + j + perturb + 1;
    }

    private static int changeIndexSign(int index) {
        return (-index) - 1;
    }

    public int size() {
        return this.size;
    }

    public double remove(int key) {
        int hash = hashOf(key);
        int index = this.mask & hash;
        if (containsKey(key, index)) {
            return doRemove(index);
        }
        if (this.states[index] == 0) {
            return this.missingEntries;
        }
        int j = index;
        int perturb = perturb(hash);
        while (this.states[index] != 0) {
            j = probe(perturb, j);
            index = j & this.mask;
            if (containsKey(key, index)) {
                return doRemove(index);
            }
            perturb >>= 5;
        }
        return this.missingEntries;
    }

    private boolean containsKey(int key, int index) {
        return (key != 0 || this.states[index] == 1) && this.keys[index] == key;
    }

    private double doRemove(int index) {
        this.keys[index] = 0;
        this.states[index] = 2;
        double previous = this.values[index];
        this.values[index] = this.missingEntries;
        this.size--;
        this.count++;
        return previous;
    }

    public double put(int key, double value) {
        int index = findInsertionIndex(key);
        double previous = this.missingEntries;
        boolean newMapping = true;
        if (index < 0) {
            index = changeIndexSign(index);
            previous = this.values[index];
            newMapping = false;
        }
        this.keys[index] = key;
        this.states[index] = 1;
        this.values[index] = value;
        if (newMapping) {
            this.size++;
            if (shouldGrowTable()) {
                growTable();
            }
            this.count++;
        }
        return previous;
    }

    private void growTable() {
        int oldLength = this.states.length;
        int[] oldKeys = this.keys;
        double[] oldValues = this.values;
        byte[] oldStates = this.states;
        int newLength = oldLength * 2;
        int[] newKeys = new int[newLength];
        double[] newValues = new double[newLength];
        byte[] newStates = new byte[newLength];
        int newMask = newLength - 1;
        for (int i = 0; i < oldLength; i++) {
            if (oldStates[i] == 1) {
                int key = oldKeys[i];
                int index = findInsertionIndex(newKeys, newStates, key, newMask);
                newKeys[index] = key;
                newValues[index] = oldValues[i];
                newStates[index] = 1;
            }
        }
        this.mask = newMask;
        this.keys = newKeys;
        this.values = newValues;
        this.states = newStates;
    }

    private boolean shouldGrowTable() {
        return ((float) this.size) > ((float) (this.mask + 1)) * LOAD_FACTOR;
    }

    private static int hashOf(int key) {
        int h = ((key >>> 20) ^ (key >>> 12)) ^ key;
        return ((h >>> 7) ^ h) ^ (h >>> 4);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.count = 0;
    }
}

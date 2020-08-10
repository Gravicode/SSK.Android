package org.apache.poi.util;

public class ShortList {
    private static final int _default_size = 128;
    private short[] _array;
    private int _limit;

    public ShortList() {
        this(128);
    }

    public ShortList(ShortList list) {
        this(list._array.length);
        System.arraycopy(list._array, 0, this._array, 0, this._array.length);
        this._limit = list._limit;
    }

    public ShortList(int initialCapacity) {
        this._array = new short[initialCapacity];
        this._limit = 0;
    }

    public void add(int index, short value) {
        if (index > this._limit) {
            throw new IndexOutOfBoundsException();
        } else if (index == this._limit) {
            add(value);
        } else {
            if (this._limit == this._array.length) {
                growArray(this._limit * 2);
            }
            System.arraycopy(this._array, index, this._array, index + 1, this._limit - index);
            this._array[index] = value;
            this._limit++;
        }
    }

    public boolean add(short value) {
        if (this._limit == this._array.length) {
            growArray(this._limit * 2);
        }
        short[] sArr = this._array;
        int i = this._limit;
        this._limit = i + 1;
        sArr[i] = value;
        return true;
    }

    public boolean addAll(ShortList c) {
        if (c._limit != 0) {
            if (this._limit + c._limit > this._array.length) {
                growArray(this._limit + c._limit);
            }
            System.arraycopy(c._array, 0, this._array, this._limit, c._limit);
            this._limit += c._limit;
        }
        return true;
    }

    public boolean addAll(int index, ShortList c) {
        if (index > this._limit) {
            throw new IndexOutOfBoundsException();
        }
        if (c._limit != 0) {
            if (this._limit + c._limit > this._array.length) {
                growArray(this._limit + c._limit);
            }
            System.arraycopy(this._array, index, this._array, c._limit + index, this._limit - index);
            System.arraycopy(c._array, 0, this._array, index, c._limit);
            this._limit += c._limit;
        }
        return true;
    }

    public void clear() {
        this._limit = 0;
    }

    public boolean contains(short o) {
        boolean rval = false;
        int j = 0;
        while (!rval && j < this._limit) {
            if (this._array[j] == o) {
                rval = true;
            }
            j++;
        }
        return rval;
    }

    public boolean containsAll(ShortList c) {
        boolean rval = true;
        if (this != c) {
            int j = 0;
            while (rval && j < c._limit) {
                if (!contains(c._array[j])) {
                    rval = false;
                }
                j++;
            }
        }
        return rval;
    }

    public boolean equals(Object o) {
        boolean rval = this == o;
        if (rval || o == null || o.getClass() != getClass()) {
            return rval;
        }
        ShortList other = (ShortList) o;
        if (other._limit != this._limit) {
            return rval;
        }
        boolean rval2 = true;
        int j = 0;
        while (rval2 && j < this._limit) {
            rval2 = this._array[j] == other._array[j];
            j++;
        }
        return rval2;
    }

    public short get(int index) {
        if (index < this._limit) {
            return this._array[index];
        }
        throw new IndexOutOfBoundsException();
    }

    public int hashCode() {
        int hash = 0;
        for (int j = 0; j < this._limit; j++) {
            hash = (hash * 31) + this._array[j];
        }
        return hash;
    }

    public int indexOf(short o) {
        int rval = 0;
        while (rval < this._limit && o != this._array[rval]) {
            rval++;
        }
        if (rval == this._limit) {
            return -1;
        }
        return rval;
    }

    public boolean isEmpty() {
        return this._limit == 0;
    }

    public int lastIndexOf(short o) {
        int rval = this._limit - 1;
        while (rval >= 0 && o != this._array[rval]) {
            rval--;
        }
        return rval;
    }

    public short remove(int index) {
        if (index >= this._limit) {
            throw new IndexOutOfBoundsException();
        }
        short rval = this._array[index];
        System.arraycopy(this._array, index + 1, this._array, index, this._limit - index);
        this._limit--;
        return rval;
    }

    public boolean removeValue(short o) {
        boolean rval = false;
        int j = 0;
        while (!rval && j < this._limit) {
            if (o == this._array[j]) {
                System.arraycopy(this._array, j + 1, this._array, j, this._limit - j);
                this._limit--;
                rval = true;
            }
            j++;
        }
        return rval;
    }

    public boolean removeAll(ShortList c) {
        boolean rval = false;
        for (int j = 0; j < c._limit; j++) {
            if (removeValue(c._array[j])) {
                rval = true;
            }
        }
        return rval;
    }

    public boolean retainAll(ShortList c) {
        boolean rval = false;
        int j = 0;
        while (j < this._limit) {
            if (!c.contains(this._array[j])) {
                remove(j);
                rval = true;
            } else {
                j++;
            }
        }
        return rval;
    }

    public short set(int index, short element) {
        if (index >= this._limit) {
            throw new IndexOutOfBoundsException();
        }
        short rval = this._array[index];
        this._array[index] = element;
        return rval;
    }

    public int size() {
        return this._limit;
    }

    public short[] toArray() {
        short[] rval = new short[this._limit];
        System.arraycopy(this._array, 0, rval, 0, this._limit);
        return rval;
    }

    public short[] toArray(short[] a) {
        if (a.length != this._limit) {
            return toArray();
        }
        System.arraycopy(this._array, 0, a, 0, this._limit);
        return a;
    }

    private void growArray(int new_size) {
        short[] new_array = new short[(new_size == this._array.length ? new_size + 1 : new_size)];
        System.arraycopy(this._array, 0, new_array, 0, this._limit);
        this._array = new_array;
    }
}

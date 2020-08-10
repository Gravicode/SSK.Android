package org.apache.poi.p009ss.formula;

/* renamed from: org.apache.poi.ss.formula.FormulaCellCacheEntrySet */
final class FormulaCellCacheEntrySet {
    private static final FormulaCellCacheEntry[] EMPTY_ARRAY = new FormulaCellCacheEntry[0];
    private FormulaCellCacheEntry[] _arr = EMPTY_ARRAY;
    private int _size;

    public FormulaCellCacheEntry[] toArray() {
        int nItems = this._size;
        if (nItems < 1) {
            return EMPTY_ARRAY;
        }
        FormulaCellCacheEntry[] result = new FormulaCellCacheEntry[nItems];
        int j = 0;
        for (FormulaCellCacheEntry cce : this._arr) {
            if (cce != null) {
                int j2 = j + 1;
                result[j] = cce;
                j = j2;
            }
        }
        if (j == nItems) {
            return result;
        }
        throw new IllegalStateException("size mismatch");
    }

    public void add(CellCacheEntry cce) {
        if (this._size * 3 >= this._arr.length * 2) {
            FormulaCellCacheEntry[] prevArr = this._arr;
            FormulaCellCacheEntry[] newArr = new FormulaCellCacheEntry[(((this._arr.length * 3) / 2) + 4)];
            for (int i = 0; i < prevArr.length; i++) {
                FormulaCellCacheEntry prevCce = this._arr[i];
                if (prevCce != null) {
                    addInternal(newArr, prevCce);
                }
            }
            this._arr = newArr;
        }
        if (addInternal(this._arr, cce)) {
            this._size++;
        }
    }

    private static boolean addInternal(CellCacheEntry[] arr, CellCacheEntry cce) {
        int startIx = cce.hashCode() % arr.length;
        for (int i = startIx; i < arr.length; i++) {
            CellCacheEntry item = arr[i];
            if (item == cce) {
                return false;
            }
            if (item == null) {
                arr[i] = cce;
                return true;
            }
        }
        for (int i2 = 0; i2 < startIx; i2++) {
            CellCacheEntry item2 = arr[i2];
            if (item2 == cce) {
                return false;
            }
            if (item2 == null) {
                arr[i2] = cce;
                return true;
            }
        }
        throw new IllegalStateException("No empty space found");
    }

    public boolean remove(CellCacheEntry cce) {
        FormulaCellCacheEntry[] arr = this._arr;
        if (this._size * 3 >= this._arr.length || this._arr.length <= 8) {
            int startIx = cce.hashCode() % arr.length;
            for (int i = startIx; i < arr.length; i++) {
                if (arr[i] == cce) {
                    arr[i] = null;
                    this._size--;
                    return true;
                }
            }
            for (int i2 = 0; i2 < startIx; i2++) {
                if (arr[i2] == cce) {
                    arr[i2] = null;
                    this._size--;
                    return true;
                }
            }
            return false;
        }
        boolean found = false;
        FormulaCellCacheEntry[] prevArr = this._arr;
        FormulaCellCacheEntry[] newArr = new FormulaCellCacheEntry[(this._arr.length / 2)];
        for (int i3 = 0; i3 < prevArr.length; i3++) {
            FormulaCellCacheEntry prevCce = this._arr[i3];
            if (prevCce != null) {
                if (prevCce == cce) {
                    found = true;
                    this._size--;
                } else {
                    addInternal(newArr, prevCce);
                }
            }
        }
        this._arr = newArr;
        return found;
    }
}

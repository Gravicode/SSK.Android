package org.apache.poi.p009ss.util;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.poi.p009ss.usermodel.Cell;
import org.apache.poi.p009ss.usermodel.CellRange;
import org.apache.poi.util.Internal;

@Internal
/* renamed from: org.apache.poi.ss.util.SSCellRange */
public final class SSCellRange<K extends Cell> implements CellRange<K> {
    private final int _firstColumn;
    private final int _firstRow;
    private final K[] _flattenedArray;
    private final int _height;
    private final int _width;

    /* renamed from: org.apache.poi.ss.util.SSCellRange$ArrayIterator */
    private static final class ArrayIterator<D> implements Iterator<D> {
        private final D[] _array;
        private int _index = 0;

        public ArrayIterator(D[] array) {
            this._array = array;
        }

        public boolean hasNext() {
            return this._index < this._array.length;
        }

        public D next() {
            if (this._index >= this._array.length) {
                throw new NoSuchElementException(String.valueOf(this._index));
            }
            D[] dArr = this._array;
            int i = this._index;
            this._index = i + 1;
            return dArr[i];
        }

        public void remove() {
            throw new UnsupportedOperationException("Cannot remove cells from this CellRange.");
        }
    }

    private SSCellRange(int firstRow, int firstColumn, int height, int width, K[] flattenedArray) {
        this._firstRow = firstRow;
        this._firstColumn = firstColumn;
        this._height = height;
        this._width = width;
        this._flattenedArray = flattenedArray;
    }

    public static <B extends Cell> SSCellRange<B> create(int firstRow, int firstColumn, int height, int width, List<B> flattenedList, Class<B> cellClass) {
        int nItems = flattenedList.size();
        if (height * width != nItems) {
            throw new IllegalArgumentException("Array size mismatch.");
        }
        B[] flattenedArray = (Cell[]) Array.newInstance(cellClass, nItems);
        flattenedList.toArray(flattenedArray);
        SSCellRange sSCellRange = new SSCellRange(firstRow, firstColumn, height, width, flattenedArray);
        return sSCellRange;
    }

    public int getHeight() {
        return this._height;
    }

    public int getWidth() {
        return this._width;
    }

    public int size() {
        return this._height * this._width;
    }

    public String getReferenceText() {
        return new CellRangeAddress(this._firstRow, (this._firstRow + this._height) - 1, this._firstColumn, (this._firstColumn + this._width) - 1).formatAsString();
    }

    public K getTopLeftCell() {
        return this._flattenedArray[0];
    }

    public K getCell(int relativeRowIndex, int relativeColumnIndex) {
        if (relativeRowIndex < 0 || relativeRowIndex >= this._height) {
            StringBuilder sb = new StringBuilder();
            sb.append("Specified row ");
            sb.append(relativeRowIndex);
            sb.append(" is outside the allowable range (0..");
            sb.append(this._height - 1);
            sb.append(").");
            throw new ArrayIndexOutOfBoundsException(sb.toString());
        } else if (relativeColumnIndex < 0 || relativeColumnIndex >= this._width) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Specified colummn ");
            sb2.append(relativeColumnIndex);
            sb2.append(" is outside the allowable range (0..");
            sb2.append(this._width - 1);
            sb2.append(").");
            throw new ArrayIndexOutOfBoundsException(sb2.toString());
        } else {
            return this._flattenedArray[(this._width * relativeRowIndex) + relativeColumnIndex];
        }
    }

    public K[] getFlattenedCells() {
        return (Cell[]) this._flattenedArray.clone();
    }

    public K[][] getCells() {
        Class<?> itemCls = this._flattenedArray.getClass();
        K[][] result = (Cell[][]) Array.newInstance(itemCls, this._height);
        Class<?> itemCls2 = itemCls.getComponentType();
        for (int r = this._height - 1; r >= 0; r--) {
            int flatIndex = this._width * r;
            System.arraycopy(this._flattenedArray, flatIndex, (Cell[]) Array.newInstance(itemCls2, this._width), 0, this._width);
        }
        return result;
    }

    public Iterator<K> iterator() {
        return new ArrayIterator(this._flattenedArray);
    }
}

package org.apache.commons.math3.linear;

import java.io.Serializable;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.OpenIntToFieldHashMap;
import org.apache.commons.math3.util.OpenIntToFieldHashMap.Iterator;

public class SparseFieldVector<T extends FieldElement<T>> implements FieldVector<T>, Serializable {
    private static final long serialVersionUID = 7841233292190413362L;
    private final OpenIntToFieldHashMap<T> entries;
    private final Field<T> field;
    private final int virtualSize;

    public SparseFieldVector(Field<T> field2) {
        this(field2, 0);
    }

    public SparseFieldVector(Field<T> field2, int dimension) {
        this.field = field2;
        this.virtualSize = dimension;
        this.entries = new OpenIntToFieldHashMap<>(field2);
    }

    protected SparseFieldVector(SparseFieldVector<T> v, int resize) {
        this.field = v.field;
        this.virtualSize = v.getDimension() + resize;
        this.entries = new OpenIntToFieldHashMap<>(v.entries);
    }

    public SparseFieldVector(Field<T> field2, int dimension, int expectedSize) {
        this.field = field2;
        this.virtualSize = dimension;
        this.entries = new OpenIntToFieldHashMap<>(field2, expectedSize);
    }

    public SparseFieldVector(Field<T> field2, T[] values) throws NullArgumentException {
        MathUtils.checkNotNull(values);
        this.field = field2;
        this.virtualSize = values.length;
        this.entries = new OpenIntToFieldHashMap<>(field2);
        for (int key = 0; key < values.length; key++) {
            this.entries.put(key, values[key]);
        }
    }

    public SparseFieldVector(SparseFieldVector<T> v) {
        this.field = v.field;
        this.virtualSize = v.getDimension();
        this.entries = new OpenIntToFieldHashMap<>(v.getEntries());
    }

    private OpenIntToFieldHashMap<T> getEntries() {
        return this.entries;
    }

    public FieldVector<T> add(SparseFieldVector<T> v) throws DimensionMismatchException {
        checkVectorDimensions(v.getDimension());
        SparseFieldVector<T> res = (SparseFieldVector) copy();
        Iterator iter = v.getEntries().iterator();
        while (iter.hasNext()) {
            iter.advance();
            int key = iter.key();
            T value = iter.value();
            if (this.entries.containsKey(key)) {
                res.setEntry(key, (FieldElement) this.entries.get(key).add(value));
            } else {
                res.setEntry(key, value);
            }
        }
        return res;
    }

    public FieldVector<T> append(SparseFieldVector<T> v) {
        SparseFieldVector<T> res = new SparseFieldVector<>(this, v.getDimension());
        Iterator iter = v.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            res.setEntry(iter.key() + this.virtualSize, iter.value());
        }
        return res;
    }

    public FieldVector<T> append(FieldVector<T> v) {
        if (v instanceof SparseFieldVector) {
            return append((SparseFieldVector) v);
        }
        int n = v.getDimension();
        FieldVector<T> res = new SparseFieldVector<>(this, n);
        for (int i = 0; i < n; i++) {
            res.setEntry(this.virtualSize + i, v.getEntry(i));
        }
        return res;
    }

    public FieldVector<T> append(T d) throws NullArgumentException {
        MathUtils.checkNotNull(d);
        FieldVector<T> res = new SparseFieldVector<>(this, 1);
        res.setEntry(this.virtualSize, d);
        return res;
    }

    public FieldVector<T> copy() {
        return new SparseFieldVector(this);
    }

    public T dotProduct(FieldVector<T> v) throws DimensionMismatchException {
        checkVectorDimensions(v.getDimension());
        T res = (FieldElement) this.field.getZero();
        Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            res = (FieldElement) res.add(v.getEntry(iter.key()).multiply(iter.value()));
        }
        return res;
    }

    public FieldVector<T> ebeDivide(FieldVector<T> v) throws DimensionMismatchException, MathArithmeticException {
        checkVectorDimensions(v.getDimension());
        SparseFieldVector<T> res = new SparseFieldVector<>(this);
        Iterator iter = res.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            res.setEntry(iter.key(), (FieldElement) iter.value().divide(v.getEntry(iter.key())));
        }
        return res;
    }

    public FieldVector<T> ebeMultiply(FieldVector<T> v) throws DimensionMismatchException {
        checkVectorDimensions(v.getDimension());
        SparseFieldVector<T> res = new SparseFieldVector<>(this);
        Iterator iter = res.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            res.setEntry(iter.key(), (FieldElement) iter.value().multiply(v.getEntry(iter.key())));
        }
        return res;
    }

    @Deprecated
    public T[] getData() {
        return toArray();
    }

    public int getDimension() {
        return this.virtualSize;
    }

    public T getEntry(int index) throws OutOfRangeException {
        checkIndex(index);
        return this.entries.get(index);
    }

    public Field<T> getField() {
        return this.field;
    }

    public FieldVector<T> getSubVector(int index, int n) throws OutOfRangeException, NotPositiveException {
        if (n < 0) {
            throw new NotPositiveException(LocalizedFormats.NUMBER_OF_ELEMENTS_SHOULD_BE_POSITIVE, Integer.valueOf(n));
        }
        checkIndex(index);
        checkIndex((index + n) - 1);
        SparseFieldVector<T> res = new SparseFieldVector<>(this.field, n);
        int end = index + n;
        Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            int key = iter.key();
            if (key >= index && key < end) {
                res.setEntry(key - index, iter.value());
            }
        }
        return res;
    }

    public FieldVector<T> mapAdd(T d) throws NullArgumentException {
        return copy().mapAddToSelf(d);
    }

    public FieldVector<T> mapAddToSelf(T d) throws NullArgumentException {
        for (int i = 0; i < this.virtualSize; i++) {
            setEntry(i, (FieldElement) getEntry(i).add(d));
        }
        return this;
    }

    public FieldVector<T> mapDivide(T d) throws NullArgumentException, MathArithmeticException {
        return copy().mapDivideToSelf(d);
    }

    public FieldVector<T> mapDivideToSelf(T d) throws NullArgumentException, MathArithmeticException {
        Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            this.entries.put(iter.key(), (FieldElement) iter.value().divide(d));
        }
        return this;
    }

    public FieldVector<T> mapInv() throws MathArithmeticException {
        return copy().mapInvToSelf();
    }

    public FieldVector<T> mapInvToSelf() throws MathArithmeticException {
        for (int i = 0; i < this.virtualSize; i++) {
            setEntry(i, (FieldElement) ((FieldElement) this.field.getOne()).divide(getEntry(i)));
        }
        return this;
    }

    public FieldVector<T> mapMultiply(T d) throws NullArgumentException {
        return copy().mapMultiplyToSelf(d);
    }

    public FieldVector<T> mapMultiplyToSelf(T d) throws NullArgumentException {
        Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            this.entries.put(iter.key(), (FieldElement) iter.value().multiply(d));
        }
        return this;
    }

    public FieldVector<T> mapSubtract(T d) throws NullArgumentException {
        return copy().mapSubtractToSelf(d);
    }

    public FieldVector<T> mapSubtractToSelf(T d) throws NullArgumentException {
        return mapAddToSelf((FieldElement) ((FieldElement) this.field.getZero()).subtract(d));
    }

    public FieldMatrix<T> outerProduct(SparseFieldVector<T> v) {
        SparseFieldMatrix<T> res = new SparseFieldMatrix<>(this.field, this.virtualSize, v.getDimension());
        Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            Iterator iter2 = v.entries.iterator();
            while (iter2.hasNext()) {
                iter2.advance();
                res.setEntry(iter.key(), iter2.key(), (FieldElement) iter.value().multiply(iter2.value()));
            }
        }
        return res;
    }

    public FieldMatrix<T> outerProduct(FieldVector<T> v) {
        if (v instanceof SparseFieldVector) {
            return outerProduct((SparseFieldVector) v);
        }
        int n = v.getDimension();
        FieldMatrix<T> res = new SparseFieldMatrix<>(this.field, this.virtualSize, n);
        Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            int row = iter.key();
            FieldElement<T> value = iter.value();
            for (int col = 0; col < n; col++) {
                res.setEntry(row, col, (FieldElement) value.multiply(v.getEntry(col)));
            }
        }
        return res;
    }

    public FieldVector<T> projection(FieldVector<T> v) throws DimensionMismatchException, MathArithmeticException {
        checkVectorDimensions(v.getDimension());
        return v.mapMultiply((FieldElement) dotProduct(v).divide(v.dotProduct(v)));
    }

    public void set(T value) {
        MathUtils.checkNotNull(value);
        for (int i = 0; i < this.virtualSize; i++) {
            setEntry(i, value);
        }
    }

    public void setEntry(int index, T value) throws NullArgumentException, OutOfRangeException {
        MathUtils.checkNotNull(value);
        checkIndex(index);
        this.entries.put(index, value);
    }

    public void setSubVector(int index, FieldVector<T> v) throws OutOfRangeException {
        checkIndex(index);
        checkIndex((v.getDimension() + index) - 1);
        int n = v.getDimension();
        for (int i = 0; i < n; i++) {
            setEntry(i + index, v.getEntry(i));
        }
    }

    public SparseFieldVector<T> subtract(SparseFieldVector<T> v) throws DimensionMismatchException {
        checkVectorDimensions(v.getDimension());
        SparseFieldVector<T> res = (SparseFieldVector) copy();
        Iterator iter = v.getEntries().iterator();
        while (iter.hasNext()) {
            iter.advance();
            int key = iter.key();
            if (this.entries.containsKey(key)) {
                res.setEntry(key, (FieldElement) this.entries.get(key).subtract(iter.value()));
            } else {
                res.setEntry(key, (FieldElement) ((FieldElement) this.field.getZero()).subtract(iter.value()));
            }
        }
        return res;
    }

    public FieldVector<T> subtract(FieldVector<T> v) throws DimensionMismatchException {
        if (v instanceof SparseFieldVector) {
            return subtract((SparseFieldVector) v);
        }
        int n = v.getDimension();
        checkVectorDimensions(n);
        SparseFieldVector<T> res = new SparseFieldVector<>(this);
        for (int i = 0; i < n; i++) {
            if (this.entries.containsKey(i)) {
                res.setEntry(i, (FieldElement) this.entries.get(i).subtract(v.getEntry(i)));
            } else {
                res.setEntry(i, (FieldElement) ((FieldElement) this.field.getZero()).subtract(v.getEntry(i)));
            }
        }
        return res;
    }

    public T[] toArray() {
        T[] res = (FieldElement[]) MathArrays.buildArray(this.field, this.virtualSize);
        Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            res[iter.key()] = iter.value();
        }
        return res;
    }

    private void checkIndex(int index) throws OutOfRangeException {
        if (index < 0 || index >= getDimension()) {
            throw new OutOfRangeException(Integer.valueOf(index), Integer.valueOf(0), Integer.valueOf(getDimension() - 1));
        }
    }

    private void checkIndices(int start, int end) throws NumberIsTooSmallException, OutOfRangeException {
        int dim = getDimension();
        if (start < 0 || start >= dim) {
            throw new OutOfRangeException(LocalizedFormats.INDEX, Integer.valueOf(start), Integer.valueOf(0), Integer.valueOf(dim - 1));
        } else if (end < 0 || end >= dim) {
            throw new OutOfRangeException(LocalizedFormats.INDEX, Integer.valueOf(end), Integer.valueOf(0), Integer.valueOf(dim - 1));
        } else if (end < start) {
            throw new NumberIsTooSmallException(LocalizedFormats.INITIAL_ROW_AFTER_FINAL_ROW, Integer.valueOf(end), Integer.valueOf(start), false);
        }
    }

    /* access modifiers changed from: protected */
    public void checkVectorDimensions(int n) throws DimensionMismatchException {
        if (getDimension() != n) {
            throw new DimensionMismatchException(getDimension(), n);
        }
    }

    public FieldVector<T> add(FieldVector<T> v) throws DimensionMismatchException {
        if (v instanceof SparseFieldVector) {
            return add((SparseFieldVector) v);
        }
        int n = v.getDimension();
        checkVectorDimensions(n);
        SparseFieldVector<T> res = new SparseFieldVector<>(this.field, getDimension());
        for (int i = 0; i < n; i++) {
            res.setEntry(i, (FieldElement) v.getEntry(i).add(getEntry(i)));
        }
        return res;
    }

    public T walkInDefaultOrder(FieldVectorPreservingVisitor<T> visitor) {
        int dim = getDimension();
        int i = 0;
        visitor.start(dim, 0, dim - 1);
        while (true) {
            int i2 = i;
            if (i2 >= dim) {
                return visitor.end();
            }
            visitor.visit(i2, getEntry(i2));
            i = i2 + 1;
        }
    }

    public T walkInDefaultOrder(FieldVectorPreservingVisitor<T> visitor, int start, int end) throws NumberIsTooSmallException, OutOfRangeException {
        checkIndices(start, end);
        visitor.start(getDimension(), start, end);
        for (int i = start; i <= end; i++) {
            visitor.visit(i, getEntry(i));
        }
        return visitor.end();
    }

    public T walkInOptimizedOrder(FieldVectorPreservingVisitor<T> visitor) {
        return walkInDefaultOrder(visitor);
    }

    public T walkInOptimizedOrder(FieldVectorPreservingVisitor<T> visitor, int start, int end) throws NumberIsTooSmallException, OutOfRangeException {
        return walkInDefaultOrder(visitor, start, end);
    }

    public T walkInDefaultOrder(FieldVectorChangingVisitor<T> visitor) {
        int dim = getDimension();
        int i = 0;
        visitor.start(dim, 0, dim - 1);
        while (true) {
            int i2 = i;
            if (i2 >= dim) {
                return visitor.end();
            }
            setEntry(i2, visitor.visit(i2, getEntry(i2)));
            i = i2 + 1;
        }
    }

    public T walkInDefaultOrder(FieldVectorChangingVisitor<T> visitor, int start, int end) throws NumberIsTooSmallException, OutOfRangeException {
        checkIndices(start, end);
        visitor.start(getDimension(), start, end);
        for (int i = start; i <= end; i++) {
            setEntry(i, visitor.visit(i, getEntry(i)));
        }
        return visitor.end();
    }

    public T walkInOptimizedOrder(FieldVectorChangingVisitor<T> visitor) {
        return walkInDefaultOrder(visitor);
    }

    public T walkInOptimizedOrder(FieldVectorChangingVisitor<T> visitor, int start, int end) throws NumberIsTooSmallException, OutOfRangeException {
        return walkInDefaultOrder(visitor, start, end);
    }

    public int hashCode() {
        int result = (((1 * 31) + (this.field == null ? 0 : this.field.hashCode())) * 31) + this.virtualSize;
        Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            result = (result * 31) + iter.value().hashCode();
        }
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SparseFieldVector)) {
            return false;
        }
        SparseFieldVector<T> other = (SparseFieldVector) obj;
        if (this.field == null) {
            if (other.field != null) {
                return false;
            }
        } else if (!this.field.equals(other.field)) {
            return false;
        }
        if (this.virtualSize != other.virtualSize) {
            return false;
        }
        Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            if (!other.getEntry(iter.key()).equals(iter.value())) {
                return false;
            }
        }
        Iterator iter2 = other.getEntries().iterator();
        while (iter2.hasNext()) {
            iter2.advance();
            if (!iter2.value().equals(getEntry(iter2.key()))) {
                return false;
            }
        }
        return true;
    }
}

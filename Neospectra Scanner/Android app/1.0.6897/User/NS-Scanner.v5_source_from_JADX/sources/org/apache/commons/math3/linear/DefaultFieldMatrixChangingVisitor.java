package org.apache.commons.math3.linear;

import org.apache.commons.math3.FieldElement;

public class DefaultFieldMatrixChangingVisitor<T extends FieldElement<T>> implements FieldMatrixChangingVisitor<T> {
    private final T zero;

    public DefaultFieldMatrixChangingVisitor(T zero2) {
        this.zero = zero2;
    }

    public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {
    }

    public T visit(int row, int column, T value) {
        return value;
    }

    public T end() {
        return this.zero;
    }
}

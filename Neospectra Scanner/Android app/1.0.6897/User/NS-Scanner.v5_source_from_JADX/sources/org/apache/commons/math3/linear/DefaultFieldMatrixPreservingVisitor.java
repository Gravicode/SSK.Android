package org.apache.commons.math3.linear;

import org.apache.commons.math3.FieldElement;

public class DefaultFieldMatrixPreservingVisitor<T extends FieldElement<T>> implements FieldMatrixPreservingVisitor<T> {
    private final T zero;

    public DefaultFieldMatrixPreservingVisitor(T zero2) {
        this.zero = zero2;
    }

    public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {
    }

    public void visit(int row, int column, T t) {
    }

    public T end() {
        return this.zero;
    }
}

package org.apache.poi.p009ss.usermodel;

import java.util.Iterator;
import org.apache.poi.p009ss.usermodel.Cell;

/* renamed from: org.apache.poi.ss.usermodel.CellRange */
public interface CellRange<C extends Cell> extends Iterable<C> {
    C getCell(int i, int i2);

    C[][] getCells();

    C[] getFlattenedCells();

    int getHeight();

    String getReferenceText();

    C getTopLeftCell();

    int getWidth();

    Iterator<C> iterator();

    int size();
}

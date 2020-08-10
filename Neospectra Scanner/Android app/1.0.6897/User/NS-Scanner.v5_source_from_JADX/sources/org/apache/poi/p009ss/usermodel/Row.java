package org.apache.poi.p009ss.usermodel;

import java.util.Iterator;

/* renamed from: org.apache.poi.ss.usermodel.Row */
public interface Row extends Iterable<Cell> {
    public static final MissingCellPolicy CREATE_NULL_AS_BLANK = new MissingCellPolicy();
    public static final MissingCellPolicy RETURN_BLANK_AS_NULL = new MissingCellPolicy();
    public static final MissingCellPolicy RETURN_NULL_AND_BLANK = new MissingCellPolicy();

    /* renamed from: org.apache.poi.ss.usermodel.Row$MissingCellPolicy */
    public static final class MissingCellPolicy {
        private static int NEXT_ID = 1;

        /* renamed from: id */
        public final int f889id;

        private MissingCellPolicy() {
            int i = NEXT_ID;
            NEXT_ID = i + 1;
            this.f889id = i;
        }
    }

    Iterator<Cell> cellIterator();

    Cell createCell(int i);

    Cell createCell(int i, int i2);

    Cell getCell(int i);

    Cell getCell(int i, MissingCellPolicy missingCellPolicy);

    short getFirstCellNum();

    short getHeight();

    float getHeightInPoints();

    short getLastCellNum();

    int getPhysicalNumberOfCells();

    int getRowNum();

    Sheet getSheet();

    boolean getZeroHeight();

    void removeCell(Cell cell);

    void setHeight(short s);

    void setHeightInPoints(float f);

    void setRowNum(int i);

    void setZeroHeight(boolean z);
}

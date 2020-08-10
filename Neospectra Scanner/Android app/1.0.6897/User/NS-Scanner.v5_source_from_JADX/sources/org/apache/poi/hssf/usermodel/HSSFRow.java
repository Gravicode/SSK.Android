package org.apache.poi.hssf.usermodel;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.apache.poi.hssf.record.CellValueRecordInterface;
import org.apache.poi.hssf.record.RowRecord;
import org.apache.poi.p009ss.SpreadsheetVersion;
import org.apache.poi.p009ss.usermodel.Cell;
import org.apache.poi.p009ss.usermodel.Font;
import org.apache.poi.p009ss.usermodel.Row;
import org.apache.poi.p009ss.usermodel.Row.MissingCellPolicy;

public final class HSSFRow implements Row {
    public static final int INITIAL_CAPACITY = 5;
    private HSSFWorkbook book;
    /* access modifiers changed from: private */
    public HSSFCell[] cells;
    private RowRecord row;
    private int rowNum;
    private HSSFSheet sheet;

    private class CellIterator implements Iterator<Cell> {
        int nextId = -1;
        int thisId = -1;

        public CellIterator() {
            findNext();
        }

        public boolean hasNext() {
            return this.nextId < HSSFRow.this.cells.length;
        }

        public Cell next() {
            if (!hasNext()) {
                throw new NoSuchElementException("At last element");
            }
            HSSFCell cell = HSSFRow.this.cells[this.nextId];
            this.thisId = this.nextId;
            findNext();
            return cell;
        }

        public void remove() {
            if (this.thisId == -1) {
                throw new IllegalStateException("remove() called before next()");
            }
            HSSFRow.this.cells[this.thisId] = null;
        }

        private void findNext() {
            int i = this.nextId;
            while (true) {
                i++;
                if (i >= HSSFRow.this.cells.length || HSSFRow.this.cells[i] != null) {
                    this.nextId = i;
                }
            }
            this.nextId = i;
        }
    }

    HSSFRow(HSSFWorkbook book2, HSSFSheet sheet2, int rowNum2) {
        this(book2, sheet2, new RowRecord(rowNum2));
    }

    HSSFRow(HSSFWorkbook book2, HSSFSheet sheet2, RowRecord record) {
        this.cells = new HSSFCell[5];
        this.book = book2;
        this.sheet = sheet2;
        this.row = record;
        setRowNum(record.getRowNumber());
        record.setEmpty();
    }

    public HSSFCell createCell(short columnIndex) {
        return createCell((int) columnIndex);
    }

    public HSSFCell createCell(short columnIndex, int type) {
        return createCell((int) columnIndex, type);
    }

    public HSSFCell createCell(int column) {
        return createCell(column, 3);
    }

    public HSSFCell createCell(int columnIndex, int type) {
        short shortCellNum = (short) columnIndex;
        if (columnIndex > 32767) {
            shortCellNum = (short) (65535 - columnIndex);
        }
        HSSFCell cell = new HSSFCell(this.book, this.sheet, getRowNum(), shortCellNum, type);
        addCell(cell);
        this.sheet.getSheet().addValueRecord(getRowNum(), cell.getCellValueRecord());
        return cell;
    }

    public void removeCell(Cell cell) {
        if (cell == null) {
            throw new IllegalArgumentException("cell must not be null");
        }
        removeCell((HSSFCell) cell, true);
    }

    private void removeCell(HSSFCell cell, boolean alsoRemoveRecords) {
        int column = cell.getColumnIndex();
        if (column < 0) {
            throw new RuntimeException("Negative cell indexes not allowed");
        } else if (column >= this.cells.length || cell != this.cells[column]) {
            throw new RuntimeException("Specified cell is not from this row");
        } else {
            if (cell.isPartOfArrayFormulaGroup()) {
                cell.notifyArrayFormulaChanging();
            }
            this.cells[column] = null;
            if (alsoRemoveRecords) {
                this.sheet.getSheet().removeValueRecord(getRowNum(), cell.getCellValueRecord());
            }
            if (cell.getColumnIndex() + 1 == this.row.getLastCol()) {
                this.row.setLastCol(calculateNewLastCellPlusOne(this.row.getLastCol()));
            }
            if (cell.getColumnIndex() == this.row.getFirstCol()) {
                this.row.setFirstCol(calculateNewFirstCell(this.row.getFirstCol()));
            }
        }
    }

    /* access modifiers changed from: protected */
    public void removeAllCells() {
        for (int i = 0; i < this.cells.length; i++) {
            if (this.cells[i] != null) {
                removeCell(this.cells[i], true);
            }
        }
        this.cells = new HSSFCell[5];
    }

    /* access modifiers changed from: 0000 */
    public HSSFCell createCellFromRecord(CellValueRecordInterface cell) {
        HSSFCell hcell = new HSSFCell(this.book, this.sheet, cell);
        addCell(hcell);
        int colIx = cell.getColumn();
        if (this.row.isEmpty()) {
            this.row.setFirstCol(colIx);
            this.row.setLastCol(colIx + 1);
        } else if (colIx < this.row.getFirstCol()) {
            this.row.setFirstCol(colIx);
        } else if (colIx > this.row.getLastCol()) {
            this.row.setLastCol(colIx + 1);
        }
        return hcell;
    }

    public void setRowNum(int rowIndex) {
        int maxrow = SpreadsheetVersion.EXCEL97.getLastRowIndex();
        if (rowIndex < 0 || rowIndex > maxrow) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid row number (");
            sb.append(rowIndex);
            sb.append(") outside allowable range (0..");
            sb.append(maxrow);
            sb.append(")");
            throw new IllegalArgumentException(sb.toString());
        }
        this.rowNum = rowIndex;
        if (this.row != null) {
            this.row.setRowNumber(rowIndex);
        }
    }

    public int getRowNum() {
        return this.rowNum;
    }

    public HSSFSheet getSheet() {
        return this.sheet;
    }

    /* access modifiers changed from: protected */
    public int getOutlineLevel() {
        return this.row.getOutlineLevel();
    }

    public void moveCell(HSSFCell cell, short newColumn) {
        if (this.cells.length > newColumn && this.cells[newColumn] != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Asked to move cell to column ");
            sb.append(newColumn);
            sb.append(" but there's already a cell there");
            throw new IllegalArgumentException(sb.toString());
        } else if (!this.cells[cell.getColumnIndex()].equals(cell)) {
            throw new IllegalArgumentException("Asked to move a cell, but it didn't belong to our row");
        } else {
            removeCell(cell, false);
            cell.updateCellNum(newColumn);
            addCell(cell);
        }
    }

    private void addCell(HSSFCell cell) {
        int column = cell.getColumnIndex();
        if (column >= this.cells.length) {
            HSSFCell[] oldCells = this.cells;
            int newSize = oldCells.length * 2;
            if (newSize < column + 1) {
                newSize = column + 1;
            }
            this.cells = new HSSFCell[newSize];
            System.arraycopy(oldCells, 0, this.cells, 0, oldCells.length);
        }
        this.cells[column] = cell;
        if (this.row.isEmpty() || column < this.row.getFirstCol()) {
            this.row.setFirstCol((short) column);
        }
        if (this.row.isEmpty() || column >= this.row.getLastCol()) {
            this.row.setLastCol((short) (column + 1));
        }
    }

    private HSSFCell retrieveCell(int cellIndex) {
        if (cellIndex < 0 || cellIndex >= this.cells.length) {
            return null;
        }
        return this.cells[cellIndex];
    }

    public HSSFCell getCell(short cellnum) {
        return getCell(65535 & cellnum);
    }

    public HSSFCell getCell(int cellnum) {
        return getCell(cellnum, this.book.getMissingCellPolicy());
    }

    public HSSFCell getCell(int cellnum, MissingCellPolicy policy) {
        HSSFCell cell = retrieveCell(cellnum);
        if (policy == RETURN_NULL_AND_BLANK) {
            return cell;
        }
        if (policy == RETURN_BLANK_AS_NULL) {
            if (cell != null && cell.getCellType() == 3) {
                return null;
            }
            return cell;
        } else if (policy != CREATE_NULL_AS_BLANK) {
            StringBuilder sb = new StringBuilder();
            sb.append("Illegal policy ");
            sb.append(policy);
            sb.append(" (");
            sb.append(policy.f889id);
            sb.append(")");
            throw new IllegalArgumentException(sb.toString());
        } else if (cell == null) {
            return createCell(cellnum, 3);
        } else {
            return cell;
        }
    }

    public short getFirstCellNum() {
        if (this.row.isEmpty()) {
            return -1;
        }
        return (short) this.row.getFirstCol();
    }

    public short getLastCellNum() {
        if (this.row.isEmpty()) {
            return -1;
        }
        return (short) this.row.getLastCol();
    }

    public int getPhysicalNumberOfCells() {
        int count = 0;
        for (HSSFCell hSSFCell : this.cells) {
            if (hSSFCell != null) {
                count++;
            }
        }
        return count;
    }

    public void setHeight(short height) {
        if (height == -1) {
            this.row.setHeight(-32513);
            return;
        }
        this.row.setBadFontHeight(true);
        this.row.setHeight(height);
    }

    public void setZeroHeight(boolean zHeight) {
        this.row.setZeroHeight(zHeight);
    }

    public boolean getZeroHeight() {
        return this.row.getZeroHeight();
    }

    public void setHeightInPoints(float height) {
        if (height == -1.0f) {
            this.row.setHeight(-32513);
            return;
        }
        this.row.setBadFontHeight(true);
        this.row.setHeight((short) ((int) (20.0f * height)));
    }

    public short getHeight() {
        short height = this.row.getHeight();
        if ((32768 & height) != 0) {
            return this.sheet.getSheet().getDefaultRowHeight();
        }
        return (short) (height & Font.COLOR_NORMAL);
    }

    public float getHeightInPoints() {
        return ((float) getHeight()) / 20.0f;
    }

    /* access modifiers changed from: protected */
    public RowRecord getRowRecord() {
        return this.row;
    }

    private int calculateNewLastCellPlusOne(int lastcell) {
        int cellIx = lastcell - 1;
        HSSFCell r = retrieveCell(cellIx);
        while (r == null) {
            if (cellIx < 0) {
                return 0;
            }
            cellIx--;
            r = retrieveCell(cellIx);
        }
        return cellIx + 1;
    }

    private int calculateNewFirstCell(int firstcell) {
        int cellIx = firstcell + 1;
        HSSFCell r = retrieveCell(cellIx);
        while (r == null) {
            if (cellIx <= this.cells.length) {
                return 0;
            }
            cellIx++;
            r = retrieveCell(cellIx);
        }
        return cellIx;
    }

    public boolean isFormatted() {
        return this.row.getFormatted();
    }

    public HSSFCellStyle getRowStyle() {
        if (!isFormatted()) {
            return null;
        }
        short styleIndex = this.row.getXFIndex();
        return new HSSFCellStyle(styleIndex, this.book.getWorkbook().getExFormatAt(styleIndex), this.book);
    }

    public void setRowStyle(HSSFCellStyle style) {
        this.row.setFormatted(true);
        this.row.setXFIndex(style.getIndex());
    }

    public Iterator<Cell> cellIterator() {
        return new CellIterator();
    }

    public Iterator iterator() {
        return cellIterator();
    }

    public int compareTo(Object obj) {
        HSSFRow loc = (HSSFRow) obj;
        if (getRowNum() == loc.getRowNum()) {
            return 0;
        }
        if (getRowNum() >= loc.getRowNum() && getRowNum() > loc.getRowNum()) {
            return 1;
        }
        return -1;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof HSSFRow)) {
            return false;
        }
        if (getRowNum() == ((HSSFRow) obj).getRowNum()) {
            return true;
        }
        return false;
    }
}

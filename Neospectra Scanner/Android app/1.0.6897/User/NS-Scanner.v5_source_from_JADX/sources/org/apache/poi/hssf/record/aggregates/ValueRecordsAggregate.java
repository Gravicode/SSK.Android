package org.apache.poi.hssf.record.aggregates;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.model.RecordStream;
import org.apache.poi.hssf.record.BlankRecord;
import org.apache.poi.hssf.record.CellValueRecordInterface;
import org.apache.poi.hssf.record.FormulaRecord;
import org.apache.poi.hssf.record.MulBlankRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordBase;
import org.apache.poi.hssf.record.StringRecord;
import org.apache.poi.hssf.record.aggregates.RecordAggregate.RecordVisitor;
import org.apache.poi.hssf.record.formula.FormulaShifter;
import org.apache.poi.hssf.record.formula.Ptg;

public final class ValueRecordsAggregate {
    private static final int INDEX_NOT_SET = -1;
    private static final int MAX_ROW_INDEX = 65535;
    private int firstcell;
    private int lastcell;
    private CellValueRecordInterface[][] records;

    public ValueRecordsAggregate() {
        this(-1, -1, new CellValueRecordInterface[30][]);
    }

    private ValueRecordsAggregate(int firstCellIx, int lastCellIx, CellValueRecordInterface[][] pRecords) {
        this.firstcell = -1;
        this.lastcell = -1;
        this.firstcell = firstCellIx;
        this.lastcell = lastCellIx;
        this.records = pRecords;
    }

    public void insertCell(CellValueRecordInterface cell) {
        short column = cell.getColumn();
        int row = cell.getRow();
        if (row >= this.records.length) {
            CellValueRecordInterface[][] oldRecords = this.records;
            int newSize = oldRecords.length * 2;
            if (newSize < row + 1) {
                newSize = row + 1;
            }
            this.records = new CellValueRecordInterface[newSize][];
            System.arraycopy(oldRecords, 0, this.records, 0, oldRecords.length);
        }
        CellValueRecordInterface[] rowCells = this.records[row];
        if (rowCells == null) {
            int newSize2 = column + 1;
            if (newSize2 < 10) {
                newSize2 = 10;
            }
            rowCells = new CellValueRecordInterface[newSize2];
            this.records[row] = rowCells;
        }
        if (column >= rowCells.length) {
            CellValueRecordInterface[] oldRowCells = rowCells;
            int newSize3 = oldRowCells.length * 2;
            if (newSize3 < column + 1) {
                newSize3 = column + 1;
            }
            rowCells = new CellValueRecordInterface[newSize3];
            System.arraycopy(oldRowCells, 0, rowCells, 0, oldRowCells.length);
            this.records[row] = rowCells;
        }
        rowCells[column] = cell;
        if (column < this.firstcell || this.firstcell == -1) {
            this.firstcell = column;
        }
        if (column > this.lastcell || this.lastcell == -1) {
            this.lastcell = column;
        }
    }

    public void removeCell(CellValueRecordInterface cell) {
        if (cell == null) {
            throw new IllegalArgumentException("cell must not be null");
        }
        int row = cell.getRow();
        if (row >= this.records.length) {
            throw new RuntimeException("cell row is out of range");
        }
        CellValueRecordInterface[] rowCells = this.records[row];
        if (rowCells == null) {
            throw new RuntimeException("cell row is already empty");
        }
        short column = cell.getColumn();
        if (column >= rowCells.length) {
            throw new RuntimeException("cell column is out of range");
        }
        rowCells[column] = null;
    }

    public void removeAllCellsValuesForRow(int rowIndex) {
        if (rowIndex < 0 || rowIndex > 65535) {
            StringBuilder sb = new StringBuilder();
            sb.append("Specified rowIndex ");
            sb.append(rowIndex);
            sb.append(" is outside the allowable range (0..");
            sb.append(65535);
            sb.append(")");
            throw new IllegalArgumentException(sb.toString());
        } else if (rowIndex < this.records.length) {
            this.records[rowIndex] = null;
        }
    }

    public int getPhysicalNumberOfCells() {
        int c = 0;
        for (CellValueRecordInterface[] rowCells : this.records) {
            if (rowCells != null) {
                int count = c;
                for (CellValueRecordInterface cellValueRecordInterface : rowCells) {
                    if (cellValueRecordInterface != null) {
                        count++;
                    }
                }
                c = count;
            }
        }
        return c;
    }

    public int getFirstCellNum() {
        return this.firstcell;
    }

    public int getLastCellNum() {
        return this.lastcell;
    }

    public void addMultipleBlanks(MulBlankRecord mbr) {
        for (int j = 0; j < mbr.getNumColumns(); j++) {
            BlankRecord br = new BlankRecord();
            br.setColumn((short) (mbr.getFirstColumn() + j));
            br.setRow(mbr.getRow());
            br.setXFIndex(mbr.getXFAt(j));
            insertCell(br);
        }
    }

    public void construct(CellValueRecordInterface rec, RecordStream rs, SharedValueManager sfh) {
        StringRecord cachedText;
        if (rec instanceof FormulaRecord) {
            FormulaRecord formulaRec = (FormulaRecord) rec;
            if (rs.peekNextClass() == StringRecord.class) {
                cachedText = (StringRecord) rs.getNext();
            } else {
                cachedText = null;
            }
            insertCell(new FormulaRecordAggregate(formulaRec, cachedText, sfh));
            return;
        }
        insertCell(rec);
    }

    public int getRowCellBlockSize(int startRow, int endRow) {
        int result = 0;
        int rowIx = startRow;
        while (rowIx <= endRow && rowIx < this.records.length) {
            result += getRowSerializedSize(this.records[rowIx]);
            rowIx++;
        }
        return result;
    }

    public boolean rowHasCells(int row) {
        if (row >= this.records.length) {
            return false;
        }
        CellValueRecordInterface[] rowCells = this.records[row];
        if (rowCells == null) {
            return false;
        }
        for (CellValueRecordInterface cellValueRecordInterface : rowCells) {
            if (cellValueRecordInterface != null) {
                return true;
            }
        }
        return false;
    }

    private static int getRowSerializedSize(CellValueRecordInterface[] rowCells) {
        int i = 0;
        if (rowCells == null) {
            return 0;
        }
        int result = 0;
        while (i < rowCells.length) {
            RecordBase cvr = (RecordBase) rowCells[i];
            if (cvr != null) {
                int nBlank = countBlanks(rowCells, i);
                if (nBlank > 1) {
                    result += (nBlank * 2) + 10;
                    i += nBlank - 1;
                } else {
                    result += cvr.getRecordSize();
                }
            }
            i++;
        }
        return result;
    }

    public void visitCellsForRow(int rowIndex, RecordVisitor rv) {
        CellValueRecordInterface[] rowCells = this.records[rowIndex];
        if (rowCells == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Row [");
            sb.append(rowIndex);
            sb.append("] is empty");
            throw new IllegalArgumentException(sb.toString());
        }
        int i = 0;
        while (i < rowCells.length) {
            RecordBase cvr = (RecordBase) rowCells[i];
            if (cvr != null) {
                int nBlank = countBlanks(rowCells, i);
                if (nBlank > 1) {
                    rv.visitRecord(createMBR(rowCells, i, nBlank));
                    i += nBlank - 1;
                } else if (cvr instanceof RecordAggregate) {
                    ((RecordAggregate) cvr).visitContainedRecords(rv);
                } else {
                    rv.visitRecord((Record) cvr);
                }
            }
            i++;
        }
    }

    private static int countBlanks(CellValueRecordInterface[] rowCellValues, int startIx) {
        int i = startIx;
        while (i < rowCellValues.length && (rowCellValues[i] instanceof BlankRecord)) {
            i++;
        }
        return i - startIx;
    }

    private MulBlankRecord createMBR(CellValueRecordInterface[] cellValues, int startIx, int nBlank) {
        short[] xfs = new short[nBlank];
        for (int i = 0; i < xfs.length; i++) {
            xfs[i] = cellValues[startIx + i].getXFIndex();
        }
        return new MulBlankRecord(cellValues[startIx].getRow(), startIx, xfs);
    }

    public void updateFormulasAfterRowShift(FormulaShifter shifter, int currentExternSheetIndex) {
        for (CellValueRecordInterface[] rowCells : this.records) {
            if (rowCells != null) {
                for (CellValueRecordInterface cell : rowCells) {
                    if (cell instanceof FormulaRecordAggregate) {
                        FormulaRecord fr = ((FormulaRecordAggregate) cell).getFormulaRecord();
                        Ptg[] ptgs = fr.getParsedExpression();
                        if (shifter.adjustFormula(ptgs, currentExternSheetIndex)) {
                            fr.setParsedExpression(ptgs);
                        }
                    }
                }
            }
        }
    }

    public CellValueRecordInterface[] getValueRecords() {
        List<CellValueRecordInterface> temp = new ArrayList<>();
        for (CellValueRecordInterface[] rowCells : this.records) {
            if (rowCells != null) {
                for (CellValueRecordInterface cell : rowCells) {
                    if (cell != null) {
                        temp.add(cell);
                    }
                }
            }
        }
        CellValueRecordInterface[] result = new CellValueRecordInterface[temp.size()];
        temp.toArray(result);
        return result;
    }

    public Object clone() {
        throw new RuntimeException("clone() should not be called.  ValueRecordsAggregate should be copied via Sheet.cloneSheet()");
    }
}

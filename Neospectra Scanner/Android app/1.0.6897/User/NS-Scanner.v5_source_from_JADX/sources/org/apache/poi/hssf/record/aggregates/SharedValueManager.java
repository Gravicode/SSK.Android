package org.apache.poi.hssf.record.aggregates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.record.ArrayRecord;
import org.apache.poi.hssf.record.SharedFormulaRecord;
import org.apache.poi.hssf.record.SharedValueRecordBase;
import org.apache.poi.hssf.record.TableRecord;
import org.apache.poi.hssf.util.CellRangeAddress8Bit;
import org.apache.poi.p009ss.util.CellReference;

public final class SharedValueManager {
    private static final Comparator<SharedFormulaGroup> SVGComparator = new Comparator<SharedFormulaGroup>() {
        public int compare(SharedFormulaGroup a, SharedFormulaGroup b) {
            CellRangeAddress8Bit rangeA = a.getSFR().getRange();
            CellRangeAddress8Bit rangeB = b.getSFR().getRange();
            int cmp = rangeA.getFirstRow() - rangeB.getFirstRow();
            if (cmp != 0) {
                return cmp;
            }
            int cmp2 = rangeA.getFirstColumn() - rangeB.getFirstColumn();
            if (cmp2 != 0) {
                return cmp2;
            }
            return 0;
        }
    };
    private final List<ArrayRecord> _arrayRecords;
    private SharedFormulaGroup[] _groups;
    private final Map<SharedFormulaRecord, SharedFormulaGroup> _groupsBySharedFormulaRecord;
    private final TableRecord[] _tableRecords;

    private static final class SharedFormulaGroup {
        private final CellReference _firstCell;
        private final FormulaRecordAggregate[] _frAggs;
        private int _numberOfFormulas;
        private final SharedFormulaRecord _sfr;

        public SharedFormulaGroup(SharedFormulaRecord sfr, CellReference firstCell) {
            if (!sfr.isInRange(firstCell.getRow(), firstCell.getCol())) {
                StringBuilder sb = new StringBuilder();
                sb.append("First formula cell ");
                sb.append(firstCell.formatAsString());
                sb.append(" is not shared formula range ");
                sb.append(sfr.getRange().toString());
                sb.append(".");
                throw new IllegalArgumentException(sb.toString());
            }
            this._sfr = sfr;
            this._firstCell = firstCell;
            this._frAggs = new FormulaRecordAggregate[(((sfr.getLastColumn() - sfr.getFirstColumn()) + 1) * ((sfr.getLastRow() - sfr.getFirstRow()) + 1))];
            this._numberOfFormulas = 0;
        }

        public void add(FormulaRecordAggregate agg) {
            if (this._numberOfFormulas == 0 && (this._firstCell.getRow() != agg.getRow() || this._firstCell.getCol() != agg.getColumn())) {
                throw new IllegalStateException("shared formula coding error");
            } else if (this._numberOfFormulas >= this._frAggs.length) {
                throw new RuntimeException("Too many formula records for shared formula group");
            } else {
                FormulaRecordAggregate[] formulaRecordAggregateArr = this._frAggs;
                int i = this._numberOfFormulas;
                this._numberOfFormulas = i + 1;
                formulaRecordAggregateArr[i] = agg;
            }
        }

        public void unlinkSharedFormulas() {
            for (int i = 0; i < this._numberOfFormulas; i++) {
                this._frAggs[i].unlinkSharedFormula();
            }
        }

        public SharedFormulaRecord getSFR() {
            return this._sfr;
        }

        public final String toString() {
            StringBuffer sb = new StringBuffer(64);
            sb.append(getClass().getName());
            sb.append(" [");
            sb.append(this._sfr.getRange().toString());
            sb.append("]");
            return sb.toString();
        }

        public boolean isFirstCell(int row, int column) {
            return this._firstCell.getRow() == row && this._firstCell.getCol() == column;
        }
    }

    public static SharedValueManager createEmpty() {
        return new SharedValueManager(new SharedFormulaRecord[0], new CellReference[0], new ArrayRecord[0], new TableRecord[0]);
    }

    private SharedValueManager(SharedFormulaRecord[] sharedFormulaRecords, CellReference[] firstCells, ArrayRecord[] arrayRecords, TableRecord[] tableRecords) {
        int nShF = sharedFormulaRecords.length;
        if (nShF != firstCells.length) {
            StringBuilder sb = new StringBuilder();
            sb.append("array sizes don't match: ");
            sb.append(nShF);
            sb.append("!=");
            sb.append(firstCells.length);
            sb.append(".");
            throw new IllegalArgumentException(sb.toString());
        }
        this._arrayRecords = toList(arrayRecords);
        this._tableRecords = tableRecords;
        Map<SharedFormulaRecord, SharedFormulaGroup> m = new HashMap<>((nShF * 3) / 2);
        for (int i = 0; i < nShF; i++) {
            SharedFormulaRecord sfr = sharedFormulaRecords[i];
            m.put(sfr, new SharedFormulaGroup(sfr, firstCells[i]));
        }
        this._groupsBySharedFormulaRecord = m;
    }

    private static <Z> List<Z> toList(Z[] zz) {
        List<Z> result = new ArrayList<>(zz.length);
        for (Z add : zz) {
            result.add(add);
        }
        return result;
    }

    public static SharedValueManager create(SharedFormulaRecord[] sharedFormulaRecords, CellReference[] firstCells, ArrayRecord[] arrayRecords, TableRecord[] tableRecords) {
        if (sharedFormulaRecords.length + firstCells.length + arrayRecords.length + tableRecords.length < 1) {
            return createEmpty();
        }
        return new SharedValueManager(sharedFormulaRecords, firstCells, arrayRecords, tableRecords);
    }

    public SharedFormulaRecord linkSharedFormulaRecord(CellReference firstCell, FormulaRecordAggregate agg) {
        SharedFormulaGroup result = findFormulaGroup(getGroups(), firstCell);
        result.add(agg);
        return result.getSFR();
    }

    private static SharedFormulaGroup findFormulaGroup(SharedFormulaGroup[] groups, CellReference firstCell) {
        int row = firstCell.getRow();
        int column = firstCell.getCol();
        for (SharedFormulaGroup svg : groups) {
            if (svg.isFirstCell(row, column)) {
                return svg;
            }
        }
        throw new RuntimeException("Failed to find a matching shared formula record");
    }

    private SharedFormulaGroup[] getGroups() {
        if (this._groups == null) {
            SharedFormulaGroup[] groups = new SharedFormulaGroup[this._groupsBySharedFormulaRecord.size()];
            this._groupsBySharedFormulaRecord.values().toArray(groups);
            Arrays.sort(groups, SVGComparator);
            this._groups = groups;
        }
        return this._groups;
    }

    public SharedValueRecordBase getRecordForFirstCell(FormulaRecordAggregate agg) {
        TableRecord[] arr$;
        CellReference firstCell = agg.getFormulaRecord().getFormula().getExpReference();
        if (firstCell == null) {
            return null;
        }
        int row = firstCell.getRow();
        int column = firstCell.getCol();
        if (agg.getRow() != row || agg.getColumn() != column) {
            return null;
        }
        SharedFormulaGroup[] groups = getGroups();
        for (SharedFormulaGroup sfg : groups) {
            if (sfg.isFirstCell(row, column)) {
                return sfg.getSFR();
            }
        }
        for (TableRecord tr : this._tableRecords) {
            if (tr.isFirstCell(row, column)) {
                return tr;
            }
        }
        for (ArrayRecord ar : this._arrayRecords) {
            if (ar.isFirstCell(row, column)) {
                return ar;
            }
        }
        return null;
    }

    public void unlink(SharedFormulaRecord sharedFormulaRecord) {
        SharedFormulaGroup svg = (SharedFormulaGroup) this._groupsBySharedFormulaRecord.remove(sharedFormulaRecord);
        if (svg == null) {
            throw new IllegalStateException("Failed to find formulas for shared formula");
        }
        this._groups = null;
        svg.unlinkSharedFormulas();
    }

    public void addArrayRecord(ArrayRecord ar) {
        this._arrayRecords.add(ar);
    }

    public CellRangeAddress8Bit removeArrayFormula(int rowIndex, int columnIndex) {
        for (ArrayRecord ar : this._arrayRecords) {
            if (ar.isInRange(rowIndex, columnIndex)) {
                this._arrayRecords.remove(ar);
                return ar.getRange();
            }
        }
        String ref = new CellReference(rowIndex, columnIndex, false, false).formatAsString();
        StringBuilder sb = new StringBuilder();
        sb.append("Specified cell ");
        sb.append(ref);
        sb.append(" is not part of an array formula.");
        throw new IllegalArgumentException(sb.toString());
    }

    public ArrayRecord getArrayRecord(int firstRow, int firstColumn) {
        for (ArrayRecord ar : this._arrayRecords) {
            if (ar.isFirstCell(firstRow, firstColumn)) {
                return ar;
            }
        }
        return null;
    }
}

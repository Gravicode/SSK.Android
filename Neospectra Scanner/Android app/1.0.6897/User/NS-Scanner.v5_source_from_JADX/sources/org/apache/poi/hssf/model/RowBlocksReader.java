package org.apache.poi.hssf.model;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.record.ArrayRecord;
import org.apache.poi.hssf.record.FormulaRecord;
import org.apache.poi.hssf.record.MergeCellsRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.SharedFormulaRecord;
import org.apache.poi.hssf.record.TableRecord;
import org.apache.poi.hssf.record.aggregates.SharedValueManager;
import org.apache.poi.p009ss.util.CellReference;

public final class RowBlocksReader {
    private final MergeCellsRecord[] _mergedCellsRecords;
    private final List _plainRecords;
    private final SharedValueManager _sfm;

    public RowBlocksReader(RecordStream rs) {
        List<Record> dest;
        List<Record> arrayList = new ArrayList<>();
        List<Record> arrayList2 = new ArrayList<>();
        List<CellReference> firstCellRefs = new ArrayList<>();
        List<Record> arrayList3 = new ArrayList<>();
        List<Record> arrayList4 = new ArrayList<>();
        List<Record> arrayList5 = new ArrayList<>();
        Record prevRec = null;
        while (!RecordOrderer.isEndOfRowBlock(rs.peekNextSid())) {
            if (!rs.hasNext()) {
                throw new RuntimeException("Failed to find end of row/cell records");
            }
            Record rec = rs.getNext();
            short sid = rec.getSid();
            if (sid == 229) {
                dest = arrayList5;
            } else if (sid == 545) {
                dest = arrayList3;
            } else if (sid == 566) {
                dest = arrayList4;
            } else if (sid != 1212) {
                dest = arrayList;
            } else {
                dest = arrayList2;
                if (!(prevRec instanceof FormulaRecord)) {
                    throw new RuntimeException("Shared formula record should follow a FormulaRecord");
                }
                FormulaRecord fr = (FormulaRecord) prevRec;
                firstCellRefs.add(new CellReference(fr.getRow(), fr.getColumn()));
            }
            dest.add(rec);
            prevRec = rec;
        }
        SharedFormulaRecord[] sharedFormulaRecs = new SharedFormulaRecord[arrayList2.size()];
        CellReference[] firstCells = new CellReference[firstCellRefs.size()];
        ArrayRecord[] arrayRecs = new ArrayRecord[arrayList3.size()];
        TableRecord[] tableRecs = new TableRecord[arrayList4.size()];
        arrayList2.toArray(sharedFormulaRecs);
        firstCellRefs.toArray(firstCells);
        arrayList3.toArray(arrayRecs);
        arrayList4.toArray(tableRecs);
        this._plainRecords = arrayList;
        this._sfm = SharedValueManager.create(sharedFormulaRecs, firstCells, arrayRecs, tableRecs);
        this._mergedCellsRecords = new MergeCellsRecord[arrayList5.size()];
        arrayList5.toArray(this._mergedCellsRecords);
    }

    public MergeCellsRecord[] getLooseMergedCells() {
        return this._mergedCellsRecords;
    }

    public SharedValueManager getSharedFormulaManager() {
        return this._sfm;
    }

    public RecordStream getPlainRecordStream() {
        return new RecordStream(this._plainRecords, 0);
    }
}

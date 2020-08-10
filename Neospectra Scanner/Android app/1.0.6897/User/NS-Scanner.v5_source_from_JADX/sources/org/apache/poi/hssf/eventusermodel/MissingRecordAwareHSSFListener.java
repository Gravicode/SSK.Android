package org.apache.poi.hssf.eventusermodel;

import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingRowDummyRecord;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.CellValueRecordInterface;
import org.apache.poi.hssf.record.MulBlankRecord;
import org.apache.poi.hssf.record.MulRKRecord;
import org.apache.poi.hssf.record.NoteRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordFactory;
import org.apache.poi.hssf.record.RowRecord;

public final class MissingRecordAwareHSSFListener implements HSSFListener {
    private HSSFListener childListener;
    private int lastCellColumn;
    private int lastCellRow;
    private int lastRowRow;

    public MissingRecordAwareHSSFListener(HSSFListener listener) {
        resetCounts();
        this.childListener = listener;
    }

    public void processRecord(Record record) {
        int thisRow;
        int thisColumn;
        CellValueRecordInterface[] expandedRecords = 0;
        if (record instanceof CellValueRecordInterface) {
            CellValueRecordInterface valueRec = (CellValueRecordInterface) record;
            thisRow = valueRec.getRow();
            thisColumn = valueRec.getColumn();
        } else {
            thisRow = -1;
            thisColumn = -1;
            short sid = record.getSid();
            if (sid == 28) {
                NoteRecord nrec = (NoteRecord) record;
                thisRow = nrec.getRow();
                thisColumn = nrec.getColumn();
                expandedRecords = expandedRecords;
            } else if (sid == 520) {
                RowRecord rowrec = (RowRecord) record;
                if (this.lastRowRow + 1 < rowrec.getRowNumber()) {
                    int i = this.lastRowRow;
                    while (true) {
                        i++;
                        if (i >= rowrec.getRowNumber()) {
                            break;
                        }
                        this.childListener.processRecord(new MissingRowDummyRecord(i));
                    }
                }
                this.lastRowRow = rowrec.getRowNumber();
                expandedRecords = expandedRecords;
            } else if (sid == 1212) {
                this.childListener.processRecord(record);
                return;
            } else if (sid != 2057) {
                switch (sid) {
                    case 189:
                        expandedRecords = RecordFactory.convertRKRecords((MulRKRecord) record);
                        break;
                    case 190:
                        expandedRecords = RecordFactory.convertBlankRecords((MulBlankRecord) record);
                        break;
                }
            } else {
                BOFRecord bof = (BOFRecord) record;
                if (bof.getType() == 5 || bof.getType() == 16) {
                    resetCounts();
                    expandedRecords = expandedRecords;
                }
            }
        }
        if (expandedRecords != null && expandedRecords.length > 0) {
            thisRow = expandedRecords[0].getRow();
            thisColumn = expandedRecords[0].getColumn();
        }
        if (thisRow != this.lastCellRow && this.lastCellRow > -1) {
            for (int i2 = this.lastCellRow; i2 < thisRow; i2++) {
                int cols = -1;
                if (i2 == this.lastCellRow) {
                    cols = this.lastCellColumn;
                }
                this.childListener.processRecord(new LastCellOfRowDummyRecord(i2, cols));
            }
        }
        if (!(this.lastCellRow == -1 || this.lastCellColumn == -1 || thisRow != -1)) {
            this.childListener.processRecord(new LastCellOfRowDummyRecord(this.lastCellRow, this.lastCellColumn));
            this.lastCellRow = -1;
            this.lastCellColumn = -1;
        }
        if (thisRow != this.lastCellRow) {
            this.lastCellColumn = -1;
        }
        if (this.lastCellColumn != thisColumn - 1) {
            int i3 = this.lastCellColumn;
            while (true) {
                i3++;
                if (i3 < thisColumn) {
                    this.childListener.processRecord(new MissingCellDummyRecord(thisRow, i3));
                }
            }
        }
        if (expandedRecords != null && expandedRecords.length > 0) {
            thisColumn = expandedRecords[expandedRecords.length - 1].getColumn();
        }
        if (thisColumn != -1) {
            this.lastCellColumn = thisColumn;
            this.lastCellRow = thisRow;
        }
        if (expandedRecords == null || expandedRecords.length <= 0) {
            this.childListener.processRecord(record);
        } else {
            for (Record processRecord : expandedRecords) {
                this.childListener.processRecord(processRecord);
            }
        }
    }

    private void resetCounts() {
        this.lastRowRow = -1;
        this.lastCellRow = -1;
        this.lastCellColumn = -1;
    }
}

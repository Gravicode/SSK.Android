package org.apache.poi.hssf.record;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;

public final class RecordFactoryInputStream {
    private int _bofDepth;
    private DrawingRecord _lastDrawingRecord = new DrawingRecord();
    private Record _lastRecord = null;
    private boolean _lastRecordWasEOFLevelZero;
    private final RecordInputStream _recStream;
    private final boolean _shouldIncludeContinueRecords;
    private Record[] _unreadRecordBuffer;
    private int _unreadRecordIndex = -1;

    private static final class StreamEncryptionInfo {
        private final FilePassRecord _filePassRec;
        private final boolean _hasBOFRecord;
        private final int _initialRecordsSize;
        private final Record _lastRecord;

        public StreamEncryptionInfo(RecordInputStream rs, List<Record> outputRecs) {
            rs.nextRecord();
            int recSize = rs.remaining() + 4;
            Record rec = RecordFactory.createSingleRecord(rs);
            outputRecs.add(rec);
            FilePassRecord fpr = null;
            if (rec instanceof BOFRecord) {
                this._hasBOFRecord = true;
                if (rs.hasNextRecord()) {
                    rs.nextRecord();
                    rec = RecordFactory.createSingleRecord(rs);
                    recSize += rec.getRecordSize();
                    outputRecs.add(rec);
                    if (rec instanceof FilePassRecord) {
                        fpr = (FilePassRecord) rec;
                        outputRecs.remove(outputRecs.size() - 1);
                        rec = (Record) outputRecs.get(0);
                    } else if (rec instanceof EOFRecord) {
                        throw new IllegalStateException("Nothing between BOF and EOF");
                    }
                }
            } else {
                this._hasBOFRecord = false;
            }
            this._initialRecordsSize = recSize;
            this._filePassRec = fpr;
            this._lastRecord = rec;
        }

        public RecordInputStream createDecryptingStream(InputStream original) {
            Biff8EncryptionKey key;
            FilePassRecord fpr = this._filePassRec;
            String userPassword = Biff8EncryptionKey.getCurrentUserPassword();
            if (userPassword == null) {
                key = Biff8EncryptionKey.create(fpr.getDocId());
            } else {
                key = Biff8EncryptionKey.create(userPassword, fpr.getDocId());
            }
            if (key.validate(fpr.getSaltData(), fpr.getSaltHash())) {
                return new RecordInputStream(original, key, this._initialRecordsSize);
            }
            StringBuilder sb = new StringBuilder();
            sb.append(userPassword == null ? "Default" : "Supplied");
            sb.append(" password is invalid for docId/saltData/saltHash");
            throw new EncryptedDocumentException(sb.toString());
        }

        public boolean hasEncryption() {
            return this._filePassRec != null;
        }

        public Record getLastRecord() {
            return this._lastRecord;
        }

        public boolean hasBOFRecord() {
            return this._hasBOFRecord;
        }
    }

    public RecordFactoryInputStream(InputStream in, boolean shouldIncludeContinueRecords) {
        RecordInputStream rs = new RecordInputStream(in);
        List<Record> records = new ArrayList<>();
        StreamEncryptionInfo sei = new StreamEncryptionInfo(rs, records);
        if (sei.hasEncryption()) {
            rs = sei.createDecryptingStream(in);
        }
        if (!records.isEmpty()) {
            this._unreadRecordBuffer = new Record[records.size()];
            records.toArray(this._unreadRecordBuffer);
            this._unreadRecordIndex = 0;
        }
        this._recStream = rs;
        this._shouldIncludeContinueRecords = shouldIncludeContinueRecords;
        this._lastRecord = sei.getLastRecord();
        this._bofDepth = sei.hasBOFRecord() ? 1 : 0;
        this._lastRecordWasEOFLevelZero = false;
    }

    public Record nextRecord() {
        Record r = getNextUnreadRecord();
        if (r != null) {
            return r;
        }
        while (this._recStream.hasNextRecord()) {
            if (this._lastRecordWasEOFLevelZero && this._recStream.getNextSid() != 2057) {
                return null;
            }
            this._recStream.nextRecord();
            Record r2 = readNextRecord();
            if (r2 != null) {
                return r2;
            }
        }
        return null;
    }

    private Record getNextUnreadRecord() {
        if (this._unreadRecordBuffer != null) {
            int ix = this._unreadRecordIndex;
            if (ix < this._unreadRecordBuffer.length) {
                Record result = this._unreadRecordBuffer[ix];
                this._unreadRecordIndex = ix + 1;
                return result;
            }
            this._unreadRecordIndex = -1;
            this._unreadRecordBuffer = null;
        }
        return null;
    }

    private Record readNextRecord() {
        Record record = RecordFactory.createSingleRecord(this._recStream);
        this._lastRecordWasEOFLevelZero = false;
        if (record instanceof BOFRecord) {
            this._bofDepth++;
            return record;
        } else if (record instanceof EOFRecord) {
            this._bofDepth--;
            if (this._bofDepth < 1) {
                this._lastRecordWasEOFLevelZero = true;
            }
            return record;
        } else if (record instanceof DBCellRecord) {
            return null;
        } else {
            if (record instanceof RKRecord) {
                return RecordFactory.convertToNumberRecord((RKRecord) record);
            }
            if (record instanceof MulRKRecord) {
                Record[] records = RecordFactory.convertRKRecords((MulRKRecord) record);
                this._unreadRecordBuffer = records;
                this._unreadRecordIndex = 1;
                return records[0];
            } else if (record.getSid() == 235 && (this._lastRecord instanceof DrawingGroupRecord)) {
                ((DrawingGroupRecord) this._lastRecord).join((AbstractEscherHolderRecord) record);
                return null;
            } else if (record.getSid() == 60) {
                ContinueRecord contRec = (ContinueRecord) record;
                if ((this._lastRecord instanceof ObjRecord) || (this._lastRecord instanceof TextObjectRecord)) {
                    this._lastDrawingRecord.processContinueRecord(contRec.getData());
                    if (this._shouldIncludeContinueRecords) {
                        return record;
                    }
                    return null;
                } else if (this._lastRecord instanceof DrawingGroupRecord) {
                    ((DrawingGroupRecord) this._lastRecord).processContinueRecord(contRec.getData());
                    return null;
                } else if (this._lastRecord instanceof DrawingRecord) {
                    ((DrawingRecord) this._lastRecord).processContinueRecord(contRec.getData());
                    return null;
                } else if ((this._lastRecord instanceof UnknownRecord) || (this._lastRecord instanceof EOFRecord)) {
                    return record;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unhandled Continue Record followining ");
                    sb.append(this._lastRecord.getClass());
                    throw new RecordFormatException(sb.toString());
                }
            } else {
                this._lastRecord = record;
                if (record instanceof DrawingRecord) {
                    this._lastDrawingRecord = (DrawingRecord) record;
                }
                return record;
            }
        }
    }
}

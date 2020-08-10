package org.apache.poi.hssf.eventmodel;

import java.io.InputStream;
import java.util.Arrays;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordFactory;
import org.apache.poi.hssf.record.RecordFormatException;
import org.apache.poi.hssf.record.RecordInputStream;

public final class EventRecordFactory {
    private final ERFListener _listener;
    private final short[] _sids;

    public EventRecordFactory(ERFListener listener, short[] sids) {
        this._listener = listener;
        if (sids == null) {
            this._sids = null;
            return;
        }
        this._sids = (short[]) sids.clone();
        Arrays.sort(this._sids);
    }

    private boolean isSidIncluded(short sid) {
        boolean z = true;
        if (this._sids == null) {
            return true;
        }
        if (Arrays.binarySearch(this._sids, sid) < 0) {
            z = false;
        }
        return z;
    }

    private boolean processRecord(Record record) {
        if (!isSidIncluded(record.getSid())) {
            return true;
        }
        return this._listener.processRecord(record);
    }

    public void processRecords(InputStream in) throws RecordFormatException {
        Record last_record = null;
        RecordInputStream recStream = new RecordInputStream(in);
        while (recStream.hasNextRecord()) {
            recStream.nextRecord();
            Record[] recs = RecordFactory.createRecord(recStream);
            int k = 0;
            if (recs.length > 1) {
                while (true) {
                    int k2 = k;
                    if (k2 >= recs.length) {
                        continue;
                        break;
                    } else if (last_record == null || processRecord(last_record)) {
                        last_record = recs[k2];
                        k = k2 + 1;
                    } else {
                        return;
                    }
                }
            } else {
                Record record = recs[0];
                if (record == null) {
                    continue;
                } else if (last_record == null || processRecord(last_record)) {
                    last_record = record;
                } else {
                    return;
                }
            }
        }
        if (last_record != null) {
            processRecord(last_record);
        }
    }
}

package org.apache.poi.hssf.record;

import java.util.Iterator;
import org.apache.poi.hssf.record.PageBreakRecord.Break;

public final class HorizontalPageBreakRecord extends PageBreakRecord {
    public static final short sid = 27;

    public HorizontalPageBreakRecord() {
    }

    public HorizontalPageBreakRecord(RecordInputStream in) {
        super(in);
    }

    public short getSid() {
        return 27;
    }

    public Object clone() {
        PageBreakRecord result = new HorizontalPageBreakRecord();
        Iterator iterator = getBreaksIterator();
        while (iterator.hasNext()) {
            Break original = (Break) iterator.next();
            result.addBreak(original.main, original.subFrom, original.subTo);
        }
        return result;
    }
}

package org.apache.poi.hssf.record;

import java.io.ByteArrayInputStream;

public abstract class Record extends RecordBase {
    public abstract short getSid();

    protected Record() {
    }

    public final byte[] serialize() {
        byte[] retval = new byte[getRecordSize()];
        serialize(0, retval);
        return retval;
    }

    public String toString() {
        return super.toString();
    }

    public Object clone() {
        StringBuilder sb = new StringBuilder();
        sb.append("The class ");
        sb.append(getClass().getName());
        sb.append(" needs to define a clone method");
        throw new RuntimeException(sb.toString());
    }

    public Record cloneViaReserialise() {
        RecordInputStream rinp = new RecordInputStream(new ByteArrayInputStream(serialize()));
        rinp.nextRecord();
        Record[] r = RecordFactory.createRecord(rinp);
        if (r.length == 1) {
            return r[0];
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Re-serialised a record to clone it, but got ");
        sb.append(r.length);
        sb.append(" records back!");
        throw new IllegalStateException(sb.toString());
    }
}

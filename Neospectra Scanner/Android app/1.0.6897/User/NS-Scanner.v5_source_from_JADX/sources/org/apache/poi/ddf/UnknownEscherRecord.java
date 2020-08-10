package org.apache.poi.ddf;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndian;

public final class UnknownEscherRecord extends EscherRecord {
    private static final byte[] NO_BYTES = new byte[0];
    private List<EscherRecord> _childRecords = new ArrayList();
    private byte[] thedata = NO_BYTES;

    public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory) {
        int bytesRemaining = readHeader(data, offset);
        int avaliable = data.length - (offset + 8);
        if (bytesRemaining > avaliable) {
            bytesRemaining = avaliable;
        }
        if (isContainerRecord()) {
            this.thedata = new byte[0];
            int offset2 = offset + 8;
            int bytesWritten = 0 + 8;
            while (bytesRemaining > 0) {
                EscherRecord child = recordFactory.createRecord(data, offset2);
                int childBytesWritten = child.fillFields(data, offset2, recordFactory);
                bytesWritten += childBytesWritten;
                offset2 += childBytesWritten;
                bytesRemaining -= childBytesWritten;
                getChildRecords().add(child);
            }
            return bytesWritten;
        }
        this.thedata = new byte[bytesRemaining];
        System.arraycopy(data, offset + 8, this.thedata, 0, bytesRemaining);
        return bytesRemaining + 8;
    }

    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);
        LittleEndian.putShort(data, offset, getOptions());
        LittleEndian.putShort(data, offset + 2, getRecordId());
        int remainingBytes = this.thedata.length;
        for (EscherRecord r : this._childRecords) {
            remainingBytes += r.getRecordSize();
        }
        LittleEndian.putInt(data, offset + 4, remainingBytes);
        System.arraycopy(this.thedata, 0, data, offset + 8, this.thedata.length);
        int pos = offset + 8 + this.thedata.length;
        for (EscherRecord r2 : this._childRecords) {
            pos += r2.serialize(pos, data, listener);
        }
        listener.afterRecordSerialize(pos, getRecordId(), pos - offset, this);
        return pos - offset;
    }

    public byte[] getData() {
        return this.thedata;
    }

    public int getRecordSize() {
        return this.thedata.length + 8;
    }

    public List<EscherRecord> getChildRecords() {
        return this._childRecords;
    }

    public void setChildRecords(List<EscherRecord> childRecords) {
        this._childRecords = childRecords;
    }

    public Object clone() {
        return super.clone();
    }

    public String getRecordName() {
        StringBuilder sb = new StringBuilder();
        sb.append("Unknown 0x");
        sb.append(HexDump.toHex(getRecordId()));
        return sb.toString();
    }

    public String toString() {
        StringBuffer children = new StringBuffer();
        if (getChildRecords().size() > 0) {
            children.append("  children: \n");
            for (EscherRecord record : this._childRecords) {
                children.append(record.toString());
                children.append(10);
            }
        }
        String theDumpHex = HexDump.toHex(this.thedata, 32);
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append(":");
        sb.append(10);
        sb.append("  isContainer: ");
        sb.append(isContainerRecord());
        sb.append(10);
        sb.append("  options: 0x");
        sb.append(HexDump.toHex(getOptions()));
        sb.append(10);
        sb.append("  recordId: 0x");
        sb.append(HexDump.toHex(getRecordId()));
        sb.append(10);
        sb.append("  numchildren: ");
        sb.append(getChildRecords().size());
        sb.append(10);
        sb.append(theDumpHex);
        sb.append(children.toString());
        return sb.toString();
    }

    public void addChildRecord(EscherRecord childRecord) {
        getChildRecords().add(childRecord);
    }
}

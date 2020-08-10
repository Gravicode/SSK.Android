package org.apache.poi.ddf;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndian;

public final class EscherContainerRecord extends EscherRecord {
    public static final short BSTORE_CONTAINER = -4095;
    public static final short DGG_CONTAINER = -4096;
    public static final short DG_CONTAINER = -4094;
    public static final short SOLVER_CONTAINER = -4091;
    public static final short SPGR_CONTAINER = -4093;
    public static final short SP_CONTAINER = -4092;
    private final List<EscherRecord> _childRecords = new ArrayList();

    private static final class ReadOnlyIterator implements Iterator<EscherRecord> {
        private int _index = 0;
        private final List<EscherRecord> _list;

        public ReadOnlyIterator(List<EscherRecord> list) {
            this._list = list;
        }

        public boolean hasNext() {
            return this._index < this._list.size();
        }

        public EscherRecord next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            List<EscherRecord> list = this._list;
            int i = this._index;
            this._index = i + 1;
            return (EscherRecord) list.get(i);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public int fillFields(byte[] data, int pOffset, EscherRecordFactory recordFactory) {
        int bytesRemaining = readHeader(data, pOffset);
        int bytesWritten = 8;
        int offset = pOffset + 8;
        while (bytesRemaining > 0 && offset < data.length) {
            EscherRecord child = recordFactory.createRecord(data, offset);
            int childBytesWritten = child.fillFields(data, offset, recordFactory);
            bytesWritten += childBytesWritten;
            offset += childBytesWritten;
            bytesRemaining -= childBytesWritten;
            addChildRecord(child);
            if (offset >= data.length && bytesRemaining > 0) {
                PrintStream printStream = System.out;
                StringBuilder sb = new StringBuilder();
                sb.append("WARNING: ");
                sb.append(bytesRemaining);
                sb.append(" bytes remaining but no space left");
                printStream.println(sb.toString());
            }
        }
        return bytesWritten;
    }

    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);
        LittleEndian.putShort(data, offset, getOptions());
        LittleEndian.putShort(data, offset + 2, getRecordId());
        int remainingBytes = 0;
        for (EscherRecord r : this._childRecords) {
            remainingBytes += r.getRecordSize();
        }
        LittleEndian.putInt(data, offset + 4, remainingBytes);
        int pos = offset + 8;
        for (EscherRecord r2 : this._childRecords) {
            pos += r2.serialize(pos, data, listener);
        }
        listener.afterRecordSerialize(pos, getRecordId(), pos - offset, this);
        return pos - offset;
    }

    public int getRecordSize() {
        int childRecordsSize = 0;
        for (EscherRecord r : this._childRecords) {
            childRecordsSize += r.getRecordSize();
        }
        return childRecordsSize + 8;
    }

    public boolean hasChildOfType(short recordId) {
        for (EscherRecord r : this._childRecords) {
            if (r.getRecordId() == recordId) {
                return true;
            }
        }
        return false;
    }

    public EscherRecord getChild(int index) {
        return (EscherRecord) this._childRecords.get(index);
    }

    public List<EscherRecord> getChildRecords() {
        return new ArrayList(this._childRecords);
    }

    public Iterator<EscherRecord> getChildIterator() {
        return new ReadOnlyIterator(this._childRecords);
    }

    public void setChildRecords(List<EscherRecord> childRecords) {
        if (childRecords == this._childRecords) {
            throw new IllegalStateException("Child records private data member has escaped");
        }
        this._childRecords.clear();
        this._childRecords.addAll(childRecords);
    }

    public boolean removeChildRecord(EscherRecord toBeRemoved) {
        return this._childRecords.remove(toBeRemoved);
    }

    public List<EscherContainerRecord> getChildContainers() {
        List<EscherContainerRecord> containers = new ArrayList<>();
        for (EscherRecord r : this._childRecords) {
            if (r instanceof EscherContainerRecord) {
                containers.add((EscherContainerRecord) r);
            }
        }
        return containers;
    }

    public String getRecordName() {
        switch (getRecordId()) {
            case -4096:
                return "DggContainer";
            case -4095:
                return "BStoreContainer";
            case -4094:
                return "DgContainer";
            case -4093:
                return "SpgrContainer";
            case -4092:
                return "SpContainer";
            case -4091:
                return "SolverContainer";
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Container 0x");
                sb.append(HexDump.toHex(getRecordId()));
                return sb.toString();
        }
    }

    public void display(PrintWriter w, int indent) {
        super.display(w, indent);
        for (EscherRecord escherRecord : this._childRecords) {
            escherRecord.display(w, indent + 1);
        }
    }

    public void addChildRecord(EscherRecord record) {
        this._childRecords.add(record);
    }

    public void addChildBefore(EscherRecord record, int insertBeforeRecordId) {
        int i = 0;
        while (i < this._childRecords.size()) {
            if (((EscherRecord) this._childRecords.get(i)).getRecordId() == insertBeforeRecordId) {
                int i2 = i + 1;
                this._childRecords.add(i, record);
                i = i2;
            }
            i++;
        }
    }

    public String toString() {
        return toString("");
    }

    public String toString(String indent) {
        String nl = System.getProperty("line.separator");
        StringBuffer children = new StringBuffer();
        if (this._childRecords.size() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("  children: ");
            sb.append(nl);
            children.append(sb.toString());
            int count = 0;
            for (EscherRecord record : this._childRecords) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(indent);
                sb2.append("   ");
                String newIndent = sb2.toString();
                StringBuilder sb3 = new StringBuilder();
                sb3.append(newIndent);
                sb3.append("Child ");
                sb3.append(count);
                sb3.append(":");
                sb3.append(nl);
                children.append(sb3.toString());
                if (record instanceof EscherContainerRecord) {
                    children.append(((EscherContainerRecord) record).toString(newIndent));
                } else {
                    children.append(record.toString());
                }
                count++;
            }
        }
        StringBuilder sb4 = new StringBuilder();
        sb4.append(indent);
        sb4.append(getClass().getName());
        sb4.append(" (");
        sb4.append(getRecordName());
        sb4.append("):");
        sb4.append(nl);
        sb4.append(indent);
        sb4.append("  isContainer: ");
        sb4.append(isContainerRecord());
        sb4.append(nl);
        sb4.append(indent);
        sb4.append("  options: 0x");
        sb4.append(HexDump.toHex(getOptions()));
        sb4.append(nl);
        sb4.append(indent);
        sb4.append("  recordId: 0x");
        sb4.append(HexDump.toHex(getRecordId()));
        sb4.append(nl);
        sb4.append(indent);
        sb4.append("  numchildren: ");
        sb4.append(this._childRecords.size());
        sb4.append(nl);
        sb4.append(indent);
        sb4.append(children.toString());
        return sb4.toString();
    }

    public EscherSpRecord getChildById(short recordId) {
        for (EscherRecord r : this._childRecords) {
            if (r.getRecordId() == recordId) {
                return (EscherSpRecord) r;
            }
        }
        return null;
    }

    public void getRecordsById(short recordId, List<EscherRecord> out) {
        for (EscherRecord r : this._childRecords) {
            if (r instanceof EscherContainerRecord) {
                ((EscherContainerRecord) r).getRecordsById(recordId, out);
            } else if (r.getRecordId() == recordId) {
                out.add(r);
            }
        }
    }
}

package org.apache.poi.hssf.usermodel;

import java.io.IOException;
import org.apache.poi.hssf.record.EmbeddedObjectRefSubRecord;
import org.apache.poi.hssf.record.ObjRecord;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.Entry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.util.HexDump;

public final class HSSFObjectData {
    private final POIFSFileSystem _poifs;
    private final ObjRecord _record;

    public HSSFObjectData(ObjRecord record, POIFSFileSystem poifs) {
        this._record = record;
        this._poifs = poifs;
    }

    public String getOLE2ClassName() {
        return findObjectRecord().getOLEClassName();
    }

    public DirectoryEntry getDirectory() throws IOException {
        int streamId = findObjectRecord().getStreamId().intValue();
        StringBuilder sb = new StringBuilder();
        sb.append("MBD");
        sb.append(HexDump.toHex(streamId));
        String streamName = sb.toString();
        Entry entry = this._poifs.getRoot().getEntry(streamName);
        if (entry instanceof DirectoryEntry) {
            return (DirectoryEntry) entry;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Stream ");
        sb2.append(streamName);
        sb2.append(" was not an OLE2 directory");
        throw new IOException(sb2.toString());
    }

    public byte[] getObjectData() {
        return findObjectRecord().getObjectData();
    }

    public boolean hasDirectoryEntry() {
        Integer streamId = findObjectRecord().getStreamId();
        return (streamId == null || streamId.intValue() == 0) ? false : true;
    }

    /* access modifiers changed from: protected */
    public EmbeddedObjectRefSubRecord findObjectRecord() {
        for (Object subRecord : this._record.getSubRecords()) {
            if (subRecord instanceof EmbeddedObjectRefSubRecord) {
                return (EmbeddedObjectRefSubRecord) subRecord;
            }
        }
        throw new IllegalStateException("Object data does not contain a reference to an embedded object OLE2 directory");
    }
}

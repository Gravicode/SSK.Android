package org.apache.poi.hssf.eventusermodel;

import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordFactoryInputStream;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class HSSFEventFactory {
    public void processWorkbookEvents(HSSFRequest req, POIFSFileSystem fs) throws IOException {
        processWorkbookEvents(req, fs.getRoot());
    }

    public void processWorkbookEvents(HSSFRequest req, DirectoryNode dir) throws IOException {
        processEvents(req, dir.createDocumentInputStream("Workbook"));
    }

    public short abortableProcessWorkbookEvents(HSSFRequest req, POIFSFileSystem fs) throws IOException, HSSFUserException {
        return abortableProcessWorkbookEvents(req, fs.getRoot());
    }

    public short abortableProcessWorkbookEvents(HSSFRequest req, DirectoryNode dir) throws IOException, HSSFUserException {
        return abortableProcessEvents(req, dir.createDocumentInputStream("Workbook"));
    }

    public void processEvents(HSSFRequest req, InputStream in) {
        try {
            genericProcessEvents(req, in);
        } catch (HSSFUserException e) {
        }
    }

    public short abortableProcessEvents(HSSFRequest req, InputStream in) throws HSSFUserException {
        return genericProcessEvents(req, in);
    }

    private short genericProcessEvents(HSSFRequest req, InputStream in) throws HSSFUserException {
        short userCode = 0;
        RecordFactoryInputStream recordStream = new RecordFactoryInputStream(in, false);
        do {
            Record r = recordStream.nextRecord();
            if (r == null) {
                break;
            }
            userCode = req.processRecord(r);
        } while (userCode == 0);
        return userCode;
    }
}

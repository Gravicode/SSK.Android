package org.apache.poi.hssf.util;

import org.apache.poi.hssf.record.RecordInputStream;

public class CellRangeAddress extends org.apache.poi.p009ss.util.CellRangeAddress {
    public CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol) {
        super(firstRow, lastRow, firstCol, lastCol);
    }

    public CellRangeAddress(RecordInputStream in) {
        super(in);
    }
}

package org.apache.poi.hssf.usermodel;

import org.apache.poi.p009ss.usermodel.AutoFilter;

public final class HSSFAutoFilter implements AutoFilter {
    private HSSFSheet _sheet;

    HSSFAutoFilter(HSSFSheet sheet) {
        this._sheet = sheet;
    }
}

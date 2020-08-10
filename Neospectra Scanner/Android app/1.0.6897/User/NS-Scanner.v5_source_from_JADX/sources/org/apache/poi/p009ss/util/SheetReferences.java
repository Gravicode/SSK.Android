package org.apache.poi.p009ss.util;

import java.util.HashMap;
import java.util.Map;

/* renamed from: org.apache.poi.ss.util.SheetReferences */
public class SheetReferences {
    Map map = new HashMap(5);

    public void addSheetReference(String sheetName, int number) {
        this.map.put(Integer.valueOf(number), sheetName);
    }

    public String getSheetName(int number) {
        return (String) this.map.get(Integer.valueOf(number));
    }
}

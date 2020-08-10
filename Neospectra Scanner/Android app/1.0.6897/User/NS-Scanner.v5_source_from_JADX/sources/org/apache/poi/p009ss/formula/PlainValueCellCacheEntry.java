package org.apache.poi.p009ss.formula;

import org.apache.poi.hssf.record.formula.eval.ValueEval;

/* renamed from: org.apache.poi.ss.formula.PlainValueCellCacheEntry */
final class PlainValueCellCacheEntry extends CellCacheEntry {
    public PlainValueCellCacheEntry(ValueEval value) {
        updateValue(value);
    }
}

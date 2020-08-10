package org.apache.poi.p009ss.formula;

import org.apache.poi.hssf.record.formula.NameXPtg;
import org.apache.poi.p009ss.SpreadsheetVersion;

/* renamed from: org.apache.poi.ss.formula.FormulaParsingWorkbook */
public interface FormulaParsingWorkbook {
    int getExternalSheetIndex(String str);

    int getExternalSheetIndex(String str, String str2);

    EvaluationName getName(String str, int i);

    NameXPtg getNameXPtg(String str);

    SpreadsheetVersion getSpreadsheetVersion();
}

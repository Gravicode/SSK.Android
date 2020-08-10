package org.apache.poi.p009ss.formula;

import org.apache.poi.hssf.record.formula.NamePtg;
import org.apache.poi.hssf.record.formula.NameXPtg;
import org.apache.poi.p009ss.formula.EvaluationWorkbook.ExternalSheet;

/* renamed from: org.apache.poi.ss.formula.FormulaRenderingWorkbook */
public interface FormulaRenderingWorkbook {
    ExternalSheet getExternalSheet(int i);

    String getNameText(NamePtg namePtg);

    String getSheetNameByExternSheet(int i);

    String resolveNameXText(NameXPtg nameXPtg);
}

package org.apache.poi.p009ss.formula;

import org.apache.poi.hssf.record.formula.NamePtg;
import org.apache.poi.hssf.record.formula.Ptg;

/* renamed from: org.apache.poi.ss.formula.EvaluationName */
public interface EvaluationName {
    NamePtg createPtg();

    Ptg[] getNameDefinition();

    String getNameText();

    boolean hasFormula();

    boolean isFunctionName();

    boolean isRange();
}

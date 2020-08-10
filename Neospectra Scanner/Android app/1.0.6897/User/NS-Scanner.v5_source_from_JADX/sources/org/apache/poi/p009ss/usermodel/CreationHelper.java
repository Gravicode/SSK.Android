package org.apache.poi.p009ss.usermodel;

/* renamed from: org.apache.poi.ss.usermodel.CreationHelper */
public interface CreationHelper {
    ClientAnchor createClientAnchor();

    DataFormat createDataFormat();

    FormulaEvaluator createFormulaEvaluator();

    Hyperlink createHyperlink(int i);

    RichTextString createRichTextString(String str);
}

package org.apache.poi.p009ss.extractor;

/* renamed from: org.apache.poi.ss.extractor.ExcelExtractor */
public interface ExcelExtractor {
    String getText();

    void setFormulasNotResults(boolean z);

    void setIncludeCellComments(boolean z);

    void setIncludeSheetNames(boolean z);
}

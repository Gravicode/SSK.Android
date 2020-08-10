package org.apache.poi.p009ss.formula;

/* renamed from: org.apache.poi.ss.formula.EvaluationCell */
public interface EvaluationCell {
    boolean getBooleanCellValue();

    int getCellType();

    int getColumnIndex();

    int getErrorCellValue();

    Object getIdentityKey();

    double getNumericCellValue();

    int getRowIndex();

    EvaluationSheet getSheet();

    String getStringCellValue();
}

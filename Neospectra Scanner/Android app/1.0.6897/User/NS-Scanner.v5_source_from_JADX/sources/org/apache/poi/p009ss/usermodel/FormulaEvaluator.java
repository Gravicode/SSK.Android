package org.apache.poi.p009ss.usermodel;

/* renamed from: org.apache.poi.ss.usermodel.FormulaEvaluator */
public interface FormulaEvaluator {
    void clearAllCachedResultValues();

    CellValue evaluate(Cell cell);

    int evaluateFormulaCell(Cell cell);

    Cell evaluateInCell(Cell cell);

    void notifyDeleteCell(Cell cell);

    void notifySetFormula(Cell cell);

    void notifyUpdateCell(Cell cell);
}

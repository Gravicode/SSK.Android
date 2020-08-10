package org.apache.poi.p009ss.formula.eval.forked;

import org.apache.poi.hssf.record.formula.eval.BoolEval;
import org.apache.poi.hssf.record.formula.eval.ErrorEval;
import org.apache.poi.hssf.record.formula.eval.NumberEval;
import org.apache.poi.hssf.record.formula.eval.StringEval;
import org.apache.poi.hssf.record.formula.eval.ValueEval;
import org.apache.poi.hssf.record.formula.udf.UDFFinder;
import org.apache.poi.hssf.usermodel.HSSFEvaluationWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.p009ss.formula.CollaboratingWorkbooksEnvironment;
import org.apache.poi.p009ss.formula.EvaluationCell;
import org.apache.poi.p009ss.formula.EvaluationWorkbook;
import org.apache.poi.p009ss.formula.IStabilityClassifier;
import org.apache.poi.p009ss.formula.WorkbookEvaluator;
import org.apache.poi.p009ss.usermodel.Workbook;

/* renamed from: org.apache.poi.ss.formula.eval.forked.ForkedEvaluator */
public final class ForkedEvaluator {
    private WorkbookEvaluator _evaluator;
    private ForkedEvaluationWorkbook _sewb;

    private ForkedEvaluator(EvaluationWorkbook masterWorkbook, IStabilityClassifier stabilityClassifier, UDFFinder udfFinder) {
        this._sewb = new ForkedEvaluationWorkbook(masterWorkbook);
        this._evaluator = new WorkbookEvaluator(this._sewb, stabilityClassifier, udfFinder);
    }

    private static EvaluationWorkbook createEvaluationWorkbook(Workbook wb) {
        if (wb instanceof HSSFWorkbook) {
            return HSSFEvaluationWorkbook.create((HSSFWorkbook) wb);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unexpected workbook type (");
        sb.append(wb.getClass().getName());
        sb.append(")");
        throw new IllegalArgumentException(sb.toString());
    }

    public static ForkedEvaluator create(Workbook wb, IStabilityClassifier stabilityClassifier) {
        return create(wb, stabilityClassifier, null);
    }

    public static ForkedEvaluator create(Workbook wb, IStabilityClassifier stabilityClassifier, UDFFinder udfFinder) {
        return new ForkedEvaluator(createEvaluationWorkbook(wb), stabilityClassifier, udfFinder);
    }

    public void updateCell(String sheetName, int rowIndex, int columnIndex, ValueEval value) {
        ForkedEvaluationCell cell = this._sewb.getOrCreateUpdatableCell(sheetName, rowIndex, columnIndex);
        cell.setValue(value);
        this._evaluator.notifyUpdateCell(cell);
    }

    public void copyUpdatedCells(Workbook workbook) {
        this._sewb.copyUpdatedCells(workbook);
    }

    public ValueEval evaluate(String sheetName, int rowIndex, int columnIndex) {
        EvaluationCell cell = this._sewb.getEvaluationCell(sheetName, rowIndex, columnIndex);
        switch (cell.getCellType()) {
            case 0:
                return new NumberEval(cell.getNumericCellValue());
            case 1:
                return new StringEval(cell.getStringCellValue());
            case 2:
                return this._evaluator.evaluate(cell);
            case 3:
                return null;
            case 4:
                return BoolEval.valueOf(cell.getBooleanCellValue());
            case 5:
                return ErrorEval.valueOf(cell.getErrorCellValue());
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Bad cell type (");
                sb.append(cell.getCellType());
                sb.append(")");
                throw new IllegalStateException(sb.toString());
        }
    }

    public static void setupEnvironment(String[] workbookNames, ForkedEvaluator[] evaluators) {
        WorkbookEvaluator[] wbEvals = new WorkbookEvaluator[evaluators.length];
        for (int i = 0; i < wbEvals.length; i++) {
            wbEvals[i] = evaluators[i]._evaluator;
        }
        CollaboratingWorkbooksEnvironment.setup(workbookNames, wbEvals);
    }
}

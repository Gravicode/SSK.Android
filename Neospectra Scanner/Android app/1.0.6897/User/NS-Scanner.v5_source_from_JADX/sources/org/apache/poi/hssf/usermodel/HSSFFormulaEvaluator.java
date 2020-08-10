package org.apache.poi.hssf.usermodel;

import java.util.Iterator;
import org.apache.poi.hssf.record.formula.eval.BoolEval;
import org.apache.poi.hssf.record.formula.eval.ErrorEval;
import org.apache.poi.hssf.record.formula.eval.NumberEval;
import org.apache.poi.hssf.record.formula.eval.StringEval;
import org.apache.poi.hssf.record.formula.eval.ValueEval;
import org.apache.poi.hssf.record.formula.udf.UDFFinder;
import org.apache.poi.p009ss.formula.CollaboratingWorkbooksEnvironment;
import org.apache.poi.p009ss.formula.IStabilityClassifier;
import org.apache.poi.p009ss.formula.WorkbookEvaluator;
import org.apache.poi.p009ss.usermodel.Cell;
import org.apache.poi.p009ss.usermodel.CellValue;
import org.apache.poi.p009ss.usermodel.FormulaEvaluator;
import org.apache.poi.p009ss.usermodel.RichTextString;
import org.apache.poi.p009ss.usermodel.Row;

public class HSSFFormulaEvaluator implements FormulaEvaluator {
    private WorkbookEvaluator _bookEvaluator;

    public HSSFFormulaEvaluator(HSSFSheet sheet, HSSFWorkbook workbook) {
        this(workbook);
    }

    public HSSFFormulaEvaluator(HSSFWorkbook workbook) {
        this(workbook, (IStabilityClassifier) null);
    }

    public HSSFFormulaEvaluator(HSSFWorkbook workbook, IStabilityClassifier stabilityClassifier) {
        this(workbook, stabilityClassifier, null);
    }

    private HSSFFormulaEvaluator(HSSFWorkbook workbook, IStabilityClassifier stabilityClassifier, UDFFinder udfFinder) {
        this._bookEvaluator = new WorkbookEvaluator(HSSFEvaluationWorkbook.create(workbook), stabilityClassifier, udfFinder);
    }

    public static HSSFFormulaEvaluator create(HSSFWorkbook workbook, IStabilityClassifier stabilityClassifier, UDFFinder udfFinder) {
        return new HSSFFormulaEvaluator(workbook, stabilityClassifier, udfFinder);
    }

    public static void setupEnvironment(String[] workbookNames, HSSFFormulaEvaluator[] evaluators) {
        WorkbookEvaluator[] wbEvals = new WorkbookEvaluator[evaluators.length];
        for (int i = 0; i < wbEvals.length; i++) {
            wbEvals[i] = evaluators[i]._bookEvaluator;
        }
        CollaboratingWorkbooksEnvironment.setup(workbookNames, wbEvals);
    }

    public void setCurrentRow(HSSFRow row) {
    }

    public void clearAllCachedResultValues() {
        this._bookEvaluator.clearAllCachedResultValues();
    }

    public void notifyUpdateCell(HSSFCell cell) {
        this._bookEvaluator.notifyUpdateCell(new HSSFEvaluationCell(cell));
    }

    public void notifyUpdateCell(Cell cell) {
        this._bookEvaluator.notifyUpdateCell(new HSSFEvaluationCell((HSSFCell) cell));
    }

    public void notifyDeleteCell(HSSFCell cell) {
        this._bookEvaluator.notifyDeleteCell(new HSSFEvaluationCell(cell));
    }

    public void notifyDeleteCell(Cell cell) {
        this._bookEvaluator.notifyDeleteCell(new HSSFEvaluationCell((HSSFCell) cell));
    }

    public void notifySetFormula(Cell cell) {
        this._bookEvaluator.notifyUpdateCell(new HSSFEvaluationCell((HSSFCell) cell));
    }

    public CellValue evaluate(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case 0:
                return new CellValue(cell.getNumericCellValue());
            case 1:
                return new CellValue(cell.getRichStringCellValue().getString());
            case 2:
                return evaluateFormulaCellValue(cell);
            case 3:
                return null;
            case 4:
                return CellValue.valueOf(cell.getBooleanCellValue());
            case 5:
                return CellValue.getError(cell.getErrorCellValue());
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Bad cell type (");
                sb.append(cell.getCellType());
                sb.append(")");
                throw new IllegalStateException(sb.toString());
        }
    }

    public int evaluateFormulaCell(Cell cell) {
        if (cell == null || cell.getCellType() != 2) {
            return -1;
        }
        CellValue cv = evaluateFormulaCellValue(cell);
        setCellValue(cell, cv);
        return cv.getCellType();
    }

    public HSSFCell evaluateInCell(Cell cell) {
        if (cell == null) {
            return null;
        }
        HSSFCell result = (HSSFCell) cell;
        if (cell.getCellType() == 2) {
            CellValue cv = evaluateFormulaCellValue(cell);
            setCellValue(cell, cv);
            setCellType(cell, cv);
        }
        return result;
    }

    private static void setCellType(Cell cell, CellValue cv) {
        int cellType = cv.getCellType();
        switch (cellType) {
            case 0:
            case 1:
            case 4:
            case 5:
                cell.setCellType(cellType);
                return;
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unexpected cell value type (");
                sb.append(cellType);
                sb.append(")");
                throw new IllegalStateException(sb.toString());
        }
    }

    private static void setCellValue(Cell cell, CellValue cv) {
        int cellType = cv.getCellType();
        switch (cellType) {
            case 0:
                cell.setCellValue(cv.getNumberValue());
                return;
            case 1:
                cell.setCellValue((RichTextString) new HSSFRichTextString(cv.getStringValue()));
                return;
            case 4:
                cell.setCellValue(cv.getBooleanValue());
                return;
            case 5:
                cell.setCellErrorValue(cv.getErrorValue());
                return;
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unexpected cell value type (");
                sb.append(cellType);
                sb.append(")");
                throw new IllegalStateException(sb.toString());
        }
    }

    public static void evaluateAllFormulaCells(HSSFWorkbook wb) {
        HSSFFormulaEvaluator evaluator = new HSSFFormulaEvaluator(wb);
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            Iterator<Row> rit = wb.getSheetAt(i).rowIterator();
            while (rit.hasNext()) {
                Iterator<Cell> cit = ((Row) rit.next()).cellIterator();
                while (cit.hasNext()) {
                    Cell c = (Cell) cit.next();
                    if (c.getCellType() == 2) {
                        evaluator.evaluateFormulaCell(c);
                    }
                }
            }
        }
    }

    private CellValue evaluateFormulaCellValue(Cell cell) {
        ValueEval eval = this._bookEvaluator.evaluate(new HSSFEvaluationCell((HSSFCell) cell));
        if (eval instanceof NumberEval) {
            return new CellValue(((NumberEval) eval).getNumberValue());
        }
        if (eval instanceof BoolEval) {
            return CellValue.valueOf(((BoolEval) eval).getBooleanValue());
        }
        if (eval instanceof StringEval) {
            return new CellValue(((StringEval) eval).getStringValue());
        }
        if (eval instanceof ErrorEval) {
            return CellValue.getError(((ErrorEval) eval).getErrorCode());
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unexpected eval class (");
        sb.append(eval.getClass().getName());
        sb.append(")");
        throw new RuntimeException(sb.toString());
    }
}

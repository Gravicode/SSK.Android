package org.apache.poi.p009ss.formula;

import org.apache.poi.hssf.record.formula.Area3DPtg;
import org.apache.poi.hssf.record.formula.NameXPtg;
import org.apache.poi.hssf.record.formula.Ptg;
import org.apache.poi.hssf.record.formula.Ref3DPtg;
import org.apache.poi.hssf.record.formula.eval.ErrorEval;
import org.apache.poi.hssf.record.formula.eval.NameXEval;
import org.apache.poi.hssf.record.formula.eval.ValueEval;
import org.apache.poi.hssf.record.formula.functions.FreeRefFunction;
import org.apache.poi.p009ss.SpreadsheetVersion;
import org.apache.poi.p009ss.formula.CollaboratingWorkbooksEnvironment.WorkbookNotFoundException;
import org.apache.poi.p009ss.formula.EvaluationWorkbook.ExternalName;
import org.apache.poi.p009ss.formula.EvaluationWorkbook.ExternalSheet;
import org.apache.poi.p009ss.util.CellReference;
import org.apache.poi.p009ss.util.CellReference.NameType;

/* renamed from: org.apache.poi.ss.formula.OperationEvaluationContext */
public final class OperationEvaluationContext {
    public static final FreeRefFunction UDF = UserDefinedFunction.instance;
    private final WorkbookEvaluator _bookEvaluator;
    private final int _columnIndex;
    private final int _rowIndex;
    private final int _sheetIndex;
    private final EvaluationTracker _tracker;
    private final EvaluationWorkbook _workbook;

    public OperationEvaluationContext(WorkbookEvaluator bookEvaluator, EvaluationWorkbook workbook, int sheetIndex, int srcRowNum, int srcColNum, EvaluationTracker tracker) {
        this._bookEvaluator = bookEvaluator;
        this._workbook = workbook;
        this._sheetIndex = sheetIndex;
        this._rowIndex = srcRowNum;
        this._columnIndex = srcColNum;
        this._tracker = tracker;
    }

    public EvaluationWorkbook getWorkbook() {
        return this._workbook;
    }

    public int getRowIndex() {
        return this._rowIndex;
    }

    public int getColumnIndex() {
        return this._columnIndex;
    }

    /* access modifiers changed from: 0000 */
    public SheetRefEvaluator createExternSheetRefEvaluator(ExternSheetReferenceToken ptg) {
        return createExternSheetRefEvaluator(ptg.getExternSheetIndex());
    }

    /* access modifiers changed from: 0000 */
    public SheetRefEvaluator createExternSheetRefEvaluator(int externSheetIndex) {
        WorkbookEvaluator targetEvaluator;
        int otherSheetIndex;
        ExternalSheet externalSheet = this._workbook.getExternalSheet(externSheetIndex);
        if (externalSheet == null) {
            otherSheetIndex = this._workbook.convertFromExternSheetIndex(externSheetIndex);
            targetEvaluator = this._bookEvaluator;
        } else {
            String workbookName = externalSheet.getWorkbookName();
            try {
                targetEvaluator = this._bookEvaluator.getOtherWorkbookEvaluator(workbookName);
                int otherSheetIndex2 = targetEvaluator.getSheetIndex(externalSheet.getSheetName());
                if (otherSheetIndex2 < 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Invalid sheet name '");
                    sb.append(externalSheet.getSheetName());
                    sb.append("' in bool '");
                    sb.append(workbookName);
                    sb.append("'.");
                    throw new RuntimeException(sb.toString());
                }
                otherSheetIndex = otherSheetIndex2;
            } catch (WorkbookNotFoundException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return new SheetRefEvaluator(targetEvaluator, this._tracker, otherSheetIndex);
    }

    private SheetRefEvaluator createExternSheetRefEvaluator(String workbookName, String sheetName) {
        WorkbookEvaluator targetEvaluator;
        if (workbookName == null) {
            targetEvaluator = this._bookEvaluator;
        } else if (sheetName == null) {
            throw new IllegalArgumentException("sheetName must not be null if workbookName is provided");
        } else {
            try {
                targetEvaluator = this._bookEvaluator.getOtherWorkbookEvaluator(workbookName);
            } catch (WorkbookNotFoundException e) {
                return null;
            }
        }
        int otherSheetIndex = sheetName == null ? this._sheetIndex : targetEvaluator.getSheetIndex(sheetName);
        if (otherSheetIndex < 0) {
            return null;
        }
        return new SheetRefEvaluator(targetEvaluator, this._tracker, otherSheetIndex);
    }

    public SheetRefEvaluator getRefEvaluatorForCurrentSheet() {
        return new SheetRefEvaluator(this._bookEvaluator, this._tracker, this._sheetIndex);
    }

    public ValueEval getDynamicReference(String workbookName, String sheetName, String refStrPart1, String refStrPart2, boolean isA1Style) {
        int lastCol;
        int lastRow;
        int firstCol;
        int firstRow;
        String str = refStrPart1;
        String str2 = refStrPart2;
        if (!isA1Style) {
            throw new RuntimeException("R1C1 style not supported yet");
        }
        SheetRefEvaluator sre = createExternSheetRefEvaluator(workbookName, sheetName);
        if (sre == null) {
            return ErrorEval.REF_INVALID;
        }
        SpreadsheetVersion ssVersion = ((FormulaParsingWorkbook) this._workbook).getSpreadsheetVersion();
        NameType part1refType = classifyCellReference(str, ssVersion);
        switch (part1refType) {
            case BAD_CELL_OR_NAMED_RANGE:
                return ErrorEval.REF_INVALID;
            case NAMED_RANGE:
                EvaluationName nm = ((FormulaParsingWorkbook) this._workbook).getName(str, this._sheetIndex);
                if (nm.isRange()) {
                    return this._bookEvaluator.evaluateNameFormula(nm.getNameDefinition(), this);
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Specified name '");
                sb.append(str);
                sb.append("' is not a range as expected.");
                throw new RuntimeException(sb.toString());
            default:
                if (str2 == null) {
                    switch (part1refType) {
                        case COLUMN:
                        case ROW:
                            return ErrorEval.REF_INVALID;
                        case CELL:
                            CellReference cr = new CellReference(str);
                            return new LazyRefEval(cr.getRow(), cr.getCol(), sre);
                        default:
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("Unexpected reference classification of '");
                            sb2.append(str);
                            sb2.append("'.");
                            throw new IllegalStateException(sb2.toString());
                    }
                } else {
                    NameType part2refType = classifyCellReference(str, ssVersion);
                    switch (part2refType) {
                        case BAD_CELL_OR_NAMED_RANGE:
                            return ErrorEval.REF_INVALID;
                        case NAMED_RANGE:
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("Cannot evaluate '");
                            sb3.append(str);
                            sb3.append("'. Indirect evaluation of defined names not supported yet");
                            throw new RuntimeException(sb3.toString());
                        default:
                            if (part2refType != part1refType) {
                                return ErrorEval.REF_INVALID;
                            }
                            switch (part1refType) {
                                case COLUMN:
                                    int lastRow2 = ssVersion.getLastRowIndex();
                                    firstRow = 0;
                                    lastRow = lastRow2;
                                    firstCol = parseColRef(refStrPart1);
                                    lastCol = parseColRef(refStrPart2);
                                    break;
                                case ROW:
                                    int lastCol2 = ssVersion.getLastColumnIndex();
                                    firstCol = 0;
                                    lastCol = lastCol2;
                                    firstRow = parseRowRef(refStrPart1);
                                    lastRow = parseRowRef(refStrPart2);
                                    break;
                                case CELL:
                                    CellReference cr2 = new CellReference(str);
                                    int firstRow2 = cr2.getRow();
                                    int firstCol2 = cr2.getCol();
                                    CellReference cr3 = new CellReference(str2);
                                    firstRow = firstRow2;
                                    firstCol = firstCol2;
                                    lastRow = cr3.getRow();
                                    lastCol = cr3.getCol();
                                    break;
                                default:
                                    StringBuilder sb4 = new StringBuilder();
                                    sb4.append("Unexpected reference classification of '");
                                    sb4.append(str);
                                    sb4.append("'.");
                                    throw new IllegalStateException(sb4.toString());
                            }
                            LazyAreaEval lazyAreaEval = new LazyAreaEval(firstRow, firstCol, lastRow, lastCol, sre);
                            return lazyAreaEval;
                    }
                }
        }
    }

    private static int parseRowRef(String refStrPart) {
        return CellReference.convertColStringToIndex(refStrPart);
    }

    private static int parseColRef(String refStrPart) {
        return Integer.parseInt(refStrPart) - 1;
    }

    private static NameType classifyCellReference(String str, SpreadsheetVersion ssVersion) {
        if (str.length() < 1) {
            return NameType.BAD_CELL_OR_NAMED_RANGE;
        }
        return CellReference.classifyCellReference(str, ssVersion);
    }

    public FreeRefFunction findUserDefinedFunction(String functionName) {
        return this._bookEvaluator.findUserDefinedFunction(functionName);
    }

    public ValueEval getRefEval(int rowIndex, int columnIndex) {
        return new LazyRefEval(rowIndex, columnIndex, getRefEvaluatorForCurrentSheet());
    }

    public ValueEval getRef3DEval(int rowIndex, int columnIndex, int extSheetIndex) {
        return new LazyRefEval(rowIndex, columnIndex, createExternSheetRefEvaluator(extSheetIndex));
    }

    public ValueEval getAreaEval(int firstRowIndex, int firstColumnIndex, int lastRowIndex, int lastColumnIndex) {
        LazyAreaEval lazyAreaEval = new LazyAreaEval(firstRowIndex, firstColumnIndex, lastRowIndex, lastColumnIndex, getRefEvaluatorForCurrentSheet());
        return lazyAreaEval;
    }

    public ValueEval getArea3DEval(int firstRowIndex, int firstColumnIndex, int lastRowIndex, int lastColumnIndex, int extSheetIndex) {
        LazyAreaEval lazyAreaEval = new LazyAreaEval(firstRowIndex, firstColumnIndex, lastRowIndex, lastColumnIndex, createExternSheetRefEvaluator(extSheetIndex));
        return lazyAreaEval;
    }

    public ValueEval getNameXEval(NameXPtg nameXPtg) {
        ExternalSheet externSheet = this._workbook.getExternalSheet(nameXPtg.getSheetRefIndex());
        if (externSheet == null) {
            return new NameXEval(nameXPtg);
        }
        NameXPtg nameXPtg2 = nameXPtg;
        String workbookName = externSheet.getWorkbookName();
        ExternalName externName = this._workbook.getExternalName(nameXPtg.getSheetRefIndex(), nameXPtg.getNameIndex());
        try {
            WorkbookEvaluator refWorkbookEvaluator = this._bookEvaluator.getOtherWorkbookEvaluator(workbookName);
            EvaluationName evaluationName = refWorkbookEvaluator.getName(externName.getName(), externName.getIx() - 1);
            if (evaluationName != null && evaluationName.hasFormula()) {
                if (evaluationName.getNameDefinition().length > 1) {
                    throw new RuntimeException("Complex name formulas not supported yet");
                }
                Ptg ptg = evaluationName.getNameDefinition()[0];
                if (ptg instanceof Ref3DPtg) {
                    Ref3DPtg ref3D = (Ref3DPtg) ptg;
                    return new LazyRefEval(ref3D.getRow(), ref3D.getColumn(), createExternSheetRefEvaluator(workbookName, refWorkbookEvaluator.getSheetName(refWorkbookEvaluator.getSheetIndexByExternIndex(ref3D.getExternSheetIndex()))));
                } else if (ptg instanceof Area3DPtg) {
                    Area3DPtg area3D = (Area3DPtg) ptg;
                    LazyAreaEval lazyAreaEval = new LazyAreaEval(area3D.getFirstRow(), area3D.getFirstColumn(), area3D.getLastRow(), area3D.getLastColumn(), createExternSheetRefEvaluator(workbookName, refWorkbookEvaluator.getSheetName(refWorkbookEvaluator.getSheetIndexByExternIndex(area3D.getExternSheetIndex()))));
                    return lazyAreaEval;
                }
            }
            return ErrorEval.REF_INVALID;
        } catch (WorkbookNotFoundException e) {
            WorkbookNotFoundException workbookNotFoundException = e;
            return ErrorEval.REF_INVALID;
        }
    }
}

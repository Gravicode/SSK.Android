package org.apache.poi.hssf.usermodel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.model.InternalSheet;
import org.apache.poi.hssf.model.InternalWorkbook;
import org.apache.poi.hssf.record.BlankRecord;
import org.apache.poi.hssf.record.BoolErrRecord;
import org.apache.poi.hssf.record.CellValueRecordInterface;
import org.apache.poi.hssf.record.CommonObjectDataSubRecord;
import org.apache.poi.hssf.record.DrawingRecord;
import org.apache.poi.hssf.record.FormulaRecord;
import org.apache.poi.hssf.record.HyperlinkRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NoteRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.ObjRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordBase;
import org.apache.poi.hssf.record.SubRecord;
import org.apache.poi.hssf.record.TextObjectRecord;
import org.apache.poi.hssf.record.aggregates.FormulaRecordAggregate;
import org.apache.poi.hssf.record.common.UnicodeString;
import org.apache.poi.hssf.record.formula.ExpPtg;
import org.apache.poi.hssf.record.formula.IntersectionPtg;
import org.apache.poi.hssf.record.formula.Ptg;
import org.apache.poi.hssf.record.formula.eval.ErrorEval;
import org.apache.poi.p009ss.SpreadsheetVersion;
import org.apache.poi.p009ss.usermodel.Cell;
import org.apache.poi.p009ss.usermodel.CellStyle;
import org.apache.poi.p009ss.usermodel.Comment;
import org.apache.poi.p009ss.usermodel.FormulaError;
import org.apache.poi.p009ss.usermodel.Hyperlink;
import org.apache.poi.p009ss.usermodel.RichTextString;
import org.apache.poi.p009ss.usermodel.Sheet;
import org.apache.poi.p009ss.util.CellRangeAddress;
import org.apache.poi.p009ss.util.CellReference;
import org.apache.poi.p009ss.util.NumberToTextConverter;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public class HSSFCell implements Cell {
    public static final short ENCODING_COMPRESSED_UNICODE = 0;
    public static final short ENCODING_UNCHANGED = -1;
    public static final short ENCODING_UTF_16 = 1;
    private static final String FILE_FORMAT_NAME = "BIFF8";
    private static final String LAST_COLUMN_NAME = SpreadsheetVersion.EXCEL97.getLastColumnName();
    public static final int LAST_COLUMN_NUMBER = SpreadsheetVersion.EXCEL97.getLastColumnIndex();
    private static POILogger log = POILogFactory.getLogger(HSSFCell.class);
    private final HSSFWorkbook _book;
    private int _cellType;
    private HSSFComment _comment;
    private CellValueRecordInterface _record;
    private final HSSFSheet _sheet;
    private HSSFRichTextString _stringValue;

    protected HSSFCell(HSSFWorkbook book, HSSFSheet sheet, int row, short col) {
        checkBounds(col);
        this._stringValue = null;
        this._book = book;
        this._sheet = sheet;
        setCellType(3, false, row, col, sheet.getSheet().getXFIndexForColAt(col));
    }

    public HSSFSheet getSheet() {
        return this._sheet;
    }

    public HSSFRow getRow() {
        return this._sheet.getRow(getRowIndex());
    }

    protected HSSFCell(HSSFWorkbook book, HSSFSheet sheet, int row, short col, int type) {
        checkBounds(col);
        this._cellType = -1;
        this._stringValue = null;
        this._book = book;
        this._sheet = sheet;
        setCellType(type, false, row, col, sheet.getSheet().getXFIndexForColAt(col));
    }

    protected HSSFCell(HSSFWorkbook book, HSSFSheet sheet, CellValueRecordInterface cval) {
        this._record = cval;
        this._cellType = determineType(cval);
        this._stringValue = null;
        this._book = book;
        this._sheet = sheet;
        switch (this._cellType) {
            case 1:
                this._stringValue = new HSSFRichTextString(book.getWorkbook(), (LabelSSTRecord) cval);
                break;
            case 2:
                this._stringValue = new HSSFRichTextString(((FormulaRecordAggregate) cval).getStringValue());
                break;
        }
        setCellStyle(new HSSFCellStyle(cval.getXFIndex(), book.getWorkbook().getExFormatAt(cval.getXFIndex()), book));
    }

    private static int determineType(CellValueRecordInterface cval) {
        if (cval instanceof FormulaRecordAggregate) {
            return 2;
        }
        Record record = (Record) cval;
        short sid = record.getSid();
        if (sid == 253) {
            return 1;
        }
        if (sid == 513) {
            return 3;
        }
        if (sid == 515) {
            return 0;
        }
        if (sid != 517) {
            StringBuilder sb = new StringBuilder();
            sb.append("Bad cell value rec (");
            sb.append(cval.getClass().getName());
            sb.append(")");
            throw new RuntimeException(sb.toString());
        }
        return ((BoolErrRecord) record).isBoolean() ? 4 : 5;
    }

    /* access modifiers changed from: protected */
    public InternalWorkbook getBoundWorkbook() {
        return this._book.getWorkbook();
    }

    public int getRowIndex() {
        return this._record.getRow();
    }

    public void setCellNum(short num) {
        this._record.setColumn(num);
    }

    /* access modifiers changed from: protected */
    public void updateCellNum(short num) {
        this._record.setColumn(num);
    }

    public short getCellNum() {
        return (short) getColumnIndex();
    }

    public int getColumnIndex() {
        return this._record.getColumn() & 65535;
    }

    public void setCellType(int cellType) {
        notifyFormulaChanging();
        if (isPartOfArrayFormulaGroup()) {
            notifyArrayFormulaChanging();
        }
        setCellType(cellType, true, this._record.getRow(), this._record.getColumn(), this._record.getXFIndex());
    }

    private void setCellType(int cellType, boolean setValue, int row, short col, short styleIndex) {
        NumberRecord nrec;
        LabelSSTRecord lrec;
        FormulaRecordAggregate frec;
        BlankRecord brec;
        BoolErrRecord boolRec;
        BoolErrRecord errRec;
        if (cellType > 5) {
            throw new RuntimeException("I have no idea what type that is!");
        }
        switch (cellType) {
            case 0:
                if (cellType != this._cellType) {
                    nrec = new NumberRecord();
                } else {
                    nrec = (NumberRecord) this._record;
                }
                nrec.setColumn(col);
                if (setValue) {
                    nrec.setValue(getNumericCellValue());
                }
                nrec.setXFIndex(styleIndex);
                nrec.setRow(row);
                this._record = nrec;
                break;
            case 1:
                if (cellType == this._cellType) {
                    lrec = (LabelSSTRecord) this._record;
                } else {
                    lrec = new LabelSSTRecord();
                    lrec.setColumn(col);
                    lrec.setRow(row);
                    lrec.setXFIndex(styleIndex);
                }
                if (setValue) {
                    int sstIndex = this._book.getWorkbook().addSSTString(new UnicodeString(convertCellValueToString()));
                    lrec.setSSTIndex(sstIndex);
                    UnicodeString us = this._book.getWorkbook().getSSTString(sstIndex);
                    this._stringValue = new HSSFRichTextString();
                    this._stringValue.setUnicodeString(us);
                }
                this._record = lrec;
                break;
            case 2:
                if (cellType != this._cellType) {
                    frec = this._sheet.getSheet().getRowsAggregate().createFormula(row, col);
                } else {
                    frec = (FormulaRecordAggregate) this._record;
                    frec.setRow(row);
                    frec.setColumn(col);
                }
                if (setValue) {
                    frec.getFormulaRecord().setValue(getNumericCellValue());
                }
                frec.setXFIndex(styleIndex);
                this._record = frec;
                break;
            case 3:
                if (cellType != this._cellType) {
                    brec = new BlankRecord();
                } else {
                    brec = (BlankRecord) this._record;
                }
                brec.setColumn(col);
                brec.setXFIndex(styleIndex);
                brec.setRow(row);
                this._record = brec;
                break;
            case 4:
                if (cellType != this._cellType) {
                    boolRec = new BoolErrRecord();
                } else {
                    boolRec = (BoolErrRecord) this._record;
                }
                boolRec.setColumn(col);
                if (setValue) {
                    boolRec.setValue(convertCellValueToBoolean());
                }
                boolRec.setXFIndex(styleIndex);
                boolRec.setRow(row);
                this._record = boolRec;
                break;
            case 5:
                if (cellType != this._cellType) {
                    errRec = new BoolErrRecord();
                } else {
                    errRec = (BoolErrRecord) this._record;
                }
                errRec.setColumn(col);
                if (setValue) {
                    errRec.setValue((byte) IntersectionPtg.sid);
                }
                errRec.setXFIndex(styleIndex);
                errRec.setRow(row);
                this._record = errRec;
                break;
        }
        if (!(cellType == this._cellType || this._cellType == -1)) {
            this._sheet.getSheet().replaceValueRecord(this._record);
        }
        this._cellType = cellType;
    }

    public int getCellType() {
        return this._cellType;
    }

    public void setCellValue(double value) {
        if (Double.isInfinite(value)) {
            setCellErrorValue(FormulaError.DIV0.getCode());
        } else if (Double.isNaN(value)) {
            setCellErrorValue(FormulaError.NUM.getCode());
        } else {
            int row = this._record.getRow();
            short col = this._record.getColumn();
            short styleIndex = this._record.getXFIndex();
            int i = this._cellType;
            if (i != 0) {
                if (i != 2) {
                    setCellType(0, false, row, col, styleIndex);
                } else {
                    ((FormulaRecordAggregate) this._record).setCachedDoubleResult(value);
                    return;
                }
            }
            ((NumberRecord) this._record).setValue(value);
        }
    }

    public void setCellValue(Date value) {
        setCellValue(HSSFDateUtil.getExcelDate(value, this._book.getWorkbook().isUsing1904DateWindowing()));
    }

    public void setCellValue(Calendar value) {
        setCellValue(HSSFDateUtil.getExcelDate(value, this._book.getWorkbook().isUsing1904DateWindowing()));
    }

    public void setCellValue(String value) {
        setCellValue((RichTextString) value == null ? null : new HSSFRichTextString(value));
    }

    public void setCellValue(RichTextString value) {
        HSSFRichTextString hvalue = (HSSFRichTextString) value;
        int row = this._record.getRow();
        short col = this._record.getColumn();
        short styleIndex = this._record.getXFIndex();
        if (hvalue == null) {
            notifyFormulaChanging();
            setCellType(3, false, row, col, styleIndex);
        } else if (hvalue.length() > SpreadsheetVersion.EXCEL97.getMaxTextLength()) {
            throw new IllegalArgumentException("The maximum length of cell contents (text) is 32,767 characters");
        } else if (this._cellType == 2) {
            ((FormulaRecordAggregate) this._record).setCachedStringResult(hvalue.getString());
            this._stringValue = new HSSFRichTextString(value.getString());
        } else {
            if (this._cellType != 1) {
                setCellType(1, false, row, col, styleIndex);
            }
            int index = this._book.getWorkbook().addSSTString(hvalue.getUnicodeString());
            ((LabelSSTRecord) this._record).setSSTIndex(index);
            this._stringValue = hvalue;
            this._stringValue.setWorkbookReferences(this._book.getWorkbook(), (LabelSSTRecord) this._record);
            this._stringValue.setUnicodeString(this._book.getWorkbook().getSSTString(index));
        }
    }

    public void setCellFormula(String formula) {
        if (isPartOfArrayFormulaGroup()) {
            notifyArrayFormulaChanging();
        }
        int row = this._record.getRow();
        short col = this._record.getColumn();
        short styleIndex = this._record.getXFIndex();
        if (formula == null) {
            notifyFormulaChanging();
            setCellType(3, false, row, col, styleIndex);
            return;
        }
        Ptg[] ptgs = HSSFFormulaParser.parse(formula, this._book, 0, this._book.getSheetIndex((Sheet) this._sheet));
        setCellType(2, false, row, col, styleIndex);
        FormulaRecordAggregate agg = (FormulaRecordAggregate) this._record;
        FormulaRecord frec = agg.getFormulaRecord();
        frec.setOptions(2);
        frec.setValue(0.0d);
        if (agg.getXFIndex() == 0) {
            agg.setXFIndex(15);
        }
        agg.setParsedExpression(ptgs);
    }

    private void notifyFormulaChanging() {
        if (this._record instanceof FormulaRecordAggregate) {
            ((FormulaRecordAggregate) this._record).notifyFormulaChanging();
        }
    }

    public String getCellFormula() {
        if (this._record instanceof FormulaRecordAggregate) {
            return HSSFFormulaParser.toFormulaString(this._book, ((FormulaRecordAggregate) this._record).getFormulaTokens());
        }
        throw typeMismatch(2, this._cellType, true);
    }

    private static String getCellTypeName(int cellTypeCode) {
        switch (cellTypeCode) {
            case 0:
                return "numeric";
            case 1:
                return "text";
            case 2:
                return "formula";
            case 3:
                return "blank";
            case 4:
                return "boolean";
            case 5:
                return "error";
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("#unknown cell type (");
                sb.append(cellTypeCode);
                sb.append(")#");
                return sb.toString();
        }
    }

    private static RuntimeException typeMismatch(int expectedTypeCode, int actualTypeCode, boolean isFormulaCell) {
        StringBuilder sb = new StringBuilder();
        sb.append("Cannot get a ");
        sb.append(getCellTypeName(expectedTypeCode));
        sb.append(" value from a ");
        sb.append(getCellTypeName(actualTypeCode));
        sb.append(" ");
        sb.append(isFormulaCell ? "formula " : "");
        sb.append("cell");
        return new IllegalStateException(sb.toString());
    }

    private static void checkFormulaCachedValueType(int expectedTypeCode, FormulaRecord fr) {
        int cachedValueType = fr.getCachedResultType();
        if (cachedValueType != expectedTypeCode) {
            throw typeMismatch(expectedTypeCode, cachedValueType, true);
        }
    }

    public double getNumericCellValue() {
        int i = this._cellType;
        if (i == 0) {
            return ((NumberRecord) this._record).getValue();
        }
        switch (i) {
            case 2:
                FormulaRecord fr = ((FormulaRecordAggregate) this._record).getFormulaRecord();
                checkFormulaCachedValueType(0, fr);
                return fr.getValue();
            case 3:
                return 0.0d;
            default:
                throw typeMismatch(0, this._cellType, false);
        }
    }

    public Date getDateCellValue() {
        if (this._cellType == 3) {
            return null;
        }
        double value = getNumericCellValue();
        if (this._book.getWorkbook().isUsing1904DateWindowing()) {
            return HSSFDateUtil.getJavaDate(value, true);
        }
        return HSSFDateUtil.getJavaDate(value, false);
    }

    public String getStringCellValue() {
        return getRichStringCellValue().getString();
    }

    public HSSFRichTextString getRichStringCellValue() {
        switch (this._cellType) {
            case 1:
                return this._stringValue;
            case 2:
                FormulaRecordAggregate fra = (FormulaRecordAggregate) this._record;
                checkFormulaCachedValueType(1, fra.getFormulaRecord());
                String strVal = fra.getStringValue();
                return new HSSFRichTextString(strVal == null ? "" : strVal);
            case 3:
                return new HSSFRichTextString("");
            default:
                throw typeMismatch(1, this._cellType, false);
        }
    }

    public void setCellValue(boolean value) {
        int row = this._record.getRow();
        short col = this._record.getColumn();
        short styleIndex = this._record.getXFIndex();
        int i = this._cellType;
        if (i != 2) {
            if (i != 4) {
                setCellType(4, false, row, col, styleIndex);
            }
            ((BoolErrRecord) this._record).setValue(value);
            return;
        }
        ((FormulaRecordAggregate) this._record).setCachedBooleanResult(value);
    }

    public void setCellErrorValue(byte errorCode) {
        int row = this._record.getRow();
        short col = this._record.getColumn();
        short styleIndex = this._record.getXFIndex();
        int i = this._cellType;
        if (i != 2) {
            if (i != 5) {
                setCellType(5, false, row, col, styleIndex);
            }
            ((BoolErrRecord) this._record).setValue(errorCode);
            return;
        }
        ((FormulaRecordAggregate) this._record).setCachedErrorResult(errorCode);
    }

    private boolean convertCellValueToBoolean() {
        boolean z = false;
        switch (this._cellType) {
            case 0:
                if (((NumberRecord) this._record).getValue() != 0.0d) {
                    z = true;
                }
                return z;
            case 1:
                return Boolean.valueOf(this._book.getWorkbook().getSSTString(((LabelSSTRecord) this._record).getSSTIndex()).getString()).booleanValue();
            case 2:
                FormulaRecord fr = ((FormulaRecordAggregate) this._record).getFormulaRecord();
                checkFormulaCachedValueType(4, fr);
                return fr.getCachedBooleanValue();
            case 3:
            case 5:
                return false;
            case 4:
                return ((BoolErrRecord) this._record).getBooleanValue();
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unexpected cell type (");
                sb.append(this._cellType);
                sb.append(")");
                throw new RuntimeException(sb.toString());
        }
    }

    private String convertCellValueToString() {
        switch (this._cellType) {
            case 0:
                return NumberToTextConverter.toText(((NumberRecord) this._record).getValue());
            case 1:
                return this._book.getWorkbook().getSSTString(((LabelSSTRecord) this._record).getSSTIndex()).getString();
            case 2:
                FormulaRecordAggregate fra = (FormulaRecordAggregate) this._record;
                FormulaRecord fr = fra.getFormulaRecord();
                switch (fr.getCachedResultType()) {
                    case 0:
                        return NumberToTextConverter.toText(fr.getValue());
                    case 1:
                        return fra.getStringValue();
                    case 4:
                        return fr.getCachedBooleanValue() ? "TRUE" : "FALSE";
                    case 5:
                        return HSSFErrorConstants.getText(fr.getCachedErrorValue());
                    default:
                        StringBuilder sb = new StringBuilder();
                        sb.append("Unexpected formula result type (");
                        sb.append(this._cellType);
                        sb.append(")");
                        throw new IllegalStateException(sb.toString());
                }
            case 3:
                return "";
            case 4:
                return ((BoolErrRecord) this._record).getBooleanValue() ? "TRUE" : "FALSE";
            case 5:
                return HSSFErrorConstants.getText(((BoolErrRecord) this._record).getErrorValue());
            default:
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Unexpected cell type (");
                sb2.append(this._cellType);
                sb2.append(")");
                throw new IllegalStateException(sb2.toString());
        }
    }

    public boolean getBooleanCellValue() {
        switch (this._cellType) {
            case 2:
                FormulaRecord fr = ((FormulaRecordAggregate) this._record).getFormulaRecord();
                checkFormulaCachedValueType(4, fr);
                return fr.getCachedBooleanValue();
            case 3:
                return false;
            case 4:
                return ((BoolErrRecord) this._record).getBooleanValue();
            default:
                throw typeMismatch(4, this._cellType, false);
        }
    }

    public byte getErrorCellValue() {
        int i = this._cellType;
        if (i == 2) {
            FormulaRecord fr = ((FormulaRecordAggregate) this._record).getFormulaRecord();
            checkFormulaCachedValueType(5, fr);
            return (byte) fr.getCachedErrorValue();
        } else if (i == 5) {
            return ((BoolErrRecord) this._record).getErrorValue();
        } else {
            throw typeMismatch(5, this._cellType, false);
        }
    }

    public void setCellStyle(CellStyle style) {
        setCellStyle((HSSFCellStyle) style);
    }

    public void setCellStyle(HSSFCellStyle style) {
        style.verifyBelongsToWorkbook(this._book);
        this._record.setXFIndex(style.getIndex());
    }

    public HSSFCellStyle getCellStyle() {
        short styleIndex = this._record.getXFIndex();
        return new HSSFCellStyle(styleIndex, this._book.getWorkbook().getExFormatAt(styleIndex), this._book);
    }

    /* access modifiers changed from: protected */
    public CellValueRecordInterface getCellValueRecord() {
        return this._record;
    }

    private static void checkBounds(int cellIndex) {
        if (cellIndex < 0 || cellIndex > LAST_COLUMN_NUMBER) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid column index (");
            sb.append(cellIndex);
            sb.append(").  Allowable column range for ");
            sb.append(FILE_FORMAT_NAME);
            sb.append(" is (0..");
            sb.append(LAST_COLUMN_NUMBER);
            sb.append(") or ('A'..'");
            sb.append(LAST_COLUMN_NAME);
            sb.append("')");
            throw new IllegalArgumentException(sb.toString());
        }
    }

    public void setAsActiveCell() {
        int row = this._record.getRow();
        short col = this._record.getColumn();
        this._sheet.getSheet().setActiveCellRow(row);
        this._sheet.getSheet().setActiveCellCol(col);
    }

    public String toString() {
        switch (getCellType()) {
            case 0:
                if (HSSFDateUtil.isCellDateFormatted(this)) {
                    return new SimpleDateFormat("dd-MMM-yyyy").format(getDateCellValue());
                }
                return String.valueOf(getNumericCellValue());
            case 1:
                return getStringCellValue();
            case 2:
                return getCellFormula();
            case 3:
                return "";
            case 4:
                return getBooleanCellValue() ? "TRUE" : "FALSE";
            case 5:
                return ErrorEval.getText(((BoolErrRecord) this._record).getErrorValue());
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown Cell Type: ");
                sb.append(getCellType());
                return sb.toString();
        }
    }

    public void setCellComment(Comment comment) {
        if (comment == null) {
            removeCellComment();
            return;
        }
        comment.setRow(this._record.getRow());
        comment.setColumn(this._record.getColumn());
        this._comment = (HSSFComment) comment;
    }

    public HSSFComment getCellComment() {
        if (this._comment == null) {
            this._comment = findCellComment(this._sheet.getSheet(), this._record.getRow(), this._record.getColumn());
        }
        return this._comment;
    }

    public void removeCellComment() {
        HSSFComment comment = findCellComment(this._sheet.getSheet(), this._record.getRow(), this._record.getColumn());
        this._comment = null;
        if (comment != null) {
            List<RecordBase> sheetRecords = this._sheet.getSheet().getRecords();
            sheetRecords.remove(comment.getNoteRecord());
            if (comment.getTextObjectRecord() != null) {
                TextObjectRecord txo = comment.getTextObjectRecord();
                int txoAt = sheetRecords.indexOf(txo);
                if (!(sheetRecords.get(txoAt - 3) instanceof DrawingRecord) || !(sheetRecords.get(txoAt - 2) instanceof ObjRecord) || !(sheetRecords.get(txoAt - 1) instanceof DrawingRecord)) {
                    throw new IllegalStateException("Found the wrong records before the TextObjectRecord, can't remove comment");
                }
                sheetRecords.remove(txoAt - 1);
                sheetRecords.remove(txoAt - 2);
                sheetRecords.remove(txoAt - 3);
                sheetRecords.remove(txo);
            }
        }
    }

    protected static HSSFComment findCellComment(InternalSheet sheet, int row, int column) {
        HSSFComment comment = null;
        Map<Integer, TextObjectRecord> noteTxo = new HashMap<>();
        int i = 0;
        Iterator<RecordBase> it = sheet.getRecords().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            RecordBase rec = (RecordBase) it.next();
            boolean z = false;
            if (rec instanceof NoteRecord) {
                NoteRecord note = (NoteRecord) rec;
                if (note.getRow() != row || note.getColumn() != column) {
                    i++;
                } else if (i < noteTxo.size()) {
                    TextObjectRecord txo = (TextObjectRecord) noteTxo.get(Integer.valueOf(note.getShapeId()));
                    if (txo != null) {
                        comment = new HSSFComment(note, txo);
                        comment.setRow(note.getRow());
                        comment.setColumn(note.getColumn());
                        comment.setAuthor(note.getAuthor());
                        if (note.getFlags() == 2) {
                            z = true;
                        }
                        comment.setVisible(z);
                        comment.setString(txo.getStr());
                    } else {
                        POILogger pOILogger = log;
                        int i2 = POILogger.WARN;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Failed to match NoteRecord and TextObjectRecord, row: ");
                        sb.append(row);
                        sb.append(", column: ");
                        sb.append(column);
                        pOILogger.log(i2, (Object) sb.toString());
                    }
                } else {
                    POILogger pOILogger2 = log;
                    int i3 = POILogger.WARN;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Failed to match NoteRecord and TextObjectRecord, row: ");
                    sb2.append(row);
                    sb2.append(", column: ");
                    sb2.append(column);
                    pOILogger2.log(i3, (Object) sb2.toString());
                }
            } else if (rec instanceof ObjRecord) {
                SubRecord sub = (SubRecord) ((ObjRecord) rec).getSubRecords().get(0);
                if (sub instanceof CommonObjectDataSubRecord) {
                    CommonObjectDataSubRecord cmo = (CommonObjectDataSubRecord) sub;
                    if (cmo.getObjectType() == 25) {
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            RecordBase rec2 = (RecordBase) it.next();
                            if (rec2 instanceof TextObjectRecord) {
                                noteTxo.put(Integer.valueOf(cmo.getObjectId()), (TextObjectRecord) rec2);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return comment;
    }

    public HSSFHyperlink getHyperlink() {
        for (RecordBase rec : this._sheet.getSheet().getRecords()) {
            if (rec instanceof HyperlinkRecord) {
                HyperlinkRecord link = (HyperlinkRecord) rec;
                if (link.getFirstColumn() == this._record.getColumn() && link.getFirstRow() == this._record.getRow()) {
                    return new HSSFHyperlink(link);
                }
            }
        }
        return null;
    }

    public void setHyperlink(Hyperlink hyperlink) {
        HSSFHyperlink link = (HSSFHyperlink) hyperlink;
        link.setFirstRow(this._record.getRow());
        link.setLastRow(this._record.getRow());
        link.setFirstColumn(this._record.getColumn());
        link.setLastColumn(this._record.getColumn());
        switch (link.getType()) {
            case 1:
            case 3:
                link.setLabel("url");
                break;
            case 2:
                link.setLabel("place");
                break;
            case 4:
                link.setLabel("file");
                break;
        }
        List<RecordBase> records = this._sheet.getSheet().getRecords();
        records.add(records.size() - 1, link.record);
    }

    public int getCachedFormulaResultType() {
        if (this._cellType == 2) {
            return ((FormulaRecordAggregate) this._record).getFormulaRecord().getCachedResultType();
        }
        throw new IllegalStateException("Only formula cells have cached results");
    }

    /* access modifiers changed from: 0000 */
    public void setCellArrayFormula(CellRangeAddress range) {
        setCellType(2, false, this._record.getRow(), this._record.getColumn(), this._record.getXFIndex());
        ((FormulaRecordAggregate) this._record).setParsedExpression(new Ptg[]{new ExpPtg(range.getFirstRow(), range.getFirstColumn())});
    }

    public CellRangeAddress getArrayFormulaRange() {
        if (this._cellType == 2) {
            return ((FormulaRecordAggregate) this._record).getArrayFormulaRange();
        }
        String ref = new CellReference((Cell) this).formatAsString();
        StringBuilder sb = new StringBuilder();
        sb.append("Cell ");
        sb.append(ref);
        sb.append(" is not part of an array formula.");
        throw new IllegalStateException(sb.toString());
    }

    public boolean isPartOfArrayFormulaGroup() {
        if (this._cellType != 2) {
            return false;
        }
        return ((FormulaRecordAggregate) this._record).isPartOfArrayFormula();
    }

    /* access modifiers changed from: 0000 */
    public void notifyArrayFormulaChanging(String msg) {
        if (getArrayFormulaRange().getNumberOfCells() > 1) {
            throw new IllegalStateException(msg);
        }
        getRow().getSheet().removeArrayFormula(this);
    }

    /* access modifiers changed from: 0000 */
    public void notifyArrayFormulaChanging() {
        CellReference ref = new CellReference((Cell) this);
        StringBuilder sb = new StringBuilder();
        sb.append("Cell ");
        sb.append(ref.formatAsString());
        sb.append(" is part of a multi-cell array formula. ");
        sb.append("You cannot change part of an array.");
        notifyArrayFormulaChanging(sb.toString());
    }
}

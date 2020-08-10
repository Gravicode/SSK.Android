package org.apache.poi.hssf.usermodel;

import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.io.PrintWriter;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import org.apache.poi.ddf.EscherRecord;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.model.InternalSheet;
import org.apache.poi.hssf.model.InternalWorkbook;
import org.apache.poi.hssf.record.AutoFilterInfoRecord;
import org.apache.poi.hssf.record.CellValueRecordInterface;
import org.apache.poi.hssf.record.EscherAggregate;
import org.apache.poi.hssf.record.NameRecord;
import org.apache.poi.hssf.record.NoteRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RowRecord;
import org.apache.poi.hssf.record.SCLRecord;
import org.apache.poi.hssf.record.WSBoolRecord;
import org.apache.poi.hssf.record.aggregates.FormulaRecordAggregate;
import org.apache.poi.hssf.record.aggregates.WorksheetProtectionBlock;
import org.apache.poi.hssf.record.formula.Area3DPtg;
import org.apache.poi.hssf.record.formula.FormulaShifter;
import org.apache.poi.hssf.record.formula.Ptg;
import org.apache.poi.hssf.util.PaneInformation;
import org.apache.poi.p009ss.SpreadsheetVersion;
import org.apache.poi.p009ss.usermodel.Cell;
import org.apache.poi.p009ss.usermodel.CellRange;
import org.apache.poi.p009ss.usermodel.CellStyle;
import org.apache.poi.p009ss.usermodel.DataValidation;
import org.apache.poi.p009ss.usermodel.DataValidationHelper;
import org.apache.poi.p009ss.usermodel.Row;
import org.apache.poi.p009ss.usermodel.Sheet;
import org.apache.poi.p009ss.util.CellRangeAddress;
import org.apache.poi.p009ss.util.CellReference;
import org.apache.poi.p009ss.util.Region;
import org.apache.poi.p009ss.util.SSCellRange;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public final class HSSFSheet implements Sheet {
    private static final int DEBUG = POILogger.DEBUG;
    public static final int INITIAL_CAPACITY = 20;
    private static final POILogger log = POILogFactory.getLogger(HSSFSheet.class);
    protected final InternalWorkbook _book;
    private int _firstrow;
    private int _lastrow;
    private HSSFPatriarch _patriarch;
    private final TreeMap<Integer, HSSFRow> _rows;
    private final InternalSheet _sheet;
    protected final HSSFWorkbook _workbook;

    protected HSSFSheet(HSSFWorkbook workbook) {
        this._sheet = InternalSheet.createSheet();
        this._rows = new TreeMap<>();
        this._workbook = workbook;
        this._book = workbook.getWorkbook();
    }

    protected HSSFSheet(HSSFWorkbook workbook, InternalSheet sheet) {
        this._sheet = sheet;
        this._rows = new TreeMap<>();
        this._workbook = workbook;
        this._book = workbook.getWorkbook();
        setPropertiesFromSheet(sheet);
    }

    /* access modifiers changed from: 0000 */
    public HSSFSheet cloneSheet(HSSFWorkbook workbook) {
        return new HSSFSheet(workbook, this._sheet.cloneSheet());
    }

    public HSSFWorkbook getWorkbook() {
        return this._workbook;
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0082  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00b2  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00ca  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void setPropertiesFromSheet(org.apache.poi.hssf.model.InternalSheet r21) {
        /*
            r20 = this;
            r0 = r20
            org.apache.poi.hssf.record.RowRecord r1 = r21.getNextRow()
            r2 = 0
            if (r1 == 0) goto L_0x000b
            r3 = 1
            goto L_0x000c
        L_0x000b:
            r3 = 0
        L_0x000c:
            if (r1 == 0) goto L_0x0016
            r0.createRowFromRecord(r1)
            org.apache.poi.hssf.record.RowRecord r1 = r21.getNextRow()
            goto L_0x000c
        L_0x0016:
            org.apache.poi.hssf.record.CellValueRecordInterface[] r4 = r21.getValueRecords()
            long r5 = java.lang.System.currentTimeMillis()
            org.apache.poi.util.POILogger r7 = log
            int r8 = org.apache.poi.util.POILogger.DEBUG
            boolean r7 = r7.check(r8)
            if (r7 == 0) goto L_0x0035
            org.apache.poi.util.POILogger r7 = log
            int r8 = DEBUG
            java.lang.String r9 = "Time at start of cell creating in HSSF sheet = "
            java.lang.Long r10 = java.lang.Long.valueOf(r5)
            r7.log(r8, r9, r10)
        L_0x0035:
            r7 = 0
        L_0x0037:
            int r8 = r4.length
            if (r2 >= r8) goto L_0x00d8
            r8 = r4[r2]
            long r9 = java.lang.System.currentTimeMillis()
            r11 = r7
            if (r11 == 0) goto L_0x0051
            int r12 = r11.getRowNum()
            int r13 = r8.getRow()
            if (r12 == r13) goto L_0x004e
            goto L_0x0051
        L_0x004e:
            r13 = r21
            goto L_0x0078
        L_0x0051:
            int r12 = r8.getRow()
            org.apache.poi.hssf.usermodel.HSSFRow r11 = r0.getRow(r12)
            r7 = r11
            if (r11 != 0) goto L_0x004e
            if (r3 == 0) goto L_0x0066
            java.lang.RuntimeException r12 = new java.lang.RuntimeException
            java.lang.String r13 = "Unexpected missing row when some rows already present"
            r12.<init>(r13)
            throw r12
        L_0x0066:
            org.apache.poi.hssf.record.RowRecord r12 = new org.apache.poi.hssf.record.RowRecord
            int r13 = r8.getRow()
            r12.<init>(r13)
            r13 = r21
            r13.addRow(r12)
            org.apache.poi.hssf.usermodel.HSSFRow r11 = r0.createRowFromRecord(r12)
        L_0x0078:
            org.apache.poi.util.POILogger r12 = log
            int r14 = org.apache.poi.util.POILogger.DEBUG
            boolean r12 = r12.check(r14)
            if (r12 == 0) goto L_0x00a5
            org.apache.poi.util.POILogger r12 = log
            int r14 = DEBUG
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            java.lang.String r0 = "record id = "
            r15.append(r0)
            r0 = r8
            org.apache.poi.hssf.record.Record r0 = (org.apache.poi.hssf.record.Record) r0
            short r0 = r0.getSid()
            java.lang.String r0 = java.lang.Integer.toHexString(r0)
            r15.append(r0)
            java.lang.String r0 = r15.toString()
            r12.log(r14, r0)
        L_0x00a5:
            r11.createCellFromRecord(r8)
            org.apache.poi.util.POILogger r0 = log
            int r12 = org.apache.poi.util.POILogger.DEBUG
            boolean r0 = r0.check(r12)
            if (r0 == 0) goto L_0x00ca
            org.apache.poi.util.POILogger r0 = log
            int r12 = DEBUG
            java.lang.String r14 = "record took "
            long r16 = java.lang.System.currentTimeMillis()
            r18 = r3
            r19 = r4
            long r3 = r16 - r9
            java.lang.Long r3 = java.lang.Long.valueOf(r3)
            r0.log(r12, r14, r3)
            goto L_0x00ce
        L_0x00ca:
            r18 = r3
            r19 = r4
        L_0x00ce:
            int r2 = r2 + 1
            r3 = r18
            r4 = r19
            r0 = r20
            goto L_0x0037
        L_0x00d8:
            r13 = r21
            r18 = r3
            r19 = r4
            org.apache.poi.util.POILogger r0 = log
            int r2 = org.apache.poi.util.POILogger.DEBUG
            boolean r0 = r0.check(r2)
            if (r0 == 0) goto L_0x00fa
            org.apache.poi.util.POILogger r0 = log
            int r2 = DEBUG
            java.lang.String r3 = "total sheet cell creation took "
            long r8 = java.lang.System.currentTimeMillis()
            long r8 = r8 - r5
            java.lang.Long r4 = java.lang.Long.valueOf(r8)
            r0.log(r2, r3, r4)
        L_0x00fa:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.hssf.usermodel.HSSFSheet.setPropertiesFromSheet(org.apache.poi.hssf.model.InternalSheet):void");
    }

    public HSSFRow createRow(int rownum) {
        HSSFRow row = new HSSFRow(this._workbook, this, rownum);
        addRow(row, true);
        return row;
    }

    private HSSFRow createRowFromRecord(RowRecord row) {
        HSSFRow hrow = new HSSFRow(this._workbook, this, row);
        addRow(hrow, false);
        return hrow;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=org.apache.poi.ss.usermodel.Row, code=org.apache.poi.ss.usermodel.Row<org.apache.poi.ss.usermodel.Cell>, for r7v0, types: [org.apache.poi.ss.usermodel.Row<org.apache.poi.ss.usermodel.Cell>, org.apache.poi.ss.usermodel.Row] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void removeRow(org.apache.poi.p009ss.usermodel.Row<org.apache.poi.p009ss.usermodel.Cell> r7) {
        /*
            r6 = this;
            r0 = r7
            org.apache.poi.hssf.usermodel.HSSFRow r0 = (org.apache.poi.hssf.usermodel.HSSFRow) r0
            org.apache.poi.ss.usermodel.Sheet r1 = r7.getSheet()
            if (r1 == r6) goto L_0x0011
            java.lang.IllegalArgumentException r1 = new java.lang.IllegalArgumentException
            java.lang.String r2 = "Specified row does not belong to this sheet"
            r1.<init>(r2)
            throw r1
        L_0x0011:
            java.util.Iterator r1 = r7.iterator()
        L_0x0015:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x0048
            java.lang.Object r2 = r1.next()
            org.apache.poi.ss.usermodel.Cell r2 = (org.apache.poi.p009ss.usermodel.Cell) r2
            r3 = r2
            org.apache.poi.hssf.usermodel.HSSFCell r3 = (org.apache.poi.hssf.usermodel.HSSFCell) r3
            boolean r4 = r3.isPartOfArrayFormulaGroup()
            if (r4 == 0) goto L_0x0047
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "Row[rownum="
            r4.append(r5)
            int r5 = r7.getRowNum()
            r4.append(r5)
            java.lang.String r5 = "] contains cell(s) included in a multi-cell array formula. You cannot change part of an array."
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            r3.notifyArrayFormulaChanging(r4)
        L_0x0047:
            goto L_0x0015
        L_0x0048:
            java.util.TreeMap<java.lang.Integer, org.apache.poi.hssf.usermodel.HSSFRow> r1 = r6._rows
            int r1 = r1.size()
            if (r1 <= 0) goto L_0x0097
            int r1 = r7.getRowNum()
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)
            java.util.TreeMap<java.lang.Integer, org.apache.poi.hssf.usermodel.HSSFRow> r2 = r6._rows
            java.lang.Object r2 = r2.remove(r1)
            org.apache.poi.hssf.usermodel.HSSFRow r2 = (org.apache.poi.hssf.usermodel.HSSFRow) r2
            if (r2 == r7) goto L_0x006a
            java.lang.IllegalArgumentException r3 = new java.lang.IllegalArgumentException
            java.lang.String r4 = "Specified row does not belong to this sheet"
            r3.<init>(r4)
            throw r3
        L_0x006a:
            int r3 = r0.getRowNum()
            int r4 = r6.getLastRowNum()
            if (r3 != r4) goto L_0x007c
            int r3 = r6._lastrow
            int r3 = r6.findLastRow(r3)
            r6._lastrow = r3
        L_0x007c:
            int r3 = r0.getRowNum()
            int r4 = r6.getFirstRowNum()
            if (r3 != r4) goto L_0x008e
            int r3 = r6._firstrow
            int r3 = r6.findFirstRow(r3)
            r6._firstrow = r3
        L_0x008e:
            org.apache.poi.hssf.model.InternalSheet r3 = r6._sheet
            org.apache.poi.hssf.record.RowRecord r4 = r0.getRowRecord()
            r3.removeRow(r4)
        L_0x0097:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.hssf.usermodel.HSSFSheet.removeRow(org.apache.poi.ss.usermodel.Row):void");
    }

    private int findLastRow(int lastrow) {
        if (lastrow < 1) {
            return 0;
        }
        int rownum = lastrow - 1;
        HSSFRow r = getRow(rownum);
        while (r == null && rownum > 0) {
            rownum--;
            r = getRow(rownum);
        }
        if (r == null) {
            return 0;
        }
        return rownum;
    }

    private int findFirstRow(int firstrow) {
        int rownum = firstrow + 1;
        HSSFRow r = getRow(rownum);
        while (r == null && rownum <= getLastRowNum()) {
            rownum++;
            r = getRow(rownum);
        }
        if (rownum > getLastRowNum()) {
            return 0;
        }
        return rownum;
    }

    private void addRow(HSSFRow row, boolean addLow) {
        this._rows.put(Integer.valueOf(row.getRowNum()), row);
        if (addLow) {
            this._sheet.addRow(row.getRowRecord());
        }
        boolean z = true;
        if (this._rows.size() != 1) {
            z = false;
        }
        boolean firstRow = z;
        if (row.getRowNum() > getLastRowNum() || firstRow) {
            this._lastrow = row.getRowNum();
        }
        if (row.getRowNum() < getFirstRowNum() || firstRow) {
            this._firstrow = row.getRowNum();
        }
    }

    public HSSFRow getRow(int rowIndex) {
        return (HSSFRow) this._rows.get(Integer.valueOf(rowIndex));
    }

    public int getPhysicalNumberOfRows() {
        return this._rows.size();
    }

    public int getFirstRowNum() {
        return this._firstrow;
    }

    public int getLastRowNum() {
        return this._lastrow;
    }

    public void addValidationData(DataValidation dataValidation) {
        if (dataValidation == null) {
            throw new IllegalArgumentException("objValidation must not be null");
        }
        this._sheet.getOrCreateDataValidityTable().addDataValidation(((HSSFDataValidation) dataValidation).createDVRecord(this));
    }

    public void setColumnHidden(short columnIndex, boolean hidden) {
        setColumnHidden((int) 65535 & columnIndex, hidden);
    }

    public boolean isColumnHidden(short columnIndex) {
        return isColumnHidden((int) 65535 & columnIndex);
    }

    public void setColumnWidth(short columnIndex, short width) {
        setColumnWidth((int) columnIndex & 65535, (int) 65535 & width);
    }

    public short getColumnWidth(short columnIndex) {
        return (short) getColumnWidth((int) 65535 & columnIndex);
    }

    public void setDefaultColumnWidth(short width) {
        setDefaultColumnWidth((int) 65535 & width);
    }

    public void setColumnHidden(int columnIndex, boolean hidden) {
        this._sheet.setColumnHidden(columnIndex, hidden);
    }

    public boolean isColumnHidden(int columnIndex) {
        return this._sheet.isColumnHidden(columnIndex);
    }

    public void setColumnWidth(int columnIndex, int width) {
        this._sheet.setColumnWidth(columnIndex, width);
    }

    public int getColumnWidth(int columnIndex) {
        return this._sheet.getColumnWidth(columnIndex);
    }

    public int getDefaultColumnWidth() {
        return this._sheet.getDefaultColumnWidth();
    }

    public void setDefaultColumnWidth(int width) {
        this._sheet.setDefaultColumnWidth(width);
    }

    public short getDefaultRowHeight() {
        return this._sheet.getDefaultRowHeight();
    }

    public float getDefaultRowHeightInPoints() {
        return ((float) this._sheet.getDefaultRowHeight()) / 20.0f;
    }

    public void setDefaultRowHeight(short height) {
        this._sheet.setDefaultRowHeight(height);
    }

    public void setDefaultRowHeightInPoints(float height) {
        this._sheet.setDefaultRowHeight((short) ((int) (20.0f * height)));
    }

    public HSSFCellStyle getColumnStyle(int column) {
        short styleIndex = this._sheet.getXFIndexForColAt((short) column);
        if (styleIndex == 15) {
            return null;
        }
        return new HSSFCellStyle(styleIndex, this._book.getExFormatAt(styleIndex), this._book);
    }

    public boolean isGridsPrinted() {
        return this._sheet.isGridsPrinted();
    }

    public void setGridsPrinted(boolean value) {
        this._sheet.setGridsPrinted(value);
    }

    public int addMergedRegion(Region region) {
        return this._sheet.addMergedRegion(region.getRowFrom(), region.getColumnFrom(), region.getRowTo(), region.getColumnTo());
    }

    public int addMergedRegion(CellRangeAddress region) {
        region.validate(SpreadsheetVersion.EXCEL97);
        validateArrayFormulas(region);
        return this._sheet.addMergedRegion(region.getFirstRow(), region.getFirstColumn(), region.getLastRow(), region.getLastColumn());
    }

    private void validateArrayFormulas(CellRangeAddress region) {
        int firstRow = region.getFirstRow();
        int firstColumn = region.getFirstColumn();
        int lastRow = region.getLastRow();
        int lastColumn = region.getLastColumn();
        for (int rowIn = firstRow; rowIn <= lastRow; rowIn++) {
            for (int colIn = firstColumn; colIn <= lastColumn; colIn++) {
                HSSFRow row = getRow(rowIn);
                if (row != null) {
                    HSSFCell cell = row.getCell(colIn);
                    if (cell != null && cell.isPartOfArrayFormulaGroup()) {
                        CellRangeAddress arrayRange = cell.getArrayFormulaRange();
                        if (arrayRange.getNumberOfCells() > 1 && (arrayRange.isInRange(region.getFirstRow(), region.getFirstColumn()) || arrayRange.isInRange(region.getFirstRow(), region.getFirstColumn()))) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("The range ");
                            sb.append(region.formatAsString());
                            sb.append(" intersects with a multi-cell array formula. ");
                            sb.append("You cannot merge cells of an array.");
                            throw new IllegalStateException(sb.toString());
                        }
                    }
                }
            }
        }
    }

    public void setForceFormulaRecalculation(boolean value) {
        this._sheet.setUncalced(value);
    }

    public boolean getForceFormulaRecalculation() {
        return this._sheet.getUncalced();
    }

    public void setVerticallyCenter(boolean value) {
        this._sheet.getPageSettings().getVCenter().setVCenter(value);
    }

    public boolean getVerticallyCenter(boolean value) {
        return getVerticallyCenter();
    }

    public boolean getVerticallyCenter() {
        return this._sheet.getPageSettings().getVCenter().getVCenter();
    }

    public void setHorizontallyCenter(boolean value) {
        this._sheet.getPageSettings().getHCenter().setHCenter(value);
    }

    public boolean getHorizontallyCenter() {
        return this._sheet.getPageSettings().getHCenter().getHCenter();
    }

    public void setRightToLeft(boolean value) {
        this._sheet.getWindowTwo().setArabic(value);
    }

    public boolean isRightToLeft() {
        return this._sheet.getWindowTwo().getArabic();
    }

    public void removeMergedRegion(int index) {
        this._sheet.removeMergedRegion(index);
    }

    public int getNumMergedRegions() {
        return this._sheet.getNumMergedRegions();
    }

    public org.apache.poi.hssf.util.Region getMergedRegionAt(int index) {
        CellRangeAddress cra = getMergedRegion(index);
        return new org.apache.poi.hssf.util.Region(cra.getFirstRow(), (short) cra.getFirstColumn(), cra.getLastRow(), (short) cra.getLastColumn());
    }

    public CellRangeAddress getMergedRegion(int index) {
        return this._sheet.getMergedRegionAt(index);
    }

    public Iterator<Row> rowIterator() {
        return this._rows.values().iterator();
    }

    public Iterator<Row> iterator() {
        return rowIterator();
    }

    /* access modifiers changed from: 0000 */
    public InternalSheet getSheet() {
        return this._sheet;
    }

    public void setAlternativeExpression(boolean b) {
        ((WSBoolRecord) this._sheet.findFirstRecordBySid(129)).setAlternateExpression(b);
    }

    public void setAlternativeFormula(boolean b) {
        ((WSBoolRecord) this._sheet.findFirstRecordBySid(129)).setAlternateFormula(b);
    }

    public void setAutobreaks(boolean b) {
        ((WSBoolRecord) this._sheet.findFirstRecordBySid(129)).setAutobreaks(b);
    }

    public void setDialog(boolean b) {
        ((WSBoolRecord) this._sheet.findFirstRecordBySid(129)).setDialog(b);
    }

    public void setDisplayGuts(boolean b) {
        ((WSBoolRecord) this._sheet.findFirstRecordBySid(129)).setDisplayGuts(b);
    }

    public void setFitToPage(boolean b) {
        ((WSBoolRecord) this._sheet.findFirstRecordBySid(129)).setFitToPage(b);
    }

    public void setRowSumsBelow(boolean b) {
        WSBoolRecord record = (WSBoolRecord) this._sheet.findFirstRecordBySid(129);
        record.setRowSumsBelow(b);
        record.setAlternateExpression(b);
    }

    public void setRowSumsRight(boolean b) {
        ((WSBoolRecord) this._sheet.findFirstRecordBySid(129)).setRowSumsRight(b);
    }

    public boolean getAlternateExpression() {
        return ((WSBoolRecord) this._sheet.findFirstRecordBySid(129)).getAlternateExpression();
    }

    public boolean getAlternateFormula() {
        return ((WSBoolRecord) this._sheet.findFirstRecordBySid(129)).getAlternateFormula();
    }

    public boolean getAutobreaks() {
        return ((WSBoolRecord) this._sheet.findFirstRecordBySid(129)).getAutobreaks();
    }

    public boolean getDialog() {
        return ((WSBoolRecord) this._sheet.findFirstRecordBySid(129)).getDialog();
    }

    public boolean getDisplayGuts() {
        return ((WSBoolRecord) this._sheet.findFirstRecordBySid(129)).getDisplayGuts();
    }

    public boolean isDisplayZeros() {
        return this._sheet.getWindowTwo().getDisplayZeros();
    }

    public void setDisplayZeros(boolean value) {
        this._sheet.getWindowTwo().setDisplayZeros(value);
    }

    public boolean getFitToPage() {
        return ((WSBoolRecord) this._sheet.findFirstRecordBySid(129)).getFitToPage();
    }

    public boolean getRowSumsBelow() {
        return ((WSBoolRecord) this._sheet.findFirstRecordBySid(129)).getRowSumsBelow();
    }

    public boolean getRowSumsRight() {
        return ((WSBoolRecord) this._sheet.findFirstRecordBySid(129)).getRowSumsRight();
    }

    public boolean isPrintGridlines() {
        return getSheet().getPrintGridlines().getPrintGridlines();
    }

    public void setPrintGridlines(boolean newPrintGridlines) {
        getSheet().getPrintGridlines().setPrintGridlines(newPrintGridlines);
    }

    public HSSFPrintSetup getPrintSetup() {
        return new HSSFPrintSetup(this._sheet.getPageSettings().getPrintSetup());
    }

    public HSSFHeader getHeader() {
        return new HSSFHeader(this._sheet.getPageSettings());
    }

    public HSSFFooter getFooter() {
        return new HSSFFooter(this._sheet.getPageSettings());
    }

    public boolean isSelected() {
        return getSheet().getWindowTwo().getSelected();
    }

    public void setSelected(boolean sel) {
        getSheet().getWindowTwo().setSelected(sel);
    }

    public boolean isActive() {
        return getSheet().getWindowTwo().isActive();
    }

    public void setActive(boolean sel) {
        getSheet().getWindowTwo().setActive(sel);
    }

    public double getMargin(short margin) {
        return this._sheet.getPageSettings().getMargin(margin);
    }

    public void setMargin(short margin, double size) {
        this._sheet.getPageSettings().setMargin(margin, size);
    }

    private WorksheetProtectionBlock getProtectionBlock() {
        return this._sheet.getProtectionBlock();
    }

    public boolean getProtect() {
        return getProtectionBlock().isSheetProtected();
    }

    public short getPassword() {
        return (short) getProtectionBlock().getPasswordHash();
    }

    public boolean getObjectProtect() {
        return getProtectionBlock().isObjectProtected();
    }

    public boolean getScenarioProtect() {
        return getProtectionBlock().isScenarioProtected();
    }

    public void protectSheet(String password) {
        getProtectionBlock().protectSheet(password, true, true);
    }

    public void setZoom(int numerator, int denominator) {
        if (numerator < 1 || numerator > 65535) {
            throw new IllegalArgumentException("Numerator must be greater than 1 and less than 65536");
        } else if (denominator < 1 || denominator > 65535) {
            throw new IllegalArgumentException("Denominator must be greater than 1 and less than 65536");
        } else {
            SCLRecord sclRecord = new SCLRecord();
            sclRecord.setNumerator((short) numerator);
            sclRecord.setDenominator((short) denominator);
            getSheet().setSCLRecord(sclRecord);
        }
    }

    public short getTopRow() {
        return this._sheet.getTopRow();
    }

    public short getLeftCol() {
        return this._sheet.getLeftCol();
    }

    public void showInPane(short toprow, short leftcol) {
        this._sheet.setTopRow(toprow);
        this._sheet.setLeftCol(leftcol);
    }

    /* access modifiers changed from: protected */
    public void shiftMerged(int startRow, int endRow, int n, boolean isRow) {
        List<CellRangeAddress> shiftedRegions = new ArrayList<>();
        int i = 0;
        while (i < getNumMergedRegions()) {
            CellRangeAddress merged = getMergedRegion(i);
            boolean inStart = merged.getFirstRow() >= startRow || merged.getLastRow() >= startRow;
            boolean inEnd = merged.getFirstRow() <= endRow || merged.getLastRow() <= endRow;
            if (inStart && inEnd && !containsCell(merged, startRow - 1, 0) && !containsCell(merged, endRow + 1, 0)) {
                merged.setFirstRow(merged.getFirstRow() + n);
                merged.setLastRow(merged.getLastRow() + n);
                shiftedRegions.add(merged);
                removeMergedRegion(i);
                i--;
            }
            i++;
        }
        for (CellRangeAddress region : shiftedRegions) {
            addMergedRegion(region);
        }
    }

    private static boolean containsCell(CellRangeAddress cr, int rowIx, int colIx) {
        if (cr.getFirstRow() > rowIx || cr.getLastRow() < rowIx || cr.getFirstColumn() > colIx || cr.getLastColumn() < colIx) {
            return false;
        }
        return true;
    }

    public void shiftRows(int startRow, int endRow, int n) {
        shiftRows(startRow, endRow, n, false, false);
    }

    public void shiftRows(int startRow, int endRow, int n, boolean copyRowHeight, boolean resetOriginalRowHeight) {
        shiftRows(startRow, endRow, n, copyRowHeight, resetOriginalRowHeight, true);
    }

    public void shiftRows(int startRow, int endRow, int n, boolean copyRowHeight, boolean resetOriginalRowHeight, boolean moveComments) {
        int inc;
        int s;
        NoteRecord[] noteRecs;
        int s2;
        int i = startRow;
        int i2 = endRow;
        int i3 = n;
        if (i3 < 0) {
            s = i;
            inc = 1;
        } else {
            s = i2;
            inc = -1;
        }
        if (moveComments) {
            noteRecs = this._sheet.getNoteRecords();
        } else {
            noteRecs = NoteRecord.EMPTY_ARRAY;
        }
        shiftMerged(i, i2, i3, true);
        this._sheet.getPageSettings().shiftRowBreaks(i, i2, i3);
        int rowNum = s;
        while (rowNum >= i && rowNum <= i2 && rowNum >= 0 && rowNum < 65536) {
            HSSFRow row = getRow(rowNum);
            if (row != null) {
                notifyRowShifting(row);
            }
            HSSFRow row2Replace = getRow(rowNum + i3);
            if (row2Replace == null) {
                row2Replace = createRow(rowNum + i3);
            }
            row2Replace.removeAllCells();
            if (row == null) {
                s2 = s;
            } else {
                if (copyRowHeight) {
                    row2Replace.setHeight(row.getHeight());
                }
                if (resetOriginalRowHeight) {
                    row.setHeight(255);
                }
                Iterator<Cell> cells = row.cellIterator();
                while (cells.hasNext()) {
                    HSSFCell cell = (HSSFCell) cells.next();
                    row.removeCell(cell);
                    CellValueRecordInterface cellRecord = cell.getCellValueRecord();
                    int s3 = s;
                    cellRecord.setRow(rowNum + i3);
                    row2Replace.createCellFromRecord(cellRecord);
                    HSSFRow row2Replace2 = row2Replace;
                    this._sheet.addValueRecord(rowNum + i3, cellRecord);
                    HSSFHyperlink link = cell.getHyperlink();
                    if (link != null) {
                        link.setFirstRow(link.getFirstRow() + i3);
                        link.setLastRow(link.getLastRow() + i3);
                    }
                    s = s3;
                    row2Replace = row2Replace2;
                }
                s2 = s;
                HSSFRow hSSFRow = row2Replace;
                row.removeAllCells();
                if (moveComments) {
                    for (int i4 = noteRecs.length - 1; i4 >= 0; i4--) {
                        NoteRecord nr = noteRecs[i4];
                        if (nr.getRow() == rowNum) {
                            HSSFComment comment = getCellComment(rowNum, nr.getColumn());
                            if (comment != null) {
                                comment.setRow(rowNum + i3);
                            }
                        }
                    }
                }
            }
            rowNum += inc;
            s = s2;
        }
        if (i2 == this._lastrow || i2 + i3 > this._lastrow) {
            this._lastrow = Math.min(i2 + i3, SpreadsheetVersion.EXCEL97.getLastRowIndex());
        }
        if (i == this._firstrow || i + i3 < this._firstrow) {
            this._firstrow = Math.max(i + i3, 0);
        }
        short externSheetIndex = this._book.checkExternSheet(this._workbook.getSheetIndex((Sheet) this));
        FormulaShifter shifter = FormulaShifter.createForRowShift(externSheetIndex, i, i2, i3);
        this._sheet.updateFormulasAfterCellShift(shifter, externSheetIndex);
        int nSheets = this._workbook.getNumberOfSheets();
        for (int i5 = 0; i5 < nSheets; i5++) {
            InternalSheet otherSheet = this._workbook.getSheetAt(i5).getSheet();
            if (otherSheet != this._sheet) {
                otherSheet.updateFormulasAfterCellShift(shifter, this._book.checkExternSheet(i5));
            }
        }
        this._workbook.getWorkbook().updateNamesAfterCellShift(shifter);
    }

    /* access modifiers changed from: protected */
    public void insertChartRecords(List<Record> records) {
        this._sheet.getRecords().addAll(this._sheet.findFirstRecordLocBySid(574), records);
    }

    private void notifyRowShifting(HSSFRow row) {
        StringBuilder sb = new StringBuilder();
        sb.append("Row[rownum=");
        sb.append(row.getRowNum());
        sb.append("] contains cell(s) included in a multi-cell array formula. ");
        sb.append("You cannot change part of an array.");
        String msg = sb.toString();
        Iterator i$ = row.iterator();
        while (i$.hasNext()) {
            HSSFCell hcell = (HSSFCell) ((Cell) i$.next());
            if (hcell.isPartOfArrayFormulaGroup()) {
                hcell.notifyArrayFormulaChanging(msg);
            }
        }
    }

    public void createFreezePane(int colSplit, int rowSplit, int leftmostColumn, int topRow) {
        validateColumn(colSplit);
        validateRow(rowSplit);
        if (leftmostColumn < colSplit) {
            throw new IllegalArgumentException("leftmostColumn parameter must not be less than colSplit parameter");
        } else if (topRow < rowSplit) {
            throw new IllegalArgumentException("topRow parameter must not be less than leftmostColumn parameter");
        } else {
            getSheet().createFreezePane(colSplit, rowSplit, topRow, leftmostColumn);
        }
    }

    public void createFreezePane(int colSplit, int rowSplit) {
        createFreezePane(colSplit, rowSplit, colSplit, rowSplit);
    }

    public void createSplitPane(int xSplitPos, int ySplitPos, int leftmostColumn, int topRow, int activePane) {
        getSheet().createSplitPane(xSplitPos, ySplitPos, topRow, leftmostColumn, activePane);
    }

    public PaneInformation getPaneInformation() {
        return getSheet().getPaneInformation();
    }

    public void setDisplayGridlines(boolean show) {
        this._sheet.setDisplayGridlines(show);
    }

    public boolean isDisplayGridlines() {
        return this._sheet.isDisplayGridlines();
    }

    public void setDisplayFormulas(boolean show) {
        this._sheet.setDisplayFormulas(show);
    }

    public boolean isDisplayFormulas() {
        return this._sheet.isDisplayFormulas();
    }

    public void setDisplayRowColHeadings(boolean show) {
        this._sheet.setDisplayRowColHeadings(show);
    }

    public boolean isDisplayRowColHeadings() {
        return this._sheet.isDisplayRowColHeadings();
    }

    public void setRowBreak(int row) {
        validateRow(row);
        this._sheet.getPageSettings().setRowBreak(row, 0, 255);
    }

    public boolean isRowBroken(int row) {
        return this._sheet.getPageSettings().isRowBroken(row);
    }

    public void removeRowBreak(int row) {
        this._sheet.getPageSettings().removeRowBreak(row);
    }

    public int[] getRowBreaks() {
        return this._sheet.getPageSettings().getRowBreaks();
    }

    public int[] getColumnBreaks() {
        return this._sheet.getPageSettings().getColumnBreaks();
    }

    public void setColumnBreak(int column) {
        validateColumn((short) column);
        this._sheet.getPageSettings().setColumnBreak((short) column, 0, (short) SpreadsheetVersion.EXCEL97.getLastRowIndex());
    }

    public boolean isColumnBroken(int column) {
        return this._sheet.getPageSettings().isColumnBroken(column);
    }

    public void removeColumnBreak(int column) {
        this._sheet.getPageSettings().removeColumnBreak(column);
    }

    /* access modifiers changed from: protected */
    public void validateRow(int row) {
        int maxrow = SpreadsheetVersion.EXCEL97.getLastRowIndex();
        if (row > maxrow) {
            StringBuilder sb = new StringBuilder();
            sb.append("Maximum row number is ");
            sb.append(maxrow);
            throw new IllegalArgumentException(sb.toString());
        } else if (row < 0) {
            throw new IllegalArgumentException("Minumum row number is 0");
        }
    }

    /* access modifiers changed from: protected */
    public void validateColumn(int column) {
        int maxcol = SpreadsheetVersion.EXCEL97.getLastColumnIndex();
        if (column > maxcol) {
            StringBuilder sb = new StringBuilder();
            sb.append("Maximum column number is ");
            sb.append(maxcol);
            throw new IllegalArgumentException(sb.toString());
        } else if (column < 0) {
            throw new IllegalArgumentException("Minimum column number is 0");
        }
    }

    public void dumpDrawingRecords(boolean fat) {
        this._sheet.aggregateDrawingRecords(this._book.getDrawingManager(), false);
        List<EscherRecord> escherRecords = ((EscherAggregate) getSheet().findFirstRecordBySid(EscherAggregate.sid)).getEscherRecords();
        PrintWriter w = new PrintWriter(System.out);
        for (EscherRecord escherRecord : escherRecords) {
            if (fat) {
                System.out.println(escherRecord.toString());
            } else {
                escherRecord.display(w, 0);
            }
        }
        w.flush();
    }

    public HSSFPatriarch createDrawingPatriarch() {
        if (this._patriarch == null) {
            this._book.createDrawingGroup();
            this._sheet.aggregateDrawingRecords(this._book.getDrawingManager(), true);
            EscherAggregate agg = (EscherAggregate) this._sheet.findFirstRecordBySid(EscherAggregate.sid);
            this._patriarch = new HSSFPatriarch(this, agg);
            agg.clear();
            agg.setPatriarch(this._patriarch);
        }
        return this._patriarch;
    }

    public EscherAggregate getDrawingEscherAggregate() {
        this._book.findDrawingGroup();
        if (this._book.getDrawingManager() == null || this._sheet.aggregateDrawingRecords(this._book.getDrawingManager(), false) == -1) {
            return null;
        }
        return (EscherAggregate) this._sheet.findFirstRecordBySid(EscherAggregate.sid);
    }

    public HSSFPatriarch getDrawingPatriarch() {
        if (this._patriarch != null) {
            return this._patriarch;
        }
        EscherAggregate agg = getDrawingEscherAggregate();
        if (agg == null) {
            return null;
        }
        this._patriarch = new HSSFPatriarch(this, agg);
        agg.setPatriarch(this._patriarch);
        agg.convertRecordsToUserModel();
        return this._patriarch;
    }

    public void setColumnGroupCollapsed(short columnNumber, boolean collapsed) {
        setColumnGroupCollapsed((int) 65535 & columnNumber, collapsed);
    }

    public void groupColumn(short fromColumn, short toColumn) {
        groupColumn((int) fromColumn & 65535, (int) 65535 & toColumn);
    }

    public void ungroupColumn(short fromColumn, short toColumn) {
        ungroupColumn((int) fromColumn & 65535, (int) 65535 & toColumn);
    }

    public void setColumnGroupCollapsed(int columnNumber, boolean collapsed) {
        this._sheet.setColumnGroupCollapsed(columnNumber, collapsed);
    }

    public void groupColumn(int fromColumn, int toColumn) {
        this._sheet.groupColumnRange(fromColumn, toColumn, true);
    }

    public void ungroupColumn(int fromColumn, int toColumn) {
        this._sheet.groupColumnRange(fromColumn, toColumn, false);
    }

    public void groupRow(int fromRow, int toRow) {
        this._sheet.groupRowRange(fromRow, toRow, true);
    }

    public void ungroupRow(int fromRow, int toRow) {
        this._sheet.groupRowRange(fromRow, toRow, false);
    }

    public void setRowGroupCollapsed(int rowIndex, boolean collapse) {
        if (collapse) {
            this._sheet.getRowsAggregate().collapseRow(rowIndex);
        } else {
            this._sheet.getRowsAggregate().expandRow(rowIndex);
        }
    }

    public void setDefaultColumnStyle(int column, CellStyle style) {
        this._sheet.setDefaultColumnStyle(column, ((HSSFCellStyle) style).getIndex());
    }

    public void autoSizeColumn(int column) {
        autoSizeColumn(column, false);
    }

    public void autoSizeColumn(int column, boolean useMergedCells) {
        double width;
        AttributedString str;
        HSSFFont defaultFont;
        Iterator it;
        double fontHeightMultiple;
        char defaultChar;
        HSSFDataFormatter formatter;
        HSSFWorkbook wb;
        FontRenderContext frc;
        double width2;
        HSSFDataFormatter formatter2;
        FontRenderContext frc2;
        double fontHeightMultiple2;
        HSSFCellStyle style;
        AttributedString str2;
        HSSFWorkbook wb2;
        HSSFRichTextString rt;
        String[] lines;
        int i = column;
        char defaultChar2 = '0';
        double fontHeightMultiple3 = 2.0d;
        FontRenderContext frc3 = new FontRenderContext(null, true, true);
        HSSFWorkbook wb3 = HSSFWorkbook.create(this._book);
        HSSFDataFormatter formatter3 = new HSSFDataFormatter();
        HSSFFont defaultFont2 = wb3.getFontAt(0);
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append('0');
        AttributedString str3 = new AttributedString(sb.toString());
        copyAttributes(defaultFont2, str3, 0, 1);
        TextLayout layout = new TextLayout(str3.getIterator(), frc3);
        int defaultCharWidth = (int) layout.getAdvance();
        Iterator rowIterator = rowIterator();
        AttributedString str4 = str3;
        TextLayout textLayout = layout;
        double width3 = -1.0d;
        while (true) {
            Iterator it2 = rowIterator;
            if (!it2.hasNext()) {
                break;
            }
            HSSFRow row = (HSSFRow) it2.next();
            HSSFCell cell = row.getCell(i);
            if (cell == null) {
                it = it2;
                defaultFont = defaultFont2;
                width = width3;
                str = str4;
            } else {
                it = it2;
                defaultFont = defaultFont2;
                HSSFCell cell2 = cell;
                int colspan = 1;
                int i2 = 0;
                while (true) {
                    str = str4;
                    width = width3;
                    int i3 = i2;
                    if (i3 < getNumMergedRegions()) {
                        CellRangeAddress region = getMergedRegion(i3);
                        if (containsCell(region, row.getRowNum(), i)) {
                            if (!useMergedCells) {
                                break;
                            }
                            cell2 = row.getCell(region.getFirstColumn());
                            colspan = (region.getLastColumn() + 1) - region.getFirstColumn();
                        }
                        i2 = i3 + 1;
                        str4 = str;
                        width3 = width;
                    } else {
                        HSSFCellStyle style2 = cell2.getCellStyle();
                        int cellType = cell2.getCellType();
                        if (cellType == 2) {
                            cellType = cell2.getCachedFormulaResultType();
                        }
                        HSSFFont font = wb3.getFontAt(style2.getFontIndex());
                        double fontHeightMultiple4 = fontHeightMultiple3;
                        if (cellType == 1) {
                            HSSFRichTextString rt2 = cell2.getRichStringCellValue();
                            HSSFRow hSSFRow = row;
                            String[] lines2 = rt2.getString().split("\\n");
                            double width4 = width;
                            int i4 = 0;
                            while (i4 < lines2.length) {
                                StringBuilder sb2 = new StringBuilder();
                                HSSFDataFormatter formatter4 = formatter3;
                                sb2.append(lines2[i4]);
                                sb2.append(defaultChar2);
                                String txt = sb2.toString();
                                AttributedString str5 = new AttributedString(txt);
                                char defaultChar3 = defaultChar2;
                                String str6 = txt;
                                copyAttributes(font, str5, 0, txt.length());
                                if (rt2.numFormattingRuns() > 0) {
                                    int j = 0;
                                    while (j < lines2[i4].length()) {
                                        short fontAtIndex = rt2.getFontAtIndex(j);
                                        if (fontAtIndex != 0) {
                                            lines = lines2;
                                            short s = fontAtIndex;
                                            copyAttributes(wb3.getFontAt((short) fontAtIndex), str5, j, j + 1);
                                        } else {
                                            lines = lines2;
                                        }
                                        j++;
                                        lines2 = lines;
                                    }
                                }
                                String[] lines3 = lines2;
                                TextLayout layout2 = new TextLayout(str5.getIterator(), frc3);
                                if (style2.getRotation() != 0) {
                                    AffineTransform trans = new AffineTransform();
                                    rt = rt2;
                                    trans.concatenate(AffineTransform.getRotateInstance(((((double) style2.getRotation()) * 2.0d) * 3.141592653589793d) / 360.0d));
                                    wb2 = wb3;
                                    str2 = str5;
                                    style = style2;
                                    double fontHeightMultiple5 = fontHeightMultiple4;
                                    trans.concatenate(AffineTransform.getScaleInstance(1.0d, fontHeightMultiple5));
                                    AffineTransform affineTransform = trans;
                                    fontHeightMultiple2 = fontHeightMultiple5;
                                    width4 = Math.max(width4, ((layout2.getOutline(trans).getBounds().getWidth() / ((double) colspan)) / ((double) defaultCharWidth)) + ((double) cell2.getCellStyle().getIndention()));
                                    frc2 = frc3;
                                } else {
                                    rt = rt2;
                                    wb2 = wb3;
                                    str2 = str5;
                                    style = style2;
                                    fontHeightMultiple2 = fontHeightMultiple4;
                                    frc2 = frc3;
                                    width4 = Math.max(width4, ((layout2.getBounds().getWidth() / ((double) colspan)) / ((double) defaultCharWidth)) + ((double) cell2.getCellStyle().getIndention()));
                                }
                                i4++;
                                TextLayout textLayout2 = layout2;
                                formatter3 = formatter4;
                                defaultChar2 = defaultChar3;
                                lines2 = lines3;
                                rt2 = rt;
                                wb3 = wb2;
                                str = str2;
                                style2 = style;
                                fontHeightMultiple4 = fontHeightMultiple2;
                                frc3 = frc2;
                                int i5 = column;
                            }
                            wb = wb3;
                            HSSFCellStyle hSSFCellStyle = style2;
                            width2 = width4;
                            str4 = str;
                            formatter = formatter3;
                            defaultChar = defaultChar2;
                            fontHeightMultiple = fontHeightMultiple4;
                            frc = frc3;
                        } else {
                            char defaultChar4 = defaultChar2;
                            FontRenderContext frc4 = frc3;
                            HSSFRow hSSFRow2 = row;
                            wb = wb3;
                            HSSFDataFormatter formatter5 = formatter3;
                            HSSFCellStyle style3 = style2;
                            double fontHeightMultiple6 = fontHeightMultiple4;
                            String sval = null;
                            if (cellType == 0) {
                                formatter2 = formatter5;
                                try {
                                    sval = formatter2.formatCellValue(cell2);
                                } catch (Exception e) {
                                    Exception exc = e;
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append("");
                                    sb3.append(cell2.getNumericCellValue());
                                    sval = sb3.toString();
                                }
                            } else {
                                formatter2 = formatter5;
                                if (cellType == 4) {
                                    sval = String.valueOf(cell2.getBooleanCellValue());
                                }
                            }
                            if (sval != null) {
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append(sval);
                                char defaultChar5 = defaultChar4;
                                sb4.append(defaultChar5);
                                String txt2 = sb4.toString();
                                AttributedString str7 = new AttributedString(txt2);
                                copyAttributes(font, str7, 0, txt2.length());
                                frc = frc4;
                                TextLayout layout3 = new TextLayout(str7.getIterator(), frc);
                                HSSFCellStyle style4 = style3;
                                if (style4.getRotation() != 0) {
                                    AffineTransform trans2 = new AffineTransform();
                                    String str8 = sval;
                                    formatter = formatter2;
                                    trans2.concatenate(AffineTransform.getRotateInstance(((((double) style4.getRotation()) * 2.0d) * 3.141592653589793d) / 360.0d));
                                    String str9 = txt2;
                                    defaultChar = defaultChar5;
                                    double fontHeightMultiple7 = fontHeightMultiple6;
                                    trans2.concatenate(AffineTransform.getScaleInstance(1.0d, fontHeightMultiple7));
                                    fontHeightMultiple = fontHeightMultiple7;
                                    width2 = Math.max(width, ((layout3.getOutline(trans2).getBounds().getWidth() / ((double) colspan)) / ((double) defaultCharWidth)) + ((double) cell2.getCellStyle().getIndention()));
                                    str4 = str7;
                                    TextLayout textLayout3 = layout3;
                                } else {
                                    formatter = formatter2;
                                    String str10 = txt2;
                                    defaultChar = defaultChar5;
                                    fontHeightMultiple = fontHeightMultiple6;
                                    AttributedString str11 = str7;
                                    TextLayout layout4 = layout3;
                                    width2 = Math.max(width, ((layout3.getBounds().getWidth() / ((double) colspan)) / ((double) defaultCharWidth)) + ((double) cell2.getCellStyle().getIndention()));
                                    str4 = str11;
                                    TextLayout textLayout4 = layout4;
                                }
                            } else {
                                formatter = formatter2;
                                width2 = width;
                                defaultChar = defaultChar4;
                                fontHeightMultiple = fontHeightMultiple6;
                                frc = frc4;
                                str4 = str;
                            }
                        }
                        frc3 = frc;
                        rowIterator = it;
                        defaultFont2 = defaultFont;
                        wb3 = wb;
                        formatter3 = formatter;
                        fontHeightMultiple3 = fontHeightMultiple;
                        width3 = width2;
                        defaultChar2 = defaultChar;
                        i = column;
                    }
                }
            }
            rowIterator = it;
            defaultFont2 = defaultFont;
            str4 = str;
            width3 = width;
        }
        double d = fontHeightMultiple3;
        HSSFWorkbook hSSFWorkbook = wb3;
        HSSFDataFormatter hSSFDataFormatter = formatter3;
        HSSFFont hSSFFont = defaultFont2;
        double width5 = width3;
        AttributedString attributedString = str4;
        FontRenderContext fontRenderContext = frc3;
        if (width5 != -1.0d) {
            double width6 = width5 * 256.0d;
            if (width6 > 32767.0d) {
                width6 = 32767.0d;
            }
            this._sheet.setColumnWidth(column, (short) ((int) width6));
            return;
        }
        int i6 = column;
    }

    private void copyAttributes(HSSFFont font, AttributedString str, int startIdx, int endIdx) {
        str.addAttribute(TextAttribute.FAMILY, font.getFontName(), startIdx, endIdx);
        str.addAttribute(TextAttribute.SIZE, new Float((float) font.getFontHeightInPoints()));
        if (font.getBoldweight() == 700) {
            str.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD, startIdx, endIdx);
        }
        if (font.getItalic()) {
            str.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE, startIdx, endIdx);
        }
        if (font.getUnderline() == 1) {
            str.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, startIdx, endIdx);
        }
    }

    public HSSFComment getCellComment(int row, int column) {
        HSSFRow r = getRow(row);
        if (r == null) {
            return null;
        }
        HSSFCell c = r.getCell(column);
        if (c != null) {
            return c.getCellComment();
        }
        return HSSFCell.findCellComment(this._sheet, row, column);
    }

    public HSSFSheetConditionalFormatting getSheetConditionalFormatting() {
        return new HSSFSheetConditionalFormatting(this);
    }

    public String getSheetName() {
        HSSFWorkbook wb = getWorkbook();
        return wb.getSheetName(wb.getSheetIndex((Sheet) this));
    }

    private CellRange<HSSFCell> getCellRange(CellRangeAddress range) {
        int firstRow = range.getFirstRow();
        int firstColumn = range.getFirstColumn();
        int lastRow = range.getLastRow();
        int lastColumn = range.getLastColumn();
        int height = (lastRow - firstRow) + 1;
        int width = (lastColumn - firstColumn) + 1;
        ArrayList arrayList = new ArrayList(height * width);
        for (int rowIn = firstRow; rowIn <= lastRow; rowIn++) {
            for (int colIn = firstColumn; colIn <= lastColumn; colIn++) {
                HSSFRow row = getRow(rowIn);
                if (row == null) {
                    row = createRow(rowIn);
                }
                HSSFCell cell = row.getCell(colIn);
                if (cell == null) {
                    cell = row.createCell(colIn);
                }
                arrayList.add(cell);
            }
        }
        return SSCellRange.create(firstRow, firstColumn, height, width, arrayList, HSSFCell.class);
    }

    public CellRange<HSSFCell> setArrayFormula(String formula, CellRangeAddress range) {
        Ptg[] ptgs = HSSFFormulaParser.parse(formula, this._workbook, 2, this._workbook.getSheetIndex((Sheet) this));
        CellRange<HSSFCell> cells = getCellRange(range);
        for (HSSFCell c : cells) {
            c.setCellArrayFormula(range);
        }
        ((FormulaRecordAggregate) ((HSSFCell) cells.getTopLeftCell()).getCellValueRecord()).setArrayFormula(range, ptgs);
        return cells;
    }

    public CellRange<HSSFCell> removeArrayFormula(Cell cell) {
        if (cell.getSheet() != this) {
            throw new IllegalArgumentException("Specified cell does not belong to this sheet.");
        }
        CellValueRecordInterface rec = ((HSSFCell) cell).getCellValueRecord();
        if (!(rec instanceof FormulaRecordAggregate)) {
            String ref = new CellReference(cell).formatAsString();
            StringBuilder sb = new StringBuilder();
            sb.append("Cell ");
            sb.append(ref);
            sb.append(" is not part of an array formula.");
            throw new IllegalArgumentException(sb.toString());
        }
        CellRange<HSSFCell> result = getCellRange(((FormulaRecordAggregate) rec).removeArrayFormula(cell.getRowIndex(), cell.getColumnIndex()));
        for (Cell c : result) {
            c.setCellType(3);
        }
        return result;
    }

    public DataValidationHelper getDataValidationHelper() {
        return new HSSFDataValidationHelper(this);
    }

    public HSSFAutoFilter setAutoFilter(CellRangeAddress range) {
        InternalWorkbook workbook = this._workbook.getWorkbook();
        int sheetIndex = this._workbook.getSheetIndex((Sheet) this);
        NameRecord name = workbook.getSpecificBuiltinRecord(13, sheetIndex + 1);
        if (name == null) {
            name = workbook.createBuiltInName(13, sheetIndex + 1);
        }
        NameRecord name2 = name;
        Area3DPtg ptg = new Area3DPtg(range.getFirstRow(), range.getLastRow(), range.getFirstColumn(), range.getLastColumn(), false, false, false, false, sheetIndex);
        name2.setNameDefinition(new Ptg[]{ptg});
        AutoFilterInfoRecord r = new AutoFilterInfoRecord();
        r.setNumEntries((short) ((range.getLastColumn() + 1) - range.getFirstColumn()));
        this._sheet.getRecords().add(this._sheet.findFirstRecordLocBySid(512), r);
        HSSFPatriarch p = createDrawingPatriarch();
        for (int col = range.getFirstColumn(); col <= range.getLastColumn(); col++) {
            HSSFClientAnchor hSSFClientAnchor = new HSSFClientAnchor(0, 0, 0, 0, (short) col, range.getFirstRow(), (short) (col + 1), range.getFirstRow() + 1);
            p.createComboBox(hSSFClientAnchor);
        }
        return new HSSFAutoFilter(this);
    }
}

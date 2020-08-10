package org.apache.poi.hssf.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.CalcCountRecord;
import org.apache.poi.hssf.record.CalcModeRecord;
import org.apache.poi.hssf.record.CellValueRecordInterface;
import org.apache.poi.hssf.record.ColumnInfoRecord;
import org.apache.poi.hssf.record.DefaultColWidthRecord;
import org.apache.poi.hssf.record.DefaultRowHeightRecord;
import org.apache.poi.hssf.record.DeltaRecord;
import org.apache.poi.hssf.record.DimensionsRecord;
import org.apache.poi.hssf.record.DrawingRecord;
import org.apache.poi.hssf.record.EOFRecord;
import org.apache.poi.hssf.record.EscherAggregate;
import org.apache.poi.hssf.record.GridsetRecord;
import org.apache.poi.hssf.record.GutsRecord;
import org.apache.poi.hssf.record.IterationRecord;
import org.apache.poi.hssf.record.NoteRecord;
import org.apache.poi.hssf.record.ObjRecord;
import org.apache.poi.hssf.record.PaneRecord;
import org.apache.poi.hssf.record.PrintGridlinesRecord;
import org.apache.poi.hssf.record.PrintHeadersRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordBase;
import org.apache.poi.hssf.record.RefModeRecord;
import org.apache.poi.hssf.record.RowRecord;
import org.apache.poi.hssf.record.SCLRecord;
import org.apache.poi.hssf.record.SaveRecalcRecord;
import org.apache.poi.hssf.record.SelectionRecord;
import org.apache.poi.hssf.record.UncalcedRecord;
import org.apache.poi.hssf.record.WSBoolRecord;
import org.apache.poi.hssf.record.WindowTwoRecord;
import org.apache.poi.hssf.record.aggregates.ChartSubstreamRecordAggregate;
import org.apache.poi.hssf.record.aggregates.ColumnInfoRecordsAggregate;
import org.apache.poi.hssf.record.aggregates.ConditionalFormattingTable;
import org.apache.poi.hssf.record.aggregates.CustomViewSettingsRecordAggregate;
import org.apache.poi.hssf.record.aggregates.DataValidityTable;
import org.apache.poi.hssf.record.aggregates.MergedCellsTable;
import org.apache.poi.hssf.record.aggregates.PageSettingsBlock;
import org.apache.poi.hssf.record.aggregates.RecordAggregate;
import org.apache.poi.hssf.record.aggregates.RecordAggregate.PositionTrackingVisitor;
import org.apache.poi.hssf.record.aggregates.RecordAggregate.RecordVisitor;
import org.apache.poi.hssf.record.aggregates.RowRecordsAggregate;
import org.apache.poi.hssf.record.aggregates.WorksheetProtectionBlock;
import org.apache.poi.hssf.record.formula.FormulaShifter;
import org.apache.poi.hssf.util.PaneInformation;
import org.apache.poi.p009ss.util.CellRangeAddress;
import org.apache.poi.util.Internal;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

@Internal
public final class InternalSheet {
    public static final short BottomMargin = 3;
    public static final short LeftMargin = 0;
    public static final byte PANE_LOWER_LEFT = 2;
    public static final byte PANE_LOWER_RIGHT = 0;
    public static final byte PANE_UPPER_LEFT = 3;
    public static final byte PANE_UPPER_RIGHT = 1;
    public static final short RightMargin = 1;
    public static final short TopMargin = 2;
    private static POILogger log = POILogFactory.getLogger(InternalSheet.class);
    ColumnInfoRecordsAggregate _columnInfos;
    private DataValidityTable _dataValidityTable = null;
    private DimensionsRecord _dimensions;
    private GutsRecord _gutsRecord;
    protected boolean _isUncalced = false;
    private final MergedCellsTable _mergedCellsTable = new MergedCellsTable();
    private final WorksheetProtectionBlock _protectionBlock = new WorksheetProtectionBlock();
    private PageSettingsBlock _psBlock;
    private List<RecordBase> _records;
    protected final RowRecordsAggregate _rowsAggregate;
    protected SelectionRecord _selection = null;
    private ConditionalFormattingTable condFormatting;
    protected DefaultColWidthRecord defaultcolwidth = null;
    protected DefaultRowHeightRecord defaultrowheight = null;
    protected GridsetRecord gridset = null;
    protected PrintGridlinesRecord printGridlines = null;
    private Iterator<RowRecord> rowRecIterator = null;
    protected WindowTwoRecord windowTwo = null;

    private static final class RecordCloner implements RecordVisitor {
        private final List<RecordBase> _destList;

        public RecordCloner(List<RecordBase> destList) {
            this._destList = destList;
        }

        public void visitRecord(Record r) {
            this._destList.add((RecordBase) r.clone());
        }
    }

    public static InternalSheet createSheet(RecordStream rs) {
        return new InternalSheet(rs);
    }

    private InternalSheet(RecordStream rs) {
        RowRecordsAggregate rra = null;
        List<RecordBase> records = new ArrayList<>(128);
        this._records = records;
        int dimsloc = -1;
        if (rs.peekNextSid() != 2057) {
            throw new RuntimeException("BOF record expected");
        }
        BOFRecord bof = (BOFRecord) rs.getNext();
        bof.getType();
        records.add(bof);
        while (true) {
            if (!rs.hasNext()) {
                break;
            }
            int recSid = rs.peekNextSid();
            if (recSid == 432) {
                this.condFormatting = new ConditionalFormattingTable(rs);
                records.add(this.condFormatting);
            } else if (recSid == 125) {
                this._columnInfos = new ColumnInfoRecordsAggregate(rs);
                records.add(this._columnInfos);
            } else if (recSid == 434) {
                this._dataValidityTable = new DataValidityTable(rs);
                records.add(this._dataValidityTable);
            } else if (RecordOrderer.isRowBlockRecord(recSid)) {
                if (rra != null) {
                    throw new RuntimeException("row/cell records found in the wrong place");
                }
                RowBlocksReader rbr = new RowBlocksReader(rs);
                this._mergedCellsTable.addRecords(rbr.getLooseMergedCells());
                rra = new RowRecordsAggregate(rbr.getPlainRecordStream(), rbr.getSharedFormulaManager());
                records.add(rra);
            } else if (CustomViewSettingsRecordAggregate.isBeginRecord(recSid)) {
                records.add(new CustomViewSettingsRecordAggregate(rs));
            } else if (PageSettingsBlock.isComponentRecord(recSid)) {
                if (this._psBlock == null) {
                    this._psBlock = new PageSettingsBlock(rs);
                    records.add(this._psBlock);
                } else {
                    this._psBlock.addLateRecords(rs);
                }
                this._psBlock.positionRecords(records);
            } else if (WorksheetProtectionBlock.isComponentRecord(recSid)) {
                this._protectionBlock.addRecords(rs);
            } else if (recSid == 229) {
                this._mergedCellsTable.read(rs);
            } else if (recSid == 2057) {
                spillAggregate(new ChartSubstreamRecordAggregate(rs), records);
            } else {
                Record rec = rs.getNext();
                if (recSid == 523) {
                    continue;
                } else if (recSid == 94) {
                    this._isUncalced = true;
                } else if (recSid == 2152 || recSid == 2151) {
                    records.add(rec);
                } else if (recSid == 10) {
                    records.add(rec);
                    break;
                } else {
                    if (recSid == 512) {
                        if (this._columnInfos == null) {
                            this._columnInfos = new ColumnInfoRecordsAggregate();
                            records.add(this._columnInfos);
                        }
                        this._dimensions = (DimensionsRecord) rec;
                        dimsloc = records.size();
                    } else if (recSid == 85) {
                        this.defaultcolwidth = (DefaultColWidthRecord) rec;
                    } else if (recSid == 549) {
                        this.defaultrowheight = (DefaultRowHeightRecord) rec;
                    } else if (recSid == 43) {
                        this.printGridlines = (PrintGridlinesRecord) rec;
                    } else if (recSid == 130) {
                        this.gridset = (GridsetRecord) rec;
                    } else if (recSid == 29) {
                        this._selection = (SelectionRecord) rec;
                    } else if (recSid == 574) {
                        this.windowTwo = (WindowTwoRecord) rec;
                    } else if (recSid == 128) {
                        this._gutsRecord = (GutsRecord) rec;
                    }
                    records.add(rec);
                }
            }
        }
        if (this.windowTwo == null) {
            throw new RuntimeException("WINDOW2 was not found");
        }
        if (this._dimensions == null) {
            if (rra == null) {
                rra = new RowRecordsAggregate();
            } else {
                log.log(POILogger.WARN, (Object) "DIMENSION record not found even though row/cells present");
            }
            dimsloc = findFirstRecordLocBySid(574);
            this._dimensions = rra.createDimensions();
            records.add(dimsloc, this._dimensions);
        }
        if (rra == null) {
            rra = new RowRecordsAggregate();
            records.add(dimsloc + 1, rra);
        }
        this._rowsAggregate = rra;
        RecordOrderer.addNewSheetRecord(records, this._mergedCellsTable);
        RecordOrderer.addNewSheetRecord(records, this._protectionBlock);
        if (log.check(POILogger.DEBUG)) {
            log.log(POILogger.DEBUG, (Object) "sheet createSheet (existing file) exited");
        }
    }

    private static void spillAggregate(RecordAggregate ra, final List<RecordBase> recs) {
        ra.visitContainedRecords(new RecordVisitor() {
            public void visitRecord(Record r) {
                recs.add(r);
            }
        });
    }

    public InternalSheet cloneSheet() {
        List<RecordBase> clonedRecords = new ArrayList<>(this._records.size());
        for (int i = 0; i < this._records.size(); i++) {
            RecordBase rb = (RecordBase) this._records.get(i);
            if (rb instanceof RecordAggregate) {
                ((RecordAggregate) rb).visitContainedRecords(new RecordCloner(clonedRecords));
            } else {
                clonedRecords.add((Record) ((Record) rb).clone());
            }
        }
        return createSheet(new RecordStream(clonedRecords, 0));
    }

    public static InternalSheet createSheet() {
        return new InternalSheet();
    }

    private InternalSheet() {
        List<RecordBase> records = new ArrayList<>(32);
        if (log.check(POILogger.DEBUG)) {
            log.log(POILogger.DEBUG, (Object) "Sheet createsheet from scratch called");
        }
        records.add(createBOF());
        records.add(createCalcMode());
        records.add(createCalcCount());
        records.add(createRefMode());
        records.add(createIteration());
        records.add(createDelta());
        records.add(createSaveRecalc());
        records.add(createPrintHeaders());
        this.printGridlines = createPrintGridlines();
        records.add(this.printGridlines);
        this.gridset = createGridset();
        records.add(this.gridset);
        this._gutsRecord = createGuts();
        records.add(this._gutsRecord);
        this.defaultrowheight = createDefaultRowHeight();
        records.add(this.defaultrowheight);
        records.add(createWSBool());
        this._psBlock = new PageSettingsBlock();
        records.add(this._psBlock);
        records.add(this._protectionBlock);
        this.defaultcolwidth = createDefaultColWidth();
        records.add(this.defaultcolwidth);
        ColumnInfoRecordsAggregate columns = new ColumnInfoRecordsAggregate();
        records.add(columns);
        this._columnInfos = columns;
        this._dimensions = createDimensions();
        records.add(this._dimensions);
        this._rowsAggregate = new RowRecordsAggregate();
        records.add(this._rowsAggregate);
        WindowTwoRecord createWindowTwo = createWindowTwo();
        this.windowTwo = createWindowTwo;
        records.add(createWindowTwo);
        this._selection = createSelection();
        records.add(this._selection);
        records.add(this._mergedCellsTable);
        records.add(EOFRecord.instance);
        this._records = records;
        if (log.check(POILogger.DEBUG)) {
            log.log(POILogger.DEBUG, (Object) "Sheet createsheet from scratch exit");
        }
    }

    public RowRecordsAggregate getRowsAggregate() {
        return this._rowsAggregate;
    }

    private MergedCellsTable getMergedRecords() {
        return this._mergedCellsTable;
    }

    public void updateFormulasAfterCellShift(FormulaShifter shifter, int externSheetIndex) {
        getRowsAggregate().updateFormulasAfterRowShift(shifter, externSheetIndex);
        if (this.condFormatting != null) {
            getConditionalFormattingTable().updateFormulasAfterCellShift(shifter, externSheetIndex);
        }
    }

    public int addMergedRegion(int rowFrom, int colFrom, int rowTo, int colTo) {
        if (rowTo < rowFrom) {
            StringBuilder sb = new StringBuilder();
            sb.append("The 'to' row (");
            sb.append(rowTo);
            sb.append(") must not be less than the 'from' row (");
            sb.append(rowFrom);
            sb.append(")");
            throw new IllegalArgumentException(sb.toString());
        } else if (colTo < colFrom) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("The 'to' col (");
            sb2.append(colTo);
            sb2.append(") must not be less than the 'from' col (");
            sb2.append(colFrom);
            sb2.append(")");
            throw new IllegalArgumentException(sb2.toString());
        } else {
            MergedCellsTable mrt = getMergedRecords();
            mrt.addArea(rowFrom, colFrom, rowTo, colTo);
            return mrt.getNumberOfMergedRegions() - 1;
        }
    }

    public void removeMergedRegion(int index) {
        MergedCellsTable mrt = getMergedRecords();
        if (index < mrt.getNumberOfMergedRegions()) {
            mrt.remove(index);
        }
    }

    public CellRangeAddress getMergedRegionAt(int index) {
        MergedCellsTable mrt = getMergedRecords();
        if (index >= mrt.getNumberOfMergedRegions()) {
            return null;
        }
        return mrt.get(index);
    }

    public int getNumMergedRegions() {
        return getMergedRecords().getNumberOfMergedRegions();
    }

    public ConditionalFormattingTable getConditionalFormattingTable() {
        if (this.condFormatting == null) {
            this.condFormatting = new ConditionalFormattingTable();
            RecordOrderer.addNewSheetRecord(this._records, this.condFormatting);
        }
        return this.condFormatting;
    }

    public void setDimensions(int firstrow, short firstcol, int lastrow, short lastcol) {
        if (log.check(POILogger.DEBUG)) {
            log.log(POILogger.DEBUG, (Object) "Sheet.setDimensions");
            POILogger pOILogger = log;
            int i = POILogger.DEBUG;
            StringBuffer stringBuffer = new StringBuffer("firstrow");
            stringBuffer.append(firstrow);
            stringBuffer.append("firstcol");
            stringBuffer.append(firstcol);
            stringBuffer.append("lastrow");
            stringBuffer.append(lastrow);
            stringBuffer.append("lastcol");
            stringBuffer.append(lastcol);
            pOILogger.log(i, (Object) stringBuffer.toString());
        }
        this._dimensions.setFirstCol(firstcol);
        this._dimensions.setFirstRow(firstrow);
        this._dimensions.setLastCol(lastcol);
        this._dimensions.setLastRow(lastrow);
        if (log.check(POILogger.DEBUG)) {
            log.log(POILogger.DEBUG, (Object) "Sheet.setDimensions exiting");
        }
    }

    public void visitContainedRecords(RecordVisitor rv, int offset) {
        PositionTrackingVisitor ptv = new PositionTrackingVisitor(rv, offset);
        boolean haveSerializedIndex = false;
        for (int k = 0; k < this._records.size(); k++) {
            RecordBase record = (RecordBase) this._records.get(k);
            if (record instanceof RecordAggregate) {
                ((RecordAggregate) record).visitContainedRecords(ptv);
            } else {
                ptv.visitRecord((Record) record);
            }
            if ((record instanceof BOFRecord) && !haveSerializedIndex) {
                haveSerializedIndex = true;
                if (this._isUncalced) {
                    ptv.visitRecord(new UncalcedRecord());
                }
                if (this._rowsAggregate != null) {
                    int initRecsSize = getSizeOfInitialSheetRecords(k);
                    ptv.visitRecord(this._rowsAggregate.createIndexRecord(ptv.getPosition(), initRecsSize));
                }
            }
        }
    }

    private int getSizeOfInitialSheetRecords(int bofRecordIndex) {
        int result = 0;
        for (int j = bofRecordIndex + 1; j < this._records.size(); j++) {
            RecordBase tmpRec = (RecordBase) this._records.get(j);
            if (tmpRec instanceof RowRecordsAggregate) {
                break;
            }
            result += tmpRec.getRecordSize();
        }
        if (this._isUncalced != 0) {
            return result + UncalcedRecord.getStaticRecordSize();
        }
        return result;
    }

    public void addValueRecord(int row, CellValueRecordInterface col) {
        if (log.check(POILogger.DEBUG)) {
            POILogger pOILogger = log;
            int i = POILogger.DEBUG;
            StringBuilder sb = new StringBuilder();
            sb.append("add value record  row");
            sb.append(row);
            pOILogger.log(i, (Object) sb.toString());
        }
        DimensionsRecord d = this._dimensions;
        if (col.getColumn() > d.getLastCol()) {
            d.setLastCol((short) (col.getColumn() + 1));
        }
        if (col.getColumn() < d.getFirstCol()) {
            d.setFirstCol(col.getColumn());
        }
        this._rowsAggregate.insertCell(col);
    }

    public void removeValueRecord(int row, CellValueRecordInterface col) {
        log.logFormatted(POILogger.DEBUG, "remove value record row %", new int[]{row});
        this._rowsAggregate.removeCell(col);
    }

    public void replaceValueRecord(CellValueRecordInterface newval) {
        if (log.check(POILogger.DEBUG)) {
            log.log(POILogger.DEBUG, (Object) "replaceValueRecord ");
        }
        this._rowsAggregate.removeCell(newval);
        this._rowsAggregate.insertCell(newval);
    }

    public void addRow(RowRecord row) {
        if (log.check(POILogger.DEBUG)) {
            log.log(POILogger.DEBUG, (Object) "addRow ");
        }
        DimensionsRecord d = this._dimensions;
        if (row.getRowNumber() >= d.getLastRow()) {
            d.setLastRow(row.getRowNumber() + 1);
        }
        if (row.getRowNumber() < d.getFirstRow()) {
            d.setFirstRow(row.getRowNumber());
        }
        RowRecord existingRow = this._rowsAggregate.getRow(row.getRowNumber());
        if (existingRow != null) {
            this._rowsAggregate.removeRow(existingRow);
        }
        this._rowsAggregate.insertRow(row);
        if (log.check(POILogger.DEBUG)) {
            log.log(POILogger.DEBUG, (Object) "exit addRow");
        }
    }

    public void removeRow(RowRecord row) {
        this._rowsAggregate.removeRow(row);
    }

    public CellValueRecordInterface[] getValueRecords() {
        return this._rowsAggregate.getValueRecords();
    }

    public RowRecord getNextRow() {
        if (this.rowRecIterator == null) {
            this.rowRecIterator = this._rowsAggregate.getIterator();
        }
        if (!this.rowRecIterator.hasNext()) {
            return null;
        }
        return (RowRecord) this.rowRecIterator.next();
    }

    public RowRecord getRow(int rownum) {
        return this._rowsAggregate.getRow(rownum);
    }

    static BOFRecord createBOF() {
        BOFRecord retval = new BOFRecord();
        retval.setVersion(BOFRecord.VERSION);
        retval.setType(16);
        retval.setBuild(3515);
        retval.setBuildYear(BOFRecord.BUILD_YEAR);
        retval.setHistoryBitMask(193);
        retval.setRequiredVersion(6);
        return retval;
    }

    private static CalcModeRecord createCalcMode() {
        CalcModeRecord retval = new CalcModeRecord();
        retval.setCalcMode(1);
        return retval;
    }

    private static CalcCountRecord createCalcCount() {
        CalcCountRecord retval = new CalcCountRecord();
        retval.setIterations(100);
        return retval;
    }

    private static RefModeRecord createRefMode() {
        RefModeRecord retval = new RefModeRecord();
        retval.setMode(1);
        return retval;
    }

    private static IterationRecord createIteration() {
        return new IterationRecord(false);
    }

    private static DeltaRecord createDelta() {
        return new DeltaRecord(0.001d);
    }

    private static SaveRecalcRecord createSaveRecalc() {
        SaveRecalcRecord retval = new SaveRecalcRecord();
        retval.setRecalc(true);
        return retval;
    }

    private static PrintHeadersRecord createPrintHeaders() {
        PrintHeadersRecord retval = new PrintHeadersRecord();
        retval.setPrintHeaders(false);
        return retval;
    }

    private static PrintGridlinesRecord createPrintGridlines() {
        PrintGridlinesRecord retval = new PrintGridlinesRecord();
        retval.setPrintGridlines(false);
        return retval;
    }

    private static GridsetRecord createGridset() {
        GridsetRecord retval = new GridsetRecord();
        retval.setGridset(true);
        return retval;
    }

    private static GutsRecord createGuts() {
        GutsRecord retval = new GutsRecord();
        retval.setLeftRowGutter(0);
        retval.setTopColGutter(0);
        retval.setRowLevelMax(0);
        retval.setColLevelMax(0);
        return retval;
    }

    private GutsRecord getGutsRecord() {
        if (this._gutsRecord == null) {
            GutsRecord result = createGuts();
            RecordOrderer.addNewSheetRecord(this._records, result);
            this._gutsRecord = result;
        }
        return this._gutsRecord;
    }

    private static DefaultRowHeightRecord createDefaultRowHeight() {
        DefaultRowHeightRecord retval = new DefaultRowHeightRecord();
        retval.setOptionFlags(0);
        retval.setRowHeight(255);
        return retval;
    }

    private static WSBoolRecord createWSBool() {
        WSBoolRecord retval = new WSBoolRecord();
        retval.setWSBool1(4);
        retval.setWSBool2(-63);
        return retval;
    }

    private static DefaultColWidthRecord createDefaultColWidth() {
        DefaultColWidthRecord retval = new DefaultColWidthRecord();
        retval.setColWidth(8);
        return retval;
    }

    public int getDefaultColumnWidth() {
        return this.defaultcolwidth.getColWidth();
    }

    public boolean isGridsPrinted() {
        if (this.gridset == null) {
            this.gridset = createGridset();
            this._records.add(findFirstRecordLocBySid(10), this.gridset);
        }
        return !this.gridset.getGridset();
    }

    public void setGridsPrinted(boolean value) {
        this.gridset.setGridset(!value);
    }

    public void setDefaultColumnWidth(int dcw) {
        this.defaultcolwidth.setColWidth(dcw);
    }

    public void setDefaultRowHeight(short dch) {
        this.defaultrowheight.setRowHeight(dch);
    }

    public short getDefaultRowHeight() {
        return this.defaultrowheight.getRowHeight();
    }

    public int getColumnWidth(int columnIndex) {
        ColumnInfoRecord ci = this._columnInfos.findColumnInfo(columnIndex);
        if (ci != null) {
            return ci.getColumnWidth();
        }
        return this.defaultcolwidth.getColWidth() * 256;
    }

    public short getXFIndexForColAt(short columnIndex) {
        ColumnInfoRecord ci = this._columnInfos.findColumnInfo(columnIndex);
        if (ci != null) {
            return (short) ci.getXFIndex();
        }
        return 15;
    }

    public void setColumnWidth(int column, int width) {
        if (width > 65280) {
            throw new IllegalArgumentException("The maximum column width for an individual cell is 255 characters.");
        }
        setColumn(column, null, Integer.valueOf(width), null, null, null);
    }

    public boolean isColumnHidden(int columnIndex) {
        ColumnInfoRecord cir = this._columnInfos.findColumnInfo(columnIndex);
        if (cir == null) {
            return false;
        }
        return cir.getHidden();
    }

    public void setColumnHidden(int column, boolean hidden) {
        setColumn(column, null, null, null, Boolean.valueOf(hidden), null);
    }

    public void setDefaultColumnStyle(int column, int styleIndex) {
        setColumn(column, Short.valueOf((short) styleIndex), null, null, null, null);
    }

    private void setColumn(int column, Short xfStyle, Integer width, Integer level, Boolean hidden, Boolean collapsed) {
        this._columnInfos.setColumn(column, xfStyle, width, level, hidden, collapsed);
    }

    public void groupColumnRange(int fromColumn, int toColumn, boolean indent) {
        this._columnInfos.groupColumnRange(fromColumn, toColumn, indent);
        int maxLevel = this._columnInfos.getMaxOutlineLevel();
        GutsRecord guts = getGutsRecord();
        guts.setColLevelMax((short) (maxLevel + 1));
        if (maxLevel == 0) {
            guts.setTopColGutter(0);
        } else {
            guts.setTopColGutter((short) (((maxLevel - 1) * 12) + 29));
        }
    }

    private static DimensionsRecord createDimensions() {
        DimensionsRecord retval = new DimensionsRecord();
        retval.setFirstCol(0);
        retval.setLastRow(1);
        retval.setFirstRow(0);
        retval.setLastCol(1);
        return retval;
    }

    private static WindowTwoRecord createWindowTwo() {
        WindowTwoRecord retval = new WindowTwoRecord();
        retval.setOptions(1718);
        retval.setTopRow(0);
        retval.setLeftCol(0);
        retval.setHeaderColor(64);
        retval.setPageBreakZoom(0);
        retval.setNormalZoom(0);
        return retval;
    }

    private static SelectionRecord createSelection() {
        return new SelectionRecord(0, 0);
    }

    public short getTopRow() {
        if (this.windowTwo == null) {
            return 0;
        }
        return this.windowTwo.getTopRow();
    }

    public void setTopRow(short topRow) {
        if (this.windowTwo != null) {
            this.windowTwo.setTopRow(topRow);
        }
    }

    public void setLeftCol(short leftCol) {
        if (this.windowTwo != null) {
            this.windowTwo.setLeftCol(leftCol);
        }
    }

    public short getLeftCol() {
        if (this.windowTwo == null) {
            return 0;
        }
        return this.windowTwo.getLeftCol();
    }

    public int getActiveCellRow() {
        if (this._selection == null) {
            return 0;
        }
        return this._selection.getActiveCellRow();
    }

    public void setActiveCellRow(int row) {
        if (this._selection != null) {
            this._selection.setActiveCellRow(row);
        }
    }

    public short getActiveCellCol() {
        if (this._selection == null) {
            return 0;
        }
        return (short) this._selection.getActiveCellCol();
    }

    public void setActiveCellCol(short col) {
        if (this._selection != null) {
            this._selection.setActiveCellCol(col);
        }
    }

    public List<RecordBase> getRecords() {
        return this._records;
    }

    public GridsetRecord getGridsetRecord() {
        return this.gridset;
    }

    public Record findFirstRecordBySid(short sid) {
        int ix = findFirstRecordLocBySid(sid);
        if (ix < 0) {
            return null;
        }
        return (Record) this._records.get(ix);
    }

    public void setSCLRecord(SCLRecord sclRecord) {
        int oldRecordLoc = findFirstRecordLocBySid(160);
        if (oldRecordLoc == -1) {
            this._records.add(findFirstRecordLocBySid(574) + 1, sclRecord);
            return;
        }
        this._records.set(oldRecordLoc, sclRecord);
    }

    public int findFirstRecordLocBySid(short sid) {
        int max = this._records.size();
        for (int i = 0; i < max; i++) {
            Object rb = this._records.get(i);
            if ((rb instanceof Record) && ((Record) rb).getSid() == sid) {
                return i;
            }
        }
        return -1;
    }

    public WindowTwoRecord getWindowTwo() {
        return this.windowTwo;
    }

    public PrintGridlinesRecord getPrintGridlines() {
        return this.printGridlines;
    }

    public void setPrintGridlines(PrintGridlinesRecord newPrintGridlines) {
        this.printGridlines = newPrintGridlines;
    }

    public void setSelected(boolean sel) {
        this.windowTwo.setSelected(sel);
    }

    public void createFreezePane(int colSplit, int rowSplit, int topRow, int leftmostColumn) {
        int paneLoc = findFirstRecordLocBySid(65);
        if (paneLoc != -1) {
            this._records.remove(paneLoc);
        }
        int loc = findFirstRecordLocBySid(574);
        PaneRecord pane = new PaneRecord();
        pane.setX((short) colSplit);
        pane.setY((short) rowSplit);
        pane.setTopRow((short) topRow);
        pane.setLeftColumn((short) leftmostColumn);
        if (rowSplit == 0) {
            pane.setTopRow(0);
            pane.setActivePane(1);
        } else if (colSplit == 0) {
            pane.setLeftColumn(64);
            pane.setActivePane(2);
        } else {
            pane.setActivePane(0);
        }
        this._records.add(loc + 1, pane);
        this.windowTwo.setFreezePanes(true);
        this.windowTwo.setFreezePanesNoSplit(true);
        ((SelectionRecord) findFirstRecordBySid(29)).setPane((byte) pane.getActivePane());
    }

    public void createSplitPane(int xSplitPos, int ySplitPos, int topRow, int leftmostColumn, int activePane) {
        int paneLoc = findFirstRecordLocBySid(65);
        if (paneLoc != -1) {
            this._records.remove(paneLoc);
        }
        int loc = findFirstRecordLocBySid(574);
        PaneRecord r = new PaneRecord();
        r.setX((short) xSplitPos);
        r.setY((short) ySplitPos);
        r.setTopRow((short) topRow);
        r.setLeftColumn((short) leftmostColumn);
        r.setActivePane((short) activePane);
        this._records.add(loc + 1, r);
        this.windowTwo.setFreezePanes(false);
        this.windowTwo.setFreezePanesNoSplit(false);
        ((SelectionRecord) findFirstRecordBySid(29)).setPane(0);
    }

    public PaneInformation getPaneInformation() {
        PaneRecord rec = (PaneRecord) findFirstRecordBySid(65);
        if (rec == null) {
            return null;
        }
        PaneInformation paneInformation = new PaneInformation(rec.getX(), rec.getY(), rec.getTopRow(), rec.getLeftColumn(), (byte) rec.getActivePane(), this.windowTwo.getFreezePanes());
        return paneInformation;
    }

    public SelectionRecord getSelection() {
        return this._selection;
    }

    public void setSelection(SelectionRecord selection) {
        this._selection = selection;
    }

    public WorksheetProtectionBlock getProtectionBlock() {
        return this._protectionBlock;
    }

    public void setDisplayGridlines(boolean show) {
        this.windowTwo.setDisplayGridlines(show);
    }

    public boolean isDisplayGridlines() {
        return this.windowTwo.getDisplayGridlines();
    }

    public void setDisplayFormulas(boolean show) {
        this.windowTwo.setDisplayFormulas(show);
    }

    public boolean isDisplayFormulas() {
        return this.windowTwo.getDisplayFormulas();
    }

    public void setDisplayRowColHeadings(boolean show) {
        this.windowTwo.setDisplayRowColHeadings(show);
    }

    public boolean isDisplayRowColHeadings() {
        return this.windowTwo.getDisplayRowColHeadings();
    }

    public boolean getUncalced() {
        return this._isUncalced;
    }

    public void setUncalced(boolean uncalced) {
        this._isUncalced = uncalced;
    }

    public int aggregateDrawingRecords(DrawingManager2 drawingManager, boolean createIfMissing) {
        int startloc = findFirstRecordLocBySid(236);
        if (!(startloc == -1)) {
            List<RecordBase> records = getRecords();
            EscherAggregate r = EscherAggregate.createAggregate(records, startloc, drawingManager);
            int loc = startloc;
            while (loc + 1 < records.size() && (records.get(loc) instanceof DrawingRecord) && (records.get(loc + 1) instanceof ObjRecord)) {
                loc += 2;
            }
            int endloc = loc - 1;
            for (int i = 0; i < (endloc - startloc) + 1; i++) {
                records.remove(startloc);
            }
            records.add(startloc, r);
            return startloc;
        } else if (!createIfMissing) {
            return -1;
        } else {
            EscherAggregate aggregate = new EscherAggregate(drawingManager);
            int loc2 = findFirstRecordLocBySid(EscherAggregate.sid);
            if (loc2 == -1) {
                loc2 = findFirstRecordLocBySid(574);
            } else {
                getRecords().remove(loc2);
            }
            getRecords().add(loc2, aggregate);
            return loc2;
        }
    }

    public void preSerialize() {
        for (RecordBase r : getRecords()) {
            if (r instanceof EscherAggregate) {
                r.getRecordSize();
            }
        }
    }

    public PageSettingsBlock getPageSettings() {
        if (this._psBlock == null) {
            this._psBlock = new PageSettingsBlock();
            RecordOrderer.addNewSheetRecord(this._records, this._psBlock);
        }
        return this._psBlock;
    }

    public void setColumnGroupCollapsed(int columnNumber, boolean collapsed) {
        if (collapsed) {
            this._columnInfos.collapseColumn(columnNumber);
        } else {
            this._columnInfos.expandColumn(columnNumber);
        }
    }

    public void groupRowRange(int fromRow, int toRow, boolean indent) {
        for (int rowNum = fromRow; rowNum <= toRow; rowNum++) {
            RowRecord row = getRow(rowNum);
            if (row == null) {
                row = RowRecordsAggregate.createRow(rowNum);
                addRow(row);
            }
            int level = row.getOutlineLevel();
            row.setOutlineLevel((short) Math.min(7, Math.max(0, indent ? level + 1 : level - 1)));
        }
        recalcRowGutter();
    }

    private void recalcRowGutter() {
        int maxLevel = 0;
        Iterator iterator = this._rowsAggregate.getIterator();
        while (iterator.hasNext()) {
            maxLevel = Math.max(((RowRecord) iterator.next()).getOutlineLevel(), maxLevel);
        }
        GutsRecord guts = getGutsRecord();
        guts.setRowLevelMax((short) (maxLevel + 1));
        guts.setLeftRowGutter((short) ((maxLevel * 12) + 29));
    }

    public DataValidityTable getOrCreateDataValidityTable() {
        if (this._dataValidityTable == null) {
            DataValidityTable result = new DataValidityTable();
            RecordOrderer.addNewSheetRecord(this._records, result);
            this._dataValidityTable = result;
        }
        return this._dataValidityTable;
    }

    public NoteRecord[] getNoteRecords() {
        List<NoteRecord> temp = new ArrayList<>();
        for (int i = this._records.size() - 1; i >= 0; i--) {
            RecordBase rec = (RecordBase) this._records.get(i);
            if (rec instanceof NoteRecord) {
                temp.add((NoteRecord) rec);
            }
        }
        if (temp.size() < 1) {
            return NoteRecord.EMPTY_ARRAY;
        }
        NoteRecord[] result = new NoteRecord[temp.size()];
        temp.toArray(result);
        return result;
    }
}

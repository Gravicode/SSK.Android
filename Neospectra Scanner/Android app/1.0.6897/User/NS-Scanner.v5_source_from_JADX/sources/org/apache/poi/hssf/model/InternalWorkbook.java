package org.apache.poi.hssf.model;

import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.poi.ddf.EscherBSERecord;
import org.apache.poi.ddf.EscherBoolProperty;
import org.apache.poi.ddf.EscherContainerRecord;
import org.apache.poi.ddf.EscherDgRecord;
import org.apache.poi.ddf.EscherDggRecord;
import org.apache.poi.ddf.EscherDggRecord.FileIdCluster;
import org.apache.poi.ddf.EscherOptRecord;
import org.apache.poi.ddf.EscherProperties;
import org.apache.poi.ddf.EscherRGBProperty;
import org.apache.poi.ddf.EscherRecord;
import org.apache.poi.ddf.EscherSpRecord;
import org.apache.poi.ddf.EscherSplitMenuColorsRecord;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BackupRecord;
import org.apache.poi.hssf.record.BookBoolRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.CodepageRecord;
import org.apache.poi.hssf.record.CountryRecord;
import org.apache.poi.hssf.record.DSFRecord;
import org.apache.poi.hssf.record.DateWindow1904Record;
import org.apache.poi.hssf.record.DrawingGroupRecord;
import org.apache.poi.hssf.record.EOFRecord;
import org.apache.poi.hssf.record.EscherAggregate;
import org.apache.poi.hssf.record.ExtSSTRecord;
import org.apache.poi.hssf.record.ExtendedFormatRecord;
import org.apache.poi.hssf.record.FileSharingRecord;
import org.apache.poi.hssf.record.FnGroupCountRecord;
import org.apache.poi.hssf.record.FontRecord;
import org.apache.poi.hssf.record.FormatRecord;
import org.apache.poi.hssf.record.HideObjRecord;
import org.apache.poi.hssf.record.HyperlinkRecord;
import org.apache.poi.hssf.record.InterfaceEndRecord;
import org.apache.poi.hssf.record.InterfaceHdrRecord;
import org.apache.poi.hssf.record.MMSRecord;
import org.apache.poi.hssf.record.NameCommentRecord;
import org.apache.poi.hssf.record.NameRecord;
import org.apache.poi.hssf.record.PaletteRecord;
import org.apache.poi.hssf.record.PasswordRecord;
import org.apache.poi.hssf.record.PasswordRev4Record;
import org.apache.poi.hssf.record.PrecisionRecord;
import org.apache.poi.hssf.record.ProtectRecord;
import org.apache.poi.hssf.record.ProtectionRev4Record;
import org.apache.poi.hssf.record.RecalcIdRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RefreshAllRecord;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.hssf.record.StyleRecord;
import org.apache.poi.hssf.record.TabIdRecord;
import org.apache.poi.hssf.record.UseSelFSRecord;
import org.apache.poi.hssf.record.WindowOneRecord;
import org.apache.poi.hssf.record.WindowProtectRecord;
import org.apache.poi.hssf.record.WriteAccessRecord;
import org.apache.poi.hssf.record.WriteProtectRecord;
import org.apache.poi.hssf.record.common.UnicodeString;
import org.apache.poi.hssf.record.formula.FormulaShifter;
import org.apache.poi.hssf.record.formula.NameXPtg;
import org.apache.poi.hssf.record.formula.Ptg;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.p009ss.formula.EvaluationWorkbook.ExternalName;
import org.apache.poi.p009ss.formula.EvaluationWorkbook.ExternalSheet;
import org.apache.poi.p009ss.usermodel.Font;
import org.apache.poi.p009ss.usermodel.ShapeTypes;
import org.apache.poi.util.Internal;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

@Internal
public final class InternalWorkbook {
    private static final short CODEPAGE = 1200;
    private static final int DEBUG = POILogger.DEBUG;
    private static final int MAX_SENSITIVE_SHEET_NAME_LEN = 31;
    private static final POILogger log = POILogFactory.getLogger(InternalWorkbook.class);
    private final List<BoundSheetRecord> boundsheets = new ArrayList();
    private final Map<String, NameCommentRecord> commentRecords = new LinkedHashMap();
    private DrawingManager2 drawingManager;
    private List<EscherBSERecord> escherBSERecords = new ArrayList();
    private FileSharingRecord fileShare;
    private final List<FormatRecord> formats = new ArrayList();
    private final List<HyperlinkRecord> hyperlinks = new ArrayList();
    private LinkTable linkTable;
    private int maxformatid = -1;
    private int numfonts = 0;
    private int numxfs = 0;
    private final WorkbookRecordList records = new WorkbookRecordList();
    protected SSTRecord sst;
    private boolean uses1904datewindowing = false;
    private WindowOneRecord windowOne;
    private WriteAccessRecord writeAccess;
    private WriteProtectRecord writeProtect;

    private InternalWorkbook() {
    }

    public static InternalWorkbook createWorkbook(List<Record> recs) {
        if (log.check(POILogger.DEBUG)) {
            log.log(DEBUG, (Object) "Workbook (readfile) created with reclen=", (Object) Integer.valueOf(recs.size()));
        }
        InternalWorkbook retval = new InternalWorkbook();
        List<Record> records2 = new ArrayList<>(recs.size() / 3);
        retval.records.setRecords(records2);
        int k = 0;
        while (true) {
            if (k < recs.size()) {
                Record rec = (Record) recs.get(k);
                if (rec.getSid() == 10) {
                    records2.add(rec);
                    if (log.check(POILogger.DEBUG)) {
                        POILogger pOILogger = log;
                        int i = DEBUG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("found workbook eof record at ");
                        sb.append(k);
                        pOILogger.log(i, (Object) sb.toString());
                    }
                } else {
                    switch (rec.getSid()) {
                        case 18:
                            if (log.check(POILogger.DEBUG)) {
                                POILogger pOILogger2 = log;
                                int i2 = DEBUG;
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("found protect record at ");
                                sb2.append(k);
                                pOILogger2.log(i2, (Object) sb2.toString());
                            }
                            retval.records.setProtpos(k);
                            break;
                        case 23:
                            throw new RuntimeException("Extern sheet is part of LinkTable");
                        case 24:
                        case 430:
                            if (log.check(POILogger.DEBUG)) {
                                POILogger pOILogger3 = log;
                                int i3 = DEBUG;
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("found SupBook record at ");
                                sb3.append(k);
                                pOILogger3.log(i3, (Object) sb3.toString());
                            }
                            retval.linkTable = new LinkTable(recs, k, retval.records, retval.commentRecords);
                            k += retval.linkTable.getRecordCount() - 1;
                            continue;
                        case 34:
                            if (log.check(POILogger.DEBUG)) {
                                POILogger pOILogger4 = log;
                                int i4 = DEBUG;
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append("found datewindow1904 record at ");
                                sb4.append(k);
                                pOILogger4.log(i4, (Object) sb4.toString());
                            }
                            retval.uses1904datewindowing = ((DateWindow1904Record) rec).getWindowing() == 1;
                            break;
                        case 49:
                            if (log.check(POILogger.DEBUG)) {
                                POILogger pOILogger5 = log;
                                int i5 = DEBUG;
                                StringBuilder sb5 = new StringBuilder();
                                sb5.append("found font record at ");
                                sb5.append(k);
                                pOILogger5.log(i5, (Object) sb5.toString());
                            }
                            retval.records.setFontpos(k);
                            retval.numfonts++;
                            break;
                        case 61:
                            if (log.check(POILogger.DEBUG)) {
                                POILogger pOILogger6 = log;
                                int i6 = DEBUG;
                                StringBuilder sb6 = new StringBuilder();
                                sb6.append("found WindowOneRecord at ");
                                sb6.append(k);
                                pOILogger6.log(i6, (Object) sb6.toString());
                            }
                            retval.windowOne = (WindowOneRecord) rec;
                            break;
                        case 64:
                            if (log.check(POILogger.DEBUG)) {
                                POILogger pOILogger7 = log;
                                int i7 = DEBUG;
                                StringBuilder sb7 = new StringBuilder();
                                sb7.append("found backup record at ");
                                sb7.append(k);
                                pOILogger7.log(i7, (Object) sb7.toString());
                            }
                            retval.records.setBackuppos(k);
                            break;
                        case 91:
                            if (log.check(POILogger.DEBUG)) {
                                POILogger pOILogger8 = log;
                                int i8 = DEBUG;
                                StringBuilder sb8 = new StringBuilder();
                                sb8.append("found FileSharing at ");
                                sb8.append(k);
                                pOILogger8.log(i8, (Object) sb8.toString());
                            }
                            retval.fileShare = (FileSharingRecord) rec;
                            break;
                        case 92:
                            if (log.check(POILogger.DEBUG)) {
                                POILogger pOILogger9 = log;
                                int i9 = DEBUG;
                                StringBuilder sb9 = new StringBuilder();
                                sb9.append("found WriteAccess at ");
                                sb9.append(k);
                                pOILogger9.log(i9, (Object) sb9.toString());
                            }
                            retval.writeAccess = (WriteAccessRecord) rec;
                            break;
                        case ShapeTypes.FLOW_CHART_DECISION /*133*/:
                            if (log.check(POILogger.DEBUG)) {
                                POILogger pOILogger10 = log;
                                int i10 = DEBUG;
                                StringBuilder sb10 = new StringBuilder();
                                sb10.append("found boundsheet record at ");
                                sb10.append(k);
                                pOILogger10.log(i10, (Object) sb10.toString());
                            }
                            retval.boundsheets.add((BoundSheetRecord) rec);
                            retval.records.setBspos(k);
                            break;
                        case ShapeTypes.FLOW_CHART_INPUT_OUTPUT /*134*/:
                            if (log.check(POILogger.DEBUG)) {
                                POILogger pOILogger11 = log;
                                int i11 = DEBUG;
                                StringBuilder sb11 = new StringBuilder();
                                sb11.append("found WriteProtect at ");
                                sb11.append(k);
                                pOILogger11.log(i11, (Object) sb11.toString());
                            }
                            retval.writeProtect = (WriteProtectRecord) rec;
                            break;
                        case ShapeTypes.FLOW_CHART_SUMMING_JUNCTION /*146*/:
                            if (log.check(POILogger.DEBUG)) {
                                POILogger pOILogger12 = log;
                                int i12 = DEBUG;
                                StringBuilder sb12 = new StringBuilder();
                                sb12.append("found palette record at ");
                                sb12.append(k);
                                pOILogger12.log(i12, (Object) sb12.toString());
                            }
                            retval.records.setPalettepos(k);
                            break;
                        case 224:
                            if (log.check(POILogger.DEBUG)) {
                                POILogger pOILogger13 = log;
                                int i13 = DEBUG;
                                StringBuilder sb13 = new StringBuilder();
                                sb13.append("found XF record at ");
                                sb13.append(k);
                                pOILogger13.log(i13, (Object) sb13.toString());
                            }
                            retval.records.setXfpos(k);
                            retval.numxfs++;
                            break;
                        case 252:
                            if (log.check(POILogger.DEBUG)) {
                                POILogger pOILogger14 = log;
                                int i14 = DEBUG;
                                StringBuilder sb14 = new StringBuilder();
                                sb14.append("found sst record at ");
                                sb14.append(k);
                                pOILogger14.log(i14, (Object) sb14.toString());
                            }
                            retval.sst = (SSTRecord) rec;
                            break;
                        case 317:
                            if (log.check(POILogger.DEBUG)) {
                                POILogger pOILogger15 = log;
                                int i15 = DEBUG;
                                StringBuilder sb15 = new StringBuilder();
                                sb15.append("found tabid record at ");
                                sb15.append(k);
                                pOILogger15.log(i15, (Object) sb15.toString());
                            }
                            retval.records.setTabpos(k);
                            break;
                        case 1054:
                            if (log.check(POILogger.DEBUG)) {
                                POILogger pOILogger16 = log;
                                int i16 = DEBUG;
                                StringBuilder sb16 = new StringBuilder();
                                sb16.append("found format record at ");
                                sb16.append(k);
                                pOILogger16.log(i16, (Object) sb16.toString());
                            }
                            retval.formats.add((FormatRecord) rec);
                            retval.maxformatid = retval.maxformatid >= ((FormatRecord) rec).getIndexCode() ? retval.maxformatid : ((FormatRecord) rec).getIndexCode();
                            break;
                        case 2196:
                            NameCommentRecord ncr = (NameCommentRecord) rec;
                            if (log.check(POILogger.DEBUG)) {
                                POILogger pOILogger17 = log;
                                int i17 = DEBUG;
                                StringBuilder sb17 = new StringBuilder();
                                sb17.append("found NameComment at ");
                                sb17.append(k);
                                pOILogger17.log(i17, (Object) sb17.toString());
                            }
                            retval.commentRecords.put(ncr.getNameText(), ncr);
                            break;
                    }
                    records2.add(rec);
                    k++;
                }
            }
        }
        while (k < recs.size()) {
            Record rec2 = (Record) recs.get(k);
            if (rec2.getSid() == 440) {
                retval.hyperlinks.add((HyperlinkRecord) rec2);
            }
            k++;
        }
        if (retval.windowOne == null) {
            retval.windowOne = createWindowOne();
        }
        if (log.check(POILogger.DEBUG)) {
            log.log(DEBUG, (Object) "exit create workbook from existing file function");
        }
        return retval;
    }

    public static InternalWorkbook createWorkbook() {
        if (log.check(POILogger.DEBUG)) {
            log.log(DEBUG, (Object) "creating new workbook from scratch");
        }
        InternalWorkbook retval = new InternalWorkbook();
        List<Record> records2 = new ArrayList<>(30);
        retval.records.setRecords(records2);
        List<FormatRecord> formats2 = retval.formats;
        records2.add(createBOF());
        records2.add(new InterfaceHdrRecord(1200));
        records2.add(createMMS());
        records2.add(InterfaceEndRecord.instance);
        records2.add(createWriteAccess());
        records2.add(createCodepage());
        records2.add(createDSF());
        records2.add(createTabId());
        retval.records.setTabpos(records2.size() - 1);
        records2.add(createFnGroupCount());
        records2.add(createWindowProtect());
        records2.add(createProtect());
        retval.records.setProtpos(records2.size() - 1);
        records2.add(createPassword());
        records2.add(createProtectionRev4());
        records2.add(createPasswordRev4());
        retval.windowOne = createWindowOne();
        records2.add(retval.windowOne);
        records2.add(createBackup());
        retval.records.setBackuppos(records2.size() - 1);
        records2.add(createHideObj());
        records2.add(createDateWindow1904());
        records2.add(createPrecision());
        records2.add(createRefreshAll());
        records2.add(createBookBool());
        records2.add(createFont());
        records2.add(createFont());
        records2.add(createFont());
        records2.add(createFont());
        retval.records.setFontpos(records2.size() - 1);
        retval.numfonts = 4;
        for (int i = 0; i <= 7; i++) {
            FormatRecord rec = createFormat(i);
            retval.maxformatid = retval.maxformatid >= rec.getIndexCode() ? retval.maxformatid : rec.getIndexCode();
            formats2.add(rec);
            records2.add(rec);
        }
        for (int k = 0; k < 21; k++) {
            records2.add(createExtendedFormat(k));
            retval.numxfs++;
        }
        retval.records.setXfpos(records2.size() - 1);
        for (int k2 = 0; k2 < 6; k2++) {
            records2.add(createStyle(k2));
        }
        records2.add(createUseSelFS());
        for (int k3 = 0; k3 < 1; k3++) {
            BoundSheetRecord bsr = createBoundSheet(k3);
            records2.add(bsr);
            retval.boundsheets.add(bsr);
            retval.records.setBspos(records2.size() - 1);
        }
        records2.add(createCountry());
        for (int k4 = 0; k4 < 1; k4++) {
            retval.getOrCreateLinkTable().checkExternSheet(k4);
        }
        retval.sst = new SSTRecord();
        records2.add(retval.sst);
        records2.add(createExtendedSST());
        records2.add(EOFRecord.instance);
        if (log.check(POILogger.DEBUG)) {
            log.log(DEBUG, (Object) "exit create new workbook from scratch");
        }
        return retval;
    }

    public NameRecord getSpecificBuiltinRecord(byte name, int sheetNumber) {
        return getOrCreateLinkTable().getSpecificBuiltinRecord(name, sheetNumber);
    }

    public void removeBuiltinRecord(byte name, int sheetIndex) {
        this.linkTable.removeBuiltinRecord(name, sheetIndex);
    }

    public int getNumRecords() {
        return this.records.size();
    }

    public FontRecord getFontRecordAt(int idx) {
        int index = idx;
        if (index > 4) {
            index--;
        }
        if (index <= this.numfonts - 1) {
            return (FontRecord) this.records.get((this.records.getFontpos() - (this.numfonts - 1)) + index);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("There are only ");
        sb.append(this.numfonts);
        sb.append(" font records, you asked for ");
        sb.append(idx);
        throw new ArrayIndexOutOfBoundsException(sb.toString());
    }

    public int getFontIndex(FontRecord font) {
        int i = 0;
        while (i <= this.numfonts) {
            if (((FontRecord) this.records.get((this.records.getFontpos() - (this.numfonts - 1)) + i)) != font) {
                i++;
            } else if (i > 3) {
                return i + 1;
            } else {
                return i;
            }
        }
        throw new IllegalArgumentException("Could not find that font!");
    }

    public FontRecord createNewFont() {
        FontRecord rec = createFont();
        this.records.add(this.records.getFontpos() + 1, rec);
        this.records.setFontpos(this.records.getFontpos() + 1);
        this.numfonts++;
        return rec;
    }

    public void removeFontRecord(FontRecord rec) {
        this.records.remove((Object) rec);
        this.numfonts--;
    }

    public int getNumberOfFontRecords() {
        return this.numfonts;
    }

    public void setSheetBof(int sheetIndex, int pos) {
        if (log.check(POILogger.DEBUG)) {
            log.log(DEBUG, (Object) "setting bof for sheetnum =", (Object) Integer.valueOf(sheetIndex), (Object) " at pos=", (Object) Integer.valueOf(pos));
        }
        checkSheets(sheetIndex);
        getBoundSheetRec(sheetIndex).setPositionOfBof(pos);
    }

    private BoundSheetRecord getBoundSheetRec(int sheetIndex) {
        return (BoundSheetRecord) this.boundsheets.get(sheetIndex);
    }

    public BackupRecord getBackupRecord() {
        return (BackupRecord) this.records.get(this.records.getBackuppos());
    }

    public void setSheetName(int sheetnum, String sheetname) {
        checkSheets(sheetnum);
        ((BoundSheetRecord) this.boundsheets.get(sheetnum)).setSheetname(sheetname);
    }

    public boolean doesContainsSheetName(String name, int excludeSheetIdx) {
        String aName = name;
        if (aName.length() > 31) {
            aName = aName.substring(0, 31);
        }
        for (int i = 0; i < this.boundsheets.size(); i++) {
            BoundSheetRecord boundSheetRecord = getBoundSheetRec(i);
            if (excludeSheetIdx != i) {
                String bName = boundSheetRecord.getSheetname();
                if (bName.length() > 31) {
                    bName = bName.substring(0, 31);
                }
                if (aName.equalsIgnoreCase(bName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setSheetOrder(String sheetname, int pos) {
        this.boundsheets.add(pos, this.boundsheets.remove(getSheetIndex(sheetname)));
    }

    public String getSheetName(int sheetIndex) {
        return getBoundSheetRec(sheetIndex).getSheetname();
    }

    public boolean isSheetHidden(int sheetnum) {
        return getBoundSheetRec(sheetnum).isHidden();
    }

    public boolean isSheetVeryHidden(int sheetnum) {
        return getBoundSheetRec(sheetnum).isVeryHidden();
    }

    public void setSheetHidden(int sheetnum, boolean hidden) {
        getBoundSheetRec(sheetnum).setHidden(hidden);
    }

    public void setSheetHidden(int sheetnum, int hidden) {
        BoundSheetRecord bsr = getBoundSheetRec(sheetnum);
        boolean h = false;
        boolean vh = false;
        if (hidden != 0) {
            if (hidden == 1) {
                h = true;
            } else if (hidden == 2) {
                vh = true;
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Invalid hidden flag ");
                sb.append(hidden);
                sb.append(" given, must be 0, 1 or 2");
                throw new IllegalArgumentException(sb.toString());
            }
        }
        bsr.setHidden(h);
        bsr.setVeryHidden(vh);
    }

    public int getSheetIndex(String name) {
        for (int k = 0; k < this.boundsheets.size(); k++) {
            if (getSheetName(k).equalsIgnoreCase(name)) {
                return k;
            }
        }
        return -1;
    }

    private void checkSheets(int sheetnum) {
        if (this.boundsheets.size() <= sheetnum) {
            if (this.boundsheets.size() + 1 <= sheetnum) {
                throw new RuntimeException("Sheet number out of bounds!");
            }
            BoundSheetRecord bsr = createBoundSheet(sheetnum);
            this.records.add(this.records.getBspos() + 1, bsr);
            this.records.setBspos(this.records.getBspos() + 1);
            this.boundsheets.add(bsr);
            getOrCreateLinkTable().checkExternSheet(sheetnum);
            fixTabIdRecord();
        } else if (this.records.getTabpos() > 0 && ((TabIdRecord) this.records.get(this.records.getTabpos()))._tabids.length < this.boundsheets.size()) {
            fixTabIdRecord();
        }
    }

    public void removeSheet(int sheetIndex) {
        if (this.boundsheets.size() > sheetIndex) {
            this.records.remove((this.records.getBspos() - (this.boundsheets.size() - 1)) + sheetIndex);
            this.boundsheets.remove(sheetIndex);
            fixTabIdRecord();
        }
        int sheetNum1Based = sheetIndex + 1;
        for (int i = 0; i < getNumNames(); i++) {
            NameRecord nr = getNameRecord(i);
            if (nr.getSheetNumber() == sheetNum1Based) {
                nr.setSheetNumber(0);
            } else if (nr.getSheetNumber() > sheetNum1Based) {
                nr.setSheetNumber(nr.getSheetNumber() - 1);
            }
        }
    }

    private void fixTabIdRecord() {
        TabIdRecord tir = (TabIdRecord) this.records.get(this.records.getTabpos());
        short[] tia = new short[this.boundsheets.size()];
        for (short k = 0; k < tia.length; k = (short) (k + 1)) {
            tia[k] = k;
        }
        tir.setTabIdArray(tia);
    }

    public int getNumSheets() {
        if (log.check(POILogger.DEBUG)) {
            log.log(DEBUG, (Object) "getNumSheets=", (Object) Integer.valueOf(this.boundsheets.size()));
        }
        return this.boundsheets.size();
    }

    public int getNumExFormats() {
        if (log.check(POILogger.DEBUG)) {
            log.log(DEBUG, (Object) "getXF=", (Object) Integer.valueOf(this.numxfs));
        }
        return this.numxfs;
    }

    public ExtendedFormatRecord getExFormatAt(int index) {
        return (ExtendedFormatRecord) this.records.get((this.records.getXfpos() - (this.numxfs - 1)) + index);
    }

    public void removeExFormatRecord(ExtendedFormatRecord rec) {
        this.records.remove((Object) rec);
        this.numxfs--;
    }

    public ExtendedFormatRecord createCellXF() {
        ExtendedFormatRecord xf = createExtendedFormat();
        this.records.add(this.records.getXfpos() + 1, xf);
        this.records.setXfpos(this.records.getXfpos() + 1);
        this.numxfs++;
        return xf;
    }

    public StyleRecord getStyleRecord(int xfIndex) {
        for (int i = this.records.getXfpos(); i < this.records.size(); i++) {
            Record r = this.records.get(i);
            if (!(r instanceof ExtendedFormatRecord) && (r instanceof StyleRecord)) {
                StyleRecord sr = (StyleRecord) r;
                if (sr.getXFIndex() == xfIndex) {
                    return sr;
                }
            }
        }
        return null;
    }

    public StyleRecord createStyleRecord(int xfIndex) {
        StyleRecord newSR = new StyleRecord();
        newSR.setXFIndex(xfIndex);
        int addAt = -1;
        for (int i = this.records.getXfpos(); i < this.records.size() && addAt == -1; i++) {
            Record r = this.records.get(i);
            if (!(r instanceof ExtendedFormatRecord) && !(r instanceof StyleRecord)) {
                addAt = i;
            }
        }
        if (addAt == -1) {
            throw new IllegalStateException("No XF Records found!");
        }
        this.records.add(addAt, newSR);
        return newSR;
    }

    public int addSSTString(UnicodeString string) {
        if (log.check(POILogger.DEBUG)) {
            log.log(DEBUG, (Object) "insert to sst string='", (Object) string);
        }
        if (this.sst == null) {
            insertSST();
        }
        return this.sst.addString(string);
    }

    public UnicodeString getSSTString(int str) {
        if (this.sst == null) {
            insertSST();
        }
        UnicodeString retval = this.sst.getString(str);
        if (log.check(POILogger.DEBUG)) {
            log.log(DEBUG, (Object) "Returning SST for index=", (Object) Integer.valueOf(str), (Object) " String= ", (Object) retval);
        }
        return retval;
    }

    public void insertSST() {
        if (log.check(POILogger.DEBUG)) {
            log.log(DEBUG, (Object) "creating new SST via insertSST!");
        }
        this.sst = new SSTRecord();
        this.records.add(this.records.size() - 1, createExtendedSST());
        this.records.add(this.records.size() - 2, this.sst);
    }

    public int serialize(int offset, byte[] data) {
        if (log.check(POILogger.DEBUG)) {
            log.log(DEBUG, (Object) "Serializing Workbook with offsets");
        }
        boolean wroteBoundSheets = false;
        int sstPos = 0;
        SSTRecord sst2 = null;
        int pos = 0;
        for (int k = 0; k < this.records.size(); k++) {
            Record record = this.records.get(k);
            if (record.getSid() != 449 || ((RecalcIdRecord) record).isNeeded()) {
                int len = 0;
                if (record instanceof SSTRecord) {
                    sst2 = (SSTRecord) record;
                    sstPos = pos;
                }
                if (record.getSid() == 255 && sst2 != null) {
                    record = sst2.createExtSSTRecord(sstPos + offset);
                }
                if (!(record instanceof BoundSheetRecord)) {
                    len = record.serialize(pos + offset, data);
                } else if (!wroteBoundSheets) {
                    int len2 = 0;
                    for (int i = 0; i < this.boundsheets.size(); i++) {
                        len2 += getBoundSheetRec(i).serialize(pos + offset + len2, data);
                    }
                    wroteBoundSheets = true;
                    len = len2;
                }
                pos += len;
            }
        }
        if (log.check(POILogger.DEBUG)) {
            log.log(DEBUG, (Object) "Exiting serialize workbook");
        }
        return pos;
    }

    public int getSize() {
        int retval = 0;
        SSTRecord sst2 = null;
        for (int k = 0; k < this.records.size(); k++) {
            Record record = this.records.get(k);
            if (record.getSid() != 449 || ((RecalcIdRecord) record).isNeeded()) {
                if (record instanceof SSTRecord) {
                    sst2 = (SSTRecord) record;
                }
                if (record.getSid() != 255 || sst2 == null) {
                    retval += record.getRecordSize();
                } else {
                    retval += sst2.calcExtSSTRecordSize();
                }
            }
        }
        return retval;
    }

    private static BOFRecord createBOF() {
        BOFRecord retval = new BOFRecord();
        retval.setVersion(BOFRecord.VERSION);
        retval.setType(5);
        retval.setBuild(BOFRecord.BUILD);
        retval.setBuildYear(BOFRecord.BUILD_YEAR);
        retval.setHistoryBitMask(65);
        retval.setRequiredVersion(6);
        return retval;
    }

    private static MMSRecord createMMS() {
        MMSRecord retval = new MMSRecord();
        retval.setAddMenuCount(0);
        retval.setDelMenuCount(0);
        return retval;
    }

    private static WriteAccessRecord createWriteAccess() {
        WriteAccessRecord retval = new WriteAccessRecord();
        try {
            retval.setUsername(System.getProperty("user.name"));
        } catch (AccessControlException e) {
            retval.setUsername("POI");
        }
        return retval;
    }

    private static CodepageRecord createCodepage() {
        CodepageRecord retval = new CodepageRecord();
        retval.setCodepage(1200);
        return retval;
    }

    private static DSFRecord createDSF() {
        return new DSFRecord(false);
    }

    private static TabIdRecord createTabId() {
        return new TabIdRecord();
    }

    private static FnGroupCountRecord createFnGroupCount() {
        FnGroupCountRecord retval = new FnGroupCountRecord();
        retval.setCount(14);
        return retval;
    }

    private static WindowProtectRecord createWindowProtect() {
        return new WindowProtectRecord(false);
    }

    private static ProtectRecord createProtect() {
        return new ProtectRecord(false);
    }

    private static PasswordRecord createPassword() {
        return new PasswordRecord(0);
    }

    private static ProtectionRev4Record createProtectionRev4() {
        return new ProtectionRev4Record(false);
    }

    private static PasswordRev4Record createPasswordRev4() {
        return new PasswordRev4Record(0);
    }

    private static WindowOneRecord createWindowOne() {
        WindowOneRecord retval = new WindowOneRecord();
        retval.setHorizontalHold(360);
        retval.setVerticalHold(EscherProperties.BLIP__PICTURELINE);
        retval.setWidth(14940);
        retval.setHeight(9150);
        retval.setOptions(56);
        retval.setActiveSheetIndex(0);
        retval.setFirstVisibleTab(0);
        retval.setNumSelectedTabs(1);
        retval.setTabWidthRatio(600);
        return retval;
    }

    private static BackupRecord createBackup() {
        BackupRecord retval = new BackupRecord();
        retval.setBackup(0);
        return retval;
    }

    private static HideObjRecord createHideObj() {
        HideObjRecord retval = new HideObjRecord();
        retval.setHideObj(0);
        return retval;
    }

    private static DateWindow1904Record createDateWindow1904() {
        DateWindow1904Record retval = new DateWindow1904Record();
        retval.setWindowing(0);
        return retval;
    }

    private static PrecisionRecord createPrecision() {
        PrecisionRecord retval = new PrecisionRecord();
        retval.setFullPrecision(true);
        return retval;
    }

    private static RefreshAllRecord createRefreshAll() {
        return new RefreshAllRecord(false);
    }

    private static BookBoolRecord createBookBool() {
        BookBoolRecord retval = new BookBoolRecord();
        retval.setSaveLinkValues(0);
        return retval;
    }

    private static FontRecord createFont() {
        FontRecord retval = new FontRecord();
        retval.setFontHeight(EscherAggregate.ST_ACTIONBUTTONMOVIE);
        retval.setAttributes(0);
        retval.setColorPaletteIndex(Font.COLOR_NORMAL);
        retval.setBoldWeight(400);
        retval.setFontName(HSSFFont.FONT_ARIAL);
        return retval;
    }

    private static FormatRecord createFormat(int id) {
        switch (id) {
            case 0:
                return new FormatRecord(5, "\"$\"#,##0_);\\(\"$\"#,##0\\)");
            case 1:
                return new FormatRecord(6, "\"$\"#,##0_);[Red]\\(\"$\"#,##0\\)");
            case 2:
                return new FormatRecord(7, "\"$\"#,##0.00_);\\(\"$\"#,##0.00\\)");
            case 3:
                return new FormatRecord(8, "\"$\"#,##0.00_);[Red]\\(\"$\"#,##0.00\\)");
            case 4:
                return new FormatRecord(42, "_(\"$\"* #,##0_);_(\"$\"* \\(#,##0\\);_(\"$\"* \"-\"_);_(@_)");
            case 5:
                return new FormatRecord(41, "_(* #,##0_);_(* \\(#,##0\\);_(* \"-\"_);_(@_)");
            case 6:
                return new FormatRecord(44, "_(\"$\"* #,##0.00_);_(\"$\"* \\(#,##0.00\\);_(\"$\"* \"-\"??_);_(@_)");
            case 7:
                return new FormatRecord(43, "_(* #,##0.00_);_(* \\(#,##0.00\\);_(* \"-\"??_);_(@_)");
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unexpected id ");
                sb.append(id);
                throw new IllegalArgumentException(sb.toString());
        }
    }

    private static ExtendedFormatRecord createExtendedFormat(int id) {
        ExtendedFormatRecord retval = new ExtendedFormatRecord();
        switch (id) {
            case 0:
                retval.setFontIndex(0);
                retval.setFormatIndex(0);
                retval.setCellOptions(-11);
                retval.setAlignmentOptions(32);
                retval.setIndentionOptions(0);
                retval.setBorderOptions(0);
                retval.setPaletteOptions(0);
                retval.setAdtlPaletteOptions(0);
                retval.setFillPaletteOptions(8384);
                break;
            case 1:
                retval.setFontIndex(1);
                retval.setFormatIndex(0);
                retval.setCellOptions(-11);
                retval.setAlignmentOptions(32);
                retval.setIndentionOptions(-3072);
                retval.setBorderOptions(0);
                retval.setPaletteOptions(0);
                retval.setAdtlPaletteOptions(0);
                retval.setFillPaletteOptions(8384);
                break;
            case 2:
                retval.setFontIndex(1);
                retval.setFormatIndex(0);
                retval.setCellOptions(-11);
                retval.setAlignmentOptions(32);
                retval.setIndentionOptions(-3072);
                retval.setBorderOptions(0);
                retval.setPaletteOptions(0);
                retval.setAdtlPaletteOptions(0);
                retval.setFillPaletteOptions(8384);
                break;
            case 3:
                retval.setFontIndex(2);
                retval.setFormatIndex(0);
                retval.setCellOptions(-11);
                retval.setAlignmentOptions(32);
                retval.setIndentionOptions(-3072);
                retval.setBorderOptions(0);
                retval.setPaletteOptions(0);
                retval.setAdtlPaletteOptions(0);
                retval.setFillPaletteOptions(8384);
                break;
            case 4:
                retval.setFontIndex(2);
                retval.setFormatIndex(0);
                retval.setCellOptions(-11);
                retval.setAlignmentOptions(32);
                retval.setIndentionOptions(-3072);
                retval.setBorderOptions(0);
                retval.setPaletteOptions(0);
                retval.setAdtlPaletteOptions(0);
                retval.setFillPaletteOptions(8384);
                break;
            case 5:
                retval.setFontIndex(0);
                retval.setFormatIndex(0);
                retval.setCellOptions(-11);
                retval.setAlignmentOptions(32);
                retval.setIndentionOptions(-3072);
                retval.setBorderOptions(0);
                retval.setPaletteOptions(0);
                retval.setAdtlPaletteOptions(0);
                retval.setFillPaletteOptions(8384);
                break;
            case 6:
                retval.setFontIndex(0);
                retval.setFormatIndex(0);
                retval.setCellOptions(-11);
                retval.setAlignmentOptions(32);
                retval.setIndentionOptions(-3072);
                retval.setBorderOptions(0);
                retval.setPaletteOptions(0);
                retval.setAdtlPaletteOptions(0);
                retval.setFillPaletteOptions(8384);
                break;
            case 7:
                retval.setFontIndex(0);
                retval.setFormatIndex(0);
                retval.setCellOptions(-11);
                retval.setAlignmentOptions(32);
                retval.setIndentionOptions(-3072);
                retval.setBorderOptions(0);
                retval.setPaletteOptions(0);
                retval.setAdtlPaletteOptions(0);
                retval.setFillPaletteOptions(8384);
                break;
            case 8:
                retval.setFontIndex(0);
                retval.setFormatIndex(0);
                retval.setCellOptions(-11);
                retval.setAlignmentOptions(32);
                retval.setIndentionOptions(-3072);
                retval.setBorderOptions(0);
                retval.setPaletteOptions(0);
                retval.setAdtlPaletteOptions(0);
                retval.setFillPaletteOptions(8384);
                break;
            case 9:
                retval.setFontIndex(0);
                retval.setFormatIndex(0);
                retval.setCellOptions(-11);
                retval.setAlignmentOptions(32);
                retval.setIndentionOptions(-3072);
                retval.setBorderOptions(0);
                retval.setPaletteOptions(0);
                retval.setAdtlPaletteOptions(0);
                retval.setFillPaletteOptions(8384);
                break;
            case 10:
                retval.setFontIndex(0);
                retval.setFormatIndex(0);
                retval.setCellOptions(-11);
                retval.setAlignmentOptions(32);
                retval.setIndentionOptions(-3072);
                retval.setBorderOptions(0);
                retval.setPaletteOptions(0);
                retval.setAdtlPaletteOptions(0);
                retval.setFillPaletteOptions(8384);
                break;
            case 11:
                retval.setFontIndex(0);
                retval.setFormatIndex(0);
                retval.setCellOptions(-11);
                retval.setAlignmentOptions(32);
                retval.setIndentionOptions(-3072);
                retval.setBorderOptions(0);
                retval.setPaletteOptions(0);
                retval.setAdtlPaletteOptions(0);
                retval.setFillPaletteOptions(8384);
                break;
            case 12:
                retval.setFontIndex(0);
                retval.setFormatIndex(0);
                retval.setCellOptions(-11);
                retval.setAlignmentOptions(32);
                retval.setIndentionOptions(-3072);
                retval.setBorderOptions(0);
                retval.setPaletteOptions(0);
                retval.setAdtlPaletteOptions(0);
                retval.setFillPaletteOptions(8384);
                break;
            case 13:
                retval.setFontIndex(0);
                retval.setFormatIndex(0);
                retval.setCellOptions(-11);
                retval.setAlignmentOptions(32);
                retval.setIndentionOptions(-3072);
                retval.setBorderOptions(0);
                retval.setPaletteOptions(0);
                retval.setAdtlPaletteOptions(0);
                retval.setFillPaletteOptions(8384);
                break;
            case 14:
                retval.setFontIndex(0);
                retval.setFormatIndex(0);
                retval.setCellOptions(-11);
                retval.setAlignmentOptions(32);
                retval.setIndentionOptions(-3072);
                retval.setBorderOptions(0);
                retval.setPaletteOptions(0);
                retval.setAdtlPaletteOptions(0);
                retval.setFillPaletteOptions(8384);
                break;
            case 15:
                retval.setFontIndex(0);
                retval.setFormatIndex(0);
                retval.setCellOptions(1);
                retval.setAlignmentOptions(32);
                retval.setIndentionOptions(0);
                retval.setBorderOptions(0);
                retval.setPaletteOptions(0);
                retval.setAdtlPaletteOptions(0);
                retval.setFillPaletteOptions(8384);
                break;
            case 16:
                retval.setFontIndex(1);
                retval.setFormatIndex(43);
                retval.setCellOptions(-11);
                retval.setAlignmentOptions(32);
                retval.setIndentionOptions(-2048);
                retval.setBorderOptions(0);
                retval.setPaletteOptions(0);
                retval.setAdtlPaletteOptions(0);
                retval.setFillPaletteOptions(8384);
                break;
            case 17:
                retval.setFontIndex(1);
                retval.setFormatIndex(41);
                retval.setCellOptions(-11);
                retval.setAlignmentOptions(32);
                retval.setIndentionOptions(-2048);
                retval.setBorderOptions(0);
                retval.setPaletteOptions(0);
                retval.setAdtlPaletteOptions(0);
                retval.setFillPaletteOptions(8384);
                break;
            case 18:
                retval.setFontIndex(1);
                retval.setFormatIndex(44);
                retval.setCellOptions(-11);
                retval.setAlignmentOptions(32);
                retval.setIndentionOptions(-2048);
                retval.setBorderOptions(0);
                retval.setPaletteOptions(0);
                retval.setAdtlPaletteOptions(0);
                retval.setFillPaletteOptions(8384);
                break;
            case 19:
                retval.setFontIndex(1);
                retval.setFormatIndex(42);
                retval.setCellOptions(-11);
                retval.setAlignmentOptions(32);
                retval.setIndentionOptions(-2048);
                retval.setBorderOptions(0);
                retval.setPaletteOptions(0);
                retval.setAdtlPaletteOptions(0);
                retval.setFillPaletteOptions(8384);
                break;
            case 20:
                retval.setFontIndex(1);
                retval.setFormatIndex(9);
                retval.setCellOptions(-11);
                retval.setAlignmentOptions(32);
                retval.setIndentionOptions(-2048);
                retval.setBorderOptions(0);
                retval.setPaletteOptions(0);
                retval.setAdtlPaletteOptions(0);
                retval.setFillPaletteOptions(8384);
                break;
            case 21:
                retval.setFontIndex(5);
                retval.setFormatIndex(0);
                retval.setCellOptions(1);
                retval.setAlignmentOptions(32);
                retval.setIndentionOptions(2048);
                retval.setBorderOptions(0);
                retval.setPaletteOptions(0);
                retval.setAdtlPaletteOptions(0);
                retval.setFillPaletteOptions(8384);
                break;
            case 22:
                retval.setFontIndex(6);
                retval.setFormatIndex(0);
                retval.setCellOptions(1);
                retval.setAlignmentOptions(32);
                retval.setIndentionOptions(23552);
                retval.setBorderOptions(0);
                retval.setPaletteOptions(0);
                retval.setAdtlPaletteOptions(0);
                retval.setFillPaletteOptions(8384);
                break;
            case 23:
                retval.setFontIndex(0);
                retval.setFormatIndex(49);
                retval.setCellOptions(1);
                retval.setAlignmentOptions(32);
                retval.setIndentionOptions(23552);
                retval.setBorderOptions(0);
                retval.setPaletteOptions(0);
                retval.setAdtlPaletteOptions(0);
                retval.setFillPaletteOptions(8384);
                break;
            case 24:
                retval.setFontIndex(0);
                retval.setFormatIndex(8);
                retval.setCellOptions(1);
                retval.setAlignmentOptions(32);
                retval.setIndentionOptions(23552);
                retval.setBorderOptions(0);
                retval.setPaletteOptions(0);
                retval.setAdtlPaletteOptions(0);
                retval.setFillPaletteOptions(8384);
                break;
            case 25:
                retval.setFontIndex(6);
                retval.setFormatIndex(8);
                retval.setCellOptions(1);
                retval.setAlignmentOptions(32);
                retval.setIndentionOptions(23552);
                retval.setBorderOptions(0);
                retval.setPaletteOptions(0);
                retval.setAdtlPaletteOptions(0);
                retval.setFillPaletteOptions(8384);
                break;
        }
        return retval;
    }

    private static ExtendedFormatRecord createExtendedFormat() {
        ExtendedFormatRecord retval = new ExtendedFormatRecord();
        retval.setFontIndex(0);
        retval.setFormatIndex(0);
        retval.setCellOptions(1);
        retval.setAlignmentOptions(32);
        retval.setIndentionOptions(0);
        retval.setBorderOptions(0);
        retval.setPaletteOptions(0);
        retval.setAdtlPaletteOptions(0);
        retval.setFillPaletteOptions(8384);
        retval.setTopBorderPaletteIdx(8);
        retval.setBottomBorderPaletteIdx(8);
        retval.setLeftBorderPaletteIdx(8);
        retval.setRightBorderPaletteIdx(8);
        return retval;
    }

    private static StyleRecord createStyle(int id) {
        StyleRecord retval = new StyleRecord();
        switch (id) {
            case 0:
                retval.setXFIndex(16);
                retval.setBuiltinStyle(3);
                retval.setOutlineStyleLevel(-1);
                break;
            case 1:
                retval.setXFIndex(17);
                retval.setBuiltinStyle(6);
                retval.setOutlineStyleLevel(-1);
                break;
            case 2:
                retval.setXFIndex(18);
                retval.setBuiltinStyle(4);
                retval.setOutlineStyleLevel(-1);
                break;
            case 3:
                retval.setXFIndex(19);
                retval.setBuiltinStyle(7);
                retval.setOutlineStyleLevel(-1);
                break;
            case 4:
                retval.setXFIndex(0);
                retval.setBuiltinStyle(0);
                retval.setOutlineStyleLevel(-1);
                break;
            case 5:
                retval.setXFIndex(20);
                retval.setBuiltinStyle(5);
                retval.setOutlineStyleLevel(-1);
                break;
        }
        return retval;
    }

    private static PaletteRecord createPalette() {
        return new PaletteRecord();
    }

    private static UseSelFSRecord createUseSelFS() {
        return new UseSelFSRecord(false);
    }

    private static BoundSheetRecord createBoundSheet(int id) {
        StringBuilder sb = new StringBuilder();
        sb.append("Sheet");
        sb.append(id + 1);
        return new BoundSheetRecord(sb.toString());
    }

    private static CountryRecord createCountry() {
        CountryRecord retval = new CountryRecord();
        retval.setDefaultCountry(1);
        if (Locale.getDefault().toString().equals("ru_RU")) {
            retval.setCurrentCountry(7);
        } else {
            retval.setCurrentCountry(1);
        }
        return retval;
    }

    private static ExtSSTRecord createExtendedSST() {
        ExtSSTRecord retval = new ExtSSTRecord();
        retval.setNumStringsPerBucket(8);
        return retval;
    }

    private LinkTable getOrCreateLinkTable() {
        if (this.linkTable == null) {
            this.linkTable = new LinkTable((short) getNumSheets(), this.records);
        }
        return this.linkTable;
    }

    public String findSheetNameFromExternSheet(int externSheetIndex) {
        int indexToSheet = this.linkTable.getIndexToInternalSheet(externSheetIndex);
        if (indexToSheet < 0) {
            return "";
        }
        if (indexToSheet >= this.boundsheets.size()) {
            return "";
        }
        return getSheetName(indexToSheet);
    }

    public ExternalSheet getExternalSheet(int externSheetIndex) {
        String[] extNames = this.linkTable.getExternalBookAndSheetName(externSheetIndex);
        if (extNames == null) {
            return null;
        }
        return new ExternalSheet(extNames[0], extNames[1]);
    }

    public ExternalName getExternalName(int externSheetIndex, int externNameIndex) {
        String nameName = this.linkTable.resolveNameXText(externSheetIndex, externNameIndex);
        if (nameName == null) {
            return null;
        }
        return new ExternalName(nameName, externNameIndex, this.linkTable.resolveNameXIx(externSheetIndex, externNameIndex));
    }

    public int getSheetIndexFromExternSheetIndex(int externSheetNumber) {
        return this.linkTable.getSheetIndexFromExternSheetIndex(externSheetNumber);
    }

    public short checkExternSheet(int sheetNumber) {
        return (short) getOrCreateLinkTable().checkExternSheet(sheetNumber);
    }

    public int getExternalSheetIndex(String workbookName, String sheetName) {
        return getOrCreateLinkTable().getExternalSheetIndex(workbookName, sheetName);
    }

    public int getNumNames() {
        if (this.linkTable == null) {
            return 0;
        }
        return this.linkTable.getNumNames();
    }

    public NameRecord getNameRecord(int index) {
        return this.linkTable.getNameRecord(index);
    }

    public NameCommentRecord getNameCommentRecord(NameRecord nameRecord) {
        return (NameCommentRecord) this.commentRecords.get(nameRecord.getNameText());
    }

    public NameRecord createName() {
        return addName(new NameRecord());
    }

    public NameRecord addName(NameRecord name) {
        getOrCreateLinkTable().addName(name);
        return name;
    }

    public NameRecord createBuiltInName(byte builtInName, int sheetNumber) {
        if (sheetNumber < 0 || sheetNumber + 1 > 32767) {
            StringBuilder sb = new StringBuilder();
            sb.append("Sheet number [");
            sb.append(sheetNumber);
            sb.append("]is not valid ");
            throw new IllegalArgumentException(sb.toString());
        }
        NameRecord name = new NameRecord(builtInName, sheetNumber);
        if (this.linkTable.nameAlreadyExists(name)) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Builtin (");
            sb2.append(builtInName);
            sb2.append(") already exists for sheet (");
            sb2.append(sheetNumber);
            sb2.append(")");
            throw new RuntimeException(sb2.toString());
        }
        addName(name);
        return name;
    }

    public void removeName(int nameIndex) {
        if (this.linkTable.getNumNames() > nameIndex) {
            this.records.remove(findFirstRecordLocBySid(24) + nameIndex);
            this.linkTable.removeName(nameIndex);
        }
    }

    public void updateNameCommentRecordCache(NameCommentRecord commentRecord) {
        if (this.commentRecords.containsValue(commentRecord)) {
            Iterator i$ = this.commentRecords.entrySet().iterator();
            while (true) {
                if (!i$.hasNext()) {
                    break;
                }
                Entry<String, NameCommentRecord> entry = (Entry) i$.next();
                if (((NameCommentRecord) entry.getValue()).equals(commentRecord)) {
                    this.commentRecords.remove(entry.getKey());
                    break;
                }
            }
        }
        this.commentRecords.put(commentRecord.getNameText(), commentRecord);
    }

    public short getFormat(String format, boolean createIfNotFound) {
        for (FormatRecord r : this.formats) {
            if (r.getFormatString().equals(format)) {
                return (short) r.getIndexCode();
            }
        }
        if (createIfNotFound) {
            return (short) createFormat(format);
        }
        return -1;
    }

    public List<FormatRecord> getFormats() {
        return this.formats;
    }

    public int createFormat(String formatString) {
        int i = 164;
        if (this.maxformatid >= 164) {
            i = this.maxformatid + 1;
        }
        this.maxformatid = i;
        FormatRecord rec = new FormatRecord(this.maxformatid, formatString);
        int pos = 0;
        while (pos < this.records.size() && this.records.get(pos).getSid() != 1054) {
            pos++;
        }
        int pos2 = pos + this.formats.size();
        this.formats.add(rec);
        this.records.add(pos2, rec);
        return this.maxformatid;
    }

    public Record findFirstRecordBySid(short sid) {
        Iterator i$ = this.records.iterator();
        while (i$.hasNext()) {
            Record record = (Record) i$.next();
            if (record.getSid() == sid) {
                return record;
            }
        }
        return null;
    }

    public int findFirstRecordLocBySid(short sid) {
        int index = 0;
        Iterator i$ = this.records.iterator();
        while (i$.hasNext()) {
            if (((Record) i$.next()).getSid() == sid) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public Record findNextRecordBySid(short sid, int pos) {
        int matches = 0;
        Iterator i$ = this.records.iterator();
        while (i$.hasNext()) {
            Record record = (Record) i$.next();
            if (record.getSid() == sid) {
                int matches2 = matches + 1;
                if (matches == pos) {
                    return record;
                }
                matches = matches2;
            }
        }
        return null;
    }

    public List<HyperlinkRecord> getHyperlinks() {
        return this.hyperlinks;
    }

    public List<Record> getRecords() {
        return this.records.getRecords();
    }

    public boolean isUsing1904DateWindowing() {
        return this.uses1904datewindowing;
    }

    public PaletteRecord getCustomPalette() {
        int palettePos = this.records.getPalettepos();
        if (palettePos != -1) {
            Record rec = this.records.get(palettePos);
            if (rec instanceof PaletteRecord) {
                return (PaletteRecord) rec;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("InternalError: Expected PaletteRecord but got a '");
            sb.append(rec);
            sb.append("'");
            throw new RuntimeException(sb.toString());
        }
        PaletteRecord palette = createPalette();
        this.records.add(1, palette);
        this.records.setPalettepos(1);
        return palette;
    }

    public void findDrawingGroup() {
        if (this.drawingManager == null) {
            Iterator i$ = this.records.iterator();
            while (i$.hasNext()) {
                Record r = (Record) i$.next();
                if (r instanceof DrawingGroupRecord) {
                    DrawingGroupRecord dg = (DrawingGroupRecord) r;
                    dg.processChildRecords();
                    EscherContainerRecord cr = dg.getEscherContainer();
                    if (cr == null) {
                        continue;
                    } else {
                        EscherDggRecord dgg = null;
                        Iterator<EscherRecord> it = cr.getChildIterator();
                        while (it.hasNext()) {
                            Object er = it.next();
                            if (er instanceof EscherDggRecord) {
                                dgg = (EscherDggRecord) er;
                            }
                        }
                        if (dgg != null) {
                            this.drawingManager = new DrawingManager2(dgg);
                            return;
                        }
                    }
                }
            }
            int dgLoc = findFirstRecordLocBySid(DrawingGroupRecord.sid);
            if (dgLoc != -1) {
                EscherDggRecord dgg2 = null;
                for (EscherRecord er2 : ((DrawingGroupRecord) this.records.get(dgLoc)).getEscherRecords()) {
                    if (er2 instanceof EscherDggRecord) {
                        dgg2 = (EscherDggRecord) er2;
                    }
                }
                if (dgg2 != null) {
                    this.drawingManager = new DrawingManager2(dgg2);
                }
            }
        }
    }

    public void createDrawingGroup() {
        if (this.drawingManager == null) {
            EscherContainerRecord dggContainer = new EscherContainerRecord();
            EscherDggRecord dgg = new EscherDggRecord();
            EscherOptRecord opt = new EscherOptRecord();
            EscherSplitMenuColorsRecord splitMenuColors = new EscherSplitMenuColorsRecord();
            dggContainer.setRecordId(EscherContainerRecord.DGG_CONTAINER);
            dggContainer.setOptions(15);
            dgg.setRecordId(EscherDggRecord.RECORD_ID);
            dgg.setOptions(0);
            dgg.setShapeIdMax(1024);
            dgg.setNumShapesSaved(0);
            dgg.setDrawingsSaved(0);
            dgg.setFileIdClusters(new FileIdCluster[0]);
            this.drawingManager = new DrawingManager2(dgg);
            EscherContainerRecord bstoreContainer = null;
            if (this.escherBSERecords.size() > 0) {
                bstoreContainer = new EscherContainerRecord();
                bstoreContainer.setRecordId(EscherContainerRecord.BSTORE_CONTAINER);
                bstoreContainer.setOptions((short) (15 | (this.escherBSERecords.size() << 4)));
                for (EscherRecord escherRecord : this.escherBSERecords) {
                    bstoreContainer.addChildRecord(escherRecord);
                }
            }
            opt.setRecordId(EscherOptRecord.RECORD_ID);
            opt.setOptions(51);
            opt.addEscherProperty(new EscherBoolProperty(191, 524296));
            opt.addEscherProperty(new EscherRGBProperty(EscherProperties.FILL__FILLCOLOR, 134217793));
            opt.addEscherProperty(new EscherRGBProperty(EscherProperties.LINESTYLE__COLOR, 134217792));
            splitMenuColors.setRecordId(EscherSplitMenuColorsRecord.RECORD_ID);
            splitMenuColors.setOptions(64);
            splitMenuColors.setColor1(134217741);
            splitMenuColors.setColor2(134217740);
            splitMenuColors.setColor3(134217751);
            splitMenuColors.setColor4(268435703);
            dggContainer.addChildRecord(dgg);
            if (bstoreContainer != null) {
                dggContainer.addChildRecord(bstoreContainer);
            }
            dggContainer.addChildRecord(opt);
            dggContainer.addChildRecord(splitMenuColors);
            int dgLoc = findFirstRecordLocBySid(DrawingGroupRecord.sid);
            if (dgLoc == -1) {
                DrawingGroupRecord drawingGroup = new DrawingGroupRecord();
                drawingGroup.addEscherRecord(dggContainer);
                getRecords().add(findFirstRecordLocBySid(140) + 1, drawingGroup);
                return;
            }
            DrawingGroupRecord drawingGroup2 = new DrawingGroupRecord();
            drawingGroup2.addEscherRecord(dggContainer);
            getRecords().set(dgLoc, drawingGroup2);
        }
    }

    public WindowOneRecord getWindowOne() {
        return this.windowOne;
    }

    public EscherBSERecord getBSERecord(int pictureIndex) {
        return (EscherBSERecord) this.escherBSERecords.get(pictureIndex - 1);
    }

    public int addBSERecord(EscherBSERecord e) {
        EscherContainerRecord bstoreContainer;
        createDrawingGroup();
        this.escherBSERecords.add(e);
        EscherContainerRecord dggContainer = (EscherContainerRecord) ((DrawingGroupRecord) getRecords().get(findFirstRecordLocBySid(DrawingGroupRecord.sid))).getEscherRecord(0);
        if (dggContainer.getChild(1).getRecordId() == -4095) {
            bstoreContainer = (EscherContainerRecord) dggContainer.getChild(1);
        } else {
            EscherContainerRecord bstoreContainer2 = new EscherContainerRecord();
            bstoreContainer2.setRecordId(EscherContainerRecord.BSTORE_CONTAINER);
            List<EscherRecord> childRecords = dggContainer.getChildRecords();
            childRecords.add(1, bstoreContainer2);
            dggContainer.setChildRecords(childRecords);
            bstoreContainer = bstoreContainer2;
        }
        bstoreContainer.setOptions((short) ((this.escherBSERecords.size() << 4) | 15));
        bstoreContainer.addChildRecord(e);
        return this.escherBSERecords.size();
    }

    public DrawingManager2 getDrawingManager() {
        return this.drawingManager;
    }

    public WriteProtectRecord getWriteProtect() {
        if (this.writeProtect == null) {
            this.writeProtect = new WriteProtectRecord();
            int i = 0;
            while (i < this.records.size() && !(this.records.get(i) instanceof BOFRecord)) {
                i++;
            }
            this.records.add(i + 1, this.writeProtect);
        }
        return this.writeProtect;
    }

    public WriteAccessRecord getWriteAccess() {
        if (this.writeAccess == null) {
            this.writeAccess = createWriteAccess();
            int i = 0;
            while (i < this.records.size() && !(this.records.get(i) instanceof InterfaceEndRecord)) {
                i++;
            }
            this.records.add(i + 1, this.writeAccess);
        }
        return this.writeAccess;
    }

    public FileSharingRecord getFileSharing() {
        if (this.fileShare == null) {
            this.fileShare = new FileSharingRecord();
            int i = 0;
            while (i < this.records.size() && !(this.records.get(i) instanceof WriteAccessRecord)) {
                i++;
            }
            this.records.add(i + 1, this.fileShare);
        }
        return this.fileShare;
    }

    public boolean isWriteProtected() {
        boolean z = false;
        if (this.fileShare == null) {
            return false;
        }
        if (getFileSharing().getReadOnly() == 1) {
            z = true;
        }
        return z;
    }

    public void writeProtectWorkbook(String password, String username) {
        FileSharingRecord frec = getFileSharing();
        WriteAccessRecord waccess = getWriteAccess();
        WriteProtectRecord writeProtect2 = getWriteProtect();
        frec.setReadOnly(1);
        frec.setPassword(FileSharingRecord.hashPassword(password));
        frec.setUsername(username);
        waccess.setUsername(username);
    }

    public void unwriteProtectWorkbook() {
        this.records.remove((Object) this.fileShare);
        this.records.remove((Object) this.writeProtect);
        this.fileShare = null;
        this.writeProtect = null;
    }

    public String resolveNameXText(int refIndex, int definedNameIndex) {
        return this.linkTable.resolveNameXText(refIndex, definedNameIndex);
    }

    public NameXPtg getNameXPtg(String name) {
        return getOrCreateLinkTable().getNameXPtg(name);
    }

    public void cloneDrawings(InternalSheet sheet) {
        findDrawingGroup();
        if (!(this.drawingManager == null || sheet.aggregateDrawingRecords(this.drawingManager, false) == -1)) {
            EscherContainerRecord escherContainer = ((EscherAggregate) sheet.findFirstRecordBySid(EscherAggregate.sid)).getEscherContainer();
            if (escherContainer != null) {
                EscherDggRecord dgg = this.drawingManager.getDgg();
                int dgId = this.drawingManager.findNewDrawingGroupId();
                dgg.addCluster(dgId, 0);
                dgg.setDrawingsSaved(dgg.getDrawingsSaved() + 1);
                EscherDgRecord dg = null;
                Iterator<EscherRecord> it = escherContainer.getChildIterator();
                while (it.hasNext()) {
                    EscherRecord er = (EscherRecord) it.next();
                    if (er instanceof EscherDgRecord) {
                        dg = (EscherDgRecord) er;
                        dg.setOptions((short) (dgId << 4));
                    } else if (er instanceof EscherContainerRecord) {
                        List<EscherRecord> spRecords = new ArrayList<>();
                        ((EscherContainerRecord) er).getRecordsById(EscherSpRecord.RECORD_ID, spRecords);
                        Iterator<EscherRecord> spIt = spRecords.iterator();
                        while (spIt.hasNext()) {
                            EscherSpRecord sp = (EscherSpRecord) spIt.next();
                            int shapeId = this.drawingManager.allocateShapeId((short) dgId, dg);
                            dg.setNumShapes(dg.getNumShapes() - 1);
                            sp.setShapeId(shapeId);
                        }
                    }
                }
            }
        }
    }

    public void updateNamesAfterCellShift(FormulaShifter shifter) {
        for (int i = 0; i < getNumNames(); i++) {
            NameRecord nr = getNameRecord(i);
            Ptg[] ptgs = nr.getNameDefinition();
            if (shifter.adjustFormula(ptgs, nr.getSheetNumber())) {
                nr.setNameDefinition(ptgs);
            }
        }
    }
}

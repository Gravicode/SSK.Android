package org.apache.poi.hssf.usermodel;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.poi.POIDocument;
import org.apache.poi.ddf.EscherBSERecord;
import org.apache.poi.ddf.EscherBitmapBlip;
import org.apache.poi.ddf.EscherBlipRecord;
import org.apache.poi.ddf.EscherRecord;
import org.apache.poi.hssf.OldExcelFormatException;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.model.InternalSheet;
import org.apache.poi.hssf.model.InternalWorkbook;
import org.apache.poi.hssf.model.RecordStream;
import org.apache.poi.hssf.record.AbstractEscherHolderRecord;
import org.apache.poi.hssf.record.DrawingGroupRecord;
import org.apache.poi.hssf.record.EmbeddedObjectRefSubRecord;
import org.apache.poi.hssf.record.FontRecord;
import org.apache.poi.hssf.record.LabelRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NameRecord;
import org.apache.poi.hssf.record.ObjRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordFactory;
import org.apache.poi.hssf.record.UnknownRecord;
import org.apache.poi.hssf.record.aggregates.RecordAggregate.RecordVisitor;
import org.apache.poi.hssf.record.common.UnicodeString;
import org.apache.poi.hssf.record.formula.Area3DPtg;
import org.apache.poi.hssf.record.formula.IntPtg;
import org.apache.poi.hssf.record.formula.IntersectionPtg;
import org.apache.poi.hssf.record.formula.MemFuncPtg;
import org.apache.poi.hssf.record.formula.NameXPtg;
import org.apache.poi.hssf.record.formula.OperandPtg;
import org.apache.poi.hssf.record.formula.Ptg;
import org.apache.poi.hssf.record.formula.Ref3DPtg;
import org.apache.poi.hssf.record.formula.SheetNameFormatter;
import org.apache.poi.hssf.record.formula.StringPtg;
import org.apache.poi.hssf.record.formula.UnaryPlusPtg;
import org.apache.poi.hssf.record.formula.UnionPtg;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.p009ss.usermodel.CreationHelper;
import org.apache.poi.p009ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.p009ss.usermodel.Sheet;
import org.apache.poi.p009ss.usermodel.Workbook;
import org.apache.poi.p009ss.util.WorkbookUtil;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public final class HSSFWorkbook extends POIDocument implements Workbook {
    private static final Pattern COMMA_PATTERN = Pattern.compile(",");
    private static final int DEBUG = POILogger.DEBUG;
    public static final int INITIAL_CAPACITY = 3;
    private static final short MAX_COLUMN = 255;
    private static final int MAX_ROW = 65535;
    private static final String[] WORKBOOK_DIR_ENTRY_NAMES = {"Workbook", "WORKBOOK"};
    private static POILogger log = POILogFactory.getLogger(HSSFWorkbook.class);
    protected List _sheets;
    private Hashtable fonts;
    private HSSFDataFormat formatter;
    private MissingCellPolicy missingCellPolicy;
    private ArrayList names;
    private boolean preserveNodes;
    private InternalWorkbook workbook;

    private static final class SheetRecordCollector implements RecordVisitor {
        private List _list = new ArrayList(128);
        private int _totalSize = 0;

        public int getTotalSize() {
            return this._totalSize;
        }

        public void visitRecord(Record r) {
            this._list.add(r);
            this._totalSize += r.getRecordSize();
        }

        public int serialize(int offset, byte[] data) {
            int result = 0;
            for (int i = 0; i < this._list.size(); i++) {
                result += ((Record) this._list.get(i)).serialize(offset + result, data);
            }
            return result;
        }
    }

    public static HSSFWorkbook create(InternalWorkbook book) {
        return new HSSFWorkbook(book);
    }

    public HSSFWorkbook() {
        this(InternalWorkbook.createWorkbook());
    }

    private HSSFWorkbook(InternalWorkbook book) {
        super(null, null);
        this.missingCellPolicy = HSSFRow.RETURN_NULL_AND_BLANK;
        this.workbook = book;
        this._sheets = new ArrayList(3);
        this.names = new ArrayList(3);
    }

    public HSSFWorkbook(POIFSFileSystem fs) throws IOException {
        this(fs, true);
    }

    public HSSFWorkbook(POIFSFileSystem fs, boolean preserveNodes2) throws IOException {
        this(fs.getRoot(), fs, preserveNodes2);
    }

    private static String getWorkbookDirEntryName(DirectoryNode directory) {
        String[] potentialNames = WORKBOOK_DIR_ENTRY_NAMES;
        int i = 0;
        while (i < potentialNames.length) {
            String wbName = potentialNames[i];
            try {
                directory.getEntry(wbName);
                return wbName;
            } catch (FileNotFoundException e) {
                i++;
            }
        }
        try {
            directory.getEntry("Book");
            throw new OldExcelFormatException("The supplied spreadsheet seems to be Excel 5.0/7.0 (BIFF5) format. POI only supports BIFF8 format (from Excel versions 97/2000/XP/2003)");
        } catch (FileNotFoundException e2) {
            throw new IllegalArgumentException("The supplied POIFSFileSystem does not contain a BIFF8 'Workbook' entry. Is it really an excel file?");
        }
    }

    public HSSFWorkbook(DirectoryNode directory, POIFSFileSystem fs, boolean preserveNodes2) throws IOException {
        super(directory, fs);
        this.missingCellPolicy = HSSFRow.RETURN_NULL_AND_BLANK;
        String workbookName = getWorkbookDirEntryName(directory);
        this.preserveNodes = preserveNodes2;
        if (!preserveNodes2) {
            this.filesystem = null;
            this.directory = null;
        }
        this._sheets = new ArrayList(3);
        this.names = new ArrayList(3);
        List records = RecordFactory.createRecords(directory.createDocumentInputStream(workbookName));
        this.workbook = InternalWorkbook.createWorkbook(records);
        setPropertiesFromWorkbook(this.workbook);
        int recOffset = this.workbook.getNumRecords();
        convertLabelRecords(records, recOffset);
        RecordStream rs = new RecordStream(records, recOffset);
        while (rs.hasNext()) {
            this._sheets.add(new HSSFSheet(this, InternalSheet.createSheet(rs)));
        }
        for (int i = 0; i < this.workbook.getNumNames(); i++) {
            NameRecord nameRecord = this.workbook.getNameRecord(i);
            this.names.add(new HSSFName(this, nameRecord, this.workbook.getNameCommentRecord(nameRecord)));
        }
    }

    public HSSFWorkbook(InputStream s) throws IOException {
        this(s, true);
    }

    public HSSFWorkbook(InputStream s, boolean preserveNodes2) throws IOException {
        this(new POIFSFileSystem(s), preserveNodes2);
    }

    private void setPropertiesFromWorkbook(InternalWorkbook book) {
        this.workbook = book;
    }

    private void convertLabelRecords(List records, int offset) {
        if (log.check(POILogger.DEBUG)) {
            log.log(POILogger.DEBUG, (Object) "convertLabelRecords called");
        }
        for (int k = offset; k < records.size(); k++) {
            Record rec = (Record) records.get(k);
            if (rec.getSid() == 516) {
                LabelRecord oldrec = (LabelRecord) rec;
                records.remove(k);
                LabelSSTRecord newrec = new LabelSSTRecord();
                int stringid = this.workbook.addSSTString(new UnicodeString(oldrec.getValue()));
                newrec.setRow(oldrec.getRow());
                newrec.setColumn(oldrec.getColumn());
                newrec.setXFIndex(oldrec.getXFIndex());
                newrec.setSSTIndex(stringid);
                records.add(k, newrec);
            }
        }
        if (log.check(POILogger.DEBUG)) {
            log.log(POILogger.DEBUG, (Object) "convertLabelRecords exit");
        }
    }

    public MissingCellPolicy getMissingCellPolicy() {
        return this.missingCellPolicy;
    }

    public void setMissingCellPolicy(MissingCellPolicy missingCellPolicy2) {
        this.missingCellPolicy = missingCellPolicy2;
    }

    public void setSheetOrder(String sheetname, int pos) {
        this._sheets.add(pos, this._sheets.remove(getSheetIndex(sheetname)));
        this.workbook.setSheetOrder(sheetname, pos);
    }

    private void validateSheetIndex(int index) {
        int lastSheetIx = this._sheets.size() - 1;
        if (index < 0 || index > lastSheetIx) {
            StringBuilder sb = new StringBuilder();
            sb.append("Sheet index (");
            sb.append(index);
            sb.append(") is out of range (0..");
            sb.append(lastSheetIx);
            sb.append(")");
            throw new IllegalArgumentException(sb.toString());
        }
    }

    public void setSelectedTab(int index) {
        validateSheetIndex(index);
        int nSheets = this._sheets.size();
        int i = 0;
        while (true) {
            boolean z = true;
            if (i < nSheets) {
                HSSFSheet sheetAt = getSheetAt(i);
                if (i != index) {
                    z = false;
                }
                sheetAt.setSelected(z);
                i++;
            } else {
                this.workbook.getWindowOne().setNumSelectedTabs(1);
                return;
            }
        }
    }

    public void setSelectedTab(short index) {
        setSelectedTab((int) index);
    }

    public void setSelectedTabs(int[] indexes) {
        for (int validateSheetIndex : indexes) {
            validateSheetIndex(validateSheetIndex);
        }
        int nSheets = this._sheets.size();
        for (int i = 0; i < nSheets; i++) {
            boolean bSelect = false;
            int j = 0;
            while (true) {
                if (j >= indexes.length) {
                    break;
                } else if (indexes[j] == i) {
                    bSelect = true;
                    break;
                } else {
                    j++;
                }
            }
            getSheetAt(i).setSelected(bSelect);
        }
        this.workbook.getWindowOne().setNumSelectedTabs((short) indexes.length);
    }

    public void setActiveSheet(int index) {
        validateSheetIndex(index);
        int nSheets = this._sheets.size();
        int i = 0;
        while (i < nSheets) {
            getSheetAt(i).setActive(i == index);
            i++;
        }
        this.workbook.getWindowOne().setActiveSheetIndex(index);
    }

    public int getActiveSheetIndex() {
        return this.workbook.getWindowOne().getActiveSheetIndex();
    }

    public short getSelectedTab() {
        return (short) getActiveSheetIndex();
    }

    public void setFirstVisibleTab(int index) {
        this.workbook.getWindowOne().setFirstVisibleTab(index);
    }

    public void setDisplayedTab(short index) {
        setFirstVisibleTab(index);
    }

    public int getFirstVisibleTab() {
        return this.workbook.getWindowOne().getFirstVisibleTab();
    }

    public short getDisplayedTab() {
        return (short) getFirstVisibleTab();
    }

    public void setSheetName(int sheetIx, String name) {
        if (this.workbook.doesContainsSheetName(name, sheetIx)) {
            throw new IllegalArgumentException("The workbook already contains a sheet with this name");
        }
        validateSheetIndex(sheetIx);
        this.workbook.setSheetName(sheetIx, name);
    }

    public String getSheetName(int sheetIndex) {
        validateSheetIndex(sheetIndex);
        return this.workbook.getSheetName(sheetIndex);
    }

    public boolean isHidden() {
        return this.workbook.getWindowOne().getHidden();
    }

    public void setHidden(boolean hiddenFlag) {
        this.workbook.getWindowOne().setHidden(hiddenFlag);
    }

    public boolean isSheetHidden(int sheetIx) {
        validateSheetIndex(sheetIx);
        return this.workbook.isSheetHidden(sheetIx);
    }

    public boolean isSheetVeryHidden(int sheetIx) {
        validateSheetIndex(sheetIx);
        return this.workbook.isSheetVeryHidden(sheetIx);
    }

    public void setSheetHidden(int sheetIx, boolean hidden) {
        validateSheetIndex(sheetIx);
        this.workbook.setSheetHidden(sheetIx, hidden);
    }

    public void setSheetHidden(int sheetIx, int hidden) {
        validateSheetIndex(sheetIx);
        WorkbookUtil.validateSheetState(hidden);
        this.workbook.setSheetHidden(sheetIx, hidden);
    }

    public int getSheetIndex(String name) {
        return this.workbook.getSheetIndex(name);
    }

    public int getSheetIndex(Sheet sheet) {
        for (int i = 0; i < this._sheets.size(); i++) {
            if (this._sheets.get(i) == sheet) {
                return i;
            }
        }
        return -1;
    }

    public int getExternalSheetIndex(int internalSheetIndex) {
        return this.workbook.checkExternSheet(internalSheetIndex);
    }

    public String findSheetNameFromExternSheet(int externSheetIndex) {
        return this.workbook.findSheetNameFromExternSheet(externSheetIndex);
    }

    public String resolveNameXText(int refIndex, int definedNameIndex) {
        return this.workbook.resolveNameXText(refIndex, definedNameIndex);
    }

    public HSSFSheet createSheet() {
        HSSFSheet sheet = new HSSFSheet(this);
        this._sheets.add(sheet);
        InternalWorkbook internalWorkbook = this.workbook;
        boolean z = true;
        int size = this._sheets.size() - 1;
        StringBuilder sb = new StringBuilder();
        sb.append("Sheet");
        sb.append(this._sheets.size() - 1);
        internalWorkbook.setSheetName(size, sb.toString());
        if (this._sheets.size() != 1) {
            z = false;
        }
        boolean isOnlySheet = z;
        sheet.setSelected(isOnlySheet);
        sheet.setActive(isOnlySheet);
        return sheet;
    }

    public HSSFSheet cloneSheet(int sheetIndex) {
        validateSheetIndex(sheetIndex);
        HSSFSheet srcSheet = (HSSFSheet) this._sheets.get(sheetIndex);
        String srcName = this.workbook.getSheetName(sheetIndex);
        HSSFSheet clonedSheet = srcSheet.cloneSheet(this);
        clonedSheet.setSelected(false);
        clonedSheet.setActive(false);
        String name = getUniqueSheetName(srcName);
        int newSheetIndex = this._sheets.size();
        this._sheets.add(clonedSheet);
        this.workbook.setSheetName(newSheetIndex, name);
        int filterDbNameIndex = findExistingBuiltinNameRecordIdx(sheetIndex, 13);
        if (filterDbNameIndex >= 0) {
            NameRecord origNameRecord = this.workbook.getNameRecord(filterDbNameIndex);
            int newExtSheetIx = this.workbook.checkExternSheet(newSheetIndex);
            Ptg[] ptgs = origNameRecord.getNameDefinition();
            for (int i = 0; i < ptgs.length; i++) {
                Ptg ptg = ptgs[i];
                if (ptg instanceof Area3DPtg) {
                    Area3DPtg a3p = (Area3DPtg) ((OperandPtg) ptg).copy();
                    a3p.setExternSheetIndex(newExtSheetIx);
                    ptgs[i] = a3p;
                } else if (ptg instanceof Ref3DPtg) {
                    Ref3DPtg r3p = (Ref3DPtg) ((OperandPtg) ptg).copy();
                    r3p.setExternSheetIndex(newExtSheetIx);
                    ptgs[i] = r3p;
                }
            }
            NameRecord newNameRecord = this.workbook.createBuiltInName(13, newSheetIndex + 1);
            newNameRecord.setNameDefinition(ptgs);
            newNameRecord.setHidden(true);
            this.names.add(new HSSFName(this, newNameRecord));
            this.workbook.cloneDrawings(clonedSheet.getSheet());
        }
        return clonedSheet;
    }

    private String getUniqueSheetName(String srcName) {
        String name;
        int uniqueIndex = 2;
        String baseName = srcName;
        int bracketPos = srcName.lastIndexOf(40);
        if (bracketPos > 0 && srcName.endsWith(")")) {
            try {
                uniqueIndex = Integer.parseInt(srcName.substring(bracketPos + 1, srcName.length() - ")".length()).trim()) + 1;
                baseName = srcName.substring(0, bracketPos).trim();
            } catch (NumberFormatException e) {
            }
        }
        while (true) {
            int uniqueIndex2 = uniqueIndex + 1;
            String index = Integer.toString(uniqueIndex);
            if (baseName.length() + index.length() + 2 < 31) {
                StringBuilder sb = new StringBuilder();
                sb.append(baseName);
                sb.append(" (");
                sb.append(index);
                sb.append(")");
                name = sb.toString();
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(baseName.substring(0, (31 - index.length()) - 2));
                sb2.append("(");
                sb2.append(index);
                sb2.append(")");
                name = sb2.toString();
            }
            if (this.workbook.getSheetIndex(name) == -1) {
                return name;
            }
            uniqueIndex = uniqueIndex2;
        }
    }

    public HSSFSheet createSheet(String sheetname) {
        if (this.workbook.doesContainsSheetName(sheetname, this._sheets.size())) {
            throw new IllegalArgumentException("The workbook already contains a sheet of this name");
        }
        HSSFSheet sheet = new HSSFSheet(this);
        this.workbook.setSheetName(this._sheets.size(), sheetname);
        this._sheets.add(sheet);
        boolean z = true;
        if (this._sheets.size() != 1) {
            z = false;
        }
        boolean isOnlySheet = z;
        sheet.setSelected(isOnlySheet);
        sheet.setActive(isOnlySheet);
        return sheet;
    }

    public int getNumberOfSheets() {
        return this._sheets.size();
    }

    public int getSheetIndexFromExternSheetIndex(int externSheetNumber) {
        return this.workbook.getSheetIndexFromExternSheetIndex(externSheetNumber);
    }

    private HSSFSheet[] getSheets() {
        HSSFSheet[] result = new HSSFSheet[this._sheets.size()];
        this._sheets.toArray(result);
        return result;
    }

    public HSSFSheet getSheetAt(int index) {
        validateSheetIndex(index);
        return (HSSFSheet) this._sheets.get(index);
    }

    public HSSFSheet getSheet(String name) {
        HSSFSheet retval = null;
        for (int k = 0; k < this._sheets.size(); k++) {
            if (this.workbook.getSheetName(k).equalsIgnoreCase(name)) {
                retval = (HSSFSheet) this._sheets.get(k);
            }
        }
        return retval;
    }

    public void removeSheetAt(int index) {
        validateSheetIndex(index);
        boolean wasActive = getSheetAt(index).isActive();
        boolean wasSelected = getSheetAt(index).isSelected();
        this._sheets.remove(index);
        this.workbook.removeSheet(index);
        int nSheets = this._sheets.size();
        if (nSheets >= 1) {
            int newSheetIndex = index;
            if (newSheetIndex >= nSheets) {
                newSheetIndex = nSheets - 1;
            }
            if (wasActive) {
                setActiveSheet(newSheetIndex);
            }
            if (wasSelected) {
                boolean someOtherSheetIsStillSelected = false;
                int i = 0;
                while (true) {
                    if (i >= nSheets) {
                        break;
                    } else if (getSheetAt(i).isSelected()) {
                        someOtherSheetIsStillSelected = true;
                        break;
                    } else {
                        i++;
                    }
                }
                if (!someOtherSheetIsStillSelected) {
                    setSelectedTab(newSheetIndex);
                }
            }
        }
    }

    public void setBackupFlag(boolean backupValue) {
        this.workbook.getBackupRecord().setBackup(backupValue);
    }

    public boolean getBackupFlag() {
        return this.workbook.getBackupRecord().getBackup() != 0;
    }

    public void setRepeatingRowsAndColumns(int sheetIndex, int startColumn, int endColumn, int startRow, int endRow) {
        NameRecord nameRecord;
        boolean isNewRecord;
        NameRecord nameRecord2;
        List list;
        HSSFSheet sheet;
        List temp;
        int i = sheetIndex;
        int i2 = startColumn;
        int i3 = endColumn;
        int i4 = startRow;
        int i5 = endRow;
        if (i2 == -1 && i3 != -1) {
            throw new IllegalArgumentException("Invalid column range specification");
        } else if (i4 == -1 && i5 != -1) {
            throw new IllegalArgumentException("Invalid row range specification");
        } else if (i2 < -1 || i2 >= 255) {
            throw new IllegalArgumentException("Invalid column range specification");
        } else if (i3 < -1 || i3 >= 255) {
            throw new IllegalArgumentException("Invalid column range specification");
        } else if (i4 < -1 || i4 > 65535) {
            throw new IllegalArgumentException("Invalid row range specification");
        } else if (i5 < -1 || i5 > 65535) {
            throw new IllegalArgumentException("Invalid row range specification");
        } else if (i2 > i3) {
            throw new IllegalArgumentException("Invalid column range specification");
        } else if (i4 > i5) {
            throw new IllegalArgumentException("Invalid row range specification");
        } else {
            HSSFSheet sheet2 = getSheetAt(sheetIndex);
            short externSheetIndex = getWorkbook().checkExternSheet(i);
            boolean settingRowAndColumn = (i2 == -1 || i3 == -1 || i4 == -1 || i5 == -1) ? false : true;
            boolean removingRange = i2 == -1 && i3 == -1 && i4 == -1 && i5 == -1;
            int rowColHeaderNameIndex = findExistingBuiltinNameRecordIdx(i, 7);
            if (removingRange) {
                if (rowColHeaderNameIndex >= 0) {
                    this.workbook.removeName(rowColHeaderNameIndex);
                }
                return;
            }
            if (rowColHeaderNameIndex < 0) {
                isNewRecord = true;
                nameRecord = this.workbook.createBuiltInName(7, i + 1);
            } else {
                nameRecord = this.workbook.getNameRecord(rowColHeaderNameIndex);
                isNewRecord = false;
            }
            boolean isNewRecord2 = isNewRecord;
            List arrayList = new ArrayList();
            if (settingRowAndColumn) {
                arrayList.add(new MemFuncPtg(23));
            }
            if (i2 >= 0) {
                ArrayList arrayList2 = arrayList;
                nameRecord2 = nameRecord;
                int i6 = rowColHeaderNameIndex;
                sheet = sheet2;
                Area3DPtg colArea = new Area3DPtg(0, 65535, i2, i3, false, false, false, false, externSheetIndex);
                list = arrayList2;
                list.add(colArea);
            } else {
                nameRecord2 = nameRecord;
                int i7 = rowColHeaderNameIndex;
                sheet = sheet2;
                list = arrayList;
            }
            if (i4 >= 0) {
                temp = list;
                Area3DPtg rowArea = new Area3DPtg(i4, i5, 0, 255, false, false, false, false, externSheetIndex);
                temp.add(rowArea);
            } else {
                temp = list;
            }
            if (settingRowAndColumn) {
                temp.add(UnionPtg.instance);
            }
            Ptg[] ptgs = new Ptg[temp.size()];
            temp.toArray(ptgs);
            NameRecord nameRecord3 = nameRecord2;
            nameRecord3.setNameDefinition(ptgs);
            if (isNewRecord2) {
                this.names.add(new HSSFName(this, nameRecord3, nameRecord3.isBuiltInName() ? null : this.workbook.getNameCommentRecord(nameRecord3)));
            }
            sheet.getPrintSetup().setValidSettings(false);
            sheet.setActive(true);
        }
    }

    private int findExistingBuiltinNameRecordIdx(int sheetIndex, byte builtinCode) {
        int defNameIndex = 0;
        while (defNameIndex < this.names.size()) {
            NameRecord r = this.workbook.getNameRecord(defNameIndex);
            if (r == null) {
                throw new RuntimeException("Unable to find all defined names to iterate over");
            } else if (r.isBuiltInName() && r.getBuiltInName() == builtinCode && r.getSheetNumber() - 1 == sheetIndex) {
                return defNameIndex;
            } else {
                defNameIndex++;
            }
        }
        return -1;
    }

    public HSSFFont createFont() {
        FontRecord createNewFont = this.workbook.createNewFont();
        short fontindex = (short) (getNumberOfFonts() - 1);
        if (fontindex > 3) {
            fontindex = (short) (fontindex + 1);
        }
        if (fontindex != Short.MAX_VALUE) {
            return getFontAt(fontindex);
        }
        throw new IllegalArgumentException("Maximum number of fonts was exceeded");
    }

    public HSSFFont findFont(short boldWeight, short color, short fontHeight, String name, boolean italic, boolean strikeout, short typeOffset, byte underline) {
        for (short i = 0; i <= getNumberOfFonts(); i = (short) (i + 1)) {
            if (i != 4) {
                HSSFFont hssfFont = getFontAt(i);
                if (hssfFont.getBoldweight() == boldWeight && hssfFont.getColor() == color && hssfFont.getFontHeight() == fontHeight && hssfFont.getFontName().equals(name) && hssfFont.getItalic() == italic && hssfFont.getStrikeout() == strikeout && hssfFont.getTypeOffset() == typeOffset && hssfFont.getUnderline() == underline) {
                    return hssfFont;
                }
            }
        }
        return null;
    }

    public short getNumberOfFonts() {
        return (short) this.workbook.getNumberOfFontRecords();
    }

    public HSSFFont getFontAt(short idx) {
        if (this.fonts == null) {
            this.fonts = new Hashtable();
        }
        Short sIdx = Short.valueOf(idx);
        if (this.fonts.containsKey(sIdx)) {
            return (HSSFFont) this.fonts.get(sIdx);
        }
        HSSFFont retval = new HSSFFont(idx, this.workbook.getFontRecordAt(idx));
        this.fonts.put(sIdx, retval);
        return retval;
    }

    /* access modifiers changed from: protected */
    public void resetFontCache() {
        this.fonts = new Hashtable();
    }

    public HSSFCellStyle createCellStyle() {
        return new HSSFCellStyle((short) (getNumCellStyles() - 1), this.workbook.createCellXF(), this);
    }

    public short getNumCellStyles() {
        return (short) this.workbook.getNumExFormats();
    }

    public HSSFCellStyle getCellStyleAt(short idx) {
        return new HSSFCellStyle(idx, this.workbook.getExFormatAt(idx), this);
    }

    public void write(OutputStream stream) throws IOException {
        byte[] bytes = getBytes();
        POIFSFileSystem fs = new POIFSFileSystem();
        List excepts = new ArrayList(1);
        fs.createDocument(new ByteArrayInputStream(bytes), "Workbook");
        writeProperties(fs, excepts);
        if (this.preserveNodes) {
            excepts.add("Workbook");
            excepts.add("WORKBOOK");
            POIFSFileSystem srcFs = this.filesystem;
            copyNodes(srcFs, fs, excepts);
            fs.getRoot().setStorageClsid(srcFs.getRoot().getStorageClsid());
        }
        fs.writeFilesystem(stream);
    }

    public byte[] getBytes() {
        if (log.check(POILogger.DEBUG)) {
            log.log(DEBUG, (Object) "HSSFWorkbook.getBytes()");
        }
        HSSFSheet[] sheets = getSheets();
        for (HSSFSheet sheet : sheets) {
            sheet.getSheet().preSerialize();
        }
        SheetRecordCollector[] srCollectors = new SheetRecordCollector[nSheets];
        int totalsize = this.workbook.getSize();
        for (int k = 0; k < nSheets; k++) {
            this.workbook.setSheetBof(k, totalsize);
            SheetRecordCollector src = new SheetRecordCollector();
            sheets[k].getSheet().visitContainedRecords(src, totalsize);
            totalsize += src.getTotalSize();
            srCollectors[k] = src;
        }
        byte[] retval = new byte[totalsize];
        int pos = this.workbook.serialize(0, retval);
        for (int k2 = 0; k2 < nSheets; k2++) {
            SheetRecordCollector src2 = srCollectors[k2];
            int serializedSize = src2.serialize(pos, retval);
            if (serializedSize != src2.getTotalSize()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Actual serialized sheet size (");
                sb.append(serializedSize);
                sb.append(") differs from pre-calculated size (");
                sb.append(src2.getTotalSize());
                sb.append(") for sheet (");
                sb.append(k2);
                sb.append(")");
                throw new IllegalStateException(sb.toString());
            }
            pos += serializedSize;
        }
        return retval;
    }

    public int addSSTString(String string) {
        return this.workbook.addSSTString(new UnicodeString(string));
    }

    public String getSSTString(int index) {
        return this.workbook.getSSTString(index).getString();
    }

    /* access modifiers changed from: 0000 */
    public InternalWorkbook getWorkbook() {
        return this.workbook;
    }

    public int getNumberOfNames() {
        return this.names.size();
    }

    public HSSFName getName(String name) {
        int nameIndex = getNameIndex(name);
        if (nameIndex < 0) {
            return null;
        }
        return (HSSFName) this.names.get(nameIndex);
    }

    public HSSFName getNameAt(int nameIndex) {
        int nNames = this.names.size();
        if (nNames < 1) {
            throw new IllegalStateException("There are no defined names in this workbook");
        } else if (nameIndex >= 0 && nameIndex <= nNames) {
            return (HSSFName) this.names.get(nameIndex);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Specified name index ");
            sb.append(nameIndex);
            sb.append(" is outside the allowable range (0..");
            sb.append(nNames - 1);
            sb.append(").");
            throw new IllegalArgumentException(sb.toString());
        }
    }

    public NameRecord getNameRecord(int nameIndex) {
        return getWorkbook().getNameRecord(nameIndex);
    }

    public String getNameName(int index) {
        return getNameAt(index).getNameName();
    }

    public void setPrintArea(int sheetIndex, String reference) {
        NameRecord name = this.workbook.getSpecificBuiltinRecord(6, sheetIndex + 1);
        if (name == null) {
            name = this.workbook.createBuiltInName(6, sheetIndex + 1);
        }
        String[] parts = COMMA_PATTERN.split(reference);
        StringBuffer sb = new StringBuffer(32);
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            SheetNameFormatter.appendFormat(sb, getSheetName(sheetIndex));
            sb.append("!");
            sb.append(parts[i]);
        }
        name.setNameDefinition(HSSFFormulaParser.parse(sb.toString(), this, 0, sheetIndex));
    }

    public void setPrintArea(int sheetIndex, int startColumn, int endColumn, int startRow, int endRow) {
        String reference = new CellReference(startRow, startColumn, true, true).formatAsString();
        CellReference cell = new CellReference(endRow, endColumn, true, true);
        StringBuilder sb = new StringBuilder();
        sb.append(reference);
        sb.append(":");
        sb.append(cell.formatAsString());
        setPrintArea(sheetIndex, sb.toString());
    }

    public String getPrintArea(int sheetIndex) {
        NameRecord name = this.workbook.getSpecificBuiltinRecord(6, sheetIndex + 1);
        if (name == null) {
            return null;
        }
        return HSSFFormulaParser.toFormulaString(this, name.getNameDefinition());
    }

    public void removePrintArea(int sheetIndex) {
        getWorkbook().removeBuiltinRecord(6, sheetIndex + 1);
    }

    public HSSFName createName() {
        HSSFName newName = new HSSFName(this, this.workbook.createName());
        this.names.add(newName);
        return newName;
    }

    public int getNameIndex(String name) {
        for (int k = 0; k < this.names.size(); k++) {
            if (getNameName(k).equalsIgnoreCase(name)) {
                return k;
            }
        }
        return -1;
    }

    public void removeName(int index) {
        this.names.remove(index);
        this.workbook.removeName(index);
    }

    public HSSFDataFormat createDataFormat() {
        if (this.formatter == null) {
            this.formatter = new HSSFDataFormat(this.workbook);
        }
        return this.formatter;
    }

    public void removeName(String name) {
        removeName(getNameIndex(name));
    }

    public HSSFPalette getCustomPalette() {
        return new HSSFPalette(this.workbook.getCustomPalette());
    }

    public void insertChartRecord() {
        this.workbook.getRecords().add(this.workbook.findFirstRecordLocBySid(252), new UnknownRecord(235, new byte[]{IntersectionPtg.sid, 0, 0, -16, 82, 0, 0, 0, 0, 0, 6, -16, 24, 0, 0, 0, 1, 8, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 3, 0, 0, 0, 51, 0, 11, -16, UnaryPlusPtg.sid, 0, 0, 0, -65, 0, 8, 0, 8, 0, -127, 1, 9, 0, 0, 8, -64, 1, Ptg.CLASS_ARRAY, 0, 0, 8, Ptg.CLASS_ARRAY, 0, IntPtg.sid, -15, UnionPtg.sid, 0, 0, 0, 13, 0, 0, 8, 12, 0, 0, 8, StringPtg.sid, 0, 0, 8, -9, 0, 0, UnionPtg.sid}));
    }

    public void dumpDrawingGroupRecords(boolean fat) {
        DrawingGroupRecord r = (DrawingGroupRecord) this.workbook.findFirstRecordBySid(DrawingGroupRecord.sid);
        r.decode();
        List<EscherRecord> escherRecords = r.getEscherRecords();
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

    public int addPicture(byte[] pictureData, int format) {
        byte[] uid = newUID();
        EscherBitmapBlip blipRecord = new EscherBitmapBlip();
        blipRecord.setRecordId((short) (format - 4072));
        switch (format) {
            case 2:
                blipRecord.setOptions(15680);
                break;
            case 3:
                blipRecord.setOptions(8544);
                break;
            case 4:
                blipRecord.setOptions(21536);
                break;
            case 5:
                blipRecord.setOptions(HSSFPictureData.MSOBI_JPEG);
                break;
            case 6:
                blipRecord.setOptions(HSSFPictureData.MSOBI_PNG);
                break;
            case 7:
                blipRecord.setOptions(HSSFPictureData.MSOBI_DIB);
                break;
        }
        blipRecord.setUID(uid);
        blipRecord.setMarker(-1);
        blipRecord.setPictureData(pictureData);
        EscherBSERecord r = new EscherBSERecord();
        r.setRecordId(EscherBSERecord.RECORD_ID);
        r.setOptions((short) ((format << 4) | 2));
        r.setBlipTypeMacOS((byte) format);
        r.setBlipTypeWin32((byte) format);
        r.setUid(uid);
        r.setTag(255);
        r.setSize(pictureData.length + 25);
        r.setRef(1);
        r.setOffset(0);
        r.setBlipRecord(blipRecord);
        return this.workbook.addBSERecord(r);
    }

    public List<HSSFPictureData> getAllPictures() {
        List<HSSFPictureData> pictures = new ArrayList<>();
        for (Record r : this.workbook.getRecords()) {
            if (r instanceof AbstractEscherHolderRecord) {
                ((AbstractEscherHolderRecord) r).decode();
                searchForPictures(((AbstractEscherHolderRecord) r).getEscherRecords(), pictures);
            }
        }
        return pictures;
    }

    private void searchForPictures(List<EscherRecord> escherRecords, List<HSSFPictureData> pictures) {
        for (EscherRecord escherRecord : escherRecords) {
            if (escherRecord instanceof EscherBSERecord) {
                EscherBlipRecord blip = ((EscherBSERecord) escherRecord).getBlipRecord();
                if (blip != null) {
                    pictures.add(new HSSFPictureData(blip));
                }
            }
            searchForPictures(escherRecord.getChildRecords(), pictures);
        }
    }

    public boolean isWriteProtected() {
        return this.workbook.isWriteProtected();
    }

    public void writeProtectWorkbook(String password, String username) {
        this.workbook.writeProtectWorkbook(password, username);
    }

    public void unwriteProtectWorkbook() {
        this.workbook.unwriteProtectWorkbook();
    }

    public List<HSSFObjectData> getAllEmbeddedObjects() {
        List<HSSFObjectData> objects = new ArrayList<>();
        for (int i = 0; i < getNumberOfSheets(); i++) {
            getAllEmbeddedObjects(getSheetAt(i).getSheet().getRecords(), objects);
        }
        return objects;
    }

    private void getAllEmbeddedObjects(List records, List<HSSFObjectData> objects) {
        for (Object obj : records) {
            if (obj instanceof ObjRecord) {
                for (Object sub : ((ObjRecord) obj).getSubRecords()) {
                    if (sub instanceof EmbeddedObjectRefSubRecord) {
                        objects.add(new HSSFObjectData((ObjRecord) obj, this.filesystem));
                    }
                }
            }
        }
    }

    public CreationHelper getCreationHelper() {
        return new HSSFCreationHelper(this);
    }

    private static byte[] newUID() {
        return new byte[16];
    }

    public NameXPtg getNameXPtg(String name) {
        return this.workbook.getNameXPtg(name);
    }
}

package org.apache.poi.p009ss.util;

import java.util.ArrayList;
import java.util.StringTokenizer;
import org.apache.poi.p009ss.SpreadsheetVersion;

/* renamed from: org.apache.poi.ss.util.AreaReference */
public class AreaReference {
    private static final char CELL_DELIMITER = ':';
    private static final char SHEET_NAME_DELIMITER = '!';
    private static final char SPECIAL_NAME_DELIMITER = '\'';
    private final CellReference _firstCell;
    private final boolean _isSingleCell;
    private final CellReference _lastCell;

    public AreaReference(String reference) {
        if (!isContiguous(reference)) {
            throw new IllegalArgumentException("References passed to the AreaReference must be contiguous, use generateContiguous(ref) if you have non-contiguous references");
        }
        String[] parts = separateAreaRefs(reference);
        String part0 = parts[0];
        if (parts.length == 1) {
            this._firstCell = new CellReference(part0);
            this._lastCell = this._firstCell;
            this._isSingleCell = true;
        } else if (parts.length != 2) {
            StringBuilder sb = new StringBuilder();
            sb.append("Bad area ref '");
            sb.append(reference);
            sb.append("'");
            throw new IllegalArgumentException(sb.toString());
        } else {
            String part1 = parts[1];
            if (!isPlainColumn(part0)) {
                this._firstCell = new CellReference(part0);
                this._lastCell = new CellReference(part1);
                this._isSingleCell = part0.equals(part1);
            } else if (!isPlainColumn(part1)) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Bad area ref '");
                sb2.append(reference);
                sb2.append("'");
                throw new RuntimeException(sb2.toString());
            } else {
                boolean firstIsAbs = CellReference.isPartAbsolute(part0);
                boolean lastIsAbs = CellReference.isPartAbsolute(part1);
                int col0 = CellReference.convertColStringToIndex(part0);
                int col1 = CellReference.convertColStringToIndex(part1);
                this._firstCell = new CellReference(0, col0, true, firstIsAbs);
                this._lastCell = new CellReference(65535, col1, true, lastIsAbs);
                this._isSingleCell = false;
            }
        }
    }

    private boolean isPlainColumn(String refPart) {
        for (int i = refPart.length() - 1; i >= 0; i--) {
            int ch = refPart.charAt(i);
            if ((ch != 36 || i != 0) && (ch < 65 || ch > 90)) {
                return false;
            }
        }
        return true;
    }

    public AreaReference(CellReference topLeft, CellReference botRight) {
        boolean lastRowAbs;
        int lastRow;
        boolean firstRowAbs;
        int firstRow;
        boolean lastColAbs;
        int lastColumn;
        boolean firstColAbs;
        int firstColumn;
        boolean z = true;
        boolean swapRows = topLeft.getRow() > botRight.getRow();
        if (topLeft.getCol() <= botRight.getCol()) {
            z = false;
        }
        boolean swapCols = z;
        if (swapRows || swapCols) {
            if (swapRows) {
                firstRow = botRight.getRow();
                firstRowAbs = botRight.isRowAbsolute();
                lastRow = topLeft.getRow();
                lastRowAbs = topLeft.isRowAbsolute();
            } else {
                firstRow = topLeft.getRow();
                firstRowAbs = topLeft.isRowAbsolute();
                lastRow = botRight.getRow();
                lastRowAbs = botRight.isRowAbsolute();
            }
            if (swapCols) {
                firstColumn = botRight.getCol();
                firstColAbs = botRight.isColAbsolute();
                lastColumn = topLeft.getCol();
                lastColAbs = topLeft.isColAbsolute();
            } else {
                firstColumn = topLeft.getCol();
                firstColAbs = topLeft.isColAbsolute();
                lastColumn = botRight.getCol();
                lastColAbs = botRight.isColAbsolute();
            }
            this._firstCell = new CellReference(firstRow, firstColumn, firstRowAbs, firstColAbs);
            this._lastCell = new CellReference(lastRow, lastColumn, lastRowAbs, lastColAbs);
        } else {
            this._firstCell = topLeft;
            this._lastCell = botRight;
        }
        this._isSingleCell = false;
    }

    public static boolean isContiguous(String reference) {
        if (reference.indexOf(44) == -1) {
            return true;
        }
        return false;
    }

    public static AreaReference getWholeRow(String start, String end) {
        StringBuilder sb = new StringBuilder();
        sb.append("$A");
        sb.append(start);
        sb.append(":$IV");
        sb.append(end);
        return new AreaReference(sb.toString());
    }

    public static AreaReference getWholeColumn(String start, String end) {
        StringBuilder sb = new StringBuilder();
        sb.append(start);
        sb.append("$1:");
        sb.append(end);
        sb.append("$65536");
        return new AreaReference(sb.toString());
    }

    public static boolean isWholeColumnReference(CellReference topLeft, CellReference botRight) {
        if (topLeft.getRow() != 0 || !topLeft.isRowAbsolute() || botRight.getRow() != SpreadsheetVersion.EXCEL97.getLastRowIndex() || !botRight.isRowAbsolute()) {
            return false;
        }
        return true;
    }

    public boolean isWholeColumnReference() {
        return isWholeColumnReference(this._firstCell, this._lastCell);
    }

    public static AreaReference[] generateContiguous(String reference) {
        ArrayList refs = new ArrayList();
        StringTokenizer st = new StringTokenizer(reference, ",");
        while (st.hasMoreTokens()) {
            refs.add(new AreaReference(st.nextToken()));
        }
        return (AreaReference[]) refs.toArray(new AreaReference[refs.size()]);
    }

    public boolean isSingleCell() {
        return this._isSingleCell;
    }

    public CellReference getFirstCell() {
        return this._firstCell;
    }

    public CellReference getLastCell() {
        return this._lastCell;
    }

    public CellReference[] getAllReferencedCells() {
        if (this._isSingleCell) {
            return new CellReference[]{this._firstCell};
        }
        int minRow = Math.min(this._firstCell.getRow(), this._lastCell.getRow());
        int maxRow = Math.max(this._firstCell.getRow(), this._lastCell.getRow());
        int minCol = Math.min(this._firstCell.getCol(), this._lastCell.getCol());
        int maxCol = Math.max(this._firstCell.getCol(), this._lastCell.getCol());
        String sheetName = this._firstCell.getSheetName();
        ArrayList refs = new ArrayList();
        int col = minRow;
        while (true) {
            int row = col;
            if (row > maxRow) {
                return (CellReference[]) refs.toArray(new CellReference[refs.size()]);
            }
            int col2 = minCol;
            while (true) {
                int col3 = col2;
                if (col3 > maxCol) {
                    break;
                }
                CellReference ref = new CellReference(sheetName, row, col3, this._firstCell.isRowAbsolute(), this._firstCell.isColAbsolute());
                refs.add(ref);
                col2 = col3 + 1;
            }
            col = row + 1;
        }
    }

    public String formatAsString() {
        if (isWholeColumnReference()) {
            StringBuilder sb = new StringBuilder();
            sb.append(CellReference.convertNumToColString(this._firstCell.getCol()));
            sb.append(":");
            sb.append(CellReference.convertNumToColString(this._lastCell.getCol()));
            return sb.toString();
        }
        StringBuffer sb2 = new StringBuffer(32);
        sb2.append(this._firstCell.formatAsString());
        if (!this._isSingleCell) {
            sb2.append(CELL_DELIMITER);
            if (this._lastCell.getSheetName() == null) {
                sb2.append(this._lastCell.formatAsString());
            } else {
                this._lastCell.appendCellReference(sb2);
            }
        }
        return sb2.toString();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(64);
        sb.append(getClass().getName());
        sb.append(" [");
        sb.append(formatAsString());
        sb.append("]");
        return sb.toString();
    }

    private static String[] separateAreaRefs(String reference) {
        int len = reference.length();
        boolean insideDelimitedName = false;
        int delimiterPos = -1;
        int i = 0;
        while (i < len) {
            char charAt = reference.charAt(i);
            if (charAt != '\'') {
                if (charAt == ':' && !insideDelimitedName) {
                    if (delimiterPos >= 0) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("More than one cell delimiter ':' appears in area reference '");
                        sb.append(reference);
                        sb.append("'");
                        throw new IllegalArgumentException(sb.toString());
                    }
                    delimiterPos = i;
                }
            } else if (!insideDelimitedName) {
                insideDelimitedName = true;
            } else if (i >= len - 1) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Area reference '");
                sb2.append(reference);
                sb2.append("' ends with special name delimiter '");
                sb2.append(SPECIAL_NAME_DELIMITER);
                sb2.append("'");
                throw new IllegalArgumentException(sb2.toString());
            } else if (reference.charAt(i + 1) == '\'') {
                i++;
            } else {
                insideDelimitedName = false;
            }
            i++;
        }
        if (delimiterPos < 0) {
            return new String[]{reference};
        }
        String partA = reference.substring(0, delimiterPos);
        String partB = reference.substring(delimiterPos + 1);
        if (partB.indexOf(33) >= 0) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Unexpected ! in second cell reference of '");
            sb3.append(reference);
            sb3.append("'");
            throw new RuntimeException(sb3.toString());
        }
        int plingPos = partA.lastIndexOf(33);
        if (plingPos < 0) {
            return new String[]{partA, partB};
        }
        String sheetName = partA.substring(0, plingPos + 1);
        StringBuilder sb4 = new StringBuilder();
        sb4.append(sheetName);
        sb4.append(partB);
        return new String[]{partA, sb4.toString()};
    }
}

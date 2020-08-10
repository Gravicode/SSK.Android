package org.apache.poi.hssf.record.p007cf;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.p009ss.util.CellRangeAddress;

/* renamed from: org.apache.poi.hssf.record.cf.CellRangeUtil */
public final class CellRangeUtil {
    public static final int ENCLOSES = 4;
    public static final int INSIDE = 3;
    public static final int NO_INTERSECTION = 1;
    public static final int OVERLAP = 2;

    private CellRangeUtil() {
    }

    public static int intersect(CellRangeAddress crA, CellRangeAddress crB) {
        int firstRow = crB.getFirstRow();
        int lastRow = crB.getLastRow();
        int firstCol = crB.getFirstColumn();
        int lastCol = crB.getLastColumn();
        if (m68gt(crA.getFirstRow(), lastRow) || m70lt(crA.getLastRow(), firstRow) || m68gt(crA.getFirstColumn(), lastCol) || m70lt(crA.getLastColumn(), firstCol)) {
            return 1;
        }
        if (contains(crA, crB)) {
            return 3;
        }
        if (contains(crB, crA)) {
            return 4;
        }
        return 2;
    }

    public static CellRangeAddress[] mergeCellRanges(CellRangeAddress[] cellRanges) {
        if (cellRanges.length < 1) {
            return cellRanges;
        }
        List<CellRangeAddress> lst = new ArrayList<>();
        for (CellRangeAddress cr : cellRanges) {
            lst.add(cr);
        }
        return toArray(mergeCellRanges((List) lst));
    }

    private static List mergeCellRanges(List cellRangeList) {
        while (cellRangeList.size() > 1) {
            boolean somethingGotMerged = false;
            for (int i = 0; i < cellRangeList.size(); i++) {
                CellRangeAddress range1 = (CellRangeAddress) cellRangeList.get(i);
                int k = i + 1;
                while (k < cellRangeList.size()) {
                    CellRangeAddress[] mergeResult = mergeRanges(range1, (CellRangeAddress) cellRangeList.get(k));
                    if (mergeResult != null) {
                        somethingGotMerged = true;
                        cellRangeList.set(i, mergeResult[0]);
                        int j = k - 1;
                        cellRangeList.remove(k);
                        for (int k2 = 1; k2 < mergeResult.length; k2++) {
                            j++;
                            cellRangeList.add(j, mergeResult[k2]);
                        }
                        k = j;
                    }
                    k++;
                }
            }
            if (!somethingGotMerged) {
                break;
            }
        }
        return cellRangeList;
    }

    private static CellRangeAddress[] mergeRanges(CellRangeAddress range1, CellRangeAddress range2) {
        int x = intersect(range1, range2);
        switch (x) {
            case 1:
                if (!hasExactSharedBorder(range1, range2)) {
                    return null;
                }
                return new CellRangeAddress[]{createEnclosingCellRange(range1, range2)};
            case 2:
                return resolveRangeOverlap(range1, range2);
            case 3:
                return new CellRangeAddress[]{range1};
            case 4:
                return new CellRangeAddress[]{range2};
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("unexpected intersection result (");
                sb.append(x);
                sb.append(")");
                throw new RuntimeException(sb.toString());
        }
    }

    static CellRangeAddress[] resolveRangeOverlap(CellRangeAddress rangeA, CellRangeAddress rangeB) {
        if (rangeA.isFullColumnRange()) {
            if (rangeA.isFullRowRange()) {
                return null;
            }
            return sliceUp(rangeA, rangeB);
        } else if (rangeA.isFullRowRange()) {
            if (rangeB.isFullColumnRange()) {
                return null;
            }
            return sliceUp(rangeA, rangeB);
        } else if (rangeB.isFullColumnRange()) {
            return sliceUp(rangeB, rangeA);
        } else {
            if (rangeB.isFullRowRange()) {
                return sliceUp(rangeB, rangeA);
            }
            return sliceUp(rangeA, rangeB);
        }
    }

    private static CellRangeAddress[] sliceUp(CellRangeAddress crA, CellRangeAddress crB) {
        List temp = new ArrayList();
        temp.add(crB);
        if (!crA.isFullColumnRange()) {
            temp = cutHorizontally(crA.getLastRow() + 1, cutHorizontally(crA.getFirstRow(), temp));
        }
        if (!crA.isFullRowRange()) {
            temp = cutVertically(crA.getLastColumn() + 1, cutVertically(crA.getFirstColumn(), temp));
        }
        CellRangeAddress[] crParts = toArray(temp);
        temp.clear();
        temp.add(crA);
        for (CellRangeAddress crPart : crParts) {
            if (intersect(crA, crPart) != 4) {
                temp.add(crPart);
            }
        }
        return toArray(temp);
    }

    private static List cutHorizontally(int cutRow, List input) {
        List result = new ArrayList();
        CellRangeAddress[] crs = toArray(input);
        for (CellRangeAddress cr : crs) {
            if (cr.getFirstRow() >= cutRow || cutRow >= cr.getLastRow()) {
                result.add(cr);
            } else {
                result.add(new CellRangeAddress(cr.getFirstRow(), cutRow, cr.getFirstColumn(), cr.getLastColumn()));
                result.add(new CellRangeAddress(cutRow + 1, cr.getLastRow(), cr.getFirstColumn(), cr.getLastColumn()));
            }
        }
        return result;
    }

    private static List cutVertically(int cutColumn, List input) {
        List result = new ArrayList();
        CellRangeAddress[] crs = toArray(input);
        for (CellRangeAddress cr : crs) {
            if (cr.getFirstColumn() >= cutColumn || cutColumn >= cr.getLastColumn()) {
                result.add(cr);
            } else {
                result.add(new CellRangeAddress(cr.getFirstRow(), cr.getLastRow(), cr.getFirstColumn(), cutColumn));
                result.add(new CellRangeAddress(cr.getFirstRow(), cr.getLastRow(), cutColumn + 1, cr.getLastColumn()));
            }
        }
        return result;
    }

    private static CellRangeAddress[] toArray(List temp) {
        CellRangeAddress[] result = new CellRangeAddress[temp.size()];
        temp.toArray(result);
        return result;
    }

    public static boolean contains(CellRangeAddress crA, CellRangeAddress crB) {
        return m69le(crA.getFirstRow(), crB.getFirstRow()) && m67ge(crA.getLastRow(), crB.getLastRow()) && m69le(crA.getFirstColumn(), crB.getFirstColumn()) && m67ge(crA.getLastColumn(), crB.getLastColumn());
    }

    public static boolean hasExactSharedBorder(CellRangeAddress crA, CellRangeAddress crB) {
        int oFirstRow = crB.getFirstRow();
        int oLastRow = crB.getLastRow();
        int oFirstCol = crB.getFirstColumn();
        int oLastCol = crB.getLastColumn();
        boolean z = false;
        if ((crA.getFirstRow() > 0 && crA.getFirstRow() - 1 == oLastRow) || (oFirstRow > 0 && oFirstRow - 1 == crA.getLastRow())) {
            if (crA.getFirstColumn() == oFirstCol && crA.getLastColumn() == oLastCol) {
                z = true;
            }
            return z;
        } else if ((crA.getFirstColumn() <= 0 || crA.getFirstColumn() - 1 != oLastCol) && (oFirstCol <= 0 || crA.getLastColumn() != oFirstCol - 1)) {
            return false;
        } else {
            if (crA.getFirstRow() == oFirstRow && crA.getLastRow() == oLastRow) {
                z = true;
            }
            return z;
        }
    }

    public static CellRangeAddress createEnclosingCellRange(CellRangeAddress crA, CellRangeAddress crB) {
        if (crB == null) {
            return crA.copy();
        }
        return new CellRangeAddress(m70lt(crB.getFirstRow(), crA.getFirstRow()) ? crB.getFirstRow() : crA.getFirstRow(), m68gt(crB.getLastRow(), crA.getLastRow()) ? crB.getLastRow() : crA.getLastRow(), m70lt(crB.getFirstColumn(), crA.getFirstColumn()) ? crB.getFirstColumn() : crA.getFirstColumn(), m68gt(crB.getLastColumn(), crA.getLastColumn()) ? crB.getLastColumn() : crA.getLastColumn());
    }

    /* renamed from: lt */
    private static boolean m70lt(int a, int b) {
        return a != -1 && (b == -1 || a < b);
    }

    /* renamed from: le */
    private static boolean m69le(int a, int b) {
        return a == b || m70lt(a, b);
    }

    /* renamed from: gt */
    private static boolean m68gt(int a, int b) {
        return m70lt(b, a);
    }

    /* renamed from: ge */
    private static boolean m67ge(int a, int b) {
        return !m70lt(a, b);
    }
}

package org.apache.poi.p009ss.util;

/* renamed from: org.apache.poi.ss.util.WorkbookUtil */
public class WorkbookUtil {
    public static final String createSafeSheetName(String nameProposal) {
        if (nameProposal == null) {
            return "null";
        }
        if (nameProposal.length() < 1) {
            return "empty";
        }
        int length = Math.min(31, nameProposal.length());
        StringBuilder result = new StringBuilder(nameProposal.substring(0, length));
        for (int i = 0; i < length; i++) {
            char ch = result.charAt(i);
            if (ch != '\'') {
                if (!(ch == '*' || ch == '/' || ch == '?')) {
                    switch (ch) {
                        case '[':
                        case '\\':
                        case ']':
                            break;
                    }
                }
                result.setCharAt(i, ' ');
            } else if (i == 0 || i == length - 1) {
                result.setCharAt(i, ' ');
            }
        }
        return result.toString();
    }

    public static void validateSheetName(String sheetName) {
        if (sheetName == null) {
            throw new IllegalArgumentException("sheetName must not be null");
        }
        int len = sheetName.length();
        if (len < 1) {
            throw new IllegalArgumentException("sheetName must not be empty string");
        }
        int i = 0;
        while (i < len) {
            char ch = sheetName.charAt(i);
            if (!(ch == '*' || ch == '/' || ch == ':' || ch == '?')) {
                switch (ch) {
                    case '[':
                    case '\\':
                    case ']':
                        break;
                    default:
                        i++;
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid char (");
            sb.append(ch);
            sb.append(") found at index (");
            sb.append(i);
            sb.append(") in sheet name '");
            sb.append(sheetName);
            sb.append("'");
            throw new IllegalArgumentException(sb.toString());
        }
        if (sheetName.charAt(0) == '\'' || sheetName.charAt(len - 1) == '\'') {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Invalid sheet name '");
            sb2.append(sheetName);
            sb2.append("'. Sheet names must not begin or end with (').");
            throw new IllegalArgumentException(sb2.toString());
        }
    }

    public static void validateSheetState(int state) {
        switch (state) {
            case 0:
            case 1:
            case 2:
                return;
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Ivalid sheet state : ");
                sb.append(state);
                sb.append("\n");
                sb.append("Sheet state must beone of the Workbook.SHEET_STATE_* constants");
                throw new IllegalArgumentException(sb.toString());
        }
    }
}

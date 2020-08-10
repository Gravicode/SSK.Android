package org.apache.poi.p009ss.usermodel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* renamed from: org.apache.poi.ss.usermodel.BuiltinFormats */
public final class BuiltinFormats {
    public static final int FIRST_USER_DEFINED_FORMAT_INDEX = 164;
    private static final String[] _formats;

    static {
        List<String> m = new ArrayList<>();
        putFormat(m, 0, "General");
        putFormat(m, 1, "0");
        putFormat(m, 2, "0.00");
        putFormat(m, 3, "#,##0");
        putFormat(m, 4, "#,##0.00");
        putFormat(m, 5, "$#,##0_);($#,##0)");
        putFormat(m, 6, "$#,##0_);[Red]($#,##0)");
        putFormat(m, 7, "$#,##0.00_);($#,##0.00)");
        putFormat(m, 8, "$#,##0.00_);[Red]($#,##0.00)");
        putFormat(m, 9, "0%");
        putFormat(m, 10, "0.00%");
        putFormat(m, 11, "0.00E+00");
        putFormat(m, 12, "# ?/?");
        putFormat(m, 13, "# ??/??");
        putFormat(m, 14, "m/d/yy");
        putFormat(m, 15, "d-mmm-yy");
        putFormat(m, 16, "d-mmm");
        putFormat(m, 17, "mmm-yy");
        putFormat(m, 18, "h:mm AM/PM");
        putFormat(m, 19, "h:mm:ss AM/PM");
        putFormat(m, 20, "h:mm");
        putFormat(m, 21, "h:mm:ss");
        putFormat(m, 22, "m/d/yy h:mm");
        for (int i = 23; i <= 36; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append("reserved-0x");
            sb.append(Integer.toHexString(i));
            putFormat(m, i, sb.toString());
        }
        putFormat(m, 37, "#,##0_);(#,##0)");
        putFormat(m, 38, "#,##0_);[Red](#,##0)");
        putFormat(m, 39, "#,##0.00_);(#,##0.00)");
        putFormat(m, 40, "#,##0.00_);[Red](#,##0.00)");
        putFormat(m, 41, "_(*#,##0_);_(*(#,##0);_(* \"-\"_);_(@_)");
        putFormat(m, 42, "_($*#,##0_);_($*(#,##0);_($* \"-\"_);_(@_)");
        putFormat(m, 43, "_(*#,##0.00_);_(*(#,##0.00);_(*\"-\"??_);_(@_)");
        putFormat(m, 44, "_($*#,##0.00_);_($*(#,##0.00);_($*\"-\"??_);_(@_)");
        putFormat(m, 45, "mm:ss");
        putFormat(m, 46, "[h]:mm:ss");
        putFormat(m, 47, "mm:ss.0");
        putFormat(m, 48, "##0.0E+0");
        putFormat(m, 49, "@");
        String[] ss = new String[m.size()];
        m.toArray(ss);
        _formats = ss;
    }

    private static void putFormat(List<String> m, int index, String value) {
        if (m.size() != index) {
            StringBuilder sb = new StringBuilder();
            sb.append("index ");
            sb.append(index);
            sb.append(" is wrong");
            throw new IllegalStateException(sb.toString());
        }
        m.add(value);
    }

    public static Map<Integer, String> getBuiltinFormats() {
        Map<Integer, String> result = new LinkedHashMap<>();
        for (int i = 0; i < _formats.length; i++) {
            result.put(Integer.valueOf(i), _formats[i]);
        }
        return result;
    }

    public static String[] getAll() {
        return (String[]) _formats.clone();
    }

    public static String getBuiltinFormat(int index) {
        if (index < 0 || index >= _formats.length) {
            return null;
        }
        return _formats[index];
    }

    public static int getBuiltinFormat(String pFmt) {
        String fmt;
        if (pFmt.equalsIgnoreCase("TEXT")) {
            fmt = "@";
        } else {
            fmt = pFmt;
        }
        for (int i = 0; i < _formats.length; i++) {
            if (fmt.equals(_formats[i])) {
                return i;
            }
        }
        return -1;
    }
}

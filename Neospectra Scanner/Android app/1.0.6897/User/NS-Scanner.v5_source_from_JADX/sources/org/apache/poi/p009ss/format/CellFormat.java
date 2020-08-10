package org.apache.poi.p009ss.format;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JLabel;
import org.apache.poi.p009ss.usermodel.Cell;

/* renamed from: org.apache.poi.ss.format.CellFormat */
public class CellFormat {
    private static final CellFormatPart DEFAULT_TEXT_FORMAT = new CellFormatPart("@");
    public static final CellFormat GENERAL_FORMAT = new CellFormat("General") {
        public CellFormatResult apply(Object value) {
            String text;
            if (value == null) {
                text = "";
            } else if (value instanceof Number) {
                text = CellNumberFormatter.SIMPLE_NUMBER.format(value);
            } else {
                text = value.toString();
            }
            return new CellFormatResult(true, text, null);
        }
    };
    private static final Pattern ONE_PART;
    private static final Map<String, CellFormat> formatCache = new WeakHashMap();
    private final String format;
    private final CellFormatPart negNumFmt;
    private final CellFormatPart posNumFmt;
    private final CellFormatPart textFmt;
    private final CellFormatPart zeroNumFmt;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(CellFormatPart.FORMAT_PAT.pattern());
        sb.append("(;|$)");
        ONE_PART = Pattern.compile(sb.toString(), 6);
    }

    public static CellFormat getInstance(String format2) {
        CellFormat fmt = (CellFormat) formatCache.get(format2);
        if (fmt == null) {
            if (format2.equals("General")) {
                fmt = GENERAL_FORMAT;
            } else {
                fmt = new CellFormat(format2);
            }
            formatCache.put(format2, fmt);
        }
        return fmt;
    }

    private CellFormat(String format2) {
        this.format = format2;
        Matcher m = ONE_PART.matcher(format2);
        List<CellFormatPart> parts = new ArrayList<>();
        while (m.find()) {
            try {
                String valueDesc = m.group();
                if (valueDesc.endsWith(";")) {
                    valueDesc = valueDesc.substring(0, valueDesc.length() - 1);
                }
                parts.add(new CellFormatPart(valueDesc));
            } catch (RuntimeException e) {
                Logger logger = CellFormatter.logger;
                Level level = Level.WARNING;
                StringBuilder sb = new StringBuilder();
                sb.append("Invalid format: ");
                sb.append(CellFormatter.quote(m.group()));
                logger.log(level, sb.toString(), e);
                parts.add(null);
            }
        }
        switch (parts.size()) {
            case 1:
                CellFormatPart cellFormatPart = (CellFormatPart) parts.get(0);
                this.negNumFmt = cellFormatPart;
                this.zeroNumFmt = cellFormatPart;
                this.posNumFmt = cellFormatPart;
                this.textFmt = DEFAULT_TEXT_FORMAT;
                return;
            case 2:
                CellFormatPart cellFormatPart2 = (CellFormatPart) parts.get(0);
                this.zeroNumFmt = cellFormatPart2;
                this.posNumFmt = cellFormatPart2;
                this.negNumFmt = (CellFormatPart) parts.get(1);
                this.textFmt = DEFAULT_TEXT_FORMAT;
                return;
            case 3:
                this.posNumFmt = (CellFormatPart) parts.get(0);
                this.zeroNumFmt = (CellFormatPart) parts.get(1);
                this.negNumFmt = (CellFormatPart) parts.get(2);
                this.textFmt = DEFAULT_TEXT_FORMAT;
                return;
            default:
                this.posNumFmt = (CellFormatPart) parts.get(0);
                this.zeroNumFmt = (CellFormatPart) parts.get(1);
                this.negNumFmt = (CellFormatPart) parts.get(2);
                this.textFmt = (CellFormatPart) parts.get(3);
                return;
        }
    }

    public CellFormatResult apply(Object value) {
        if (!(value instanceof Number)) {
            return this.textFmt.apply(value);
        }
        double val = ((Number) value).doubleValue();
        if (val > 0.0d) {
            return this.posNumFmt.apply(value);
        }
        if (val < 0.0d) {
            return this.negNumFmt.apply(Double.valueOf(-val));
        }
        return this.zeroNumFmt.apply(value);
    }

    public CellFormatResult apply(Cell c) {
        switch (ultimateType(c)) {
            case 0:
                return apply((Object) Double.valueOf(c.getNumericCellValue()));
            case 1:
                return apply((Object) c.getStringCellValue());
            case 3:
                return apply((Object) "");
            case 4:
                return apply((Object) c.getStringCellValue());
            default:
                return apply((Object) "?");
        }
    }

    public CellFormatResult apply(JLabel label, Object value) {
        CellFormatResult result = apply(value);
        label.setText(result.text);
        if (result.textColor != null) {
            label.setForeground(result.textColor);
        }
        return result;
    }

    public CellFormatResult apply(JLabel label, Cell c) {
        switch (ultimateType(c)) {
            case 0:
                return apply(label, (Object) Double.valueOf(c.getNumericCellValue()));
            case 1:
                return apply(label, (Object) c.getStringCellValue());
            case 3:
                return apply(label, (Object) "");
            case 4:
                return apply(label, (Object) c.getStringCellValue());
            default:
                return apply(label, (Object) "?");
        }
    }

    public static int ultimateType(Cell cell) {
        int type = cell.getCellType();
        if (type == 2) {
            return cell.getCachedFormulaResultType();
        }
        return type;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CellFormat)) {
            return false;
        }
        return this.format.equals(((CellFormat) obj).format);
    }

    public int hashCode() {
        return this.format.hashCode();
    }
}

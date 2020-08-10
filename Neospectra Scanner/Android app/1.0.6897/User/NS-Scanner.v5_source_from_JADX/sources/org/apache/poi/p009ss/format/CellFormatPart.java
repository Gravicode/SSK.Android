package org.apache.poi.p009ss.format;

import java.awt.Color;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JLabel;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.p009ss.usermodel.ShapeTypes;

/* renamed from: org.apache.poi.ss.format.CellFormatPart */
public class CellFormatPart {
    public static final int COLOR_GROUP = findGroup(FORMAT_PAT, "[Blue]@", "Blue");
    public static final Pattern COLOR_PAT;
    public static final int CONDITION_OPERATOR_GROUP = findGroup(FORMAT_PAT, "[>=1]@", ">=");
    public static final Pattern CONDITION_PAT;
    public static final int CONDITION_VALUE_GROUP = findGroup(FORMAT_PAT, "[>=1]@", "1");
    public static final Pattern FORMAT_PAT;
    private static final Map<String, Color> NAMED_COLORS = new TreeMap(String.CASE_INSENSITIVE_ORDER);
    public static final int SPECIFICATION_GROUP = findGroup(FORMAT_PAT, "[Blue][>1]\\a ?", "\\a ?");
    public static final Pattern SPECIFICATION_PAT;
    private final Color color;
    private CellFormatCondition condition;
    private final CellFormatter format;

    /* renamed from: org.apache.poi.ss.format.CellFormatPart$PartHandler */
    interface PartHandler {
        String handlePart(Matcher matcher, String str, CellFormatType cellFormatType, StringBuffer stringBuffer);
    }

    static {
        for (HSSFColor color2 : HSSFColor.getIndexHash().values()) {
            String name = color2.getClass().getSimpleName();
            if (name.equals(name.toUpperCase())) {
                short[] rgb = color2.getTriplet();
                Color c = new Color(rgb[0], rgb[1], rgb[2]);
                NAMED_COLORS.put(name, c);
                if (name.indexOf(95) > 0) {
                    NAMED_COLORS.put(name.replace('_', ' '), c);
                }
                if (name.indexOf("_PERCENT") > 0) {
                    NAMED_COLORS.put(name.replace("_PERCENT", "%").replace('_', ' '), c);
                }
            }
        }
        String condition2 = "([<>=]=?|!=|<>)    # The operator\n  \\s*([0-9]+(?:\\.[0-9]*)?)\\s*  # The constant to test against\n";
        String color3 = "\\[(black|blue|cyan|green|magenta|red|white|yellow|color [0-9]+)\\]";
        String part = "\\\\.                 # Quoted single character\n|\"([^\\\\\"]|\\\\.)*\"         # Quoted string of characters (handles escaped quotes like \\\") \n|_.                             # Space as wide as a given character\n|\\*.                           # Repeating fill character\n|@                              # Text: cell text\n|([0?\\#](?:[0?\\#,]*))         # Number: digit + other digits and commas\n|e[-+]                          # Number: Scientific: Exponent\n|m{1,5}                         # Date: month or minute spec\n|d{1,4}                         # Date: day/date spec\n|y{2,4}                         # Date: year spec\n|h{1,2}                         # Date: hour spec\n|s{1,2}                         # Date: second spec\n|am?/pm?                        # Date: am/pm spec\n|\\[h{1,2}\\]                   # Elapsed time: hour spec\n|\\[m{1,2}\\]                   # Elapsed time: minute spec\n|\\[s{1,2}\\]                   # Elapsed time: second spec\n|[^;]                           # A character\n";
        StringBuilder sb = new StringBuilder();
        sb.append("(?:");
        sb.append(color3);
        sb.append(")?                  # Text color\n");
        sb.append("(?:\\[");
        sb.append(condition2);
        sb.append("\\])?                # Condition\n");
        sb.append("((?:");
        sb.append(part);
        sb.append(")+)                        # Format spec\n");
        String format2 = sb.toString();
        COLOR_PAT = Pattern.compile(color3, 6);
        CONDITION_PAT = Pattern.compile(condition2, 6);
        SPECIFICATION_PAT = Pattern.compile(part, 6);
        FORMAT_PAT = Pattern.compile(format2, 6);
    }

    public CellFormatPart(String desc) {
        Matcher m = FORMAT_PAT.matcher(desc);
        if (!m.matches()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Unrecognized format: ");
            sb.append(CellFormatter.quote(desc));
            throw new IllegalArgumentException(sb.toString());
        }
        this.color = getColor(m);
        this.condition = getCondition(m);
        this.format = getFormatter(m);
    }

    public boolean applies(Object valueObject) {
        if (this.condition != null && (valueObject instanceof Number)) {
            return this.condition.pass(((Number) valueObject).doubleValue());
        } else if (valueObject != null) {
            return true;
        } else {
            throw new NullPointerException("valueObject");
        }
    }

    private static int findGroup(Pattern pat, String str, String marker) {
        Matcher m = pat.matcher(str);
        if (!m.find()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Pattern \"");
            sb.append(pat.pattern());
            sb.append("\" doesn't match \"");
            sb.append(str);
            sb.append("\"");
            throw new IllegalArgumentException(sb.toString());
        }
        for (int i = 1; i <= m.groupCount(); i++) {
            String grp = m.group(i);
            if (grp != null && grp.equals(marker)) {
                return i;
            }
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("\"");
        sb2.append(marker);
        sb2.append("\" not found in \"");
        sb2.append(pat.pattern());
        sb2.append("\"");
        throw new IllegalArgumentException(sb2.toString());
    }

    private static Color getColor(Matcher m) {
        String cdesc = m.group(COLOR_GROUP);
        if (cdesc == null || cdesc.length() == 0) {
            return null;
        }
        Color c = (Color) NAMED_COLORS.get(cdesc);
        if (c == null) {
            Logger logger = CellFormatter.logger;
            StringBuilder sb = new StringBuilder();
            sb.append("Unknown color: ");
            sb.append(CellFormatter.quote(cdesc));
            logger.warning(sb.toString());
        }
        return c;
    }

    private CellFormatCondition getCondition(Matcher m) {
        String mdesc = m.group(CONDITION_OPERATOR_GROUP);
        if (mdesc == null || mdesc.length() == 0) {
            return null;
        }
        return CellFormatCondition.getInstance(m.group(CONDITION_OPERATOR_GROUP), m.group(CONDITION_VALUE_GROUP));
    }

    private CellFormatter getFormatter(Matcher matcher) {
        String fdesc = matcher.group(SPECIFICATION_GROUP);
        return formatType(fdesc).formatter(fdesc);
    }

    private CellFormatType formatType(String fdesc) {
        String fdesc2 = fdesc.trim();
        if (fdesc2.equals("") || fdesc2.equalsIgnoreCase("General")) {
            return CellFormatType.GENERAL;
        }
        Matcher m = SPECIFICATION_PAT.matcher(fdesc2);
        boolean couldBeDate = false;
        boolean seenZero = false;
        while (m.find()) {
            String repl = m.group(0);
            if (repl.length() > 0) {
                switch (repl.charAt(0)) {
                    case '#':
                    case '?':
                        return CellFormatType.NUMBER;
                    case '0':
                        seenZero = true;
                        break;
                    case '@':
                        return CellFormatType.TEXT;
                    case 'D':
                    case 'Y':
                    case 'd':
                    case ShapeTypes.CLOUD /*121*/:
                        return CellFormatType.DATE;
                    case 'H':
                    case 'M':
                    case 'S':
                    case 'h':
                    case 'm':
                    case 's':
                        couldBeDate = true;
                        break;
                    case '[':
                        return CellFormatType.ELAPSED;
                }
            }
        }
        if (couldBeDate) {
            return CellFormatType.DATE;
        }
        if (seenZero) {
            return CellFormatType.NUMBER;
        }
        return CellFormatType.TEXT;
    }

    static String quoteSpecial(String repl, CellFormatType type) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < repl.length(); i++) {
            char ch = repl.charAt(i);
            if (ch != '\'' || !type.isSpecial('\'')) {
                boolean special = type.isSpecial(ch);
                if (special) {
                    sb.append("'");
                }
                sb.append(ch);
                if (special) {
                    sb.append("'");
                }
            } else {
                sb.append(0);
            }
        }
        return sb.toString();
    }

    public CellFormatResult apply(Object value) {
        Color textColor;
        String text;
        boolean applies = applies(value);
        if (applies) {
            text = this.format.format(value);
            textColor = this.color;
        } else {
            text = this.format.simpleFormat(value);
            textColor = null;
        }
        return new CellFormatResult(applies, text, textColor);
    }

    public CellFormatResult apply(JLabel label, Object value) {
        CellFormatResult result = apply(value);
        label.setText(result.text);
        if (result.textColor != null) {
            label.setForeground(result.textColor);
        }
        return result;
    }

    public static StringBuffer parseFormat(String fdesc, CellFormatType type, PartHandler partHandler) {
        int pos;
        Matcher m = SPECIFICATION_PAT.matcher(fdesc);
        StringBuffer fmt = new StringBuffer();
        while (true) {
            pos = 0;
            if (!m.find()) {
                break;
            }
            String part = group(m, 0);
            if (part.length() > 0) {
                String repl = partHandler.handlePart(m, part, type, fmt);
                if (repl == null) {
                    char charAt = part.charAt(0);
                    if (charAt == '\"') {
                        repl = quoteSpecial(part.substring(1, part.length() - 1), type);
                    } else if (charAt == '*') {
                        repl = expandChar(part);
                    } else if (charAt == '\\') {
                        repl = quoteSpecial(part.substring(1), type);
                    } else if (charAt != '_') {
                        repl = part;
                    } else {
                        repl = " ";
                    }
                }
                m.appendReplacement(fmt, Matcher.quoteReplacement(repl));
            }
        }
        m.appendTail(fmt);
        if (type.isSpecial('\'')) {
            while (true) {
                int indexOf = fmt.indexOf("''", pos);
                int pos2 = indexOf;
                if (indexOf < 0) {
                    break;
                }
                fmt.delete(pos2, pos2 + 2);
                pos = pos2;
            }
            int pos3 = 0;
            while (true) {
                int indexOf2 = fmt.indexOf("\u0000", pos3);
                pos3 = indexOf2;
                if (indexOf2 < 0) {
                    break;
                }
                fmt.replace(pos3, pos3 + 1, "''");
            }
        }
        return fmt;
    }

    static String expandChar(String part) {
        char ch = part.charAt(1);
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(ch);
        sb.append(ch);
        sb.append(ch);
        return sb.toString();
    }

    public static String group(Matcher m, int g) {
        String str = m.group(g);
        return str == null ? "" : str;
    }
}

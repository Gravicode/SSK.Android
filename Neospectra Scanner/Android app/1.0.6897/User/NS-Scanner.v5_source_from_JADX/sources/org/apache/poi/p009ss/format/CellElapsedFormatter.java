package org.apache.poi.p009ss.format;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* renamed from: org.apache.poi.ss.format.CellElapsedFormatter */
public class CellElapsedFormatter extends CellFormatter {
    private static final double HOUR__FACTOR = 0.041666666666666664d;
    private static final double MIN__FACTOR = 6.944444444444444E-4d;
    /* access modifiers changed from: private */
    public static final Pattern PERCENTS = Pattern.compile("%");
    private static final double SEC__FACTOR = 1.1574074074074073E-5d;
    private final String printfFmt;
    private final List<TimeSpec> specs = new ArrayList();
    /* access modifiers changed from: private */
    public TimeSpec topmost;

    /* renamed from: org.apache.poi.ss.format.CellElapsedFormatter$ElapsedPartHandler */
    private class ElapsedPartHandler implements PartHandler {
        private ElapsedPartHandler() {
        }

        public String handlePart(Matcher m, String part, CellFormatType type, StringBuffer desc) {
            int pos = desc.length();
            char firstCh = part.charAt(0);
            if (firstCh == 10) {
                return "%n";
            }
            if (firstCh == '\"') {
                part = part.substring(1, part.length() - 1);
            } else if (firstCh != '*') {
                if (firstCh != '0') {
                    if (firstCh != '_') {
                        if (!(firstCh == 'h' || firstCh == 'm' || firstCh == 's')) {
                            switch (firstCh) {
                                case '[':
                                    if (part.length() >= 3) {
                                        if (CellElapsedFormatter.this.topmost != null) {
                                            throw new IllegalArgumentException("Duplicate '[' times in format");
                                        }
                                        String part2 = part.toLowerCase();
                                        int specLen = part2.length() - 2;
                                        CellElapsedFormatter.this.topmost = CellElapsedFormatter.this.assignSpec(part2.charAt(1), pos, specLen);
                                        return part2.substring(1, specLen + 1);
                                    }
                                    break;
                                case '\\':
                                    part = part.substring(1);
                                    break;
                            }
                        }
                    } else {
                        return null;
                    }
                }
                String part3 = part.toLowerCase();
                CellElapsedFormatter.this.assignSpec(part3.charAt(0), pos, part3.length());
                return part3;
            } else if (part.length() > 1) {
                part = CellFormatPart.expandChar(part);
            }
            return CellElapsedFormatter.PERCENTS.matcher(part).replaceAll("%%");
        }
    }

    /* renamed from: org.apache.poi.ss.format.CellElapsedFormatter$TimeSpec */
    private static class TimeSpec {
        final double factor;
        final int len;
        double modBy = 0.0d;
        final int pos;
        final char type;

        public TimeSpec(char type2, int pos2, int len2, double factor2) {
            this.type = type2;
            this.pos = pos2;
            this.len = len2;
            this.factor = factor2;
        }

        public long valueFor(double elapsed) {
            double val;
            if (this.modBy == 0.0d) {
                val = elapsed / this.factor;
            } else {
                val = (elapsed / this.factor) % this.modBy;
            }
            if (this.type == '0') {
                return Math.round(val);
            }
            return (long) val;
        }
    }

    public CellElapsedFormatter(String pattern) {
        super(pattern);
        StringBuffer desc = CellFormatPart.parseFormat(pattern, CellFormatType.ELAPSED, new ElapsedPartHandler());
        ListIterator<TimeSpec> it = this.specs.listIterator(this.specs.size());
        while (it.hasPrevious()) {
            TimeSpec spec = (TimeSpec) it.previous();
            int i = spec.pos;
            int i2 = spec.pos + spec.len;
            StringBuilder sb = new StringBuilder();
            sb.append("%0");
            sb.append(spec.len);
            sb.append("d");
            desc.replace(i, i2, sb.toString());
            if (spec.type != this.topmost.type) {
                spec.modBy = modFor(spec.type, spec.len);
            }
        }
        this.printfFmt = desc.toString();
    }

    /* access modifiers changed from: private */
    public TimeSpec assignSpec(char type, int pos, int len) {
        TimeSpec spec = new TimeSpec(type, pos, len, factorFor(type, len));
        this.specs.add(spec);
        return spec;
    }

    private static double factorFor(char type, int len) {
        if (type == '0') {
            return SEC__FACTOR / Math.pow(10.0d, (double) len);
        }
        if (type == 'h') {
            return HOUR__FACTOR;
        }
        if (type == 'm') {
            return MIN__FACTOR;
        }
        if (type == 's') {
            return SEC__FACTOR;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Uknown elapsed time spec: ");
        sb.append(type);
        throw new IllegalArgumentException(sb.toString());
    }

    private static double modFor(char type, int len) {
        if (type == '0') {
            return Math.pow(10.0d, (double) len);
        }
        if (type == 'h') {
            return 24.0d;
        }
        if (type == 'm' || type == 's') {
            return 60.0d;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Uknown elapsed time spec: ");
        sb.append(type);
        throw new IllegalArgumentException(sb.toString());
    }

    public void formatValue(StringBuffer toAppendTo, Object value) {
        double elapsed = ((Number) value).doubleValue();
        if (elapsed < 0.0d) {
            toAppendTo.append('-');
            elapsed = -elapsed;
        }
        Object[] parts = new Long[this.specs.size()];
        for (int i = 0; i < this.specs.size(); i++) {
            parts[i] = Long.valueOf(((TimeSpec) this.specs.get(i)).valueFor(elapsed));
        }
        new Formatter(toAppendTo).format(this.printfFmt, parts);
    }

    public void simpleValue(StringBuffer toAppendTo, Object value) {
        formatValue(toAppendTo, value);
    }
}

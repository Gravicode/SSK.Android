package org.apache.poi.p009ss.format;

import java.text.AttributedCharacterIterator;
import java.text.DateFormat;
import java.text.DateFormat.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;

/* renamed from: org.apache.poi.ss.format.CellDateFormatter */
public class CellDateFormatter extends CellFormatter {
    private static final Date EXCEL_EPOCH_DATE;
    private static final long EXCEL_EPOCH_TIME;
    private static final CellFormatter SIMPLE_DATE = new CellDateFormatter("mm/d/y");
    /* access modifiers changed from: private */
    public boolean amPmUpper;
    private final DateFormat dateFmt;
    /* access modifiers changed from: private */
    public String sFmt;
    /* access modifiers changed from: private */
    public boolean showAmPm;
    /* access modifiers changed from: private */
    public boolean showM;

    /* renamed from: org.apache.poi.ss.format.CellDateFormatter$DatePartHandler */
    private class DatePartHandler implements PartHandler {
        private int hLen;
        private int hStart;
        private int mLen;
        private int mStart;

        private DatePartHandler() {
            this.mStart = -1;
            this.hStart = -1;
        }

        /* JADX WARNING: type inference failed for: r1v0 */
        /* JADX WARNING: type inference failed for: r1v3, types: [boolean] */
        /* JADX WARNING: type inference failed for: r1v5 */
        /* JADX WARNING: type inference failed for: r1v15, types: [int] */
        /* JADX WARNING: type inference failed for: r1v16, types: [int] */
        /* JADX WARNING: type inference failed for: r1v19 */
        /* JADX WARNING: type inference failed for: r1v20 */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r1v0
          assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], int, ?[boolean, int, float, short, byte, char]]
          uses: [boolean, ?[int, byte, short, char], int]
          mth insns count: 83
        	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
        	at jadx.core.ProcessClass.process(ProcessClass.java:30)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
         */
        /* JADX WARNING: Unknown variable types count: 3 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.String handlePart(java.util.regex.Matcher r8, java.lang.String r9, org.apache.poi.p009ss.format.CellFormatType r10, java.lang.StringBuffer r11) {
            /*
                r7 = this;
                int r0 = r11.length()
                r1 = 0
                char r2 = r9.charAt(r1)
                r3 = 109(0x6d, float:1.53E-43)
                r4 = -1
                switch(r2) {
                    case 48: goto L_0x00ae;
                    case 65: goto L_0x0071;
                    case 68: goto L_0x0056;
                    case 72: goto L_0x0047;
                    case 77: goto L_0x003a;
                    case 80: goto L_0x0071;
                    case 83: goto L_0x0021;
                    case 89: goto L_0x0011;
                    case 97: goto L_0x0071;
                    case 100: goto L_0x0056;
                    case 104: goto L_0x0047;
                    case 109: goto L_0x003a;
                    case 112: goto L_0x0071;
                    case 115: goto L_0x0021;
                    case 121: goto L_0x0011;
                    default: goto L_0x000f;
                }
            L_0x000f:
                goto L_0x00e2
            L_0x0011:
                r7.mStart = r4
                int r1 = r9.length()
                r3 = 3
                if (r1 != r3) goto L_0x001c
                java.lang.String r9 = "yyyy"
            L_0x001c:
                java.lang.String r1 = r9.toLowerCase()
                return r1
            L_0x0021:
                int r5 = r7.mStart
                if (r5 < 0) goto L_0x0035
            L_0x0026:
                int r5 = r7.mLen
                if (r1 >= r5) goto L_0x0033
                int r5 = r7.mStart
                int r5 = r5 + r1
                r11.setCharAt(r5, r3)
                int r1 = r1 + 1
                goto L_0x0026
            L_0x0033:
                r7.mStart = r4
            L_0x0035:
                java.lang.String r1 = r9.toLowerCase()
                return r1
            L_0x003a:
                r7.mStart = r0
                int r1 = r9.length()
                r7.mLen = r1
                java.lang.String r1 = r9.toUpperCase()
                return r1
            L_0x0047:
                r7.mStart = r4
                r7.hStart = r0
                int r1 = r9.length()
                r7.hLen = r1
                java.lang.String r1 = r9.toLowerCase()
                return r1
            L_0x0056:
                r7.mStart = r4
                int r1 = r9.length()
                r3 = 2
                if (r1 > r3) goto L_0x0064
                java.lang.String r1 = r9.toLowerCase()
                return r1
            L_0x0064:
                java.lang.String r1 = r9.toLowerCase()
                r3 = 100
                r4 = 69
                java.lang.String r1 = r1.replace(r3, r4)
                return r1
            L_0x0071:
                int r5 = r9.length()
                r6 = 1
                if (r5 <= r6) goto L_0x00e2
                r7.mStart = r4
                org.apache.poi.ss.format.CellDateFormatter r4 = org.apache.poi.p009ss.format.CellDateFormatter.this
                r4.showAmPm = r6
                org.apache.poi.ss.format.CellDateFormatter r4 = org.apache.poi.p009ss.format.CellDateFormatter.this
                char r5 = r9.charAt(r6)
                char r5 = java.lang.Character.toLowerCase(r5)
                if (r5 != r3) goto L_0x008d
                r3 = 1
                goto L_0x008e
            L_0x008d:
                r3 = 0
            L_0x008e:
                r4.showM = r3
                org.apache.poi.ss.format.CellDateFormatter r3 = org.apache.poi.p009ss.format.CellDateFormatter.this
                org.apache.poi.ss.format.CellDateFormatter r4 = org.apache.poi.p009ss.format.CellDateFormatter.this
                boolean r4 = r4.showM
                if (r4 != 0) goto L_0x00a7
                char r4 = r9.charAt(r1)
                boolean r4 = java.lang.Character.isUpperCase(r4)
                if (r4 == 0) goto L_0x00a6
                goto L_0x00a7
            L_0x00a6:
                goto L_0x00a8
            L_0x00a7:
                r1 = 1
            L_0x00a8:
                r3.amPmUpper = r1
                java.lang.String r1 = "a"
                return r1
            L_0x00ae:
                r7.mStart = r4
                int r1 = r9.length()
                org.apache.poi.ss.format.CellDateFormatter r3 = org.apache.poi.p009ss.format.CellDateFormatter.this
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                r4.<init>()
                java.lang.String r5 = "%0"
                r4.append(r5)
                int r5 = r1 + 2
                r4.append(r5)
                java.lang.String r5 = "."
                r4.append(r5)
                r4.append(r1)
                java.lang.String r5 = "f"
                r4.append(r5)
                java.lang.String r4 = r4.toString()
                r3.sFmt = r4
                r3 = 48
                r4 = 83
                java.lang.String r3 = r9.replace(r3, r4)
                return r3
            L_0x00e2:
                r1 = 0
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.p009ss.format.CellDateFormatter.DatePartHandler.handlePart(java.util.regex.Matcher, java.lang.String, org.apache.poi.ss.format.CellFormatType, java.lang.StringBuffer):java.lang.String");
        }

        public void finish(StringBuffer toAppendTo) {
            if (this.hStart >= 0 && !CellDateFormatter.this.showAmPm) {
                for (int i = 0; i < this.hLen; i++) {
                    toAppendTo.setCharAt(this.hStart + i, 'H');
                }
            }
        }
    }

    static {
        Calendar c = Calendar.getInstance();
        c.set(1904, 0, 1, 0, 0, 0);
        EXCEL_EPOCH_DATE = c.getTime();
        EXCEL_EPOCH_TIME = c.getTimeInMillis();
    }

    public CellDateFormatter(String format) {
        super(format);
        DatePartHandler partHandler = new DatePartHandler();
        StringBuffer descBuf = CellFormatPart.parseFormat(format, CellFormatType.DATE, partHandler);
        partHandler.finish(descBuf);
        this.dateFmt = new SimpleDateFormat(descBuf.toString());
    }

    public void formatValue(StringBuffer toAppendTo, Object value) {
        Object value2;
        Object value3;
        boolean doneMillis;
        StringBuffer stringBuffer = toAppendTo;
        if (value == null) {
            value2 = Double.valueOf(0.0d);
        } else {
            value2 = value;
        }
        if (value2 instanceof Number) {
            double v = ((Number) value2).doubleValue();
            if (v == 0.0d) {
                value2 = EXCEL_EPOCH_DATE;
            } else {
                value2 = new Date((long) (((double) EXCEL_EPOCH_TIME) + v));
            }
        }
        AttributedCharacterIterator it = this.dateFmt.formatToCharacterIterator(value2);
        boolean doneAm = false;
        boolean doneMillis2 = false;
        it.first();
        char ch = it.first();
        while (ch != 65535) {
            if (it.getAttribute(Field.MILLISECOND) == null) {
                value3 = value2;
                doneMillis = doneMillis2;
                if (it.getAttribute(Field.AM_PM) == null) {
                    stringBuffer.append(ch);
                } else if (!doneAm) {
                    if (this.showAmPm) {
                        if (this.amPmUpper) {
                            stringBuffer.append(Character.toUpperCase(ch));
                            if (this.showM) {
                                stringBuffer.append('M');
                            }
                        } else {
                            stringBuffer.append(Character.toLowerCase(ch));
                            if (this.showM) {
                                stringBuffer.append('m');
                            }
                        }
                    }
                    doneAm = true;
                }
            } else if (!doneMillis2) {
                Date dateObj = (Date) value2;
                int pos = toAppendTo.length();
                long msecs = dateObj.getTime() % 1000;
                value3 = value2;
                boolean z = doneMillis2;
                new Formatter(stringBuffer).format(LOCALE, this.sFmt, new Object[]{Double.valueOf(((double) msecs) / 1000.0d)});
                stringBuffer.delete(pos, pos + 2);
                doneMillis2 = true;
                ch = it.next();
                value2 = value3;
            } else {
                value3 = value2;
                doneMillis = doneMillis2;
            }
            doneMillis2 = doneMillis;
            ch = it.next();
            value2 = value3;
        }
        boolean z2 = doneMillis2;
    }

    public void simpleValue(StringBuffer toAppendTo, Object value) {
        SIMPLE_DATE.formatValue(toAppendTo, value);
    }
}

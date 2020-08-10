package org.apache.poi.p009ss.format;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.util.Collections;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.regex.Matcher;

/* renamed from: org.apache.poi.ss.format.CellNumberFormatter */
public class CellNumberFormatter extends CellFormatter {
    /* access modifiers changed from: private */
    public static final CellFormatter SIMPLE_FLOAT = new CellNumberFormatter("#.#");
    /* access modifiers changed from: private */
    public static final CellFormatter SIMPLE_INT = new CellNumberFormatter("#");
    static final CellFormatter SIMPLE_NUMBER = new CellFormatter("General") {
        public void formatValue(StringBuffer toAppendTo, Object value) {
            if (value != null) {
                if (!(value instanceof Number)) {
                    CellTextFormatter.SIMPLE_TEXT.formatValue(toAppendTo, value);
                } else if (((Number) value).doubleValue() % 1.0d == 0.0d) {
                    CellNumberFormatter.SIMPLE_INT.formatValue(toAppendTo, value);
                } else {
                    CellNumberFormatter.SIMPLE_FLOAT.formatValue(toAppendTo, value);
                }
            }
        }

        public void simpleValue(StringBuffer toAppendTo, Object value) {
            formatValue(toAppendTo, value);
        }
    };
    private Special afterFractional;
    private Special afterInteger;
    private DecimalFormat decimalFmt;
    /* access modifiers changed from: private */
    public Special decimalPoint;
    private String denominatorFmt;
    private List<Special> denominatorSpecials;
    private final String desc;
    /* access modifiers changed from: private */
    public Special exponent;
    private List<Special> exponentDigitSpecials;
    private List<Special> exponentSpecials;
    private List<Special> fractionalSpecials;
    /* access modifiers changed from: private */
    public boolean improperFraction;
    private boolean integerCommas;
    private List<Special> integerSpecials;
    private int maxDenominator;
    /* access modifiers changed from: private */
    public Special numerator;
    private String numeratorFmt;
    private List<Special> numeratorSpecials;
    private String printfFmt;
    private double scale = 1.0d;
    /* access modifiers changed from: private */
    public Special slash;
    /* access modifiers changed from: private */
    public final List<Special> specials = new LinkedList();

    /* renamed from: org.apache.poi.ss.format.CellNumberFormatter$Fraction */
    private static class Fraction {
        private final int denominator;
        private final int numerator;

        private Fraction(double value, double epsilon, int maxDenominator, int maxIterations) {
            long p2;
            long q2;
            long a0;
            long a1;
            long p1;
            double d = value;
            int i = maxDenominator;
            int i2 = maxIterations;
            long p12 = 2147483647L;
            double r0 = d;
            long a02 = (long) Math.floor(r0);
            if (a02 > 2147483647L) {
                StringBuilder sb = new StringBuilder();
                sb.append("Overflow trying to convert ");
                sb.append(d);
                sb.append(" to fraction (");
                sb.append(a02);
                sb.append("/");
                double d2 = r0;
                sb.append(1);
                sb.append(")");
                throw new IllegalArgumentException(sb.toString());
            }
            double r02 = r0;
            int i3 = 1;
            if (Math.abs(((double) a02) - d) < epsilon) {
                this.numerator = (int) a02;
                this.denominator = 1;
                return;
            }
            long p0 = 1;
            long q0 = 0;
            long p13 = a02;
            long q1 = 1;
            int n = 0;
            boolean stop = false;
            while (true) {
                n += i3;
                double r1 = 1.0d / (r02 - ((double) a02));
                long a03 = a02;
                long a12 = (long) Math.floor(r1);
                double r12 = r1;
                p2 = (a12 * p13) + p0;
                long p02 = p0;
                q2 = (a12 * q1) + q0;
                if (p2 > p12) {
                    long overflow = p12;
                    int i4 = n;
                    boolean z = stop;
                    break;
                } else if (q2 > p12) {
                    long j = p12;
                    int i5 = n;
                    boolean z2 = stop;
                    break;
                } else {
                    long overflow2 = p12;
                    boolean stop2 = stop;
                    double convergent = ((double) p2) / ((double) q2);
                    if (n >= i2 || Math.abs(convergent - d) <= epsilon || q2 >= ((long) i)) {
                        stop = true;
                        p1 = p13;
                        a1 = q1;
                        a0 = a03;
                    } else {
                        q0 = q1;
                        a0 = a12;
                        r02 = r12;
                        p02 = p13;
                        p1 = p2;
                        a1 = q2;
                        stop = stop2;
                    }
                    if (!stop) {
                        boolean z3 = stop;
                        p13 = p1;
                        q1 = a1;
                        a02 = a0;
                        p0 = p02;
                        p12 = overflow2;
                        i3 = 1;
                    } else if (n >= i2) {
                        int i6 = n;
                        StringBuilder sb2 = new StringBuilder();
                        boolean z4 = stop;
                        sb2.append("Unable to convert ");
                        sb2.append(d);
                        sb2.append(" to fraction after ");
                        sb2.append(i2);
                        sb2.append(" iterations");
                        throw new RuntimeException(sb2.toString());
                    } else {
                        boolean z5 = stop;
                        if (q2 < ((long) i)) {
                            this.numerator = (int) p2;
                            this.denominator = (int) q2;
                        } else {
                            this.numerator = (int) p1;
                            this.denominator = (int) a1;
                        }
                        return;
                    }
                }
            }
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Overflow trying to convert ");
            sb3.append(d);
            sb3.append(" to fraction (");
            sb3.append(p2);
            sb3.append("/");
            sb3.append(q2);
            sb3.append(")");
            throw new RuntimeException(sb3.toString());
        }

        public Fraction(double value, int maxDenominator) {
            this(value, 0.0d, maxDenominator, 100);
        }

        public int getDenominator() {
            return this.denominator;
        }

        public int getNumerator() {
            return this.numerator;
        }
    }

    /* renamed from: org.apache.poi.ss.format.CellNumberFormatter$NumPartHandler */
    private class NumPartHandler implements PartHandler {
        private char insertSignForExponent;

        private NumPartHandler() {
        }

        public String handlePart(Matcher m, String part, CellFormatType type, StringBuffer desc) {
            int pos = desc.length();
            char firstCh = part.charAt(0);
            if (firstCh != '#') {
                if (firstCh == '%') {
                    CellNumberFormatter.access$1034(CellNumberFormatter.this, 100.0d);
                } else if (firstCh != '?') {
                    if (firstCh != 'E' && firstCh != 'e') {
                        switch (firstCh) {
                            case '.':
                                if (CellNumberFormatter.this.decimalPoint == null && CellNumberFormatter.this.specials.size() > 0) {
                                    CellNumberFormatter.this.specials.add(CellNumberFormatter.this.decimalPoint = new Special('.', pos));
                                    break;
                                }
                            case '/':
                                if (CellNumberFormatter.this.slash == null && CellNumberFormatter.this.specials.size() > 0) {
                                    CellNumberFormatter.this.numerator = CellNumberFormatter.this.previousNumber();
                                    if (CellNumberFormatter.this.numerator == CellNumberFormatter.firstDigit(CellNumberFormatter.this.specials)) {
                                        CellNumberFormatter.this.improperFraction = true;
                                    }
                                    CellNumberFormatter.this.specials.add(CellNumberFormatter.this.slash = new Special('.', pos));
                                    break;
                                }
                            case '0':
                                break;
                            default:
                                return null;
                        }
                    } else if (CellNumberFormatter.this.exponent == null && CellNumberFormatter.this.specials.size() > 0) {
                        CellNumberFormatter.this.specials.add(CellNumberFormatter.this.exponent = new Special('.', pos));
                        this.insertSignForExponent = part.charAt(1);
                        return part.substring(0, 1);
                    }
                }
                return part;
            }
            if (this.insertSignForExponent != 0) {
                CellNumberFormatter.this.specials.add(new Special(this.insertSignForExponent, pos));
                desc.append(this.insertSignForExponent);
                this.insertSignForExponent = 0;
                pos++;
            }
            for (int i = 0; i < part.length(); i++) {
                CellNumberFormatter.this.specials.add(new Special(part.charAt(i), pos + i));
            }
            return part;
        }
    }

    /* renamed from: org.apache.poi.ss.format.CellNumberFormatter$Special */
    static class Special {

        /* renamed from: ch */
        final char f883ch;
        int pos;

        Special(char ch, int pos2) {
            this.f883ch = ch;
            this.pos = pos2;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("'");
            sb.append(this.f883ch);
            sb.append("' @ ");
            sb.append(this.pos);
            return sb.toString();
        }
    }

    /* renamed from: org.apache.poi.ss.format.CellNumberFormatter$StringMod */
    static class StringMod implements Comparable<StringMod> {
        public static final int AFTER = 2;
        public static final int BEFORE = 1;
        public static final int REPLACE = 3;
        Special end;
        boolean endInclusive;

        /* renamed from: op */
        final int f884op;
        final Special special;
        boolean startInclusive;
        CharSequence toAdd;

        private StringMod(Special special2, CharSequence toAdd2, int op) {
            this.special = special2;
            this.toAdd = toAdd2;
            this.f884op = op;
        }

        public StringMod(Special start, boolean startInclusive2, Special end2, boolean endInclusive2, char toAdd2) {
            this(start, startInclusive2, end2, endInclusive2);
            StringBuilder sb = new StringBuilder();
            sb.append(toAdd2);
            sb.append("");
            this.toAdd = sb.toString();
        }

        public StringMod(Special start, boolean startInclusive2, Special end2, boolean endInclusive2) {
            this.special = start;
            this.startInclusive = startInclusive2;
            this.end = end2;
            this.endInclusive = endInclusive2;
            this.f884op = 3;
            this.toAdd = "";
        }

        public int compareTo(StringMod that) {
            int diff = this.special.pos - that.special.pos;
            if (diff != 0) {
                return diff;
            }
            return this.f884op - that.f884op;
        }

        public boolean equals(Object that) {
            boolean z = false;
            try {
                if (compareTo((StringMod) that) == 0) {
                    z = true;
                }
                return z;
            } catch (RuntimeException e) {
                return false;
            }
        }

        public int hashCode() {
            return this.special.hashCode() + this.f884op;
        }
    }

    static /* synthetic */ double access$1034(CellNumberFormatter x0, double x1) {
        double d = x0.scale * x1;
        x0.scale = d;
        return d;
    }

    public CellNumberFormatter(String format) {
        int precision;
        super(format);
        StringBuffer descBuf = CellFormatPart.parseFormat(format, CellFormatType.NUMBER, new NumPartHandler());
        if (!((this.decimalPoint == null && this.exponent == null) || this.slash == null)) {
            this.slash = null;
            this.numerator = null;
        }
        interpretCommas(descBuf);
        int fractionPartWidth = 0;
        if (this.decimalPoint == null) {
            precision = 0;
        } else {
            int precision2 = interpretPrecision();
            fractionPartWidth = precision2 + 1;
            if (precision2 == 0) {
                this.specials.remove(this.decimalPoint);
                this.decimalPoint = null;
            }
            precision = precision2;
        }
        if (precision == 0) {
            this.fractionalSpecials = Collections.emptyList();
        } else {
            this.fractionalSpecials = this.specials.subList(this.specials.indexOf(this.decimalPoint) + 1, fractionalEnd());
        }
        if (this.exponent == null) {
            this.exponentSpecials = Collections.emptyList();
        } else {
            int exponentPos = this.specials.indexOf(this.exponent);
            this.exponentSpecials = specialsFor(exponentPos, 2);
            this.exponentDigitSpecials = specialsFor(exponentPos + 2);
        }
        if (this.slash == null) {
            this.numeratorSpecials = Collections.emptyList();
            this.denominatorSpecials = Collections.emptyList();
        } else {
            if (this.numerator == null) {
                this.numeratorSpecials = Collections.emptyList();
            } else {
                this.numeratorSpecials = specialsFor(this.specials.indexOf(this.numerator));
            }
            this.denominatorSpecials = specialsFor(this.specials.indexOf(this.slash) + 1);
            if (this.denominatorSpecials.isEmpty()) {
                this.numeratorSpecials = Collections.emptyList();
            } else {
                this.maxDenominator = maxValue(this.denominatorSpecials);
                this.numeratorFmt = singleNumberFormat(this.numeratorSpecials);
                this.denominatorFmt = singleNumberFormat(this.denominatorSpecials);
            }
        }
        this.integerSpecials = this.specials.subList(0, integerEnd());
        if (this.exponent == null) {
            StringBuffer fmtBuf = new StringBuffer("%");
            int totalWidth = calculateIntegerPartWidth() + fractionPartWidth;
            fmtBuf.append('0');
            fmtBuf.append(totalWidth);
            fmtBuf.append('.');
            fmtBuf.append(precision);
            fmtBuf.append("f");
            this.printfFmt = fmtBuf.toString();
        } else {
            StringBuffer fmtBuf2 = new StringBuffer();
            boolean first = true;
            List<Special> specialList = this.integerSpecials;
            if (this.integerSpecials.size() == 1) {
                fmtBuf2.append("0");
                first = false;
            } else {
                for (Special s : specialList) {
                    if (isDigitFmt(s)) {
                        fmtBuf2.append(first ? '#' : '0');
                        first = false;
                    }
                }
            }
            if (this.fractionalSpecials.size() > 0) {
                fmtBuf2.append('.');
                for (Special s2 : this.fractionalSpecials) {
                    if (isDigitFmt(s2)) {
                        if (!first) {
                            fmtBuf2.append('0');
                        }
                        first = false;
                    }
                }
            }
            fmtBuf2.append('E');
            placeZeros(fmtBuf2, this.exponentSpecials.subList(2, this.exponentSpecials.size()));
            this.decimalFmt = new DecimalFormat(fmtBuf2.toString());
        }
        if (this.exponent != null) {
            this.scale = 1.0d;
        }
        this.desc = descBuf.toString();
    }

    private static void placeZeros(StringBuffer sb, List<Special> specials2) {
        for (Special s : specials2) {
            if (isDigitFmt(s)) {
                sb.append('0');
            }
        }
    }

    /* access modifiers changed from: private */
    public static Special firstDigit(List<Special> specials2) {
        for (Special s : specials2) {
            if (isDigitFmt(s)) {
                return s;
            }
        }
        return null;
    }

    static StringMod insertMod(Special special, CharSequence toAdd, int where) {
        return new StringMod(special, toAdd, where);
    }

    static StringMod deleteMod(Special start, boolean startInclusive, Special end, boolean endInclusive) {
        return new StringMod(start, startInclusive, end, endInclusive);
    }

    static StringMod replaceMod(Special start, boolean startInclusive, Special end, boolean endInclusive, char withChar) {
        StringMod stringMod = new StringMod(start, startInclusive, end, endInclusive, withChar);
        return stringMod;
    }

    private static String singleNumberFormat(List<Special> numSpecials) {
        StringBuilder sb = new StringBuilder();
        sb.append("%0");
        sb.append(numSpecials.size());
        sb.append("d");
        return sb.toString();
    }

    private static int maxValue(List<Special> s) {
        return (int) Math.round(Math.pow(10.0d, (double) s.size()) - 1.0d);
    }

    private List<Special> specialsFor(int pos, int takeFirst) {
        if (pos >= this.specials.size()) {
            return Collections.emptyList();
        }
        ListIterator<Special> it = this.specials.listIterator(pos + takeFirst);
        Special last = (Special) it.next();
        int end = pos + takeFirst;
        while (it.hasNext()) {
            Special s = (Special) it.next();
            if (!isDigitFmt(s) || s.pos - last.pos > 1) {
                break;
            }
            end++;
            last = s;
        }
        return this.specials.subList(pos, end + 1);
    }

    private List<Special> specialsFor(int pos) {
        return specialsFor(pos, 0);
    }

    private static boolean isDigitFmt(Special s) {
        return s.f883ch == '0' || s.f883ch == '?' || s.f883ch == '#';
    }

    /* access modifiers changed from: private */
    public Special previousNumber() {
        ListIterator<Special> it = this.specials.listIterator(this.specials.size());
        while (it.hasPrevious()) {
            Special last = (Special) it.previous();
            if (isDigitFmt(last)) {
                Special numStart = last;
                while (it.hasPrevious()) {
                    Special s = (Special) it.previous();
                    if (last.pos - s.pos > 1 || !isDigitFmt(s)) {
                        break;
                    }
                    numStart = s;
                    last = s;
                }
                return numStart;
            }
        }
        return null;
    }

    private int calculateIntegerPartWidth() {
        ListIterator<Special> it = this.specials.listIterator();
        int digitCount = 0;
        while (it.hasNext()) {
            Special s = (Special) it.next();
            if (s == this.afterInteger) {
                break;
            } else if (isDigitFmt(s)) {
                digitCount++;
            }
        }
        return digitCount;
    }

    private int interpretPrecision() {
        if (this.decimalPoint == null) {
            return -1;
        }
        int precision = 0;
        ListIterator<Special> it = this.specials.listIterator(this.specials.indexOf(this.decimalPoint));
        if (it.hasNext()) {
            it.next();
        }
        while (it.hasNext() && isDigitFmt((Special) it.next())) {
            precision++;
        }
        return precision;
    }

    private void interpretCommas(StringBuffer sb) {
        ListIterator<Special> it = this.specials.listIterator(integerEnd());
        boolean stillScaling = true;
        int removed = 0;
        this.integerCommas = false;
        while (it.hasPrevious()) {
            if (((Special) it.previous()).f883ch != ',') {
                stillScaling = false;
            } else if (stillScaling) {
                this.scale /= 1000.0d;
            } else {
                this.integerCommas = true;
            }
        }
        if (this.decimalPoint != null) {
            ListIterator<Special> it2 = this.specials.listIterator(fractionalEnd());
            while (it2.hasPrevious() && ((Special) it2.previous()).f883ch == ',') {
                this.scale /= 1000.0d;
            }
        }
        ListIterator<Special> it3 = this.specials.listIterator();
        while (it3.hasNext()) {
            Special s = (Special) it3.next();
            s.pos -= removed;
            if (s.f883ch == ',') {
                removed++;
                it3.remove();
                sb.deleteCharAt(s.pos);
            }
        }
    }

    private int integerEnd() {
        if (this.decimalPoint != null) {
            this.afterInteger = this.decimalPoint;
        } else if (this.exponent != null) {
            this.afterInteger = this.exponent;
        } else if (this.numerator != null) {
            this.afterInteger = this.numerator;
        } else {
            this.afterInteger = null;
        }
        return this.afterInteger == null ? this.specials.size() : this.specials.indexOf(this.afterInteger);
    }

    private int fractionalEnd() {
        if (this.exponent != null) {
            this.afterFractional = this.exponent;
        } else if (this.numerator != null) {
            this.afterInteger = this.numerator;
        } else {
            this.afterFractional = null;
        }
        return this.afterFractional == null ? this.specials.size() : this.specials.indexOf(this.afterFractional);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:72:0x01b8, code lost:
        r1.insert(r6 + r18, r3.toAdd);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x01c0, code lost:
        r7 = r27;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x01c2, code lost:
        r4 = r4 + (r1.length() - r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x01cc, code lost:
        if (r2.hasNext() == false) goto L_0x01d6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x01ce, code lost:
        r3 = (org.apache.poi.p009ss.format.CellNumberFormatter.StringMod) r2.next();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x01d6, code lost:
        r3 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x01d8, code lost:
        r0 = r25;
        r6 = r26;
        r13 = r28;
        r8 = r32;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void formatValue(java.lang.StringBuffer r33, java.lang.Object r34) {
        /*
            r32 = this;
            r8 = r32
            r9 = r33
            r0 = r34
            java.lang.Number r0 = (java.lang.Number) r0
            double r0 = r0.doubleValue()
            double r2 = r8.scale
            double r0 = r0 * r2
            r2 = 0
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            r11 = 0
            r3 = 1
            if (r2 >= 0) goto L_0x001a
            r2 = 1
            goto L_0x001b
        L_0x001a:
            r2 = 0
        L_0x001b:
            r12 = r2
            if (r12 == 0) goto L_0x001f
            double r0 = -r0
        L_0x001f:
            r4 = 0
            org.apache.poi.ss.format.CellNumberFormatter$Special r2 = r8.slash
            if (r2 == 0) goto L_0x0033
            boolean r2 = r8.improperFraction
            if (r2 == 0) goto L_0x002d
            r4 = r0
            r0 = 0
            goto L_0x0033
        L_0x002d:
            r6 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r4 = r0 % r6
            long r6 = (long) r0
            double r0 = (double) r6
        L_0x0033:
            r13 = r0
            r15 = r4
            java.util.TreeSet r0 = new java.util.TreeSet
            r0.<init>()
            r7 = r0
            java.lang.StringBuffer r0 = new java.lang.StringBuffer
            java.lang.String r1 = r8.desc
            r0.<init>(r1)
            r6 = r0
            org.apache.poi.ss.format.CellNumberFormatter$Special r0 = r8.exponent
            if (r0 == 0) goto L_0x0050
            r8.writeScientific(r13, r6, r7)
            r19 = r6
            r18 = r7
            goto L_0x00ae
        L_0x0050:
            boolean r0 = r8.improperFraction
            if (r0 == 0) goto L_0x0062
            r3 = 0
            r0 = r8
            r1 = r13
            r4 = r15
            r17 = r6
            r18 = r7
            r0.writeFraction(r1, r3, r4, r6, r7)
            r19 = r17
            goto L_0x00ae
        L_0x0062:
            r17 = r6
            r18 = r7
            java.lang.StringBuffer r0 = new java.lang.StringBuffer
            r0.<init>()
            r7 = r0
            java.util.Formatter r0 = new java.util.Formatter
            r0.<init>(r7)
            r6 = r0
            java.util.Locale r0 = LOCALE
            java.lang.String r1 = r8.printfFmt
            java.lang.Object[] r2 = new java.lang.Object[r3]
            java.lang.Double r3 = java.lang.Double.valueOf(r13)
            r2[r11] = r3
            r6.format(r0, r1, r2)
            org.apache.poi.ss.format.CellNumberFormatter$Special r0 = r8.numerator
            if (r0 != 0) goto L_0x009d
            r5 = r17
            r8.writeFractional(r7, r5)
            java.util.List<org.apache.poi.ss.format.CellNumberFormatter$Special> r3 = r8.integerSpecials
            boolean r4 = r8.integerCommas
            r0 = r8
            r1 = r7
            r2 = r5
            r17 = r4
            r4 = r18
            r19 = r5
            r5 = r17
            r0.writeInteger(r1, r2, r3, r4, r5)
            goto L_0x00ae
        L_0x009d:
            r19 = r17
            r0 = r8
            r1 = r13
            r3 = r7
            r4 = r15
            r17 = r6
            r6 = r19
            r20 = r7
            r7 = r18
            r0.writeFraction(r1, r3, r4, r6, r7)
        L_0x00ae:
            java.util.List<org.apache.poi.ss.format.CellNumberFormatter$Special> r0 = r8.specials
            java.util.ListIterator r0 = r0.listIterator()
            r1 = r18
            java.util.Iterator r2 = r1.iterator()
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x00c7
            java.lang.Object r3 = r2.next()
            org.apache.poi.ss.format.CellNumberFormatter$StringMod r3 = (org.apache.poi.p009ss.format.CellNumberFormatter.StringMod) r3
            goto L_0x00c8
        L_0x00c7:
            r3 = 0
        L_0x00c8:
            r4 = 0
            java.util.BitSet r5 = new java.util.BitSet
            r5.<init>()
            r6 = 0
            r7 = 0
            r17 = 0
        L_0x00d2:
            boolean r18 = r0.hasNext()
            if (r18 == 0) goto L_0x01f9
            java.lang.Object r18 = r0.next()
            r11 = r18
            org.apache.poi.ss.format.CellNumberFormatter$Special r11 = (org.apache.poi.p009ss.format.CellNumberFormatter.Special) r11
            r21 = r0
            int r0 = r11.pos
            int r0 = r0 + r4
            r22 = r1
            int r1 = r11.pos
            boolean r1 = r5.get(r1)
            if (r1 != 0) goto L_0x0108
            r23 = r3
            r1 = r19
            char r3 = r1.charAt(r0)
            r24 = r6
            r6 = 35
            if (r3 != r6) goto L_0x010e
            r1.deleteCharAt(r0)
            int r4 = r4 + -1
            int r3 = r11.pos
            r5.set(r3)
            goto L_0x010e
        L_0x0108:
            r23 = r3
            r24 = r6
            r1 = r19
        L_0x010e:
            r3 = r23
            r6 = r24
        L_0x0112:
            if (r3 == 0) goto L_0x01e2
            r25 = r0
            org.apache.poi.ss.format.CellNumberFormatter$Special r0 = r3.special
            if (r11 != r0) goto L_0x01e2
            int r0 = r1.length()
            r26 = r6
            int r6 = r11.pos
            int r6 = r6 + r4
            r18 = 0
            r27 = r7
            int r7 = r3.f884op
            switch(r7) {
                case 1: goto L_0x01b6;
                case 2: goto L_0x019e;
                case 3: goto L_0x0147;
                default: goto L_0x012c;
            }
        L_0x012c:
            r28 = r13
            java.lang.IllegalStateException r7 = new java.lang.IllegalStateException
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r10 = "Unknown op: "
            r8.append(r10)
            int r10 = r3.f884op
            r8.append(r10)
            java.lang.String r8 = r8.toString()
            r7.<init>(r8)
            throw r7
        L_0x0147:
            int r7 = r11.pos
            boolean r8 = r3.startInclusive
            if (r8 != 0) goto L_0x0151
            int r7 = r7 + 1
            int r6 = r6 + 1
        L_0x0151:
            r31 = r7
            r7 = r6
            r6 = r31
        L_0x0156:
            boolean r8 = r5.get(r6)
            if (r8 == 0) goto L_0x0161
            int r6 = r6 + 1
            int r7 = r7 + 1
            goto L_0x0156
        L_0x0161:
            org.apache.poi.ss.format.CellNumberFormatter$Special r8 = r3.end
            int r8 = r8.pos
            boolean r10 = r3.endInclusive
            if (r10 == 0) goto L_0x016b
            int r8 = r8 + 1
        L_0x016b:
            int r10 = r8 + r4
            if (r7 >= r10) goto L_0x0195
            r28 = r13
            java.lang.CharSequence r13 = r3.toAdd
            java.lang.String r14 = ""
            if (r13 != r14) goto L_0x017b
            r1.delete(r7, r10)
            goto L_0x0191
        L_0x017b:
            java.lang.CharSequence r13 = r3.toAdd
            r14 = 0
            char r13 = r13.charAt(r14)
            r17 = r7
        L_0x0184:
            r30 = r17
            r14 = r30
            if (r14 >= r10) goto L_0x0191
            r1.setCharAt(r14, r13)
            int r17 = r14 + 1
            r14 = 0
            goto L_0x0184
        L_0x0191:
            r5.set(r6, r8)
            goto L_0x0197
        L_0x0195:
            r28 = r13
        L_0x0197:
            r26 = r6
            r6 = r7
            r7 = r8
            r17 = r10
            goto L_0x01c2
        L_0x019e:
            r28 = r13
            java.lang.CharSequence r7 = r3.toAdd
            java.lang.String r8 = ","
            boolean r7 = r7.equals(r8)
            if (r7 == 0) goto L_0x01b3
            int r7 = r11.pos
            boolean r7 = r5.get(r7)
            if (r7 == 0) goto L_0x01b3
            goto L_0x01c0
        L_0x01b3:
            r18 = 1
            goto L_0x01b8
        L_0x01b6:
            r28 = r13
        L_0x01b8:
            int r7 = r6 + r18
            java.lang.CharSequence r8 = r3.toAdd
            r1.insert(r7, r8)
        L_0x01c0:
            r7 = r27
        L_0x01c2:
            int r8 = r1.length()
            int r8 = r8 - r0
            int r4 = r4 + r8
            boolean r8 = r2.hasNext()
            if (r8 == 0) goto L_0x01d6
            java.lang.Object r8 = r2.next()
            r3 = r8
            org.apache.poi.ss.format.CellNumberFormatter$StringMod r3 = (org.apache.poi.p009ss.format.CellNumberFormatter.StringMod) r3
            goto L_0x01d8
        L_0x01d6:
            r0 = 0
            r3 = r0
        L_0x01d8:
            r0 = r25
            r6 = r26
            r13 = r28
            r8 = r32
            goto L_0x0112
        L_0x01e2:
            r26 = r6
            r27 = r7
            r28 = r13
            r19 = r1
            r0 = r21
            r1 = r22
            r6 = r26
            r7 = r27
            r13 = r28
            r8 = r32
            r11 = 0
            goto L_0x00d2
        L_0x01f9:
            r21 = r0
            r22 = r1
            r23 = r3
            r28 = r13
            r1 = r19
            if (r12 == 0) goto L_0x020a
            r0 = 45
            r9.append(r0)
        L_0x020a:
            r9.append(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.p009ss.format.CellNumberFormatter.formatValue(java.lang.StringBuffer, java.lang.Object):void");
    }

    private void writeScientific(double value, StringBuffer output, Set<StringMod> mods) {
        Set<StringMod> set = mods;
        StringBuffer result = new StringBuffer();
        FieldPosition fractionPos = new FieldPosition(1);
        this.decimalFmt.format(value, result, fractionPos);
        writeInteger(result, output, this.integerSpecials, set, this.integerCommas);
        StringBuffer stringBuffer = output;
        writeFractional(result, stringBuffer);
        int signPos = fractionPos.getEndIndex() + 1;
        char expSignRes = result.charAt(signPos);
        if (expSignRes != '-') {
            expSignRes = '+';
            result.insert(signPos, '+');
        }
        char expSignRes2 = expSignRes;
        ListIterator<Special> it = this.exponentSpecials.listIterator(1);
        Special expSign = (Special) it.next();
        char expSignFmt = expSign.f883ch;
        if (expSignRes2 == '-' || expSignFmt == '+') {
            set.add(replaceMod(expSign, true, expSign, true, expSignRes2));
        } else {
            set.add(deleteMod(expSign, true, expSign, true));
        }
        char c = expSignFmt;
        Special special = expSign;
        ListIterator listIterator = it;
        char c2 = expSignRes2;
        writeInteger(new StringBuffer(result.substring(signPos + 1)), stringBuffer, this.exponentDigitSpecials, set, false);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x00be, code lost:
        if (hasChar('0', r7.numeratorSpecials) == false) goto L_0x00c0;
     */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00a6  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00d9  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00e0 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x010f  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x011b  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x0155 A[Catch:{ RuntimeException -> 0x0147 }] */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x0162 A[Catch:{ RuntimeException -> 0x0147 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void writeFraction(double r20, java.lang.StringBuffer r22, double r23, java.lang.StringBuffer r25, java.util.Set<org.apache.poi.p009ss.format.CellNumberFormatter.StringMod> r26) {
        /*
            r19 = this;
            r7 = r19
            r8 = r23
            r10 = r26
            boolean r1 = r7.improperFraction
            r11 = 0
            if (r1 != 0) goto L_0x0125
            int r1 = (r8 > r11 ? 1 : (r8 == r11 ? 0 : -1))
            r6 = 2
            r5 = 63
            r2 = 48
            r4 = 0
            r3 = 1
            if (r1 != 0) goto L_0x007d
            java.util.List[] r1 = new java.util.List[r3]
            java.util.List<org.apache.poi.ss.format.CellNumberFormatter$Special> r3 = r7.numeratorSpecials
            r1[r4] = r3
            boolean r1 = hasChar(r2, r1)
            if (r1 != 0) goto L_0x007d
            java.util.List<org.apache.poi.ss.format.CellNumberFormatter$Special> r11 = r7.integerSpecials
            r12 = 0
            r1 = r7
            r2 = r22
            r13 = 1
            r3 = r25
            r4 = r11
            r11 = 63
            r5 = r10
            r11 = 2
            r6 = r12
            r1.writeInteger(r2, r3, r4, r5, r6)
            java.util.List<org.apache.poi.ss.format.CellNumberFormatter$Special> r1 = r7.integerSpecials
            java.util.List<org.apache.poi.ss.format.CellNumberFormatter$Special> r2 = r7.integerSpecials
            int r2 = r2.size()
            int r2 = r2 - r13
            java.lang.Object r1 = r1.get(r2)
            org.apache.poi.ss.format.CellNumberFormatter$Special r1 = (org.apache.poi.p009ss.format.CellNumberFormatter.Special) r1
            java.util.List<org.apache.poi.ss.format.CellNumberFormatter$Special> r2 = r7.denominatorSpecials
            java.util.List<org.apache.poi.ss.format.CellNumberFormatter$Special> r3 = r7.denominatorSpecials
            int r3 = r3.size()
            int r3 = r3 - r13
            java.lang.Object r2 = r2.get(r3)
            org.apache.poi.ss.format.CellNumberFormatter$Special r2 = (org.apache.poi.p009ss.format.CellNumberFormatter.Special) r2
            r3 = 3
            java.util.List[] r3 = new java.util.List[r3]
            java.util.List<org.apache.poi.ss.format.CellNumberFormatter$Special> r4 = r7.integerSpecials
            r5 = 0
            r3[r5] = r4
            java.util.List<org.apache.poi.ss.format.CellNumberFormatter$Special> r4 = r7.numeratorSpecials
            r3[r13] = r4
            java.util.List<org.apache.poi.ss.format.CellNumberFormatter$Special> r4 = r7.denominatorSpecials
            r3[r11] = r4
            r4 = 63
            boolean r3 = hasChar(r4, r3)
            if (r3 == 0) goto L_0x0075
            r3 = 32
            org.apache.poi.ss.format.CellNumberFormatter$StringMod r3 = replaceMod(r1, r5, r2, r13, r3)
            r10.add(r3)
            goto L_0x007c
        L_0x0075:
            org.apache.poi.ss.format.CellNumberFormatter$StringMod r3 = deleteMod(r1, r5, r2, r13)
            r10.add(r3)
        L_0x007c:
            return
        L_0x007d:
            r15 = r11
            r1 = 63
            r5 = 0
            r11 = 2
            r13 = 1
            int r3 = (r20 > r15 ? 1 : (r20 == r15 ? 0 : -1))
            if (r3 != 0) goto L_0x008d
            int r3 = (r8 > r15 ? 1 : (r8 == r15 ? 0 : -1))
            if (r3 != 0) goto L_0x008d
            r3 = 1
            goto L_0x008e
        L_0x008d:
            r3 = 0
        L_0x008e:
            r12 = r3
            int r3 = (r8 > r15 ? 1 : (r8 == r15 ? 0 : -1))
            if (r3 != 0) goto L_0x00a2
            java.util.List[] r3 = new java.util.List[r13]
            java.util.List<org.apache.poi.ss.format.CellNumberFormatter$Special> r4 = r7.numeratorSpecials
            r3[r5] = r4
            boolean r3 = hasChar(r2, r3)
            if (r3 == 0) goto L_0x00a0
            goto L_0x00a2
        L_0x00a0:
            r3 = 0
            goto L_0x00a3
        L_0x00a2:
            r3 = 1
        L_0x00a3:
            r14 = r3
            if (r12 == 0) goto L_0x00c2
            r3 = 35
            java.util.List[] r4 = new java.util.List[r13]
            java.util.List<org.apache.poi.ss.format.CellNumberFormatter$Special> r6 = r7.integerSpecials
            r4[r5] = r6
            boolean r3 = hasOnly(r3, r4)
            if (r3 != 0) goto L_0x00c0
            java.util.List[] r3 = new java.util.List[r13]
            java.util.List<org.apache.poi.ss.format.CellNumberFormatter$Special> r4 = r7.numeratorSpecials
            r3[r5] = r4
            boolean r3 = hasChar(r2, r3)
            if (r3 != 0) goto L_0x00c2
        L_0x00c0:
            r3 = 1
            goto L_0x00c3
        L_0x00c2:
            r3 = 0
        L_0x00c3:
            r17 = r3
            if (r12 != 0) goto L_0x00db
            int r3 = (r20 > r15 ? 1 : (r20 == r15 ? 0 : -1))
            if (r3 != 0) goto L_0x00db
            if (r14 == 0) goto L_0x00db
            java.util.List[] r3 = new java.util.List[r13]
            java.util.List<org.apache.poi.ss.format.CellNumberFormatter$Special> r4 = r7.integerSpecials
            r3[r5] = r4
            boolean r2 = hasChar(r2, r3)
            if (r2 != 0) goto L_0x00db
            r2 = 1
            goto L_0x00dc
        L_0x00db:
            r2 = 0
        L_0x00dc:
            r18 = r2
            if (r17 != 0) goto L_0x00f0
            if (r18 == 0) goto L_0x00e3
            goto L_0x00f0
        L_0x00e3:
            java.util.List<org.apache.poi.ss.format.CellNumberFormatter$Special> r4 = r7.integerSpecials
            r6 = 0
            r1 = r7
            r2 = r22
            r3 = r25
            r5 = r10
            r1.writeInteger(r2, r3, r4, r5, r6)
            goto L_0x0126
        L_0x00f0:
            java.util.List<org.apache.poi.ss.format.CellNumberFormatter$Special> r2 = r7.integerSpecials
            java.util.List<org.apache.poi.ss.format.CellNumberFormatter$Special> r3 = r7.integerSpecials
            int r3 = r3.size()
            int r3 = r3 - r13
            java.lang.Object r2 = r2.get(r3)
            org.apache.poi.ss.format.CellNumberFormatter$Special r2 = (org.apache.poi.p009ss.format.CellNumberFormatter.Special) r2
            java.util.List[] r3 = new java.util.List[r11]
            java.util.List<org.apache.poi.ss.format.CellNumberFormatter$Special> r4 = r7.integerSpecials
            r3[r5] = r4
            java.util.List<org.apache.poi.ss.format.CellNumberFormatter$Special> r4 = r7.numeratorSpecials
            r3[r13] = r4
            boolean r1 = hasChar(r1, r3)
            if (r1 == 0) goto L_0x011b
            org.apache.poi.ss.format.CellNumberFormatter$Special r1 = r7.numerator
            r3 = 32
            org.apache.poi.ss.format.CellNumberFormatter$StringMod r1 = replaceMod(r2, r13, r1, r5, r3)
            r10.add(r1)
            goto L_0x0124
        L_0x011b:
            org.apache.poi.ss.format.CellNumberFormatter$Special r1 = r7.numerator
            org.apache.poi.ss.format.CellNumberFormatter$StringMod r1 = deleteMod(r2, r13, r1, r5)
            r10.add(r1)
        L_0x0124:
            goto L_0x0126
        L_0x0125:
            r15 = r11
        L_0x0126:
            int r1 = (r8 > r15 ? 1 : (r8 == r15 ? 0 : -1))
            if (r1 == 0) goto L_0x014a
            boolean r1 = r7.improperFraction     // Catch:{ RuntimeException -> 0x0147 }
            if (r1 == 0) goto L_0x0137
            r1 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r1 = r8 % r1
            int r1 = (r1 > r15 ? 1 : (r1 == r15 ? 0 : -1))
            if (r1 != 0) goto L_0x0137
            goto L_0x014a
        L_0x0137:
            org.apache.poi.ss.format.CellNumberFormatter$Fraction r1 = new org.apache.poi.ss.format.CellNumberFormatter$Fraction     // Catch:{ RuntimeException -> 0x0147 }
            int r2 = r7.maxDenominator     // Catch:{ RuntimeException -> 0x0147 }
            r1.<init>(r8, r2)     // Catch:{ RuntimeException -> 0x0147 }
            int r2 = r1.getNumerator()     // Catch:{ RuntimeException -> 0x0147 }
            int r3 = r1.getDenominator()     // Catch:{ RuntimeException -> 0x0147 }
            goto L_0x0150
        L_0x0147:
            r0 = move-exception
            r1 = r0
            goto L_0x017c
        L_0x014a:
            long r1 = java.lang.Math.round(r23)     // Catch:{ RuntimeException -> 0x0147 }
            int r2 = (int) r1     // Catch:{ RuntimeException -> 0x0147 }
            r3 = 1
        L_0x0150:
            r11 = r3
            boolean r1 = r7.improperFraction     // Catch:{ RuntimeException -> 0x0147 }
            if (r1 == 0) goto L_0x0162
            long r3 = (long) r2     // Catch:{ RuntimeException -> 0x0147 }
            double r5 = (double) r11     // Catch:{ RuntimeException -> 0x0147 }
            double r5 = r5 * r20
            long r5 = java.lang.Math.round(r5)     // Catch:{ RuntimeException -> 0x0147 }
            r1 = 0
            long r3 = r3 + r5
            int r1 = (int) r3     // Catch:{ RuntimeException -> 0x0147 }
            r12 = r1
            goto L_0x0163
        L_0x0162:
            r12 = r2
        L_0x0163:
            java.lang.String r2 = r7.numeratorFmt     // Catch:{ RuntimeException -> 0x0147 }
            java.util.List<org.apache.poi.ss.format.CellNumberFormatter$Special> r5 = r7.numeratorSpecials     // Catch:{ RuntimeException -> 0x0147 }
            r1 = r7
            r3 = r12
            r4 = r25
            r6 = r10
            r1.writeSingleInteger(r2, r3, r4, r5, r6)     // Catch:{ RuntimeException -> 0x0147 }
            java.lang.String r2 = r7.denominatorFmt     // Catch:{ RuntimeException -> 0x0147 }
            java.util.List<org.apache.poi.ss.format.CellNumberFormatter$Special> r5 = r7.denominatorSpecials     // Catch:{ RuntimeException -> 0x0147 }
            r1 = r7
            r3 = r11
            r4 = r25
            r6 = r10
            r1.writeSingleInteger(r2, r3, r4, r5, r6)     // Catch:{ RuntimeException -> 0x0147 }
            goto L_0x0180
        L_0x017c:
            r1.printStackTrace()
        L_0x0180:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.p009ss.format.CellNumberFormatter.writeFraction(double, java.lang.StringBuffer, double, java.lang.StringBuffer, java.util.Set):void");
    }

    private static boolean hasChar(char ch, List<Special>... numSpecials) {
        for (List<Special> specials2 : numSpecials) {
            for (Special s : specials2) {
                if (s.f883ch == ch) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean hasOnly(char ch, List<Special>... numSpecials) {
        for (List<Special> specials2 : numSpecials) {
            for (Special s : specials2) {
                if (s.f883ch != ch) {
                    return false;
                }
            }
        }
        return true;
    }

    private void writeSingleInteger(String fmt, int num, StringBuffer output, List<Special> numSpecials, Set<StringMod> mods) {
        StringBuffer sb = new StringBuffer();
        new Formatter(sb).format(LOCALE, fmt, new Object[]{Integer.valueOf(num)});
        writeInteger(sb, output, numSpecials, mods, false);
    }

    private void writeInteger(StringBuffer result, StringBuffer output, List<Special> numSpecials, Set<StringMod> mods, boolean showCommas) {
        char c;
        char resultCh;
        StringBuffer stringBuffer = result;
        List<Special> list = numSpecials;
        Set<StringMod> set = mods;
        int pos = stringBuffer.indexOf(".") - 1;
        if (pos < 0) {
            if (this.exponent == null || list != this.integerSpecials) {
                pos = result.length() - 1;
            } else {
                pos = stringBuffer.indexOf("E") - 1;
            }
        }
        int strip = 0;
        while (true) {
            c = '0';
            if (strip >= pos) {
                break;
            }
            char resultCh2 = stringBuffer.charAt(strip);
            if (resultCh2 != '0' && resultCh2 != ',') {
                break;
            }
            strip++;
        }
        ListIterator<Special> it = list.listIterator(numSpecials.size());
        Special lastOutputIntegerDigit = null;
        int pos2 = pos;
        int digit = 0;
        while (it.hasPrevious()) {
            if (pos2 >= 0) {
                resultCh = stringBuffer.charAt(pos2);
            } else {
                resultCh = '0';
            }
            Special s = (Special) it.previous();
            boolean followWithComma = showCommas && digit > 0 && digit % 3 == 0;
            boolean zeroStrip = false;
            if (resultCh != c || s.f883ch == c || s.f883ch == '?' || pos2 >= strip) {
                zeroStrip = s.f883ch == '?' && pos2 < strip;
                output.setCharAt(s.pos, zeroStrip ? ' ' : resultCh);
                lastOutputIntegerDigit = s;
            } else {
                StringBuffer stringBuffer2 = output;
            }
            if (followWithComma) {
                set.add(insertMod(s, zeroStrip ? " " : ",", 2));
            }
            digit++;
            pos2--;
            c = '0';
        }
        StringBuffer stringBuffer3 = output;
        new StringBuffer();
        if (pos2 >= 0) {
            int pos3 = pos2 + 1;
            StringBuffer extraLeadingDigits = new StringBuffer(stringBuffer.substring(0, pos3));
            if (showCommas) {
                while (pos3 > 0) {
                    if (digit > 0 && digit % 3 == 0) {
                        extraLeadingDigits.insert(pos3, ',');
                    }
                    digit++;
                    pos3--;
                }
            }
            set.add(insertMod(lastOutputIntegerDigit, extraLeadingDigits, 1));
        }
    }

    private void writeFractional(StringBuffer result, StringBuffer output) {
        int strip;
        if (this.fractionalSpecials.size() > 0) {
            int digit = result.indexOf(".") + 1;
            if (this.exponent != null) {
                strip = result.indexOf("e");
            } else {
                strip = result.length();
            }
            while (true) {
                strip--;
                if (strip <= digit || result.charAt(strip) != '0') {
                    ListIterator<Special> it = this.fractionalSpecials.listIterator();
                }
            }
            ListIterator<Special> it2 = this.fractionalSpecials.listIterator();
            while (it2.hasNext()) {
                Special s = (Special) it2.next();
                char resultCh = result.charAt(digit);
                if (resultCh != '0' || s.f883ch == '0' || digit < strip) {
                    output.setCharAt(s.pos, resultCh);
                } else if (s.f883ch == '?') {
                    output.setCharAt(s.pos, ' ');
                }
                digit++;
            }
        }
    }

    public void simpleValue(StringBuffer toAppendTo, Object value) {
        SIMPLE_NUMBER.formatValue(toAppendTo, value);
    }
}

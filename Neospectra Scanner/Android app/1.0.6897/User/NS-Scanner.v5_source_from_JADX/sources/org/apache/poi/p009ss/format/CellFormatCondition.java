package org.apache.poi.p009ss.format;

import java.util.HashMap;
import java.util.Map;

/* renamed from: org.apache.poi.ss.format.CellFormatCondition */
public abstract class CellFormatCondition {

    /* renamed from: EQ */
    private static final int f877EQ = 4;

    /* renamed from: GE */
    private static final int f878GE = 3;

    /* renamed from: GT */
    private static final int f879GT = 2;

    /* renamed from: LE */
    private static final int f880LE = 1;

    /* renamed from: LT */
    private static final int f881LT = 0;

    /* renamed from: NE */
    private static final int f882NE = 5;
    private static final Map<String, Integer> TESTS = new HashMap();

    public abstract boolean pass(double d);

    static {
        TESTS.put("<", Integer.valueOf(0));
        TESTS.put("<=", Integer.valueOf(1));
        TESTS.put(">", Integer.valueOf(2));
        TESTS.put(">=", Integer.valueOf(3));
        TESTS.put("=", Integer.valueOf(4));
        TESTS.put("==", Integer.valueOf(4));
        TESTS.put("!=", Integer.valueOf(5));
        TESTS.put("<>", Integer.valueOf(5));
    }

    public static CellFormatCondition getInstance(String opString, String constStr) {
        if (!TESTS.containsKey(opString)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Unknown test: ");
            sb.append(opString);
            throw new IllegalArgumentException(sb.toString());
        }
        int test = ((Integer) TESTS.get(opString)).intValue();
        final double c = Double.parseDouble(constStr);
        switch (test) {
            case 0:
                return new CellFormatCondition() {
                    public boolean pass(double value) {
                        return value < c;
                    }
                };
            case 1:
                return new CellFormatCondition() {
                    public boolean pass(double value) {
                        return value <= c;
                    }
                };
            case 2:
                return new CellFormatCondition() {
                    public boolean pass(double value) {
                        return value > c;
                    }
                };
            case 3:
                return new CellFormatCondition() {
                    public boolean pass(double value) {
                        return value >= c;
                    }
                };
            case 4:
                return new CellFormatCondition() {
                    public boolean pass(double value) {
                        return value == c;
                    }
                };
            case 5:
                return new CellFormatCondition() {
                    public boolean pass(double value) {
                        return value != c;
                    }
                };
            default:
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Cannot create for test number ");
                sb2.append(test);
                sb2.append("(\"");
                sb2.append(opString);
                sb2.append("\")");
                throw new IllegalArgumentException(sb2.toString());
        }
    }
}

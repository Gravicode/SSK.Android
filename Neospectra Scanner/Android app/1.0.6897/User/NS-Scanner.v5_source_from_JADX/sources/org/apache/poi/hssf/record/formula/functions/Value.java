package org.apache.poi.hssf.record.formula.functions;

import org.apache.poi.hssf.record.formula.eval.ErrorEval;
import org.apache.poi.hssf.record.formula.eval.EvaluationException;
import org.apache.poi.hssf.record.formula.eval.NumberEval;
import org.apache.poi.hssf.record.formula.eval.OperandResolver;
import org.apache.poi.hssf.record.formula.eval.ValueEval;

public final class Value extends Fixed1ArgFunction {
    private static final int MIN_DISTANCE_BETWEEN_THOUSANDS_SEPARATOR = 4;
    private static final Double ZERO = new Double(0.0d);

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {
        try {
            Double result = convertTextToNumber(OperandResolver.coerceValueToString(OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex)));
            if (result == null) {
                return ErrorEval.VALUE_INVALID;
            }
            return new NumberEval(result.doubleValue());
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0037, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0049, code lost:
        if (r2 < r5) goto L_0x0056;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x004b, code lost:
        if (r3 != false) goto L_0x0055;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x004d, code lost:
        if (r4 != false) goto L_0x0055;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x004f, code lost:
        if (r7 == false) goto L_0x0052;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0054, code lost:
        return ZERO;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0055, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0056, code lost:
        r11 = false;
        r12 = -32768;
        r13 = new java.lang.StringBuffer(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0060, code lost:
        if (r2 >= r5) goto L_0x00c6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0062, code lost:
        r15 = r1.charAt(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x006a, code lost:
        if (java.lang.Character.isDigit(r15) == false) goto L_0x0070;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x006c, code lost:
        r13.append(r15);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0070, code lost:
        if (r15 == r8) goto L_0x00b2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0074, code lost:
        if (r15 == ',') goto L_0x00a5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0076, code lost:
        if (r15 == r9) goto L_0x0093;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x007a, code lost:
        if (r15 == 'E') goto L_0x0081;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x007e, code lost:
        if (r15 == 'e') goto L_0x0081;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0080, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0084, code lost:
        if ((r2 - r12) >= 4) goto L_0x0087;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x0086, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x0087, code lost:
        r13.append(r1.substring(r2));
        r2 = r5;
        r9 = '.';
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0093, code lost:
        if (r11 == false) goto L_0x0096;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x0095, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0099, code lost:
        if ((r2 - r12) >= 4) goto L_0x009c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x009b, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x009c, code lost:
        r9 = '.';
        r13.append('.');
        r11 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00a5, code lost:
        if (r11 == false) goto L_0x00a8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00a7, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00a8, code lost:
        r8 = r2 - r12;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x00ab, code lost:
        if (r8 >= 4) goto L_0x00ae;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x00ad, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x00ae, code lost:
        r12 = r2;
        r14 = r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x00be, code lost:
        if (r1.substring(r2).trim().length() <= 0) goto L_0x00c1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x00c0, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x00c1, code lost:
        r2 = r2 + 1;
        r8 = ' ';
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x00c6, code lost:
        if (r11 != false) goto L_0x00ce;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x00cb, code lost:
        if ((r2 - r12) >= 4) goto L_0x00ce;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x00cd, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:?, code lost:
        r8 = java.lang.Double.parseDouble(r13.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x00d9, code lost:
        if (r4 == false) goto L_0x00dd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x00db, code lost:
        r14 = -r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x00dd, code lost:
        r14 = r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x00e1, code lost:
        return new java.lang.Double(r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x00e2, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x00e3, code lost:
        r6 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x00e4, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.Double convertTextToNumber(java.lang.String r18) {
        /*
            r1 = r18
            r2 = 0
            r3 = 0
            r4 = 0
            int r5 = r18.length()
            r6 = 0
            r7 = r3
            r3 = r2
            r2 = 0
        L_0x000d:
            r8 = 32
            r9 = 46
            r10 = 0
            if (r2 >= r5) goto L_0x0049
            char r11 = r1.charAt(r2)
            boolean r12 = java.lang.Character.isDigit(r11)
            if (r12 != 0) goto L_0x0049
            if (r11 != r9) goto L_0x0021
            goto L_0x0049
        L_0x0021:
            if (r11 == r8) goto L_0x0045
            r8 = 36
            if (r11 == r8) goto L_0x0040
            r8 = 43
            if (r11 == r8) goto L_0x0038
            r8 = 45
            if (r11 == r8) goto L_0x0030
            return r10
        L_0x0030:
            if (r4 != 0) goto L_0x0037
            if (r7 == 0) goto L_0x0035
            goto L_0x0037
        L_0x0035:
            r4 = 1
            goto L_0x0046
        L_0x0037:
            return r10
        L_0x0038:
            if (r4 != 0) goto L_0x003f
            if (r7 == 0) goto L_0x003d
            goto L_0x003f
        L_0x003d:
            r7 = 1
            goto L_0x0046
        L_0x003f:
            return r10
        L_0x0040:
            if (r3 == 0) goto L_0x0043
            return r10
        L_0x0043:
            r3 = 1
            goto L_0x0046
        L_0x0045:
        L_0x0046:
            int r2 = r2 + 1
            goto L_0x000d
        L_0x0049:
            if (r2 < r5) goto L_0x0056
            if (r3 != 0) goto L_0x0055
            if (r4 != 0) goto L_0x0055
            if (r7 == 0) goto L_0x0052
            goto L_0x0055
        L_0x0052:
            java.lang.Double r6 = ZERO
            return r6
        L_0x0055:
            return r10
        L_0x0056:
            r11 = 0
            r12 = -32768(0xffffffffffff8000, float:NaN)
            java.lang.StringBuffer r13 = new java.lang.StringBuffer
            r13.<init>(r5)
            r6 = r10
            r14 = 0
        L_0x0060:
            if (r2 >= r5) goto L_0x00c6
            char r15 = r1.charAt(r2)
            boolean r16 = java.lang.Character.isDigit(r15)
            if (r16 == 0) goto L_0x0070
            r13.append(r15)
            goto L_0x00c1
        L_0x0070:
            if (r15 == r8) goto L_0x00b2
            r8 = 44
            if (r15 == r8) goto L_0x00a5
            if (r15 == r9) goto L_0x0093
            r8 = 69
            if (r15 == r8) goto L_0x0081
            r8 = 101(0x65, float:1.42E-43)
            if (r15 == r8) goto L_0x0081
            return r10
        L_0x0081:
            int r8 = r2 - r12
            r9 = 4
            if (r8 >= r9) goto L_0x0087
            return r10
        L_0x0087:
            java.lang.String r8 = r1.substring(r2)
            r13.append(r8)
            r2 = r5
            r9 = 46
            goto L_0x00c1
        L_0x0093:
            if (r11 == 0) goto L_0x0096
            return r10
        L_0x0096:
            int r8 = r2 - r12
            r9 = 4
            if (r8 >= r9) goto L_0x009c
            return r10
        L_0x009c:
            r8 = 1
            r9 = 46
            r13.append(r9)
            r11 = r8
            goto L_0x00c1
        L_0x00a5:
            if (r11 == 0) goto L_0x00a8
            return r10
        L_0x00a8:
            int r8 = r2 - r12
            r14 = 4
            if (r8 >= r14) goto L_0x00ae
            return r10
        L_0x00ae:
            r12 = r2
            r14 = r8
            goto L_0x00c1
        L_0x00b2:
            java.lang.String r6 = r1.substring(r2)
            java.lang.String r8 = r6.trim()
            int r8 = r8.length()
            if (r8 <= 0) goto L_0x00c1
            return r10
        L_0x00c1:
            int r2 = r2 + 1
            r8 = 32
            goto L_0x0060
        L_0x00c6:
            if (r11 != 0) goto L_0x00ce
            int r6 = r2 - r12
            r8 = 4
            if (r6 >= r8) goto L_0x00ce
            return r10
        L_0x00ce:
            java.lang.String r6 = r13.toString()     // Catch:{ NumberFormatException -> 0x00e2 }
            double r8 = java.lang.Double.parseDouble(r6)     // Catch:{ NumberFormatException -> 0x00e2 }
            java.lang.Double r6 = new java.lang.Double
            if (r4 == 0) goto L_0x00dd
            double r14 = -r8
            goto L_0x00de
        L_0x00dd:
            r14 = r8
        L_0x00de:
            r6.<init>(r14)
            return r6
        L_0x00e2:
            r0 = move-exception
            r6 = r0
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.hssf.record.formula.functions.Value.convertTextToNumber(java.lang.String):java.lang.Double");
    }
}

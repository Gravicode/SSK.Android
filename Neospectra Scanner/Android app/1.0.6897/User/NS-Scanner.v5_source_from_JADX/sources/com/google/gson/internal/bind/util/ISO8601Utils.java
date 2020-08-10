package com.google.gson.internal.bind.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class ISO8601Utils {
    private static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone(UTC_ID);
    private static final String UTC_ID = "UTC";

    public static String format(Date date) {
        return format(date, false, TIMEZONE_UTC);
    }

    public static String format(Date date, boolean millis) {
        return format(date, millis, TIMEZONE_UTC);
    }

    public static String format(Date date, boolean millis, TimeZone tz) {
        Calendar calendar = new GregorianCalendar(tz, Locale.US);
        calendar.setTime(date);
        StringBuilder formatted = new StringBuilder("yyyy-MM-ddThh:mm:ss".length() + (millis ? ".sss".length() : 0) + (tz.getRawOffset() == 0 ? "Z" : "+hh:mm").length());
        padInt(formatted, calendar.get(1), "yyyy".length());
        char c = '-';
        formatted.append('-');
        padInt(formatted, calendar.get(2) + 1, "MM".length());
        formatted.append('-');
        padInt(formatted, calendar.get(5), "dd".length());
        formatted.append('T');
        padInt(formatted, calendar.get(11), "hh".length());
        formatted.append(':');
        padInt(formatted, calendar.get(12), "mm".length());
        formatted.append(':');
        padInt(formatted, calendar.get(13), "ss".length());
        if (millis) {
            formatted.append('.');
            padInt(formatted, calendar.get(14), "sss".length());
        }
        int offset = tz.getOffset(calendar.getTimeInMillis());
        if (offset != 0) {
            int hours = Math.abs((offset / 60000) / 60);
            int minutes = Math.abs((offset / 60000) % 60);
            if (offset >= 0) {
                c = '+';
            }
            formatted.append(c);
            padInt(formatted, hours, "hh".length());
            formatted.append(':');
            padInt(formatted, minutes, "mm".length());
        } else {
            formatted.append('Z');
        }
        return formatted.toString();
    }

    /* JADX WARNING: Removed duplicated region for block: B:112:0x0225  */
    /* JADX WARNING: Removed duplicated region for block: B:113:0x0228  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Date parse(java.lang.String r28, java.text.ParsePosition r29) throws java.text.ParseException {
        /*
            r1 = r28
            r2 = r29
            r3 = 0
            r4 = r3
            int r5 = r29.getIndex()     // Catch:{ IndexOutOfBoundsException -> 0x021d, NumberFormatException -> 0x0217, IllegalArgumentException -> 0x0211 }
            int r6 = r5 + 4
            int r5 = parseInt(r1, r5, r6)     // Catch:{ IndexOutOfBoundsException -> 0x021d, NumberFormatException -> 0x0217, IllegalArgumentException -> 0x0211 }
            r7 = 45
            boolean r8 = checkOffset(r1, r6, r7)     // Catch:{ IndexOutOfBoundsException -> 0x021d, NumberFormatException -> 0x0217, IllegalArgumentException -> 0x0211 }
            if (r8 == 0) goto L_0x001a
            int r6 = r6 + 1
        L_0x001a:
            int r8 = r6 + 2
            int r6 = parseInt(r1, r6, r8)     // Catch:{ IndexOutOfBoundsException -> 0x021d, NumberFormatException -> 0x0217, IllegalArgumentException -> 0x0211 }
            boolean r9 = checkOffset(r1, r8, r7)     // Catch:{ IndexOutOfBoundsException -> 0x021d, NumberFormatException -> 0x0217, IllegalArgumentException -> 0x0211 }
            if (r9 == 0) goto L_0x0028
            int r8 = r8 + 1
        L_0x0028:
            int r9 = r8 + 2
            int r8 = parseInt(r1, r8, r9)     // Catch:{ IndexOutOfBoundsException -> 0x021d, NumberFormatException -> 0x0217, IllegalArgumentException -> 0x0211 }
            r10 = 0
            r11 = 0
            r12 = 0
            r13 = 0
            r14 = 84
            boolean r14 = checkOffset(r1, r9, r14)     // Catch:{ IndexOutOfBoundsException -> 0x021d, NumberFormatException -> 0x0217, IllegalArgumentException -> 0x0211 }
            if (r14 != 0) goto L_0x0061
            int r15 = r28.length()     // Catch:{ IndexOutOfBoundsException -> 0x005b, NumberFormatException -> 0x0055, IllegalArgumentException -> 0x004f }
            if (r15 > r9) goto L_0x0061
            java.util.GregorianCalendar r7 = new java.util.GregorianCalendar     // Catch:{ IndexOutOfBoundsException -> 0x005b, NumberFormatException -> 0x0055, IllegalArgumentException -> 0x004f }
            int r15 = r6 + -1
            r7.<init>(r5, r15, r8)     // Catch:{ IndexOutOfBoundsException -> 0x005b, NumberFormatException -> 0x0055, IllegalArgumentException -> 0x004f }
            r2.setIndex(r9)     // Catch:{ IndexOutOfBoundsException -> 0x005b, NumberFormatException -> 0x0055, IllegalArgumentException -> 0x004f }
            java.util.Date r15 = r7.getTime()     // Catch:{ IndexOutOfBoundsException -> 0x005b, NumberFormatException -> 0x0055, IllegalArgumentException -> 0x004f }
            return r15
        L_0x004f:
            r0 = move-exception
            r3 = r0
            r21 = r4
            goto L_0x0215
        L_0x0055:
            r0 = move-exception
            r3 = r0
            r21 = r4
            goto L_0x021b
        L_0x005b:
            r0 = move-exception
            r3 = r0
            r21 = r4
            goto L_0x0221
        L_0x0061:
            r3 = 90
            if (r14 == 0) goto L_0x00d8
            int r9 = r9 + 1
            int r7 = r9 + 2
            int r9 = parseInt(r1, r9, r7)     // Catch:{ IndexOutOfBoundsException -> 0x005b, NumberFormatException -> 0x0055, IllegalArgumentException -> 0x004f }
            r10 = r9
            r9 = 58
            boolean r18 = checkOffset(r1, r7, r9)     // Catch:{ IndexOutOfBoundsException -> 0x005b, NumberFormatException -> 0x0055, IllegalArgumentException -> 0x004f }
            if (r18 == 0) goto L_0x0078
            int r7 = r7 + 1
        L_0x0078:
            int r15 = r7 + 2
            int r7 = parseInt(r1, r7, r15)     // Catch:{ IndexOutOfBoundsException -> 0x005b, NumberFormatException -> 0x0055, IllegalArgumentException -> 0x004f }
            r11 = r7
            boolean r7 = checkOffset(r1, r15, r9)     // Catch:{ IndexOutOfBoundsException -> 0x005b, NumberFormatException -> 0x0055, IllegalArgumentException -> 0x004f }
            if (r7 == 0) goto L_0x0087
            int r15 = r15 + 1
        L_0x0087:
            r9 = r15
            int r7 = r28.length()     // Catch:{ IndexOutOfBoundsException -> 0x005b, NumberFormatException -> 0x0055, IllegalArgumentException -> 0x004f }
            if (r7 <= r9) goto L_0x00d8
            char r7 = r1.charAt(r9)     // Catch:{ IndexOutOfBoundsException -> 0x005b, NumberFormatException -> 0x0055, IllegalArgumentException -> 0x004f }
            if (r7 == r3) goto L_0x00d8
            r15 = 43
            if (r7 == r15) goto L_0x00d8
            r15 = 45
            if (r7 == r15) goto L_0x00d8
            int r15 = r9 + 2
            int r9 = parseInt(r1, r9, r15)     // Catch:{ IndexOutOfBoundsException -> 0x005b, NumberFormatException -> 0x0055, IllegalArgumentException -> 0x004f }
            r12 = 59
            if (r9 <= r12) goto L_0x00ac
            r12 = 63
            if (r9 >= r12) goto L_0x00ac
            r9 = 59
        L_0x00ac:
            r12 = r9
            r9 = 46
            boolean r9 = checkOffset(r1, r15, r9)     // Catch:{ IndexOutOfBoundsException -> 0x005b, NumberFormatException -> 0x0055, IllegalArgumentException -> 0x004f }
            if (r9 == 0) goto L_0x00d7
            int r15 = r15 + 1
            int r9 = r15 + 1
            int r9 = indexOfNonDigit(r1, r9)     // Catch:{ IndexOutOfBoundsException -> 0x005b, NumberFormatException -> 0x0055, IllegalArgumentException -> 0x004f }
            int r3 = r15 + 3
            int r3 = java.lang.Math.min(r9, r3)     // Catch:{ IndexOutOfBoundsException -> 0x005b, NumberFormatException -> 0x0055, IllegalArgumentException -> 0x004f }
            int r18 = parseInt(r1, r15, r3)     // Catch:{ IndexOutOfBoundsException -> 0x005b, NumberFormatException -> 0x0055, IllegalArgumentException -> 0x004f }
            int r19 = r3 - r15
            switch(r19) {
                case 1: goto L_0x00d2;
                case 2: goto L_0x00cf;
                default: goto L_0x00cc;
            }
        L_0x00cc:
            r13 = r18
            goto L_0x00d5
        L_0x00cf:
            int r13 = r18 * 10
            goto L_0x00d5
        L_0x00d2:
            int r13 = r18 * 100
        L_0x00d5:
            goto L_0x00d8
        L_0x00d7:
            r9 = r15
        L_0x00d8:
            int r3 = r28.length()     // Catch:{ IndexOutOfBoundsException -> 0x021d, NumberFormatException -> 0x0217, IllegalArgumentException -> 0x0211 }
            if (r3 > r9) goto L_0x00e6
            java.lang.IllegalArgumentException r3 = new java.lang.IllegalArgumentException     // Catch:{ IndexOutOfBoundsException -> 0x005b, NumberFormatException -> 0x0055, IllegalArgumentException -> 0x004f }
            java.lang.String r7 = "No time zone indicator"
            r3.<init>(r7)     // Catch:{ IndexOutOfBoundsException -> 0x005b, NumberFormatException -> 0x0055, IllegalArgumentException -> 0x004f }
            throw r3     // Catch:{ IndexOutOfBoundsException -> 0x005b, NumberFormatException -> 0x0055, IllegalArgumentException -> 0x004f }
        L_0x00e6:
            r3 = 0
            char r7 = r1.charAt(r9)     // Catch:{ IndexOutOfBoundsException -> 0x021d, NumberFormatException -> 0x0217, IllegalArgumentException -> 0x0211 }
            r15 = 90
            if (r7 != r15) goto L_0x00fa
            java.util.TimeZone r15 = TIMEZONE_UTC     // Catch:{ IndexOutOfBoundsException -> 0x005b, NumberFormatException -> 0x0055, IllegalArgumentException -> 0x004f }
            r3 = r15
            r15 = 1
            int r9 = r9 + r15
            r21 = r4
            r24 = r7
            goto L_0x01d5
        L_0x00fa:
            r15 = 43
            if (r7 == r15) goto L_0x0127
            r15 = 45
            if (r7 != r15) goto L_0x0107
            r20 = r3
            r21 = r4
            goto L_0x012b
        L_0x0107:
            java.lang.IndexOutOfBoundsException r15 = new java.lang.IndexOutOfBoundsException     // Catch:{ IndexOutOfBoundsException -> 0x021d, NumberFormatException -> 0x0217, IllegalArgumentException -> 0x0211 }
            r20 = r3
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x021d, NumberFormatException -> 0x0217, IllegalArgumentException -> 0x0211 }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x021d, NumberFormatException -> 0x0217, IllegalArgumentException -> 0x0211 }
            r21 = r4
            java.lang.String r4 = "Invalid time zone indicator '"
            r3.append(r4)     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            r3.append(r7)     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            java.lang.String r4 = "'"
            r3.append(r4)     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            r15.<init>(r3)     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            throw r15     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
        L_0x0127:
            r20 = r3
            r21 = r4
        L_0x012b:
            java.lang.String r3 = r1.substring(r9)     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            int r4 = r3.length()     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            r15 = 5
            if (r4 < r15) goto L_0x0138
            r4 = r3
            goto L_0x0149
        L_0x0138:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            r4.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            r4.append(r3)     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            java.lang.String r15 = "00"
            r4.append(r15)     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            java.lang.String r4 = r4.toString()     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
        L_0x0149:
            r3 = r4
            int r4 = r3.length()     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            int r9 = r9 + r4
            java.lang.String r4 = "+0000"
            boolean r4 = r4.equals(r3)     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            if (r4 != 0) goto L_0x01ca
            java.lang.String r4 = "+00:00"
            boolean r4 = r4.equals(r3)     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            if (r4 == 0) goto L_0x0166
            r23 = r3
            r24 = r7
            r25 = r9
            goto L_0x01d0
        L_0x0166:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            r4.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            java.lang.String r15 = "GMT"
            r4.append(r15)     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            r4.append(r3)     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            java.lang.String r4 = r4.toString()     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            java.util.TimeZone r15 = java.util.TimeZone.getTimeZone(r4)     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            java.lang.String r17 = r15.getID()     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            r22 = r17
            r23 = r3
            r3 = r22
            boolean r17 = r3.equals(r4)     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            if (r17 != 0) goto L_0x01c4
            r24 = r7
            java.lang.String r7 = ":"
            r25 = r9
            java.lang.String r9 = ""
            java.lang.String r7 = r3.replace(r7, r9)     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            boolean r9 = r7.equals(r4)     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            if (r9 != 0) goto L_0x01c8
            java.lang.IndexOutOfBoundsException r9 = new java.lang.IndexOutOfBoundsException     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            r26 = r3
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            r27 = r7
            java.lang.String r7 = "Mismatching time zone indicator: "
            r3.append(r7)     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            r3.append(r4)     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            java.lang.String r7 = " given, resolves to "
            r3.append(r7)     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            java.lang.String r7 = r15.getID()     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            r3.append(r7)     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            r9.<init>(r3)     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            throw r9     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
        L_0x01c4:
            r24 = r7
            r25 = r9
        L_0x01c8:
            r3 = r15
            goto L_0x01d2
        L_0x01ca:
            r23 = r3
            r24 = r7
            r25 = r9
        L_0x01d0:
            java.util.TimeZone r3 = TIMEZONE_UTC     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
        L_0x01d2:
            r9 = r25
        L_0x01d5:
            java.util.GregorianCalendar r4 = new java.util.GregorianCalendar     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            r4.<init>(r3)     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            r7 = 0
            r4.setLenient(r7)     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            r7 = 1
            r4.set(r7, r5)     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            int r7 = r6 + -1
            r15 = 2
            r4.set(r15, r7)     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            r7 = 5
            r4.set(r7, r8)     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            r7 = 11
            r4.set(r7, r10)     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            r7 = 12
            r4.set(r7, r11)     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            r7 = 13
            r4.set(r7, r12)     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            r7 = 14
            r4.set(r7, r13)     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            r2.setIndex(r9)     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            java.util.Date r7 = r4.getTime()     // Catch:{ IndexOutOfBoundsException -> 0x020e, NumberFormatException -> 0x020b, IllegalArgumentException -> 0x0208 }
            return r7
        L_0x0208:
            r0 = move-exception
            r3 = r0
            goto L_0x0215
        L_0x020b:
            r0 = move-exception
            r3 = r0
            goto L_0x021b
        L_0x020e:
            r0 = move-exception
            r3 = r0
            goto L_0x0221
        L_0x0211:
            r0 = move-exception
            r21 = r4
            r3 = r0
        L_0x0215:
            goto L_0x0223
        L_0x0217:
            r0 = move-exception
            r21 = r4
            r3 = r0
        L_0x021b:
            goto L_0x0222
        L_0x021d:
            r0 = move-exception
            r21 = r4
            r3 = r0
        L_0x0221:
        L_0x0222:
        L_0x0223:
            if (r1 != 0) goto L_0x0228
            r16 = 0
            goto L_0x0240
        L_0x0228:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r5 = 34
            r4.append(r5)
            r4.append(r1)
            java.lang.String r5 = "'"
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            r16 = r4
        L_0x0240:
            r4 = r16
            java.lang.String r5 = r3.getMessage()
            if (r5 == 0) goto L_0x024e
            boolean r6 = r5.isEmpty()
            if (r6 == 0) goto L_0x026c
        L_0x024e:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "("
            r6.append(r7)
            java.lang.Class r7 = r3.getClass()
            java.lang.String r7 = r7.getName()
            r6.append(r7)
            java.lang.String r7 = ")"
            r6.append(r7)
            java.lang.String r5 = r6.toString()
        L_0x026c:
            java.text.ParseException r6 = new java.text.ParseException
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "Failed to parse date ["
            r7.append(r8)
            r7.append(r4)
            java.lang.String r8 = "]: "
            r7.append(r8)
            r7.append(r5)
            java.lang.String r7 = r7.toString()
            int r8 = r29.getIndex()
            r6.<init>(r7, r8)
            r6.initCause(r3)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.gson.internal.bind.util.ISO8601Utils.parse(java.lang.String, java.text.ParsePosition):java.util.Date");
    }

    private static boolean checkOffset(String value, int offset, char expected) {
        return offset < value.length() && value.charAt(offset) == expected;
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x003c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int parseInt(java.lang.String r6, int r7, int r8) throws java.lang.NumberFormatException {
        /*
            if (r7 < 0) goto L_0x0069
            int r0 = r6.length()
            if (r8 > r0) goto L_0x0069
            if (r7 <= r8) goto L_0x000b
            goto L_0x0069
        L_0x000b:
            r0 = r7
            r1 = 0
            r2 = 10
            if (r0 >= r8) goto L_0x003a
            int r3 = r0 + 1
            char r0 = r6.charAt(r0)
            int r0 = java.lang.Character.digit(r0, r2)
            if (r0 >= 0) goto L_0x0038
            java.lang.NumberFormatException r2 = new java.lang.NumberFormatException
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "Invalid number: "
            r4.append(r5)
            java.lang.String r5 = r6.substring(r7, r8)
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            r2.<init>(r4)
            throw r2
        L_0x0038:
            int r1 = -r0
        L_0x0039:
            r0 = r3
        L_0x003a:
            if (r0 >= r8) goto L_0x0067
            int r3 = r0 + 1
            char r0 = r6.charAt(r0)
            int r0 = java.lang.Character.digit(r0, r2)
            if (r0 >= 0) goto L_0x0063
            java.lang.NumberFormatException r2 = new java.lang.NumberFormatException
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "Invalid number: "
            r4.append(r5)
            java.lang.String r5 = r6.substring(r7, r8)
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            r2.<init>(r4)
            throw r2
        L_0x0063:
            int r1 = r1 * 10
            int r1 = r1 - r0
            goto L_0x0039
        L_0x0067:
            int r2 = -r1
            return r2
        L_0x0069:
            java.lang.NumberFormatException r0 = new java.lang.NumberFormatException
            r0.<init>(r6)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.gson.internal.bind.util.ISO8601Utils.parseInt(java.lang.String, int, int):int");
    }

    private static void padInt(StringBuilder buffer, int value, int length) {
        String strValue = Integer.toString(value);
        for (int i = length - strValue.length(); i > 0; i--) {
            buffer.append('0');
        }
        buffer.append(strValue);
    }

    private static int indexOfNonDigit(String string, int offset) {
        for (int i = offset; i < string.length(); i++) {
            char c = string.charAt(i);
            if (c < '0' || c > '9') {
                return i;
            }
        }
        return string.length();
    }
}

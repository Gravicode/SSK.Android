package org.apache.poi.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class HexRead {
    public static byte[] readData(String filename) throws IOException {
        FileInputStream stream = new FileInputStream(new File(filename));
        try {
            return readData((InputStream) stream, -1);
        } finally {
            stream.close();
        }
    }

    public static byte[] readData(InputStream stream, String section) throws IOException {
        try {
            StringBuffer sectionText = new StringBuffer();
            boolean inSection = false;
            int c = stream.read();
            while (c != -1) {
                if (c == 10 || c == 13) {
                    inSection = false;
                    sectionText = new StringBuffer();
                } else if (c == 91) {
                    inSection = true;
                } else if (c == 93) {
                    inSection = false;
                    if (sectionText.toString().equals(section)) {
                        return readData(stream, 91);
                    }
                    sectionText = new StringBuffer();
                } else if (inSection) {
                    sectionText.append((char) c);
                }
                c = stream.read();
            }
            stream.close();
            StringBuilder sb = new StringBuilder();
            sb.append("Section '");
            sb.append(section);
            sb.append("' not found");
            throw new IOException(sb.toString());
        } finally {
            stream.close();
        }
    }

    public static byte[] readData(String filename, String section) throws IOException {
        return readData((InputStream) new FileInputStream(new File(filename)), section);
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x006c A[LOOP:1: B:22:0x0069->B:24:0x006c, LOOP_END] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] readData(java.io.InputStream r9, int r10) throws java.io.IOException {
        /*
            r0 = 0
            r1 = 0
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>()
            r3 = 0
            r4 = r0
            r0 = 0
        L_0x000a:
            if (r0 != 0) goto L_0x005d
            int r5 = r9.read()
            r6 = 97
            if (r5 != r10) goto L_0x0015
            goto L_0x005d
        L_0x0015:
            r7 = -1
            if (r5 == r7) goto L_0x005a
            r7 = 35
            if (r5 == r7) goto L_0x0056
            r7 = 2
            switch(r5) {
                case 48: goto L_0x0040;
                case 49: goto L_0x0040;
                case 50: goto L_0x0040;
                case 51: goto L_0x0040;
                case 52: goto L_0x0040;
                case 53: goto L_0x0040;
                case 54: goto L_0x0040;
                case 55: goto L_0x0040;
                case 56: goto L_0x0040;
                case 57: goto L_0x0040;
                default: goto L_0x0020;
            }
        L_0x0020:
            switch(r5) {
                case 65: goto L_0x0027;
                case 66: goto L_0x0027;
                case 67: goto L_0x0027;
                case 68: goto L_0x0027;
                case 69: goto L_0x0027;
                case 70: goto L_0x0027;
                default: goto L_0x0023;
            }
        L_0x0023:
            switch(r5) {
                case 97: goto L_0x0029;
                case 98: goto L_0x0029;
                case 99: goto L_0x0029;
                case 100: goto L_0x0029;
                case 101: goto L_0x0029;
                case 102: goto L_0x0029;
                default: goto L_0x0026;
            }
        L_0x0026:
            goto L_0x005c
        L_0x0027:
            r6 = 65
        L_0x0029:
            int r8 = r1 << 4
            byte r1 = (byte) r8
            int r8 = r5 + 10
            int r8 = r8 - r6
            byte r8 = (byte) r8
            int r8 = r8 + r1
            byte r1 = (byte) r8
            int r4 = r4 + 1
            if (r4 != r7) goto L_0x005c
            java.lang.Byte r7 = java.lang.Byte.valueOf(r1)
            r2.add(r7)
            r4 = 0
            r1 = 0
            goto L_0x005c
        L_0x0040:
            int r8 = r1 << 4
            byte r1 = (byte) r8
            int r8 = r5 + -48
            byte r8 = (byte) r8
            int r8 = r8 + r1
            byte r1 = (byte) r8
            int r4 = r4 + 1
            if (r4 != r7) goto L_0x005c
            java.lang.Byte r7 = java.lang.Byte.valueOf(r1)
            r2.add(r7)
            r4 = 0
            r1 = 0
            goto L_0x005c
        L_0x0056:
            readToEOL(r9)
            goto L_0x005c
        L_0x005a:
            r0 = 1
        L_0x005c:
            goto L_0x000a
        L_0x005d:
            java.lang.Byte[] r5 = new java.lang.Byte[r3]
            java.lang.Object[] r5 = r2.toArray(r5)
            java.lang.Byte[] r5 = (java.lang.Byte[]) r5
            int r6 = r5.length
            byte[] r6 = new byte[r6]
        L_0x0069:
            int r7 = r5.length
            if (r3 >= r7) goto L_0x0077
            r7 = r5[r3]
            byte r7 = r7.byteValue()
            r6[r3] = r7
            int r3 = r3 + 1
            goto L_0x0069
        L_0x0077:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.util.HexRead.readData(java.io.InputStream, int):byte[]");
    }

    public static byte[] readFromString(String data) {
        try {
            return readData((InputStream) new ByteArrayInputStream(data.getBytes()), -1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void readToEOL(InputStream stream) throws IOException {
        int c = stream.read();
        while (c != -1 && c != 10 && c != 13) {
            c = stream.read();
        }
    }
}

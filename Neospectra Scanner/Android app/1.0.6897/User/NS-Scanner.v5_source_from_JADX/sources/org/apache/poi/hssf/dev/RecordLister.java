package org.apache.poi.hssf.dev;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordFactory;
import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class RecordLister {
    String file;

    public void run() throws IOException {
        RecordInputStream rinp = new RecordInputStream(new POIFSFileSystem(new FileInputStream(this.file)).createDocumentInputStream("Workbook"));
        while (rinp.hasNextRecord()) {
            int sid = rinp.getNextSid();
            rinp.nextRecord();
            int size = rinp.available();
            Class<? extends Record> clz = RecordFactory.getRecordClass(sid);
            PrintStream printStream = System.out;
            StringBuilder sb = new StringBuilder();
            sb.append(formatSID(sid));
            sb.append(" - ");
            sb.append(formatSize(size));
            sb.append(" bytes");
            printStream.print(sb.toString());
            if (clz != null) {
                System.out.print("  \t");
                System.out.print(clz.getName().replace("org.apache.poi.hssf.record.", ""));
            }
            System.out.println();
            byte[] data = rinp.readRemainder();
            if (data.length > 0) {
                System.out.print("   ");
                System.out.println(formatData(data));
            }
        }
    }

    private static String formatSID(int sid) {
        String hex = Integer.toHexString(sid);
        String dec = Integer.toString(sid);
        StringBuffer s = new StringBuffer();
        s.append("0x");
        for (int i = hex.length(); i < 4; i++) {
            s.append('0');
        }
        s.append(hex);
        s.append(" (");
        for (int i2 = dec.length(); i2 < 4; i2++) {
            s.append('0');
        }
        s.append(dec);
        s.append(")");
        return s.toString();
    }

    private static String formatSize(int size) {
        String hex = Integer.toHexString(size);
        String dec = Integer.toString(size);
        StringBuffer s = new StringBuffer();
        for (int i = hex.length(); i < 3; i++) {
            s.append('0');
        }
        s.append(hex);
        s.append(" (");
        for (int i2 = dec.length(); i2 < 3; i2++) {
            s.append('0');
        }
        s.append(dec);
        s.append(")");
        return s.toString();
    }

    private static String formatData(byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }
        StringBuffer s = new StringBuffer();
        int i = 0;
        if (data.length <= 9) {
            while (true) {
                int i2 = i;
                if (i2 >= data.length) {
                    break;
                }
                s.append(byteToHex(data[i2]));
                s.append(' ');
                i = i2 + 1;
            }
        } else {
            s.append(byteToHex(data[0]));
            s.append(' ');
            s.append(byteToHex(data[1]));
            s.append(' ');
            s.append(byteToHex(data[2]));
            s.append(' ');
            s.append(byteToHex(data[3]));
            s.append(' ');
            s.append(" .... ");
            s.append(' ');
            s.append(byteToHex(data[data.length - 4]));
            s.append(' ');
            s.append(byteToHex(data[data.length - 3]));
            s.append(' ');
            s.append(byteToHex(data[data.length - 2]));
            s.append(' ');
            s.append(byteToHex(data[data.length - 1]));
        }
        return s.toString();
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r4v0, types: [int, byte] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String byteToHex(int r4) {
        /*
            r0 = r4
            if (r0 >= 0) goto L_0x0005
            int r0 = r0 + 256
        L_0x0005:
            java.lang.String r1 = java.lang.Integer.toHexString(r0)
            r2 = 16
            if (r0 >= r2) goto L_0x001f
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "0"
            r2.append(r3)
            r2.append(r1)
            java.lang.String r2 = r2.toString()
            return r2
        L_0x001f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.hssf.dev.RecordLister.byteToHex(byte):java.lang.String");
    }

    public void setFile(String file2) {
        this.file = file2;
    }

    public static void main(String[] args) {
        if (args.length != 1 || args[0].equals("--help")) {
            System.out.println("RecordLister");
            System.out.println("Outputs the summary of the records in file order");
            System.out.println("usage: java org.apache.poi.hssf.dev.RecordLister filename");
            return;
        }
        try {
            RecordLister viewer = new RecordLister();
            viewer.setFile(args[0]);
            viewer.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

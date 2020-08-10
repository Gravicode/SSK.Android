package org.apache.poi.ddf;

import android.support.p001v4.app.FrameMetricsAggregator;
import android.support.p001v4.view.InputDeviceCompat;
import android.support.p004v7.widget.helper.ItemTouchHelper.Callback;
import com.polidea.rxandroidble2.RxBleConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import org.apache.poi.p009ss.usermodel.ShapeTypes;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.HexRead;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.LittleEndian.BufferUnderrunException;

public final class EscherDump {
    public void dump(byte[] data, int offset, int size, PrintStream out) {
        EscherRecordFactory recordFactory = new DefaultEscherRecordFactory();
        int pos = offset;
        while (pos < offset + size) {
            EscherRecord r = recordFactory.createRecord(data, pos);
            int bytesRead = r.fillFields(data, pos, recordFactory);
            out.println(r.toString());
            pos += bytesRead;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:102:0x0484  */
    /* JADX WARNING: Removed duplicated region for block: B:105:0x04bf  */
    /* JADX WARNING: Removed duplicated region for block: B:112:0x04d7  */
    /* JADX WARNING: Removed duplicated region for block: B:113:0x04da  */
    /* JADX WARNING: Removed duplicated region for block: B:125:0x04fc  */
    /* JADX WARNING: Removed duplicated region for block: B:131:0x050c  */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x0437  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void dumpOld(long r23, java.io.InputStream r25, java.io.PrintStream r26) throws java.io.IOException, org.apache.poi.util.LittleEndian.BufferUnderrunException {
        /*
            r22 = this;
            r0 = r22
            r1 = r25
            r2 = r26
            r3 = r23
            java.lang.StringBuffer r5 = new java.lang.StringBuffer
            r5.<init>()
            r6 = 0
            r7 = 0
            r8 = r7
            r7 = r5
            r4 = r3
            r3 = 0
        L_0x0013:
            if (r3 != 0) goto L_0x0518
            r9 = 0
            int r11 = (r4 > r9 ? 1 : (r4 == r9 ? 0 : -1))
            if (r11 <= 0) goto L_0x0518
            java.lang.StringBuffer r11 = new java.lang.StringBuffer
            r11.<init>()
            r7 = r11
            short r11 = org.apache.poi.util.LittleEndian.readShort(r25)
            short r12 = org.apache.poi.util.LittleEndian.readShort(r25)
            int r13 = org.apache.poi.util.LittleEndian.readInt(r25)
            r14 = 8
            long r4 = r4 - r14
            r14 = -3817(0xfffffffffffff117, float:NaN)
            r15 = -4072(0xfffffffffffff018, float:NaN)
            r9 = -3806(0xfffffffffffff122, float:NaN)
            r10 = 15
            if (r12 == r9) goto L_0x00ba
            switch(r12) {
                case -4096: goto L_0x00b7;
                case -4095: goto L_0x00b4;
                case -4094: goto L_0x00b1;
                case -4093: goto L_0x00ae;
                case -4092: goto L_0x00ab;
                case -4091: goto L_0x00a8;
                case -4090: goto L_0x00a5;
                case -4089: goto L_0x00a2;
                case -4088: goto L_0x009f;
                case -4087: goto L_0x009c;
                case -4086: goto L_0x0099;
                case -4085: goto L_0x0096;
                case -4084: goto L_0x0093;
                case -4083: goto L_0x0090;
                case -4082: goto L_0x008d;
                case -4081: goto L_0x008a;
                case -4080: goto L_0x0087;
                case -4079: goto L_0x0084;
                case -4078: goto L_0x0081;
                case -4077: goto L_0x007e;
                case -4076: goto L_0x007b;
                case -4075: goto L_0x0078;
                case -4074: goto L_0x0075;
                case -4073: goto L_0x0072;
                default: goto L_0x003d;
            }
        L_0x003d:
            switch(r12) {
                case -3816: goto L_0x006f;
                case -3815: goto L_0x006b;
                case -3814: goto L_0x0067;
                default: goto L_0x0040;
            }
        L_0x0040:
            switch(r12) {
                case -3811: goto L_0x0063;
                case -3810: goto L_0x005f;
                case -3809: goto L_0x005b;
                case -3808: goto L_0x0057;
                default: goto L_0x0043;
            }
        L_0x0043:
            if (r12 < r15) goto L_0x004b
            if (r12 > r14) goto L_0x004b
            java.lang.String r8 = "MsofbtBLIP"
            goto L_0x00bd
        L_0x004b:
            r8 = r11 & 15
            if (r8 != r10) goto L_0x0053
            java.lang.String r8 = "UNKNOWN container"
            goto L_0x00bd
        L_0x0053:
            java.lang.String r8 = "UNKNOWN ID"
            goto L_0x00bd
        L_0x0057:
            java.lang.String r8 = "MsofbtColorScheme"
            goto L_0x00bd
        L_0x005b:
            java.lang.String r8 = "MsofbtOleObject"
            goto L_0x00bd
        L_0x005f:
            java.lang.String r8 = "MsofbtSplitMenuColors"
            goto L_0x00bd
        L_0x0063:
            java.lang.String r8 = "MsofbtDeletedPspl"
            goto L_0x00bd
        L_0x0067:
            java.lang.String r8 = "MsofbtColorMRU"
            goto L_0x00bd
        L_0x006b:
            java.lang.String r8 = "MsofbtSelection"
            goto L_0x00bd
        L_0x006f:
            java.lang.String r8 = "MsofbtRegroupItem"
            goto L_0x00bd
        L_0x0072:
            java.lang.String r8 = "MsofbtCalloutRule"
            goto L_0x00bd
        L_0x0075:
            java.lang.String r8 = "MsofbtCLSID"
            goto L_0x00bd
        L_0x0078:
            java.lang.String r8 = "MsofbtClientRule"
            goto L_0x00bd
        L_0x007b:
            java.lang.String r8 = "MsofbtArcRule"
            goto L_0x00bd
        L_0x007e:
            java.lang.String r8 = "MsofbtAlignRule"
            goto L_0x00bd
        L_0x0081:
            java.lang.String r8 = "MsofbtConnectorRule"
            goto L_0x00bd
        L_0x0084:
            java.lang.String r8 = "MsofbtClientData"
            goto L_0x00bd
        L_0x0087:
            java.lang.String r8 = "MsofbtClientAnchor"
            goto L_0x00bd
        L_0x008a:
            java.lang.String r8 = "MsofbtChildAnchor"
            goto L_0x00bd
        L_0x008d:
            java.lang.String r8 = "MsofbtAnchor"
            goto L_0x00bd
        L_0x0090:
            java.lang.String r8 = "MsofbtClientTextbox"
            goto L_0x00bd
        L_0x0093:
            java.lang.String r8 = "MsofbtTextbox"
            goto L_0x00bd
        L_0x0096:
            java.lang.String r8 = "MsofbtOPT"
            goto L_0x00bd
        L_0x0099:
            java.lang.String r8 = "MsofbtSp"
            goto L_0x00bd
        L_0x009c:
            java.lang.String r8 = "MsofbtSpgr"
            goto L_0x00bd
        L_0x009f:
            java.lang.String r8 = "MsofbtDg"
            goto L_0x00bd
        L_0x00a2:
            java.lang.String r8 = "MsofbtBSE"
            goto L_0x00bd
        L_0x00a5:
            java.lang.String r8 = "MsofbtDgg"
            goto L_0x00bd
        L_0x00a8:
            java.lang.String r8 = "MsofbtSolverContainer"
            goto L_0x00bd
        L_0x00ab:
            java.lang.String r8 = "MsofbtSpContainer"
            goto L_0x00bd
        L_0x00ae:
            java.lang.String r8 = "MsofbtSpgrContainer"
            goto L_0x00bd
        L_0x00b1:
            java.lang.String r8 = "MsofbtDgContainer"
            goto L_0x00bd
        L_0x00b4:
            java.lang.String r8 = "MsofbtBstoreContainer"
            goto L_0x00bd
        L_0x00b7:
            java.lang.String r8 = "MsofbtDggContainer"
            goto L_0x00bd
        L_0x00ba:
            java.lang.String r8 = "MsofbtUDefProp"
        L_0x00bd:
            java.lang.String r10 = "  "
            r7.append(r10)
            java.lang.String r10 = org.apache.poi.util.HexDump.toHex(r12)
            r7.append(r10)
            java.lang.String r10 = "  "
            r7.append(r10)
            r7.append(r8)
            java.lang.String r10 = " ["
            r7.append(r10)
            java.lang.String r10 = org.apache.poi.util.HexDump.toHex(r11)
            r7.append(r10)
            r10 = 44
            r7.append(r10)
            java.lang.String r10 = org.apache.poi.util.HexDump.toHex(r13)
            r7.append(r10)
            java.lang.String r10 = "]  instance: "
            r7.append(r10)
            int r10 = r11 >> 4
            short r10 = (short) r10
            java.lang.String r10 = org.apache.poi.util.HexDump.toHex(r10)
            r7.append(r10)
            java.lang.String r10 = r7.toString()
            r2.println(r10)
            r10 = -4089(0xfffffffffffff007, float:NaN)
            r14 = 16
            r9 = 2
            if (r12 != r10) goto L_0x01b5
            r16 = 36
            int r10 = (r16 > r4 ? 1 : (r16 == r4 ? 0 : -1))
            if (r10 > 0) goto L_0x01b5
            r10 = 36
            if (r10 > r13) goto L_0x01b5
            java.lang.StringBuffer r10 = new java.lang.StringBuffer
            java.lang.String r15 = "    btWin32: "
            r10.<init>(r15)
            r7 = r10
            int r10 = r25.read()
            byte r10 = (byte) r10
            java.lang.String r15 = org.apache.poi.util.HexDump.toHex(r10)
            r7.append(r15)
            java.lang.String r15 = getBlipType(r10)
            r7.append(r15)
            java.lang.String r15 = "  btMacOS: "
            r7.append(r15)
            int r15 = r25.read()
            byte r10 = (byte) r15
            java.lang.String r15 = org.apache.poi.util.HexDump.toHex(r10)
            r7.append(r15)
            java.lang.String r15 = getBlipType(r10)
            r7.append(r15)
            java.lang.String r15 = r7.toString()
            r2.println(r15)
            java.lang.String r15 = "    rgbUid:"
            r2.println(r15)
            org.apache.poi.util.HexDump.dump(r1, r2, r6, r14)
            java.lang.String r14 = "    tag: "
            r2.print(r14)
            r0.outHex(r9, r1, r2)
            r26.println()
            java.lang.String r9 = "    size: "
            r2.print(r9)
            r9 = 4
            r0.outHex(r9, r1, r2)
            r26.println()
            java.lang.String r14 = "    cRef: "
            r2.print(r14)
            r0.outHex(r9, r1, r2)
            r26.println()
            java.lang.String r14 = "    offs: "
            r2.print(r14)
            r0.outHex(r9, r1, r2)
            r26.println()
            java.lang.String r9 = "    usage: "
            r2.print(r9)
            r15 = 1
            r0.outHex(r15, r1, r2)
            r26.println()
            java.lang.String r9 = "    cbName: "
            r2.print(r9)
            r0.outHex(r15, r1, r2)
            r26.println()
            java.lang.String r9 = "    unused2: "
            r2.print(r9)
            r0.outHex(r15, r1, r2)
            r26.println()
            java.lang.String r9 = "    unused3: "
            r2.print(r9)
            r0.outHex(r15, r1, r2)
            r26.println()
            long r4 = r4 - r16
            r13 = 0
        L_0x01b1:
            r19 = r3
            goto L_0x04d1
        L_0x01b5:
            r15 = 1
            r10 = -4080(0xfffffffffffff010, float:NaN)
            if (r12 != r10) goto L_0x021a
            r16 = 18
            int r10 = (r16 > r4 ? 1 : (r16 == r4 ? 0 : -1))
            if (r10 > 0) goto L_0x021a
            r10 = 18
            if (r10 > r13) goto L_0x021a
            java.lang.String r10 = "    Flag: "
            r2.print(r10)
            r0.outHex(r9, r1, r2)
            r26.println()
            java.lang.String r10 = "    Col1: "
            r2.print(r10)
            r0.outHex(r9, r1, r2)
            java.lang.String r10 = "    dX1: "
            r2.print(r10)
            r0.outHex(r9, r1, r2)
            java.lang.String r10 = "    Row1: "
            r2.print(r10)
            r0.outHex(r9, r1, r2)
            java.lang.String r10 = "    dY1: "
            r2.print(r10)
            r0.outHex(r9, r1, r2)
            r26.println()
            java.lang.String r10 = "    Col2: "
            r2.print(r10)
            r0.outHex(r9, r1, r2)
            java.lang.String r10 = "    dX2: "
            r2.print(r10)
            r0.outHex(r9, r1, r2)
            java.lang.String r10 = "    Row2: "
            r2.print(r10)
            r0.outHex(r9, r1, r2)
            java.lang.String r10 = "    dY2: "
            r2.print(r10)
            r0.outHex(r9, r1, r2)
            r26.println()
            long r4 = r4 - r16
            int r13 = r13 + -18
            goto L_0x01b1
        L_0x021a:
            r9 = -4085(0xfffffffffffff00b, float:NaN)
            if (r12 == r9) goto L_0x03ea
            r9 = -3806(0xfffffffffffff122, float:NaN)
            if (r12 != r9) goto L_0x0228
            r19 = r3
            r20 = r7
            goto L_0x03ee
        L_0x0228:
            r9 = -4078(0xfffffffffffff012, float:NaN)
            if (r12 != r9) goto L_0x027b
            java.lang.String r9 = "    Connector rule: "
            r2.print(r9)
            int r9 = org.apache.poi.util.LittleEndian.readInt(r25)
            r2.print(r9)
            java.lang.String r9 = "    ShapeID A: "
            r2.print(r9)
            int r9 = org.apache.poi.util.LittleEndian.readInt(r25)
            r2.print(r9)
            java.lang.String r9 = "   ShapeID B: "
            r2.print(r9)
            int r9 = org.apache.poi.util.LittleEndian.readInt(r25)
            r2.print(r9)
            java.lang.String r9 = "    ShapeID connector: "
            r2.print(r9)
            int r9 = org.apache.poi.util.LittleEndian.readInt(r25)
            r2.print(r9)
            java.lang.String r9 = "   Connect pt A: "
            r2.print(r9)
            int r9 = org.apache.poi.util.LittleEndian.readInt(r25)
            r2.print(r9)
            java.lang.String r9 = "   Connect pt B: "
            r2.print(r9)
            int r9 = org.apache.poi.util.LittleEndian.readInt(r25)
            r2.println(r9)
            int r13 = r13 + -24
            r9 = 24
            long r4 = r4 - r9
            goto L_0x01b1
        L_0x027b:
            r9 = -4072(0xfffffffffffff018, float:NaN)
            if (r12 < r9) goto L_0x03e2
            r9 = -3817(0xfffffffffffff117, float:NaN)
            if (r12 >= r9) goto L_0x03e2
            java.lang.String r9 = "    Secondary UID: "
            r2.println(r9)
            org.apache.poi.util.HexDump.dump(r1, r2, r6, r14)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "    Cache of size: "
            r9.append(r10)
            int r10 = org.apache.poi.util.LittleEndian.readInt(r25)
            java.lang.String r10 = org.apache.poi.util.HexDump.toHex(r10)
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            r2.println(r9)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "    Boundary top: "
            r9.append(r10)
            int r10 = org.apache.poi.util.LittleEndian.readInt(r25)
            java.lang.String r10 = org.apache.poi.util.HexDump.toHex(r10)
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            r2.println(r9)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "    Boundary left: "
            r9.append(r10)
            int r10 = org.apache.poi.util.LittleEndian.readInt(r25)
            java.lang.String r10 = org.apache.poi.util.HexDump.toHex(r10)
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            r2.println(r9)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "    Boundary width: "
            r9.append(r10)
            int r10 = org.apache.poi.util.LittleEndian.readInt(r25)
            java.lang.String r10 = org.apache.poi.util.HexDump.toHex(r10)
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            r2.println(r9)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "    Boundary height: "
            r9.append(r10)
            int r10 = org.apache.poi.util.LittleEndian.readInt(r25)
            java.lang.String r10 = org.apache.poi.util.HexDump.toHex(r10)
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            r2.println(r9)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "    X: "
            r9.append(r10)
            int r10 = org.apache.poi.util.LittleEndian.readInt(r25)
            java.lang.String r10 = org.apache.poi.util.HexDump.toHex(r10)
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            r2.println(r9)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "    Y: "
            r9.append(r10)
            int r10 = org.apache.poi.util.LittleEndian.readInt(r25)
            java.lang.String r10 = org.apache.poi.util.HexDump.toHex(r10)
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            r2.println(r9)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "    Cache of saved size: "
            r9.append(r10)
            int r10 = org.apache.poi.util.LittleEndian.readInt(r25)
            java.lang.String r10 = org.apache.poi.util.HexDump.toHex(r10)
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            r2.println(r9)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "    Compression Flag: "
            r9.append(r10)
            int r10 = r25.read()
            byte r10 = (byte) r10
            java.lang.String r10 = org.apache.poi.util.HexDump.toHex(r10)
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            r2.println(r9)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "    Filter: "
            r9.append(r10)
            int r10 = r25.read()
            byte r10 = (byte) r10
            java.lang.String r10 = org.apache.poi.util.HexDump.toHex(r10)
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            r2.println(r9)
            java.lang.String r9 = "    Data (after decompression): "
            r2.println(r9)
            int r13 = r13 + -50
            r9 = 50
            long r4 = r4 - r9
            int r9 = (int) r4
            if (r13 <= r9) goto L_0x03b5
            int r9 = (int) r4
            short r9 = (short) r9
            goto L_0x03b6
        L_0x03b5:
            short r9 = (short) r13
        L_0x03b6:
            byte[] r10 = new byte[r9]
            int r14 = r1.read(r10)
        L_0x03bc:
            r15 = -1
            if (r14 == r15) goto L_0x03c9
            if (r14 >= r9) goto L_0x03c9
            int r15 = r10.length
            int r15 = r1.read(r10, r14, r15)
            int r14 = r14 + r15
            r15 = 1
            goto L_0x03bc
        L_0x03c9:
            java.io.ByteArrayInputStream r15 = new java.io.ByteArrayInputStream
            r15.<init>(r10)
            java.util.zip.InflaterInputStream r6 = new java.util.zip.InflaterInputStream
            r6.<init>(r15)
            r19 = r3
            r3 = -1
            r20 = r7
            r7 = 0
            org.apache.poi.util.HexDump.dump(r6, r2, r7, r3)
            int r13 = r13 - r9
            r21 = r6
            long r6 = (long) r9
            long r4 = r4 - r6
            goto L_0x03e6
        L_0x03e2:
            r19 = r3
            r20 = r7
        L_0x03e6:
            r7 = r20
            goto L_0x04d1
        L_0x03ea:
            r19 = r3
            r20 = r7
        L_0x03ee:
            r3 = 0
            java.lang.String r6 = "    PROPID        VALUE"
            r2.println(r6)
        L_0x03f4:
            int r6 = r3 + 6
            if (r13 < r6) goto L_0x04b7
            int r6 = r3 + 6
            long r6 = (long) r6
            int r6 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r6 < 0) goto L_0x04b7
            short r6 = org.apache.poi.util.LittleEndian.readShort(r25)
            int r7 = org.apache.poi.util.LittleEndian.readInt(r25)
            int r13 = r13 + -6
            r9 = 6
            long r4 = r4 - r9
            java.lang.String r9 = "    "
            r2.print(r9)
            java.lang.String r9 = org.apache.poi.util.HexDump.toHex(r6)
            r2.print(r9)
            java.lang.String r9 = " ("
            r2.print(r9)
            r9 = r6 & 16383(0x3fff, float:2.2957E-41)
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r14 = " "
            r10.append(r14)
            r10.append(r9)
            java.lang.String r10 = r10.toString()
            r2.print(r10)
            r10 = r6 & -32768(0xffffffffffff8000, float:NaN)
            if (r10 != 0) goto L_0x0484
            r10 = r6 & 16384(0x4000, float:2.2959E-41)
            if (r10 == 0) goto L_0x0440
            java.lang.String r10 = ", fBlipID"
            r2.print(r10)
        L_0x0440:
            java.lang.String r10 = ")  "
            r2.print(r10)
            java.lang.String r10 = org.apache.poi.util.HexDump.toHex(r7)
            r2.print(r10)
            r10 = r6 & 16384(0x4000, float:2.2959E-41)
            if (r10 != 0) goto L_0x0480
            java.lang.String r10 = " ("
            r2.print(r10)
            java.lang.String r10 = r0.dec1616(r7)
            r2.print(r10)
            r10 = 41
            r2.print(r10)
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r14 = " {"
            r10.append(r14)
            short r14 = (short) r9
            java.lang.String r14 = r0.propName(r14)
            r10.append(r14)
            java.lang.String r14 = "}"
            r10.append(r14)
            java.lang.String r10 = r10.toString()
            r2.print(r10)
        L_0x0480:
            r26.println()
            goto L_0x04b5
        L_0x0484:
            java.lang.String r10 = ", fComplex)  "
            r2.print(r10)
            java.lang.String r10 = org.apache.poi.util.HexDump.toHex(r7)
            r2.print(r10)
            java.lang.String r10 = " - Complex prop len"
            r2.print(r10)
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r14 = " {"
            r10.append(r14)
            short r14 = (short) r9
            java.lang.String r14 = r0.propName(r14)
            r10.append(r14)
            java.lang.String r14 = "}"
            r10.append(r14)
            java.lang.String r10 = r10.toString()
            r2.println(r10)
            int r3 = r3 + r7
        L_0x04b5:
            goto L_0x03f4
        L_0x04b7:
            long r6 = (long) r3
            long r6 = r6 & r4
            r9 = 0
            int r6 = (r6 > r9 ? 1 : (r6 == r9 ? 0 : -1))
            if (r6 <= 0) goto L_0x04cf
            int r6 = (int) r4
            if (r3 <= r6) goto L_0x04c5
            int r6 = (int) r4
            short r6 = (short) r6
            goto L_0x04c6
        L_0x04c5:
            short r6 = (short) r3
        L_0x04c6:
            r7 = 0
            org.apache.poi.util.HexDump.dump(r1, r2, r7, r6)
            int r3 = r3 - r6
            int r13 = r13 - r6
            long r9 = (long) r6
            long r4 = r4 - r9
            goto L_0x04b7
        L_0x04cf:
            goto L_0x03e6
        L_0x04d1:
            r3 = r11 & 15
            r6 = 15
            if (r3 != r6) goto L_0x04da
            r18 = 1
            goto L_0x04dc
        L_0x04da:
            r18 = 0
        L_0x04dc:
            r3 = r18
            if (r3 == 0) goto L_0x04f6
            r9 = 0
            int r6 = (r4 > r9 ? 1 : (r4 == r9 ? 0 : -1))
            if (r6 < 0) goto L_0x04f6
            int r6 = (int) r4
            if (r13 > r6) goto L_0x04f0
            java.lang.String r6 = "            completed within"
            r2.println(r6)
        L_0x04ee:
            r9 = 0
            goto L_0x0512
        L_0x04f0:
            java.lang.String r6 = "            continued elsewhere"
            r2.println(r6)
            goto L_0x04ee
        L_0x04f6:
            r9 = 0
            int r6 = (r4 > r9 ? 1 : (r4 == r9 ? 0 : -1))
            if (r6 < 0) goto L_0x050c
            int r6 = (int) r4
            if (r13 <= r6) goto L_0x0502
            int r6 = (int) r4
            short r6 = (short) r6
            goto L_0x0503
        L_0x0502:
            short r6 = (short) r13
        L_0x0503:
            if (r6 == 0) goto L_0x04ee
            r9 = 0
            org.apache.poi.util.HexDump.dump(r1, r2, r9, r6)
            long r14 = (long) r6
            long r4 = r4 - r14
            goto L_0x0512
        L_0x050c:
            r9 = 0
            java.lang.String r6 = " >> OVERRUN <<"
            r2.println(r6)
        L_0x0512:
            r3 = r19
            r6 = 0
            goto L_0x0013
        L_0x0518:
            r19 = r3
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.ddf.EscherDump.dumpOld(long, java.io.InputStream, java.io.PrintStream):void");
    }

    private String propName(short propertyId) {
        AnonymousClass1PropName[] props = {new Object(4, "transform.rotation") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(119, "protection.lockrotation") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(ShapeTypes.CLOUD_CALLOUT, "protection.lockaspectratio") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(ShapeTypes.CLOUD, "protection.lockposition") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(ShapeTypes.RIBBON, "protection.lockagainstselect") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(ShapeTypes.RIBBON_2, "protection.lockcropping") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(ShapeTypes.ELLIPSE_RIBBON, "protection.lockvertices") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(ShapeTypes.ELLIPSE_RIBBON_2, "protection.locktext") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(ShapeTypes.LEFT_RIGHT_RIBBON, "protection.lockadjusthandles") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(ShapeTypes.VERTICAL_SCROLL, "protection.lockagainstgrouping") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(128, "text.textid") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(129, "text.textleft") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(130, "text.texttop") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(ShapeTypes.PLUS, "text.textright") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(ShapeTypes.FLOW_CHART_PROCESS, "text.textbottom") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(ShapeTypes.FLOW_CHART_DECISION, "text.wraptext") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(ShapeTypes.FLOW_CHART_INPUT_OUTPUT, "text.scaletext") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(ShapeTypes.FLOW_CHART_PREDEFINED_PROCESS, "text.anchortext") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(ShapeTypes.FLOW_CHART_INTERNAL_STORAGE, "text.textflow") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(ShapeTypes.FLOW_CHART_DOCUMENT, "text.fontrotation") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(ShapeTypes.FLOW_CHART_MULTIDOCUMENT, "text.idofnextshape") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(ShapeTypes.FLOW_CHART_TERMINATOR, "text.bidir") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(ShapeTypes.CHART_PLUS, "text.singleclickselects") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(188, "text.usehostmargins") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(189, "text.rotatetextwithshape") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(190, "text.sizeshapetofittext") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(191, "text.sizetexttofitshape") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(192, "geotext.unicode") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(193, "geotext.rtftext") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(194, "geotext.alignmentoncurve") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(195, "geotext.defaultpointsize") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(196, "geotext.textspacing") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(197, "geotext.fontfamilyname") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(240, "geotext.reverseroworder") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(241, "geotext.hastexteffect") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(242, "geotext.rotatecharacters") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(243, "geotext.kerncharacters") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(244, "geotext.tightortrack") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(245, "geotext.stretchtofitshape") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(246, "geotext.charboundingbox") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(247, "geotext.scaletextonpath") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(248, "geotext.stretchcharheight") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(249, "geotext.nomeasurealongpath") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(Callback.DEFAULT_SWIPE_ANIMATION_DURATION, "geotext.boldfont") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(251, "geotext.italicfont") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(252, "geotext.underlinefont") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(253, "geotext.shadowfont") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(254, "geotext.smallcapsfont") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(255, "geotext.strikethroughfont") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(256, "blip.cropfromtop") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(InputDeviceCompat.SOURCE_KEYBOARD, "blip.cropfrombottom") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(258, "blip.cropfromleft") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(259, "blip.cropfromright") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(260, "blip.bliptodisplay") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(261, "blip.blipfilename") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(262, "blip.blipflags") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(263, "blip.transparentcolor") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(264, "blip.contrastsetting") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(265, "blip.brightnesssetting") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(266, "blip.gamma") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(267, "blip.pictureid") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(268, "blip.doublemod") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(269, "blip.picturefillmod") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(270, "blip.pictureline") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(271, "blip.printblip") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(272, "blip.printblipfilename") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(273, "blip.printflags") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(316, "blip.nohittestpicture") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(317, "blip.picturegray") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(318, "blip.picturebilevel") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(319, "blip.pictureactive") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(320, "geometry.left") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(321, "geometry.top") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(322, "geometry.right") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(323, "geometry.bottom") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(324, "geometry.shapepath") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(325, "geometry.vertices") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(326, "geometry.segmentinfo") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(327, "geometry.adjustvalue") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(328, "geometry.adjust2value") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(329, "geometry.adjust3value") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(330, "geometry.adjust4value") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(331, "geometry.adjust5value") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(332, "geometry.adjust6value") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(333, "geometry.adjust7value") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(334, "geometry.adjust8value") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(335, "geometry.adjust9value") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(336, "geometry.adjust10value") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(378, "geometry.shadowOK") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(379, "geometry.3dok") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(380, "geometry.lineok") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(381, "geometry.geotextok") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(382, "geometry.fillshadeshapeok") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(383, "geometry.fillok") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(384, "fill.filltype") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(385, "fill.fillcolor") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(386, "fill.fillopacity") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(387, "fill.fillbackcolor") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(388, "fill.backopacity") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(389, "fill.crmod") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(390, "fill.patterntexture") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(391, "fill.blipfilename") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(392, "fill.blipflags") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(393, "fill.width") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(394, "fill.height") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(395, "fill.angle") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(396, "fill.focus") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(397, "fill.toleft") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(398, "fill.totop") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(399, "fill.toright") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(400, "fill.tobottom") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(401, "fill.rectleft") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(402, "fill.recttop") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(403, "fill.rectright") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(404, "fill.rectbottom") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(405, "fill.dztype") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(406, "fill.shadepreset") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(407, "fill.shadecolors") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(408, "fill.originx") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(409, "fill.originy") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(410, "fill.shapeoriginx") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(411, "fill.shapeoriginy") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(412, "fill.shadetype") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(443, "fill.filled") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(444, "fill.hittestfill") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(445, "fill.shape") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(446, "fill.userect") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(447, "fill.nofillhittest") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(448, "linestyle.color") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(449, "linestyle.opacity") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(450, "linestyle.backcolor") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(451, "linestyle.crmod") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(452, "linestyle.linetype") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(453, "linestyle.fillblip") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(454, "linestyle.fillblipname") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(455, "linestyle.fillblipflags") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(456, "linestyle.fillwidth") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(457, "linestyle.fillheight") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(458, "linestyle.filldztype") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(459, "linestyle.linewidth") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(460, "linestyle.linemiterlimit") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(461, "linestyle.linestyle") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(462, "linestyle.linedashing") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(463, "linestyle.linedashstyle") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(464, "linestyle.linestartarrowhead") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(465, "linestyle.lineendarrowhead") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(466, "linestyle.linestartarrowwidth") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(467, "linestyle.lineestartarrowlength") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(468, "linestyle.lineendarrowwidth") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(469, "linestyle.lineendarrowlength") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(470, "linestyle.linejoinstyle") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(471, "linestyle.lineendcapstyle") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(507, "linestyle.arrowheadsok") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(508, "linestyle.anyline") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(509, "linestyle.hitlinetest") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(510, "linestyle.linefillshape") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(FrameMetricsAggregator.EVERY_DURATION, "linestyle.nolinedrawdash") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(512, "shadowstyle.type") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(InputDeviceCompat.SOURCE_DPAD, "shadowstyle.color") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(514, "shadowstyle.highlight") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(515, "shadowstyle.crmod") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(516, "shadowstyle.opacity") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(RxBleConnection.GATT_MTU_MAXIMUM, "shadowstyle.offsetx") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(518, "shadowstyle.offsety") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(519, "shadowstyle.secondoffsetx") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(520, "shadowstyle.secondoffsety") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(521, "shadowstyle.scalextox") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(522, "shadowstyle.scaleytox") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(523, "shadowstyle.scalextoy") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(524, "shadowstyle.scaleytoy") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(525, "shadowstyle.perspectivex") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(526, "shadowstyle.perspectivey") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(527, "shadowstyle.weight") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(528, "shadowstyle.originx") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(529, "shadowstyle.originy") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(574, "shadowstyle.shadow") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(575, "shadowstyle.shadowobsured") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(576, "perspective.type") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(577, "perspective.offsetx") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(578, "perspective.offsety") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(579, "perspective.scalextox") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(580, "perspective.scaleytox") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(581, "perspective.scalextoy") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(582, "perspective.scaleytox") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(583, "perspective.perspectivex") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(584, "perspective.perspectivey") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(585, "perspective.weight") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(586, "perspective.originx") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(587, "perspective.originy") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(639, "perspective.perspectiveon") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(640, "3d.specularamount") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(661, "3d.diffuseamount") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(662, "3d.shininess") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(663, "3d.edgethickness") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(664, "3d.extrudeforward") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(665, "3d.extrudebackward") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(666, "3d.extrudeplane") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(667, "3d.extrusioncolor") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(648, "3d.crmod") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(700, "3d.3deffect") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(701, "3d.metallic") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(702, "3d.useextrusioncolor") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(703, "3d.lightface") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(704, "3dstyle.yrotationangle") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(705, "3dstyle.xrotationangle") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(706, "3dstyle.rotationaxisx") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(707, "3dstyle.rotationaxisy") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(708, "3dstyle.rotationaxisz") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(709, "3dstyle.rotationangle") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(710, "3dstyle.rotationcenterx") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(711, "3dstyle.rotationcentery") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(712, "3dstyle.rotationcenterz") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(713, "3dstyle.rendermode") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(714, "3dstyle.tolerance") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(715, "3dstyle.xviewpoint") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(716, "3dstyle.yviewpoint") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(717, "3dstyle.zviewpoint") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(718, "3dstyle.originx") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(719, "3dstyle.originy") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(720, "3dstyle.skewangle") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(721, "3dstyle.skewamount") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(722, "3dstyle.ambientintensity") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(723, "3dstyle.keyx") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(724, "3dstyle.keyy") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(725, "3dstyle.keyz") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(726, "3dstyle.keyintensity") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(727, "3dstyle.fillx") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(728, "3dstyle.filly") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(729, "3dstyle.fillz") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(730, "3dstyle.fillintensity") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(763, "3dstyle.constrainrotation") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(764, "3dstyle.rotationcenterauto") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(765, "3dstyle.parallel") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(766, "3dstyle.keyharsh") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(767, "3dstyle.fillharsh") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(769, "shape.master") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(771, "shape.connectorstyle") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(772, "shape.blackandwhitesettings") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(773, "shape.wmodepurebw") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(774, "shape.wmodebw") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(826, "shape.oleicon") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(827, "shape.preferrelativeresize") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(828, "shape.lockshapetype") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(830, "shape.deleteattachedobject") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(831, "shape.backgroundshape") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(832, "callout.callouttype") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(833, "callout.xycalloutgap") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(834, "callout.calloutangle") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(835, "callout.calloutdroptype") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(836, "callout.calloutdropspecified") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(837, "callout.calloutlengthspecified") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(889, "callout.iscallout") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(890, "callout.calloutaccentbar") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(891, "callout.callouttextborder") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(892, "callout.calloutminusx") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(893, "callout.calloutminusy") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(894, "callout.dropauto") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(895, "callout.lengthspecified") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(896, "groupshape.shapename") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(897, "groupshape.description") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(898, "groupshape.hyperlink") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(899, "groupshape.wrappolygonvertices") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(900, "groupshape.wrapdistleft") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(901, "groupshape.wrapdisttop") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(902, "groupshape.wrapdistright") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(903, "groupshape.wrapdistbottom") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(904, "groupshape.regroupid") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(953, "groupshape.editedwrap") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(954, "groupshape.behinddocument") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(955, "groupshape.ondblclicknotify") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(956, "groupshape.isbutton") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(957, "groupshape.1dadjustment") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(958, "groupshape.hidden") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }, new Object(959, "groupshape.print") {
            final int _id;
            final String _name;

            {
                this._id = id;
                this._name = name;
            }
        }};
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= props.length) {
                short s = propertyId;
                return "unknown property";
            } else if (props[i2]._id == propertyId) {
                return props[i2]._name;
            } else {
                i = i2 + 1;
            }
        }
    }

    private static String getBlipType(byte b) {
        return EscherBSERecord.getBlipType(b);
    }

    private String dec1616(int n32) {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append((short) (n32 >> 16));
        String result = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(result);
        sb2.append('.');
        String result2 = sb2.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(result2);
        sb3.append((short) (n32 & -1));
        return sb3.toString();
    }

    private void outHex(int bytes, InputStream in, PrintStream out) throws IOException, BufferUnderrunException {
        if (bytes != 4) {
            switch (bytes) {
                case 1:
                    out.print(HexDump.toHex((byte) in.read()));
                    return;
                case 2:
                    out.print(HexDump.toHex(LittleEndian.readShort(in)));
                    return;
                default:
                    throw new IOException("Unable to output variable of that width");
            }
        } else {
            out.print(HexDump.toHex(LittleEndian.readInt(in)));
        }
    }

    public static void main(String[] args) {
        byte[] bytes = HexRead.readFromString("0F 00 00 F0 89 07 00 00 00 00 06 F0 18 00 00 00 05 04 00 00 02 00 00 00 05 00 00 00 01 00 00 00 01 00 00 00 05 00 00 00 4F 00 01 F0 2F 07 00 00 42 00 07 F0 B7 01 00 00 03 04 3F 14 AE 6B 0F 65 B0 48 BF 5E 94 63 80 E8 91 73 FF 00 93 01 00 00 01 00 00 00 00 00 00 00 00 00 FF FF 20 54 1C F0 8B 01 00 00 3F 14 AE 6B 0F 65 B0 48 BF 5E 94 63 80 E8 91 73 92 0E 00 00 00 00 00 00 00 00 00 00 D1 07 00 00 DD 05 00 00 4A AD 6F 00 8A C5 53 00 59 01 00 00 00 FE 78 9C E3 9B C4 00 04 AC 77 D9 2F 32 08 32 FD E7 61 F8 FF 0F C8 FD 05 C5 30 19 10 90 63 90 FA 0F 06 0C 8C 0C 5C 70 19 43 30 EB 0E FB 05 86 85 0C DB 18 58 80 72 8C 70 16 0B 83 05 56 51 29 88 C9 60 D9 69 0C 6C 20 26 23 03 C8 74 B0 A8 0E 03 07 FB 45 56 C7 A2 CC C4 1C 06 66 A0 0D 2C 40 39 5E 86 4C 06 3D A0 4E 10 D0 60 D9 C8 58 CC E8 CF B0 80 61 3A 8A 7E 0D C6 23 AC 4F E0 E2 98 B6 12 2B 06 73 9D 12 E3 52 56 59 F6 08 8A CC 52 66 A3 50 FF 96 2B 94 E9 DF 4C A1 FE 2D 3A 03 AB 9F 81 C2 F0 A3 54 BF 0F 85 EE A7 54 FF 40 FB 7F A0 E3 9F D2 F4 4F 71 FE 19 58 FF 2B 31 7F 67 36 3B 25 4F 99 1B 4E 53 A6 5F 89 25 95 E9 C4 00 C7 83 12 F3 1F 26 35 4A D3 D2 47 0E 0A C3 41 8E C9 8A 52 37 DC 15 A1 D0 0D BC 4C 06 0C 2B 28 2C 13 28 D4 EF 43 61 5A A0 58 3F 85 71 E0 4B 69 9E 64 65 FE 39 C0 E5 22 30 1D 30 27 0E 74 3A 18 60 FD 4A CC B1 2C 13 7D 07 36 2D 2A 31 85 B2 6A 0D 74 1D 1D 22 4D 99 FE 60 0A F5 9B EC 1C 58 FD 67 06 56 3F 38 0D 84 3C A5 30 0E 28 D3 AF C4 A4 CA FA 44 7A 0D 65 6E 60 7F 4D A1 1B 24 58 F7 49 AF A5 CC 0D CC DF 19 FE 03 00 F0 B1 25 4D 42 00 07 F0 E1 01 00 00 03 04 39 50 BE 98 B0 6F 57 24 31 70 5D 23 2F 9F 10 66 FF 00 BD 01 00 00 01 00 00 00 00 00 00 00 00 00 FF FF 20 54 1C F0 B5 01 00 00 39 50 BE 98 B0 6F 57 24 31 70 5D 23 2F 9F 10 66 DA 03 00 00 00 00 00 00 00 00 00 00 D1 07 00 00 DD 05 00 00 4A AD 6F 00 8A C5 53 00 83 01 00 00 00 FE 78 9C A5 52 BF 4B 42 51 14 3E F7 DC 77 7A 16 45 48 8B 3C 48 A8 16 15 0D 6C 88 D0 04 C3 40 A3 32 1C 84 96 08 21 04 A1 C5 5C A2 35 82 C0 35 6A AB 1C 6A 6B A8 24 5A 83 68 08 84 84 96 A2 86 A0 7F C2 86 5E E7 5E F5 41 E4 10 BC 03 1F E7 FB F1 CE B9 F7 F1 9E 7C 05 2E 7A 37 9B E0 45 7B 10 EC 6F 96 5F 1D 74 13 55 7E B0 6C 5D 20 60 C0 49 A2 9A BD 99 4F 50 83 1B 30 38 13 0E 33 60 A6 A7 6B B5 37 EB F4 10 FA 14 15 A0 B6 6B 37 0C 1E B3 49 73 5B A5 C2 26 48 3E C1 E0 6C 08 4A 30 C9 93 AA 02 B8 20 13 62 05 4E E1 E8 D7 7C C0 B8 14 95 5E BE B8 A7 CF 1E BE 55 2C 56 B9 78 DF 08 7E 88 4C 27 FF 7B DB FF 7A DD B7 1A 17 67 34 6A AE BA DA 35 D1 E7 72 BE FE EC 6E FE DA E5 7C 3D EC 7A DE 03 FD 50 06 0B 23 F2 0E F3 B2 A5 11 91 0D 4C B5 B5 F3 BF 94 C1 8F 24 F7 D9 6F 60 94 3B C9 9A F3 1C 6B E7 BB F0 2E 49 B2 25 2B C6 B1 EE 69 EE 15 63 4F 71 7D CE 85 CC C8 35 B9 C3 28 28 CE D0 5C 67 79 F2 4A A2 14 23 A4 38 43 73 9D 2D 69 2F C1 08 31 9F C5 5C 9B EB 7B C5 69 19 B3 B4 81 F3 DC E3 B4 8E 8B CC B3 94 53 5A E7 41 2A 63 9A AA 38 C5 3D 48 BB EC 57 59 6F 2B AD 73 1F 1D 60 92 AE 70 8C BB 8F CE 31 C1 3C 49 27 4A EB DC A4 5B 8C D1 0B 0E 73 37 E9 11 A7 99 C7 E8 41 69 B0 7F 00 96 F2 A7 E8 42 00 07 F0 B4 01 00 00 03 04 1A BA F9 D6 A9 B9 3A 03 08 61 E9 90 FF 7B 9E E6 FF 00 90 01 00 00 01 00 00 00 00 00 00 00 00 00 FF FF 20 54 1C F0 88 01 00 00 1A BA F9 D6 A9 B9 3A 03 08 61 E9 90 FF 7B 9E E6 12 0E 00 00 00 00 00 00 00 00 00 00 D1 07 00 00 DD 05 00 00 4A AD 6F 00 8A C5 53 00 56 01 00 00 00 FE 78 9C E3 13 62 00 02 D6 BB EC 17 19 04 99 FE F3 30 FC FF 07 E4 FE 82 62 98 0C 08 C8 31 48 FD 07 03 06 46 06 2E B8 8C 21 98 75 87 FD 02 C3 42 86 6D 0C 2C 40 39 46 38 8B 85 C1 02 AB A8 14 C4 64 B0 EC 34 06 36 10 93 91 01 64 3A 58 54 87 81 83 FD 22 AB 63 51 66 62 0E 03 33 D0 06 16 A0 1C 2F 43 26 83 1E 50 27 08 68 B0 6C 64 2C 66 F4 67 58 C0 30 1D 45 BF 06 E3 11 D6 27 70 71 4C 5B 89 15 83 B9 4E 89 71 29 AB 2C 7B 04 45 66 29 B3 51 A8 7F CB 15 CA F4 6F A6 50 FF 16 9D 81 D5 CF 40 61 F8 51 AA DF 87 42 F7 53 AA 7F A0 FD 3F D0 F1 4F 69 FA A7 38 FF 0C AC FF 95 98 BF 33 9B 9D 92 A7 CC 0D A7 29 D3 AF C4 92 CA 74 62 80 E3 41 89 F9 0F 93 1A A5 69 E9 23 07 85 E1 20 C7 64 45 A9 1B EE 8A 50 E8 06 5E 26 03 86 15 14 96 09 14 EA F7 A1 30 2D 50 AC 9F C2 38 F0 A5 34 4F B2 32 FF 1C E0 72 11 98 0E 98 13 07 38 1D 28 31 C7 B2 4C F4 1D D8 B4 A0 C4 14 CA AA 35 D0 75 64 88 34 65 FA 83 29 D4 6F B2 73 60 F5 9F A1 54 FF 0E CA D3 40 C8 53 0A E3 E0 09 85 6E 50 65 7D 22 BD 86 32 37 B0 BF A6 D0 0D 12 AC FB A4 D7 52 E6 06 E6 EF 0C FF 01 97 1D 12 C7 42 00 07 F0 C3 01 00 00 03 04 BA 4C B6 23 BA 8B 27 BE C8 55 59 86 24 9F 89 D4 FF 00 9F 01 00 00 01 00 00 00 00 00 00 00 00 00 FF FF 20 54 1C F0 97 01 00 00 BA 4C B6 23 BA 8B 27 BE C8 55 59 86 24 9F 89 D4 AE 0E 00 00 00 00 00 00 00 00 00 00 D1 07 00 00 DD 05 00 00 4A AD 6F 00 8A C5 53 00 65 01 00 00 00 FE 78 9C E3 5B C7 00 04 AC 77 D9 2F 32 08 32 FD E7 61 F8 FF 0F C8 FD 05 C5 30 19 10 90 63 90 FA 0F 06 0C 8C 0C 5C 70 19 43 30 EB 0E FB 05 86 85 0C DB 18 58 80 72 8C 70 16 0B 83 05 56 51 29 88 C9 60 D9 69 0C 6C 20 26 23 03 C8 74 B0 A8 0E 03 07 FB 45 56 C7 A2 CC C4 1C 06 66 A0 0D 2C 40 39 5E 86 4C 06 3D A0 4E 10 D0 60 99 C6 B8 98 D1 9F 61 01 C3 74 14 FD 1A 8C 2B D8 84 B1 88 4B A5 A5 75 03 01 50 DF 59 46 77 46 0F A8 3C A6 AB 88 15 83 B9 5E 89 B1 8B D5 97 2D 82 22 B3 94 29 D5 BF E5 CA C0 EA DF AC 43 A1 FD 14 EA 67 A0 30 FC 28 D5 EF 43 A1 FB 7D 87 B8 FF 07 3A FE 07 3A FD 53 EA 7E 0A C3 4F 89 F9 0E 73 EA 69 79 CA DC 70 8A 32 FD 4A 2C 5E 4C DF 87 7A 3C BC E0 A5 30 1E 3E 31 C5 33 AC A0 30 2F 52 A8 DF 87 C2 30 A4 54 3F A5 65 19 85 65 A9 12 D3 2B 16 0D 8A CB 13 4A F3 E3 27 E6 09 03 9D 0E 06 58 BF 12 B3 13 CB C1 01 4E 8B 4A 4C 56 AC 91 03 5D 37 86 48 53 A6 3F 98 42 FD 26 3B 07 56 FF 99 1D 14 EA A7 CC 7E 70 1A 08 79 42 61 1C 3C A5 D0 0D 9C 6C C2 32 6B 29 73 03 DB 6B CA DC C0 F8 97 F5 AD CC 1A CA DC C0 F4 83 32 37 B0 A4 30 CE FC C7 48 99 1B FE 33 32 FC 07 00 6C CC 2E 23 33 00 0B F0 12 00 00 00 BF 00 08 00 08 00 81 01 09 00 00 08 C0 01 40 00 00 08 40 00 1E F1 10 00 00 00 0D 00 00 08 0C 00 00 08 17 00 00 08 F7 00 00 10                                              ");
        new EscherDump().dump(bytes, 0, bytes.length, System.out);
    }

    public void dump(int recordSize, byte[] data, PrintStream out) {
        dump(data, 0, recordSize, out);
    }
}

package org.apache.poi.ddf;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.InflaterInputStream;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public final class EscherMetafileBlip extends EscherBlipRecord {
    private static final int HEADER_SIZE = 8;
    public static final short RECORD_ID_EMF = -4070;
    public static final short RECORD_ID_PICT = -4068;
    public static final short RECORD_ID_WMF = -4069;
    public static final short SIGNATURE_EMF = 15680;
    public static final short SIGNATURE_PICT = 21536;
    public static final short SIGNATURE_WMF = 8544;
    private static final POILogger log = POILogFactory.getLogger(EscherMetafileBlip.class);
    private byte[] field_1_UID;
    private byte[] field_2_UID;
    private int field_2_cb;
    private int field_3_rcBounds_x1;
    private int field_3_rcBounds_x2;
    private int field_3_rcBounds_y1;
    private int field_3_rcBounds_y2;
    private int field_4_ptSize_h;
    private int field_4_ptSize_w;
    private int field_5_cbSave;
    private byte field_6_fCompression;
    private byte field_7_fFilter;
    private byte[] raw_pictureData;
    private byte[] remainingData;

    public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory) {
        int bytesAfterHeader = readHeader(data, offset);
        int pos = offset + 8;
        this.field_1_UID = new byte[16];
        System.arraycopy(data, pos, this.field_1_UID, 0, 16);
        int pos2 = pos + 16;
        if ((getOptions() ^ getSignature()) == 16) {
            this.field_2_UID = new byte[16];
            System.arraycopy(data, pos2, this.field_2_UID, 0, 16);
            pos2 += 16;
        }
        this.field_2_cb = LittleEndian.getInt(data, pos2);
        int pos3 = pos2 + 4;
        this.field_3_rcBounds_x1 = LittleEndian.getInt(data, pos3);
        int pos4 = pos3 + 4;
        this.field_3_rcBounds_y1 = LittleEndian.getInt(data, pos4);
        int pos5 = pos4 + 4;
        this.field_3_rcBounds_x2 = LittleEndian.getInt(data, pos5);
        int pos6 = pos5 + 4;
        this.field_3_rcBounds_y2 = LittleEndian.getInt(data, pos6);
        int pos7 = pos6 + 4;
        this.field_4_ptSize_w = LittleEndian.getInt(data, pos7);
        int pos8 = pos7 + 4;
        this.field_4_ptSize_h = LittleEndian.getInt(data, pos8);
        int pos9 = pos8 + 4;
        this.field_5_cbSave = LittleEndian.getInt(data, pos9);
        int pos10 = pos9 + 4;
        this.field_6_fCompression = data[pos10];
        int pos11 = pos10 + 1;
        this.field_7_fFilter = data[pos11];
        int pos12 = pos11 + 1;
        this.raw_pictureData = new byte[this.field_5_cbSave];
        System.arraycopy(data, pos12, this.raw_pictureData, 0, this.field_5_cbSave);
        int pos13 = pos12 + this.field_5_cbSave;
        if (this.field_6_fCompression == 0) {
            this.field_pictureData = inflatePictureData(this.raw_pictureData);
        } else {
            this.field_pictureData = this.raw_pictureData;
        }
        int remaining = (bytesAfterHeader - pos13) + offset + 8;
        if (remaining > 0) {
            this.remainingData = new byte[remaining];
            System.arraycopy(data, pos13, this.remainingData, 0, remaining);
        }
        return bytesAfterHeader + 8;
    }

    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);
        int pos = offset;
        LittleEndian.putShort(data, pos, getOptions());
        int pos2 = pos + 2;
        LittleEndian.putShort(data, pos2, getRecordId());
        int pos3 = pos2 + 2;
        LittleEndian.putInt(data, pos3, getRecordSize() - 8);
        int pos4 = pos3 + 4;
        System.arraycopy(this.field_1_UID, 0, data, pos4, this.field_1_UID.length);
        int pos5 = pos4 + this.field_1_UID.length;
        if ((getOptions() ^ getSignature()) == 16) {
            System.arraycopy(this.field_2_UID, 0, data, pos5, this.field_2_UID.length);
            pos5 += this.field_2_UID.length;
        }
        LittleEndian.putInt(data, pos5, this.field_2_cb);
        int pos6 = pos5 + 4;
        LittleEndian.putInt(data, pos6, this.field_3_rcBounds_x1);
        int pos7 = pos6 + 4;
        LittleEndian.putInt(data, pos7, this.field_3_rcBounds_y1);
        int pos8 = pos7 + 4;
        LittleEndian.putInt(data, pos8, this.field_3_rcBounds_x2);
        int pos9 = pos8 + 4;
        LittleEndian.putInt(data, pos9, this.field_3_rcBounds_y2);
        int pos10 = pos9 + 4;
        LittleEndian.putInt(data, pos10, this.field_4_ptSize_w);
        int pos11 = pos10 + 4;
        LittleEndian.putInt(data, pos11, this.field_4_ptSize_h);
        int pos12 = pos11 + 4;
        LittleEndian.putInt(data, pos12, this.field_5_cbSave);
        int pos13 = pos12 + 4;
        data[pos13] = this.field_6_fCompression;
        int pos14 = pos13 + 1;
        data[pos14] = this.field_7_fFilter;
        int pos15 = pos14 + 1;
        System.arraycopy(this.raw_pictureData, 0, data, pos15, this.raw_pictureData.length);
        int pos16 = pos15 + this.raw_pictureData.length;
        if (this.remainingData != null) {
            System.arraycopy(this.remainingData, 0, data, pos16, this.remainingData.length);
            int pos17 = pos16 + this.remainingData.length;
        }
        listener.afterRecordSerialize(getRecordSize() + offset, getRecordId(), getRecordSize(), this);
        return getRecordSize();
    }

    private static byte[] inflatePictureData(byte[] data) {
        try {
            InflaterInputStream in = new InflaterInputStream(new ByteArrayInputStream(data));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            while (true) {
                int read = in.read(buf);
                int readBytes = read;
                if (read <= 0) {
                    return out.toByteArray();
                }
                out.write(buf, 0, readBytes);
            }
        } catch (IOException e) {
            log.log(POILogger.WARN, (Object) "Possibly corrupt compression or non-compressed data", (Throwable) e);
            return data;
        }
    }

    public int getRecordSize() {
        int size = this.raw_pictureData.length + 58;
        if (this.remainingData != null) {
            size += this.remainingData.length;
        }
        if ((getOptions() ^ getSignature()) == 16) {
            return size + this.field_2_UID.length;
        }
        return size;
    }

    public byte[] getUID() {
        return this.field_1_UID;
    }

    public void setUID(byte[] uid) {
        this.field_1_UID = uid;
    }

    public byte[] getPrimaryUID() {
        return this.field_2_UID;
    }

    public void setPrimaryUID(byte[] primaryUID) {
        this.field_2_UID = primaryUID;
    }

    public int getUncompressedSize() {
        return this.field_2_cb;
    }

    public void setUncompressedSize(int uncompressedSize) {
        this.field_2_cb = uncompressedSize;
    }

    public Rectangle getBounds() {
        return new Rectangle(this.field_3_rcBounds_x1, this.field_3_rcBounds_y1, this.field_3_rcBounds_x2 - this.field_3_rcBounds_x1, this.field_3_rcBounds_y2 - this.field_3_rcBounds_y1);
    }

    public void setBounds(Rectangle bounds) {
        this.field_3_rcBounds_x1 = bounds.x;
        this.field_3_rcBounds_y1 = bounds.y;
        this.field_3_rcBounds_x2 = bounds.x + bounds.width;
        this.field_3_rcBounds_y2 = bounds.y + bounds.height;
    }

    public Dimension getSizeEMU() {
        return new Dimension(this.field_4_ptSize_w, this.field_4_ptSize_h);
    }

    public void setSizeEMU(Dimension sizeEMU) {
        this.field_4_ptSize_w = sizeEMU.width;
        this.field_4_ptSize_h = sizeEMU.height;
    }

    public int getCompressedSize() {
        return this.field_5_cbSave;
    }

    public void setCompressedSize(int compressedSize) {
        this.field_5_cbSave = compressedSize;
    }

    public boolean isCompressed() {
        return this.field_6_fCompression == 0;
    }

    public void setCompressed(boolean compressed) {
        this.field_6_fCompression = compressed ? (byte) 0 : -2;
    }

    public byte[] getRemainingData() {
        return this.remainingData;
    }

    public String toString() {
        String str;
        String str2;
        String extraData = "";
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append(":");
        sb.append(10);
        sb.append("  RecordId: 0x");
        sb.append(HexDump.toHex(getRecordId()));
        sb.append(10);
        sb.append("  Options: 0x");
        sb.append(HexDump.toHex(getOptions()));
        sb.append(10);
        sb.append("  UID: 0x");
        sb.append(HexDump.toHex(this.field_1_UID));
        sb.append(10);
        if (this.field_2_UID == null) {
            str = "";
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("  UID2: 0x");
            sb2.append(HexDump.toHex(this.field_2_UID));
            sb2.append(10);
            str = sb2.toString();
        }
        sb.append(str);
        sb.append("  Uncompressed Size: ");
        sb.append(HexDump.toHex(this.field_2_cb));
        sb.append(10);
        sb.append("  Bounds: ");
        sb.append(getBounds());
        sb.append(10);
        sb.append("  Size in EMU: ");
        sb.append(getSizeEMU());
        sb.append(10);
        sb.append("  Compressed Size: ");
        sb.append(HexDump.toHex(this.field_5_cbSave));
        sb.append(10);
        sb.append("  Compression: ");
        sb.append(HexDump.toHex(this.field_6_fCompression));
        sb.append(10);
        sb.append("  Filter: ");
        sb.append(HexDump.toHex(this.field_7_fFilter));
        sb.append(10);
        sb.append("  Extra Data:");
        sb.append(10);
        sb.append(extraData);
        if (this.remainingData == null) {
            str2 = null;
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("\n Remaining Data: ");
            sb3.append(HexDump.toHex(this.remainingData, 32));
            str2 = sb3.toString();
        }
        sb.append(str2);
        return sb.toString();
    }

    public short getSignature() {
        switch (getRecordId()) {
            case -4070:
                return 15680;
            case -4069:
                return 8544;
            case -4068:
                return 21536;
            default:
                POILogger pOILogger = log;
                int i = POILogger.WARN;
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown metafile: ");
                sb.append(getRecordId());
                pOILogger.log(i, (Object) sb.toString());
                return 0;
        }
    }
}

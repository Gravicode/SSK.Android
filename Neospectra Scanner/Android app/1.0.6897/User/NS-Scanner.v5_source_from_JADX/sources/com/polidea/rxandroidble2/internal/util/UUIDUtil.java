package com.polidea.rxandroidble2.internal.util;

import android.os.ParcelUuid;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.util.SparseArray;
import com.polidea.rxandroidble2.internal.RxBleLog;
import com.polidea.rxandroidble2.internal.scan.ScanRecordImplCompat;
import com.polidea.rxandroidble2.scan.ScanRecord;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.apache.poi.hssf.record.formula.UnionPtg;

@Deprecated
public class UUIDUtil {
    public static final ParcelUuid BASE_UUID = ParcelUuid.fromString("00000000-0000-1000-8000-00805F9B34FB");
    private static final int DATA_TYPE_FLAGS = 1;
    private static final int DATA_TYPE_LOCAL_NAME_COMPLETE = 9;
    private static final int DATA_TYPE_LOCAL_NAME_SHORT = 8;
    private static final int DATA_TYPE_MANUFACTURER_SPECIFIC_DATA = 255;
    private static final int DATA_TYPE_SERVICE_DATA = 22;
    private static final int DATA_TYPE_SERVICE_UUIDS_128_BIT_COMPLETE = 7;
    private static final int DATA_TYPE_SERVICE_UUIDS_128_BIT_PARTIAL = 6;
    private static final int DATA_TYPE_SERVICE_UUIDS_16_BIT_COMPLETE = 3;
    private static final int DATA_TYPE_SERVICE_UUIDS_16_BIT_PARTIAL = 2;
    private static final int DATA_TYPE_SERVICE_UUIDS_32_BIT_COMPLETE = 5;
    private static final int DATA_TYPE_SERVICE_UUIDS_32_BIT_PARTIAL = 4;
    private static final int DATA_TYPE_TX_POWER_LEVEL = 10;
    private static final String UUID_BASE_FORMAT = "%08x-0000-1000-8000-00805f9b34fb";
    public static final int UUID_BYTES_128_BIT = 16;
    public static final int UUID_BYTES_16_BIT = 2;
    public static final int UUID_BYTES_32_BIT = 4;

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x000f, code lost:
        continue;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<java.util.UUID> extractUUIDs(byte[] r11) {
        /*
            r10 = this;
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            java.nio.ByteBuffer r1 = java.nio.ByteBuffer.wrap(r11)
            java.nio.ByteOrder r2 = java.nio.ByteOrder.LITTLE_ENDIAN
            java.nio.ByteBuffer r1 = r1.order(r2)
        L_0x000f:
            int r2 = r1.remaining()
            r3 = 2
            if (r2 <= r3) goto L_0x0088
            byte r2 = r1.get()
            if (r2 != 0) goto L_0x001d
            goto L_0x0088
        L_0x001d:
            byte r4 = r1.get()
            r5 = 0
            r6 = 1
            switch(r4) {
                case 2: goto L_0x0068;
                case 3: goto L_0x0068;
                case 4: goto L_0x0030;
                case 5: goto L_0x0030;
                case 6: goto L_0x0050;
                case 7: goto L_0x0050;
                default: goto L_0x0026;
            }
        L_0x0026:
            int r3 = r1.position()
            int r3 = r3 + r2
            int r3 = r3 - r6
            r1.position(r3)
            goto L_0x0087
        L_0x0030:
            r3 = 4
            if (r2 < r3) goto L_0x0050
            java.lang.String r3 = "%08x-0000-1000-8000-00805f9b34fb"
            java.lang.Object[] r7 = new java.lang.Object[r6]
            int r8 = r1.getInt()
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)
            r7[r5] = r8
            java.lang.String r3 = java.lang.String.format(r3, r7)
            java.util.UUID r3 = java.util.UUID.fromString(r3)
            r0.add(r3)
            int r3 = r2 + -4
            byte r2 = (byte) r3
            goto L_0x0030
        L_0x0050:
            r3 = 16
            if (r2 < r3) goto L_0x0087
            long r5 = r1.getLong()
            long r7 = r1.getLong()
            java.util.UUID r3 = new java.util.UUID
            r3.<init>(r7, r5)
            r0.add(r3)
            int r3 = r2 + -16
            byte r2 = (byte) r3
            goto L_0x0050
        L_0x0068:
            if (r2 < r3) goto L_0x0087
            java.lang.String r7 = "%08x-0000-1000-8000-00805f9b34fb"
            java.lang.Object[] r8 = new java.lang.Object[r6]
            short r9 = r1.getShort()
            java.lang.Short r9 = java.lang.Short.valueOf(r9)
            r8[r5] = r9
            java.lang.String r7 = java.lang.String.format(r7, r8)
            java.util.UUID r7 = java.util.UUID.fromString(r7)
            r0.add(r7)
            int r7 = r2 + -2
            byte r2 = (byte) r7
            goto L_0x0068
        L_0x0087:
            goto L_0x000f
        L_0x0088:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.polidea.rxandroidble2.internal.util.UUIDUtil.extractUUIDs(byte[]):java.util.List");
    }

    @NonNull
    public Set<UUID> toDistinctSet(@Nullable UUID[] uuids) {
        if (uuids == null) {
            uuids = new UUID[0];
        }
        return new HashSet(Arrays.asList(uuids));
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public ScanRecord parseFromBytes(byte[] scanRecord) {
        HashMap hashMap;
        Throwable th;
        byte[] bArr = scanRecord;
        if (bArr == null) {
            return null;
        }
        int currentPos = 0;
        List<ParcelUuid> arrayList = new ArrayList<>();
        SparseArray sparseArray = new SparseArray();
        HashMap hashMap2 = new HashMap();
        byte b = -1;
        String localName = null;
        byte b2 = -2147483648;
        while (true) {
            hashMap = hashMap2;
            try {
                if (currentPos < bArr.length) {
                    int currentPos2 = currentPos + 1;
                    try {
                        int i = bArr[currentPos] & 255;
                        if (i == 0) {
                            int i2 = currentPos2;
                        } else {
                            int dataLength = i - 1;
                            int currentPos3 = currentPos2 + 1;
                            try {
                                int currentPos4 = bArr[currentPos2] & 255;
                                if (currentPos4 != 22) {
                                    if (currentPos4 != 255) {
                                        switch (currentPos4) {
                                            case 1:
                                                b = bArr[currentPos3] & 255;
                                                break;
                                            case 2:
                                            case 3:
                                                parseServiceUuid(bArr, currentPos3, dataLength, 2, arrayList);
                                                break;
                                            case 4:
                                            case 5:
                                                parseServiceUuid(bArr, currentPos3, dataLength, 4, arrayList);
                                                break;
                                            case 6:
                                            case 7:
                                                parseServiceUuid(bArr, currentPos3, dataLength, 16, arrayList);
                                                break;
                                            case 8:
                                            case 9:
                                                localName = new String(extractBytes(bArr, currentPos3, dataLength));
                                                break;
                                            case 10:
                                                b2 = bArr[currentPos3];
                                                break;
                                        }
                                    } else {
                                        sparseArray.put(((bArr[currentPos3 + 1] & 255) << 8) + (255 & bArr[currentPos3]), extractBytes(bArr, currentPos3 + 2, dataLength - 2));
                                    }
                                    int length = i;
                                } else {
                                    byte b3 = i;
                                    hashMap.put(parseUuidFrom(extractBytes(bArr, currentPos3, 2)), extractBytes(bArr, currentPos3 + 2, dataLength - 2));
                                }
                                currentPos = currentPos3 + dataLength;
                                hashMap2 = hashMap;
                            } catch (Exception e) {
                                th = e;
                                ArrayList arrayList2 = arrayList;
                                int i3 = currentPos3;
                                Throwable th2 = th;
                                StringBuilder sb = new StringBuilder();
                                sb.append("unable to parse scan record: ");
                                sb.append(Arrays.toString(scanRecord));
                                RxBleLog.m20e(th2, sb.toString(), new Object[0]);
                                Throwable th3 = th2;
                                ScanRecordImplCompat scanRecordImplCompat = new ScanRecordImplCompat(null, null, null, -1, Integer.MIN_VALUE, null, bArr);
                                return scanRecordImplCompat;
                            }
                        }
                    } catch (Exception e2) {
                        th = e2;
                        int i4 = currentPos2;
                        List<ParcelUuid> serviceUuids = arrayList;
                        Throwable th22 = th;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("unable to parse scan record: ");
                        sb2.append(Arrays.toString(scanRecord));
                        RxBleLog.m20e(th22, sb2.toString(), new Object[0]);
                        Throwable th32 = th22;
                        ScanRecordImplCompat scanRecordImplCompat2 = new ScanRecordImplCompat(null, null, null, -1, Integer.MIN_VALUE, null, bArr);
                        return scanRecordImplCompat2;
                    }
                }
            } catch (Exception e3) {
                e = e3;
                int i5 = currentPos;
                ArrayList arrayList3 = arrayList;
                th = e;
                Throwable th222 = th;
                StringBuilder sb22 = new StringBuilder();
                sb22.append("unable to parse scan record: ");
                sb22.append(Arrays.toString(scanRecord));
                RxBleLog.m20e(th222, sb22.toString(), new Object[0]);
                Throwable th322 = th222;
                ScanRecordImplCompat scanRecordImplCompat22 = new ScanRecordImplCompat(null, null, null, -1, Integer.MIN_VALUE, null, bArr);
                return scanRecordImplCompat22;
            }
        }
        try {
        } catch (Exception e4) {
            th = e4;
            List<ParcelUuid> serviceUuids2 = arrayList;
            Throwable th2222 = th;
            StringBuilder sb222 = new StringBuilder();
            sb222.append("unable to parse scan record: ");
            sb222.append(Arrays.toString(scanRecord));
            RxBleLog.m20e(th2222, sb222.toString(), new Object[0]);
            Throwable th3222 = th2222;
            ScanRecordImplCompat scanRecordImplCompat222 = new ScanRecordImplCompat(null, null, null, -1, Integer.MIN_VALUE, null, bArr);
            return scanRecordImplCompat222;
        }
        try {
            ScanRecordImplCompat scanRecordImplCompat3 = new ScanRecordImplCompat(arrayList.isEmpty() != 0 ? null : arrayList, sparseArray, hashMap, b, b2, localName, bArr);
            return scanRecordImplCompat3;
        } catch (Exception e5) {
            e = e5;
            th = e;
            Throwable th22222 = th;
            StringBuilder sb2222 = new StringBuilder();
            sb2222.append("unable to parse scan record: ");
            sb2222.append(Arrays.toString(scanRecord));
            RxBleLog.m20e(th22222, sb2222.toString(), new Object[0]);
            Throwable th32222 = th22222;
            ScanRecordImplCompat scanRecordImplCompat2222 = new ScanRecordImplCompat(null, null, null, -1, Integer.MIN_VALUE, null, bArr);
            return scanRecordImplCompat2222;
        }
    }

    private static ParcelUuid parseUuidFrom(byte[] uuidBytes) {
        long shortUuid;
        if (uuidBytes == null) {
            throw new IllegalArgumentException("uuidBytes cannot be null");
        }
        int length = uuidBytes.length;
        if (length != 2 && length != 4 && length != 16) {
            StringBuilder sb = new StringBuilder();
            sb.append("uuidBytes length invalid - ");
            sb.append(length);
            throw new IllegalArgumentException(sb.toString());
        } else if (length == 16) {
            ByteBuffer buf = ByteBuffer.wrap(uuidBytes).order(ByteOrder.LITTLE_ENDIAN);
            return new ParcelUuid(new UUID(buf.getLong(8), buf.getLong(0)));
        } else {
            if (length == 2) {
                shortUuid = ((long) (uuidBytes[0] & 255)) + ((long) ((uuidBytes[1] & 255) << 8));
            } else {
                shortUuid = ((long) ((uuidBytes[3] & 255) << 24)) + ((long) (uuidBytes[0] & 255)) + ((long) ((uuidBytes[1] & 255) << 8)) + ((long) ((uuidBytes[2] & 255) << UnionPtg.sid));
            }
            return new ParcelUuid(new UUID(BASE_UUID.getUuid().getMostSignificantBits() + (shortUuid << 32), BASE_UUID.getUuid().getLeastSignificantBits()));
        }
    }

    private static int parseServiceUuid(byte[] scanRecord, int currentPos, int dataLength, int uuidLength, List<ParcelUuid> serviceUuids) {
        while (dataLength > 0) {
            serviceUuids.add(parseUuidFrom(extractBytes(scanRecord, currentPos, uuidLength)));
            dataLength -= uuidLength;
            currentPos += uuidLength;
        }
        return currentPos;
    }

    private static byte[] extractBytes(byte[] scanRecord, int start, int length) {
        byte[] bytes = new byte[length];
        System.arraycopy(scanRecord, start, bytes, 0, length);
        return bytes;
    }
}

package com.polidea.rxandroidble2.helpers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AdvertisedServiceUUIDExtractor {
    private static final String UUID_BASE = "%08x-0000-1000-8000-00805f9b34fb";

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
            java.util.UUID r7 = java.util.UUID.fromString(r3)
            r0.add(r7)
            int r8 = r2 + -4
            byte r2 = (byte) r8
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
            java.util.UUID r8 = java.util.UUID.fromString(r7)
            r0.add(r8)
            int r9 = r2 + -2
            byte r2 = (byte) r9
            goto L_0x0068
        L_0x0087:
            goto L_0x000f
        L_0x0088:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.polidea.rxandroidble2.helpers.AdvertisedServiceUUIDExtractor.extractUUIDs(byte[]):java.util.List");
    }

    @NonNull
    public Set<UUID> toDistinctSet(@Nullable UUID[] uuids) {
        if (uuids == null) {
            uuids = new UUID[0];
        }
        return new HashSet(Arrays.asList(uuids));
    }
}

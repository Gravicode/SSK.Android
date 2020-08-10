package com.google.android.gms.internal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public final class zzaj {
    private static Comparator<byte[]> zzbw = new zzak();
    private final List<byte[]> zzbs = new LinkedList();
    private final List<byte[]> zzbt = new ArrayList(64);
    private int zzbu = 0;
    private final int zzbv = 4096;

    public zzaj(int i) {
    }

    private final synchronized void zzm() {
        while (this.zzbu > this.zzbv) {
            byte[] bArr = (byte[]) this.zzbs.remove(0);
            this.zzbt.remove(bArr);
            this.zzbu -= bArr.length;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002f, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final synchronized void zza(byte[] r3) {
        /*
            r2 = this;
            monitor-enter(r2)
            if (r3 == 0) goto L_0x002e
            int r0 = r3.length     // Catch:{ all -> 0x002b }
            int r1 = r2.zzbv     // Catch:{ all -> 0x002b }
            if (r0 <= r1) goto L_0x0009
            goto L_0x002e
        L_0x0009:
            java.util.List<byte[]> r0 = r2.zzbs     // Catch:{ all -> 0x002b }
            r0.add(r3)     // Catch:{ all -> 0x002b }
            java.util.List<byte[]> r0 = r2.zzbt     // Catch:{ all -> 0x002b }
            java.util.Comparator<byte[]> r1 = zzbw     // Catch:{ all -> 0x002b }
            int r0 = java.util.Collections.binarySearch(r0, r3, r1)     // Catch:{ all -> 0x002b }
            if (r0 >= 0) goto L_0x001b
            int r0 = -r0
            int r0 = r0 + -1
        L_0x001b:
            java.util.List<byte[]> r1 = r2.zzbt     // Catch:{ all -> 0x002b }
            r1.add(r0, r3)     // Catch:{ all -> 0x002b }
            int r0 = r2.zzbu     // Catch:{ all -> 0x002b }
            int r3 = r3.length     // Catch:{ all -> 0x002b }
            int r0 = r0 + r3
            r2.zzbu = r0     // Catch:{ all -> 0x002b }
            r2.zzm()     // Catch:{ all -> 0x002b }
            monitor-exit(r2)
            return
        L_0x002b:
            r3 = move-exception
            monitor-exit(r2)
            throw r3
        L_0x002e:
            monitor-exit(r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzaj.zza(byte[]):void");
    }

    public final synchronized byte[] zzb(int i) {
        for (int i2 = 0; i2 < this.zzbt.size(); i2++) {
            byte[] bArr = (byte[]) this.zzbt.get(i2);
            if (bArr.length >= i) {
                this.zzbu -= bArr.length;
                this.zzbt.remove(i2);
                this.zzbs.remove(bArr);
                return bArr;
            }
        }
        return new byte[i];
    }
}

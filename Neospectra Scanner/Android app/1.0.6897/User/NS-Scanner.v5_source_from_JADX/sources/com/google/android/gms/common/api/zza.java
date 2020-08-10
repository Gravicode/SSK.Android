package com.google.android.gms.common.api;

final class zza implements com.google.android.gms.common.api.PendingResult.zza {
    private /* synthetic */ Batch zzfmb;

    zza(Batch batch) {
        this.zzfmb = batch;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0067, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void zzr(com.google.android.gms.common.api.Status r5) {
        /*
            r4 = this;
            com.google.android.gms.common.api.Batch r0 = r4.zzfmb
            java.lang.Object r0 = r0.mLock
            monitor-enter(r0)
            com.google.android.gms.common.api.Batch r1 = r4.zzfmb     // Catch:{ all -> 0x0068 }
            boolean r1 = r1.isCanceled()     // Catch:{ all -> 0x0068 }
            if (r1 == 0) goto L_0x0011
            monitor-exit(r0)     // Catch:{ all -> 0x0068 }
            return
        L_0x0011:
            boolean r1 = r5.isCanceled()     // Catch:{ all -> 0x0068 }
            r2 = 1
            if (r1 == 0) goto L_0x001e
            com.google.android.gms.common.api.Batch r5 = r4.zzfmb     // Catch:{ all -> 0x0068 }
            r5.zzflz = true     // Catch:{ all -> 0x0068 }
            goto L_0x0029
        L_0x001e:
            boolean r5 = r5.isSuccess()     // Catch:{ all -> 0x0068 }
            if (r5 != 0) goto L_0x0029
            com.google.android.gms.common.api.Batch r5 = r4.zzfmb     // Catch:{ all -> 0x0068 }
            r5.zzfly = true     // Catch:{ all -> 0x0068 }
        L_0x0029:
            com.google.android.gms.common.api.Batch r5 = r4.zzfmb     // Catch:{ all -> 0x0068 }
            r5.zzflx = r5.zzflx - 1     // Catch:{ all -> 0x0068 }
            com.google.android.gms.common.api.Batch r5 = r4.zzfmb     // Catch:{ all -> 0x0068 }
            int r5 = r5.zzflx     // Catch:{ all -> 0x0068 }
            if (r5 != 0) goto L_0x0066
            com.google.android.gms.common.api.Batch r5 = r4.zzfmb     // Catch:{ all -> 0x0068 }
            boolean r5 = r5.zzflz     // Catch:{ all -> 0x0068 }
            if (r5 == 0) goto L_0x0044
            com.google.android.gms.common.api.Batch r5 = r4.zzfmb     // Catch:{ all -> 0x0068 }
            com.google.android.gms.common.api.zza.super.cancel()     // Catch:{ all -> 0x0068 }
            goto L_0x0066
        L_0x0044:
            com.google.android.gms.common.api.Batch r5 = r4.zzfmb     // Catch:{ all -> 0x0068 }
            boolean r5 = r5.zzfly     // Catch:{ all -> 0x0068 }
            if (r5 == 0) goto L_0x0054
            com.google.android.gms.common.api.Status r5 = new com.google.android.gms.common.api.Status     // Catch:{ all -> 0x0068 }
            r1 = 13
            r5.<init>(r1)     // Catch:{ all -> 0x0068 }
            goto L_0x0056
        L_0x0054:
            com.google.android.gms.common.api.Status r5 = com.google.android.gms.common.api.Status.zzfni     // Catch:{ all -> 0x0068 }
        L_0x0056:
            com.google.android.gms.common.api.Batch r1 = r4.zzfmb     // Catch:{ all -> 0x0068 }
            com.google.android.gms.common.api.BatchResult r2 = new com.google.android.gms.common.api.BatchResult     // Catch:{ all -> 0x0068 }
            com.google.android.gms.common.api.Batch r3 = r4.zzfmb     // Catch:{ all -> 0x0068 }
            com.google.android.gms.common.api.PendingResult[] r3 = r3.zzfma     // Catch:{ all -> 0x0068 }
            r2.<init>(r5, r3)     // Catch:{ all -> 0x0068 }
            r1.setResult(r2)     // Catch:{ all -> 0x0068 }
        L_0x0066:
            monitor-exit(r0)     // Catch:{ all -> 0x0068 }
            return
        L_0x0068:
            r5 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0068 }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.zza.zzr(com.google.android.gms.common.api.Status):void");
    }
}

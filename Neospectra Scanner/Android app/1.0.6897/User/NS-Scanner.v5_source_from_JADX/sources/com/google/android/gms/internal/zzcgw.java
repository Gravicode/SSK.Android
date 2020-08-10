package com.google.android.gms.internal;

import com.google.android.gms.common.internal.zzbq;

final class zzcgw {
    final String mAppId;
    final String mName;
    final long zzizk;
    final long zzizl;
    final long zzizm;
    final long zzizn;
    final Long zzizo;
    final Long zzizp;
    final Boolean zzizq;

    zzcgw(String str, String str2, long j, long j2, long j3, long j4, Long l, Long l2, Boolean bool) {
        long j5 = j;
        long j6 = j2;
        long j7 = j4;
        zzbq.zzgm(str);
        zzbq.zzgm(str2);
        boolean z = false;
        zzbq.checkArgument(j5 >= 0);
        zzbq.checkArgument(j6 >= 0);
        if (j7 >= 0) {
            z = true;
        }
        zzbq.checkArgument(z);
        this.mAppId = str;
        this.mName = str2;
        this.zzizk = j5;
        this.zzizl = j6;
        this.zzizm = j3;
        this.zzizn = j7;
        this.zzizo = l;
        this.zzizp = l2;
        this.zzizq = bool;
    }

    /* access modifiers changed from: 0000 */
    public final zzcgw zza(Long l, Long l2, Boolean bool) {
        zzcgw zzcgw = new zzcgw(this.mAppId, this.mName, this.zzizk, this.zzizl, this.zzizm, this.zzizn, l, l2, (bool == null || bool.booleanValue()) ? bool : null);
        return zzcgw;
    }

    /* access modifiers changed from: 0000 */
    public final zzcgw zzayw() {
        zzcgw zzcgw = new zzcgw(this.mAppId, this.mName, this.zzizk + 1, 1 + this.zzizl, this.zzizm, this.zzizn, this.zzizo, this.zzizp, this.zzizq);
        return zzcgw;
    }

    /* access modifiers changed from: 0000 */
    public final zzcgw zzbb(long j) {
        zzcgw zzcgw = new zzcgw(this.mAppId, this.mName, this.zzizk, this.zzizl, j, this.zzizn, this.zzizo, this.zzizp, this.zzizq);
        return zzcgw;
    }

    /* access modifiers changed from: 0000 */
    public final zzcgw zzbc(long j) {
        zzcgw zzcgw = new zzcgw(this.mAppId, this.mName, this.zzizk, this.zzizl, this.zzizm, j, this.zzizo, this.zzizp, this.zzizq);
        return zzcgw;
    }
}

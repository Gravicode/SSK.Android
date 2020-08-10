package com.google.android.gms.internal;

import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.gms.common.internal.zzbq;
import java.util.Iterator;
import org.apache.commons.math3.geometry.VectorFormat;

public final class zzcgv {
    final String mAppId;
    final String mName;
    private String mOrigin;
    final long zzfij;
    final long zzizi;
    final zzcgx zzizj;

    zzcgv(zzcim zzcim, String str, String str2, String str3, long j, long j2, Bundle bundle) {
        zzbq.zzgm(str2);
        zzbq.zzgm(str3);
        this.mAppId = str2;
        this.mName = str3;
        if (TextUtils.isEmpty(str)) {
            str = null;
        }
        this.mOrigin = str;
        this.zzfij = j;
        this.zzizi = j2;
        if (this.zzizi != 0 && this.zzizi > this.zzfij) {
            zzcim.zzawy().zzazf().zzj("Event created with reverse previous/current timestamps. appId", zzchm.zzjk(str2));
        }
        this.zzizj = zza(zzcim, bundle);
    }

    private zzcgv(zzcim zzcim, String str, String str2, String str3, long j, long j2, zzcgx zzcgx) {
        zzbq.zzgm(str2);
        zzbq.zzgm(str3);
        zzbq.checkNotNull(zzcgx);
        this.mAppId = str2;
        this.mName = str3;
        if (TextUtils.isEmpty(str)) {
            str = null;
        }
        this.mOrigin = str;
        this.zzfij = j;
        this.zzizi = j2;
        if (this.zzizi != 0 && this.zzizi > this.zzfij) {
            zzcim.zzawy().zzazf().zzj("Event created with reverse previous/current timestamps. appId", zzchm.zzjk(str2));
        }
        this.zzizj = zzcgx;
    }

    private static zzcgx zza(zzcim zzcim, Bundle bundle) {
        if (bundle == null || bundle.isEmpty()) {
            return new zzcgx(new Bundle());
        }
        Bundle bundle2 = new Bundle(bundle);
        Iterator it = bundle2.keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (str == null) {
                zzcim.zzawy().zzazd().log("Param name can't be null");
            } else {
                Object zzk = zzcim.zzawu().zzk(str, bundle2.get(str));
                if (zzk == null) {
                    zzcim.zzawy().zzazf().zzj("Param value can't be null", zzcim.zzawt().zzji(str));
                } else {
                    zzcim.zzawu().zza(bundle2, str, zzk);
                }
            }
            it.remove();
        }
        return new zzcgx(bundle2);
    }

    public final String toString() {
        String str = this.mAppId;
        String str2 = this.mName;
        String valueOf = String.valueOf(this.zzizj);
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 33 + String.valueOf(str2).length() + String.valueOf(valueOf).length());
        sb.append("Event{appId='");
        sb.append(str);
        sb.append("', name='");
        sb.append(str2);
        sb.append("', params=");
        sb.append(valueOf);
        sb.append(VectorFormat.DEFAULT_SUFFIX);
        return sb.toString();
    }

    /* access modifiers changed from: 0000 */
    public final zzcgv zza(zzcim zzcim, long j) {
        zzcgv zzcgv = new zzcgv(zzcim, this.mOrigin, this.mAppId, this.mName, this.zzfij, j, this.zzizj);
        return zzcgv;
    }
}

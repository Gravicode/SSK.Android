package com.google.android.gms.internal;

import android.os.Binder;
import android.support.annotation.BinderThread;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.common.util.zzx;
import com.google.android.gms.common.zzp;
import com.google.android.gms.common.zzq;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public final class zzcir extends zzchf {
    /* access modifiers changed from: private */
    public final zzcim zziwf;
    private Boolean zzjgl;
    @Nullable
    private String zzjgm;

    public zzcir(zzcim zzcim) {
        this(zzcim, null);
    }

    private zzcir(zzcim zzcim, @Nullable String str) {
        zzbq.checkNotNull(zzcim);
        this.zziwf = zzcim;
        this.zzjgm = null;
    }

    @BinderThread
    private final void zzb(zzcgi zzcgi, boolean z) {
        zzbq.checkNotNull(zzcgi);
        zzf(zzcgi.packageName, false);
        this.zziwf.zzawu().zzkg(zzcgi.zzixs);
    }

    @BinderThread
    private final void zzf(String str, boolean z) {
        boolean z2;
        if (TextUtils.isEmpty(str)) {
            this.zziwf.zzawy().zzazd().log("Measurement Service called without app package");
            throw new SecurityException("Measurement Service called without app package");
        }
        if (z) {
            try {
                if (this.zzjgl == null) {
                    if (!"com.google.android.gms".equals(this.zzjgm) && !zzx.zzf(this.zziwf.getContext(), Binder.getCallingUid())) {
                        if (!zzq.zzci(this.zziwf.getContext()).zzbq(Binder.getCallingUid())) {
                            z2 = false;
                            this.zzjgl = Boolean.valueOf(z2);
                        }
                    }
                    z2 = true;
                    this.zzjgl = Boolean.valueOf(z2);
                }
                if (this.zzjgl.booleanValue()) {
                    return;
                }
            } catch (SecurityException e) {
                this.zziwf.zzawy().zzazd().zzj("Measurement Service called with invalid calling package. appId", zzchm.zzjk(str));
                throw e;
            }
        }
        if (this.zzjgm == null && zzp.zzb(this.zziwf.getContext(), Binder.getCallingUid(), str)) {
            this.zzjgm = str;
        }
        if (!str.equals(this.zzjgm)) {
            throw new SecurityException(String.format("Unknown calling package name '%s'.", new Object[]{str}));
        }
    }

    @BinderThread
    public final List<zzcln> zza(zzcgi zzcgi, boolean z) {
        zzb(zzcgi, false);
        try {
            List<zzclp> list = (List) this.zziwf.zzawx().zzc((Callable<V>) new zzcjh<V>(this, zzcgi)).get();
            ArrayList arrayList = new ArrayList(list.size());
            for (zzclp zzclp : list) {
                if (z || !zzclq.zzki(zzclp.mName)) {
                    arrayList.add(new zzcln(zzclp));
                }
            }
            return arrayList;
        } catch (InterruptedException | ExecutionException e) {
            this.zziwf.zzawy().zzazd().zze("Failed to get user attributes. appId", zzchm.zzjk(zzcgi.packageName), e);
            return null;
        }
    }

    @BinderThread
    public final List<zzcgl> zza(String str, String str2, zzcgi zzcgi) {
        zzb(zzcgi, false);
        try {
            return (List) this.zziwf.zzawx().zzc((Callable<V>) new zzciz<V>(this, zzcgi, str, str2)).get();
        } catch (InterruptedException | ExecutionException e) {
            this.zziwf.zzawy().zzazd().zzj("Failed to get conditional user properties", e);
            return Collections.emptyList();
        }
    }

    @BinderThread
    public final List<zzcln> zza(String str, String str2, String str3, boolean z) {
        zzf(str, true);
        try {
            List<zzclp> list = (List) this.zziwf.zzawx().zzc((Callable<V>) new zzciy<V>(this, str, str2, str3)).get();
            ArrayList arrayList = new ArrayList(list.size());
            for (zzclp zzclp : list) {
                if (z || !zzclq.zzki(zzclp.mName)) {
                    arrayList.add(new zzcln(zzclp));
                }
            }
            return arrayList;
        } catch (InterruptedException | ExecutionException e) {
            this.zziwf.zzawy().zzazd().zze("Failed to get user attributes. appId", zzchm.zzjk(str), e);
            return Collections.emptyList();
        }
    }

    @BinderThread
    public final List<zzcln> zza(String str, String str2, boolean z, zzcgi zzcgi) {
        zzb(zzcgi, false);
        try {
            List<zzclp> list = (List) this.zziwf.zzawx().zzc((Callable<V>) new zzcix<V>(this, zzcgi, str, str2)).get();
            ArrayList arrayList = new ArrayList(list.size());
            for (zzclp zzclp : list) {
                if (z || !zzclq.zzki(zzclp.mName)) {
                    arrayList.add(new zzcln(zzclp));
                }
            }
            return arrayList;
        } catch (InterruptedException | ExecutionException e) {
            this.zziwf.zzawy().zzazd().zze("Failed to get user attributes. appId", zzchm.zzjk(zzcgi.packageName), e);
            return Collections.emptyList();
        }
    }

    @BinderThread
    public final void zza(long j, String str, String str2, String str3) {
        zzcih zzawx = this.zziwf.zzawx();
        zzcjj zzcjj = new zzcjj(this, str2, str3, str, j);
        zzawx.zzg(zzcjj);
    }

    @BinderThread
    public final void zza(zzcgi zzcgi) {
        zzb(zzcgi, false);
        zzcji zzcji = new zzcji(this, zzcgi);
        if (this.zziwf.zzawx().zzazs()) {
            zzcji.run();
        } else {
            this.zziwf.zzawx().zzg(zzcji);
        }
    }

    @BinderThread
    public final void zza(zzcgl zzcgl, zzcgi zzcgi) {
        zzcih zzawx;
        Runnable zzciu;
        zzbq.checkNotNull(zzcgl);
        zzbq.checkNotNull(zzcgl.zziyg);
        zzb(zzcgi, false);
        zzcgl zzcgl2 = new zzcgl(zzcgl);
        zzcgl2.packageName = zzcgi.packageName;
        if (zzcgl.zziyg.getValue() == null) {
            zzawx = this.zziwf.zzawx();
            zzciu = new zzcit(this, zzcgl2, zzcgi);
        } else {
            zzawx = this.zziwf.zzawx();
            zzciu = new zzciu(this, zzcgl2, zzcgi);
        }
        zzawx.zzg(zzciu);
    }

    @BinderThread
    public final void zza(zzcha zzcha, zzcgi zzcgi) {
        zzbq.checkNotNull(zzcha);
        zzb(zzcgi, false);
        this.zziwf.zzawx().zzg(new zzcjc(this, zzcha, zzcgi));
    }

    @BinderThread
    public final void zza(zzcha zzcha, String str, String str2) {
        zzbq.checkNotNull(zzcha);
        zzbq.zzgm(str);
        zzf(str, true);
        this.zziwf.zzawx().zzg(new zzcjd(this, zzcha, str));
    }

    @BinderThread
    public final void zza(zzcln zzcln, zzcgi zzcgi) {
        zzcih zzawx;
        Runnable zzcjg;
        zzbq.checkNotNull(zzcln);
        zzb(zzcgi, false);
        if (zzcln.getValue() == null) {
            zzawx = this.zziwf.zzawx();
            zzcjg = new zzcjf(this, zzcln, zzcgi);
        } else {
            zzawx = this.zziwf.zzawx();
            zzcjg = new zzcjg(this, zzcln, zzcgi);
        }
        zzawx.zzg(zzcjg);
    }

    @BinderThread
    public final byte[] zza(zzcha zzcha, String str) {
        zzbq.zzgm(str);
        zzbq.checkNotNull(zzcha);
        zzf(str, true);
        this.zziwf.zzawy().zzazi().zzj("Log and bundle. event", this.zziwf.zzawt().zzjh(zzcha.name));
        long nanoTime = this.zziwf.zzws().nanoTime() / 1000000;
        try {
            byte[] bArr = (byte[]) this.zziwf.zzawx().zzd((Callable<V>) new zzcje<V>(this, zzcha, str)).get();
            if (bArr == null) {
                this.zziwf.zzawy().zzazd().zzj("Log and bundle returned null. appId", zzchm.zzjk(str));
                bArr = new byte[0];
            }
            this.zziwf.zzawy().zzazi().zzd("Log and bundle processed. event, size, time_ms", this.zziwf.zzawt().zzjh(zzcha.name), Integer.valueOf(bArr.length), Long.valueOf((this.zziwf.zzws().nanoTime() / 1000000) - nanoTime));
            return bArr;
        } catch (InterruptedException | ExecutionException e) {
            this.zziwf.zzawy().zzazd().zzd("Failed to log and bundle. appId, event, error", zzchm.zzjk(str), this.zziwf.zzawt().zzjh(zzcha.name), e);
            return null;
        }
    }

    @BinderThread
    public final void zzb(zzcgi zzcgi) {
        zzb(zzcgi, false);
        this.zziwf.zzawx().zzg(new zzcis(this, zzcgi));
    }

    @BinderThread
    public final void zzb(zzcgl zzcgl) {
        zzcih zzawx;
        Runnable zzciw;
        zzbq.checkNotNull(zzcgl);
        zzbq.checkNotNull(zzcgl.zziyg);
        zzf(zzcgl.packageName, true);
        zzcgl zzcgl2 = new zzcgl(zzcgl);
        if (zzcgl.zziyg.getValue() == null) {
            zzawx = this.zziwf.zzawx();
            zzciw = new zzciv(this, zzcgl2);
        } else {
            zzawx = this.zziwf.zzawx();
            zzciw = new zzciw(this, zzcgl2);
        }
        zzawx.zzg(zzciw);
    }

    @BinderThread
    public final String zzc(zzcgi zzcgi) {
        zzb(zzcgi, false);
        return this.zziwf.zzjx(zzcgi.packageName);
    }

    @BinderThread
    public final void zzd(zzcgi zzcgi) {
        zzf(zzcgi.packageName, false);
        this.zziwf.zzawx().zzg(new zzcjb(this, zzcgi));
    }

    @BinderThread
    public final List<zzcgl> zzj(String str, String str2, String str3) {
        zzf(str, true);
        try {
            return (List) this.zziwf.zzawx().zzc((Callable<V>) new zzcja<V>(this, str, str2, str3)).get();
        } catch (InterruptedException | ExecutionException e) {
            this.zziwf.zzawy().zzazd().zzj("Failed to get conditional user properties", e);
            return Collections.emptyList();
        }
    }
}

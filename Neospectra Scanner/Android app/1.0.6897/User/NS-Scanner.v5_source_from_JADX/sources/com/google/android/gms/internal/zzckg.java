package com.google.android.gms.internal;

import android.content.ComponentName;
import android.content.Context;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.common.stats.zza;
import com.google.android.gms.common.util.zzd;
import com.google.android.gms.measurement.AppMeasurement.zzb;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public final class zzckg extends zzcjl {
    /* access modifiers changed from: private */
    public final zzcku zzjic;
    /* access modifiers changed from: private */
    public zzche zzjid;
    private volatile Boolean zzjie;
    private final zzcgs zzjif;
    private final zzclk zzjig;
    private final List<Runnable> zzjih = new ArrayList();
    private final zzcgs zzjii;

    protected zzckg(zzcim zzcim) {
        super(zzcim);
        this.zzjig = new zzclk(zzcim.zzws());
        this.zzjic = new zzcku(this);
        this.zzjif = new zzckh(this, zzcim);
        this.zzjii = new zzckm(this, zzcim);
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void onServiceDisconnected(ComponentName componentName) {
        zzve();
        if (this.zzjid != null) {
            this.zzjid = null;
            zzawy().zzazj().zzj("Disconnected from device MeasurementService", componentName);
            zzve();
            zzyc();
        }
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zzbat() {
        zzve();
        zzawy().zzazj().zzj("Processing queued up service tasks", Integer.valueOf(this.zzjih.size()));
        for (Runnable run : this.zzjih) {
            try {
                run.run();
            } catch (Throwable th) {
                zzawy().zzazd().zzj("Task exception while flushing queue", th);
            }
        }
        this.zzjih.clear();
        this.zzjii.cancel();
    }

    @Nullable
    @WorkerThread
    private final zzcgi zzbr(boolean z) {
        return zzawn().zzjg(z ? zzawy().zzazk() : null);
    }

    @WorkerThread
    private final void zzj(Runnable runnable) throws IllegalStateException {
        zzve();
        if (isConnected()) {
            runnable.run();
        } else if (((long) this.zzjih.size()) >= 1000) {
            zzawy().zzazd().log("Discarding data. Max runnable queue size reached");
        } else {
            this.zzjih.add(runnable);
            this.zzjii.zzs(60000);
            zzyc();
        }
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zzxr() {
        zzve();
        this.zzjig.start();
        this.zzjif.zzs(((Long) zzchc.zzjbj.get()).longValue());
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zzxs() {
        zzve();
        if (isConnected()) {
            zzawy().zzazj().log("Inactivity, disconnecting from the service");
            disconnect();
        }
    }

    @WorkerThread
    public final void disconnect() {
        zzve();
        zzxf();
        try {
            zza.zzamc();
            getContext().unbindService(this.zzjic);
        } catch (IllegalArgumentException | IllegalStateException e) {
        }
        this.zzjid = null;
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    @WorkerThread
    public final boolean isConnected() {
        zzve();
        zzxf();
        return this.zzjid != null;
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void resetAnalyticsData() {
        zzve();
        zzxf();
        zzcgi zzbr = zzbr(false);
        zzawr().resetAnalyticsData();
        zzj(new zzcki(this, zzbr));
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void zza(zzche zzche) {
        zzve();
        zzbq.checkNotNull(zzche);
        this.zzjid = zzche;
        zzxr();
        zzbat();
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zza(zzche zzche, zzbfm zzbfm, zzcgi zzcgi) {
        int i;
        zzcho zzazd;
        String str;
        zzve();
        zzxf();
        int i2 = 0;
        int i3 = 100;
        while (i2 < 1001 && i3 == 100) {
            ArrayList arrayList = new ArrayList();
            List zzeb = zzawr().zzeb(100);
            if (zzeb != null) {
                arrayList.addAll(zzeb);
                i = zzeb.size();
            } else {
                i = 0;
            }
            if (zzbfm != null && i < 100) {
                arrayList.add(zzbfm);
            }
            ArrayList arrayList2 = arrayList;
            int size = arrayList2.size();
            int i4 = 0;
            while (i4 < size) {
                Object obj = arrayList2.get(i4);
                i4++;
                zzbfm zzbfm2 = (zzbfm) obj;
                if (zzbfm2 instanceof zzcha) {
                    try {
                        zzche.zza((zzcha) zzbfm2, zzcgi);
                    } catch (RemoteException e) {
                        e = e;
                        zzazd = zzawy().zzazd();
                        str = "Failed to send event to the service";
                    }
                } else if (zzbfm2 instanceof zzcln) {
                    try {
                        zzche.zza((zzcln) zzbfm2, zzcgi);
                    } catch (RemoteException e2) {
                        e = e2;
                        zzazd = zzawy().zzazd();
                        str = "Failed to send attribute to the service";
                    }
                } else if (zzbfm2 instanceof zzcgl) {
                    try {
                        zzche.zza((zzcgl) zzbfm2, zzcgi);
                    } catch (RemoteException e3) {
                        e = e3;
                        zzazd = zzawy().zzazd();
                        str = "Failed to send conditional property to the service";
                    }
                } else {
                    zzawy().zzazd().log("Discarding data. Unrecognized parcel type.");
                }
            }
            i2++;
            i3 = i;
        }
        return;
        zzazd.zzj(str, e);
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void zza(zzb zzb) {
        zzve();
        zzxf();
        zzj(new zzckl(this, zzb));
    }

    @WorkerThread
    public final void zza(AtomicReference<String> atomicReference) {
        zzve();
        zzxf();
        zzj(new zzckj(this, atomicReference, zzbr(false)));
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void zza(AtomicReference<List<zzcgl>> atomicReference, String str, String str2, String str3) {
        zzve();
        zzxf();
        zzckq zzckq = new zzckq(this, atomicReference, str, str2, str3, zzbr(false));
        zzj(zzckq);
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void zza(AtomicReference<List<zzcln>> atomicReference, String str, String str2, String str3, boolean z) {
        zzve();
        zzxf();
        zzckr zzckr = new zzckr(this, atomicReference, str, str2, str3, z, zzbr(false));
        zzj(zzckr);
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void zza(AtomicReference<List<zzcln>> atomicReference, boolean z) {
        zzve();
        zzxf();
        zzj(new zzckt(this, atomicReference, zzbr(false), z));
    }

    public final /* bridge */ /* synthetic */ void zzawi() {
        super.zzawi();
    }

    public final /* bridge */ /* synthetic */ void zzawj() {
        super.zzawj();
    }

    public final /* bridge */ /* synthetic */ zzcgd zzawk() {
        return super.zzawk();
    }

    public final /* bridge */ /* synthetic */ zzcgk zzawl() {
        return super.zzawl();
    }

    public final /* bridge */ /* synthetic */ zzcjn zzawm() {
        return super.zzawm();
    }

    public final /* bridge */ /* synthetic */ zzchh zzawn() {
        return super.zzawn();
    }

    public final /* bridge */ /* synthetic */ zzcgu zzawo() {
        return super.zzawo();
    }

    public final /* bridge */ /* synthetic */ zzckg zzawp() {
        return super.zzawp();
    }

    public final /* bridge */ /* synthetic */ zzckc zzawq() {
        return super.zzawq();
    }

    public final /* bridge */ /* synthetic */ zzchi zzawr() {
        return super.zzawr();
    }

    public final /* bridge */ /* synthetic */ zzcgo zzaws() {
        return super.zzaws();
    }

    public final /* bridge */ /* synthetic */ zzchk zzawt() {
        return super.zzawt();
    }

    public final /* bridge */ /* synthetic */ zzclq zzawu() {
        return super.zzawu();
    }

    public final /* bridge */ /* synthetic */ zzcig zzawv() {
        return super.zzawv();
    }

    public final /* bridge */ /* synthetic */ zzclf zzaww() {
        return super.zzaww();
    }

    public final /* bridge */ /* synthetic */ zzcih zzawx() {
        return super.zzawx();
    }

    public final /* bridge */ /* synthetic */ zzchm zzawy() {
        return super.zzawy();
    }

    public final /* bridge */ /* synthetic */ zzchx zzawz() {
        return super.zzawz();
    }

    public final /* bridge */ /* synthetic */ zzcgn zzaxa() {
        return super.zzaxa();
    }

    /* access modifiers changed from: protected */
    public final boolean zzaxz() {
        return false;
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void zzb(zzcln zzcln) {
        zzve();
        zzxf();
        zzj(new zzcks(this, zzawr().zza(zzcln), zzcln, zzbr(true)));
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void zzbaq() {
        zzve();
        zzxf();
        zzj(new zzckn(this, zzbr(true)));
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void zzbar() {
        zzve();
        zzxf();
        zzj(new zzckk(this, zzbr(true)));
    }

    /* access modifiers changed from: 0000 */
    public final Boolean zzbas() {
        return this.zzjie;
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void zzc(zzcha zzcha, String str) {
        zzbq.checkNotNull(zzcha);
        zzve();
        zzxf();
        zzcko zzcko = new zzcko(this, true, zzawr().zza(zzcha), zzcha, zzbr(true), str);
        zzj(zzcko);
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void zzf(zzcgl zzcgl) {
        zzbq.checkNotNull(zzcgl);
        zzve();
        zzxf();
        zzckp zzckp = new zzckp(this, true, zzawr().zzc(zzcgl), new zzcgl(zzcgl), zzbr(true), zzcgl);
        zzj(zzckp);
    }

    public final /* bridge */ /* synthetic */ void zzve() {
        super.zzve();
    }

    public final /* bridge */ /* synthetic */ zzd zzws() {
        return super.zzws();
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00ca, code lost:
        r0 = true;
     */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00f3  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void zzyc() {
        /*
            r6 = this;
            r6.zzve()
            r6.zzxf()
            boolean r0 = r6.isConnected()
            if (r0 == 0) goto L_0x000d
            return
        L_0x000d:
            java.lang.Boolean r0 = r6.zzjie
            r1 = 0
            r2 = 1
            if (r0 != 0) goto L_0x0100
            r6.zzve()
            r6.zzxf()
            com.google.android.gms.internal.zzchx r0 = r6.zzawz()
            java.lang.Boolean r0 = r0.zzazo()
            if (r0 == 0) goto L_0x002c
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x002c
            r3 = 1
            goto L_0x00fa
        L_0x002c:
            com.google.android.gms.internal.zzchh r0 = r6.zzawn()
            int r0 = r0.zzazb()
            if (r0 != r2) goto L_0x003a
        L_0x0036:
            r0 = 1
            r3 = 1
            goto L_0x00f1
        L_0x003a:
            com.google.android.gms.internal.zzchm r0 = r6.zzawy()
            com.google.android.gms.internal.zzcho r0 = r0.zzazj()
            java.lang.String r3 = "Checking service availability"
            r0.log(r3)
            com.google.android.gms.internal.zzclq r0 = r6.zzawu()
            com.google.android.gms.common.zzf r3 = com.google.android.gms.common.zzf.zzafy()
            android.content.Context r0 = r0.getContext()
            int r0 = r3.isGooglePlayServicesAvailable(r0)
            r3 = 9
            if (r0 == r3) goto L_0x00e6
            r3 = 18
            if (r0 == r3) goto L_0x00db
            switch(r0) {
                case 0: goto L_0x00cc;
                case 1: goto L_0x00bd;
                case 2: goto L_0x0085;
                case 3: goto L_0x0077;
                default: goto L_0x0062;
            }
        L_0x0062:
            com.google.android.gms.internal.zzchm r3 = r6.zzawy()
            com.google.android.gms.internal.zzcho r3 = r3.zzazf()
            java.lang.String r4 = "Unexpected service status"
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            r3.zzj(r4, r0)
        L_0x0073:
            r0 = 0
        L_0x0074:
            r3 = 0
            goto L_0x00f1
        L_0x0077:
            com.google.android.gms.internal.zzchm r0 = r6.zzawy()
            com.google.android.gms.internal.zzcho r0 = r0.zzazf()
            java.lang.String r3 = "Service disabled"
        L_0x0081:
            r0.log(r3)
            goto L_0x0073
        L_0x0085:
            com.google.android.gms.internal.zzchm r0 = r6.zzawy()
            com.google.android.gms.internal.zzcho r0 = r0.zzazi()
            java.lang.String r3 = "Service container out of date"
            r0.log(r3)
            com.google.android.gms.internal.zzclq r0 = r6.zzawu()
            com.google.android.gms.common.zzf.zzafy()
            android.content.Context r0 = r0.getContext()
            int r0 = com.google.android.gms.common.zzf.zzcf(r0)
            r3 = 11400(0x2c88, float:1.5975E-41)
            if (r0 >= r3) goto L_0x00a6
            goto L_0x00ca
        L_0x00a6:
            com.google.android.gms.internal.zzchx r0 = r6.zzawz()
            java.lang.Boolean r0 = r0.zzazo()
            if (r0 == 0) goto L_0x00b9
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x00b7
            goto L_0x00b9
        L_0x00b7:
            r0 = 0
            goto L_0x00ba
        L_0x00b9:
            r0 = 1
        L_0x00ba:
            r3 = r0
            r0 = 0
            goto L_0x00f1
        L_0x00bd:
            com.google.android.gms.internal.zzchm r0 = r6.zzawy()
            com.google.android.gms.internal.zzcho r0 = r0.zzazj()
            java.lang.String r3 = "Service missing"
            r0.log(r3)
        L_0x00ca:
            r0 = 1
            goto L_0x0074
        L_0x00cc:
            com.google.android.gms.internal.zzchm r0 = r6.zzawy()
            com.google.android.gms.internal.zzcho r0 = r0.zzazj()
            java.lang.String r3 = "Service available"
        L_0x00d6:
            r0.log(r3)
            goto L_0x0036
        L_0x00db:
            com.google.android.gms.internal.zzchm r0 = r6.zzawy()
            com.google.android.gms.internal.zzcho r0 = r0.zzazf()
            java.lang.String r3 = "Service updating"
            goto L_0x00d6
        L_0x00e6:
            com.google.android.gms.internal.zzchm r0 = r6.zzawy()
            com.google.android.gms.internal.zzcho r0 = r0.zzazf()
            java.lang.String r3 = "Service invalid"
            goto L_0x0081
        L_0x00f1:
            if (r0 == 0) goto L_0x00fa
            com.google.android.gms.internal.zzchx r0 = r6.zzawz()
            r0.zzbm(r3)
        L_0x00fa:
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r3)
            r6.zzjie = r0
        L_0x0100:
            java.lang.Boolean r0 = r6.zzjie
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x010e
            com.google.android.gms.internal.zzcku r0 = r6.zzjic
            r0.zzbau()
            return
        L_0x010e:
            android.content.Context r0 = r6.getContext()
            android.content.pm.PackageManager r0 = r0.getPackageManager()
            android.content.Intent r3 = new android.content.Intent
            r3.<init>()
            android.content.Context r4 = r6.getContext()
            java.lang.String r5 = "com.google.android.gms.measurement.AppMeasurementService"
            android.content.Intent r3 = r3.setClassName(r4, r5)
            r4 = 65536(0x10000, float:9.18355E-41)
            java.util.List r0 = r0.queryIntentServices(r3, r4)
            if (r0 == 0) goto L_0x0134
            int r0 = r0.size()
            if (r0 <= 0) goto L_0x0134
            r1 = 1
        L_0x0134:
            if (r1 == 0) goto L_0x0151
            android.content.Intent r0 = new android.content.Intent
            java.lang.String r1 = "com.google.android.gms.measurement.START"
            r0.<init>(r1)
            android.content.ComponentName r1 = new android.content.ComponentName
            android.content.Context r2 = r6.getContext()
            java.lang.String r3 = "com.google.android.gms.measurement.AppMeasurementService"
            r1.<init>(r2, r3)
            r0.setComponent(r1)
            com.google.android.gms.internal.zzcku r1 = r6.zzjic
            r1.zzn(r0)
            return
        L_0x0151:
            com.google.android.gms.internal.zzchm r0 = r6.zzawy()
            com.google.android.gms.internal.zzcho r0 = r0.zzazd()
            java.lang.String r1 = "Unable to use remote or local measurement implementation. Please register the AppMeasurementService service in the app manifest"
            r0.log(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzckg.zzyc():void");
    }
}

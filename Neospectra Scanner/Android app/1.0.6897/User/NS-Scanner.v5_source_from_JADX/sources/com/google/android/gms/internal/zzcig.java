package com.google.android.gms.internal;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.support.annotation.WorkerThread;
import android.support.p001v4.util.ArrayMap;
import android.text.TextUtils;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.common.util.zzd;
import com.google.android.gms.measurement.AppMeasurement.Event;
import com.google.android.gms.measurement.AppMeasurement.Param;
import com.google.android.gms.measurement.AppMeasurement.UserProperty;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.io.IOException;
import java.util.Map;

public final class zzcig extends zzcjl {
    private static int zzjdx = 65535;
    private static int zzjdy = 2;
    private final Map<String, Map<String, String>> zzjdz = new ArrayMap();
    private final Map<String, Map<String, Boolean>> zzjea = new ArrayMap();
    private final Map<String, Map<String, Boolean>> zzjeb = new ArrayMap();
    private final Map<String, zzcly> zzjec = new ArrayMap();
    private final Map<String, Map<String, Integer>> zzjed = new ArrayMap();
    private final Map<String, String> zzjee = new ArrayMap();

    zzcig(zzcim zzcim) {
        super(zzcim);
    }

    private static Map<String, String> zza(zzcly zzcly) {
        zzclz[] zzclzArr;
        ArrayMap arrayMap = new ArrayMap();
        if (!(zzcly == null || zzcly.zzjky == null)) {
            for (zzclz zzclz : zzcly.zzjky) {
                if (zzclz != null) {
                    arrayMap.put(zzclz.key, zzclz.value);
                }
            }
        }
        return arrayMap;
    }

    private final void zza(String str, zzcly zzcly) {
        zzclx[] zzclxArr;
        ArrayMap arrayMap = new ArrayMap();
        ArrayMap arrayMap2 = new ArrayMap();
        ArrayMap arrayMap3 = new ArrayMap();
        if (!(zzcly == null || zzcly.zzjkz == null)) {
            for (zzclx zzclx : zzcly.zzjkz) {
                if (TextUtils.isEmpty(zzclx.name)) {
                    zzawy().zzazf().log("EventConfig contained null event name");
                } else {
                    String zziq = Event.zziq(zzclx.name);
                    if (!TextUtils.isEmpty(zziq)) {
                        zzclx.name = zziq;
                    }
                    arrayMap.put(zzclx.name, zzclx.zzjkt);
                    arrayMap2.put(zzclx.name, zzclx.zzjku);
                    if (zzclx.zzjkv != null) {
                        if (zzclx.zzjkv.intValue() < zzjdy || zzclx.zzjkv.intValue() > zzjdx) {
                            zzawy().zzazf().zze("Invalid sampling rate. Event name, sample rate", zzclx.name, zzclx.zzjkv);
                        } else {
                            arrayMap3.put(zzclx.name, zzclx.zzjkv);
                        }
                    }
                }
            }
        }
        this.zzjea.put(str, arrayMap);
        this.zzjeb.put(str, arrayMap2);
        this.zzjed.put(str, arrayMap3);
    }

    @WorkerThread
    private final zzcly zzc(String str, byte[] bArr) {
        if (bArr == null) {
            return new zzcly();
        }
        zzfjj zzn = zzfjj.zzn(bArr, 0, bArr.length);
        zzcly zzcly = new zzcly();
        try {
            zzcly.zza(zzn);
            zzawy().zzazj().zze("Parsed config. version, gmp_app_id", zzcly.zzjkw, zzcly.zzixs);
            return zzcly;
        } catch (IOException e) {
            zzawy().zzazf().zze("Unable to merge remote config. appId", zzchm.zzjk(str), e);
            return new zzcly();
        }
    }

    @WorkerThread
    private final void zzjr(String str) {
        zzxf();
        zzve();
        zzbq.zzgm(str);
        if (this.zzjec.get(str) == null) {
            byte[] zzjd = zzaws().zzjd(str);
            if (zzjd == null) {
                this.zzjdz.put(str, null);
                this.zzjea.put(str, null);
                this.zzjeb.put(str, null);
                this.zzjec.put(str, null);
                this.zzjee.put(str, null);
                this.zzjed.put(str, null);
                return;
            }
            zzcly zzc = zzc(str, zzjd);
            this.zzjdz.put(str, zza(zzc));
            zza(str, zzc);
            this.zzjec.put(str, zzc);
            this.zzjee.put(str, null);
        }
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final String zzam(String str, String str2) {
        zzve();
        zzjr(str);
        Map map = (Map) this.zzjdz.get(str);
        if (map != null) {
            return (String) map.get(str2);
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final boolean zzan(String str, String str2) {
        zzve();
        zzjr(str);
        if (zzawu().zzkl(str) && zzclq.zzki(str2)) {
            return true;
        }
        if (zzawu().zzkm(str) && zzclq.zzjz(str2)) {
            return true;
        }
        Map map = (Map) this.zzjea.get(str);
        if (map == null) {
            return false;
        }
        Boolean bool = (Boolean) map.get(str2);
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final boolean zzao(String str, String str2) {
        zzve();
        zzjr(str);
        if (FirebaseAnalytics.Event.ECOMMERCE_PURCHASE.equals(str2)) {
            return true;
        }
        Map map = (Map) this.zzjeb.get(str);
        if (map == null) {
            return false;
        }
        Boolean bool = (Boolean) map.get(str2);
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final int zzap(String str, String str2) {
        zzve();
        zzjr(str);
        Map map = (Map) this.zzjed.get(str);
        if (map == null) {
            return 1;
        }
        Integer num = (Integer) map.get(str2);
        if (num == null) {
            return 1;
        }
        return num.intValue();
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
    public final boolean zzb(String str, byte[] bArr, String str2) {
        byte[] bArr2;
        zzcls[] zzclsArr;
        zzclv[] zzclvArr;
        String str3 = str;
        zzxf();
        zzve();
        zzbq.zzgm(str);
        zzcly zzc = zzc(str, bArr);
        if (zzc == null) {
            return false;
        }
        zza(str3, zzc);
        this.zzjec.put(str3, zzc);
        this.zzjee.put(str3, str2);
        this.zzjdz.put(str3, zza(zzc));
        zzcgk zzawl = zzawl();
        zzclr[] zzclrArr = zzc.zzjla;
        zzbq.checkNotNull(zzclrArr);
        int length = zzclrArr.length;
        int i = 0;
        while (i < length) {
            zzclr zzclr = zzclrArr[i];
            for (zzcls zzcls : zzclr.zzjju) {
                String zziq = Event.zziq(zzcls.zzjjx);
                if (zziq != null) {
                    zzcls.zzjjx = zziq;
                }
                zzclt[] zzcltArr = zzcls.zzjjy;
                int length2 = zzcltArr.length;
                int i2 = 0;
                while (i2 < length2) {
                    zzclt zzclt = zzcltArr[i2];
                    int i3 = length;
                    String zziq2 = Param.zziq(zzclt.zzjkf);
                    if (zziq2 != null) {
                        zzclt.zzjkf = zziq2;
                    }
                    i2++;
                    length = i3;
                }
                int i4 = length;
            }
            int i5 = length;
            for (zzclv zzclv : zzclr.zzjjt) {
                String zziq3 = UserProperty.zziq(zzclv.zzjkm);
                if (zziq3 != null) {
                    zzclv.zzjkm = zziq3;
                }
            }
            i++;
            length = i5;
        }
        zzawl.zzaws().zza(str3, zzclrArr);
        try {
            zzc.zzjla = null;
            bArr2 = new byte[zzc.zzho()];
            zzc.zza(zzfjk.zzo(bArr2, 0, bArr2.length));
        } catch (IOException e) {
            zzawy().zzazf().zze("Unable to serialize reduced-size config. Storing full config instead. appId", zzchm.zzjk(str), e);
            bArr2 = bArr;
        }
        zzcgo zzaws = zzaws();
        zzbq.zzgm(str);
        zzaws.zzve();
        zzaws.zzxf();
        ContentValues contentValues = new ContentValues();
        contentValues.put("remote_config", bArr2);
        try {
            if (((long) zzaws.getWritableDatabase().update("apps", contentValues, "app_id = ?", new String[]{str3})) == 0) {
                zzaws.zzawy().zzazd().zzj("Failed to update remote config (got 0). appId", zzchm.zzjk(str));
                return true;
            }
        } catch (SQLiteException e2) {
            zzaws.zzawy().zzazd().zze("Error storing remote config. appId", zzchm.zzjk(str), e2);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final zzcly zzjs(String str) {
        zzxf();
        zzve();
        zzbq.zzgm(str);
        zzjr(str);
        return (zzcly) this.zzjec.get(str);
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final String zzjt(String str) {
        zzve();
        return (String) this.zzjee.get(str);
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void zzju(String str) {
        zzve();
        this.zzjee.put(str, null);
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzjv(String str) {
        zzve();
        this.zzjec.remove(str);
    }

    public final /* bridge */ /* synthetic */ void zzve() {
        super.zzve();
    }

    public final /* bridge */ /* synthetic */ zzd zzws() {
        return super.zzws();
    }
}

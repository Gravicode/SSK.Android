package com.google.android.gms.internal;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.common.util.zzd;
import com.google.android.gms.measurement.AppMeasurement;

public final class zzchm extends zzcjl {
    private final String zzgay = ((String) zzchc.zzjad.get());
    private final long zzixd = 11910;
    private final char zzjbt;
    private final zzcho zzjbu;
    private final zzcho zzjbv;
    private final zzcho zzjbw;
    private final zzcho zzjbx;
    private final zzcho zzjby;
    private final zzcho zzjbz;
    private final zzcho zzjca;
    private final zzcho zzjcb;
    private final zzcho zzjcc;

    zzchm(zzcim zzcim) {
        super(zzcim);
        this.zzjbt = zzaxa().zzyp() ? 'C' : 'c';
        this.zzjbu = new zzcho(this, 6, false, false);
        this.zzjbv = new zzcho(this, 6, true, false);
        this.zzjbw = new zzcho(this, 6, false, true);
        this.zzjbx = new zzcho(this, 5, false, false);
        this.zzjby = new zzcho(this, 5, true, false);
        this.zzjbz = new zzcho(this, 5, false, true);
        this.zzjca = new zzcho(this, 4, false, false);
        this.zzjcb = new zzcho(this, 3, false, false);
        this.zzjcc = new zzcho(this, 2, false, false);
    }

    private static String zza(boolean z, String str, Object obj, Object obj2, Object obj3) {
        if (str == null) {
            str = "";
        }
        String zzb = zzb(z, obj);
        String zzb2 = zzb(z, obj2);
        String zzb3 = zzb(z, obj3);
        StringBuilder sb = new StringBuilder();
        String str2 = "";
        if (!TextUtils.isEmpty(str)) {
            sb.append(str);
            str2 = ": ";
        }
        if (!TextUtils.isEmpty(zzb)) {
            sb.append(str2);
            sb.append(zzb);
            str2 = ", ";
        }
        if (!TextUtils.isEmpty(zzb2)) {
            sb.append(str2);
            sb.append(zzb2);
            str2 = ", ";
        }
        if (!TextUtils.isEmpty(zzb3)) {
            sb.append(str2);
            sb.append(zzb3);
        }
        return sb.toString();
    }

    private static String zzb(boolean z, Object obj) {
        if (obj == null) {
            return "";
        }
        if (obj instanceof Integer) {
            obj = Long.valueOf((long) ((Integer) obj).intValue());
        }
        int i = 0;
        if (obj instanceof Long) {
            if (!z) {
                return String.valueOf(obj);
            }
            Long l = (Long) obj;
            if (Math.abs(l.longValue()) < 100) {
                return String.valueOf(obj);
            }
            String str = String.valueOf(obj).charAt(0) == '-' ? "-" : "";
            String valueOf = String.valueOf(Math.abs(l.longValue()));
            long round = Math.round(Math.pow(10.0d, (double) (valueOf.length() - 1)));
            long round2 = Math.round(Math.pow(10.0d, (double) valueOf.length()) - 1.0d);
            StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 43 + String.valueOf(str).length());
            sb.append(str);
            sb.append(round);
            sb.append("...");
            sb.append(str);
            sb.append(round2);
            return sb.toString();
        } else if (obj instanceof Boolean) {
            return String.valueOf(obj);
        } else {
            if (!(obj instanceof Throwable)) {
                return obj instanceof zzchp ? ((zzchp) obj).zzgxt : z ? "-" : String.valueOf(obj);
            }
            Throwable th = (Throwable) obj;
            StringBuilder sb2 = new StringBuilder(z ? th.getClass().getName() : th.toString());
            String zzjl = zzjl(AppMeasurement.class.getCanonicalName());
            String zzjl2 = zzjl(zzcim.class.getCanonicalName());
            StackTraceElement[] stackTrace = th.getStackTrace();
            int length = stackTrace.length;
            while (true) {
                if (i >= length) {
                    break;
                }
                StackTraceElement stackTraceElement = stackTrace[i];
                if (!stackTraceElement.isNativeMethod()) {
                    String className = stackTraceElement.getClassName();
                    if (className != null) {
                        String zzjl3 = zzjl(className);
                        if (zzjl3.equals(zzjl) || zzjl3.equals(zzjl2)) {
                            sb2.append(": ");
                            sb2.append(stackTraceElement);
                        }
                    } else {
                        continue;
                    }
                }
                i++;
            }
            return sb2.toString();
        }
    }

    protected static Object zzjk(String str) {
        if (str == null) {
            return null;
        }
        return new zzchp(str);
    }

    private static String zzjl(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        int lastIndexOf = str.lastIndexOf(46);
        return lastIndexOf == -1 ? str : str.substring(0, lastIndexOf);
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    /* access modifiers changed from: protected */
    public final void zza(int i, boolean z, boolean z2, String str, Object obj, Object obj2, Object obj3) {
        String str2;
        if (!z && zzae(i)) {
            zzk(i, zza(false, str, obj, obj2, obj3));
        }
        if (!z2 && i >= 5) {
            zzbq.checkNotNull(str);
            zzcih zzazy = this.zziwf.zzazy();
            if (zzazy == null) {
                str2 = "Scheduler not set. Not logging error/warn";
            } else if (!zzazy.isInitialized()) {
                str2 = "Scheduler not initialized. Not logging error/warn";
            } else {
                if (i < 0) {
                    i = 0;
                }
                if (i >= 9) {
                    i = 8;
                }
                String str3 = "2";
                char charAt = "01VDIWEA?".charAt(i);
                char c = this.zzjbt;
                long j = this.zzixd;
                String zza = zza(true, str, obj, obj2, obj3);
                StringBuilder sb = new StringBuilder(String.valueOf(str3).length() + 23 + String.valueOf(zza).length());
                sb.append(str3);
                sb.append(charAt);
                sb.append(c);
                sb.append(j);
                sb.append(":");
                sb.append(zza);
                String sb2 = sb.toString();
                if (sb2.length() > 1024) {
                    sb2 = str.substring(0, 1024);
                }
                zzazy.zzg(new zzchn(this, sb2));
            }
            zzk(6, str2);
        }
    }

    /* access modifiers changed from: protected */
    public final boolean zzae(int i) {
        return Log.isLoggable(this.zzgay, i);
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

    public final zzcho zzazd() {
        return this.zzjbu;
    }

    public final zzcho zzaze() {
        return this.zzjbv;
    }

    public final zzcho zzazf() {
        return this.zzjbx;
    }

    public final zzcho zzazg() {
        return this.zzjbz;
    }

    public final zzcho zzazh() {
        return this.zzjca;
    }

    public final zzcho zzazi() {
        return this.zzjcb;
    }

    public final zzcho zzazj() {
        return this.zzjcc;
    }

    public final String zzazk() {
        Pair<String, Long> zzaad = zzawz().zzjcq.zzaad();
        if (zzaad == null || zzaad == zzchx.zzjcp) {
            return null;
        }
        String valueOf = String.valueOf(zzaad.second);
        String str = (String) zzaad.first;
        StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 1 + String.valueOf(str).length());
        sb.append(valueOf);
        sb.append(":");
        sb.append(str);
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public final void zzk(int i, String str) {
        Log.println(i, this.zzgay, str);
    }

    public final /* bridge */ /* synthetic */ void zzve() {
        super.zzve();
    }

    public final /* bridge */ /* synthetic */ zzd zzws() {
        return super.zzws();
    }
}

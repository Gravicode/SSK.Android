package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.common.util.zzd;
import com.google.android.gms.measurement.AppMeasurement.Event;
import com.google.android.gms.measurement.AppMeasurement.Param;
import com.google.android.gms.measurement.AppMeasurement.UserProperty;
import org.apache.commons.math3.geometry.VectorFormat;

public final class zzchk extends zzcjl {
    private static String[] zzjbq = new String[Event.zziwg.length];
    private static String[] zzjbr = new String[Param.zziwi.length];
    private static String[] zzjbs = new String[UserProperty.zziwn.length];

    zzchk(zzcim zzcim) {
        super(zzcim);
    }

    @Nullable
    private static String zza(String str, String[] strArr, String[] strArr2, String[] strArr3) {
        String str2;
        zzbq.checkNotNull(strArr);
        zzbq.checkNotNull(strArr2);
        zzbq.checkNotNull(strArr3);
        boolean z = true;
        zzbq.checkArgument(strArr.length == strArr2.length);
        if (strArr.length != strArr3.length) {
            z = false;
        }
        zzbq.checkArgument(z);
        for (int i = 0; i < strArr.length; i++) {
            if (zzclq.zzas(str, strArr[i])) {
                synchronized (strArr3) {
                    if (strArr3[i] == null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(strArr2[i]);
                        sb.append("(");
                        sb.append(strArr[i]);
                        sb.append(")");
                        strArr3[i] = sb.toString();
                    }
                    str2 = strArr3[i];
                }
                return str2;
            }
        }
        return str;
    }

    private static void zza(StringBuilder sb, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            sb.append("  ");
        }
    }

    private final void zza(StringBuilder sb, int i, zzclt zzclt) {
        String[] strArr;
        if (zzclt != null) {
            zza(sb, i);
            sb.append("filter {\n");
            zza(sb, i, "complement", (Object) zzclt.zzjke);
            zza(sb, i, "param_name", (Object) zzji(zzclt.zzjkf));
            int i2 = i + 1;
            String str = "string_filter";
            zzclw zzclw = zzclt.zzjkc;
            if (zzclw != null) {
                zza(sb, i2);
                sb.append(str);
                sb.append(" {\n");
                if (zzclw.zzjko != null) {
                    String str2 = "UNKNOWN_MATCH_TYPE";
                    switch (zzclw.zzjko.intValue()) {
                        case 1:
                            str2 = "REGEXP";
                            break;
                        case 2:
                            str2 = "BEGINS_WITH";
                            break;
                        case 3:
                            str2 = "ENDS_WITH";
                            break;
                        case 4:
                            str2 = "PARTIAL";
                            break;
                        case 5:
                            str2 = "EXACT";
                            break;
                        case 6:
                            str2 = "IN_LIST";
                            break;
                    }
                    zza(sb, i2, "match_type", (Object) str2);
                }
                zza(sb, i2, "expression", (Object) zzclw.zzjkp);
                zza(sb, i2, "case_sensitive", (Object) zzclw.zzjkq);
                if (zzclw.zzjkr.length > 0) {
                    zza(sb, i2 + 1);
                    sb.append("expression_list {\n");
                    for (String str3 : zzclw.zzjkr) {
                        zza(sb, i2 + 2);
                        sb.append(str3);
                        sb.append("\n");
                    }
                    sb.append("}\n");
                }
                zza(sb, i2);
                sb.append("}\n");
            }
            zza(sb, i2, "number_filter", zzclt.zzjkd);
            zza(sb, i);
            sb.append("}\n");
        }
    }

    private final void zza(StringBuilder sb, int i, String str, zzclu zzclu) {
        if (zzclu != null) {
            zza(sb, i);
            sb.append(str);
            sb.append(" {\n");
            if (zzclu.zzjkg != null) {
                String str2 = "UNKNOWN_COMPARISON_TYPE";
                switch (zzclu.zzjkg.intValue()) {
                    case 1:
                        str2 = "LESS_THAN";
                        break;
                    case 2:
                        str2 = "GREATER_THAN";
                        break;
                    case 3:
                        str2 = "EQUAL";
                        break;
                    case 4:
                        str2 = "BETWEEN";
                        break;
                }
                zza(sb, i, "comparison_type", (Object) str2);
            }
            zza(sb, i, "match_as_float", (Object) zzclu.zzjkh);
            zza(sb, i, "comparison_value", (Object) zzclu.zzjki);
            zza(sb, i, "min_comparison_value", (Object) zzclu.zzjkj);
            zza(sb, i, "max_comparison_value", (Object) zzclu.zzjkk);
            zza(sb, i);
            sb.append("}\n");
        }
    }

    private static void zza(StringBuilder sb, int i, String str, zzcmf zzcmf) {
        if (zzcmf != null) {
            int i2 = i + 1;
            zza(sb, i2);
            sb.append(str);
            sb.append(" {\n");
            int i3 = 0;
            if (zzcmf.zzjmq != null) {
                zza(sb, i2 + 1);
                sb.append("results: ");
                long[] jArr = zzcmf.zzjmq;
                int length = jArr.length;
                int i4 = 0;
                int i5 = 0;
                while (i4 < length) {
                    Long valueOf = Long.valueOf(jArr[i4]);
                    int i6 = i5 + 1;
                    if (i5 != 0) {
                        sb.append(", ");
                    }
                    sb.append(valueOf);
                    i4++;
                    i5 = i6;
                }
                sb.append(10);
            }
            if (zzcmf.zzjmp != null) {
                zza(sb, i2 + 1);
                sb.append("status: ");
                long[] jArr2 = zzcmf.zzjmp;
                int length2 = jArr2.length;
                int i7 = 0;
                while (i3 < length2) {
                    Long valueOf2 = Long.valueOf(jArr2[i3]);
                    int i8 = i7 + 1;
                    if (i7 != 0) {
                        sb.append(", ");
                    }
                    sb.append(valueOf2);
                    i3++;
                    i7 = i8;
                }
                sb.append(10);
            }
            zza(sb, i2);
            sb.append("}\n");
        }
    }

    private static void zza(StringBuilder sb, int i, String str, Object obj) {
        if (obj != null) {
            zza(sb, i + 1);
            sb.append(str);
            sb.append(": ");
            sb.append(obj);
            sb.append(10);
        }
    }

    private final void zza(StringBuilder sb, int i, zzcma[] zzcmaArr) {
        if (zzcmaArr != null) {
            for (zzcma zzcma : zzcmaArr) {
                if (zzcma != null) {
                    zza(sb, 2);
                    sb.append("audience_membership {\n");
                    zza(sb, 2, "audience_id", (Object) zzcma.zzjjs);
                    zza(sb, 2, "new_audience", (Object) zzcma.zzjlf);
                    zza(sb, 2, "current_data", zzcma.zzjld);
                    zza(sb, 2, "previous_data", zzcma.zzjle);
                    zza(sb, 2);
                    sb.append("}\n");
                }
            }
        }
    }

    private final void zza(StringBuilder sb, int i, zzcmb[] zzcmbArr) {
        if (zzcmbArr != null) {
            for (zzcmb zzcmb : zzcmbArr) {
                if (zzcmb != null) {
                    zza(sb, 2);
                    sb.append("event {\n");
                    zza(sb, 2, "name", (Object) zzjh(zzcmb.name));
                    zza(sb, 2, "timestamp_millis", (Object) zzcmb.zzjli);
                    zza(sb, 2, "previous_timestamp_millis", (Object) zzcmb.zzjlj);
                    zza(sb, 2, "count", (Object) zzcmb.count);
                    zzcmc[] zzcmcArr = zzcmb.zzjlh;
                    if (zzcmcArr != null) {
                        for (zzcmc zzcmc : zzcmcArr) {
                            if (zzcmc != null) {
                                zza(sb, 3);
                                sb.append("param {\n");
                                zza(sb, 3, "name", (Object) zzji(zzcmc.name));
                                zza(sb, 3, "string_value", (Object) zzcmc.zzgcc);
                                zza(sb, 3, "int_value", (Object) zzcmc.zzjll);
                                zza(sb, 3, "double_value", (Object) zzcmc.zzjjl);
                                zza(sb, 3);
                                sb.append("}\n");
                            }
                        }
                    }
                    zza(sb, 2);
                    sb.append("}\n");
                }
            }
        }
    }

    private final void zza(StringBuilder sb, int i, zzcmg[] zzcmgArr) {
        if (zzcmgArr != null) {
            for (zzcmg zzcmg : zzcmgArr) {
                if (zzcmg != null) {
                    zza(sb, 2);
                    sb.append("user_property {\n");
                    zza(sb, 2, "set_timestamp_millis", (Object) zzcmg.zzjms);
                    zza(sb, 2, "name", (Object) zzjj(zzcmg.name));
                    zza(sb, 2, "string_value", (Object) zzcmg.zzgcc);
                    zza(sb, 2, "int_value", (Object) zzcmg.zzjll);
                    zza(sb, 2, "double_value", (Object) zzcmg.zzjjl);
                    zza(sb, 2);
                    sb.append("}\n");
                }
            }
        }
    }

    private final boolean zzazc() {
        return this.zziwf.zzawy().zzae(3);
    }

    @Nullable
    private final String zzb(zzcgx zzcgx) {
        if (zzcgx == null) {
            return null;
        }
        return !zzazc() ? zzcgx.toString() : zzx(zzcgx.zzayx());
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    /* access modifiers changed from: protected */
    @Nullable
    public final String zza(zzcgv zzcgv) {
        if (zzcgv == null) {
            return null;
        }
        if (!zzazc()) {
            return zzcgv.toString();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Event{appId='");
        sb.append(zzcgv.mAppId);
        sb.append("', name='");
        sb.append(zzjh(zzcgv.mName));
        sb.append("', params=");
        sb.append(zzb(zzcgv.zzizj));
        sb.append(VectorFormat.DEFAULT_SUFFIX);
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public final String zza(zzcls zzcls) {
        if (zzcls == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\nevent_filter {\n");
        zza(sb, 0, "filter_id", (Object) zzcls.zzjjw);
        zza(sb, 0, "event_name", (Object) zzjh(zzcls.zzjjx));
        zza(sb, 1, "event_count_filter", zzcls.zzjka);
        sb.append("  filters {\n");
        for (zzclt zza : zzcls.zzjjy) {
            zza(sb, 2, zza);
        }
        zza(sb, 1);
        sb.append("}\n}\n");
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public final String zza(zzclv zzclv) {
        if (zzclv == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\nproperty_filter {\n");
        zza(sb, 0, "filter_id", (Object) zzclv.zzjjw);
        zza(sb, 0, "property_name", (Object) zzjj(zzclv.zzjkm));
        zza(sb, 1, zzclv.zzjkn);
        sb.append("}\n");
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public final String zza(zzcmd zzcmd) {
        zzcme[] zzcmeArr;
        StringBuilder sb = new StringBuilder();
        sb.append("\nbatch {\n");
        if (zzcmd.zzjlm != null) {
            for (zzcme zzcme : zzcmd.zzjlm) {
                if (!(zzcme == null || zzcme == null)) {
                    zza(sb, 1);
                    sb.append("bundle {\n");
                    zza(sb, 1, "protocol_version", (Object) zzcme.zzjlo);
                    zza(sb, 1, "platform", (Object) zzcme.zzjlw);
                    zza(sb, 1, "gmp_version", (Object) zzcme.zzjma);
                    zza(sb, 1, "uploading_gmp_version", (Object) zzcme.zzjmb);
                    zza(sb, 1, "config_version", (Object) zzcme.zzjmn);
                    zza(sb, 1, "gmp_app_id", (Object) zzcme.zzixs);
                    zza(sb, 1, "app_id", (Object) zzcme.zzcn);
                    zza(sb, 1, "app_version", (Object) zzcme.zzifm);
                    zza(sb, 1, "app_version_major", (Object) zzcme.zzjmj);
                    zza(sb, 1, "firebase_instance_id", (Object) zzcme.zziya);
                    zza(sb, 1, "dev_cert_hash", (Object) zzcme.zzjmf);
                    zza(sb, 1, "app_store", (Object) zzcme.zzixt);
                    zza(sb, 1, "upload_timestamp_millis", (Object) zzcme.zzjlr);
                    zza(sb, 1, "start_timestamp_millis", (Object) zzcme.zzjls);
                    zza(sb, 1, "end_timestamp_millis", (Object) zzcme.zzjlt);
                    zza(sb, 1, "previous_bundle_start_timestamp_millis", (Object) zzcme.zzjlu);
                    zza(sb, 1, "previous_bundle_end_timestamp_millis", (Object) zzcme.zzjlv);
                    zza(sb, 1, "app_instance_id", (Object) zzcme.zzjme);
                    zza(sb, 1, "resettable_device_id", (Object) zzcme.zzjmc);
                    zza(sb, 1, "device_id", (Object) zzcme.zzjmm);
                    zza(sb, 1, "limited_ad_tracking", (Object) zzcme.zzjmd);
                    zza(sb, 1, "os_version", (Object) zzcme.zzdb);
                    zza(sb, 1, "device_model", (Object) zzcme.zzjlx);
                    zza(sb, 1, "user_default_language", (Object) zzcme.zzjly);
                    zza(sb, 1, "time_zone_offset_minutes", (Object) zzcme.zzjlz);
                    zza(sb, 1, "bundle_sequential_index", (Object) zzcme.zzjmg);
                    zza(sb, 1, "service_upload", (Object) zzcme.zzjmh);
                    zza(sb, 1, "health_monitor", (Object) zzcme.zzixw);
                    if (zzcme.zzfkk.longValue() != 0) {
                        zza(sb, 1, "android_id", (Object) zzcme.zzfkk);
                    }
                    zza(sb, 1, zzcme.zzjlq);
                    zza(sb, 1, zzcme.zzjmi);
                    zza(sb, 1, zzcme.zzjlp);
                    zza(sb, 1);
                    sb.append("}\n");
                }
            }
        }
        sb.append("}\n");
        return sb.toString();
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
    @Nullable
    public final String zzb(zzcha zzcha) {
        if (zzcha == null) {
            return null;
        }
        if (!zzazc()) {
            return zzcha.toString();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("origin=");
        sb.append(zzcha.zziyf);
        sb.append(",name=");
        sb.append(zzjh(zzcha.name));
        sb.append(",params=");
        sb.append(zzb(zzcha.zzizt));
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    @Nullable
    public final String zzjh(String str) {
        if (str == null) {
            return null;
        }
        return !zzazc() ? str : zza(str, Event.zziwh, Event.zziwg, zzjbq);
    }

    /* access modifiers changed from: protected */
    @Nullable
    public final String zzji(String str) {
        if (str == null) {
            return null;
        }
        return !zzazc() ? str : zza(str, Param.zziwj, Param.zziwi, zzjbr);
    }

    /* access modifiers changed from: protected */
    @Nullable
    public final String zzjj(String str) {
        if (str == null) {
            return null;
        }
        if (!zzazc()) {
            return str;
        }
        if (!str.startsWith("_exp_")) {
            return zza(str, UserProperty.zziwo, UserProperty.zziwn, zzjbs);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("experiment_id");
        sb.append("(");
        sb.append(str);
        sb.append(")");
        return sb.toString();
    }

    public final /* bridge */ /* synthetic */ void zzve() {
        super.zzve();
    }

    public final /* bridge */ /* synthetic */ zzd zzws() {
        return super.zzws();
    }

    /* access modifiers changed from: protected */
    @Nullable
    public final String zzx(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        if (!zzazc()) {
            return bundle.toString();
        }
        StringBuilder sb = new StringBuilder();
        for (String str : bundle.keySet()) {
            sb.append(sb.length() != 0 ? ", " : "Bundle[{");
            sb.append(zzji(str));
            sb.append("=");
            sb.append(bundle.get(str));
        }
        sb.append("}]");
        return sb.toString();
    }
}

package com.google.android.gms.internal;

import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.common.util.zzb;
import com.google.android.gms.common.util.zzo;
import com.google.android.gms.common.util.zzp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.math3.geometry.VectorFormat;

public abstract class zzbgn {
    protected static <O, I> I zza(zzbgo<I, O> zzbgo, Object obj) {
        return zzbgo.zzgcn != null ? zzbgo.convertBack(obj) : obj;
    }

    private static void zza(StringBuilder sb, zzbgo zzbgo, Object obj) {
        String str;
        if (zzbgo.zzgce == 11) {
            str = ((zzbgn) zzbgo.zzgck.cast(obj)).toString();
        } else if (zzbgo.zzgce == 7) {
            sb.append("\"");
            sb.append(zzo.zzgr((String) obj));
            str = "\"";
        } else {
            sb.append(obj);
            return;
        }
        sb.append(str);
    }

    private static void zza(StringBuilder sb, zzbgo zzbgo, ArrayList<Object> arrayList) {
        sb.append("[");
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(",");
            }
            Object obj = arrayList.get(i);
            if (obj != null) {
                zza(sb, zzbgo, obj);
            }
        }
        sb.append("]");
    }

    public String toString() {
        String str;
        String str2;
        Map zzaav = zzaav();
        StringBuilder sb = new StringBuilder(100);
        for (String str3 : zzaav.keySet()) {
            zzbgo zzbgo = (zzbgo) zzaav.get(str3);
            if (zza(zzbgo)) {
                Object zza = zza(zzbgo, zzb(zzbgo));
                sb.append(sb.length() == 0 ? VectorFormat.DEFAULT_PREFIX : ",");
                sb.append("\"");
                sb.append(str3);
                sb.append("\":");
                if (zza == null) {
                    str2 = "null";
                } else {
                    switch (zzbgo.zzgcg) {
                        case 8:
                            sb.append("\"");
                            str = zzb.zzk((byte[]) zza);
                            break;
                        case 9:
                            sb.append("\"");
                            str = zzb.zzl((byte[]) zza);
                            break;
                        case 10:
                            zzp.zza(sb, (HashMap) zza);
                            continue;
                        default:
                            if (!zzbgo.zzgcf) {
                                zza(sb, zzbgo, zza);
                                break;
                            } else {
                                zza(sb, zzbgo, (ArrayList) zza);
                                continue;
                            }
                    }
                    sb.append(str);
                    str2 = "\"";
                }
                sb.append(str2);
            }
        }
        sb.append(sb.length() > 0 ? VectorFormat.DEFAULT_SUFFIX : "{}");
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public boolean zza(zzbgo zzbgo) {
        if (zzbgo.zzgcg != 11) {
            return zzgp(zzbgo.zzgci);
        }
        if (zzbgo.zzgch) {
            String str = zzbgo.zzgci;
            throw new UnsupportedOperationException("Concrete type arrays not supported");
        }
        String str2 = zzbgo.zzgci;
        throw new UnsupportedOperationException("Concrete types not supported");
    }

    public abstract Map<String, zzbgo<?, ?>> zzaav();

    /* access modifiers changed from: protected */
    public Object zzb(zzbgo zzbgo) {
        String str = zzbgo.zzgci;
        if (zzbgo.zzgck == null) {
            return zzgo(zzbgo.zzgci);
        }
        zzgo(zzbgo.zzgci);
        zzbq.zza(true, "Concrete field shouldn't be value object: %s", zzbgo.zzgci);
        boolean z = zzbgo.zzgch;
        try {
            char upperCase = Character.toUpperCase(str.charAt(0));
            String substring = str.substring(1);
            StringBuilder sb = new StringBuilder(String.valueOf(substring).length() + 4);
            sb.append("get");
            sb.append(upperCase);
            sb.append(substring);
            return getClass().getMethod(sb.toString(), new Class[0]).invoke(this, new Object[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* access modifiers changed from: protected */
    public abstract Object zzgo(String str);

    /* access modifiers changed from: protected */
    public abstract boolean zzgp(String str);
}

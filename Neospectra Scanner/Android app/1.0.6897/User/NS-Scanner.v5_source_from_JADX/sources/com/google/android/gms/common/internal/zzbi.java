package com.google.android.gms.common.internal;

import java.util.ArrayList;
import java.util.List;

public final class zzbi {
    private final Object zzddc;
    private final List<String> zzgbe;

    private zzbi(Object obj) {
        this.zzddc = zzbq.checkNotNull(obj);
        this.zzgbe = new ArrayList();
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append(this.zzddc.getClass().getSimpleName());
        sb.append('{');
        int size = this.zzgbe.size();
        for (int i = 0; i < size; i++) {
            sb.append((String) this.zzgbe.get(i));
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append('}');
        return sb.toString();
    }

    public final zzbi zzg(String str, Object obj) {
        List<String> list = this.zzgbe;
        String str2 = (String) zzbq.checkNotNull(str);
        String valueOf = String.valueOf(obj);
        StringBuilder sb = new StringBuilder(String.valueOf(str2).length() + 1 + String.valueOf(valueOf).length());
        sb.append(str2);
        sb.append("=");
        sb.append(valueOf);
        list.add(sb.toString());
        return this;
    }
}

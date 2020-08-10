package com.google.android.gms.internal;

import com.google.android.gms.internal.zzfjm;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public final class zzfjn<M extends zzfjm<M>, T> {
    public final int tag;
    private int type;
    protected final Class<T> zznfk;
    private zzffu<?, ?> zzpgu;
    protected final boolean zzpnd;

    private zzfjn(int i, Class<T> cls, int i2, boolean z) {
        this(11, cls, null, i2, false);
    }

    private zzfjn(int i, Class<T> cls, zzffu<?, ?> zzffu, int i2, boolean z) {
        this.type = i;
        this.zznfk = cls;
        this.tag = i2;
        this.zzpnd = false;
        this.zzpgu = null;
    }

    public static <M extends zzfjm<M>, T extends zzfjs> zzfjn<M, T> zza(int i, Class<T> cls, long j) {
        return new zzfjn<>(11, cls, (int) j, false);
    }

    private final Object zzan(zzfjj zzfjj) {
        Class<T> componentType = this.zzpnd ? this.zznfk.getComponentType() : this.zznfk;
        try {
            switch (this.type) {
                case 10:
                    zzfjs zzfjs = (zzfjs) componentType.newInstance();
                    zzfjj.zza(zzfjs, this.tag >>> 3);
                    return zzfjs;
                case 11:
                    zzfjs zzfjs2 = (zzfjs) componentType.newInstance();
                    zzfjj.zza(zzfjs2);
                    return zzfjs2;
                default:
                    int i = this.type;
                    StringBuilder sb = new StringBuilder(24);
                    sb.append("Unknown type ");
                    sb.append(i);
                    throw new IllegalArgumentException(sb.toString());
            }
        } catch (InstantiationException e) {
            String valueOf = String.valueOf(componentType);
            StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf).length() + 33);
            sb2.append("Error creating instance of class ");
            sb2.append(valueOf);
            throw new IllegalArgumentException(sb2.toString(), e);
        } catch (IllegalAccessException e2) {
            String valueOf2 = String.valueOf(componentType);
            StringBuilder sb3 = new StringBuilder(String.valueOf(valueOf2).length() + 33);
            sb3.append("Error creating instance of class ");
            sb3.append(valueOf2);
            throw new IllegalArgumentException(sb3.toString(), e2);
        } catch (IOException e3) {
            throw new IllegalArgumentException("Error reading extension field", e3);
        }
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzfjn)) {
            return false;
        }
        zzfjn zzfjn = (zzfjn) obj;
        return this.type == zzfjn.type && this.zznfk == zzfjn.zznfk && this.tag == zzfjn.tag && this.zzpnd == zzfjn.zzpnd;
    }

    public final int hashCode() {
        return ((((((this.type + 1147) * 31) + this.zznfk.hashCode()) * 31) + this.tag) * 31) + (this.zzpnd ? 1 : 0);
    }

    /* access modifiers changed from: protected */
    public final void zza(Object obj, zzfjk zzfjk) {
        try {
            zzfjk.zzmi(this.tag);
            switch (this.type) {
                case 10:
                    int i = this.tag >>> 3;
                    ((zzfjs) obj).zza(zzfjk);
                    zzfjk.zzz(i, 4);
                    return;
                case 11:
                    zzfjk.zzb((zzfjs) obj);
                    return;
                default:
                    int i2 = this.type;
                    StringBuilder sb = new StringBuilder(24);
                    sb.append("Unknown type ");
                    sb.append(i2);
                    throw new IllegalArgumentException(sb.toString());
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /* access modifiers changed from: 0000 */
    public final T zzbq(List<zzfju> list) {
        if (list == null) {
            return null;
        }
        if (this.zzpnd) {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                zzfju zzfju = (zzfju) list.get(i);
                if (zzfju.zzjng.length != 0) {
                    arrayList.add(zzan(zzfjj.zzbe(zzfju.zzjng)));
                }
            }
            int size = arrayList.size();
            if (size == 0) {
                return null;
            }
            T cast = this.zznfk.cast(Array.newInstance(this.zznfk.getComponentType(), size));
            for (int i2 = 0; i2 < size; i2++) {
                Array.set(cast, i2, arrayList.get(i2));
            }
            return cast;
        } else if (list.isEmpty()) {
            return null;
        } else {
            return this.zznfk.cast(zzan(zzfjj.zzbe(((zzfju) list.get(list.size() - 1)).zzjng)));
        }
    }

    /* access modifiers changed from: protected */
    public final int zzcs(Object obj) {
        int i = this.tag >>> 3;
        switch (this.type) {
            case 10:
                return (zzfjk.zzlg(i) << 1) + ((zzfjs) obj).zzho();
            case 11:
                return zzfjk.zzb(i, (zzfjs) obj);
            default:
                int i2 = this.type;
                StringBuilder sb = new StringBuilder(24);
                sb.append("Unknown type ");
                sb.append(i2);
                throw new IllegalArgumentException(sb.toString());
        }
    }
}

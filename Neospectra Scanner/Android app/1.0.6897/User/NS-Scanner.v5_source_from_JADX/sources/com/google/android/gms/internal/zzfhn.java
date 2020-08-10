package com.google.android.gms.internal;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

final class zzfhn {
    private static final zzfhn zzpjb = new zzfhn();
    private final zzfhw zzpjc;
    private final ConcurrentMap<Class<?>, zzfhv<?>> zzpjd = new ConcurrentHashMap();

    private zzfhn() {
        String[] strArr = {"com.google.protobuf.AndroidProto3SchemaFactory"};
        zzfhw zzfhw = null;
        for (int i = 0; i <= 0; i++) {
            zzfhw = zztw(strArr[0]);
            if (zzfhw != null) {
                break;
            }
        }
        if (zzfhw == null) {
            zzfhw = new zzfgq();
        }
        this.zzpjc = zzfhw;
    }

    public static zzfhn zzcyz() {
        return zzpjb;
    }

    private static zzfhw zztw(String str) {
        try {
            return (zzfhw) Class.forName(str).getConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Throwable th) {
            return null;
        }
    }

    public final <T> zzfhv<T> zzl(Class<T> cls) {
        zzffz.zzc(cls, "messageType");
        zzfhv<T> zzfhv = (zzfhv) this.zzpjd.get(cls);
        if (zzfhv != null) {
            return zzfhv;
        }
        zzfhv<T> zzk = this.zzpjc.zzk(cls);
        zzffz.zzc(cls, "messageType");
        zzffz.zzc(zzk, "schema");
        zzfhv zzfhv2 = (zzfhv) this.zzpjd.putIfAbsent(cls, zzk);
        return zzfhv2 != null ? zzfhv2 : zzk;
    }
}

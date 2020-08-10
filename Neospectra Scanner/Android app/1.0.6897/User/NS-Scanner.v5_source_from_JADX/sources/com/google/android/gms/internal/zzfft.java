package com.google.android.gms.internal;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

final class zzfft implements zzfhd {
    private static final zzfft zzpgp = new zzfft();
    private final Map<Class<?>, Method> zzpgq = new HashMap();

    private zzfft() {
    }

    public static zzfft zzcxl() {
        return zzpgp;
    }

    public final boolean zzi(Class<?> cls) {
        return zzffu.class.isAssignableFrom(cls);
    }

    public final zzfhc zzj(Class<?> cls) {
        if (!zzffu.class.isAssignableFrom(cls)) {
            String str = "Unsupported message type: ";
            String valueOf = String.valueOf(cls.getName());
            throw new IllegalArgumentException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
        }
        try {
            Method method = (Method) this.zzpgq.get(cls);
            if (method == null) {
                method = cls.getDeclaredMethod("buildMessageInfo", new Class[0]);
                method.setAccessible(true);
                this.zzpgq.put(cls, method);
            }
            return (zzfhc) method.invoke(null, new Object[0]);
        } catch (Exception e) {
            String str2 = "Unable to get message info for ";
            String valueOf2 = String.valueOf(cls.getName());
            throw new RuntimeException(valueOf2.length() != 0 ? str2.concat(valueOf2) : new String(str2), e);
        }
    }
}

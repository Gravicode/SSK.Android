package com.google.android.gms.internal;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

final class zzdvn {
    private final ConcurrentHashMap<zzdvo, List<Throwable>> zzmaj = new ConcurrentHashMap<>(16, 0.75f, 10);
    private final ReferenceQueue<Throwable> zzmak = new ReferenceQueue<>();

    zzdvn() {
    }

    public final List<Throwable> zza(Throwable th, boolean z) {
        while (true) {
            Reference poll = this.zzmak.poll();
            if (poll != null) {
                this.zzmaj.remove(poll);
            } else {
                return (List) this.zzmaj.get(new zzdvo(th, null));
            }
        }
    }
}

package com.google.android.gms.internal;

import java.util.AbstractMap.SimpleEntry;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Stack;

public final class zzeak<K, V> implements Iterator<Entry<K, V>> {
    private final Stack<zzeat<K, V>> zzmmg = new Stack<>();
    private final boolean zzmmh;

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0044, code lost:
        if (r6 != false) goto L_0x002d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0047, code lost:
        r3 = r3.zzbtp();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x002b, code lost:
        if (r6 == false) goto L_0x002d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    zzeak(com.google.android.gms.internal.zzeap<K, V> r3, K r4, java.util.Comparator<K> r5, boolean r6) {
        /*
            r2 = this;
            r2.<init>()
            java.util.Stack r0 = new java.util.Stack
            r0.<init>()
            r2.zzmmg = r0
            r2.zzmmh = r6
        L_0x000c:
            boolean r0 = r3.isEmpty()
            if (r0 != 0) goto L_0x004c
            if (r4 == 0) goto L_0x0028
            if (r6 == 0) goto L_0x001f
            java.lang.Object r0 = r3.getKey()
            int r0 = r5.compare(r4, r0)
            goto L_0x0029
        L_0x001f:
            java.lang.Object r0 = r3.getKey()
            int r0 = r5.compare(r0, r4)
            goto L_0x0029
        L_0x0028:
            r0 = 1
        L_0x0029:
            if (r0 >= 0) goto L_0x0032
            if (r6 != 0) goto L_0x0047
        L_0x002d:
            com.google.android.gms.internal.zzeap r3 = r3.zzbtq()
            goto L_0x000c
        L_0x0032:
            if (r0 != 0) goto L_0x003c
            java.util.Stack<com.google.android.gms.internal.zzeat<K, V>> r4 = r2.zzmmg
            com.google.android.gms.internal.zzeat r3 = (com.google.android.gms.internal.zzeat) r3
            r4.push(r3)
            return
        L_0x003c:
            java.util.Stack<com.google.android.gms.internal.zzeat<K, V>> r0 = r2.zzmmg
            r1 = r3
            com.google.android.gms.internal.zzeat r1 = (com.google.android.gms.internal.zzeat) r1
            r0.push(r1)
            if (r6 == 0) goto L_0x0047
            goto L_0x002d
        L_0x0047:
            com.google.android.gms.internal.zzeap r3 = r3.zzbtp()
            goto L_0x000c
        L_0x004c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzeak.<init>(com.google.android.gms.internal.zzeap, java.lang.Object, java.util.Comparator, boolean):void");
    }

    /* access modifiers changed from: private */
    public final Entry<K, V> next() {
        try {
            zzeat zzeat = (zzeat) this.zzmmg.pop();
            SimpleEntry simpleEntry = new SimpleEntry(zzeat.getKey(), zzeat.getValue());
            if (this.zzmmh) {
                for (zzeap zzbtp = zzeat.zzbtp(); !zzbtp.isEmpty(); zzbtp = zzbtp.zzbtq()) {
                    this.zzmmg.push((zzeat) zzbtp);
                }
            } else {
                for (zzeap zzbtq = zzeat.zzbtq(); !zzbtq.isEmpty(); zzbtq = zzbtq.zzbtp()) {
                    this.zzmmg.push((zzeat) zzbtq);
                }
            }
            return simpleEntry;
        } catch (EmptyStackException e) {
            throw new NoSuchElementException();
        }
    }

    public final boolean hasNext() {
        return this.zzmmg.size() > 0;
    }

    public final void remove() {
        throw new UnsupportedOperationException("remove called on immutable collection");
    }
}

package com.google.android.gms.internal;

import java.util.Arrays;
import java.util.Stack;

final class zzfhs {
    private final Stack<zzfes> zzpjm;

    private zzfhs() {
        this.zzpjm = new Stack<>();
    }

    private final void zzba(zzfes zzfes) {
        while (!zzfes.zzcvo()) {
            if (zzfes instanceof zzfhq) {
                zzfhq zzfhq = (zzfhq) zzfes;
                zzba(zzfhq.zzpji);
                zzfes = zzfhq.zzpjj;
            } else {
                String valueOf = String.valueOf(zzfes.getClass());
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 49);
                sb.append("Has a new type of ByteString been created? Found ");
                sb.append(valueOf);
                throw new IllegalArgumentException(sb.toString());
            }
        }
        int zzlz = zzlz(zzfes.size());
        int i = zzfhq.zzpjg[zzlz + 1];
        if (this.zzpjm.isEmpty() || ((zzfes) this.zzpjm.peek()).size() >= i) {
            this.zzpjm.push(zzfes);
            return;
        }
        int i2 = zzfhq.zzpjg[zzlz];
        zzfes zzfes2 = (zzfes) this.zzpjm.pop();
        while (!this.zzpjm.isEmpty() && ((zzfes) this.zzpjm.peek()).size() < i2) {
            zzfes2 = new zzfhq((zzfes) this.zzpjm.pop(), zzfes2);
        }
        zzfhq zzfhq2 = new zzfhq(zzfes2, zzfes);
        while (!this.zzpjm.isEmpty()) {
            if (((zzfes) this.zzpjm.peek()).size() >= zzfhq.zzpjg[zzlz(zzfhq2.size()) + 1]) {
                break;
            }
            zzfhq2 = new zzfhq((zzfes) this.zzpjm.pop(), zzfhq2);
        }
        this.zzpjm.push(zzfhq2);
    }

    /* access modifiers changed from: private */
    public final zzfes zzc(zzfes zzfes, zzfes zzfes2) {
        zzba(zzfes);
        zzba(zzfes2);
        zzfes zzfes3 = (zzfes) this.zzpjm.pop();
        while (!this.zzpjm.isEmpty()) {
            zzfes3 = new zzfhq((zzfes) this.zzpjm.pop(), zzfes3);
        }
        return zzfes3;
    }

    private static int zzlz(int i) {
        int binarySearch = Arrays.binarySearch(zzfhq.zzpjg, i);
        return binarySearch < 0 ? (-(binarySearch + 1)) - 1 : binarySearch;
    }
}

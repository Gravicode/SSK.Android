package com.google.firebase.iid;

import android.os.Bundle;

final class zzq extends zzr<Void> {
    zzq(int i, int i2, Bundle bundle) {
        super(i, 2, bundle);
    }

    /* access modifiers changed from: 0000 */
    public final void zzac(Bundle bundle) {
        if (bundle.getBoolean("ack", false)) {
            finish(null);
        } else {
            zzb(new zzs(4, "Invalid response to one way request"));
        }
    }

    /* access modifiers changed from: 0000 */
    public final boolean zzcje() {
        return true;
    }
}

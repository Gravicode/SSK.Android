package com.google.firebase.iid;

import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.tasks.TaskCompletionSource;
import org.apache.commons.math3.geometry.VectorFormat;

abstract class zzr<T> {
    final int what;
    final TaskCompletionSource<T> zzgrq = new TaskCompletionSource<>();
    final int zzift;
    final Bundle zznzj;

    zzr(int i, int i2, Bundle bundle) {
        this.zzift = i;
        this.what = i2;
        this.zznzj = bundle;
    }

    /* access modifiers changed from: 0000 */
    public final void finish(T t) {
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            String valueOf = String.valueOf(this);
            String valueOf2 = String.valueOf(t);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 16 + String.valueOf(valueOf2).length());
            sb.append("Finishing ");
            sb.append(valueOf);
            sb.append(" with ");
            sb.append(valueOf2);
            Log.d("MessengerIpcClient", sb.toString());
        }
        this.zzgrq.setResult(t);
    }

    public String toString() {
        int i = this.what;
        int i2 = this.zzift;
        boolean zzcje = zzcje();
        StringBuilder sb = new StringBuilder(55);
        sb.append("Request { what=");
        sb.append(i);
        sb.append(" id=");
        sb.append(i2);
        sb.append(" oneWay=");
        sb.append(zzcje);
        sb.append(VectorFormat.DEFAULT_SUFFIX);
        return sb.toString();
    }

    /* access modifiers changed from: 0000 */
    public abstract void zzac(Bundle bundle);

    /* access modifiers changed from: 0000 */
    public final void zzb(zzs zzs) {
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            String valueOf = String.valueOf(this);
            String valueOf2 = String.valueOf(zzs);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 14 + String.valueOf(valueOf2).length());
            sb.append("Failing ");
            sb.append(valueOf);
            sb.append(" with ");
            sb.append(valueOf2);
            Log.d("MessengerIpcClient", sb.toString());
        }
        this.zzgrq.setException(zzs);
    }

    /* access modifiers changed from: 0000 */
    public abstract boolean zzcje();
}

package com.google.android.gms.common.api.internal;

import android.support.annotation.NonNull;
import android.support.p001v4.util.ArrayMap;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.AvailabilityException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.util.Collections;
import java.util.Map;

final class zzac implements OnCompleteListener<Map<zzh<?>, String>> {
    private /* synthetic */ zzaa zzfqm;

    private zzac(zzaa zzaa) {
        this.zzfqm = zzaa;
    }

    public final void onComplete(@NonNull Task<Map<zzh<?>, String>> task) {
        zzaa zzaa;
        ConnectionResult connectionResult;
        Map zzd;
        this.zzfqm.zzfps.lock();
        try {
            if (this.zzfqm.zzfqh) {
                if (task.isSuccessful()) {
                    this.zzfqm.zzfqi = new ArrayMap(this.zzfqm.zzfpy.size());
                    for (zzz zzagn : this.zzfqm.zzfpy.values()) {
                        this.zzfqm.zzfqi.put(zzagn.zzagn(), ConnectionResult.zzfkr);
                    }
                } else {
                    if (task.getException() instanceof AvailabilityException) {
                        AvailabilityException availabilityException = (AvailabilityException) task.getException();
                        if (this.zzfqm.zzfqf) {
                            this.zzfqm.zzfqi = new ArrayMap(this.zzfqm.zzfpy.size());
                            for (zzz zzz : this.zzfqm.zzfpy.values()) {
                                zzh zzagn2 = zzz.zzagn();
                                ConnectionResult connectionResult2 = availabilityException.getConnectionResult(zzz);
                                if (this.zzfqm.zza(zzz, connectionResult2)) {
                                    zzd = this.zzfqm.zzfqi;
                                    connectionResult2 = new ConnectionResult(16);
                                } else {
                                    zzd = this.zzfqm.zzfqi;
                                }
                                zzd.put(zzagn2, connectionResult2);
                            }
                        } else {
                            this.zzfqm.zzfqi = availabilityException.zzagj();
                        }
                        zzaa = this.zzfqm;
                        connectionResult = this.zzfqm.zzaht();
                    } else {
                        Log.e("ConnectionlessGAC", "Unexpected availability exception", task.getException());
                        this.zzfqm.zzfqi = Collections.emptyMap();
                        zzaa = this.zzfqm;
                        connectionResult = new ConnectionResult(8);
                    }
                    zzaa.zzfql = connectionResult;
                }
                if (this.zzfqm.zzfqj != null) {
                    this.zzfqm.zzfqi.putAll(this.zzfqm.zzfqj);
                    this.zzfqm.zzfql = this.zzfqm.zzaht();
                }
                if (this.zzfqm.zzfql == null) {
                    this.zzfqm.zzahr();
                    this.zzfqm.zzahs();
                } else {
                    this.zzfqm.zzfqh = false;
                    this.zzfqm.zzfqb.zzc(this.zzfqm.zzfql);
                }
                this.zzfqm.zzfqd.signalAll();
            }
        } finally {
            this.zzfqm.zzfps.unlock();
        }
    }
}

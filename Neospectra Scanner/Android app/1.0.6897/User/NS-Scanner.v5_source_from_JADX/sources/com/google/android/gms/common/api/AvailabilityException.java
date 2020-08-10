package com.google.android.gms.common.api;

import android.support.p001v4.util.ArrayMap;
import android.text.TextUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.internal.zzh;
import com.google.android.gms.common.internal.zzbq;
import java.util.ArrayList;
import org.apache.commons.math3.geometry.VectorFormat;

public class AvailabilityException extends Exception {
    private final ArrayMap<zzh<?>, ConnectionResult> zzflw;

    public AvailabilityException(ArrayMap<zzh<?>, ConnectionResult> arrayMap) {
        this.zzflw = arrayMap;
    }

    public ConnectionResult getConnectionResult(GoogleApi<? extends ApiOptions> googleApi) {
        zzh zzagn = googleApi.zzagn();
        zzbq.checkArgument(this.zzflw.get(zzagn) != null, "The given API was not part of the availability request.");
        return (ConnectionResult) this.zzflw.get(zzagn);
    }

    public String getMessage() {
        ArrayList arrayList = new ArrayList();
        boolean z = true;
        for (zzh zzh : this.zzflw.keySet()) {
            ConnectionResult connectionResult = (ConnectionResult) this.zzflw.get(zzh);
            if (connectionResult.isSuccess()) {
                z = false;
            }
            String zzagy = zzh.zzagy();
            String valueOf = String.valueOf(connectionResult);
            StringBuilder sb = new StringBuilder(String.valueOf(zzagy).length() + 2 + String.valueOf(valueOf).length());
            sb.append(zzagy);
            sb.append(": ");
            sb.append(valueOf);
            arrayList.add(sb.toString());
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(z ? "None of the queried APIs are available. " : "Some of the queried APIs are unavailable. ");
        sb2.append(TextUtils.join(VectorFormat.DEFAULT_SEPARATOR, arrayList));
        return sb2.toString();
    }

    public final ArrayMap<zzh<?>, ConnectionResult> zzagj() {
        return this.zzflw;
    }
}

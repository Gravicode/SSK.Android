package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzab;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.common.internal.zzr;
import com.google.android.gms.dynamite.DynamiteModule;

public final class zzdxl extends zzab<zzdxp> implements zzdxk {
    private static zzbgg zzecc = new zzbgg("FirebaseAuth", "FirebaseAuth:");
    private final Context mContext;
    private final zzdxt zzmez;

    public zzdxl(Context context, Looper looper, zzr zzr, zzdxt zzdxt, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 112, zzr, connectionCallbacks, onConnectionFailedListener);
        this.mContext = (Context) zzbq.checkNotNull(context);
        this.zzmez = zzdxt;
    }

    /* access modifiers changed from: protected */
    public final Bundle zzaap() {
        Bundle zzaap = super.zzaap();
        if (zzaap == null) {
            zzaap = new Bundle();
        }
        if (this.zzmez != null) {
            zzaap.putString("com.google.firebase.auth.API_KEY", this.zzmez.getApiKey());
        }
        return zzaap;
    }

    public final boolean zzagg() {
        return DynamiteModule.zzab(this.mContext, "com.google.firebase.auth") == 0;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0037  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x004b  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0076  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.String zzakh() {
        /*
            r6 = this;
            java.lang.String r0 = "firebear.preference"
            java.lang.String r0 = com.google.android.gms.internal.zzdyh.getProperty(r0)
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            if (r1 == 0) goto L_0x000e
            java.lang.String r0 = "default"
        L_0x000e:
            int r1 = r0.hashCode()
            r2 = 103145323(0x625df6b, float:3.1197192E-35)
            r3 = -1
            r4 = 0
            if (r1 == r2) goto L_0x0029
            r5 = 1544803905(0x5c13d641, float:1.66449585E17)
            if (r1 == r5) goto L_0x001f
            goto L_0x0033
        L_0x001f:
            java.lang.String r1 = "default"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x0033
            r1 = 1
            goto L_0x0034
        L_0x0029:
            java.lang.String r1 = "local"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x0033
            r1 = 0
            goto L_0x0034
        L_0x0033:
            r1 = -1
        L_0x0034:
            switch(r1) {
                case 0: goto L_0x0039;
                case 1: goto L_0x0039;
                default: goto L_0x0037;
            }
        L_0x0037:
            java.lang.String r0 = "default"
        L_0x0039:
            int r1 = r0.hashCode()
            if (r1 == r2) goto L_0x0040
            goto L_0x0049
        L_0x0040:
            java.lang.String r1 = "local"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0049
            r3 = 0
        L_0x0049:
            if (r3 == 0) goto L_0x0076
            com.google.android.gms.internal.zzbgg r0 = zzecc
            java.lang.String r1 = "Loading module via FirebaseOptions."
            java.lang.Object[] r2 = new java.lang.Object[r4]
            r0.zze(r1, r2)
            com.google.android.gms.internal.zzdxt r0 = r6.zzmez
            boolean r0 = r0.zzmei
            if (r0 == 0) goto L_0x006a
            com.google.android.gms.internal.zzbgg r0 = zzecc
            java.lang.String r1 = "Preparing to create service connection to fallback implementation"
            java.lang.Object[] r2 = new java.lang.Object[r4]
            r0.zze(r1, r2)
            android.content.Context r0 = r6.mContext
            java.lang.String r0 = r0.getPackageName()
            return r0
        L_0x006a:
            com.google.android.gms.internal.zzbgg r0 = zzecc
            java.lang.String r1 = "Preparing to create service connection to gms implementation"
            java.lang.Object[] r2 = new java.lang.Object[r4]
            r0.zze(r1, r2)
            java.lang.String r0 = "com.google.android.gms"
            return r0
        L_0x0076:
            com.google.android.gms.internal.zzbgg r0 = zzecc
            java.lang.String r1 = "Loading fallback module override."
            java.lang.Object[] r2 = new java.lang.Object[r4]
            r0.zze(r1, r2)
            android.content.Context r0 = r6.mContext
            java.lang.String r0 = r0.getPackageName()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzdxl.zzakh():java.lang.String");
    }

    public final /* synthetic */ zzdxp zzbrm() throws DeadObjectException {
        return (zzdxp) super.zzakn();
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ IInterface zzd(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.firebase.auth.api.internal.IFirebaseAuthService");
        return queryLocalInterface instanceof zzdxp ? (zzdxp) queryLocalInterface : new zzdxq(iBinder);
    }

    /* access modifiers changed from: protected */
    public final String zzhi() {
        return "com.google.firebase.auth.api.gms.service.START";
    }

    /* access modifiers changed from: protected */
    public final String zzhj() {
        return "com.google.firebase.auth.api.internal.IFirebaseAuthService";
    }
}

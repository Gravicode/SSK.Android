package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.zzc;
import com.google.android.gms.common.zzf;
import com.google.android.gms.internal.zzbfm;
import com.google.android.gms.internal.zzbfp;

public final class zzz extends zzbfm {
    public static final Creator<zzz> CREATOR = new zzaa();
    private int version;
    private int zzfzr;
    private int zzfzs;
    String zzfzt;
    IBinder zzfzu;
    Scope[] zzfzv;
    Bundle zzfzw;
    Account zzfzx;
    zzc[] zzfzy;

    public zzz(int i) {
        this.version = 3;
        this.zzfzs = zzf.GOOGLE_PLAY_SERVICES_VERSION_CODE;
        this.zzfzr = i;
    }

    /* JADX WARNING: type inference failed for: r1v1 */
    /* JADX WARNING: type inference failed for: r1v2, types: [android.accounts.Account] */
    /* JADX WARNING: type inference failed for: r1v3, types: [com.google.android.gms.common.internal.zzan] */
    /* JADX WARNING: type inference failed for: r1v4, types: [android.accounts.Account] */
    /* JADX WARNING: type inference failed for: r1v7, types: [com.google.android.gms.common.internal.zzap] */
    /* JADX WARNING: type inference failed for: r1v8, types: [com.google.android.gms.common.internal.zzan] */
    /* JADX WARNING: type inference failed for: r1v9 */
    /* JADX WARNING: type inference failed for: r1v10 */
    /* JADX WARNING: type inference failed for: r1v11 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r1v1
      assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], com.google.android.gms.common.internal.zzap, android.accounts.Account, com.google.android.gms.common.internal.zzan]
      uses: [android.accounts.Account, com.google.android.gms.common.internal.zzan]
      mth insns count: 30
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 4 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    zzz(int r1, int r2, int r3, java.lang.String r4, android.os.IBinder r5, com.google.android.gms.common.api.Scope[] r6, android.os.Bundle r7, android.accounts.Account r8, com.google.android.gms.common.zzc[] r9) {
        /*
            r0 = this;
            r0.<init>()
            r0.version = r1
            r0.zzfzr = r2
            r0.zzfzs = r3
            java.lang.String r2 = "com.google.android.gms"
            boolean r2 = r2.equals(r4)
            if (r2 == 0) goto L_0x0016
            java.lang.String r2 = "com.google.android.gms"
            r0.zzfzt = r2
            goto L_0x0018
        L_0x0016:
            r0.zzfzt = r4
        L_0x0018:
            r2 = 2
            if (r1 >= r2) goto L_0x003a
            r1 = 0
            if (r5 == 0) goto L_0x0037
            if (r5 != 0) goto L_0x0021
            goto L_0x0033
        L_0x0021:
            java.lang.String r1 = "com.google.android.gms.common.internal.IAccountAccessor"
            android.os.IInterface r1 = r5.queryLocalInterface(r1)
            boolean r2 = r1 instanceof com.google.android.gms.common.internal.zzan
            if (r2 == 0) goto L_0x002e
            com.google.android.gms.common.internal.zzan r1 = (com.google.android.gms.common.internal.zzan) r1
            goto L_0x0033
        L_0x002e:
            com.google.android.gms.common.internal.zzap r1 = new com.google.android.gms.common.internal.zzap
            r1.<init>(r5)
        L_0x0033:
            android.accounts.Account r1 = com.google.android.gms.common.internal.zza.zza(r1)
        L_0x0037:
            r0.zzfzx = r1
            goto L_0x003e
        L_0x003a:
            r0.zzfzu = r5
            r0.zzfzx = r8
        L_0x003e:
            r0.zzfzv = r6
            r0.zzfzw = r7
            r0.zzfzy = r9
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.zzz.<init>(int, int, int, java.lang.String, android.os.IBinder, com.google.android.gms.common.api.Scope[], android.os.Bundle, android.accounts.Account, com.google.android.gms.common.zzc[]):void");
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int zze = zzbfp.zze(parcel);
        zzbfp.zzc(parcel, 1, this.version);
        zzbfp.zzc(parcel, 2, this.zzfzr);
        zzbfp.zzc(parcel, 3, this.zzfzs);
        zzbfp.zza(parcel, 4, this.zzfzt, false);
        zzbfp.zza(parcel, 5, this.zzfzu, false);
        zzbfp.zza(parcel, 6, (T[]) this.zzfzv, i, false);
        zzbfp.zza(parcel, 7, this.zzfzw, false);
        zzbfp.zza(parcel, 8, (Parcelable) this.zzfzx, i, false);
        zzbfp.zza(parcel, 10, (T[]) this.zzfzy, i, false);
        zzbfp.zzai(parcel, zze);
    }
}

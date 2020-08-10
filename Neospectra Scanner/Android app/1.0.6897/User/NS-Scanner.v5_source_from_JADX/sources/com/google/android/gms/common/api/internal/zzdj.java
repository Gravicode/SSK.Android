package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.Api.zzc;
import com.google.android.gms.common.api.Api.zze;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public final class zzdj {
    public static final Status zzfvg = new Status(8, "The connection to Google Play services was lost");
    private static final BasePendingResult<?>[] zzfvh = new BasePendingResult[0];
    private final Map<zzc<?>, zze> zzfsb;
    final Set<BasePendingResult<?>> zzfvi = Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap()));
    private final zzdm zzfvj = new zzdk(this);

    public zzdj(Map<zzc<?>, zze> map) {
        this.zzfsb = map;
    }

    /* JADX WARNING: type inference failed for: r5v0, types: [com.google.android.gms.common.api.ResultCallback, com.google.android.gms.common.api.zze, com.google.android.gms.common.api.internal.zzdk, com.google.android.gms.common.api.internal.zzdm] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r5v0, types: [com.google.android.gms.common.api.ResultCallback, com.google.android.gms.common.api.zze, com.google.android.gms.common.api.internal.zzdk, com.google.android.gms.common.api.internal.zzdm]
      assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY]]
      uses: [com.google.android.gms.common.api.internal.zzdm, com.google.android.gms.common.api.ResultCallback, com.google.android.gms.common.api.zze, com.google.android.gms.common.api.internal.zzdk]
      mth insns count: 42
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
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void release() {
        /*
            r8 = this;
            java.util.Set<com.google.android.gms.common.api.internal.BasePendingResult<?>> r0 = r8.zzfvi
            com.google.android.gms.common.api.internal.BasePendingResult<?>[] r1 = zzfvh
            java.lang.Object[] r0 = r0.toArray(r1)
            com.google.android.gms.common.api.internal.BasePendingResult[] r0 = (com.google.android.gms.common.api.internal.BasePendingResult[]) r0
            int r1 = r0.length
            r2 = 0
            r3 = 0
        L_0x000d:
            if (r3 >= r1) goto L_0x0077
            r4 = r0[r3]
            r5 = 0
            r4.zza(r5)
            java.lang.Integer r6 = r4.zzagv()
            if (r6 != 0) goto L_0x0027
            boolean r5 = r4.zzahh()
            if (r5 == 0) goto L_0x0074
        L_0x0021:
            java.util.Set<com.google.android.gms.common.api.internal.BasePendingResult<?>> r5 = r8.zzfvi
            r5.remove(r4)
            goto L_0x0074
        L_0x0027:
            r4.setResultCallback(r5)
            java.util.Map<com.google.android.gms.common.api.Api$zzc<?>, com.google.android.gms.common.api.Api$zze> r6 = r8.zzfsb
            r7 = r4
            com.google.android.gms.common.api.internal.zzm r7 = (com.google.android.gms.common.api.internal.zzm) r7
            com.google.android.gms.common.api.Api$zzc r7 = r7.zzagf()
            java.lang.Object r6 = r6.get(r7)
            com.google.android.gms.common.api.Api$zze r6 = (com.google.android.gms.common.api.Api.zze) r6
            android.os.IBinder r6 = r6.zzagh()
            boolean r7 = r4.isReady()
            if (r7 == 0) goto L_0x004c
            com.google.android.gms.common.api.internal.zzdl r7 = new com.google.android.gms.common.api.internal.zzdl
            r7.<init>(r4, r5, r6, r5)
            r4.zza(r7)
            goto L_0x0021
        L_0x004c:
            if (r6 == 0) goto L_0x0062
            boolean r7 = r6.isBinderAlive()
            if (r7 == 0) goto L_0x0062
            com.google.android.gms.common.api.internal.zzdl r7 = new com.google.android.gms.common.api.internal.zzdl
            r7.<init>(r4, r5, r6, r5)
            r4.zza(r7)
            r6.linkToDeath(r7, r2)     // Catch:{ RemoteException -> 0x0060 }
            goto L_0x0021
        L_0x0060:
            r6 = move-exception
            goto L_0x0065
        L_0x0062:
            r4.zza(r5)
        L_0x0065:
            r4.cancel()
            java.lang.Integer r6 = r4.zzagv()
            int r6 = r6.intValue()
            r5.remove(r6)
            goto L_0x0021
        L_0x0074:
            int r3 = r3 + 1
            goto L_0x000d
        L_0x0077:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.zzdj.release():void");
    }

    public final void zzaju() {
        for (BasePendingResult zzv : (BasePendingResult[]) this.zzfvi.toArray(zzfvh)) {
            zzv.zzv(zzfvg);
        }
    }

    /* access modifiers changed from: 0000 */
    public final void zzb(BasePendingResult<? extends Result> basePendingResult) {
        this.zzfvi.add(basePendingResult);
        basePendingResult.zza(this.zzfvj);
    }
}

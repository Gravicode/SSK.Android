package com.google.firebase.storage;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.TaskCompletionSource;

final class zzo implements OnCompleteListener<TResult> {
    private /* synthetic */ TaskCompletionSource zzgbh;
    private /* synthetic */ StorageTask zzojr;
    private /* synthetic */ Continuation zzojs;

    zzo(StorageTask storageTask, Continuation continuation, TaskCompletionSource taskCompletionSource) {
        this.zzojr = storageTask;
        this.zzojs = continuation;
        this.zzgbh = taskCompletionSource;
    }

    /* JADX WARNING: type inference failed for: r2v1, types: [com.google.android.gms.tasks.RuntimeExecutionException] */
    /* JADX WARNING: type inference failed for: r2v3, types: [java.lang.Exception] */
    /* JADX WARNING: type inference failed for: r2v4, types: [java.lang.Exception] */
    /* JADX WARNING: type inference failed for: r2v5 */
    /* JADX WARNING: type inference failed for: r2v6, types: [java.lang.Exception] */
    /* JADX WARNING: type inference failed for: r2v9 */
    /* JADX WARNING: type inference failed for: r2v10 */
    /* JADX WARNING: type inference failed for: r2v11 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v5
      assigns: []
      uses: []
      mth insns count: 25
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
    /* JADX WARNING: Unknown variable types count: 3 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void onComplete(@android.support.annotation.NonNull com.google.android.gms.tasks.Task<TResult> r2) {
        /*
            r1 = this;
            com.google.android.gms.tasks.Continuation r2 = r1.zzojs     // Catch:{ RuntimeExecutionException -> 0x0021, Exception -> 0x001a }
            com.google.firebase.storage.StorageTask r0 = r1.zzojr     // Catch:{ RuntimeExecutionException -> 0x0021, Exception -> 0x001a }
            java.lang.Object r2 = r2.then(r0)     // Catch:{ RuntimeExecutionException -> 0x0021, Exception -> 0x001a }
            com.google.android.gms.tasks.TaskCompletionSource r0 = r1.zzgbh
            com.google.android.gms.tasks.Task r0 = r0.getTask()
            boolean r0 = r0.isComplete()
            if (r0 != 0) goto L_0x0019
            com.google.android.gms.tasks.TaskCompletionSource r0 = r1.zzgbh
            r0.setResult(r2)
        L_0x0019:
            return
        L_0x001a:
            r2 = move-exception
        L_0x001b:
            com.google.android.gms.tasks.TaskCompletionSource r0 = r1.zzgbh
        L_0x001d:
            r0.setException(r2)
            return
        L_0x0021:
            r2 = move-exception
            java.lang.Throwable r0 = r2.getCause()
            boolean r0 = r0 instanceof java.lang.Exception
            if (r0 == 0) goto L_0x001b
            com.google.android.gms.tasks.TaskCompletionSource r0 = r1.zzgbh
            java.lang.Throwable r2 = r2.getCause()
            java.lang.Exception r2 = (java.lang.Exception) r2
            goto L_0x001d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.storage.zzo.onComplete(com.google.android.gms.tasks.Task):void");
    }
}

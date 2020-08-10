package com.google.firebase.storage;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.storage.StorageTask.ProvideError;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Executor;

public abstract class StorageTask<TResult extends ProvideError> extends ControllableTask<TResult> {
    private static final HashMap<Integer, HashSet<Integer>> zzojj = new HashMap<>();
    private static final HashMap<Integer, HashSet<Integer>> zzojk = new HashMap<>();
    protected final Object mSyncObject = new Object();
    private volatile int zzdfx = 1;
    @VisibleForTesting
    private zzx<OnSuccessListener<? super TResult>, TResult> zzojl = new zzx<>(this, 128, new zzj(this));
    @VisibleForTesting
    private zzx<OnFailureListener, TResult> zzojm = new zzx<>(this, 320, new zzk(this));
    @VisibleForTesting
    private zzx<OnCompleteListener<TResult>, TResult> zzojn = new zzx<>(this, 448, new zzl(this));
    @VisibleForTesting
    private zzx<OnProgressListener<? super TResult>, TResult> zzojo = new zzx<>(this, -465, new zzm(this));
    @VisibleForTesting
    private zzx<OnPausedListener<? super TResult>, TResult> zzojp = new zzx<>(this, 16, new zzn(this));
    private TResult zzojq;

    public interface ProvideError {
        Exception getError();
    }

    public class SnapshotBase implements ProvideError {
        private final Exception zzoju;

        public SnapshotBase(@Nullable Exception exc) {
            StorageException storageException;
            Status status;
            if (exc == null) {
                if (StorageTask.this.isCanceled()) {
                    status = Status.zzfnm;
                } else if (StorageTask.this.zzclt() == 64) {
                    status = Status.zzfnk;
                } else {
                    storageException = null;
                    this.zzoju = storageException;
                    return;
                }
                storageException = StorageException.fromErrorStatus(status);
                this.zzoju = storageException;
                return;
            }
            this.zzoju = exc;
        }

        @Nullable
        public Exception getError() {
            return this.zzoju;
        }

        @NonNull
        public StorageReference getStorage() {
            return getTask().getStorage();
        }

        @NonNull
        public StorageTask<TResult> getTask() {
            return StorageTask.this;
        }
    }

    static {
        zzojj.put(Integer.valueOf(1), new HashSet(Arrays.asList(new Integer[]{Integer.valueOf(16), Integer.valueOf(256)})));
        zzojj.put(Integer.valueOf(2), new HashSet(Arrays.asList(new Integer[]{Integer.valueOf(8), Integer.valueOf(32)})));
        zzojj.put(Integer.valueOf(4), new HashSet(Arrays.asList(new Integer[]{Integer.valueOf(8), Integer.valueOf(32)})));
        zzojj.put(Integer.valueOf(16), new HashSet(Arrays.asList(new Integer[]{Integer.valueOf(2), Integer.valueOf(256)})));
        zzojj.put(Integer.valueOf(64), new HashSet(Arrays.asList(new Integer[]{Integer.valueOf(2), Integer.valueOf(256)})));
        zzojk.put(Integer.valueOf(1), new HashSet(Arrays.asList(new Integer[]{Integer.valueOf(2), Integer.valueOf(64)})));
        zzojk.put(Integer.valueOf(2), new HashSet(Arrays.asList(new Integer[]{Integer.valueOf(4), Integer.valueOf(64), Integer.valueOf(128)})));
        zzojk.put(Integer.valueOf(4), new HashSet(Arrays.asList(new Integer[]{Integer.valueOf(4), Integer.valueOf(64), Integer.valueOf(128)})));
        zzojk.put(Integer.valueOf(8), new HashSet(Arrays.asList(new Integer[]{Integer.valueOf(16), Integer.valueOf(64), Integer.valueOf(128)})));
        zzojk.put(Integer.valueOf(32), new HashSet(Arrays.asList(new Integer[]{Integer.valueOf(256), Integer.valueOf(64), Integer.valueOf(128)})));
    }

    protected StorageTask() {
    }

    @NonNull
    private final <TContinuationResult> Task<TContinuationResult> zza(@Nullable Executor executor, @NonNull Continuation<TResult, TContinuationResult> continuation) {
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        this.zzojn.zza(null, executor, new zzo(this, continuation, taskCompletionSource));
        return taskCompletionSource.getTask();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00c9, code lost:
        return true;
     */
    @android.support.annotation.VisibleForTesting
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final boolean zza(int[] r9, boolean r10) {
        /*
            r8 = this;
            if (r10 == 0) goto L_0x0005
            java.util.HashMap<java.lang.Integer, java.util.HashSet<java.lang.Integer>> r0 = zzojj
            goto L_0x0007
        L_0x0005:
            java.util.HashMap<java.lang.Integer, java.util.HashSet<java.lang.Integer>> r0 = zzojk
        L_0x0007:
            java.lang.Object r1 = r8.mSyncObject
            monitor-enter(r1)
            int r2 = r9.length     // Catch:{ all -> 0x0113 }
            r3 = 0
            r4 = 0
        L_0x000d:
            if (r4 >= r2) goto L_0x00ce
            r5 = r9[r4]     // Catch:{ all -> 0x0113 }
            int r6 = r8.zzdfx     // Catch:{ all -> 0x0113 }
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ all -> 0x0113 }
            java.lang.Object r6 = r0.get(r6)     // Catch:{ all -> 0x0113 }
            java.util.HashSet r6 = (java.util.HashSet) r6     // Catch:{ all -> 0x0113 }
            if (r6 == 0) goto L_0x00ca
            java.lang.Integer r7 = java.lang.Integer.valueOf(r5)     // Catch:{ all -> 0x0113 }
            boolean r6 = r6.contains(r7)     // Catch:{ all -> 0x0113 }
            if (r6 == 0) goto L_0x00ca
            r8.zzdfx = r5     // Catch:{ all -> 0x0113 }
            int r9 = r8.zzdfx     // Catch:{ all -> 0x0113 }
            r0 = 2
            if (r9 == r0) goto L_0x0058
            r0 = 4
            if (r9 == r0) goto L_0x0054
            r0 = 16
            if (r9 == r0) goto L_0x0050
            r0 = 64
            if (r9 == r0) goto L_0x004c
            r0 = 128(0x80, float:1.794E-43)
            if (r9 == r0) goto L_0x0048
            r0 = 256(0x100, float:3.59E-43)
            if (r9 == r0) goto L_0x0044
            goto L_0x0062
        L_0x0044:
            r8.onCanceled()     // Catch:{ all -> 0x0113 }
            goto L_0x0062
        L_0x0048:
            r8.onSuccess()     // Catch:{ all -> 0x0113 }
            goto L_0x0062
        L_0x004c:
            r8.onFailure()     // Catch:{ all -> 0x0113 }
            goto L_0x0062
        L_0x0050:
            r8.onPaused()     // Catch:{ all -> 0x0113 }
            goto L_0x0062
        L_0x0054:
            r8.onProgress()     // Catch:{ all -> 0x0113 }
            goto L_0x0062
        L_0x0058:
            com.google.firebase.storage.zzt r9 = com.google.firebase.storage.zzt.zzclx()     // Catch:{ all -> 0x0113 }
            r9.zzb(r8)     // Catch:{ all -> 0x0113 }
            r8.onQueued()     // Catch:{ all -> 0x0113 }
        L_0x0062:
            com.google.firebase.storage.zzx<com.google.android.gms.tasks.OnSuccessListener<? super TResult>, TResult> r9 = r8.zzojl     // Catch:{ all -> 0x0113 }
            r9.zzcmb()     // Catch:{ all -> 0x0113 }
            com.google.firebase.storage.zzx<com.google.android.gms.tasks.OnFailureListener, TResult> r9 = r8.zzojm     // Catch:{ all -> 0x0113 }
            r9.zzcmb()     // Catch:{ all -> 0x0113 }
            com.google.firebase.storage.zzx<com.google.android.gms.tasks.OnCompleteListener<TResult>, TResult> r9 = r8.zzojn     // Catch:{ all -> 0x0113 }
            r9.zzcmb()     // Catch:{ all -> 0x0113 }
            com.google.firebase.storage.zzx<com.google.firebase.storage.OnPausedListener<? super TResult>, TResult> r9 = r8.zzojp     // Catch:{ all -> 0x0113 }
            r9.zzcmb()     // Catch:{ all -> 0x0113 }
            com.google.firebase.storage.zzx<com.google.firebase.storage.OnProgressListener<? super TResult>, TResult> r9 = r8.zzojo     // Catch:{ all -> 0x0113 }
            r9.zzcmb()     // Catch:{ all -> 0x0113 }
            java.lang.String r9 = "StorageTask"
            r0 = 3
            boolean r9 = android.util.Log.isLoggable(r9, r0)     // Catch:{ all -> 0x0113 }
            if (r9 == 0) goto L_0x00c7
            java.lang.String r9 = "StorageTask"
            java.lang.String r0 = zzij(r5)     // Catch:{ all -> 0x0113 }
            int r2 = r8.zzdfx     // Catch:{ all -> 0x0113 }
            java.lang.String r2 = zzij(r2)     // Catch:{ all -> 0x0113 }
            java.lang.String r3 = java.lang.String.valueOf(r0)     // Catch:{ all -> 0x0113 }
            int r3 = r3.length()     // Catch:{ all -> 0x0113 }
            int r3 = r3 + 53
            java.lang.String r4 = java.lang.String.valueOf(r2)     // Catch:{ all -> 0x0113 }
            int r4 = r4.length()     // Catch:{ all -> 0x0113 }
            int r3 = r3 + r4
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0113 }
            r4.<init>(r3)     // Catch:{ all -> 0x0113 }
            java.lang.String r3 = "changed internal state to: "
            r4.append(r3)     // Catch:{ all -> 0x0113 }
            r4.append(r0)     // Catch:{ all -> 0x0113 }
            java.lang.String r0 = " isUser: "
            r4.append(r0)     // Catch:{ all -> 0x0113 }
            r4.append(r10)     // Catch:{ all -> 0x0113 }
            java.lang.String r10 = " from state:"
            r4.append(r10)     // Catch:{ all -> 0x0113 }
            r4.append(r2)     // Catch:{ all -> 0x0113 }
            java.lang.String r10 = r4.toString()     // Catch:{ all -> 0x0113 }
            android.util.Log.d(r9, r10)     // Catch:{ all -> 0x0113 }
        L_0x00c7:
            monitor-exit(r1)     // Catch:{ all -> 0x0113 }
            r9 = 1
            return r9
        L_0x00ca:
            int r4 = r4 + 1
            goto L_0x000d
        L_0x00ce:
            java.lang.String r0 = "StorageTask"
            java.lang.String r9 = zzj(r9)     // Catch:{ all -> 0x0113 }
            int r2 = r8.zzdfx     // Catch:{ all -> 0x0113 }
            java.lang.String r2 = zzij(r2)     // Catch:{ all -> 0x0113 }
            java.lang.String r4 = java.lang.String.valueOf(r9)     // Catch:{ all -> 0x0113 }
            int r4 = r4.length()     // Catch:{ all -> 0x0113 }
            int r4 = r4 + 62
            java.lang.String r5 = java.lang.String.valueOf(r2)     // Catch:{ all -> 0x0113 }
            int r5 = r5.length()     // Catch:{ all -> 0x0113 }
            int r4 = r4 + r5
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x0113 }
            r5.<init>(r4)     // Catch:{ all -> 0x0113 }
            java.lang.String r4 = "unable to change internal state to: "
            r5.append(r4)     // Catch:{ all -> 0x0113 }
            r5.append(r9)     // Catch:{ all -> 0x0113 }
            java.lang.String r9 = " isUser: "
            r5.append(r9)     // Catch:{ all -> 0x0113 }
            r5.append(r10)     // Catch:{ all -> 0x0113 }
            java.lang.String r9 = " from state:"
            r5.append(r9)     // Catch:{ all -> 0x0113 }
            r5.append(r2)     // Catch:{ all -> 0x0113 }
            java.lang.String r9 = r5.toString()     // Catch:{ all -> 0x0113 }
            android.util.Log.w(r0, r9)     // Catch:{ all -> 0x0113 }
            monitor-exit(r1)     // Catch:{ all -> 0x0113 }
            return r3
        L_0x0113:
            r9 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0113 }
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.storage.StorageTask.zza(int[], boolean):boolean");
    }

    @NonNull
    private final <TContinuationResult> Task<TContinuationResult> zzb(@Nullable Executor executor, @NonNull Continuation<TResult, Task<TContinuationResult>> continuation) {
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        this.zzojn.zza(null, executor, new zzp(this, continuation, taskCompletionSource));
        return taskCompletionSource.getTask();
    }

    private final TResult zzclv() {
        if (this.zzojq != null) {
            return this.zzojq;
        }
        if (!isComplete()) {
            return null;
        }
        if (this.zzojq == null) {
            this.zzojq = zzclu();
        }
        return this.zzojq;
    }

    /* access modifiers changed from: private */
    public final void zzclw() {
        if (!isComplete() && !isPaused() && this.zzdfx != 2 && !zzk(256, false)) {
            zzk(64, false);
        }
    }

    private static String zzij(int i) {
        if (i == 4) {
            return "INTERNAL_STATE_IN_PROGRESS";
        }
        if (i == 8) {
            return "INTERNAL_STATE_PAUSING";
        }
        if (i == 16) {
            return "INTERNAL_STATE_PAUSED";
        }
        if (i == 32) {
            return "INTERNAL_STATE_CANCELING";
        }
        if (i == 64) {
            return "INTERNAL_STATE_FAILURE";
        }
        if (i == 128) {
            return "INTERNAL_STATE_SUCCESS";
        }
        if (i == 256) {
            return "INTERNAL_STATE_CANCELED";
        }
        switch (i) {
            case 1:
                return "INTERNAL_STATE_NOT_STARTED";
            case 2:
                return "INTERNAL_STATE_QUEUED";
            default:
                return "Unknown Internal State!";
        }
    }

    private static String zzj(int[] iArr) {
        if (iArr.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int zzij : iArr) {
            sb.append(zzij(zzij));
            sb.append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    @NonNull
    public StorageTask<TResult> addOnCompleteListener(@NonNull Activity activity, @NonNull OnCompleteListener<TResult> onCompleteListener) {
        zzbq.checkNotNull(onCompleteListener);
        zzbq.checkNotNull(activity);
        this.zzojn.zza(activity, null, onCompleteListener);
        return this;
    }

    @NonNull
    public StorageTask<TResult> addOnCompleteListener(@NonNull OnCompleteListener<TResult> onCompleteListener) {
        zzbq.checkNotNull(onCompleteListener);
        this.zzojn.zza(null, null, onCompleteListener);
        return this;
    }

    @NonNull
    public StorageTask<TResult> addOnCompleteListener(@NonNull Executor executor, @NonNull OnCompleteListener<TResult> onCompleteListener) {
        zzbq.checkNotNull(onCompleteListener);
        zzbq.checkNotNull(executor);
        this.zzojn.zza(null, executor, onCompleteListener);
        return this;
    }

    @NonNull
    public StorageTask<TResult> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
        zzbq.checkNotNull(onFailureListener);
        zzbq.checkNotNull(activity);
        this.zzojm.zza(activity, null, onFailureListener);
        return this;
    }

    @NonNull
    public StorageTask<TResult> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
        zzbq.checkNotNull(onFailureListener);
        this.zzojm.zza(null, null, onFailureListener);
        return this;
    }

    @NonNull
    public StorageTask<TResult> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
        zzbq.checkNotNull(onFailureListener);
        zzbq.checkNotNull(executor);
        this.zzojm.zza(null, executor, onFailureListener);
        return this;
    }

    public StorageTask<TResult> addOnPausedListener(@NonNull Activity activity, @NonNull OnPausedListener<? super TResult> onPausedListener) {
        zzbq.checkNotNull(onPausedListener);
        zzbq.checkNotNull(activity);
        this.zzojp.zza(activity, null, onPausedListener);
        return this;
    }

    public StorageTask<TResult> addOnPausedListener(@NonNull OnPausedListener<? super TResult> onPausedListener) {
        zzbq.checkNotNull(onPausedListener);
        this.zzojp.zza(null, null, onPausedListener);
        return this;
    }

    public StorageTask<TResult> addOnPausedListener(@NonNull Executor executor, @NonNull OnPausedListener<? super TResult> onPausedListener) {
        zzbq.checkNotNull(onPausedListener);
        zzbq.checkNotNull(executor);
        this.zzojp.zza(null, executor, onPausedListener);
        return this;
    }

    public StorageTask<TResult> addOnProgressListener(@NonNull Activity activity, @NonNull OnProgressListener<? super TResult> onProgressListener) {
        zzbq.checkNotNull(onProgressListener);
        zzbq.checkNotNull(activity);
        this.zzojo.zza(activity, null, onProgressListener);
        return this;
    }

    public StorageTask<TResult> addOnProgressListener(@NonNull OnProgressListener<? super TResult> onProgressListener) {
        zzbq.checkNotNull(onProgressListener);
        this.zzojo.zza(null, null, onProgressListener);
        return this;
    }

    public StorageTask<TResult> addOnProgressListener(@NonNull Executor executor, @NonNull OnProgressListener<? super TResult> onProgressListener) {
        zzbq.checkNotNull(onProgressListener);
        zzbq.checkNotNull(executor);
        this.zzojo.zza(null, executor, onProgressListener);
        return this;
    }

    @NonNull
    public StorageTask<TResult> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super TResult> onSuccessListener) {
        zzbq.checkNotNull(activity);
        zzbq.checkNotNull(onSuccessListener);
        this.zzojl.zza(activity, null, onSuccessListener);
        return this;
    }

    @NonNull
    public StorageTask<TResult> addOnSuccessListener(@NonNull OnSuccessListener<? super TResult> onSuccessListener) {
        zzbq.checkNotNull(onSuccessListener);
        this.zzojl.zza(null, null, onSuccessListener);
        return this;
    }

    @NonNull
    public StorageTask<TResult> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super TResult> onSuccessListener) {
        zzbq.checkNotNull(executor);
        zzbq.checkNotNull(onSuccessListener);
        this.zzojl.zza(null, executor, onSuccessListener);
        return this;
    }

    public boolean cancel() {
        return zza(new int[]{256, 32}, true);
    }

    @NonNull
    public <TContinuationResult> Task<TContinuationResult> continueWith(@NonNull Continuation<TResult, TContinuationResult> continuation) {
        return zza((Executor) null, continuation);
    }

    @NonNull
    public <TContinuationResult> Task<TContinuationResult> continueWith(@NonNull Executor executor, @NonNull Continuation<TResult, TContinuationResult> continuation) {
        return zza(executor, continuation);
    }

    @NonNull
    public <TContinuationResult> Task<TContinuationResult> continueWithTask(@NonNull Continuation<TResult, Task<TContinuationResult>> continuation) {
        return zzb(null, continuation);
    }

    @NonNull
    public <TContinuationResult> Task<TContinuationResult> continueWithTask(@NonNull Executor executor, @NonNull Continuation<TResult, Task<TContinuationResult>> continuation) {
        return zzb(executor, continuation);
    }

    @Nullable
    public Exception getException() {
        if (zzclv() == null) {
            return null;
        }
        return zzclv().getError();
    }

    public TResult getResult() {
        if (zzclv() == null) {
            throw new IllegalStateException();
        }
        Exception error = zzclv().getError();
        if (error == null) {
            return zzclv();
        }
        throw new RuntimeExecutionException(error);
    }

    public <X extends Throwable> TResult getResult(@NonNull Class<X> cls) throws Throwable {
        if (zzclv() == null) {
            throw new IllegalStateException();
        } else if (cls.isInstance(zzclv().getError())) {
            throw ((Throwable) cls.cast(zzclv().getError()));
        } else {
            Exception error = zzclv().getError();
            if (error == null) {
                return zzclv();
            }
            throw new RuntimeExecutionException(error);
        }
    }

    public TResult getSnapshot() {
        return zzclu();
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public abstract StorageReference getStorage();

    public boolean isCanceled() {
        return this.zzdfx == 256;
    }

    public boolean isComplete() {
        return ((this.zzdfx & 128) == 0 && (this.zzdfx & 320) == 0) ? false : true;
    }

    public boolean isInProgress() {
        return (this.zzdfx & -465) != 0;
    }

    public boolean isPaused() {
        return (this.zzdfx & 16) != 0;
    }

    public boolean isSuccessful() {
        return (this.zzdfx & 128) != 0;
    }

    /* access modifiers changed from: protected */
    public void onCanceled() {
    }

    /* access modifiers changed from: protected */
    public void onFailure() {
    }

    /* access modifiers changed from: protected */
    public void onPaused() {
    }

    /* access modifiers changed from: protected */
    public void onProgress() {
    }

    /* access modifiers changed from: protected */
    public void onQueued() {
    }

    /* access modifiers changed from: protected */
    public void onSuccess() {
    }

    public boolean pause() {
        return zza(new int[]{16, 8}, true);
    }

    public StorageTask<TResult> removeOnCompleteListener(@NonNull OnCompleteListener<TResult> onCompleteListener) {
        zzbq.checkNotNull(onCompleteListener);
        this.zzojn.zzcl(onCompleteListener);
        return this;
    }

    public StorageTask<TResult> removeOnFailureListener(@NonNull OnFailureListener onFailureListener) {
        zzbq.checkNotNull(onFailureListener);
        this.zzojm.zzcl(onFailureListener);
        return this;
    }

    public StorageTask<TResult> removeOnPausedListener(@NonNull OnPausedListener<? super TResult> onPausedListener) {
        zzbq.checkNotNull(onPausedListener);
        this.zzojp.zzcl(onPausedListener);
        return this;
    }

    public StorageTask<TResult> removeOnProgressListener(@NonNull OnProgressListener<? super TResult> onProgressListener) {
        zzbq.checkNotNull(onProgressListener);
        this.zzojo.zzcl(onProgressListener);
        return this;
    }

    public StorageTask<TResult> removeOnSuccessListener(@NonNull OnSuccessListener<? super TResult> onSuccessListener) {
        zzbq.checkNotNull(onSuccessListener);
        this.zzojl.zzcl(onSuccessListener);
        return this;
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void resetState() {
    }

    public boolean resume() {
        if (!zzk(2, true)) {
            return false;
        }
        resetState();
        schedule();
        return true;
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public abstract void run();

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public abstract void schedule();

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public final Runnable zzbko() {
        return new zzs(this);
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    @NonNull
    public abstract TResult zzcln();

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public final boolean zzcls() {
        if (!zzk(2, false)) {
            return false;
        }
        schedule();
        return true;
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public final int zzclt() {
        return this.zzdfx;
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    @NonNull
    public final TResult zzclu() {
        TResult zzcln;
        synchronized (this.mSyncObject) {
            zzcln = zzcln();
        }
        return zzcln;
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public final boolean zzk(int i, boolean z) {
        return zza(new int[]{i}, z);
    }
}

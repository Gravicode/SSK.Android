package com.google.android.gms.tasks;

import android.app.Activity;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.api.internal.LifecycleCallback;
import com.google.android.gms.common.api.internal.zzcf;
import com.google.android.gms.common.internal.zzbq;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

final class zzn<TResult> extends Task<TResult> {
    private final Object mLock = new Object();
    private final zzl<TResult> zzkun = new zzl<>();
    private boolean zzkuo;
    private TResult zzkup;
    private Exception zzkuq;

    static class zza extends LifecycleCallback {
        private final List<WeakReference<zzk<?>>> zzezo = new ArrayList();

        private zza(zzcf zzcf) {
            super(zzcf);
            this.zzfud.zza("TaskOnStopCallback", (LifecycleCallback) this);
        }

        public static zza zzr(Activity activity) {
            zzcf zzn = zzn(activity);
            zza zza = (zza) zzn.zza("TaskOnStopCallback", zza.class);
            return zza == null ? new zza(zzn) : zza;
        }

        @MainThread
        public final void onStop() {
            synchronized (this.zzezo) {
                for (WeakReference weakReference : this.zzezo) {
                    zzk zzk = (zzk) weakReference.get();
                    if (zzk != null) {
                        zzk.cancel();
                    }
                }
                this.zzezo.clear();
            }
        }

        public final <T> void zzb(zzk<T> zzk) {
            synchronized (this.zzezo) {
                this.zzezo.add(new WeakReference(zzk));
            }
        }
    }

    zzn() {
    }

    private final void zzbjk() {
        zzbq.zza(this.zzkuo, (Object) "Task is not yet complete");
    }

    private final void zzbjl() {
        zzbq.zza(!this.zzkuo, (Object) "Task is already complete");
    }

    private final void zzbjm() {
        synchronized (this.mLock) {
            if (this.zzkuo) {
                this.zzkun.zzb(this);
            }
        }
    }

    @NonNull
    public final Task<TResult> addOnCompleteListener(@NonNull Activity activity, @NonNull OnCompleteListener<TResult> onCompleteListener) {
        zze zze = new zze(TaskExecutors.MAIN_THREAD, onCompleteListener);
        this.zzkun.zza(zze);
        zza.zzr(activity).zzb(zze);
        zzbjm();
        return this;
    }

    @NonNull
    public final Task<TResult> addOnCompleteListener(@NonNull OnCompleteListener<TResult> onCompleteListener) {
        return addOnCompleteListener(TaskExecutors.MAIN_THREAD, onCompleteListener);
    }

    @NonNull
    public final Task<TResult> addOnCompleteListener(@NonNull Executor executor, @NonNull OnCompleteListener<TResult> onCompleteListener) {
        this.zzkun.zza(new zze(executor, onCompleteListener));
        zzbjm();
        return this;
    }

    @NonNull
    public final Task<TResult> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
        zzg zzg = new zzg(TaskExecutors.MAIN_THREAD, onFailureListener);
        this.zzkun.zza(zzg);
        zza.zzr(activity).zzb(zzg);
        zzbjm();
        return this;
    }

    @NonNull
    public final Task<TResult> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
        return addOnFailureListener(TaskExecutors.MAIN_THREAD, onFailureListener);
    }

    @NonNull
    public final Task<TResult> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
        this.zzkun.zza(new zzg(executor, onFailureListener));
        zzbjm();
        return this;
    }

    @NonNull
    public final Task<TResult> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super TResult> onSuccessListener) {
        zzi zzi = new zzi(TaskExecutors.MAIN_THREAD, onSuccessListener);
        this.zzkun.zza(zzi);
        zza.zzr(activity).zzb(zzi);
        zzbjm();
        return this;
    }

    @NonNull
    public final Task<TResult> addOnSuccessListener(@NonNull OnSuccessListener<? super TResult> onSuccessListener) {
        return addOnSuccessListener(TaskExecutors.MAIN_THREAD, onSuccessListener);
    }

    @NonNull
    public final Task<TResult> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super TResult> onSuccessListener) {
        this.zzkun.zza(new zzi(executor, onSuccessListener));
        zzbjm();
        return this;
    }

    @NonNull
    public final <TContinuationResult> Task<TContinuationResult> continueWith(@NonNull Continuation<TResult, TContinuationResult> continuation) {
        return continueWith(TaskExecutors.MAIN_THREAD, continuation);
    }

    @NonNull
    public final <TContinuationResult> Task<TContinuationResult> continueWith(@NonNull Executor executor, @NonNull Continuation<TResult, TContinuationResult> continuation) {
        zzn zzn = new zzn();
        this.zzkun.zza(new zza(executor, continuation, zzn));
        zzbjm();
        return zzn;
    }

    @NonNull
    public final <TContinuationResult> Task<TContinuationResult> continueWithTask(@NonNull Continuation<TResult, Task<TContinuationResult>> continuation) {
        return continueWithTask(TaskExecutors.MAIN_THREAD, continuation);
    }

    @NonNull
    public final <TContinuationResult> Task<TContinuationResult> continueWithTask(@NonNull Executor executor, @NonNull Continuation<TResult, Task<TContinuationResult>> continuation) {
        zzn zzn = new zzn();
        this.zzkun.zza(new zzc(executor, continuation, zzn));
        zzbjm();
        return zzn;
    }

    @Nullable
    public final Exception getException() {
        Exception exc;
        synchronized (this.mLock) {
            exc = this.zzkuq;
        }
        return exc;
    }

    public final TResult getResult() {
        TResult tresult;
        synchronized (this.mLock) {
            zzbjk();
            if (this.zzkuq != null) {
                throw new RuntimeExecutionException(this.zzkuq);
            }
            tresult = this.zzkup;
        }
        return tresult;
    }

    public final <X extends Throwable> TResult getResult(@NonNull Class<X> cls) throws Throwable {
        TResult tresult;
        synchronized (this.mLock) {
            zzbjk();
            if (cls.isInstance(this.zzkuq)) {
                throw ((Throwable) cls.cast(this.zzkuq));
            } else if (this.zzkuq != null) {
                throw new RuntimeExecutionException(this.zzkuq);
            } else {
                tresult = this.zzkup;
            }
        }
        return tresult;
    }

    public final boolean isComplete() {
        boolean z;
        synchronized (this.mLock) {
            z = this.zzkuo;
        }
        return z;
    }

    public final boolean isSuccessful() {
        boolean z;
        synchronized (this.mLock) {
            z = this.zzkuo && this.zzkuq == null;
        }
        return z;
    }

    public final void setException(@NonNull Exception exc) {
        zzbq.checkNotNull(exc, "Exception must not be null");
        synchronized (this.mLock) {
            zzbjl();
            this.zzkuo = true;
            this.zzkuq = exc;
        }
        this.zzkun.zzb(this);
    }

    public final void setResult(TResult tresult) {
        synchronized (this.mLock) {
            zzbjl();
            this.zzkuo = true;
            this.zzkup = tresult;
        }
        this.zzkun.zzb(this);
    }

    public final boolean trySetException(@NonNull Exception exc) {
        zzbq.checkNotNull(exc, "Exception must not be null");
        synchronized (this.mLock) {
            if (this.zzkuo) {
                return false;
            }
            this.zzkuo = true;
            this.zzkuq = exc;
            this.zzkun.zzb(this);
            return true;
        }
    }

    public final boolean trySetResult(TResult tresult) {
        synchronized (this.mLock) {
            if (this.zzkuo) {
                return false;
            }
            this.zzkuo = true;
            this.zzkup = tresult;
            this.zzkun.zzb(this);
            return true;
        }
    }
}

package com.google.android.gms.tasks;

import android.support.annotation.NonNull;
import com.google.android.gms.common.internal.zzbq;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class Tasks {

    static final class zza implements zzb {
        private final CountDownLatch zzapd;

        private zza() {
            this.zzapd = new CountDownLatch(1);
        }

        /* synthetic */ zza(zzo zzo) {
            this();
        }

        public final void await() throws InterruptedException {
            this.zzapd.await();
        }

        public final boolean await(long j, TimeUnit timeUnit) throws InterruptedException {
            return this.zzapd.await(j, timeUnit);
        }

        public final void onFailure(@NonNull Exception exc) {
            this.zzapd.countDown();
        }

        public final void onSuccess(Object obj) {
            this.zzapd.countDown();
        }
    }

    interface zzb extends OnFailureListener, OnSuccessListener<Object> {
    }

    static final class zzc implements zzb {
        private final Object mLock = new Object();
        private final zzn<Void> zzkul;
        private Exception zzkuq;
        private final int zzkut;
        private int zzkuu;
        private int zzkuv;

        public zzc(int i, zzn<Void> zzn) {
            this.zzkut = i;
            this.zzkul = zzn;
        }

        private final void zzbjn() {
            if (this.zzkuu + this.zzkuv == this.zzkut) {
                if (this.zzkuq == null) {
                    this.zzkul.setResult(null);
                    return;
                }
                zzn<Void> zzn = this.zzkul;
                int i = this.zzkuv;
                int i2 = this.zzkut;
                StringBuilder sb = new StringBuilder(54);
                sb.append(i);
                sb.append(" out of ");
                sb.append(i2);
                sb.append(" underlying tasks failed");
                zzn.setException(new ExecutionException(sb.toString(), this.zzkuq));
            }
        }

        public final void onFailure(@NonNull Exception exc) {
            synchronized (this.mLock) {
                this.zzkuv++;
                this.zzkuq = exc;
                zzbjn();
            }
        }

        public final void onSuccess(Object obj) {
            synchronized (this.mLock) {
                this.zzkuu++;
                zzbjn();
            }
        }
    }

    private Tasks() {
    }

    public static <TResult> TResult await(@NonNull Task<TResult> task) throws ExecutionException, InterruptedException {
        zzbq.zzgn("Must not be called on the main application thread");
        zzbq.checkNotNull(task, "Task must not be null");
        if (task.isComplete()) {
            return zzc(task);
        }
        zza zza2 = new zza(null);
        zza(task, zza2);
        zza2.await();
        return zzc(task);
    }

    public static <TResult> TResult await(@NonNull Task<TResult> task, long j, @NonNull TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        zzbq.zzgn("Must not be called on the main application thread");
        zzbq.checkNotNull(task, "Task must not be null");
        zzbq.checkNotNull(timeUnit, "TimeUnit must not be null");
        if (task.isComplete()) {
            return zzc(task);
        }
        zza zza2 = new zza(null);
        zza(task, zza2);
        if (zza2.await(j, timeUnit)) {
            return zzc(task);
        }
        throw new TimeoutException("Timed out waiting for Task");
    }

    public static <TResult> Task<TResult> call(@NonNull Callable<TResult> callable) {
        return call(TaskExecutors.MAIN_THREAD, callable);
    }

    public static <TResult> Task<TResult> call(@NonNull Executor executor, @NonNull Callable<TResult> callable) {
        zzbq.checkNotNull(executor, "Executor must not be null");
        zzbq.checkNotNull(callable, "Callback must not be null");
        zzn zzn = new zzn();
        executor.execute(new zzo(zzn, callable));
        return zzn;
    }

    public static <TResult> Task<TResult> forException(@NonNull Exception exc) {
        zzn zzn = new zzn();
        zzn.setException(exc);
        return zzn;
    }

    public static <TResult> Task<TResult> forResult(TResult tresult) {
        zzn zzn = new zzn();
        zzn.setResult(tresult);
        return zzn;
    }

    public static Task<Void> whenAll(Collection<? extends Task<?>> collection) {
        if (collection.isEmpty()) {
            return forResult(null);
        }
        for (Task task : collection) {
            if (task == null) {
                throw new NullPointerException("null tasks are not accepted");
            }
        }
        zzn zzn = new zzn();
        zzc zzc2 = new zzc(collection.size(), zzn);
        for (Task zza2 : collection) {
            zza(zza2, zzc2);
        }
        return zzn;
    }

    public static Task<Void> whenAll(Task<?>... taskArr) {
        return taskArr.length == 0 ? forResult(null) : whenAll((Collection<? extends Task<?>>) Arrays.asList(taskArr));
    }

    public static Task<List<Task<?>>> whenAllComplete(Collection<? extends Task<?>> collection) {
        return whenAll(collection).continueWith(new zzq(collection));
    }

    public static Task<List<Task<?>>> whenAllComplete(Task<?>... taskArr) {
        return whenAllComplete((Collection<? extends Task<?>>) Arrays.asList(taskArr));
    }

    public static <TResult> Task<List<TResult>> whenAllSuccess(Collection<? extends Task<?>> collection) {
        return whenAll(collection).continueWith(new zzp(collection));
    }

    public static <TResult> Task<List<TResult>> whenAllSuccess(Task<?>... taskArr) {
        return whenAllSuccess((Collection<? extends Task<?>>) Arrays.asList(taskArr));
    }

    private static void zza(Task<?> task, zzb zzb2) {
        task.addOnSuccessListener(TaskExecutors.zzkum, (OnSuccessListener<? super TResult>) zzb2);
        task.addOnFailureListener(TaskExecutors.zzkum, (OnFailureListener) zzb2);
    }

    private static <TResult> TResult zzc(Task<TResult> task) throws ExecutionException {
        if (task.isSuccessful()) {
            return task.getResult();
        }
        throw new ExecutionException(task.getException());
    }
}

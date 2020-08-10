package com.google.firebase.storage;

import android.support.annotation.NonNull;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

final class zzp implements OnCompleteListener<TResult> {
    final /* synthetic */ TaskCompletionSource zzgbh;
    private /* synthetic */ StorageTask zzojr;
    private /* synthetic */ Continuation zzojs;

    zzp(StorageTask storageTask, Continuation continuation, TaskCompletionSource taskCompletionSource) {
        this.zzojr = storageTask;
        this.zzojs = continuation;
        this.zzgbh = taskCompletionSource;
    }

    public final void onComplete(@NonNull Task<TResult> task) {
        try {
            Task task2 = (Task) this.zzojs.then(this.zzojr);
            if (!this.zzgbh.getTask().isComplete()) {
                if (task2 == null) {
                    this.zzgbh.setException(new NullPointerException("Continuation returned null"));
                } else {
                    task2.addOnSuccessListener(new zzq(this));
                    task2.addOnFailureListener(new zzr(this));
                }
            }
        } catch (RuntimeExecutionException e) {
            if (e.getCause() instanceof Exception) {
                this.zzgbh.setException((Exception) e.getCause());
            } else {
                this.zzgbh.setException(e);
            }
        } catch (Exception e2) {
            this.zzgbh.setException(e2);
        }
    }
}

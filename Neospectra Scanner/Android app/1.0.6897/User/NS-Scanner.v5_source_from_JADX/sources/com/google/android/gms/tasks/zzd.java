package com.google.android.gms.tasks;

final class zzd implements Runnable {
    private /* synthetic */ Task zzkua;
    private /* synthetic */ zzc zzkuc;

    zzd(zzc zzc, Task task) {
        this.zzkuc = zzc;
        this.zzkua = task;
    }

    public final void run() {
        try {
            Task task = (Task) this.zzkuc.zzkty.then(this.zzkua);
            if (task == null) {
                this.zzkuc.onFailure(new NullPointerException("Continuation returned null"));
                return;
            }
            task.addOnSuccessListener(TaskExecutors.zzkum, (OnSuccessListener<? super TResult>) this.zzkuc);
            task.addOnFailureListener(TaskExecutors.zzkum, (OnFailureListener) this.zzkuc);
        } catch (RuntimeExecutionException e) {
            if (e.getCause() instanceof Exception) {
                this.zzkuc.zzktz.setException((Exception) e.getCause());
            } else {
                this.zzkuc.zzktz.setException(e);
            }
        } catch (Exception e2) {
            this.zzkuc.zzktz.setException(e2);
        }
    }
}

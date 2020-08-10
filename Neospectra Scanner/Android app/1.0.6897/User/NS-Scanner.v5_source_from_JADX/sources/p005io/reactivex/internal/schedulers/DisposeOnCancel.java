package p005io.reactivex.internal.schedulers;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import p005io.reactivex.disposables.Disposable;

/* renamed from: io.reactivex.internal.schedulers.DisposeOnCancel */
final class DisposeOnCancel implements Future<Object> {

    /* renamed from: d */
    final Disposable f426d;

    DisposeOnCancel(Disposable d) {
        this.f426d = d;
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        this.f426d.dispose();
        return false;
    }

    public boolean isCancelled() {
        return false;
    }

    public boolean isDone() {
        return false;
    }

    public Object get() throws InterruptedException, ExecutionException {
        return null;
    }

    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }
}

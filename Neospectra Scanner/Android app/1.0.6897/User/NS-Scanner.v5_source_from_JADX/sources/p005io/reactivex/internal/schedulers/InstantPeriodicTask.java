package p005io.reactivex.internal.schedulers;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.functions.Functions;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.schedulers.InstantPeriodicTask */
final class InstantPeriodicTask implements Callable<Void>, Disposable {
    static final FutureTask<Void> CANCELLED = new FutureTask<>(Functions.EMPTY_RUNNABLE, null);
    final ExecutorService executor;
    final AtomicReference<Future<?>> first = new AtomicReference<>();
    final AtomicReference<Future<?>> rest = new AtomicReference<>();
    Thread runner;
    final Runnable task;

    InstantPeriodicTask(Runnable task2, ExecutorService executor2) {
        this.task = task2;
        this.executor = executor2;
    }

    public Void call() throws Exception {
        this.runner = Thread.currentThread();
        try {
            this.task.run();
            setRest(this.executor.submit(this));
            this.runner = null;
        } catch (Throwable ex) {
            this.runner = null;
            RxJavaPlugins.onError(ex);
        }
        return null;
    }

    public void dispose() {
        Future<?> current = (Future) this.first.getAndSet(CANCELLED);
        boolean z = false;
        if (!(current == null || current == CANCELLED)) {
            current.cancel(this.runner != Thread.currentThread());
        }
        Future<?> current2 = (Future) this.rest.getAndSet(CANCELLED);
        if (current2 != null && current2 != CANCELLED) {
            if (this.runner != Thread.currentThread()) {
                z = true;
            }
            current2.cancel(z);
        }
    }

    public boolean isDisposed() {
        return this.first.get() == CANCELLED;
    }

    /* access modifiers changed from: 0000 */
    public void setFirst(Future<?> f) {
        Future<?> current;
        do {
            current = (Future) this.first.get();
            if (current == CANCELLED) {
                f.cancel(this.runner != Thread.currentThread());
                return;
            }
        } while (!this.first.compareAndSet(current, f));
    }

    /* access modifiers changed from: 0000 */
    public void setRest(Future<?> f) {
        Future<?> current;
        do {
            current = (Future) this.rest.get();
            if (current == CANCELLED) {
                f.cancel(this.runner != Thread.currentThread());
                return;
            }
        } while (!this.rest.compareAndSet(current, f));
    }
}

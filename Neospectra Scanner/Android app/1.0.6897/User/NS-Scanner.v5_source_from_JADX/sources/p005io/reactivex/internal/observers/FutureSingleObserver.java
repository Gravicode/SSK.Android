package p005io.reactivex.internal.observers;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.util.BlockingHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.observers.FutureSingleObserver */
public final class FutureSingleObserver<T> extends CountDownLatch implements SingleObserver<T>, Future<T>, Disposable {
    Throwable error;

    /* renamed from: s */
    final AtomicReference<Disposable> f84s = new AtomicReference<>();
    T value;

    public FutureSingleObserver() {
        super(1);
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        Disposable a;
        do {
            a = (Disposable) this.f84s.get();
            if (a == this || a == DisposableHelper.DISPOSED) {
                return false;
            }
        } while (!this.f84s.compareAndSet(a, DisposableHelper.DISPOSED));
        if (a != null) {
            a.dispose();
        }
        countDown();
        return true;
    }

    public boolean isCancelled() {
        return DisposableHelper.isDisposed((Disposable) this.f84s.get());
    }

    public boolean isDone() {
        return getCount() == 0;
    }

    public T get() throws InterruptedException, ExecutionException {
        if (getCount() != 0) {
            BlockingHelper.verifyNonBlocking();
            await();
        }
        if (isCancelled()) {
            throw new CancellationException();
        }
        Throwable ex = this.error;
        if (ex == null) {
            return this.value;
        }
        throw new ExecutionException(ex);
    }

    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (getCount() != 0) {
            BlockingHelper.verifyNonBlocking();
            if (!await(timeout, unit)) {
                throw new TimeoutException();
            }
        }
        if (isCancelled()) {
            throw new CancellationException();
        }
        Throwable ex = this.error;
        if (ex == null) {
            return this.value;
        }
        throw new ExecutionException(ex);
    }

    public void onSubscribe(Disposable s) {
        DisposableHelper.setOnce(this.f84s, s);
    }

    public void onSuccess(T t) {
        Disposable a = (Disposable) this.f84s.get();
        if (a != DisposableHelper.DISPOSED) {
            this.value = t;
            this.f84s.compareAndSet(a, this);
            countDown();
        }
    }

    public void onError(Throwable t) {
        Disposable a;
        do {
            a = (Disposable) this.f84s.get();
            if (a == DisposableHelper.DISPOSED) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.error = t;
        } while (!this.f84s.compareAndSet(a, this));
        countDown();
    }

    public void dispose() {
    }

    public boolean isDisposed() {
        return isDone();
    }
}

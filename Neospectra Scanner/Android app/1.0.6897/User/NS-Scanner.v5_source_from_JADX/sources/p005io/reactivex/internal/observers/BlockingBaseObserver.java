package p005io.reactivex.internal.observers;

import java.util.concurrent.CountDownLatch;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.util.BlockingHelper;
import p005io.reactivex.internal.util.ExceptionHelper;

/* renamed from: io.reactivex.internal.observers.BlockingBaseObserver */
public abstract class BlockingBaseObserver<T> extends CountDownLatch implements Observer<T>, Disposable {
    volatile boolean cancelled;

    /* renamed from: d */
    Disposable f79d;
    Throwable error;
    T value;

    public BlockingBaseObserver() {
        super(1);
    }

    public final void onSubscribe(Disposable d) {
        this.f79d = d;
        if (this.cancelled) {
            d.dispose();
        }
    }

    public final void onComplete() {
        countDown();
    }

    public final void dispose() {
        this.cancelled = true;
        Disposable d = this.f79d;
        if (d != null) {
            d.dispose();
        }
    }

    public final boolean isDisposed() {
        return this.cancelled;
    }

    public final T blockingGet() {
        if (getCount() != 0) {
            try {
                BlockingHelper.verifyNonBlocking();
                await();
            } catch (InterruptedException ex) {
                dispose();
                throw ExceptionHelper.wrapOrThrow(ex);
            }
        }
        Throwable e = this.error;
        if (e == null) {
            return this.value;
        }
        throw ExceptionHelper.wrapOrThrow(e);
    }
}

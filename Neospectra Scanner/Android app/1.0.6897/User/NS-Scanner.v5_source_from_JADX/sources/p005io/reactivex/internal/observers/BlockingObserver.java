package p005io.reactivex.internal.observers;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.util.NotificationLite;

/* renamed from: io.reactivex.internal.observers.BlockingObserver */
public final class BlockingObserver<T> extends AtomicReference<Disposable> implements Observer<T>, Disposable {
    public static final Object TERMINATED = new Object();
    private static final long serialVersionUID = -4875965440900746268L;
    final Queue<Object> queue;

    public BlockingObserver(Queue<Object> queue2) {
        this.queue = queue2;
    }

    public void onSubscribe(Disposable s) {
        DisposableHelper.setOnce(this, s);
    }

    public void onNext(T t) {
        this.queue.offer(NotificationLite.next(t));
    }

    public void onError(Throwable t) {
        this.queue.offer(NotificationLite.error(t));
    }

    public void onComplete() {
        this.queue.offer(NotificationLite.complete());
    }

    public void dispose() {
        if (DisposableHelper.dispose(this)) {
            this.queue.offer(TERMINATED);
        }
    }

    public boolean isDisposed() {
        return get() == DisposableHelper.DISPOSED;
    }
}

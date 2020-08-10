package p008rx.internal.operators;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import p008rx.Observable;
import p008rx.Subscriber;
import p008rx.Subscription;

/* renamed from: rx.internal.operators.BlockingOperatorToFuture */
public final class BlockingOperatorToFuture {
    private BlockingOperatorToFuture() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Future<T> toFuture(Observable<? extends T> that) {
        final CountDownLatch finished = new CountDownLatch(1);
        final AtomicReference<T> value = new AtomicReference<>();
        final AtomicReference<Throwable> error = new AtomicReference<>();
        final Subscription s = that.single().subscribe((Subscriber<? super T>) new Subscriber<T>() {
            public void onCompleted() {
                finished.countDown();
            }

            public void onError(Throwable e) {
                error.compareAndSet(null, e);
                finished.countDown();
            }

            public void onNext(T v) {
                value.set(v);
            }
        });
        return new Future<T>() {
            private volatile boolean cancelled;

            public boolean cancel(boolean mayInterruptIfRunning) {
                if (finished.getCount() <= 0) {
                    return false;
                }
                this.cancelled = true;
                s.unsubscribe();
                finished.countDown();
                return true;
            }

            public boolean isCancelled() {
                return this.cancelled;
            }

            public boolean isDone() {
                return finished.getCount() == 0;
            }

            public T get() throws InterruptedException, ExecutionException {
                finished.await();
                return getValue();
            }

            public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                if (finished.await(timeout, unit)) {
                    return getValue();
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Timed out after ");
                sb.append(unit.toMillis(timeout));
                sb.append("ms waiting for underlying Observable.");
                throw new TimeoutException(sb.toString());
            }

            private T getValue() throws ExecutionException {
                Throwable throwable = (Throwable) error.get();
                if (throwable != null) {
                    throw new ExecutionException("Observable onError", throwable);
                } else if (!this.cancelled) {
                    return value.get();
                } else {
                    throw new CancellationException("Subscription unsubscribed");
                }
            }
        };
    }
}

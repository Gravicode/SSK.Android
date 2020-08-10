package p005io.reactivex.internal.subscribers;

import java.util.NoSuchElementException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscription;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.BlockingHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.subscribers.FutureSubscriber */
public final class FutureSubscriber<T> extends CountDownLatch implements FlowableSubscriber<T>, Future<T>, Subscription {
    Throwable error;

    /* renamed from: s */
    final AtomicReference<Subscription> f434s = new AtomicReference<>();
    T value;

    public FutureSubscriber() {
        super(1);
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        Subscription a;
        do {
            a = (Subscription) this.f434s.get();
            if (a == this || a == SubscriptionHelper.CANCELLED) {
                return false;
            }
        } while (!this.f434s.compareAndSet(a, SubscriptionHelper.CANCELLED));
        if (a != null) {
            a.cancel();
        }
        countDown();
        return true;
    }

    public boolean isCancelled() {
        return SubscriptionHelper.isCancelled((Subscription) this.f434s.get());
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

    public void onSubscribe(Subscription s) {
        SubscriptionHelper.setOnce(this.f434s, s, Long.MAX_VALUE);
    }

    public void onNext(T t) {
        if (this.value != null) {
            ((Subscription) this.f434s.get()).cancel();
            onError(new IndexOutOfBoundsException("More than one element received"));
            return;
        }
        this.value = t;
    }

    public void onError(Throwable t) {
        Subscription a;
        do {
            a = (Subscription) this.f434s.get();
            if (a == this || a == SubscriptionHelper.CANCELLED) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.error = t;
        } while (!this.f434s.compareAndSet(a, this));
        countDown();
    }

    public void onComplete() {
        Subscription a;
        if (this.value == null) {
            onError(new NoSuchElementException("The source is empty"));
            return;
        }
        do {
            a = (Subscription) this.f434s.get();
            if (a == this || a == SubscriptionHelper.CANCELLED) {
                return;
            }
        } while (!this.f434s.compareAndSet(a, this));
        countDown();
    }

    public void cancel() {
    }

    public void request(long n) {
    }
}

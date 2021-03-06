package p005io.reactivex.internal.subscribers;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.AtomicThrowable;
import p005io.reactivex.internal.util.HalfSerializer;

/* renamed from: io.reactivex.internal.subscribers.StrictSubscriber */
public class StrictSubscriber<T> extends AtomicInteger implements FlowableSubscriber<T>, Subscription {
    private static final long serialVersionUID = -4945028590049415624L;
    final Subscriber<? super T> actual;
    volatile boolean done;
    final AtomicThrowable error = new AtomicThrowable();
    final AtomicBoolean once = new AtomicBoolean();
    final AtomicLong requested = new AtomicLong();

    /* renamed from: s */
    final AtomicReference<Subscription> f454s = new AtomicReference<>();

    public StrictSubscriber(Subscriber<? super T> actual2) {
        this.actual = actual2;
    }

    public void request(long n) {
        if (n <= 0) {
            cancel();
            StringBuilder sb = new StringBuilder();
            sb.append("§3.9 violated: positive request amount required but it was ");
            sb.append(n);
            onError(new IllegalArgumentException(sb.toString()));
            return;
        }
        SubscriptionHelper.deferredRequest(this.f454s, this.requested, n);
    }

    public void cancel() {
        if (!this.done) {
            SubscriptionHelper.cancel(this.f454s);
        }
    }

    public void onSubscribe(Subscription s) {
        if (this.once.compareAndSet(false, true)) {
            this.actual.onSubscribe(this);
            SubscriptionHelper.deferredSetOnce(this.f454s, this.requested, s);
            return;
        }
        s.cancel();
        cancel();
        onError(new IllegalStateException("§2.12 violated: onSubscribe must be called at most once"));
    }

    public void onNext(T t) {
        HalfSerializer.onNext(this.actual, t, (AtomicInteger) this, this.error);
    }

    public void onError(Throwable t) {
        this.done = true;
        HalfSerializer.onError(this.actual, t, (AtomicInteger) this, this.error);
    }

    public void onComplete() {
        this.done = true;
        HalfSerializer.onComplete(this.actual, (AtomicInteger) this, this.error);
    }
}

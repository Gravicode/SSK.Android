package p005io.reactivex.internal.subscribers;

import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.BackpressureHelper;

/* renamed from: io.reactivex.internal.subscribers.SinglePostCompleteSubscriber */
public abstract class SinglePostCompleteSubscriber<T, R> extends AtomicLong implements FlowableSubscriber<T>, Subscription {
    static final long COMPLETE_MASK = Long.MIN_VALUE;
    static final long REQUEST_MASK = Long.MAX_VALUE;
    private static final long serialVersionUID = 7917814472626990048L;
    protected final Subscriber<? super R> actual;
    protected long produced;

    /* renamed from: s */
    protected Subscription f453s;
    protected R value;

    public SinglePostCompleteSubscriber(Subscriber<? super R> actual2) {
        this.actual = actual2;
    }

    public void onSubscribe(Subscription s) {
        if (SubscriptionHelper.validate(this.f453s, s)) {
            this.f453s = s;
            this.actual.onSubscribe(this);
        }
    }

    /* access modifiers changed from: protected */
    public final void complete(R n) {
        long p = this.produced;
        if (p != 0) {
            BackpressureHelper.produced(this, p);
        }
        while (true) {
            long r = get();
            if ((r & COMPLETE_MASK) != 0) {
                onDrop(n);
                return;
            } else if ((REQUEST_MASK & r) != 0) {
                lazySet(-9223372036854775807L);
                this.actual.onNext(n);
                this.actual.onComplete();
                return;
            } else {
                this.value = n;
                if (!compareAndSet(0, COMPLETE_MASK)) {
                    this.value = null;
                } else {
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDrop(R r) {
    }

    public final void request(long n) {
        long r;
        if (SubscriptionHelper.validate(n)) {
            do {
                r = get();
                if ((r & COMPLETE_MASK) != 0) {
                    if (compareAndSet(COMPLETE_MASK, -9223372036854775807L)) {
                        this.actual.onNext(this.value);
                        this.actual.onComplete();
                        return;
                    }
                    return;
                }
            } while (!compareAndSet(r, BackpressureHelper.addCap(r, n)));
            this.f453s.request(n);
        }
    }

    public void cancel() {
        this.f453s.cancel();
    }
}

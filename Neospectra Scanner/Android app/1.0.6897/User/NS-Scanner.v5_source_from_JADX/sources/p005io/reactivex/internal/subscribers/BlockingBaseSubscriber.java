package p005io.reactivex.internal.subscribers;

import java.util.concurrent.CountDownLatch;
import org.reactivestreams.Subscription;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.BlockingHelper;
import p005io.reactivex.internal.util.ExceptionHelper;

/* renamed from: io.reactivex.internal.subscribers.BlockingBaseSubscriber */
public abstract class BlockingBaseSubscriber<T> extends CountDownLatch implements FlowableSubscriber<T> {
    volatile boolean cancelled;
    Throwable error;

    /* renamed from: s */
    Subscription f432s;
    T value;

    public BlockingBaseSubscriber() {
        super(1);
    }

    public final void onSubscribe(Subscription s) {
        if (SubscriptionHelper.validate(this.f432s, s)) {
            this.f432s = s;
            if (!this.cancelled) {
                s.request(Long.MAX_VALUE);
                if (this.cancelled) {
                    this.f432s = SubscriptionHelper.CANCELLED;
                    s.cancel();
                }
            }
        }
    }

    public final void onComplete() {
        countDown();
    }

    public final T blockingGet() {
        if (getCount() != 0) {
            try {
                BlockingHelper.verifyNonBlocking();
                await();
            } catch (InterruptedException ex) {
                Subscription s = this.f432s;
                this.f432s = SubscriptionHelper.CANCELLED;
                if (s != null) {
                    s.cancel();
                }
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

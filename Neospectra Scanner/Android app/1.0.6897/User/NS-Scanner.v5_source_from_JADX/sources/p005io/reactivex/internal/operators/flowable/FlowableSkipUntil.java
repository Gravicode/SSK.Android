package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.internal.fuseable.ConditionalSubscriber;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.AtomicThrowable;
import p005io.reactivex.internal.util.HalfSerializer;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableSkipUntil */
public final class FlowableSkipUntil<T, U> extends AbstractFlowableWithUpstream<T, T> {
    final Publisher<U> other;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableSkipUntil$SkipUntilMainSubscriber */
    static final class SkipUntilMainSubscriber<T> extends AtomicInteger implements ConditionalSubscriber<T>, Subscription {
        private static final long serialVersionUID = -6270983465606289181L;
        final Subscriber<? super T> actual;
        final AtomicThrowable error = new AtomicThrowable();
        volatile boolean gate;
        final OtherSubscriber other = new OtherSubscriber<>();
        final AtomicLong requested = new AtomicLong();

        /* renamed from: s */
        final AtomicReference<Subscription> f207s = new AtomicReference<>();

        /* renamed from: io.reactivex.internal.operators.flowable.FlowableSkipUntil$SkipUntilMainSubscriber$OtherSubscriber */
        final class OtherSubscriber extends AtomicReference<Subscription> implements FlowableSubscriber<Object> {
            private static final long serialVersionUID = -5592042965931999169L;

            OtherSubscriber() {
            }

            public void onSubscribe(Subscription s) {
                SubscriptionHelper.setOnce(this, s, Long.MAX_VALUE);
            }

            public void onNext(Object t) {
                SkipUntilMainSubscriber.this.gate = true;
                ((Subscription) get()).cancel();
            }

            public void onError(Throwable t) {
                SubscriptionHelper.cancel(SkipUntilMainSubscriber.this.f207s);
                HalfSerializer.onError(SkipUntilMainSubscriber.this.actual, t, (AtomicInteger) SkipUntilMainSubscriber.this, SkipUntilMainSubscriber.this.error);
            }

            public void onComplete() {
                SkipUntilMainSubscriber.this.gate = true;
            }
        }

        SkipUntilMainSubscriber(Subscriber<? super T> actual2) {
            this.actual = actual2;
        }

        public void onSubscribe(Subscription s) {
            SubscriptionHelper.deferredSetOnce(this.f207s, this.requested, s);
        }

        public void onNext(T t) {
            if (!tryOnNext(t)) {
                ((Subscription) this.f207s.get()).request(1);
            }
        }

        public boolean tryOnNext(T t) {
            if (!this.gate) {
                return false;
            }
            HalfSerializer.onNext(this.actual, t, (AtomicInteger) this, this.error);
            return true;
        }

        public void onError(Throwable t) {
            SubscriptionHelper.cancel(this.other);
            HalfSerializer.onError(this.actual, t, (AtomicInteger) this, this.error);
        }

        public void onComplete() {
            SubscriptionHelper.cancel(this.other);
            HalfSerializer.onComplete(this.actual, (AtomicInteger) this, this.error);
        }

        public void request(long n) {
            SubscriptionHelper.deferredRequest(this.f207s, this.requested, n);
        }

        public void cancel() {
            SubscriptionHelper.cancel(this.f207s);
            SubscriptionHelper.cancel(this.other);
        }
    }

    public FlowableSkipUntil(Flowable<T> source, Publisher<U> other2) {
        super(source);
        this.other = other2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> child) {
        SkipUntilMainSubscriber<T> parent = new SkipUntilMainSubscriber<>(child);
        child.onSubscribe(parent);
        this.other.subscribe(parent.other);
        this.source.subscribe((FlowableSubscriber<? super T>) parent);
    }
}

package p005io.reactivex.internal.operators.flowable;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.annotations.Nullable;
import p005io.reactivex.internal.fuseable.QueueSubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableIgnoreElements */
public final class FlowableIgnoreElements<T> extends AbstractFlowableWithUpstream<T, T> {

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableIgnoreElements$IgnoreElementsSubscriber */
    static final class IgnoreElementsSubscriber<T> implements FlowableSubscriber<T>, QueueSubscription<T> {
        final Subscriber<? super T> actual;

        /* renamed from: s */
        Subscription f168s;

        IgnoreElementsSubscriber(Subscriber<? super T> actual2) {
            this.actual = actual2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f168s, s)) {
                this.f168s = s;
                this.actual.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public boolean offer(T t) {
            throw new UnsupportedOperationException("Should not be called!");
        }

        public boolean offer(T t, T t2) {
            throw new UnsupportedOperationException("Should not be called!");
        }

        @Nullable
        public T poll() {
            return null;
        }

        public boolean isEmpty() {
            return true;
        }

        public void clear() {
        }

        public void request(long n) {
        }

        public void cancel() {
            this.f168s.cancel();
        }

        public int requestFusion(int mode) {
            return mode & 2;
        }
    }

    public FlowableIgnoreElements(Flowable<T> source) {
        super(source);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> t) {
        this.source.subscribe((FlowableSubscriber<? super T>) new IgnoreElementsSubscriber<Object>(t));
    }
}

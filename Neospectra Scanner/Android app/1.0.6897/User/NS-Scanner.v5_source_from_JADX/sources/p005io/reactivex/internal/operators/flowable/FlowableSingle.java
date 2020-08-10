package p005io.reactivex.internal.operators.flowable;

import java.util.NoSuchElementException;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableSingle */
public final class FlowableSingle<T> extends AbstractFlowableWithUpstream<T, T> {
    final T defaultValue;
    final boolean failOnEmpty;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableSingle$SingleElementSubscriber */
    static final class SingleElementSubscriber<T> extends DeferredScalarSubscription<T> implements FlowableSubscriber<T> {
        private static final long serialVersionUID = -5526049321428043809L;
        final T defaultValue;
        boolean done;
        final boolean failOnEmpty;

        /* renamed from: s */
        Subscription f200s;

        SingleElementSubscriber(Subscriber<? super T> actual, T defaultValue2, boolean failOnEmpty2) {
            super(actual);
            this.defaultValue = defaultValue2;
            this.failOnEmpty = failOnEmpty2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f200s, s)) {
                this.f200s = s;
                this.actual.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                if (this.value != null) {
                    this.done = true;
                    this.f200s.cancel();
                    this.actual.onError(new IllegalArgumentException("Sequence contains more than one element!"));
                    return;
                }
                this.value = t;
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.actual.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                T v = this.value;
                this.value = null;
                if (v == null) {
                    v = this.defaultValue;
                }
                if (v != null) {
                    complete(v);
                } else if (this.failOnEmpty) {
                    this.actual.onError(new NoSuchElementException());
                } else {
                    this.actual.onComplete();
                }
            }
        }

        public void cancel() {
            super.cancel();
            this.f200s.cancel();
        }
    }

    public FlowableSingle(Flowable<T> source, T defaultValue2, boolean failOnEmpty2) {
        super(source);
        this.defaultValue = defaultValue2;
        this.failOnEmpty = failOnEmpty2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.source.subscribe((FlowableSubscriber<? super T>) new SingleElementSubscriber<Object>(s, this.defaultValue, this.failOnEmpty));
    }
}

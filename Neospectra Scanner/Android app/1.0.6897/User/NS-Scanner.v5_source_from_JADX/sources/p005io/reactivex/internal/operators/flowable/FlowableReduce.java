package p005io.reactivex.internal.operators.flowable;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.BiFunction;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableReduce */
public final class FlowableReduce<T> extends AbstractFlowableWithUpstream<T, T> {
    final BiFunction<T, T, T> reducer;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableReduce$ReduceSubscriber */
    static final class ReduceSubscriber<T> extends DeferredScalarSubscription<T> implements FlowableSubscriber<T> {
        private static final long serialVersionUID = -4663883003264602070L;
        final BiFunction<T, T, T> reducer;

        /* renamed from: s */
        Subscription f183s;

        ReduceSubscriber(Subscriber<? super T> actual, BiFunction<T, T, T> reducer2) {
            super(actual);
            this.reducer = reducer2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f183s, s)) {
                this.f183s = s;
                this.actual.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (this.f183s != SubscriptionHelper.CANCELLED) {
                T v = this.value;
                if (v == null) {
                    this.value = t;
                } else {
                    try {
                        this.value = ObjectHelper.requireNonNull(this.reducer.apply(v, t), "The reducer returned a null value");
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        this.f183s.cancel();
                        onError(ex);
                    }
                }
            }
        }

        public void onError(Throwable t) {
            if (this.f183s == SubscriptionHelper.CANCELLED) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.f183s = SubscriptionHelper.CANCELLED;
            this.actual.onError(t);
        }

        public void onComplete() {
            if (this.f183s != SubscriptionHelper.CANCELLED) {
                this.f183s = SubscriptionHelper.CANCELLED;
                T v = this.value;
                if (v != null) {
                    complete(v);
                } else {
                    this.actual.onComplete();
                }
            }
        }

        public void cancel() {
            super.cancel();
            this.f183s.cancel();
            this.f183s = SubscriptionHelper.CANCELLED;
        }
    }

    public FlowableReduce(Flowable<T> source, BiFunction<T, T, T> reducer2) {
        super(source);
        this.reducer = reducer2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.source.subscribe((FlowableSubscriber<? super T>) new ReduceSubscriber<Object>(s, this.reducer));
    }
}

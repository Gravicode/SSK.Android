package p005io.reactivex.internal.operators.flowable;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.BiFunction;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableScan */
public final class FlowableScan<T> extends AbstractFlowableWithUpstream<T, T> {
    final BiFunction<T, T, T> accumulator;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableScan$ScanSubscriber */
    static final class ScanSubscriber<T> implements FlowableSubscriber<T>, Subscription {
        final BiFunction<T, T, T> accumulator;
        final Subscriber<? super T> actual;
        boolean done;

        /* renamed from: s */
        Subscription f194s;
        T value;

        ScanSubscriber(Subscriber<? super T> actual2, BiFunction<T, T, T> accumulator2) {
            this.actual = actual2;
            this.accumulator = accumulator2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f194s, s)) {
                this.f194s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                Subscriber<? super T> a = this.actual;
                T v = this.value;
                if (v == null) {
                    this.value = t;
                    a.onNext(t);
                } else {
                    try {
                        T u = ObjectHelper.requireNonNull(this.accumulator.apply(v, t), "The value returned by the accumulator is null");
                        this.value = u;
                        a.onNext(u);
                    } catch (Throwable e) {
                        Exceptions.throwIfFatal(e);
                        this.f194s.cancel();
                        onError(e);
                    }
                }
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
                this.actual.onComplete();
            }
        }

        public void request(long n) {
            this.f194s.request(n);
        }

        public void cancel() {
            this.f194s.cancel();
        }
    }

    public FlowableScan(Flowable<T> source, BiFunction<T, T, T> accumulator2) {
        super(source);
        this.accumulator = accumulator2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.source.subscribe((FlowableSubscriber<? super T>) new ScanSubscriber<Object>(s, this.accumulator));
    }
}

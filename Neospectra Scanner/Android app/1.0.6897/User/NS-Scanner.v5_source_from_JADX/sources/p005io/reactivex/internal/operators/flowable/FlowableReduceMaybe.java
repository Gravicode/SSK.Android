package p005io.reactivex.internal.operators.flowable;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.Maybe;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.BiFunction;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.fuseable.FuseToFlowable;
import p005io.reactivex.internal.fuseable.HasUpstreamPublisher;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableReduceMaybe */
public final class FlowableReduceMaybe<T> extends Maybe<T> implements HasUpstreamPublisher<T>, FuseToFlowable<T> {
    final BiFunction<T, T, T> reducer;
    final Flowable<T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableReduceMaybe$ReduceSubscriber */
    static final class ReduceSubscriber<T> implements FlowableSubscriber<T>, Disposable {
        final MaybeObserver<? super T> actual;
        boolean done;
        final BiFunction<T, T, T> reducer;

        /* renamed from: s */
        Subscription f184s;
        T value;

        ReduceSubscriber(MaybeObserver<? super T> actual2, BiFunction<T, T, T> reducer2) {
            this.actual = actual2;
            this.reducer = reducer2;
        }

        public void dispose() {
            this.f184s.cancel();
            this.done = true;
        }

        public boolean isDisposed() {
            return this.done;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f184s, s)) {
                this.f184s = s;
                this.actual.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                T v = this.value;
                if (v == null) {
                    this.value = t;
                } else {
                    try {
                        this.value = ObjectHelper.requireNonNull(this.reducer.apply(v, t), "The reducer returned a null value");
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        this.f184s.cancel();
                        onError(ex);
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
                T v = this.value;
                if (v != null) {
                    this.actual.onSuccess(v);
                } else {
                    this.actual.onComplete();
                }
            }
        }
    }

    public FlowableReduceMaybe(Flowable<T> source2, BiFunction<T, T, T> reducer2) {
        this.source = source2;
        this.reducer = reducer2;
    }

    public Publisher<T> source() {
        return this.source;
    }

    public Flowable<T> fuseToFlowable() {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableReduce<T>(this.source, this.reducer));
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> observer) {
        this.source.subscribe((FlowableSubscriber<? super T>) new ReduceSubscriber<Object>(observer, this.reducer));
    }
}

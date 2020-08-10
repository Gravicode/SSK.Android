package p005io.reactivex.internal.operators.flowable;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.BiFunction;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableReduceSeedSingle */
public final class FlowableReduceSeedSingle<T, R> extends Single<R> {
    final BiFunction<R, ? super T, R> reducer;
    final R seed;
    final Publisher<T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableReduceSeedSingle$ReduceSeedObserver */
    static final class ReduceSeedObserver<T, R> implements FlowableSubscriber<T>, Disposable {
        final SingleObserver<? super R> actual;
        final BiFunction<R, ? super T, R> reducer;

        /* renamed from: s */
        Subscription f185s;
        R value;

        ReduceSeedObserver(SingleObserver<? super R> actual2, BiFunction<R, ? super T, R> reducer2, R value2) {
            this.actual = actual2;
            this.value = value2;
            this.reducer = reducer2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f185s, s)) {
                this.f185s = s;
                this.actual.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T value2) {
            R v = this.value;
            if (v != null) {
                try {
                    this.value = ObjectHelper.requireNonNull(this.reducer.apply(v, value2), "The reducer returned a null value");
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    this.f185s.cancel();
                    onError(ex);
                }
            }
        }

        public void onError(Throwable e) {
            if (this.value != null) {
                this.value = null;
                this.f185s = SubscriptionHelper.CANCELLED;
                this.actual.onError(e);
                return;
            }
            RxJavaPlugins.onError(e);
        }

        public void onComplete() {
            R v = this.value;
            if (v != null) {
                this.value = null;
                this.f185s = SubscriptionHelper.CANCELLED;
                this.actual.onSuccess(v);
            }
        }

        public void dispose() {
            this.f185s.cancel();
            this.f185s = SubscriptionHelper.CANCELLED;
        }

        public boolean isDisposed() {
            return this.f185s == SubscriptionHelper.CANCELLED;
        }
    }

    public FlowableReduceSeedSingle(Publisher<T> source2, R seed2, BiFunction<R, ? super T, R> reducer2) {
        this.source = source2;
        this.seed = seed2;
        this.reducer = reducer2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super R> observer) {
        this.source.subscribe(new ReduceSeedObserver(observer, this.reducer, this.seed));
    }
}

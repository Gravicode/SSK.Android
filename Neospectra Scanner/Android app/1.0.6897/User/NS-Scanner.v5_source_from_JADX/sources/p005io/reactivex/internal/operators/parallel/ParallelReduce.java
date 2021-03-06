package p005io.reactivex.internal.operators.parallel;

import java.util.concurrent.Callable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.BiFunction;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.subscribers.DeferredScalarSubscriber;
import p005io.reactivex.internal.subscriptions.EmptySubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.parallel.ParallelFlowable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.parallel.ParallelReduce */
public final class ParallelReduce<T, R> extends ParallelFlowable<R> {
    final Callable<R> initialSupplier;
    final BiFunction<R, ? super T, R> reducer;
    final ParallelFlowable<? extends T> source;

    /* renamed from: io.reactivex.internal.operators.parallel.ParallelReduce$ParallelReduceSubscriber */
    static final class ParallelReduceSubscriber<T, R> extends DeferredScalarSubscriber<T, R> {
        private static final long serialVersionUID = 8200530050639449080L;
        R accumulator;
        boolean done;
        final BiFunction<R, ? super T, R> reducer;

        ParallelReduceSubscriber(Subscriber<? super R> subscriber, R initialValue, BiFunction<R, ? super T, R> reducer2) {
            super(subscriber);
            this.accumulator = initialValue;
            this.reducer = reducer2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f433s, s)) {
                this.f433s = s;
                this.actual.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    this.accumulator = ObjectHelper.requireNonNull(this.reducer.apply(this.accumulator, t), "The reducer returned a null value");
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    cancel();
                    onError(ex);
                }
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.accumulator = null;
            this.actual.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                R a = this.accumulator;
                this.accumulator = null;
                complete(a);
            }
        }

        public void cancel() {
            super.cancel();
            this.f433s.cancel();
        }
    }

    public ParallelReduce(ParallelFlowable<? extends T> source2, Callable<R> initialSupplier2, BiFunction<R, ? super T, R> reducer2) {
        this.source = source2;
        this.initialSupplier = initialSupplier2;
        this.reducer = reducer2;
    }

    public void subscribe(Subscriber<? super R>[] subscribers) {
        if (validate(subscribers)) {
            int n = subscribers.length;
            Subscriber<T>[] parents = new Subscriber[n];
            int i = 0;
            while (i < n) {
                try {
                    parents[i] = new ParallelReduceSubscriber<>(subscribers[i], ObjectHelper.requireNonNull(this.initialSupplier.call(), "The initialSupplier returned a null value"), this.reducer);
                    i++;
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    reportError(subscribers, ex);
                    return;
                }
            }
            this.source.subscribe(parents);
        }
    }

    /* access modifiers changed from: 0000 */
    public void reportError(Subscriber<?>[] subscribers, Throwable ex) {
        for (Subscriber<?> s : subscribers) {
            EmptySubscription.error(ex, s);
        }
    }

    public int parallelism() {
        return this.source.parallelism();
    }
}

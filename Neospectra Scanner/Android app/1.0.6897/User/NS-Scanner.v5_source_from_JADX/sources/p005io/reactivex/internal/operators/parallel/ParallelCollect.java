package p005io.reactivex.internal.operators.parallel;

import java.util.concurrent.Callable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.BiConsumer;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.subscribers.DeferredScalarSubscriber;
import p005io.reactivex.internal.subscriptions.EmptySubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.parallel.ParallelFlowable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.parallel.ParallelCollect */
public final class ParallelCollect<T, C> extends ParallelFlowable<C> {
    final BiConsumer<? super C, ? super T> collector;
    final Callable<? extends C> initialCollection;
    final ParallelFlowable<? extends T> source;

    /* renamed from: io.reactivex.internal.operators.parallel.ParallelCollect$ParallelCollectSubscriber */
    static final class ParallelCollectSubscriber<T, C> extends DeferredScalarSubscriber<T, C> {
        private static final long serialVersionUID = -4767392946044436228L;
        C collection;
        final BiConsumer<? super C, ? super T> collector;
        boolean done;

        ParallelCollectSubscriber(Subscriber<? super C> subscriber, C initialValue, BiConsumer<? super C, ? super T> collector2) {
            super(subscriber);
            this.collection = initialValue;
            this.collector = collector2;
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
                    this.collector.accept(this.collection, t);
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
            this.collection = null;
            this.actual.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                C c = this.collection;
                this.collection = null;
                complete(c);
            }
        }

        public void cancel() {
            super.cancel();
            this.f433s.cancel();
        }
    }

    public ParallelCollect(ParallelFlowable<? extends T> source2, Callable<? extends C> initialCollection2, BiConsumer<? super C, ? super T> collector2) {
        this.source = source2;
        this.initialCollection = initialCollection2;
        this.collector = collector2;
    }

    public void subscribe(Subscriber<? super C>[] subscribers) {
        if (validate(subscribers)) {
            int n = subscribers.length;
            Subscriber<T>[] parents = new Subscriber[n];
            int i = 0;
            while (i < n) {
                try {
                    parents[i] = new ParallelCollectSubscriber<>(subscribers[i], ObjectHelper.requireNonNull(this.initialCollection.call(), "The initialSupplier returned a null value"), this.collector);
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
